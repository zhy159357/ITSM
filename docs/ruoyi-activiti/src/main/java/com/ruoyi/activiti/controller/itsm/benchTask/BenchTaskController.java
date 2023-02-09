package com.ruoyi.activiti.controller.itsm.benchTask;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.ruoyi.activiti.domain.BenchTask;
import com.ruoyi.activiti.domain.BenchTaskGroup;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.BenchService;
import com.ruoyi.activiti.service.EventRunService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/benchTask")
public class BenchTaskController extends BaseController {
    private String prefixFmBiz = "redirect:/fmbiz";//业务事件单controller前缀
    private String prefixCmSj = "redirect:/bg/sjbg";//数据变更单controller前缀
    private String prefixVmBn = "redirect:/version/public";//版本发布controller前缀
    private String prefixCmBiz = "redirect:/system/single";//资源变更单controller前缀
    private String prefixImBiz = "redirect:/issueList/activiti";//应用问题单controller前缀
    private String prefixFmSw = "redirect:/trans/sw";//事务事件单controller前缀
    private String prefixFmYx = "redirect:/activiti/run";//运行事件单controller前缀
    private String prefixBGQQ = "redirect:/activiti/qingqiu";//变更请求单controller前缀
    private String prefixLXBG = "redirect:/lxbg/addlxbg";//例行变更单controller前缀
    private String prefixShLXBG = "redirect:/lxbg/auditlxbg";//审核例行变更单controller前缀
    private String prefixTbLXBG = "redirect:/lxbg/filllxbg";//填报例行变更单controller前缀
    private String prefixFmDd = "redirect:/dispatch/mydispatch";//调度请求单controller前缀
    private String prefixShFmDd = "redirect:/dispatch/audit";//审核调度请求单controller前缀
    private String prefixClFmDd = "redirect:/dispatch/dispose";//处理调度请求单controller前缀
    private String prefixFkFmDd = "redirect:/dispatch/feedback";//反馈调度请求单controller前缀
    private String prefixVM = "redirect:/system/notice";//工作通知controller前缀
    private String prefixVMC = "redirect:/system/noticeCheck";
    private String prefixVMS = "redirect:/system/noticeHandle";
    private String prefixAM = "redirect:/activiti/notice";//公告通知
    private String prefixFfSawo = "redirect:/sawo/dispense";//态势感知工单分发controller前缀
    private String prefixCzSawo = "redirect:/sawo/dispose";//态势感知工单处置controller前缀
    private String prefixYzSawo = "redirect:/sawo/verify";//态势感知工单验证controller前缀



    @Autowired
    private BenchService benchService;
    @Autowired
    private EventRunService eventRunService;
    @Autowired
    private ActivitiCommService activitiCommService;

    /**
     * 加载对应待办列表
     *
     * @param type 任务类型
     * @return
     */
    @PostMapping("/list/{type}")
    @ResponseBody
    public TableDataInfo getUserTasks(@PathVariable("type") String type) {
        String userId = ShiroUtils.getOgUser().getpId();
        if ("FmYx".equals(type)) {
            startPage();
            EventRun eventRun = new EventRun();
            eventRun.getParams().put("userId", userId);
            List<EventRun> list = eventRunService.selectActEventRunList(eventRun);
            List<BenchTask> endTaskVos = new ArrayList<>();
            EndTaskVo endTaskVo;
            for (EventRun ev : list) {
                BenchTask benchTask = new BenchTask();
                benchTask.setType("FmYx");//任务类型
                benchTask.setTaskId(ev.getParams().get("taskId").toString());//任务ID
                benchTask.setBizId(ev.getEventId());//业务ID
                benchTask.setNumber(ev.getEventNo());//单号
                benchTask.setTitle(ev.getEventTitle());//标题
                benchTask.setTaskName(ev.getParams().get("taskName").toString());//任务名称
                benchTask.setPrePerformTime(ev.getParams().get("taskCreateTime").toString());//上一步操作时间
                benchTask.setCurrentState(ev.getStatus());
                benchTask.setDescription(ev.getParams().get("description") == null ? "" : ev.getParams().get("description").toString());
                endTaskVos.add(benchTask);
            }
            return getDataTable(endTaskVos, list);
        }
        return benchService.getBenchPagerTasks(userId, type);
    }

    /**
     * 查询待办任务数量
     *
     * @return
     */
    @PostMapping("/getTaskCounts")
    @ResponseBody
    public TableDataInfo getTaskCounts() {
        List<BenchTaskGroup> list = new ArrayList<>();
        BenchTaskGroup btg = new BenchTaskGroup();
        String userId = ShiroUtils.getOgUser().getpId();
        TableDataInfo tdi = benchService.getBenchPagerTasks(userId, "FmBiz");//过滤掉存在转数据变更单在途的
        btg.setFmbizCount(tdi.getTotal());//业务事件单数量
        btg.setCmsjCount(benchService.getBenchPagerTasks(userId, "FmSJBG").getTotal());//数据变更单数量
        btg.setVmbnCount(benchService.getBenchPagerTasks(userId, "VmBn").getTotal());//发布管理数量
        btg.setCmbizCount(benchService.getBenchPagerTasks(userId, "CmZy").getTotal());//资源变更单数量
        btg.setImbizCount(benchService.getBenchPagerTasks(userId, "PMYY").getTotal());//应用问题单数量
        btg.setFmswCount(benchService.getBenchPagerTasks(userId, "FmSw").getTotal());//事务事件单数量
        Long fmyx = activitiCommService.userTaskCount(userId, "FmYx");
        btg.setFmyxCount(fmyx);//运行事件单数量
        btg.setCmqqCount(benchService.getBenchPagerTasks(userId, "BGQQ").getTotal());//变更请求单数量
        btg.setFmddCount(benchService.getBenchPagerTasks(userId, "FmDd").getTotal());//调度请求单数量
        btg.setLxbgCount(benchService.getBenchPagerTasks(userId, "LXBG").getTotal());//例行变更单数量
        btg.setSawoCount(benchService.getBenchPagerTasks(userId, "FMSAWO").getTotal());//态势感知工单数量
        list.add(btg);
        return getDataTable(list);
    }

    /**
     * 打开对应的待办任务
     *
     * @param type     任务类型
     * @param bizId    业务ID
     * @param taskId   任务ID
     * @param taskName 任务名称
     * @return
     */
    @GetMapping("/toTaskView")
    public String toTaskView(String type, String bizId, String taskId, String taskName) {
        String taskNameOld = taskName;
        try {
            taskName = URLDecoder.decode(taskName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "";
        if ("FmBiz".equals(type)) {//业务事件单
            if ("分派".equals(taskName))
                url = prefixFmBiz + "/passignss?id=" + bizId + "&&taskId=" + taskId + "&&flag=" + "1";
            if ("修改".equals(taskName))
                url = prefixFmBiz + "/edit/" + bizId + "/1";
            if ("事件单领取（省）".equals(taskName) || "事件单领取".equals(taskName))
                url = prefixFmBiz + "/receivess?id=" + bizId + "&&taskId=" + taskId + "&&flag=" + "1";
            if ("事件单处理（省）".equals(taskName) || "事件单处理".equals(taskName))
                url = prefixFmBiz + "/deal/" + bizId + "/1";
            if ("评价".equals(taskName))
                url = prefixFmBiz + "/close/" + bizId + "/1";
        }
        if ("FmSJBG".equals(type)) {//数据变更单
            if ("省处理".equals(taskName))
                url = prefixCmSj + "/dealEdit/" + bizId;
            if ("总行业务部门审核".equals(taskName))
                url = prefixCmSj + "/auditEdit/" + bizId;
            if ("修改".equals(taskName))
                url = prefixCmSj + "/returnedEdit/" + bizId;
            if ("处长审核变更单".equals(taskName))
                url = prefixCmSj + "/czauditEdit/" + bizId;
            if ("项目经理提供方案".equals(taskName))
                url = prefixCmSj + "/jlauditEdit/" + bizId;
            if ("实施".equals(taskName))
                url = prefixCmSj + "/approvalEdit/" + bizId;
            if ("关闭".equals(taskName))
                url = prefixCmSj + "/sjbggbEdit/" + bizId + "/1";
        }
        if ("VmBn".equals(type)) {//发布管理
            if ("安全审核".equals(taskName))
                url = prefixVmBn + "/security/authorApprove/" + bizId + "/" + taskId;
            if ("业务审核".equals(taskName))
                url = prefixVmBn + "/business/authorApprove/" + bizId + "/" + taskId;
            if ("业务主管审核".equals(taskName))
                url = prefixVmBn + "/business/leaderApprove/" + bizId + "/" + taskId;
            if ("技术审核".equals(taskName))
                url = prefixVmBn + "/technology/authorApprove/" + bizId + "/" + taskId;
            if ("技术主管审核".equals(taskName))
                url = prefixVmBn + "/technology/leaderApprove/" + bizId + "/" + taskId;
            if ("版本审核".equals(taskName))
                url = prefixVmBn + "/version/versionApproval/" + bizId + "/" + taskId;
            if ("运维审核".equals(taskName))
                url = prefixVmBn + "/operation/operationApproval/" + bizId + "/" + taskId;
            if ("发布审批".equals(taskName))
                url = prefixVmBn + "/release/releaseApproval/" + bizId + "/" + taskId;
            if ("紧急发布审批".equals(taskName))
                url = prefixVmBn + "/urgent/urgentApproval/" + bizId + "/" + taskId;
        }
        if ("CmZy".equals(type)) {//资源变更单
            if ("协同评估".equals(taskName))
                url = prefixCmBiz + "/assessor/" + bizId;
            if ("评估变更单".equals(taskName))
                url = prefixCmBiz + "/assessor/" + bizId;
            if ("修改变更单".equals(taskName))
                url = prefixCmBiz + "/edit/" + bizId;
            if ("审批变更单".equals(taskName))
                url = prefixCmBiz + "/check/" + bizId;
            if ("分管领导审批".equals(taskName))
                url = prefixCmBiz + "/check/" + bizId;
            if ("实施变更单".equals(taskName))
                url = prefixCmBiz + "/implementor/" + bizId;
        }
        if ("PMYY".equals(type)) { //应用问题单
            if ("inside_deal".equals(taskName) || "inside_yujiejue".equals(taskName) || "inside_jiejue".equals(taskName)) {
                url = prefixImBiz + "/goDataDeal/" + bizId + "/" + taskName;
            } else {
                url = prefixImBiz + "/view?issuefxId=" + bizId + "&&type=" + taskName;
            }
        }

        if ("FmSw".equals(type)) {//事务事件单
            if ("审核".equals(taskName)) {
                url = prefixFmSw + "/checkEdit/" + bizId;
            }
            if ("修改".equals(taskName)) {
                url = prefixFmSw + "/returnedEdit/" + bizId;
            }
            if ("授权".equals(taskName)) {
                url = prefixFmSw + "/authorEdit/" + bizId;
            }
            if ("处理".equals(taskName)) {
                url = prefixFmSw + "/dealEdit/" + bizId;
            }
            if("审核人2".equals(taskName)){
                url = prefixFmSw + "/checkEditTwo/" + bizId;
            }
            if("审核人3".equals(taskName)){
                url = prefixFmSw + "/checkEditThree/" + bizId;
            }
            if("审核人4".equals(taskName)){
                url = prefixFmSw + "/checkEditFour/" + bizId;
            }
            if("处理人5".equals(taskName)){
                url = prefixFmSw + "/dealPersonEdit/" + bizId;
            }
            if("申请人确认".equals(taskName)){
                url = prefixFmSw + "/applicantPersonEdit/" + bizId;
            }
        }
        if ("FmYx".equals(type)) {//监控事件单
            if ("处理".equals(taskName))
                url = prefixFmYx + "/dealView/" + bizId + "/chuli";
            if ("退回修改".equals(taskName))
                url = prefixFmYx + "/edit/" + bizId;
            if ("关闭".equals(taskName))
                url = prefixFmYx + "/passignss?id=" + bizId + "&&taskId=" + taskId;
        }
        if ("BGQQ".equals(type)) {//变更请求单
            if ("审批".equals(taskName))
                url = prefixBGQQ + "/SpDetail?id=" + bizId + "&&taskName=" + taskNameOld;
            if ("分管领导审批".equals(taskName))
                url = prefixBGQQ + "/SpDetail?id=" + bizId + "&&taskName=" + taskNameOld;
            if ("受理".equals(taskName))
                url = prefixBGQQ + "/SlDetail?id=" + bizId + "&&taskName=" + taskNameOld;
            if ("修改".equals(taskName))
                url = prefixBGQQ + "/edit/" + bizId;
            if ("协同受理".equals(taskName))
                url = prefixBGQQ + "/SlDetail?id=" + bizId;
            if ("审批转受理".equals(taskName))
                url = prefixBGQQ + "/turnAcceptanceDetail?id=" + bizId;
        }
        if ("VM".equals(type)) {//工作通知
            if ("02".equals(taskName))
                url = prefixVMC + "/edit/" + bizId;
            if ("03".equals(taskName))
                url = prefixVM + "/edit/" + bizId;
            if ("04".equals(taskName) || "05".equals(taskName))
                url = prefixVM + "/details/" + bizId;
        }
        if ("AM".equals(type)) {//公告通知
            url = prefixAM + "/details/" + bizId;
        }
        if ("LXBG".equals(type)) {//例行变更单
            if ("退回待修改".equals(taskName))
                url = prefixLXBG + "/edit/" + bizId;
            if ("待审批".equals(taskName))
                url = prefixShLXBG + "/lxbg/auditlxbg/" + bizId;
            if ("待填报".equals(taskName))
                url = prefixTbLXBG + "/lxbg/filllxbg/" + bizId;
        }
        if ("FmDd".equals(type)) {//调度请求单
            if ("修改".equals(taskName))
                url = prefixFmDd + "/edit/" + bizId;
            if ("审核".equals(taskName))
                url = prefixShFmDd + "/audit/auditAuthor/" + bizId;
            if ("调度处理".equals(taskName))
                url = prefixClFmDd + "/dispose/disposeAuthor/" + bizId;
            if ("协同处理".equals(taskName))
                url = prefixFkFmDd + "/feedback/feedbackAuthor/" + bizId;
        }
        if ("FMSAWO".equals(type)) {//态势感知工单数量
            if ("分发".equals(taskName)) {
                url = prefixFfSawo + "/edit/" + bizId;
            }
            if ("处置".equals(taskName)) {
                url = prefixCzSawo + "/edit/" + bizId;
            }
            if ("验证".equals(taskName)) {
                url = prefixYzSawo + "/edit/" + bizId;
            }
        }

        return url;
    }
}
