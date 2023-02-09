package com.ruoyi.activiti.listener;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.service.IAutoItsmFastfileService;
import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.activiti.service.impl.*;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.impl.OgPersonServiceImpl;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 版本发布监听类
 *
 * @author 14735
 */
@Service("VersionPublicProcessListener")
public class VersionPublicProcessListener implements JavaDelegate, ExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(VersionPublicProcessListener.class);

    @Override
    public void notify(DelegateExecution delegateExecution) {

    }

    @Override
    public void execute(DelegateExecution delegateExecution) {

    }

    private OgPersonServiceImpl ogPersonService;
    private VmBizInfoServiceImpl vmBizInfoService;
    private String userId;

    /**
     * 版本发布流程监听类
     * 各节点结束时监听，该类主要作用是修改版本状态和发送短信
     * 发送短信只是部分情况，所有上行短信均在ActivitiCommServiceImpl类中的sendMsg()方法
     *
     * @param execution
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleVersion(DelegateExecution execution) {
        log.debug("###############版本发布单工作流监听开始################");
        DelegateExecution parent = execution.getParent();
        String businessId = parent.getProcessInstanceBusinessKey();
        Map<String, Object> variable = parent.getVariables();
        String reCode = (String) variable.get("reCode");
        String userId = (String) variable.get("userId");
        FlowElement currentFlowElement = execution.getCurrentFlowElement();
        // 任务名称
        String taskName = currentFlowElement.getName();
        log.debug("###############版本发布单工作流节点任务名称taskName:" + taskName + ",businessKey=" + businessId + "################");

        this.userId = userId;
        // 业务层实现类
        this.vmBizInfoService = SpringUtils.getBean(VmBizInfoServiceImpl.class);
        this.ogPersonService = SpringUtils.getBean(OgPersonServiceImpl.class);

        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoByQuartz(businessId);
        // invalidationMark为TK时标识定时任务作废流程，故不再走以下监听逻辑，直接return
        if("TK".equals(vmBizInfo.getInvalidationMark())){
            log.debug("###############版本发布单ID=" + businessId + ",版本编号versionInfoNo=" + vmBizInfo.getVersionInfoNo() + "的版本单属于定时任务作废流程不再向下执行监听类相关逻辑################");
            return;
        }
        // 业务审核状态  1-待业务审核    2-待业务主管审核   BY-业务主管审核已通过
        String businessState = vmBizInfo.getBusinessState();
        // 技术审核状态  1-待技术审核    2-待技术主管审核   TY-技术主管审核已通过
        String technologyState = vmBizInfo.getTechnologyState();

        // 版本创建人(项目经理)
        OgPerson producerPerson = ogPersonService.selectOgPersonById(vmBizInfo.getVersionProducerId());
        // 应用负责人
        OgPerson useApproval = ogPersonService.selectOgPersonById(vmBizInfo.getUseApprovalId());
        // 当前操作人
        //OgPerson person = ogPersonService.selectOgPersonById(userId);

        // 版本状态（页面展示）
        String versionStatus = "";
        // 短信信息   业务主管｜技术主管｜应用处长｜发布审批｜紧急审批有上行短信批量审批
        String msg = "";
        if ("安全审核".equals(taskName)) {
            if ("1".equals(reCode)) {
                // 作废需要向项目经理｜应用负责人发送短信
                sendCancelMsg(producerPerson, useApproval, vmBizInfo);
            } else {
                OgPerson technologyPerson = null;
                // 业务｜技术审核路线
                if (!"1".equals(vmBizInfo.getVersionType())) {
                    // 安全审核通过需要向业务审核｜技术审核发送短信
                    List<OgPerson> businessPersons = ogPersonService.selectPersonListByPIds(Convert.toStrArray(vmBizInfo.getBusinessAuditId()));
                    String businessPersonName = "";
                    for (OgPerson business : businessPersons) {
                        businessPersonName += business.getpName() + ",";
                    }
                    businessPersonName = StringUtils.removeLastChar(businessPersonName);
                    technologyPerson = ogPersonService.selectOgPersonById(vmBizInfo.getTechnologyAuditId());
                    msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本安全审核已通过," +
                            "下一步业务审核.技术审核,业务审核人：(" + businessPersonName + "),技术审核人：" + technologyPerson.getpName() + "。";
                    // 向版本创建人发送短信
                    this.sendMsgNoUp(producerPerson, msg);

                    // 向业务审核发送短信
                    msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本已经创建，请登录运维管理平台查看审核。";
                    for (OgPerson business : businessPersons) {
                        this.sendMsgNoUp(business, msg);
                    }
                } else {
                    // 纯技术路线
                    technologyPerson = ogPersonService.selectOgPersonById(vmBizInfo.getTechnologyAuditId());
                    msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本已经提交," +
                            "下一步技术审核,技术审核人：" + technologyPerson.getpName() + "。";
                    // 向项目经理发送短信
                    this.sendMsgNoUp(producerPerson, msg);
                }
                // 两种情况都需要向技术审核发送短信
                msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本已经创建，请登录运维管理平台查看审核。";
                this.sendMsgNoUp(technologyPerson, msg);
            }
        }
        if ("业务审核".equals(taskName)) {
            if ("1".equals(reCode)) {
                // 作废
                versionStatus = VersionStatusConstants.VERSION_STATUS_14;
                //sendCancelMsg(producerPerson, useApproval, vmBizInfo);
            } else {
                // 业务状态修改为待业务主管审核
                businessState = VersionStatusConstants.BUSINESS_STATUS_2;
                // 版本状态修改为待业务主管审核
                versionStatus = VersionStatusConstants.VERSION_STATUS_7;
            }
        } else if ("技术审核".equals(taskName)) {
            if ("1".equals(reCode)) {
                // 作废
                versionStatus = VersionStatusConstants.VERSION_STATUS_14;
                // 作废需要向项目经理｜应用负责人发送短信
                sendCancelMsg(producerPerson, useApproval, vmBizInfo);
            } else {
                // 技术状态修改为待技术主管审核
                technologyState = VersionStatusConstants.TECHNOLOGY_STATUS_2;
                // 版本状态修改为待技术主管审核
                versionStatus = VersionStatusConstants.VERSION_STATUS_8;
            }
        } else if ("业务主管审核".equals(taskName)) {
            if ("1".equals(reCode)) {
                // 作废
                versionStatus = VersionStatusConstants.VERSION_STATUS_14;
                // 作废需要向项目经理｜应用负责人发送短信
                sendCancelMsg(producerPerson, useApproval, vmBizInfo);
            } else {
                // 业务状态修改为业务主管审核已通过
                businessState = VersionStatusConstants.BUSINESS_STATUS_PASS;
                if (VersionStatusConstants.TECHNOLOGY_STATUS_PASS.equals(technologyState)) {
                    // 业务主管审核通过并且技术主管审核完成状态修改为待版本审核
                    versionStatus = VersionStatusConstants.VERSION_STATUS_9;
                } else if (VersionStatusConstants.TECHNOLOGY_STATUS_1.equals(technologyState)) {
                    // 业务主管先审核完成后如果技术审核状态为待技术审核则修改为待技术审核
                    versionStatus = VersionStatusConstants.VERSION_STATUS_6;
                } else if (VersionStatusConstants.TECHNOLOGY_STATUS_2.equals(technologyState)) {
                    // 业务主管先审核完成后如果技术审核状态为待技术主管审核则修改为待技术主管审核
                    versionStatus = VersionStatusConstants.VERSION_STATUS_8;
                }
            }
        } else if ("技术主管审核".equals(taskName)) {
            if ("1".equals(reCode)) {
                // 作废
                versionStatus = VersionStatusConstants.VERSION_STATUS_14;
                // 作废需要向项目经理｜应用负责人发送短信
                sendCancelMsg(producerPerson, useApproval, vmBizInfo);
            } else {
                // 技术状态修改为技术主管审核已通过
                technologyState = VersionStatusConstants.TECHNOLOGY_STATUS_PASS;
                if (VersionStatusConstants.BUSINESS_STATUS_PASS.equals(businessState)) {
                    // 业务主管审核通过并且技术主管审核完成状态修改为待版本审核
                    versionStatus = VersionStatusConstants.VERSION_STATUS_9;
                } else if (VersionStatusConstants.BUSINESS_STATUS_1.equals(businessState)) {
                    // 技术主管先审核完成后如果业务审核状态为待业务审核则修改为待业务审核
                    versionStatus = VersionStatusConstants.VERSION_STATUS_4;
                } else if (VersionStatusConstants.BUSINESS_STATUS_2.equals(businessState)) {
                    // 技术主管先审核完成后如果业务审核状态为待业务主管审核则修改为待业务主管审核
                    versionStatus = VersionStatusConstants.VERSION_STATUS_7;
                }

                // 需要向AUTO_ITSM_FASTFILE中记录附件类型为版本包的数据
                this.saveAutoItsmFastFile(vmBizInfo);
            }
        } else if ("版本审核".equals(taskName)) {
            if ("1".equals(reCode)) {
                // 作废
                versionStatus = VersionStatusConstants.VERSION_STATUS_14;
                // 作废需要向项目经理｜应用负责人发送短信
                sendCancelMsg(producerPerson, useApproval, vmBizInfo);
            } else {
            }
        } else if ("运维审核".equals(taskName)) {
            if ("1".equals(reCode)) {
                // 作废
                versionStatus = VersionStatusConstants.VERSION_STATUS_14;
                // 作废需要向项目经理｜应用负责人发送短信
                sendCancelMsg(producerPerson, useApproval, vmBizInfo);
            } else {
                versionStatus = VersionStatusConstants.VERSION_STATUS_11;
            }
        } else if ("发布审批".equals(taskName)) {
            // 发布审批reCode=0流程走向是通过或者作废  reCode=1走向紧急审批需要在页面由客户操作
            if ("0".equals(reCode)) {
                // 作废
                if (VersionStatusConstants.VERSION_STATUS_14.equals(vmBizInfo.getVersionStatus())) {
                    // 作废需要向项目经理｜应用负责人发送短信
                    sendCancelMsg(producerPerson, useApproval, vmBizInfo);
                } else {
                    // 发布成功
                    versionStatus = VersionStatusConstants.VERSION_STATUS_12;
                    this.sendSuccessMsg(producerPerson, useApproval, vmBizInfo);
                }
            } else {
            }
        } else if ("紧急发布审批".equals(taskName)) {
            // 发布审批reCode=0流程走向是通过或者作废
            if ("0".equals(reCode)) {
                // 作废
                if (VersionStatusConstants.VERSION_STATUS_14.equals(vmBizInfo.getVersionStatus())) {
                    // 作废需要向项目经理｜应用负责人发送短信
                    sendCancelMsg(producerPerson, useApproval, vmBizInfo);
                } else {
                    // 发布成功
                    versionStatus = VersionStatusConstants.VERSION_STATUS_12;
                    this.sendSuccessMsg(producerPerson, useApproval, vmBizInfo);
                }
            } else {
            }
        }

        log.debug("###############版本发布单ID=" + businessId + ",版本编号versionInfoNo=" + vmBizInfo.getVersionInfoNo() + "的版本单状态由oldVersionStatus=" + vmBizInfo.getVersionStatus() + "变更为newVersionStatus=" + versionStatus + "################");
        vmBizInfo.setVersionStatus(versionStatus);
        vmBizInfo.setBusinessState(businessState);
        vmBizInfo.setTechnologyState(technologyState);
        vmBizInfoService.updateVmBizInfo(vmBizInfo);
        log.debug("###############版本发布单工作流监听结束################");
    }

    /**
     * 作废发送短信方法
     *
     * @param producerPerson
     * @param useApproval
     * @param vmBizInfo
     */
    public void sendCancelMsg(OgPerson producerPerson, OgPerson useApproval, VmBizInfo vmBizInfo) {
        // 向版本创建人(项目经理)发送短信
        String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本已被否决。";
        this.sendMsgNoUp(producerPerson, msg);
        // 向应用负责人发送短信
        this.sendMsgNoUp(useApproval, msg);
        // 调用自动化接口
        this.forbiddenVersion(vmBizInfo);
    }

    /**
     * 发送短信方法(不包含上行)
     *
     * @param person
     * @param msg
     */
    public void sendMsgNoUp(OgPerson person, String msg) {
        vmBizInfoService.sendSms(msg, person);
    }

    /**
     * 版本已发布向项目经理｜应用负责人发短信
     *
     * @param producerPerson
     * @param useApproval
     * @param vmBizInfo
     */
    public void sendSuccessMsg(OgPerson producerPerson, OgPerson useApproval, VmBizInfo vmBizInfo) {
        String msg = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本已经通过发布审批，可以实施。";
        // 向项目经理发短信
        this.sendMsgNoUp(producerPerson, msg);
        // 向应用负责人发送短信
        this.sendMsgNoUp(useApproval, msg);
    }

    /**
     * 保存自动化下载附件的中间表
     *
     * @param vmBizInfo
     */
    public void saveAutoItsmFastFile(VmBizInfo vmBizInfo) {
        try {
            IAutoItsmFastfileService fastFileService = SpringUtils.getBean(AutoItsmFastfileServiceImpl.class);
            IPubAttachmentService attachmentService = SpringUtils.getBean(PubAttachmentServiceImpl.class);
            Attachment att = new Attachment();
            att.setOwnerId(vmBizInfo.getVersionInfoId());
            att.setFileType("2");
            List<Attachment> attachments = attachmentService.selectAttachmentList(att);
            if(!CollectionUtils.isEmpty(attachments)){
                for(Attachment attachment : attachments){
                    AutoItsmFastfile autoItsmFastfile = new AutoItsmFastfile();
                    autoItsmFastfile.setUuid(UUID.getUUIDStr());
                    autoItsmFastfile.setIitsmno(vmBizInfo.getVersionInfoNo());
                    autoItsmFastfile.setIuploadtime(new Date());
                    autoItsmFastfile.setFastno(attachment.getFilePath());
                    autoItsmFastfile.setIsystem(attachment.getSysName());
                    autoItsmFastfile.setFilename(attachment.getFileName());
                    autoItsmFastfile.setGroupname(attachment.getGroupName());
                    autoItsmFastfile.setIsystemabb(vmBizInfo.getOgSys() == null ? "" : vmBizInfo.getOgSys().getCode());
                    autoItsmFastfile.setIstatus(0L);
                    autoItsmFastfile.setIstatusCentral(0L);
                    // fast码和组名都不为空的情况才保存
                    if(StringUtils.isNotEmpty(attachment.getFilePath()) && StringUtils.isNotEmpty(attachment.getGroupName())) {
                        int rows = fastFileService.insertAutoItsmFastfile(autoItsmFastfile);
                        if (rows > 0) {
                            log.debug("-----------保存[AUTO_ITSM_FASTFILE]表信息成功-----------");
                        }
                    } else {
                        log.debug("-----------版本单号:" + vmBizInfo.getVersionInfoNo() + "，附件信息fast码值或组名为空不保存自动化附件中间表信息-----------");
                    }
                }
            } else {
                log.debug("-----------版本单号:" + vmBizInfo.getVersionInfoNo() + "未查询到附件类型为版本包的附件信息,不保存[AUTO_ITSM_FASTFILE]表信息------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("-----------保存--AUTO_ITSM_FASTFILE--表信息失败，失败原因为：" + e.getMessage() + "-----------");
        }
    }

    /**
     * 作废时需要向自动化接口发送作废通知
     * @param vmBizInfo
     */
    public void forbiddenVersion(VmBizInfo vmBizInfo){
        // 当前操作人
        OgPerson person = ogPersonService.selectOgPersonById(userId);
        if(StringUtils.isNotNull(person)){
            vmBizInfo.getParams().put("mobilePhone", person.getMobilPhone());
        }
        vmBizInfoService.forbiddenVersion(vmBizInfo);
    }
}
