package com.ruoyi.form.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.activiti.domain.HistoricActivity;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.activiti.ChangeUtil;
import com.ruoyi.form.activiti.listener.ChangeNodeCompleteListener;
import com.ruoyi.form.constants.CustomerFlowConstants;
import com.ruoyi.form.domain.*;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.openapi.vo.ECompleteVO;
import com.ruoyi.form.service.*;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.*;
import com.ruoyi.system.service.server.EntegorServerImpl;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.ruoyi.form.activiti.Base.ADMIN_ORG_LIST;


@Service
public class ChangeServiceImpl implements IChangeService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ChangeServiceImpl.class);
    @Resource
    CustomerFormMapper customerFormMapper;
    @Autowired
    ForeignService foreignService;
    @Autowired
    FormVersionService formVersionService;
    @Autowired
    CustomerFormService customerFormService;
    @Autowired
    TaskService taskService;
    @Autowired
    IOgPersonService ogPersonService;
    @Autowired
    IOgUserService ogUserService;
    @Autowired
    IChangeTaskSceneService changeTaskSceneService;
    @Autowired
    DesignBizJsonDataService designBizJsonDataService;
    @Autowired
    ISysDeptService sysDeptService;
    @Autowired
    Base base;
    @Autowired
    IChangeTaskOrgService changeTaskOrgService;
    @Autowired
    ISysApplicationManagerService sysApplicationManagerService;
    @Autowired
    IRiskAccessRecordService riskAccessRecordService;
    @Autowired
    IRiskAssessmentService riskAssessmentService;
    @Autowired
    private IPubParaValueService pubParaValueService;
    @Autowired
    IImplRecordService iImplRecordService;
    @Autowired
    IChangePersonService changePersonService;
    @Autowired
    IChangeDeptService changeDeptService;
    @Autowired
    HistoryService historyService;
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    OperationDetailsService operationDetailsService;
    @Autowired
    IFormApprovalFreeService formApprovalFreeService;
    @Autowired
    ButtonActionService buttonActionService;
    @Autowired
    EntegorServerImpl entegorServer;
    @Autowired
    OgPostService ogPostService;
    @Autowired
    IOgUserPostService ogUserPostService;
    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    @Qualifier(value = "autoRestTemplate")
    RestTemplate restTemplate;


    @Value("${foreign.entegorPyService.url}")
    String address;
    @Value("${foreign.entegorPyService.username}")
    String user;
    @Value("${foreign.entegorPyService.password}")
    String password;

    @Override
    public String getChangeVersion() {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(ChangeTableNameEnum.CHANGE.getName(), null);
        return String.valueOf(currentTableInfo.get("version"));
    }

    @Override
    public String getChangeTaskVersion() {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(ChangeTableNameEnum.CHANGE_TASK.getName(), null);
        return String.valueOf(currentTableInfo.get("version"));
    }

    @Override
    public void updateChangeIdByChangeNoANDTaskId(String id, String changeNo) {
        /*MybatisPlusConfig.customerTableName.set(Base.CHANGE_TABLE_NAME);
        List<Object> list = customerFormMapper.selectObjs(Wrappers.<CustomerFormContent>query().select(ChangeFieldEnum.ID.getName()).eq(ChangeFieldEnum.EXTRA1.getName(), changeNo));
        MybatisPlusConfig.customerTableName.remove();*/
        String changeId = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.ID, ChangeFieldEnum.EXTRA1, changeNo);
        base.updateChangeTaskSingle(ChangeTaskFieldEnum.ID, id, ChangeTaskFieldEnum.CHANGE_ID, changeId);
        /*if (!list.isEmpty() && list.get(0) != null) {
            String changeId = list.get(0).toString();
            MybatisPlusConfig.customerTableName.set(Base.CHANGE_TASK_TABLE_NAME);
            //??????
            customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().set(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId).eq(ChangeTaskFieldEnum.ID.getName(), id));
            MybatisPlusConfig.customerTableName.remove();
        }*/

    }

   /* @Override
    public void updateApprovalById(String tableName, String id, String approval) {
        MybatisPlusConfig.customerTableName.set(tableName);
        //??????
        customerFormMapper.update(null, Wrappers.<CustomerFormContent>update().eq(Base.ID, id).set(Base.APPROVAL, approval));
        MybatisPlusConfig.customerTableName.remove();
    }*/

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult init(String formCode, String id, String currentUserId, Map<String, Object> fieldMap, Boolean pageFlag) {
        CustomerFormContent customerFormContent = initCustomerFormContent(ChangeTableNameEnum.CHANGE.getName());
        OgPerson starter = ogPersonService.selectOgPersonById(currentUserId);
        OgUser user = ogUserService.selectOgUserByUserId(currentUserId);
        Map<String, Object> map = customerFormContent.getFields();
        map.put("basisType", "3");
        map.put("incident_relation_flag", 0);
        map.putAll(fieldMap);
        String orgId = starter.getOrgId();
        String orgName = starter.getOrgname();
        /* if (pageFlag) {*/
        OgOrg ogOrg = sysDeptService.selectDeptById(orgId);
        if ("1".equals(ogOrg.getType())) {
            map.put(ChangeFieldEnum.BRANCH_FLAG.getName(), 1);
            //??????????????????????????????????????????????????????
            orgId = changePersonService.selectDept(orgId);
            OgOrg branchOrg = sysDeptService.selectDeptById(orgId);
            orgName = branchOrg.getOrgName();
            //?????????????????????????????????????????????????????????
            String branchChangeMangerUserId = base.getBranchChangeMangerUserId(orgId);
            if (branchChangeMangerUserId == null) {
                return AjaxResult.warn("???????????????????????????" + orgName + "??????????????????????????????");
            }
            String branchGeneralMangerUserId = base.getBranchGeneralMangerUserId(orgId);
            if (branchGeneralMangerUserId == null) {
                return AjaxResult.warn("???????????????????????????" + orgName + "???????????????????????????");
            }
        }
        /* }*/
        //???????????????????????????????????????????????????
        String changeMangerUserId = base.getChangeMangerUserId();
        if (changeMangerUserId == null) {
            return AjaxResult.warn("????????????????????????????????????????????????");
        }
        String generalManagerUserId = base.getGeneralManagerUserId();
        if (generalManagerUserId == null) {
            return AjaxResult.warn("?????????????????????????????????????????????");
        }
        ChangeDeptPersonVo changeDeptPersonVo = changePersonService.selectOneId(orgId);
        if (changeDeptPersonVo == null) {
            return AjaxResult.warn("????????????????????????" + orgName + "?????????????????????????????????");
        }
        map.put("startDept", orgName);
        map.put("extra2", orgId);
        String leaderName = changeDeptPersonVo.getDeptPersonName();
        String personId = changeDeptPersonVo.getDeptPersonId();
        OgUser leader = ogUserService.selectOgUserByUserId(personId);
        if (leader != null) {
            leaderName = leaderName + "(" + leader.getUsername() + ")";
        }
        map.put("startDeptLeader", leaderName);
        String starterName = starter.getpName();
        if (user != null) {
            starterName = starterName + "(" + user.getUsername() + ")";
        }
        map.put("starter", starterName);
        map.put("starterTel", starter.getMobilPhone());
        map.put("supplyProcess", 0);
        String code = "change";
        Object appProcessId = map.get("appProcessId");
        String desc = "";
        if (appProcessId != null && !"".equals(appProcessId)) {
            desc = "???adpm";
            map.put(ChangeFieldEnum.BRANCH_FLAG.getName(), 0);
        }
        if (WorkOrderInformation.problem.getCode().equals(formCode) ||
                WorkOrderInformation.incident.getCode().equals(formCode) ||
                WorkOrderInformation.problem_task.getCode().equals(formCode)) {
            customerFormService.insertOrUpdate(formCode, id, code, customerFormContent);
        } else {
            customerFormService.insertOrUpdate(code, customerFormContent);
        }
        String businessKey = String.valueOf(customerFormContent.getId());
        base.updateChangeApproval(ChangeFieldEnum.ID, businessKey, currentUserId);
        base.updateChangeStatus(ChangeFieldEnum.ID, businessKey, ChangeStatusEnum.unSubmit);
        String changeNo = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.EXTRA1, ChangeFieldEnum.ID, businessKey);
        base.updateChangeSingle(ChangeFieldEnum.ID, businessKey, ChangeFieldEnum.CHANGE_NO, changeNo);
        OperationDetails operationDetails = new OperationDetails();
        operationDetails.setBizNo(changeNo);
        operationDetails.setOperationType("?????????");
        operationDetails.setCreatedName(starterName);
        operationDetails.setCreatedBy(currentUserId);
        operationDetails.setDescription(desc+"?????????????????????");
        operationDetails.setCreatedTime(new Date());
        operationDetailsService.saveOperationDetailsforChange(operationDetails);
        /*changeNo = "CHM" + changeNo.substring(0, changeNo.length() - 3) + businessKey.substring(1);
        base.updateChangeSingle(ChangeFieldEnum.ID, businessKey, ChangeFieldEnum.EXTRA1, changeNo);*/
        String version = getChangeVersion();
        customerFormService.processSubmit(code, businessKey, version, new HashMap<>());
        String processId = base.getChangeInstanceIdByChangeId(businessKey);
        AjaxResult ajaxResult = AjaxResult.success();
        //AjaxResult ajaxResult = customerFormService.approvalPopUp(code, processId, Integer.valueOf(businessKey), "2", version);
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        String taskId = task.getId();
        List<Map<String, Object>> list = base.getChangeRow(businessKey);
        list.forEach(p -> {
            p.put("taskId", taskId);
            p.put("changeNo", changeNo);
            p.put("version", version);
        });
        /*ajaxResult.put("type", code);
        ajaxResult.put("id", businessKey);
        ajaxResult.put("taskId", task.getId());
        ajaxResult.put("instance_id", processId);*/
        ajaxResult.put("records", list);
        ajaxResult.put("changeNo", changeNo);
        return ajaxResult;
    }


    @Override
    //@Transactional(rollbackFor = Throwable.class)
    public AjaxResult initByInterface(String userId, Map<String, Object> changeParam, List<Map<String, Object>> taskParamList) {
        AjaxResult result = init(null, null, userId, changeParam, false);
        if (!"0".equals(result.get(AjaxResult.CODE_TAG).toString())) {
            return result;
        }
        String changeNo = result.get("changeNo").toString();
        //??????????????????????????????
        for (Map<String, Object> taskParam : taskParamList) {
            String referSys = taskParam.get("referSys").toString();
            //TODO ????????????CODE???????????????????????????
            AjaxResult taskResult = initAndSubmitChangeTask(changeNo, "310200898");
            if (!"0".equals(taskResult.get(AjaxResult.CODE_TAG).toString())) {
                String msg = taskResult.get(AjaxResult.MSG_TAG).toString();
                msg = "????????????????????????????????????????????????????????????????????????????????????" + msg;
                taskResult.put(AjaxResult.MSG_TAG, msg);
                return taskResult;
            } else {
                //????????????????????????
                Map<String, Object> param = new HashMap<>();
                Map<String, String> query = new HashMap<>();
                String referSysId = ChangeTaskFieldEnum.REFER_SYS.getName();
                String wantImplTime = ChangeTaskFieldEnum.WANT_IMPL_TIME.getName();
                String deployWay = ChangeTaskFieldEnum.DEPLOY_WAY.getName();
                String mergeFlag = ChangeTaskFieldEnum.MERGE_FLAG.getName();
                String mergeTaskNo = ChangeTaskFieldEnum.MERGE_TASK_NO.getName();
                String branchId = ChangeTaskFieldEnum.BRANCH_ID.getName();
                OgSys ogSys = sysApplicationManagerService.selectOgSysBySysId(taskParam.get(referSysId).toString());
                String deployWayValue = taskParam.get(deployWay).toString();
                param.put(referSysId, ogSys.getCode());
                param.put(wantImplTime, taskParam.get(wantImplTime));
                param.put(deployWay, deployWayValue);
                param.put(mergeFlag, taskParam.get(mergeFlag));
                param.put(mergeTaskNo, taskParam.get(mergeTaskNo));
                param.put(branchId, taskParam.get(branchId));
                String changeTaskNo = taskResult.get("changeTaskNo").toString();
                query.put(ChangeTaskFieldEnum.EXTRA1.getName(), changeTaskNo);
                //?????????devops??????????????????devops????????????????????????
                if("2".equals(deployWayValue)){
                    param.put(ChangeTaskFieldEnum.TYPE.getName(), "design_biz_develop_soft");
                    Map<String,Object> devopsParam = new HashMap<>();
                    devopsParam.put("changeTaskNo",changeTaskNo);
                    devopsParam.put("devopsAppCode",taskParam.get("devopsAppCode"));
                    devopsParam.put("name",taskParam.get("name"));
                    devopsParam.put("path",taskParam.get("path"));
                    devopsParam.put("releaseVersion",taskParam.get("releaseVersion"));
                    addOrUpdateSceneData("design_biz_develop_soft_auto",devopsParam);
                }
                base.update(ChangeTableNameEnum.CHANGE_TASK, param, query);
                String instanceId = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.INSTANCE_ID,ChangeTaskFieldEnum.EXTRA1,changeTaskNo);
                ChangeUtil.ADPM_POOL.submit(instanceId,()->{
                    foreignService.updateAdpmChangeTask(changeTaskNo);
                });
            }
        }
        AjaxResult ajaxResult = AjaxResult.success();
        Map<String, Object> map = new HashMap<>();
        map.put("changeNo", changeNo);
        map.put("changeURL", "");
        ajaxResult.put("resData", map);
        return ajaxResult;
    }

    /**
     * ???????????????????????????id??????????????????????????????????????????
     *
     * @param changNo
     * @param taskDept
     * @return
     */
    @Override
    public AjaxResult initTaskByPage(String changNo, String taskDept) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("changeNo", changNo);
        dataMap.put("taskDept", taskDept);
        //?????????????????????????????????????????????
        ChangeDeptPersonVo changeDeptPersonVo = changePersonService.selectOneId(taskDept);
        if (changeDeptPersonVo == null) {
            return AjaxResult.warn("?????????????????????????????????");
        }
        String preApproval = changeDeptPersonVo.getUserId();
        //String preApproval = ogPersonService.selectOgPersonListByOrgId(taskDept).get(0).getpId();
        dataMap.put(ChangeTaskFieldEnum.PRE_APPROVAL.getName(), preApproval);
        OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ogUser.getpId());
        dataMap.put(ChangeTaskFieldEnum.ASSIGNEE.getName(), ogUser.getpId());
        dataMap.put("assignedGroup", ogPerson.getOrgId());
        dataMap.put(ChangeTaskFieldEnum.CURRENT_PROCESSOR.getName(), ogUser.getPname());
        OgOrg ogOrg = sysDeptService.selectDeptById(taskDept);
        String typeCode = "branch";
        if (!"1".equals(ogOrg.getType())) {
            List<ChangeTaskScene> list = changeTaskSceneService.getListByOrgId(taskDept);
            typeCode = list.get(0).getCode();
        }
        dataMap.put("type", typeCode);
        dataMap.put("taskStatus", "");
        return initChangeTask(dataMap);
    }

    @Override
    public AjaxResult addBasis(String changeNo, String basis) {
        base.updateChangeSingle(ChangeFieldEnum.EXTRA1, changeNo, ChangeFieldEnum.BASIS, basis);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult initChangeTask(Map<String, Object> params) {
        CustomerFormContent customerFormContent = initCustomerFormContent(ChangeTableNameEnum.CHANGE_TASK.getName());
        String dataJson = customerFormContent.getDataJson();
        /*Object o = params.get("enclosureUploadFile");
        if (o != null && !"".equals(o.toString())) {
            String enclosureUpload = o.toString();
            enclosureUpload = enclosureUpload.replaceAll("\"", "");
            enclosureUpload = StringUtils.strip(enclosureUpload, "[]");
            String[] enArray = enclosureUpload.split(",");
            if (enArray.length == 0) {
                params.put("enclosureUploadFile", new String[]{});
            } else {
                params.put("enclosureUploadFile", enArray);
            }
        }*/
        /*Object deployWay = params.get("deployWay");
        if (deployWay != null && !"".equals(deployWay.toString())) {
            String deployWayStr = deployWay.toString();
            deployWayStr = deployWayStr.replaceAll("\"", "");
            deployWayStr = StringUtils.strip(deployWayStr, "[]");
            String[] deployArray = deployWayStr.split(",");
            params.put("deployWay", deployArray);
        }*/
       /* Object remedyReason = params.get(ChangeTaskFieldEnum.REMEDY_REASON.getName());
        if (remedyReason != null && !"".equals(remedyReason.toString())) {
            String remedyReasonStr = remedyReason.toString();
            remedyReasonStr = remedyReasonStr.replaceAll("\"", "");
            remedyReasonStr = StringUtils.strip(remedyReasonStr, "[]");
            String[] remedyReasonArray = remedyReasonStr.split(",");
            params.put(ChangeTaskFieldEnum.REMEDY_REASON.getName(), remedyReasonArray);
        }*/
        params = base.transferTaskMultipleChoiceFieldMap(params);
        dataJson = VueDataJsonUtil.analysisDataJson(dataJson, params);
        customerFormContent.setDataJson(dataJson);
        Map<String, Object> map = customerFormContent.getFields();
        map.putAll(params);
        /*Map<String, Object> button = new HashMap<>();
        button.put("buttonName", "??????????????????");
        button.put("buttonUrlPath", "/customerForm/change/view/table/page");
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(button);
        Map<String,Object> button2 = new HashMap<>();
        button2.put("buttonName","??????");
        button2.put("buttonUrlPath","/init/transfer");*/
        /*String sid = "sid-B4A57C40-93DB-48E5-9C3C-9999F30D2730";
        //????????????????????????sid????????????
        ButtonAction one = buttonActionService.getOne(Wrappers.<ButtonAction>query().eq(ButtonAction.COL_ACTIVITY_NODE_ID, sid));
        Long templateId = one.getTemplateId();*/
        /*Map<String,Object> param = new HashMap<>();
        param.put("*","");
        Map<String,String> query = new HashMap<>();
        query.put("id",String.valueOf(templateId));
        Map<String,Object> resultParam = base.selectMap("approve_template",param,query);
        String json = resultParam.get("json").toString();
        Map<String,Object> actionMap = new HashMap<>();
        actionMap.put("popJson",json);
        List<Map<String,Object>> buttonActionList = new ArrayList<>();
        Map<String,Object> action = new HashMap<>();
        action.put("prop",ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName());
        action.put("val",params.get(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName()));
        buttonActionList.add(action);
        Map<String,Object> action2 = new HashMap<>();
        action2.put("prop",ChangeTaskFieldEnum.ASSIGNEE.getName());
        action2.put("val",params.get(ChangeTaskFieldEnum.ASSIGNEE.getName()));
        buttonActionList.add(action2);*/
        /*actionMap.put("buttonActionList",buttonActionList);
        button2.put("buttonAction",actionMap);*/
        //list.add(button2);
        //result.put("actionButtonList", list);
        return AjaxResult.success(customerFormContent);
    }

    @Override
    public AjaxResult setAdminFreeApproval(String flag, String comment, String changeId) {
        FormApprovalFree formApprovalFree = new FormApprovalFree();
        formApprovalFree.setApproval(CustomerBizInterceptor.currentUserThread.get().getUserId());
        formApprovalFree.setType("change");
        formApprovalFree.setDefineKey(ChangeDefineKeyEnum.adminApproval.getName());
        formApprovalFree.setComment(comment);
        Long flagL = Long.parseLong(flag);
        formApprovalFree.setFlag(flagL);
        formApprovalFree.setFormId(changeId);
        formApprovalFreeService.updateFormApprovalFreeByCondition(formApprovalFree);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult initAndSubmitChangeTask(String changeNo, String taskDept) {
        if (changeNo == null || "".equals(changeNo.trim())) {
            return AjaxResult.error("????????????????????????????????????");
        }
        String createdBy = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.CREATOR, ChangeFieldEnum.EXTRA1, changeNo);
        if (createdBy == null) {
            return AjaxResult.warn("?????????????????????????????????????????????????????????");
        }
        OgOrg ogOrg = sysDeptService.selectDeptById(taskDept);
        String orgName = ogOrg.getOrgName();
        ChangeDeptPersonVo changeDeptPersonVo = changePersonService.selectOneId(taskDept);
        if (changeDeptPersonVo == null) {
            return AjaxResult.warn("?????????" + orgName + "????????????????????????????????????");
        }
        OgPost ogPost = new OgPost();
        ogPost.setPostName("?????????");
        List<OgPost> postList = ogPostService.selectPostList(ogPost);
        String postStr = postList.get(0).getPostId();
        List<OgPerson> personList = ogPersonService.selectListByOrgIdAllAndPostId(taskDept, postStr);
        //??????????????????????????????????????????????????????????????????
        if ("1".equals(ogOrg.getType())) {
            personList = ogPersonService.selectListByOrgIdAllAndPostIdBosc(taskDept, postStr);
        }
        if (personList.isEmpty()) {
            return AjaxResult.warn("????????????" + orgName + "??????????????????");
        }
        String preApproval = personList.get(0).getpId();
        if (changeDeptPersonVo.getUserId() != null && !"".equals(changeDeptPersonVo.getUserId().trim())) {
            //?????????????????????????????????????????????
            String userId = changeDeptPersonVo.getUserId().trim();
            OgPerson ogPerson = ogPersonService.selectOgPersonById(userId);
            if ("1".equals(ogOrg.getType())) {
                OgOrg accountOrg = sysDeptService.selectDeptById(ogPerson.getOrgId());
                if (!accountOrg.getLevelCode().contains(taskDept)) {
                    return AjaxResult.warn("??????????????????" + changeDeptPersonVo.getUserAccount() + "?????????????????????????????????");
                }
            } else {
                if (!taskDept.equals(ogPerson.getOrgId())) {
                    return AjaxResult.warn("??????????????????" + changeDeptPersonVo.getUserAccount() + "?????????????????????????????????");
                }
            }
            OgUserPost ogUserPost = new OgUserPost();
            ogUserPost.setPostId(postStr);
            ogUserPost.setUserId(userId);
            List<OgUserPost> ogUserPostList = ogUserPostService.selectPostByUserId(ogUserPost);
            if (ogUserPostList.isEmpty()) {
                return AjaxResult.warn("???????????????????????????" + changeDeptPersonVo.getUserAccount() + "????????????????????????");
            }
            preApproval = userId;
        }
        CustomerFormContent customerFormContent = initCustomerFormContent(ChangeTableNameEnum.CHANGE_TASK.getName());
        String dataJson = customerFormContent.getDataJson();
        Map<String, Object> fieldMap = customerFormContent.getFields();
        fieldMap.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        fieldMap.put(ChangeTaskFieldEnum.TASKDEPT.getName(), taskDept);
        fieldMap.put(ChangeTaskFieldEnum.PRE_APPROVAL.getName(), preApproval);
        OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ogUser.getpId());
        fieldMap.put(ChangeTaskFieldEnum.ASSIGNEE.getName(), ogUser.getpId());
        fieldMap.put(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName(), ogPerson.getOrgId());
        fieldMap.put(ChangeTaskFieldEnum.MERGE_FLAG.getName(), 1);
        fieldMap.put(ChangeTaskFieldEnum.PRE_TASK_FLAG.getName(), 1);
        fieldMap.put(ChangeTaskFieldEnum.REFER_CENTER_FLAG.getName(), 1);
        fieldMap.put(ChangeTaskFieldEnum.CURRENT_PROCESSOR.getName(), ogPerson.getpName());
        fieldMap.put(ChangeTaskFieldEnum.REFER_FLAG.getName(), 1);
        fieldMap.put("fortressMachineFlag", 0);
        String typeCode = "branch";
        String flag = "B";
        if (!"1".equals(ogOrg.getType())) {
            List<ChangeTaskScene> list = changeTaskSceneService.getListByOrgId(taskDept);
            typeCode = list.get(0).getCode();
            ChangeTaskScene changeTaskScene = list.stream().filter(p->"??????".equals(p.getName())).findFirst().orElse(null);
            if(changeTaskScene!=null){
                typeCode = changeTaskScene.getCode();
            }
            ChangeTaskOrg changeTaskOrg = changeTaskOrgService.getByOrgid(taskDept);
            flag = changeTaskOrg.getFlag();
        }
        fieldMap.put(ChangeTaskFieldEnum.TYPE.getName(), typeCode);
        fieldMap.put("taskStatus", ChangeTaskStatusEnum.registered.getName());
        fieldMap.put("created_by_name", ogPerson.getpName() + "(" + ogUser.getUsername() + ")");
        fieldMap.put("created_dept_name", orgName);
        dataJson = VueDataJsonUtil.analysisDataJson(dataJson, fieldMap);
        customerFormContent.setDataJson(dataJson);
        AjaxResult ajaxResult = customerFormService.insertOrUpdate("changeTask", customerFormContent);
        if (!"0".equals(ajaxResult.get("code").toString())) {
            return ajaxResult;
        }
        //???????????????changeId??????????????????
        String businessKey = String.valueOf(customerFormContent.getId());
        String changeId = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.ID, ChangeFieldEnum.EXTRA1, changeNo);
        Integer count = base.getTaskCountNoCondition(changeId, 0);
        String countStr = String.valueOf(count);
        count++;
        if (count >= 100) {
            return AjaxResult.warn("?????????????????????????????????????????????????????????");
        }
        if (count < 10) {
            countStr = "0" + count;
        }
        String changeTaskNo = changeNo + flag + countStr;
        base.updateChangeTaskSingle(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
        base.updateChangeTaskSingle(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskFieldEnum.CHANGE_TASK_NO, changeTaskNo);
        updateChangeIdByChangeNoANDTaskId(businessKey, changeNo);
        base.updateChangeTaskApproval(ChangeTaskFieldEnum.ID, businessKey, ogPerson.getpId());
        String version = getChangeTaskVersion();
        ajaxResult = customerFormService.processSubmit("changeTask", businessKey, version, new HashMap<>());
        OperationDetails operationDetails = new OperationDetails();
        operationDetails.setBizNo(changeTaskNo);
        operationDetails.setOperationType("???????????????");
        String starter = ogPerson.getpName() + "(" + ogUser.getUsername() + ")";
        operationDetails.setCreatedName(starter);
        operationDetails.setCreatedBy(ogUser.getpId());
        operationDetails.setDescription("???????????????");
        operationDetails.setCreatedTime(new Date());
        operationDetailsService.saveOperationDetailsforChange(operationDetails);
        if (!"0".equals(ajaxResult.get("code").toString())) {
            return ajaxResult;
        }
        List<Map<String, Object>> list = base.getChangeTaskRow(businessKey);
        String processId = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.INSTANCE_ID, ChangeTaskFieldEnum.ID, businessKey);
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        String taskId = task.getId();
        list.forEach(p -> {
            p.put("taskId", taskId);
            p.put("changeTaskNo", changeTaskNo);
            p.put("version", version);
        });
        AjaxResult result = AjaxResult.success();
        result.put("records", list);
        result.put("changeTaskNo", changeTaskNo);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult saveTaskAndStart(CustomerFormContent customerFormContent) {
        String code = "changeTask";
        Object changNoObject = customerFormContent.getFields().get("changeNo");
        if (changNoObject == null || "".equals(changNoObject.toString().trim())) {
            return AjaxResult.error("????????????????????????????????????");
        }
        //??????????????????????????????????????????????????????????????????
        String changeNo = changNoObject.toString();
        String createdBy = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.CREATOR, ChangeFieldEnum.EXTRA1, changeNo);
        if (createdBy == null) {
            return AjaxResult.warn("?????????????????????????????????????????????????????????");
        }
        Object assigneeObj = customerFormContent.getFields().get(ChangeTaskFieldEnum.ASSIGNEE.getName());
        if (assigneeObj == null) {
            return AjaxResult.warn("?????????????????????");
        }
        Object preApprovalObj = customerFormContent.getFields().get(ChangeTaskFieldEnum.PRE_APPROVAL.getName());
        if (assigneeObj.toString().trim().equals(preApprovalObj.toString().trim())) {
            return AjaxResult.warn("??????????????????????????????????????????");
        }
        //????????????
        AjaxResult ajaxResult = AjaxResult.success();
        Object mergeFlag = customerFormContent.getFields().get(ChangeTaskFieldEnum.MERGE_FLAG.getName());
        Object preTaskFlag = customerFormContent.getFields().get(ChangeTaskFieldEnum.PRE_TASK_FLAG.getName());
        if (mergeFlag != null) {
            Object mergeNo = customerFormContent.getFields().get(ChangeTaskFieldEnum.MERGE_TASK_NO.getName());
            ajaxResult = base.checkMergeNo(Integer.parseInt(mergeFlag.toString()), mergeNo, null);
            if (Integer.parseInt(ajaxResult.get(AjaxResult.CODE_TAG).toString()) != AjaxResult.Type.SUCCESS.value()) {
                return ajaxResult;
            }
        }
        if (preTaskFlag != null && "1".equals(preTaskFlag.toString())) {
            Object preTaskNo = customerFormContent.getFields().get(ChangeTaskFieldEnum.PRE_TASK_NO.getName());
            if (preTaskNo == null || "".equals(preTaskNo.toString())) {
                return AjaxResult.warn("???????????????????????????");
            }
        }
        String businessKey = "";
        if (customerFormContent.getFields().get("id") == null) {
            //??????taskName???created_by_name???created_dept_name
            OgPerson ogPerson = ogPersonService.selectOgPersonById(createdBy);
            OgUser ogUser = ogUserService.selectOgUserByUserId(createdBy);
            String createdByName = ogPerson.getpName();
            String createdDeptName = ogPerson.getOrgname();
            String taskDept = customerFormContent.getFields().get("taskDept").toString();
            String type = customerFormContent.getFields().get("type").toString();
            ChangeTaskScene changeTaskScene = changeTaskSceneService.getChangeTaskSceneByCode(type);
            OgOrg ogOrg = sysDeptService.selectDeptById(taskDept);
            String taskName = ogOrg.getOrgName() + changeTaskScene.getName();
            Map<String, Object> map = customerFormContent.getFields();
            map.put("taskName", taskName);
            map.put("created_dept_name", createdDeptName);
            map.put("created_by_name", createdByName + "(" + ogUser.getUsername() + ")");
            Object referSys = customerFormContent.getFields().get(ChangeTaskFieldEnum.REFER_SYS.getName());
            ChangeTaskOrg taskOrg = changeTaskOrgService.getByOrgName("???????????????");
            if (createdBy.equals(assigneeObj.toString().trim()) &&
                    taskOrg.getOrgid().equals(taskDept) && (referSys == null || "".equals(referSys.toString().trim()))) {
                return AjaxResult.warn("??????????????????????????????");
            }
            String assignee = assigneeObj.toString();
            if (assignee.equals(createdBy)) {
                //?????????????????????????????????
                Object changeTaskVerification = customerFormContent.getFields().get(ChangeTaskFieldEnum.CHANGE_TASK_VERIFICATION.getName());
                //Object taskContent = customerFormContent.getFields().get(ChangeTaskFieldEnum.TASK_CONTENT.getName());
                Object backupStep = customerFormContent.getFields().get(ChangeTaskFieldEnum.BACKUP_STEP.getName());
                Object operateStep = customerFormContent.getFields().get(ChangeTaskFieldEnum.OPERATE_STEP.getName());
                Object verificationStep = customerFormContent.getFields().get(ChangeTaskFieldEnum.VERIFI_STEP.getName());
                Object returnStep = customerFormContent.getFields().get(ChangeTaskFieldEnum.RETURN_STEP.getName());
                // Object infRange = customerFormContent.getFields().get(ChangeTaskFieldEnum.INF_RANGE.getName());
                if (changeTaskVerification == null || "".equals(changeTaskVerification.toString().trim())) {
                    return AjaxResult.warn("?????????????????????????????????");
                }
            /*    if (taskContent == null || "".equals(taskContent.toString().trim())) {
                    return AjaxResult.warn("?????????????????????????????????");
                }*/
                if (backupStep == null || "".equals(backupStep.toString().trim())) {
                    return AjaxResult.warn("???????????????????????????");
                }
                if (operateStep == null || "".equals(operateStep.toString().trim())) {
                    return AjaxResult.warn("???????????????????????????");
                }
                if (verificationStep == null || "".equals(verificationStep.toString().trim())) {
                    return AjaxResult.warn("???????????????????????????");
                }
                if (returnStep == null || "".equals(returnStep.toString().trim())) {
                    return AjaxResult.warn("???????????????????????????");
                }
            /*    if (infRange == null || "".equals(infRange.toString().trim())) {
                    return AjaxResult.warn("???????????????????????????");
                }*/
            }
            customerFormService.insertOrUpdate(code, customerFormContent);
            //???????????????changeId??????????????????
            /* OgPerson assigneeInfo = ogPersonService.selectOgPersonById(assignee);*/
            businessKey = String.valueOf(customerFormContent.getId());
            //String changeTaskNo = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.EXTRA1, ChangeTaskFieldEnum.ID, businessKey);
            String dept = customerFormContent.getFields().get(ChangeTaskFieldEnum.TASKDEPT.getName()).toString();
            OgOrg taskDeptObj = sysDeptService.selectDeptById(dept);
            String flag = "B";
            if (!"1".equals(taskDeptObj.getType())) {
                ChangeTaskOrg changeTaskOrg = changeTaskOrgService.getByOrgid(dept);
                flag = changeTaskOrg.getFlag();
            }
            String changeId = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.ID, ChangeFieldEnum.EXTRA1, changeNo);
            Integer count = base.getTaskCountNoCondition(changeId, 0);
            String countStr = String.valueOf(count);
            count++;
            if (count >= 100) {
                return AjaxResult.warn("?????????????????????????????????????????????????????????");
            }
            if (count < 10) {
                countStr = "0" + count;
            }
            String changeTaskNo = changNoObject + flag + countStr;
          /*  Map<String,String> query = new HashMap<>();
            query.put(ChangeTaskFieldEnum.EXTRA1.getName(),changeTaskNo);
            Integer changeTaskNoCount = base.getCount(ChangeTableNameEnum.CHANGE_TASK,query);
            if(changeTaskNoCount>=1){
                return AjaxResult.warn("????????????????????????");
            }*/
            base.updateChangeTaskSingle(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
            base.updateChangeTaskSingle(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskFieldEnum.CHANGE_TASK_NO, changeTaskNo);
            updateChangeIdByChangeNoANDTaskId(businessKey, changeNo);
            base.updateChangeTaskApproval(ChangeTaskFieldEnum.ID, businessKey, assignee);
            String version = getChangeTaskVersion();
            ajaxResult = customerFormService.processSubmit(code, businessKey, version, new HashMap<>());
            if (!"0".equals(ajaxResult.get("code").toString())) {
                return ajaxResult;
            }
            base.updateChangeTaskStatus(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskStatusEnum.registered);
           /* if (assignee.equals(createdBy)) {
                //??????????????????????????????
                String instanceId = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.INSTANCE_ID, ChangeTaskFieldEnum.ID, businessKey);
                Task task = taskService.createTaskQuery()
                        .processInstanceId(instanceId).taskAssignee(assignee).singleResult();
                taskService.complete(task.getId());
                //????????????????????????????????????????????????????????????????????????????????????
                base.addChangeTaskSysOperateDetail(businessKey,"???????????????????????????",base.getUsernameAndPname(createdBy));
                base.updateChangeTaskStatus(ChangeTaskFieldEnum.ID, businessKey, ChangeTaskStatusEnum.planReviewed);
            }*/
        }
        //?????????????????????????????????,???????????????????????????
        /*else {
            //?????????changeNo
            String businessKey = String.valueOf(customerFormContent.getFields().get(Base.ID));
            MybatisPlusConfig.customerTableName.set(Base.CHANGE_TASK_TABLE_NAME);
            List<Object> list = customerFormMapper.selectObjs(Wrappers.<CustomerFormContent>query().select(Base.CHANGE_NO).eq(Base.ID, businessKey));
            String oldNo = list.get(0).toString();
            MybatisPlusConfig.customerTableName.remove();
            if (!changeNo.equals(oldNo)) {
                return AjaxResult.warn("???????????????????????????");
            }
            return customerFormService.insertOrUpdate(code, customerFormContent);
        }*/
        //????????????????????????????????????????????????????????????????????????????????????????????????
        //???????????????????????????????????????
        return ajaxResult;
    }

    @Override
    public CustomerFormContent initCustomerFormContent(String tableName) {
        //TODO ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        DesignFormVersion formVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", tableName).eq("is_current", 1));
        String json = "";
        CustomerFormContent customerFormContent = new CustomerFormContent();
        if (formVersion != null) {
            json = formVersion.getJson();
            List<Object> list = getFormFieldByFormId(String.valueOf(formVersion.getId()));
            Map<String, Object> fields = new HashMap<>();
            list.forEach(i -> {
                String key = i.toString().trim();
                Object value = "";
                fields.put(key, value);
            });
            customerFormContent.setFields(fields);
        }
        customerFormContent.setDataJson(json);
        return customerFormContent;
    }

    @Override
    public AjaxResult getChangeTotalPage() {
        String table = "design_biz_change_total_page";
        DesignFormVersion designFormVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", table).eq("is_current", 1));
        AjaxResult ajaxResult = AjaxResult.success(designFormVersion);
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String date = dateTimeFormatter.format(localDate);
        ajaxResult.put("date", date);
        return ajaxResult;
    }

    @Override
    public List<Map<String, Object>> getFormField(String tableCode) {
        DesignFormVersion formVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", tableCode).eq("is_current", 1));
        return getFormFieldNameAndDescByFormId(String.valueOf(formVersion.getId()));
    }

    public List<Map<String, Object>> getSceneData(String tableCode, String changeTaskNo) {
        List<Map<String, Object>> fieldList = getFormField(tableCode);
        List<Map<String, Object>> valueList = getAllFieldValueBySingleCondition(tableCode, "changeTaskNo", changeTaskNo);
        if (!valueList.isEmpty()) {
            Map<String, Object> map = valueList.get(0);
            fieldList.forEach(p -> p.put("value", map.get(p.get("name"))));
        } else {
            fieldList.forEach(p -> p.put("value", ""));
        }
        return fieldList;
    }

    @Override
    public List<Map<String, Object>> getAllFieldValueBySingleCondition(String tableCode, String key, String value) {
        Integer count = base.tableExist(tableCode);
        if (count == 0) {
            return new ArrayList<>();
        }
        MybatisPlusConfig.customerTableName.set(tableCode);
        List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("*").eq(key, value));
        MybatisPlusConfig.customerTableName.remove();
        return list;
    }

    @Override
    public Page<Map<String, Object>> getAllFieldValuePageBySingleCondition(String tableCode, String key, String value, Page<Map<String, Object>> page) {
        Integer count = base.tableExist(tableCode);
        if (count == 0) {
            return null;
        }
        MybatisPlusConfig.customerTableName.set(tableCode);
        Page<Map<String, Object>> result = customerFormMapper.selectMapsPage(page, Wrappers.<CustomerFormContent>query().select("*").eq(key, value));
        MybatisPlusConfig.customerTableName.remove();
        return result;
    }

    @Override
    public AjaxResult delARowBySingleCondition(String tableCode, String key, String value) {
        Integer count = base.tableExist(tableCode);
        if (count == 0) {
            return AjaxResult.warn("??????????????????code");
        }
        MybatisPlusConfig.customerTableName.set(tableCode);
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);
        customerFormMapper.deleteByMap(map);
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success();
    }

    @Override
    public int getCountBySingleCondition(String tableCode, String key, String value) {
        Integer count = base.tableExist(tableCode);
        if (count == 0) {
            return 0;
        }
        MybatisPlusConfig.customerTableName.set(tableCode);
        List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("count(*) as total").eq(key, value));
        MybatisPlusConfig.customerTableName.remove();
        if (list.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(list.get(0).get("total").toString());
        }
    }

    private List<Object> getFormFieldByFormId(String formId) {
       /* MybatisPlusConfig.customerTableName.set("design_table");
        List<Object> list = customerFormMapper.selectObjs(Wrappers.<CustomerFormContent>query().select("id").eq("form_id", formId));
        String tableId = list.get(0).toString();*/
//        MybatisPlusConfig.customerTableName.remove();
        MybatisPlusConfig.customerTableName.set("design_field_version");
        List<Object> list = customerFormMapper.selectObjs(Wrappers.<CustomerFormContent>query().select("name").eq("form_version_id", formId));
        MybatisPlusConfig.customerTableName.remove();
        return list;
    }

    private List<Map<String, Object>> getFormFieldNameAndDescByFormId(String formId) {
      /*  MybatisPlusConfig.customerTableName.set("design_table");
        List<Object> list = customerFormMapper.selectObjs(Wrappers.<CustomerFormContent>query().select("id").eq("form_id", formId));
        String tableId = list.get(0).toString();
        MybatisPlusConfig.customerTableName.remove();*/
        MybatisPlusConfig.customerTableName.set("design_field_version");
        List<Map<String, Object>> list = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("name,description").eq("form_version_id", formId));
        MybatisPlusConfig.customerTableName.remove();
        return list;
    }

 /*   @Override
    public OgPerson getOrgLeaderByOrgId(String orgId) {
        *//*OgPerson ogPerson = new OgPerson();
        ogPerson.setOrgId(orgId);
        List<OgPerson> list = ogPersonService.selectOgPersonJqList(ogPerson);
        AtomicReference<OgPerson> leader = new AtomicReference<>();
        list.stream().anyMatch(p -> {
            if ("1".equals(p.getLeader())) {
                leader.set(p);
                return true;
            }
            return false;
        });
        return leader.get();*//*

    }*/

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult backCompletion(String taskId, String instanceId, CustomerVo customerVo) {
        customerFormService.complete("change", taskId, instanceId, "????????????", customerVo);
        //????????????????????????????????????????????????
        //??????????????????
        String approval = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.APPROVAL, ChangeFieldEnum.INSTANCE_ID, instanceId);
        Task task = taskService.createTaskQuery().taskCandidateOrAssigned(approval)
                .processInstanceId(instanceId).singleResult();
        Map<String, Object> variables = new HashMap<>();
        variables.put(Base.RECODE, 2);
        taskService.complete(task.getId(), variables);
        base.updateChangeStatus(ChangeFieldEnum.INSTANCE_ID, instanceId, ChangeStatusEnum.prepared);
        return AjaxResult.success();
    }


    @Override
    public AjaxResult changeTaskList(String changeNo) {
        List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        list.forEach(p -> {
            p.put("changeTaskNo", p.get("extra1"));
            Object sys = p.get("referSys");
            String sysId = "";
            if (sys != null) {
                sysId = sys.toString();
            }
            String caption = "";
            if (!"".equals(sysId.trim())) {
                OgSys ogSys = sysApplicationManagerService.selectOgSysBySysCode(sysId);
                if (ogSys != null) {
                    caption = ogSys.getCaption();
                }
            }
            p.put("referSys", caption);
            Object flag = p.get(ChangeTaskFieldEnum.BACK_TO_APPROVAL_FLAG.getName());
            String backFlag = "???";
            if (flag != null && Integer.parseInt(flag.toString()) == 1) {
                backFlag = "???";
            }
            Object backToStartFlag = p.get(ChangeTaskFieldEnum.BACK_TO_START.getName());
            String backStartFlag = "???";
            if (backToStartFlag != null && Integer.parseInt(backToStartFlag.toString()) == 1) {
                backStartFlag = "???";
            }
            p.put(ChangeTaskFieldEnum.BACK_TO_APPROVAL_FLAG.getName(), backFlag);
            p.put(ChangeTaskFieldEnum.BACK_TO_START.getName(), backStartFlag);
        });
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult changeTaskListToTable(String changeNo) {
        List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        for (Map<String, Object> map : list) {
            String preApproval = map.get(ChangeTaskFieldEnum.PRE_APPROVAL.getName()).toString();
            String preName = base.getUsernameAndPname(preApproval);
            map.put("preApprovalName", preName);
        }
        List<Map<String, Object>> fieldList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("label", "???????????????");
        map1.put("val", "changeTaskNo");
        map1.put("width", 180);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("label", "????????????");
        map2.put("val", "taskName");
        map2.put("width", 100);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("label", "????????????");
        map3.put("val", "status");
        map3.put("width", 100);
        Map<String, Object> map4 = new HashMap<>();
        map4.put("label", "????????????");
        map4.put("val", "referSys");
        map4.put("width", 160);
        Map<String, Object> map5 = new HashMap<>();
        map5.put("label", "??????????????????");
        map5.put("val", "planStartDate");
        map5.put("width", 160);
        Map<String, Object> map6 = new HashMap<>();
        map6.put("label", "??????????????????");
        map6.put("val", "planOverDate");
        map6.put("width", 160);
        Map<String, Object> map7 = new HashMap<>();
        map7.put("label", "???????????????");
        map7.put("val", "currentProcessor");
        map7.put("width", 100);
        Map<String, Object> map12 = new HashMap<>();
        map12.put("label", "????????????");
        map12.put("val", "implResultCheck");
        map12.put("width", 100);
        Map<String, Object> map8 = new HashMap<>();
        map8.put("label", "?????????");
        map8.put("val", "preApprovalName");
        map8.put("width", 100);
        Map<String, Object> map9 = new HashMap<>();
        map9.put("label", "?????????");
        map9.put("val", "created_by_name");
        map9.put("width", 100);
        Map<String, Object> map10 = new HashMap<>();
        map10.put("label", "????????????");
        map10.put("val", "created_dept_name");
        map10.put("width", 100);
        Map<String, Object> map11 = new HashMap<>();
        map11.put("label", "????????????");
        map11.put("val", "preTaskNo");
        fieldList.add(map1);
        fieldList.add(map2);
        fieldList.add(map3);
        fieldList.add(map4);
        fieldList.add(map5);
        fieldList.add(map12);
        fieldList.add(map6);
        fieldList.add(map7);
        fieldList.add(map8);
        fieldList.add(map9);
        fieldList.add(map10);
        fieldList.add(map11);
        list.forEach(p -> {
            Object sys = p.get("referSys");
            String sysId = "";
            if (sys != null) {
                sysId = sys.toString();
            }
            String caption = "";
            if (!"".equals(sysId.trim())) {
                OgSys ogSys = sysApplicationManagerService.selectOgSysBySysCode(sysId);
                if (ogSys != null) {
                    caption = ogSys.getCaption();
                }
            }
            p.put("referSys", caption);
            //??????????????????
            Object implResult = p.get(ChangeTaskFieldEnum.IMPL_RESULT.getName());
            if (implResult != null && !"".equals(implResult.toString().trim())) {
                PubParaValue pubParaValue = pubParaValueService.selectPubParaValue("change_task_impl_result", implResult.toString());
                String valueDetail = pubParaValue.getValueDetail();
                p.put(ChangeTaskFieldEnum.IMPL_RESULT.getName(), valueDetail);
            }
           /* Object flag = p.get("backToApprovalFlag");
            String backFlag = "???";
            if (flag != null && Integer.parseInt(flag.toString()) == 1) {
                backFlag = "???";
            }
            p.put("preApprovalBackFlag", backFlag);*/
        });
        Map<String, Object> result = new HashMap<>();
        result.put("col", fieldList);
        result.put("list", list);
        result.put("total", list.size());
        result.put("pageNum", 1);
        result.put("pageSize", 100);

        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult getAppAccessData(String changeNo, String prefix) {
        Map<String, Object> param = new HashMap<>();
        param.put(ChangeTaskFieldEnum.EXTRA1.getName(), "");
        param.put(ChangeTaskFieldEnum.STATUS.getName(), "");
        Map<String, String> query = new HashMap<>();
        query.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        List<Map<String, Object>> taskList = base.selectListMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        List<String> list = new ArrayList<>();
        taskList.stream().filter(p -> !"??????".equals(p.get("status").toString()))
                .forEach(a -> {
                    String changeTaskNo = a.get(ChangeTaskFieldEnum.EXTRA1.getName()).toString();
                    list.add(changeTaskNo);
                });
        //??????design_field_version?????????????????????????????????
        String table = "design_biz_develop_soft";
        List<Map<String, Object>> fieldList = getFormField(table);
        List<Map<String, Object>> colList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("label", "??????????????????");
        map.put("val", "changeTaskNo");
        colList.add(map);
        fieldList.forEach(p -> {
            String fieldName = p.get("name").toString();
            if (StrUtil.startWith(fieldName, prefix)) {
                p.put("label", p.get("description"));
                p.put("val", p.get("name"));
               /* p.remove("name");
                p.remove("description");*/
                colList.add(p);
            }
        });
        List<Map<String, Object>> resultList = new ArrayList<>();
        list.stream().map(p -> base.getAllColumnsValueByColumn(table, "changeTaskNo", p))
                .filter(a -> !a.isEmpty()).collect(Collectors.toList()).forEach(resultList::addAll);
        Map<String, Object> result = new HashMap<>();
        result.put("col", colList);
        result.put("list", resultList);
        result.put("total", resultList.size());
        result.put("pageNum", 1);
        result.put("pageSize", 100);
        return AjaxResult.success(result);
    }

    @Override
    public String getCurrentVersion(String tableName) {
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(tableName, null);
        System.out.println(currentTableInfo.get("version"));
        return String.valueOf(currentTableInfo.get("version"));
    }

    @Override
    public AjaxResult viewTaskPage(String changeTaskId) {
        List<Object> list = new ArrayList<>();
        DesignFormVersion formVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", "design_biz_changetask").eq("is_current", 1));
        String jsonData = formVersion.getJson();
        List<Map<String, Object>> taskList = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.ID.getName(), changeTaskId);
        Map<String, Object> map = taskList.get(0);
        map = base.transferTaskMultipleChoiceFieldMap(map);
        jsonData = VueDataJsonUtil.analysisDataJson(jsonData, map);
        list.add(jsonData);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("formDataJson", list);
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), "");
        param.put(ChangeTaskFieldEnum.STATUS.getName(), "");
        param.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), "");
        query.put(ChangeTaskFieldEnum.ID.getName(), changeTaskId);
        Map<String, Object> result = base.selectMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        String changeId = result.get(ChangeTaskFieldEnum.CHANGE_ID.getName()).toString();
        /*String taskStatus = result.get(ChangeTaskFieldEnum.STATUS.getName()).toString();
        String status = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.STATUS, ChangeFieldEnum.ID, changeId);*/
        Object taskInstanceId = result.get(ChangeTaskFieldEnum.INSTANCE_ID.getName());
        String changeInstanceId = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.INSTANCE_ID, ChangeFieldEnum.ID, changeId);
        Map<String, Object> showHistoryPng = new HashMap<>();
        showHistoryPng.put("buttonName", CustomerFlowConstants.approvalImageButtonName);
        showHistoryPng.put("buttonUrlPath", CustomerFlowConstants.approvalImageButtonPath);
        List<Map<String, Object>> buttons = new ArrayList<>();
        buttons.add(showHistoryPng);
        /*String changeCreatedBy = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.CREATOR, ChangeFieldEnum.ID, changeId);
        String assignee = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.ASSIGNEE, ChangeTaskFieldEnum.ID, changeTaskId);
        if(assignee.equals(changeCreatedBy)&&ChangeStatusEnum.unSubmit.getName().equals(status)&&ChangeTaskStatusEnum.planReviewed.getName().equals(taskStatus)????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????){
            Map<String, Object> sceneButton = new HashMap<>();
            sceneButton.put("buttonName", "??????????????????");
            sceneButton.put("buttonUrlPath", "/customerForm/change/view/table/page");
            buttons.add(sceneButton);
        }*/
        if (changeInstanceId != null) {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(changeInstanceId).list();
            if (!tasks.isEmpty()) {
                Task task = tasks.get(0);
                String changeKey = task.getTaskDefinitionKey();
                if (ChangeDefineKeyEnum.changeMangerApproval.getName().equals(changeKey)) {
                    Map<String, Object> switchButtonMap = new HashMap<>();
                    switchButtonMap.put("buttonName", "????????????");
                    switchButtonMap.put("buttonUrl", "/customerForm/change/task/back/preApproval");
                    switchButtonMap.put("buttonMethod", "POST");
                    switchButtonMap.put("buttonUrlParams", new String[]{"changeTaskNo", "flag"});
                    switchButtonMap.put("buttonBodyParams", null);
                    Map<String, Object> switchButtonMap2 = new HashMap<>();
                    switchButtonMap2.put("buttonName", "????????????");
                    switchButtonMap2.put("buttonUrl", "/customerForm/change/task/back/start");
                    switchButtonMap2.put("buttonMethod", "POST");
                    switchButtonMap2.put("buttonUrlParams", new String[]{"changeTaskNo", "flag"});
                    switchButtonMap2.put("buttonBodyParams", null);
                    List<Map<String, Object>> switchList = new ArrayList<>();
                    switchList.add(switchButtonMap);
                    switchList.add(switchButtonMap2);
                    resultMap.put("switchButtonList", switchList);
                }
                if (taskInstanceId != null) {
                    String taskInstance = taskInstanceId.toString();
                    List<Task> taskTaskList = taskService.createTaskQuery().processInstanceId(taskInstance).list();
                    if (!taskTaskList.isEmpty()) {
                        Task taskTask = taskTaskList.get(0);
                        String taskKey = taskTask.getTaskDefinitionKey();
                        if (ChangeDefineKeyEnum.submit.getName().equals(changeKey) &&
                                (ChangeTaskDefineKeyEnum.submitTask.getName().equals(taskKey) ||
                                        ChangeTaskDefineKeyEnum.waitApproval.getName().equals(taskKey))) {
                            Map<String, Object> button = new HashMap<>();
                            button.put("buttonName", "??????");
                            button.put("buttonUrlPath", "/customerForm/change/task/cancel");
                            buttons.add(button);
                        }
                    }
                }
            }
        }
        resultMap.put("actionButtonList", buttons);
        return AjaxResult.success(resultMap);
    }

    @Override
    public AjaxResult viewChangePage(String changeNo) {
        String changeId = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.ID, ChangeFieldEnum.EXTRA1, changeNo);
        List<Object> list = new ArrayList<>();
        DesignBizJsonData designBizJsonData1 = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query().eq(DesignBizJsonData.COL_BIZ_ID, changeId).eq(DesignBizJsonData.COL_BIZ_TABLE, ChangeTableNameEnum.CHANGE_TASK.getName()));
        String jsonData = designBizJsonData1.getJsonData();
        List<Map<String, Object>> dataList = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE, ChangeFieldEnum.ID.getName(), changeId);
        Map<String, Object> map = dataList.get(0);
        /*Object o = map.get("uploadFile");
        if (o != null && !"".equals(o.toString())) {
            String enclosureUpload = o.toString();
            enclosureUpload = enclosureUpload.replaceAll("\"", "");
            enclosureUpload = StringUtils.strip(enclosureUpload, "[]");
            String[] enArray = enclosureUpload.split(",");
            if (enArray.length == 0 || "".equals(enArray[0])) {
                map.put("uploadFile", new String[]{});
            } else {
                map.put("uploadFile", enArray);
            }
        }*/
        jsonData = VueDataJsonUtil.analysisDataJson(jsonData, map);
        list.add(jsonData);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("formDataJson", list);
        return AjaxResult.success(resultMap);
    }

    @Override
    public AjaxResult setCandidate() {
        /*taskService.addCandidateUser(taskId,candidate);
        taskService.claim(taskId,candidate);*/
        return null;
    }

    @Override
    public AjaxResult getAdminApprovalRecord(String changeNo) {
        String instanceId = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.INSTANCE_ID, ChangeFieldEnum.EXTRA1, changeNo);
        List<HistoricActivity> activityList = new ArrayList<>();
        if (instanceId != null && !"".equals(instanceId.trim())) {
            List<HistoricActivityInstance> historyList = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId)
                    .activityType("userTask")
                    .orderByHistoricActivityInstanceStartTime()
                    .desc()
                    .list();
            historyList.forEach(instance -> {
                HistoricActivity activity = new HistoricActivity();
                BeanUtils.copyProperties(instance, activity);
                String taskId = instance.getTaskId();
                List<Comment> comment = taskService.getTaskComments(taskId, "comment");
                if (!CollectionUtils.isEmpty(comment)) {
                    activity.setComment(comment.get(0).getFullMessage());
                }
                activity.setAssigneeName(instance.getAssignee());
                String assignee = instance.getAssignee();
                OgPerson ogPerson = ogPersonService.selectOgPersonById(assignee);
                if (ogPerson != null) {
                    String name = base.getUsernameAndPname(assignee);
                    activity.setAssigneeName(name);
                }
                activityList.add(activity);
            });
        }
        List<HistoricActivity> list = activityList.stream().filter(p -> ChangeDefineKeyEnum.adminApproval.getName().equals(p.getActivityId()))
                /*.filter(p->p.getComment()!=null&&!"".equals(p.getComment().trim()))*/.collect(Collectors.toList());
        List<Map<String, Object>> resultList = new ArrayList<>();
        list.forEach(p -> {
            String during = "";
            if (p.getDurationInMillis() != null) {
                long duration = p.getDurationInMillis();
                during = base.formatDuring(duration);
            }
            if (p.getDurationInMillis() == null || (p.getDurationInMillis() != null && (p.getComment() != null && !"".equals(p.getComment().trim())))) {
                Map<String, Object> map = new HashMap<>();
                map.put("during", during);
                map.put("assigneeName", p.getAssigneeName());
                map.put("comment", p.getComment());
                map.put("startTime", p.getStartTime());
                map.put("endTime", p.getEndTime());
                resultList.add(map);
            }
        });

        List<Map<String, Object>> colList = new ArrayList<>();
        Map<String, Object> col1 = new HashMap<>();
        col1.put("label", "?????????");
        col1.put("val", "assigneeName");
        Map<String, Object> col2 = new HashMap<>();
        col2.put("label", "????????????");
        col2.put("val", "comment");
        Map<String, Object> col3 = new HashMap<>();
        col3.put("label", "??????????????????");
        col3.put("val", "startTime");
        Map<String, Object> col4 = new HashMap<>();
        col4.put("label", "??????????????????");
        col4.put("val", "endTime");
        Map<String, Object> col5 = new HashMap<>();
        col5.put("label", "????????????");
        col5.put("val", "during");
        colList.add(col1);
        colList.add(col2);
        colList.add(col3);
        colList.add(col4);
        colList.add(col5);
        Map<String, Object> result = new HashMap<>();
        result.put("col", colList);
        result.put("list", resultList);
        result.put("total", resultList.size());
        /*result.put("pageNum", 1);
        result.put("pageSize", 100);*/
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult getTotalList(Integer type, String date, Integer pageNum, Integer pageSize) {
        //TODO ??????date?????????????????????
        String year = date.substring(0, 4);
        List<Map<String, Object>> changeTotalListMonth = base.getChangeMonthTotalCount(date, pageNum, pageSize);
        changeTotalListMonth.forEach(p -> {
            p.put("typeTotal", 0);
            p.put("monthRate", "0%");
            p.put("yearTypeTotal", 0);
            p.put("yearRate", "0%");
        });
        List<Map<String, Object>> taskTotalListMonth = base.getTaskMonthTotalCount(date, pageNum, pageSize);
        taskTotalListMonth = taskTotalListMonth.stream().filter(p -> p.get("referSys") != null && !"".equals(p.get("referSys").toString().trim())).collect(Collectors.toList());
        taskTotalListMonth.forEach(p -> {
            p.put("typeTotal", 0);
            p.put("monthRate", "0%");
            p.put("yearTypeTotal", 0);
            p.put("yearRate", "0%");
        });
        List<Map<String, Object>> changeTotalListYear = base.getChangeTotalCount(year, pageNum, pageSize);
        List<Map<String, Object>> taskTotalListYear = base.getTaskTotalCount(year, pageNum, pageSize);
        List<Map<String, Object>> finalTaskTotalListYear = taskTotalListYear.stream().filter(p -> p.get("referSys") != null && !"".equals(p.get("referSys").toString().trim())).collect(Collectors.toList());
        List<Map<String, Object>> colList = new ArrayList<>();
        Map<String, Object> dept = new HashMap<>();
        if (type == 5) {
            dept.put("label", "????????????");
            dept.put("val", "referSys");
        } else {
            dept.put("label", "??????");
            dept.put("val", "startDept");
        }
        Map<String, Object> change = new HashMap<>();
        change.put("label", "?????????");
        change.put("val", "total");
        colList.add(dept);
        colList.add(change);
        String typeLabel = "???????????????";
        String monthRateLabel = "?????????";
        String yearTypeCountLabel = "????????????????????????";
        String yearRateLabel = "???????????????????????????";
        if (type == 1) {
            typeLabel = "???????????????";
            monthRateLabel = "?????????";
            yearTypeCountLabel = "????????????????????????";
            yearRateLabel = "???????????????????????????";
            List<Map<String, Object>> successChangeCountListMonth = base.getSuccessMonthChangeCount(date, pageNum, pageSize);
            List<Map<String, Object>> successChangeCountListYear = base.getSuccessChangeCount(year, pageNum, pageSize);
            changeTotalListMonth.forEach(p -> {
                String startDept = p.get("startDept").toString();
                successChangeCountListMonth.forEach(a -> {
                    if (startDept.equals(a.get("startDept").toString())) {
                        String typeTotalStr = a.get("total").toString();
                        p.put("typeTotal", typeTotalStr);
                        Double total = Double.parseDouble(p.get("total").toString());
                        Double typeTotal = Double.parseDouble(typeTotalStr);
                        Double rate = typeTotal / total;
                        DecimalFormat df = new DecimalFormat("0.00%");
                        String rateStr = df.format(rate);
                        p.put("monthRate", rateStr);
                    }
                });
            });
            changeTotalListYear.forEach(p -> {
                String startDept = p.get("startDept").toString();
                p.put("yearTotal", p.get("total").toString());
                successChangeCountListYear.forEach(a -> {
                    if (startDept.equals(a.get("startDept").toString())) {
                        String typeTotalStr = a.get("total").toString();
                        p.put("yearTypeTotal", typeTotalStr);
                        Double total = Double.parseDouble(p.get("yearTotal").toString());
                        Double typeTotal = Double.parseDouble(typeTotalStr);
                        Double rate = typeTotal / total;
                        DecimalFormat df = new DecimalFormat("0.00%");
                        String rateStr = df.format(rate);
                        p.put("yearRate", rateStr);
                    }
                });
                p.remove("total");
            });
        } else if (type == 2) {
            typeLabel = "???????????????";
            monthRateLabel = "??????????????????";
            yearTypeCountLabel = "????????????????????????";
            yearRateLabel = "???????????????????????????";
            List<Map<String, Object>> mergeChangeCountListMonth = base.getMergeChangeMonthCount(date, pageNum, pageSize);
            List<Map<String, Object>> mergeChangeCountListYear = base.getMergeChangeCount(year, pageNum, pageSize);
            changeTotalListMonth.forEach(p -> {
                String startDept = p.get("startDept").toString();
                mergeChangeCountListMonth.forEach(a -> {
                    if (startDept.equals(a.get("startDept").toString())) {
                        String typeTotalStr = a.get("total").toString();
                        p.put("typeTotal", typeTotalStr);
                        Double total = Double.parseDouble(p.get("total").toString());
                        Double typeTotal = Double.parseDouble(typeTotalStr);
                        Double rate = typeTotal / total;
                        DecimalFormat df = new DecimalFormat("0.00%");
                        String rateStr = df.format(rate);
                        p.put("monthRate", rateStr);
                    }
                });
            });
            changeTotalListYear.forEach(p -> {
                String startDept = p.get("startDept").toString();
                p.put("yearTotal", p.get("total").toString());
                mergeChangeCountListYear.forEach(a -> {
                    if (startDept.equals(a.get("startDept").toString())) {
                        String typeTotalStr = a.get("total").toString();
                        p.put("yearTypeTotal", typeTotalStr);
                        Double total = Double.parseDouble(p.get("yearTotal").toString());
                        Double typeTotal = Double.parseDouble(typeTotalStr);
                        Double rate = typeTotal / total;
                        DecimalFormat df = new DecimalFormat("0.00%");
                        String rateStr = df.format(rate);
                        p.put("yearRate", rateStr);
                    }
                });
                p.remove("total");
            });
        } else if (type == 3) {
            typeLabel = "?????????????????????";
            monthRateLabel = "????????????????????????";
            yearTypeCountLabel = "??????????????????????????????";
            yearRateLabel = "?????????????????????????????????";
            List<Map<String, Object>> eventChangeCountListMonth = base.getEventChangeMonthCount(date, pageNum, pageSize);
            List<Map<String, Object>> eventChangeCountListYear = base.getEventChangeCount(year, pageNum, pageSize);
            changeTotalListMonth.forEach(p -> {
                String startDept = p.get("startDept").toString();
                eventChangeCountListMonth.forEach(a -> {
                    if (startDept.equals(a.get("startDept").toString())) {
                        String typeTotalStr = a.get("total").toString();
                        p.put("typeTotal", typeTotalStr);
                        Double total = Double.parseDouble(p.get("total").toString());
                        Double typeTotal = Double.parseDouble(typeTotalStr);
                        Double rate = typeTotal / total;
                        DecimalFormat df = new DecimalFormat("0.00%");
                        String rateStr = df.format(rate);
                        p.put("monthRate", rateStr);
                    }
                });
            });
            changeTotalListYear.forEach(p -> {
                String startDept = p.get("startDept").toString();
                p.put("yearTotal", p.get("total").toString());
                eventChangeCountListYear.forEach(a -> {
                    if (startDept.equals(a.get("startDept").toString())) {
                        String typeTotalStr = a.get("total").toString();
                        p.put("yearTypeTotal", typeTotalStr);
                        p.put("yearTotal", p.get("total").toString());
                        Double total = Double.parseDouble(p.get("yearTotal").toString());
                        Double typeTotal = Double.parseDouble(typeTotalStr);
                        Double rate = typeTotal / total;
                        DecimalFormat df = new DecimalFormat("0.00%");
                        String rateStr = df.format(rate);
                        p.put("yearRate", rateStr);
                    }
                });
                p.remove("total");
            });
        } else if (type == 4) {
            typeLabel = "???????????????";
            monthRateLabel = "??????????????????";
            yearTypeCountLabel = "????????????????????????";
            yearRateLabel = "???????????????????????????";
            List<Map<String, Object>> defectChangeCountListMonth = base.getDefectChangeMonthCount(date, pageNum, pageSize);
            List<Map<String, Object>> defectChangeCountListYear = base.getDefectChangeCount(year, pageNum, pageSize);
            changeTotalListMonth.forEach(p -> {
                String startDept = p.get("startDept").toString();
                defectChangeCountListMonth.forEach(a -> {
                    if (startDept.equals(a.get("startDept").toString())) {
                        String typeTotalStr = a.get("total").toString();
                        p.put("typeTotal", typeTotalStr);
                        Double total = Double.parseDouble(p.get("total").toString());
                        Double typeTotal = Double.parseDouble(typeTotalStr);
                        Double rate = typeTotal / total;
                        DecimalFormat df = new DecimalFormat("0.00%");
                        String rateStr = df.format(rate);
                        p.put("monthRate", rateStr);
                    }
                });
            });
            changeTotalListYear.forEach(p -> {
                String startDept = p.get("startDept").toString();
                p.put("yearTotal", p.get("total").toString());
                defectChangeCountListYear.forEach(a -> {
                    if (startDept.equals(a.get("startDept").toString())) {
                        String typeTotalStr = a.get("total").toString();
                        p.put("yearTypeTotal", typeTotalStr);
                        Double total = Double.parseDouble(p.get("yearTotal").toString());
                        Double typeTotal = Double.parseDouble(typeTotalStr);
                        Double rate = typeTotal / total;
                        DecimalFormat df = new DecimalFormat("0.00%");
                        String rateStr = df.format(rate);
                        p.put("yearRate", rateStr);
                    }
                });
                p.remove("total");
            });
        } else if (type == 5) {
            typeLabel = "???????????????";
            monthRateLabel = "??????????????????";
            yearTypeCountLabel = "????????????????????????";
            yearRateLabel = "???????????????????????????";
            List<Map<String, Object>> defectTaskCountListMonth = base.getDefectTaskMonthCount(date, pageNum, pageSize);
            List<Map<String, Object>> finalDefectTaskCountListMonth = defectTaskCountListMonth.stream().filter(p -> p.get("referSys") != null && !"".equals(p.get("referSys").toString().trim())).collect(Collectors.toList());
            List<Map<String, Object>> defectTaskCountListYear = base.getDefectTaskCount(year, pageNum, pageSize);
            List<Map<String, Object>> finalDefectTaskCountListYear = defectTaskCountListYear.stream().filter(p -> p.get("referSys") != null && !"".equals(p.get("referSys").toString().trim())).collect(Collectors.toList());
            taskTotalListMonth.stream().filter(p -> p.get("referSys") != null && !"".equals(p.get("referSys").toString().trim())).collect(Collectors.toList()).forEach(p -> {
                String referSys = p.get("referSys").toString();
                finalDefectTaskCountListMonth.forEach(a -> {
                    if (referSys.equals(a.get("referSys").toString())) {
                        String typeTotalStr = a.get("total").toString();
                        p.put("typeTotal", typeTotalStr);
                        Double total = Double.parseDouble(p.get("total").toString());
                        Double typeTotal = Double.parseDouble(typeTotalStr);
                        Double rate = typeTotal / total;
                        DecimalFormat df = new DecimalFormat("0.00%");
                        String rateStr = df.format(rate);
                        p.put("monthRate", rateStr);
                    }
                });
            });
            taskTotalListYear.stream().filter(p -> p.get("referSys") != null && !"".equals(p.get("referSys").toString().trim())).collect(Collectors.toList()).forEach(p -> {
                String referSys = p.get("referSys").toString();
                p.put("yearTotal", p.get("total").toString());
                finalDefectTaskCountListYear.forEach(a -> {
                    if (referSys.equals(a.get("referSys").toString())) {
                        String typeTotalStr = a.get("total").toString();
                        p.put("yearTypeTotal", typeTotalStr);
                        Double total = Double.parseDouble(p.get("yearTotal").toString());
                        Double typeTotal = Double.parseDouble(typeTotalStr);
                        Double rate = typeTotal / total;
                        DecimalFormat df = new DecimalFormat("0.00%");
                        String rateStr = df.format(rate);
                        p.put("yearRate", rateStr);
                    }
                });
                p.remove("total");
            });
        }
        changeTotalListMonth.forEach(p -> {
            String startDept = p.get("startDept").toString();
            changeTotalListYear.forEach(a -> {
                if (startDept.equals(a.get("startDept").toString())) {
                    p.putAll(a);
                }
            });
        });
        taskTotalListMonth.forEach(p -> {
            String referSys = p.get("referSys").toString();
            finalTaskTotalListYear.forEach(a -> {
                if (referSys.equals(a.get("referSys").toString())) {
                    p.putAll(a);
                }
            });
        });
        taskTotalListMonth.forEach(p -> {
            String referSys = p.get("referSys").toString();
            if (referSys != null && !"".equals(referSys.trim())) {
                OgSys ogSys = sysApplicationManagerService.selectOgSysBySysCode(referSys);
                String sysName = referSys;
                if (ogSys != null) {
                    sysName = ogSys.getCaption();
                }
                p.put("referSys", sysName);
            }
        });
        taskTotalListMonth = taskTotalListMonth.stream().filter(p -> {
            String referSys = p.get("referSys").toString();
            return referSys != null && !"".equals(referSys.trim());
        }).collect(Collectors.toList());
        String label = "??????????????????";
        if (type == 5) {
            label = "??????????????????";
        }
        Map<String, Object> typeTotal = new HashMap<>();
        typeTotal.put("label", typeLabel);
        typeTotal.put("val", "typeTotal");
        colList.add(typeTotal);
        Map<String, Object> monthRate = new HashMap<>();
        monthRate.put("label", monthRateLabel);
        monthRate.put("val", "monthRate");
        colList.add(monthRate);
        Map<String, Object> yearCount = new HashMap<>();
        yearCount.put("label", label);
        yearCount.put("val", "yearTotal");
        colList.add(yearCount);
        Map<String, Object> yearTypeCount = new HashMap<>();
        yearTypeCount.put("label", yearTypeCountLabel);
        yearTypeCount.put("val", "yearTypeTotal");
        colList.add(yearTypeCount);
        Map<String, Object> yearRate = new HashMap<>();
        yearRate.put("label", yearRateLabel);
        yearRate.put("val", "yearRate");
        colList.add(yearRate);
        Map<String, Object> result = new HashMap<>();
        result.put("col", colList);
        if (type == 5) {
            result.put("list", taskTotalListMonth);
            result.put("total", taskTotalListMonth.size());
        } else {
            result.put("list", changeTotalListMonth);
            result.put("total", changeTotalListMonth.size());
        }
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult viewTaskSceneTablePageAndData(String code, String changeTaskNo) {
        CustomerFormContent content = initCustomerFormContent(code);
        if ("".equals(content.getDataJson())) {
            return AjaxResult.warn("?????????????????????????????????");
        }
        String formJson = content.getDataJson();
        List<Map<String, Object>> list = getAllFieldValueBySingleCondition(code, "changeTaskNo", changeTaskNo);
        Map<String, Object> map = new HashMap<>();
        map.put("changeTaskNo", changeTaskNo);
        if (!list.isEmpty()) {
            Map<String, Object> dataMap = list.get(0);
            map.putAll(dataMap);
        }
        content.setFields(map);
        String dataJson = VueDataJsonUtil.analysisDataJson(formJson, map);
        content.setDataJson(dataJson);
        return AjaxResult.success(content);
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult cancelChangeProcess(String id) {
        //?????????????????????ID??????????????????????????????ID
        String instanceId = base.getChangeInstanceIdByChangeId(id);
        Map<String, Object> params = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        params.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), id);
        List<String> list = base.selectList(ChangeTableNameEnum.CHANGE_TASK, params, query, String.class);
        list.forEach(p -> {
            //?????????????????????????????????????????????
            String status = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.STATUS, ChangeTaskFieldEnum.INSTANCE_ID, p);
            if (!ChangeTaskStatusEnum.canceled.getName().equals(status)) {
                customerFormService.cancelApply(p);
                String changeTaskId = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.ID, ChangeTaskFieldEnum.INSTANCE_ID, p);
                base.addChangeTaskSysOperateDetail(changeTaskId, "?????????????????????", CustomerBizInterceptor.currentUserThread.get().getUserId());
                base.updateChangeTaskApproval(ChangeTaskFieldEnum.INSTANCE_ID, p, "");
                base.syncTaskStatusAndCurrentProcessor(ChangeTaskFieldEnum.INSTANCE_ID, p);
                String changeTaskNo = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.EXTRA1, ChangeTaskFieldEnum.INSTANCE_ID, p);
                List<Map<String, Object>> mergeList = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.MERGE_TASK_NO.getName(), changeTaskNo);
                mergeList.forEach(taskMap -> {
                    String iId = taskMap.get(ChangeTaskFieldEnum.INSTANCE_ID.getName()).toString();
                    String mergeTaskStatus = taskMap.get(ChangeTaskFieldEnum.STATUS.getName()).toString();
                    if (!ChangeTaskStatusEnum.canceled.getName().equals(mergeTaskStatus)) {
                        customerFormService.cancelApply(iId);
                        String taskId = taskMap.get(ChangeTaskFieldEnum.ID.getName()).toString();
                        base.addChangeTaskSysOperateDetail(taskId, "?????????????????????", CustomerBizInterceptor.currentUserThread.get().getUserId());
                        base.updateChangeTaskApproval(ChangeTaskFieldEnum.INSTANCE_ID, iId, "");
                        base.syncTaskStatusAndCurrentProcessor(ChangeTaskFieldEnum.INSTANCE_ID, iId);
                    }
                });
            }
        });
        AjaxResult result = customerFormService.cancelApply(instanceId);
        Map<String, Object> param = new HashMap<>();
        param.put(ChangeFieldEnum.STATUS.getName(), ChangeStatusEnum.cancel.getName());
        param.put(ChangeFieldEnum.APPROVAL.getName(), "");
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(ChangeFieldEnum.INSTANCE_ID.getName(), instanceId);
        base.update(ChangeTableNameEnum.CHANGE, param, queryMap);
        base.syncChangeStatusAndCurrentProcessor(ChangeFieldEnum.INSTANCE_ID, instanceId);
        base.addOneOverSize(id);
        base.addChangeSysOperateDetail(id, "??????????????????", CustomerBizInterceptor.currentUserThread.get().getUserId());
        return result;
    }

    @Override
    public AjaxResult getAllMergeTaskInfoList(String changeId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.EXTRA1.getName(), "");
        query.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), changeId);
        List<String> changeTaskNo = base.selectList(ChangeTableNameEnum.CHANGE_TASK, param, query, String.class);
        List<Map<String, Object>> allList = new ArrayList<>();
        changeTaskNo.forEach(p -> {
            List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.MERGE_TASK_NO.getName(), p);
            allList.addAll(list);
        });
        if (allList.isEmpty()) {
            return AjaxResult.success();
        } else {
            return AjaxResult.warn("???" + allList.size() + "???????????????????????????", allList);
        }
    }

    @Override
    public AjaxResult getAllMergeTaskInfoListByTaskNo(String changeTaskNo) {
        if (changeTaskNo == null || "".equals(changeTaskNo.trim())) {
            return AjaxResult.warn("?????????????????????");
        }
        List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.MERGE_TASK_NO.getName(), changeTaskNo);
        if (list.isEmpty()) {
            return AjaxResult.success();
        } else {
            return AjaxResult.warn("???" + list.size() + "???????????????????????????", list);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult cancelChangeTaskProcess(String changeTaskNo) {
        //????????????????????????????????????????????????
        String loginId = CustomerBizInterceptor.currentUserThread.get().getpId();
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.STATUS.getName(), "");
        param.put(ChangeTaskFieldEnum.INSTANCE_ID.getName(), "");
        param.put(ChangeTaskFieldEnum.CHANGE_NO.getName(), "");
        param.put(ChangeTaskFieldEnum.CHANGE_ID.getName(), "");
        param.put(ChangeTaskFieldEnum.ID.getName(), "");
        query.put(ChangeTaskFieldEnum.EXTRA1.getName(), changeTaskNo);
        Map<String, Object> map = base.selectMap(ChangeTableNameEnum.CHANGE_TASK, param, query);
        String status = map.get(ChangeTaskFieldEnum.STATUS.getName()).toString();
        String instanceId = map.get(ChangeTaskFieldEnum.INSTANCE_ID.getName()).toString();
        String changeNo = map.get(ChangeTaskFieldEnum.CHANGE_NO.getName()).toString();
        String creator = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.CREATOR, ChangeFieldEnum.EXTRA1, changeNo);
        if (loginId.equals(creator) && (ChangeTaskStatusEnum.registered.getName().equals(status) ||
                ChangeTaskStatusEnum.planReviewed.getName().equals(status)||
                ChangeTaskStatusEnum.preApprovaling.getName().equals(status))) {
            AjaxResult result = customerFormService.cancelApply(instanceId);
            String changeId = map.get(ChangeTaskFieldEnum.CHANGE_ID.getName()).toString();
            String changeInstanceId = base.getChangeInstanceIdByChangeId(changeId);
            base.updateChangeSingle(ChangeFieldEnum.ID, changeId, ChangeFieldEnum.PRE_RECODE, "4");
            base.activeChangeSecPartByInstanceId(changeInstanceId);
            String changeTaskId = map.get(ChangeTaskFieldEnum.ID.getName()).toString();
            base.addChangeTaskSysOperateDetail(changeTaskId, "?????????????????????", CustomerBizInterceptor.currentUserThread.get().getUserId());
            List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.MERGE_TASK_NO.getName(), changeTaskNo);
            list.forEach(taskMap -> {
                String iId = taskMap.get(ChangeTaskFieldEnum.INSTANCE_ID.getName()).toString();
                String mergeTaskStatus = taskMap.get(ChangeTaskFieldEnum.STATUS.getName()).toString();
                if (!ChangeTaskStatusEnum.canceled.getName().equals(mergeTaskStatus)) {
                    customerFormService.cancelApply(iId);
                    base.updateChangeTaskApproval(ChangeTaskFieldEnum.INSTANCE_ID, iId, "");
                    base.syncTaskStatusAndCurrentProcessor(ChangeTaskFieldEnum.INSTANCE_ID, iId);
                    String taskId = taskMap.get(ChangeTaskFieldEnum.ID.getName()).toString();
                    base.addChangeTaskSysOperateDetail(taskId, "?????????????????????", CustomerBizInterceptor.currentUserThread.get().getUserId());
                }
            });
            base.updateChangeTaskApproval(ChangeTaskFieldEnum.INSTANCE_ID, instanceId, "");
            base.syncTaskStatusAndCurrentProcessor(ChangeTaskFieldEnum.INSTANCE_ID, instanceId);
            return result;
        } else {
            return AjaxResult.warn("??????????????????????????????????????????????????????");
        }

    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult transferTask(String taskId, String changeTaskNo, CustomerFormContent customerFormContent) {
        String code = "changeTask";
        Task task = processEngine.getTaskService().createTaskQuery() // ??????????????????
                .taskId(taskId) // ????????????id??????
                .singleResult();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        //?????????????????????????????????????????????
        //String status = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.STATUS, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
        String update = "";
        if (ChangeTaskDefineKeyEnum.submitTask.getName().equals(taskDefinitionKey) ||
                ChangeTaskDefineKeyEnum.edit_task.getName().equals(taskDefinitionKey) ||
                ChangeTaskDefineKeyEnum.remedySubmit.getName().equals(taskDefinitionKey)) {
            //???????????????????????????????????????????????????????????????
            update = customerFormContent.getFields().get(ChangeTaskFieldEnum.ASSIGNEE.getName()).toString();
            String old = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.ASSIGNEE, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
            if (old.equals(update)) {
                return AjaxResult.warn("???????????????????????????????????????");
            }
        } else if (ChangeTaskDefineKeyEnum.taskPreApproval.getName().equals(taskDefinitionKey) ||
                ChangeTaskDefineKeyEnum.remedyApproval.getName().equals(taskDefinitionKey)) {
            //???????????????????????????????????????????????????????????????
            update = customerFormContent.getFields().get(ChangeTaskFieldEnum.PRE_APPROVAL.getName()).toString();
            String old = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.PRE_APPROVAL, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
            if (old.equals(update)) {
                return AjaxResult.warn("???????????????????????????????????????");
            }
        } else {
            return AjaxResult.warn("???????????????????????????");
        }
        customerFormService.insertOrUpdate(code, customerFormContent);
        //???????????????assignee
        //String id = String.valueOf(customerFormContent.getFields().get(ChangeTaskFieldEnum.ID.getName()));
        //String assignee = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.ASSIGNEE, ChangeTaskFieldEnum.ID, id);
        taskService.setAssignee(taskId, update);
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult tarnsFerTaskWaitImpl(String taskId, String changeTaskNo, CustomerFormContent
            customerFormContent, String transferMan) {
        if (transferMan == null || "".equals(transferMan)) {
            return AjaxResult.warn("??????????????????");
        }
        String code = "changeTask";
        Task task = processEngine.getTaskService().createTaskQuery() // ??????????????????
                .taskId(taskId) // ????????????id??????
                .singleResult();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        String id = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.ID, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
        OgPerson ogPerson = ogPersonService.selectOgPersonById(transferMan);
        Map<String, Object> map = customerFormContent.getFields();
        map.put("id", id);
        String current = ogPerson.getpName();
        OgUser ogUser = ogUserService.selectOgUserByUserId(transferMan);
        if (ogUser != null) {
            current = current + "(" + ogUser.getUsername() + ")";
        }
        map.put(ChangeTaskFieldEnum.CURRENT_PROCESSOR.getName(), current);
        String dataJson = customerFormContent.getDataJson();
        Object mergeTaskNo = customerFormContent.getFields().get(ChangeTaskFieldEnum.MERGE_TASK_NO.getName());
        if (mergeTaskNo != null && !"".equals(mergeTaskNo.toString())) {
            AjaxResult ajaxResult = base.checkMergeNo(1, mergeTaskNo, changeTaskNo);
            if (Integer.parseInt(ajaxResult.get(AjaxResult.CODE_TAG).toString()) != AjaxResult.Type.SUCCESS.value()) {
                return ajaxResult;
            }
        }
        if (ChangeTaskDefineKeyEnum.receive.getName().equals(taskDefinitionKey)) {
            String implMan = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.IMPL_MAN, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
            if (transferMan.equals(implMan)) {
                return AjaxResult.warn("?????????????????????");
            }
            //??????
            dataJson = VueDataJsonUtil.analysisDataJson(dataJson, map);
            customerFormContent.setDataJson(dataJson);
            customerFormService.insertOrUpdate(code, customerFormContent);
            taskService.setAssignee(taskId, transferMan);
            base.updateChangeTaskApproval(ChangeTaskFieldEnum.ID,id,transferMan);
            //???????????????????????????
            base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.IMPL_MAN, transferMan);
            ImplRecord implRecord = new ImplRecord();
            implRecord.setUserid(transferMan);
            implRecord.setChangeTaskNo(changeTaskNo);
            iImplRecordService.updateImplRecord(implRecord);
            base.addChangeTaskSysOperateDetail(id, "???????????????" + base.getUsernameAndPname(transferMan), implMan);
            return AjaxResult.success();
        } else if (ChangeTaskDefineKeyEnum.taskPreApproval.getName().equals(taskDefinitionKey) ||
                ChangeTaskDefineKeyEnum.remedyApproval.getName().equals(taskDefinitionKey)) {
            //???????????????
            //?????????????????????
            String old = map.get(ChangeTaskFieldEnum.PRE_APPROVAL.getName()).toString();
            if (transferMan.equals(old)) {
                return AjaxResult.warn("?????????????????????");
            }
            map.put(ChangeTaskFieldEnum.PRE_APPROVAL.getName(), transferMan);
            dataJson = VueDataJsonUtil.analysisDataJson(dataJson, map);
            customerFormContent.setDataJson(dataJson);
            customerFormService.insertOrUpdate(code, customerFormContent);
            taskService.setAssignee(taskId, transferMan);
            base.updateChangeTaskApproval(ChangeTaskFieldEnum.ID,id,transferMan);
            ImplRecord implRecord = new ImplRecord();
            implRecord.setUserid(transferMan);
            implRecord.setChangeTaskNo(changeTaskNo);
            iImplRecordService.updateImplRecord(implRecord);
            base.addChangeTaskSysOperateDetail(id, "???????????????" + base.getUsernameAndPname(transferMan), old);
            return AjaxResult.success();
        } else if (ChangeTaskDefineKeyEnum.submitTask.getName().equals(taskDefinitionKey) ||
                ChangeTaskDefineKeyEnum.edit_task.getName().equals(taskDefinitionKey) ||
                ChangeTaskDefineKeyEnum.remedySubmit.getName().equals(taskDefinitionKey)) {
            //???????????????????????????
            String old = map.get(ChangeTaskFieldEnum.ASSIGNEE.getName()).toString();
            if (transferMan.equals(old)) {
                return AjaxResult.warn("?????????????????????");
            }
            map.put(ChangeTaskFieldEnum.ASSIGNEE.getName(), transferMan);
            String orgId = ogPerson.getOrgId();
            map.put(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName(), orgId);
            dataJson = VueDataJsonUtil.analysisDataJson(dataJson, map);
            customerFormContent.setDataJson(dataJson);
            customerFormService.insertOrUpdate(code, customerFormContent);
            taskService.setAssignee(taskId, transferMan);
            base.updateChangeTaskApproval(ChangeTaskFieldEnum.ID,id,transferMan);
            base.addChangeTaskSysOperateDetail(id, "???????????????" + base.getUsernameAndPname(transferMan), old);
            return AjaxResult.success();
        } else {
            return AjaxResult.warn("???????????????????????????");
        }
    }

    @Override
    public AjaxResult getSysDealList(String changeNo) {
        RiskAssessment riskAssessment = riskAssessmentService.selectRiskAssessmentById(changeNo);
        List<String> dataList = new ArrayList<>();
        if (riskAssessment != null) {
            String referSysStr = riskAssessment.getReferSys();
            String[] referSysArray = referSysStr.split(",");
            //TODO ????????????id????????????????????????????????????????????????
        }
        List<Map<String, Object>> colList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("label", "????????????");
        map1.put("val", "referApp");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("label", "??????????????????");
        map2.put("val", "eventCount");
        Map<String, Object> map3 = new HashMap<>();
        map3.put("label", "??????????????????");
        map3.put("val", "problemCount");
        colList.add(map1);
        colList.add(map2);
        colList.add(map3);
        Map<String, Object> result = new HashMap<>();
        result.put("col", colList);
        result.put("list", dataList);
        result.put("total", dataList.size());
        result.put("pageNum", 1);
        result.put("pageSize", 100);
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult getStartDeptMangerData(String startDept) {
        //??????
        //base.getChangeRateList();
        return null;
    }

    @Override
    public AjaxResult updateChangeInfoToAdpm(Map<String, Object> map) {

        return null;
    }

    @Override
    public AjaxResult updateChangeTaskInfoToAdpm(Map<String, Object> map) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult robTask(String changeTaskNo) {
        //?????????????????????????????????????????????????????????????????????1????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????setAssignee?????????????????????????????????????????????????????????
        /*String lockFlag = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.LOCK_FLAG, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
        int flag = Integer.parseInt(lockFlag);*/
        /*if (flag == 0) {*/
        //base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "1");
        String instanceId = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.INSTANCE_ID, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        if (!ChangeTaskDefineKeyEnum.receive.getName().equals(task.getTaskDefinitionKey())) {
            return AjaxResult.warn("???????????????????????????");
        }
        OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
        String assignee = ogUser.getUserId();
        taskService.setAssignee(task.getId(), assignee);
        //???????????????????????????
        base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.IMPL_MAN, assignee);
        OgPerson ogPerson = ogPersonService.selectOgPersonById(assignee);
        String current = ogPerson.getpName() + "(" + ogUser.getUsername() + ")";
        base.updateChangeTaskApproval(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, assignee);
        ImplRecord implRecord = new ImplRecord();
        implRecord.setUserid(assignee);
        implRecord.setChangeTaskNo(changeTaskNo);
        iImplRecordService.updateImplRecord(implRecord);
        String changeTaskId = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.ID, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
        base.addChangeTaskSysOperateDetail(changeTaskId, "?????????" + current + "??????", assignee);
        /*base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");*/
        return AjaxResult.success();
        /*} else {
            return AjaxResult.warn("?????????????????????");
        }*/
    }

    @Override
    public AjaxResult chargePlanTime(String changeTaskNo, String planStartDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime planStartDateTime = LocalDateTime.parse(planStartDate, dateTimeFormatter);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Map<String, Object> params = new HashMap<>();
        params.put(ChangeTaskFieldEnum.REFER_SYS.getName(), "");
        Map<String, String> query = new HashMap<>();
        query.put(ChangeTaskFieldEnum.EXTRA1.getName(), changeTaskNo);
        Map<String, Object> resultMap = base.selectMap(ChangeTableNameEnum.CHANGE_TASK, params, query);
        Object referSys = resultMap.get(ChangeTaskFieldEnum.REFER_SYS.getName());
        if (referSys != null && !"".equals(referSys.toString().trim())) {
            OgSys sysApp = sysApplicationManagerService.selectOgSysBySysCode(referSys.toString().trim());
            String redLine = sysApp.getRedlineTime();
            LocalDateTime redTime = LocalDateTime.of(LocalDate.now(), LocalTime.parse(redLine, timeFormatter));
            if (planStartDateTime.isBefore(redTime)) {
                //??????????????????????????????????????????????????????????????????
                return AjaxResult.warn("?????????????????????????????????????????????????????????[" + sysApp.getCaption() + "]??????????????????" + redLine);
            }
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult setBackToPreApprovalFlag(String changeTaskNo, Integer flag) {
        if (flag != 0 && flag != 1) {
            return AjaxResult.warn("???????????????????????????");
        }
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.BACK_TO_APPROVAL_FLAG.getName(), flag);
        query.put(ChangeTaskFieldEnum.EXTRA1.getName(), changeTaskNo);
        base.update(ChangeTableNameEnum.CHANGE_TASK, param, query);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult setBackToStart(String changeTaskNo, Integer flag) {
        if (flag != 0 && flag != 1) {
            return AjaxResult.warn("???????????????????????????");
        }
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.BACK_TO_START.getName(), flag);
        query.put(ChangeTaskFieldEnum.EXTRA1.getName(), changeTaskNo);
        base.update(ChangeTableNameEnum.CHANGE_TASK, param, query);
        return AjaxResult.success();
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult autoStartDevTaskNew(String changeTaskNo) {
        //??????????????????,????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????
        Map<String, Object> param = new HashMap<>();
        Map<String, String> query = new HashMap<>();
        param.put(ChangeTaskFieldEnum.LOCK_FLAG.getName(), "");
        query.put(ChangeTaskFieldEnum.EXTRA1.getName(), changeTaskNo);
        Integer lock = base.selectObject(ChangeTableNameEnum.CHANGE_TASK, param, query, Integer.class);
        if (lock != 0) {
            return AjaxResult.error("??????????????????");
        } else {
            base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "1");
        }
        String tokenStr = getAutoToken();
        if (tokenStr == null) {
            base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
            return AjaxResult.error("??????token??????");
        }
        String code = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.TYPE, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
        String taskDept = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.TASKDEPT, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
        String sceneName = changeTaskSceneService.getChangeTaskSceneByCode(code).getName();
        ChangeTaskScene scene = changeTaskSceneService.getAutoChangeTaskScene(taskDept, sceneName);
        if (scene == null) {
            base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
            return AjaxResult.warn("????????????????????????");
        }
        code = scene.getCode();
        List<Map<String, Object>> list = getAllFieldValueBySingleCondition(code, "changeTaskNo", changeTaskNo);
        if (list.isEmpty()) {
            base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
            return AjaxResult.warn("????????????????????????");
        }
        List<Map<String, Object>> orders = new ArrayList<>();
        for (Map<String, Object> params : list) {
            Object templateName = params.get("scene_name");
            Object itilNo = params.get("changeTaskNo");
            Object ipList = params.get("iplist");
            if (templateName == null || itilNo == null || ipList == null) {
                base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
                return AjaxResult.warn("???????????????????????????");
            }
            params.remove("id");
            params.remove("scene_name");
            params.remove("changeTaskNo");
            params.remove("iplist");
            params.entrySet().removeIf(entry -> entry.getValue() == null || "".equals(entry.getValue().toString().trim()));
            Map<String, Object> order = new HashMap<>();
            order.put("orderName", templateName);
            order.put("templateType", 8);
            order.put("templateName", templateName);
            order.put("orderSort", 1);
            order.put("params", params);
            String[] ipListArray = ipList.toString().split("\\|");
            List<Map<String, Object>> ipListIncludePort = new ArrayList<>();
            for (String ip : ipListArray) {
                if (!"".equals(ip.trim())) {
                    ip = ip.trim();
                    Map<String, Object> map = new HashMap<>();
                    String[] ipPort = ip.split(":");
                    if (ipPort.length == 2) {
                        map.put("ip", ipPort[0]);
                        map.put("port", Integer.parseInt(ipPort[1]));
                    } else {
                        map.put("ip", ip);
                        map.put("port", 16005);
                    }
                    ipListIncludePort.add(map);
                }
            }
            order.put("computers", ipListIncludePort);
            orders.add(order);
        }
        String url = "/aoms/order/createOrderNew.do";
        JSONObject paramHeader = new JSONObject();
        paramHeader.put("token", tokenStr);
        paramHeader.put("userName", user);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(address).
                path(url).build(true);
        URI uri = uriComponents.toUri();
        JSONObject requestParam = new JSONObject();
        requestParam.put("itilNo", changeTaskNo);
        requestParam.put("createUser", "ideal");
        requestParam.put("platsource", "itsm");
        requestParam.put("orders", orders);
        RequestEntity<JSONObject> requestEntity = RequestEntity.post(uri).
                header("paramsHeader", paramHeader.toJSONString()).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(requestParam);
        System.out.println("body:" + requestParam);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject responseEntityBody = responseEntity.getBody();
        System.out.println("res:" + responseEntityBody);
        Boolean status = responseEntityBody.getBoolean("isOk");
        String message = responseEntityBody.getString("message");
        if (!status) {
            base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
            return AjaxResult.error(message);
        } else {
            return AjaxResult.success(message);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult getAutoStatusNew(String changeTaskNo) {
        String token = getAutoToken();
        if (token == null) {
            AjaxResult.error("??????token??????");
        }
        String url = "/aoms/order/getOrderStatus.do";
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(address).
                path(url).build(true);
        URI uri = uriComponents.toUri();
        JSONObject requestParam = new JSONObject();
        requestParam.put("itilNo", changeTaskNo);
        JSONObject paramHeader = new JSONObject();
        paramHeader.put("token", token);
        paramHeader.put("userName", user);
        RequestEntity<JSONObject> requestEntity = RequestEntity.post(uri).
                header("paramsHeader", paramHeader.toJSONString()).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(requestParam);
        log.info("body:" + requestParam);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject responseEntityBody = responseEntity.getBody();
        log.info("res:" + responseEntityBody);
        Boolean ok = responseEntityBody.getBoolean("isOk");
        String message = responseEntityBody.getString("message");
        if (!ok) {
            return AjaxResult.error("??????????????????");
        }
        Object orders = responseEntityBody.get("orders");
        if (orders == null) {
            return AjaxResult.error("??????????????????");
        }
        JSONArray array = JSON.parseArray(JSON.toJSONString(orders));
        if (array.isEmpty()) {
            return AjaxResult.error("??????????????????");
        }
        List<String> statusList = new ArrayList<>();
        int count = 0;
        for (Object p : array) {
            String istatusdesc = JSON.parseObject(JSON.toJSONString(p)).getString("istatusdesc");
            statusList.add(istatusdesc);
            if ("??????".equals(istatusdesc)) {
                ++count;
            }
        }
        String statusResult = "?????????";
        if (statusList.contains("??????")) {
            statusResult = "??????";
        } else if (count == statusList.size()) {
            statusResult = "??????";
        } else if (statusList.contains("??????")) {
            statusResult = "??????";
        } else if (statusList.contains("?????????")) {
            statusResult = "?????????";
        }
        //?????????????????????
        //??????????????????????????????
        base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.AUTO_TASK_FLAG, statusResult);
        AjaxResult ajaxResult = AjaxResult.success();
        //????????????
        ajaxResult.put("status", statusResult);
        ajaxResult.put(AjaxResult.MSG_TAG, message);
        return ajaxResult;
    }

    @Override
    public AjaxResult autoStartDevTask(String changeTaskNo) {
        //??????????????????,????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????
        String message = "";
        try {
            Map<String, Object> param = new HashMap<>();
            Map<String, String> query = new HashMap<>();
            param.put(ChangeTaskFieldEnum.LOCK_FLAG.getName(), "");
            query.put(ChangeTaskFieldEnum.EXTRA1.getName(), changeTaskNo);
            Integer lock = base.selectObject(ChangeTableNameEnum.CHANGE_TASK, param, query, Integer.class);
            if (lock != 0) {
                return AjaxResult.error("??????????????????");
            } else {
                base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "1");
            }
            /*String tokenStr = getAutoToken();
            if (tokenStr == null) {
                base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
                return AjaxResult.error("??????token??????");
            }*/
            String code = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.TYPE, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
            String taskDept = base.getChangeTaskColumnValueBySingleCondition(ChangeTaskFieldEnum.TASKDEPT, ChangeTaskFieldEnum.EXTRA1, changeTaskNo);
            String sceneName = changeTaskSceneService.getChangeTaskSceneByCode(code).getName();
            ChangeTaskScene scene = changeTaskSceneService.getAutoChangeTaskScene(taskDept, sceneName);
            if (scene == null) {
                base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
                return AjaxResult.warn("????????????????????????");
            }
            code = scene.getCode();
            List<Map<String, Object>> list = getAllFieldValueBySingleCondition(code, "changeTaskNo", changeTaskNo);
            Map<String, Object> params = new HashMap<>();
            if (!list.isEmpty()) {
                params = list.get(0);
            }
            Object templateName = params.get("scene_name");
            Object itilNo = params.get("changeTaskNo");
            Object ipList = params.get("iplist");
            if (templateName == null || itilNo == null || ipList == null) {
                base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
                return AjaxResult.warn("???????????????????????????");
            }
            Map<String, Object> autoParam = new HashMap<>();
            autoParam.put("user", "ideal");
            autoParam.put("passwd", "ideal");
            autoParam.put("templateName", templateName.toString());
            autoParam.put("iplist", ipList.toString());
            autoParam.put("itilNo", itilNo.toString());
            params.remove("scene_name");
            params.remove("changeTaskNo");
            params.remove("iplist");
            params.remove("id");
            autoParam.put("Params", params);
            String jsonString = JSON.toJSONString(autoParam);
            System.out.println(jsonString);
            Map<String, Object> retMap = entegorServer.createItsmToMiddleUsePythonTemplate(jsonString);
            /*String url = address + "/aoms/getModelListInterface.do?user=" + user + "&token=" + tokenStr + "&templateName=" + sceneName;
            ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(url, JSONObject.class);
            JSONObject responseEntityBody = responseEntity.getBody();
            Object dataList = responseEntityBody.get("dataList");
            if (dataList == null) {
                base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
                return AjaxResult.error("???????????????????????????");
            }
            JSONArray array = JSON.parseArray(JSON.toJSONString(dataList));
            if (array.isEmpty()) {
                base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
                return AjaxResult.error("????????????????????????");
            }
            JSONObject data = JSON.parseObject(JSON.toJSONString(array.get(0)));
            String iid = data.getString("iid");
            String type = data.getString("type");
            String iname = data.getString("iname");
            url = address + "/aoms/getZHParamById.do?user=" + user + "&token=" + tokenStr + "&templateModelId=" + iid + "&sysType=" + type;
            responseEntity = restTemplate.getForEntity(url, JSONObject.class);
            responseEntityBody = responseEntity.getBody();
            //dataList = responseEntityBody.get("dataList");
            Object agent = responseEntityBody.get("computers");
            if (*//*dataList == null ||*//* agent == null) {
                base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
                return AjaxResult.error("?????????????????????????????????");
            }
            url = address + "/aoms/order/createOrder.do";
            JSONObject paramHeader = new JSONObject();
            paramHeader.put("token", tokenStr);
            paramHeader.put("userName", user);
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(address).
                    path(url).build(true);
            URI uri = uriComponents.toUri();
            JSONObject requestParam = new JSONObject();
            requestParam.put("itilNo", changeTaskNo);
            requestParam.put("createUser", user);
            requestParam.put("platsource", "itsm");
            List<JSONObject> orders = new ArrayList<>();
            JSONObject order = new JSONObject();
            order.put("orderName", sceneName);
            order.put("templateType", type);
            order.put("templateName", iname);
            order.put("templateId", iid);
            order.put("orderSort", 1);
            order.put("iswhite", false);
            List<Map<String, Object>> list = getAllFieldValueBySingleCondition(code, "changeTaskNo", changeTaskNo);
            Map<String,Object> params = new HashMap<>();
            if(!list.isEmpty()){
                params = list.get(0);
            }
            //TODO params?????????????????????
            order.put("params", params);
            orders.add(order);
            requestParam.put("orders", orders);
            requestParam.put("computers", agent);
            RequestEntity<JSONObject> requestEntity = RequestEntity.post(uri).
                    header("paramsHeader", paramHeader.toJSONString()).
                    accept(MediaType.APPLICATION_JSON).
                    contentType(MediaType.APPLICATION_JSON).
                    body(requestParam);
            responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
            responseEntityBody = responseEntity.getBody();
            Boolean flag = responseEntityBody.getBoolean("success");
            message = responseEntityBody.getString("message");*/
            String status = retMap.get("status").toString();
            message = retMap.get("message").toString();
            if (!"0".equals(status)) {
                base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
                return AjaxResult.error(message);
            }
        } catch (Exception e) {
            base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1, changeTaskNo, ChangeTaskFieldEnum.LOCK_FLAG, "0");
            e.printStackTrace();
            return AjaxResult.error(e.toString());
        }
        return AjaxResult.success(message);
    }

    @Override
    public AjaxResult getAutoStatus(String changeTaskNo) {
        /*String token = getAutoToken();
        if (token == null) {
            AjaxResult.error("??????token??????");
        }
        String url = "/aoms/order/getOrderStatus.do";
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(address).
                path(url).build(true);
        URI uri = uriComponents.toUri();
        JSONObject requestParam = new JSONObject();
        requestParam.put("itilNo", changeTaskNo);
        JSONObject paramHeader = new JSONObject();
        paramHeader.put("token", token);
        paramHeader.put("userName", user);
        RequestEntity<JSONObject> requestEntity = RequestEntity.post(uri).
                header("paramsHeader", paramHeader.toJSONString()).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(requestParam);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject responseEntityBody = responseEntity.getBody();
        Object orders = responseEntityBody.get("orders");
        if (orders == null) {
            return AjaxResult.error("??????????????????");
        }
        JSONArray array = JSON.parseArray(JSON.toJSONString(orders));
        if (array.isEmpty()) {
            return AjaxResult.error("??????????????????");
        }
        String status = JSON.parseObject(JSON.toJSONString(array.get(0))).getString("istatusdesc");*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itilNo", changeTaskNo);
        jsonObject.put("user", "ideal");
        jsonObject.put("passwd", "ideal");
        String jsonStr = JSON.toJSONString(jsonObject);
        Map<String, Object> result = entegorServer.queryShItsmToMiddleUsePythonTemplate(jsonStr);
        String status = result.get("status").toString();
        if (!"0".equals(status)) {
            return AjaxResult.warn(result.get("message").toString());
        } else {
            //?????????????????????json
            Object message = result.get("retJson");
            //System.out.println(message);
            /*JSONObject ordersObj = JSON.parseObject(JSON.toJSONString(message));
            Object orders = ordersObj.get("orders");
            if (orders == null) {
                return AjaxResult.error("??????????????????");
            }
            JSONArray array = JSON.parseArray(JSON.toJSONString(orders));
            if (array.isEmpty()) {
                return AjaxResult.error("??????????????????");
            }
            List<String> statusList = new ArrayList<>();
            int count = 0;
            for(Object p:array){
                String  istatusdesc =  JSON.parseObject(JSON.toJSONString(p)).getString("istatusdesc");
                statusList.add(istatusdesc);
                if("??????".equals(istatusdesc)){
                    ++count;
                }
            }
            String statusResult = "?????????";
            if(statusList.contains("??????")){
                statusResult = "??????";
            }else if(*//*??????????????????*//*count==statusList.size()){
                statusResult = "??????";
            }else if(*//*??????????????????????????????*//*statusList.contains("??????")){
                statusResult = "??????";
            }else if(statusList.contains("?????????")){
                statusResult = "?????????";
            }
            //?????????????????????
            //??????????????????????????????
            base.updateChangeTaskSingle(ChangeTaskFieldEnum.EXTRA1,changeTaskNo,ChangeTaskFieldEnum.AUTO_TASK_FLAG,statusResult);*/
            AjaxResult ajaxResult = AjaxResult.success();
            //????????????
            String statusResult = "";
            ajaxResult.put("status", statusResult);
            ajaxResult.put(AjaxResult.MSG_TAG, message);
            return ajaxResult;
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult adjustRiskLevel(RiskAccessRecord riskAccessRecord) {
        String changeNo = riskAccessRecord.getChangeNo();
        String currentRiskLevelStr = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.CURRENT_RISK_LEVEL, ChangeFieldEnum.EXTRA1, changeNo);
        if (currentRiskLevelStr == null || "".equals(currentRiskLevelStr.trim())) {
            return AjaxResult.warn("????????????????????????");
        }
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        String currentUserId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        String changeManagerUserId = base.getChangeMangerUserId();
        String initLevel = riskAccessRecord.getInitLevel();
        String currentLevel = riskAccessRecord.getCurrentLevel();
        PubParaValue initLevelValue = pubParaValueService.selectPubParaValue("change_risk_level", initLevel);
        PubParaValue currentLevelValue = pubParaValueService.selectPubParaValue("change_risk_level", currentLevel);
        riskAccessRecord.setInitLevel(initLevelValue.getValueDetail());
        riskAccessRecord.setCurrentLevel(currentLevelValue.getValueDetail());
        Integer updateRiskLevel = Integer.parseInt(currentLevel);
        Integer currentRiskLevel = Integer.parseInt(currentRiskLevelStr);
        if (updateRiskLevel.equals(currentRiskLevel)) {
            return AjaxResult.warn("?????????????????????????????????????????????");
        }
        String type = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.TYPE, ChangeFieldEnum.EXTRA1, changeNo);
        if ("1".equals(type) /*&& updateRiskLevel != 1*/) {
            return AjaxResult.warn("????????????????????????????????????");
        }
        String deputyUserId = base.getVaildDeputyUserId(changeManagerUserId);
        if (!changeManagerUserId.equals(currentUserId)&&!currentUserId.equals(deputyUserId)) {
            if (updateRiskLevel < currentRiskLevel) {
                return AjaxResult.warn("?????????????????????????????????????????????");
            }
        }
        base.updateChangeSingle(ChangeFieldEnum.EXTRA1, changeNo, ChangeFieldEnum.CURRENT_RISK_LEVEL, updateRiskLevel.toString());
        RiskAssessment riskAssessment = new RiskAssessment();
        riskAssessment.setCurrentLevel(currentLevel);
        riskAssessment.setFormId(changeNo);
        riskAssessment.setRiskLevel(Integer.valueOf(initLevel));
        riskAssessmentService.updateCurrentLevel(currentLevel, changeNo);
        riskAccessRecord.setOperator(ogPersonService.selectOgPersonById(currentUserId).getpName());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        riskAccessRecord.setOperateTime(dateTimeFormatter.format(LocalDateTime.now()));
        riskAccessRecordService.insertRiskAccessRecord(riskAccessRecord);
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult addOrUpdateSceneData(String code, Map<String, Object> fields) {
        //????????????????????????
        Integer count = base.tableExist(code);
        if (count == 0) {
            return AjaxResult.warn("???????????????");
        }
        Object changeTaskNo = fields.get("changeTaskNo");
        if (changeTaskNo == null || "".equals(changeTaskNo.toString().trim())) {
            return AjaxResult.warn("??????????????????????????????????????????");
        }
        Map<String, String> query = new HashMap<>();
        String changeTaskNoStr = String.valueOf(changeTaskNo);
        query.put("changeTaskNo", changeTaskNoStr);
        Integer tableCount = base.getCount(code, query);
        if (tableCount == 0) {
            base.insertARowByTableName(code, fields);
        } else {
            fields.remove("changeTaskNo");
            base.updateByTableName(code, fields, query);
        }
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult addARowSceneData(String code, Map<String, Object> fields) {
        //????????????????????????
        Integer count = base.tableExist(code);
        if (count == 0) {
            return AjaxResult.warn("???????????????");
        }
        Object changeTaskNo = fields.get("changeTaskNo");
        if (changeTaskNo == null || "".equals(changeTaskNo.toString().trim())) {
            return AjaxResult.warn("??????????????????????????????????????????");
        }
        base.insertARowByTableName(code, fields);
        return AjaxResult.success();
    }
    @Override
    public AjaxResult delSceneDataById(String code, String id) {
        Integer count = base.tableExist(code);
        if (count == 0) {
            return AjaxResult.warn("???????????????" + code);
        }
        MybatisPlusConfig.customerTableName.set(code);
        customerFormMapper.delete(Wrappers.<CustomerFormContent>query().select("*").eq("id", id));
        MybatisPlusConfig.customerTableName.remove();
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AjaxResult syncSceneDataToAuto(String code, String changeTaskNo) {
        ChangeTaskScene changeTaskScene = changeTaskSceneService.getChangeTaskSceneByCode(code);
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (changeTaskScene.getAutoFlag() == 0) {
            String sceneName = changeTaskScene.getName();
            String orgId = changeTaskScene.getOrgid();
            ChangeTaskScene scene = changeTaskSceneService.getAutoChangeTaskScene(orgId, sceneName);
            if (scene != null) {
                Integer autoCount = base.tableExist(code);
                if (autoCount != 0) {
                    String sceneCode = scene.getCode();
                    List<Map<String, Object>> fieldListMap = base.getTableFieldMap(sceneCode);
                    List<Map<String, Object>> valueList = getAllFieldValueBySingleCondition(code, "changeTaskNo", changeTaskNo);
                    Map<String, Object> fields;
                    Map<String, Object> fieldMap = new HashMap<>();
                    fieldListMap.forEach(p -> {
                        String field = p.get("column_name").toString();
                        if(!(field.equals("id")||field.equals("status")||field.equals("updated_by")||
                                field.equals("instance_id")||field.equals("extra1")||
                                field.equals("extra2")||field.equals("extra3")||field.equals("extra4")||
                                field.equals("extra5")||field.equals("created_by")||field.equals("created_time")
                        ||field.equals("updated_time"))){
                            fieldMap.put(field, "");
                        }
                    });
                    if (valueList.size()==1) {
                        fields = valueList.get(0);
                        fieldMap.forEach((key, value) -> {
                            fieldMap.put(key, fields.get(key));
                        });
                        Map<String, String> query = new HashMap<>();
                        query.put("changeTaskNo", changeTaskNo);
                        Integer autoTableCount = base.getCount(sceneCode, query);
                        if (autoTableCount == 0) {
                            fieldMap.put("changeTaskNo", changeTaskNo);
                            base.insertARowByTableName(sceneCode, fieldMap);
                        } else {
                            fieldMap.remove("changeTaskNo");
                            base.updateByTableName(sceneCode, fieldMap, query);
                        }
                    } else if(valueList.size()>1){
                        // ???????????????????????????????????????
                        Map<String, String> query = new HashMap<>();
                        query.put("changeTaskNo", changeTaskNo);
                        base.delByCondition(sceneCode,query);
                        List<Map<String,Object>> multiData = new ArrayList<>();
                        valueList.forEach(map->{
                            Map<String,Object> data = new HashMap<>();
                            fieldMap.forEach((key, value) -> {
                                String val = "";
                                Object o = map.get(key);
                                if(o!=null){
                                    val = o.toString();
                                }
                                data.put(key, val);
                            });
                            multiData.add(data);
                        });
                        base.batchInsert(sceneCode,fieldMap,multiData);
                    } else {
                        return AjaxResult.warn("?????????????????????");
                    }
                }
            }
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult completeByPhone(ECompleteVO eCompleteVO) {
        String userCode = eCompleteVO.getUserCode();
        Map<String, Object> map = new HashMap<>();
        map.put("username", userCode);
        OgUser ogUser = ogUserService.selectUserByLoginName(map);
        if (ogUser == null) {
            return AjaxResult.warn("???????????????");
        }
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        String userId = ogUser.getUserId();
        String changeNo = eCompleteVO.getChangeNo();
        String instanceId = eCompleteVO.getInstanceId();
        if (instanceId == null||"".equals(instanceId.trim())) {
            return AjaxResult.warn("???????????????");
        }
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).taskCandidateOrAssigned(userId).singleResult();
        if (task == null) {
            return AjaxResult.warn("??????????????????");
        }
        String taskId = task.getId();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        AtomicReference<AjaxResult> result = new AtomicReference<>(AjaxResult.success());
        transactionTemplate.execute((status)->{
            try{
                if(task.getAssignee()==null){
                    taskService.claim(taskId,userId);
                }
                String changeId = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.ID, ChangeFieldEnum.EXTRA1, changeNo);
                if (ChangeDefineKeyEnum.changeMangerApproval.getName().equals(taskDefinitionKey)) {
                    Integer recode = Integer.valueOf(eCompleteVO.getRecode());
                    if (recode == 3) {
                        //????????????????????????????????????????????????
                        Integer count = base.getTaskCountByChangeIdAndTaskColumn(changeId, ChangeTaskFieldEnum.BACK_TO_APPROVAL_FLAG, "1");
                        if (count == 0) {
                            throw new Exception("???????????????????????????????????????");
                        }
                    }
                }//?????????????????????????????????????????????????????????????????????????????????????????????
                else if (ChangeDefineKeyEnum.adminApproval.getName().equals(taskDefinitionKey)) {
                    Integer recode = Integer.parseInt(eCompleteVO.getRecode());
                    List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).list();
                    if (recode == 1) {
                        List<String> deputyList = base.getAdminDeputyIdList(userId);
                        deputyList.add(userId);
                        List<String> finalDeputyList = deputyList.stream().distinct().collect(Collectors.toList());
                        List<String> users = new ArrayList<>();
                        list.forEach(p -> {
                            String current = p.getAssignee();
                            if (!finalDeputyList.contains(current)){
                                users.add(current);
                            }else if(!userId.equals(current)){
                                taskService.complete(p.getId());
                            }
                        });
                        base.updateAdminCurrentProcessor(users, changeId);
                    } else if (recode == 0) {
                        list.forEach(p -> {
                            String id = p.getId();
                            if (!taskId.equals(id)) {
                                taskService.complete(id);
                            }
                        });
                        ChangeNodeCompleteListener.map.put(changeId, 0);
                    }
                }
                Map<String, Object> param = new HashMap<>();
                String recode = eCompleteVO.getRecode();
                if (ChangeDefineKeyEnum.changeMangerApprovalWhenBranch.getName().equals(taskDefinitionKey)) {
                    if ("1".equals(recode)) {
                        List<String> adminOrgList = eCompleteVO.getAdminOrgList();
                        if (adminOrgList.isEmpty()) {
                            throw new Exception("?????????????????????");
                        }
                        param.put(ADMIN_ORG_LIST, adminOrgList);
                    }
                }
                param.put("recode", recode);
                String content = eCompleteVO.getContent();
                content = "??????????????? "+content;
                taskService.addComment(taskId, instanceId, content);
                taskService.complete(taskId, param);
                String chaneId = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.ID, ChangeFieldEnum.EXTRA1, changeNo);
                base.addChangeSysOperateDetail(chaneId, content, userId);
                return Boolean.TRUE;
            }catch (Exception t){
                status.setRollbackOnly();
                result.set(AjaxResult.warn(t.getMessage()));
                return Boolean.FALSE;
            }
        });
        ChangeUtil.ADPM_POOL.submit(instanceId,()->{
            base.updateAdpmChange(instanceId);
        });
        return result.get();
    }

    @Override
    public AjaxResult processData(String changeNo) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
        String year = dateTimeFormatter.format(now);
        String creator = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.CREATOR, ChangeFieldEnum.EXTRA1, changeNo);
        OgPerson starter = ogPersonService.selectOgPersonById(creator);
        return base.getChangeRateList(year, starter.getOrgId());
    }

    @Override
    public AjaxResult getButtonList(String currentId, String changeNo) {
        Map<String, Object> result = new HashMap<>();
        String changeMangerId = base.getChangeMangerUserId();
        List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE, ChangeFieldEnum.EXTRA1.getName(), changeNo);
        Map<String, Object> map = list.get(0);
        String instanceId = map.get(ChangeFieldEnum.INSTANCE_ID.getName()).toString();
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId).taskCandidateOrAssigned(changeMangerId).list();
        Task task = null;
        if(!taskList.isEmpty()){
            task = taskList.get(0);
        }
        String deputyUserId = base.getVaildDeputyUserId(changeMangerId);
        if (task != null && (changeMangerId.equals(currentId)||currentId.equals(deputyUserId)) && ChangeDefineKeyEnum.changeMangerApproval.getName().equals(task.getTaskDefinitionKey())) {
            //????????????????????????????????????
            List<Map<String, Object>> buttonList = new ArrayList<>();
            Map<String, Object> switchButtonMap = new HashMap<>();
            switchButtonMap.put("buttonName", "????????????");
            switchButtonMap.put("buttonUrl", "/customerForm/change/task/back/preApproval");
            switchButtonMap.put("buttonMethod", "POST");
            switchButtonMap.put("buttonUrlParams", new String[]{"changeTaskNo", "flag"});
            switchButtonMap.put("buttonBodyParams", null);
            Map<String, Object> switchButtonMap2 = new HashMap<>();
            switchButtonMap2.put("buttonName", "???????????????");
            switchButtonMap2.put("buttonUrl", "/customerForm/change/task/back/start");
            switchButtonMap2.put("buttonMethod", "POST");
            switchButtonMap2.put("buttonUrlParams", new String[]{"changeTaskNo", "flag"});
            switchButtonMap2.put("buttonBodyParams", null);
            buttonList.add(switchButtonMap);
            buttonList.add(switchButtonMap2);
            result.put("switchButtonList", buttonList);
        }
        //??????????????????????????????????????????????????????????????????????????????
        String status = map.get(ChangeFieldEnum.STATUS.getName()).toString();
        String creator = map.get(ChangeFieldEnum.CREATOR.getName()).toString();
        if ((ChangeStatusEnum.unSubmit.getName().equals(status)||ChangeStatusEnum.preApproval.getName().equals(status))
            && creator.equals(currentId)) {
            //?????????????????????
            List<Map<String, Object>> buttonList = new ArrayList<>();
            Map<String, Object> button = new HashMap<>();
            button.put("buttonName", "??????");
            button.put("buttonUrlPath", "/customerForm/change/task/cancel");
            buttonList.add(button);
            result.put("actionButtonList", buttonList);
        }
        return AjaxResult.success(result);
    }

    public String getAutoToken() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(address).
                path("/aoms/token/getToken.do").build(true);
        URI uri = uriComponents.toUri();
        JSONObject requestParam = new JSONObject();
        requestParam.put("userName", user);
        requestParam.put("userPwd", password);
        RequestEntity<JSONObject> requestEntity = RequestEntity.post(uri).
                header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8").
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(requestParam);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject responseEntityBody = responseEntity.getBody();
        Object token = responseEntityBody.get("token");
        if (token == null) {
            return null;
        }
        return token.toString();
    }

    @Bean("autoRestTemplate")
    public RestTemplate autoRestTemplate
            (@Qualifier(value = "simpleClientHttpRequestFactory") ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean("simpleClientHttpRequestFactory")
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(15000);
        // ????????????
        //factory.setProxy(null);
        return factory;
    }
}
