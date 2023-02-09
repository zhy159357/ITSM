package com.ruoyi.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义导出word报表数据注解
 *
 * @author zhangchao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Word {
    /**
     * 字典key名称 例如：sys_user_sex
     */
    public String paraName() default "";
}
