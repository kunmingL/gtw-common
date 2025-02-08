package com.changjiang.grpc.annotation;

import com.changjiang.grpc.config.GrpcAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GrpcAutoConfiguration.class)
public @interface EnableGrpcService {
} 