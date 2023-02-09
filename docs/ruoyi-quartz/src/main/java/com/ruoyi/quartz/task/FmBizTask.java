package com.ruoyi.quartz.task;

import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IDifficultEventsService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务事件单自动关闭
 *
 * @author liuliang
 */
@Component("fmBizTask")
public class FmBizTask {

    private final String TIME_SOURCE = "2";
    private final String COUNT_SOURCE = "3";

    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    private IFmBizService fmBizService;
    @Autowired
    ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    IOgPersonService iOgPersonService;
    @Autowired
    ISysDeptService iSysDeptService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private TaskLockManager taskLockManager;
    @Autowired
    private IPubHolidayService iPubHolidayService;
    @Autowired
    private IDifficultEventsService iDifficultEventsService;

    @Value("${Antifraud.url}")
    private String AntifraudUrl;

    private String stepNumberLimit = "Step_Number_Limit";

    private static final Logger log = LoggerFactory.getLogger(FmBizTask.class);

    String format = "yyyyMMddHHmmss";

    //业务事件单自动关闭调度任务
    public void getPowerOffFmYw() {
        if (taskLockManager.lock("fmBizTask")) {
            long start = System.currentTimeMillis();
            try {
                log.debug("############业务事件单自动关闭定时任务执行开始#############");
                saveFlowClose();
                log.debug("############业务事件单自动关闭定时任务执行结束#############");
            } finally {
                taskLockManager.unlock("fmBizTask");
            }
            long end = System.currentTimeMillis();
            log.debug("--------定时任务【fmBizTask】执行总时长【" + (end - start) + "】毫秒");
        } else {
            log.debug("fmBizTask - 任务已有其他服务执行...");
        }
    }

    public void getFmBizByRequestFail() {
        if (taskLockManager.lock("getFmBizByRequestFail_Task")) {
            try {
                System.out.println("############业务事件单重发反欺诈系统定时任务执行开始#############");
                List<FmBiz> fmBizFaultinfos = fmBizService.getFmBizByRequestFail();
                if (!fmBizFaultinfos.isEmpty()) {
                    for (FmBiz fmBizFaultinfo : fmBizFaultinfos) {
                        fmBizService.sendAntifraud(fmBizFaultinfo, AntifraudUrl);
                    }
                }
                System.out.println("############业务事件单重发反欺诈系统定时任务执行结束#############");
            } finally {
                taskLockManager.unlock("getFmBizByRequestFail_Task");
            }
        } else {
            System.out.println("getFmBizByRequestFail任务已有其他服务执行...");
        }
    }

    public void saveFlowClose() {
        List<PubParaValue> vs = pubParaValueService.selectPubParaValueByParaName("faultclose");//拿到对应自动关闭天数
        PubParaValue pv = vs.get(0);
        int date5 = Integer.parseInt(pv.getValue());
        List<FmBiz> list2 = fmBizService.getPowerOffFmYw(date5);//查看满足条件的事件单
        if (!list2.isEmpty()) {
            for (FmBiz f : list2) {
                try {
                    f.setEvaluateResult("1");//评价结果
                    f.setEvaluate("未在" + date5
                            + "天内手动关闭，系统自动关闭！");//评价
                    f.setEvaluaterId(f.getCreateId());//评价人
                    f.setEvaluateTime(DateUtils.dateTimeNow("yyyyMMddHHmmss"));//关闭时间/评价时间
                    f.setCurrentState("09");//更新为关闭状态

                    //计算赋值处理总耗时
                    if (StringUtils.isNotEmpty(f.getToQgzxTime())) {
                        SimpleDateFormat sd = new SimpleDateFormat(format);
                        try {
                            long data1 = sd.parse(f.getDealTime()).getTime();
                            long data2 = sd.parse(f.getToQgzxTime()).getTime();
                            long l = data1 - data2;
                            f.setDealUseTime(l / 1000 + "");
                        } catch (Exception e) {
                            System.out.println("日期转换失败");
                            continue;
                        }
                    }
                    Map<String, Object> reMap = new HashMap<>();
                    reMap.put("businessKey", f.getFmId());
                    reMap.put("userId", f.getCreateId());
                    reMap.put("processDefinitionKey", "FmBiz");
                    reMap.put("comment", "未在" + date5
                            + "天内手动关闭，系统自动关闭！");
                    reMap.put("reCode", "0");
                    try {
                        fmBizService.updateFmBiz(f);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        throw new BusinessException("自动关闭业务事件单失败，单号：" + f.getFaultNo());
                    }
                    //如果是反欺诈相关调用反欺诈系统http接口发送事件单信息给对方

                    if ("1".equals(f.getIsAntiFraud())) {
                        fmBizService.sendAntifraud(f, AntifraudUrl);
                    }
                    fmBizService.calculationDealTime(f);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    log.debug("自动关闭业务事件单失败，单号是：" + f.getFaultNo());
                    continue;
                }
            }
        }
    }

    /**
     * 业务事件单定时转疑难
     * 1、流程步骤超过20
     * 2、超过2个工作如（5*24）时间未处理完的
     * 3、手工转疑难（点击转疑难按钮填写）
     */
    public void fmBizToYn() {
        if (taskLockManager.lock("fmBizToYnTask")) {
            try {
                log.info("############业务事件单转疑难任务执行开始#############");
                String order = pubParaValueService.selectPubParaValueByParaName(stepNumberLimit).get(0).getValue();//拿到配置的步骤数
                List<FmBiz> byFlowLogCountList = fmBizService.getFmBizByFlowLogCount(Integer.parseInt(order));//拿到所有超過20次的事件单
                String toQgzxTime = iPubHolidayService.getTimeByToQgzxTime(DateUtils.dateTimeNow("yyyy-MM-dd HH:mm:ss"));//拿到48小时之前的全国中心时间
                List<FmBiz> byToQgzxTimeList = fmBizService.getFmBizByToQgzxTime(DateUtils.handleTimeYYYYMMDDHHMMSS(toQgzxTime));//拿到超48小时未处理的事件单
                for (FmBiz fmBiz : byFlowLogCountList) {
                    iDifficultEventsService.insertDifficultEventsByFmBiz(fmBiz, COUNT_SOURCE);
                }
                for (FmBiz fmBiz : byToQgzxTimeList) {
                    iDifficultEventsService.insertDifficultEventsByFmBiz(fmBiz, TIME_SOURCE);
                }
                log.info("############业务事件单自动关闭定时任务执行结束#############");
            } finally {
                taskLockManager.unlock("fmBizToYnTask");
            }
        } else {
            log.debug("fmBizToYnTask - 任务已有其他服务执行...");
        }
    }
}
