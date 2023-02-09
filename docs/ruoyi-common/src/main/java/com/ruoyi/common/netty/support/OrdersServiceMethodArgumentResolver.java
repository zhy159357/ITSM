/*    */ package com.ruoyi.common.netty.support;
/*    */ 
/*    */

import com.ruoyi.common.core.ConditionUtils;
import com.ruoyi.common.core.OrderCollection;
import org.springframework.core.MethodParameter;

import java.util.*;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ 
/*    */ public class OrdersServiceMethodArgumentResolver
/*    */   implements ServiceMethodArgumentResolver
/*    */ {
/*    */   public static final String PREFIX_ORDERBY_DESC = "desc:";
/*    */   public static final String PREFIX_ORDERBY_ASC = "asc:";
/*    */ 
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 32 */     return (Collection.class.isAssignableFrom(parameter.getParameterType())) && 
/* 32 */       (parameter.hasParameterAnnotation(OrderCollection.class));
/*    */   }
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, Map<String, List<Object>> params, Object[] providedArgs)
/*    */     throws Exception
/*    */   {
/* 41 */     Collection orders = new ArrayList();
/* 42 */     List orderBys = (List)params.get("orderBy");
/*    */ 
/* 44 */     if (orderBys != null) {
/* 45 */       for (Iterator localIterator = orderBys.iterator(); localIterator.hasNext(); ) { Object orderBy = localIterator.next();
/* 46 */         String propertyName = orderBy.toString();
/* 47 */         if (propertyName.startsWith("asc:"))
/* 48 */           orders.add(ConditionUtils.getOrder(propertyName.substring("asc:".length()), true));
/* 49 */         else if (propertyName.startsWith("desc:")) {
/* 50 */           orders.add(ConditionUtils.getOrder(propertyName.substring("desc:".length()), false));
/*    */         }
/*    */       }
/*    */     }
/* 54 */     return orders;
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.OrdersServiceMethodArgumentResolver
 * JD-Core Version:    0.6.0
 */