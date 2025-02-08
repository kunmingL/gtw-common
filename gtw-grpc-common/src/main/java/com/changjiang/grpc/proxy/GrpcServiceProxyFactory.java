package com.changjiang.grpc.proxy;

import com.changjiang.grpc.client.GrpcTemplate;
import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.changjiang.grpc.util.SerializationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
                .setPayload(SerializationUtil.serialize(args))
                .build();

            // 发送请求并获取响应
            GrpcResponse response = grpcTemplate.execute(request);

            // 处理响应
            if (response.getCode() != 200) {
                throw new RuntimeException(response.getMessage());
            }

            // 反序列化响应结果
            return SerializationUtil.deserialize(
                response.getPayload().toByteArray(), 
                method.getReturnType()
            );
        }
    }
} 