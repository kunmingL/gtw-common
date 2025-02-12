package com.changjiang.python.model;

import java.util.Map;

/**
 * 文件响应实体类
 * 用于封装从 Python 服务返回的文件相关信息
 */
public class FileResponse {
    private String fileName;           // 文件名
    private String contentType;        // 文件类型
    private long fileSize;            // 文件大小
    private byte[] content;           // 文件内容
    private Map<String, String> metadata;  // 文件元数据

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
        this.fileSize = content != null ? content.length : 0;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
} 