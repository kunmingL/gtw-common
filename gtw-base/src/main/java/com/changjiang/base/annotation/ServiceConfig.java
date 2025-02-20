package com.changjiang.base.annotation;

import com.changjiang.base.constants.SrvChannel;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceConfig {
    String registryId() default "";
    String url() default "";
    SrvChannel[] channel() default {SrvChannel.PC};
    boolean dealResType() default false;
    boolean dataMask() default false;
    String methodName() default "";
    SpecClassReference[] specClassReference() default {};
    String referField() default "";
}
