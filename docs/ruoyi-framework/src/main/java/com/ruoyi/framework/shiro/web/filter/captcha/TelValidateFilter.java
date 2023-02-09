package com.ruoyi.framework.shiro.web.filter.captcha;

import com.google.code.kaptcha.Constants;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 验证码过滤器
 *
 * @author ruoyi
 */
public class TelValidateFilter extends AccessControlFilter {
    /**
     * 是否开启验证码
     */
    private boolean telEnabled = true;
    private String smsnode;
    private String smshost;
    private int smsport;

    public void setTelEnabled(boolean telEnabled) {
        this.telEnabled = telEnabled;
    }

    public void setSmshost(String smshost) {
        this.smshost = smshost;
    }

    public void setSmsport(int smsport) {
        this.smsport = smsport;
    }

    public void setSmsnode(String smsnode) {
        this.smsnode = smsnode;
    }


    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        request.setAttribute(ShiroConstants.TEL_CURRENT_ENABLED, telEnabled);
        return super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        return true;
    }

    public boolean validateResponse(HttpServletRequest request, String validateCode) {
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        request.setAttribute(ShiroConstants.TEL_CURRENT_ENABLED, ShiroConstants.CAPTCHA_ERROR);
        return true;
    }
}
