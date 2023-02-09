package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.ICmBizSingleService;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.CmBizSingle;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component("cmBizTask")
public class CmBizTask {
    private static final Logger logger = LoggerFactory.getLogger(CmBizTask.class);
    @Autowired
    private ICmBizSingleService cmBizSingleService;
    @Autowired
    private IPubBizSmsService pubBizSmsService;
    @Autowired
    private IPubBizPresmsService pubBizPresmsService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private TaskLockManager taskLockManager;
    @Autowired
    private ISysDeptService iSysDeptService;

    /**
     * 按规则发送短信
     * 状态待实施：
     * 1、当前时间>计划实施结束时间时，立马发送短信，发送间隔20分钟。
     * 2、当前时间<计划实施结束时间时，如果计划实施结束时间-当前时间<4小时，开始发送短信，发送间隔20分钟
     * （待实施的  计划实施结束时间-当前时间<4小时 就发）
     * 给 变更发起人、处长、配置人员。（配置人员按机构进行参数配置）
     * 不区分工作时间
     */
    public void sendSmsToCmBiz() {
        if (taskLockManager.lock("sendSmsToCmBiz")) {
            long start = System.currentTimeMillis();
            try {
                CmBizSingle cbs = new CmBizSingle();
                cbs.setInvalidationMark("1");
                cbs.setChangeSingleStauts("0500");
                List<CmBizSingle> list = cmBizSingleService.selectCmBizSingleList(cbs);
                if (!list.isEmpty()) {
                    for (CmBizSingle cmbiz : list) {
                        OgOrg org = iSysDeptService.selectDeptById(cmbiz.getChangeOrg());

                        if (org.getLevelCode().contains("/000000000/010000888/")) {//是否为全国中心变更单
                            String expTime = cmbiz.getExpectEndTime();//从对象中拿到时间
                            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long startTime = df1.parse(expTime).getTime();//实施结束时间+18小时
                            long endTime = System.currentTimeMillis();//当前时间
                            long nd = 1000 * 24 * 60 * 60;
                            long nh = 1000 * 60 * 60;
                            long nm = 1000 * 60;
                            long diff = ((startTime + 86400000) - endTime);
                            long hour = diff / nh;//判断时间差(小时)
                            long min = diff % nd % nh / nm;//实际时间差(分钟)
                            if (endTime > (startTime + 64800000)) {//当前时间>实施结束时间+18小时
                                logger.debug("变更单定时任务开始时间" + expTime + "，当前时间是：" + DateUtils.dateTimeNow("yyyy-MM-dd HH:mm:ss") + "，相差" + hour + "小时" + min + "分钟");
                                String smsText = "资源变更单号：" + cmbiz.getChangeCode() + "，标题：" + cmbiz.getChangeSingleName() + "，发起人：" + cmbiz.getApplicantName() + "，距离关闭还有" + hour + "小时" + min + "分钟" + "时间,请及时关闭。";
                                OgPerson person1 = ogPersonService.selectOgPersonById(cmbiz.getApplicantId());//申请人
                                if (person1 != null) {
                                    sendSms(person1, smsText);//发送变更申请人
                                }
                                OgPerson person2 = ogPersonService.selectOgPersonById(cmbiz.getCheckerId());//处长
                                if (person2 != null) {
                                    sendSms(person2, smsText);//发送处长
                                }
                                String orgId = ogPersonService.selectOgPersonById(cmbiz.getCheckerId()).getOrgId();//根据配置查询到的人
                                OgUser user = new OgUser();
                                user.setPostId(1001L);
                                user.setOrgId(orgId);
                                List<OgUser> list2 = iSysUserService.selectPostAndOrgByUser(user);
                                if (!list2.isEmpty()) {
                                    for (OgUser u : list2) {
                                        OgPerson person3 = ogPersonService.selectOgPersonById(u.getpId());
                                        if (person3 != null) {
                                            sendSms(person3, smsText);//发送配置的人
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                taskLockManager.unlock("sendSmsToCmBiz");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【sendSmsToCmBiz】执行总时长【"+(end-start)+"】毫秒");
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
