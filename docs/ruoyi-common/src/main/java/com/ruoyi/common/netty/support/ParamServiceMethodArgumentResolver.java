/*    */ package com.ruoyi.common.netty.support;
/*    */ 
/*    */

import com.ruoyi.common.core.ServiceParam;
import com.ruoyi.common.esb.data.PubContext;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.core.MethodParameter;

import java.util.List;
import java.util.Map;

/*    */
/*    */
/*    */
/*    */ 
/*    */ public class ParamServiceMethodArgumentResolver
/*    */   implements ServiceMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 26 */     return parameter.getParameterAnnotation(ServiceParam.class) != null;
/*    */   }
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, Map<String, List<Object>> params, Object[] providedArgs)
/*    */     throws Exception
/*    */   {
/* 34 */     ServiceParam serviceParam = (ServiceParam)parameter.getParameterAnnotation(ServiceParam.class);
/* 35 */     String paramName = serviceParam.name();
/* 36 */     List values = (List)params.get(paramName);
/*    */ 
/* 39 */     if ((values == null) && (StringUtils.isNotEmpty(serviceParam.pubProperty()))) {
/* 40 */       PubContext pubContext = null;
/* 41 */       for (Object providedArg : providedArgs)
/*    */       {
/* 43 */         if ((providedArg instanceof PubContext)) {
/* 44 */           pubContext = (PubContext)providedArg;
/*    */         }
/*    */       }
/* 47 */       if (pubContext != null) {
/* 48 */         return pubContext.getParams().get(serviceParam.pubProperty());
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 54 */     Class pType = parameter.getParameterType();
/* 55 */     if (List.class.isAssignableFrom(pType))
/* 56 */       return values;
/* 57 */     if ((pType.isArray()) && (values != null)) {
/* 58 */       return values.toArray(new String[values.size()]);
/*    */     }
/* 60 */     return (values == null) || (values.size() == 0) ? null : values.get(0);
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.ParamServiceMethodArgumentResolver
 * JD-Core Version:    0.6.0
 */