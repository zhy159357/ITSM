package com.ruoyi.activiti.web;

public @interface PubCondition {

    String property();

    String operator() default Condition.EQUALS;

    String pubProperty();
}
