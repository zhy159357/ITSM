package com.ruoyi.form.activiti;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.form.domain.ChangeDeptEntity;
import com.ruoyi.form.domain.ChangeDeptPersonVo;
import com.ruoyi.form.domain.DesignBizJsonData;
import com.ruoyi.form.domain.DesignBizShieldWarn;
import com.ruoyi.form.domain.DesignFormVersion;
import com.ruoyi.form.domain.FormApprovalFree;
import com.ruoyi.form.domain.ImplRecord;
import com.ruoyi.form.domain.OperationDetails;
import com.ruoyi.form.domain.RelationLog;
import com.ruoyi.form.enums.ChangeAutoAssigneeEnum;
import com.ruoyi.form.enums.ChangeDefineKeyEnum;
import com.ruoyi.form.enums.ChangeFieldEnum;
import com.ruoyi.form.enums.ChangeRoleEnum;
import com.ruoyi.form.enums.ChangeStatusEnum;
import com.ruoyi.form.enums.ChangeTableNameEnum;
import com.ruoyi.form.enums.ChangeTaskDeptEnum;
import com.ruoyi.form.enums.ChangeTaskFieldEnum;
import com.ruoyi.form.enums.ChangeTaskStatusEnum;
import com.ruoyi.form.enums.SqlTypeEnum;
import com.ruoyi.form.service.DesignBizJsonDataService;
import com.ruoyi.form.service.ForeignService;
import com.ruoyi.form.service.FormVersionService;
import com.ruoyi.form.service.IChangeDeptService;
import com.ruoyi.form.service.IChangePersonService;
import com.ruoyi.form.service.IChangeService;
import com.ruoyi.form.service.IChangeTaskOrgService;
import com.ruoyi.form.service.IFormApprovalFreeService;
import com.ruoyi.form.service.IImplRecordService;
import com.ruoyi.form.service.IRelationLogService;
import com.ruoyi.form.service.OperationDetailsService;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.domain.OgDeputyCfg;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.IOgDeputyCfgService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.OgPostService;
import com.ruoyi.system.service.server.EntegorServerImpl;

@Component
public class Base {
    public static final String BUSINESS_KEY = "businessKey";
    public static final String RECODE = "recode";
    public static final String USERS = "users";
    public static final String CHANGE = "change";
    public static final String CHANGE_TASK = "changeTask";
    public static final String ADMIN_ORG_LIST = "adminOrgList";

    @Autowired
    IChangeDeptService changeDeptService;
    @Autowired
    FormVersionService formVersionService;
    @Autowired
    IChangePersonService changePersonService;
    @Autowired
    TaskService taskService;
    @Autowired
    ISysUserService userService;
    @Autowired
    OgPostService ogPostService;
    @Autowired
    IChangeService changeService;
    @Autowired
    IOgUserService ogUserService;
    @Autowired
    ISysDeptService sysDeptService;
    @Autowired
    IChangeTaskOrgService changeTaskOrgService;
    @Autowired
    IOgPersonService ogPersonService;
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    DesignBizJsonDataService designBizJsonDataService;
    @Autowired
    IImplRecordService iImplRecordService;
    @Autowired
    OperationDetailsService operationDetailsService;
    @Autowired
    IFormApprovalFreeService formApprovalFreeService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    IRelationLogService relationLogService;
    @Autowired
    ForeignService foreignService;
    @Autowired
    IOgDeputyCfgService ogDeputyCfgService;


    public List<String> getAdminDeputyIdList(String userId){
        List<String> result = new ArrayList<>();
        OgDeputyCfg ogDeputyCfg = new OgDeputyCfg();
        ogDeputyCfg.setDirector(userId);
        ogDeputyCfg.setStatus("0");
        ogDeputyCfg.setCfgType(2L);
        List<OgDeputyCfg> list = ogDeputyCfgService.selectOgDeputyCfgList(ogDeputyCfg);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(!list.isEmpty()){
            OgDeputyCfg old = list.get(0);
            String startTime = old.getStartTime();
            String endTime = old.getEndTime();
            LocalDateTime start = LocalDateTime.parse(startTime,dateTimeFormatter);
            LocalDateTime end = LocalDateTime.parse(endTime,dateTimeFormatter);
            if(start.isBefore(now)&&end.isAfter(now)){
                 String sec = old.getSecondaryId();
                 result.add(sec);
            }
        }
        OgDeputyCfg ogDeputyCfg2 = new OgDeputyCfg();
        ogDeputyCfg2.setSecondary(userId);
        ogDeputyCfg2.setStatus("0");
        ogDeputyCfg2.setCfgType(2L);
        List<OgDeputyCfg> seclist = ogDeputyCfgService.selectOgDeputyCfgList(ogDeputyCfg2);
        seclist.forEach(deputy->{
            String startTime = deputy.getStartTime();
            String endTime = deputy.getEndTime();
            LocalDateTime start = LocalDateTime.parse(startTime,dateTimeFormatter);
            LocalDateTime end = LocalDateTime.parse(endTime,dateTimeFormatter);
            if(start.isBefore(now)&&end.isAfter(now)){
                result.add(deputy.getDirectorId());
            }
        });
        return result.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getChangeAdminDisFreeApprovalUserList(String changeId, List<String> userList) {
        List<String> resultList = new ArrayList<>();
        String changeNo = getChangeColumnValueBySingleCondition(ChangeFieldEnum.EXTRA1, ChangeFieldEnum.ID, changeId);
        userList.forEach(user -> {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(user);
            FormApprovalFree formApprovalFreeCondition = new FormApprovalFree();
            formApprovalFreeCondition.setApproval(user);
            formApprovalFreeCondition.setDefineKey(ChangeDefineKeyEnum.adminApproval.getName());
            formApprovalFreeCondition.setType("change");
            formApprovalFreeCondition.setFormId(changeId);
            List<FormApprovalFree> formApprovalFreeList = formApprovalFreeService.selectFormApprovalFreeList(formApprovalFreeCondition);
            if (formApprovalFreeList.isEmpty()) {
                FormApprovalFree formApproval = new FormApprovalFree();
                formApproval.setApproval(user);
                formApproval.setFormId(changeId);
                formApproval.setComment("通过");
                formApproval.setFormNo(changeNo);
                formApproval.setType("change");
                formApproval.setDefineKey(ChangeDefineKeyEnum.adminApproval.getName());
                formApproval.setApprovalName(ogPerson.getpName());
                formApprovalFreeService.insertFormApprovalFree(formApproval);
                resultList.add(user);
            } else if (formApprovalFreeList.get(0).getFlag() == 0) {
                resultList.add(user);
            }
        });
        return resultList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void lockChange(String changeNo, String sysId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("*", "");
        query.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        query.put(ChangeTaskFieldEnum.REFER_SYS.getName(), sysId);
        List<Map<String, Object>> list = selectNonCancelListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        list.forEach(p -> {
            int lock = Integer.parseInt(p.get(ChangeTaskFieldEnum.LOCK.getName()).toString());
            ++lock;
            updateChangeTaskSingle(ChangeTaskFieldEnum.ID, p.get(ChangeTaskFieldEnum.ID.getName()).toString(), ChangeTaskFieldEnum.LOCK, String.valueOf(lock));
        });
    }


    @Transactional(rollbackFor = Throwable.class)
    public void unLockChange(String changeNo, String sysId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("*", "");
        query.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        query.put(ChangeTaskFieldEnum.REFER_SYS.getName(), sysId);
        List<Map<String, Object>> list = selectNonCancelListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        list.forEach(p -> {
            int lock = Integer.parseInt(p.get(ChangeTaskFieldEnum.LOCK.getName()).toString());
            --lock;
            if (lock <= 0) {
                lock = 0;
            }
            updateChangeTaskSingle(ChangeTaskFieldEnum.ID, p.get(ChangeTaskFieldEnum.ID.getName()).toString(), ChangeTaskFieldEnum.LOCK, String.valueOf(lock));
        });
    }

    public boolean changeLock(String changeNo) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("*", "");
        query.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        List<Map<String, Object>> list = selectNonCancelListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        for (Map<String, Object> map : list) {
            String lockStr = map.get(ChangeTaskFieldEnum.LOCK.getName()).toString();
            if (!"0".equals(lockStr)) {
                return true;
            }
        }
        return false;
    }


    public List<String> getAllInstanceId(String changeNo) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), "");
        query.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        List<String> list = selectNonCancelList(ChangeTableNameEnum.CHANGE_TASK, param, query, String.class);
        String instanceId = getChangeColumnValueBySingleCondition(ChangeFieldEnum.INSTANCE_ID, ChangeFieldEnum.EXTRA1, changeNo);
        list.add(instanceId);
        return list;
    }

    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult activeAllProcess(String changeNo) {
        List<String> list = getAllInstanceId(changeNo);
        list.forEach(instanceId -> {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            if (processInstance.isSuspended()) {
                runtimeService.activateProcessInstanceById(processInstance.getId());
            }
        });
        return AjaxResult.success("变更单已完全解锁");
    }

    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult suspendAllProcess(String changeNo) {
        List<String> list = getAllInstanceId(changeNo);
        list.forEach(instanceId -> {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            if (!processInstance.isSuspended()) {
                runtimeService.suspendProcessInstanceById(processInstance.getId());
            }
        });
        return AjaxResult.success("锁定成功");
    }

    @Transactional(rollbackFor = Throwable.class)
    public List<String> getChangeNoListByCreateTime(String startDate, String endDate) {
        String sql = "select changeNo from design_biz_change where 1=1 ";
        if (startDate != null && !"".equals(startDate)) {
            sql = sql + " and created_time >= :startDate ";
        }
        if (endDate != null && !"".equals(endDate)) {
            sql = sql + " and created_time <= :endDate ";
        }
        Map<String, String> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValues(param);
        return jdbcTemplate.queryForList(sql, mapSqlParameterSource, String.class);
    }

    /**
     * 获取变更经理的userId
     *
     * @return
     */
    public String getChangeMangerUserId() {
        return getUserIdByPostName(ChangeRoleEnum.changeManger.getName());
    }

    public String getVaildDeputyUserId(String approval){
        OgDeputyCfg ogDeputyCfg = new OgDeputyCfg();
        ogDeputyCfg.setDirector(approval);
        ogDeputyCfg.setStatus("0");
        ogDeputyCfg.setCfgType(2L);
        List<OgDeputyCfg> list = ogDeputyCfgService.selectOgDeputyCfgList(ogDeputyCfg);
        if(!list.isEmpty()){
            OgDeputyCfg old = list.get(0);
            String startTime = old.getStartTime();
            String endTime = old.getEndTime();
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(startTime,dateTimeFormatter);
            LocalDateTime end = LocalDateTime.parse(endTime,dateTimeFormatter);
            if(start.isBefore(now)&&end.isAfter(now)){
                return old.getSecondaryId();
            }
        }
        return null;
    }
    public String getBranchChangeMangerUserId(String orgId) {
        return getBranchPostUserId(orgId, ChangeRoleEnum.branchChangeManager);
    }

    public String getBranchPostUserId(String orgId, ChangeRoleEnum roleEnum) {
        String branchOrgId = changePersonService.selectDept(orgId);
        return getUserIdByPostNameAndOrgId(roleEnum.getName(), branchOrgId);
    }

    public String getBranchGeneralMangerUserId(String orgId) {
        return getBranchPostUserId(orgId, ChangeRoleEnum.branchGeneralManager);
    }

    @Transactional(rollbackFor = Throwable.class)
    public String updateAdminCurrentProcessor(List<String> users, String changeId) {
        StringJoiner joiner = new StringJoiner(",");
        StringJoiner approvalJoiner = new StringJoiner(",");
        users.forEach(p -> {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(p);
            OgUser ogUser = ogUserService.selectOgUserByUserId(ogPerson.getpId());
            String currentProcessor = ogPerson.getpName();
            if (ogUser != null) {
                currentProcessor = ogPerson.getpName() + "(" + ogUser.getUsername() + ")";
            }
            joiner.add(currentProcessor);
            approvalJoiner.add(ogPerson.getpId());
        });
        String userList = joiner.toString();
        String approvalList = approvalJoiner.toString();
        updateChangeSingle(ChangeFieldEnum.ID, changeId, ChangeFieldEnum.CURRENT_PROCESSOR, userList);
        updateChangeSingle(ChangeFieldEnum.ID, changeId, ChangeFieldEnum.APPROVAL, approvalList);
        return userList;
    }

    public String formatDuring(long ms) {
        long dayDiv = 1000 * 60 * 60 * 24;
        long hourDiv = 1000 * 60 * 60;
        long miDiv = 1000 * 60;
        long day = ms / dayDiv;
        long hour = (ms % dayDiv) / hourDiv;
        long minute = (ms % hourDiv) / miDiv;
        long second = (ms % miDiv) / 1000;
        StringBuilder during = new StringBuilder();
        if (day != 0) {
            during.append(day).append("天");
        }
        if (day != 0 || hour != 0) {
            during.append(hour).append("小时");
        }
        if (day != 0 || hour != 0 || minute != 0) {
            during.append(minute).append("分钟");
        }
        during.append(second).append("秒");
        return during.toString();
    }

    private String getUserIdByPostName(String postName) {
        OgPost post = new OgPost();
        post.setPostName(postName);
        List<OgPost> list = ogPostService.selectPostList(post);
        OgPost ogPost = list.stream().filter(p -> postName.equals(p.getPostName())).findAny().orElse(null);
        if (ogPost == null) {
            return null;
        }
        String postId = ogPost.getPostId();
        OgUser user = new OgUser();
        user.setPostId(Long.valueOf(postId));
        List<OgUser> userList = userService.selectAllocatedListPost(user);
        return userList.get(0).getUserId();
    }

    public List<String> getSysListByChangeNo(String changeNo) {
        //通过变更单获取所有任务单里的所涉系统与基础设施
        Map<String, Object> params = new HashMap<>();
        params.put(ChangeTaskFieldEnum.REFER_SYS.getName(), "");
        params.put(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName(), "");
        params.put(ChangeTaskFieldEnum.STATUS.getName(), "");
        Map<String, String> query = new HashMap<>();
        query.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        List<String> referSysList = new ArrayList<>();
        List<Map<String, Object>> list = selectListMap(ChangeTableNameEnum.CHANGE_TASK, params, query);
        list.stream().filter(p -> !ChangeTaskStatusEnum.canceled.getName().equals(p.get(ChangeTaskFieldEnum.STATUS.getName())))
                .filter(p -> {
                    Object sys = p.get(ChangeTaskFieldEnum.REFER_SYS.getName());
                    Object infrastructure = p.get(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName());
                    return (sys != null && !"".equals(sys.toString().trim())) || (infrastructure != null && !"".equals(infrastructure.toString().trim()));
                }).collect(Collectors.toList()).forEach(p -> {
                    Object sys = p.get(ChangeTaskFieldEnum.REFER_SYS.getName());
                    Object ins = p.get(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName());
                    if (sys != null && !"".equals(sys.toString())) {
                        referSysList.add(sys.toString());
                    } else if (ins != null && !"".equals(ins.toString())) {
                        //当成数组处理
                        String insStr = StringUtils.strip(ins.toString(), "[]");
                        insStr = insStr.replaceAll("\"", "");
                        String[] insArr = insStr.split(",");
                        if (insArr.length != 0) {
                            List<String> insList = Arrays.asList(insArr);
                            if (!insList.isEmpty()) {
                                insList = insList.stream().filter(a -> a != null && !"".equals(a)).collect(Collectors.toList());
                                referSysList.addAll(insList);
                            }
                        }
                    }
                });
        return referSysList;
    }

    private String getUserIdByPostNameAndOrgId(String postName, String orgId) {
        OgPost post = new OgPost();
        post.setPostName(postName);
        List<OgPost> list = ogPostService.selectPostList(post);
        String postId = list.get(0).getPostId();
        OgPerson ogPerson = new OgPerson();
        List<String> postIdList = new ArrayList<>();
        postIdList.add(postId);
        ogPerson.getParams().put("postIds", postIdList);
        List<OgPerson> personList = ogPersonService.selectOgPersonByPostIds(ogPerson);
        OgPerson person = personList.stream().filter(p -> {
            String personOrgId = p.getOrgId();
            String branchOrgId = changePersonService.selectDept(personOrgId);
            return orgId.equals(branchOrgId);
        }).findFirst().orElse(null);
        if (person == null) {
            return null;
        }
        return person.getpId();
    }

    public String getGeneralManagerUserId() {
        return getUserIdByPostName(ChangeRoleEnum.generalManager.getName());
    }

    public List<String> getChangeTaskNoListByChangeNo(String changeNo) {
        Map<String, Object> params = new HashMap<>();
        params.put(ChangeTaskFieldEnum.EXTRA1.getName(), "");
        Map<String, String> query = new HashMap<>();
        query.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        return selectList(ChangeTableNameEnum.CHANGE_TASK, params, query, String.class);
    }

    public List<OperationDetails> getAllOperationDetails(String bizNo, Page page) {
        List<String> changeTaskNoList = getChangeTaskNoListByChangeNo(bizNo);
        List<OperationDetails> list = new ArrayList();
        changeTaskNoList.forEach(p -> {
            Page page1 = operationDetailsService.page(page, Wrappers.<OperationDetails>query().eq(OperationDetails.COL_BIZ_NO, p).orderByDesc(OperationDetails.COL_ID));
            list.addAll(page1.getRecords());
        });
        return list;
    }
/*    public String getChangeTableStarterUserId(String id) {
        MybatisPlusConfig.customerTableName.set(CHANGE_TABLE_NAME);
        List<Object> list = customerFormMapper.selectObjs(Wrappers.<CustomerFormContent>query().select(ChangeFieldEnum.CREATOR.getName()).eq(ChangeFieldEnum.ID.getName(), id));
        MybatisPlusConfig.customerTableName.remove();
        return list.get(0).toString();
    }*/

    public String getChangeTableStarterUserId(String id) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeFieldEnum.CREATOR.getName(), "");
        query.put(ChangeFieldEnum.ID.getName(), id);
        return selectObject(ChangeTableNameEnum.CHANGE, param, query, String.class);
    }

    public String getChangeTableStarterOrgId(String id) {
        String userId = getChangeTableStarterUserId(id);
        OgPerson ogPerson = ogPersonService.selectOgPersonById(userId);
        return ogPerson.getOrgId();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateChangePlanTime(String changeId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.PLAN_START_DATE.getName(), "");
        param.put(ChangeTaskFieldEnum.PLAN_OVER_DATE.getName(), "");
        param.put(ChangeTaskFieldEnum.STATUS.getName(), "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        List<Map<String, Object>> list = selectListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        list = list.stream().filter(map->!ChangeTaskStatusEnum.canceled.getName().equals(map.get(ChangeTaskFieldEnum.STATUS.getName()))).collect(Collectors.toList());
        String planStartDate = getMinTime(list, ChangeTaskFieldEnum.PLAN_START_DATE.getName());
        String planOverDate = getMaxTime(list, ChangeTaskFieldEnum.PLAN_OVER_DATE.getName());
        Map<String, Object> changeParam = new HashMap<>();
        changeParam.put(ChangeFieldEnum.PLAN_START_DATE.getName(), planStartDate);
        changeParam.put(ChangeFieldEnum.PLAN_COMPLETE_DATE.getName(), planOverDate);
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(ChangeFieldEnum.ID.getName(), changeId);
        update(ChangeTableNameEnum.CHANGE, changeParam, queryMap);
    }


    public String getMaxTime(List<Map<String, Object>> list, String key) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime maxDateTime = list.stream().map(p -> p.get(key))
                .filter(p -> p != null && !"".equals(p.toString().trim()))
                .map(p -> LocalDateTime.parse(p.toString(), dateTimeFormatter))
                .reduce((a, b) -> a.isAfter(b) ? a : b).orElse(null);
        String maxDate = "";
        if (maxDateTime != null) {
            maxDate = dateTimeFormatter.format(maxDateTime);
        }
        return maxDate;
    }

    public String getMinTime(List<Map<String, Object>> list, String key) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime minDateTime = list.stream().map(p -> p.get(key))
                .filter(p -> p != null && !"".equals(p.toString().trim()))
                .map(p -> LocalDateTime.parse(p.toString(), dateTimeFormatter))
                .reduce((a, b) -> a.isBefore(b) ? a : b).orElse(null);
        String minDate = "";
        if (minDateTime != null) {
            minDate = dateTimeFormatter.format(minDateTime);
        }
        return minDate;
    }

    /*   public String getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum column, String id) {
           String columnName = column.getName();
           MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
           List<Object> list = customerFormMapper.selectObjs(Wrappers.<CustomerFormContent>query().select(columnName).eq(ChangeTaskFieldEnum.ID.getName(), id));
           MybatisPlusConfig.customerTableName.remove();
           return list.get(0).toString();
       }*/
    public String getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum column, String id) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(column.getName(), "");
        query.put(ChangeTaskFieldEnum.ID.getName(), id);
        return selectObject(ChangeTableNameEnum.CHANGE_TASK, param, query, String.class);
    }

    public Boolean taskAllPassed(String changeId, ChangeTaskStatusEnum status) {
        Integer count = getTaskCountByChangeId(changeId);
        Integer passCount = getTaskCountByChangeIdAndStatus(changeId, status);
        return passCount.equals(count);
    }


    public void addChangeSysOperateDetail(String businessKey, String des, String creator) {
        String changeNo = getChangeColumnValueBySingleCondition(ChangeFieldEnum.EXTRA1, ChangeFieldEnum.ID, businessKey);
        OperationDetails operationDetails = new OperationDetails();
        operationDetails.setOperationType("变更单");
        operationDetails.setBizNo(changeNo);
        operationDetails.setDescription(des);
        operationDetails.setCreatedTime(new Date());
        operationDetails.setCreatedBy(creator);
        String name = getUsernameAndPname(creator);
        if ("".equals(name)) {
            name = "系统";
        }
        operationDetails.setCreatedName(name);
        operationDetailsService.saveOperationDetailsforChange(operationDetails);
    }

    public void addChangeTaskSysOperateDetail(String businessKey, String des, String creator) {
        String changeTaskNo = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.EXTRA1, ChangeTaskFieldEnum.ID, businessKey);
        OperationDetails operationDetails = new OperationDetails();
        operationDetails.setOperationType("变更任务单");
        operationDetails.setBizNo(changeTaskNo);
        operationDetails.setDescription(des);
        operationDetails.setCreatedTime(new Date());
        operationDetails.setCreatedBy(creator);
        String name = getUsernameAndPname(creator);
        if ("".equals(name)) {
            name = "系统";
        }
        operationDetails.setCreatedName(name);
        operationDetailsService.saveOperationDetailsforChange(operationDetails);
    }
/*
    public Integer getTaskCountByChangeIdAndStatus(String changeId, ChangeTaskStatusEnum status) {
        MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
        List<Map<String, Object>> pass = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("count(*)").
                and(i -> i.eq(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId).eq(ChangeTaskFieldEnum.STATUS.getName(), status.getName())));
        System.out.println(Arrays.toString(pass.toArray()));
        MybatisPlusConfig.customerTableName.remove();
        return Integer.valueOf(pass.get(0).get("count(*)").toString());
    }*/

    public Integer getTaskCountByChangeIdAndStatus(String changeId, ChangeTaskStatusEnum status) {
        //Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        //param.put("count(*)", "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        query.put(ChangeTaskFieldEnum.STATUS.getName(), status.getName());
        return getCount(ChangeTableNameEnum.CHANGE_TASK, query);
        //return selectObject(ChangeTableNameEnum.CHANGE_TASK, param, query, Integer.class);
    }

    public Integer getTaskCountByChangeIdAndTaskColumn(String changeId, ChangeTaskFieldEnum changeTaskFieldEnum, String changeTaskFieldValue) {
        //Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        //param.put("count(*)", "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        query.put(changeTaskFieldEnum.getName(), changeTaskFieldValue);
        return getCount(ChangeTableNameEnum.CHANGE_TASK, query);
        //return selectObject(ChangeTableNameEnum.CHANGE_TASK, param, query, Integer.class);
    }

/*    public Integer getTaskCount(String changeId, ChangeTaskStatusEnum status, Integer remedyFlag) {
        MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
        List<Map<String, Object>> pass = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("count(*)").
                and(i -> i.eq(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId).
                        eq(ChangeTaskFieldEnum.STATUS.getName(), status.getName())).eq(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), remedyFlag));
        System.out.println(Arrays.toString(pass.toArray()));
        MybatisPlusConfig.customerTableName.remove();
        return Integer.valueOf(pass.get(0).get("count(*)").toString());
    }*/

    public Integer getTaskCount(String changeId, ChangeTaskStatusEnum status, Integer remedyFlag) {
        //Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        //param.put("count(*)", "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        query.put(ChangeTaskFieldEnum.STATUS.getName(), status.getName());
        query.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), String.valueOf(remedyFlag));
        return getCount(ChangeTableNameEnum.CHANGE_TASK, query);
        //return selectObject(ChangeTableNameEnum.CHANGE_TASK, param, query, Integer.class);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void syncRemedyCheckManToOldTask(String remedyChangeTaskId, String endTaskDate) {
        //TODO 判断是否是补救单
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.EXTRA1.getName(), "");
        param.put(ChangeTaskFieldEnum.CHECK_MAN.getName(), "");
        query.put(ChangeTaskFieldEnum.ID.getName(), remedyChangeTaskId);
        Map<String, Object> result = selectMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        String remedyTaskNo = result.get(ChangeTaskFieldEnum.EXTRA1.getName()).toString();
        String taskNo = remedyTaskNo.substring(0, remedyTaskNo.length() - 1);
        String checkMan = result.get(ChangeTaskFieldEnum.CHECK_MAN.getName()).toString();
        updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, taskNo, ChangeTaskFieldEnum.CHECK_MAN, checkMan);
        updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, taskNo, ChangeTaskFieldEnum.IMPL_END_DATE, endTaskDate);
        //更新一个新的组装好数据的json
        String changeTaskId = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.ID, ChangeTaskFieldEnum.EXTRA1, taskNo);
        addOrUpdateChangeTaskJSON(Long.parseLong(changeTaskId));
    }

    public void addTaskJson(Map<String, Object> map, Long changeTaskId) {
        DesignFormVersion formVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", ChangeTableNameEnum.CHANGE_TASK.getName()).eq("is_current", 1));
        String jsonData = formVersion.getJson();
        map = transferTaskMultipleChoiceFieldMap(map);
        jsonData = VueDataJsonUtil.analysisDataJson(jsonData, map);
        DesignBizJsonData designBizJsonData = DesignBizJsonData.builder()
                .bizTable(ChangeTableNameEnum.CHANGE_TASK.getName())
                .jsonData(jsonData)
                .bizId(changeTaskId).build();
        designBizJsonDataService.save(designBizJsonData);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addOrUpdateChangeTaskJSON(Long changeTaskId) {
        DesignBizJsonData designBizJsonData1 = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, changeTaskId).eq(DesignBizJsonData.COL_BIZ_TABLE, ChangeTableNameEnum.CHANGE_TASK.getName()));
        List<Map<String, Object>> taskList = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.ID.getName(), String.valueOf(changeTaskId));
        Map<String, Object> map = taskList.get(0);
        map = transferTaskMultipleChoiceFieldMap(map);
        if (designBizJsonData1 != null) {
            String jsonData = designBizJsonData1.getJsonData();
            jsonData = VueDataJsonUtil.analysisDataJson(jsonData, map);
            designBizJsonData1.setJsonData(jsonData);
            designBizJsonDataService.updateById(designBizJsonData1);
        } else {
            DesignFormVersion formVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", ChangeTableNameEnum.CHANGE_TASK.getName()).eq("is_current", 1));
            String jsonData = formVersion.getJson();
            jsonData = VueDataJsonUtil.analysisDataJson(jsonData, map);
            DesignBizJsonData designBizJsonData = DesignBizJsonData.builder()
                    .bizTable(ChangeTableNameEnum.CHANGE_TASK.getName())
                    .jsonData(jsonData)
                    .bizId(changeTaskId).build();
            designBizJsonDataService.save(designBizJsonData);
        }
    }

    /*
     * 对任务单的多选进行转换
     * */
    public Map<String, Object> transferTaskMultipleChoiceFieldMap(Map<String, Object> map) {
        Object infrastructure = map.get(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName());
        if (infrastructure != null && !"".equals(infrastructure.toString())) {
            String infrastructureStr = infrastructure.toString();
            infrastructureStr = infrastructureStr.replaceAll("\"", "").replaceAll(" ", "");
            infrastructureStr = StringUtils.strip(infrastructureStr, "[]");
            String[] infrastructureArray = infrastructureStr.split(",");
            map.put(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName(), infrastructureArray);
        } else {
            map.put(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName(), new String[]{});
        }
        Object reason = map.get(ChangeTaskFieldEnum.REMEDY_REASON.getName());
        if (reason != null && !"".equals(reason.toString())) {
            String reasonStr = reason.toString();
            reasonStr = reasonStr.replaceAll("\"", "").replaceAll(" ", "");
            reasonStr = StringUtils.strip(reasonStr, "[]");
            String[] remedyReasonArray = reasonStr.split(",");
            map.put(ChangeTaskFieldEnum.REMEDY_REASON.getName(), remedyReasonArray);
        } else {
            map.put(ChangeTaskFieldEnum.REMEDY_REASON.getName(), new String[]{});
        }
        return map;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void closeRemedyOldTask(String remedyChangeTaskId, String nowStr) {
        //TODO 判断是否是补救单
        String remedyTaskNo = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.EXTRA1, ChangeTaskFieldEnum.ID, remedyChangeTaskId);
        String taskNo = remedyTaskNo.substring(0, remedyTaskNo.length() - 1);
        updateChangeTaskStatus(ChangeTaskFieldEnum.EXTRA1, taskNo, ChangeTaskStatusEnum.closed);
        updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, taskNo, ChangeTaskFieldEnum.IMPL_OVER_DATE, nowStr);
        String changeId = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.ID, ChangeTaskFieldEnum.EXTRA1, taskNo);
        addOrUpdateChangeTaskJSON(Long.parseLong(changeId));
    }

    public Integer getTaskCountNoCondition(String changeId, Integer remedyFlag) {
        //Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        //param.put("count(*)", "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        query.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), String.valueOf(remedyFlag));
        return getCount(ChangeTableNameEnum.CHANGE_TASK, query);
        //return selectObject(ChangeTableNameEnum.CHANGE_TASK, param, query, Integer.class);
    }
/*

    public List<Map<String, Object>> getAllColumnsValueByColumn(String tableName, String column, String columnValue) {
        MybatisPlusConfig.customerTableName.set(tableName);
        List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq(column, columnValue));
        MybatisPlusConfig.customerTableName.remove();
        return list;
    }
*/

    public List<Map<String, Object>> getAllColumnsValueByColumn(ChangeTableNameEnum table, String column, String columnValue) {
        return getAllColumnsValueByColumn(table.getName(), column, columnValue);
    }

    public List<Map<String, Object>> getAllColumnsValueByColumn(String table, String column, String columnValue) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("*", "");
        query.put(column, columnValue);
        return selectListMap(table, param, query);
    }


    public List<Map<String, Object>> selectListMap(String table, Map<String, Object> param, Map<String, String> query) {
        String sql = createSql(table, SqlTypeEnum.SELECT, param, query);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValues(query);
        Integer count = getCountByTableName(table, query);
        if (count == 0) {
            return new ArrayList<>();
        }
        return jdbcTemplate.queryForList(sql, mapSqlParameterSource);
    }


    public List<Map<String, Object>> selectNonCancelListMap(ChangeTableNameEnum table, Map<String, Object> param, Map<String, String> query) {
        String sql = createSql(table, SqlTypeEnum.SELECT, param, query);
        sql = sql + " and status!=\"取消\"";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValues(query);
        Integer count = getNONCancelCount(table, query);
        if (count == 0) {
            return new ArrayList<>();
        }
        return jdbcTemplate.queryForList(sql, mapSqlParameterSource);
    }

/*
    public Boolean checkAllTaskReviewedByChangeId(String changeId) {
        MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
        List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(ChangeTaskFieldEnum.REVIEWED.getName()).
                eq(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId));
        MybatisPlusConfig.customerTableName.remove();
        return list.stream().anyMatch(p -> {
            Object reviewed = p.get(ChangeTaskFieldEnum.REVIEWED.getName());
            if (reviewed == null) {
                return false;
            } else {
                Integer re = Integer.valueOf(reviewed.toString());
                return !re.equals(0);
            }
        });
    }*/

    public Boolean checkAllTaskReviewedByChangeId(String changeId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.REVIEWED.getName(), "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        List<String> list = selectList(ChangeTableNameEnum.CHANGE_TASK, param, query, String.class);
        return list.stream().allMatch("1"::equals);
    }

   /* public void completeAllTask(String changeId, String candidate, ChangeTaskStatusEnum status, ChangeTaskStatusEnum updateStatus, Map<String, Object> variables) {
        MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
        List<Map<String, Object>> taskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").
                and(i -> i.eq(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId).
                        eq(ChangeTaskFieldEnum.STATUS.getName(), status.getName()).eq(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), 0)));
        System.out.println(Arrays.toString(taskList.toArray()));
        taskList.forEach(p -> {
            String instanceId = p.get(ChangeTaskFieldEnum.INSTANCE_ID.getName()).toString();
            Task task = taskService.createTaskQuery().processInstanceId(instanceId).taskCandidateOrAssigned(candidate).singleResult();
            System.out.println(task.getName());
            taskService.complete(task.getId(), variables);
            //更新一下下一个节点的状态
            updateChangeTaskStatus(ChangeTaskFieldEnum.INSTANCE_ID, instanceId, updateStatus);
        });
        MybatisPlusConfig.customerTableName.remove();
    }*/

    public void activeAllTask(String changeId, String activityId, Map<String, Object> params, ChangeTaskStatusEnum updateStatus) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("*", "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        query.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), "0");
        List<Map<String, Object>> list = selectListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        activeTaskList(list, activityId, params, updateStatus);
    }


    public void activeTaskList(List<Map<String, Object>> list, String activityId, Map<String, Object> params, ChangeTaskStatusEnum updateStatus) {
        list.forEach(p -> {
            String instanceId = p.get(ChangeTaskFieldEnum.INSTANCE_ID.getName()).toString();
            Execution execution = runtimeService.createExecutionQuery()
                    .processInstanceId(instanceId)
                    .activityId(activityId)
                    .singleResult();
            if (execution != null) {
                String executionId = execution.getId();
                runtimeService.setVariables(executionId, params);
                runtimeService.trigger(executionId);
                updateChangeTaskStatus(ChangeTaskFieldEnum.INSTANCE_ID, instanceId, updateStatus);
                String changeTaskId = p.get(ChangeTaskFieldEnum.ID.getName()).toString();
                if (ChangeTaskStatusEnum.preApprovaling == updateStatus) {
                    String preMan = p.get(ChangeTaskFieldEnum.PRE_APPROVAL.getName()).toString();
                    addChangeTaskSysOperateDetail(changeTaskId, "任务转到" + getUsernameAndPname(preMan) + "预审", "sys");
                } else if (ChangeTaskStatusEnum.registered == updateStatus) {
                    String preMan = p.get(ChangeTaskFieldEnum.ASSIGNEE.getName()).toString();
                    addChangeTaskSysOperateDetail(changeTaskId, "任务转到" + getUsernameAndPname(preMan), "sys");
                } else if (ChangeTaskStatusEnum.waitImpl == updateStatus) {
                    String preMan = p.get(ChangeTaskFieldEnum.PRE_APPROVAL.getName()).toString();
                    Object preManObj = p.get(ChangeTaskFieldEnum.IMPL_MAN.getName());
                    if (preManObj != null) {
                        preMan = preManObj.toString();
                    }
                    addChangeTaskSysOperateDetail(changeTaskId, "任务转到" + getUsernameAndPname(preMan) + "待实施", "sys");
                }
                ChangeUtil.ADPM_POOL.submit(instanceId, () -> {
                    updateAdpmTask(instanceId);
                });
            }
        });
    }

    public void completeAllTask(String changeId, String candidate, ChangeTaskStatusEnum status, ChangeTaskStatusEnum updateStatus, Map<String, Object> variables) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("*", "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        query.put(ChangeTaskFieldEnum.STATUS.getName(), status.getName());
        query.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), "0");
        List<Map<String, Object>> list = selectListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        /*list.forEach(p -> {
            String instanceId = p.get(ChangeTaskFieldEnum.INSTANCE_ID.getName()).toString();
            Task task = taskService.createTaskQuery().processInstanceId(instanceId).taskCandidateOrAssigned(candidate).singleResult();
            System.out.println(task.getName());
            taskService.complete(task.getId(), variables);
            //更新一下下一个节点的状态
            updateChangeTaskStatus(ChangeTaskFieldEnum.INSTANCE_ID, instanceId, updateStatus);
        });*/
        completeTaskList(list, candidate, variables, updateStatus);
    }

    public void completeTaskList(List<Map<String, Object>> list, String candidate, Map<String, Object> variables, ChangeTaskStatusEnum updateStatus) {
        list.forEach(p -> {
            String instanceId = p.get(ChangeTaskFieldEnum.INSTANCE_ID.getName()).toString();
            Task task = taskService.createTaskQuery().processInstanceId(instanceId).taskAssignee(candidate).singleResult();
            System.out.println(task.getName());
            taskService.complete(task.getId(), variables);
            //更新一下下一个节点的状态
            updateChangeTaskStatus(ChangeTaskFieldEnum.INSTANCE_ID, instanceId, updateStatus);
        });
    }

 /*   public void updateChangeStatus(ChangeFieldEnum conditionKey, String conditionValue, ChangeStatusEnum status) {
        String conditionKeyName = conditionKey.getName();
        String statusName = status.getName();
        MybatisPlusConfig.customerTableName.set(CHANGE_TABLE_NAME);
        customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().
                eq(conditionKeyName, conditionValue).set(ChangeFieldEnum.STATUS.getName(), statusName).
                set(ChangeFieldEnum.CHANGE_STATUS.getName(), statusName));
        MybatisPlusConfig.customerTableName.remove();
    }*/

    public void activeWaitImplChange(String instanceId) {
        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(instanceId)
                .activityId(ChangeDefineKeyEnum.waitOver.getName())
                .singleResult();
        if (execution != null) {
            String executionId = execution.getId();
            runtimeService.trigger(executionId);
        }
    }

    public String getUsernameAndPname(String userId) {
        OgPerson ogPerson = ogPersonService.selectOgPersonById(userId);
        if (ogPerson == null) {
            return "";
        }
        OgUser ogUser = ogUserService.selectOgUserByUserId(userId);
        String username = ogPerson.getpName();
        if (ogUser != null) {
            username = username + "(" + ogUser.getUsername() + ")";
        }
        return username;
    }


    @Transactional(rollbackFor = Throwable.class)
    public void updateChangeStatus(ChangeFieldEnum conditionKey, String conditionValue, ChangeStatusEnum status) {
        String conditionKeyName = conditionKey.getName();
        String statusName = status.getName();
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeFieldEnum.STATUS.getName(), statusName);
        param.put(ChangeFieldEnum.CHANGE_STATUS.getName(), statusName);
        query.put(conditionKeyName, conditionValue);
        update(ChangeTableNameEnum.CHANGE, param, query);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ChangeFieldEnum.EXTRA1.getName(), "");
        String changeNo = selectObject(ChangeTableNameEnum.CHANGE, paramMap, query, String.class);
        //更新下问题单关联表
        relationLogService.update(null, Wrappers.<RelationLog>update().eq("request_no", String.valueOf(changeNo)).set("status", status.getName()));
    }

/*    public List<Map<String, Object>> getChangeRow(String changId) {
        MybatisPlusConfig.customerTableName.set(CHANGE_TABLE_NAME);
        List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq(ChangeFieldEnum.ID.getName(), changId));
        MybatisPlusConfig.customerTableName.remove();
        return list;
    }*/

    public List<Map<String, Object>> getChangeRow(String changeId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("*", "");
        query.put(ChangeFieldEnum.ID.getName(), changeId);
        return selectListMap(ChangeTableNameEnum.CHANGE, param, query);
    }

    public List<Map<String, Object>> getChangeTaskRow(String changeTaskId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("*", "");
        query.put(ChangeTaskFieldEnum.ID.getName(), changeTaskId);
        return selectListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
    }
/*

    public void updateChangeTaskStatus(ChangeTaskFieldEnum conditionKey, String conditionValue, ChangeTaskStatusEnum status) {
        String conditionKeyName = conditionKey.getName();
        String statusName = status.getName();
        MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
        customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq(conditionKeyName, conditionValue).
                set(ChangeTaskFieldEnum.STATUS.getName(), statusName).set(ChangeTaskFieldEnum.TASK_STATUS.getName(), statusName));
        MybatisPlusConfig.customerTableName.remove();
    }
*/

    public void updateChangeTaskStatus(ChangeTaskFieldEnum conditionKey, String conditionValue, ChangeTaskStatusEnum status) {
        String conditionKeyName = conditionKey.getName();
        String statusName = status.getName();
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.STATUS.getName(), statusName);
        param.put(ChangeTaskFieldEnum.TASK_STATUS.getName(), statusName);
        query.put(conditionKeyName, conditionValue);
        update(ChangeTableNameEnum.CHANGE_TASK, param, query);
    }

    public void syncTaskStatus(ChangeTaskFieldEnum key, String value) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("status", "");
        query.put(key.getName(), value);
        String status = selectObject(ChangeTableNameEnum.CHANGE_TASK, param, query, String.class);
        Map<String, Object> updateParam = new HashMap<>();
        updateParam.put("taskStatus", status);
        update(ChangeTableNameEnum.CHANGE_TASK, updateParam, query);
    }

    public void syncTaskStatusAndCurrentProcessor(ChangeTaskFieldEnum key, String value) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("status", "");
        param.put("approval", "");
        param.put(ChangeTaskFieldEnum.EXTRA1.getName(), "");
        query.put(key.getName(), value);
        Map<String, Object> map = selectMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        Map<String, Object> updateParam = new HashMap<>();
        updateParam.put(ChangeTaskFieldEnum.TASK_STATUS.getName(), map.get("status").toString());
        String approval = map.get("approval").toString();
        String currentProcessor = "";
        if (approval != null && !"".equals(approval)) {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(approval);
            OgUser ogUser = ogUserService.selectOgUserByUserId(ogPerson.getpId());
            if (ogUser != null) {
                currentProcessor = ogPerson.getpName() + "(" + ogUser.getUsername() + ")";
            }
        }
        updateParam.put("currentProcessor", currentProcessor);
        update(ChangeTableNameEnum.CHANGE_TASK, updateParam, query);
        ImplRecord implRecord = new ImplRecord();
        implRecord.setChangeTaskNo(map.get(ChangeTaskFieldEnum.EXTRA1.getName()).toString());
        implRecord.setTaskStatus(map.get("status").toString());
        iImplRecordService.updateImplRecord(implRecord);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void syncChangeStatusAndCurrentProcessor(ChangeFieldEnum key, String value) {
        syncChangeStatus(key, value);
        syncCurrentProcessor(ChangeTableNameEnum.CHANGE, key, value);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateAdpmTask(String instanceId) {
        String changeId = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.CHANGE_ID, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
        List<Map<String, Object>> list = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE, ChangeFieldEnum.ID.getName(), changeId);
        if (!list.isEmpty()) {
            Map<String, Object> map = list.get(0);
            Object appProcessId = map.get("appProcessId");
            if (appProcessId != null && !"".equals(appProcessId.toString().trim())) {
                String changeTaskNo = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.EXTRA1, ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
                foreignService.updateAdpmChangeTask(changeTaskNo);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateAdpmChange(String instanceId) {
        List<Map<String, Object>> list = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE, ChangeFieldEnum.INSTANCE_ID.getName(), instanceId);
        if (!list.isEmpty()) {
            Map<String, Object> map = list.get(0);
            Object appProcessId = map.get("appProcessId");
            if (appProcessId != null && !"".equals(appProcessId.toString().trim())) {
                String changeNo = map.get(ChangeFieldEnum.EXTRA1.getName()).toString();
                foreignService.modifyAdpmChange(changeNo);
            }
        }
    }

    public void syncChangeStatus(ChangeFieldEnum key, String value) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeFieldEnum.STATUS.getName(), "");
        query.put(key.getName(), value);
        String status = selectObject(ChangeTableNameEnum.CHANGE, param, query, String.class);
        Map<String, Object> updateParam = new HashMap<>();
        updateParam.put(ChangeFieldEnum.CHANGE_STATUS.getName(), status);
        update(ChangeTableNameEnum.CHANGE, updateParam, query);
        Map<String, Object> result = new HashMap<>();
        result.put(ChangeFieldEnum.EXTRA1.getName(), "");
        String changeNo = selectObject(ChangeTableNameEnum.CHANGE, result, query, String.class);
        //更新下问题单关联表
        relationLogService.update(null, Wrappers.<RelationLog>update().eq("request_no", String.valueOf(changeNo)).set("status", status));
    }

    public void syncCurrentProcessor(ChangeTableNameEnum table, ChangeFieldEnum key, String value) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("approval", "");
        query.put(key.getName(), value);
        String approval = selectObject(ChangeTableNameEnum.CHANGE, param, query, String.class);
        String currentProcessor = "";
        if (approval != null && !"".equals(approval)) {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(approval);
            OgUser ogUser = ogUserService.selectOgUserByUserId(ogPerson.getpId());
            if (ogUser != null) {
                currentProcessor = ogPerson.getpName() + "(" + ogUser.getUsername() + ")";
            }
        }
        Map<String, Object> updateParam = new HashMap<>();
        updateParam.put("currentProcessor", currentProcessor);
        update(table, updateParam, query);
    }

    public String getChangeInstanceIdByChangeId(String changeId) {
        return getChangeColumnValueBySingleCondition(ChangeFieldEnum.INSTANCE_ID, ChangeFieldEnum.ID, changeId);
    }

    public String getChangeIdByTaskId(String taskId) {
        return getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.CHANGE_ID, ChangeTaskFieldEnum.ID, taskId);
    }

   /* public String getChangeColumnValueBySingleCondition(ChangeFieldEnum column, ChangeFieldEnum conditionKey, String conditionValue) {
        String columnName = column.getName();
        MybatisPlusConfig.customerTableName.set(CHANGE_TABLE_NAME);
        List<Map<String, Object>> taskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(columnName).eq(conditionKey.getName(), conditionValue));
        MybatisPlusConfig.customerTableName.remove();
        if (taskList.isEmpty() || taskList.get(0) == null) {
            return null;
        }
        Object value = taskList.get(0).get(columnName);
        if (value != null) {
            return value.toString();
        }
        return null;
    }*/

    public String getChangeColumnValueBySingleCondition(ChangeFieldEnum column, ChangeFieldEnum conditionKey, String conditionValue) {
        String columnName = column.getName();
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(columnName, "");
        query.put(conditionKey.getName(), conditionValue);
        return selectObject(ChangeTableNameEnum.CHANGE, param, query, String.class);
    }

   /* public String getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum column, ChangeTaskFieldEnum conditionKey, String conditionValue) {
        String columnName = column.getName();
        MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
        List<Map<String, Object>> taskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select(columnName).eq(conditionKey.getName(), conditionValue));
        MybatisPlusConfig.customerTableName.remove();
        if (taskList.isEmpty() || taskList.get(0) == null) {
            return null;
        }
        Object value = taskList.get(0).get(columnName);
        if (value != null) {
            return value.toString();
        }
        return null;
    }*/

    public String getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum column, ChangeTaskFieldEnum conditionKey, String conditionValue) {
        String columnName = column.getName();
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(columnName, "");
        query.put(conditionKey.getName(), conditionValue);
        return selectObject(ChangeTableNameEnum.CHANGE_TASK, param, query, String.class);
    }

 /*   public void updateChangeSingle(ChangeFieldEnum conditionKey, String conditionValue, ChangeFieldEnum updateKey, String updateValue) {
        MybatisPlusConfig.customerTableName.set(CHANGE_TABLE_NAME);
        customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq(conditionKey.getName(), conditionValue).set(updateKey.getName(), updateValue.trim()));
        MybatisPlusConfig.customerTableName.remove();
    }*/

    public void updateChangeSingle(ChangeFieldEnum conditionKey, String conditionValue, ChangeFieldEnum updateKey, String updateValue) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(updateKey.getName(), updateValue);
        query.put(conditionKey.getName(), conditionValue);
        update(ChangeTableNameEnum.CHANGE, param, query);
    }
/*
    public void updateChangeTaskSingle(ChangeTaskFieldEnum conditionKey, String conditionValue, ChangeTaskFieldEnum updateKey, String updateValue) {
        MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
        customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq(conditionKey.getName(), conditionValue).set(updateKey.getName(), updateValue.trim()));
        MybatisPlusConfig.customerTableName.remove();
    }*/

    public void updateChangeTaskSingle(ChangeTaskFieldEnum conditionKey, String conditionValue, ChangeTaskFieldEnum updateKey, String updateValue) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(updateKey.getName(), updateValue);
        query.put(conditionKey.getName(), conditionValue);
        update(ChangeTableNameEnum.CHANGE_TASK, param, query);
    }


    @Transactional(rollbackFor = Throwable.class)
    public void updateMergeTask(String changeTaskId) {
        List<Map<String, Object>> list = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.ID.getName(), changeTaskId);
        Map<String, Object> map = list.get(0);
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        query.put(ChangeTaskFieldEnum.MERGE_TASK_NO.getName(), map.get(ChangeTaskFieldEnum.EXTRA1.getName()).toString());
        param.put(ChangeTaskFieldEnum.IMPL_RESULT.getName(), map.get(ChangeTaskFieldEnum.IMPL_RESULT.getName()).toString());
        param.put(ChangeTaskFieldEnum.STATUS.getName(), ChangeTaskStatusEnum.closed.getName());
        param.put(ChangeTaskFieldEnum.TASK_STATUS.getName(), ChangeTaskStatusEnum.closed.getName());
        param.put(ChangeTaskFieldEnum.APPROVAL.getName(), "");
        param.put(ChangeTaskFieldEnum.CURRENT_PROCESSOR.getName(), "");
        update(ChangeTableNameEnum.CHANGE_TASK, param, query);
        Map<String, Object> instanceIdMap = new HashMap<>();
        instanceIdMap.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), "");
        List<String> instanceIdList = selectList(ChangeTableNameEnum.CHANGE_TASK, instanceIdMap, query, String.class);
        instanceIdList.forEach(p -> {
            //TODO 是否流程直接就结束了，待确认
            Task task = taskService.createTaskQuery().processInstanceId(p).singleResult();
            if (task != null) {
                endTask(task.getId());
            }
        });
    }

    @Autowired
    RepositoryService repositoryService;

    /**
     * 结束任务
     *
     * @param taskId 当前任务ID
     */
    public void endTask(String taskId) {
        //  当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();

        //  建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //  完成当前任务
        taskService.complete(task.getId());

        //  可以不用恢复原始方向，不影响其它的流程
//        currentFlowNode.setOutgoingFlows(originalSequenceFlowList);
    }


    /*public void updateChangeApproval(ChangeFieldEnum conditionKey, String conditionValue, String approval) {
        OgPerson ogPerson = ogPersonService.selectOgPersonById(approval);
        String currentProcessor = "";
        if (ogPerson != null) {
            currentProcessor = ogPerson.getpName();
        }
        MybatisPlusConfig.customerTableName.set(CHANGE_TABLE_NAME);
        customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq(conditionKey.getName(), conditionValue).
                set(ChangeFieldEnum.APPROVAL.getName(), approval).set(ChangeFieldEnum.CURRENT_PROCESSOR.getName(), currentProcessor));
        System.out.println(approval);
        MybatisPlusConfig.customerTableName.remove();
    }*/

    public void updateChangeApproval(ChangeFieldEnum conditionKey, String conditionValue, String approval) {
        OgPerson ogPerson = ogPersonService.selectOgPersonById(approval);
        String currentProcessor = "";
        if (ogPerson != null) {
            OgUser ogUser = ogUserService.selectOgUserByUserId(ogPerson.getpId());
            if (ogUser != null) {
                currentProcessor = ogPerson.getpName() + "(" + ogUser.getUsername() + ")";
            }
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ChangeFieldEnum.APPROVAL.getName(), approval);
        paramMap.put(ChangeFieldEnum.CURRENT_PROCESSOR.getName(), currentProcessor);
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(conditionKey.getName(), conditionValue);
        update(ChangeTableNameEnum.CHANGE, paramMap, queryMap);
    }

/*    public static void main(String[] args) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("status", "");
        paramMap.put("currentProcessor", "");
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("id", "");
        //queryMap.put("name","");
        String sql = createSql(ChangeTableNameEnum.CHANGE, SqlTypeEnum.SELECT, paramMap, queryMap);
        System.out.println(sql);
    }*/


    public void update(ChangeTableNameEnum table, Map<String, Object> param, Map<String, String> query) {
        String sql = createSql(table, SqlTypeEnum.UPDATE, param, query);
        param.putAll(query);
        jdbcTemplate.update(sql, param);
    }

    public void updateByTableName(String table, Map<String, Object> param, Map<String, String> query) {
        String sql = createSql(table, SqlTypeEnum.UPDATE, param, query);
        param.putAll(query);
        jdbcTemplate.update(sql, param);
    }

    public <E> E selectObject(ChangeTableNameEnum table, Map<String, Object> param, Map<String, String> query, Class<E> clazz) {
        String sql = createSql(table, SqlTypeEnum.SELECT, param, query);
        Integer count = getCount(table, query);
        if (count == 0) {
            return null;
        }
        return jdbcTemplate.queryForObject(sql, query, new SingleColumnRowMapper<>(clazz));
    }

    public <E> E selectObject(String sql, String table, Map<String, Object> param, Map<String, String> query, Class<E> clazz) {
        Integer count = getCount(table, query);
        if (count == 0) {
            return null;
        }
        return jdbcTemplate.queryForObject(sql, query, new SingleColumnRowMapper<>(clazz));
    }

    public Integer getCount(String table, Map<String, String> query) {
        String countSql = createCountSqlByTableName(table, query);
        return jdbcTemplate.queryForObject(countSql, query, Integer.class);
    }

    public Integer getCount(ChangeTableNameEnum table, Map<String, String> query) {
        String countSql = createCountSql(table, query);
        return jdbcTemplate.queryForObject(countSql, query, Integer.class);
    }


    public Integer getCountByTableName(String table, Map<String, String> query) {
        String countSql = createCountSqlByTableName(table, query);
        return jdbcTemplate.queryForObject(countSql, query, Integer.class);
    }

    public Map<String, Object> selectMap(ChangeTableNameEnum table, Map<String, Object> param, Map<String, String> query) {
        String sql = createSql(table, SqlTypeEnum.SELECT, param, query);
        Integer count = getCount(table, query);
        if (count == 0) {
            return new HashMap<>();
        }
        return jdbcTemplate.queryForMap(sql, query);
    }

    public Map<String, Object> selectMap(String table, Map<String, Object> param, Map<String, String> query) {
        String sql = createSql(table, SqlTypeEnum.SELECT, param, query);
        Integer count = getCount(table, query);
        if (count == 0) {
            return new HashMap<>();
        }
        return jdbcTemplate.queryForMap(sql, query);
    }

    public <E> List<E> selectList(ChangeTableNameEnum table, Map<String, Object> param, Map<String, String> query, Class<E> clazz) {
        String sql = createSql(table, SqlTypeEnum.SELECT, param, query);
        Integer count = getCount(table, query);
        if (count == 0) {
            return new ArrayList<>();
        }
        return jdbcTemplate.queryForList(sql, query, clazz);
    }

    public <E> List<E> selectNonCancelList(ChangeTableNameEnum table, Map<String, Object> param, Map<String, String> query, Class<E> clazz) {
        String sql = createSql(table, SqlTypeEnum.SELECT, param, query);
        sql = sql + " and status!=\"取消\"";
        Integer count = getNONCancelCount(table, query);
        if (count == 0) {
            return new ArrayList<>();
        }
        return jdbcTemplate.queryForList(sql, query, clazz);
    }

    public <E> List<E> selectListByTableName(String table, Map<String, Object> param, Map<String, String> query, Class<E> clazz) {
        String sql = createSql(table, SqlTypeEnum.SELECT, param, query);
        Integer count = getCountByTableName(table, query);
        if (count == 0) {
            return new ArrayList<>();
        }
        return jdbcTemplate.queryForList(sql, query, clazz);
    }

    public List<Map<String, Object>> selectListMap(ChangeTableNameEnum table, Map<String, Object> param, Map<String, String> query) {
        String sql = createSql(table, SqlTypeEnum.SELECT, param, query);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValues(query);
        Integer count = getCount(table, query);
        if (count == 0) {
            return new ArrayList<>();
        }
        return jdbcTemplate.queryForList(sql, mapSqlParameterSource);
    }

    public void remedyPass(String changeTaskInstanceId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.PRE_APPROVAL.getName(), "");
        param.put(ChangeTaskFieldEnum.ASSIGNEE.getName(), "");
        param.put(ChangeTaskFieldEnum.EXTRA1.getName(), "");
        query.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), changeTaskInstanceId);
        List<Map<String, Object>> list = selectListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        Map<String, Object> map = list.get(0);
        String rePreApproval = map.get(ChangeTaskFieldEnum.PRE_APPROVAL.getName()).toString();
        String reAssignee = map.get(ChangeTaskFieldEnum.ASSIGNEE.getName()).toString();
        String reChangeTaskNo = map.get(ChangeTaskFieldEnum.EXTRA1.getName()).toString();
        String changeTaskNo = reChangeTaskNo.substring(0, reChangeTaskNo.length() - 1);
        String impl = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.IMPL_MAN, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
        if (reAssignee.equals(rePreApproval) && rePreApproval.equals(impl)) {
            Task task = taskService.createTaskQuery().processInstanceId(changeTaskInstanceId).singleResult();
            Map<String, Object> variables = new HashMap<>();
            variables.put(RECODE, 1);
            taskService.complete(task.getId(), variables);
        }
    }

    public String createSql(ChangeTableNameEnum tableName, SqlTypeEnum type, Map<String, Object> paramMap, Map<String, String> queryMap) {
        return createSql(tableName.getName(), type, paramMap, queryMap);
    }

    public String createSql(String tableName, SqlTypeEnum type, Map<String, Object> paramMap, Map<String, String> queryMap) {
        StringBuilder sql = new StringBuilder("");
        StringJoiner param = new StringJoiner(",");
        StringJoiner query = new StringJoiner(" and ");
        if (type == SqlTypeEnum.SELECT) {
            sql.append("SELECT ");
            paramMap.forEach((key, value) -> {
                param.add(key);
            });
            sql.append(param.toString());
            sql.append(" FROM ").append(tableName).append(" WHERE ");
        } else if (type == SqlTypeEnum.UPDATE) {
            sql.append("UPDATE ").append(tableName).append(" SET ");
            paramMap.forEach((key, value) -> {
                param.add(key + "=:" + key);
            });
            sql.append(param.toString()).append(" WHERE ");
        } else if (type == SqlTypeEnum.INSERT) {
            sql.append("INSERT INTO ").append(tableName).append("(");
            StringJoiner valueStr = new StringJoiner(",");
            paramMap.forEach((key, value) -> {
                param.add(key);
                valueStr.add(":" + key);
            });
            sql.append(param.toString()).append(")").append(" VALUES(").append(valueStr.toString()).append(")");
            return sql.toString();
        } else if(type == SqlTypeEnum.DELETE){
            sql.append("DELETE  FROM ").append(tableName).append(" WHERE ");
        }
        queryMap.forEach((key, value) -> {
            query.add(key + "=:" + key);
        });
        sql.append(query.toString());
        return sql.toString();
    }

    public String createCountSql(ChangeTableNameEnum tableName, Map<String, String> queryMap) {
        return createCountSqlByTableName(tableName.getName(), queryMap);
    }

    public String createCountSqlByTableName(String tableName, Map<String, String> queryMap) {
        StringBuilder sql = new StringBuilder("");
        StringJoiner query = new StringJoiner(" and ");
        sql.append("SELECT count(*) as count");
        sql.append(" FROM ").append(tableName).append(" WHERE ");
        queryMap.forEach((key, value) -> {
            query.add(key + "=:" + key);
        });
        sql.append(query.toString());
        return sql.toString();
    }


    public KeyHolder insertARow(ChangeTableNameEnum table, Map<String, Object> param) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = createSql(table, SqlTypeEnum.INSERT, param, null);
        //TODO 如果map的value是list的话会被解析为多个值（猜测），所以要把list转换为字符串
        SqlParameterSource parameterSource = new MapSqlParameterSource(param);
        jdbcTemplate.update(sql, parameterSource, keyHolder, new String[]{"id"});
        return keyHolder;
    }

    public KeyHolder insertARowByTableName(String table, Map<String, Object> param) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = createSql(table, SqlTypeEnum.INSERT, param, null);
        SqlParameterSource parameterSource = new MapSqlParameterSource(param);
        jdbcTemplate.update(sql, parameterSource, keyHolder, new String[]{"id"});
        return keyHolder;
    }

    public void batchInsert(String table,Map<String,Object> fields,List<Map<String,Object>> paramList){
        String sql = createSql(table, SqlTypeEnum.INSERT, fields, null);
        jdbcTemplate.batchUpdate(sql,SqlParameterSourceUtils.createBatch(paramList));
    }

    public void delByCondition(String table,Map<String,String> params){
        String sql = createSql(table,SqlTypeEnum.DELETE,null,params);
        jdbcTemplate.update(sql,params);
    }



    /*public KeyHolder inserBatchByTableName(String table, Map<String, Object> param) {
        String sql = createSql(table, SqlTypeEnum.INSERT, param, null);
        jdbcTemplate.batchUpdate(sql)
    }*/

    @Transactional(rollbackFor = Throwable.class)
    public Map<String, Object> createRemedy(Map<String, Object> old, String instanceId) {
        List<Map<String, Object>> list = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.INSTANCE_ID.getName(), instanceId);
        Map<String, Object> map = list.get(0);
        map.putAll(old);
        //String changeTaskId = map.get(ChangeTaskFieldEnum.ID.getName()).toString();
        String changeTaskNo = map.get(ChangeTaskFieldEnum.CHANGE_TASK_NO.getName()).toString();
        /*map.put(ChangeTaskFieldEnum.ID.getName(), null);
        map.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), null);*/
        map.remove(ChangeTaskFieldEnum.ID.getName());
        map.remove(ChangeTaskFieldEnum.INSTANCE_ID.getName());
        List<String> reason = (List<String>) map.get(ChangeTaskFieldEnum.REMEDY_REASON.getName());
        List<String> fundamental = (List<String>) map.get(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName());
        fundamental = fundamental.stream().filter(p -> !"".equals(p)).map(p -> "\"" + p + "\"").collect(Collectors.toList());
        reason = reason.stream().filter(p -> !"".equals(p)).map(p -> "\"" + p + "\"").collect(Collectors.toList());
        String reasonStr = reason.toString();
        String fundamentalStr = fundamental.toString();
        map.put(ChangeTaskFieldEnum.REMEDY_REASON.getName(), reasonStr);
        map.put(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName(), fundamentalStr);
        //反显的时候去掉_
        map.put(ChangeTaskFieldEnum.STATUS.getName(), "复核中_");
        map.put(ChangeTaskFieldEnum.TASK_STATUS.getName(), "复核中_");
        map.put(ChangeTaskFieldEnum.IMPL_RESULT.getName(), "3");
        map.put(ChangeTaskFieldEnum.CURRENT_PROCESSOR.getName(), "");
        KeyHolder keyHolder = insertARow(ChangeTableNameEnum.CHANGE_TASK, map);
        long bizId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        //添加一个jsondata
        addTaskJson(map, bizId);
        /*DesignBizJsonData designBizJsonData = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, changeTaskId).eq(DesignBizJsonData.COL_BIZ_TABLE, ChangeTableNameEnum.CHANGE_TASK.getName()));
        String jsonData = designBizJsonData.getJsonData();
        *//*Object file = map.get(ChangeTaskFieldEnum.UPLOAD_FILE.getName());
        if (file != null) {
            Object o = JSON.parse(file.toString());
            map.put(ChangeTaskFieldEnum.UPLOAD_FILE.getName(), o);
        }*//*
        Object infrastructure = map.get(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName());
        if (infrastructure != null && !"".equals(infrastructure.toString())) {
            String infrastructureStr = infrastructure.toString();
            infrastructureStr = infrastructureStr.replaceAll("\"", "").replaceAll(" ","");
            infrastructureStr = StringUtils.strip(infrastructureStr, "[]");
            String[] infrastructureArray = infrastructureStr.split(",");
            map.put(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName(), infrastructureArray);
        } else {
            map.put(ChangeTaskFieldEnum.REFER_INFRASTRUCTURE.getName(), new String[]{});
        }
        reasonStr = reasonStr.replaceAll("\"", "").replaceAll(" ","");
        reasonStr = StringUtils.strip(reasonStr, "[]");
        String[] remedyReasonArray = reasonStr.split(",");
        map.put(ChangeTaskFieldEnum.REMEDY_REASON.getName(), remedyReasonArray);
        jsonData = VueDataJsonUtil.analysisDataJson(jsonData, map);
        designBizJsonData.setJsonData(jsonData);
        designBizJsonData.setBizId(bizId);
        designBizJsonData.setId(null);
        designBizJsonDataService.save(designBizJsonData);*/
        //把相关日期都置空
        Map<String, Object> map1 = new HashMap<>();
        String remedyNo = changeTaskNo + "R";
        map1.put(ChangeTaskFieldEnum.EXTRA1.getName(), remedyNo);
        map1.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), 1);
        map1.put(ChangeTaskFieldEnum.IMPL_MAN.getName(), "");
        map1.put(ChangeTaskFieldEnum.APPROVAL.getName(), "");
        OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
        String userId = ogUser.getpId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(userId);
        map1.put("created_time", new Date());
        map1.put("created_by", userId);
        map1.put("created_by_name", ogPerson.getpName() + "(" + ogUser.getUsername() + ")");
        map1.put("updated_time", new Date());
        map1.put("updated_by", userId);
        /*map1.put(ChangeTaskFieldEnum.IMPL_RECEIVE_DATE.getName(), "");
        map1.put(ChangeTaskFieldEnum.IMPL_START_DATE.getName(), "");
        map1.put(ChangeTaskFieldEnum.IMPL_END_DATE.getName(), "");
        map1.put(ChangeTaskFieldEnum.IMPL_OVER_DATE.getName(), "");
        map1.put(ChangeTaskFieldEnum.IMPL_RESULT.getName(), "");
        map1.put("autoTaskFlag", "");
        map1.put("scheduleDate", "");
        map1.put("scheduleMan", "");
        String pId = CustomerBizInterceptor.currentUserThread.get().getpId();
        map1.put(ChangeTaskFieldEnum.ASSIGNEE.getName(), pId);
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        map1.put(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName(),ogPerson.getOrgId());
        map1.put(ChangeTaskFieldEnum.CHANGE_TASK_NO.getName(),remedyNo);*/
        Map<String, String> query = new HashMap<>();
        query.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), instanceId);
        update(ChangeTableNameEnum.CHANGE_TASK, map1, query);
        //新建补救单的时候更新
        Map<String, Object> result = new HashMap<>();
        result.put(ChangeTaskFieldEnum.IMPL_RECEIVE_DATE.getName(), "");
        result.put(ChangeTaskFieldEnum.IMPL_START_DATE.getName(), "");
        result.put(ChangeTaskFieldEnum.IMPL_END_DATE.getName(), "");
        result.put(ChangeTaskFieldEnum.IMPL_OVER_DATE.getName(), "");
        result.put(ChangeTaskFieldEnum.IMPL_RESULT.getName(), "");
        result.put(ChangeTaskFieldEnum.AUTO_TASK_FLAG.getName(), "");
        result.put(ChangeTaskFieldEnum.SCHEDULE_DATE.getName(), "");
        result.put(ChangeTaskFieldEnum.SCHEDULE_MAN.getName(), "");
        result.put(ChangeTaskFieldEnum.CHECK_MAN.getName(), "");
        result.put(ChangeTaskFieldEnum.PLAN_START_DATE.getName(), "");
        result.put(ChangeTaskFieldEnum.PLAN_OVER_DATE.getName(), "");
        result.put(ChangeTaskFieldEnum.PLAN_DES.getName(), "");
        //TODO 加上堡垒机的
        result.put(ChangeTaskFieldEnum.PRE_APPROVAL.getName(), userId);
        result.put(ChangeTaskFieldEnum.ASSIGNEE.getName(), userId);
        result.put(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName(), ogPerson.getOrgId());
        result.put(ChangeTaskFieldEnum.CHANGE_TASK_NO.getName(), remedyNo);
        result.put(ChangeTaskFieldEnum.REMEDY_REASON.getName(), "");
        //TODO 插入一条原场景表单数据
        return result;
    }

/*    public void updateChangeTaskApproval(ChangeTaskFieldEnum conditionKey, String conditionValue, String approval) {
        OgPerson ogPerson = ogPersonService.selectOgPersonById(approval);
        String currentProcessor = "";
        if (ogPerson != null) {
            currentProcessor = ogPerson.getpName();
        }
        MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
        customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq(conditionKey.getName(), conditionValue).
                set(ChangeTaskFieldEnum.APPROVAL.getName(), approval).set(ChangeTaskFieldEnum.CURRENT_PROCESSOR.getName(), currentProcessor));
        MybatisPlusConfig.customerTableName.remove();
    }*/

    public void updateChangeTaskApproval(ChangeTaskFieldEnum conditionKey, String conditionValue, String approval) {
        OgPerson ogPerson = ogPersonService.selectOgPersonById(approval);
        String currentProcessor = "";
        if (ogPerson != null) {
            OgUser ogUser = ogUserService.selectOgUserByUserId(ogPerson.getpId());
            if (ogUser != null) {
                currentProcessor = ogPerson.getpName() + "(" + ogUser.getUsername() + ")";
            }
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ChangeFieldEnum.APPROVAL.getName(), approval);
        paramMap.put(ChangeFieldEnum.CURRENT_PROCESSOR.getName(), currentProcessor);
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(conditionKey.getName(), conditionValue);
        update(ChangeTableNameEnum.CHANGE_TASK, paramMap, queryMap);
    }


 /*   public Integer getTaskCountByChangeId(String changeId) {
        MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
        List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("count(*)").eq(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId));
        System.out.println(Arrays.toString(list.toArray()));
        MybatisPlusConfig.customerTableName.remove();
        return Integer.valueOf(list.get(0).get("count(*)").toString());
    }*/


    public Integer getTaskCountByChangeId(String changeId) {
        //Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        //param.put("count(*)", "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        return getNONCancelCount(ChangeTableNameEnum.CHANGE_TASK, query);
        //return selectObject(ChangeTableNameEnum.CHANGE_TASK, param, query, Integer.class);
    }

    public Integer getNONCancelCount(ChangeTableNameEnum table, Map<String, String> query) {
        String countSql = createCountSql(table, query);
        countSql = countSql + " and status!=\"取消\"";
        return jdbcTemplate.queryForObject(countSql, query, Integer.class);
    }

   /* public Boolean managerApproval(String changeId) {
        //先判断变更单是不是普通变更
        MybatisPlusConfig.customerTableName.set(CHANGE_TABLE_NAME);
        List<Map<String, Object>> changeData = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq(ChangeFieldEnum.ID.getName(), changeId));
        Map<String, Object> map = changeData.get(0);
        int type = Integer.parseInt(map.get(ChangeFieldEnum.TYPE.getName()).toString());
        String startDept = map.get(ChangeFieldEnum.START_DEPT.getName()).toString();
        MybatisPlusConfig.customerTableName.remove();
        //如果不是普通变更，那都要走变更经理
        if (type == 1) {
            //查一下是不是生产管理部
            if (ChangeTaskDeptEnum.production.getName().equals(startDept)) {
                return false;
            }
            MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
            List<Map<String, Object>> taskList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").
                    and(i -> i.eq(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId).
                            eq(ChangeTaskFieldEnum.STATUS.getName(), ChangeTaskStatusEnum.preApprovalPassed.getName()).
                            eq(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), 0)));
            MybatisPlusConfig.customerTableName.remove();
            ChangeTaskDeptEnum[] changeTaskDeptEnumArray =
                    {ChangeTaskDeptEnum.system, ChangeTaskDeptEnum.netWork, ChangeTaskDeptEnum.baseManager, ChangeTaskDeptEnum.operation};
            boolean flag = taskList.stream().anyMatch(p -> {
                String taskDept = p.get(ChangeTaskFieldEnum.TASKDEPT.getName()).toString();
                OgOrg org = sysDeptService.selectDeptById(taskDept);
                taskDept = org.getOrgName();
                return !startDept.equals(taskDept) && (ChangeTaskDeptEnum.match(taskDept, changeTaskDeptEnumArray));
            });
            if (flag) {
                return false;
            }
            ChangeTaskDeptEnum[] changeTaskDeptEnum =
                    {ChangeTaskDeptEnum.appSupport, ChangeTaskDeptEnum.system, ChangeTaskDeptEnum.netWork,
                            ChangeTaskDeptEnum.baseManager, ChangeTaskDeptEnum.operation};
            if (ChangeTaskDeptEnum.match(startDept, changeTaskDeptEnum)) {
                return taskList.stream().anyMatch(p -> {
                    String assignee = p.get(ChangeTaskFieldEnum.ASSIGNEE.getName()).toString();
                    OgUser ogUser = ogUserService.selectOgUserByUserId(assignee);
                    return !startDept.equals(ogUser.getOrgname());
                });
            }
        }
        return true;
    }*/

    public Integer getPreRecode(String changeId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeFieldEnum.PRE_RECODE.getName(), "");
        query.put(ChangeTaskFieldEnum.ID.getName(), changeId);
        return selectObject(ChangeTableNameEnum.CHANGE, param, query, Integer.class);
    }

    public Boolean managerApproval(String changeId) {
        //先判断变更单是不是普通变更
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put("*", "");
        query.put(ChangeTaskFieldEnum.ID.getName(), changeId);
        Map<String, Object> map = selectMap(ChangeTableNameEnum.CHANGE, param, query);
        int type = Integer.parseInt(map.get(ChangeFieldEnum.TYPE.getName()).toString());
        Object startDeptObj = map.get(ChangeFieldEnum.EXTRA2.getName());
        String startDeptId = "";
        if (startDeptObj != null) {
            startDeptId = startDeptObj.toString();
        }
        String startDept = map.get(ChangeFieldEnum.START_DEPT.getName()).toString();
        OgOrg ogOrg = sysDeptService.selectDeptById(startDeptId);
        if (ogOrg != null) {
            startDept = ogOrg.getOrgName();
        }
        //如果不是普通变更，那都要走变更经理
        if (type == 1) {
            //查一下是不是生产管理部
            if (ChangeTaskDeptEnum.production.getName().equals(startDept)) {
                return false;
            }
            Map<String, Object> param2 = new HashMap<>();
            Map<String, String> query2 = new HashMap<>();
            param2.put("*", "");
            query2.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
//            query2.put(ChangeTaskFieldEnum.STATUS.getName(), ChangeTaskStatusEnum.preApprovalPassed.getName());
            query2.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), "0");
            List<Map<String, Object>> taskList = selectListMap(ChangeTableNameEnum.CHANGE_TASK, param2, query2);
            taskList = taskList.stream().filter(p->!ChangeTaskStatusEnum.canceled.getName().equals(p.get(ChangeTaskFieldEnum.STATUS.getName()))).collect(Collectors.toList());
            //如果全部任务都是应用支持部的，并且变更发起科室是应用支持部，也不需要变更经理审批
            boolean allFlag = taskList.stream().map(f -> {
                String taskDeptId = f.get(ChangeTaskFieldEnum.TASKDEPT.getName()).toString();
                OgOrg org = sysDeptService.selectDeptById(taskDeptId);
                return org.getOrgName();
            }).allMatch(p -> ChangeTaskDeptEnum.appSupport.getName().equals(p));
            if (allFlag && ChangeTaskDeptEnum.appSupport.getName().equals(startDept)) {
                return false;
            }
            String[] changeTaskDeptEnumArray =
                    {ChangeTaskDeptEnum.monitor.getName(),
                            ChangeTaskDeptEnum.system.getName(),
                            ChangeTaskDeptEnum.netWork.getName(),
                            ChangeTaskDeptEnum.baseManager.getName(),
                            ChangeTaskDeptEnum.operation.getName()
                    };
            List<String> changeTaskDeptEnumList = Arrays.asList(changeTaskDeptEnumArray);
            return !taskList.stream().map(p -> {
                String taskDept = p.get(ChangeTaskFieldEnum.TASKDEPT.getName()).toString();
                OgOrg org = sysDeptService.selectDeptById(taskDept);
                return org.getOrgName();
            }).allMatch(changeTaskDeptEnumList::contains);
        }
        return true;
    }

    public AjaxResult checkMergeNo(Integer mergeFlag, Object mergeTaskNo, String changeTaskNo) {
        if (1 == mergeFlag) {
            if (mergeTaskNo == null || "".equals(mergeTaskNo.toString().trim())) {
                return AjaxResult.warn("请输入并包任务单号");
            }
            String status = getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.STATUS, ChangeTaskFieldEnum.EXTRA1, mergeTaskNo.toString());
            if (mergeTaskNo.equals(changeTaskNo)) {
                return AjaxResult.warn("不允许输入本任务单号");
            } else if (ChangeTaskStatusEnum.canceled.getName().equals(status) || ChangeTaskStatusEnum.closed.getName().equals(status)) {
                return AjaxResult.warn("不允许输入取消和已关闭的任务单号");
            } else {
                //判断并包的任务单号是否是非并包的
                Map<String, Object> params = new HashMap<>();
                Map<String, String> query = new HashMap<>();
                params.put(ChangeTaskFieldEnum.MERGE_FLAG.getName(), "");
                query.put(ChangeTaskFieldEnum.EXTRA1.getName(), mergeTaskNo.toString().trim());
                Integer flag = selectObject(ChangeTableNameEnum.CHANGE_TASK, params, query, Integer.class);
                if (flag == null || flag != 0) {
                    return AjaxResult.warn("并包任务单号不符合要求，请输入真正执行的并包单号");
                }
            }
        }
        return AjaxResult.success();
    }

 /*   public Boolean generalMangerApproval(String changeId) {
        //目前是，只有风险等级为重大才需要
        MybatisPlusConfig.customerTableName.set(CHANGE_TABLE_NAME);
        List<Object> taskList = customerFormMapper.selectObjs(Wrappers.<CustomerFormContent>query().
                select(ChangeFieldEnum.CURRENT_RISK_LEVEL.getName()).eq(ChangeFieldEnum.ID.getName(), changeId));
        int currentRiskLevel = Integer.parseInt(taskList.get(0).toString());
        MybatisPlusConfig.customerTableName.remove();
        if (currentRiskLevel != 4) {
            return false;
        }
        return true;
    }*/

    public Boolean generalMangerApproval(String changeId) {
        //目前是，只有风险等级为重大才需要
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeFieldEnum.CURRENT_RISK_LEVEL.getName(), "");
        query.put(ChangeFieldEnum.ID.getName(), changeId);
        Integer currentRiskLevel = selectObject(ChangeTableNameEnum.CHANGE, param, query, Integer.class);
        if (currentRiskLevel != 4) {
            return false;
        }
        return true;
    }

    public void approvalChangeSecPartByInstanceId(String instanceId) {
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).taskCandidateOrAssigned(ChangeAutoAssigneeEnum.CHANGE_1.getName()).singleResult();
        if (task == null) {
            return;
        }
        taskService.complete(task.getId());
    }

    public void activeChangeSecPartByInstanceId(String instanceId) {
        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(instanceId)
                .activityId(ChangeDefineKeyEnum.waitTaskPreOver.getName())
                .singleResult();
        if (execution != null) {
            String executionId = execution.getId();
            runtimeService.trigger(executionId);
        }
    }

    /*public List<String> getAdminUserList(String changeId) {
        //判断行政审批是否需要领导审批（这里需要给下一节点输出参与并行审批的所有人员）
        System.out.println("//判断行政审批是否需要领导审批（这里需要给下一节点输出参与并行审批的所有人员）");
        //获取发起部门领导和所有任务单受派者所在部门的领导
        //查一下所有任务单的单位负责人
        List<String> approvalList = new ArrayList<>();
        MybatisPlusConfig.customerTableName.set(CHANGE_TASK_TABLE_NAME);
        List<Object> taskList = customerFormMapper.selectObjs(Wrappers.<CustomerFormContent>query().select(ChangeTaskFieldEnum.ASSIGNEE.getName()).
                and(i -> i.eq(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId).
                        eq(ChangeTaskFieldEnum.STATUS.getName(), ChangeTaskStatusEnum.preApprovalPassed.getName()).
                        eq(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), 0)));
        taskList.forEach(p -> {
            String assignee = p.toString();
            OgUser ogUser = ogUserService.selectOgUserByUserId(assignee);
            String leader = changeService.getOrgLeaderByOrgId(ogUser.getOrgId());
            approvalList.add(leader);
        });
        //查变更单是否是项目上线、紧急变更、风险级别为高以上、系统下线，是的话，查一下科室领导人
        //获取所有需要审批的分管领导

        MybatisPlusConfig.customerTableName.remove();
        MybatisPlusConfig.customerTableName.set(CHANGE_TABLE_NAME);
        List<Object> list = customerFormMapper.selectObjs(Wrappers.<CustomerFormContent>query().select(ChangeFieldEnum.CREATOR.getName()).eq(ChangeFieldEnum.ID.getName(), changeId));
        OgUser ogUser = ogUserService.selectOgUserByUserId(list.get(0).toString());
        String changeLeader = changeService.getOrgLeaderByOrgId(ogUser.getOrgId());
        MybatisPlusConfig.customerTableName.remove();
        approvalList.add(changeLeader);
        //去重
        return approvalList.stream().distinct().collect(Collectors.toList());
    }*/

    public List<String> getHeadListByOrgList(List<String> orgList, Boolean leaderFlag) {
        List<String> result = new ArrayList<>();
        if (leaderFlag) {
            orgList.forEach(orgId -> {
                ChangeDeptPersonVo changeDeptPersonVo = changePersonService.selectOneId(orgId);
                String person = changeDeptPersonVo.getDeptPersonId();
                String leader = changeDeptPersonVo.getDeptLeaderPersonId();
                result.add(person);
                result.add(leader);
            });
        } else {
            orgList.forEach(orgId -> {
                ChangeDeptPersonVo changeDeptPersonVo = changePersonService.selectOneId(orgId);
                String person = changeDeptPersonVo.getDeptPersonId();
                result.add(person);
            });
        }
        return result.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getAdminUserList(String changeId) {
        //判断行政审批是否需要领导审批（这里需要给下一节点输出参与并行审批的所有人员）
        //获取发起部门领导和所有任务单实施人所在部门的领导
        //查一下所有任务单的单位负责人
        List<String> approvalList = new ArrayList<>();
        boolean flag = false;
        //如果是项目上线，系统下线，风险级别高及以上，紧急变更,则需要分管领导
        List<Map<String, Object>> list = getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE, ChangeFieldEnum.ID.getName(), changeId);
        Map<String, Object> result = list.get(0);
        String type = result.get("type").toString();
        String reason = result.get("reason").toString();
        Integer level = Integer.parseInt(result.get("currentRiskLevel").toString());
        if ("2".equals(type) || "5".equals(reason) || "6".equals(reason) || level >= 3) {
            flag = true;
        }
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.TASKDEPT.getName(), "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        query.put(ChangeTaskFieldEnum.STATUS.getName(), ChangeTaskStatusEnum.preApprovalPassed.getName());
        query.put(ChangeTaskFieldEnum.REMEDY_FLAG.getName(), "0");
        List<String> taskList = selectList(ChangeTableNameEnum.CHANGE_TASK, param, query, String.class);
        boolean finalFlag = flag;
        taskList.forEach(p -> {
            ChangeDeptPersonVo changeDeptPersonVo = changePersonService.selectOneId(p);
            String person = changeDeptPersonVo.getDeptPersonId();
            approvalList.add(person);
            if (finalFlag) {
                String leader = changeDeptPersonVo.getDeptLeaderPersonId();
                approvalList.add(leader);
            }
        });
        Map<String, Object> param2 = new HashMap<>();
        Map<String, String> query2 = new HashMap<>();
        param2.put(ChangeFieldEnum.CREATOR.getName(), "");
        query2.put(ChangeFieldEnum.ID.getName(), changeId);
        String creator = selectObject(ChangeTableNameEnum.CHANGE, param2, query2, String.class);
        OgPerson ogPerson = ogPersonService.selectOgPersonById(creator);
        String orgId = ogPerson.getOrgId();
        ChangeDeptPersonVo changeDeptPerson = changePersonService.selectOneId(orgId);
        if (changeDeptPerson != null) {
            String changePerson = changeDeptPerson.getDeptPersonId();
            approvalList.add(changePerson);
            if (flag) {
                String changeLeader = changeDeptPerson.getDeptLeaderPersonId();
                approvalList.add(changeLeader);
            }
        }
        //去重
        return approvalList.stream().distinct().collect(Collectors.toList());
    }

    //
    public void delOneOverSize(String businessKey) {
        String type = getChangeColumnValueBySingleCondition(ChangeFieldEnum.TYPE, ChangeFieldEnum.ID, businessKey);
        String orgId = getChangeTableStarterOrgId(businessKey);
        if (changeDeptService.isDeptAndSystem(orgId) && "2".equals(type)) {
            changeDeptService.updateOverSizerOne(orgId, "1");
        }
    }

    public Integer tableExist(String table) {
        Map<String, Object> param = new HashMap<>();
        param.put("count(*) as count", "");
        Map<String, String> query = new HashMap<>();
        query.put("table_name", table);
        String countSql = createSql("information_schema.TABLES", SqlTypeEnum.SELECT, param, query);
        return jdbcTemplate.queryForObject(countSql, query, Integer.class);
        /* String sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name =";*/
    }

    public List<Map<String, Object>> getTableFieldMap(String table) {
        Map<String, Object> param = new HashMap<>();
        param.put("column_name", "");
        param.put("data_type", "");
        param.put("character_maximum_length", "");
        Map<String, String> query = new HashMap<>();
        query.put("table_name", table);
        String countSql = createSql("information_schema.COLUMNS", SqlTypeEnum.SELECT, param, query);
        return jdbcTemplate.queryForList(countSql, query);
        /* String sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name =";*/
    }

    public String getTaskAssigneeGroupLeaderUserId(String taskId) {
        String orgId = getChangeTaskUserIdByColumnAndId(ChangeTaskFieldEnum.ASSIGNEE_GROUP, taskId);
        ChangeDeptPersonVo changeDeptPersonVo = changePersonService.selectOneId(orgId);
        if (changeDeptPersonVo == null) {
            return null;
        } else {
            String leader = changeDeptPersonVo.getDeptPersonId();
            if (leader == null || "".equals(leader.trim())) {
                return null;
            }
            return leader;
        }
    }

    public void addOneOverSize(String businessKey) {
        String type = getChangeColumnValueBySingleCondition(ChangeFieldEnum.TYPE, ChangeFieldEnum.ID, businessKey);
        String orgId = getChangeTableStarterOrgId(businessKey);
        if (changeDeptService.isDeptAndSystem(orgId) && "2".equals(type)) {
            changeDeptService.addOverSizerOne(orgId);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void clearChangeRisk(String changeId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeFieldEnum.CURRENT_RISK_LEVEL.getName(), "");
        param.put(ChangeFieldEnum.RISK_LEVEL.getName(), "");
        param.put(ChangeFieldEnum.REFER_SYS.getName(), "");
        query.put(ChangeFieldEnum.ID.getName(), changeId);
        update(ChangeTableNameEnum.CHANGE, param, query);
    }

    public void setClosedCodeAndStatus(String changeId) {
        Map<String, Object> params = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        params.put(ChangeFieldEnum.STATUS.getName(), ChangeStatusEnum.closed.getName());
        params.put(ChangeFieldEnum.CHANGE_STATUS.getName(), ChangeStatusEnum.closed.getName());
        String closedCode = getClosedCode(changeId);
        params.put(ChangeFieldEnum.CLOSE_CODE.getName(), closedCode);
        query.put(ChangeFieldEnum.ID.getName(), changeId);
        update(ChangeTableNameEnum.CHANGE, params, query);
    }

    /*[任务单
  {
    "label": "成功",
    "value": "1"
  },
  {
    "label": "失败",
    "value": "2"
  },
  {
    "label": "缺陷",
    "value": "3"
  },
  {
    "label": "取消",
    "value": "4"
  }
]*/
    public String getClosedCode(String changeId) {
        //获取所有任务单的实施结果
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.IMPL_RESULT.getName(), "");
        param.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        List<Map<String, Object>> listResult = selectListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        List<Integer> list = new ArrayList<>();
        listResult.forEach(p -> {
            Object instanceId = p.get(ChangeTaskFieldEnum.INSTANCE_ID.getName());
            Object implResult = p.get(ChangeTaskFieldEnum.IMPL_RESULT.getName());
            if ((instanceId != null && !"".equals(instanceId.toString().trim())) && (implResult != null && !"".equals(implResult.toString().trim()))) {
                list.add(Integer.parseInt(implResult.toString()));
            }
        });

        long allCount = list.size();
        long successCount = list.stream().filter(p ->
                p == 1
        ).count();
        long failCount = list.stream().filter(p ->
                p == 2
        ).count();
        long cancelCount = list.stream().filter(p ->
                p == 4
        ).count();
        String code = "缺陷";
        if (allCount == successCount) {
            code = "成功";
        } else if (allCount == failCount) {
            code = "失败";
        } else if (allCount == cancelCount) {
            code = "取消";
        }
        return code;
    }

    public List<Map<String, Object>> getChangeTotalCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String changeTotalSql = getTotalMatchSql(0, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getDate(date);
        return jdbcTemplate.queryForList(changeTotalSql, mapSqlParameterSource);
    }


    public List<Map<String, Object>> getTaskTotalCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String taskTotalSql = "select a.referSys as referSys,count(*) as total from design_biz_changeTask a LEFT JOIN design_biz_change b ON a.changeId=b.id " +
                " where (a.status=\"复核中\" or a.status=\"复核中_\" or a.status=\"已关闭\") and a.remedyFlag=0 and b.created_time like concat(:date,\"%\")   and a.referSys is not null and a.referSys!='' and implResultCheck!=4" +
                " group by a.referSys " +
                " limit " + passed + "," + pageSize;
        MapSqlParameterSource mapSqlParameterSource = getDate(date);
        return jdbcTemplate.queryForList(taskTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getTaskMonthTotalCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String changeTotalSql = "select a.referSys as referSys,count(*) as total from design_biz_changeTask a LEFT JOIN design_biz_change b ON a.changeId=b.id " +
                " where (a.status=\"复核中\" or a.status=\"复核中_\" or a.status=\"已关闭\") and a.remedyFlag=0 and b.created_time >= :startDate and b.created_time <= :endDate  and a.referSys is not null and a.referSys!='' and implResultCheck!=4" +
                " group by a.referSys" +
                " limit " + passed + "," + pageSize;
        MapSqlParameterSource mapSqlParameterSource = getStartDateAndEndDate(date);
        return jdbcTemplate.queryForList(changeTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getChangeMonthTotalCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String changeTotalSql = getTotalSql(0, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getStartDateAndEndDate(date);
        return jdbcTemplate.queryForList(changeTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getMergeChangeCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String mergeChangeTotalSql = getTotalMatchSql(2, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getDate(date);
        return jdbcTemplate.queryForList(mergeChangeTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getDefectChangeCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String defectChangeTotalSql = getTotalMatchSql(4, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getDate(date);
        return jdbcTemplate.queryForList(defectChangeTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getDefectTaskCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String defectTaskTotalSql = getTotalMatchSql(5, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getDate(date);
        return jdbcTemplate.queryForList(defectTaskTotalSql, mapSqlParameterSource);
    }


    public List<Map<String, Object>> getMergeChangeMonthCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String mergeChangeTotalSql = getTotalSql(2, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getStartDateAndEndDate(date);
        return jdbcTemplate.queryForList(mergeChangeTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getDefectChangeMonthCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String defectChangeTotalSql = getTotalSql(4, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getStartDateAndEndDate(date);
        return jdbcTemplate.queryForList(defectChangeTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getDefectTaskMonthCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String defectTaskTotalSql = getTotalSql(5, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getStartDateAndEndDate(date);
        return jdbcTemplate.queryForList(defectTaskTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getSuccessChangeCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String successChangeTotalSql = getTotalMatchSql(1, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getDate(date);
        return jdbcTemplate.queryForList(successChangeTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getSuccessMonthChangeCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String successChangeTotalSql = getTotalSql(1, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getStartDateAndEndDate(date);
        return jdbcTemplate.queryForList(successChangeTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getEventChangeCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String eventChangeTotalSql = getTotalMatchSql(3, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getDate(date);
        return jdbcTemplate.queryForList(eventChangeTotalSql, mapSqlParameterSource);
    }

    public List<Map<String, Object>> getEventChangeMonthCount(String date, Integer pageNum, Integer pageSize) {
        Integer passed = (pageNum - 1) * pageSize;
        String eventChangeTotalSql = getTotalSql(3, passed, pageSize);
        MapSqlParameterSource mapSqlParameterSource = getStartDateAndEndDate(date);
        return jdbcTemplate.queryForList(eventChangeTotalSql, mapSqlParameterSource);
    }

    public List<String> getChangeAllTotalTypeId(String date) {
        List<String> result = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = getStartDateAndEndDate(date);
        String eventChangeTotalSql = getTotalRecordSql(3);
        List<String> eventList = jdbcTemplate.queryForList(eventChangeTotalSql, mapSqlParameterSource, String.class);
        String mergeChangeTotalSql = getTotalRecordSql(2);
        List<String> mergeList = jdbcTemplate.queryForList(mergeChangeTotalSql, mapSqlParameterSource, String.class);
        String successChangeTotalSql = getTotalRecordSql(1);
        List<String> successList = jdbcTemplate.queryForList(successChangeTotalSql, mapSqlParameterSource, String.class);
        String defectChangeTotalSql = getTotalRecordSql(4);
        List<String> defectList = jdbcTemplate.queryForList(defectChangeTotalSql, mapSqlParameterSource, String.class);
        result.addAll(eventList);
        result.addAll(mergeList);
        result.addAll(successList);
        result.addAll(defectList);
        return result;
    }

    public List<String> getChangeAllTotalId(String date) {
        MapSqlParameterSource mapSqlParameterSource = getStartDateAndEndDate(date);
        String eventChangeTotalSql = getTotalRecordSql(0);
        return jdbcTemplate.queryForList(eventChangeTotalSql, mapSqlParameterSource, String.class);
    }

    public List<String> getChangeTaskAllTotalId(String date) {
        MapSqlParameterSource mapSqlParameterSource = getStartDateAndEndDate(date);
        String defectChangeTaskTotalSql = getTotalRecordSql(5);
        return jdbcTemplate.queryForList(defectChangeTaskTotalSql, mapSqlParameterSource, String.class);
    }

    public String getTotalRecordSql(int type) {
        String condition = "";
        if (type == 1) {
            condition = "and `closedCode`!=\"失败\"";
        } else if (type == 2) {
            condition = "and `type`=\"2\" ";
        } else if (type == 3) {
            condition = "and `basis` like concat(\"%\",\"IM\",\"%\") ";
        } else if (type == 4) {
            condition = "and `closedCode`=\"缺陷\"";
        } else if (type == 5) {
            return "select a.id as id FROM design_biz_changeTask a LEFT JOIN design_biz_change b ON a.changeId=b.id " +
                    " where (a.status=\"复核中\" or a.status=\"复核中_\" or a.status=\"已关闭\") and a.remedyFlag=0 and b.created_time >= :startDate and b.created_time <= :endDate  and a.referSys is not null and a.referSys!='' and implResultCheck!=4";
        }
        return "select id  from design_biz_change " +
                "where (status=\"变更实施中\" or status=\"已完成\" or status=\"已关闭\")  and created_time >= :startDate and created_time <= :endDate and `closedCode`!=\"取消\"" +
                condition;
    }

    public String getTotalSql(int type, Integer passed, Integer pageSize) {
        String condition = "";
        if (type == 1) {
            condition = "and `closedCode`!=\"失败\"";
        } else if (type == 2) {
            condition = "and `type`=\"2\" ";
        } else if (type == 3) {
            condition = "and `basis` like concat(\"%\",\"IM\",\"%\") ";
        } else if (type == 4) {
            condition = "and `closedCode`=\"缺陷\"";
        } else if (type == 5) {
            return "select a.referSys as referSys,count(*) as total from design_biz_changeTask a LEFT JOIN design_biz_change b ON a.changeId=b.id " +
                    " where (a.status=\"复核中\" or a.status=\"复核中_\" or a.status=\"已关闭\") and a.remedyFlag=0 and b.created_time >= :startDate and b.created_time <= :endDate  and a.referSys is not null and a.referSys!=''" +
                    " and a.implResultCheck = \"3\"" +
                    " group by a.referSys" +
                    " limit " + passed + "," + pageSize;
        }
        return "select startDept,count(*) as total from design_biz_change " +
                "where (status=\"变更实施中\" or status=\"已完成\" or status=\"已关闭\")  and created_time >= :startDate and created_time <= :endDate and `closedCode`!=\"取消\"" +
                condition +
                " group by startDept" +
                " limit " + passed + "," + pageSize;
    }

    public String getTotalMatchSql(int type, Integer passed, Integer pageSize) {
        String condition = "";
        if (type == 1) {
            condition = "and `closedCode`!=\"失败\"";
        } else if (type == 2) {
            condition = "and `type`=\"2\" ";
        } else if (type == 3) {
            condition = "and `basis` like concat(\"%\",\"IM\",\"%\") ";
        } else if (type == 4) {
            condition = "and `closedCode`=\"缺陷\"";
        } else if (type == 5) {
            return "select a.referSys as referSys,count(*) as total from design_biz_changeTask a LEFT JOIN design_biz_change b ON a.changeId=b.id " +
                    " where (a.status=\"复核中\" or a.status=\"复核中_\" or a.status=\"已关闭\") and a.remedyFlag=0 and b.created_time like concat(:date,\"%\")   and a.referSys is not null and a.referSys!=''" +
                    " and a.implResultCheck = \"3\"" +
                    " group by a.referSys " +
                    " limit " + passed + "," + pageSize;
        }
        return "select startDept,count(*) as total from design_biz_change " +
                "where (status=\"变更实施中\" or status=\"已完成\" or status=\"已关闭\")  and  created_time like concat(:date,\"%\")  and `closedCode`!=\"取消\"" +
                condition +
                " group by startDept " +
                " limit " + passed + "," + pageSize;
    }

    public AjaxResult getChangeRateList(String date, String startDeptId) {
        String totalSql = getStartDeptTotalSql(0);
        Map<String, Object> query = new HashMap<>();
        OgOrg ogOrg = sysDeptService.selectDeptById(startDeptId);
        String startDept = ogOrg.getOrgName();
        query.put("startDept", startDept);
        query.put("date", date);
        String totalStr = jdbcTemplate.queryForObject(totalSql, query, String.class);
        String fetchTotalSql = getStartDeptTotalSql(1);
        String fetchTotalStr = jdbcTemplate.queryForObject(fetchTotalSql, query, String.class);
        String mergeTotalSql = getStartDeptTotalSql(2);
        String mergeTotalStr = jdbcTemplate.queryForObject(mergeTotalSql, query, String.class);
        Double total = Double.parseDouble(totalStr);
        Double fetchTotal = Double.parseDouble(fetchTotalStr);
        Double mergeTotal = Double.parseDouble(mergeTotalStr);
        Double fetchRate = fetchTotal / total;
        Double mergeRate = mergeTotal / total;
        DecimalFormat df = new DecimalFormat("0.00%");
        String fetchRateStr = df.format(fetchRate);
        String mergeRateStr = df.format(mergeRate);
        ChangeDeptEntity changeDeptEntity = changeDeptService.selectByDeptId(startDeptId);
        //TODO 补充排名
        String fetchRateRank = "暂无";
        String mergeRateRank = "暂无";
        String mergeLeftSize = "无限制";
        if (changeDeptEntity != null) {
            mergeLeftSize = changeDeptEntity.getOverSize();
        }
        // 整合数据
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> label = new HashMap<>();
        label.put("label", "本年度紧急变更率");
        label.put("val", "mergeRateRank");
        label.put("rate", mergeRateStr);
        Map<String, Object> label2 = new HashMap<>();
        label2.put("label", "本年度缺陷变更率");
        label2.put("val", "fetchRateRank");
        label2.put("rate", fetchRateStr);
        list.add(label);
        list.add(label2);
        Map<String, Object> rank = new HashMap<>();
        rank.put("fetchRateRank", fetchRateRank);
        rank.put("mergeRateRank", mergeRateRank);
        rank.put("mergeLeftSize", mergeLeftSize);
        Map<String, Object> result = new HashMap<>();
        result.put("level1", rank);
        result.put("level2", list);
        return AjaxResult.success(result);
    }

    public String getStartDeptTotalSql(Integer type) {
        String condition = "";
        if (type == 1) {
            condition = "and `closedCode`=\"缺陷\"";
        } else if (type == 2) {
            condition = "and `type`=\"2\"";
        }
        return "select count(*) as total from design_biz_change " +
                " where status!=\"取消\" and  created_time like concat(:date,\"%\")  and startDept=:startDept and `closedCode`!=\"取消\""
                + condition;
    }

    public MapSqlParameterSource getStartDateAndEndDate(String date) {
        //TODO 检查date格式，必须是yyyy-MM
        String endDate = date + "-25";
        LocalDate localDate = LocalDate.parse(endDate);
        localDate = localDate.minusMonths(1);
        localDate = localDate.plusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = dateTimeFormatter.format(localDate);
        Map<String, String> param = new HashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValues(param);
        return mapSqlParameterSource;
    }

    public MapSqlParameterSource getDate(String date) {
        Map<String, String> param = new HashMap<>();
        param.put("date", date);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValues(param);
        return mapSqlParameterSource;
    }


    public Map<String, Object> parseToCondition(List<DesignBizShieldWarn> dataList) {
        List<Map<String, Object>> resultGroupList = new ArrayList<>();
        dataList.forEach(p -> {
            String indexType = p.getIndexType();
            String insName = p.getInsName();
            String ipAddress = p.getIpAddress();
            String net = p.getNet();
            String objectName = p.getObjectName();
            String pasoCode = p.getPasoCode();
            String systemCode = p.getSystemCode();
            Map<String, Object> groupMap = new HashMap<>();
            groupMap.put("isAnd", true);
            List<Map<String, Object>> groupList = new ArrayList<>();
            if (indexType != null && !"".equals(indexType)) {
                String[] indexTypeArray = indexType.split(",");
                if (indexTypeArray.length > 0) {
                    List<String> indexTypeList = Arrays.asList(indexTypeArray);
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", getId());
                    map.put("key", "extend6");
                    map.put("isAnd", true);
                    map.put("operator", "includes");
                    map.put("values", indexTypeList);
                    groupList.add(map);
                }
            }
            if (insName != null && !"".equals(insName)) {
                String[] insNameArray = insName.split(",");
                if (insNameArray.length > 0) {
                    List<String> insNameList = Arrays.asList(insNameArray);
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", getId());
                    map.put("key", "objectInstance");
                    map.put("isAnd", true);
                    map.put("operator", "includes");
                    map.put("values", insNameList);
                    groupList.add(map);
                }
            }
            if (ipAddress != null && !"".equals(ipAddress)) {
                String[] ipAddressArray = ipAddress.split(",");
                if (ipAddressArray.length > 0) {
                    List<String> ipAddressList = Arrays.asList(ipAddressArray);
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", getId());
                    map.put("key", "host");
                    map.put("isAnd", true);
                    map.put("operator", "includes");
                    map.put("values", ipAddressList);
                    groupList.add(map);
                }
            }
            List<String> objectNameList = new ArrayList<>();
            if (net != null && !"".equals(net)) {
                String[] netArray = net.split(",");
                if (netArray.length > 0) {
                    objectNameList.addAll(Arrays.asList(netArray));
                }
            }
            if (objectName != null && !"".equals(objectName)) {
                String[] objectNameArray = objectName.split(",");
                if (objectNameArray.length > 0) {
                    objectNameList.addAll(Arrays.asList(objectNameArray));
                }
            }
            if (!objectNameList.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", getId());
                map.put("key", "object");
                map.put("isAnd", true);
                map.put("operator", "includes");
                map.put("values", objectNameList);
                groupList.add(map);
            }
            if (pasoCode != null && !"".equals(pasoCode)) {
                String[] pasoCodeArray = pasoCode.split(",");
                if (pasoCodeArray.length > 0) {
                    List<String> pasCodeList = Arrays.asList(pasoCodeArray);
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", getId());
                    map.put("key", "businessEnName");
                    map.put("isAnd", true);
                    map.put("operator", "includes");
                    map.put("values", pasCodeList);
                    groupList.add(map);
                }
            }
            if (systemCode != null && !"".equals(systemCode)) {
                String[] systemCodeArray = systemCode.split(",");
                if (systemCodeArray.length > 0) {
                    List<String> systemCodeList = Arrays.asList(systemCodeArray);
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", getId());
                    map.put("key", "location");
                    map.put("isAnd", true);
                    map.put("operator", "includes");
                    map.put("values", systemCodeList);
                    groupList.add(map);
                }
            }
            groupMap.put("group", groupList);
            resultGroupList.add(groupMap);
        });
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", getId());
        conditionMap.put("isAnd", false);
        conditionMap.put("group", resultGroupList);
        return conditionMap;
    }

    public String getId() {
        return new Date().getTime() + UUID.randomUUID().toString().replaceAll("\\-", "").substring(2, 6);
    }
    
    @Autowired
    private EntegorServerImpl entegorServer;
    public Map orgEntegorData(String from,String json,String userName,String itsmNo,String selfToken) {
        Map<String, Object> m = new HashMap<String, Object>();
        m = entegorServer.startAutoEntegorMiddleGround(from, json, userName, itsmNo, selfToken);
        return m;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateStatus(ChangeFieldEnum conditionKey, String conditionValue, String status,String table) {
        String conditionKeyName = conditionKey.getName();
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeFieldEnum.STATUS.getName(), status);
        query.put(conditionKeyName, conditionValue);
        updateByTableName(table, param, query);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ChangeFieldEnum.EXTRA1.getName(), "");
        String sql="";
        String changeNo = selectObject(sql,table, paramMap, query, String.class);
        relationLogService.update(null, Wrappers.<RelationLog>update().eq("request_no", String.valueOf(changeNo)).set("status", status));
    }
}
