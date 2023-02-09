/*     */ package com.ruoyi.common.netty.support;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class ServiceMethod
/*     */ {
/*  24 */   protected final Log logger = LogFactory.getLog(ServiceMethod.class);
/*     */   private final Object bean;
/*     */   private final Method method;
/*     */   private final BeanFactory beanFactory;
/*     */   private MethodParameter[] parameters;
/*     */   private final Method bridgedMethod;
/*     */ 
/*     */   public ServiceMethod(Object bean, Method method)
/*     */   {
/*  41 */     Assert.notNull(bean, "bean is required");
/*  42 */     Assert.notNull(method, "method is required");
/*  43 */     this.bean = bean;
/*  44 */     this.beanFactory = null;
/*  45 */     this.method = method;
/*  46 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*     */   }
/*     */ 
/*     */   public ServiceMethod(Object bean, String methodName, Class<?>[] parameterTypes)
/*     */     throws NoSuchMethodException
/*     */   {
/*  54 */     Assert.notNull(bean, "bean is required");
/*  55 */     Assert.notNull(methodName, "method is required");
/*  56 */     this.bean = bean;
/*  57 */     this.beanFactory = null;
/*  58 */     this.method = bean.getClass().getMethod(methodName, parameterTypes);
/*  59 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
/*     */   }
/*     */ 
/*     */   public ServiceMethod(String beanName, BeanFactory beanFactory, Method method)
/*     */   {
/*  68 */     Assert.hasText(beanName, "beanName is required");
/*  69 */     Assert.notNull(beanFactory, "beanFactory is required");
/*  70 */     Assert.notNull(method, "method is required");
/*  71 */     Assert.isTrue(beanFactory.containsBean(beanName), 
/*  72 */       "Bean factory [" + beanFactory + "] does not contain bean [" + beanName + "]");
/*  73 */     this.bean = beanName;
/*  74 */     this.beanFactory = beanFactory;
/*  75 */     this.method = method;
/*  76 */     this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*     */   }
/*     */ 
/*     */   protected ServiceMethod(ServiceMethod handlerMethod)
/*     */   {
/*  83 */     Assert.notNull(handlerMethod, "HandlerMethod is required");
/*  84 */     this.bean = handlerMethod.bean;
/*  85 */     this.beanFactory = handlerMethod.beanFactory;
/*  86 */     this.method = handlerMethod.method;
/*  87 */     this.bridgedMethod = handlerMethod.bridgedMethod;
/*  88 */     this.parameters = handlerMethod.parameters;
/*     */   }
/*     */ 
/*     */   public Object getBean()
/*     */   {
/*  95 */     return this.bean;
/*     */   }
/*     */ 
/*     */   public Method getMethod()
/*     */   {
/* 102 */     return this.method;
/*     */   }
/*     */ 
/*     */   public Class<?> getBeanType()
/*     */   {
/* 110 */     Class clazz = (this.bean instanceof String) ? 
/* 111 */       this.beanFactory.getType((String)this.bean) : this.bean.getClass();
/*     */ 
/* 113 */     return ClassUtils.getUserClass(clazz);
/*     */   }
/*     */ 
/*     */   protected Method getBridgedMethod()
/*     */   {
/* 121 */     return this.bridgedMethod;
/*     */   }
/*     */ 
/*     */   public MethodParameter[] getMethodParameters()
/*     */   {
/* 128 */     if (this.parameters == null) {
/* 129 */       int parameterCount = this.bridgedMethod.getParameterTypes().length;
/* 130 */       this.parameters = new MethodParameter[parameterCount];
/* 131 */       for (int i = 0; i < parameterCount; i++) {
/* 132 */         this.parameters[i] = new HandlerMethodParameter(i);
/*     */       }
/*     */     }
/* 135 */     return this.parameters;
/*     */   }
/*     */ 
/*     */   public MethodParameter getReturnType()
/*     */   {
/* 142 */     return new HandlerMethodParameter(-1);
/*     */   }
/*     */ 
/*     */   public MethodParameter getReturnValueType(Object returnValue)
/*     */   {
/* 149 */     return new ReturnValueMethodParameter(returnValue);
/*     */   }
/*     */ 
/*     */   public boolean isVoid()
/*     */   {
/* 156 */     return Void.TYPE.equals(getReturnType().getParameterType());
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType)
/*     */   {
/* 166 */     return AnnotationUtils.findAnnotation(this.method, annotationType);
/*     */   }
/*     */ 
/*     */   public ServiceMethod createWithResolvedBean()
/*     */   {
/* 174 */     Object handler = this.bean;
/* 175 */     if ((this.bean instanceof String)) {
/* 176 */       String beanName = (String)this.bean;
/* 177 */       handler = this.beanFactory.getBean(beanName);
/*     */     }
/* 179 */     ServiceMethod handlerMethod = new ServiceMethod(handler, this.method);
/* 180 */     handlerMethod.parameters = getMethodParameters();
/* 181 */     return handlerMethod;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 186 */     if (this == o) {
/* 187 */       return true;
/*     */     }
/* 189 */     if ((o != null) && ((o instanceof ServiceMethod))) {
/* 190 */       ServiceMethod other = (ServiceMethod)o;
/* 191 */       return (this.bean.equals(other.bean)) && (this.method.equals(other.method));
/*     */     }
/* 193 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 198 */     return 31 * this.bean.hashCode() + this.method.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 203 */     return this.method.toGenericString();
/*     */   }
/*     */ 
/*     */   private class HandlerMethodParameter extends MethodParameter
/*     */   {
/*     */     protected HandlerMethodParameter(int index)
/*     */     {
                super(method,index);
/*     */     }
/*     */ 
/*     */     public Class<?> getDeclaringClass()
/*     */     {
/* 217 */       return ServiceMethod.this.getBeanType();
/*     */     }
/*     */ 
/*     */     public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType)
/*     */     {
/* 222 */       return ServiceMethod.this.getMethodAnnotation(annotationType);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ReturnValueMethodParameter extends HandlerMethodParameter
/*     */   {
/*     */     private final Object returnValue;
/*     */ 
/*     */     public ReturnValueMethodParameter(Object returnValue)
/*     */     {
/* 234 */       super(-1);
/* 235 */       this.returnValue = returnValue;
/*     */     }
/*     */ 
/*     */     public Class<?> getParameterType()
/*     */     {
/* 240 */       return this.returnValue != null ? this.returnValue.getClass() : super.getParameterType();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.method.support.ServiceMethod
 * JD-Core Version:    0.6.0
 */