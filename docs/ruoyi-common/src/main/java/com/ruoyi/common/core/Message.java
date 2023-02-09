/*    */ package com.ruoyi.common.core;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Message
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -694991823003131043L;
/*    */   public String info;
/*    */   public String code;
/*    */ 
/*    */   public Message(String info)
/*    */   {
/* 12 */     this.code = "000000";
/* 13 */     this.info = info;
/*    */   }
/*    */ 
/*    */   public Message(String code, String info) {
/* 17 */     this.code = code;
/* 18 */     this.info = info;
/*    */   }
/*    */ 
/*    */   public Message() {
/* 22 */     this.code = "000000";
/*    */   }
/*    */ 
/*    */   public String getInfo() {
/* 26 */     return this.info;
/*    */   }
/*    */ 
/*    */   public void setInfo(String info) {
/* 30 */     this.info = info;
/*    */   }
/*    */ 
/*    */   public String getCode() {
/* 34 */     return this.code;
/*    */   }
/*    */ 
/*    */   public void setCode(String code) {
/* 38 */     this.code = code;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 42 */     return "Message [info=" + this.info + ", code=" + this.code + "]";
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.core.web.view.Message
 * JD-Core Version:    0.6.0
 */