package com.ruoyi.common.core;

public abstract interface Condition
{
  public static final String BETWEEN = "BETWEEN";
  public static final String LIKE = "LIKE";
  public static final String START = "START";
  public static final String END = "END";
  public static final String EQUALS = "EQUALS";
  public static final String IS_NOT_NULL = "NOT_NULL";
  public static final String IS_NULL = "IS_NULL";
  public static final String OR = "OR";
  public static final String IN = "IN";
  public static final String NOT_IN = "NOT_IN";
  public static final String NOT_EQUALS = "NOT_EQUALS";
  public static final String LEFT = "LEFT";
  public static final String RIGHT = "RIGHT";
  public static final String LEFT_EQ = "LEFT_EQ";
  public static final String RIGHT_EQ = "RIGHT_EQ";
  public static final String BETWEEN_SPLIT = "-BTW-";

  public abstract String getProperty();

  public abstract String getOperator();

  public abstract Object getValue();
}

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.core.orm.Condition
 * JD-Core Version:    0.6.0
 */