package com.ruoyi.common.esb.invoke;

import com.ruoyi.common.core.*;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.common.esb.data.PubContext;
import com.ruoyi.common.esb.data.ResContext;
import com.ruoyi.common.netty.support.InvocableSeriveMethod;
import com.ruoyi.common.netty.support.ServiceMethodSelector;
import com.ruoyi.common.netty.utils.PagerRecords;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.*;

@Service("serviceInvoker")
  public class ServiceInvoker
          implements ApplicationContextAware
          {
     private static final Log logger = LogFactory.getLog(ServiceInvoker.class);
        public static final String SERVICE_ERROR_CODE = "999999";
        private ApplicationContext applicationContext;

     
     
    public void setApplicationContext(ApplicationContext applicationContext)
      {
        this.applicationContext = applicationContext;
         
    }

     
     
    public ResContext invoke(String beanName, String methodName, Map<String, List<Object>> params, PubContext pubContext)
          throws Exception
      {
        ResContext res = new ResContext();
         
        if ((beanName != null) && (methodName != null))
              {
            Object bean = null;
             
            try {
                bean = this.applicationContext.getBean(beanName);
                 
            } catch (BeansException e) {
                logger.error("从上下文中获取实例" + beanName + "失败");
                throw e;
                 
            }
             
            /*  72 */
            Method serviceMethod = findServiceMethod(bean, methodName);
             
            /*  74 */
            String defaultTranCode = beanName + "." + methodName;
            /*  75 */
            if (serviceMethod == null) {
                /*  76 */
                res.setMessage(new Message("999991", "未找到服务[" + defaultTranCode + "]."));
                 
            } else {
                /*  78 */
                Object result = doInvokeMethod(params, bean, serviceMethod, pubContext);
                 
                /*  80 */
                EsbServiceMapping mapping = (EsbServiceMapping) serviceMethod.getAnnotation(EsbServiceMapping.class);
                 
                /*  82 */
                String trancode = StringUtils.isNotEmpty(mapping.trancode()) ? mapping.trancode() : defaultTranCode;
                 
                /*  84 */
                res.setTrancode(trancode);
                 
                /*  86 */
                if (result == null) {
                    /*  87 */
                    return res;
                     
                }
                /*  89 */
                Class resultType = result.getClass();
                 
                /*  91 */
                if (List.class.isAssignableFrom(resultType))
                      {
                    /*  93 */
                    res.setRecords((List) result);
                    /*  94 */
                } else if (PagerRecords.class.isAssignableFrom(resultType))
                      {
                    /*  96 */
                    PagerRecords pagerRecords = (PagerRecords) result;
                    /*  97 */
                    res.setRecords(pagerRecords.getRecords());
                    /*  98 */
                    res.setTotalCount(pagerRecords.getTotalCount());
                    /*  99 */
                } else if (Domain.class.isAssignableFrom(resultType))
                      {
                    /* 101 */
                    res.setRecord((Domain) result);
                    /* 102 */
                } else if (Message.class.isAssignableFrom(resultType))
                      {
                    /* 104 */
                    res.setMessage((Message) result);
                    /* 105 */
                } else if (ResContext.class.isAssignableFrom(resultType)) {
                    /* 106 */
                    res = (ResContext) result;
                    /* 107 */
                } else if (String.class.isAssignableFrom(resultType)) {
                    /* 108 */
                    Record record = new Record();
                    /* 109 */
                    record.put("html", result);
                    /* 110 */
                    res.setRecord(record);
                    /* 111 */
                } else if (Convert.class.isAssignableFrom(resultType)) {
                    /* 112 */
                    Record record = new Record();
                    /* 113 */
                    record.putAll((Convert) result);
                    /* 114 */
                    res.setRecord(record);
                     
                }
                 
            }
             
        } else {
            /* 118 */
            res.setMessage(new Message("960004", "缺少服务参数."));
             
        }
         
        /* 121 */
        return res;
         
    }

     
     
    private Method findServiceMethod(Object bean, String methodName)
      {
        /* 133 */
        Class beanClass = getTargetClass(bean);
         
        /* 135 */
        boolean isInterfaceMethod = false;
        /* 136 */
        for (Class beanInterfaceClazz : beanClass.getInterfaces()) {
            /* 137 */
            if (ClassUtils.getMethodCountForName(beanInterfaceClazz, methodName) > 0) {
                /* 138 */
                isInterfaceMethod = true;
                /* 139 */
                break;
                 
            }
             
        }
         
        /* 143 */
        if (isInterfaceMethod) {
            /* 144 */
            Set methods = ServiceMethodSelector.selectMethods(beanClass, new ReflectionUtils.MethodFilter() {
                public boolean matches(Method method) {
                    return getMappingForMethod(method, beanClass,methodName) != null;
                }
            });
            if (methods.size() > 0)
               return ((Method[]) methods.toArray(new Method[methods.size()]))[0];
        }
        else {
            logger.error(methodName + "不是接口方法");
        }
         
        /* 156 */
        return null;
         
    }

     
     
    private Object doInvokeMethod(Map<String, List<Object>> params, Object bean, Method method, PubContext pubContext) throws Exception
      {
        /* 161 */
        Object result = null;
         
        /* 164 */
        if (method != null) {
            /* 165 */
            InvocableSeriveMethod serviceMethod = new InvocableSeriveMethod(bean, method);
             
            /* 167 */
            result = serviceMethod.invokeForRequest(params, new Object[]{pubContext});
             
            /* 169 */
            ResultMessage resultMessage = (ResultMessage) method.getAnnotation(ResultMessage.class);
             
            /* 171 */
            if (resultMessage != null)
                  {
                /* 173 */
                if ((result != null) && (Message.class.isAssignableFrom(result.getClass()))) {
                    /* 174 */
                    return result;
                     
                }
                /* 176 */
                String code = resultMessage.code();
                /* 177 */
                String info = resultMessage.info();
                /* 178 */
                if ((info == null) || ("".equals(info))) {
                    /* 179 */
                    info = result.toString();
                     
                }
                /* 181 */
                Message message = new Message(code, info);
                /* 182 */
                return message;
                 
            }
             
            /* 185 */
            EsbServiceMapping mapping = (EsbServiceMapping) method.getAnnotation(EsbServiceMapping.class);
             
            try
                  {
                /* 188 */
                if (result != null)
                    /* 189 */ focreInitializeProperties(result, mapping.initializeProperties());
                 
            }
              catch (Exception e) {
                /* 192 */
                logger.warn("强制加载延迟数据失败.");
                 
            }
             
             
        }
         
        /* 197 */
        return result;
         
    }

     
     
    private void focreInitializeProperties(Object result, InitializeProperty[] initializeProperties)
      {
        /* 208 */
        if ((initializeProperties != null) && (initializeProperties.length > 0))
              {
            /* 210 */
            Class resultType = result.getClass();
            /* 211 */
            List entityList = null;
            /* 212 */
            if (List.class.isAssignableFrom(resultType))
                  {
                /* 214 */
                entityList = (List) result;
                /* 215 */
            } else if (PagerRecords.class.isAssignableFrom(resultType))
                  {
                /* 217 */
                entityList = ((PagerRecords) result).getRecords();
                /* 218 */
            } else if (Domain.class.isAssignableFrom(resultType))
                  {
                /* 220 */
                entityList = new ArrayList();
                /* 221 */
                entityList.add(result);
                 
            }
            /* 223 */
            if (entityList != null)
                /* 224 */ for (Iterator localIterator = entityList.iterator(); localIterator.hasNext(); ) {
                Object entity = localIterator.next();
                /* 225 */
                focreDomainInitializeProperties(entity, initializeProperties);
                 
            }
             
        }
         
    }
     
     
    private void focreDomainInitializeProperties(Object entity, InitializeProperty[] initializeProperties)
      {
        /* 236 */
        for (InitializeProperty property : initializeProperties) {
            Object result = PropertyUtils.getSimplePropertyValue(entity, property.value());
        }
         
    }

     
     
    private Class<?> getTargetClass(Object bean)
      {
        /* 245 */
        return AopUtils.getTargetClass(bean);
         
    }

     
     
    private Method getMappingForMethod(Method method, Class<?> beanClass, String methodName)
      {
          if("getNo".equals(method.getName())){
              logger.info("method:"+method+",methodName:"+methodName+"，method.getName:"+method.getName()+"，00000:"+method.getAnnotation(EsbServiceMapping.class));
          }
        if (methodName.equals(method.getName()) || methodName==method.getName()) {
            return method;
        }
        return null;
         
    }
     
}

/* Location:           D:\app\apache-maven-3.6.3.cps\resp\com\gsoft\framework\gsoft-framework-esb\1.0.4-SNAPSHOT\gsoft-framework-esb-1.0.4-SNAPSHOT.jar
 * Qualified Name:     com.gsoft.framework.esb.invoke.ServiceInvoker
 * JD-Core Version:    0.6.0
 */