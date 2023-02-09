 package com.ruoyi.common.netty.support;
 
 import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
 
 public abstract class ServiceMethodSelector
 {
   public static Set<Method> selectMethods(Class<?> handlerType, MethodFilter handlerMethodFilter)
   {
     Set handlerMethods = new LinkedHashSet();
     Set handlerTypes = new LinkedHashSet();
     Class specificHandlerType = null;
     if (!Proxy.isProxyClass(handlerType)) {
       handlerTypes.add(handlerType);
       specificHandlerType = handlerType;
     }
     handlerTypes.addAll(Arrays.asList(handlerType.getInterfaces()));
     for(Object currentHandlerType : handlerTypes) {
      Class targetClass = specificHandlerType != null ? specificHandlerType :(Class<?>)currentHandlerType;
      ReflectionUtils.doWithMethods((Class<?>) currentHandlerType, new MethodCallback() {
         public void doWith(Method method) {
           Method specificMethod = ClassUtils.getMostSpecificMethod(method, ServiceMethodSelector.class);
          Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
          if ((handlerMethodFilter.matches(specificMethod)) && (
             (bridgedMethod == specificMethod) || (!handlerMethodFilter.matches(bridgedMethod))))
            handlerMethods.add(specificMethod);
         }
       }, ReflectionUtils.USER_DECLARED_METHODS);
     }
     return handlerMethods;
   }
 }
