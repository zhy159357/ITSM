package com.ruoyi.form.openapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.domain.ChangeTaskScene;
import com.ruoyi.form.domain.CustomerFormListDTO;
import com.ruoyi.form.domain.RiskAccessRecord;
import com.ruoyi.form.enums.*;
import com.ruoyi.form.openapi.vo.*;
import com.ruoyi.form.service.CustomerFormService;
import com.ruoyi.form.service.IChangeService;
import com.ruoyi.form.service.IChangeTaskSceneService;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/change")
@Api(tags = "变更单对接外部模块接口")
public class EsbChangeController {

    @Autowired
    IChangeService changeService;
    @Autowired
    IOgUserService ogUserService;
    @Autowired
    CustomerFormService customerFormService;
    @Autowired
    Base base;
    @Autowired
    TaskService taskService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    IChangeTaskSceneService changeTaskSceneService;
    @Autowired
    ISysApplicationManagerService sysApplicationManagerService;
    @Autowired
    IOgPersonService ogPersonService;
    @Autowired
    ISysDeptService sysDeptService;
    @Autowired
    IPubParaValueService pubParaValueService;

    @PostMapping(value = "/create")
    @ApiOperation("adpm发起变更单")
    public AjaxResult createChange(@RequestBody AdpmChangeVO adpmChangeVO) {
        List<AdpmChangeTaskVO> taskContent = adpmChangeVO.getTaskContent();
        for (AdpmChangeTaskVO changeTask : taskContent) {
            //做一下校验，看所涉系统是否都有，没有的话就返回错误
            String referSys = changeTask.getReferSys();
            OgSys ogSys = sysApplicationManagerService.selectOgSysBySysId(referSys);
            if (ogSys == null) {
                return AjaxResult.warn("ERROR:应用系统【" + referSys + "】不存在，请联系管理员添加");
            }
        }
        List<AdpmBasisVO> basis = adpmChangeVO.getBasis();
        StringJoiner stringJoiner = new StringJoiner(";\n");
        for (AdpmBasisVO basisVO : basis) {
            //将变更依据拼接成字符串
            stringJoiner.add("需求编号：" + basisVO.getBrCode());
            stringJoiner.add("需求名称：" + basisVO.getBrName());
            stringJoiner.add("需求发起子部门：" + basisVO.getBrProposerDept());
            stringJoiner.add("需求发起一级部门：" + basisVO.getBrProposerSupDept());
            stringJoiner.add("牵头部室：" + basisVO.getBrDept());
            stringJoiner.add("需求负责人：" + basisVO.getBrChief());
            stringJoiner.add("批次号：" + basisVO.getBatchNum());
            stringJoiner.add("计划投产日期：" + basisVO.getDeployDate());
        }
        String basisStr = stringJoiner.toString();
        String userCode = adpmChangeVO.getCurrentUserCode();
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("username", userCode);
        OgUser ogUser = ogUserService.selectUserByLoginName(loginMap);
        if (ogUser == null) {
            return AjaxResult.warn("工号不存在");
        }
        ogUser = ogUserService.selectOgUserByUserId(ogUser.getpId());
        String userId = ogUser.getpId();
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        Map<String, Object> beanMap = (Map<String, Object>) BeanMap.create(adpmChangeVO);
        Map<String, Object> map = new HashMap<>();
        beanMap.entrySet().forEach(p -> {
            String key = p.getKey();
            if (!"taskContent".equals(key) && !"currentUserCode".equals(key)) {
                Object value = p.getValue();
                if ("basis".equals(key)) {
                    value = basisStr;
                }
                map.put(key, value);
            }
        });
        List<Map<String, Object>> list = new ArrayList<>();
        taskContent.forEach(task -> {
            BeanMap taskMap = BeanMap.create(task);
            list.add(taskMap);
        });
        return changeService.initByInterface(userId, map, list);
    }

    @PostMapping(value = "/lock")
    @ApiOperation("锁定或/解锁变更单")
    public AjaxResult lockChange(@RequestBody AdpmLockVO adpmLockVO) {
        String changeNo = adpmLockVO.getChangeNo();
        String flag = adpmLockVO.getOperateType();
        String sysId = adpmLockVO.getSysId();
        if ("1".equals(flag)) {
            //解锁
            base.unLockChange(changeNo, sysId);
            if (!base.changeLock(changeNo)) {
                //激活所有单子
                return base.activeAllProcess(changeNo);
            } else {
                return AjaxResult.success("解锁成功");
            }
        } else if ("2".equals(flag)) {
            //加锁
            //挂起所有单子
            base.lockChange(changeNo, sysId);
            return base.suspendAllProcess(changeNo);
        } else {
            return AjaxResult.warn("请输入正确的操作类型");
        }
    }


    @PostMapping(value = "/status")
    @ApiOperation("查询变更单状态和完成时间")
    public AjaxResult getChangeStatusAndOverTime(@RequestBody List<String> changeNoList) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<PubParaValue> implResult = pubParaValueService.selectPubParaValueByParaName("change_task_impl_result");
        Map<String, Object> implResultMap = new HashMap<>();
        implResult.forEach(p -> {
            implResultMap.put(p.getValueDetail(), p.getValue());
        });
        List<PubParaValue> changeStatus = pubParaValueService.selectPubParaValueByParaName("change_status");
        Map<String, Object> changeStatusMap = new HashMap<>();
        changeStatus.forEach(p -> {
            changeStatusMap.put(p.getValueDetail(), p.getValue());
        });
        for (String changeNo : changeNoList) {
            List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE, ChangeFieldEnum.EXTRA1.getName(), changeNo);
            if (!list.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put(ChangeFieldEnum.CHANGE_NO.getName(), changeNo);
                Map<String, Object> map = list.get(0);
                Object closeCodeObj = map.get(ChangeFieldEnum.CLOSE_CODE.getName());
                String closeCode = "";
                if(closeCodeObj!=null&&!"".equals(closeCodeObj.toString().trim())){
                    closeCode = closeCodeObj.toString().trim();
                }
                Object changeStatusObj = map.get(ChangeFieldEnum.CHANGE_STATUS.getName());
                String changeStatusStr = "";
                if(changeStatusObj!=null&&!"".equals(changeStatusObj.toString().trim())){
                    changeStatusStr = changeStatusObj.toString().trim();
                }
                data.put(ChangeFieldEnum.CLOSE_CODE.getName(), implResultMap.get(closeCode));
                data.put(ChangeFieldEnum.CHANGE_STATUS.getName(), changeStatusMap.get(changeStatusStr));
                data.put(ChangeFieldEnum.CHANGE_FINISH_TIME.getName(), map.get(ChangeFieldEnum.CHANGE_FINISH_TIME.getName()));
                data.put(ChangeFieldEnum.CHANGE_CLOSED_TIME.getName(), map.get(ChangeFieldEnum.CHANGE_CLOSED_TIME.getName()));
                data.put("maxChildActualFinishTime", map.get(ChangeFieldEnum.CHANGE_FINISH_TIME.getName()));
                resultList.add(data);
            }
        }
        AjaxResult result = AjaxResult.success();
        result.put("resData", resultList);
        return result;
    }

    @PostMapping(value = "/task")
    @ApiOperation("查询变更单的子任务信息")
    public AjaxResult getChangeTaskListAndOverTime(@RequestBody JSONObject jsonObject) {
        Object changeNoObj = jsonObject.get("changeNo");
        if (changeNoObj == null) {
            return AjaxResult.warn("请传入变更单号");
        }
        String changeNo = changeNoObj.toString();
        List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        if (list.isEmpty()) {
            return AjaxResult.warn("未找到该变更单的任务单，请查看变更单号是否正确");
        }
        List<PubParaValue> changeTaskStatus = pubParaValueService.selectPubParaValueByParaName("change_task_status");
        Map<String, Object> changeTaskStatusMap = new HashMap<>();
        changeTaskStatus.forEach(p -> {
            changeTaskStatusMap.put(p.getValueDetail(), p.getValue());
        });
        list.forEach(p->{
            Object sys = p.get(ChangeTaskFieldEnum.REFER_SYS.getName());
            if(sys!=null&&!"".equals(sys.toString().trim())){
                OgSys ogSys = sysApplicationManagerService.selectOgSysBySysCode(sys.toString());
                if(ogSys!=null){
                    p.put(ChangeTaskFieldEnum.REFER_SYS.getName(),ogSys.getSysId());
                }
            }
            Object status = p.get(ChangeTaskFieldEnum.TASK_STATUS.getName());
            if(status!=null&&!"".equals(status.toString().trim())){
                p.put(ChangeTaskFieldEnum.TASK_STATUS.getName(),changeTaskStatusMap.get(status.toString().trim()));
            }
        });
        AjaxResult result = AjaxResult.success();
        result.put("resData", list);
        return result;
    }

    @GetMapping(value = "/mobile/info")
    @ApiOperation("E事通查看变更单信息")
    public AjaxResult getChangeInfo(@RequestParam("changeNo") String changeNo) {
        List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE, ChangeFieldEnum.EXTRA1.getName(), changeNo);
        if (list.isEmpty()) {
            return AjaxResult.warn("未找到该变更单,请查看变更单号是否正确");
        }
        Map<String, Object> result = list.get(0);
        return AjaxResult.success(result);
    }

    @GetMapping(value = "/mobile/work/detail")
    @ApiOperation("E事通查看变更单工作详情")
    public AjaxResult getChangeOperateInfo(@RequestParam("changeNo") String changeNo, @RequestParam("pageNum") String pageNum, @RequestParam("pageSize") String pageSize) {
        Integer num = Integer.parseInt(pageNum);
        Integer size = Integer.parseInt(pageSize);
        Page page = new Page(num, size);
        return customerFormService.getOperationDetails(changeNo, page);
    }

    @GetMapping(value = "/mobile/tasklist")
    @ApiOperation("E事通查看变更单的任务单列表信息")
    public AjaxResult getChangeTaskInfo(@RequestParam("changeNo") String changeNo) {
        List<Map<String, Object>> list = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.CHANGE_NO.getName(), changeNo);
        if (list.isEmpty()) {
            return AjaxResult.warn("未找到该变更单的任务单，请查看变更单号是否正确");
        }
        list.forEach(map -> {
            String type = map.get(ChangeTaskFieldEnum.TYPE.getName()).toString();
            Object referSys = map.get(ChangeTaskFieldEnum.REFER_SYS.getName());
            if (referSys != null && !"".equals(referSys.toString())) {
                OgSys ogSys = sysApplicationManagerService.selectOgSysBySysCode(referSys.toString());
                if (ogSys != null) {
                    map.put(ChangeTaskFieldEnum.REFER_SYS.getName(), ogSys.getCaption() + "(" + ogSys.getCode() + ")");
                }
            }
            ChangeTaskScene changeTaskScene = changeTaskSceneService.getChangeTaskSceneByCode(type);
            map.put(ChangeTaskFieldEnum.TYPE.getName(), changeTaskScene.getName());
            String preApproval = map.get(ChangeTaskFieldEnum.PRE_APPROVAL.getName()).toString();
            String assigneeGroup = map.get(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName()).toString();
            String assignee = map.get(ChangeTaskFieldEnum.ASSIGNEE.getName()).toString();
            OgPerson pre = ogPersonService.selectOgPersonById(preApproval);
            OgUser preUser = ogUserService.selectOgUserByUserId(preApproval);
            OgPerson ass = ogPersonService.selectOgPersonById(assignee);
            OgUser assUser = ogUserService.selectOgUserByUserId(assignee);
            map.put(ChangeTaskFieldEnum.PRE_APPROVAL.getName(), pre.getpName() + "(" + preUser.getUsername() + ")");
            map.put(ChangeTaskFieldEnum.ASSIGNEE.getName(), ass.getpName() + "(" + assUser.getUsername() + ")");
            OgOrg ogOrg = sysDeptService.selectDeptById(assigneeGroup);
            if (ogOrg.getParentName() == null) {
                map.put(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName(), ogOrg.getOrgName());
            } else {
                map.put(ChangeTaskFieldEnum.ASSIGNEE_GROUP.getName(), ogOrg.getParentName() + "-" + ogOrg.getOrgName());
            }
        });
        return AjaxResult.success(list);
    }

    @PostMapping(value = "/mobile/list")
    @ApiOperation("E事通查看变更单列表")
    public AjaxResult getChangeList(@RequestBody EChangeVO eChangeVO) {
        String type = eChangeVO.getType();
        String userNo = eChangeVO.getUserNo();
        Map<String, Object> map = new HashMap<>();
        map.put("username", userNo);
        OgUser ogUser = ogUserService.selectUserByLoginName(map);
        if (ogUser == null) {
            return AjaxResult.warn("工号不存在");
        }
        ogUser = ogUserService.selectOgUserByUserId(ogUser.getpId());
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        String code = Base.CHANGE;
        Page page = new Page(eChangeVO.getPageNum(), eChangeVO.getPageSize());
        CustomerFormListDTO customerFormListDTO = new CustomerFormListDTO();
        customerFormListDTO.setSearchDTOList(new ArrayList<>());
        AjaxResult listResult = customerFormService.list(code, type, page, customerFormListDTO);
        Map<String, Object> data = (Map<String, Object>) listResult.get("data");
        Object info = data.get("pageListInfo");
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(info));
        List<Map<String, Object>> list = (List<Map<String, Object>>) jsonObject.get("records");
        if("2".equals(type)){
            String userId = ogUser.getUserId();
            String changeMangerId = base.getChangeMangerUserId();
            String deputyId = base.getVaildDeputyUserId(changeMangerId);
            if(!changeMangerId.equals(userId)&&!userId.equals(deputyId)){
                list = list.stream().filter(p-> !ChangeStatusEnum.prepared.getName().equals(p.get(ChangeFieldEnum.STATUS.getName()))).collect(Collectors.toList());
            }
            list = list.stream().filter(p->{
                Object status = p.get(ChangeFieldEnum.STATUS.getName());
                return status!=null&&!"".equals(status.toString())
                        &&!ChangeStatusEnum.unSubmit.getName().equals(status.toString())
                        &&!ChangeStatusEnum.branchManagerApproval.getName().equals(status.toString())
                        &&!ChangeStatusEnum.branchGeneralApproval.getName().equals(status.toString())
                        &&!ChangeStatusEnum.cancel.getName().equals(status.toString());
            }).collect(Collectors.toList());
        }
        //时间匹配
        String startDate = eChangeVO.getStartDate();
        String endDate = eChangeVO.getEndDate();
        if ((startDate != null && !"".equals(startDate)) || (endDate != null && !"".equals(endDate))) {
            List<Map<String, Object>> resultList = new ArrayList<>();
            List<String> changeNoList = base.getChangeNoListByCreateTime(startDate, endDate);
            list.forEach(p -> {
                String changeNo = p.get("changeNo").toString();
                if (changeNoList.contains(changeNo)) {
                    resultList.add(p);
                }
            });
            jsonObject.put("records", resultList);
        }
        return AjaxResult.success(jsonObject);
    }

    @PostMapping(value = "/mobile/complete")
    @ApiOperation("E事通行政审批审批变更单")
    public AjaxResult eComplete(@RequestBody ECompleteVO eCompleteVO) {
        String changeNo = eCompleteVO.getChangeNo();
        String instanceId = base.getChangeColumnValueBySingleCondition(ChangeFieldEnum.INSTANCE_ID, ChangeFieldEnum.EXTRA1, changeNo);
        if (instanceId == null) {
            return AjaxResult.warn("变更单号异常！");
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        if (processInstance.isSuspended()) {
            return AjaxResult.warn("该变更已锁定");
        }
        eCompleteVO.setInstanceId(instanceId);
        return changeService.completeByPhone(eCompleteVO);
    }

    @PostMapping("/adjust/risklevel")
    @ApiOperation("调整风险级别")
    public AjaxResult adjustRiskLevel(@RequestHeader("userCode") String userCode, @RequestBody RiskAccessRecord riskAccessRecord) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", userCode);
        OgUser ogUser = ogUserService.selectUserByLoginName(map);
        if (ogUser == null) {
            return AjaxResult.warn("工号不存在");
        }
        ogUser = ogUserService.selectOgUserByUserId(ogUser.getpId());
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        return changeService.adjustRiskLevel(riskAccessRecord);
    }

    @PostMapping(value = "/task/back/preApproval")
    @ApiOperation("e事通设置任务是否需要退回预审")
    public AjaxResult setPreApproval(@RequestHeader("userCode") String userCode, @RequestParam("changeTaskNo") String changeTaskNo, @RequestParam("flag") Integer flag) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", userCode);
        OgUser ogUser = ogUserService.selectUserByLoginName(map);
        if (ogUser == null) {
            return AjaxResult.warn("工号不存在");
        }
        ogUser = ogUserService.selectOgUserByUserId(ogUser.getpId());
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        String changeMangerId = base.getChangeMangerUserId();
        String deputyId = base.getVaildDeputyUserId(changeMangerId);
        String currentUserId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        if (currentUserId.equals(changeMangerId)||currentUserId.equals(deputyId)) {
            return changeService.setBackToPreApprovalFlag(changeTaskNo, flag);
        } else {
            return AjaxResult.warn("当前登录人非变更经理或变更经理的代理人，不允许操作");
        }
    }

    @PostMapping(value = "/task/back/start")
    @ApiOperation("E事通设置任务是否需要退回发起（已注册状态）")
    public AjaxResult setBackToStart(@RequestHeader("userCode") String userCode, @RequestParam("changeTaskNo") String changeTaskNo, @RequestParam("flag") Integer flag) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", userCode);
        OgUser ogUser = ogUserService.selectUserByLoginName(map);
        if (ogUser == null) {
            return AjaxResult.warn("工号不存在");
        }
        ogUser = ogUserService.selectOgUserByUserId(ogUser.getpId());
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        String changeMangerId = base.getChangeMangerUserId();
        String deputyId = base.getVaildDeputyUserId(changeMangerId);
        String currentUserId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        if (currentUserId.equals(changeMangerId)||currentUserId.equals(deputyId)) {
            return changeService.setBackToStart(changeTaskNo, flag);
        } else {
            return AjaxResult.warn("当前登录人非变更经理或变更经理代理人，不允许操作");
        }
    }
}
