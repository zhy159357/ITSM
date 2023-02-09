package com.ruoyi.activiti.task.thread;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.task.ForwardFileCheckTask;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.domain.entity.AutomateMiddle;
import com.ruoyi.common.core.domain.entity.VmBizInfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 定时任务启动流程
 *
 * @author 14735
 */
public class ForwardFileCheckAndStart {

    private AutomateMiddle automateMiddle;

    private ForwardFileCheckTask forwardFileCheckTask;

    private static final Logger log = LoggerFactory.getLogger(ForwardFileCheckAndStart.class);

    public void checkAndStart() {
        Map checkMap = null;
        VmBizInfo vmBizInfo = null;
        try {
            log.debug("------------定时任务线程校验自动化步骤并启动流程休眠1500毫秒-------------");
            Thread.sleep(1500);
            vmBizInfo = forwardFileCheckTask.getVmBizInfoService().selectVmBizInfoByQuartz(automateMiddle.getBusinessId());
            // 此处定时任务查询到的版本单必须是待提交的状态，才能往下执行校验-校验通过之后才能启动流程，防止线程并发重复启动流程
            if(!VersionStatusConstants.VERSION_STATUS_1.equals(vmBizInfo.getVersionStatus())){
                log.debug("-------------版本单id=" + vmBizInfo.getVersionInfoId() + ",单号versionInfoNo=" + vmBizInfo.getVersionInfoNo() + ",版本单状态versionStatus=" + vmBizInfo.getVersionStatus() + ",流程已经启动成功，不可重复启动------------");
                return;
            }
            checkMap = sendAutomateCheck(vmBizInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("版本单id=" + automateMiddle.getBusinessId() + ",单号versionInfoNo=" + automateMiddle.getBusinessNo() + "发送自动化平台校验失败", e);
        }
        // 如果是版本单下的附件没有全部下载完成则跳过本次校验
        if ((boolean) (checkMap.get("checkNo"))) {
            return;
        }
        try {
            // 发送自动化校验成功则将status置为1成功，否则置为9失败
            if ((boolean) (checkMap.get("flag"))) {
                automateMiddle.setStatus(AutomateMiddle.AUTO_STATUS_OK);
                automateMiddle.setCheckCount(0);
                // 提交流程
                log.debug("版本单id=" + automateMiddle.getBusinessId() + ",单号versionInfoNo=" + automateMiddle.getBusinessNo() + ",开始启动流程...........");
                this.submit(vmBizInfo);
                log.debug("版本单id=" + automateMiddle.getBusinessId() + ",单号versionInfoNo=" + automateMiddle.getBusinessNo() + ",启动流程成功...........");
            } else {
                automateMiddle.setCheckCount(1);
                // 校验不通过则将状态置为失败
                automateMiddle.setStatus(AutomateMiddle.AUTO_STATUS_FAIL);
                if (AutomateMiddle.AUTO_STATUS_FAIL.equals(automateMiddle.getStatus())) {
                    // 此处给版本创建人发送短信提示信息
                    String msg = "版本单号【" + automateMiddle.getBusinessNo() + "】的自动化版本单提交失败，请登录运维管理平台检查版本单对应的自动化步骤附件后重新提交；具体错误信息描述为：【" + checkMap.get("message") + "】";
                    forwardFileCheckTask.getVmBizInfoService().sendSms(msg,
                            forwardFileCheckTask.getOgPersonService().selectOgPersonById(vmBizInfo.getVersionProducerId()));
                }
            }
            forwardFileCheckTask.getAutomateMiddleService().updateAutomateMiddle(automateMiddle);

            //  中间表数据量较小，暂时注释掉自清理中间表数据
            // this.deleteOneMonthMiddle();
        } catch (Exception e) {
            e.printStackTrace();
            automateMiddle.setStatus(AutomateMiddle.AUTO_STATUS_FAIL);
            forwardFileCheckTask.getAutomateMiddleService().updateAutomateMiddle(automateMiddle);
            throw new BusinessException("版本单id=" + automateMiddle.getBusinessId() + ",单号versionInfoNo=" + automateMiddle.getBusinessNo() + ",启动流程失败", e);
        }
    }

    /**
     * 定时任务线程启动流程
     * @param vmBizInfo
     */
    public synchronized void submit(VmBizInfo vmBizInfo) {
        // 标识定时任务启动流程，区分正常从页面提交流程，因为定时任务启动流程没有登录人id
        vmBizInfo.getParams().put("automateCheckFlag", "1");
        forwardFileCheckTask.getVmBizInfoService().startProcess(vmBizInfo);
    }

    /**
     * 一个版本单下的附件都下载完成后才发送自动化校验，否则不发送
     *
     * @param vmBizInfo
     * @return
     */
    public Map sendAutomateCheck(VmBizInfo vmBizInfo) {
        Map map = new HashMap<>();
        boolean flag = true;
        Attachment attachment = new Attachment();
        attachment.setOwnerId(vmBizInfo.getVersionInfoId());
        attachment.setFileType("3");
        List<Attachment> attachments = forwardFileCheckTask.getAttachmentService().selectAttachmentList(attachment);
        if (CollectionUtils.isEmpty(attachments)) {
            map.put("checkNo", true);
            return map;
        }
        for (Attachment att : attachments) {
            if (StringUtils.isNotEmpty(att.getFileCiphertext())) {
                if (!Attachment.FILE_STATUS_OK.equals(att.getFileStatus())) {
                    log.debug("版本单id=" + automateMiddle.getBusinessId() + ",单号versionInfoNo=" + automateMiddle.getBusinessNo() + "对应的附件尚未从跳板机全部下载完成，不做自动化校验............");
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            log.debug("版本单id=" + automateMiddle.getBusinessId() + ",单号versionInfoNo=" + automateMiddle.getBusinessNo() + "的开始发送自动化校验............");
            vmBizInfo.getParams().put("attachmentList", attachments);
            map = forwardFileCheckTask.getVmBizInfoService().checkVersionAttachmentExcel(vmBizInfo);
            log.debug("版本单id=" + automateMiddle.getBusinessId() + ",单号versionInfoNo=" + automateMiddle.getBusinessNo() + "的发送自动化校验结果为:" + JSON.toJSONString(map));
            map.put("checkNo", false);
        } else {
            map.put("checkNo", true);
        }
        return map;
    }

    /**
     * 默删除3个月之前中间表的数据，起到自清理的作用，防止automate_middle这张中间表数据过大
     */
    public void deleteOneMonthMiddle() {
        Date oneMonthAgoDate = DateUtils.add(new Date(), Calendar.MONTH, -3);
        List<AutomateMiddle> automateMiddles = forwardFileCheckTask.getAutomateMiddleService().selectAutomateMiddleList(new AutomateMiddle());
        for (AutomateMiddle middle : automateMiddles){
            if (middle.getCreateTime().before(oneMonthAgoDate)) {
                // 删除一个月之前的数据，排除中间状态为0的数据
                if (!AutomateMiddle.AUTO_STATUS_LOADING.equals(middle.getStatus())) {
                    forwardFileCheckTask.getAutomateMiddleService().deleteAutomateMiddleById(middle.getAutoId());
                }
            }
        }
    }

    public AutomateMiddle getAutomateMiddle() {
        return automateMiddle;
    }

    public void setAutomateMiddle(AutomateMiddle automateMiddle) {
        this.automateMiddle = automateMiddle;
    }

    public ForwardFileCheckTask getForwardFileCheckTask() {
        return forwardFileCheckTask;
    }

    public void setForwardFileCheckTask(ForwardFileCheckTask forwardFileCheckTask) {
        this.forwardFileCheckTask = forwardFileCheckTask;
    }
}
