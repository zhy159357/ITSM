package com.ruoyi.activiti.web;

public @interface Filter {

    String name();//页面参数

    String operator();

    String mapProperty() default "";//映射到实体的属性

    Class<? extends java.io.Serializable> valueClazz() default String.class;

}
