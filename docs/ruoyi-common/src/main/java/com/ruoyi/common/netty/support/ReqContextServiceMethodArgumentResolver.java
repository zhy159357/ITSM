/*    */ package com.ruoyi.common.netty.support;
/*    */ 
/*    */

import com.ruoyi.common.core.ReqContext;
import org.springframework.core.MethodParameter;

import java.util.List;
import java.util.Map;

/*    */
/*    */
/*    */ 
/*    */ public class ReqContextServiceMethodArgumentResolver
/*    */   implements ServiceMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 25 */     return ReqContext.class.isAssignableFrom(parameter.getParameterType());
/*    */   }
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, Map<String, List<Object>> params, Object[] providedArgs)
/*    */     throws Exception
/*    */   {
/* 35 */     if ((params instanceof ReqContext)) {
/* 36 */       return params;
/*    */     }
/* 38 */     return null;
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.ReqContextServiceMethodArgumentResolver
 * JD-Core Version:    0.6.0
 */