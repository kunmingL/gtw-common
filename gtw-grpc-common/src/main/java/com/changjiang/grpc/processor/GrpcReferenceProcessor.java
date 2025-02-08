package com.changjiang.grpc.processor;

import com.changjiang.grpc.annotation.GrpcReference;
import com.changjiang.grpc.client.GrpcClient;
import com.changjiang.grpc.client.GrpcTemplate;
import com.changjiang.grpc.config.GrpcRegistryConfig;
import com.changjiang.grpc.proxy.GrpcServiceProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GrpcReferenceProcessor implements BeanPostProcessor, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(GrpcReferenceProcessor.class);
    
    private ApplicationContext applicationContext;
    private final Map<String, GrpcTemplate> templateCache = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        ReflectionUtils.doWithFields(clazz, field -> processField(bean, field));
        return bean;
    }

    private void processField(Object bean, Field field) {
        GrpcReference reference = field.getAnnotation(GrpcReference.class);
        if (reference != null) {
            String register = reference.register();
            GrpcTemplate template = getOrCreateTemplate(register);
            
            // 创建服务代理
            Object serviceProxy = GrpcServiceProxyFactory.createProxy(
                field.getType(),
                template,
                register
            );
            
            field.setAccessible(true);
            try {
                field.set(bean, serviceProxy);
            } catch (IllegalAccessException e) {
                logger.error("Failed to inject service proxy for register: " + register, e);
                throw new RuntimeException("Failed to inject service proxy", e);
            }
        }
    }

    private GrpcTemplate getOrCreateTemplate(String register) {
        return templateCache.computeIfAbsent(register, key -> {
            GrpcRegistryConfig registryConfig = applicationContext.getBean(GrpcRegistryConfig.class);
            GrpcRegistryConfig.ServiceConfig serviceConfig = registryConfig.getServices().get(key);
            
            if (serviceConfig == null) {
                throw new RuntimeException("Service configuration not found for register: " + key);
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