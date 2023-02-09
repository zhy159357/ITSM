/*    */ package com.ruoyi.common.netty.support;
/*    */ 
/*    */

import com.ruoyi.common.core.Domain;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.common.netty.utils.ServiceDataBinder;
import org.springframework.core.MethodParameter;

import java.util.List;
import java.util.Map;

/*    */
/*    */
/*    */
/*    */
/*    */ 
/*    */ public class DomainServiceMethodArgumentResolver
/*    */   implements ServiceMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 26 */     return Domain.class.isAssignableFrom(parameter.getParameterType());
/*    */   }
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, Map<String, List<Object>> params, Object[] providedArgs)
/*    */     throws Exception
/*    */   {
/* 35 */     ServiceDataBinder dataBinder = EsbDomainMappingUtils.getEsbMappingDomainBinder(
/* 36 */       parameter.getParameterType(), 
/* 37 */       params, 
/* 38 */       (EsbServiceMapping)parameter.getMethodAnnotation(EsbServiceMapping.class),
/* 39 */       providedArgs);
/*    */ 
/* 41 */     return dataBinder.getTarget();
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.DomainServiceMethodArgumentResolver
 * JD-Core Version:    0.6.0
 */