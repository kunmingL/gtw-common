package com.changjiang.grpc.processor;

import com.changjiang.grpc.annotation.GrpcService;
import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.changjiang.grpc.server.AbstractGrpcService;
import com.changjiang.grpc.util.SerializationUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import io.grpc.stub.StreamObserver;

import java.lang.reflect.Method;

@Component
public class GrpcServiceProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(GrpcService.class)) {
            return createServiceProxy(bean);
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
                        args[0] = SerializationUtil.deserialize(payload, paramTypes[0]);
                    } else {
                        // 如果有多个参数，需要先解析参数数组
                        Object[] params = SerializationUtil.deserialize(payload, Object[].class);
                        for (int i = 0; i < paramTypes.length && i < params.length; i++) {
                            if (params[i] != null) {
                                // 将每个参数转换为正确的类型
                                args[i] = SerializationUtil.convertValue(params[i], paramTypes[i]);
                            }
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