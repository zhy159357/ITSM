package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户信息表
 */
public class CustInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String cId;

    /**
     * 客户姓名
     */
    private String cName;

    /**
     * 客户电话
     */
    private String cPhone;

    /**
     * 客户部门
     */
    private String cDept;

    /**
     * 客户职务
     */
    private String cPost;

    /**
     * 客户地址
     */
    private String cAddress;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 扩展字段
     */
    private String cColumn;



    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcPhone() {
        return cPhone;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public String getcDept() {
        return cDept;
    }

    public void setcDept(String cDept) {
        this.cDept = cDept;
    }

    public String getcPost() {
        return cPost;
    }

    public void setcPost(String cPost) {
        this.cPost = cPost;
    }

    public String getcAddress() {
        return cAddress;
    }

    public void setcAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getcColumn() {
        return cColumn;
    }

    public void setcColumn(String cColumn) {
        this.cColumn = cColumn;
    }
}
