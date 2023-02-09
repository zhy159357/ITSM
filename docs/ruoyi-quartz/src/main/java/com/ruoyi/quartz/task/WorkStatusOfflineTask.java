package com.ruoyi.quartz.task;

import com.ruoyi.activiti.constants.WorkStatusConstants;
import com.ruoyi.activiti.domain.ItsmWorkStatus;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.activiti.service.ItsmWorkStatusService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysUserOnlineService;
import com.ruoyi.system.service.impl.PubHolidayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 定时任务判断系统用户是否离线状态
 *
 * @author zhangchao
 */
@Component("offlineTask")
public class WorkStatusOfflineTask {

    private final String taskName = "offlineTask";

    private final int checkHour = 2;

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Autowired
    private TaskLockManager taskLockManager;

    @Autowired
    private ISysUserOnlineService onlineService;

    @Autowired
    private ItsmWorkStatusService workStatusService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private PubHolidayServiceImpl holidayService;

    private static final Logger log = LoggerFactory.getLogger(WorkStatusOfflineTask.class);

    public void judgeUserOffline() {
        if (holidayService.isHoliday(DateUtils.getNowDate())) {
            log.debug("-------------当前日期属于节假日，不执行定时任务判断系统用户是否离线状态-------------");
            return;
        }
        if (taskLockManager.lock(taskName)) {
            long start = System.currentTimeMillis();
            try {
                log.debug("-------------定时任务判断系统用户是否离线状态开始-------------");
                //  当前时间-2小时 > 在线用户表最后访问时间
                final Date lastAccessTime = DateUtils.addHours(new Date(), 0 - checkHour);
                List<SysUserOnline> onlineList = onlineService.selectOnlineByExpired(lastAccessTime);
                if (!CollectionUtils.isEmpty(onlineList)) {
                    Set<String> set = removeRepeat(onlineList);
                    for (String userId : set) {
                        // 根据userId再去查询一次在线用户表，然后根据最后访问时间倒序排序，取第一个元素，然后取出最后访问系统时间与当前时间-2小时比较
                        SysUserOnline online = onlineService.selectOnlineByUserId(userId);
                        if (null != online) {
                            // 当前时间-2小时 > 数据库查询到的最后访问时间，需要发送短信。(否则例如 10点-2点=8点 < 数据库最后访问时间9点，则直接跳过本次循环)
                            if (null == online.getLastAccessTime() || lastAccessTime.before(online.getLastAccessTime())) {
                                continue;
                            }
                        } else {
                            continue;
                        }
                        ItsmWorkStatus workStatus = workStatusService.selectItsmWorkStatusByPid(userId);
                        // 不是正常签退状态 & 是数据中心人员或厂商人员  判断离线之后发送短信提示并修改工作状态表的状态
                        if ((null != workStatus && !StringUtils.isEmpty(workStatus.getWorkStatus())
                                && !WorkStatusConstants.qtzt.equals(workStatus.getWorkStatus()))
                                && workStatusService.isDataCenter(userId)) {
                            ItsmWorkStatus itsmWorkStatus = new ItsmWorkStatus();
                            itsmWorkStatus.setWorkStatus(WorkStatusConstants.lxzt);
                            itsmWorkStatus.setOperateTime(DateUtils.dateTimeNow());
                            itsmWorkStatus.setPid(userId);
                            int rows = workStatusService.updateItsmWorkStatus(itsmWorkStatus);
                            if (rows > 0) {
                                // 更新工作状态表成功后发送短信
                                OgPerson person = ogPersonService.selectOgPersonById(userId);
                                String msg = "尊敬的用户【" + person.getpName() + "】您好，距您最近一次操作运维管理系统已超过2小时，工作状态变更为【离线】，请及时刷新页面查看工作状态。";
                                vmBizInfoService.sendSms(msg, person);
                            }
                        }
                    }
                }
                // 发送完成线程休眠1秒时间，防止运行时间过快释放锁以后被其他服务读获取到锁
                Thread.sleep(1000);
                log.debug("-------------定时任务判断系统用户是否离线状态结束-------------");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("-------------定时任务判断系统用户是否离线状态失败，失败原因" + e.getMessage() + "-------------");
            } finally {
                taskLockManager.unlock(taskName);
            }
            long end = System.currentTimeMillis();
            log.debug("--------定时任务【offlineTask】执行总时长【" + (end - start) + "】毫秒");
        } else {
            log.debug("-----------offlineTask - 任务已有其他服务执行-------------");
        }
    }

    /**
     * 将集合中的所有userId取出放入set集合去重
     */
    public Set<String> removeRepeat(List<SysUserOnline> list) {
        Set<String> set = list.stream().map(sysUserOnline -> sysUserOnline.getUserId()).collect(Collectors.toSet());
        return set;
    }
}
