package com.ruoyi.common.core;
  
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

 
 
 
 
 
 
  
  public class ReflectionUtils extends org.springframework.util.ReflectionUtils
  {
    public static List<Method> annotationedGetterMethod(Class targetClass, Class annotationClass)
    {
/* 36 */     List methods = new ArrayList();
/* 37 */     doWithMethods(targetClass, new MethodCallback()
      {
        public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
/* 40 */         Annotation annotation = method.getAnnotation(annotationClass);
/* 41 */         if ((annotation != null) && (method.getName().startsWith("get")))
/* 42 */           methods.add(method);
        }
      });
/* 45 */     return methods;
    }
  
    public static List<Field> annotationedField(Class targetClass, Class annotationClass)
    {
/* 50 */     List fields = new ArrayList();
/* 51 */     doWithFields(targetClass, new FieldCallback()
      {
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException
        {
/* 55 */         Annotation annotation = field.getAnnotation(annotationClass);
/* 56 */         if ((annotation != null) && (!fields.contains(fields)))
/* 57 */           fields.add(field);
        }
      });
/* 61 */     return fields;
    }
  
    public static Object getPrivateFieldValue(Object bean, String fieldName)
    {
      try
      {
/* 71 */       Field field = bean.getClass().getDeclaredField(fieldName);
/* 72 */       field.setAccessible(true);
/* 73 */       return field.get(bean);
      } catch (SecurityException e) {
/* 75 */       e.printStackTrace();
      } catch (NoSuchFieldException e) {
/* 77 */       e.printStackTrace();
      } catch (IllegalAccessException e) {
/* 79 */       e.printStackTrace();
      }
/* 81 */     return null;
    }
  }

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-core\1.0.4-SNAPSHOT\gsoft-framework-core-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.util.ReflectionUtils
 * JD-Core Version:    0.6.0
 */