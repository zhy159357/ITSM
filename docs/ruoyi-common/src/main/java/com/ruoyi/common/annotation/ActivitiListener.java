package com.ruoyi.common.annotation;

import com.ruoyi.common.enums.ListenerBusinessType;
import com.ruoyi.common.enums.ListenerType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ActivitiListener {

    /**
     * 监听器名称
     */
    public String listenerName();

    /**
     * 监听器类型 默认执行监听器
     */
    public ListenerType listenerType() default ListenerType.EXECUTION_LISTENER;

    /**
     * 监听器业务类型
     */
    public ListenerBusinessType listenerBusinessType();
}
