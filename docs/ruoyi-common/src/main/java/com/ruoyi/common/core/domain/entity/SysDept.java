package com.ruoyi.common.core.domain.entity;

import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 部门表 sys_dept
 * 
 * @author ruoyi
 */
public class SysDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    private Long deptId;

    /** 父部门ID */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 部门名称 */
    private String deptName;

    /** 显示顺序 */
    private String orderNum;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 部门状态:0正常,1停用 */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 父部门名称 */
    private String parentName;

    /**
     * 机构等级
     *
     */
    private String deptlevel;

    /**
     *总部标记
     */
    private String branchMark;

    /**
     * 内部标记
     */
    private String inoutsideMark;

    /**
     * 厂商类型
     */
    private String type;

    /**
     * 运维总人数
     */
    private String operaterNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 机构层级
     */
    private String tier;

    /**
     * 部门编码
     */
    private String deptCode;


    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    //@NotBlank(message = "显示顺序不能为空")
    public String getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(String orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getLeader()
    {
        return leader;
    }

    public void setLeader(String leader)
    {
        this.leader = leader;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getDeptlevel() {
        return deptlevel;
    }

    public void setDeptlevel(String deptlevel) {
        this.deptlevel = deptlevel;
    }

    public String getBranchMark() {
        return branchMark;
    }

    public void setBranchMark(String branchMark) {
        this.branchMark = branchMark;
    }

    public String getInoutsideMark() {
        return inoutsideMark;
    }

    public void setInoutsideMark(String inoutsideMark) {
        this.inoutsideMark = inoutsideMark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperaterNum() {
        return operaterNum;
    }

    public void setOperaterNum(String operaterNum) {
        this.operaterNum = operaterNum;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    @Override
    public String toString() {
        return "SysDept{" +
                "deptId=" + deptId +
                ", parentId=" + parentId +
                ", ancestors='" + ancestors + '\'' +
                ", deptName='" + deptName + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", leader='" + leader + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", parentName='" + parentName + '\'' +
                ", deptlevel='" + deptlevel + '\'' +
                ", branchMark='" + branchMark + '\'' +
                ", inoutsideMark='" + inoutsideMark + '\'' +
                ", type='" + type + '\'' +
                ", operaterNum='" + operaterNum + '\'' +
                ", remark='" + remark + '\'' +
                ", tier='" + tier + '\'' +
                '}';
    }
}
