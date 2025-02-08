package com.changjiang.grpc.client;

import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrpcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(GrpcTemplate.class);
    
    private final GrpcClient grpcClient;

    @Autowired
    public GrpcTemplate(GrpcClient grpcClient) {
        this.grpcClient = grpcClient;
    }

    public GrpcResponse execute(String serviceId, String methodName, byte[] payload) {
        GrpcRequest request = buildRequest(serviceId, methodName, payload);
        return grpcClient.getBlockingStub().invoke(request);
    }

    public void executeServerStream(String serviceId, String methodName, byte[] payload, 
            StreamObserver<GrpcResponse> responseObserver) {
        GrpcRequest request = buildRequest(serviceId, methodName, payload);
        grpcClient.getAsyncStub().invokeServerStream(request, responseObserver);
    }

    public StreamObserver<GrpcRequest> executeClientStream(StreamObserver<GrpcResponse> responseObserver) {
        return grpcClient.getAsyncStub().invokeClientStream(responseObserver);
    }

    public StreamObserver<GrpcRequest> executeBidiStream(StreamObserver<GrpcResponse> responseObserver) {
        return grpcClient.getAsyncStub().invokeBidiStream(responseObserver);
    }

    public GrpcResponse execute(GrpcRequest request) {
        return grpcClient.getBlockingStub().invoke(request);
    }

    private GrpcRequest buildRequest(String serviceId, String methodName, byte[] payload) {
        return GrpcRequest.newBuilder()
                .setServiceId(serviceId)
                .setMethodName(methodName)
                .setPayload(ByteString.copyFrom(payload))
                .build();
    }
} 