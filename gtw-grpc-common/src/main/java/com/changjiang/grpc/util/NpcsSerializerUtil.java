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

/**
 * NpcsSerializerUtil 是一个工具类，用于序列化和反序列化对象。
 * 它使用 Jackson 库来处理 JSON 数据，并提供了两种 ObjectMapper 实例：
 * - serializeObjectMapper：用于常规的序列化和反序列化。
 * - accuracyObjectMapper：用于需要高精度处理的序列化和反序列化。
 */
public class NpcsSerializerUtil {
    private static ObjectMapper serializeObjectMapper;
    private static ObjectMapper accuracyObjectMapper;

    /**
     * 私有构造函数，防止实例化。
     */
    public NpcsSerializerUtil() {}

    /**
     * 初始化两个 ObjectMapper 实例。
     * 如果 serializeObjectMapper 或 accuracyObjectMapper 未初始化，则进行初始化。
     */
    public static void doMapperInit() {
        if (serializeObjectMapper == null) {
            initSerializeObjectMapper();
        }
        if (accuracyObjectMapper == null) {
            initAccuracyObjectMapper();
        }
    }

    /**
     * 初始化 serializeObjectMapper 实例。
     * 配置了自定义的属性命名策略和序列化器。
     */
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

    /**
     * 初始化 accuracyObjectMapper 实例。
     * 配置了高精度处理的序列化器和反序列化器。
     */
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

    /**
     * 将对象序列化为 JSON 字符串。
     * 使用 serializeObjectMapper 进行序列化。
     *
     * @param value 要序列化的对象。
     * @return 序列化后的 JSON 字符串。
     */
    public static String writeValueAsStringNormal(Object value) {
        try {
            doMapperInit();
            return serializeObjectMapper.writeValueAsString(value);
        } catch (JsonProcessingException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象。
     * 使用 serializeObjectMapper 进行反序列化。
     *
     * @param value JSON 字符串。
     * @param classtype 目标对象的类型。
     * @param <T> 泛型类型。
     * @return 反序列化后的对象。
     */
    public static <T> T readValueNormal(String value, Class<T> classtype) {
        try {
            doMapperInit();
            return serializeObjectMapper.readValue(value, classtype);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象。
     * 使用 serializeObjectMapper 进行反序列化。
     *
     * @param value JSON 字符串。
     * @param valueTypeRef 目标对象的类型引用。
     * @param <T> 泛型类型。
     * @return 反序列化后的对象。
     */
    public static <T> T readValueNormalType(String value, TypeReference<T> valueTypeRef) {
        try {
            doMapperInit();
            return serializeObjectMapper.readValue(value, valueTypeRef);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象。
     * 使用 serializeObjectMapper 进行反序列化。
     *
     * @param value JSON 字符串。
     * @param valueType 目标对象的 JavaType。
     * @param <T> 泛型类型。
     * @return 反序列化后的对象。
     */
    public static <T> T readValueNormalJavaType(String value, JavaType valueType) {
        try {
            doMapperInit();
            return serializeObjectMapper.readValue(value, valueType);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    /**
     * 将对象序列化为 JSON 字符串。
     * 使用 accuracyObjectMapper 进行序列化。
     *
     * @param value 要序列化的对象。
     * @return 序列化后的 JSON 字符串。
     */
    public static String writeValueAsStringAccuracy(Object value) {
        try {
            doMapperInit();
            return accuracyObjectMapper.writeValueAsString(value);
        } catch (JsonProcessingException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象。
     * 使用 accuracyObjectMapper 进行反序列化。
     *
     * @param value JSON 字符串。
     * @param classtype 目标对象的类型。
     * @param <T> 泛型类型。
     * @return 反序列化后的对象。
     */
    public static <T> T readValueAccuracy(String value, Class<T> classtype) {
        try {
            doMapperInit();
            return accuracyObjectMapper.readValue(value, classtype);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    /**
     * 根据类类型获取 JavaType。
     *
     * @param reqClass 类类型。
     * @return JavaType 对象。
     */
    public static JavaType getTypeByClass(Class reqClass) {
        doMapperInit();
        return serializeObjectMapper.getTypeFactory().constructType(reqClass);
    }

    /**
     * 根据参数化类型获取 JavaType。
     *
     * @param parametrized 参数化类型。
     * @param parameterClasses 参数化类型的参数类。
     * @return JavaType 对象。
     */
    public static JavaType getParameterizeTypeByClass(Class<?> parametrized, Class<?>... parameterClasses) {
        doMapperInit();
        return serializeObjectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    /**
     * 清除 ObjectMapper 实例。
     */
    public static void remove() {
        serializeObjectMapper = null;
        accuracyObjectMapper = null;
    }

    static {
        initSerializeObjectMapper();
        initAccuracyObjectMapper();
    }
}
