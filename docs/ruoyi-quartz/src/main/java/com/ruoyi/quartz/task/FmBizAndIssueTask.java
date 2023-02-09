package com.ruoyi.quartz.task;

import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IFmbizAndIssueService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.FmbizAndIssue;
import com.ruoyi.common.core.domain.entity.OgGroupPerson;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysWorkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("FmBizAndIssueTask")
public class FmBizAndIssueTask {

    private static final Logger log = LoggerFactory.getLogger(FmBizAndIssueTask.class);
    @Autowired
    private TaskLockManager taskLockManager;
    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    private IFmbizAndIssueService iFmbizAndIssueService;
    @Autowired
    private ISysWorkService iSysWorkService;
    @Autowired
    private IFmBizService fmBizService;
    @Autowired
    private IOgPersonService iOgPersonService;

    public void getRelationFmBiz() {
        if (taskLockManager.lock("FmBizAndIssueTask")) {
            log.info("---------------查询超过天数未关联事件单开始");
            try {
                List<PubParaValue> vs = pubParaValueService.selectPubParaValueByParaName("relation_fmbiz_day");
                PubParaValue pv = vs.get(0);
                int date5 = Integer.parseInt(pv.getValue());//从配置参数中拿到超时天数
                List<FmbizAndIssue> list = iFmbizAndIssueService.getFmBizAndIssueByDay(date5);//拿到满足条件的数据
                if (!list.isEmpty()) {
                    for (FmbizAndIssue fai : list) {
                        sendSmsbiz(fai);
                    }
                }
            } finally {
                taskLockManager.unlock("FmBizAndIssueTask");
            }
            log.info("---------------查询超过5天未关联事件单发送短信结束");
        } else {
            log.info("查询超过5天未关联事件单任务已有其他服务执行...");
        }
    }

    public void sendSmsbiz(FmbizAndIssue fai) {
        String groupId = fai.getGroupId();
        String grpName = iSysWorkService.selectOgGroupById(groupId).getGrpName();
        List<OgGroupPerson> gps = iSysWorkService.selectGroupIdByPersonList(groupId);
        if (!gps.isEmpty()) {
            List<OgPerson> ops = new ArrayList<OgPerson>();
            for (OgGroupPerson gp : gps) {
                String gposition = gp.getGpOsition();//1组长 2数据中心负责人 3组员
                if ("1".equals(gposition)) {
                    ops.add(gp.getPerson());
                }
            }
            for (OgPerson op : ops) {
                //发送短信
                OgPerson person = iOgPersonService.selectOgPersonEvoById(op.getpId());// 获取短信接收人
                String setSmstext = "您好！" + grpName + "有" + fai.getCcount() + "个事件单超时未关联问题单，请关注。";// 短信内容
                fmBizService.sendSms(setSmstext, person);
            }
        }
    }
}
