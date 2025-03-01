package com.changjiang.python;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.changjiang.python.model.FileResponse;
import com.changjiang.python.model.StanderResponseModle;
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
public abstract class PythonRestClient {
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
     * 发送请求并返回指定类型的数据
     * @param url 请求URL
     * @param params 请求参数
     * @param responseType 期望的返回类型
     * @return 指定类型的响应数据
     */
    public <T> T callPythonService(String url, Object params, Class<T> responseType) {
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

            // 解析标准响应模型
            StanderResponseModle standerResponse = JSON.parseObject(response.getBody(), StanderResponseModle.class);
            
            // 检查响应状态
            if (!"200".equals(standerResponse.getCode())) {
                throw new RuntimeException("Service call failed: " + standerResponse.getMsg());
            }

            // 从data字段中提取数据并转换为指定类型
            JSONObject data = standerResponse.getData();
            if (data == null) {
                return null;
            }

            // 获取响应数据
            Object responseData = data.containsKey("response") ? data.get("response") : data;
            
            // 处理基础类型
            if (isPrimitiveOrWrapper(responseType)) {
                if (responseData == null) {
                    return null;
                }
                // 直接转换基础类型
                return convertPrimitiveType(responseData, responseType);
            }
            
            // 处理字符串类型
            if (String.class.equals(responseType)) {
                return responseType.cast(responseData.toString());
            }
            
            // 其他复杂类型使用JSON转换
            return JSON.parseObject(JSON.toJSONString(responseData), responseType);
        } catch (Exception e) {
            logger.error("Error calling Python service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Python service: " + e.getMessage(), e);
        }
    }

    /**
     * 判断是否为基础类型或其包装类
     */
    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() || 
               Integer.class.equals(clazz) ||
               Long.class.equals(clazz) ||
               Double.class.equals(clazz) ||
               Float.class.equals(clazz) ||
               Boolean.class.equals(clazz) ||
               Byte.class.equals(clazz) ||
               Short.class.equals(clazz) ||
               Character.class.equals(clazz);
    }

    /**
     * 转换基础类型
     */
    @SuppressWarnings("unchecked")
    private <T> T convertPrimitiveType(Object value, Class<T> targetType) {
        if (value == null) {
            return null;
        }

        // 处理数字类型
        if (value instanceof Number) {
            Number number = (Number) value;
            if (targetType == Integer.class || targetType == int.class) {
                return (T) Integer.valueOf(number.intValue());
            }
            if (targetType == Long.class || targetType == long.class) {
                return (T) Long.valueOf(number.longValue());
            }
            if (targetType == Double.class || targetType == double.class) {
                return (T) Double.valueOf(number.doubleValue());
            }
            if (targetType == Float.class || targetType == float.class) {
                return (T) Float.valueOf(number.floatValue());
            }
            if (targetType == Short.class || targetType == short.class) {
                return (T) Short.valueOf(number.shortValue());
            }
            if (targetType == Byte.class || targetType == byte.class) {
                return (T) Byte.valueOf(number.byteValue());
            }
        }

        // 处理布尔类型
        if (targetType == Boolean.class || targetType == boolean.class) {
            if (value instanceof Boolean) {
                return (T) value;
            }
            if (value instanceof String) {
                return (T) Boolean.valueOf(value.toString());
            }
            if (value instanceof Number) {
                return (T) Boolean.valueOf(((Number) value).intValue() != 0);
            }
        }

        // 处理字符类型
        if (targetType == Character.class || targetType == char.class) {
            if (value instanceof Character) {
                return (T) value;
            }
            String str = value.toString();
            if (str.length() > 0) {
                return (T) Character.valueOf(str.charAt(0));
            }
        }

        // 如果无法转换，尝试使用toString后再转换
        try {
            return (T) value;
        } catch (ClassCastException e) {
            throw new RuntimeException("Cannot convert value of type " + 
                value.getClass().getName() + " to target type " + targetType.getName());
        }
    }

    /**
     * 发送请求并返回复杂类型的数据（支持泛型）
     * @param url 请求URL
     * @param params 请求参数
     * @param typeReference 期望的返回类型引用
     * @return 指定类型的响应数据
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

            StanderResponseModle standerResponse = JSON.parseObject(response.getBody(), StanderResponseModle.class);
            
            if (!"200".equals(standerResponse.getCode())) {
                throw new RuntimeException("Service call failed: " + standerResponse.getMsg());
            }

            JSONObject data = standerResponse.getData();
            if (data == null) {
                return null;
            }

            // 如果data中有response字段，则从response中获取数据
            if (data.containsKey("response")) {
                return JSON.parseObject(data.getString("response"), typeReference);
            }
            
            // 否则直接转换整个data对象
            return JSON.parseObject(data.toJSONString(), typeReference);
        } catch (Exception e) {
            logger.error("Error calling Python service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Python service: " + e.getMessage(), e);
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
