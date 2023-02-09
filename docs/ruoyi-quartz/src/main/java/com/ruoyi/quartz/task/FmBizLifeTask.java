package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.service.IFmBizLifeService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmBizLife;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IPubHolidayService;
import com.ruoyi.system.service.IPubParaValueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.List;

/**
 * 业务事件单效率统计
 *
 * @author liuliang
 */
@Component("fmBizLifeTask")
public class FmBizLifeTask {

    private static final Logger log = LoggerFactory.getLogger(FmBizLifeTask.class);

    @Autowired
    private IFmBizLifeService fmBizLifeService;
    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    private IPubHolidayService pubHolidayService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    private IFmBizService fmBizService;
    @Autowired
    private TaskLockManager taskLockManager;

    //业务事件单效率统计调度任务
    public void getLifeCount() {
        log.debug("############业务事件单处理效率定时任务执行开始#############");
        if (taskLockManager.lock("fmBizLifeTask")) {
            long start = System.currentTimeMillis();
            try {
                fmBizLifeService.deleteAll();//刪除所有效率表中数据

                String endTime = DateUtils.getToday("yyyy-MM-dd HH:mm:ss");// 拿到当前时间
                String enDtime = DateUtils.getToday("yyyyMMddHHmmss");// 拿到当前时间

                List<PubParaValue> vs = pubParaValueService.selectPubParaValueByParaName("FmBizLife");//拿到对应自动关闭天数
                String eTime = "";//8小时
                String sTime = "";//16小时
                String tTime = "";//24小时
                if (!vs.isEmpty()) {
                    for (PubParaValue ppv : vs) {
                        if ("8小时".equals(ppv.getValueDetail())) {
                            eTime = ppv.getValue();
                        }
                        if ("16小时".equals(ppv.getValueDetail())) {
                            sTime = ppv.getValue();
                        }
                        if ("24小时".equals(ppv.getValueDetail())) {
                            tTime = ppv.getValue();
                        }
                    }
                }
                int eHour = Integer.parseInt(eTime);
                int sHour = Integer.parseInt(sTime);
                int tHour = Integer.parseInt(tTime);
                String a1 = pubHolidayService.getdateBefore(eHour, endTime);
                String a2 = pubHolidayService.getdateBefore(sHour, endTime);
                String a3 = pubHolidayService.getdateBefore(tHour, endTime);
                String eStartTime = a1.replaceAll("[[\\s-:punct:]]", "");
                String sStartTime = a2.replaceAll("[[\\s-:punct:]]", "");
                String tStartTime = a3.replaceAll("[[\\s-:punct:]]", "");
                saveSysLifeCount(eStartTime, sStartTime, tStartTime, enDtime);// 系统的添加
                log.debug("############业务事件单处理效率定时任务执行结束#############");
            } finally {
                taskLockManager.unlock("fmBizLifeTask");
            }
            long end = System.currentTimeMillis();
            log.debug("--------定时任务【fmBizLifeTask】执行总时长【"+(end-start)+"】毫秒");
        } else {
            log.debug("fmBizLifeTask - 任务已有其他服务执行...");
        }
    }

    public void saveSysLifeCount(String eStartTime, String sStartTime,
                                 String tStartTime, String endTime) {
        List<FmBizLife> record = fmBizLifeService.getSysCount();// 拿到剩余的
        List<FmBizLife> list = fmBizLifeService.getSysDealCount(eStartTime,
                sStartTime, tStartTime, endTime);// 拿到处理的
        String enCount = "";
        String snCount = "";
        String tnCount = "";

        if (!record.isEmpty()) {
            for (FmBizLife fb : record) {// 遍历剩余事件单
                String sysid = fb.getSysId();// 拿到公司ID和剩余数
                String surCount = fb.getSurCount();
                // start８小时的处理　未处理　效率赋值计算
                List<FmBiz> list1 = fmBizService
                        .getSysNDealCount(sysid);
                if (!list1.isEmpty()) {
                    int c1 = 0;
                    int c2 = 0;
                    int c3 = 0;
                    PubFlowLog flowLog = null;
                    for (FmBiz fmBiz : list1) {
                        try {
                            List<PubFlowLog> flowLogList = pubFlowLogService.selectPubFlowLogAll(fmBiz.getFmId());

                            if (!flowLogList.isEmpty()) {
                                flowLog = flowLogList.get(0);
                                if (StringUtils.isNotEmpty(flowLog.getNextTaskName())) {
                                    if ("事件单领取".equals(flowLog.getNextTaskName())) {
                                        String starttime = flowLogList.get(0)
                                                .getPerformerTime();// 获取事件单处理的上一步操作时间
                                        if (StringUtils.isNotEmpty(starttime)) {

                                            if (Long.parseLong(starttime) < Long
                                                    .parseLong(eStartTime)) {
                                                c1++;
                                            }
                                            if (Long.parseLong(starttime) < Long
                                                    .parseLong(sStartTime)) {
                                                c2++;
                                            }
                                            if (Long.parseLong(starttime) < Long
                                                    .parseLong(tStartTime)) {
                                                c3++;
                                            }
                                        }

                                        // 2.处理 -- 处理
                                    } else if ("事件单处理".equals(flowLog.getNextTaskName())) {
                                        for (int n = 1; n < flowLogList.size(); n++) {

                                            if ("事件单领取".equals(flowLogList.get(n).getNextTaskName())) {

                                                String starttime = flowLogList.get(n)
                                                        .getPerformerTime();
                                                if (StringUtils.isNotEmpty(starttime)) {

                                                    if (Long.parseLong(starttime) < Long
                                                            .parseLong(eStartTime)) {
                                                        c1++;
                                                    }
                                                    if (Long.parseLong(starttime) < Long
                                                            .parseLong(sStartTime)) {
                                                        c2++;
                                                    }
                                                    if (Long.parseLong(starttime) < Long
                                                            .parseLong(tStartTime)) {
                                                        c3++;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.debug("领取不及时单号为：" + fmBiz.getFmId() + " 的业务事件单获取失败");
                        }
                    }
                    enCount = Integer.toString(c1);
                    snCount = Integer.toString(c2);
                    tnCount = Integer.toString(c3);
                } else {
                    enCount = "0";
                    snCount = "0";
                    tnCount = "0";
                }
                String eCount = "0";
                String sCount = "0";
                String tCount = "0";

                for (FmBizLife fbl2 : list) {
                    if (sysid.equals(fbl2.getSysId())) {
                        eCount = fbl2.getEightDealCount();
                        sCount = fbl2.getSixteenDealCount();
                        tCount = fbl2.getTtfourDealCount();
                    }
                }
                int a1 = Integer.parseInt(eCount);
                int b1 = Integer.parseInt(surCount);
                int d1 = Integer.parseInt(enCount);
                int e1 = a1 + b1;
                int a2 = Integer.parseInt(sCount);
                int d2 = Integer.parseInt(snCount);
                int e2 = a2 + b1;
                int a3 = Integer.parseInt(tCount);
                int d3 = Integer.parseInt(tnCount);
                int e3 = a3 + b1;
                String edLife;
                String sdLife;
                String tdLife;
                if (d1 != 0) {
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    numberFormat.setMaximumFractionDigits(2);
                    edLife = numberFormat
                            .format((1 - (float) d1 / (float) e1) * 100);
                } else {
                    edLife = "100";
                }
                if (d2 != 0) {
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    numberFormat.setMaximumFractionDigits(2);
                    sdLife = numberFormat
                            .format((1 - (float) d2 / (float) e2) * 100);
                } else {
                    sdLife = "100";
                }
                if (d3 != 0) {
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    numberFormat.setMaximumFractionDigits(2);
                    tdLife = numberFormat
                            .format((1 - (float) d3 / (float) e3) * 100);
                } else {
                    tdLife = "100";
                }
                FmBizLife fmBizLife = new FmBizLife();
                fmBizLife.setFmLifeId(UUID.getUUIDStr());
                fmBizLife.setSysId(sysid);
                fmBizLife.setSurCount(surCount);
                fmBizLife.setEightDealCount(eCount);//8小时处理
                System.out.println("8小时未处理数量是：" + enCount);
                fmBizLife.setEightNdealCount(enCount);//8小时未处理
                fmBizLife.setEightDealLife(edLife + "%");//8小时效率
                fmBizLife.setSixteenDealCount(sCount);// 16小时处理
                fmBizLife.setSixteenNdealCount(snCount);// 16小时未处理
                fmBizLife.setSixteenDealLife(sdLife + "%");// 16小时效率
                fmBizLife.setTtfourDealCount(tCount);// 24小时处理
                fmBizLife.setTtfourNdealCount(tnCount);// 24小时未处理
                fmBizLife.setTtfourDealLife(tdLife + "%");// 24小时效率
                fmBizLife.setFlag("1");
                fmBizLife.setDeptLev("2");
                fmBizLife.setInvalidationMark("1");
                fmBizLife.setStatisticalTime(DateUtils.dateTimeNow());
                fmBizLifeService.insertFmBizLife(fmBizLife);
            }
        }

    }
}
