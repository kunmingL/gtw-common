package com.changjiang.grpc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "grpc.server")
public class GrpcServerConfig {
    private boolean enabled = true;
    private String host = "0.0.0.0";
    private int port = 9090;
    private int maxConcurrentCallsPerConnection = 100;
    private int keepAliveTimeInSeconds = 60;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public int getMaxConcurrentCallsPerConnection() {
        return maxConcurrentCallsPerConnection;
    }

    public void setMaxConcurrentCallsPerConnection(int maxConcurrentCallsPerConnection) {
        this.maxConcurrentCallsPerConnection = maxConcurrentCallsPerConnection;
    }

    public int getKeepAliveTimeInSeconds() {
        return keepAliveTimeInSeconds;
    }

    public void setKeepAliveTimeInSeconds(int keepAliveTimeInSeconds) {
        this.keepAliveTimeInSeconds = keepAliveTimeInSeconds;
    }
} 