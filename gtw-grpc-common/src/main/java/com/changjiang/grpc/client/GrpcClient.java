package com.changjiang.grpc.client;

import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.changjiang.grpc.lib.GrpcServiceGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

public class GrpcClient {
    private static final Logger logger = LoggerFactory.getLogger(GrpcClient.class);
    
    private final ManagedChannel channel;
    private final GrpcServiceGrpc.GrpcServiceBlockingStub blockingStub;
    private final GrpcServiceGrpc.GrpcServiceStub asyncStub;

    public GrpcClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build());
    }

    public GrpcClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = GrpcServiceGrpc.newBlockingStub(channel);
        this.asyncStub = GrpcServiceGrpc.newStub(channel);
    }

    public GrpcResponse callService(String serviceId, String methodName, byte[] payload) {
        GrpcRequest request = GrpcRequest.newBuilder()
                .setServiceId(serviceId)
                .setMethodName(methodName)
                .setPayload(ByteString.copyFrom(payload))
                .build();
        return blockingStub.invoke(request);
    }

    public GrpcServiceGrpc.GrpcServiceBlockingStub getBlockingStub() {
        return blockingStub;
    }

    public GrpcServiceGrpc.GrpcServiceStub getAsyncStub() {
        return asyncStub;
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("Error shutting down gRPC channel", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
