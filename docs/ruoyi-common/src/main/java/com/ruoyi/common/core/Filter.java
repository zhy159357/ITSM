package com.ruoyi.common.core;

import java.io.Serializable;

public @interface Filter
{
  public abstract String name();

  public abstract String operator();

  public abstract String mapProperty();

  public abstract Class<? extends Serializable> valueClazz();
}

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.core.web.annotation.Filter
 * JD-Core Version:    0.6.0
 */