package com.changjiang.grpc.lib;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: grpc_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GrpcServiceGrpc {

  private GrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "grpc.GrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest,
      com.changjiang.grpc.lib.GrpcResponse> getInvokeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "invoke",
      requestType = com.changjiang.grpc.lib.GrpcRequest.class,
      responseType = com.changjiang.grpc.lib.GrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest,
      com.changjiang.grpc.lib.GrpcResponse> getInvokeMethod() {
    io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest, com.changjiang.grpc.lib.GrpcResponse> getInvokeMethod;
    if ((getInvokeMethod = GrpcServiceGrpc.getInvokeMethod) == null) {
      synchronized (GrpcServiceGrpc.class) {
        if ((getInvokeMethod = GrpcServiceGrpc.getInvokeMethod) == null) {
          GrpcServiceGrpc.getInvokeMethod = getInvokeMethod =
              io.grpc.MethodDescriptor.<com.changjiang.grpc.lib.GrpcRequest, com.changjiang.grpc.lib.GrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "invoke"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.changjiang.grpc.lib.GrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.changjiang.grpc.lib.GrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcServiceMethodDescriptorSupplier("invoke"))
              .build();
        }
      }
    }
    return getInvokeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest,
      com.changjiang.grpc.lib.GrpcResponse> getInvokeServerStreamMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "invokeServerStream",
      requestType = com.changjiang.grpc.lib.GrpcRequest.class,
      responseType = com.changjiang.grpc.lib.GrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest,
      com.changjiang.grpc.lib.GrpcResponse> getInvokeServerStreamMethod() {
    io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest, com.changjiang.grpc.lib.GrpcResponse> getInvokeServerStreamMethod;
    if ((getInvokeServerStreamMethod = GrpcServiceGrpc.getInvokeServerStreamMethod) == null) {
      synchronized (GrpcServiceGrpc.class) {
        if ((getInvokeServerStreamMethod = GrpcServiceGrpc.getInvokeServerStreamMethod) == null) {
          GrpcServiceGrpc.getInvokeServerStreamMethod = getInvokeServerStreamMethod =
              io.grpc.MethodDescriptor.<com.changjiang.grpc.lib.GrpcRequest, com.changjiang.grpc.lib.GrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "invokeServerStream"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.changjiang.grpc.lib.GrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.changjiang.grpc.lib.GrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcServiceMethodDescriptorSupplier("invokeServerStream"))
              .build();
        }
      }
    }
    return getInvokeServerStreamMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest,
      com.changjiang.grpc.lib.GrpcResponse> getInvokeClientStreamMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "invokeClientStream",
      requestType = com.changjiang.grpc.lib.GrpcRequest.class,
      responseType = com.changjiang.grpc.lib.GrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest,
      com.changjiang.grpc.lib.GrpcResponse> getInvokeClientStreamMethod() {
    io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest, com.changjiang.grpc.lib.GrpcResponse> getInvokeClientStreamMethod;
    if ((getInvokeClientStreamMethod = GrpcServiceGrpc.getInvokeClientStreamMethod) == null) {
      synchronized (GrpcServiceGrpc.class) {
        if ((getInvokeClientStreamMethod = GrpcServiceGrpc.getInvokeClientStreamMethod) == null) {
          GrpcServiceGrpc.getInvokeClientStreamMethod = getInvokeClientStreamMethod =
              io.grpc.MethodDescriptor.<com.changjiang.grpc.lib.GrpcRequest, com.changjiang.grpc.lib.GrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "invokeClientStream"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.changjiang.grpc.lib.GrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.changjiang.grpc.lib.GrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcServiceMethodDescriptorSupplier("invokeClientStream"))
              .build();
        }
      }
    }
    return getInvokeClientStreamMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest,
      com.changjiang.grpc.lib.GrpcResponse> getInvokeBidiStreamMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "invokeBidiStream",
      requestType = com.changjiang.grpc.lib.GrpcRequest.class,
      responseType = com.changjiang.grpc.lib.GrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest,
      com.changjiang.grpc.lib.GrpcResponse> getInvokeBidiStreamMethod() {
    io.grpc.MethodDescriptor<com.changjiang.grpc.lib.GrpcRequest, com.changjiang.grpc.lib.GrpcResponse> getInvokeBidiStreamMethod;
    if ((getInvokeBidiStreamMethod = GrpcServiceGrpc.getInvokeBidiStreamMethod) == null) {
      synchronized (GrpcServiceGrpc.class) {
        if ((getInvokeBidiStreamMethod = GrpcServiceGrpc.getInvokeBidiStreamMethod) == null) {
          GrpcServiceGrpc.getInvokeBidiStreamMethod = getInvokeBidiStreamMethod =
              io.grpc.MethodDescriptor.<com.changjiang.grpc.lib.GrpcRequest, com.changjiang.grpc.lib.GrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "invokeBidiStream"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.changjiang.grpc.lib.GrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.changjiang.grpc.lib.GrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GrpcServiceMethodDescriptorSupplier("invokeBidiStream"))
              .build();
        }
      }
    }
    return getInvokeBidiStreamMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcServiceStub>() {
        @java.lang.Override
        public GrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcServiceStub(channel, callOptions);
        }
      };
    return GrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcServiceBlockingStub>() {
        @java.lang.Override
        public GrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return GrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GrpcServiceFutureStub>() {
        @java.lang.Override
        public GrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GrpcServiceFutureStub(channel, callOptions);
        }
      };
    return GrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void invoke(com.changjiang.grpc.lib.GrpcRequest request,
        io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInvokeMethod(), responseObserver);
    }

    /**
     */
    default void invokeServerStream(com.changjiang.grpc.lib.GrpcRequest request,
        io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInvokeServerStreamMethod(), responseObserver);
    }

    /**
     */
    default io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcRequest> invokeClientStream(
        io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getInvokeClientStreamMethod(), responseObserver);
    }

    /**
     */
    default io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcRequest> invokeBidiStream(
        io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getInvokeBidiStreamMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GrpcService.
   */
  public static abstract class GrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GrpcService.
   */
  public static final class GrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<GrpcServiceStub> {
    private GrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void invoke(com.changjiang.grpc.lib.GrpcRequest request,
        io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInvokeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void invokeServerStream(com.changjiang.grpc.lib.GrpcRequest request,
        io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getInvokeServerStreamMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcRequest> invokeClientStream(
        io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getInvokeClientStreamMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcRequest> invokeBidiStream(
        io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getInvokeBidiStreamMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GrpcService.
   */
  public static final class GrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GrpcServiceBlockingStub> {
    private GrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.changjiang.grpc.lib.GrpcResponse invoke(com.changjiang.grpc.lib.GrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInvokeMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.changjiang.grpc.lib.GrpcResponse> invokeServerStream(
        com.changjiang.grpc.lib.GrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getInvokeServerStreamMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GrpcService.
   */
  public static final class GrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<GrpcServiceFutureStub> {
    private GrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.changjiang.grpc.lib.GrpcResponse> invoke(
        com.changjiang.grpc.lib.GrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInvokeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INVOKE = 0;
  private static final int METHODID_INVOKE_SERVER_STREAM = 1;
  private static final int METHODID_INVOKE_CLIENT_STREAM = 2;
  private static final int METHODID_INVOKE_BIDI_STREAM = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INVOKE:
          serviceImpl.invoke((com.changjiang.grpc.lib.GrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse>) responseObserver);
          break;
        case METHODID_INVOKE_SERVER_STREAM:
          serviceImpl.invokeServerStream((com.changjiang.grpc.lib.GrpcRequest) request,
              (io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INVOKE_CLIENT_STREAM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.invokeClientStream(
              (io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse>) responseObserver);
        case METHODID_INVOKE_BIDI_STREAM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.invokeBidiStream(
              (io.grpc.stub.StreamObserver<com.changjiang.grpc.lib.GrpcResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getInvokeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.changjiang.grpc.lib.GrpcRequest,
              com.changjiang.grpc.lib.GrpcResponse>(
                service, METHODID_INVOKE)))
        .addMethod(
          getInvokeServerStreamMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.changjiang.grpc.lib.GrpcRequest,
              com.changjiang.grpc.lib.GrpcResponse>(
                service, METHODID_INVOKE_SERVER_STREAM)))
        .addMethod(
          getInvokeClientStreamMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              com.changjiang.grpc.lib.GrpcRequest,
              com.changjiang.grpc.lib.GrpcResponse>(
                service, METHODID_INVOKE_CLIENT_STREAM)))
        .addMethod(
          getInvokeBidiStreamMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.changjiang.grpc.lib.GrpcRequest,
              com.changjiang.grpc.lib.GrpcResponse>(
                service, METHODID_INVOKE_BIDI_STREAM)))
        .build();
  }

  private static abstract class GrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.changjiang.grpc.lib.GrpcServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GrpcService");
    }
  }

  private static final class GrpcServiceFileDescriptorSupplier
      extends GrpcServiceBaseDescriptorSupplier {
    GrpcServiceFileDescriptorSupplier() {}
  }

  private static final class GrpcServiceMethodDescriptorSupplier
      extends GrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GrpcServiceFileDescriptorSupplier())
              .addMethod(getInvokeMethod())
              .addMethod(getInvokeServerStreamMethod())
              .addMethod(getInvokeClientStreamMethod())
              .addMethod(getInvokeBidiStreamMethod())
              .build();
        }
      }
    }
    return result;
  }
}
