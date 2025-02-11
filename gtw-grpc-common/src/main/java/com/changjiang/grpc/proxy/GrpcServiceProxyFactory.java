package com.changjiang.grpc.proxy;

import com.changjiang.grpc.client.GrpcTemplate;
import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.changjiang.grpc.util.SerializationUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

public class GrpcServiceProxyFactory {
    private static final Logger logger = LoggerFactory.getLogger(GrpcServiceProxyFactory.class);

    public static <T> T createProxy(Class<T> interfaceClass, GrpcTemplate grpcTemplate, String serviceId) {
        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class[]{interfaceClass},
            new GrpcInvocationHandler(grpcTemplate, serviceId)
        );
    }

    private static class GrpcInvocationHandler implements InvocationHandler {
        private final GrpcTemplate grpcTemplate;
        private final String serviceId;

        public GrpcInvocationHandler(GrpcTemplate grpcTemplate, String serviceId) {
            this.grpcTemplate = grpcTemplate;
            this.serviceId = serviceId;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }

            // 构建gRPC请求
            GrpcRequest request = GrpcRequest.newBuilder()
                .setServiceId(serviceId)
                .setMethodName(method.getName())
                .setPayload(serializeMethodArgs(method, args))
                .build();

            // 发送请求并获取响应
            GrpcResponse response = grpcTemplate.execute(request);

            // 处理响应
            if (response.getCode() != 200) {
                throw new RuntimeException(response.getMessage());
            }

            // 反序列化响应结果
            Type returnType = method.getGenericReturnType();
            TypeReference<?> typeRef = createTypeReference(returnType);
            return SerializationUtil.deserialize(response.getPayload().toByteArray(), typeRef);
        }

        private ByteString serializeMethodArgs(Method method, Object[] args) {
            try {
                if (args == null || args.length == 0) {
                    return ByteString.EMPTY;
                }
                
                // 如果只有一个参数，直接序列化该参数
                if (args.length == 1) {
                    return SerializationUtil.serialize(args[0]);
                }
                
                // 多个参数时，序列化为参数数组
                return SerializationUtil.serialize(args);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize method arguments", e);
            }
        }

        private static TypeReference<?> createTypeReference(Type type) {
            return new TypeReference<Object>() {
                @Override
                public Type getType() {
                    return type;
                }
            };
        }
    }

    public static <T> TypeReference<T> toTypeReference(Class<T> clazz) {
        return new TypeReference<T>() {
            @Override
            public Type getType() {
                return clazz;
            }
        };
    }
} 