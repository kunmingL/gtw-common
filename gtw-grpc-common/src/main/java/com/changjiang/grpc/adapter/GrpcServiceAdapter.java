package com.changjiang.grpc.adapter;

import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.changjiang.grpc.server.AbstractGrpcService;
import com.changjiang.grpc.util.NpcsSerializerUtil;
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
            // 如果只有一个参数，直接反序列化
            if (paramTypes.length == 1) {
                Type genericType = method.getGenericParameterTypes()[0];
                TypeReference<?> typeRef = createTypeReference(genericType);
                args[0] = SerializationUtil.deserialize(payload, typeRef);
            } else {
                // 多个参数时，先反序列化为对象数组
                Object[] rawArgs = SerializationUtil.deserialize(payload, 
                    new TypeReference<Object[]>() {});
                
                // 转换每个参数到正确的类型
                for (int i = 0; i < paramTypes.length && i < rawArgs.length; i++) {
                    Type genericType = method.getGenericParameterTypes()[i];
                    args[i] = SerializationUtil.convertValue(rawArgs[i], paramTypes[i]);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to deserialize arguments", e);
            throw new RuntimeException("Failed to deserialize arguments", e);
        }

        return args;
    }

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