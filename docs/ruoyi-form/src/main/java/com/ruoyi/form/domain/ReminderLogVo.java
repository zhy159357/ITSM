package com.ruoyi.form.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.ToString;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@ToString
public class ReminderLogVo {

    private Long id;// id

    private String type;// 类型  0-流程日志；2-催单日志；1-客户信息（特定后加的）

    private String workNo;// 处理人

    private String dealDepartment;// 处理部门

    private Long dealTime;// 处理时间

    private String dealTimeStr;// 处理时间字符串

    private String content;// 描述

    public ReminderLogVo() {

    }

    public ReminderLogVo(String type, String workNo, String dealDepartment, String dealTimeStr, String content) {
        this.type = type;
        this.workNo = workNo;
        this.dealDepartment = dealDepartment;
        this.dealTimeStr = dealTimeStr;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getDealDepartment() {
        return dealDepartment;
    }

    public void setDealDepartment(String dealDepartment) {
        this.dealDepartment = dealDepartment;
    }

    public Long getDealTime() {
        return dealTime;
    }

    public void setDealTime(Long dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealTimeStr() {
        return dealTimeStr;
    }

    public void setDealTimeStr(String dealTimeStr) {
        this.dealTimeStr = dealTimeStr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
