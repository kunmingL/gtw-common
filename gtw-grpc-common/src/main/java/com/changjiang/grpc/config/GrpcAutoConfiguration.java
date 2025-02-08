package com.changjiang.grpc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({GrpcServerAutoConfiguration.class, GrpcClientAutoConfiguration.class})
public class GrpcAutoConfiguration {
} 