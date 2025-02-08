package com.changjiang.grpc.server;

import com.changjiang.grpc.lib.GrpcRequest;
import com.changjiang.grpc.lib.GrpcResponse;
import com.changjiang.grpc.lib.GrpcServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.changjiang.grpc.annotation.GrpcService;

@GrpcService
public abstract class AbstractGrpcService extends GrpcServiceGrpc.GrpcServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(AbstractGrpcService.class);
    
    @Override
    public void invoke(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) {
        try {
            logger.debug("Received request: serviceId={}, methodName={}", 
                request.getServiceId(), request.getMethodName());
            
            GrpcResponse response = handleRequest(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            logger.debug("Request completed: code={}", response.getCode());
        } catch (Exception e) {
            logger.error("Error processing request: " + e.getMessage(), e);
            handleError(e, responseObserver);
        }
    }

    @Override
    public void invokeServerStream(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) {
        try {
            handleServerStream(request, responseObserver);
        } catch (Exception e) {
            logger.error("Error in server stream: " + e.getMessage(), e);
            handleError(e, responseObserver);
        }
    }

    @Override
    public StreamObserver<GrpcRequest> invokeClientStream(StreamObserver<GrpcResponse> responseObserver) {
        return new StreamObserver<GrpcRequest>() {
            @Override
            public void onNext(GrpcRequest request) {
                try {
                    handleClientStream(request, responseObserver);
                } catch (Exception e) {
                    logger.error("Error in client stream: " + e.getMessage(), e);
                    handleError(e, responseObserver);
                }
            }

            @Override
            public void onError(Throwable t) {
                logger.error("Client stream error: " + t.getMessage(), t);
                handleError(new Exception(t), responseObserver);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<GrpcRequest> invokeBidiStream(StreamObserver<GrpcResponse> responseObserver) {
        return new StreamObserver<GrpcRequest>() {
            @Override
            public void onNext(GrpcRequest request) {
                try {
                    handleBidiStream(request, responseObserver);
                } catch (Exception e) {
                    logger.error("Error in bidi stream: " + e.getMessage(), e);
                    handleError(e, responseObserver);
                }
            }

            @Override
            public void onError(Throwable t) {
                logger.error("Bidi stream error: " + t.getMessage(), t);
                handleError(new Exception(t), responseObserver);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    // 子类需要实现的业务逻辑
    protected abstract GrpcResponse handleRequest(GrpcRequest request) throws Exception;

    // 可选实现的流式处理方法
    protected void handleServerStream(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) throws Exception {
        throw new UnsupportedOperationException("Server stream not implemented");
    }

    protected void handleClientStream(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) throws Exception {
        throw new UnsupportedOperationException("Client stream not implemented");
    }

    protected void handleBidiStream(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) throws Exception {
        throw new UnsupportedOperationException("Bidirectional stream not implemented");
    }

    protected void handleError(Exception e, StreamObserver<GrpcResponse> responseObserver) {
        GrpcResponse response = GrpcResponse.newBuilder()
                .setCode(500)
                .setMessage("Internal Server Error: " + e.getMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
} 