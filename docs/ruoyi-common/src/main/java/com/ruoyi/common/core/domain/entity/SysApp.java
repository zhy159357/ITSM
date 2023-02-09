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
public class SysApp extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 应用系统ID
     */
    @Excel(name = "系统序号", cellType = ColumnType.STRING)
    private String id;

    /**
     * 应用系统名称
     */
    @Excel(name = "应用系统名称")
    private String sysname;

    /**
     * 应用系统url
     */
    @Excel(name = "系统url")
    private String sysurl;

    /**
     * 角色排序
     */
    @Excel(name = "系统排序", cellType = ColumnType.NUMERIC)
    private Long sysorder;

    /**
     * 角色排序
     */
    @Excel(name = "系统排序", cellType = ColumnType.NUMERIC)
    private Long appSort;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限）
     */
    @Excel(name = "系统编码")
    private String syscode;

    @Excel(name = "参数")
    private String param;

    /**
     * 系统状态（0正常 1停用）
     */
    @Excel(name = "系统状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标识
     */
    @Excel(name = "删除标识", cellType = ColumnType.NUMERIC)
    private Long delFlag;
    //当前sysid
    private String appid;
    //所选角色id
    private String roleid;
    private String rolename;

    private String csscode;
    private String syncFlag;

    public String getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(String syncFlag) {
        this.syncFlag = syncFlag;
    }

    public String getCsscode() {
        return csscode;
    }

    public void setCsscode(String csscode) {
        this.csscode = csscode;
    }

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    private boolean flag = false;

    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public SysApp() {
    }

    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public SysApp(String id) {
        this.id = id;
    }

    public static boolean isAdmin(String id) {
        return id != null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSysname() {
        return sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getSysurl() {
        return sysurl;
    }

    public void setSysurl(String sysurl) {
        this.sysurl = sysurl;
    }

    public Long getSysorder() {
        return sysorder;
    }

    public void setSysorder(Long sysorder) {
        this.sysorder = sysorder;
    }

    public Long getAppSort() {
        return appSort;
    }

    public void setAppSort(Long appSort) {
        this.appSort = appSort;
    }

    public String getSyscode() {
        return syscode;
    }

    public void setSyscode(String syscode) {
        this.syscode = syscode;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Long getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("syscode", getSyscode())
                .append("sysname", getSysname())
                .append("sysUrl", getSysurl())
                .append("sysOrder", getSysorder())
                .append("appSort", getAppSort())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .append("param", getParam())
                .append("status", getStatus())
                .toString();
    }
}
