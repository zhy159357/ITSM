package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.CmBizSingleSJVo;
import com.ruoyi.activiti.domain.OgRPywRywcz;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.CmBizSingleSJService;
import com.ruoyi.activiti.service.IOgRPywRywczService;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("sjbgTask")
public class SjbgTask {
    private static final Logger logger = LoggerFactory.getLogger(SjbgTask.class);
    @Autowired
    private IPubBizSmsService pubBizSmsService;
    @Autowired
    private IPubBizPresmsService pubBizPresmsService;
    @Autowired
    private TaskLockManager taskLockManager;
    @Autowired
    private IOgRPywRywczService iOgRPywRywczService;
    @Autowired
    private CmBizSingleSJService cmBizSingleSJService;
    @Autowired
    private IOgPersonService iOgPersonService;

    /**
     * 按规则发送短信
     * 状态待实施：
     */
    public void sendSmsToCmBiz2() {
        if (taskLockManager.lock("sendSmsToSjbg")) {

            String smsText = "";
            long start = System.currentTimeMillis();
            try {
                //查询的是人的数量  去重过得
                List<OgRPywRywcz> ogRPywRywczs = iOgRPywRywczService.selectOgRPywRywczList(new OgRPywRywcz());
                //第一个判断是否为空 为空就不发
                if (!ogRPywRywczs.isEmpty()) {
                    for (OgRPywRywcz list : ogRPywRywczs) {
                        try {
                            //单子的集合
                            List<CmBizSingleSJVo> cmBizSingleSJVos = cmBizSingleSJService.sjbgTask(list.getCzName());
                            if (!cmBizSingleSJVos.isEmpty()) {
                                // 发送短信   cmBizSingleSJVos.size()单子的数量
                                smsText = "您好，您处室有" + cmBizSingleSJVos.size() + "个数据变更单待业务审批，特此提醒！请协助督促。";
                                // 获取短信接收人
                                OgPerson person = iOgPersonService.selectOgPersonByPhone(list.getCzPhone());
                                if (null != person) {
                                    sendSms(person, smsText);
                                } else {
                                    logger.debug("*****************pserson is null**********************");
                                }
                            }
                        } catch (Exception e) {
                            logger.error("sendSmsToCmBiz2.sendSmsToSjbg", e);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("sendSmsToCmBiz2", e);
            } finally {
                taskLockManager.unlock("sendSmsToSjbg");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【sendSmsToSjbg】执行总时长【" + (end - start) + "】毫秒");
        } else {
            logger.debug("sendSmsToCmBiz - 任务已有其他服务执行...");
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
}
