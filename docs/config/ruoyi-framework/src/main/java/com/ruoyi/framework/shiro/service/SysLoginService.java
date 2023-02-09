package com.ruoyi.framework.shiro.service;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.user.*;
import com.ruoyi.common.utils.*;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
@Configuration
public class SysLoginService {

    @Value("${shiro.user.telEnabled}")
    private boolean telEnabled;

    @Value("${shiro.user.isTest}")
    private boolean isTest;

    @Value("${user.telsmscode.maxRetryCount}")
    private String maxRetryCount;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 登录
     */
    public OgUser login(String username, String password, String sms) {
        // 验证码校验
        if (ShiroConstants.CAPTCHA_ERROR.equals(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("dbType",dbType);
        OgUser ogUserTelValid = ogUserService.selectUserTelValidByUsername(map);
        /**
         * 查询短信验证码信息，如果为空直接抛出异常中断
         * 如果超过一天有效时间直接抛出异常，这种情况只能重新获取
         * 如果超过最大重试次数直接抛出异常
         * 如果在重试次数之内记录到数据库并抛出异常
         * 如果在最大重试次数之内验证成功，则重置错误次数为0，并流程往下执行校验用户名密码
         */
        if (!isTest && telEnabled) {
            String smscode = "";
            //获取具体信息，判断是否超过错误次数
            if (null != ogUserTelValid) {
                smscode = ogUserTelValid.getSmsCode();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
                throw new UserNotExistsException();
            }
            if (!"".equals(smscode)) {
                String currtTime = ogUserTelValid.getCurrtTime();
                Long lastTime = ogUserTelValid.getLastTime();
                Long time = Long.valueOf(currtTime) - lastTime;
                Long oneDay = 24 * 60 * 60 * 1000L;
                if (time > oneDay || !smscode.equals(sms)) {
                    String errorMsgKey = "user.sms.retry.limit.exceed";
                    // 判断短信验证码如果超过24小时，则抛出异常提示超时
                    if(time > oneDay){
                        errorMsgKey = "user.sms.retry.limit.timeout";
                    }

                    //  短信验证码错误需要记录到数据库
                    Long num = Long.parseLong(StringUtils.isEmpty(ogUserTelValid.getlCount()) ? "0" : ogUserTelValid.getlCount());
                    if (num >= new Long(maxRetryCount) &&  new Long(maxRetryCount) > 0) {
                        num = new Long(maxRetryCount);
                        ogUserTelValid.setlCount(String.valueOf(num));
                        // 错误次数超过重试次数直接抛出异常中断
                        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.sms.not.pattern.limit.exceed")));
                        throw new UserSmsRetryLimitExceedException(Integer.parseInt(maxRetryCount));
                    } else {
                        ogUserTelValid.setlCount(String.valueOf(num + 1));
                        ogUserService.updateOgUserTelValidNum(ogUserTelValid);
                    }

                   AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message(errorMsgKey)));
                   throw new UserSmsNotPaternMatchException(errorMsgKey);
                } else {
                    // 重试次数之内验证码验证成功将次数置为0
                    ogUserTelValid.setlCount("0");
                    ogUserService.updateOgUserTelValidNum(ogUserTelValid);
                }
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.sms.retry.limit.exceed")));
                throw new UserSmsNotPaternMatchException();
            }
        }
        // 查询用户信息
        Map<String,Object> map1 = new HashMap<>();
        map1.put("username",username);
        map1.put("dbType",dbType);
        OgUser ogUser = ogUserService.selectUserByLoginName(map1);

        if (UserStatus.DELETED.getCode().equals(ogUser.getDelFlag())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete")));
            throw new UserDeleteException();
        }

        if (UserStatus.DISABLE.getCode().equals(ogUser.getStatus())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked")));
            throw new UserBlockedException();
        }

        // 登录校验用户信息是否禁用
        if (!"1".equals(ogUser.getInvalidationMark())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked")));
            throw new UserBlockedException();
        }

        // 查询人员信息，如果用户启用状态，人员禁用则不允许登录
        OgPerson person = ogPersonService.selectOgPersonEvoById(ogUser.getUserId());
        if(!"1".equals(person.getInvalidationMark())){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("person.blocked")));
            throw new PersonBlockedException();
        }

        //内存验证，如果失败，直接计入数据库
        passwordService.validate(ogUser, password);

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        return ogUser;
    }

    /**
     * app的登录方法
     */
    public OgUser appLogin(String username){
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("dbType",dbType);
        OgUser ogUser = ogUserService.selectUserByLoginName(map);
        // 登录校验用户信息是否禁用
        if (!"1".equals(ogUser.getInvalidationMark())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked")));
            throw new UserBlockedException();
        }

        // 查询人员信息，如果用户启用状态，人员禁用则不允许登录
        OgPerson person = ogPersonService.selectOgPersonEvoById(ogUser.getUserId());
        if(!"1".equals(person.getInvalidationMark())){
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("person.blocked")));
            throw new PersonBlockedException();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        return ogUser;
    }
}
