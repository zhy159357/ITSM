package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 应用系统表 system_app
 * 
 * @author dayong_sun
 */
public class SysAppUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 应用系统ID */
    @Excel(name = "序号", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 应用系统名称 */
    @Excel(name = "应用系统名称")
    private String guid;

    /** 角色排序 */
    @Excel(name = "系统id", cellType = ColumnType.NUMERIC)
    private String appId;

    /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限） */
    @Excel(name = "用户id")
    private String userId;

    @Excel(name = "排序")
    private String sysorder;

    /** 删除标识 */
    @Excel(name = "删除标识", cellType = ColumnType.NUMERIC)
    private Long delFlag;

    public SysAppUser(){}

    public boolean isAdmin()
    {
        return isAdmin(this.id);
    }

    public SysAppUser(Long id)
    {
        this.id = id;
    }

    public static boolean isAdmin(Long id)
    {
        return id != null && 1L == id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSysorder() {
        return sysorder;
    }

    public void setSysorder(String sysorder) {
        this.sysorder = sysorder;
    }

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("guid", getGuid())
            .append("appId", getAppId())
            .append("userId", getUserId())
            .append("sysorder", getSysorder())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
