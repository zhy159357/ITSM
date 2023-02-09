package com.ruoyi.framework.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.system.service.IOgUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName CustomerBizInterceptor
 * @Description 自定义功能模块拦截器
 * @Author JiaQi Zhang
 * @Date 2022/7/20 14:58
 */
@Component
@Slf4j
public class CustomerBizInterceptor extends HandlerInterceptorAdapter {
    private static final String currentUserKey="CurrentUserId";

    @Resource
    IOgUserService ogUserService;

    public static ThreadLocal<OgUser> currentUserThread=new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("自定义功能业务模块拦截器开始拦截.......");
        String currentUserId = request.getHeader(currentUserKey);
        log.info("自定义功能业务模块获取请求头中的当前用户ID值.......: "+currentUserId);
        OgUser ogUser = ogUserService.selectOgUserByUserId(currentUserId);
        if (ObjectUtil.isNull(ogUser)){
            throw  new BusinessException("非法途径请求");
            //return false;
        }
        currentUserThread.set(ogUser);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        CustomerBizInterceptor.currentUserThread.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
