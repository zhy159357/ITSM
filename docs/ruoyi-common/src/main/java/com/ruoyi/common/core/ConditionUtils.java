/*     */ package com.ruoyi.common.core;
/*     */ 
/*     */ import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class ConditionUtils
/*     */ {
/*     */   public static Collection<Condition> getConditions(String prefix, Domain bean, BindingResult result, Map<String, String> filterMap, Map<String, String[]> parameterMap)
/*     */   {
/*  60 */     Collection conditions = new ArrayList();
/*  61 */     if (prefix != null)
/*  62 */       prefix = prefix + ".";
/*     */     else {
/*  64 */       prefix = "";
/*     */     }
/*  66 */     PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(bean.getClass());
/*  67 */     for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
/*  68 */       String name = propertyDescriptor.getName();
/*  69 */       if ("class".equals(name))
/*     */         continue;
/*  71 */       String propertyName = prefix + name;
/*     */ 
///*  73 */       Transient tsi = (Transient)propertyDescriptor.getReadMethod().getAnnotation(Transient.class);
///*  74 */       if (tsi != null)
///*     */       {
///*     */         continue;
///*     */       }
/*  78 */       Object value = result.getFieldValue(propertyName);
/*  79 */       String operator = (String)filterMap.get(propertyName);
/*  80 */       if (operator == null)
/*  81 */         operator = "EQUALS";
/*  82 */       if ((value != null) && (!value.equals("")))
/*  83 */         if ((value instanceof Domain)) {
/*  84 */           conditions.addAll(getConditions(propertyName, (Domain)value, result, filterMap, parameterMap)); } else {
/*  85 */           if ((value instanceof Set))
/*     */             continue;
/*  87 */           if ((value instanceof Collection))
/*     */             continue;
/*  89 */           if ((value instanceof Map))
/*     */           {
/*  91 */             for (Entry entry : parameterMap.entrySet()) {
/*  92 */               if ((((String)entry.getKey()).startsWith(prefix + propertyName + ".")) && (((String[])entry.getValue()).length > 0))
/*  93 */                 conditions.add(ConditionFactory.getInstance().getCondition((String)entry.getKey(), operator, ((String[])entry.getValue())[0]));
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*  98 */             if (!parameterMap.containsKey(propertyName)) {
/*     */               continue;
/*     */             }
/* 101 */             conditions.add(ConditionFactory.getInstance().getCondition(propertyName, operator, value));
/*     */           }
/*     */         }
/*     */     }
/* 105 */     return conditions;
/*     */   }
/*     */ 
/*     */   public static Condition getCondition(String propertyName, String operator, Object value)
/*     */   {
/* 117 */     return new HibernateCondition(propertyName, operator, value);
/*     */   }
/*     */ 
/*     */   public static Order getOrder(String propertyName, boolean ascending)
/*     */   {
/* 128 */     return new HibernateOrder(propertyName, ascending);
/*     */   }
/*     */ 
/*     */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.util.ConditionUtils
 * JD-Core Version:    0.6.0
 */