package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.service.*;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 业务事件单监控统计
 *
 * @author liuliang
 */
@Component("fmBizMonitorTask")
public class FmBizMonitorTask {
    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    private IFmBizControlService fmBizControlService;
    @Autowired
    private IFmBizService fmBizService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    private IPubHolidayService pubHolidayService;
    @Autowired
    ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    private TaskLockManager taskLockManager;
    @Autowired
    private IPubBizSmsService pubBizSmsService;
    @Autowired
    private IOgGroupPersonService ogGroupPersonService;
    @Autowired
    private ISysWorkService sysWorkService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private IPubBizPresmsService pubBizPresmsService;
    @Autowired
    private IFmbizAndIssueService iFmbizAndIssueService;
    private static final Logger log = LoggerFactory.getLogger(FmBizMonitorTask.class);

    //业务事件单监控数据统计调度任务
    public void getFmBizMonitor() {
        if (taskLockManager.lock("fmBizMonitorTask")) {
            long start = System.currentTimeMillis();
            try {
                fmBizControlService.deleteAll();//删除全部数据

                int recHCount = 0;// 长时间未领取时间配置
                int recHCounts = 0;// 超3天无人领取时间配置
                int dealHCount = 0;// 长时间未处理时间配置
                int count = 0;// 不高效次数配置
                int StepNumber = 0;// 步骤上限数
                int jjHCount = 0;// 紧急处理时间配置
                String nowTime = DateUtils.dateTimeNow();
                List<PubParaValue> rDay = pubParaValueService.selectPubParaValueByParaName("relation_fmbiz_day");//关联问题单超时天数

                List<PubParaValue> val = pubParaValueService.selectPubParaValueByParaName("p_conf_fmkh_count");//不高效流转次数
                if (val.isEmpty()) {
                    throw new BusinessException("未配置业务事件单监控所需的不高效流转次数参数");
                } else {
                    count = Integer.valueOf(val.get(0).getValue());
                }
                List<PubParaValue> val2 = pubParaValueService.selectPubParaValueByParaName("Step_Number_Call");//流程步骤上限数参数
                if (val2.isEmpty()) {
                    throw new BusinessException("未配置业务事件单监控所需的流程步骤上限数参数");
                } else {
                    StepNumber = Integer.valueOf(val2.get(0).getValue());
                }
                List<PubParaValue> val3 = pubParaValueService.selectPubParaValueByParaName("p_conf_fmcontrol_jj");//紧急处理时间时长参数
                if (val3.isEmpty()) {
                    throw new BusinessException("未配置业务事件单监控所需的紧急处理时间时长参数");
                } else {
                    jjHCount = Integer.valueOf(val3.get(0).getValue());
                }

                List<PubParaValue> val4 = pubParaValueService.selectPubParaValueByParaName("p_conf_fmcontrol_receive");//长时间未领取参数
                if (val4.isEmpty()) {
                    throw new BusinessException("未配置业务事件单监控所需的长时间未领取参数");
                } else {
                    recHCount = Integer.valueOf(val4.get(0).getValue());
                }

                List<PubParaValue> val5 = pubParaValueService.selectPubParaValueByParaName("p_conf_fmcontrol_receives");//长时间未领取参数
                if (val5.isEmpty()) {
                    throw new BusinessException("未配置业务事件单监控所需的长时间未领取参数");
                } else {
                    recHCounts = Integer.valueOf(val5.get(0).getValue());
                }

                List<PubParaValue> val6 = pubParaValueService.selectPubParaValueByParaName("p_conf_fmcontrol_deal");//长时间未处理时长参数
                if (val6.isEmpty()) {
                    throw new BusinessException("未配置业务事件单监控所需的长时间未处理时长参数");
                } else {
                    dealHCount = Integer.valueOf(val6.get(0).getValue());
                }
                System.out.println("############业务事件单监控定时任务执行开始#############");
                getRelation(rDay.get(0).getValue());
                getNotRecFmBizs(nowTime, recHCount, recHCounts);//获取长时间未领取事件单
                getNotDealFmBizs(nowTime, dealHCount);
                getNotEfficientFmBizs(nowTime, count);
                getUntimelyResolution(nowTime);
                getStepNumber(StepNumber);
                getJjDealFmBizs(nowTime, jjHCount);
                getFmBizsResolution();
                log.debug("############业务事件单监控定时任务执行关闭#############");
            } finally {
                taskLockManager.unlock("fmBizMonitorTask");
            }
            long end = System.currentTimeMillis();
            log.debug("--------定时任务【fmBizMonitorTask】执行总时长【" + (end - start) + "】毫秒");
        } else {
            log.debug("fmBizMonitorTask - 任务已有其他服务执行...");
        }
    }

    public void getRelation(String day) {
        List<FmbizAndIssue> list = iFmbizAndIssueService.getRelationByDay(day);
        for (FmbizAndIssue fai : list) {
            FmBizControl fmBizControlDetail = new FmBizControl();
            fmBizControlDetail.setControlTime(DateUtils.dateTimeNow());//当前时间
            fmBizControlDetail.setFmBizId(fai.getFmId());//事件单ID
            fmBizControlDetail.setGroupId(fai.getGroupId());//工作组ID
            fmBizControlDetail.setSysId(fmBizService.selectFmBizById(fai.getFmId()).getSysid());//系统ID
            fmBizControlDetail.setUnusualType("9");// 类型：关联超时
            fmBizControlDetail.setControlId(UUID.getUUIDStr());
            fmBizControlService.insertFmBizControl(fmBizControlDetail);
        }
    }

    /**
     * 获取长时间未领取事件单
     *
     * @param nowTime    当前时间
     * @param recHCount  长时间未领取参数
     * @param recHCounts 超三天无人领取参数
     */
    public void getNotRecFmBizs(String nowTime, int recHCount, int recHCounts) {
        FmBiz fb = new FmBiz();
        fb.setCurrentState("03");
        fb.setInvalidationMark("1");
        List<FmBiz> list = fmBizService.selectFmBizListByMonitor(fb);//获取所有待领取状态的有效事件单
        PubFlowLog pubFlowLog = null;
        if (!list.isEmpty()) {
            for (FmBiz fmBiz : list) {
                List<PubFlowLog> flowLogList = pubFlowLogService.selectPubFlowLogDesc(fmBiz.getFmId());
                if (!flowLogList.isEmpty()) {
                    pubFlowLog = flowLogList.get(0);
                    if (StringUtils.isNotEmpty(pubFlowLog.getNextTaskNames())) {
                        if (pubFlowLog.getNextTaskNames().equals("事件单领取")) {
                            try {
                                String startTime = pubFlowLog.getPerformerTime();// 获取当前操作时间
                                long usedTimes = pubHolidayService.getWorkDaySysTimes(iSysApplicationManagerService.selectOgSysBySysId(fmBiz.getSysid()).getSysType(), startTime, nowTime);
                                if (usedTimes / (60 * 60) > recHCount) {
                                    // 为长时间未领取的事件单,存入业务事件单监控明细
                                    FmBizControl fmBizControlDetail = new FmBizControl();
                                    fmBizControlDetail.setControlTime(nowTime);//当前时间
                                    fmBizControlDetail.setFmBizId(fmBiz.getFmId());//事件单ID
                                    fmBizControlDetail.setGroupId(fmBiz.getGroupId());//工作组ID
                                    fmBizControlDetail.setSysId(fmBiz.getSysid());//系统ID
                                    fmBizControlDetail.setUnusualType("1");// 类型：长时间未领取
                                    fmBizControlDetail.setControlId(UUID.getUUIDStr());
                                    fmBizControlService.insertFmBizControl(fmBizControlDetail);
                                }
                                if (usedTimes / (60 * 60) > recHCounts) {
                                    // 为长时间未领取的事件单,存入业务事件单监控明细
                                    FmBizControl fmBizControlDetail = new FmBizControl();
                                    fmBizControlDetail.setControlTime(nowTime);//当前时间
                                    fmBizControlDetail.setFmBizId(fmBiz.getFmId());//事件单ID
                                    fmBizControlDetail.setGroupId(fmBiz.getGroupId());//工作组ID
                                    fmBizControlDetail.setSysId(fmBiz.getSysid());//系统ID
                                    fmBizControlDetail.setUnusualType("7");// 类型：长时间未领取
                                    fmBizControlDetail.setControlId(UUID.getUUIDStr());
                                    fmBizControlService.insertFmBizControl(fmBizControlDetail);
                                }
                            } catch (Exception e) {
                                log.debug("业务事件单长时间未领取统计失败单号为：" + fmBiz.getFaultNo());
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理不及时统计
     *
     * @param nowTime    当前时间
     * @param dealHCount 处理不及时参数
     */
    public void getNotDealFmBizs(String nowTime, int dealHCount) {
        FmBiz fb = new FmBiz();
        fb.setCurrentState("04");
        fb.setInvalidationMark("1");
        List<FmBiz> list = fmBizService.selectFmBizListByMonitor(fb);//获取所有处理中状态的有效事件单
        PubFlowLog flowLog = null;
        if (!list.isEmpty()) {
            for (FmBiz fmBiz : list) {
                List<PubFlowLog> flowLogList = pubFlowLogService.selectPubFlowLogDesc(fmBiz.getFmId());
                if (!flowLogList.isEmpty()) {
                    flowLog = flowLogList.get(0);// 获取事件单处理的上一步操作
                    if (StringUtils.isNotEmpty(flowLog.getNextTaskName())) {
                        if ("事件单处理".equals(flowLog.getNextTaskName())) {
                            String startTime = flowLog.getPerformerTime();// 获取事件单处理的上一步操作时间
                            try {
                                long usedTimes = pubHolidayService.getWorkDaySysTimes(iSysApplicationManagerService.selectOgSysBySysId(fmBiz.getSysid()).getSysType(), startTime, nowTime);
                                if (usedTimes / (60 * 60) > dealHCount) {
                                    // 为长时间未领取的事件单,存入业务事件单监控明细
                                    FmBizControl fmBizControlDetail = new FmBizControl();
                                    fmBizControlDetail.setControlTime(nowTime);//当前时间
                                    fmBizControlDetail.setFmBizId(fmBiz.getFmId());//事件单ID
                                    fmBizControlDetail.setGroupId(fmBiz.getGroupId());//工作组ID
                                    fmBizControlDetail.setSysId(fmBiz.getSysid());//系统ID
                                    fmBizControlDetail.setUnusualType("2");// 类型：长时间未处理
                                    fmBizControlDetail.setControlId(UUID.getUUIDStr());
                                    fmBizControlService.insertFmBizControl(fmBizControlDetail);
                                }
                            } catch (Exception e) {
                                log.info("业务事件单长时间未处理统计失败单号为：" + fmBiz.getFaultNo());
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * 不高效事件单统计
     *
     * @param nowTime 当前时间
     * @param count   不高效次数参数
     */
    public void getNotEfficientFmBizs(String nowTime, int count) {
        List<FmBiz> fmBizs = fmBizControlService.getNoEfficientFms(count);
        if (!fmBizs.isEmpty()) {
            for (FmBiz fmBiz : fmBizs) {
                // 不高效的事件单,存入业务事件单监控明细
                FmBizControl fmBizControlDetail = new FmBizControl();
                fmBizControlDetail.setControlTime(nowTime);//当前时间
                fmBizControlDetail.setFmBizId(fmBiz.getFmId());//事件单ID
                fmBizControlDetail.setGroupId(fmBiz.getGroupId());//工作组ID
                fmBizControlDetail.setSysId(fmBiz.getSysid());//系统ID
                fmBizControlDetail.setUnusualType("3");// 类型：不高效
                fmBizControlDetail.setControlId(UUID.getUUIDStr());
                fmBizControlService.insertFmBizControl(fmBizControlDetail);
            }
        }
    }

    /**
     * 解决不及时统计
     *
     * @param nowTime
     */
    public void getUntimelyResolution(String nowTime) {
        List<FmBiz> fmBizs = fmBizControlService.getUntimelyResolution();
        if (!fmBizs.isEmpty()) {
            for (FmBiz fmBiz : fmBizs) {
                List<PubFlowLog> flowLogList = pubFlowLogService.selectPubFlowLogDesc(fmBiz.getFmId());
                if (!flowLogList.isEmpty()) {
                    for (int i = 1; i < flowLogList.size(); i++) {
                        try {
                            if ("事件单领取".equals(flowLogList.get(i).getNextTaskName())) {
                                String startTime = flowLogList.get(i).getPerformerTime();// 获取当前操作时间
                                long usedTimes = pubHolidayService.getWorkDaySysTimes(iSysApplicationManagerService.selectOgSysBySysId(fmBiz.getSysid()).getSysType(), startTime, nowTime);
                                if (usedTimes / (60 * 60) > 8) {
                                    // 为解决不及时的事件单,存入业务事件单监控明细
                                    FmBizControl fmBizControlDetail = new FmBizControl();
                                    fmBizControlDetail.setControlTime(nowTime);//当前时间
                                    fmBizControlDetail.setFmBizId(fmBiz.getFmId());//事件单ID
                                    fmBizControlDetail.setGroupId(fmBiz.getGroupId());//工作组ID
                                    fmBizControlDetail.setSysId(fmBiz.getSysid());//系统ID
                                    fmBizControlDetail.setUnusualType("4");// 类型：解決不及時
                                    fmBizControlDetail.setControlId(UUID.getUUIDStr());
                                    fmBizControlService.insertFmBizControl(fmBizControlDetail);
                                }
                            }
                        } catch (Exception e) {
                            log.info("业务事件单长时间未处理统计失败单号为：" + fmBiz.getFaultNo());
                            continue;
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 流程步骤上限统计
     *
     * @param StepNumber 步骤上限数参数
     */
    public void getStepNumber(int StepNumber) {
        List<FmBiz> fmBizs = fmBizControlService.getUntimelyResolution();
        if (!fmBizs.isEmpty()) {
            for (FmBiz fmBiz : fmBizs) {
                List<PubFlowLog> flowLogList = pubFlowLogService.selectPubFlowLogDesc(fmBiz.getFmId());
                if (flowLogList.size() > StepNumber) {
                    // 为解决不及时的事件单,存入业务事件单监控明细
                    FmBizControl fmBizControlDetail = new FmBizControl();
                    fmBizControlDetail.setControlTime(DateUtils.dateTimeNow());//当前时间
                    fmBizControlDetail.setFmBizId(fmBiz.getFmId());//事件单ID
                    fmBizControlDetail.setGroupId(fmBiz.getGroupId());//工作组ID
                    fmBizControlDetail.setSysId(fmBiz.getSysid());//系统ID
                    fmBizControlDetail.setUnusualType("5");// 类型：解決不及時
                    fmBizControlDetail.setControlId(UUID.getUUIDStr());
                    fmBizControlService.insertFmBizControl(fmBizControlDetail);
                }
            }
        }
    }

    /**
     * 紧急事件单处理统计
     *
     * @param nowTime
     * @param jjHCount
     */
    public void getJjDealFmBizs(String nowTime, int jjHCount) {
        List<FmBiz> fmBizs = fmBizControlService.getJjDealFmBizs();
        if (!fmBizs.isEmpty()) {
            for (FmBiz fmBiz : fmBizs) {
                try {
                    String startTime = fmBiz.getToQgzxTime();
                    long usedTimes = pubHolidayService.getWorkDaySysTimes(iSysApplicationManagerService.selectOgSysBySysId(fmBiz.getSysid()).getSysType(), startTime, nowTime);// 获取去除非工作时间的时长（秒）
                    if (usedTimes / (60 * 60) > jjHCount) {
                        // 为需要紧急处理的事件单,存入业务事件单监控明细
                        FmBizControl fmBizControlDetail = new FmBizControl();
                        fmBizControlDetail.setControlTime(DateUtils.dateTimeNow());//当前时间
                        fmBizControlDetail.setFmBizId(fmBiz.getFmId());//事件单ID
                        fmBizControlDetail.setGroupId(fmBiz.getGroupId());//工作组ID
                        fmBizControlDetail.setSysId(fmBiz.getSysid());//系统ID
                        fmBizControlDetail.setUnusualType("6");// 类型：紧急处理
                        fmBizControlDetail.setControlId(UUID.getUUIDStr());
                        fmBizControlService.insertFmBizControl(fmBizControlDetail);
                    }
                } catch (Exception e) {
                    log.info("紧急处理不及时单号为：" + fmBiz.getFmId() + " 的业务事件单获取失败");
                }
            }
        }

    }

    /**
     * 获取进入全国中心待领取、处理中状态超过72小时的事件单
     */
    public void getFmBizsResolution() {

        List<FmBiz> fmBizs = fmBizControlService.getTimeResolution(3);
        if (!fmBizs.isEmpty()) {
            for (FmBiz fmBiz : fmBizs) {
                //添加全国中心超过772小时事件单数据
                FmBizControl fmBizControlDetail = new FmBizControl();
                fmBizControlDetail.setControlTime(DateUtils.dateTimeNow());//当前时间
                fmBizControlDetail.setFmBizId(fmBiz.getFmId());//事件单ID
                fmBizControlDetail.setGroupId(fmBiz.getGroupId());//工作组ID
                fmBizControlDetail.setSysId(fmBiz.getSysid());//系统ID
                fmBizControlDetail.setUnusualType("8");// 类型：全国中心超72小时
                fmBizControlDetail.setControlId(UUID.getUUIDStr());
                fmBizControlService.insertFmBizControl(fmBizControlDetail);
            }
        }
    }

    public void sendFmBizGroupGios() {
        if (taskLockManager.lock("sendFmBizGroupGiosTask")) {
            try {
                System.out.println("############定时发送短信提醒工作组组长事件单未处理数量定时任务执行开始#############");
                List<FmBizCDealerD> list = fmBizService.getGroupCount();//拿到未处理完事件单
                if (!list.isEmpty()) {
                    for (FmBizCDealerD fcd : list) {
                        //根据工作组ID拿到组长
                        OgGroup group = sysWorkService.selectOgGroupById(fcd.getGroupName());
                        if (group != null) {
                            OgGroupPerson gp = new OgGroupPerson();
                            gp.setGpOsition("1");
                            gp.setGroupId(fcd.getGroupName());
                            List<OgGroupPerson> gpList = ogGroupPersonService.selectOgGroupPersonList(gp);
                            if (!gpList.isEmpty()) {
                                for (OgGroupPerson ogp : gpList) {
                                    OgPerson person = ogPersonService.selectOgPersonById(ogp.getPid());
                                    if (person != null) {
                                        PubBizPresms pbp = new PubBizPresms();
                                        pbp.setTelephone(person.getMobilPhone());// 手机号
                                        pbp.setName(person.getpName());// 姓名
                                        pbp.setPubBizPresmsId(UUID.getUUIDStr());
                                        pbp.setSmsState("4");
                                        pbp.setInvalidationMark("1");
                                        pbp.setInspectTime(DateUtils.dateTimeNow());
                                        pbp.setSmsText(group.getGrpName() + "还有" + fcd.getNotDealCount() + "个事件单未处理,其中数据变更处理的有" + fcd.getBySjCount() + "个。");
                                        pubBizPresmsService.insertPubBizPresms(pbp);
                                        pubBizSmsService.findPreSmsAndSend(pbp);
                                    }
                                }
                            }
                        }
                    }
                }
                System.out.println("############定时发送短信提醒工作组组长事件单未处理数量定时任务执行结束#############");
            } finally {
                taskLockManager.unlock("sendFmBizGroupGiosTask");
            }
        } else {
            System.out.println("sendFmBizGroupGiosTask - 任务已有其他服务执行...");
        }
    }

    /**
     * 用于测试定时任务是否执行--此方法不要删除  cuicc
     */
    public void testTask() {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "-----执行testTask方法");
    }
}
