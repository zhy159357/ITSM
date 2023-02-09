package com.ruoyi.common.core.domain.entity;

import java.io.Serializable;


/**
 * 短信对象
 *
 * @author ruoyi
 * @date 2020-12-08
 */
public class SmsObj implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private SmsPreObj pbp;
    private String number;//序号
    private String telephone;//电话
    private String name;//名称
    private String smstext;//短信内容
    private String smstime;//短信发送时间
    private String state;//短信状态
    private String createid;//创建人
    private OgPerson ogPerson;//创建人
    private String updatetime;//更新时间
    private String mark;//有效标志

    public OgPerson getOgPerson() {
        return ogPerson;
    }

    public void setOgPerson(OgPerson ogPerson) {
        this.ogPerson = ogPerson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SmsPreObj getPbp() {
        return pbp;
    }

    public void setPbp(SmsPreObj pbp) {
        this.pbp = pbp;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getSmstime() {
        return smstime;
    }

    public void setSmstime(String smstime) {
        this.smstime = smstime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
