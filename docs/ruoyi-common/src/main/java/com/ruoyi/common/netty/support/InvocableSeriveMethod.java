package com.ruoyi.common.netty.support;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InvocableSeriveMethod extends ServiceMethod {
    private ServiceMethodArgumentResolverComposite argumentResolvers = new ServiceMethodArgumentResolverComposite();

    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public InvocableSeriveMethod(Object bean, Method method) {
        super(bean, method);
        this.argumentResolvers.addResolver(new ParamServiceMethodArgumentResolver());
        this.argumentResolvers.addResolver(new DomainServiceMethodArgumentResolver());
        this.argumentResolvers.addResolver(new DomainCollectionServiceMethodArgumentResolver());
        this.argumentResolvers.addResolver(new PagerServiceMethodArgumentResolver());
        this.argumentResolvers.addResolver(new OrdersServiceMethodArgumentResolver());
        this.argumentResolvers.addResolver(new ConditionsServiceMethodArgumentResolver());
        this.argumentResolvers.addResolver(new PubContextServiceMethodArgumentResolver());
        this.argumentResolvers.addResolver(new ReqContextServiceMethodArgumentResolver());
        this.argumentResolvers.addResolver(new MapServiceMethodArgumentResolver());
    }

    public InvocableSeriveMethod(ServiceMethod handlerMethod) {
        super(handlerMethod);
    }

    public InvocableSeriveMethod(Object bean, String methodName, Class<?>[] parameterTypes)
            throws NoSuchMethodException {
        super(bean, methodName, parameterTypes);
    }

    public void setHandlerMethodArgumentResolvers(ServiceMethodArgumentResolverComposite argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    public final Object invokeForRequest(Map<String, List<Object>> params, Object[] providedArgs)
            throws Exception {
        Object[] args = getMethodArgumentValues(params, providedArgs);
        if (this.logger.isTraceEnabled()) {
            StringBuilder builder = new StringBuilder("Invoking [");
            builder.append(getMethod().getName()).append("] method with arguments ");
            builder.append(Arrays.asList(args));
            this.logger.trace(builder.toString());
        }

        Object returnValue = invoke(args);

        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Method [" + getMethod().getName() + "] returned [" + returnValue + "]");
        }
        return returnValue;
    }

    private Object[] getMethodArgumentValues(Map<String, List<Object>> params, Object[] providedArgs)
            throws Exception {
        MethodParameter[] parameters = getMethodParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];
            parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            GenericTypeResolver.resolveParameterType(parameter, getBean().getClass());
            args[i] = resolveProvidedArgument(parameter, providedArgs);
            if (args[i] != null) {
                continue;
            }
            if (this.argumentResolvers.supportsParameter(parameter)) {
                try {
                    args[i] = this.argumentResolvers.resolveArgument(parameter, params, providedArgs);
                } catch (Exception ex) {
                    if (this.logger.isTraceEnabled()) {
                        this.logger.trace(getArgumentResolutionErrorMessage("Error resolving argument", i), ex);
                    }
                    throw ex;
                }
            }
            else if (args[i] == null) {
                String msg = getArgumentResolutionErrorMessage("No suitable resolver for argument", i);
                throw new IllegalStateException(msg);
            }
        }
        return args;
    }

    private String getArgumentResolutionErrorMessage(String message, int index) {
        MethodParameter param = getMethodParameters()[index];
        message = message + " [" + index + "] [type=" + param.getParameterType().getName() + "]";
        return getDetailedErrorMessage(message);
    }

    protected String getDetailedErrorMessage(String message) {
        StringBuilder sb = new StringBuilder(message).append("\n");
        sb.append("HandlerMethod details: \n");
        sb.append("Controller [").append(getBeanType().getName()).append("]\n");
        sb.append("Method [").append(getBridgedMethod().toGenericString()).append("]\n");
        return sb.toString();
    }

    private Object resolveProvidedArgument(MethodParameter parameter, Object[] providedArgs) {
        if (providedArgs == null) {
            return null;
        }
        for (Object providedArg : providedArgs) {
            if (parameter.getParameterType().isInstance(providedArg)) {
                return providedArg;
            }
        }
        return null;
    }

    private Object invoke(Object[] args) throws Exception {
        ReflectionUtils.makeAccessible(getBridgedMethod());
        Throwable targetException;
        String msg;
        try {
            return getBridgedMethod().invoke(getBean(), args);
        } catch (IllegalArgumentException e) {
            msg = getInvocationErrorMessage(e.getMessage(), args);
            throw new IllegalArgumentException(msg, e);
        } catch (InvocationTargetException e) {
            targetException = e.getTargetException();
            if ((targetException instanceof RuntimeException)) {
                throw ((RuntimeException) targetException);
            }
            if ((targetException instanceof Error)) {
                throw ((Error) targetException);
            }
            if ((targetException instanceof Exception)) {
                throw ((Exception) targetException);
            }
            msg = getInvocationErrorMessage("Failed to invoke controller method", args);
        }
        throw new IllegalStateException(msg, targetException);
    }

    private String getInvocationErrorMessage(String message, Object[] resolvedArgs) {
        StringBuilder sb = new StringBuilder(getDetailedErrorMessage(message));
        sb.append("Resolved arguments: \n");
        for (int i = 0; i < resolvedArgs.length; i++) {
            sb.append("[").append(i).append("] ");
            if (resolvedArgs[i] == null) {
                sb.append("[null] \n");
            } else {
                sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
                sb.append("[value=").append(resolvedArgs[i]).append("]\n");
            }
        }
        return sb.toString();
    }
}