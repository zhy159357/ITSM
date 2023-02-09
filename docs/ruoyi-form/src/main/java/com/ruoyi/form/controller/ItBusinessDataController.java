package com.ruoyi.form.controller;

import com.ruoyi.activiti.bmp.service.IBmpService;
import com.ruoyi.activiti.bmp.entity.BmpEntity;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.form.domain.ItBusinessData;
import com.ruoyi.form.domain.ItSourceData;
import com.ruoyi.form.service.IItBusinessDataService;
import com.ruoyi.form.util.FormUtil;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务-通用Controller
 *
 * @author ruoyi
 * @date 2022-04-06
 */
@Controller
@RequestMapping("/business/data")
public class ItBusinessDataController extends BaseController {
    private String prefix = "business/data";

    @Autowired
    private IItBusinessDataService itBusinessDataService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IBmpService bmpService;

    @GetMapping()
    public String data() {
        return prefix + "/data";
    }

    /**
     * 查询业务-通用列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ItBusinessData itBusinessData) {
        startPage();
        List<ItBusinessData> list = itBusinessDataService.selectItBusinessDataList(itBusinessData);
        return getDataTable(list);
    }

    /**
     * 导出业务-通用列表
     */
    @Log(title = "业务-通用", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ItBusinessData itBusinessData) {
        List<ItBusinessData> list = itBusinessDataService.selectItBusinessDataList(itBusinessData);
        ExcelUtil<ItBusinessData> util = new ExcelUtil<ItBusinessData>(ItBusinessData.class);
        return util.exportExcel(list, "data");
    }

    /**
     * 新增业务-通用
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存业务-通用
     */
    @Log(title = "业务-通用", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ItBusinessData itBusinessData) {
        return toAjax(itBusinessDataService.insertItBusinessData(itBusinessData));
    }

    /**
     * 修改业务-通用
     */
    @GetMapping("/edit/{businessId}")
    public String edit(@PathVariable("businessId") String businessId, ModelMap mmap) {
        ItBusinessData itBusinessData = itBusinessDataService.selectItBusinessDataById(businessId);
        mmap.put("itBusinessData", itBusinessData);
        return prefix + "/edit";
    }

    /**
     * 修改保存业务-通用
     */
    @Log(title = "业务-通用", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ItBusinessData itBusinessData) {
        return toAjax(itBusinessDataService.updateItBusinessData(itBusinessData));
    }

    /**
     * 删除业务-通用
     */
    @Log(title = "业务-通用", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(itBusinessDataService.deleteItBusinessDataByIds(ids));
    }

    /**
     * 测试后端组装form
     */
    @GetMapping("/testShowForm/{businessId}")
    public String testShowForm(@PathVariable("businessId") String businessId, ModelMap map) {
        ItBusinessData itBusinessData = itBusinessDataService.selectItBusinessDataById(businessId);
        String formId = itBusinessData.getFormId();
        List<ItSourceData> sourceDataList = itBusinessData.getSourceDataList();
        Map<String, List<ItSourceData>> listMap = sourceDataList.stream().collect(Collectors.groupingBy(ItSourceData::getFormKey));
        Map<String, Object> params = FormUtil.packageFormHtml("", formId, businessId, listMap);
        map.put("formHtmlMap", params);
        return prefix + "/testForm";
    }

    /**
     * 测试启动流程
     */
    @PostMapping("/startProcess")
    @ResponseBody
    @Transactional
    public AjaxResult startProcess(ItBusinessData businessData) {
        String businessId = businessData.getBusinessId();
        String processDefinitionKey ="testProcess";
        String processDefinitionId = "testProcess:11:1152507";
        BmpEntity bmpEntity = new BmpEntity();
        bmpEntity.setUserId(ShiroUtils.getUserId());
        bmpEntity.setProcessDefinitionId(processDefinitionId);
        bmpEntity.setProcessDefinitionKey(processDefinitionKey);
        bmpEntity.setProcessDefinitionVersion(11);
        bmpEntity.setBusinessKey(businessId);
        try {
            bmpService.startProcess(bmpEntity);
            ItBusinessData itBusinessData = new ItBusinessData();
            itBusinessData.setBusinessId(businessId);
            itBusinessData.setProcessId(bmpEntity.getProcessInstanceId());
            int rows = itBusinessDataService.updateItBusinessData(itBusinessData);
            if(rows > 0) {
                logger.info("流程启动成功 formId:{},businessId:{},processInstanceId:{}", businessData.getFormId(), businessId, bmpEntity.getProcessInstanceId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("启动流程失败，失败原因:" + e.getMessage());
        }
        return AjaxResult.success();
    }

    /**
     * 业务待办页面
     */
    @GetMapping("/businessAgency")
    public String businessAgency() {
        return prefix + "/businessAgency";
    }

    /**
     * 查询待办
     */
    @PostMapping("/businessAgencyList")
    @ResponseBody
    public TableDataInfo businessAgencyList(ItBusinessData businessData) {
        BmpEntity bmpEntity = new BmpEntity();
        //String description = (String) businessData.getParams().get("description");
        bmpEntity.setProcessDefinitionKey("testProcess");
        bmpEntity.setUserId(ShiroUtils.getUserId());
        List<BmpEntity> reList = bmpService.getUserTask(bmpEntity);
        List<ItBusinessData> resultList = new ArrayList<>();
        for (BmpEntity bmp : reList) {
            String businessKey = bmp.getBusinessKey();
            String taskId = bmp.getTaskId();
            if (StringUtils.isNotEmpty(businessKey)) {
                businessData.setBusinessId(businessKey);
                ItBusinessData businessData1 = itBusinessDataService.selectItBusinessDataByCondition(businessData);
                if (businessData1 != null) {
                    businessData1.getParams().put("taskId", taskId);
                    resultList.add(businessData1);
                }
            }
        }
        // 待办列表根据生成时间倒序排序
        if (resultList.size() > 1) {
            resultList = resultList.stream().sorted(Comparator.comparing(ItBusinessData::getCreateTime).reversed()).collect(Collectors.toList());
        }
        return getDataTable_ideal(resultList);
    }

    /**
     * 流程审批节点页面form表单加载
     */
    @GetMapping("/approval/{businessId}/{taskId}")
    public String approval(@PathVariable("businessId") String businessId, @PathVariable("taskId") String taskId, ModelMap map) {
        ItBusinessData itBusinessData = itBusinessDataService.selectItBusinessDataById(businessId);
        String formId = itBusinessData.getFormId();
        List<ItSourceData> sourceDataList = itBusinessData.getSourceDataList();
        Map<String, List<ItSourceData>> listMap = sourceDataList.stream().collect(Collectors.groupingBy(ItSourceData::getFormKey));
        Map<String, Object> params = FormUtil.packageFormHtml(taskId, formId, businessId, listMap);
        map.put("formHtmlMap", params);
        map.put("businessId", businessId);
        map.put("taskId", taskId);
        return prefix + "/approval";
    }

    /**
     * 流程审批节点页面form表单加载及审批
     */
    @PostMapping("/approval")
    @ResponseBody
    @Transactional
    public AjaxResult approval(ItBusinessData businessData) {
        String businessId = businessData.getBusinessId();
        ItBusinessData itBusinessData = itBusinessDataService.selectItBusinessDataById(businessId);
        String taskId = (String)businessData.getParams().get("taskId");
        if(StringUtils.isNotEmpty(taskId)) {
            Task task = activitiCommService.getTaskByTaskId(taskId);
            if(StringUtils.isNotEmpty(task.getFormKey())) {
                // 业务数据表发起状态formId不为空，然后拼接流程任务中的formKey字段保存起来
                if(StringUtils.isNotEmpty(itBusinessData.getFormId()) && !itBusinessData.getFormId().contains(task.getFormKey())) {
                    businessData.setFormId(itBusinessData.getFormId() + "|" + task.getFormKey());
                }
            }
        }
        BmpEntity bmpEntity = new BmpEntity();
        bmpEntity.setProcessDefinitionKey("testProcess");
        bmpEntity.setUserId(ShiroUtils.getUserId());
        bmpEntity.setTaskId(taskId);
        bmpEntity.setComment("同意");
        bmpEntity.setFlowConditionKey("key");
        bmpEntity.setFlowConditionValue("2");
        try {
            bmpService.complete(bmpEntity);
            itBusinessDataService.updateItBusinessData(businessData);
            logger.info("流程执行成功 businessId:{}, taskId:{}", businessId, taskId);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }

    @PostMapping("/testMybatisPlus")
    @ResponseBody
    public AjaxResult testMybatisPlus() {
    // 测试mybatis-plus的使用情况
        List<ItBusinessData> list = itBusinessDataService.selectList();
        list.forEach(it -> {
            logger.info("businessId:{},businessNo:{}", it.getBusinessId(),it.getBusinessNo());
        });
        return AjaxResult.success("测试mybatis-plus成功！");
    }
}
