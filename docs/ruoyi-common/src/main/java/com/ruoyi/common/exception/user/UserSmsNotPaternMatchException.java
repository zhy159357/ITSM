package com.ruoyi.common.exception.user;

/**
 * 用户密码不正确或不符合规范异常类
 * 
 * @author ruoyi
 */
public class UserSmsNotPaternMatchException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserSmsNotPaternMatchException()
    {
        super("user.sms.retry.limit.exceed", null);
    }

    public UserSmsNotPaternMatchException(String msg)
    {
        super(msg, null);
    }
}
