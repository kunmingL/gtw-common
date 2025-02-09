package com.changjiang.grpc.factory;

import com.changjiang.grpc.client.GrpcClient;
import com.changjiang.grpc.client.GrpcTemplate;
import com.changjiang.grpc.config.GrpcRegistryConfig;
import com.changjiang.grpc.proxy.GrpcServiceProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GrpcServiceFactory implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(GrpcServiceFactory.class);

    private ApplicationContext applicationContext;
    private final Map<String, GrpcTemplate> templateCache = new ConcurrentHashMap<>();

    /**
     * 使用已加载的Class对象创建服务实例（新增方法）
     *
     * @param registerId 注册ID
     * @param loadedClass 已经加载的Class对象
     * @return 服务实例
     */
    public Object createServiceFromLoadedClass(String registerId, Class<?> loadedClass) {
        try {
            logger.debug("Creating service instance for registerId: {} with loaded class: {}",
                    registerId, loadedClass.getName());
            return createService(registerId, loadedClass);
        } catch (Exception e) {
            logger.error("Failed to create service instance from loaded class: " + loadedClass.getName(), e);
            throw new RuntimeException("Failed to create service instance", e);
        }
    }

    /**
     * 创建服务实例
     *
     * @param registerId 注册ID，用于从配置文件中获取服务地址
     * @param serviceClass 服务接口类
     * @param <T> 服务接口类型
     * @return 服务实例
     */
    public <T> T createService(String registerId, Class<T> serviceClass) {
        try {
            // 获取或创建 GrpcTemplate
            GrpcTemplate template = getOrCreateTemplate(registerId);

            // 使用代理工厂创建服务实例
            return GrpcServiceProxyFactory.createProxy(serviceClass, template, registerId);
        } catch (Exception e) {
            logger.error("Failed to create service instance for registerId: " + registerId, e);
            throw new RuntimeException("Failed to create service instance", e);
        }
    }

    /**
     * 根据类路径创建服务实例
     *
     * @param registerId 注册ID
     * @param classPath 类路径
     * @return 服务实例
     */
    @SuppressWarnings("unchecked")
    public Object createServiceByClassPath(String registerId, String classPath) {
        try {
            // 加载服务接口类
            Class<?> serviceClass = Class.forName(classPath);
            return createService(registerId, serviceClass);
        } catch (ClassNotFoundException e) {
            logger.error("Class not found: " + classPath, e);
            throw new RuntimeException("Class not found: " + classPath, e);
        }
    }

    /**
     * 获取或创建 GrpcTemplate
     */
    private GrpcTemplate getOrCreateTemplate(String registerId) {
        return templateCache.computeIfAbsent(registerId, key -> {
            GrpcRegistryConfig registryConfig = applicationContext.getBean(GrpcRegistryConfig.class);
            GrpcRegistryConfig.ServiceConfig serviceConfig = registryConfig.getServices().get(key);

            if (serviceConfig == null) {
                throw new RuntimeException("Service configuration not found for registerId: " + key);
            }

            if (!serviceConfig.isEnabled()) {
                throw new RuntimeException("Service is disabled for registerId: " + key);
            }

            GrpcClient client = new GrpcClient(serviceConfig.getHost(), serviceConfig.getPort());
            return new GrpcTemplate(client);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
