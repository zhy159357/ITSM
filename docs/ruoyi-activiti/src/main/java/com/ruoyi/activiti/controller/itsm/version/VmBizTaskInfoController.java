package com.ruoyi.activiti.controller.itsm.version;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 版本任务Controller
 *
 * @author ruoyi
 * @date 2021-01-06
 */
@Controller
@RequestMapping("/version/taskInfo")
public class VmBizTaskInfoController extends BaseController {
    private String prefix = "version/taskInfo";
    private String prefix_search = "version/search";
    private String prefix_monitor = "version/monitor";

    @Autowired
    private IVmBizTaskinfoService vmBiztaskInfoService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private IPubAttachmentService pubAttachmentService;

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private IAutoItsmResultmsgService resultmsgService;

    @Autowired
    private IAutoItsmFastfileService autoItsmFastfileService;

    /**
     * 版本实施页面查询列表页面
     *
     * @return
     */
    @GetMapping
    public String taskInfo() {
        return prefix + "/taskInfo";
    }

    /**
     * 版本升级任务查询列表页面
     *
     * @return
     */
    @GetMapping("/versionTaskInfoList")
    public String versionTaskInfoList() {
        return prefix_search + "/versionTaskInfoList";
    }

    /**
     * 版本监控查询列表页面
     *
     * @return
     */
    @GetMapping("/monitor")
    public String monitor() {
        return prefix_monitor + "/list";
    }

    /**
     * 查询版本任务列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(VmBizTaskinfo vmBiztaskInfo) {
        Map<String, Object> params = vmBiztaskInfo.getParams();
        convertActualTime(params);
        if (StringUtils.isNotEmpty((String)params.get("caption"))) {
            params.put("captionArray", Convert.toStrArray((String)params.get("caption")));
        }

        // 版本实施查询列表需要控制当前登录人自己所属机构的数据，版本任务列表增加全国中心判断，全国中心查询所有，省中心查询自己机构
        if (!"versionTaskInfoList".equals(vmBiztaskInfo.getRemark())) {
            OgPerson person = ogPersonService.selectOgPersonById(ShiroUtils.getUserId());
            vmBiztaskInfo.setOrg(person.getOrgId());
        } else {
            authControlVersion(vmBiztaskInfo);
        }
        startPage();
        /*List<VmBizTaskinfo> list = unionFlag ?  vmBiztaskInfoService.selectVmBizTaskinfoListUnion(vmBiztaskInfo)
                                             :  vmBiztaskInfoService.selectVmBizTaskinfoList(vmBiztaskInfo);*/
        List<VmBizTaskinfo> list = vmBiztaskInfoService.selectVmBizTaskinfoList(vmBiztaskInfo);
        return getDataTable(list);
    }

    /**
     * 导出版本任务列表
     */
    @Log(title = "版本任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(VmBizTaskinfo vmBiztaskInfo) {
        Map<String, Object> params = vmBiztaskInfo.getParams();
        convertActualTime(params);
        authControlVersion(vmBiztaskInfo);
        String currentPage = (String) params.get("currentPage");
        if ("currentPage".equals(currentPage)) {
            startPage();
        }
        List<VmBizTaskinfo> list = vmBiztaskInfoService.selectVmBizTaskinfoList(vmBiztaskInfo);
        ExcelUtil<VmBizTaskinfo> util = new ExcelUtil<VmBizTaskinfo>(VmBizTaskinfo.class);
        return util.exportExcel(list, "版本任务");
    }

    /**
     * 转换时间
     * @param params
     */
    public void convertActualTime(Map<String, Object> params) {
        if (StringUtils.isNotEmpty((String)params.get("actualStartTimeStart"))) {
            params.put("actualStartTimeStart", handleTimeYYYYMMDDHHMMSS((String) params.get("actualStartTimeStart")));
        }
        if (StringUtils.isNotEmpty((String)params.get("actualStartTimeEnd"))) {
            params.put("actualStartTimeEnd", handleTimeYYYYMMDDHHMMSS((String) params.get("actualStartTimeEnd")));
        }
    }

    /**
     * 判断权限是否全国中心
     * @param vmBiztaskInfo
     */
    public void authControlVersion(VmBizTaskinfo vmBiztaskInfo) {
        OgPerson person = ogPersonService.selectOgPersonById(ShiroUtils.getUserId());
        OgOrg org = sysDeptService.selectDeptById(person.getOrgId());
        if ("000000000".equals(org.getOrgCode()) || org.getLevelCode().contains("/000000000/010000888/")) {
            // 全国中心查看所有
        } else {
            // 省中心查询自己机构
            vmBiztaskInfo.setOrg(person.getOrgId());
        }
    }

    /**
     * 版本实施下载版本页面
     */
    @GetMapping("/download/{versionInfoId}/{taskId}")
    public String download(@PathVariable("versionInfoId") String ownerId, @PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizTaskinfo vmBizTaskinfo = vmBiztaskInfoService.selectVmBizTaskinfoById(taskId);
        mmap.put("ownerId", ownerId);
        mmap.put("taskId", taskId);
        mmap.put("taskStatus", vmBizTaskinfo.getTaskStatus());
        return prefix + "/download";
    }

    /**
     * 版本实施确认版本任务页面
     */
    @GetMapping("/confirm/{taskId}")
    public String confirm(@PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizTaskinfo vmBizTaskinfo = vmBiztaskInfoService.selectVmBizTaskinfoById(taskId);
        mmap.put("vmBizTaskInfo", vmBizTaskinfo);
        return prefix + "/confirm";
    }

    /**
     * 下载版本｜确认版本任务完成后修改状态
     */
    @PostMapping("/complete")
    @ResponseBody
    public AjaxResult complete(VmBizTaskinfo vmBiztaskInfo) {
        handleTime(vmBiztaskInfo);
        if ("download".equals(vmBiztaskInfo.getRemark())) {
            // download标识下载，只有状态为未下载的情况才更新任务状态  未下载状态为1，已下载2，确认完成3，已评价4
            if("1".equals(vmBiztaskInfo.getTaskStatus())){
                vmBiztaskInfo.setTaskStatus("2");
            }
            return toAjax(vmBiztaskInfoService.downloadVersion(vmBiztaskInfo));
        } else {
            vmBiztaskInfo.setTaskStatus("3");
            return toAjax(vmBiztaskInfoService.completeVersion(vmBiztaskInfo));
        }
    }

    /**
     * 统一处理时间字段
     *
     * @param vmBiztaskInfo
     */
    public void handleTime(VmBizTaskinfo vmBiztaskInfo) {
        vmBiztaskInfo.setActualStartTime((handleTimeYYYYMMDDHHMMSS(vmBiztaskInfo.getActualStartTime())));
        vmBiztaskInfo.setActualFinishTime(handleTimeYYYYMMDDHHMMSS(vmBiztaskInfo.getActualFinishTime()));
    }

    /**
     * 查看任务详情页面
     */
    @GetMapping("/detail/{taskId}")
    public String detail(@PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizTaskinfo vmBizTaskinfo = vmBiztaskInfoService.selectVmBizTaskinfoById(taskId);
        String ifBigFault = vmBizTaskinfo.getIfBigFault();
        String ifResultSmoothly = vmBizTaskinfo.getIfResultSmoothly();
        String ifSupport = vmBizTaskinfo.getIfSupport();
        // 翻译是否引发生产故障|升级结果填报|是否现场支持字段
        vmBizTaskinfo.setIfBigFault(StringUtils.isEmpty(ifBigFault) ? "" : ("1".equals(ifBigFault) ? "是" : "否"));
        vmBizTaskinfo.setIfSupport(StringUtils.isEmpty(ifSupport) ? "" : ("1".equals(ifSupport) ? "是" : "否"));
        mmap.put("vmBizTaskInfo", vmBizTaskinfo);
        return prefix + "/detail";
    }

    /**
     * 版本发布监控页面
     */
    @GetMapping("/monitor/{versionInfoId}/{versionInfoNo}")
    public String monitor(@PathVariable("versionInfoId") String versionInfoId, @PathVariable("versionInfoNo") String versionInfoNo, ModelMap mmap) {
        mmap.put("versionInfoId", versionInfoId);
        mmap.put("versionInfoNo", versionInfoNo);
        return prefix_monitor + "/monitor";
    }

    /**
     * 版本发布监控页面
     */
    @PostMapping("/monitorTaskInfoList")
    @ResponseBody
    public TableDataInfo monitorTaskInfoList(VmBizTaskinfo vmBizTaskinfo) {
        startPage();
        List<VmBizTaskinfo> list = vmBiztaskInfoService.selectVmBizTaskinfoList(vmBizTaskinfo);
        return getDataTable(list);
    }

    /**
     * 版本发布发送短信页面
     */
    @GetMapping("/message")
    public String message(String ownerId, String flag, String msg, ModelMap mmap) {
        mmap.put("ownerId", ownerId);
        mmap.put("msgFlag", flag);
        mmap.put("msg", msg);
        return prefix_monitor + "/message";
    }

    /**
     * 根据msgFlag和ownerId查询人员
     *
     * @param msgFlag 未下载或者未完成标识
     * @param ownerId 版本单主键ID
     * @return
     */
    @PostMapping("/selectPersons")
    @ResponseBody
    public TableDataInfo selectPersons(String msgFlag, String ownerId) {
        VmBizTaskinfo vmBizTaskinfo = new VmBizTaskinfo();
        String taskStatus = "";
        // 向未下载省发送短信
        if (VersionStatusConstants.MSG_FLAG_1.equals(msgFlag)) {
            taskStatus = "1";
        }
        // 向未完成省发送短信
        else if (VersionStatusConstants.MSG_FLAG_2.equals(msgFlag)) {
            taskStatus = "2";
        }
        vmBizTaskinfo.setVersionInfoId(ownerId);
        vmBizTaskinfo.setTaskStatus(taskStatus);
        List<String> orgIds = new ArrayList<>();
        List<VmBizTaskinfo> list = vmBiztaskInfoService.selectVmBizTaskinfoList(vmBizTaskinfo);
        if (CollectionUtils.isEmpty(list)) {
            // 如果未查询到符合条件的orgId，则赋值默认值"NO_ORG"，防止sql查询出不符合要求的人员数据
            orgIds.add("NO_ORG");
        } else {
            for (VmBizTaskinfo taskInfo : list) {
                orgIds.add(taskInfo.getOrg());
            }
        }
        OgPerson ogPerson = new OgPerson();
        Map<String, Object> params = new HashMap<>();
        params.put("orgIds", orgIds);
        // 岗位id为 0017
        params.put("postId", "0017");
        ogPerson.setParams(params);
        startPage();
        List<OgPerson> personList = ogPersonService.selectOgPersonByOrgAndPostId(ogPerson);
        return getDataTable(personList);
    }

    /**
     * 版本发布发送短信页面
     */
    @PostMapping("/sendMsg")
    @ResponseBody
    public AjaxResult sendMsg(String pId, String ownerId, String msgFlag, String msg) {
        // 此处发送短信信息
        vmBiztaskInfoService.sendMsg(pId, msg);
        return success();
    }

    /**
     * 查询确认完成时间与下载时间得差值是否在12小时以内
     */
    @PostMapping("/selectCompleteDate/{taskId}")
    @ResponseBody
    public AjaxResult selectCompleteDate(@PathVariable("taskId") String taskId) {
        VmBizTaskinfo taskInfo = vmBiztaskInfoService.selectVmBizTaskinfoById(taskId);
        String updateTime = taskInfo.getUpdate_time();
        Map<String, Object> map = new HashMap();
        Boolean flag = true;
        String msg = "";
        final long completeHour = 12L;
        if (StringUtils.isEmpty(updateTime)) {
            flag = false;
            msg = "版本操作完成后，超过" + completeHour + "小时方可评估，距离可评估还差" + completeHour + "小时！";
        } else {
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            Date nowDate = DateUtils.dateTime(DateUtils.YYYYMMDDHHMMSS, DateUtils.dateTimeNow());
            Date downloadDate = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, updateTime);
            // 当前时期大于下载日期
            if (nowDate.compareTo(downloadDate) > 0) {
                long diff = nowDate.getTime() - downloadDate.getTime();
                // 计算天
                long day = diff / nd;
                // 计算小时
                long hour = diff % nd / nh;
                // 计算分
                long min = diff % nd % nh / nm;
                if (day < 1 && hour < completeHour) {
                    flag = false;
                    // 如果是11时0分，则12-11-1=0时，60-0=60，显示成0小时60分，需要修改为1小时0分
                    if (min == 0) {
                        hour = hour - 1;
                        min = 60;
                    }
                    msg = "版本操作完成后，超过" + completeHour + "小时方可评估，距离可评估还差" + (completeHour - hour - 1) + "小时" + (60 - min) + "分！";
                }
            } else {
                flag = false;
                msg = "版本操作完成后，超过" + completeHour + "小时方可确认，距离可评估还差" + completeHour + "小时！";
            }
        }
        map.put("flag", flag);
        map.put("msg", msg);
        return AjaxResult.success("操作成功", map);
    }

    /**
     * 版本任务启动自动化页面
     */
    @GetMapping("/automate/{taskId}/{versionInfoId}")
    public String automate(@PathVariable("versionInfoId") String versionInfoId,@PathVariable("taskId") String taskId, ModelMap map) {
        map.put("taskId", taskId);
        map.put("versionInfoId", versionInfoId);
        return prefix + "/automate";
    }

    /**启动自动化基线控制开关标识先暂时关闭**/
    private boolean controlUpgradeFlag = false;

    /**
     * 启动自动化
     * @param atId
     * @return
     */
    @PostMapping("/startAgent")
    @ResponseBody
    public AjaxResult startAgent(String atId, String taskId) {
        Attachment attachment = pubAttachmentService.selectAttachmentById(atId);
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(attachment.getOwnerId());
        // 校验基线时间  upgradeTime为系统基线起始时间，该字段为空不执行基线时间校验逻辑
        boolean check = false;
        OgSys ogSys = vmBizInfo.getOgSys();
        String upgradeTime = null;
        if (ogSys != null) {
            upgradeTime = ogSys.getUpgradeTime();
            if (StringUtils.isNotEmpty(upgradeTime)) {
                check = true;
            }
        }
        if (check && controlUpgradeFlag) {
            String upgradeTimeEnd = pubParaValueService.selectPubParaValueByNameValue("system_upgrade_time_end", "end_upgrade_time");
            if (StringUtils.isEmpty(upgradeTimeEnd) || upgradeTimeEnd.split(":").length < 1) {
                return AjaxResult.error("请配置基线结束时间,时间格式为HH:mm,例如: 04:00代表凌晨4点！");
            }
            // 当前时间
            Date now = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM, DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM));
            // 基线结束时间
            String[] arrTime = upgradeTimeEnd.split(":");
            Integer hour = Integer.valueOf(arrTime[0]);
            Integer minute = Integer.valueOf(arrTime[1]);
            // 基线结束时间 目前数据库配置04:00,要求当前操作时间必须小于第二天凌晨4点，故使用当天晚上23:59:59增加4个小时
            Date time = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now) + " " + "23:59:59");
            Date endTime = DateUtils.addHourMinute(time, hour, minute);
            // 基线开始时间 CMDB接口字段同步到系统表 19:00表示晚上7点
            Date startTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM, DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now) + " " + upgradeTime);

            // 当前时间>基线起始时间 && 当前时间<基线结束时间 故反向判断不满足直接返回错误信息
            if (now.before(startTime) || now.after(endTime)) {
                return AjaxResult.error(ogSys.getCaption() + ",基线起始时间:" + upgradeTime + ",结束时间:" + upgradeTimeEnd + ",在此基线时间段内才能自动化发布！");
            }
        }
        Map map = vmBiztaskInfoService.sendMessageInstanceStartup(vmBizInfo, attachment);
        if ((Boolean) map.get("flag")) {
            // 自动化启动成功更新任务时间，并且变更任务单状态
            VmBizTaskinfo vmBiztaskInfo = new VmBizTaskinfo();
            vmBiztaskInfo.setTaskId(taskId);
            vmBiztaskInfo.setUpdate_time(DateUtils.dateTimeNow());
            vmBiztaskInfo.setTaskStatus("3");
            vmBiztaskInfoService.updateVmBizTaskinfo(vmBiztaskInfo);
            return AjaxResult.success("启动自动化成功!");
        }
        return AjaxResult.error("启动自动化失败,失败原因:" + map.get("message"));
    }

    /**
     * 查询自动化启动结果
     *
     * @param atId
     * @return
     */
    @PostMapping("/selectResultMsg")
    @ResponseBody
    public TableDataInfo selectResultMsg(String atId) {
        if (StringUtils.isEmpty(atId)) {
            return getDataTable(new ArrayList<>());
        }
        Attachment attachment = pubAttachmentService.selectAttachmentById(atId);
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(attachment.getOwnerId());
        List<Map> resultList = vmBiztaskInfoService.selectResultMsg(vmBizInfo, attachment);
        // 查询结果按照开始时间大小倒序排序
        if (resultList.size() > 1) {
            Collections.sort(resultList, (o1, o2) -> {
                if (StringUtils.isNotNull(o1.get("startTime")) && StringUtils.isNotNull(o2.get("startTime"))) {
                    Date d1 = DateUtils.parseDate(o1.get("startTime").toString(), DateUtils.YYYY_MM_DD_HH_MM_SS);
                    Date d2 = DateUtils.parseDate(o2.get("startTime").toString(), DateUtils.YYYY_MM_DD_HH_MM_SS);
                    if (d2.after(d1)) {
                        return 1;
                    }
                    return -1;
                }
                return 0;
            });
        }
        return getDataTable_ideal(resultList);
    }

    /**
     * 查询自动化历史结果
     *
     * @param ownerId
     * @return
     */
    @PostMapping("/automateResultHistory")
    @ResponseBody
    public TableDataInfo automateResultHistory(String ownerId) {
        if (StringUtils.isEmpty(ownerId)) {
            return getDataTable(new ArrayList<>());
        }
        AutoItsmResultmsg msg = new AutoItsmResultmsg();
        msg.setBusinessId(ownerId);
        // 默认只查询自动化附件类型为3（自动化步骤）
        msg.getParams().put("fileType", "3");
        startPage();
        List<AutoItsmResultmsg> list = resultmsgService.selectAutoItsmResultmsgList(msg);
        return getDataTable(list);
    }

    /**
     * 升级后评估页面
     * @param taskId
     * @param map
     * @return
     */
    @GetMapping("/upgradeAfterCondition/{taskId}")
    public String upgradeAfterCondition(@PathVariable("taskId") String taskId, ModelMap map){
        map.put("taskId", taskId);
        return prefix + "/upgrade";
    }

    /**
     * 升级后评估保存数据
     * @param vmBizTaskinfo
     * @return
     */
    @PostMapping("/upgradeAfterCondition")
    @ResponseBody
    public AjaxResult upgradeAfterCondition(VmBizTaskinfo vmBizTaskinfo){
        boolean flag;
        // 升级结果填报完成属于正常情况不需要走强制校验上传附件截图，赋值为true，其他情况需要强制校验是否有截图附件
        if("1".equals(vmBizTaskinfo.getIfResultSmoothly())){
            flag = true;
        } else {
            flag = vmBiztaskInfoService.judgeVersionTaskInfoImage(vmBizTaskinfo.getTaskId());
        }
        if(flag){
            // 已评估将任务状态设置为4
            vmBizTaskinfo.setTaskStatus("4");
            return toAjax(vmBiztaskInfoService.updateVmBizTaskinfo(vmBizTaskinfo));
        } else {
            return AjaxResult.error("升级结果填报为【回退】或【其他异常】，必须上传拉群请示领导的截图！");
        }
    }

    /**
     * 升级后评估截图上传
     * @return
     */
    @GetMapping("/upload/{taskId}")
    public String upload(@PathVariable("taskId") String taskId, ModelMap map) {
        VmBizTaskinfo vmBizTaskinfo = vmBiztaskInfoService.selectVmBizTaskinfoById(taskId);
        map.put("ownerId", vmBizTaskinfo.getVersionInfoId());
        map.put("businessNumberNo", VersionStatusConstants.VERSION_INFO_IMAGE);
        return "attachment" + "/upload";
    }

    /**
     * 版本包就绪状态查询
     * @param atId 附件id
     * @param flag 区分亦庄｜合肥  1-亦庄  2-合肥
     * @return
     */
    @PostMapping("/versionReadyStatus")
    @ResponseBody
    public AjaxResult versionReadyStatus(String atId, String flag) {
        AjaxResult ajaxResult;
        Attachment attachment = pubAttachmentService.selectAttachmentById(atId);
        AutoItsmFastfile autoItsmFastfile = new AutoItsmFastfile();
        autoItsmFastfile.setFastno(attachment.getFilePath());
        List<AutoItsmFastfile> autoItsmFastfiles = autoItsmFastfileService.selectAutoItsmFastfileList(autoItsmFastfile);
        if(CollectionUtils.isEmpty(autoItsmFastfiles)){
            ajaxResult = error();
        } else {
            AutoItsmFastfile fastfile = autoItsmFastfiles.get(0);
            if("1".equals(flag)){
                // yzCenterStatus='1'表示亦庄下载完成
                if("1".equals(fastfile.getYzCenterStatus())){
                    ajaxResult = success();
                } else {
                    ajaxResult = error();
                }
            } else if("2".equals(flag)){
                // hfCenterStatus='1'表示合肥下载完成
                if("1".equals(fastfile.getHfCenterStatus())){
                    ajaxResult = success();
                } else {
                    ajaxResult = error();
                }
            } else {
                ajaxResult = error();
            }
        }
        return ajaxResult;
    }
}
