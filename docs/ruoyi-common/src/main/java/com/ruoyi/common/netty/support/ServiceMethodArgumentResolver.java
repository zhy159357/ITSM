package com.ruoyi.common.netty.support;

import org.springframework.core.MethodParameter;

import java.util.List;
import java.util.Map;

public abstract interface ServiceMethodArgumentResolver
{
  public abstract boolean supportsParameter(MethodParameter paramMethodParameter);

  public abstract Object resolveArgument(MethodParameter paramMethodParameter, Map<String, List<Object>> paramMap, Object[] paramArrayOfObject)
    throws Exception;
}

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.ServiceMethodArgumentResolver
 * JD-Core Version:    0.6.0
 */