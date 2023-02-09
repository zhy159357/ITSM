/*    */ package com.ruoyi.common.core;
/*    */ 
/*    */ public class HibernateOrder
/*    */   implements com.ruoyi.common.core.Order
/*    */ {
/*    */   private String propertyName;
/*    */   private boolean ascending;
/*    */   private static final long serialVersionUID = 6022774683096569626L;
/*    */ 
/*    */   public HibernateOrder(String propertyName, boolean ascending)
/*    */   {
/* 17 */     this.propertyName = propertyName;
/* 18 */     this.ascending = ascending;
/*    */   }
/*    */ 
/*    */   public String getProperty() {
/* 22 */     return this.propertyName;
/*    */   }
/*    */ 
/*    */   public boolean isAscending() {
/* 26 */     return this.ascending;
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.core.orm.hibernate.HibernateOrder
 * JD-Core Version:    0.6.0
 */