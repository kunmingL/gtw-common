package com.changjiang.grpc.util;

import com.changjiang.grpc.strategy.BigDecimalSerializer;
import com.changjiang.grpc.strategy.NpcsPropertyNamingStrategy;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class NpcsSerializerUtil {
    private static ObjectMapper serializeObjectMapper;
    private static ObjectMapper accuracyObjectMapper;

    public NpcsSerializerUtil() {}

    public static void doMapperInit() {
        if (serializeObjectMapper == null) {
            initSerializeObjectMapper();
        }
        if (accuracyObjectMapper == null) {
            initAccuracyObjectMapper();
        }
    }

    public static void initSerializeObjectMapper() {
        JsonFactoryBuilder jsonFactoryBuilder = new JsonFactoryBuilder();
        jsonFactoryBuilder.configure(JsonFactory.Feature.INTERN_FIELD_NAMES, false);
        jsonFactoryBuilder.configure(JsonFactory.Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING, false);
        JsonFactory jsonFactory = jsonFactoryBuilder.build();
        serializeObjectMapper = new ObjectMapper(jsonFactory);
        serializeObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        serializeObjectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        serializeObjectMapper.setPropertyNamingStrategy(new NpcsPropertyNamingStrategy());
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
        serializeObjectMapper.registerModule(simpleModule);
        serializeObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static void initAccuracyObjectMapper() {
        JsonFactoryBuilder jsonFactoryBuilder = new JsonFactoryBuilder();
        jsonFactoryBuilder.configure(JsonFactory.Feature.INTERN_FIELD_NAMES, false);
        jsonFactoryBuilder.configure(JsonFactory.Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING, false);
        JsonFactory jsonFactory = jsonFactoryBuilder.build();
        accuracyObjectMapper = new ObjectMapper(jsonFactory);
        accuracyObjectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        accuracyObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        accuracyObjectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        accuracyObjectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        accuracyObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(Date.class, new DateSerializer());
        simpleModule.addSerializer(BigDecimal.class, BigDecimalSerializer.instance);
        accuracyObjectMapper.registerModule(simpleModule);
        accuracyObjectMapper.setPropertyNamingStrategy(new NpcsPropertyNamingStrategy());
    }

    public static String writeValueAsStringNormal(Object value) {
        try {
            doMapperInit();
            return serializeObjectMapper.writeValueAsString(value);
        } catch (JsonProcessingException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static <T> T readValueNormal(String value, Class<T> classtype) {
        try {
            doMapperInit();
            return serializeObjectMapper.readValue(value, classtype);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static <T> T readValueNormalType(String value, TypeReference<T> valueTypeRef) {
        try {
            doMapperInit();
            return serializeObjectMapper.readValue(value, valueTypeRef);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static <T> T readValueNormalJavaType(String value, JavaType valueType) {
        try {
            doMapperInit();
            return serializeObjectMapper.readValue(value, valueType);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String writeValueAsStringAccuracy(Object value) {
        try {
            doMapperInit();
            return accuracyObjectMapper.writeValueAsString(value);
        } catch (JsonProcessingException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static <T> T readValueAccuracy(String value, Class<T> classtype) {
        try {
            doMapperInit();
            return accuracyObjectMapper.readValue(value, classtype);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static JavaType getTypeByClass(Class reqClass) {
        doMapperInit();
        return serializeObjectMapper.getTypeFactory().constructType(reqClass);
    }

    public static JavaType getParameterizeTypeByClass(Class<?> parametrized, Class<?>... parameterClasses) {
        doMapperInit();
        return serializeObjectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    public static void remove() {
        serializeObjectMapper = null;
        accuracyObjectMapper = null;
    }

    static {
        initSerializeObjectMapper();
        initAccuracyObjectMapper();
    }
}
