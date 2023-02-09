package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 用户考核对象 user_task_test
 * 
 * @author ruoyi
 * @date 2022-01-13
 */
public class UserTaskTest extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;
    @Excel(name="用户名")
    /** $column.columnComment */
    private String userName;

    /** $column.columnComment */
    private String userid;
    @Excel(name="已完成")
    /** $column.columnComment */
    private String comeplete;
    @Excel(name="未完成")
    /** $column.columnComment */
    private String uncomeplete;
    @Excel(name="超时")
    /** $column.columnComment */
    private String timeout;
    @Excel(name="类型")
    /** $column.columnComment */
    private String tasktype;

    /** $column.columnComment */
    private Date time;
    @Excel(name="机构")
    private String orgName;
    public void setOrgName(String orgName){this.orgName =orgName;}
    public String getOrgName(){
        return orgName;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setUserid(String userid) 
    {
        this.userid = userid;
    }

    public String getUserid() 
    {
        return userid;
    }
    public void setComeplete(String comeplete) 
    {
        this.comeplete = comeplete;
    }

    public String getComeplete() 
    {
        return comeplete;
    }
    public void setUncomeplete(String uncomeplete) 
    {
        this.uncomeplete = uncomeplete;
    }

    public String getUncomeplete() 
    {
        return uncomeplete;
    }
    public void setTimeout(String timeout) 
    {
        this.timeout = timeout;
    }

    public String getTimeout() 
    {
        return timeout;
    }
    public void setTasktype(String tasktype) 
    {
        this.tasktype = tasktype;
    }

    public String getTasktype() 
    {
        return tasktype;
    }
    public void setTime(Date time) 
    {
        this.time = time;
    }

    public Date getTime() 
    {
        return time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("user", getUserName())
            .append("userid", getUserid())
            .append("comeplete", getComeplete())
            .append("uncomeplete", getUncomeplete())
            .append("timeout", getTimeout())
            .append("tasktype", getTasktype())
            .append("time", getTime())
            .toString();
    }
}
