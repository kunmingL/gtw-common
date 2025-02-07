package com.changjiang.grpc.client;

import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.changjiang.grpc.lib.GrpcServiceGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
    private final ManagedChannel channel;
    private final GrpcServiceGrpc.GrpcServiceBlockingStub blockingStub;

    public GrpcClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = GrpcServiceGrpc.newBlockingStub(channel);
    }

    public GrpcResponse callService(String serviceId, String methodName, byte[] payload) {
        GrpcRequest request = GrpcRequest.newBuilder()
                .setServiceId(serviceId)
                .setMethodName(methodName)
                .setPayload(ByteString.copyFrom(payload))
                .build();
        return blockingStub.invoke(request);
    }

    public void shutdown() {
        channel.shutdown();
    }
}
