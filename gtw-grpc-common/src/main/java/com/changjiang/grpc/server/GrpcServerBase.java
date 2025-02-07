package com.changjiang.grpc.server;

import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.changjiang.grpc.lib.GrpcServiceGrpc;
import io.grpc.stub.StreamObserver;

public abstract class GrpcServerBase extends GrpcServiceGrpc.GrpcServiceImplBase {
    @Override
    public void invoke(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) {
        // 调用子类实现的业务逻辑
        GrpcResponse response = handleRequest(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // 子类需要实现的业务逻辑
    protected abstract GrpcResponse handleRequest(GrpcRequest request);
}
