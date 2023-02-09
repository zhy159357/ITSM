/*    */ package com.ruoyi.common.netty.support;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*    */
/*    */
/*    */
/*    */
/*    */ 
/*    */ public class MapServiceMethodArgumentResolver
/*    */   implements ServiceMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 24 */     return Map.class.isAssignableFrom(parameter.getParameterType());
/*    */   }
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, Map<String, List<Object>> params, Object[] providedArgs)
/*    */     throws Exception
/*    */   {
/* 35 */     Map mapParams = new HashMap();
/*    */ 
/* 37 */     for (Entry entry : params.entrySet()) {
/* 38 */       if (entry.getValue() == null)
/*    */         continue;
/* 40 */       if (((List)entry.getValue()).size() == 1)
/* 41 */         mapParams.put((String)entry.getKey(), ((List)entry.getValue()).get(0));
/*    */       else {
/* 43 */         mapParams.put((String)entry.getKey(), entry.getValue());
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 51 */     return mapParams;
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.MapServiceMethodArgumentResolver
 * JD-Core Version:    0.6.0
 */