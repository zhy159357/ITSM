/*    */ package com.ruoyi.common.core;
/*    */ 
/*    */ import java.util.LinkedHashMap;

/*    */
/*    */ 
/*    */ public class Convert<T> extends LinkedHashMap<String, T>
/*    */   implements IConvert<T>
/*    */ {
/*    */   private static final long serialVersionUID = 8592585220347877766L;
/*    */   private String name;
/*    */ 
/*    */   public String getName()
/*    */   {
/* 43 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void setName(String name)
/*    */   {
/* 50 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public String toJson() {
/* 54 */     StringBuffer jsonBuf = new StringBuffer("{");
/*    */ 
/* 56 */     for (String key : keySet()) {
/* 57 */       jsonBuf.append("\"").append(key).append("\":\"").append(get(key)).append("\",");
/*    */     }
/*    */ 
/* 63 */     if (jsonBuf.length() > 1) {
/* 64 */       jsonBuf.deleteCharAt(jsonBuf.length() - 1);
/*    */     }
/* 66 */     jsonBuf.append("}");
/* 67 */     return jsonBuf.toString();
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.taglib.convert.Convert
 * JD-Core Version:    0.6.0
 */