package com.changjiang.grpc.processor;

import com.changjiang.grpc.adapter.GrpcServiceAdapter;
import com.changjiang.grpc.annotation.GrpcService;
import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.changjiang.grpc.server.AbstractGrpcService;
import com.changjiang.grpc.util.SerializationUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import io.grpc.stub.StreamObserver;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

@Component
public class GrpcServiceProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(GrpcServiceProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(GrpcService.class)) {
            GrpcService annotation = bean.getClass().getAnnotation(GrpcService.class);
            String serviceId = annotation.value().isEmpty() ? beanName : annotation.value();
            
            // 创建适配器
            GrpcServiceAdapter adapter = new GrpcServiceAdapter(bean, serviceId);
            logger.info("Created gRPC service adapter for bean: {} with serviceId: {}", beanName, serviceId);
            
            // 返回适配器实例，它将替换原始的 bean
            return adapter;
        }
        return bean;
    }

    private Object createServiceProxy(Object serviceBean) {
        return new AbstractGrpcService() {
            @Override
            protected GrpcResponse handleRequest(GrpcRequest request) throws Exception {
                Method method = findMethod(serviceBean.getClass(), request.getMethodName());
                
                // 修改这里的反序列化逻辑
                Object[] args;
                if (method.getParameterCount() > 0) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    args = new Object[paramTypes.length];
                    byte[] payload = request.getPayload().toByteArray();
                    
                    // 如果只有一个参数，直接反序列化
                    if (paramTypes.length == 1) {
                        // 获取目标方法的第一个参数的泛型类型
                        Type genericType = method.getGenericParameterTypes()[0];
                        // 动态构造 TypeReference
                        TypeReference<?> typeRef = createTypeReference(genericType);
                        // 调用 SerializationUtil.deserialize 方法
                        args[0] = SerializationUtil.deserialize(payload, typeRef);
                    } else {
                        // 如果有多个参数，需要先解析参数数组
                        for (int i = 0; i < paramTypes.length; i++) {
                            Type genericType = method.getGenericParameterTypes()[i];
                            TypeReference<?> typeRef = createTypeReference(genericType);
                            args[i] = SerializationUtil.deserialize(payload, typeRef);
                        }
                    }
                } else {
                    args = new Object[0];
                }
                
                Object result = method.invoke(serviceBean, args);
                
                return GrpcResponse.newBuilder()
                    .setCode(200)
                    .setPayload(SerializationUtil.serialize(result))
                    .build();
            }

            private static TypeReference<?> createTypeReference(Type type) {
                return new TypeReference<Object>() {
                    @Override
                    public Type getType() {
                        return type;
                    }
                };
            }

            @Override
            protected void handleServerStream(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) throws Exception {
                // 默认实现，如果需要流式处理，子类可以重写此方法
                super.handleServerStream(request, responseObserver);
            }
        };
    }




    private Method findMethod(Class<?> serviceClass, String methodName) {
        for (Method method : serviceClass.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new RuntimeException("Method not found: " + methodName);
    }
} 