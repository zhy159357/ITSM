package com.ruoyi.common.exception;

/**
 * 业务异常
 * 
 * @author ruoyi
 */
public class BusinessException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private String code;
    protected final String message;

    public BusinessException(String message)
    {
        this.message = message;
    }

    public BusinessException(String code,String message)
    {
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message, Throwable e)
    {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
