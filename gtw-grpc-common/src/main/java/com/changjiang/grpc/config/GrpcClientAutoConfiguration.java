package com.changjiang.grpc.config;

import com.changjiang.grpc.client.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "grpc.client.enabled", havingValue = "true", matchIfMissing = true)
public class GrpcClientAutoConfiguration {

    @Bean
    public GrpcClient grpcClient(@Value("${grpc.server.host}") String host, @Value("${grpc.server.port}") int port) {
        return new GrpcClient(host, port);
    }
}
