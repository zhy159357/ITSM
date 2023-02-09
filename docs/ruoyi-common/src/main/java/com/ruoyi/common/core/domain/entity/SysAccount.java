package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 账号表 system_app
 * 
 * @author dayong_sun
 */
public class SysAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @Excel(name = "序号", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 账号名称 */
    @Excel(name = "账号名称")
    private String accountName;

    /** 用户id */
    @Excel(name = "用户id", cellType = ColumnType.NUMERIC)
    private Long userId;

    /** 岗位id */
    @Excel(name = "岗位id", cellType = ColumnType.NUMERIC)
    private Long postId;

    /** 用户是否存在此角色标识 默认不存在 */
    private boolean flag = false;

    /** 状态 */
    @Excel(name = "状态", cellType = ColumnType.NUMERIC)
    private  Long status;

    /** 邮箱 */
    @Excel(name = "用户名称")
    private String userName;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phonenumber;

    /** 所属机构 */
    @Excel(name = "所属机构")
    private String deptName;

    /** ip起 */
    @Excel(name = "ip起")
    private String ipStart;

    /** ip止 */
    @Excel(name = "ip止")
    private String ipEnd;

    /** 岗位编码 */
    @Excel(name = "岗位编码")
    private String postCode;

    /** 岗位名称 */
    @Excel(name = "岗位名称")
    private String postName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getIpStart() {
        return ipStart;
    }

    public void setIpStart(String ipStart) {
        this.ipStart = ipStart;
    }

    public String getIpEnd() {
        return ipEnd;
    }

    public void setIpEnd(String ipEnd) {
        this.ipEnd = ipEnd;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("accountName", getAccountName())
            .append("userId", getUserId())
            .append("postId", getPostId())
            .append("status",getStatus())
            .toString();
    }
}
