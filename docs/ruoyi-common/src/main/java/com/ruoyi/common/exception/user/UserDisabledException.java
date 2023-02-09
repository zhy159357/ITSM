package com.ruoyi.common.exception.user;

import org.apache.shiro.authc.AuthenticationException;

public class UserDisabledException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserDisabledException()
    {
        super("user.password.delete", null);
    }
}
