/*    */ package com.ruoyi.common.esb.data;
/*    */ 
/*    */ import com.ruoyi.common.utils.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*    */
/*    */
/*    */
/*    */ 
/*    */ public class PubContext
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 8278975025549567107L;
/*    */   public static final String PROPERTY_PARAM_PREFIX = "param.";
/*    */   private String username;
/*    */   private String roles;
/* 29 */   private Map<String, Object> params = new HashMap();
/*    */ 
/*    */   public void addParam(String paramName, Object paramValue) {
/* 32 */     if ((StringUtils.isNotEmpty(paramName)) && (paramValue != null))
/* 33 */       this.params.put(paramName, paramValue);
/*    */   }
/*    */ 
/*    */   public void addParams(Map<String, Object> params)
/*    */   {
/* 38 */     if (params != null)
/* 39 */       this.params.putAll(params);
/*    */   }
/*    */ 
/*    */   public String getRoles()
/*    */   {
/* 44 */     return this.roles;
/*    */   }
/*    */ 
/*    */   public void setRoles(String roles) {
/* 48 */     this.roles = roles;
/*    */   }
/*    */ 
/*    */   public String getUsername() {
/* 52 */     return this.username;
/*    */   }
/*    */ 
/*    */   public void setUsername(String username) {
/* 56 */     this.username = username;
/*    */   }
/*    */ 
/*    */   public Map<String, Object> getParams() {
/* 60 */     return this.params;
/*    */   }
/*    */ 
/*    */   public void setParams(Map<String, Object> params) {
/* 64 */     this.params = params;
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.data.PubContext
 * JD-Core Version:    0.6.0
 */