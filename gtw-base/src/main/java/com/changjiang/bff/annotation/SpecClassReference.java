package com.changjiang.bff.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecClassReference {

    String field();

    Class<?> targetClass();

}
