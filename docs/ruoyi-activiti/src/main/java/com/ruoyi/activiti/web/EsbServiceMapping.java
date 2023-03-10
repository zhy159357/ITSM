package com.ruoyi.activiti.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EsbServiceMapping {
    InitializeProperty[] initializeProperties() default {};

    PubCondition[] pubConditions() default {};

    Filter[] filters() default {};

    String caption() default "";//交易描述

    String trancode() default "";//交易代码
}