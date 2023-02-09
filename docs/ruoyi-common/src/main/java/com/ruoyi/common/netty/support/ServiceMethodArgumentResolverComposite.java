/*    */ package com.ruoyi.common.netty.support;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ 
/*    */ public class ServiceMethodArgumentResolverComposite
/*    */   implements ServiceMethodArgumentResolver
/*    */ {
/* 23 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */ 
/* 26 */   private final List<ServiceMethodArgumentResolver> argumentResolvers = new LinkedList();
/*    */ 
/* 29 */   private final Map<MethodParameter, ServiceMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap(256);
/*    */ 
/*    */   public List<ServiceMethodArgumentResolver> getResolvers()
/*    */   {
/* 34 */     return Collections.unmodifiableList(this.argumentResolvers);
/*    */   }
/*    */ 
/*    */   public boolean supportsParameter(MethodParameter parameter)
/*    */   {
/* 42 */     return getArgumentResolver(parameter) != null;
/*    */   }
/*    */ 
/*    */   public Object resolveArgument(MethodParameter parameter, Map<String, List<Object>> params, Object[] providedArgs)
/*    */     throws Exception
/*    */   {
/* 53 */     ServiceMethodArgumentResolver resolver = getArgumentResolver(parameter);
/* 54 */     Assert.notNull(resolver, "Unknown parameter type [" + parameter.getParameterType().getName() + "]");
/* 55 */     return resolver.resolveArgument(parameter, params, providedArgs);
/*    */   }
/*    */ 
/*    */   private ServiceMethodArgumentResolver getArgumentResolver(MethodParameter parameter)
/*    */   {
/* 62 */     ServiceMethodArgumentResolver result = (ServiceMethodArgumentResolver)this.argumentResolverCache.get(parameter);
/* 63 */     if (result == null) {
/* 64 */       for (ServiceMethodArgumentResolver methodArgumentResolver : this.argumentResolvers) {
/* 65 */         if (this.logger.isTraceEnabled()) {
/* 66 */           this.logger.trace("Testing if argument resolver [" + methodArgumentResolver + "] supports [" + 
/* 67 */             parameter.getGenericParameterType() + "]");
/*    */         }
/* 69 */         if (methodArgumentResolver.supportsParameter(parameter)) {
/* 70 */           result = methodArgumentResolver;
/* 71 */           this.argumentResolverCache.put(parameter, result);
/* 72 */           break;
/*    */         }
/*    */       }
/*    */     }
/* 76 */     return result;
/*    */   }
/*    */ 
/*    */   public ServiceMethodArgumentResolverComposite addResolver(ServiceMethodArgumentResolver argumentResolver)
/*    */   {
/* 83 */     this.argumentResolvers.add(argumentResolver);
/* 84 */     return this;
/*    */   }
/*    */ 
/*    */   public ServiceMethodArgumentResolverComposite addResolvers(List<? extends ServiceMethodArgumentResolver> argumentResolvers)
/*    */   {
/* 92 */     if (argumentResolvers != null) {
/* 93 */       for (ServiceMethodArgumentResolver resolver : argumentResolvers) {
/* 94 */         this.argumentResolvers.add(resolver);
/*    */       }
/*    */     }
/* 97 */     return this;
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.ServiceMethodArgumentResolverComposite
 * JD-Core Version:    0.6.0
 */