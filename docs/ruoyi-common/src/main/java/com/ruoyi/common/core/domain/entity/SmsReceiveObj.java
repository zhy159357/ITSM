package com.ruoyi.common.core.domain.entity;

import java.io.Serializable;


/**
 * 接收短信对象
 *
 * @author ruoyi
 * @date 2020-12-08
 */
public class SmsReceiveObj implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String telephone;//手机号
    private String name;//名称
    private String smstext;//短信内容
    private String sendtime;//发送时间
    private String vfCode;//单号
    private String result;//结果

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmstext() {
        return smstext;
    }

    public void setSmstext(String smstext) {
        this.smstext = smstext;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getVfCode() {
        return vfCode;
    }

    public void setVfCode(String vfCode) {
        this.vfCode = vfCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
