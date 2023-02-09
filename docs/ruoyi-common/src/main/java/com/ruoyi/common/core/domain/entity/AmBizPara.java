package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 am_biz_para
 * 
 * @author ruoyi
 * @date 2021-01-19
 */
public class AmBizPara extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    private String amParaId;


    private String receiveRange;

    public void setAmParaId(String amParaId) 
    {
        this.amParaId = amParaId;
    }

    public String getAmParaId() 
    {
        return amParaId;
    }
    public void setReceiveRange(String receiveRange) 
    {
        this.receiveRange = receiveRange;
    }

    public String getReceiveRange() 
    {
        return receiveRange;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("amParaId", getAmParaId())
            .append("receiveRange", getReceiveRange())
            .toString();
    }
}
