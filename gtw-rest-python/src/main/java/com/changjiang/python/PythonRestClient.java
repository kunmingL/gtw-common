package com.changjiang.python;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.changjiang.python.model.FileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

/**
 * Python REST 服务调用客户端
 * 提供了多种方式调用 Python 服务的能力，支持普通参数、文件上传和二进制数据处理
 */
@Component
public class PythonRestClient {
    private static final Logger logger = LoggerFactory.getLogger(PythonRestClient.class);
    
    /**
     * Spring REST 请求模板，用于执行 HTTP 请求
     */
    private final RestTemplate restTemplate;

    /**
     * 构造函数，初始化 RestTemplate
     */
    public PythonRestClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 发送普通 JSON 请求到 Python 服务
     * 适用于简单的请求-响应场景，响应类型明确的情况
     *
     * @param url 目标 Python 服务的 URL
     * @param params 请求参数对象，将被转换为 JSON
     * @param responseType 期望的响应类型
     * @return 指定类型的响应对象
     * @throws RuntimeException 当请求失败或响应处理出错时抛出
     */
    public <T> T callPythonService(String url, Object params, Class<T> responseType) {
        try {
            // 设置请求头，指定 Content-Type 为 application/json
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 将参数对象序列化为 JSON 字符串
            String jsonBody = JSON.toJSONString(params);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            // 执行 POST 请求
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            // 将响应 JSON 转换为指定类型对象
            return JSON.parseObject(response.getBody(), responseType);
        } catch (Exception e) {
            logger.error("Error calling Python service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Python service", e);
        }
    }

    /**
     * 发送支持泛型的 JSON 请求
     * 适用于需要处理复杂类型（如 List<T>, Map<K,V> 等）的场景
     *
     * @param url 目标 Python 服务的 URL
     * @param params 请求参数对象
     * @param typeReference 泛型类型引用，用于正确解析复杂类型
     * @return 指定泛型类型的响应对象
     * @throws RuntimeException 当请求失败或响应处理出错时抛出
     */
    public <T> T callPythonService(String url, Object params, TypeReference<T> typeReference) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonBody = JSON.toJSONString(params);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            return JSON.parseObject(response.getBody(), typeReference);
        } catch (Exception e) {
            logger.error("Error calling Python service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Python service", e);
        }
    }

    /**
     * 发送混合类型请求，支持文件上传和普通参数
     * 适用于需要同时上传文件和其他数据的场景
     *
     * @param url 目标 Python 服务的 URL
     * @param files 文件映射，key 为文件参数名，value 为文件对象
     * @param params 其他参数映射
     * @param responseType 期望的响应类型
     * @return 指定类型的响应对象
     * @throws RuntimeException 当文件处理、请求发送或响应处理出错时抛出
     */
    public <T> T callPythonServiceWithFiles(
            String url,
            Map<String, MultipartFile> files,
            Map<String, Object> params,
            Class<T> responseType) {
        try {
            // 设置请求头为 multipart/form-data，用于文件上传
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 创建多部分请求体
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // 处理文件部分
            if (files != null) {
                for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
                    MultipartFile file = entry.getValue();
                    // 将文件转换为 ByteArrayResource
                    ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    };
                    body.add(entry.getKey(), resource);
                }
            }

            // 处理其他参数部分
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    body.add(entry.getKey(), JSON.toJSONString(entry.getValue()));
                }
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            return JSON.parseObject(response.getBody(), responseType);
        } catch (IOException e) {
            logger.error("Error processing files: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process files", e);
        } catch (Exception e) {
            logger.error("Error calling Python service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Python service", e);
        }
    }

    /**
     * 处理二进制响应的请求
     * 适用于需要获取二进制数据（如图片、文件等）的场景
     *
     * @param url 目标 Python 服务的 URL
     * @param params 请求参数对象
     * @return 二进制响应数据
     * @throws RuntimeException 当请求失败或响应处理出错时抛出
     */
    public byte[] callPythonServiceForBytes(String url, Object params) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonBody = JSON.toJSONString(params);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            // 直接获取字节数组响应
            ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                byte[].class
            );

            return response.getBody();
        } catch (Exception e) {
            logger.error("Error calling Python service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Python service", e);
        }
    }

    /**
     * 处理文件下载请求
     * 适用于需要获取文件数据及其元信息的场景
     *
     * @param url 目标 Python 服务的 URL
     * @param params 请求参数对象
     * @return 文件响应对象，包含文件内容和元数据
     * @throws RuntimeException 当请求失败或响应处理出错时抛出
     */
    public FileResponse callPythonServiceForFile(String url, Object params) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonBody = JSON.toJSONString(params);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            // 发送请求并获取响应
            ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                byte[].class
            );

            // 创建文件响应对象
            FileResponse fileResponse = new FileResponse();
            
            // 设置文件内容
            fileResponse.setContent(response.getBody());
            
            // 从响应头中获取文件信息
            HttpHeaders responseHeaders = response.getHeaders();
            
            // 获取文件名
            String contentDisposition = responseHeaders.getFirst(HttpHeaders.CONTENT_DISPOSITION);
            if (contentDisposition != null) {
                // 解析文件名
                String fileName = extractFileName(contentDisposition);
                fileResponse.setFileName(fileName);
            }
            
            // 获取内容类型
            MediaType contentType = responseHeaders.getContentType();
            if (contentType != null) {
                fileResponse.setContentType(contentType.toString());
            }
            
            // 获取其他元数据
            Map<String, String> metadata = new HashMap<>();
            responseHeaders.forEach((key, value) -> {
                if (key.startsWith("X-File-")) {
                    // 提取自定义文件元数据
                    String metaKey = key.substring(7).toLowerCase();
                    metadata.put(metaKey, value.get(0));
                }
            });
            fileResponse.setMetadata(metadata);

            return fileResponse;
        } catch (Exception e) {
            logger.error("Error downloading file from Python service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to download file from Python service", e);
        }
    }

    /**
     * 从 Content-Disposition 头中提取文件名
     */
    private String extractFileName(String contentDisposition) {
        try {
            return ContentDisposition.parse(contentDisposition)
                .getFilename();
        } catch (Exception e) {
            // 如果解析失败，尝试手动解析
            String[] parts = contentDisposition.split(";");
            for (String part : parts) {
                part = part.trim();
                if (part.startsWith("filename=")) {
                    return part.substring(9).replace("\"", "");
                }
            }
            return "unknown_file";
        }
    }
}
