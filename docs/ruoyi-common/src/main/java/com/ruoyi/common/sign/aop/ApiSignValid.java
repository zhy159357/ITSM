package com.ruoyi.common.sign.aop;

import java.lang.annotation.*;

/**
 * 注解 - 接口签名校验
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiSignValid {

}
