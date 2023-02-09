package com.ruoyi.common.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceParam
{
  public abstract String name()default "";

  public abstract String pubProperty() default "";

  public abstract boolean isTokenParam() default false;
}
