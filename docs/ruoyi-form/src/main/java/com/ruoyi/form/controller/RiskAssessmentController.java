package com.ruoyi.form.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.RiskAccessRecord;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.service.CustomerFormService;
import com.ruoyi.form.service.IChangeService;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.form.domain.RiskAssessment;
import com.ruoyi.form.service.IRiskAssessmentService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 风险评估Controller
 *
 * @author zzz
 * @date 2022-08-05
 */
@Controller
@RequestMapping("/customerForm/assessment")
public class RiskAssessmentController extends BaseController {
    private String prefix = "assessment";

    @Autowired
    private IRiskAssessmentService riskAssessmentService;
    @Autowired
    private IChangeService changeService;
    @Autowired
    private CustomerFormService customerFormService;
    @Autowired
    Base base;

/*
    @RequiresPermissions("system:assessment:view")
    @GetMapping()
    public String assessment()
    {
        return prefix + "/assessment";
    }*/

    /**
     * 查询列表
     *//*
    @RequiresPermissions("system:assessment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(RiskAssessment riskAssessment)
    {
        startPage();
        List<RiskAssessment> list = riskAssessmentService.selectRiskAssessmentList(riskAssessment);
        return getDataTable(list);
    }*/

    /**
     * 导出列表
     *//*
    @RequiresPermissions("system:assessment:export")
    @Log(title = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RiskAssessment riskAssessment)
    {
        List<RiskAssessment> list = riskAssessmentService.selectRiskAssessmentList(riskAssessment);
        ExcelUtil<RiskAssessment> util = new ExcelUtil<RiskAssessment>(RiskAssessment.class);
        return util.exportExcel(list, "assessment");
    }*/

    /**
     * 新增或更新
     */
    @PostMapping("/form/{changeNo}")
    @ResponseBody
    public AjaxResult getPage(@PathVariable("changeNo") String changeNo, @RequestBody CustomerFormContent changeCustomerFormContent) {
        //根据changeNo判断是否已经有风险评估了
        //先进行保存或更新
        Map<String, Object> param = new HashMap<>();
        param.put(ChangeFieldEnum.ID.getName(), "");
        param.put(ChangeFieldEnum.STATUS.getName(), "");
        param.put(ChangeFieldEnum.CREATOR.getName(), "");
        Map<String, String> query = new HashMap<>();
        query.put(ChangeFieldEnum.EXTRA1.getName(), changeNo);
        Map<String, Object> result = base.selectMap(ChangeTableNameEnum.CHANGE, param, query);
        Object type = changeCustomerFormContent.getFields().get(ChangeFieldEnum.TYPE.getName());
        if (type == null) {
            return AjaxResult.warn("请先填写变更类型");
        }
        changeCustomerFormContent.getFields().put(ChangeFieldEnum.ID.getName(), result.get("id"));
        customerFormService.insertOrUpdate("change", changeCustomerFormContent);
        //通过变更单获取所有任务单里的所涉系统
        List<String> sysList = base.getSysListByChangeNo(changeNo);
        sysList = sysList.stream().distinct().collect(Collectors.toList());
        RiskAssessment riskAssessment = riskAssessmentService.selectRiskAssessmentById(changeNo);
        CustomerFormContent customerFormContent = changeService.initCustomerFormContent("design_biz_risk_assessment");
        String formJson = customerFormContent.getDataJson();
        Map<String, Object> map = new HashMap<>();
        map.put("formId", changeNo);
        if (riskAssessment != null) {
            map.put("infSysCount", riskAssessment.getInfSysCount());
            map.put("infFuncInterruptType", riskAssessment.getInfFuncInterruptType());
            map.put("changeType", riskAssessment.getChangeType());
            map.put("changePlanTime", riskAssessment.getChangePlanTime());
            map.put("changeImplTime", riskAssessment.getChangeImplTime());
            map.put("backTime", riskAssessment.getBackTime());
            map.put("backPlanFlag", riskAssessment.getBackPlanFlag());
            map.put("referDeptCount", riskAssessment.getReferDeptCount());
            map.put("softwareChangeType", riskAssessment.getSoftwareChangeType());
            map.put("appChangeType", riskAssessment.getAppChangeType());
            String referSys = riskAssessment.getReferSys();
            String[] referSysList = referSys.split(",");
            List<String> sys = Arrays.stream(referSysList).distinct().collect(Collectors.toList());
            if (!sysList.isEmpty() && !sys.containsAll(sysList)) {
                sys = sysList;
            }
            map.put("referSysList", sys);
            map.put("currentLevel", riskAssessment.getCurrentLevel());
            map.put("riskLevel", riskAssessment.getRiskLevel());
        } else {
            if (!sysList.isEmpty()) {
                map.put("referSysList", sysList);
            }
            if ("1".equals(type.toString())) {
                map.put("infSysCount", "1");
                map.put("infFuncInterruptType", "1");
                map.put("changeType", "1");
                map.put("changePlanTime", "1");
                map.put("changeImplTime", "1");
                map.put("backTime", "1");
                map.put("backPlanFlag", "1");
                map.put("referDeptCount", "1");
                map.put("currentLevel", "1");
                map.put("riskLevel", "1");
            }
        }
        String dataJson = VueDataJsonUtil.analysisDataJson(formJson, map);
        customerFormContent.setDataJson(dataJson);
        customerFormContent.setFields(map);
        List<Map<String, Object>> buttonList = new ArrayList<>();
        //如果当前操作人是发起人或发起人的代签人并且变更单的状态是待提交
        Map<String, Object> button = new HashMap<>();
        String creator = result.get(ChangeFieldEnum.CREATOR.getName()).toString();
        String status = result.get(ChangeFieldEnum.STATUS.getName()).toString();
        String currentUserId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        String deputyId = base.getVaildDeputyUserId(creator);
        if ((currentUserId.equals(creator)||currentUserId.equals(deputyId)) && ChangeStatusEnum.unSubmit.getName().equals(status)) {
            button.put("flag", 1);
            button.put("buttonName", "评估");
            button.put("buttonUrl", "/customerForm/assessment/add");
            button.put("buttonMethod", "POST");
            button.put("buttonBodyParams", new RiskAssessment());
            //需要看所有非取消的任务单是否数量大于等于1，并且任务单状态为方案审核完成
            Map<String, Object> paramMap = new HashMap<>();
            Map<String, String> queryMap = new HashMap<>();
            paramMap.put(ChangeTaskFieldEnum.STATUS.getName(), "");
            queryMap.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
            queryMap.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), "0");
            List<String> list = base.selectList(ChangeTableNameEnum.CHANGE_TASK, paramMap, queryMap, String.class);
            list = list.stream().filter(s -> !ChangeTaskStatusEnum.canceled.getName().equals(s)).collect(Collectors.toList());
            if (list.isEmpty()) {
                return AjaxResult.warn("请先发起任务");
            }
            for (String s : list) {
                if (ChangeTaskStatusEnum.registered.getName().equals(s)) {
                    return AjaxResult.warn("请等待已注册的任务提交");
                }
            }
        } else {
            button.put("flag", 2);
            button.put("buttonName", "调整风险级别");
            button.put("buttonUrl", "/customerForm/change/adjust/risklevel");
            button.put("buttonMethod", "POST");
            button.put("buttonBodyParams", new RiskAccessRecord());
        }
        buttonList.add(button);
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(customerFormContent));
        jsonObject.put("actionButtonList", buttonList);
        return AjaxResult.success(jsonObject);
    }

    /**
     * 新增或更新
     */
    @Log(title = "新增或更新", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addOrUpdate(@RequestBody RiskAssessment riskAssessment) {
        //判断changeId是否存在
        String changeNo = riskAssessment.getFormId();
        if (changeNo == null || "".equals(changeNo.trim())) {
            return AjaxResult.error("无变更单号！");
        }
        String assessManId = riskAssessmentService.getAssessManIdByChangeNo(changeNo);
        if (assessManId == null) {
            return AjaxResult.error("请检查所属变更单是否存在或是否创建人为空");
        }
        //判断当前登录人是否是初始风险评估人或代理人
        OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
        String currentId = ogUser.getpId();
        String deputyId = base.getVaildDeputyUserId(assessManId);
        if (!assessManId.equals(currentId)&&!currentId.equals(deputyId)) {
            return AjaxResult.error("当前操作人无权限");
        }
        riskAssessmentService.access(riskAssessment);
        String type = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.TYPE, ChangeFieldEnum.EXTRA1, changeNo);
        if (type == null) {
            return AjaxResult.warn("请先填写变更类型");
        }
        Integer level = riskAssessment.getRiskLevel();
        if ("1".equals(type) && level != 1) {
            return AjaxResult.warn("普通变更风险级别只可以为低，请修改评估项，调整风险级别");
        }
        String[] referSysList = riskAssessment.getReferSysList();
        List<String> sys = Arrays.stream(referSysList).distinct().collect(Collectors.toList());
        List<String> sysList = base.getSysListByChangeNo(changeNo);
        sysList = sysList.stream().distinct().collect(Collectors.toList());
        if (!sysList.isEmpty() && !sys.containsAll(sysList)) {
            return AjaxResult.warn("不可减少所涉系统！");
        }
        StringJoiner referSys = new StringJoiner(",");
        for (String refer : referSysList) {
            referSys.add(refer);
        }
        riskAssessment.setReferSys(referSys.toString());
        RiskAssessment result = riskAssessmentService.selectRiskAssessmentById(changeNo);
        if (result != null) {
            return toAjax(riskAssessmentService.updateRiskAssessment(riskAssessment));
        }
        return toAjax(riskAssessmentService.insertRiskAssessment(riskAssessment));
    }



    /* *//**
     * 修改【请填写功能名称】
     *//*
    @GetMapping("/edit/{formId}")
    public String edit(@PathVariable("formId") String formId, ModelMap mmap)
    {
        RiskAssessment riskAssessment = riskAssessmentService.selectRiskAssessmentById(formId);
        mmap.put("riskAssessment", riskAssessment);
        return prefix + "/edit";
    }
*/
    /**
     * 修改保存【请填写功能名称】
     *//*
    @RequiresPermissions("system:assessment:edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(RiskAssessment riskAssessment)
    {
        return toAjax(riskAssessmentService.updateRiskAssessment(riskAssessment));
    }

    *//**
     * 删除【请填写功能名称】
     *//*
    @RequiresPermissions("system:assessment:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(riskAssessmentService.deleteRiskAssessmentByIds(ids));
    }*/
}
