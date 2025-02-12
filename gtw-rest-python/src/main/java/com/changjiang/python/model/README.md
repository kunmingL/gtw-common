# Python REST Client 使用说明

本文档介绍了 Python REST Client 的使用方法，包括各种请求场景和示例代码。

## 1. 基础 JSON 请求

### 1.1 简单对象请求
适用于请求参数和返回值都是简单对象的场景。

```java
// 示例1：获取用户信息
UserRequest request = new UserRequest();
request.setUserId("12345");

User user = pythonRestClient.callPythonService(
    "http://python-service/api/user",
    request,
    User.class
);

// 示例2：处理计算结果
CalculateRequest calcRequest = new CalculateRequest();
calcRequest.setNumbers(Arrays.asList(1, 2, 3, 4, 5));

Integer result = pythonRestClient.callPythonService(
    "http://python-service/api/calculate",
    calcRequest,
    Integer.class
);
```

### 1.2 泛型对象请求
适用于返回值是泛型类型的场景，如 List、Map 等。

```java
// 示例1：获取用户列表
SearchRequest request = new SearchRequest();
request.setKeyword("张");

List<User> users = pythonRestClient.callPythonService(
    "http://python-service/api/users/search",
    request,
    new TypeReference<List<User>>() {}
);

// 示例2：获取复杂的嵌套数据结构
Map<String, List<OrderDetail>> orderMap = pythonRestClient.callPythonService(
    "http://python-service/api/orders/group",
    params,
    new TypeReference<Map<String, List<OrderDetail>>>() {}
);
```

## 2. 文件处理请求

### 2.1 上传文件
支持同时上传多个文件和其他参数。

```java
// 准备文件
Map<String, MultipartFile> files = new HashMap<>();
files.put("image", imageFile);
files.put("document", docFile);

// 准备其他参数
Map<String, Object> params = new HashMap<>();
params.put("userId", "12345");
params.put("description", "测试文件上传");

// 发送请求
UploadResult result = pythonRestClient.callPythonServiceWithFiles(
    "http://python-service/api/upload",
    files,
    params,
    UploadResult.class
);
```

### 2.2 下载文件
支持获取文件内容和相关元数据。

```java
// 准备下载参数
DownloadRequest request = new DownloadRequest();
request.setFileId("file123");

// 方式1：仅获取文件内容
byte[] fileContent = pythonRestClient.callPythonServiceForBytes(
    "http://python-service/api/download",
    request
);

// 方式2：获取完整的文件信息
FileResponse fileResponse = pythonRestClient.callPythonServiceForFile(
    "http://python-service/api/download",
    request
);

// 使用文件信息
String fileName = fileResponse.getFileName();
String contentType = fileResponse.getContentType();
long fileSize = fileResponse.getFileSize();
byte[] content = fileResponse.getContent();
Map<String, String> metadata = fileResponse.getMetadata();

// 保存文件
try (FileOutputStream fos = new FileOutputStream(fileName)) {
    fos.write(fileResponse.getContent());
}
```

## 3. FileResponse 对象说明

FileResponse 是文件下载的响应对象，包含以下属性：

| 属性名 | 类型 | 说明 |
|--------|------|------|
| fileName | String | 文件名称 |
| contentType | String | 文件类型（MIME类型） |
| fileSize | long | 文件大小（字节） |
| content | byte[] | 文件二进制内容 |
| metadata | Map<String, String> | 文件元数据 |

### 元数据示例

Python 服务可以通过自定义 HTTP 头返回文件的元数据：

```python
@app.route('/api/download', methods=['POST'])
def download_file():
    # 处理文件
    file_content = get_file_content()
    
    response = make_response(file_content)
    response.headers['Content-Type'] = 'application/pdf'
    response.headers['Content-Disposition'] = 'attachment; filename="report.pdf"'
    response.headers['X-File-Author'] = 'John Doe'
    response.headers['X-File-CreateTime'] = '2024-01-20 10:00:00'
    response.headers['X-File-Version'] = '1.0'
    
    return response
```

Java 端获取元数据：
```java
FileResponse response = pythonRestClient.callPythonServiceForFile(url, params);
Map<String, String> metadata = response.getMetadata();

String author = metadata.get("author");         // "John Doe"
String createTime = metadata.get("createtime"); // "2024-01-20 10:00:00"
String version = metadata.get("version");       // "1.0"
```

## 4. 错误处理

所有方法都会进行异常捕获和包装，当发生错误时会抛出 RuntimeException：

```java
try {
    FileResponse response = pythonRestClient.callPythonServiceForFile(url, params);
    // 处理正常响应
} catch (RuntimeException e) {
    // 处理异常
    logger.error("下载文件失败: {}", e.getMessage());
    // 进行错误处理...
}
```

## 5. 最佳实践

1. 文件处理
   - 对于大文件，建议使用流式处理
   - 设置适当的超时时间
   - 考虑添加进度回调

2. 性能优化
   - 合理配置连接池
   - 考虑使用异步调用
   - 添加请求重试机制

3. 安全性
   - 添加适当的认证信息
   - 验证文件类型和大小
   - 使用 HTTPS 进行传输 