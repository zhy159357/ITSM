/*    */ package com.ruoyi.common.netty.support;
/*    */ 
/*    */

import com.ruoyi.common.core.DomainCollection;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import org.springframework.core.MethodParameter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/*    */
/*    */
/*    */
/*    */ 
/*    */ public class DomainCollectionServiceMethodArgumentResolver
/*    */   implements ServiceMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 28 */     return (Collection.class.isAssignableFrom(parameter.getParameterType())) && 
/* 28 */       (parameter.hasParameterAnnotation(DomainCollection.class));
/*    */   }
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, Map<String, List<Object>> params, Object[] providedArgs)
/*    */     throws Exception
/*    */   {
/* 39 */     DomainCollection domainCollection = (DomainCollection)parameter.getParameterAnnotation(DomainCollection.class);
/*    */ 
/* 41 */     if (domainCollection == null) {
/* 42 */       return null;
/*    */     }
/*    */ 
/* 45 */     return EsbDomainMappingUtils.getEsbMappingDomainsBinder(
/* 46 */       domainCollection.name(), 
/* 47 */       domainCollection.domainClazz(), 
/* 48 */       params, 
/* 49 */       (EsbServiceMapping)parameter.getMethodAnnotation(EsbServiceMapping.class),
/* 50 */       providedArgs);
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.DomainCollectionServiceMethodArgumentResolver
 * JD-Core Version:    0.6.0
 */