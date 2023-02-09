/*    */ package com.ruoyi.common.core;
/*    */ 
/*    */ import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

/*    */
/*    */ 
/*    */ public class ReqContext<V> extends LinkedMultiValueMap<String, Object>
/*    */ {
/*    */   private static final long serialVersionUID = -6862991049418613486L;
/*    */   private String authorization;
/*    */ 
/*    */   public ReqContext()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ReqContext(String interfaceName, String serviceName)
/*    */   {
/* 30 */     add("esbpub.interfaceName", interfaceName);
/* 31 */     add("esbpub.serviceName", serviceName);
/*    */   }
/*    */ 
/*    */   public String getInterfaceName()
/*    */   {
/* 40 */     return getString("esbpub.interfaceName");
/*    */   }
/*    */ 
/*    */   public String getServiceName()
/*    */   {
/* 49 */     return getString("esbpub.serviceName");
/*    */   }
/*    */ 
/*    */   public String getInstanceId() {
/* 53 */     return getString("esbpub.instanceId");
/*    */   }
/*    */ 
/*    */   private String getString(String key)
/*    */   {
/* 61 */     List values = get(key);
/* 62 */     return (values == null) || (values.size() == 0) ? null : values.get(0).toString();
/*    */   }
/*    */ 
/*    */   public String getAuthorization() {
/* 66 */     return this.authorization;
/*    */   }
/*    */ 
/*    */   public void setAuthorization(String authorization) {
/* 70 */     this.authorization = authorization;
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.data.ReqContext
 * JD-Core Version:    0.6.0
 */