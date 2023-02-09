package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

public class OgPost {

    @Excel(name = "岗位代码")
    private String postId;

    @Excel(name = "岗位名称")
    private String postName;

    private String addEr;//添加人

    private String addTime;//添加时间

    private String moder;

    private String updateTime;//修改时间

    @Excel(name = "备注")
    private String memo;

    @Excel(name = "岗位顺序")
    private String serialNum;

    @Excel(name = "状态", readConverterExp = "0=停用,1=启用")
    private String invalidationMark;

    /** 权限标识 */
    private String powerFlag;


    private String roleId;// 角色id

    private String userId;// 角色id

    private Map<String, Object> params;//请求参数


    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }




    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getAddEr() {
        return addEr;
    }

    public void setAddEr(String addEr) {
        this.addEr = addEr;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getModer() {
        return moder;
    }

    public void setModer(String moder) {
        this.moder = moder;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPowerFlag() {
        return powerFlag;
    }

    public void setPowerFlag(String powerFlag) {
        this.powerFlag = powerFlag;
    }
}
