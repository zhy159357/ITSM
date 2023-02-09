package com.ruoyi.framework.interceptor;

import com.ruoyi.common.annotation.AppMobile;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.json.JSON;
import com.ruoyi.common.netty.utils.DESUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * app访问请求拦截器
 */
@Component
public class AppMobileInterceptor extends HandlerInterceptorAdapter {

    @Value("${forward.encryKey}")
    private String encryKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AppMobile annotation = method.getAnnotation(AppMobile.class);
            if (annotation != null) {
                // 验证参数解析错误，则返回失败信息，验证成功放入HttpSession域中
                Map<String, Object> params = convertParam(request);
                if (StringUtils.isEmpty(params)) {
                    AjaxResult ajaxResult = AjaxResult.error("运维管理平台参数解析错误，请检查参数信息是否正确!");
                    String encrypt = DESUtils.encrypt(encryKey, JSON.marshal(ajaxResult));
                    ServletUtils.renderString(response, encrypt);
                    return false;
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("appMobileParams", params);
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    /**
     * 解析加密参数信息
     */
    private Map<String, Object> convertParam(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        String params = ServletUtils.convertParam(request);
        try {
            String decrypt = DESUtils.decrypt(params, encryKey);
            map = com.alibaba.fastjson.JSON.parseObject(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
