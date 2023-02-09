package com.ruoyi.form.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 硬件保障对象 design_biz_chm
 *
 * @author ruoyi
 * @date 2022-11-24
 */
public class DesignBizChm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 实例ID */
    private String instanceId;

    /** 备用字段1 */
    @Excel(name = "单号")
    private String extra1;

    /** 备用字段2 */
    private String extra2;

    /** 备用字段3 */
    private String extra3;

    /** 备用字段4 */
    private String extra4;

    /** 备用字段5 */
    private String extra5;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createdBy;

    /** 更新时间 */
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    /** 更新人 */
    private String updatedBy;

    /** 阶段 */
    private String stage;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 故障类别 */
    @Excel(name = "故障类别" ,readConverterExp ="1=硬件,2=软件")
    private String chmType;

    /** 机具号 */
    @Excel(name = "机具号")
    private String implementNo;

    /** 事件描述 */
    @Excel(name = "事件描述")
    private String faultDescription;

    /** 设备类型(一级) */
    @Excel(name = "设备类型(一级)")
    private String equipmentType;
    /** 设备类型(二级) */
    @Excel(name = "设备类型(二级)")
    private String equipmentName;
    /** 设备类型(三级) */
    @Excel(name = "设备类型(三级)")
    private String equipmentModel;

    /** 目标解决日期 */
    @Excel(name = "目标解决日期")
    private String expectTime;

    /** 是否加急 */
    @Excel(name = "是否加急" ,readConverterExp="0=否,1=是")
    private Long urgent;

    /** 影响范围 */
    @Excel(name = "影响范围" ,readConverterExp="1=单台设备,2=多台设备,3=网点所有设备")
    private String scopeInfluence;

    /** 优先级 */
    private String priority;

    /** 上报人 */
    @Excel(name = "上报人")
    private String contact;

    /** 电话 */
    @Excel(name = "电话")
    private String contactDetails;

    /** 受派组 */
    private String assignedGroup;

    /** 受派人 */
    private String assignedPerson;

    /** 提交日期(合胜开单日期) */
    @Excel(name = "提交日期(厂商开单日期)")
    private String orderTime;

    /** 到达时间 */
    @Excel(name = "到达时间")
    private String arriveTime;

    /** 完成时间 */
    @Excel(name = "完成时间")
    private String completeTime;

    /** 解决方式 */
    @Excel(name = "解决方式",readConverterExp="0=现场,1=远程")
    private String solution;

    /** 故障类型 */
    @Excel(name = "故障类型" ,readConverterExp="1=硬件更换,2=硬件调试,3=位移搬迁,4=项目实施,5=安装开通,6=人为故障,7=软件调试,8=网络通信")
    private String faultType;

    /** 服务评价 */
    @Excel(name = "服务评价",readConverterExp="1=优,2=良,3=中")
    private String serviceEvaluation;

    /** 设备类型(一级) */
    private String equipmentTypeB;

    /** 设备类型(二级) */
    private String equipmentNameB;

    /** 设备类型(三级) */
    private String equipmentModelB;

    /** 取消原因 */
    @Excel(name = "取消原因")
    private String cancleNote;

    /** 附件上传 */
    private String file;
    /** 上报部门: */
    @Excel(name = "上报部门:")
    private String reportDepartment;

    /** $column.columnComment */
    @Excel(name = "来源",readConverterExp="1=移动端,2=PC端")
    private String source;

    /** $column.columnComment */
    private String requestId;

    /** 电话回访 */
    @Excel(name = "电话回访",readConverterExp="0=未回访,1=已回访")
    private String telephoneFollow;

    /** 合胜flowNo
     */
    @Excel(name = "合胜flowNo ")
    private String flowNo;

    /** 设备标识号: */
    @Excel(name = "设备标识号:")
    private String implementId;

    /** 处理过程记录 */
    @Excel(name = "处理过程记录")
    private String dealNotes;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 厂商 */
    @Excel(name = "厂商")
    private String facturer;

    /** 厂商受理时间 */
    @Excel(name = "厂商受理时间")
    private String facturerAcceptTime;

    /** 工程师受理时间 */
    @Excel(name = "工程师受理时间")
    private String engineerAcceptTime;

    /** 工程师到场时间 */
    @Excel(name = "工程师到场时间")
    private String engineerArriveTime;
    /** 任务当前处理人 */
    @Excel(name="当前处理人")
    private String currentHandlerId;
    /**
     * 服务台处理时间
     * @param id
     */
   // @Excel(name="服务台处理时间")
    private String itDealTime;
    /**
     * 服务台处理人
     * @param id
     */
    @Excel(name="服务台处理人")/**/
    private String itDealUser;
    @Excel(name="实际处理人")
    private String realDealUser;
    /** 复核时间 */
    @Excel(name = "复核时间")
    private String fuHeTime;

    public String getFuHeTime() {
        return fuHeTime;
    }

    public void setFuHeTime(String fuHeTime) {
        this.fuHeTime = fuHeTime;
    }

    public String getRealDealUser() {
        return realDealUser;
    }

    public void setRealDealUser(String realDealUser) {
        this.realDealUser = realDealUser;
    }

    public String getItDealTime() {
        return itDealTime;
    }

    public void setItDealTime(String itDealTime) {
        this.itDealTime = itDealTime;
    }

    public String getItDealUser() {
        return itDealUser;
    }

    public void setItDealUser(String itDealUser) {
        this.itDealUser = itDealUser;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    public String getInstanceId()
    {
        return instanceId;
    }
    public void setExtra1(String extra1)
    {
        this.extra1 = extra1;
    }

    public String getExtra1()
    {
        return extra1;
    }
    public void setExtra2(String extra2)
    {
        this.extra2 = extra2;
    }

    public String getExtra2()
    {
        return extra2;
    }
    public void setExtra3(String extra3)
    {
        this.extra3 = extra3;
    }

    public String getExtra3()
    {
        return extra3;
    }
    public void setExtra4(String extra4)
    {
        this.extra4 = extra4;
    }

    public String getExtra4()
    {
        return extra4;
    }
    public void setExtra5(String extra5)
    {
        this.extra5 = extra5;
    }

    public String getExtra5()
    {
        return extra5;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }
    public void setUpdatedTime(Date updatedTime)
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime()
    {
        return updatedTime;
    }
    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy()
    {
        return updatedBy;
    }
    public void setStage(String stage)
    {
        this.stage = stage;
    }

    public String getStage()
    {
        return stage;
    }
    public void setCurrentHandlerId(String currentHandlerId)
    {
        this.currentHandlerId = currentHandlerId;
    }

    public String getCurrentHandlerId()
    {
        return currentHandlerId;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setChmType(String chmType)
    {
        this.chmType = chmType;
    }

    public String getChmType()
    {
        return chmType;
    }
    public void setImplementNo(String implementNo)
    {
        this.implementNo = implementNo;
    }

    public String getImplementNo()
    {
        return implementNo;
    }
    public void setFaultDescription(String faultDescription)
    {
        this.faultDescription = faultDescription;
    }

    public String getFaultDescription()
    {
        return faultDescription;
    }
    public void setEquipmentType(String equipmentType)
    {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentType()
    {
        return equipmentType;
    }
    public void setExpectTime(String expectTime)
    {
        this.expectTime = expectTime;
    }

    public String getExpectTime()
    {
        return expectTime;
    }
    public void setUrgent(Long urgent)
    {
        this.urgent = urgent;
    }

    public Long getUrgent()
    {
        return urgent;
    }
    public void setEquipmentName(String equipmentName)
    {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentName()
    {
        return equipmentName;
    }
    public void setScopeInfluence(String scopeInfluence)
    {
        this.scopeInfluence = scopeInfluence;
    }

    public String getScopeInfluence()
    {
        return scopeInfluence;
    }
    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public String getPriority()
    {
        return priority;
    }
    public void setEquipmentModel(String equipmentModel)
    {
        this.equipmentModel = equipmentModel;
    }

    public String getEquipmentModel()
    {
        return equipmentModel;
    }
    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getContact()
    {
        return contact;
    }
    public void setContactDetails(String contactDetails)
    {
        this.contactDetails = contactDetails;
    }

    public String getContactDetails()
    {
        return contactDetails;
    }
    public void setAssignedGroup(String assignedGroup)
    {
        this.assignedGroup = assignedGroup;
    }

    public String getAssignedGroup()
    {
        return assignedGroup;
    }
    public void setAssignedPerson(String assignedPerson)
    {
        this.assignedPerson = assignedPerson;
    }

    public String getAssignedPerson()
    {
        return assignedPerson;
    }
    public void setOrderTime(String orderTime)
    {
        this.orderTime = orderTime;
    }

    public String getOrderTime()
    {
        return orderTime;
    }
    public void setArriveTime(String arriveTime)
    {
        this.arriveTime = arriveTime;
    }

    public String getArriveTime()
    {
        return arriveTime;
    }
    public void setCompleteTime(String completeTime)
    {
        this.completeTime = completeTime;
    }

    public String getCompleteTime()
    {
        return completeTime;
    }
    public void setSolution(String solution)
    {
        this.solution = solution;
    }

    public String getSolution()
    {
        return solution;
    }
    public void setFaultType(String faultType)
    {
        this.faultType = faultType;
    }

    public String getFaultType()
    {
        return faultType;
    }
    public void setServiceEvaluation(String serviceEvaluation)
    {
        this.serviceEvaluation = serviceEvaluation;
    }

    public String getServiceEvaluation()
    {
        return serviceEvaluation;
    }
    public void setEquipmentTypeB(String equipmentTypeB)
    {
        this.equipmentTypeB = equipmentTypeB;
    }

    public String getEquipmentTypeB()
    {
        return equipmentTypeB;
    }
    public void setEquipmentNameB(String equipmentNameB)
    {
        this.equipmentNameB = equipmentNameB;
    }

    public String getEquipmentNameB()
    {
        return equipmentNameB;
    }
    public void setEquipmentModelB(String equipmentModelB)
    {
        this.equipmentModelB = equipmentModelB;
    }

    public String getEquipmentModelB()
    {
        return equipmentModelB;
    }
    public void setCancleNote(String cancleNote)
    {
        this.cancleNote = cancleNote;
    }

    public String getCancleNote()
    {
        return cancleNote;
    }
    public void setFile(String file)
    {
        this.file = file;
    }

    public String getFile()
    {
        return file;
    }
    public void setReportDepartment(String reportDepartment)
    {
        this.reportDepartment = reportDepartment;
    }

    public String getReportDepartment()
    {
        return reportDepartment;
    }
    public void setSource(String source)
    {
        this.source = source;
    }

    public String getSource()
    {
        return source;
    }
    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public String getRequestId()
    {
        return requestId;
    }
    public void setTelephoneFollow(String telephoneFollow)
    {
        this.telephoneFollow = telephoneFollow;
    }

    public String getTelephoneFollow()
    {
        return telephoneFollow;
    }
    public void setFlowNo(String flowNo)
    {
        this.flowNo = flowNo;
    }

    public String getFlowNo()
    {
        return flowNo;
    }
    public void setImplementId(String implementId)
    {
        this.implementId = implementId;
    }

    public String getImplementId()
    {
        return implementId;
    }
    public void setDealNotes(String dealNotes)
    {
        this.dealNotes = dealNotes;
    }

    public String getDealNotes()
    {
        return dealNotes;
    }
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getRemarks()
    {
        return remarks;
    }
    public void setFacturer(String facturer)
    {
        this.facturer = facturer;
    }

    public String getFacturer()
    {
        return facturer;
    }
    public void setFacturerAcceptTime(String facturerAcceptTime)
    {
        this.facturerAcceptTime = facturerAcceptTime;
    }

    public String getFacturerAcceptTime()
    {
        return facturerAcceptTime;
    }
    public void setEngineerAcceptTime(String engineerAcceptTime)
    {
        this.engineerAcceptTime = engineerAcceptTime;
    }

    public String getEngineerAcceptTime()
    {
        return engineerAcceptTime;
    }
    public void setEngineerArriveTime(String engineerArriveTime)
    {
        this.engineerArriveTime = engineerArriveTime;
    }

    public String getEngineerArriveTime()
    {
        return engineerArriveTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("instanceId", getInstanceId())
                .append("extra1", getExtra1())
                .append("extra2", getExtra2())
                .append("extra3", getExtra3())
                .append("extra4", getExtra4())
                .append("extra5", getExtra5())
                .append("status", getStatus())
                .append("createdTime", getCreatedTime())
                .append("createdBy", getCreatedBy())
                .append("updatedTime", getUpdatedTime())
                .append("updatedBy", getUpdatedBy())
                .append("stage", getStage())
                .append("currentHandlerId", getCurrentHandlerId())
                .append("title", getTitle())
                .append("chmType", getChmType())
                .append("implementNo", getImplementNo())
                .append("faultDescription", getFaultDescription())
                .append("equipmentType", getEquipmentType())
                .append("expectTime", getExpectTime())
                .append("urgent", getUrgent())
                .append("equipmentName", getEquipmentName())
                .append("scopeInfluence", getScopeInfluence())
                .append("priority", getPriority())
                .append("equipmentModel", getEquipmentModel())
                .append("contact", getContact())
                .append("contactDetails", getContactDetails())
                .append("assignedGroup", getAssignedGroup())
                .append("assignedPerson", getAssignedPerson())
                .append("orderTime", getOrderTime())
                .append("arriveTime", getArriveTime())
                .append("completeTime", getCompleteTime())
                .append("solution", getSolution())
                .append("faultType", getFaultType())
                .append("serviceEvaluation", getServiceEvaluation())
                .append("equipmentTypeB", getEquipmentTypeB())
                .append("equipmentNameB", getEquipmentNameB())
                .append("equipmentModelB", getEquipmentModelB())
                .append("cancleNote", getCancleNote())
                .append("file", getFile())
                .append("reportDepartment", getReportDepartment())
                .append("source", getSource())
                .append("requestId", getRequestId())
                .append("telephoneFollow", getTelephoneFollow())
                .append("flowNo", getFlowNo())
                .append("implementId", getImplementId())
                .append("dealNotes", getDealNotes())
                .append("remarks", getRemarks())
                .append("facturer", getFacturer())
                .append("facturerAcceptTime", getFacturerAcceptTime())
                .append("engineerAcceptTime", getEngineerAcceptTime())
                .append("engineerArriveTime", getEngineerArriveTime())
                .toString();
    }
}