package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 am_biz_para_value
 * 
 * @author ruoyi
 * @date 2021-01-19
 */
public class AmBizParaValue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    //主键id
    private String amParaValueId;

    //接收范围id
    private String amParaId;

    //接收机构
    private String receiveDept;

    //接收工作组
    private String receiveGroup;

    //接收机构id
    private String receivedeptid;

    //接收工作组id
    private String  receivegroupid;

    //接收机构名称
    private String orgName;

    //接收工作组名称
    private String grpName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getReceivegroupid() {
        return receivegroupid;
    }

    public void setReceivegroupid(String receivegroupid) {
        this.receivegroupid = receivegroupid;
    }

    public String getAmParaValueId() {
        return amParaValueId;
    }

    public void setAmParaValueId(String amParaValueId) {
        this.amParaValueId = amParaValueId;
    }

    public String getAmParaId() {
        return amParaId;
    }

    public void setAmParaId(String amParaId) {
        this.amParaId = amParaId;
    }

    public String getReceiveDept() {
        return receiveDept;
    }

    public void setReceiveDept(String receiveDept) {
        this.receiveDept = receiveDept;
    }

    public String getReceiveGroup() {
        return receiveGroup;
    }

    public void setReceiveGroup(String receiveGroup) {
        this.receiveGroup = receiveGroup;
    }

    public String getReceivedeptid() {
        return receivedeptid;
    }

    public void setReceivedeptid(String receivedeptid) {
        this.receivedeptid = receivedeptid;
    }

    @Override
    public String toString() {
        return "AmBizParaValue{" +
                "amParaValueId='" + amParaValueId + '\'' +
                ", amParaId='" + amParaId + '\'' +
                ", receiveDept='" + receiveDept + '\'' +
                ", receiveGroup='" + receiveGroup + '\'' +
                ", receivedeptid='" + receivedeptid + '\'' +
                '}';
    }
}
