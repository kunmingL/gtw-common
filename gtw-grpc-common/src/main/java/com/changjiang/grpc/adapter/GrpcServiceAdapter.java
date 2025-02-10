package com.changjiang.grpc.adapter;

import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.changjiang.grpc.server.AbstractGrpcService;
import com.changjiang.grpc.util.SerializationUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;


public class GrpcServiceAdapter extends AbstractGrpcService {
    private static final Logger logger = LoggerFactory.getLogger(GrpcServiceAdapter.class);
    
    private final Object serviceImpl;
    private final String serviceId;

    public GrpcServiceAdapter(Object serviceImpl, String serviceId) {
        this.serviceImpl = serviceImpl;
        this.serviceId = serviceId;
    }

    @Override
    protected GrpcResponse handleRequest(GrpcRequest request) throws Exception {
        try {
            // 验证服务ID
            if (!serviceId.equals(request.getServiceId())) {
                throw new IllegalArgumentException("Service ID mismatch");
            }

            // 查找对应的方法
            Method method = findMethod(serviceImpl.getClass(), request.getMethodName());
            
            // 处理参数
            Object[] args = deserializeArguments(request, method);
            
            // 调用实际的服务方法
            Object result = method.invoke(serviceImpl, args);
            
            // 构建响应
            return GrpcResponse.newBuilder()
                    .setCode(200)
                    .setPayload(SerializationUtil.serialize(result))
                    .build();
                    
        } catch (Exception e) {
            logger.error("Error handling request", e);
            return GrpcResponse.newBuilder()
                    .setCode(500)
                    .setMessage(e.getMessage())
                    .build();
        }
    }

    private Object[] deserializeArguments(GrpcRequest request, Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 0) {
            return new Object[0];
        }

        byte[] payload = request.getPayload().toByteArray();
        Object[] args = new Object[paramTypes.length];

        try {
            if (paramTypes.length == 1) {
                // 获取目标方法的第一个参数的泛型类型
                Type genericType = method.getGenericParameterTypes()[0];
                // 动态构造 TypeReference
                TypeReference<?> typeRef = createTypeReference(genericType);
                // 调用 SerializationUtil.deserialize 方法
                args[0] = SerializationUtil.deserialize(payload, typeRef);
            } else {
                for (int i = 0; i < paramTypes.length; i++) {
                    Type genericType = method.getGenericParameterTypes()[i];
                    TypeReference<?> typeRef = createTypeReference(genericType);
                    args[i] = SerializationUtil.deserialize(payload, typeRef);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to deserialize arguments", e);
            throw new RuntimeException("Failed to deserialize arguments", e);
        }

        return args;
    }
    // 辅助方法：动态构造 TypeReference
    private static TypeReference<?> createTypeReference(Type type) {
        return new TypeReference<Object>() {
            @Override
            public Type getType() {
                return type;
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