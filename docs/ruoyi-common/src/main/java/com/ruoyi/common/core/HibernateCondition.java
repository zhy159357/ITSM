/*     */ package com.ruoyi.common.core;
/*     */ 
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class HibernateCondition
/*     */   implements Condition
/*     */ {
/*     */   protected String property;
/*     */   protected String operator;
/*     */   protected Object value;
/*     */ 
/*     */   public HibernateCondition(String property, String operator, Object value)
/*     */   {
/*  31 */     this.property = property;
/*  32 */     this.operator = operator;
/*  33 */     this.value = value;
/*     */   }
/*     */ 
/*     */   /*public Criterion generateExpression(String alias)
*//*     *//*   {
*//*  41 *//*     if (this.value != null) {
*//*  42 *//*       if (this.operator.equals("EQUALS"))
*//*  43 *//*         return Restrictions.eq(this.property, this.value);
*//*  44 *//*       if (this.operator.equals("NOT_EQUALS"))
*//*  45 *//*         return Restrictions.ne(this.property, this.value);
*//*  46 *//*       if (this.operator.equals("LIKE"))
*//*  47 *//*         return Restrictions.like(this.property, this.value.toString(), MatchMode.ANYWHERE);
*//*  48 *//*       if (this.operator.equals("END"))
*//*  49 *//*         return Restrictions.like(this.property, this.value.toString(), MatchMode.END);
*//*  50 *//*       if (this.operator.equals("START"))
*//*  51 *//*         return Restrictions.like(this.property, this.value.toString(), MatchMode.START);
*//*  52 *//*       if (this.operator.equals("BETWEEN")) {
*//*  53 *//*         String[] betweenArray = this.value.toString().split("-BTW-");
*//*  54 *//*         if (betweenArray.length < 2) return null;
*//*  55 *//*         return generateBetween(betweenArray[0], betweenArray[1]);
*//*  56 *//*       }if (this.operator.equals("IN")) {
*//*  57 *//*         if ((this.value instanceof Object[]))
*//*  58 *//*           return Restrictions.in(this.property, (Object[])(Object[])this.value);
*//*     *//*       }
*//*  60 *//*       else if (this.operator.equals("NOT_IN")) {
*//*  61 *//*         if ((this.value instanceof Object[]))
*//*  62 *//*           return Restrictions.not(Restrictions.in(this.property, (Object[])(Object[])this.value));
*//*     *//*       } else {
*//*  64 *//*         if (this.operator.equals("LEFT"))
*//*  65 *//*           return Restrictions.lt(this.property, this.value);
*//*  66 *//*         if (this.operator.equals("RIGHT"))
*//*  67 *//*           return Restrictions.gt(this.property, this.value);
*//*  68 *//*         if (this.operator.equals("LEFT_EQ"))
*//*  69 *//*           return Restrictions.le(this.property, this.value);
*//*  70 *//*         if (this.operator.equals("RIGHT_EQ")) {
*//*  71 *//*           return Restrictions.ge(this.property, this.value);
*//*     *//*         }
*//*     *//*       }
*//*     *//*     }
*//*  75 *//*     if (this.operator.equals("IS_NULL"))
*//*  76 *//*       return Restrictions.isNull(this.property);
*//*  77 *//*     if (this.operator.equals("NOT_NULL")) {
*//*  78 *//*       return Restrictions.isNotNull(this.property);
*//*     *//*     }
*//*     *//*
*//*  81 *//*     return null;
*//*     *//*   }
*//*     *//*
*//*     *//*   public Criterion generateExpression() {
*//*  85 *//*     return generateExpression(null);
*//*     *//*   }
*//*     *//*
*//*     *//*   private Criterion generateBetween(String begin, String end) {
*//*  89 *//*     return Restrictions.between(this.property, begin, end);
*//*     *//*   }*/
/*     */ 
/*     */   public String getOperator() {
/*  93 */     return this.operator;
/*     */   }
/*     */ 
/*     */   public void setOperator(String operator) {
/*  97 */     this.operator = operator;
/*     */   }
/*     */ 
/*     */   public Object getValue() {
/* 101 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void setValue(Object value) {
/* 105 */     this.value = value;
/*     */   }
/*     */ 
/*     */   public String getProperty() {
/* 109 */     return this.property;
/*     */   }
/*     */ 
/*     */   public void setProperty(String property) {
/* 113 */     this.property = property;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 117 */     return this.property + " " + this.operator + " " + this.value;
/*     */   }
/*     */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.core.orm.hibernate.HibernateCondition
 * JD-Core Version:    0.6.0
 */