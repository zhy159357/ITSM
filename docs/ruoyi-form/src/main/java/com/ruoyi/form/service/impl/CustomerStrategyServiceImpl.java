package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.constants.EventFieldConstants;
import com.ruoyi.form.domain.ButtonConfigInfo;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.DesignBizJsonData;
import com.ruoyi.form.entity.CompleteParamDto;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignBizIncidentMapper;
import com.ruoyi.form.service.CustomerFormService;
import com.ruoyi.form.service.CustomerStrategyService;
import com.ruoyi.form.service.DesignBizIncidentService;
import com.ruoyi.form.service.DesignBizJsonDataService;
import com.ruoyi.form.util.CustomerStrategyUtil;
import com.ruoyi.form.util.GeneralQueryUtil;
import com.ruoyi.form.util.IDCodeConvertChineseUtil;
import com.ruoyi.form.vo.DetailsPageVO;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyi.form.constants.CustomerStrategyConstants.*;
import static com.ruoyi.form.constants.ProblemConstant.INSTANCE_ID;

/**
 * @ClassName CustomerStrategyServiceImpl
 * @Description 自定义模块抽象实现类
 * @Author JiaQi Zhang
 * @Date 2022/10/2 16:13
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerStrategyServiceImpl implements CustomerStrategyService {

    private final String bizTablePrefix = "design_biz";

    private final CustomerFormMapper customerFormMapper;
    private final DesignBizJsonDataService designBizJsonDataService;
    private final CustomerFormService customerFormService;
    private final TaskService taskService;
    private final ISysRoleService roleService;
    private final DesignBizIncidentService designBizIncidentService;
    private final DesignBizIncidentMapper designBizIncidentMapper;


    @Override
    public AjaxResult customerDetailsPage(String code, String processId, Integer id, String type, String version) {

        dynamicTableName(code);
        // 获取design_form_version表中的主键ID
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, code), Integer.valueOf(version));
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
        //根据业务ID获取表单详情
        DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                .eq(DesignBizJsonData.COL_BIZ_ID, id)
                .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", bizTablePrefix, code)));
        //根据业务ID查询当前状态
        List<Map<String, Object>> statusList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(STATUS, EXTRA1).eq(ID, id));

        //构造表单步骤条信息
        Map<String, Object> formStepMap = GeneralQueryUtil.buildFormStep(currentTableInfo, String.valueOf(statusList.get(0).get(STATUS)));
        //构造追加的JSON  即编号、状态
        List<Map<String, String>> appendJsonMap = CustomerStrategyUtil.buildAppendJson(statusList, formName);
        //构造页面按钮信息
        List<ButtonConfigInfo> buttonConfigInfos = GeneralQueryUtil.buildButtonInfo(processId);

        DetailsPageVO detailsPageVO = DetailsPageVO.builder()
                .appendJsonMap(appendJsonMap)
                .buttonConfigInfos(buttonConfigInfos)
                .formJson(currentNodeFormInfo.getJsonData())
                .formStepMap(formStepMap).build();
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success(detailsPageVO);
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public AjaxResult processComplete(CompleteParamDto completeParamDto) {
        //获取数据库中数据
        List<Map<String, Object>> oldData = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq(INSTANCE_ID, completeParamDto.getInstanceId()));
        //走审批时修改数据库
        customerFormService.dataOperation(completeParamDto.getCode(), completeParamDto.getCustomerFormContent());
        //构造审批对象
        Map<String, Object> variables = GeneralQueryUtil.buildCompleteVariables(completeParamDto.getButtonConfigId(), completeParamDto.getApproveObject(), oldData);
        taskService.addComment(completeParamDto.getTaskId(), completeParamDto.getInstanceId(), completeParamDto.getApproveOpinion());
        taskService.resolveTask(completeParamDto.getTaskId(), variables);
        taskService.claim(completeParamDto.getTaskId(), CustomerBizInterceptor.currentUserThread.get().getUserId());
        taskService.complete(completeParamDto.getTaskId(), variables);
        return null;
    }


    @Override
    public AjaxResult customerDetailsPage(List<Map<String, Object>> formJsonData, List<Map<String, String>> formJsonAppendInfo, String code, Integer id, String processId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("jsonData", formJsonData);
        resultMap.put("appenJsondata", formJsonAppendInfo);
        return AjaxResult.success(resultMap);
    }


    /**
     * 概述控制台
     *
     * @param searchType 检索类型：1-我发起的  2-指派给我
     * @return 返回结果
     */
    @Override
    public AjaxResult summaryConsoleDesk(Page page, String searchType) {
        String currentPersonalId="";
        Map<String,Object> resultMap=new HashMap<>();
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", WorkOrderInformation.biz_table_prefix.getCode(), WorkOrderInformation.incident.getCode()), null);
        CustomerStrategyUtil.verificationNull(currentTableInfo, CustomerBusinessEnum.FORM_VERSION_EXCEPTION);
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        CustomerStrategyUtil.extractedSelectSql("事件",formFieldInfos);
        CustomerStrategyUtil.verificationNull(formFieldInfos, CustomerBusinessEnum.FORM_FIELD_EXCEPTION);

        if ("1".equals(searchType)) {
            // 判断事件管理员角色不加创建人条件
            if (!roleService.judgeHasRole(EventFieldConstants.EVENT_ADMIN_ROLE_ID, CustomerBizInterceptor.currentUserThread.get().getUserId())) {
                currentPersonalId=CustomerBizInterceptor.currentUserThread.get().getUserId();
            }
            IPage<Map<String, Object>> mapIPage = designBizIncidentMapper.summaryConsoleDesk(page, currentPersonalId);
            ((List<Map<String,Object>>) mapIPage.getRecords()).forEach(a -> {
                a.put("taskId", "");
                a.put("popType", "1");
            });
            resultMap.put("fieldInfo", formFieldInfos);
            resultMap.put("pageListInfo", mapIPage);
            IDCodeConvertChineseUtil.convertIdToName(WorkOrderInformation.incident.getCode(), (List<Map<String, Object>>) mapIPage.getRecords());
            IDCodeConvertChineseUtil.convertEnumToName(WorkOrderInformation.incident.getCode(), (List<Map<String, Object>>) mapIPage.getRecords());
            return AjaxResult.success(resultMap);
        } else {
            // 判断事件管理员角色不加当前处理人条件
            if (!roleService.judgeHasRole(EventFieldConstants.EVENT_ADMIN_ROLE_ID, CustomerBizInterceptor.currentUserThread.get().getUserId())) {
                currentPersonalId=CustomerBizInterceptor.currentUserThread.get().getUserId();
            }
            IPage<Map<String, Object>> mapIPage = designBizIncidentMapper.summaryTodoConsoleDesk(page, currentPersonalId);
            ((List<Map<String,Object>>) mapIPage.getRecords()).forEach(a -> {
                String instanceId = String.valueOf(a.get("instance_id"));
                //通过实例ID查询当前任务ID
                List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                        .processInstanceId(instanceId)
                        .list();    // 例如请假会签，会同时拥有多个任务

                if (CollectionUtils.isEmpty(taskList)) {
                    taskList = taskService.createTaskQuery().processInstanceId(instanceId).list();
                }
                a.put("taskId", CollectionUtils.isEmpty(taskList) ? "" : ((TaskEntityImpl) taskList.get(0)).getId());
                a.put("popType", "2");
            });
            resultMap.put("fieldInfo", formFieldInfos);
            IDCodeConvertChineseUtil.convertIdToName(WorkOrderInformation.incident.getCode(), (List<Map<String, Object>>) mapIPage.getRecords());
            IDCodeConvertChineseUtil.convertEnumToName(WorkOrderInformation.incident.getCode(), (List<Map<String, Object>>) mapIPage.getRecords());
            resultMap.put("pageListInfo", mapIPage);
        }
        return AjaxResult.success(resultMap);
    }


    @Override
    public AjaxResult console(Page page) {

        return AjaxResult.success();
    }

    /**
     * 构造动态表名
     *
     * @param code 表名
     */
    private void dynamicTableName(String code) {
        MybatisPlusConfig.customerTableName.set(String.format("%s_%s", bizTablePrefix, code));
    }
}
