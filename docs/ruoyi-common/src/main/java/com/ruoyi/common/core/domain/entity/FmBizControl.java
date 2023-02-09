package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业务事件单监控对象 fm_biz_control
 *
 * @author ruoyi
 * @date 2021-01-11
 */
public class FmBizControl extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String controlId;

    /**
     * 业务事件单id
     */
    private String fmBizId;
    @Excel(name = "事件编号", sort = 1)
    private String fmNo;
    @Excel(name = "事件标题", sort = 2)
    private String faultDecs;
    /**
     * 所属应用系统
     */
    private String sysId;
    @Excel(name = "所属应用系统", sort = 3)
    private String sysName;
    /**
     * 所属工作组
     */
    private String groupId;
    @Excel(name = "所属工作组", sort = 4)
    private String groupName;
    @Excel(name = "处理方式", sort = 5, readConverterExp = "01=正常处理,02=疑难事件处理,03=多方配合处理,04=数据变更处理,05=差错处理")
    private String fmDealMode;
    @Excel(name = "是否通报", sort = 6, readConverterExp = "1=是")
    private String fmBulletion;
    @Excel(name = "状态", sort = 8, readConverterExp = "01=待提交,02=待分派,03=待处理,04=处理中,05=退回,06=待评价,07=省中心退回,08=待领取(省),09=关闭,10=作废,11=处理中(省)")
    private String fmCurrentState;
    @Excel(name = "异常分类", sort = 7, readConverterExp = "1=领取不及时,2=处理不及时,3=处理不高效,4=解决不及时,5=处理步骤上限,6=紧急处理,7=三天无人领取,8=超72小时未解决,9=长时间未关联问题单")
    private String unusualType;
    @Excel(name = "当前处理人", sort = 9)
    private String dealId;

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getFmNo() {
        return fmNo;
    }

    public void setFmNo(String fmNo) {
        this.fmNo = fmNo;
    }

    public String getFaultDecs() {
        return faultDecs;
    }

    public void setFaultDecs(String faultDecs) {
        this.faultDecs = faultDecs;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFmDealMode() {
        return fmDealMode;
    }

    public void setFmDealMode(String fmDealMode) {
        this.fmDealMode = fmDealMode;
    }

    public String getFmBulletion() {
        return fmBulletion;
    }

    public void setFmBulletion(String fmBulletion) {
        this.fmBulletion = fmBulletion;
    }

    public String getFmCurrentState() {
        return fmCurrentState;
    }

    public void setFmCurrentState(String fmCurrentState) {
        this.fmCurrentState = fmCurrentState;
    }

    /**
     * 统计时间
     */
    @Excel(name = "监控时间", sort = 9)
    private String controlTime;

    /**
     * 关联事件单做为监控展示用
     */
    private FmBiz fmBiz;
    /**
     * 关联系统做为监控展示用
     */
    private OgSys ogSys;
    /**
     * 关联工作组做为监控展示用
     */
    private OgGroup ogGroup;

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getControlId() {
        return controlId;
    }

    public void setFmBizId(String fmBizId) {
        this.fmBizId = fmBizId;
    }

    public String getFmBizId() {
        return fmBizId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setUnusualType(String unusualType) {
        this.unusualType = unusualType;
    }

    public String getUnusualType() {
        return unusualType;
    }

    public void setControlTime(String controlTime) {
        this.controlTime = controlTime;
    }

    public String getControlTime() {
        return controlTime;
    }

    public FmBiz getFmBiz() {
        return fmBiz;
    }

    public void setFmBiz(FmBiz fmBiz) {
        this.fmBiz = fmBiz;
    }

    public OgSys getOgSys() {
        return ogSys;
    }

    public void setOgSys(OgSys ogSys) {
        this.ogSys = ogSys;
    }

    public OgGroup getOgGroup() {
        return ogGroup;
    }

    public void setOgGroup(OgGroup ogGroup) {
        this.ogGroup = ogGroup;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("controlId", getControlId())
                .append("fmBizId", getFmBizId())
                .append("sysId", getSysId())
                .append("groupId", getGroupId())
                .append("unusualType", getUnusualType())
                .append("controlTime", getControlTime())
                .toString();
    }
}
