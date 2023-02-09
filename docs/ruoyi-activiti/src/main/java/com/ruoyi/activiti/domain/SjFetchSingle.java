package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 【数据提取单】对象 sj_fetch_single
 *
 * @author ruoyi
 * @date 2021-04-07
 */
public class SjFetchSingle extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数据提取单id
     */
    private String fetchId;

    /**
     * 数据提取单单号
     */
    @Excel(name = "数据提取单单号")
    private String fetchNo;

    private String creatTime;

    /**
     * 创建人ID
     */
    private String createId;

    /**
     * 数据提取单的需求来源 01-大数据门户 02-OA
     */
    //@NotBlank(message = "需求来源不能为空!")
    private String sourceType;

    @Excel(name = "需求来源")
    private String sourceTypeText;

    /**
     * 需求标题
     */
    @Excel(name = "需求标题")
    //@NotBlank(message = "需求标题不能为空!")
    //@Size(min = 1,max = 128,message = "需求标题的最大长度为128")
    private String titleName;

    /**
     * 大数据门户实例ID
     */
    //@NotBlank(message = "流程号不能为空!")
    @Size(min = 1, max = 32, message = "需求标题的最大长度为32")
    private String processid;

    /**
     * 联系部门
     */
    @Excel(name = "联系部门")
    //@NotBlank(message = "联系部门不能为空!")
    private String orgName;

    private String orgId;

    /**
     * 联系人
     */
    @Excel(name = "联系人")
    //@NotBlank(message = "联系人不能为空!")
    private String personName;

    /**
     * 需求单号
     */
    @Excel(name = "需求单号")
    //@NotBlank(message = "需求单号不能为空!")
    //@Size(min = 1,max = 32,message = "需求单号的最大长度为32")
    private String businessNumber;


    //@NotBlank(message = "需求类别不能为空!")
    private String businessType;

    /**
     * 01-监管报送，02-经营分析，03-开发测试，04-其他
     */
    @Excel(name = "需求类别")
    private String businessTypeText;

    /**
     * 业务部门
     */
    @Excel(name = "业务部门")
    private String businessOrgName;

    private String businessOrgId;

    /**
     * 业务联系人
     */
    @Excel(name = "业务联系人")
    private String bbusinessPersonName;

    private String sysId;
    /**
     * 涉及系统
     */
    @Excel(name = "涉及系统")
    //@NotBlank(message = "涉及系统不能为空!")
    private String sysName;


    //@NotBlank(message = "接口人姓名不能为空!")
    private String interfaceName;

    //@NotBlank(message = "接口人手机号不能为空!")
    private String interfacePhone;

    private Long fileSize;

    private String fetchState;

    /**
     * 数据提取单创建时间
     */
    @Excel(name = "创建时间")
    private String creatTimeText;

    /**
     * 数据提取单状态
     */
    @Excel(name = "数据提取单状态")
    private String fetchStateText;

    private String groupId;


    private String assingId;

    /*系统机构id*/
    private String sysOrgId;

    /*系统机构名称*/
    private String sysOrgName;

    private String sysManagerId;

    /**
     * 系统负责人名称
     */
    private String sysManagerName;

    private String dealId;

    private String auditBackId;

    private String collectBackId;
    /**
     * 文件对象
     */
    private MultipartFile[] files;

    /**
     * 数据提取单id集合
     */
    private List<String> ids;

    private String flag;

    /**
     * 操作说明
     */
    private String logPerformDesc;

    /**
     * fetch_demandId  需求单ID
     */
    private String fetchDemandId;

    //处理标识  1 系统负责人处理 2 工作组处理
    private String dealFlag;

    public void setFetchId(String fetchId) {
        this.fetchId = fetchId;
    }

    public String getFetchId() {
        return fetchId;
    }

    public void setFetchNo(String fetchNo) {
        this.fetchNo = fetchNo;
    }

    public String getFetchNo() {
        return fetchNo;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public String getCreatTimeText() {
        return creatTimeText;
    }

    public void setCreatTimeText(String creatTimeText) {
        this.creatTimeText = creatTimeText;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getSourceTypeText() {
        return sourceTypeText;
    }

    public void setSourceTypeText(String sourceTypeText) {
        this.sourceTypeText = sourceTypeText;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public String getProcessid() {
        return processid;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getBusinessTypeText() {
        return businessTypeText;
    }

    public void setBusinessTypeText(String businessTypeText) {
        this.businessTypeText = businessTypeText;
    }

    public void setBusinessOrgName(String businessOrgName) {
        this.businessOrgName = businessOrgName;
    }

    public String getBusinessOrgName() {
        return businessOrgName;
    }

    public String getBusinessOrgId() {
        return businessOrgId;
    }

    public void setBusinessOrgId(String businessOrgId) {
        this.businessOrgId = businessOrgId;
    }

    public void setBbusinessPersonName(String bbusinessPersonName) {
        this.bbusinessPersonName = bbusinessPersonName;
    }

    public String getBbusinessPersonName() {
        return bbusinessPersonName;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfacePhone(String interfacePhone) {
        this.interfacePhone = interfacePhone;
    }

    public String getInterfacePhone() {
        return interfacePhone;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFetchState(String fetchState) {
        this.fetchState = fetchState;
    }

    public String getFetchState() {
        return fetchState;
    }

    public String getFetchStateText() {
        return fetchStateText;
    }

    public void setFetchStateText(String fetchStateText) {
        this.fetchStateText = fetchStateText;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setAssingId(String assingId) {
        this.assingId = assingId;
    }

    public String getSysOrgId() {
        return sysOrgId;
    }

    public void setSysOrgId(String sysOrgId) {
        this.sysOrgId = sysOrgId;
    }

    public String getSysOrgName() {
        return sysOrgName;
    }

    public void setSysOrgName(String sysOrgName) {
        this.sysOrgName = sysOrgName;
    }

    public String getAssingId() {
        return assingId;
    }

    public void setSysManagerId(String sysManagerId) {
        this.sysManagerId = sysManagerId;
    }

    public String getSysManagerId() {
        return sysManagerId;
    }

    public String getSysManagerName() {
        return sysManagerName;
    }

    public void setSysManagerName(String sysManagerName) {
        this.sysManagerName = sysManagerName;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getDealId() {
        return dealId;
    }

    public void setAuditBackId(String auditBackId) {
        this.auditBackId = auditBackId;
    }

    public String getAuditBackId() {
        return auditBackId;
    }

    public void setCollectBackId(String collectBackId) {
        this.collectBackId = collectBackId;
    }

    public String getCollectBackId() {
        return collectBackId;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLogPerformDesc() {
        return logPerformDesc;
    }

    public void setLogPerformDesc(String logPerformDesc) {
        this.logPerformDesc = logPerformDesc;
    }

    public String getFetchDemandId() {
        return fetchDemandId;
    }

    public void setFetchDemandId(String fetchDemandId) {
        this.fetchDemandId = fetchDemandId;
    }

    public String getDealFlag() {
        return dealFlag;
    }

    public void setDealFlag(String dealFlag) {
        this.dealFlag = dealFlag;
    }

    public void validate() {
        if (StringUtils.isEmpty(sourceType)) {
            throw new BusinessException("需求来源不能为空!");
        }
        if (StringUtils.isEmpty(titleName)) {
            throw new BusinessException("需求标题不能为空!");
        }
        if (StringUtils.isEmpty(processid)) {
            throw new BusinessException("流程号不能为空!");
        }
        if (StringUtils.isEmpty(orgName)) {
            throw new BusinessException("联系部门不能为空!");
        }
        if (StringUtils.isEmpty(personName)) {
            throw new BusinessException("联系人不能为空!");
        }
        if (StringUtils.isEmpty(businessNumber)) {
            throw new BusinessException("需求单号不能为空!");
        }
        if (StringUtils.isEmpty(businessType)) {
            throw new BusinessException("需求类别不能为空!");
        }
        if (StringUtils.isEmpty(sysName)) {
            throw new BusinessException("涉及系统不能为空!");
        }
        if (StringUtils.isEmpty(interfaceName)) {
            throw new BusinessException("接口人姓名不能为空!");
        }
        if (StringUtils.isEmpty(interfacePhone)) {
            throw new BusinessException("接口人手机号不能为空!");
        }
        if (fileSize == null || fileSize == 0) {
            throw new BusinessException("附件个数不能为空!");
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fetchId", getFetchId())
                .append("fetchNo", getFetchNo())
                .append("creatTime", getCreatTime())
                .append("sourceType", getSourceType())
                .append("titleName", getTitleName())
                .append("processid", getProcessid())
                .append("orgName", getOrgName())
                .append("personName", getPersonName())
                .append("businessNumber", getBusinessNumber())
                .append("businessType", getBusinessType())
                .append("businessOrgName", getBusinessOrgName())
                .append("bbusinessPersonName", getBbusinessPersonName())
                .append("sysName", getSysName())
                .append("interfaceName", getInterfaceName())
                .append("interfacePhone", getInterfacePhone())
                .append("fileSize", getFileSize())
                .append("fetchState", getFetchState())
                .append("groupId", getGroupId())
                .append("assingId", getAssingId())
                .append("sysManagerId", getSysManagerId())
                .append("dealId", getDealId())
                .append("auditBackId", getAuditBackId())
                .append("collectBackId", getCollectBackId())
                .toString();
    }

}
