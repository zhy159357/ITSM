package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.*;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 例行变更计划自动发送短信
 *
 * @author st
 */
@Component("SmBizLxbgTask")
public class SmBizLxbgTask {
    @Autowired
    ISysApplicationManagerService iSysApplicationManagerService;

    @Autowired
    IOgPersonService iOgPersonService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISmBizTaskinfoService smBizTaskinfoService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private com.ruoyi.system.service.ISysWorkService ISysWorkService;

    @Autowired
    private AddlxbgService addlxbgService;

    @Autowired
    private TaskLockManager taskLockManager;

    private static final Logger log = LoggerFactory.getLogger(SmBizLxbgTask.class);


    //例行变更计划发送短信
    public void getSmLxbg() {
        if (taskLockManager.lock("SmBizLxbgTask")) {
            long start = System.currentTimeMillis();
            try {
                log.debug("############例行变更定时任务执行开始#############");
                pubLishTask();
                log.debug("############例行变更定时任务执行结束#############");
            } finally {
                taskLockManager.unlock("SmBizLxbgTask");
            }
            long end = System.currentTimeMillis();
            log.debug("--------定时任务【SmBizLxbgTask】执行总时长【"+(end-start)+"】毫秒");
        } else {
            log.debug("SmBizLxbgTask - 任务已有其他服务执行...");
        }
    }


    public void pubLishTask() {

        List<SmBizTaskinfo> list = smBizTaskinfoService.selectPubLishTask();
        try {
            if (!list.isEmpty()) {
                for (SmBizTaskinfo st : list) {
                    //获取计划日期
                    String publishTime = st.getPerformDate();
                    //获取当前时间
                    String today = DateUtils.dateTimeNow();
                    if(Long.parseLong(today)>=Long.parseLong(publishTime)){
                        publishTaskSub(st);
                    }

                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return;
        }

    }

    private void publishTaskSub(SmBizTaskinfo task) {

        //未填报
        task.setTaskFormStatus("02");
        //发布时间
        task.setPublishTime(DateUtils.dateTimeNow("yyyyMMddHHmmss"));
        SmBizScheduling smBizScheduling = new SmBizScheduling();
        smBizScheduling.setEffectiveTime(task.getPublishTime());
        String scid = task.getSmBizScheduling().getSchedulingId();
        smBizScheduling.setSchedulingId(scid);
        //此时计划单状态变为已发布
        smBizScheduling.setPlanStatus("05");

        addlxbgService.updatelxbg(smBizScheduling);

        smBizTaskinfoService.updateSmBizTaskinfo(task);

        //给所选机构或工作组下的人发送短信
        String sendSms = taskService.selectTaskById(task.getTaskId()).getMsgDoor();
        if (StringUtils.isNotEmpty(sendSms) && "1".equals(sendSms)) {
            //开始发送短信

            //根据任务查询出作业
            SmBizTask smBizTask = taskService.selectTaskById(task.getTaskId());

            //根据机构生成执行机构id
            String masterOrgId=smBizTask.getPerformDeptId()==null?"":smBizTask.getPerformDeptId().toString();
            String[] masterOrgIds=masterOrgId.split(",");
            //根据工作组生成工作组id
            String workGroup=smBizTask.getPerformGroupId()==null?"":smBizTask.getPerformGroupId().toString();
            String[] workGroups=workGroup.split(",");

            //根据所选机构发送短信
            if(masterOrgIds.length>0){
                for(String mId:masterOrgIds){
                    if(StringUtils.isNotEmpty(mId)){
                        //根据机构id查询人员且人员角色为：例行变更计划执行人
                        List<OgPerson> ogPerson = iOgPersonService.selectLxbgOgPersonOrgById(mId);// 获取短信接收人
                        String smsText = "任务单号：" + task.getTaskFromNo() + "；任务单标题：" + smBizTask.getTaskTitle() + "；任务已发布，请知晓。";

                        //遍历所选机构的人员
                        if(ogPerson != null){
                            for(OgPerson person : ogPerson){
                                sendSms(person, smsText);//发送短信给所选的机构人员
                            }
                        }
                    }
                }
            }

            //根据所选工作组发送短信
            if(workGroups.length>0){
                for(String wId:workGroups){
                    if(StringUtils.isNotEmpty(wId)){
                        //根据工作组id查询人员
                        List<OgPerson> ogPeople = ISysWorkService.selectLxbgGroupByPerson(wId);// 获取短信接收人
                        String smsText = "任务单号：" + task.getTaskFromNo()+ "；任务单标题：" + smBizTask.getTaskTitle() + "；任务已发布，请知晓。";
                        //遍历所选工作组的人员
                        if(ogPeople.size()>0){
                            for(OgPerson person : ogPeople){
                                sendSms(person, smsText);//发送短信给所选的工作组人员
                            }
                        }
                    }
                }
            }
        }
    }


    public void sendSms(OgPerson person, String smsText) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(smsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("030100");// 检查对象
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);
        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

}
