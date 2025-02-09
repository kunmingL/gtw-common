package com.changjiang.grpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@ConfigurationProperties(prefix = "grpc.registry")
public class GrpcRegistryConfig {

    private final Map<String, ServiceConfig> services = new HashMap<>();

    public static class ServiceConfig {
        private String registerId;
        private String host;
        private int port;
        private boolean enabled = true;

        // Getters and Setters
        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public Map<String, ServiceConfig> getServices() {
        return services;
    }
}