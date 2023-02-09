/*    */ package com.ruoyi.common.core;
/*    */ 
/*    */ public class ConditionFactory
/*    */ {
/* 14 */   private static ConditionFactory conditionFactory = null;
/*    */ 
/*    */   public static ConditionFactory getInstance()
/*    */   {
/* 21 */     if (conditionFactory == null) {
/* 22 */       conditionFactory = new ConditionFactory();
/*    */     }
/* 24 */     return conditionFactory;
/*    */   }
/*    */ 
/*    */   public Condition getCondition(String property, String operator, Object value) {
/* 28 */     return new HibernateCondition(property, operator, value);
/*    */   }
/*    */ 
/*    */   public Order getOrder(String propertyName, boolean ascending) {
/* 32 */     return new HibernateOrder(propertyName, ascending);
/*    */   }
/*    */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.core.orm.ConditionFactory
 * JD-Core Version:    0.6.0
 */