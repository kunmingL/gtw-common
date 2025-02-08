# GTW gRPC Common SDK 3.0

GTW gRPC Common SDK 是一个基于 gRPC 的微服务通信框架，提供了简单易用的服务端和客户端实现。3.0 版本引入了更加便捷的服务调用方式，使得服务间通信更加透明和易用。

## 3.0 版本新特性

- **接口透明调用**：服务消费方可以直接通过接口调用远程服务，无需关心 gRPC 实现细节
- **自动参数序列化**：支持多参数方法调用，自动处理参数的序列化和反序列化
- **类型安全**：增强的类型转换支持，确保方法调用的类型安全
- **配置简化**：通过注解和配置文件即可完成服务注册和发现
- **无感知集成**：服务提供方和消费方无需感知 gRPC 的存在

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.changjiang</groupId>
    <artifactId>gtw-grpc-common</artifactId>
    <version>3.0.0</version>
</dependency>
```

### 2. 服务提供方实现

1. 定义服务接口：

```java
public interface UserService {
    User getUserById(Long id);
    List<User> getUsersByName(String name, Integer age);
    void updateUser(User user);
}
```

2. 实现服务接口：

```java
@GrpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(Long id) {
        // 实现业务逻辑
        return userRepository.findById(id);
    }

    @Override
    public List<User> getUsersByName(String name, Integer age) {
        // 支持多参数方法
        return userRepository.findByNameAndAge(name, age);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
```

### 3. 服务消费方使用

1. 配置服务地址：

```yaml
grpc:
  registry:
    services:
      userService:  # 服务名称
        host: user-service.example.com
        port: 9090
        enabled: true
```

2. 注入并使用服务：

```java
@Service
public class OrderService {
    @GrpcReference(register = "userService")
    private UserService userService;  // 直接注入接口

    public void processOrder(Long userId) {
        // 直接调用接口方法，就像调用本地服务一样
        User user = userService.getUserById(userId);
        
        // 支持多参数调用
        List<User> users = userService.getUsersByName("张三", 20);
        
        // 支持复杂对象传输
        user.setStatus("PROCESSED");
        userService.updateUser(user);
    }
}
```

## 配置说明

### 服务端配置

```yaml
grpc:
  server:
    enabled: true
    port: 9090
    host: 0.0.0.0
    maxConcurrentCallsPerConnection: 100
    keepAliveTimeInSeconds: 60
```

### 客户端配置

```yaml
grpc:
  registry:
    services:
      serviceA:
        host: service-a.example.com
        port: 9090
      serviceB:
        host: service-b.example.com
        port: 9091
```

## 主要改进

相比 2.0 版本，3.0 版本主要改进：

1. **接口调用方式**：
   - 2.0：需要手动构建 GrpcRequest 和处理 GrpcResponse
   - 3.0：直接使用接口方法，框架自动处理请求和响应

2. **参数处理**：
   - 2.0：仅支持单一参数的序列化
   - 3.0：支持多参数方法调用，自动处理参数序列化

3. **类型转换**：
   - 2.0：基础的类型转换支持
   - 3.0：增强的类型转换，支持复杂对象和集合类型

4. **错误处理**：
   - 2.0：统一的错误处理
   - 3.0：更细粒度的错误处理，保留异常堆栈信息

## 最佳实践

1. **接口设计**：
   - 保持接口简单明确
   - 使用 DTO 对象进行数据传输
   - 避免过大的数据传输

2. **性能优化**：
   - 合理设置连接池大小
   - 使用适当的超时设置
   - 考虑使用流式调用处理大量数据

3. **错误处理**：
   - 实现全局异常处理
   - 使用有意义的错误码和消息
   - 记录详细的错误日志

## 版本历史

### 3.0.0
- 引入接口透明调用
- 支持多参数方法调用
- 增强的类型转换支持
- 改进的错误处理机制

### 2.0.0
- 增强的性能优化和安全性
- 新增流式调用支持
- 改进的错误处理机制
- 支持 Spring Boot 2.5+

### 1.0.0
- 初始版本
- 基础的 gRPC 通信支持
- Spring Boot 自动配置
- 流式调用支持

## 许可证

本项目采用 [Apache License 2.0](LICENSE) 许可证。