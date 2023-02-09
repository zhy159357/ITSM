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
import com.ruoyi.form.enums.ProblemStatus;
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
public class ProblemServiceImpl implements CustomerStrategyService {
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
        log.info("问题单定制化开发审批详情接口");
        return AjaxResult.success("问题单测试111");
    }

    @Override
    public AjaxResult processComplete(CompleteParamDto completeParamDto) {
        log.info("问题单定制化开发审批接口");
        return null;
    }

    @Override
    public AjaxResult customerDetailsPage(List<Map<String, Object>> formJsonData, List<Map<String, String>> formJsonAppendInfo, String code, Integer id, String processId) {
        GeneralQueryUtil.dynamicTableName(code);
        List<Map<String, Object>> problemList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(STATUS, ORIGINATOR_ID).eq(ID, id));
        if (CollectionUtils.isEmpty(problemList)) {
            throw new BusinessException("问题单表数据不存在!");
        }

        String type;
        String userId = problemList.get(0).get(ORIGINATOR_ID).toString();
        if (ObjectUtil.equal(problemList.get(0).get(STATUS).toString(), ProblemStatus.WAIT_SUBMIT.getInfo())) {
            type = "1";
            // 此时判断当前登录人不是问题发起人且不是管理员则直接返回
            AjaxResult resultMap = getAjaxResult(type, formJsonData, formJsonAppendInfo, userId);
            if (resultMap != null) {
                return resultMap;
            }
            type = "2";
        } else if (Arrays.asList(ProblemStatus.CANCEL.getInfo(), ProblemStatus.CLOSE.getInfo()).contains(problemList.get(0).get(STATUS).toString())) {
            type = "3";
            // 此时判断当前登录人不是问题发起人也是问题管理员则直接返回
            AjaxResult resultMap = getAjaxResult(type, formJsonData, formJsonAppendInfo, userId);
            if (resultMap != null) return resultMap;
        } else if (Arrays.asList(ProblemStatus.CANCEL.getInfo(), ProblemStatus.CLOSE.getInfo()).contains(problemList.get(0).get(STATUS).toString())) {
            type = "3";
            // 此时判断当前登录人不是问题发起人也是问题管理员则直接返回
            AjaxResult resultMap = getAjaxResult(type, formJsonData, formJsonAppendInfo, userId);
            if (resultMap != null) return resultMap;
        } else {
            type = "2";
            // 当前登录人不是问题管理员且不是当前单子待办人员则直接返回 (不是管理员看当前登录人在此流程实例下有没有待办)
            List<Task> list = taskService.createTaskQuery().processInstanceId(processId).processDefinitionKey(code).taskCandidateGroupIn(getGroupsList(CustomerBizInterceptor.currentUserThread.get().getUserId())).list();
            List<Task> list1 = taskService.createTaskQuery().processInstanceId(processId).processDefinitionKey(code).taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId()).list();
            List<Task> allTaskList = new ArrayList<>();
            allTaskList.addAll(list);
            allTaskList.addAll(list1);
            if (!checkAdmin(CustomerBizInterceptor.currentUserThread.get().getUserId())
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
     * 根据人员id获取机构id｜角色id集合｜工作组集合id
     *
     * @param userId
     * @return
     */

    /**
     * 各种状态下判断返回结果
     *
     * @param formJsonData
     * @param formJsonAppendInfo
     * @param userId
     * @return
     */
    private AjaxResult getAjaxResult(String type, List<Map<String, Object>> formJsonData, List<Map<String, String>> formJsonAppendInfo, String userId) {
        if ("3".equals(type) && !ObjectUtil.equal(userId, CustomerBizInterceptor.currentUserThread.get().getUserId())
                && !checkAdmin(CustomerBizInterceptor.currentUserThread.get().getUserId())) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("jsonData", formJsonData);
            resultMap.put("appenJsondata", formJsonAppendInfo);
            return AjaxResult.success(resultMap);
        } else if ("1".equals(type) && !ObjectUtil.equal(userId, CustomerBizInterceptor.currentUserThread.get().getUserId())
                      && !checkAdmin(CustomerBizInterceptor.currentUserThread.get().getUserId())) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("jsonData", formJsonData);
            resultMap.put("appenJsondata", formJsonAppendInfo);
            return AjaxResult.success(resultMap);
        }
        return null;
    }

    /**
     * 判断用户是否为管理员
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
     * 根据人员id获取机构id｜角色id集合｜工作组集合id
     *
     * @param userId
     * @return
     */
    public List<String> getGroupsList(String userId) {
        List<String> reList = new ArrayList<>();
        // 获取工作组
        List<OgGroup> ogGroups = sysWorkService.selectGroupByUserId(userId);
        // 查询角色
        List<OgRole> ogRoles = sysRoleService.selectRolesByUserId(userId);
        // 机构
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
