# GTW Common SDK

GTW Common SDK 是一个用于微服务间通信的公共库，提供了基于gRPC的通信能力。

## 模块说明

- gtw-base: 基础功能模块
- gtw-grpc-common: gRPC通信模块

## 快速开始

### 1. 添加依赖
```xml
<dependency>
<groupId>com.changjiang</groupId>
<artifactId>gtw-grpc-common</artifactId>
<version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 2. 配置文件

在application.yml中添加gRPC服务器配置：
```yaml
grpc:
  server:
    enabled: true
    port: 9090
    host: 0.0.0.0
    maxConcurrentCallsPerConnection: 100
    keepAliveTimeInSeconds: 60
```

### 3. 服务端使用示例
```java
@Service
public class YourGrpcService extends GrpcServiceGrpc.GrpcServiceImplBase {
@Override
public void invoke(GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) {
// 处理请求逻辑
GrpcResponse response = GrpcResponse.newBuilder()
.setCode(200)
.setMessage("Success")
.setPayload(yourProcessedData)
.build();
responseObserver.onNext(response);
responseObserver.onCompleted();
}
}
```
### 4. 客户端使用示例
```java
@Service
public class GrpcClientService {
private final ManagedChannel channel;
private final GrpcServiceGrpc.GrpcServiceBlockingStub blockingStub;
public GrpcClientService(@Value("${grpc.server.host}") String host,
@Value("${grpc.server.port}") int port) {
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
}
```

## 注意事项

1. 确保服务端和客户端使用相同版本的SDK
2. 建议在生产环境中配置适当的超时时间和重试策略
3. 考虑添加适当的监控和日志记录

## 版本历史

- 0.0.1-SNAPSHOT: 初始版本，提供基础的gRPC通信能力

