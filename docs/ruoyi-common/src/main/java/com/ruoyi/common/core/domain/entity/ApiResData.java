package com.ruoyi.common.core.domain.entity;

/**
 * 外部接口返回对象
 */
public class ApiResData {

    public final static String RESCODE_SUCEESS = "0";

    /**
     * 返回码
     */
    public String resCode;
    /**
     * 返回信息
     */
    public String resMsg;

    /**
     * 返回对象
     */
    public String resDataJson;

    public ApiResData() {
    }

    public ApiResData(String resCode) {
        super();
        this.resCode = resCode;
    }

    public ApiResData(String resCode, String resMsg) {
        super();
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    public ApiResData(String resCode, String resMsg, String resDataJson) {
        super();
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.resDataJson = resDataJson;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getResDataJson() {
        return resDataJson;
    }

    public void setResDataJson(String resDataJson) {
        this.resDataJson = resDataJson;
    }

}