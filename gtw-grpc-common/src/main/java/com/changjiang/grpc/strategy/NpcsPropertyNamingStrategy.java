package com.changjiang.grpc.strategy;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

public class NpcsPropertyNamingStrategy extends PropertyNamingStrategy {
    private static final long serialVersionUID = -500693678625482342L;

    public NpcsPropertyNamingStrategy() {}

    public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        String configName = method.getName().substring(3);
        return configName.substring(0, 1).toLowerCase().concat(configName.substring(1));
    }

    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        String configName = null;
        if (method.getName().startsWith("is")) {
            configName = method.getName().substring(2);
        } else {
            configName = method.getName().substring(3);
        }
        return configName.substring(0, 1).toLowerCase().concat(configName.substring(1));
    }
}
