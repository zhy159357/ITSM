package com.ruoyi.common.core;

import org.springframework.context.MessageSource;

import java.util.Locale;

public abstract interface ExceptionMessage
{
  public abstract Message getExceptionMessage();

  public abstract Message getExceptionMessage(MessageSource paramMessageSource, Locale paramLocale);
}

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.core.exception.ExceptionMessage
 * JD-Core Version:    0.6.0
 */