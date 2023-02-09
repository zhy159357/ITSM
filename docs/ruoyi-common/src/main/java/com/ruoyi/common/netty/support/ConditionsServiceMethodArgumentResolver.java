/*     */ package com.ruoyi.common.netty.support;
/*     */ 
/*     */

import com.ruoyi.common.core.*;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.common.netty.utils.ConditionCollection;
import com.ruoyi.common.netty.utils.ServiceDataBinder;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.reflect.Field;
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
/*     */ public class ConditionsServiceMethodArgumentResolver
/*     */   implements ServiceMethodArgumentResolver
/*     */ {
/*     */   public static final int MAX_CASCADE_LEVEL = 2;
/*  39 */   private Map<String, Set<String>> cachedDomainFieldNames = Collections.synchronizedMap(new HashMap());
/*     */ 
/*  41 */   private GenericConversionService conversionService = new GenericConversionService();
/*     */ 
/*     */   public boolean supportsParameter(MethodParameter parameter)
/*     */   {
/*  46 */     return (Collection.class.isAssignableFrom(parameter.getParameterType())) && 
/*  46 */       (parameter.hasParameterAnnotation(ConditionCollection.class));
/*     */   }
/*     */ 
/*     */   public Object resolveArgument(MethodParameter parameter, Map<String, List<Object>> params, Object[] providedArgs)
/*     */     throws Exception
/*     */   {
/*  52 */     ConditionCollection conditionCollection = (ConditionCollection)parameter.getParameterAnnotation(ConditionCollection.class);
/*  53 */     EsbServiceMapping esbServiceMapping = (EsbServiceMapping)parameter.getMethodAnnotation(EsbServiceMapping.class);
/*     */ 
/*  56 */     Class domainClazz = conditionCollection.domainClazz();
/*     */ 
/*  58 */     ServiceDataBinder dataBinder = EsbDomainMappingUtils.getEsbMappingDomainBinder(domainClazz, params, esbServiceMapping,
/*  59 */       providedArgs);
/*  60 */     Object domainBean = dataBinder.getTarget();
/*     */ 
/*  64 */     Collection conditions = new ArrayList();
/*     */ 
/*  67 */     Set paramNames = getDomainParamNames(domainClazz);
/*     */ 
/*  69 */     for (String customParam : conditionCollection.customParams()) {
/*  70 */       paramNames.add(customParam);
/*     */     }
/*     */ 
/*  73 */     for (Entry param : params.entrySet()) {
/*  74 */       String beanProperty = getConditionProperty((String)param.getKey(), esbServiceMapping);
/*     */ 
/*  76 */       if (!paramNames.contains(beanProperty))
/*     */         continue;
/*  78 */       Object value = null;
/*  79 */       if (beanProperty.equals(param.getKey())) {
/*  80 */         value = PropertyUtils.getPropertyValue(domainBean, beanProperty);
/*     */       } else {
/*  82 */         value = ((List)param.getValue()).size() > 0 ? ((List)param.getValue()).get(0) : null;
/*  83 */         value = convertValue((String)param.getKey(), value, esbServiceMapping);
/*     */       }
/*     */ 
/*  86 */       if (value != null) {
/*  87 */         Object operator = null;
/*  88 */         String operatorKey = "operator:" + (String)param.getKey();
/*     */ 
/*  90 */         if ((params.containsKey(operatorKey)) && (((List)params.get(operatorKey)).size() > 0))
/*  91 */           operator = ((List)params.get(operatorKey)).get(0);
/*     */         else {
/*  93 */           operator = getOperator((String)param.getKey(), esbServiceMapping);
/*     */         }
/*     */ 
/*  96 */         if (operator == null) {
/*  97 */           operator = "EQUALS";
/*     */         }
/*  99 */         conditions.add(ConditionUtils.getCondition(beanProperty, operator.toString(), value));
/*     */       }
/* 101 */       beanProperty = null;
/* 102 */       value = null;
/*     */     }
/*     */ 
/* 107 */     if ((esbServiceMapping != null) && (esbServiceMapping.pubConditions().length > 0)) {
/* 108 */       conditions.addAll(createPubConditions(esbServiceMapping.pubConditions(), domainBean));
/*     */     }
/* 110 */     return conditions;
/*     */   }
/*     */ 
/*     */   private String getOperator(String paramKey, EsbServiceMapping esbServiceMapping)
/*     */   {
/* 121 */     for (Filter filter : esbServiceMapping.filters()) {
/* 122 */       if (paramKey.equals(filter.name())) {
/* 123 */         return filter.operator();
/*     */       }
/*     */     }
/* 126 */     return "EQUALS";
/*     */   }
/*     */ 
/*     */   private String getConditionProperty(String paramKey, EsbServiceMapping esbServiceMapping)
/*     */   {
/* 137 */     for (Filter filter : esbServiceMapping.filters()) {
/* 138 */       if ((paramKey.equals(filter.name())) && (StringUtils.isNotEmpty(filter.mapProperty()))) {
/* 139 */         return filter.mapProperty();
/*     */       }
/*     */     }
/* 142 */     return paramKey;
/*     */   }
/*     */ 
/*     */   private Object convertValue(String paramKey, Object value, EsbServiceMapping esbServiceMapping)
/*     */   {
/* 154 */     for (Filter filter : esbServiceMapping.filters()) {
/* 155 */       if ((paramKey.equals(filter.name())) && (!paramKey.equals(filter.mapProperty())))
/*     */       {
/* 157 */         return this.conversionService.convert(value, filter.valueClazz());
/*     */       }
/*     */     }
/* 160 */     return value;
/*     */   }
/*     */ 
/*     */   private Set<String> getDomainParamNames(Class<?> domainClazz)
/*     */   {
/* 170 */     String domainClazzName = domainClazz.getName();
/*     */ 
/* 172 */     Set paramNames = new HashSet();
/* 173 */     if (this.cachedDomainFieldNames.containsKey(domainClazzName)) {
/* 174 */       paramNames = (Set)this.cachedDomainFieldNames.get(domainClazzName);
/*     */     }
/*     */     else {
/* 177 */       ConditionFieldCallback conditionFieldCallback = new ConditionFieldCallback("");
/* 178 */       ReflectionUtils.doWithFields(domainClazz, conditionFieldCallback);
/*     */ 
/* 182 */       paramNames.addAll(conditionFieldCallback.getFieldNames());
/* 183 */       this.cachedDomainFieldNames.put(domainClazzName, paramNames);
/*     */     }
/* 185 */     return paramNames;
/*     */   }
/*     */ 
/*     */   private Collection<Condition> createPubConditions(PubCondition[] pubConditions, Object domainBean)
/*     */   {
/* 197 */     Collection conditions = new ArrayList();
/*     */ 
/* 199 */     Object value = null;
/* 200 */     for (PubCondition pubCondition : pubConditions) {
/* 201 */       value = PropertyUtils.getPropertyValue(domainBean, pubCondition.property());
/* 202 */       if (value != null) {
/* 203 */         conditions.add(ConditionUtils.getCondition(pubCondition.property(), pubCondition.operator(), value));
/*     */       }
/* 205 */       value = null;
/*     */     }
/* 207 */     return conditions;
/*     */   }
/*     */ 
/*     */   class ConditionFieldCallback
/*     */     implements FieldCallback
/*     */   {
/* 217 */     private List<String> fieldNames = new ArrayList();
/*     */ 
/* 219 */     private String prefix = "";
/*     */ 
/*     */     public ConditionFieldCallback(String prefix) {
/* 222 */       this.prefix = (prefix == null ? "" : prefix);
/*     */     }
/*     */ 
/*     */     public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException
/*     */     {
/* 227 */       String fieldName = field.getName();
/*     */ 
///* 229 */       Column column = (Column)field.getAnnotation(Column.class);
/////* 230 */       if (column != null) {
/////* 231 */         this.fieldNames.add(this.prefix + fieldName);
/////*     */       } else {
/* 233 */         Class fieldClass = field.getType();
/*     */ 
/* 235 */         if (Domain.class.isAssignableFrom(fieldClass))
/*     */         {
/* 238 */           if (this.prefix.split("\\.").length > 3) {
/* 239 */             return;
/*     */           }
/* 242 */           ConditionFieldCallback conditionFieldCallback = new ConditionFieldCallback
                    (this.prefix + field.getName() + ".");
/* 244 */           ReflectionUtils.doWithFields(fieldClass, conditionFieldCallback);
/* 245 */           this.fieldNames.addAll(conditionFieldCallback.getFieldNames());
/*     */         }
///*     */       }
/*     */     }
/*     */ 
/*     */     public List<String> getFieldNames() {
/* 251 */       return this.fieldNames;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.ConditionsServiceMethodArgumentResolver
 * JD-Core Version:    0.6.0
 */