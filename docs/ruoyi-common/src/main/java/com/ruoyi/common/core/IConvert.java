package com.ruoyi.common.core;

public abstract interface IConvert<T>
{
  public abstract T get(T paramT);

  public abstract String getName();

  public abstract String toJson();
}

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.core.convert.IConvert
 * JD-Core Version:    0.6.0
 */