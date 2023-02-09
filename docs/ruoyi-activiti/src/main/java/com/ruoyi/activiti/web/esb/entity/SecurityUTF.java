package com.ruoyi.activiti.web.esb.entity;

public class SecurityUTF
{
    private String sysCode;
    private String token;

    public static SecurityUTF newSecurityUTF(String sysCode, String token)
    {
        SecurityUTF securityUTF = new SecurityUTF();
        securityUTF.setSysCode(sysCode);
        securityUTF.setToken(token);
        return securityUTF;
    }

    public String getSysCode()
    {
        return this.sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
