package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.*;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.DutyScheduling;
import com.ruoyi.common.core.domain.entity.DutyTypeinfo;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 * 值班管理定时任务
 * @author ruoyi
 */
@Component("dutyTask")
public class DutyTask
{
    private static final Logger logger = LoggerFactory.getLogger(DutyTask.class);
    @Autowired
    private TaskLockManager taskLockManager;
    @Autowired
    private IDutySchedulingService dutySchedulingService;
    @Autowired
    private IPubBizSmsService pubBizSmsService;
    @Autowired
    private IPubBizPresmsService pubBizPresmsService;
    @Autowired
    private IDutyTypeinfoService dutyTypeinfoService;

    Stack<String> stack = new Stack<>();

    /**
     * 按规则（每天下午5.30）发送短信
     * 给值班人发送信息：
     */
    public void sendDutyTask() {

        if(taskLockManager.lock("sendDutyTask")){
            logger.debug("--------定时任务【sendDutyTask】执行开始--------");
            long start = System.currentTimeMillis();
            try {
                DutyScheduling dutyScheduling = new DutyScheduling();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.getNowDate());
                logger.debug("--------DateUtils.getNowDate()--------"+DateUtils.getNowDate());
                calendar.add(Calendar.DAY_OF_MONTH,1);
                Date date = calendar.getTime();
                String lastDate = DateUtils.dateTime(date);
                logger.debug("--------lastDate--------"+lastDate);

                dutyScheduling.setDutyDate(lastDate);
                String smsText = "";
                String typeName = "";
                List<DutyScheduling> list = dutySchedulingService.selectDutySchedulingList(dutyScheduling);
                if(null!=list&&list.size()>0){
                    for(DutyScheduling ds : list){
                        stack = jointTypeName(ds.getTypeinfoId());
                        StringBuilder stringBuilder = new StringBuilder();
                        while (!stack.isEmpty()){
                            stringBuilder.append(stack.pop()+"-");
                        }
                        if(stringBuilder.toString().length() >1){
                            typeName = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
                        }
                        smsText = "您好，您明天（"+lastDate+"）有值班信息，值班类别为:"+typeName+"，请查收。";
                        OgPerson person = new OgPerson();
                        person.setpName(ds.getPname());
                        person.setMobilPhone(ds.getMobilephone());
                        sendSms(person,smsText);
                    }
                    Thread.sleep(1500);
                }
            } catch (Exception e) {
                logger.error("sendDutyTask", e);
            }finally {
                taskLockManager.unlock("sendDutyTask");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【sendDutyTask】执行总时长【" + (end - start) + "】毫秒");
        }else {
            logger.debug("sendDutyTask - 任务已有其他服务执行...");
        }

    }

    public void sendSms(OgPerson person, String smsText) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(smsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("111103");// 检查对象
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    /**
     * 递归获取值班类型名称添加到队列
     * @param typeInfoId
     * @return
     */
    public Stack jointTypeName(String typeInfoId){
        if(StringUtils.isEmpty(typeInfoId)){
            return null;
        }
        DutyTypeinfo dutyTypeinfo = dutyTypeinfoService.selectDutyTypeinfoById(typeInfoId);
        if(dutyTypeinfo != null && !StringUtils.isEmpty(dutyTypeinfo.getParentId())){
            stack.push(dutyTypeinfo.getTypeName());
            jointTypeName(dutyTypeinfo.getParentId());
        }
        return stack;
    }
    
    
    
}
