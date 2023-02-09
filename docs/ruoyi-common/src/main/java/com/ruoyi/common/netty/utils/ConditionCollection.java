package com.ruoyi.common.netty.utils;

import com.ruoyi.common.core.Domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionCollection
{
  public abstract Class<? extends Domain> domainClazz();

  public abstract String[] customParams();
}

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.annotation.ConditionCollection
 * JD-Core Version:    0.6.0
 */