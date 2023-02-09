package com.ruoyi.system.domain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 系统名称对象 sys_notify
 *
 * @author zx
 * @date 2021-08-26
 */
public class SysNotify extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 角色ID */
    @Excel(name = "角色ID")
    private String roleId;

    /** 通知类型 */
    @Excel(name = "通知类型")
    private String type;

    /** 是否推送 */
    @Excel(name = "是否推送")
    private String isPush;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String creatTime;

    /** 通知内容 */
    @Excel(name = "通知内容")
    private String text;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleId()
    {
        return roleId;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setIsPush(String isPush)
    {
        this.isPush = isPush;
    }

    public String getIsPush()
    {
        return isPush;
    }
    public void setCreatTime(String creatTime)
    {
        this.creatTime = creatTime;
    }

    public String getCreatTime()
    {
        return creatTime;
    }
    public void setText(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("roleId", getRoleId())
                .append("type", getType())
                .append("isPush", getIsPush())
                .append("creatTime", getCreatTime())
                .append("text", getText())
                .toString();
    }
}
