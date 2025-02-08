package com.changjiang.grpc.strategy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {
    public DateSerializer() {}

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        Long beginDate = -62135798400000L;
        Long time = value.getTime();
        String valueName = value.getClass().getName();
        String trans;
        if (valueName.contains("Time")) {
            trans = value.toString();
            jgen.writeString(trans);
        } else if (time.equals(beginDate)) {
            trans = "-";
            jgen.writeString(trans);
        } else {
            jgen.writeObject(value.getTime());
        }
    }
}
