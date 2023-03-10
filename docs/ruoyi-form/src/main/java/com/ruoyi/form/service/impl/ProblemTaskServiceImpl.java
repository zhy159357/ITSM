package com.ruoyi.form.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.form.constants.CustomerFlowConstants;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.entity.CompleteParamDto;
import com.ruoyi.form.enums.ProblemTaskStatus;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.service.CustomerFormService;
import com.ruoyi.form.service.CustomerStrategyService;
import com.ruoyi.form.util.GeneralQueryUtil;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysWorkService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.form.constants.ProblemConstant.*;

/**
 * @ClassName ProblemServiceImpl
 * @Description TODO
 * @Author JiaQi Zhang
 * @Date 2022/10/2 16:25
 */
@Service
@Slf4j
public class ProblemTaskServiceImpl implements CustomerStrategyService {
    @Resource
    private CustomerFormService customerFormService;
    @Resource
    CustomerFormMapper customerFormMapper;
    @Resource
    ISysWorkService sysWorkService;

    @Resource
    IOgPersonService ogPersonService;

    @Resource
    ISysRoleService sysRoleService;

    @Resource
    TaskService taskService;

    @Override
    public AjaxResult customerDetailsPage(String code, String processId, Integer id, String type, String version) {
        log.info("??????????????????????????????????????????");
        return AjaxResult.success("???????????????111");
    }

    @Override
    public AjaxResult processComplete(CompleteParamDto completeParamDto) {
        log.info("????????????????????????????????????");
        return null;
    }

    @Override
    public AjaxResult customerDetailsPage(List<Map<String, Object>> formJsonData, List<Map<String, String>> formJsonAppendInfo, String code, Integer id, String processId) {
        GeneralQueryUtil.dynamicTableName(code);
        List<Map<String, Object>> problemTaskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(STATUS, ORIGINATOR_ID, "assistant_id").eq(ID, id));
        if (CollectionUtils.isEmpty(problemTaskList)) {
            throw new BusinessException("?????????????????????????????????!");
        }

        String type;
        // ?????????
        String userId = problemTaskList.get(0).get(ORIGINATOR_ID).toString();
        // ?????????
        String assistantId = problemTaskList.get(0).get("assistant_id").toString();
        if (ObjectUtil.equal(problemTaskList.get(0).get(STATUS).toString(), ProblemTaskStatus.WAIT_SUBMIT.getInfo())) {
            type = "1";
            // ?????????????????????????????????????????????????????????????????????
            AjaxResult resultMap = getAjaxResult(type, formJsonData, formJsonAppendInfo, userId, assistantId);
            if (resultMap != null) {
                return resultMap;
            }
            type = "2";
        } else if (Arrays.asList(ProblemTaskStatus.CANCEL.getInfo(), ProblemTaskStatus.CLOSE.getInfo()).contains(problemTaskList.get(0).get(STATUS).toString())) {
            type = "3";
            // ?????????????????????????????????????????????????????????????????????????????????????????????
            AjaxResult resultMap = getAjaxResult("3", formJsonData, formJsonAppendInfo, userId, assistantId);
            if (resultMap != null) return resultMap;
        } else {
            type = "2";
            // ???????????????????????????????????????????????????????????????????????????????????? (?????????????????????????????????????????????????????????????????????)
            List<Task> list = taskService.createTaskQuery().processInstanceId(processId).processDefinitionKey(code).taskCandidateGroupIn(getGroupsList(CustomerBizInterceptor.currentUserThread.get().getUserId())).list();
            List<Task> list1 = taskService.createTaskQuery().processInstanceId(processId).processDefinitionKey(code).taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId()).list();
            List<Task> allTaskList = new ArrayList<>();
            allTaskList.addAll(list);
            allTaskList.addAll(list1);
            if (!(checkAdmin(CustomerBizInterceptor.currentUserThread.get().getUserId())
                    || CustomerBizInterceptor.currentUserThread.get().getUserId().equals(userId))
                    && CollectionUtils.isEmpty(allTaskList)) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("jsonData", formJsonData);
                resultMap.put("appenJsondata", formJsonAppendInfo);
                return AjaxResult.success(resultMap);
            }
        }
        return customerFormService.approvalPopUp(code, processId, id, type, null);
    }

    /**
     * ????????????id????????????id?????????id????????????????????????id
     *
     * @param userId
     * @return
     */

    /**
     * ?????????????????????????????????
     *
     * @param formJsonData
     * @param formJsonAppendInfo
     * @param userId
     * @return
     */
    private AjaxResult getAjaxResult(String type, List<Map<String, Object>> formJsonData, List<Map<String, String>> formJsonAppendInfo, String userId, String assistantId) {
        if ("3".equals(type) && !ObjectUtil.equal(userId, CustomerBizInterceptor.currentUserThread.get().getUserId())
                && !checkAdmin(CustomerBizInterceptor.currentUserThread.get().getUserId())) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("jsonData", formJsonData);
            resultMap.put("appenJsondata", formJsonAppendInfo);
            return AjaxResult.success(resultMap);
        } else if ("1".equals(type)
                && !(ObjectUtil.equal(userId, CustomerBizInterceptor.currentUserThread.get().getUserId()))
                && !checkAdmin(CustomerBizInterceptor.currentUserThread.get().getUserId())) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("jsonData", formJsonData);
            resultMap.put("appenJsondata", formJsonAppendInfo);
            return AjaxResult.success(resultMap);
        }
        return null;
    }

    /**
     * ??????????????????????????????
     */
    public Boolean checkAdmin(String userId) {
        Boolean isAdmin = false;
        List<String> allIds = getGroupsList(userId);
        if (allIds.contains(CustomerFlowConstants.PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.TINGJING_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.CHENDU_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.NANJIN_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.SUZHOU_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.NINGBO_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.BEIJIN_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.SHENZHENG_BRANCH_PROBLEM_ADMIN)
                || allIds.contains(CustomerFlowConstants.HANZHOU_BRANCH_PROBLEM_ADMIN)
        ) {
            isAdmin = true;
        }
        return isAdmin;
    }

    /**
     * ????????????id????????????id?????????id????????????????????????id
     *
     * @param userId
     * @return
     */
    public List<String> getGroupsList(String userId) {
        List<String> reList = new ArrayList<>();
        // ???????????????
        List<OgGroup> ogGroups = sysWorkService.selectGroupByUserId(userId);
        // ????????????
        List<OgRole> ogRoles = sysRoleService.selectRolesByUserId(userId);
        // ??????
        OgPerson ogPerson = ogPersonService.selectOgPersonEvoById(userId);
        if (com.ruoyi.common.utils.StringUtils.isNotEmpty(ogPerson)) {
            reList.add(ogPerson.getOrgId());
        }
        if (!org.springframework.util.CollectionUtils.isEmpty(ogGroups)) {
            List<String> groupIdList = ogGroups.stream().map(group -> {
                return group.getGroupId();
            }).collect(Collectors.toList());
            reList.addAll(groupIdList);
        }
        if (!org.springframework.util.CollectionUtils.isEmpty(ogRoles)) {
            List<String> rIdList = ogRoles.stream().map(role -> {
                return role.getRid();
            }).collect(Collectors.toList());
            reList.addAll(rIdList);
        }
        return reList;
    }


    @Override
    public AjaxResult summaryConsoleDesk(Page page, String searchType) {
        return null;
    }

    @Override
    public AjaxResult console(Page page) {
        return null;
    }
}
