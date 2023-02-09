package com.ruoyi.framework.shiro.service;

import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;

import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5OriginalUtils;

import com.ruoyi.system.service.IOgUserService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.exception.user.UserPasswordRetryLimitExceedException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;

/**
 * 登录密码方法
 *
 * @author ruoyi
 */
@Component
public class SysPasswordService {
    @Autowired
    private CacheManager cacheManager;

    private Cache<String, AtomicInteger> loginRecordCache;

    @Value(value = "${user.password.maxRetryCount}")
    private String maxRetryCount;

    @Autowired
    private IOgUserService ogUserService;

    @PostConstruct
    public void init() {
        loginRecordCache = cacheManager.getCache(ShiroConstants.LOGINRECORDCACHE);
    }

    public void validate(OgUser user, String password) {
        String loginName = user.getUsername();
        //验证数据库中密码错误次数
        int pNotCount = 0;
        String lockTime = "";
        String currtTime = "";
        boolean setlocktime = false;
        if (user != null) {
            pNotCount = user.getpNotCount() == null ? 0 : Integer.parseInt(user.getpNotCount());
            lockTime = user.getLockTime();
            currtTime = user.getCurrtTime();
            if (!"0".equals(lockTime) && StringUtils.isNotEmpty(lockTime) && Long.parseLong(currtTime) - Long.parseLong(lockTime) > 1000) {
                //异常超过时间，进行恢复
                setlocktime = true;
                user.setpNotCount("0");
                user.setLastTime(0L);
                pNotCount = 0;
                ogUserService.updateOgUserPwdLockTime(user);
            }
        }
        AtomicInteger retryCount = loginRecordCache.get(loginName);

        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            loginRecordCache.put(loginName, retryCount);
        }
        // 超过错误次数进行锁定
        if (pNotCount >= Integer.valueOf(maxRetryCount).intValue()) {
            if (!setlocktime && StringUtils.isEmpty(lockTime)) {
                ogUserService.updateOgUserPwdLockTime(user);
            }
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount)));
            throw new UserPasswordRetryLimitExceedException(Integer.valueOf(maxRetryCount).intValue());
        }
//        if (retryCount.incrementAndGet() > Integer.valueOf(maxRetryCount).intValue()) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount)));
//            throw new UserPasswordRetryLimitExceedException(Integer.valueOf(maxRetryCount).intValue());
//        }

        if (!matches(user, password)) {
            //失败，更新数据库以前的错误数据
            user.setpNotCount(String.valueOf(pNotCount + 1));
            ogUserService.updateOgUserPwdLock(user);
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.count", retryCount)));
            loginRecordCache.put(loginName, retryCount);
            throw new UserPasswordNotMatchException();
        } else {
            //没有失败，更新数据库以前的错误数据为正常
            OgUser unOgUser = new OgUser();
            unOgUser.setUserId(user.getUserId());
            ogUserService.updateOgUserPwdUnLock(unOgUser);
            clearLoginRecordCache(loginName);
        }
    }

    public boolean matches(OgUser user, String newPassword) {
        String s = encryptPassword(user.getUsername(), newPassword, user.getUserId());
        return user.getPassword().equals(s);
    }

    public void clearLoginRecordCache(String loginName) {
        loginRecordCache.remove(loginName);
    }

    public String encryptPassword(String loginName, String password, String salt) {
//        return new Md5Hash(loginName + password + salt).toHex();
        //gg 修改密码加密方式，兼容老平台数据
        return Md5OriginalUtils.md5Password(password);
    }
}
