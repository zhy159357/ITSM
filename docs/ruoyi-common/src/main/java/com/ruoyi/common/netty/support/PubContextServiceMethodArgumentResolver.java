/*    */ package com.ruoyi.common.netty.support;
/*    */ 
/*    */

import com.ruoyi.common.esb.data.PubContext;
import org.springframework.core.MethodParameter;

import java.util.List;
import java.util.Map;

/*    */
/*    */
/*    */ 
/*    */ public class PubContextServiceMethodArgumentResolver
/*    */   implements ServiceMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 21 */     return PubContext.class.isAssignableFrom(parameter.getParameterType());
/*    */   }
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, Map<String, List<Object>> params, Object[] providedArgs)
/*    */     throws Exception
/*    */   {
/* 28 */     for (Object providedArg : providedArgs) {
/* 29 */       if ((providedArg instanceof PubContext)) {
/* 30 */         return (PubContext)providedArg;
/*    */       }
/*    */     }
/* 33 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.PubContextServiceMethodArgumentResolver
 * JD-Core Version:    0.6.0
 */