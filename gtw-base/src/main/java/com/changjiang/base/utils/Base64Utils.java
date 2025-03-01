package com.changjiang.base.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64Utils {

    /**
     * 将文件内容转换为 Base64 编码字符串
     *
     * @param filePath 文件路径
     * @return Base64 编码字符串
     * @throws IOException 如果文件读取失败
     */
    public static String encodeFileToBase64(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        Path path = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(fileBytes);
    }

    /**
     * 将 Base64 编码字符串解码并写入文件
     *
     * @param base64String Base64 编码字符串
     * @param outputFilePath 输出文件路径
     * @throws IOException 如果文件写入失败
     */
    public static void decodeBase64ToFile(String base64String, String outputFilePath) throws IOException {
        if (base64String == null || outputFilePath == null) {
            throw new IllegalArgumentException("Base64 string and output file path cannot be null");
        }
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);
        Path path = Paths.get(outputFilePath);
        Files.write(path, decodedBytes);
    }

    /**
     * 将字节数组转换为 Base64 编码字符串
     *
     * @param bytes 字节数组
     * @return Base64 编码字符串
     */
    public static String encode(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Input byte array cannot be null");
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 将 Base64 编码字符串解码为字节数组
     *
     * @param base64String Base64 编码字符串
     * @return 解码后的字节数组
     */
    public static byte[] decode(String base64String) {
        if (base64String == null) {
            throw new IllegalArgumentException("Input Base64 string cannot be null");
        }
        return Base64.getDecoder().decode(base64String);
    }

    /**
     * 将字符串转换为 Base64 编码字符串
     *
     * @param text 原始字符串
     * @return Base64 编码字符串
     */
    public static String encodeFromString(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Input text cannot be null");
        }
        return encode(text.getBytes());
    }

    /**
     * 将 Base64 编码字符串解码为原始字符串
     *
     * @param base64String Base64 编码字符串
     * @return 解码后的原始字符串
     */
    public static String decodeToString(String base64String) {
        if (base64String == null) {
            throw new IllegalArgumentException("Input Base64 string cannot be null");
        }
        return new String(decode(base64String));
    }
}
