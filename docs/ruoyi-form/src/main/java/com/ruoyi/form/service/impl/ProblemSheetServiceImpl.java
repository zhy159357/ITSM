package com.ruoyi.form.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.activiti.bmp.entity.BmpEntity;
import com.ruoyi.activiti.bmp.service.IBmpService;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.constants.EventFlowConstants;
import com.ruoyi.form.constants.ProblemFlowConstants;
import com.ruoyi.form.domain.ProblemSheet;
import com.ruoyi.form.enums.ProblemStatus;
import com.ruoyi.form.mapper.ProblemSheetMapper;
import com.ruoyi.form.service.IProblemSheetService;
import com.ruoyi.system.service.IOgTypeinfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static com.ruoyi.form.constants.ProblemFlowConstants.*;
import static com.ruoyi.form.enums.ProblemStatus.*;

/**
 * 问题单Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-06-21
 */
@Service
public class ProblemSheetServiceImpl  extends ServiceImpl<ProblemSheetMapper, ProblemSheet>  implements IProblemSheetService
{
    private final Logger log = LoggerFactory.getLogger(ProblemSheetServiceImpl.class);

    @Autowired
    private ProblemSheetMapper problemSheetMapper;

    @Autowired
    private IBmpService bmpService;

    @Autowired
    private IOgTypeinfoService ogTypeinfoService;

    private final String PROBLEM_SHEET_PROCESS_DEFINITION_KEY = "bizIMP";

    /**
     * 查询问题单
     * 
     * @param problemId 问题单ID
     * @return 问题单
     */
    @Override
    public ProblemSheet selectProblemSheetById(String problemId)
    {
        return problemSheetMapper.selectProblemSheetById(problemId);
    }

    /**
     * 查询问题单列表
     * 
     * @param problemSheet 问题单
     * @return 问题单
     */
    @Override
    public List<ProblemSheet> selectProblemSheetList(ProblemSheet problemSheet)
    {
        return problemSheetMapper.selectProblemSheetList(problemSheet);
    }

    /**
     * 新增问题单
     * 
     * @param problemSheet 问题单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertProblemSheet(ProblemSheet problemSheet)
    {
        problemSheet.setCreateBy(ShiroUtils.getUserId());
        problemSheet.setCreateTime(DateUtils.getNowDate());
        return problemSheetMapper.insertProblemSheet(problemSheet);
    }

    /**
     * 修改问题单
     * 
     * @param problemSheet 问题单
     * @return 结果
     */
    @Override
    @Transactional
    public int updateProblemSheet(ProblemSheet problemSheet)
    {
        problemSheet.setUpdateBy(ShiroUtils.getUserId());
        problemSheet.setUpdateTime(DateUtils.getNowDate());
        return problemSheetMapper.updateProblemSheet(problemSheet);
    }

    /**
     * 删除问题单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteProblemSheetByIds(String ids)
    {
        return problemSheetMapper.deleteProblemSheetByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除问题单信息
     * 
     * @param problemId 问题单ID
     * @return 结果
     */
    @Override
    public int deleteProblemSheetById(String problemId)
    {
        return problemSheetMapper.deleteProblemSheetById(problemId);
    }

    @Transactional
    @Override
    public BmpEntity startProcess(String problemId, Map<String, Object> variables) {
            BmpEntity bmpEntity = new BmpEntity();
            bmpEntity.setBusinessKey(problemId);
            bmpEntity.setProcessDefinitionKey("bizIMP");
            bmpEntity.setProcessVariables(variables);
            bmpService.startProcess(bmpEntity);
            return bmpEntity;
    }

    /**
     * 查询待办
     *
     * @param processDefinitionKey
     * @param description
     * @param problemSheet
     * @return
     */
    @Override
    public List<ProblemSheet> selectProblemAgencyList(String processDefinitionKey, String description, ProblemSheet problemSheet) {
            BmpEntity bmpEntity = new BmpEntity();
            bmpEntity.setProcessDefinitionKey(processDefinitionKey);
            bmpEntity.setDescription(description);
            // 是否查询组任务标识
            String groupFlag = (String) problemSheet.getParams().get("groupFlag");
            bmpEntity.setGroupFlag(StringUtils.isNotEmpty(groupFlag) ? Boolean.valueOf(groupFlag) : false);
            List<BmpEntity> userTask = bmpService.getUserTask(bmpEntity);
            if(CollectionUtils.isEmpty(userTask)) {
                return new ArrayList<>();
            }
            List<ProblemSheet> collect = userTask.stream().map(bmp -> {
                        problemSheet.setProblemId(bmp.getBusinessKey());
                        ProblemSheet problem = this.selectProblemSheetByCondition(problemSheet);
                        if(problem != null) {
                            problem.getParams().put("taskId", bmp.getTaskId());
                        }
                        return problem;
                    }).filter(item -> {
                        if ("审核".equals(description)) {
                            return item != null && Arrays.asList(COMPLIANCE_AUDIT.getCode(), TECHNOLOGY_AUDIT.getCode(), SOLUTION_AUDIT.getCode()).contains(item.getStatus());
                        } else if ("接收问题".equals(description)) {
                            return item != null && Arrays.asList(ASSIGNED.getCode(), UNDER_INVESTIGATION.getCode()).contains(item.getStatus());
                        } else if ("根因已明,制定解决方案".equals(description)) {
                            return item != null && Arrays.asList(SOLUTION_PENDING.getCode()).contains(item.getStatus());
                        } else if ("解决".equals(description)) {
                            return item != null && Arrays.asList(SOLVING.getCode()).contains(item.getStatus());
                        }  else if ("确认".equals(description)) {
                            return item != null && Arrays.asList(AUDITOR_CONFIRMING.getCode(), ADMIN_CONFIRMING.getCode(),
                                    ORIGINATOR_CONFIRMING.getCode(), ORIGINATE_DEPART_MANAGER_CONFIRMING.getCode(), GENERAL_MANAGER_AUDIT.getCode(), OBSERVING.getCode()).contains(item.getStatus());
                        }
                        return item != null;
                    }).sorted(Comparator.comparing(ProblemSheet::getCreateTime).reversed())
                    .collect(Collectors.toList());
            return collect;

    }

    /**
     * 取消问题单
     *
     * @param id
     */
    @Override
    @Transactional
    public void cancelProblemSheetById(String id) {
        ProblemSheet problemSheet = this.selectProblemSheetById(id);
        if(Arrays.asList(CANCEL.getCode(), CLOSE.getCode()).contains(problemSheet.getStatus())) {
            throw new BusinessException("该问题单已取消或已关闭，不允许再操作!");
        }
        String userId = ShiroUtils.getUserId();
        // todo 管理员可以在问题单关闭前任意阶段取消问题单 发起人只能在草稿阶段取消问题单 管理员角色
//        if (!userId.equals("管理员") && !(userId.equals(problemSheet.getCreateBy()) && ProblemStatus.WAIT_SUBMIT.getCode().equals(problemSheet.getStatus()))) {
//            throw new BusinessException("管理员和问题单发起人才有权限删除!");
//        }

        // 非草稿状态需要撤销流程
        if (!ProblemStatus.WAIT_SUBMIT.getCode().equals(problemSheet.getStatus())) {
            // 取消
            bmpService.cancelProcess(id);
        }

        // 更新问题单状态
        ProblemSheet problem = new ProblemSheet();
        problem.setProblemId(id);
        problem.setStatus(ProblemStatus.CANCEL.getCode());// 已取消
        problem.setStage(ProblemStatus.stageMap.get(problem.getStatus()));
        problem.setUpdateBy(ShiroUtils.getUserId());
        problem.setUpdateTime(DateUtils.getNowDate());
        problem.setProblemCloseTime(DateUtils.getNowDate());
        problemSheetMapper.updateProblemSheet(problem);
    }

    /**
     * 问题单审核
     *
     * @param problemSheet
     */
    @Transactional
    @Override
    public void assignProblem(ProblemSheet problemSheet) {
        this.dealParam(problemSheet);
        BmpEntity bmpEntity = new BmpEntity();
        bmpEntity.setFlowConditionKey(PROBLEM_FLOW_BRANCH_KEY);
        bmpEntity.setFlowConditionValue(PROBLEM_FLOW_BRANCH_POSITIVE1);
        this.setFlowCommonPrams(bmpEntity, problemSheet);
        // 合规审核
        if (COMPLIANCE_AUDIT.getCode().equals(problemSheet.getStatus())) {
            bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM_FLOW_NODE_KEY_ASSIGN);
            bmpEntity.setFlowApprovalValue(problemSheet.getProblemAuditor());
            problemSheet.setStatus(TECHNOLOGY_AUDIT.getCode());
            problemSheet.setProblemManager(ShiroUtils.getUserId());
        } else if (TECHNOLOGY_AUDIT.getCode().equals(problemSheet.getStatus())) {
            // 技术审核
            String problemSolver = problemSheet.getProblemSolver();
            bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM__FLOW_NODE_KEY_SOLVER);
            bmpEntity.setFlowApprovalValue(problemSolver);
            problemSheet.setStatus(ASSIGNED.getCode());
        } else if (SOLUTION_AUDIT.getCode().equals(problemSheet.getStatus())){
           // 解决方案审核
            String problemSolver = problemSheet.getProblemAuditor();
            bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM__FLOW_NODE_KEY_SOLVER);
            bmpEntity.setFlowApprovalValue(problemSolver);
            problemSheet.setStatus(SOLVING.getCode());
            this.setSolutionModifyNumAndPlanSolveTimeModifyNum(problemSheet);
        }
        bmpService.complete(bmpEntity);
        // 流程执行成功更新状态
        problemSheet.setStage(stageMap.get(problemSheet.getStatus()));
        problemSheet.setProblemCurrentHandler(ShiroUtils.getUserId());
        this.updateProblemSheet(problemSheet);
    }

    private void dealParam(ProblemSheet problemSheet) {
        if (StringUtils.isNotBlank(problemSheet.getProblemSolverDepartment())
                && problemSheet.getProblemSolverDepartment().endsWith(",")) {
            problemSheet.setProblemSolverDepartment(problemSheet.getProblemSolverDepartment().substring(0, problemSheet.getProblemSolverDepartment().length() - 1));
        }
        if (StringUtils.isNotBlank(problemSheet.getProblemAuditor())
                && problemSheet.getProblemAuditor().endsWith(",")) {
            problemSheet.setProblemAuditor(problemSheet.getProblemAuditor().substring(0, problemSheet.getProblemAuditor().length() - 1));
        }
        if (StringUtils.isNotBlank(problemSheet.getProblemSolverDepartmentName())
                && problemSheet.getProblemSolverDepartmentName().endsWith(",")) {
            problemSheet.setProblemSolverDepartmentName(problemSheet.getProblemSolverDepartmentName().substring(0, problemSheet.getProblemSolverDepartmentName().length() - 1));
        }
        if (StringUtils.isNotBlank(problemSheet.getProblemAuditorName())
                && problemSheet.getProblemAuditorName().endsWith(",")) {
            problemSheet.setProblemAuditorName(problemSheet.getProblemAuditorName().substring(0, problemSheet.getProblemAuditorName().length() - 1));
        }
    }

    private void setSolutionModifyNumAndPlanSolveTimeModifyNum(ProblemSheet problemSheet) {
        ProblemSheet problemSheetOld = problemSheetMapper.selectProblemSheetById(problemSheet.getProblemId());
        if (StringUtils.isNotBlank(problemSheetOld.getProblemSolutionSummary())) {
            problemSheet.setSolutionModifyNum(0l);
        } else {
            problemSheet.setSolutionModifyNum(problemSheetOld.getSolutionModifyNum() + 1);
        }

//        if () {
//            problemSheet.setSolutionModifyNum(0l);
//        } else {
//            problemSheet.setSolutionModifyNum(problemSheetOld.getSolutionModifyNum() + 1);
//        }
    }

    @Transactional
    @Override
    public void receiveProblem(ProblemSheet problemSheet) {
        BmpEntity bmpEntity = new BmpEntity();
        this.setFlowCommonPrams(bmpEntity, problemSheet);
        bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM__FLOW_NODE_KEY_SOLVER);
        bmpEntity.setFlowApprovalValue(problemSheet.getProblemSolver()); //TODO
        bmpEntity.setFlowApprovalValue("8b8080f457fffe39015800015ce60006");
        bmpEntity.setFlowConditionKey(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_KEY);
        bmpEntity.setFlowConditionValue(PROBLEM_FLOW_BRANCH_POSITIVE1);
        bmpEntity.setComment("提交根因");
        bmpService.complete(bmpEntity);
        // 流程执行成功更新状态
        problemSheet.setStatus(SOLUTION_PENDING.getCode());
        problemSheet.setStage(stageMap.get(problemSheet.getStatus()));
        problemSheet.setProblemCurrentHandler(ShiroUtils.getUserId());
        problemSheet.setProblemSolverLastUpdated(DateUtils.getNowDate());
        problemSheet.setTempSolveFlag("1");
        this.updateProblemSheet(problemSheet);
    }

    @Override
    public void translateCatType(ProblemSheet problemSheet) {
        List<OgTypeinfo> ogTypeInfoList = ogTypeinfoService.selectOgTypeinfoList(new OgTypeinfo());
        String[] categoryArr = new String[4];
        // 翻译类别
        if(StringUtils.isNotEmpty(problemSheet.getProblemCategory())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(problemSheet.getProblemCategory().equals(ogTypeInfo.getTypeNo())) {
                    categoryArr[0] = ogTypeInfo.getTypeName();
                }
            });
        }
        // 翻译子类
        if(StringUtils.isNotEmpty(problemSheet.getProblemSubclass())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(problemSheet.getProblemSubclass().equals(ogTypeInfo.getTypeNo())) {
                    categoryArr[1] = ogTypeInfo.getTypeName();
                }
            });
        }
        // 翻译条目
        if(StringUtils.isNotEmpty(problemSheet.getProblemEntry())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(problemSheet.getProblemEntry().equals(ogTypeInfo.getTypeNo())) {
                    categoryArr[2] = ogTypeInfo.getTypeName();
                }
            });
        }
        // 翻译子条目
        if(StringUtils.isNotEmpty(problemSheet.getProblemSubentry())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                    if(problemSheet.getProblemSubentry().equals(ogTypeInfo.getTypeNo())) {
                    categoryArr[3] = ogTypeInfo.getTypeName();
                }
            });
        }
        if(StringUtils.isNotEmpty(categoryArr)) {
            problemSheet.setProblemCategoryName(categoryArr[0]);
            problemSheet.setProblemSubclassName(categoryArr[1]);
            problemSheet.setProblemEntryName(categoryArr[2]);
            problemSheet.setProblemSubentryName(categoryArr[3]);
        }
        this.transProblemCause(ogTypeInfoList, problemSheet);
    }

    /**
     * 根因分类转换
     *
     * @param ogTypeInfoList
     * @param problemSheet
     */
    private void transProblemCause(List<OgTypeinfo> ogTypeInfoList, ProblemSheet problemSheet) {
        String[] causeArr = new String[2];
        // 翻译类别
        if(StringUtils.isNotEmpty(problemSheet.getProblemCauseClass1())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(problemSheet.getProblemCauseClass1().equals(ogTypeInfo.getTypeNo())) {
                    causeArr[0] = ogTypeInfo.getTypeName();
                }
            });
        }
        // 翻译子类
        if(StringUtils.isNotEmpty(problemSheet.getProblemCauseClass2())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(problemSheet.getProblemCauseClass2().equals(ogTypeInfo.getTypeNo())) {
                    causeArr[1] = ogTypeInfo.getTypeName();
                }
            });
        }
        if(StringUtils.isNotEmpty(causeArr)) {
            problemSheet.setProblemCauseClass1Name(causeArr[0]);
            problemSheet.setProblemCauseClass2Name(causeArr[1]);
        }
    }

    /**
     * 问题单退回
     *
     * @param problemSheet
     */
    @Override
    @Transactional
    public void returnProblem(ProblemSheet problemSheet) {
        BmpEntity bmpEntity = new BmpEntity();
        bmpEntity.setFlowConditionKey(PROBLEM_FLOW_BRANCH_KEY);
        bmpEntity.setFlowConditionValue(PROBLEM_FLOW_BRANCH_ZERO);
        // 合规审核退回到草稿
        if (COMPLIANCE_AUDIT.getCode().equals(problemSheet.getStatus())) {
            this.setFlowCommonPrams(bmpEntity, problemSheet);
            problemSheet.setStatus(WAIT_SUBMIT.getCode());
            problemSheet.setProblemManager(ShiroUtils.getUserId());
        } else if (TECHNOLOGY_AUDIT.getCode().equals(problemSheet.getStatus())) {
            // 技术审核退回到合规审核
            this.setFlowCommonPrams(bmpEntity, problemSheet);
            problemSheet.setStatus(COMPLIANCE_AUDIT.getCode());
        } else if (Arrays.asList(ASSIGNED.getCode(), UNDER_INVESTIGATION.getCode()).contains(problemSheet.getStatus())){
            // 已指派和调查中退回到技术审核
            this.setFlowCommonPrams(bmpEntity, problemSheet);
            bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM_FLOW_NODE_KEY_ASSIGN);
            bmpEntity.setFlowApprovalValue(problemSheet.getProblemAuditor());
            problemSheet.setStatus(TECHNOLOGY_AUDIT.getCode());
        } else if (Arrays.asList(SOLUTION_PENDING.getCode()).contains(problemSheet.getStatus())){
            // 根因已明退回到合规审核
            this.setFlowCommonPrams(bmpEntity, problemSheet);
            problemSheet.setStatus(COMPLIANCE_AUDIT.getCode());
        } else if (Arrays.asList(SOLUTION_AUDIT.getCode()).contains(problemSheet.getStatus())){
            // 解决方案审核中退回到根因已明制定解决方案
            this.setFlowCommonPrams(bmpEntity, problemSheet);
            bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM__FLOW_NODE_KEY_SOLVER);
            bmpEntity.setFlowApprovalValue(problemSheet.getProblemSolver());
            problemSheet.setStatus(SOLUTION_PENDING.getCode());
        }
        bmpService.complete(bmpEntity);
        // 流程执行成功更新状态
        problemSheet.setStage(stageMap.get(problemSheet.getStatus()));
        problemSheet.setProblemCurrentHandler(ShiroUtils.getUserId());
        this.updateProblemSheet(problemSheet);
    }

    /**
     * 关闭问题单
     *
     * @param id
     */
    @Override
    @Transactional
    public void closeProblem(String id) {
        ProblemSheet problemSheet = this.selectProblemSheetById(id);
        if(Arrays.asList(CANCEL.getCode(), CLOSE.getCode()).contains(problemSheet.getStatus())) {
            throw new BusinessException("该问题单已取消或已关闭，不允许再操作!");
        }

        String userId = ShiroUtils.getUserId();
        // todo 管理员可以在问题单关闭前任意阶段取消问题单 发起人只能在草稿阶段取消问题单 管理员角色
//        if (!userId.equals("管理员") && !(userId.equals(problemSheet.getCreateBy()) && ProblemStatus.WAIT_SUBMIT.getCode().equals(problemSheet.getStatus()))) {
//            throw new BusinessException("管理员和问题单发起人才有权限删除!");
//        }

        // 关闭
        bmpService.closeProcess(id, "关闭流程");

        // 更新问题单状态
        ProblemSheet problem = new ProblemSheet();
        problem.setProblemId(id);
        problem.setStatus(ProblemStatus.CLOSE.getCode());// 已关闭
        problem.setStage(ProblemStatus.stageMap.get(problem.getStatus()));
        problem.setUpdateBy(ShiroUtils.getUserId());
        problem.setUpdateTime(DateUtils.getNowDate());
        problem.setProblemCloseType("");// todo
        problem.setProblemCloseTime(DateUtils.getNowDate());
        problemSheetMapper.updateProblemSheet(problem);
    }

    /**
     * 问题单确认
     *
     * @param problemSheet
     */
    @Override
    @Transactional
    public void confirmProblem(ProblemSheet problemSheet) {
        BmpEntity bmpEntity = new BmpEntity();
        String comment = (String)problemSheet.getParams().get("comment");
        if (StringUtils.isBlank(comment)) {
            problemSheet.getParams().put("comment", "问题单确认");
        }
        this.setFlowCommonPrams(bmpEntity, problemSheet);
        bmpEntity.setFlowConditionKey(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_KEY);

        // 根据remark值来判断流程走向
        String flag = problemSheet.getRemark();
        if ("1".equals(flag)) {
            // 根据参数里问题单状态设置下一节点处理人及对应问题单待更新为的状态
            if (AUDITOR_CONFIRMING.getCode().equals(problemSheet.getStatus())) {
                bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM_ORIGINATOR);
                bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());//todo
                problemSheet.setStatus(ProblemStatus.ORIGINATOR_CONFIRMING.getCode());
            } else if (ORIGINATOR_CONFIRMING.getCode().equals(problemSheet.getStatus())) {
                bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM_ORIGINATE_DEPART_MANAGER);
                bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());//todo
                problemSheet.setStatus(ProblemStatus.ORIGINATE_DEPART_MANAGER_CONFIRMING.getCode());
            } else if (Arrays.asList(ORIGINATE_DEPART_MANAGER_CONFIRMING.getCode(), GENERAL_MANAGER_AUDIT.getCode(), OBSERVING.getCode()).contains(problemSheet.getStatus())) {
                bmpEntity.setFlowApprovalKey(ProblemFlowConstants.COMPLIANCE_ID);
                bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());//todo
                problemSheet.setStatus(ProblemStatus.ADMIN_CONFIRMING.getCode());
            } else if (ADMIN_CONFIRMING.getCode().equals(problemSheet.getStatus())) {
                bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM__FLOW_NODE_KEY_GENERAL_MANAGER);
                bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());//todo
                problemSheet.setStatus(ProblemStatus.GENERAL_MANAGER_AUDIT.getCode());
            }
        } else if ("3".equals(flag)) {
            bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM_ORIGINATOR);
            bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());//todo
//            bmpEntity.setFlowApprovalValue(problemSheet.getProblemOriginator());
            problemSheet.setStatus(ProblemStatus.OBSERVING.getCode());
        } else if (Arrays.asList("4", "5", "6").contains(flag)) {
            bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM__FLOW_NODE_KEY_SOLVER);
            bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());//todo
//            bmpEntity.setFlowApprovalValue(problemSheet.getProblemSolver());
            problemSheet.setStatus("4".equals(flag)
                    ? UNDER_INVESTIGATION.getCode()
                    : ("5".equals(flag)
                    ? SOLUTION_PENDING.getCode()
                    : SOLVING.getCode()));
        }

        switch (flag) {
            case PROBLEM_FLOW_BRANCH_POSITIVE1:
                if (AUDITOR_CONFIRMING.getCode().equals(problemSheet.getStatus())
                        && problemSheet.getProblemOriginator().equals(problemSheet.getProblemSolver())
                        || (ORIGINATOR_CONFIRMING.getCode().equals(problemSheet.getStatus())
                        && problemSheet.getProblemOriginateDepartManager().equals(problemSheet.getProblemAuditor()))) {
                    bmpEntity.setFlowConditionValue(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE2);
                    bmpEntity.setFlowApprovalKey(ProblemFlowConstants.COMPLIANCE_ID);
                    bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());//todo
                    problemSheet.setStatus(ProblemStatus.ADMIN_CONFIRMING.getCode());
                } else {
                    bmpEntity.setFlowConditionValue(PROBLEM_FLOW_BRANCH_POSITIVE1);
                }
                break;
            case ProblemFlowConstants.PROBLEM_FLOW_BRANCH_ZERO:
                problemSheet.setStatus(ProblemStatus.ADMIN_CONFIRMING.getCode());
                bmpEntity.setFlowApprovalKey(ProblemFlowConstants.COMPLIANCE_ID);
                bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());//todo
                bmpEntity.setFlowConditionValue(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_ZERO);
                break;
            case ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE3:
                bmpEntity.setFlowConditionValue(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE2);
                break;
            case ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE4:
                bmpEntity.setFlowConditionValue(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE3);
                break;
            case ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE5:
                bmpEntity.setFlowConditionValue(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE4);
                break;
            case ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE6:
                bmpEntity.setFlowConditionValue(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE5);
                break;
            default:
                throw new BusinessException("流程分支有误!");
        }
        bmpService.complete(bmpEntity);
        // 流程执行成功更新状态
        problemSheet.setStage(stageMap.get(problemSheet.getStatus()));
        problemSheet.setProblemCurrentHandler(ShiroUtils.getUserId());
        this.updateProblemSheet(problemSheet);
    }

    /**
     * 解决问题单
     *
     * @param problemSheet
     */
    @Override
    @Transactional
    public void solutionProblem(ProblemSheet problemSheet) {
        BmpEntity bmpEntity = new BmpEntity();
        problemSheet.getParams().put("comment", "问题已解决");
        this.setFlowCommonPrams(bmpEntity, problemSheet);
        bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM_FLOW_NODE_KEY_ASSIGN);
        bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());//todo
        bmpEntity.setFlowConditionKey(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_KEY);
        // 根据remark值来判断流程走向
        String flag = problemSheet.getRemark();
        switch (flag) {
            case PROBLEM_FLOW_BRANCH_POSITIVE1:
                bmpEntity.setFlowConditionValue(PROBLEM_FLOW_BRANCH_POSITIVE1);
                break;
            case ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE2:
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE2);
                break;
            case ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE3:
                bmpEntity.setFlowConditionValue(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE3);
                break;
            default:
                throw new BusinessException("流程分支有误!");
        }
        bmpService.complete(bmpEntity);
        // 流程执行成功更新状态
        problemSheet.setStatus(ProblemStatus.AUDITOR_CONFIRMING.getCode());
        problemSheet.setStage(stageMap.get(problemSheet.getStatus()));
        problemSheet.setProblemCurrentHandler(ShiroUtils.getUserId());
        problemSheet.setProblemSolveTime(DateUtils.getNowDate());
        this.updateProblemSheet(problemSheet);
    }

    /**
     * 处理问题单
     *
     * @param problemSheet
     */
    @Transactional
    @Override
    public void dealProblem(ProblemSheet problemSheet) {
        BmpEntity bmpEntity = new BmpEntity();
        problemSheet.getParams().put("comment", "提交解决方案");
        this.setFlowCommonPrams(bmpEntity, problemSheet);
        bmpEntity.setFlowApprovalKey(ProblemFlowConstants.PROBLEM_FLOW_NODE_KEY_ASSIGN);
        bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());//todo
        bmpEntity.setFlowConditionKey(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_KEY);
        // 根据remark值来判断流程走向
        String flag = problemSheet.getRemark();
        switch (flag) {
            case PROBLEM_FLOW_BRANCH_POSITIVE1:
                bmpEntity.setFlowConditionValue(PROBLEM_FLOW_BRANCH_POSITIVE1);
                break;
            case ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE2:
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE2);
                break;
            case ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE3:
                bmpEntity.setFlowConditionValue(ProblemFlowConstants.PROBLEM_FLOW_BRANCH_POSITIVE3);
                break;
            default:
                throw new BusinessException("流程分支有误!");
        }
        bmpService.complete(bmpEntity);
        // 流程执行成功更新状态
        problemSheet.setStatus(ProblemStatus.SOLUTION_AUDIT.getCode());
        problemSheet.setStage(stageMap.get(problemSheet.getStatus()));
        problemSheet.setProblemCurrentHandler(ShiroUtils.getUserId());
        this.updateProblemSheet(problemSheet);
    }

    /**
     * 设置流程公用参数
     *
     * @param bmpEntity
     * @param problemSheet
     */
    public void setFlowCommonPrams(BmpEntity bmpEntity, ProblemSheet problemSheet) {
        bmpEntity.setBusinessKey(problemSheet.getProblemId());
        bmpEntity.setTaskId((String) problemSheet.getParams().get("taskId"));
        bmpEntity.setComment((String) problemSheet.getParams().get("comment"));
        bmpEntity.setUserId(ShiroUtils.getUserId());
        bmpEntity.setProcessDefinitionKey(PROBLEM_SHEET_PROCESS_DEFINITION_KEY);
    }

    private ProblemSheet selectProblemSheetByCondition(ProblemSheet problemSheet) {
        return problemSheetMapper.selectProblemSheetById(problemSheet.getProblemId());
    }
}
