/*    */ package com.ruoyi.common.netty.support;
/*    */ 
/*    */

import com.ruoyi.common.netty.utils.Pager;
import org.springframework.core.MethodParameter;

import java.util.List;
import java.util.Map;

/*    */
/*    */
/*    */ 
/*    */ public class PagerServiceMethodArgumentResolver
/*    */   implements ServiceMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 14 */     return Pager.class.isAssignableFrom(parameter.getParameterType());
/*    */   }
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, Map<String, List<Object>> params, Object[] providedArgs)
/*    */     throws Exception
/*    */   {
/* 20 */     String pageSize = getString(params, "pager:pageSize");
/* 21 */     String pageIndex = getString(params, "pager:pageIndex");
/* 22 */     String pageType = getString(params, "pager:pageType");
/*    */ 
/* 24 */     return new Pager(pageSize, pageIndex, pageType);
/*    */   }
/*    */ 
/*    */   public String getString(Map<String, List<Object>> params, String paramName) {
/* 28 */     List values = (List)params.get(paramName);
/* 29 */     if ((values == null) || (values.size() == 0)) {
/* 30 */       return "";
/*    */     }
/* 32 */     return values.get(0).toString();
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.PagerServiceMethodArgumentResolver
 * JD-Core Version:    0.6.0
 */