package com.ruoyi.common.exception.user;

/**
 * 用户密码不正确或不符合规范异常类
 * 
 * @author ruoyi
 */
public class UserPasswordNotPaternMatchException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserPasswordNotPaternMatchException()
    {
        super("user.password.not.pattern", null);
    }
}
