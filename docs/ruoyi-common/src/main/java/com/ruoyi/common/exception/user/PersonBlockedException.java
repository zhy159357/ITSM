package com.ruoyi.common.exception.user;

public class PersonBlockedException extends UserException {

    private static final long serialVersionUID = 1L;

    public PersonBlockedException()
    {
        super("person.blocked", null);
    }
}
