/*     */ package com.ruoyi.common.netty.support;
/*     */ 
/*     */

import com.ruoyi.common.core.Domain;
import com.ruoyi.common.core.PropertyUtils;
import com.ruoyi.common.core.PubCondition;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.common.esb.data.PubContext;
import com.ruoyi.common.netty.utils.ServiceDataBinder;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import com.ruoyi.common.core.BeanUtils;
import java.util.*;
import java.util.Map.Entry;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class EsbDomainMappingUtils
/*     */ {
/*     */   private static final String DOMAIN_PROP_SPLIT_KEY = "].";
/*     */ 
/*     */   public static ServiceDataBinder getEsbMappingDomainBinder(Class domainClazz, Map<String, List<Object>> params, EsbServiceMapping esbServiceMapping, Object[] providedArgs)
/*     */   {
/*  41 */     Object target = BeanUtils.instantiateClass(domainClazz);
/*  42 */     ServiceDataBinder dataBinder = new ServiceDataBinder(target);
/*     */ 
/*  44 */     Map pValues = new HashMap();
/*  45 */     for (Entry entry : params.entrySet()) {
/*     */       try {
/*  47 */         pValues.put((String)entry.getKey(), (String[])((List)entry.getValue()).toArray(new String[((List)entry.getValue()).size()]));
/*     */       } catch (Exception e) {
/*  49 */         System.out.println("ESB参数转换异常..." + (String)entry.getKey() + " = " + entry.getValue());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  54 */     if (esbServiceMapping != null) {
/*  55 */       PubContext putContext = null;
/*     */       Object localObject1;
                for (int e = 0; e < providedArgs.length; ++e) {
                    Object providedArg = providedArgs[e];
/*  58 */         if ((providedArg instanceof PubContext)) {
/*  59 */           putContext = (PubContext)providedArg;
/*     */         }
/*     */       }
/*     */ 
/*  63 */       if ((putContext != null) && (esbServiceMapping.pubConditions().length > 0)) {
                    for (int e = 0; e < esbServiceMapping.pubConditions().length; ++e) {
                        PubCondition pubCondition = esbServiceMapping.pubConditions()[e];
                       Object value = PropertyUtils.getPropertyValue(putContext, pubCondition.pubProperty());
/*  66 */           if (value != null) {
/*  67 */             pValues.put(pubCondition.property(), new String[] { value.toString() });
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  73 */     PropertyValues pvs = new MutablePropertyValues(pValues);
/*  74 */     dataBinder.bind(pvs);
/*  75 */     return (ServiceDataBinder)dataBinder;
/*     */   }
/*     */ 
/*     */   public static Object getEsbMappingDomainsBinder(String propName, Class<? extends Domain> domainClazz, Map<String, List<Object>> params, EsbServiceMapping esbServiceMapping, Object[] providedArgs)
/*     */   {
/*  90 */     Map domainMappings = new HashMap();
/*     */ 
/*  92 */     Collection records = new ArrayList();
/*     */     String paramName;
/*  94 */     for (Entry entry : params.entrySet()) {
/*  95 */       paramName = (String)entry.getKey();
/*  96 */       int splitIndex = paramName.indexOf("].");
/*  97 */       if ((splitIndex > 0) && (paramName.startsWith(propName + "["))) {
/*  98 */         String key = paramName.substring(0, splitIndex + "].".length());
/*  99 */         String recordPropName = paramName.substring(splitIndex + "].".length());
/*     */         DomainMapping domainMapping;
/* 102 */         if (domainMappings.containsKey(key))
/* 103 */           domainMapping = (DomainMapping)domainMappings.get(key);
/*     */         else {
/* 105 */           domainMapping = new DomainMapping();
/*     */         }
/*     */ 
/* 108 */         domainMapping.addPropValue(recordPropName, (List)entry.getValue());
/* 109 */         domainMappings.put(key, domainMapping);
/*     */       }
/*     */     }
/*     */ 
/* 113 */     if (domainMappings.size() > 0)
/*     */     {
/* 115 */       for (Object domainMapping : domainMappings.values()) {
/* 116 */         Object record = getEsbMappingDomainBinder(domainClazz, ((DomainMapping)domainMapping).getParams(), esbServiceMapping, providedArgs)
/* 117 */           .getTarget();
/* 118 */         if (record != null) {
/* 119 */           records.add(record);
/*     */         }
/* 121 */         record = null;
/*     */       }
/*     */     }
/*     */ 
/* 125 */     return records;
/*     */   }
/*     */ 
/*     */   private static class DomainMapping
/*     */   {
/* 130 */     private Map<String, List<Object>> params = new HashMap();
/*     */ 
/*     */     void addPropValue(String propName, List<Object> values) {
/* 133 */       this.params.put(propName, values);
/*     */     }
/*     */ 
/*     */     public Map<String, List<Object>> getParams() {
/* 137 */       return this.params;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.EsbDomainMappingUtils
 * JD-Core Version:    0.6.0
 */