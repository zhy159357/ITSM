package com.ruoyi.common.core.domain.entity;

/**
 * 统一监控门户 输出参数
 */
public class UnMonitorResult {

    private Integer status;

    private String msg;

    private String account;

    /**
     * 返回
     */
    public static final String set_status_200 = "200";
    public static final String set_status_400 = "400";
    public static final String set_status_401 = "401";
    public static final String set_status_1 = "-1";




    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }




}
