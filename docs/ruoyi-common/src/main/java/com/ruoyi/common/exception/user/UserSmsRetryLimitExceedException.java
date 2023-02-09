package com.ruoyi.common.exception.user;

/**
 * 用户错误最大次数异常类
 * 
 * @author ruoyi
 */
public class UserSmsRetryLimitExceedException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserSmsRetryLimitExceedException(int retryLimitCount)
    {
        super("user.sms.not.pattern.limit.exceed", new Object[] { retryLimitCount });
    }
}
