package com.changjiang.grpc.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.protobuf.ByteString;

public class SerializationUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 配置 ObjectMapper
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static ByteString serialize(Object obj) {
        try {
            if (obj == null) {
                return ByteString.EMPTY;
            }
            return ByteString.copyFrom(objectMapper.writeValueAsBytes(obj));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }

    public static <T> T deserialize(byte[] bytes, TypeReference<T> typeReference) {
        try {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            return objectMapper.readValue(bytes, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize object", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        try {
            if (fromValue == null) {
                return null;
            }
            if (toValueType.isAssignableFrom(fromValue.getClass())) {
                return (T) fromValue;
            }
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert value", e);
        }
    }
} 