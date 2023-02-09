package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.mapper.PubAttachmentMapper;
import com.ruoyi.activiti.mapper.VmBizTaskinfoMapper;
import com.ruoyi.activiti.service.IAutoItsmResultmsgService;
import com.ruoyi.activiti.service.IVmBizTaskinfoService;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.http.entegorserver.entity.EntegorConstances;
import com.ruoyi.system.http.entegorserver.entity.InstanceStartup;
import com.ruoyi.system.mapper.OgPersonMapper;
import com.ruoyi.system.service.IEntegorServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 版本任务Service业务层处理
 *
 * @author ruoyi
 * @date 2021-01-06
 */
@Service
public class VmBizTaskinfoServiceImpl implements IVmBizTaskinfoService {
    @Autowired
    private VmBizInfoServiceImpl vmBizInfoService;

    @Autowired
    private VmBizTaskinfoMapper vmBizTaskinfoMapper;

    @Autowired
    private OgPersonMapper ogPersonMapper;

    @Autowired
    private IEntegorServer entegorServer;

    @Autowired
    private PubAttachmentMapper attachmentMapper;

    @Autowired
    private IAutoItsmResultmsgService resultmsgService;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    private static final Logger log = LoggerFactory.getLogger(VmBizTaskinfoServiceImpl.class);

    /**
     * 查询版本任务
     *
     * @param taskId 版本任务ID
     * @return 版本任务
     */
    @Override
    public VmBizTaskinfo selectVmBizTaskinfoById(String taskId) {
        VmBizTaskinfo vmBizTaskinfo = vmBizTaskinfoMapper.selectVmBizTaskinfoById(taskId);
        if (vmBizTaskinfo.getVmBizInfo() != null && StringUtils.isNotEmpty(vmBizTaskinfo.getVmBizInfo().getVersionType())) {
            vmBizInfoService.convertVersionType(vmBizTaskinfo.getVmBizInfo());
        }
        return vmBizTaskinfo;
    }

    /**
     * 查询版本任务
     *
     * @param taskNo 版本任务编号
     * @return 版本任务
     */
    @Override
    public VmBizTaskinfo selectVmBizTaskinfoByTaskNo(String taskNo) {
        VmBizTaskinfo vmBizTaskinfo = vmBizTaskinfoMapper.selectVmBizTaskinfoByTaskNo(taskNo);
        if (vmBizTaskinfo.getVmBizInfo() != null && StringUtils.isNotEmpty(vmBizTaskinfo.getVmBizInfo().getVersionType())) {
            vmBizInfoService.convertVersionType(vmBizTaskinfo.getVmBizInfo());
        }
        return vmBizTaskinfo;
    }

    /**
     * 查询版本任务列表
     *
     * @param vmBizTaskinfo 版本任务
     * @return 版本任务
     */
    @Override
    public List<VmBizTaskinfo> selectVmBizTaskinfoList(VmBizTaskinfo vmBizTaskinfo) {
        return vmBizTaskinfoMapper.selectVmBizTaskinfoList(vmBizTaskinfo);
    }

    /**
     * 查询版本任务列表
     *
     * @param vmBizTaskinfo 版本任务
     * @return 版本任务
     */
    @Override
    public List<VmBizTaskinfo> selectVmBizTaskinfoListUnion(VmBizTaskinfo vmBizTaskinfo) {
        return vmBizTaskinfoMapper.selectVmBizTaskinfoListUnion(vmBizTaskinfo);
    }

    /**
     * 新增版本任务
     *
     * @param vmBizTaskinfo 版本任务
     * @return 结果
     */
    @Override
    public int insertVmBizTaskinfo(VmBizTaskinfo vmBizTaskinfo) {
        return vmBizTaskinfoMapper.insertVmBizTaskinfo(vmBizTaskinfo);
    }

    /**
     * 修改版本任务
     *
     * @param vmBizTaskinfo 版本任务
     * @return 结果
     */
    @Override
    public int updateVmBizTaskinfo(VmBizTaskinfo vmBizTaskinfo) {
        return vmBizTaskinfoMapper.updateVmBizTaskinfo(vmBizTaskinfo);
    }

    /**
     * 删除版本任务对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteVmBizTaskinfoByIds(String ids) {
        return vmBizTaskinfoMapper.deleteVmBizTaskinfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除版本任务信息
     *
     * @param taskId 版本任务ID
     * @return 结果
     */
    @Override
    public int deleteVmBizTaskinfoById(String taskId) {
        return vmBizTaskinfoMapper.deleteVmBizTaskinfoById(taskId);
    }

    /**
     * 下载版本
     *
     * @param vmBiztaskInfo
     * @return
     */
    @Override
    public int downloadVersion(VmBizTaskinfo vmBiztaskInfo) {
        // 版本下载时间
        vmBiztaskInfo.setEstFinishDate(DateUtils.dateTimeNow());
        // 版本下载人
        vmBiztaskInfo.setTaskDealUserId(ShiroUtils.getUserId());
        return updateVmBizTaskinfo(vmBiztaskInfo);
    }

    /**
     * 确认版本
     *
     * @param vmBiztaskInfo
     * @return
     */
    @Override
    public int completeVersion(VmBizTaskinfo vmBiztaskInfo) {
        // 确认操作时间
        vmBiztaskInfo.setUpdate_time(DateUtils.dateTimeNow());
        // 版本确认人
        vmBiztaskInfo.setCompleteUserId(ShiroUtils.getUserId());
        return updateVmBizTaskinfo(vmBiztaskInfo);
    }

    /**
     * 向人员发送信息
     *
     * @param pId
     * @param msg
     */
    @Override
    public void sendMsg(String pId, String msg) {
        List<OgPerson> persons = ogPersonMapper.selectPersonListByPIds(Convert.toStrArray(pId));
        for (OgPerson person : persons) {
            // 此处需要调用向人员发送短信的方法
            vmBizInfoService.sendSms(msg, person);
        }
    }

    /**
     * 自动化对接接口-实例启动(仅仅启动)
     */
    @Override
    public Map sendMessageInstanceStartup(VmBizInfo vmBizInfo, Attachment attachment) {
        boolean flag;
        List list = new ArrayList<>();
        InstanceStartup instance = new InstanceStartup();
        instance.setIINSTANCENAME(attachment.getSysName());
        instance.setIVERSIONALIAS(vmBizInfo.getVersionInfoNo());
        instance.setInstName_input(attachment.getChangeReason());
        instance.setDeploymentEnv("");
        instance.setStartUserName("ideal");
        instance.setPassword("ideal");
        instance.setTasktype("4");
        instance.setStartMode(EntegorConstances.START_MODE_0);
        instance.setStartTime(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM));
        list.add(instance);
        String mobilPhone = ShiroUtils.getOgUser().getMobilPhone();
        Map<String, Object> resultMap = entegorServer.sendMessageInstanceStartup(list, mobilPhone);
        String message = (String) resultMap.get("message");
        String automateStatus;
        if ("200".equals(resultMap.get("status"))) {
            automateStatus = "3";
            flag = true;
        } else {
            automateStatus = "4";
            flag = false;
        }
        resultMap.put("flag", flag);
        Attachment attachment1 = new Attachment();
        attachment1.setAutomateStatus(automateStatus);
        attachment1.setAutomateResultMsg(message);
        attachment1.setAtId(attachment.getAtId());
        attachmentMapper.updateAttachment(attachment1);
        return resultMap;
    }

    @Override
    public List<Map> selectResultMsg(VmBizInfo vmBizInfo, Attachment attachment) {
        List<Map> resultList = new ArrayList<>();

        AutoItsmResultmsg msg = new AutoItsmResultmsg();
        msg.setBusinessId(vmBizInfo.getVersionInfoId());
        msg.setChangeReason(attachment.getChangeReason());
        Map<String, Object> pa = msg.getParams();
        String[] stateCodeArray = {"1", "2", "3"};
        pa.put("stateCodeList", Arrays.asList(stateCodeArray));
        // 自动化查询结果先查询结果信息表，表中没有再通过接口查询表中有数据直接取，stateCode为1、2、3是终态数据写库
        List<AutoItsmResultmsg> msgList = resultmsgService.selectAutoItsmResultmsgList(msg);
        if (!CollectionUtils.isEmpty(msgList)) {
            for (AutoItsmResultmsg resultMsg : msgList) {
                Map resultMap = new HashMap<>();
                resultMap.put("status", resultMsg.getStatus());
                resultMap.put("startTime", resultMsg.getStartTime());
                resultMap.put("endTime", resultMsg.getEndTime());
                resultList.add(resultMap);
            }
        } else {
            Map<String, Object> params = new HashMap<>();
            /*版本单号*/
            params.put("version", vmBizInfo.getVersionInfoNo());
            /*系统名称*/
            params.put("sysName", attachment.getSysName());
            /*变更原因*/
            params.put("insName", attachment.getChangeReason());
            params.put("mobilePhone", ShiroUtils.getOgUser().getMobilPhone());
            Map<String, Object> resultMap = entegorServer.selectResultMsg(params);
            if ("200".equals(resultMap.get("status"))) {
                resultList = (List<Map>) resultMap.get("result");
                for (Map map : resultList) {
                    String stateCode = String.valueOf(map.get("stateCode"));
                    map.put("status", map.get("state"));
                    map.put("resultMsg", resultMap.get("message"));
                    if ("1".equals(stateCode) || "2".equals(stateCode) || "3".equals(stateCode)) {
                        saveResultMsg(map, vmBizInfo, attachment);
                    }
                }
            } else {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", "查询失败");
                errorMap.put("resultMsg", resultMap.get("message"));
                resultList.add(errorMap);
            }
        }
        return resultList;
    }

    @Override
    public boolean judgeVersionTaskInfoImage(String taskId) {
        VmBizTaskinfo taskInfo = this.selectVmBizTaskinfoById(taskId);
        String[] ownerIds = Convert.toStrArray(taskInfo.getVersionInfoId());
        List<Attachment> attachments;
        if("mysql".equals(dbType)){
            attachments = attachmentMapper.selectAttachmentByOwnerIdsMysql(ownerIds);
        }else{
            attachments = attachmentMapper.selectAttachmentByOwnerIds(ownerIds);
        }
        if(!CollectionUtils.isEmpty(attachments)){
            for(Attachment attachment : attachments){
                if(VersionStatusConstants.VERSION_INFO_IMAGE.equals(attachment.getFlag())){
                    return true;
                }
            }
        }
        return false;
    }

    public void saveResultMsg(Map map, VmBizInfo vmBizInfo, Attachment attachment) {
        AutoItsmResultmsg msg = new AutoItsmResultmsg();
        String status = (String) map.get("status");
        msg.setBusinessId(vmBizInfo.getVersionInfoId());
        msg.setBusinessNo(vmBizInfo.getVersionInfoNo());
        msg.setChangeReason(attachment.getChangeReason());
        msg.setStatus(status);
        msg.setStartTime((String) map.get("startTime"));
        msg.setEndTime((String) map.get("endTime"));
        msg.setResultId(UUID.getUUIDStr());
        // 复用该字段为状态码值
        msg.setResultMsg(String.valueOf(map.get("stateCode")));
        if (resultmsgService.insertAutoItsmResultmsg(msg) > 0) {
            log.debug("保存自动化结果信息成功............");
        }
    }
}
