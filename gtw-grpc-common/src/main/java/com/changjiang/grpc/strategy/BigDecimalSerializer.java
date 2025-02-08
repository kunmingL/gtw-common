package com.changjiang.grpc.strategy;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializerBase;

import java.math.BigDecimal;

public class BigDecimalSerializer extends ToStringSerializerBase {
    public static final BigDecimalSerializer instance = new BigDecimalSerializer();

    public BigDecimalSerializer() {
        super(Object.class);
    }

    public BigDecimalSerializer(Class<?> handledType) {
        super(handledType);
    }

    public final String valueToString(Object value) {
        if (value == null) {
            return null;
        } else {
            BigDecimal valueDecimal = (BigDecimal)value;
            return valueDecimal == null ? "" : valueDecimal.toPlainString();
        }
    }
}