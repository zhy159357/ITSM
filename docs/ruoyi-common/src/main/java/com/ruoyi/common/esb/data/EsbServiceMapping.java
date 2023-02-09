package com.ruoyi.common.esb.data;

import com.ruoyi.common.core.Filter;
import com.ruoyi.common.core.InitializeProperty;
import com.ruoyi.common.core.PubCondition;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EsbServiceMapping
{
    InitializeProperty[] initializeProperties() default {};

    PubCondition[] pubConditions()default {};

    Filter[] filters()default {};

    String caption()default "";

    String trancode()default "";
}