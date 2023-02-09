/*     */ package com.ruoyi.common.core;
/*     */ 
/*     */ import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class PropertyUtils
/*     */ {
/*  27 */   private static final Log logger = LogFactory.getLog(PropertyUtils.class);
/*     */ 
/*  29 */   private static final IntParser INT_PARSER = new IntParser();
/*     */ 
/*  31 */   private static final LongParser LONG_PARSER = new LongParser();
/*     */ 
/*  33 */   private static final FloatParser FLOAT_PARSER = new FloatParser();
/*     */ 
/*  35 */   private static final DoubleParser DOUBLE_PARSER = new DoubleParser();
/*     */ 
/*  37 */   private static final BooleanParser BOOLEAN_PARSER = new BooleanParser();
/*     */ 
/*     */   public static void setSimplePropertyValue(Object bean, String property, Object value)
/*     */   {
/*  46 */     Object convertValue = value;
/*  47 */     Field field = FieldUtils.getField(bean.getClass(), property);
/*  48 */     if (field != null) {
/*  49 */       Class fieldType = field.getType();
/*  50 */       if ((fieldType != String.class) && ((value == null) || ((value instanceof String)))) {
/*  51 */         convertValue = getConvertValue(value, fieldType);
/*     */       }
/*     */       try
/*     */       {
/*  55 */         org.apache.commons.beanutils.PropertyUtils.setSimpleProperty(bean, property, convertValue);
/*     */       } catch (IllegalAccessException e) {
/*  57 */         logger.warn(e.getMessage());
/*     */       } catch (InvocationTargetException e) {
/*  59 */         logger.warn(e.getMessage());
/*     */       } catch (NoSuchMethodException e) {
/*  61 */         logger.warn(e.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Object getConvertValue(Object value, Class<?> type) {
/*  67 */     String strValue = null;
/*  68 */     if (value != null) {
/*  69 */       strValue = value.toString();
/*     */     }
/*  71 */     if (type == Integer.TYPE)
/*  72 */       return Integer.valueOf(StringUtils.isEmpty(strValue) ? 0 : INT_PARSER.doParse(strValue).intValue());
/*  73 */     if (type == Long.TYPE)
/*  74 */       return Long.valueOf(StringUtils.isEmpty(strValue) ? 0L : LONG_PARSER.doParse(strValue).longValue());
/*  75 */     if (type == Float.TYPE)
/*  76 */       return Float.valueOf(StringUtils.isEmpty(strValue) ? 0.0F : FLOAT_PARSER.doParse(strValue).floatValue());
/*  77 */     if (type == Double.TYPE)
/*  78 */       return Double.valueOf(StringUtils.isEmpty(strValue) ? 0.0D : DOUBLE_PARSER.doParse(strValue).doubleValue());
/*  79 */     if (type == Boolean.TYPE) {
/*  80 */       return Boolean.valueOf(StringUtils.isEmpty(strValue) ? false : BOOLEAN_PARSER.doParse(strValue).booleanValue());
/*     */     }
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */   public static void setPropertyValue(Object bean, String property, Object value)
/*     */   {
/*  91 */     String[] properties = property.split("\\.");
/*  92 */     if (properties.length > 1) {
/*  93 */       Object objBean = bean;
/*  94 */       Object valueBean = null;
/*  95 */       for (int i = 0; i < properties.length; i++)
/*  96 */         if (i < properties.length - 1) {
/*  97 */           valueBean = createObject(objBean, properties[i]);
/*  98 */           if (valueBean == null) break;
/*  99 */           setPropertyValue(objBean, properties[i], valueBean);
/* 100 */           objBean = valueBean;
/*     */         }
/*     */         else {
/* 103 */           setSimplePropertyValue(objBean, properties[i], value);
/*     */         }
/*     */     }
/*     */     else {
/* 107 */       setSimplePropertyValue(bean, property, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object getSimplePropertyValue(Object bean, String property)
/*     */   {
/* 118 */     if (bean == null) {
/* 119 */       return null;
/*     */     }
/* 121 */     if (Map.class.isAssignableFrom(bean.getClass()))
/* 122 */       return ((Map)bean).get(property);
/*     */     try
/*     */     {
/* 125 */       if (ReflectionUtils.findField(bean.getClass(), property) == null) {
/* 126 */         return null;
/*     */       }
/* 128 */       return org.apache.commons.beanutils.PropertyUtils.getSimpleProperty(bean, property);
/*     */     } catch (IllegalAccessException e) {
/* 130 */       logger.warn("【" + property + "】IllegalAccessException：" + e.getMessage());
/*     */     } catch (IllegalStateException e) {
/* 132 */       logger.warn("【" + property + "】IllegalStateException：" + e.getMessage());
/*     */     } catch (InvocationTargetException e) {
/* 134 */       logger.warn("【" + property + "】InvocationTargetException：" + e.getMessage());
/*     */     } catch (NoSuchMethodException e) {
/* 136 */       logger.debug("【" + property + "】NoSuchMethodException：" + e.getMessage());
/*     */     }
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */   public static Object getPropertyValue(Object bean, String property)
/*     */   {
/* 148 */     String[] properties = property.split("\\.");
/* 149 */     if (properties.length > 1) {
/* 150 */       Object objBean = bean;
/* 151 */       for (int i = 0; i < properties.length; i++)
/* 152 */         if (i < properties.length - 1) {
/* 153 */           objBean = getSimplePropertyValue(objBean, properties[i]);
/*     */         }
/*     */         else
/* 156 */           return getSimplePropertyValue(objBean, properties[i]);
/*     */     }
/*     */     else
/*     */     {
/* 160 */       return getSimplePropertyValue(bean, property);
/*     */     }
/* 162 */     return null;
/*     */   }
/*     */ 
/*     */   private static Object createObject(Object objBean, String property)
/*     */   {
/* 172 */     PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(objBean.getClass(), property);
/*     */ 
/* 174 */     if (pd != null) {
/* 175 */       Class valueClass = pd.getPropertyType();
/* 176 */       Object valueBean = null;
/*     */       try {
/* 178 */         valueBean = valueClass.newInstance();
/*     */       } catch (InstantiationException e) {
/* 180 */         logger.error("创建对象异常【InstantiationException】:" + e.getMessage());
/*     */       } catch (IllegalAccessException e) {
/* 182 */         logger.error("创建对象异常【IllegalAccessException】:" + e.getMessage());
/*     */       }
/* 184 */       return valueBean;
/*     */     }
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */   private static class BooleanParser extends ParameterParser<Boolean>
/*     */   {
/*     */     private BooleanParser()
/*     */     {
/* 257 */       super();
/*     */     }
/*     */ 
/*     */     protected String getType() {
/* 261 */       return "boolean";
/*     */     }
/*     */ 
/*     */     protected Boolean doParse(String parameter) throws NumberFormatException
/*     */     {
/* 266 */       return Boolean.valueOf((parameter.equalsIgnoreCase("true")) || (parameter.equalsIgnoreCase("on")) || (parameter.equalsIgnoreCase("yes")) || (parameter.equals("1")));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class DoubleParser extends ParameterParser<Double>
/*     */   {
/*     */     private DoubleParser()
/*     */     {
/* 243 */       super();
/*     */     }
/*     */ 
/*     */     protected String getType() {
/* 247 */       return "double";
/*     */     }
/*     */ 
/*     */     protected Double doParse(String parameter) throws NumberFormatException
/*     */     {
/* 252 */       return Double.valueOf(parameter);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class FloatParser extends ParameterParser<Float>
/*     */   {
/*     */     private FloatParser()
/*     */     {
/* 228 */       super();
/*     */     }
/*     */ 
/*     */     protected String getType() {
/* 232 */       return "float";
/*     */     }
/*     */ 
/*     */     protected Float doParse(String parameter) throws NumberFormatException
/*     */     {
/* 237 */       return Float.valueOf(parameter);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LongParser extends ParameterParser<Long>
/*     */   {
/*     */     private LongParser()
/*     */     {
/* 213 */       super();
/*     */     }
/*     */ 
/*     */     protected String getType() {
/* 217 */       return "long";
/*     */     }
/*     */ 
/*     */     protected Long doParse(String parameter) throws NumberFormatException
/*     */     {
/* 222 */       return Long.valueOf(parameter);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class IntParser extends ParameterParser<Integer>
/*     */   {
/*     */     private IntParser()
/*     */     {
/* 199 */       super();
/*     */     }
/*     */ 
/*     */     protected String getType() {
/* 203 */       return "int";
/*     */     }
/*     */ 
/*     */     protected Integer doParse(String s) throws NumberFormatException
/*     */     {
/* 208 */       return Integer.valueOf(s);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class ParameterParser<T>
/*     */   {
/*     */     protected abstract String getType();
/*     */ 
/*     */     protected abstract T doParse(String paramString)
/*     */       throws NumberFormatException;
/*     */   }
/*     */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.util.PropertyUtils
 * JD-Core Version:    0.6.0
 */