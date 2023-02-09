package com.ruoyi.activiti.controller.itsm.gzfault;

import com.ruoyi.activiti.constants.GzFaultStatusConstants;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IGzFaultReportLogService;
import com.ruoyi.activiti.service.IGzFaultReportService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.GzFaultReport;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.poi.WordUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 应急事件单Controller
 *
 * @author zhangchao
 * @date 2021-08-09
 */
@Controller
@RequestMapping("/gzFault/report")
public class GzFaultReportController extends BaseController {
    private String prefix = "gzFault/report";
    private String prefix_assess = "gzFault/assess";
    private String prefix_analy = "gzFault/analy";
    private String prefix_recty = "gzFault/recty";
    private String prefix_confirm = "gzFault/confirm";
    private String prefix_check = "gzFault/check";
    private String prefix_submit = "gzFault/submit";
    private String prefix_search = "gzFault/search";

    @Autowired
    private IGzFaultReportService gzFaultReportService;

    @Autowired
    private IGzFaultReportLogService gzFaultReportLogService;

    @Autowired
    private IIdGeneratorService generatorService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @GetMapping()
    public String report() {
        return prefix + "/report";
    }

    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(GzFaultReport gzFaultReport) {
        setParams(gzFaultReport);
        if ("report".equals(gzFaultReport.getRemark())) {
            gzFaultReport.setCreateBy(ShiroUtils.getUserId());
        }
        startPage();
        List<GzFaultReport> list = gzFaultReportService.selectGzFaultReportList(gzFaultReport);
        return getDataTable(list);
    }

    public void setParams(GzFaultReport gzFaultReport) {
        Map<String, Object> params = gzFaultReport.getParams();
        // 系统如果选择多个使用list传参，in来查询
        if (StringUtils.isNotEmpty(gzFaultReport.getSysName())) {
            params.put("sysNameArray", Convert.toStrArray(gzFaultReport.getSysName()));
        }
        if (!StringUtils.isEmpty(params.get("happenTimeStart"))) {
            params.put("happenTimeStart", params.get("happenTimeStart") + " 00:00:00");
        }
        if (!StringUtils.isEmpty(params.get("happenTimeEnd"))) {
            params.put("happenTimeEnd", params.get("happenTimeEnd") + " 23:59:59");
        }
    }

    /**
     * 导出应急事件单列表
     */
    @Log(title = "应急事件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(GzFaultReport gzFaultReport) {
        setParams(gzFaultReport);
        String currentPage = (String) gzFaultReport.getParams().get("currentPage");
        if ("currentPage".equals(currentPage)) {
            startPage();
        }
        List<GzFaultReport> list = gzFaultReportService.selectGzFaultReportList(gzFaultReport);
        ExcelUtil<GzFaultReport> util = new ExcelUtil<GzFaultReport>(GzFaultReport.class);
        return util.exportExcel(list, "应急事件");
    }

    /**
     * 新增
     */
    @GetMapping("/add")
    public String add(ModelMap map) {
        String bizType = "GZ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = generatorService.selectIdGeneratorByType(generator);
        map.put("num", bizType + nowDateStr + numStr);
        return prefix + "/add";
    }

    /**
     * 新增保存
     */
    @Log(title = "应急事件", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(GzFaultReport gzFaultReport) {
        if (StringUtils.isNotEmpty(gzFaultReport.getGzId())) {
            gzFaultReport.setUpdateBy(ShiroUtils.getUserId());
            gzFaultReport.setUpdateTime(new Date());
            if (StringUtils.isEmpty(gzFaultReport.getGzFaultStatus())) {
                gzFaultReport.setGzFaultStatus(GzFaultStatusConstants.GZ_FAULT_STATUS_1);
            }
            // 如果不是待提交状态的需要记录应急事件log表
            else if (!GzFaultStatusConstants.GZ_FAULT_STATUS_1.equals(gzFaultReport.getGzFaultStatus())) {
                gzFaultReportLogService.insertGzFaultReportLog(gzFaultReport);
            }
            gzFaultReportService.updateGzFaultReport(gzFaultReport);
        } else {
            gzFaultReport.setGzId(UUID.getUUIDStr());
            gzFaultReport.setCreateBy(ShiroUtils.getUserId());
            gzFaultReport.setCreateTime(new Date());
            gzFaultReport.setGzFaultStatus(GzFaultStatusConstants.GZ_FAULT_STATUS_1);
            gzFaultReportService.insertGzFaultReport(gzFaultReport);
        }
        return AjaxResult.success("操作成功", gzFaultReport.getGzId());
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{gzId}")
    public String edit(@PathVariable("gzId") String gzId, ModelMap mmap) {
        GzFaultReport gzFaultReport = gzFaultReportService.selectGzFaultReportById(gzId);
        mmap.put("gzFaultReport", gzFaultReport);
        return prefix + "/edit";
    }

    /**
     * 修改保存
     */
    @Log(title = "应急事件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(GzFaultReport gzFaultReport) {
        return toAjax(gzFaultReportService.updateGzFaultReport(gzFaultReport));
    }

    /**
     * 提交
     */
    @Log(title = "应急事件", businessType = BusinessType.OTHER)
    @PostMapping("/submit")
    @ResponseBody
    @Transactional
    public AjaxResult submit(GzFaultReport gzFaultReport) {
        Map map = new HashMap<String, Object>();
        String businessKey = "";
        String userId = ShiroUtils.getUserId();
        int rows;
        String msg = "";
        try {
            // 区分暂存之后的提交和直接提交
            if (StringUtils.isNotEmpty(gzFaultReport.getGzId())) {
                businessKey = gzFaultReport.getGzId();
                gzFaultReport.setUpdateTime(new Date());
                gzFaultReport.setUpdateBy(userId);
                gzFaultReport.setGzFaultStatus(GzFaultStatusConstants.GZ_FAULT_STATUS_2);
                rows = gzFaultReportService.updateGzFaultReport(gzFaultReport);
            } else {
                businessKey = UUID.getUUIDStr();
                gzFaultReport.setGzId(businessKey);
                gzFaultReport.setCreateTime(new Date());
                gzFaultReport.setCreateBy(userId);
                gzFaultReport.setGzFaultStatus(GzFaultStatusConstants.GZ_FAULT_STATUS_2);
                rows = gzFaultReportService.insertGzFaultReport(gzFaultReport);
            }
            if (rows > 0) {
                map.put("reCode", "0");
                map.put("userId", userId);
                // 分析事件影响环节取值是应用管理员
                map.put("appAssessId", gzFaultReport.getAppAssessId());
                activitiCommService.startProcess(businessKey, "GzRp", map);

                // 提交按钮插入应急事件单记录表
                gzFaultReportLogService.insertGzFaultReportLog(gzFaultReport);
                // 发送短信
                this.sendMsg(gzFaultReport, "分析事件影响", gzFaultReport.getAppAssessId());
            } else {
                msg = "提交应急事件单保存信息失败！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "提交应急事件单，启动流程失败！";
        }
        if (StringUtils.isNotEmpty(msg)) {
            return error(msg);
        }
        return success();
    }

    /**
     * 删除
     */
    @Log(title = "应急事件", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(gzFaultReportService.deleteGzFaultReportByIds(ids));
    }

    /**
     * 选择系统
     */
    @GetMapping("/selectApplication")
    public String selectApplication() {
        return prefix + "/selectApplication";
    }

    /**
     * 选择分析事件影响人
     */
    @GetMapping("/selectAppAssess")
    public String selectAppAssess() {
        return prefix + "/selectAppAssess";
    }

    /**
     * 选择分析事件影响人
     */
    @PostMapping("/selectOgPersonByOrg")
    @ResponseBody
    public TableDataInfo selectOgPersonByOrg(OgPerson ogPerson) {
        startPage();
        List<OgPerson> personList = ogPersonService.selectAppAssessPersonByPostId(ogPerson);
        return getDataTable(personList);
    }

    /**
     * 查看详情
     */
    @GetMapping("/detail/{gzId}")
    public String detail(@PathVariable("gzId") String gzId, ModelMap map) {
        map.put("gzFaultReport", gzFaultReportService.selectGzFaultReportById(gzId));
        return prefix + "/detail";
    }

    /**
     * 分析事件影响页面
     */
    @GetMapping("/assess")
    public String assess() {
        return prefix_assess + "/assess";
    }

    /**
     * 事件原因分析页面
     */
    @GetMapping("/analy")
    public String analy() {
        return prefix_analy + "/analy";
    }

    /**
     * 整改报告页面
     */
    @GetMapping("/recty")
    public String recty() {
        return prefix_recty + "/recty";
    }

    /**
     * 确认报告页面
     */
    @GetMapping("/confirm")
    public String confirm() {
        return prefix_confirm + "/confirm";
    }

    /**
     * 应用复核页面
     */
    @GetMapping("/check")
    public String check() {
        return prefix_check + "/check";
    }

    /**
     * 审核报告页面
     */
    @GetMapping("/confirmSubmit")
    public String submit() {
        return prefix_submit + "/submit";
    }

    /**
     * 分析事件影响｜事件原因分析｜整改报告｜确认报告待办查询
     */
    @PostMapping(value = {"/assess", "/analy", "/recty", "/confirm", "/check", "/confirmSubmit"})
    @ResponseBody
    public TableDataInfo prepareApprovalList(GzFaultReport gzFaultReport) {
        setParams(gzFaultReport);
        String description = (String) gzFaultReport.getParams().get("description");
        List<Map<String, Object>> reList = activitiCommService.userTask("GzRp", description);
        List<GzFaultReport> resultList = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            String business_key = (String) map.get("businesskey");
            String taskId = (String) map.get("taskId");
            if (StringUtils.isNotEmpty(business_key)) {
                gzFaultReport.setGzId(business_key);
                GzFaultReport gzFaultReport1 = gzFaultReportService.selectGzFaultReportByCondition(gzFaultReport);
                if (gzFaultReport1 != null) {
                    if(StringUtils.isNotEmpty(gzFaultReport.getSysName())){
                        // 系统名称不为空，则包含即为符合条件查询
                        if(gzFaultReport.getSysName().contains(gzFaultReport1.getSysName())){
                            gzFaultReport1.getParams().put("taskId", taskId);
                            resultList.add(gzFaultReport1);
                        }
                    } else {
                        // 系统名称为空，则直接不做匹配
                        gzFaultReport1.getParams().put("taskId", taskId);
                        resultList.add(gzFaultReport1);
                    }
                }
            }
        }
        // 待办列表根据生成时间倒序排序
        if (resultList.size() > 1) {
            resultList = resultList.stream().sorted(Comparator.comparing(GzFaultReport::getCreateTime).reversed()).collect(Collectors.toList());
        }
        return getDataTable_ideal(resultList);
    }

    // 分析事件影响 按钮打开页面
    @GetMapping("/assessApproval/{gzId}")
    public String assessApproval(@PathVariable("gzId") String gzId, ModelMap map) {
        map.put("gzFaultReport", gzFaultReportService.selectGzFaultReportById(gzId));
        return prefix_assess + "/assessApproval";
    }

    // 事件原因分析 按钮打开页面
    @GetMapping("/anlayApproval/{gzId}")
    public String anlayApproval(@PathVariable("gzId") String gzId, ModelMap map) {
        map.put("gzFaultReport", gzFaultReportService.selectGzFaultReportById(gzId));
        return prefix_analy + "/anlayApproval";
    }

    // 整改报告 按钮打开页面
    @GetMapping("/rectyApproval/{gzId}")
    public String rectyApproval(@PathVariable("gzId") String gzId, ModelMap map) {
        map.put("gzFaultReport", gzFaultReportService.selectGzFaultReportById(gzId));
        return prefix_recty + "/rectyApproval";
    }

    // 确认报告 按钮打开页面
    @GetMapping("/confirmApproval/{gzId}")
    public String confirmApproval(@PathVariable("gzId") String gzId, ModelMap map) {
        map.put("gzFaultReport", gzFaultReportService.selectGzFaultReportById(gzId));
        return prefix_confirm + "/confirmApproval";
    }

    // 应用复核 按钮打开页面
    @GetMapping("/checkApproval/{gzId}")
    public String checkApproval(@PathVariable("gzId") String gzId, ModelMap map) {
        map.put("gzFaultReport", gzFaultReportService.selectGzFaultReportById(gzId));
        return prefix_check + "/checkApproval";
    }

    // 审核报告 按钮打开页面
    @GetMapping("/submitApproval/{gzId}")
    public String submitApproval(@PathVariable("gzId") String gzId, ModelMap map) {
        map.put("gzFaultReport", gzFaultReportService.selectGzFaultReportById(gzId));
        return prefix_submit + "/submitApproval";
    }

    // 事件原因分析人页面
    @GetMapping("/selectAssessPerson")
    public String selectAssessPerson() {
        return prefix_assess + "/assessApprovalPerson";
    }

    /**
     * 查询事件原因分析人
     */
    @PostMapping("/selectAssessList")
    @ResponseBody
    public TableDataInfo selectAssessList(OgPerson ogPerson) {
        startPage();
        // 标识使用机构和岗位union查询
        ogPerson.setRemark("union");
        List<OgPerson> list = ogPersonService.selectAssessList(ogPerson);
        return getDataTable(list);
    }

    /**
     * 分析事件影响环节转发页面
     */
    @GetMapping("/assessForward/{gzId}")
    public String assessForward(@PathVariable("gzId") String gzId, ModelMap map) {
        map.put("gzId", gzId);
        return prefix_assess + "/assessForward";
    }

    /**
     * 审核报告页面
     */
    @GetMapping("/selectConfirmLeader")
    public String selectConfirmLeader() {
        return prefix_check + "/selectConfirmLeader";
    }

    /**
     * 查询审核报告人
     */
    @PostMapping("/selectConfirmLeader")
    @ResponseBody
    public TableDataInfo selectConfirmLeader(OgPerson ogPerson) {
        startPage();
        ogPerson.setRemark("noUnion");
        List<OgPerson> list = ogPersonService.selectAssessList(ogPerson);
        return getDataTable(list);
    }

    /**
     * 选择应用复核人页面
     */
    @GetMapping("/selectAppChecker")
    public String selectAppChecker() {
        return prefix_confirm + "/selectAppChecker";
    }

    /**
     * 查询应用复核人
     * 对应的逻辑是需要取当前登录人对应机构下的处长+数据中心处长（岗位）
     */
    @PostMapping("/selectAppCheck")
    @ResponseBody
    public TableDataInfo selectAppCheck(OgPerson ogPerson) {
        OgUser ogUser = ShiroUtils.getOgUser();
        OgPerson person = ogPersonService.selectOgPersonById(ogUser.getpId());
        ogPerson.setOrgId(person.getOrgId());
        startPage();
        List<OgPerson> list = ogPersonService.selectAppCheck(ogPerson);
        return getDataTable(list);
    }

    /**
     * 分析事件影响环节转发功能，支持从页面重新选择分析事件影响人
     */
    @PostMapping("/assessForward")
    @ResponseBody
    @Transactional
    public AjaxResult assessForward(GzFaultReport gzFaultReport) {
        // 此处需要校验转发人不能是自己，从库里存储的做对比，如果相同报错提示
        GzFaultReport report = gzFaultReportService.selectGzFaultReportById(gzFaultReport.getGzId());
        if (StringUtils.isNotNull(report) && report.getAppAssessId().equals(gzFaultReport.getAppAssessId())) {
            return error("【分析事件影响】环节转发功能不支持自己转发给自己，请知晓！");
        }
        GzFaultReport gz = new GzFaultReport();
        gz.setAppAssessId(gzFaultReport.getAppAssessId());
        gz.setGzId(gzFaultReport.getGzId());
        Map<String, Object> map = new HashMap<>();
        String comment = (String) gzFaultReport.getParams().get("comment");
        map.put("businessKey", gzFaultReport.getGzId());
        map.put("comment", comment);
        map.put("userId", ShiroUtils.getUserId());
        map.put("reCode", "1");
        map.put("appAssessId", gzFaultReport.getAppAssessId());
        map.put("processDefinitionKey", "GzRp");
        String msg = "";
        try {
            gzFaultReportService.updateGzFaultReport(gz);
            activitiCommService.complete(map);
            this.sendMsg(report, "分析事件影响", gzFaultReport.getAppAssessId());
        } catch (Exception e) {
            e.printStackTrace();
            msg = "分析事件影响转发失败！";
        }
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    private final String commentAssess = "分析事件影响";
    private final String commentAnaly = "事件原因分析";
    private final String commentRecty = "整改报告";
    private final String commentConfirm = "确认报告";
    private final String commentCheck = "应用复核";
    private final String commentConfirmSubmit = "审核报告";

    /**
     * 分析事件影响|事件原因分析|整改报告|确认报告|应用复核|审核报告 环节审核报告 流程提交方法
     */
    @PostMapping(value = {"/assessApproval", "/analyApproval", "/rectyApproval", "/confirmApproval", "/appCheckApproval", "/confirmSubmitApproval"})
    @ResponseBody
    @Transactional
    public AjaxResult approvalProcess(GzFaultReport gzFaultReport) {
        String remark = gzFaultReport.getRemark();
        Map<String, Object> map = new HashMap<>();
        String comment = (String) gzFaultReport.getParams().get("comment");
        // 审批意见为空默认赋值通过，后续退回环节默认填写"退回"
        if (StringUtils.isEmpty(comment)) {
            comment = "通过";
        }
        String status = "";
        String processName = "";
        String pid = "";
        boolean insertLogFlag = true;
        boolean endFlag = false;
        switch (remark) {
            case commentAssess:
                // 分析事件影响
                status = GzFaultStatusConstants.GZ_FAULT_STATUS_3;
                // 分析事件影响来源于前端页面选择，若前端页面未选择，则取值当前操作人
                gzFaultReport.setFaultAnalyId(StringUtils.isNotEmpty(gzFaultReport.getFaultAnalyId()) ? gzFaultReport.getFaultAnalyId() : ShiroUtils.getUserId());
                map.put("faultAnalyId", gzFaultReport.getFaultAnalyId());
                map.put("reCode", "0");
                processName = commentAnaly;
                pid = gzFaultReport.getFaultAnalyId();
                break;
            case commentAnaly:
                // 事件原因分析
                status = GzFaultStatusConstants.GZ_FAULT_STATUS_4;
                // 整改报告人与分析事件影响人｜事件原因分析人一致
                gzFaultReport.setFaultRectyId(ShiroUtils.getUserId());
                map.put("faultRectyId", gzFaultReport.getFaultRectyId());
                map.put("reCode", "0");
                processName = commentRecty;
                pid = gzFaultReport.getFaultRectyId();
                break;
            case commentRecty:
                // 整改报告
                status = GzFaultStatusConstants.GZ_FAULT_STATUS_5;
                // 确认报告人与分析事件影响选择的人一致
                gzFaultReport.setFaultConfirmId(gzFaultReport.getAppAssessId());
                map.put("faultConfirmId", gzFaultReport.getFaultConfirmId());
                map.put("reCode", "0");
                processName = commentConfirm;
                pid = gzFaultReport.getFaultConfirmId();
                break;
            case commentConfirm:
                // 确认报告
                // 下一步流程环节是"应用审核"，状态是待复核
                status = GzFaultStatusConstants.GZ_FAULT_STATUS_6;
                map.put("appCheckId", gzFaultReport.getAppCheckId());
                map.put("reCode", "0");
                processName = commentCheck;
                pid = gzFaultReport.getAppCheckId();
                break;
            case commentCheck:
                // 应用复核环节
                // 如果应用审核环节"退回"，则将reCode设为2  checkFlag=false表示退回
                String checkFlag = gzFaultReport.getParams().get("flag").toString();
                if(!Boolean.valueOf(checkFlag)) {
                    map.put("faultConfirmId", gzFaultReport.getFaultConfirmId());
                    map.put("reCode", "2");
                    // 应用复核退回之后修改状态为"待确认"
                    status = GzFaultStatusConstants.GZ_FAULT_STATUS_5;
                    pid = gzFaultReport.getFaultConfirmId();
                    processName = commentConfirm;
                }
                // 如果"应用复核"环节领导审核人员id为空，则标识下一环节是结束流程，若不为空，"应用复核"之后是"审核报告"
                else if (StringUtils.isNotEmpty(gzFaultReport.getConfirmSubmitId())) {
                    map.put("confirmSubmitId", gzFaultReport.getConfirmSubmitId());
                    map.put("reCode", "0");
                    // 提交到领导审批环节"审核报告"改为待审核
                    status = GzFaultStatusConstants.GZ_FAULT_STATUS_7;
                    pid = gzFaultReport.getConfirmSubmitId();
                    processName = commentConfirmSubmit;
                } else {
                    map.put("reCode", "1");
                    status = GzFaultStatusConstants.GZ_FAULT_STATUS_8;
                    endFlag = true;
                }
                // 处长审核不修改数据故而不需要保存log信息
                insertLogFlag = false;
                break;
            case commentConfirmSubmit:
                // 审核报告
                String flag = gzFaultReport.getParams().get("flag").toString();
                if (Boolean.valueOf(flag)) {
                    // flag = true标识通过
                    map.put("reCode", "0");
                    status = GzFaultStatusConstants.GZ_FAULT_STATUS_8;
                    endFlag = true;
                } else {
                    // flag = false标识退回
                    map.put("reCode", "1");
                    // 领导退回改为待确认，流程退回到确认报告环节，状态变更为"待确认"
                    status = GzFaultStatusConstants.GZ_FAULT_STATUS_5;
                    processName = commentConfirm;
                    pid = gzFaultReport.getFaultConfirmId();
                }
                // 领导审核报告审批没有修改信息所以不插入log日志
                insertLogFlag = false;
                break;
        }
        gzFaultReport.setGzFaultStatus(status);
        map.put("businessKey", gzFaultReport.getGzId());
        map.put("comment", comment);
        map.put("userId", ShiroUtils.getUserId());
        map.put("processDefinitionKey", "GzRp");
        String msg = "";
        try {
            int rows = gzFaultReportService.updateGzFaultReport(gzFaultReport);
            // 领导审核报告环节不修改信息因此不需要修改应急事件单表信息和新插入一条log
            if (insertLogFlag) {
                if (rows > 0) {
                    gzFaultReportLogService.insertGzFaultReportLog(gzFaultReport);
                }
            }
            activitiCommService.complete(map);
            // 流程环节未结束时当前处理人完成后需要给下一环节处理人发送短信
            if (!endFlag)
                this.sendMsg(gzFaultReport, processName, pid);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "应急事件单流程提交失败！";
        }
        if (StringUtils.isNotEmpty(msg)) {
            return error(msg);
        }
        return success();
    }

    /**
     * 发送短信
     */
    public void sendMsg(GzFaultReport gzFaultReport, String processName, String pid) {
        // 如果前端传递过来的参数信息为空重新到数据库查询一次
        if(StringUtils.isEmpty(gzFaultReport.getHappenTime()) || StringUtils.isEmpty(gzFaultReport.getSysName())
                || StringUtils.isEmpty(gzFaultReport.getGzNo())) {
            gzFaultReport = gzFaultReportService.selectGzFaultReportById(gzFaultReport.getGzId());
        }
        String msg = "关于" + gzFaultReport.getHappenTime() + gzFaultReport.getSysName() + "发生异常，应急事件单号" + gzFaultReport.getGzNo() + ",已到达[" + processName + "]环节，请登录运维管理平台处理。";
        OgPerson person = ogPersonService.selectOgPersonEvoById(pid);
        vmBizInfoService.sendSms(msg, person);
    }

    /**
     * 应急事件单查询页面
     */
    @GetMapping("/search")
    public String search() {
        return prefix_search + "/list";
    }

    private String prefixWordName = "应急事件分析与整改报告-";
    private final String resourceTemplateLocation = "/doc/应急事件分析报告.docx";

    /**
     * 应急事件单导出word文档
     */
    @GetMapping("/exportWord/{gzId}")
    public void exportWord(@PathVariable("gzId") String gzId, HttpServletResponse response) {
        GzFaultReport gzFaultReport = gzFaultReportService.selectGzFaultReportById(gzId);
        OgPerson person = ogPersonService.selectOgPersonEvoById(gzFaultReport.getCreateBy());
        gzFaultReport.setCreatePhone(person.getMobilPhone());
        InputStream in = this.getClass().getResourceAsStream(this.resourceTemplateLocation);
        if (StringUtils.isNotEmpty(gzFaultReport.getSysName())) {
            this.prefixWordName += gzFaultReport.getSysName();
        }
        WordUtil wordUtil = new WordUtil(gzFaultReport);
        wordUtil.exportWord(in, this.prefixWordName, response);
    }
}
