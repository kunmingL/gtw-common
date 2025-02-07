package com.changjiang.grpc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GrpcServerConfig.class)
@ConditionalOnProperty(prefix = "grpc.server", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GrpcServerAutoConfiguration {

    @Bean
    public GrpcServerRunner grpcServerRunner(GrpcServerConfig config, ApplicationContext applicationContext) {
        return new GrpcServerRunner(config, applicationContext);
    }
}
