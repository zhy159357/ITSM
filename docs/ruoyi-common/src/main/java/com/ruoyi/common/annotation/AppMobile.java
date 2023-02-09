package com.ruoyi.common.annotation;

import java.lang.annotation.*;

/**
 * app前端访问自定义拦截注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AppMobile {
}
