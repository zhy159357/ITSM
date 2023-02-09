package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 关联关系对象 pub_relation
 * 
 * @author zx
 * @date 2021-01-25
 */
    public class PubRelation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String relationId;

    /** 对象1ID */
    @Excel(name = "对象1ID")
    private String obj1Id;

    /** 对象2ID */
    @Excel(name = "对象2ID")
    private String obj2Id;

    /** 关系分类：01事件-问题，02事件-数据变更，03业务事件-综合变更，04问题-综合变更，05问题-版本，06计划（任务）-公告，07事务事件-综合变更，08监控事件-综合变更，09故障事件-综合变更，10调度事件-综合变更,11计划-综合变更单 12业务-止付；注：根据上述五种对用关系，对象1和对象2正向排列，方便查询 */
    @Excel(name = "关系分类：01事件-问题，02事件-数据变更，03业务事件-综合变更，04问题-综合变更，05问题-版本，06计划", readConverterExp = "任=务")
    private String type;

    /** 挂起任务，用于标记业务事件单挂起的任务 */
    @Excel(name = "挂起任务，用于标记业务事件单挂起的任务")
    private String hangupTask;

    public void setRelationId(String relationId) 
    {
        this.relationId = relationId;
    }

    public String getRelationId() 
    {
        return relationId;
    }
    public void setObj1Id(String obj1Id) 
    {
        this.obj1Id = obj1Id;
    }

    public String getObj1Id() 
    {
        return obj1Id;
    }
    public void setObj2Id(String obj2Id) 
    {
        this.obj2Id = obj2Id;
    }

    public String getObj2Id() 
    {
        return obj2Id;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setHangupTask(String hangupTask) 
    {
        this.hangupTask = hangupTask;
    }

    public String getHangupTask() 
    {
        return hangupTask;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("relationId", getRelationId())
            .append("obj1Id", getObj1Id())
            .append("obj2Id", getObj2Id())
            .append("type", getType())
            .append("hangupTask", getHangupTask())
            .toString();
    }
}
