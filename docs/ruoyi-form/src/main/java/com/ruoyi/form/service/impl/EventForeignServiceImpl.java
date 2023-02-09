package com.ruoyi.form.service.impl;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.core.BeanUtils;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.*;
import com.ruoyi.form.domain.*;
import com.ruoyi.form.enums.RedundancyFieldEnum;
import com.ruoyi.form.mapper.DesignBizIncidentMapper;
import com.ruoyi.form.service.*;
import com.ruoyi.system.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.form.constants.CustomerFlowConstants;
import com.ruoyi.form.constants.EventFieldConstants;
import com.ruoyi.form.enums.EventIncSource;
import com.ruoyi.form.enums.EventStatusEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.ItBizExtendMapper;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.domain.SysBizFile;

@Service
public class EventForeignServiceImpl implements EventForeignService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EventForeignServiceImpl.class);

    private final String code = WorkOrderInformation.incident.getCode();

    private final String EVENT_PREFIX_KEY = "IM";

    // 默认接口创建人系统账号：客服(kefu)
    private final String SERVICE_CREATE_USER_ID = "8050d19a1f41462bbdf5ad366661971b";
    // 默认接口创建人系统账号：柜面(gm)
    private final String TIPSERVICE_CREATE_USER_ID = "3f33bd3b4fc1406aa7a4a92b47c8be70";
    // 默认接口创建人系统账号：监控(monitor)
    private final String MONITOR_CREATE_USER_ID = "04977895151d4c13816a9cb39193ac1e";
    // 客服默认接口创建人上报人部门  零售业务部
    private final String SERVICE_REPORT_ORG = "310200079";
    // 客服默认接口创建人上报人电话
    private final String SERVICE_REPORT_PHONE = "95594";

    // 客服默认的应用系统
    private final String SERVICE_SYSTEM_NAME = "TPB.TD";
    // 柜面默认的应用系统
    private final String COUNTER_SYSTEM_NAME = "BBP";
    // 监控系统的应用系统
    private final String MONITOR_SYSTEM_NAME = "IOPS.IM";

    @Autowired
    private CustomerFormServiceImpl customerFormService;

    @Autowired
    private FormVersionService formVersionService;

    @Autowired
    private DesignBizJsonDataService designBizJsonDataService;

    @Autowired
    private IIdGeneratorService generatorService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    @Autowired
    private IShDutyService dutyService;

    @Autowired
    private IncidentSubEventService subEventService;

    @Autowired
    private ICommonTreeService commonTreeService;

    @Autowired
    private ISysBizFileService sysBizFileService;

    @Autowired
    private CustomerFormMapper customerFormMapper;

    @Autowired
    private ItBizExtendMapper itBizExtendMapper;

    @Autowired
    private DesignBizIncidentMapper designBizIncidentMapper;

    @Autowired
    private EmailMessageService emailMessageService;

    @Autowired
    private ForeignService foreignService;

    @Value("${foreign.customerServiceUrl}")
    private String customerServiceUrl;
    @Value("${foreign.monitorService.url}")
    private String monitorService;
    @Value("${foreign.nlpService.url}")
    private String nlpService;

    private final String serviceGroupName = "IT服务台";
    private final String applicationSupportCounterGroupName = "应用支持部柜面组";
    private final String applicationSupportServiceGroupName = "应用支持部客服组";

    private final String eventAdminRoleId = "9305";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult startEventProcess(EventForeignVo vo) {
        String eventNo = generatorService.createNoAsLength(EVENT_PREFIX_KEY, 5);
        vo.setEventNo(eventNo);
        DesignFormVersion formVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", String.format("%s_%s", "design_biz", code)).eq("is_current", 1));
        String s = JSON.toJSONString(vo);
        Map<String, Object> paramMap = JSON.parseObject(s, new TypeReference<Map<String, Object>>() {
        });
        //走添加 把通用字段中的创建时间、修改时间、创建人、修改人添加至字段中
        paramMap.put("created_time", DateUtils.getNowDate());
        paramMap.put("created_by", SERVICE_CREATE_USER_ID);
        paramMap.put("updated_time", DateUtils.getNowDate());
        paramMap.put("updated_by", SERVICE_CREATE_USER_ID);
        paramMap.put("extra1", eventNo);
        String dataJson = VueDataJsonUtil.analysisDataJson(formVersion.getJson(), paramMap);
        CustomerFormContent customerFormContent = new CustomerFormContent();
        // 转换数据的字段map
        filterFieldsMap(paramMap);
        customerFormContent.setFields(paramMap);
        boolean flag = false;
        try {
            MybatisPlusConfig.customerTableName.set(String.format("%s_%s", "design_biz", code));
            // 插入自定义业务表
            customerFormMapper.insert(customerFormContent);
            // 保存jsonData数据对象
            DesignBizJsonData jsonData = DesignBizJsonData.builder().bizTable(String.format("%s_%s", "design_biz", code)).bizId(customerFormContent.getId()).jsonData(dataJson).build();
            designBizJsonDataService.save(jsonData);
            // 保存业务扩展信息表
            this.saveBizExtendInfoMonitor(vo, customerFormContent.getId(), eventNo);

            Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", "design_biz", code), null);
            if (currentTableInfo != null) {
                String version = String.valueOf(currentTableInfo.get("version"));
                Map<String, Object> map = packageProcessCondition(vo);
                OgUser user = new OgUser(SERVICE_CREATE_USER_ID);
                CustomerBizInterceptor.currentUserThread.set(user);
                AjaxResult ajaxResult = customerFormService.processSubmit(code, customerFormContent.getId().toString(), version, map);
                if (Integer.valueOf(ajaxResult.get("code").toString()) == 0) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("接口调用异常，异常原因:" + e.getMessage());
        } finally {
            MybatisPlusConfig.customerTableName.remove();
        }

        if (flag) {
            Map<String, Object> map = new HashMap<>();
            map.put("eventNo", eventNo);
            map.put("eventStatus", EventStatusEnum.ASSIGNED.getInfo());
            AjaxResult ajaxResult = AjaxResult.success("事件发起调用成功", map);
            return ajaxResult;
        }
        return AjaxResult.error("事件发起调用失败");
    }

    /**
     * 事件单监控、新柜面接口发起
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult createEvent(EventIncCreateVo incCreate) {
        log.info("-----事件创建接口接收到外接系统发送数据 data:{}", incCreate);
        String imCode = generatorService.createNoAsLength(EVENT_PREFIX_KEY, 5);
        DesignFormVersion formVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", String.format("%s_%s", "design_biz", code)).eq("is_current", 1));
        Map<String, Object> fieldsMap = getCounterFieldsMap(incCreate);
        Map<String, Object> map = null;// 自动分派值班组
        //走添加 把通用字段中的创建时间、修改时间、创建人、修改人添加至字段中
        String createByUserId = "";
        if(EventIncSource.Service.getCode().equals(incCreate.getEventSource())) {
            createByUserId = SERVICE_CREATE_USER_ID;
            fieldsMap.put("created_by", SERVICE_CREATE_USER_ID);
            fieldsMap.put("updated_by", SERVICE_CREATE_USER_ID);
            map = selectDutyPersonByGroupName(applicationSupportServiceGroupName);
        } else if(EventIncSource.TIPService.getCode().equals(incCreate.getEventSource())) {
            if(StringUtils.isNotEmpty(incCreate.getOperatorNo())) {
                OgUser ogUser = ogUserService.selectOgUserByUsername(incCreate.getOperatorNo());
                if(ogUser != null) {
                    createByUserId = ogUser.getUserId();
                    fieldsMap.put("created_by", ogUser.getUserId());
                    fieldsMap.put("updated_by", ogUser.getUserId());
                }
            }
            if(StringUtils.isEmpty(createByUserId)) {
                createByUserId = TIPSERVICE_CREATE_USER_ID;
            }
            map = selectDutyPersonByGroupName(applicationSupportCounterGroupName);
        } else if(EventIncSource.Monitor.getCode().equals(incCreate.getEventSource())) {
            createByUserId = MONITOR_CREATE_USER_ID;
            fieldsMap.put("created_by", MONITOR_CREATE_USER_ID);
            fieldsMap.put("updated_by", MONITOR_CREATE_USER_ID);
            map = selectDutyPersonByGroupName(incCreate.getAssignedGroup());
        } else if(EventIncSource.MobileService.getCode().equals(incCreate.getEventSource())) {
            // 移动保障单独设置上报人相关逻辑
            String eventInfo = incCreate.getEvenTdesc();
            String[] arr = eventInfo.split("\\|");
            if(StringUtils.isNotEmpty(arr)) {
                // 根据员工号去查询创建人id
                String transTeller = arr[0];
                if(StringUtils.isNotEmpty(transTeller)) {
                    OgUser ogUser = ogUserService.selectOgUserByUsername(transTeller);
                    if(ogUser != null) {
                        fieldsMap.put("created_by", ogUser.getUserId());
                        createByUserId = ogUser.getUserId();
                        fieldsMap.put("updated_by", ogUser.getUserId());
                    }
                }
            }
            map = selectMobilePhoneBySys(incCreate.getSystemName());
        }
        fieldsMap.put("created_time", DateUtils.getNowDate());
        fieldsMap.put("updated_time", DateUtils.getNowDate());
        fieldsMap.put("extra1", imCode);
        if(StringUtils.isNotEmpty(map)) {
            CommonTree commonTree = commonTreeService.selectCommonTreeByName((String) map.get("groupName"));
            fieldsMap.put(EventFieldConstants.ASSIGNED_GROUP, commonTree == null ? "" : commonTree.getOgId());// 受派组从机构树查询 包含IT服务台
            fieldsMap.put(EventFieldConstants.ASSIGNED_PERSON, map.get("userId"));// 受派人
        }
        String dataJson = VueDataJsonUtil.analysisDataJson(formVersion.getJson(), fieldsMap);
        CustomerFormContent customerFormContent = new CustomerFormContent();
        // 转换数据的字段map
        filterFieldsMap(fieldsMap);
        customerFormContent.setFields(fieldsMap);
        boolean flag = false;
        String errorMsg = "";
        try {
        	log.info("createEvent.Transactional.code:"+code+",imCode:"+imCode);
            MybatisPlusConfig.customerTableName.set(String.format("%s_%s", "design_biz", code));
            // 插入自定义业务表
            customerFormMapper.insert(customerFormContent);
            // 保存jsonData数据对象
            DesignBizJsonData jsonData = DesignBizJsonData.builder().bizTable(String.format("%s_%s", "design_biz", code)).bizId(customerFormContent.getId()).jsonData(dataJson).build();
            designBizJsonDataService.save(jsonData);
            // 保存业务扩展信息表
        	log.info("createEvent.saveBizExtendInfoCounter.code:"+code+",imCode:"+imCode);
            AjaxResult ajaxResult1 = this.saveBizExtendInfoCounter(incCreate, customerFormContent.getId(), imCode);
            if(Integer.valueOf(ajaxResult1.get(AjaxResult.CODE_TAG).toString()) != 0) {
                return ajaxResult1;
            }

            Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", "design_biz", code), null);
            if (currentTableInfo != null) {
                String version = String.valueOf(currentTableInfo.get("version"));
                Map<String, Object> params = new HashMap<>();
                /*if (EventIncSource.Monitor.getCode().equals(incCreate.getEventSource())) {
                    // 流程走到处理人接单环节
                    params.put("reCode", "2");
                    params.put("dealReceive", CustomerFlowConstants.ADMIN_USER_ID);
                } else {
                    // 流程走到IT服务台接单
                    params.put("reCode", "0");
                    String receptionId = customerFormService.selectServiceByGroupName(serviceGroupName);
                    params.put("receptionReceive", receptionId);
                }*/
                if((Boolean) map.get("flag")) {
                    // 流程走到处理人接单环节
                    params.put("reCode", "2");
                    params.put("dealReceive", map.get("userId"));
                } else {
                    // 流程走到IT服务台接单
                    params.put("reCode", "0");
                    params.put("receptionReceive", map.get("userId"));
                }
                log.info("start createEvent.setCustomerBizInterceptorOgUser.code:"+code+",imCode:"+imCode);
                this.setCustomerBizInterceptorOgUser(createByUserId);
                log.info("start createEvent.processSubmit.code:"+code+",imCode:"+imCode);
                AjaxResult ajaxResult = customerFormService.processSubmit(code, customerFormContent.getId().toString(), version, params);
                if (Integer.valueOf(ajaxResult.get("code").toString()) == 0) {
                    flag = true;
                }
                log.info("finish createEvent.processSubmit.code:"+code+",imCode:"+imCode);

            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg += "接口调用异常，异常原因:" + e.getMessage();
        } finally {
            MybatisPlusConfig.customerTableName.remove();
        }
        AjaxResult ajaxResult;
        if (flag) {
            Map<String, Object> result = new HashMap<>();
            result.put("imCode", imCode);
            ajaxResult = AjaxResult.success("事件单创建成功", result);
            // 监控单独返回一种json格式
            if(EventIncSource.Monitor.getCode().equals(incCreate.getEventSource())) {
                ajaxResult = AjaxResult.success("事件单创建成功");
                ajaxResult.put("imCode", imCode);
            }
        } else {
            ajaxResult = AjaxResult.error("事件单创建失败，失败原因:" + errorMsg);
        }
        return ajaxResult;
    }

    /**
     * 根据值班组获取当天值班人
     * @param groupName
     * @return
     */
    public Map<String, Object> selectDutyPersonByGroupName(String groupName) {
        Map<String, Object> map = new HashMap<>();
        Boolean flag = true;
        String userId = null;
        String date = DateUtils.getDate();
        // 查询值班组，值班组为空，默认查询服务台值班组，服务台值班组为空默认取值事件管理员，事件管理员为空默认取值amdin
        ShDuty shDuty = dutyService.queryDutyPerson(date, groupName);
        if(shDuty == null) {
            groupName = serviceGroupName;
            shDuty = dutyService.queryDutyPerson(date, serviceGroupName);
            if(shDuty == null) {
                flag = false;
                userId = selectEventAdmin();
            } else {
                OgUser ogUser = ogUserService.selectTimeByUsername(shDuty.getUser_no());
                userId = ogUser == null ? shDuty.getUser_no() : ogUser.getUserId();
            }
        } else {
            OgUser ogUser = ogUserService.selectTimeByUsername(shDuty.getUser_no());
            if(ogUser == null) {
                groupName = serviceGroupName;
                shDuty = dutyService.queryDutyPerson(date, serviceGroupName);
                if(shDuty == null) {
                    flag = false;
                    userId = selectEventAdmin();
                } else {
                    ogUser = ogUserService.selectTimeByUsername(shDuty.getUser_no());
                    userId = ogUser == null ? shDuty.getUser_no() : ogUser.getUserId();
                }
            } else {
                userId = ogUser.getUserId();
            }
        }
        map.put("flag", flag);
        map.put("userId", userId);
        map.put("groupName", groupName);
        return map;
    }

    /**
     * 移动端  运维门户自动分派规则
     * @param sysCode
     * @return
     */
    public Map<String, Object> selectMobilePhoneBySys(String sysCode) {
        String groupName = "IT服务台";
        OgSys ogSys = applicationManagerService.selectOgSysBySysCode(sysCode);
        if(ogSys != null) {
            String sysId = ogSys.getSysId();
            List<OgGroup> groupList = workService.selectOgGroupBySys(sysId);
            if(!CollectionUtils.isEmpty(groupList)) {
                OgGroup ogGroup = groupList.get(0);
                groupName = ogGroup.getGrpName();
            }
        }
        Map<String, Object> map = selectDutyPersonByGroupName(groupName);
        return map;
    }
    
    private String selectEventAdmin() {
        OgPerson ogPerson = new OgPerson();
        ogPerson.setrId(eventAdminRoleId);
        List<OgPerson> personList = ogPersonService.selectListByRoleId(ogPerson);
        String userId = CollectionUtils.isEmpty(personList) ? CustomerFlowConstants.ADMIN_USER_ID : personList.get(0).getpId();
        return userId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult addInfoEvent(EventIncModifyVo incModifyVo) {
        log.info("---------事件追加信息接口接收到外接系统数据 data:{}", incModifyVo);
        AjaxResult ajaxResult = null;
        final String tableName = String.format("%s_%s", "design_biz", code);
        EventIncSearchReqVo searchReqVo = new EventIncSearchReqVo();
        searchReqVo.setEventNo(incModifyVo.getEventNo());// 柜面｜客服事件单号
        searchReqVo.setPageStart(0);
        searchReqVo.setPageSize(1);
        List<Map<String, Object>> designBizMapList = customerFormMapper.selectIncidentMapByCondition(tableName, searchReqVo);
        if (CollectionUtils.isEmpty(designBizMapList)) {
            return AjaxResult.error("根据事件编号[" + incModifyVo.getEventNo() + "]查询无数据！");
        }
        Map<String, Object> map = designBizMapList.get(0);
        // 柜面事件操作，撤单和催单
        if (StringUtils.isNotEmpty(incModifyVo.getOperationType())) {
            // 柜面事件撤单
            if ("cancel".equals(incModifyVo.getOperationType())) {
                log.info("柜面接口调用撤单.......");
                ajaxResult = cancelProcess(EventStatusEnum.CLOSED.getInfo(), incModifyVo.getOperationType(), map);
            } else if ("urge".equals(incModifyVo.getOperationType())) {
                log.info("柜面接口调用催单.......");
                // 柜面事件催单
                String bizNo = (String) map.get("imCode");
                IncidentSubEvent subEvent = new IncidentSubEvent();
                subEvent.setUrgeFlag("1");// 催单标识
                subEvent.setUrgeTime(DateUtils.getTime());
                subEvent.setEventNo(bizNo);
                subEventService.updateIncidentSubEventByNo(subEvent);
                ajaxResult = AjaxResult.success();
            }
        } else {
            log.info("客服接口调用追加信息.......");
            // 客服事件追加信息
            // im事件单号
            String imCode = map.get("imCode").toString();
            // 如果是追加信息，需要保存追加记录
            ajaxResult = addInfoBizExtend(incModifyVo, imCode);
        }

        return ajaxResult;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult cancelEvent(EventIncModifyVo incModifyVo) {
        log.info("------客服接口调用撤单，接收到参数 data:{}", incModifyVo);
        final String tableName = String.format("%s_%s", "design_biz", code);
        EventIncSearchReqVo searchReqVo = new EventIncSearchReqVo();
        searchReqVo.setEventNo(incModifyVo.getEventNo());// 客服事件单号
        searchReqVo.setPageStart(0);
        searchReqVo.setPageSize(1);
        List<Map<String, Object>> designBizMapList = customerFormMapper.selectIncidentMapByCondition(tableName, searchReqVo);
        if (CollectionUtils.isEmpty(designBizMapList)) {
            return AjaxResult.error("根据事件编号[" + incModifyVo.getEventNo() + "]查询无数据！");
        }
        Map<String, Object> map = designBizMapList.get(0);
        log.info("客服接口调用撤单.......");
        return cancelProcess(EventStatusEnum.CLOSED.getInfo(), "cancel", map);
    }

    // 撤销流程
    private AjaxResult cancelProcess(String status, String operationType, Map<String, Object> map) {
        // 如果是撤单，流程结束
        String instanceId = map.get("instanceId").toString();
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        if(task == null) {
            return AjaxResult.error("该事件单流程任务已取消，请检查确认后再次发起交易！");
        }
        this.setCustomerBizInterceptorOgUser(SERVICE_CREATE_USER_ID);
        AjaxResult ajaxResult = customerFormService.cancelApply(instanceId);
        // TODO 撤单需要修改业务主表状态
        updateEventIncident(status, operationType, map);
        return ajaxResult;
    }

    private void updateEventIncident(String status, String operatorFlag, Map<String, Object> fieldsMap) {
        Long bizId = (Long) fieldsMap.get("id");
        String bizNo = (String) fieldsMap.get("imCode");
        IncidentSubEvent incidentSubEvent = subEventService.selectIncidentSubEventByEventNo(bizNo);
        Map<String, Object> params = new HashMap<>();
        final String tableName = String.format("%s_%s", "design_biz", code);
        if ("cancel".equals(operatorFlag)) {
            // 如果撤单，需要记录撤单时间
            incidentSubEvent.setCancelFalg("1");// 撤销标识
            incidentSubEvent.setCancelTime(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));// 撤销时间
            params.put("cleanCurrentUserFlag", "1");
            params.put("finalFirstLevel", "ProductEvent");
            params.put("solvePlan", "对方系统撤单");
            Map<String, Object> map = new HashMap<>();
            map.put(EventFieldConstants.FINAL_FIRST_LEVEL, "ProductEvent");
            map.put(EventFieldConstants.SOLVE_PLAN, "对方系统撤单");
            // 更新数据json信息
            updateDesignBizJsonDataById(bizId, map);
        } else if ("close".equals(operatorFlag)) {
            // 如果是关闭事件单，则记录关闭时间
            incidentSubEvent.setCloseTime(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));// 关闭时间
        } else if("back".equals(operatorFlag)) {
            incidentSubEvent.setBackCompletionFlag("3");// 确认关闭环节退回到服务台
        }
        subEventService.updateIncidentSubEvent(incidentSubEvent);
        // 所有操作均需要记录数据库状态
        params.put("status", status);
        params.put("bizId", bizId);
        customerFormMapper.updateEventIncident(tableName, params);
    }

    /**
     * 客服、柜面追加其他信息字段
     *
     * @param incModifyVo
     * @param imCode
     */
    private AjaxResult addInfoBizExtend(EventIncModifyVo incModifyVo, String imCode) {
        ItBizExtend extend = itBizExtendMapper.selectOne(new QueryWrapper<ItBizExtend>().eq("biz_no", imCode));
        ItBizExtend bizExtend = new ItBizExtend();
        if (EventIncSource.TIPService.getCode().equals(extend.getSourceType())) {
            // 评价内容
            bizExtend.setExtend31(incModifyVo.getEnvaluationDesc());
            // 满意度
            bizExtend.setExtend32(incModifyVo.getEvenTenvaluation());
        } else if (EventIncSource.Service.getCode().equals(extend.getSourceType())) {
            // 处理意见
            bizExtend.setExtend31(incModifyVo.getAddInfoDesc());
            // 流程日志
            bizExtend.setExtend32(incModifyVo.getProcessLog());
            // 催单日志
            bizExtend.setExtend33(incModifyVo.getReminderLog());
            // 附件路径
            bizExtend.setExtend34(incModifyVo.getFiles());
            if(StringUtils.isNotEmpty(incModifyVo.getFiles())) {
                this.saveServiceImageFile(extend.getBizId(), incModifyVo.getFiles());
            }
            // 客户信息
            bizExtend.setExtend35(incModifyVo.getCustInfo());
        }
        bizExtend.setExtend1(incModifyVo.getOperationType());

        itBizExtendMapper.update(bizExtend, new UpdateWrapper<ItBizExtend>().eq("id", extend.getId()));
        return AjaxResult.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult confirmEvent(EventIncConfirmVo incConfirmVo) {
        log.info("------外接系统调用事件确认接口，接收到参数 data:{}", incConfirmVo);
        Map<String, Object> incidentMap = customerFormMapper.selectIncidentMapByCode(String.format("%s_%s", "design_biz", code), incConfirmVo.getImCode());
        if (CollectionUtils.isEmpty(incidentMap)) {
            return AjaxResult.error("根据IM事件编号[" + incConfirmVo.getImCode() + "]查询无数据！");
        }
        Task task = taskService.createTaskQuery().processInstanceId(incidentMap.get("instanceId").toString()).active().singleResult();
        if (task == null) {
            return AjaxResult.error("根据IM事件编号[" + incConfirmVo.getImCode() + "]查询无流程信息！");
        }
        // 判断事件单状态，控制该交易
        Object status1 = incidentMap.get("status");
        if(!EventStatusEnum.RESOLVED.getInfo().equals(status1)) {
            return AjaxResult.error("根据IM事件编号[" + incConfirmVo.getImCode() + "]查询状态尚未解决，不能发起确认关闭！");
        }
        Long id = (Long) incidentMap.get("id");
        Map<String, Object> fieldsMap = new HashMap<>();
        fieldsMap.put("id", id);
        ItBizExtend bizExtend = new ItBizExtend();
        CustomerVo customerVo = null;
        String comment = "";
        String operationType = "";
        String status = "";
        if ("Y".equals(incConfirmVo.getConfirmFlag())) {
            status = EventStatusEnum.CLOSED.getInfo();
            fieldsMap.put("status", status);
            // 确认标识Y，流程走向关闭
            if (StringUtils.isNotEmpty(incConfirmVo.getEvaluateLabel())) {
                bizExtend.setExtend39(incConfirmVo.getEvaluateLabel());
            }
            customerVo = getCustomerVo("0", "", "", fieldsMap);
            comment = StringUtils.isNotEmpty(incConfirmVo.getEnvaluationDesc()) ? incConfirmVo.getEnvaluationDesc() : "外接系统事件确认关闭";
            operationType = "close";
        } else {
            status = EventStatusEnum.COMPLETION.getInfo();
            fieldsMap.put("status", status);
            // 确认标识N，流程走到退回补全
            String receptionId = customerFormService.selectServiceByGroupName("IT服务台");
            customerVo = getCustomerVo("-1", "receptionReceive", receptionId, fieldsMap);
            comment = incConfirmVo.getEnvaluationDesc();
            operationType = "back";
        }
        // 确认标识
        bizExtend.setExtend27(incConfirmVo.getConfirmFlag());
        // 事件解决方案
        bizExtend.setExtend36(incConfirmVo.getEventSolution());
        // 事件处理时长
        bizExtend.setExtend37(incConfirmVo.getProcessTime());
        // 评价分数
        bizExtend.setExtend38(incConfirmVo.getEvaluationScore());
        // 评价标签
        bizExtend.setExtend39(incConfirmVo.getEvaluateLabel());
        // 拒绝理由｜评价内容[意见及建议]
        bizExtend.setExtend40(incConfirmVo.getEnvaluationDesc());
        itBizExtendMapper.update(bizExtend, new UpdateWrapper<ItBizExtend>().eq("biz_no", incConfirmVo.getImCode()));
        // TODO 此处需要处理业务表的状态信息
        updateEventIncident(status, operationType, incidentMap);
        ItBizExtend bizExtend1 = itBizExtendMapper.selectOne(new QueryWrapper<ItBizExtend>().eq("biz_no", incConfirmVo.getImCode()));
        String createUserId =  CustomerFlowConstants.ADMIN_USER_ID;
        if(EventIncSource.Service.getCode().equals(bizExtend1.getSourceType())) {
            createUserId = SERVICE_CREATE_USER_ID;
        } else if(EventIncSource.TIPService.getCode().equals(bizExtend1.getSourceType())) {
            createUserId = TIPSERVICE_CREATE_USER_ID;
        } else if(EventIncSource.Monitor.getCode().equals(bizExtend1.getSourceType())) {
            createUserId = MONITOR_CREATE_USER_ID;
        } else if(EventIncSource.MobileService.getCode().equals(bizExtend1.getSourceType())) {
            String transTeller = bizExtend1.getExtend8();
            if(StringUtils.isNotEmpty(transTeller)) {
                OgUser ogUser = ogUserService.selectOgUserByUsername(transTeller);
                if(ogUser != null) {
                    createUserId = ogUser.getUserId();
                }
            }
        }
        this.setCustomerBizInterceptorOgUser(createUserId);
        AjaxResult ajaxResult = AjaxResult.success();
        try {
            customerFormService.complete(code, task.getId(), incidentMap.get("instanceId").toString(), comment, customerVo);
        } catch (Exception e) {
            ajaxResult = AjaxResult.error(e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * 封装流程complete参数
     *
     * @param reCode
     * @param expression
     * @param assignee
     * @return
     */
    private CustomerVo getCustomerVo(String reCode, String expression, String assignee, Map<String, Object> fieldsMap) {
        CustomerVo customerVo = new CustomerVo();
        Map<String, Object> variables = new HashMap<>();
        variables.put("reCode", reCode);
        if (StringUtils.isNotEmpty(expression)) {
            variables.put(expression, assignee);
        }
        customerVo.setVariables(variables);
        CustomerFormContent customerFormContent = new CustomerFormContent();
        customerFormContent.setFields(fieldsMap);
        customerVo.setCustomerFormContent(customerFormContent);
        return customerVo;
    }

    @Override
    public Map<String, Object> searchEvent(EventIncSearchReqVo vo) {
        log.info("------外接系统调用事件查询接口，接收到参数 data:{}", vo);
        if(vo.getIsTimeFlag()!=1) {
            if (StringUtils.isEmpty(vo.getStratTime())) {
                // 如果开始时间为空，默认查询当前时间三个月之前
                Date date = DateUtils.addMonths(DateUtils.getNowDate(), -3);
                vo.setStratTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date));
            }
            if (StringUtils.isEmpty(vo.getEndTime())) {
                // 如果结束时间为空，默认获取当前时间
                Date date = DateUtils.getNowDate();
                vo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date));
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        String tableName = String.format("%s_%s", "design_biz", code);
        //String fields = " id,instance_id as instanceId,extra1 as bizNo,status ";
        CompletableFuture<List<Map<String, Object>>> bizFuture = CompletableFuture.supplyAsync(() -> {
            List<Map<String, Object>> designBizMapList = customerFormMapper.selectIncidentMapByCondition(tableName, vo);
            return designBizMapList;
        }, threadPoolTaskExecutor);
        CompletableFuture<List<EventIncSearchRespVo>> searchFuture = bizFuture.thenApplyAsync((t) -> {
            List<EventIncSearchRespVo> collect = new ArrayList<>();
            if (!CollectionUtils.isEmpty(t)) {
                collect = t.stream().map(bizMap -> {
                    ItBizExtend bizExtend = itBizExtendMapper.selectOne(new QueryWrapper<ItBizExtend>().eq("biz_no", bizMap.get("imCode")));
                    /*Map<String, Object> bizExtendMap = new HashMap<>();
                    if (bizExtend != null) {
                        // 告警id
                        String alarmId = bizExtend.getExtend2();
                        bizExtendMap.put("alarmId", alarmId);
                        bizExtendMap.put("eventNo", eventNo);
                        bizExtendMap.put("status", bizMap.get("status"));
                        return bizExtendMap;
                    } else {
                        return bizExtendMap;
                    }*/
                    return collectSearchResult(bizMap, bizExtend);
                }).collect(Collectors.toList());
            }
            return collect;
        }, threadPoolTaskExecutor);

        CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(() -> {
            Long count = customerFormMapper.selectIncidentCountByCondition(tableName, vo);
            return count;
        }, threadPoolTaskExecutor);
        try {
            CompletableFuture.allOf(searchFuture, countFuture).join();
            List<EventIncSearchRespVo> resultList = searchFuture.get();
            Long count = countFuture.get();
            resultMap.put("resultList", resultList);
            resultMap.put("count", count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> modifyEventPriority(EventMonitorVo vo) {
        log.info("------外接系统调用事件更新等级接口，接收到参数 data:{}", vo);
        Map<String, Object> incidentMap = customerFormMapper.selectIncidentMapByCode(String.format("%s_%s", "design_biz", code), vo.getImCode());
        if (CollectionUtils.isEmpty(incidentMap)) {
            return AjaxResult.error("根据事件单号[" + vo.getImCode() + "]查询无数据！");
        }
        ProcessInstanceQuery instance = runtimeService.createProcessInstanceQuery().processInstanceId((String)incidentMap.get("instanceId"));
        if (instance == null) {
            return AjaxResult.error("根据事件单号[" + vo.getImCode() + "]查询流程已关闭或撤销，不能更新事件等级！");
        }
        if(StringUtils.isEmpty(vo.getEventPriority())) {
            return AjaxResult.error("事件等级不可为空！");
        }
        String oldPriority = (String) incidentMap.get(EventFieldConstants.EVENT_PRIORITY);
        //String newPriority = EventSeverityEnum.convertMonitorCode(vo.getEventPriority());
        if(!hasPriorityUpdate(vo.getEventPriority(), (String) incidentMap.get(EventFieldConstants.EVENT_PRIORITY))) {
            return AjaxResult.error("原事件等级["+oldPriority+"],最新事件等级["+vo.getEventPriority()+"] "
                    + oldPriority + ">=" + vo.getEventPriority() + "不做更新事件等级处理!");
        }
        Long bizId = (Long) incidentMap.get("id");
        Map<String, Object> fieldsMap = new HashMap<>();
        fieldsMap.put(EventFieldConstants.EVENT_PRIORITY, vo.getEventPriority());
        fieldsMap.put(EventFieldConstants.TARGET_RESOLVE_DATE, mathDateByPriority(vo.getEventPriority()));
        // 更新json字段
        this.updateDesignBizJsonDataById(bizId, fieldsMap);
        // 事件单等级
        fieldsMap.put("bizId", bizId);
        customerFormMapper.updateEventIncident(String.format("%s_%s", "design_biz", code), fieldsMap);
        AjaxResult success = AjaxResult.success();
        success.put("imCode", incidentMap.get("imCode"));
        return success;
    }

    /**
     * 更新json字段信息
     * @param bizId
     * @param fieldsMap
     */
    private void updateDesignBizJsonDataById(Long bizId,  Map<String, Object> fieldsMap) {
        DesignBizJsonData designBizJsonData = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", code)));
        String dataJson = VueDataJsonUtil.analysisDataJson(designBizJsonData.getJsonData(), fieldsMap);
        // 保存jsonData数据对象
        DesignBizJsonData jsonData = DesignBizJsonData.builder().bizTable(String.format("%s_%s", "design_biz", code)).bizId(bizId).jsonData(dataJson).build();
        //designBizJsonDataService.save(jsonData);
        designBizJsonDataService.update(jsonData, Wrappers.<DesignBizJsonData>update().eq(DesignBizJsonData.COL_BIZ_ID, bizId).eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", code)));

    }

    /**
     * 更新事件等级判断
     * 如果原来P1，新值不需要更新
     * 如果原来P2，新值P2,P3,P4不更新，P1才更新
     * 如果原来P3，新值P3,P4不更新，P1，P2才更新
     * 如果原来P4，新值P4不更新，P1，P2，P3才更新
     * @param newPriority
     * @param oldPriority
     * @return
     */
    public boolean hasPriorityUpdate(String newPriority, String oldPriority) {
        boolean flag = true;
        if("P1".equals(oldPriority)) {
            flag = false;
        } else if("P2".equals(oldPriority)
                && ("P2".equalsIgnoreCase(newPriority) || "P3".equalsIgnoreCase(newPriority) || "P4".equalsIgnoreCase(newPriority))) {
            flag = false;
        } else if("P3".equals(oldPriority) &&
                ("P3".equalsIgnoreCase(newPriority) || "P4".equalsIgnoreCase(newPriority))) {
            flag = false;
        } else if("P4".equals(oldPriority) && "P4".equalsIgnoreCase(newPriority)) {
            flag = false;
        }
        return flag;
    }

    /**
     * <eventID>IM2021020125073</eventID>            事件编号
     * <eventRemark>测试eventRemark</eventRemark>    事件描述
     * <eventStatus>Resolved</eventStatus>          事件状态
     * <sendEmpCode>930151</sendEmpCode>            流程平台发送人工号
     * <tcsdID>210201112674</tcsdID>                派单号
     * @param
     */
    @Override
    public void sendMsgTokeFu(String instanceId, String status, String statusStr) {
        List<Map<String, Object>> records = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().select("extra1, solve_plan").eq("instance_id", instanceId));
        if(CollectionUtils.isEmpty(records)) {
            return;
        }
        String imCode = (String) records.get(0).get("extra1");
        String solvePlan = (String) records.get(0).get(EventFieldConstants.SOLVE_PLAN);
        ItBizExtend bizExtend = itBizExtendMapper.selectOne(new QueryWrapper<ItBizExtend>().eq("biz_no", imCode));
        if(bizExtend == null || !EventIncSource.Service.getCode().equals(bizExtend.getSourceType())) {
            return;
        }

        String url = customerServiceUrl;

        boolean backFlag = false;
        String statusCode = "";
        if(status.equals(EventStatusEnum.ASSIGNED.getInfo()) || status.equals(EventStatusEnum.PROCESS.getInfo())) {
            // 已分派
            statusCode = "Assigned";
            statusStr = StringUtils.isNotEmpty(statusStr) ? statusStr : "事件单已分派";
        } else if(status.equals(EventStatusEnum.RESOLVED.getInfo())) {
            // 已解决
            statusCode = "Resolved";
            statusStr = StringUtils.isNotEmpty(solvePlan) ? solvePlan : statusStr;
        } else if(status.equals(EventStatusEnum.CLOSED.getInfo())) {
            // 撤销和已关闭标识已关闭
            statusCode = "Closed";
            statusStr = "事件单已关闭";
        }

        IncidentSubEvent incidentSubEvent = subEventService.selectIncidentSubEventByEventNo(imCode);
        // 事件单确认关闭环节流程退回到服务台，此时不需要再次调用客服的退回接口 处理环节退回backCompletionFlag=1
        if(incidentSubEvent != null && "1".equals(incidentSubEvent.getBackCompletionFlag())) {
            backFlag = true;
        }

        String elName = "updatePdDataFromBMCResponse";
        if(backFlag) {
            url += "updatePdBack";
            statusCode = "Resolved";
            elName = "updatePdBackResponse";
        } else {
            url += "updatePdDataFromBMC";
        }

        Map<String, Object> params = new HashMap<>();
        params.put("eventID", imCode);// 事件编号
        params.put("eventRemark", statusStr);// 描述信息
        params.put("eventStatus", statusCode);// 事件状态
        OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
        OgPerson person = ogPersonService.selectOgPersonById(ogUser.getUserId());

        params.put("sendEmpCode", ogUser.getUsername() + "/" + person.getpName());// 流程平台发送人工号
        params.put("tcsdID", bizExtend.getExtend2());// 派单号

        this.sendXmlMsg(url, elName, params, backFlag);
    }

    @Override
    public AjaxResult selectServiceAssignDetailsByNo(String bizNo, Page page) {
        QueryWrapper<ItBizExtend> queryWrapper = new QueryWrapper<>();
        ItBizExtend bizExtend = itBizExtendMapper.selectOne(queryWrapper.eq("biz_no", bizNo));
        if(bizExtend == null) {
            return AjaxResult.success(new HashMap<>());
        }
        // 流程日志
        String extend32 = bizExtend.getExtend32();
        // 催单日志
        String extend33 = bizExtend.getExtend33();
        List<ReminderLogVo> dataList = new ArrayList<>();
        if(StringUtils.isNotEmpty(extend32)) {
            dataList = ConverterUtils.getDTOList(extend32, ReminderLogVo.class);
            if(!CollectionUtils.isEmpty(dataList)) {
                if(StringUtils.isNotEmpty(extend33)) {
                    dataList.addAll(ConverterUtils.getDTOList(extend33, ReminderLogVo.class));
                }
                dataList = dataList.stream().map(l -> {
                    if(l != null) {
                        l.setDealTimeStr(DateUtils.convertLongTimeString(l.getDealTime()));
                        l.setType(StringUtils.isNotEmpty(l.getType()) ? ("0".equals(l.getType()) ? "流程日志" : "催单日志") : "");
                    }
                    return l;
                }).sorted(Comparator.comparing(ReminderLogVo::getDealTime).reversed()).collect(Collectors.toList());
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> colLists = new ArrayList<>();
        Map<String, Object> colMap1 = new HashMap<>();
        Map<String, Object> colMap2 = new HashMap<>();
        Map<String, Object> colMap3 = new HashMap<>();
        Map<String, Object> colMap4 = new HashMap<>();
        Map<String, Object> colMap5 = new HashMap<>();
        colMap1.put("label", "类型");
        colMap1.put("val", "type");
        colMap2.put("label", "处理人");
        colMap2.put("val", "workNo");
        colMap3.put("label", "处理部门");
        colMap3.put("val", "dealDepartment");
        colMap4.put("label", "处理时间");
        colMap4.put("val", "dealTimeStr");
        colMap5.put("label", "描述");
        colMap5.put("val", "content");

        // 客户信息
        String extend35 = bizExtend.getExtend35();
        if(StringUtils.isNotEmpty(extend35)) {
            // 客户信息展示在客户派单详情里面，缺的字段有特殊字符*展示补全
            final String symbols = "******";
            dataList.add(0, new ReminderLogVo("客户信息", symbols, symbols, symbols, extend35));
        }

        colLists.add(colMap1);
        colLists.add(colMap2);
        colLists.add(colMap3);
        colLists.add(colMap4);
        colLists.add(colMap5);
        resultMap.put("total", dataList.size());
        resultMap.put("col", colLists);
        resultMap.put("pageNum", page.getCurrent());
        resultMap.put("pageSize", page.getSize());
        if(!CollectionUtils.isEmpty(dataList)) {
            List<?> pageList = getPageList(dataList, (int) page.getCurrent(), (int) page.getSize());
            resultMap.put("list", pageList);
        } else {
            resultMap.put("list", dataList);
        }

        return AjaxResult.success(resultMap);
    }

    @Override
    public boolean judgeInterfaceCreateBack(String bizNo) {
        ItBizExtend bizExtend = itBizExtendMapper.selectOne(new QueryWrapper<ItBizExtend>().eq("biz_no", bizNo));
        return bizExtend != null;
    }

    @Override
    public boolean judgeMonitorCreateIncident(String bizNo) {
        boolean flag = false;
        ItBizExtend bizExtend = itBizExtendMapper.selectOne(new QueryWrapper<ItBizExtend>().eq("biz_no", bizNo));
        if(bizExtend != null) {
            String sourceType = bizExtend.getSourceType();
            if(EventIncSource.Monitor.getCode().equals(sourceType)) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean judgeMonitorCreateIncidentByInstanceId(String instanceId) {
        boolean flag = false;
        String bizNo = customerFormMapper.selectEventNoByInstanceId(instanceId);
        ItBizExtend bizExtend = itBizExtendMapper.selectOne(new QueryWrapper<ItBizExtend>().eq("biz_no", bizNo));
        if(bizExtend != null) {
            String sourceType = bizExtend.getSourceType();
            if(EventIncSource.Monitor.getCode().equals(sourceType)) {
                flag = true;
            }
        }
        return flag;
    }

    private List<?> getPageList(List<?> list, Integer pageNum, Integer pageSize) {
        List<?> newList;
        if (pageNum * pageSize > list.size()) {
            if (pageSize > list.size()) {
                newList = list;
            } else {
                if((pageNum - 1) * pageSize > list.size()){
                    newList = new ArrayList<>();
                }else{
                    newList = list.subList((pageNum - 1) * pageSize, list.size());
                }
            }
        } else {
            newList = list.subList((pageNum - 1) * pageSize, pageNum * pageSize);
        }
        return newList;
    }

    public void sendXmlMsg(String url, String elName, Map<String, Object> params, boolean backFlag) {
        String xml =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"http://impl.service.wpwebService\">\n" +
                        "    <soapenv:Body>\n" +
                        "        <urn:updatePdDataFromBMC>\n" +
                        "            <wpEvent>\n" +
                        "                <eventID>" + params.get("eventID") + "</eventID>\n" +
                        "                <eventRemark>" + params.get("eventRemark") + "</eventRemark>\n" +
                        "                <eventStatus>" + params.get("eventStatus") + "</eventStatus>\n" +
                        "                <sendEmpCode>" + params.get("sendEmpCode") + "</sendEmpCode>\n" +
                        "                <tcsdID>" + params.get("tcsdID") + "</tcsdID>\n" +
                        "            </wpEvent>\n" +
                        "        </urn:updatePdDataFromBMC>\n" +
                        "    </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        // 如果是退回接口场景，则直接将"updatePdDataFromBMC"替换成退回接口"updatePdBack"
        if(backFlag) {
            xml = xml.replace("updatePdDataFromBMC", "updatePdBack");
        }
        log.info("---发送客服派单[更新信息接口]请求 url:{},xml:{}", url, xml);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> httpEntity = new HttpEntity(xml, headers);
        ResponseEntity<String> responseEntity;
        String response = "";
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        } catch (Exception e) {
            log.error("---发送客服派单[更新信息接口],url=" + url + "请求失败，失败原因", e);
            return;
        }
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            response = responseEntity.getBody();
        } else {
            log.info("请求发送失败，错误码值:" + responseEntity.getStatusCode().value());
            return;
        }
        log.info("---接收客服派单[更新信息接口]返回 response:{}", response);
        Map<String, String> map = convertXml(response, elName);
        if (!CollectionUtils.isEmpty(map)) {
            if ("pd00".equals(map.get("msgCode"))) {
                log.info("客服派单接口调用成功");
            } else {
                log.info("客服派单接口调用失败，失败原因:" + map.get("msgInfo"));
            }
        } else {
            log.info("客服派单接口调用失败！");
        }
    }

    private void filterFieldsMap(Map<String, Object> fields) {
        // 将单号保存到备用字段中
        fields.remove("eventNo");
        fields.remove("alarmId");
        fields.remove("batchLabel");
    }

    /**
     * 获取新柜面系统传输来的业务字段信息
     *
     * @param incCreate
     * @return
     */
    private Map<String, Object> getCounterFieldsMap(EventIncCreateVo incCreate) {
        Map<String, Object> map = new HashMap<>();
        // 事件标题
        map.put(EventFieldConstants.EVENT_TITLE, incCreate.getEvenTile());
        // 事件详述
        map.put(EventFieldConstants.EVENT_INFO, incCreate.getEvenTdesc());
        // 中心侧/网点侧事件
        map.put(EventFieldConstants.SIDE_FLAG, "1");
        // 总分行事件
        map.put(EventFieldConstants.ORG_FLAG, "1");

        // 事件来源
        String eventSource = "";
        if(EventIncSource.Monitor.getCode().equals(incCreate.getEventSource())) {
            eventSource = "9";
        } else if(EventIncSource.Service.getCode().equals(incCreate.getEventSource())) {
            eventSource = "2";
        } else if(EventIncSource.TIPService.getCode().equals(incCreate.getEventSource())) {
            eventSource = "7";
        }else if (EventIncSource.MobileService.getCode().equals(incCreate.getEventSource())) {
            eventSource = "6";
        }
        map.put(EventFieldConstants.EVENT_SOURCE, eventSource);
        // 应用系统名称
        map.put(EventFieldConstants.SYSTEM_NAME, incCreate.getSystemName());
        // 事件单接口创建一级分类默认 突发事件 "ProductEvent"
        map.put(EventFieldConstants.INIT_FIRST_LEVEL, "ProductEvent");

        if (EventIncSource.Service.getCode().equals(incCreate.getEventSource())) {// 客服派单
            // 事件详述 ===> 工单内容
            map.put(EventFieldConstants.EVENT_INFO, incCreate.getWorkContent());
            // 客服事件标题需要拼接  紧急程度+客服编号+业务大类+业务小类
            String emergencyLevel = incCreate.getEmergencyLevel();
            if(StringUtils.isNotEmpty(emergencyLevel)) {
                switch (emergencyLevel) {
                    case "0":
                        emergencyLevel = "特急";
                        break;
                    case "1":
                        emergencyLevel = "紧急";
                        break;
                    case "2":
                        emergencyLevel = "加急";
                        break;
                    case "3":
                        emergencyLevel = "一般";
                        break;
                }
            }
            String title = emergencyLevel + " " + "客服派单号:" + incCreate.getEventNo();
            if(StringUtils.isNotEmpty(incCreate.getBusinessCategory())) {
                title += " " + incCreate.getBusinessCategory();
            }
            if(StringUtils.isNotEmpty(incCreate.getBusinessSubCategory())) {
                title += " " + incCreate.getBusinessSubCategory();
            }
            map.put(EventFieldConstants.EVENT_TITLE, title);

            // 影响面  默认填	7	其他-未影响前台业务操作
            map.put(EventFieldConstants.INF_FACE, "7");
            // 上报人机构  客服默认 零售业务部
            map.put(EventFieldConstants.REPORT_ORG, "零售业务部");
            // 上报人
            map.put(EventFieldConstants.REPORT_PERSON, "客服系统");
            // 上报人电话
            map.put(EventFieldConstants.REPORT_PHONE, SERVICE_REPORT_PHONE);
            // 事件影响程度  11-其他非柜面类
            map.put(EventFieldConstants.INF_LEVEL, "11");
            // 优先级  默认  P4
            map.put(EventFieldConstants.EVENT_PRIORITY, "P4");
            // 目标解决时间
            map.put(EventFieldConstants.TARGET_RESOLVE_DATE, mathDateByPriority((String) map.get(EventFieldConstants.EVENT_PRIORITY)));
            // 应用系统名称
            map.put(EventFieldConstants.SYSTEM_NAME, SERVICE_SYSTEM_NAME);
        } else if(EventIncSource.TIPService.getCode().equals(incCreate.getEventSource())) {// 柜面
            // 设置上报人相关参数
            OgUser ogUser = ogUserService.selectOgUserByUsername(incCreate.getOperatorNo());
            if(ogUser != null) {
                setReportParams(ogUser.getUserId(), map);
            } else  {
                // 上报人部门
                map.put(EventFieldConstants.REPORT_ORG, StringUtils.isEmpty(incCreate.getReportOrg()) ? incCreate.getTransNo() : incCreate.getReportOrg());
                // 上报人
                map.put(EventFieldConstants.REPORT_PERSON, StringUtils.isEmpty(incCreate.getReportPerson()) ? incCreate.getTransName() : incCreate.getReportPerson());
            }

            // 事件影响程度  10-其他-柜面类
            map.put(EventFieldConstants.INF_LEVEL, "10");
            // 优先级  默认  P2
            map.put(EventFieldConstants.EVENT_PRIORITY, "P2");
            // 目标解决时间
            map.put(EventFieldConstants.TARGET_RESOLVE_DATE, mathDateByPriority((String) map.get(EventFieldConstants.EVENT_PRIORITY)));
            // 应用系统名称
            map.put(EventFieldConstants.SYSTEM_NAME, COUNTER_SYSTEM_NAME);

            // 上报人电话
            map.put(EventFieldConstants.REPORT_PHONE, incCreate.getReportPhone());
            String eventInfo = setEventInfo(incCreate);
            map.put(EventFieldConstants.EVENT_INFO, eventInfo);
        } else if (EventIncSource.MobileService.getCode().equals(incCreate.getEventSource())) {// 移动报障
            // 事件影响程度
            map.put(EventFieldConstants.INF_LEVEL, incCreate.getInfLevel());
            // 财务事件
            map.put(EventFieldConstants.FINANCE_FLAG, incCreate.getFinanceFlag());
            // 是否涉及投诉
            map.put(EventFieldConstants.COMPLAIN_FLAG, incCreate.getComplainFlag());
            // 事件初始类型一级
            //map.put(EventFieldConstants.INIT_FIRST_LEVEL,incCreate.getInitFirstLevel());
            // 优先级  默认  P4
            map.put(EventFieldConstants.EVENT_PRIORITY, "P4");
            // 目标解决时间
            map.put(EventFieldConstants.TARGET_RESOLVE_DATE, mathDateByPriority((String) map.get(EventFieldConstants.EVENT_PRIORITY)));
            // 上报人部门
            map.put(EventFieldConstants.REPORT_ORG, incCreate.getReportOrg());
            // 上报人
            map.put(EventFieldConstants.REPORT_PERSON, incCreate.getReportPerson());
            // 上报人电话
            map.put(EventFieldConstants.REPORT_PHONE, incCreate.getReportPhone());
        } else if(EventIncSource.Monitor.getCode().equals(incCreate.getEventSource())) {// 监控
            // 设置上报人相关参数
            setReportParams(MONITOR_CREATE_USER_ID, map);
            // 事件影响程度  13
            map.put(EventFieldConstants.INF_LEVEL, "13");
            // 优先级
            map.put(EventFieldConstants.EVENT_PRIORITY, incCreate.getPriority());
            // 目标解决时间
            map.put(EventFieldConstants.TARGET_RESOLVE_DATE, mathDateByPriority((String) map.get(EventFieldConstants.EVENT_PRIORITY)));
            String eventInfo = dealMonitorInfo(incCreate.getEvenTdesc());
            map.put(EventFieldConstants.EVENT_INFO, eventInfo);
            // 应用系统名称
            map.put(EventFieldConstants.SYSTEM_NAME, MONITOR_SYSTEM_NAME);
            // 监控最终事件类型 默认 "突发事件"
            map.put(EventFieldConstants.FINAL_FIRST_LEVEL, "ProductEvent");
        }
        return map;
    }

    public static String dealMonitorInfo(String eventInfo) {
        String eventInfoStr = "";
        if(StringUtils.isNotEmpty(eventInfo)) {
            String[] strArr = eventInfo.split("\\\\r\\\\n");
            if(StringUtils.isNotEmpty(strArr)) {
                for(String info : strArr) {
                    eventInfoStr += info + "\r\n";
                }
            }
        }
        return eventInfoStr;
    }

    private String setEventInfo(EventIncCreateVo vo) {
        String eventInfo = "事件内容：" + vo.getEvenTdesc() + "\r\n";
        // 异常事件上报号
        if(StringUtils.isNotEmpty(vo.getEventNo())) {
            eventInfo += "异常事件上报号：" + vo.getEventNo() + "\r\n";
        }
        // 紧急程度
        if(StringUtils.isNotEmpty(vo.getEmergencyLevel())) {
            eventInfo += "紧急程度：" + vo.getEmergencyLevel() + "\r\n";
        }
        // 柜员钱箱
        if(StringUtils.isNotEmpty(vo.getCashBoxNo())) {
            eventInfo += "柜员钱箱：" + vo.getCashBoxNo() + "\r\n";
        }
        // 交易码
        if(StringUtils.isNotEmpty(vo.getTransCode())) {
            eventInfo += "交易码：" + vo.getTransCode() + "\r\n";
        }
        // 交易名称
        if(StringUtils.isNotEmpty(vo.getTransBusName())) {
            eventInfo += "交易名称：" + vo.getTransBusName() + "\r\n";
        }
        // 交易流水
        if(StringUtils.isNotEmpty(vo.getTransSerialNo())) {
            eventInfo += "交易流水：" + vo.getTransSerialNo() + "\r\n";
        }
        // 交易类型
        if(StringUtils.isNotEmpty(vo.getTransType())) {
            eventInfo += "交易类型：" + vo.getTransType() + "\r\n";
        }
        // 借方账号
        if(StringUtils.isNotEmpty(vo.getDebitNo())) {
            eventInfo += "借方账号：" + vo.getDebitNo() + "\r\n";
        }
        // 贷方账号
        if(StringUtils.isNotEmpty(vo.getCreditNo())) {
            eventInfo += "贷方账号：" + vo.getCreditNo() + "\r\n";
        }
        // 交易金额
        if(StringUtils.isNotEmpty(vo.getTransAmount())) {
            eventInfo += "交易金额：" + vo.getTransAmount() + "\r\n";
        }
        // 交易网点号
        if(StringUtils.isNotEmpty(vo.getTransNo())) {
            eventInfo += "交易网点号：" + vo.getTransNo() + "\r\n";
        }
        // N天内的异常事件
        if(StringUtils.isNotEmpty(vo.getExceptionCount())) {
            eventInfo += "N天内的异常事件：" + vo.getExceptionCount() + "\r\n";
        }
        // 预计处理时长
        if(StringUtils.isNotEmpty(vo.getExpectProcessTime())) {
            eventInfo += "预计处理时长：" + vo.getExpectProcessTime() + "\r\n";
        }
        // 操作员工号
        if(StringUtils.isNotEmpty(vo.getOperatorNo())) {
            eventInfo += "操作员工号：" + vo.getOperatorNo() + "\r\n";
        }
        // 操作员名称
        if(StringUtils.isNotEmpty(vo.getOperatorName())) {
            eventInfo += "操作员名称：" + vo.getOperatorName() + "\r\n";
        }
        // 交易币种
        if(StringUtils.isNotEmpty(vo.getTransCurrency())) {
            eventInfo += "交易币种：" + vo.getTransCurrency();
        }
        return eventInfo;
    }

    private void setReportParams(String userId, Map<String, Object> map) {
        OgPerson ogPerson = ogPersonService.selectOgPersonById(userId);
        if(ogPerson != null) {
            // 上报人
            map.put(EventFieldConstants.REPORT_PERSON, ogPerson.getpName());
            // 上报人电话
            map.put(EventFieldConstants.REPORT_PHONE, ogPerson.getMobilPhone());
            // 上报人机构
            map.put(EventFieldConstants.REPORT_ORG, ogPerson.getOrgname());
        } else {
            // 上报人
            map.put(EventFieldConstants.REPORT_PERSON, "******");
            // 上报人电话
            map.put(EventFieldConstants.REPORT_PHONE, "******");
            // 上报人机构
            map.put(EventFieldConstants.REPORT_ORG, "******");
        }
    }

    /**
     * 根据优先级计算目标解决时间
     * P1-2小时
     * P2-24小时
     * P3-48小时
     * P4--48小时
     */
    public String mathDateByPriority(String priority) {
        Date date = DateUtils.getNowDate();
        switch (priority) {
            case "P1":
                date = DateUtils.addHours(date, 2);
                break;
            case "P2":
                date = DateUtils.addHours(date, 24);
                break;
            case "P3":
                date = DateUtils.addHours(date, 48);
                break;
            case "P4":
                date = DateUtils.addHours(date, 48);
                break;
        }
        return DateUtils.formatDate(date, DateUtils.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 保存业务扩展信息  新柜面
     *
     * @param vo
     */
    private AjaxResult saveBizExtendInfoCounter(EventIncCreateVo vo, Long bizId, String bizNo) {
        if(StringUtils.isNotEmpty(vo.getEventNo())) {
            List<ItBizExtend> bizExtends = itBizExtendMapper.selectList(new QueryWrapper<ItBizExtend>().eq("extend2", vo.getEventNo()).orderByDesc("id"));
            if(!CollectionUtils.isEmpty(bizExtends)) {
                boolean flag = false;
                for(ItBizExtend extend : bizExtends) {
                    IncidentSubEvent incidentSubEvent = subEventService.selectIncidentSubEventByEventNo(extend.getBizNo());
                    if(incidentSubEvent != null) {
                        // 查询遍历，所有的单子如果有存在没有被撤销的则直接报错
                        String cancelFlag = incidentSubEvent.getCancelFalg();
                        if(!"1".equals(cancelFlag)) {
                            flag = true;
                        }
                    }
                }
                if(flag && !EventIncSource.Monitor.getCode().equals(vo.getEventSource())) {
                    return AjaxResult.error("单号["+vo.getEventNo()+"]已存在，不可重复新建！");
                }
            }
        }
        ItBizExtend bizExtend = new ItBizExtend();
        bizExtend.setBizId(bizId);
        bizExtend.setBizNo(bizNo);
        bizExtend.setBizType(code);
        // 事件单来源
        bizExtend.setSourceType(vo.getEventSource());
        // 外系统单号（柜面，客服）
        bizExtend.setExtend2(vo.getEventNo());
        // 上报时间
        bizExtend.setExtend3(vo.getTransDate());
        // 应用系统名称
        bizExtend.setExtend4(vo.getSystemName());
        // 事件标题
        bizExtend.setExtend5(vo.getEvenTile());
        // 事件内容
        bizExtend.setExtend6(vo.getEvenTdesc());
        // 上报人机构
        bizExtend.setExtend7(vo.getTransInstno());
        // 上报人工号
        bizExtend.setExtend8(vo.getTransTeller());
        // 上报人姓名
        bizExtend.setExtend9(vo.getTransName());
        // 上报人联系方式
        bizExtend.setExtend10(vo.getReportPhone());
        // 上报人地址
        bizExtend.setExtend11(vo.getReportAddress());
        // 紧急程度
        bizExtend.setExtend12(vo.getEmergencyLevel());

        if (EventIncSource.TIPService.getCode().equals(vo.getEventSource())) {// 柜面报障设置柜面特有字段
            // 柜员钱箱
            bizExtend.setExtend13(vo.getCashBoxNo());
            // 交易码
            bizExtend.setExtend14(vo.getTransCode());
            // 交易名称
            bizExtend.setExtend15(vo.getTransBusName());
            // 交易流水
            bizExtend.setExtend16(vo.getTransSerialNo());
            // 交易类型
            bizExtend.setExtend17(vo.getTransType());
            // 借方账号
            bizExtend.setExtend18(vo.getDebitNo());
            // 贷方账号
            bizExtend.setExtend19(vo.getCreditNo());
            // 交易金额
            bizExtend.setExtend20(vo.getTransAmount());
            // 交易网点号
            bizExtend.setExtend21(vo.getTransNo());
            // N天内的异常事件
            bizExtend.setExtend22(vo.getExceptionCount());
            // 预计处理时长
            bizExtend.setExtend23(vo.getExpectProcessTime());
            // 操作员工号
            bizExtend.setExtend24(vo.getOperatorNo());
            // 操作员名称
            bizExtend.setExtend25(vo.getOperatorName());
            // 交易币种
            bizExtend.setExtend26(vo.getTransCurrency());
        } else if (EventIncSource.Service.getCode().equals(vo.getEventSource())) {// 客服特有字段
            // 业务大类
            bizExtend.setExtend13(vo.getBusinessCategory());
            // 业务小类
            bizExtend.setExtend14(vo.getBusinessSubCategory());
            // 工单内容
            bizExtend.setExtend15(vo.getWorkContent());
            /****新建新增流程日志和催单日志*****/
            // 流程日志
            bizExtend.setExtend32(vo.getProcessLog());
            // 催单日志
            bizExtend.setExtend33(vo.getReminderLog());
            // 附件路径
            bizExtend.setExtend34(vo.getFiles());
            if(StringUtils.isNotEmpty(vo.getFiles())) {
                // 如果附件
                this.saveServiceImageFile(bizId, vo.getFiles());
            }
            // 客户信息
            bizExtend.setExtend35(vo.getCustInfo());
        } else if (EventIncSource.Monitor.getCode().equals(vo.getEventSource())) {// 监控特有字段
            // 关联变更id
            bizExtend.setExtend13(vo.getChangeNo());
            // 是否自愈
            bizExtend.setExtend14(vo.getSelfHealing());
            // 自愈脚本
            bizExtend.setExtend15(vo.getSelfDesc());
            // 派单类型
            bizExtend.setExtend16(vo.getDispatchType());
        }else if (EventIncSource.MobileService.getCode().equals(vo.getEventSource())) {
            // E事通特有字段
            //涉及交易
            bizExtend.setExtend13(vo.getInvolveBusiness());
            //错误信息
            bizExtend.setExtend14(vo.getErrorMsg());
            //故障现象
            bizExtend.setExtend15(vo.getFaultDesc());
            //是否涉及客户
            bizExtend.setExtend16(vo.getInvolveCust());
            //客户账号
            bizExtend.setExtend17(vo.getCustAccount());
            //客户名称
            bizExtend.setExtend18(vo.getCustName());
            //客户证件号
            bizExtend.setExtend19(vo.getCustNumber());
            //操作时间
            bizExtend.setExtend20(vo.getOperateTime());
            // 涉及金额
            bizExtend.setExtend21(vo.getInvolveAmount());

            String eventInfo=vo.getEvenTdesc();
            String[] eventInfos=eventInfo.split("\\|");
            if(null!=eventInfos && eventInfos.length>0) {
                bizExtend.setExtend8(eventInfos[0]);
            }
        }

        itBizExtendMapper.insert(bizExtend);
        return AjaxResult.success();
    }

    /**
     * 保存业务扩展信息  告警
     *
     * @param vo
     */
    private void saveBizExtendInfoMonitor(EventForeignVo vo, Long bizId, String bizNo) {
        ItBizExtend bizExtend = new ItBizExtend();
        bizExtend.setBizId(bizId);
        bizExtend.setBizNo(bizNo);
        bizExtend.setBizType(code);
        // 事件单来源
        bizExtend.setSourceType(vo.getSourceType());
        // 告警信息id
        bizExtend.setExtend2(vo.getAlarmId());
        // 标签信息
        bizExtend.setExtend3(vo.getBatchLabel());
        itBizExtendMapper.insert(bizExtend);
    }

    private Map<String, Object> packageProcessCondition(EventForeignVo vo) {
        Map<String, Object> map = new HashMap<>();
        // 监控自动分派
        if (EventIncSource.Monitor.getCode().equals(vo.getSourceType())) {
            // 流程走到接单环节
            map.put("reCode", "1");
            map.put("reveiveGroupId", vo.getAssignedGroup());
        } else {
            // 流程走到IT服务台
            map.put("reCode", "0");
            map.put("receptionId", vo.getAssignedGroup());
        }
        return map;
    }

    /**
     * 封装返回结果
     *
     * @param bizMap
     * @param bizExtend
     * @return
     */
    private EventIncSearchRespVo collectSearchResult(Map<String, Object> bizMap, ItBizExtend bizExtend) {
        EventIncSearchRespVo searchRespVo = new EventIncSearchRespVo();
        if (bizExtend != null) {
            // 交易网点号
            searchRespVo.setTransInstno(bizExtend.getExtend7());
            // 操作员号
            searchRespVo.setTransTeller(bizExtend.getExtend24());
            // 操作员名称
            searchRespVo.setTransTellerName(bizExtend.getExtend25());
            // 柜面事件单号
            searchRespVo.setEventNo(bizExtend.getExtend2());
            // 应用系统名称
            searchRespVo.setSystemName(bizExtend.getExtend4());
            // 上报时间
            searchRespVo.setTransDate(bizExtend.getExtend3());
            // 报障的来源
            searchRespVo.setEventSource(bizExtend.getSourceType());
            // 评价内容
            searchRespVo.setEvenTenvaluation(bizExtend.getExtend32());
            // 上报人机构
            searchRespVo.setReportOrg(bizExtend.getExtend7());
            // 上报人姓名
            searchRespVo.setReportPerson(bizExtend.getExtend9());
            // 上报人联系方式
            searchRespVo.setReportPhone(bizExtend.getExtend10());
            // 上报人地址
            searchRespVo.setReportAddress(bizExtend.getExtend11());
            // 紧急程度
            searchRespVo.setEmergencyLevel(bizExtend.getExtend12());
            // 柜员钱箱
            searchRespVo.setCashBoxNo(bizExtend.getExtend13());
            // 交易码
            searchRespVo.setTransCode(bizExtend.getExtend14());
            // 交易名称
            searchRespVo.setTransBusName(bizExtend.getExtend15());
            // 交易流水
            searchRespVo.setTransSerialNo(bizExtend.getExtend16());
            // 交易类型
            searchRespVo.setTransType(bizExtend.getExtend17());
            // 借方账号
            searchRespVo.setDebitNo(bizExtend.getExtend18());
            // 贷方账号
            searchRespVo.setCreditNo(bizExtend.getExtend19());
            // 交易金额
            searchRespVo.setTransAmount(bizExtend.getExtend20());
            // N天内的异常事件
            searchRespVo.setExceptionCount(bizExtend.getExtend22());
            // 预计处理时长
            searchRespVo.setExpectProcessTime(bizExtend.getExtend23());
            // 交易币种
            searchRespVo.setTransCurrency(bizExtend.getExtend26());
            // 确认标识
            searchRespVo.setConfirmFlag(bizExtend.getExtend27());
            // 评价分数
            searchRespVo.setEvaluationScore(bizExtend.getExtend38());
            // 评价标签
            searchRespVo.setEvaluateLabel(bizExtend.getExtend39());
            // 拒绝理由｜评价内容[意见及建议]
            searchRespVo.setEnvaluationDesc(bizExtend.getExtend40());
        }
        // IM事件单号
        searchRespVo.setImCode((String) bizMap.get("imCode"));
        // 解决方案
        searchRespVo.setEventResolution((String) bizMap.get(EventFieldConstants.SOLVE_PLAN));
        // 事件标题
        searchRespVo.setEvenTile((String) bizMap.get(EventFieldConstants.EVENT_TITLE));
        // 事件详述
        searchRespVo.setEvenTdesc((String) bizMap.get(EventFieldConstants.EVENT_INFO));
        // 总分行事件标识
        searchRespVo.setEvenHeadBranch((String) bizMap.get(EventFieldConstants.ORG_FLAG));

        // 中心侧/网点侧事件标识
        searchRespVo.setEventCenterNet((String) bizMap.get(EventFieldConstants.SIDE_FLAG));
        // 事件优先级
        searchRespVo.setEventPriority((String) bizMap.get(EventFieldConstants.EVENT_PRIORITY));
        // 事件影响面
        searchRespVo.setEventFactor((String) bizMap.get(EventFieldConstants.INF_FACE));
        // 事件级别
        searchRespVo.setEventLevel((String) bizMap.get(EventFieldConstants.EVENT_LEVEL));
        // 事件原因
        searchRespVo.setEventReasonType((String) bizMap.get(EventFieldConstants.EVENT_REASON_CATEGORY));
        // 事件当前受派组
        searchRespVo.setEventAssigneegrp((String) bizMap.get(EventFieldConstants.ASSIGNED_GROUP));
        if(StringUtils.isNotEmpty(bizMap.get(EventFieldConstants.ASSIGNED_GROUP))) {
            OgGroup ogGroup = workService.selectOgGroupById((String) bizMap.get(EventFieldConstants.ASSIGNED_GROUP));
            if(ogGroup != null) {
                searchRespVo.setEventAssigneegrp(ogGroup.getGrpName());
            }
        }
        // 事件当前受派人
        String assignedPerson = (String) bizMap.get(EventFieldConstants.ASSIGNED_PERSON);
        searchRespVo.setEventAssignee((String) bizMap.get(EventFieldConstants.ASSIGNED_PERSON));
        if(StringUtils.isNotEmpty(assignedPerson)) {
            OgPerson ogPerson = ogPersonService.selectOgPersonByPid(assignedPerson);
            if(ogPerson != null) {
                searchRespVo.setEventAssignee(ogPerson.getpName());
            }
        }
        // 事件状态
        String status = (String) bizMap.get("status");
        searchRespVo.setEventStatus(status);
        // 状态理由
        //searchRespVo.setEventStatusReason(EventStatusEnum.getStatusName(status));
        // 关闭代码
        searchRespVo.setEventcCloseCode((String) bizMap.get(EventFieldConstants.CLOSE_CODE));
        // 所属系统
        searchRespVo.setBelongSystem((String) bizMap.get(EventFieldConstants.SYSTEM_NAME));
        if(StringUtils.isNotEmpty(bizMap.get(EventFieldConstants.SYSTEM_NAME))) {
            OgSys ogSys = applicationManagerService.selectOgSysBySysCode((String) bizMap.get(EventFieldConstants.SYSTEM_NAME));
            if(ogSys != null) {
                searchRespVo.setBelongSystem(ogSys.getCaption());
            }
        }

        // 事件开启时间
        Date createdTime = (Date) bizMap.get("created_time");
        searchRespVo.setEventReporttime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, createdTime));

        IncidentSubEvent incidentSubEvent = subEventService.selectIncidentSubEventByEventNo(searchRespVo.getImCode());
        if(incidentSubEvent != null) {
            // 事件解决时间
            searchRespVo.setEventResolvetime(incidentSubEvent.getSolveTime());
            // 事件关闭时间
            searchRespVo.setEventCloseTime(incidentSubEvent.getCloseTime());
            // 计算处理时长=解决时间-开始时间
            if(StringUtils.isNotEmpty(incidentSubEvent.getSolveTime())) {
                Date closeDate = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, incidentSubEvent.getSolveTime());
                searchRespVo.setProcessTime(DateUtils.getDatePoor(closeDate, createdTime));
            }

            // 挂起标识
            searchRespVo.setEventPendmark("1".equals(incidentSubEvent.getSuspendFlag()) ? "Y" : "N");
            // 客户撤单标识
            searchRespVo.setEventCancelFlag("1".equals(incidentSubEvent.getCancelFalg()) ? "Y" : "N");
            // 客户撤单时间
            searchRespVo.setEventCancelTime(incidentSubEvent.getCancelTime());
            // 客户加急标识
            searchRespVo.setEventExpeditedFlag("1".equals(incidentSubEvent.getUrgeFlag()) ? "Y" : "N");
        }

        return searchRespVo;
    }

    // 接口发起设置自定义模块拦截器用户信息默认一个外接系统的账号ID
    private void setCustomerBizInterceptorOgUser(String userId) {
        OgUser user = ogUserService.selectOgUserByUserId(userId);
        CustomerBizInterceptor.currentUserThread.set(user);
    }

    public static void main(String[] args) throws DocumentException {
        /*String xml = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <soap:Body>\n" +
                "        <ns2:updatePdDataFromBMCResponse xmlns:ns2=\"http://impl.service.wpwebService\">\n" +
                "            <return>\n" +
                "                <msgCode>pd00</msgCode>\n" +
                "                <msgInfo>成功！</msgInfo>\n" +
                "            </return>\n" +
                "        </ns2:updatePdDataFromBMCResponse>\n" +
                "    </soap:Body>\n" +
                "</soap:Envelope>";
        Document doc = DocumentHelper.parseText(xml);
        Element rootElt = doc.getRootElement(); // 获取根节点
        System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称 DATA
        Element body = rootElt.element("Body");
        Element updatePdDataFromBMCResponse = body.element("updatePdDataFromBMCResponse");
        List<Element> elements = updatePdDataFromBMCResponse.elements();
        for (Element e : elements) {
            System.out.println(e.elementTextTrim("msgCode"));
            System.out.println(e.elementTextTrim("msgInfo"));
        }*/
        String s = "{\n" +
                "    \"code\": 0,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"detail\": \"\",\n" +
                "            \"msg\": \"success\",\n" +
                "            \"status\": \"\",\n" +
                "            \"value\": \"[\n" +
                "\t\t\t\t{\\\"evt_id\\\":\\\"IM2022061722192\\\",\\\"evt_title\\\":\\\"特急 客服派单号:220616972924 上行快线 E账户\\\",\\\"evt_desc\\\":\\\"核身通过：是\\\\ne账号：623185009126427506\\\\n开户渠道：上行快线\\\\n新手机号（更改绑定手机号使用）：19101671896\\\\n情况描述：上行快线\\\\n客户要求：客户要求更改手机号，客户自行修改登录手机号码时，提示“该手机已注册”，原登陆手机号：18930618769，客户急着给孩子交学费，客户要求今天处理，我处未承诺，烦请处理，谢谢\\\\n联系电话：19101671896   \\\\n小孩： 姓名：肖梦洋   学校： 上南中学  身份证： 310115200602022928\\\\n注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2022-06-17 12:18:21\\\",\\\"evt_solution\\\":\\\"请联系网络金融部郭海晖老师\\\",\\\"solve_person\\\":\\\"郑子熠(zhengzy) .\\\",\\\"top_pod\\\":0.7200000286},\n" +
                "\t\t\t\t{\\\"evt_id\\\":\\\"IM2022030395808\\\",\\\"evt_title\\\":\\\"加急 客服派单号:220223466948 上行快线 E账户\\\",\\\"evt_desc\\\":\\\"核身通过：是\\\\ne账号：\\\\n开户渠道：\\\\n新绑定银行卡（修改绑定卡使用）：\\\\n新手机号（更改绑定手机号使用）：\\\\n情况描述：客户来电表示今天通过上行快线小程序，给小孩做教育缴费业务，但是在身份证上传时，一直出现页面乱码，无法上传。客户表其他家长也有这样，烦请后台核实处理。\\\\n身份证号310108198710291566\\\\n联系电话：13916203441\\\\n缴费信息   洋泾菊园  澳洲护照  PB1502214\\\\n\\\\n注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2022-03-03 08:53:44\\\",\\\"evt_solution\\\":\\\"乱码是因为某次换版的文件编码格式有问题，其实不影响业务流程。反馈已有相同事件跟踪，本事件做关闭处理。\\\",\\\"solve_person\\\":\\\"常金钗(changjch) .\\\",\\\"top_pod\\\":0.7099999785},\n" +
                "\t\t\t\t{\\\"evt_id\\\":\\\"IM2021090726127\\\",\\\"evt_title\\\":\\\"一般 客服派单号:210903927524 上行快线 E账户\\\",\\\"evt_desc\\\":\\\"核身通过：是e账号：623185009081977081开户渠道：主账户产品新手机号（更改绑定手机号使用）：13052061530客户要求：客户绑定预留手机号为空，要求更改预留手机号联系电话：13052061530注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-09-07 10:55:13\\\",\\\"evt_solution\\\":\\\"请用户上行快线APP或者上行快线微版小程序操作。\\\",\\\"solve_person\\\":\\\"常金钗(changjch) .\\\",\\\"top_pod\\\":0.6999999881},{\\\"evt_id\\\":\\\"IM2021080612476\\\",\\\"evt_title\\\":\\\"加急 客服派单号:210806560862 上行快线 功能菜单\\\",\\\"evt_desc\\\":\\\"核身通过：是e账号：623185008201112322开户渠道：\\/新绑定银行卡（修改绑定卡使用）：\\/新手机号（更改绑定手机号使用）：\\/情况描述：   客户表示要求将名下理财 ：添利日添金产品  全部赎回，客户表示在上行快线APP上无法操作，今天一直在尝试，烦请处理，客户着急赎回，谢谢，客户要求尽快回复处理，表示如有问题请联系电话：18227600300注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-08-06 15:21:48\\\",\\\"evt_solution\\\":\\\"客户申请结单，关闭操作\\\",\\\"solve_person\\\":null,\\\"top_pod\\\":0.6899999976},\n" +
                "\t\t\t\t{\\\"evt_id\\\":\\\"IM2021120663557\\\",\\\"evt_title\\\":\\\"特急 客服派单号:211204348745 上行快线 E账户\\\",\\\"evt_desc\\\":\\\"核身通过：是\\\\ne账号：623185009141298965\\\\n开户渠道：主帐户产品\\\\n新绑定银行卡（修改绑定卡使用）：\\\\n新手机号（更改绑定手机号使用）：\\\\n情况描述：\\\\n客户要求：客户之前都是通过上行快线微版小程序教育缴费，这次换了一部苹果13的手机登陆，系统提示需要客户上传身份证和脸部识别，但客户尝试了很多很多次就是无法通过，客户也尝试了换回原来苹果11的手机，仍然如此，客户最晚缴费时间是12\\/5，希望我行尽快想办理解决，麻烦核实具体原因\\\\n联系电话：13918696408\\\\n\\\\n注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-12-06 14:26:29\\\",\\\"evt_solution\\\":\\\"查询发现用户是上行快线微版小程序操作的，请使用APP端重新试试。\\\",\\\"solve_person\\\":\\\"赵帅(zhaoshuai) .\\\",\\\"top_pod\\\":0.6700000167},\n" +
                "\t\t\t\t{\\\"evt_id\\\":\\\"IM2021072607317\\\",\\\"evt_title\\\":\\\"PAD移动服务银行升级迁移\\\",\\\"evt_desc\\\":\\\"PAD移动服务银行升级迁移\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-07-26 15:01:50\\\",\\\"evt_solution\\\":\\\"PAD移动服务银行升级迁移\\\",\\\"solve_person\\\":\\\"杨军利(yangjl1) .\\\",\\\"top_pod\\\":0.6600000262},\n" +
                "\t\t\t\t{\\\"evt_id\\\":\\\"IM2021052079368\\\",\\\"evt_title\\\":\\\"一般 客服派单号:210520472706 短信 短信对应账户查询\\\",\\\"evt_desc\\\":\\\"来电号码：18810080991 手机号码：18810080991 短信内容：验证码    尊敬的客户，您的企业网银操作员预留手机号已更改，若有疑问请及时联系网银管理员。    18810080991    2021-05-20 09:51:23    2021-05-20 09:51:28    验证码    动态密码：625655(6分钟内有效)。您正在登录上海银行企业网银身份认证,如非本人操作，请及时联系我行客服热线95594.    18810080991    2021-05-20 08:58:03    2021-05-20 08:58:08 收到短信日期：2021-05-20 08:58:03；2021-05-20 09:51:23 客户要求：客户表示突然收到我行信息，关于企业网银，客户表示之前在我行有开立企业，但三年未使用，为何现在会收到此类短信，要求确认，并要求电话回复，未承诺（备注：删除不需要项非本人短信）非本人短信时，如来电人坚持不愿留下个人姓名和证件号码，请留下姓氏称谓，比如X先生，待后续处理。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-05-20 13:38:51\\\",\\\"evt_solution\\\":\\\"北京傲翼网络科技有限公司的操作员80057579702绑定的手机号18810080991，应该是在登陆的时候发送的短信，该企业已经手机号更改，后续不会再发送此类短信的\\\",\\\"solve_person\\\":\\\"李飞(o_lifei) .\\\",\\\"top_pod\\\":0.6399999857},{\\\"evt_id\\\":\\\"IM2022020887797\\\",\\\"evt_title\\\":\\\"特急 客服派单号:220208277720 上行快线 E账户\\\",\\\"evt_desc\\\":\\\"核身通过：是\\/否\\\\ne账号：\\/\\\\n开户渠道：微信小程序\\\\n新绑定银行卡（修改绑定卡使用）：\\\\n新手机号（更改绑定手机号使用）：\\\\n情况描述：客户在微信小程序上银微服务做教育缴费使用我行卡622892002005827892开户失败报错提示“ 开通E账户出现异常”，客户急需缴费学费，要求今天处理请确认\\\\n客户要求：开户缴费\\\\n联系电话：18917131184\\\\n\\\\n注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2022-02-08 12:56:29\\\",\\\"evt_solution\\\":\\\"客户申请结单，关闭操作已经没有问题了 请联系客户后再试\\\",\\\"solve_person\\\":\\\"胡杰(hujie5) .\\\",\\\"top_pod\\\":0.6399999857},\n" +
                "\t\t\t\t{\\\"evt_id\\\":\\\"IM2021052481188\\\",\\\"evt_title\\\":\\\"一般 客服派单号:210518455854 上行快线 E账户\\\",\\\"evt_desc\\\":\\\"核身通过：是e账号：623185009349334679开户渠道：上行快线新绑定银行卡（修改绑定卡使用）：\\/新手机号（更改绑定手机号使用）：\\/情况描述：客户来电表示自己上行快线账户已销户，今天通过微信小程序重新注册开通的时候提示“该身份已使用另一手机注册”，无法注册，但是使用13482872911的号码登陆到上行快线APP或微信小程序中显示的信息还是本人的信息，进行信息完善时身份验证又无法通过，提示信息不一致，客户着急教育缴费，要求确认原因以及如何重新开户，烦请处理。注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-05-24 16:18:12\\\",\\\"evt_solution\\\":\\\"销户没删干净，请联系网金部张丽萍老师\\\",\\\"solve_person\\\":\\\"凌元操(lingyc) .\\\",\\\"top_pod\\\":0.6399999857},{\\\"evt_id\\\":\\\"IM2021111052568\\\",\\\"evt_title\\\":\\\"一般 客服派单号:211031799848 上行快线 E账户\\\",\\\"evt_desc\\\":\\\"核身通过：是\\\\ne账号：623185009143870779\\\\n开户渠道：上行快线微信小程序\\\\n新绑定银行卡（修改绑定卡使用）：\\/\\\\n新手机号（更改绑定手机号使用）：\\/\\\\n情况描述：客户想要操作教育缴费，通过微信小程序开户一直无法开通，上传身份证后无法通过，再次上传时提示重复上传，后我处通过互金查询到客户已经开户成功，建议客户登陆上行快线APP进行操作缴费，但客户表示登陆上行快线后无法查询到E帐户信息，点击E帐户账号就提示开户，我处已经让客户把操作界面进行截图发送至后台邮箱，烦请确认。\\\\n客户要求：要求开户后进行教育缴费。\\\\n联系电话：\\\\n\\\\n注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-11-10 16:26:39\\\",\\\"evt_solution\\\":\\\"客户申请结单，关闭操作请联系网络金融部赵晶晶处理。\\\",\\\"solve_person\\\":\\\"夏立(xiali1) .\\\",\\\"top_pod\\\":0.6399999857},{\\\"evt_id\\\":\\\"IM2021061791667\\\",\\\"evt_title\\\":\\\"加急 客服派单号:210611760322 上行快线 功能菜单\\\",\\\"evt_desc\\\":\\\"核身通过：是e账号：623185009570119617开户渠道：主账户产品新绑定银行卡（修改绑定卡使用）：\\/新手机号（更改绑定手机号使用）：\\/情况描述：见派单“210527555574”客户表示自己因从事医生行业，故电话接听不是随时方便，且孩子餐费已经3个月未缴纳，校方催促，客户不满，望我行后台节后第一时间回复客户要求：平时12：30-13:00左右方便接听，如若未接听到，烦请将处理方式告知客服，方便客户后续来电可以知晓解决方案，未承诺联系电话：13818310667注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-06-17 14:42:44\\\",\\\"evt_solution\\\":\\\"学校上传的订单文件里，有空格，导致李旭宸同学有两条学生信息，查询报错。需要修数处理，请联系网金部汪然老师知悉。\\\",\\\"solve_person\\\":\\\"常金钗(changjch) .\\\",\\\"top_pod\\\":0.6299999952},\n" +
                "\t\t\t\t{\\\"evt_id\\\":\\\"IM2022022192030\\\",\\\"evt_title\\\":\\\"一般 客服派单号:220217403668 上行快线 E账户\\\",\\\"evt_desc\\\":\\\"核身通过：\\/\\\\ne账号：\\/\\\\n开户渠道：上行快线微版小程序\\\\n新绑定银行卡（修改绑定卡使用）：\\/\\\\n新手机号（更改绑定手机号使用）：\\/\\\\n情况描述：客户来电表示自己在上行快线微版小程序注册账户已经注册好了，而后自己在绑卡的时候有报错，报错信息为“银行卡鉴权异常：认证信息不匹配“绑定的卡片为我行借记卡，卡号为620522001004710382。\\\\n客户要求：客户希望绑卡，且想知道为什麽原因。烦请老师处理谢谢。\\\\n联系电话：13761311658\\\\n\\\\n注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2022-02-21 16:43:36\\\",\\\"evt_solution\\\":\\\"[补充信息]请提供操作时间 及操作手机号 22 21日均无日志\\\",\\\"solve_person\\\":\\\"朱雯(zhuwen2) .\\\",\\\"top_pod\\\":0.6299999952},\n" +
                "\t\t\t\t{\\\"evt_id\\\":\\\"IM2021052682331\\\",\\\"evt_title\\\":\\\"加急 客服派单号:210518454121 上行快线 功能菜单\\\",\\\"evt_desc\\\":\\\"核身通过：是e账号：623185009385792897开户渠道：主帐户新绑定银行卡（修改绑定卡使用）：\\/新手机号（更改绑定手机号使用）：\\/情况描述：客户来电表示通过上行快线APP和上行快线微信小程序进行“上海市洋泾—菊园实验学校”的教育缴费，缴费时一直提示其“短信发送异常”无法正常接收到短信，客户登陆上行快线APP时的验证码是可以正常接收的，客户对此非常不满，要求我行查询原因进行处理，报错截图已让客户发送至邮箱，客户比较着急，要求加急处理，烦请核实处理。客户要求：\\/联系电话：13601664785注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-05-26 17:55:51\\\",\\\"evt_solution\\\":\\\"客户申请结单，关闭操作[补充信息]短信发送异常是用户做协议支付时发送的，后台没查到具体记录，请提供下具体操作时间吧\\\",\\\"solve_person\\\":\\\"朱雯(zhuwen2) .\\\",\\\"top_pod\\\":0.6299999952},{\\\"evt_id\\\":\\\"IM2021041463776\\\",\\\"evt_title\\\":\\\"一般 客服派单号:210407840319 上行快线 落账处理\\\",\\\"evt_desc\\\":\\\"核身通过：是e账号：623185009112450991开户渠道：甜橙新绑定银行卡（修改绑定卡使用）：新手机号（更改绑定手机号使用）：情况描述：客户来电反馈自己于3\\/23有转出5笔资金 ，但是最后一笔2万一直显示处理中 ，截至今日还未有金额入账到银行卡，要求核实金额处理情况以及告知为何会有这个情况发生客户要求：要求核实金额处理情况以及告知为何会有这个情况发生，要求电话回复 ，烦请核实处理 联系电话：13956161044注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-04-14 10:45:04\\\",\\\"evt_solution\\\":\\\"客户申请结单，关闭操作运营管理部哪位老师请直接找运营部的老师吧\\\",\\\"solve_person\\\":\\\"韩怀柱(hanhzh_s) .\\\",\\\"top_pod\\\":0.6200000048},{\\\"evt_id\\\":\\\"IM2021112357908\\\",\\\"evt_title\\\":\\\"特急 客服派单号:211116118491 上行快线 E账户\\\",\\\"evt_desc\\\":\\\"核身通过：是\\\\ne账号：623185009138016313\\\\n开户渠道：教育缴费\\\\n新绑定银行卡（修改绑定卡使用）：\\\\n新手机号（更改绑定手机号使用）：\\\\n情况描述：客户通过上行快线微版小程序无法实名认证缴费，显示认证失败，请稍后重试，客户要求我处协助客户认证以及操作缴费，学生姓名陆奕呈  金洲小学  310107201111235712   \\\\n客户要求：客户要求我处协助客户认证以及操作缴费，客户要求今天回复，今天是缴费截至日\\\\n电话 \\\\n13816845316\\\\n注：涉及账户操作类的回访选“否”。\\\",\\\"evt_resource\\\":3,\\\"submit_date\\\":\\\"2021-11-23 17:03:33\\\",\\\"evt_solution\\\":\\\"客户申请结单，关闭操作人脸识别失败后台没法查询具体的原因，还是需要客户在保持光线充足的场景下多试几次。\\\",\\\"solve_person\\\":\\\"张宇阳(zhangyy9) .\\\",\\\"top_pod\\\":0.6200000048}\n" +
                "\t\t\t]\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        JSONObject map = JSONObject.parseObject(s);
        Integer code = (Integer) map.get("code");
        JSONArray data = (JSONArray) map.get("data");
        JSONObject o = (JSONObject) data.get(0);
        String valueStr = (String) o.get("value");
        List<Map> values = JSONObject.parseArray(valueStr, Map.class);
        values.forEach(v -> {
            System.out.println(v.getClass());
            System.out.println(v);
        });

        System.out.println("code=" + code + "\r\n" + "values=" + values);
    }

    /**
     * updatePdDataFromBMCResponse
     * updatePdBackResponse
     *
     * @param xml
     * @param elName
     * @return
     */
    public Map<String, String> convertXml(String xml, String elName) {
        Map<String, String> result = new HashMap<>();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
            Element rootElt = doc.getRootElement(); // 获取根节点
            log.info("根节点：" + rootElt.getName()); // 拿到根节点的名称 DATA
            Element body = rootElt.element("Body");
            Element updatePdDataFromBMCResponse = body.element(elName);
            List<Element> elements = updatePdDataFromBMCResponse.elements();
            for (Element e : elements) {
                result.put("msgCode", e.elementTextTrim("msgCode"));
                result.put("msgInfo", e.elementTextTrim("msgInfo"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return result;
    }

    public AjaxResult selectIncidentCloseByPost(String postId) {
        Map<String, String> map = new HashMap<>();
        map.put("postId", postId);
        List<OgPerson> personList = ogPersonService.selectPersonByPostId(map);
        List<Map<String, String>> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(personList)) {
            for (OgPerson person : personList) {
                Map<String, String> m = new HashMap<>();
                m.put("value", person.getpName());
                m.put("label", person.getpId());
                resultList.add(m);
            }
        }
        return AjaxResult.success(resultList);
    }
    
    @Override
    public Map<String, Object> getAlertListByItsmId(String itsmId) {
        log.info("-----getAlertListByItsmId 参数: {}",itsmId);
        if(StringUtils.isEmpty(itsmId)) {
            return AjaxResult.error("工单号不能为空");
        }
        AjaxResult ar=AjaxResult.success();
        String url = monitorService+"/refiner/api/itsm/getAlertListByItsmId";
        List<FormAlert> alertList = null;
        try {
            //请求参数
            Map<String, String> body = new HashMap<>();
            //body.put("itsmId", itsmId);
            url += "?itsmId=" + itsmId;
            log.info("----调用告警查询列表接口getAlertListByItsmId开始，url:{}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> entity = restTemplate.postForEntity(url, httpEntity, String.class);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                log.error("根据工单号[{}]查询对应告警失败：", entity);
                return AjaxResult.error("获取告警失败");
            }
            String resultStr = entity.getBody();
            JSONObject result = JSON.parseObject(resultStr);
            log.info("工单号[{}], 告警：{}", itsmId, resultStr);

            //0000为成功
            if (!"0000".equals(result.get("retCode"))) {
                log.error("根据工单号[{}]查询对应告警失败：", entity);
                return AjaxResult.error("获取告警失败："+result.getString("retMsg"));
            }
            alertList = JSONObject.parseArray(result.get("entity").toString(), FormAlert.class);
            StringBuilder sbl = new StringBuilder("数据运行情况：");
            sbl.append("\n");
            int dispose=0;
            int close=0;
            int processing=0;
            int resolved=0;
            int other=0;
            for (Iterator iterator = alertList.iterator(); iterator.hasNext();) {
                FormAlert formAlert = (FormAlert) iterator.next();
                String statusStr = "";
                if("dispose".equals(formAlert.getStatus())) {
                    dispose++;
                    statusStr = "待处理";
                }else if("closed".equals(formAlert.getStatus())) {
                    close++;
                    statusStr = "已关闭";
                }else if("processing".equals(formAlert.getStatus())) {
                    processing++;
                    statusStr = "处理中";
                }else if("resolved".equals(formAlert.getStatus())) {
                    resolved++;
                    statusStr = "已解决";
                }else {
                    other++;
                }
                formAlert.setStatus(statusStr);
                String severityStr = "";
                Integer severity = formAlert.getSeverity();
                if(severity != null) {
                    switch (severity) {
                        case 0:
                            severityStr = "恢复";
                            break;
                        case 20:
                            severityStr = "通知";
                            break;
                        case 30:
                            severityStr = "次要";
                            break;
                        case 40:
                            severityStr = "一般";
                            break;
                        case 50:
                            severityStr = "严重";
                            break;
                        case 60:
                            severityStr = "紧急";
                            break;
                    }
                }
                formAlert.setSeverityStr(severityStr);
                if(StringUtils.isNotEmpty(formAlert.getBeginTime())) {
                    formAlert.setBeginTimeStr(DateUtils.convertLongTimeString(formAlert.getBeginTime()));
                }
            }
            sbl.append("待处理:").append(dispose).append("\n");
            sbl.append("已关闭:").append(close).append("\n");
            sbl.append("处理中:").append(processing).append("\n");
            sbl.append("已解决:").append(resolved).append("\n");
            //删除以前的数据
            /*QueryWrapper<FormAlert> faWrapper = new QueryWrapper<FormAlert>();
            this.formAlertService.remove(faWrapper);

            formAlertService.saveBatch(alertList);*/

            ar=AjaxResult.success(alertList);
            ar.put("info", sbl.toString());
        } catch (Exception e) {
            e.printStackTrace();
			log.error("根据工单号查询对应告警异常：", e);
            /*return AjaxResult.error("获取告警异常！");*/
            StringBuilder sbl = new StringBuilder("【测试数据】监控数据情况：");
            sbl.append("待处理:").append(1).append("\n");
            sbl.append("已关闭:").append(2).append("\n");
            sbl.append("处理中:").append(3).append("\n");
            sbl.append("已解决:").append(4).append("\n");
            alertList = new ArrayList<>();
            FormAlert formAlert = new FormAlert(1233333L, "告警对象", "告警内容", 40, "一般", 1668993109814L, "2022-11-25 15:31:35", "已解决");
            alertList.add(formAlert);
            ar=AjaxResult.success(alertList);
            ar.put("info", sbl.toString());
        }

        return ar;
    }

	@Override
	public Map<String, Object> closeAlertByItsmId(Map<String, Object> params) {
		log.info("---- closeAlertByItsmId 根据工单号关闭对应告警 参数: {}",params);
		if(StringUtils.isEmpty(params.get("itsmId"))) {
        	return AjaxResult.error("工单号不能为空");
        }
		if(StringUtils.isEmpty(params.get("solution"))) {
        	return AjaxResult.error("解决方案不能为空");
        }
		if(StringUtils.isEmpty(params.get("handler"))) {
        	return AjaxResult.error("处理人不能为空");
        }
		if(StringUtils.isEmpty(params.get("closeTime"))) {
        	return AjaxResult.error("关单时间不能为空");
        }
        
        String url = monitorService+"/refiner/api/itsm/closeAlertByItsmId";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(params, headers);
            ResponseEntity<JSONObject> entity = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
            log.info("根据工单号关闭对应告警 结果: {}", entity);
            if (!entity.getStatusCode().is2xxSuccessful()) {
            	log.error("根据工单号[{}]关闭对应告警失败：", entity);
            	return AjaxResult.error("关闭告警失败");
            } 
            JSONObject result = entity.getBody();
            //0000为成功
            if (!"0000".equals(result.get("retCode"))) {
            	log.error("根据工单号[{}]关闭对应告警失败：", entity);
            	return AjaxResult.error("关闭告警失败："+result.getString("retMsg"));
            } 
		} catch (Exception e) {
			log.error("根据工单号关闭对应告警异常：", e);
			return AjaxResult.error("关闭告警异常！");
		}
        
        return AjaxResult.success();
	}

	@Override
    public Map<String, Object> queryEventByState(String jsonData) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = JSON.parseObject(jsonData);
        log.info("---queryEventByState方法，查询参数 data:{}", data);
        //workNo\eventStatus\timeszone
        String workNo = String.valueOf(data.get("workNo"));
        String eventStatus = String.valueOf(data.get("eventStatus"));
        String startTime = String.valueOf(data.get("startTime"));
        String endTime = String.valueOf(data.get("endTime"));
        String pageNum = String.valueOf(data.get("pageNum"));
        String pageSize = String.valueOf(data.get("pageSize"));
        EventIncSearchReqVo vo = new EventIncSearchReqVo();
        int amount=0;
        log.info("根据workNo查询数据参数: {}", data);
        if (StringUtils.isEmpty(workNo)) {
            return AjaxResult.error("工号不能为空");
        }
        if (StringUtils.isEmpty(eventStatus)) {
            return AjaxResult.error("状态不能为空");
        }else {
            List list = new ArrayList();
            if("1".equals(eventStatus)) {
                list.add("已分派");
                vo.setStatus(list);
                vo.setImStatus("待分派");
            }else if("2".equals(eventStatus)) {
                vo.setImStatus("3,6");
                list.add("进行中");
                vo.setStatus(list);
            }else if("3".equals(eventStatus)) {
                /*list.add("已关闭");*/
                list.add("已解决");
                vo.setStatus(list);
                vo.setImStatus("已关闭");
                vo.setStratTime(startTime);
                vo.setEndTime(endTime);
            }
            vo.setIsTimeFlag(1);
        }

//		if (StringUtils.isEmpty(timeszone)) {
//			return AjaxResult.error("时间区域不能为空");
//		}else {
//			if ("one".equals(timeszone)) {
//				amount=-1;
//			}else if ("two".equals(timeszone)) {
//				amount=-2;
//			}else if ("three".equals(timeszone)) {
//				amount=-3;
//			}
//		}
//		String end=DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
//		String start=DateUtils.getAddDay(end, DateUtils.DEFAULT_MONTH, amount, DateUtils.YYYY_MM_DD_HH_MM_SS);
//		工号  workNo
//		状态 eventStatus
//		1.未处理:E事通发起,未接单的事件单
//		2.处理中:E事通发起,已接单但未点击已解决的事件单
//		3.已完成:E事通发起,已关闭的事件单
//		时间区间 :
//		timesZone
//		one 最近一个月  two:最近两个月 three:最近三个月
//        vo.setStratTime(start);
//        vo.setEndTime(end);
        if(!StringUtils.isEmpty(pageNum) && !"null".equals(pageNum)) {
            vo.setPageNum(Integer.parseInt(pageNum));
        }
        if(!StringUtils.isEmpty(pageSize) && !"null".equals(pageSize)) {
            vo.setPageSize(Integer.parseInt(pageSize));
        }
        // 起始时间特殊处理
        if("null".equalsIgnoreCase(vo.getStratTime())) {
            vo.setStratTime("");
        }
        if("null".equalsIgnoreCase(vo.getEndTime())) {
            vo.setEndTime("");
        }
        if(vo.getPageNum() == null) {
            vo.setPageNum(1);
        }
        if(vo.getPageSize() == null) {
            vo.setPageSize(100);
        }
        vo.setPageStart((vo.getPageNum() - 1) * vo.getPageSize());
        vo.setTransTeller(workNo);
        Map<String, Object> resultMap = searchEvent(vo);
        List<EventIncSearchRespVo> resultList = (List<EventIncSearchRespVo>) resultMap.get("resultList");
        List<EventIncSearchVo> list= JSONArray.parseArray(JSON.toJSONString(resultList),EventIncSearchVo.class);
        Long count = (Long) resultMap.get("count");
//		List<Map<String, Object>> list = new ArrayList<>();
//		list = customeMapper.queryEventByState(workNo,state,start,end);
        result.put("total",count);
        result.put("rows", list);
        result.put("code", 0);
        result.put("msg", "查询成功！");
        return result;
    }

    @Override
    public Map<String, Object> queryEventById(String jsonData) {
        Map<String, Object> result = new HashMap<>();
        // eventNo
        Map<String, Object> data = JSON.parseObject(jsonData);
        log.info("---queryEventById方法，查询参数 data:{}", data);
        String eventNo = String.valueOf(data.get("eventNo"));
        String pageNum = String.valueOf(data.get("pageNum"));
        String pageSize = String.valueOf(data.get("pageSize"));
        EventIncSearchReqVo vo = new EventIncSearchReqVo();
        if(!StringUtils.isEmpty(pageNum) && !"null".equals(pageNum)) {
            vo.setPageNum(Integer.parseInt(pageNum));
        }
        if(!StringUtils.isEmpty(pageSize) && !"null".equals(pageSize)) {
            vo.setPageSize(Integer.parseInt(pageSize));
        }
        if(vo.getPageNum() == null) {
            vo.setPageNum(1);
        }
        if(vo.getPageSize() == null) {
            vo.setPageSize(100);
        }
        vo.setPageStart((vo.getPageNum() - 1) * vo.getPageSize());
        vo.setImCode(eventNo);
        Map<String, Object> resultMap = searchEvent(vo);
        List<EventIncSearchRespVo> resultList = (List<EventIncSearchRespVo>) resultMap.get("resultList");
        List<EventIncSearchInfoVo> list= JSONArray.parseArray(JSON.toJSONString(resultList),EventIncSearchInfoVo.class);
        Long count = (Long) resultMap.get("count");
//		list = customeMapper.queryEventById(eventNo);
        result.put("total", count);
        result.put("rows", list);
        result.put("code", 0);
        result.put("msg", "查询成功！");
        return result;
    }

    @Override
    public Map<String, Object> updateEventState(String jsonData) {
        Map<String, Object> result = new HashMap<>();
        //工单号 eventNo 是否完成 result  0:未解决  1:已解决  反馈内容: feedbackInfo
        Map<String, Object> data = JSON.parseObject(jsonData);
        log.info("---updateEventState方法，查询参数 data:{}", data);
        String eventNo = String.valueOf(data.get("eventNo"));
        String state = String.valueOf(data.get("result"));
        String feedbackInfo = String.valueOf(data.get("feedbackInfo"));
        EventIncConfirmVo vo = new EventIncConfirmVo();
        vo.setImCode(eventNo);
        String feedback=feedbackInfo;
        if("1".equals(state)) {
            vo.setConfirmFlag("Y");
            String[] pingjia=feedbackInfo.split("\\|");
            String pjV="";
            if(null!=pingjia &&pingjia.length>1) {
                pjV=pingjia[0];
                vo.setEvaluationScore(pjV);
                feedback=feedbackInfo.substring(feedbackInfo.indexOf("|")+1, feedbackInfo.length());
            }

        }else {
            vo.setConfirmFlag("N");
        }
        vo.setEnvaluationDesc(feedback);
        confirmEvent(vo);
        //int value = customeMapper.updateEventState(eventNo,state,feedbackInfo);
        Map<String, Object> ret = new HashMap<>();
        ret.put("imCode", eventNo);
        result.put("code", 0);
        result.put("msg", "反馈状态成功！");
        result.put("data", ret);
        return result;
    }

    /**
     * evt_title 概要描述信息
     * evt_desc 详细描述信息
     * evt_resource 来源
     * type=0 表示查询
     * @param jsonData
     * @return
     */
	@Override
	public Map<String, Object> queryPredict(String jsonData) {
		Map<String, Object> map= JSON.parseObject(jsonData);
		// 转换事件单来源码值
        String evt_resource = (String) map.get("evt_resource");
        switch (evt_resource) {
            case "1":
                evt_resource = "2";
                break;
            case "2":
                evt_resource = "3";
                break;
            case "3":
                evt_resource = "5";
                break;
            case "4":
                evt_resource = "6";
                break;
            case "5":
                evt_resource = "8";
                break;
            case "6":
                evt_resource = "9";
                break;
            case "7":
                evt_resource = "11";
                break;
            case "9":
                evt_resource = "1";
                break;
            case "10":
                evt_resource = "0";
                break;
            case "11":
                evt_resource = "4";
                break;
            case "12":
                evt_resource = "7";
                break;
            case "13":
                evt_resource = "10";
                break;
            case "14":
                evt_resource = "12";
                break;
            case "15":
                evt_resource = "13";
                break;
            case "16":
                evt_resource = "14";
                break;
        }
        map.put("evt_resource", evt_resource);
		log.info("根据NLP 参数: {}",map);
        String url = nlpService + "/predict";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
            ResponseEntity<String> entity = restTemplate.postForEntity(url, httpEntity, String.class);
            log.info("根据name、type、value查询NLP 结果: {}");
            if (!entity.getStatusCode().is2xxSuccessful()) {
            	log.error("根据name、type、value查询NLP失败：", entity);
            	return AjaxResult.error("根据name、type、value查询NLP失败");
            } 
            String result = entity.getBody();
            List<Map> list = transNlPResult(result);
            if(CollectionUtils.isEmpty(list)) {
                return AjaxResult.error("根据name、type、value查询NLP失败！");
            } else {
                return AjaxResult.success(list);
            }
        } catch (Exception e) {
			log.error("根据name、type、value查询NLP异常：", e);
			return AjaxResult.error("根据name、type、value查询NLP异常！");
		}
	}

	public List<Map> transNlPResult(String s) {
        JSONObject map = JSONObject.parseObject(s);
        Integer code = (Integer) map.get("code");
        if(code == 0) {
            JSONArray data = (JSONArray) map.get("data");
            JSONObject o = (JSONObject) data.get(0);
            String valueStr = (String) o.get("value");
            List<Map> values = JSONObject.parseArray(valueStr, Map.class);
            values.forEach(v -> {
                if(StringUtils.isNotEmpty(v.get("evt_resource"))) {
                    String evt_resource = (String)v.get("evt_resource");
                    // 翻译事件单来源
                    switch (evt_resource) {
                        case "0":
                            evt_resource = "电话";
                            break;
                        case "1":
                            evt_resource = "监控";
                            break;
                        case "2":
                            evt_resource = "日常检查";
                            break;
                        case "3":
                            evt_resource = "客服派单";
                            break;
                        case "4":
                            evt_resource = "邮件报修";
                            break;
                        case "5":
                            evt_resource = "容量管理";
                            break;
                        case "6":
                            evt_resource = "应急演练";
                            break;
                        case "7":
                            evt_resource = "其他";
                            break;
                        case "8":
                            evt_resource = "网页报障";
                            break;
                        case "9":
                            evt_resource = "移动报障";
                            break;
                        case "10":
                            evt_resource = "IM报障";
                            break;
                        case "11":
                            evt_resource = "柜面报障";
                            break;
                        case "12":
                            evt_resource = "PAD报障";
                            break;
                        case "13":
                            evt_resource = "电话(事件报备)";
                            break;
                        case "14":
                            evt_resource = "运营助手";
                            break;
                    }
                    v.put("evt_resource", evt_resource);
                }
            });
            log.info("---查询NLP成功，结果信息 values:{}---", values);
            return values;
        } else {
            log.info("---查询NLP失败---");
           return null;
        }
    }

	// 保存客服派单附件图片
	private void saveServiceImageFile(Long bizId, String urlStr) {
        if(StringUtils.isNotEmpty(urlStr)) {
            try {
                String[] urlArr = urlStr.split(",");
                if(StringUtils.isNotEmpty(urlArr)) {
                    // 附件信息不为空先删除，每次覆盖保存数据
                    sysBizFileService.deleteByBizId("incident" + bizId);
                    for(String url : urlArr) {
                        SysBizFile file = new SysBizFile();
                        file.setTicketId("incident" + bizId);
                        file.setFileName("客服派单图片");
                        file.setOriginFileName("客服派单图片");
                        file.setLocation(url);
                        file.setUrl(url);
                        file.setContentType("");
                        file.setLength(0L);
                        file.setCreateTime(new Date());
                        file.setCreateBy(SERVICE_CREATE_USER_ID);
                        sysBizFileService.create(file);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public Map<String, Object> muliCloseEvent(String jsonData) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = JSON.parseObject(jsonData);
        log.info("---muliCloseEvent方法，查询参数 data:{}", data);
        String eventTitle = String.valueOf(data.get("eventTitle"));
        String startTime = String.valueOf(data.get("startTime"));
        String endTime = String.valueOf(data.get("endTime"));
        String currentBizId = String.valueOf(data.get("currentBizId"));
        String userId = CustomerBizInterceptor.currentUserThread.get().getUserId();

        if (null == startTime || "".equals(startTime) || "null".equals(startTime)) {
            if (null == endTime || "".equals(endTime) || "null".equals(endTime)) {
                String d = DateUtils.getTime();
                startTime = DateUtils.getSubtractionDay(d, 30);
                endTime = d;
            } else {
                startTime = DateUtils.getSubtractionDay(endTime, 30);
            }
        } else {
            if (null == endTime || "".equals(endTime) || "null".equals(endTime)) {
                endTime = DateUtils.getSubtractionDay(startTime, -30);
            }
        }
        List<Map<String, Object>> ret = designBizIncidentMapper.selectIncidentListByBatchClose(eventTitle, startTime, endTime, userId, currentBizId);
        result.put("code", 0);
        result.put("msg", "批量关闭接口查询成功！");
        result.put("data", ret);
        return result;
    }

    private final String EVENT_MONITOR_BATCH_SOLVE_FLAG = "solve";// 批量处理标识
    private final String EVENT_MONITOR_BATCH_RECEIVE_FLAG = "receive";// 批量接单标识

    @Override
    public AjaxResult execMuliCloseEvent(String code,String taskId, String instanceId,String statusStr, CustomerVo customerVo) {
        String eventNos = customerVo.getEventNos();
        log.info("------批量关单方法开始，eventNos:{}------", eventNos);
        log.info("---execMuliCloseEvent方法 code:"+code+",taskId:"+taskId+",instanceId:"+instanceId+",statusStr:"+statusStr+",参数 customerVo:{}", customerVo);
        AjaxResult ajaxResult = customerFormService.complete(code, taskId, instanceId, statusStr, customerVo);
        // 先调用主单关闭complete方法，待主单完成后才使用线程池启动关闭附单，防止线程之间出现脏数据
        if(Integer.valueOf(String.valueOf(ajaxResult.get("code"))) == 0) {
            if(StringUtils.isNotEmpty(eventNos)) {
                try {
                    String[] eventArr = Convert.toStrArray(eventNos);
                    OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
                    batchCloseEventAsync(code, instanceId, statusStr, customerVo, ogUser, Arrays.asList(eventArr));
                    // 批量插入中间表
                    /*List<IncidentBatchTemp> collect = Arrays.asList(Convert.toStrArray(eventNos)).stream().map(eventNo -> {
                        IncidentBatchTemp batchTemp = IncidentBatchTemp.builder().eventNo(eventNo).statusStr(statusStr).instanceId(instanceId).
                                customerJson(JSON.toJSONString(customerVo)).type(IncidentBatchTemp.INCIDENT_BATCH_OPERATOR_TYPE_DEAL).
                                executeStatus(IncidentBatchTemp.INCIDENT_BATCH_UNEXECUTED).userId(CustomerBizInterceptor.currentUserThread.get().getUserId()).build();
                        return batchTemp;
                    }).collect(Collectors.toList());
                    incidentBatchService.insertIncidentBatch(collect);
                    Arrays.stream(Convert.toStrArray(eventNos)).forEach(e -> {
                        designBizIncidentMapper.updateDesignBizIncidentBatchStatusByMonitor(e, EVENT_MONITOR_BATCH_SOLVE_FLAG);
                    });*/
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("----批量告警批量保存中间表异常，异常原因：message:{}----", e.getMessage());
                }
            }
        }
        return ajaxResult;
    }

    /**
     * 批量处理查询
     * @param page
     * @param params
     * @return
     */
    @Override
    public AjaxResult selectIncidentListByDealClose(Page page, Map<String, Object> params) {
        String userId = CustomerBizInterceptor.currentUserThread.get().getUserId();
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotEmpty(startTime)) {
            if (StringUtils.isNotEmpty(endTime)) {
                String d = DateUtils.getTime();
                startTime = DateUtils.getSubtractionDay(d, 30);
                endTime = d;
            } else {
                startTime = DateUtils.getSubtractionDay(endTime, 30);
            }
        } else {
            if (StringUtils.isNotEmpty(endTime)) {
                endTime = DateUtils.getSubtractionDay(startTime, -30);
            }
        }
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("userId", userId);
        IPage<Map<String, Object>> page1 = designBizIncidentMapper.selectIncidentListByDealClose(page, params);
        Map<String, Object> result = new HashMap<>();
        result.put("records", page1.getRecords());
        result.put("total", page1.getTotal());
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult batchDealEvent(String code, String taskId, String instanceId, String statusStr, CustomerVo customerVo) {
        String eventNos = customerVo.getEventNos();
        log.info("---batchDealEvent code:"+code+",taskId:"+taskId+",instanceId:"+instanceId+",statusStr:"+statusStr+",参数 data:{}", customerVo);
        AjaxResult ajaxResult = customerFormService.complete(code, taskId, instanceId, statusStr, customerVo);
        // 先调用主单关闭complete方法，待主单完成后才使用线程池启接单，防止线程之间出现脏数据
        if(Integer.valueOf(String.valueOf(ajaxResult.get("code"))) == 0) {
            if(StringUtils.isNotEmpty(eventNos)) {
                try {
                    String[] eventArr = Convert.toStrArray(eventNos);
                    OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
                    batchDealEventAsync(code, instanceId, statusStr, customerVo, ogUser, Arrays.asList(eventArr));
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("----批量告警接单异常，异常原因：message:{}----", e.getMessage());
                }
            }
        }
        return ajaxResult;
    }

    @Override
    public Map<String, Object> selectEventByMobile(String jsonData) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = JSON.parseObject(jsonData);
        log.info("------selectEventByMobile，查询参数 data:{}", data);
        String workNo = String.valueOf(data.get("workNo"));
        String eventStatus = String.valueOf(data.get("eventStatus"));
        String startTime = String.valueOf(data.get("startTime"));
        String endTime = String.valueOf(data.get("endTime"));
        String pageNum = String.valueOf(data.get("pageNum"));
        String pageSize = String.valueOf(data.get("pageSize"));
        EventIncSearchReqVo vo = new EventIncSearchReqVo();
        List list = new ArrayList();
        if("1".equals(eventStatus)) {
            vo.setImStatus("已分派");
            list.add("已分派");
        } else if("2".equals(eventStatus)) {
            vo.setImStatus("进行中");
            list.add("进行中");
        } else if("3".equals(eventStatus)) {
            vo.setImStatus("已解决");
            list.add("已解决");
        }
        vo.setStatus(list);
        if(!StringUtils.isEmpty(pageNum) && !"null".equals(pageNum)) {
            vo.setPageNum(Integer.parseInt(pageNum));
        }
        if(!StringUtils.isEmpty(pageSize) && !"null".equals(pageSize)) {
            vo.setPageSize(Integer.parseInt(pageSize));
        }
        // 起始时间特殊处理
        if("null".equalsIgnoreCase(vo.getStratTime())) {
            vo.setStratTime("");
        }
        if("null".equalsIgnoreCase(vo.getEndTime())) {
            vo.setEndTime("");
        }
        if(vo.getPageNum() == null) {
            vo.setPageNum(1);
        }
        if(vo.getPageSize() == null) {
            vo.setPageSize(100);
        }
        vo.setPageStart((vo.getPageNum() - 1) * vo.getPageSize());
        vo.setTransTeller(workNo);
        vo.setStratTime(startTime);
        vo.setEndTime(endTime);
        Map<String, Object> rMap = searchEvent(vo);
        List<EventIncSearchRespVo> resultList = (List<EventIncSearchRespVo>) rMap.get("resultList");
        List<EventIncSearchVo> searchList = JSONArray.parseArray(JSON.toJSONString(resultList), EventIncSearchVo.class);
        Long count = (Long) rMap.get("count");
        result.put("total",count);
        result.put("rows", searchList);
        result.put("code", 0);
        result.put("msg", "查询成功！");
        return result;
    }

    /**
     * 事件单发送邮件信息
     * 只有P1级别才实时通知
     * userId  事件单当前处理人
     * eventNo 事件单号
     * eventPriority 事件优先级
     * reportOrg 事件单上报部门
     * createTime 创建时间
     * timeFlag 定时发送标记 true-是；false-实时发送
     */
    @Override
    public void sendEmailMessage(String userId, Map<String, Object> map) {
        if(StringUtils.isNotEmpty(map.get("businessKey"))) {
            List<Map<String, Object>> mapList = customerFormMapper.selectMaps(Wrappers.<CustomerFormContent>query().
                    select(RedundancyFieldEnum.extra1.getName(),
                            EventFieldConstants.EVENT_PRIORITY,
                            EventFieldConstants.REPORT_ORG,
                            EventFieldConstants.EVENT_TITLE,
                            EventFieldConstants.EVENT_SOURCE,
                            EventFieldConstants.CREATED_TIME).
                    eq("id", map.get("businessKey")));
            if(!CollectionUtils.isEmpty(mapList)) {
                map = mapList.get(0);
            }
        }
        String imCode = String.valueOf(map.get(RedundancyFieldEnum.extra1.getName()));
        String eventPriority = String.valueOf(map.get(EventFieldConstants.EVENT_PRIORITY));
        String reportOrgName = String.valueOf(map.get(EventFieldConstants.REPORT_ORG));
        String eventTitle = String.valueOf(map.get(EventFieldConstants.EVENT_TITLE));
        String eventSource = String.valueOf(map.get(EventFieldConstants.EVENT_SOURCE));
        Date createTime = (Date)map.get(EventFieldConstants.CREATED_TIME);
        // 即时发送 如果当前处理人是自己，则不发送邮件，P1并且来源不是监控告警
        if(StringUtils.isNotEmpty(userId)
                && !userId.equals(CustomerBizInterceptor.currentUserThread.get().getUserId())
                && "P1".equals(eventPriority) && !"9".equals(eventSource)) {
            CompletableFuture.runAsync(() -> {
                OgPerson ogPerson = ogPersonService.selectOgPersonByPid(userId);
                if(ogPerson != null && StringUtils.isNotEmpty(ogPerson.getEmail())) {
                    String title = "P1事件 " + imCode + " 已指派给你";
                    String message =
                            "事件单号:" + imCode + "<br/>" +
                            "事件标题:" + eventTitle + "<br/>" +
                            "上报部门:" + reportOrgName + "<br/>" +
                            "提交日期:" + DateUtils.formatDate(createTime, DateUtils.YYYY_MM_DD_HH_MM_SS);

                    log.info("---保存邮件信息开始，邮件内容 message:{}", message);
                    EmailMessage emailMessage = EmailMessage.builder().bizNo(imCode).bizType(EmailMessage.EMAIL_MESSAGE_BIZ_TYPE_EVENT).
                            email(ogPerson.getEmail()).title(title).emailMessage(message).
                            sendStatus(EmailMessage.EMAIL_MESSAGE_STATUS_NO).sendTime(new Date()).build();
                    emailMessageService.insertEmailMessage(emailMessage);
                    //foreignService.sendComplexMail(userId, ogPerson.getEmail(), title, message, null, null, null);
                } else {
                    log.info("----人员userId:{},对应的人员信息或者邮箱有空，不保存邮件信息表----", userId);
                }
            }, threadPoolTaskExecutor).exceptionally((e) -> {
                e.printStackTrace();
                return null;
            });
        }
    }

    /**
     * 下午4点定时任务发送逻辑
     */
    public void sendEmailMessageTimeTask() {
        final String title = "下午4点累计工作列表";
        Map<String, Object> params = new HashMap<>();
        String currentTime = DateUtils.getTime();
        String startTime = DateUtils.formatDate(DateUtils.addDays(new Date(), -7), DateUtils.YYYY_MM_DD_HH_MM_SS);
        params.put("startTime", startTime);
        params.put("endTime", currentTime);
        List<Map<String, String>> list = designBizIncidentMapper.selectIncidentListTaskEmail(params);
        List<EventEmailMessage> eventEmailMessageList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)) {
            Map<String ,List<Map<String, String>>> mapList = list.stream().collect(Collectors.groupingBy(m -> m.get("currentUserId")));
            if(StringUtils.isNotEmpty(mapList)) {
                mapList.forEach((k,v) -> {
                    OgPerson ogPerson = ogPersonService.selectOgPersonByPid(k);
                    if(ogPerson != null && StringUtils.isNotEmpty(ogPerson.getEmail())) {
                        int countP1 = 0;
                        int countP2 = 0;
                        int countP3 = 0;
                        int countP4 = 0;
                        String eventNoP1 = "";
                        String eventNoP2 = "";
                        String eventNoP3 = "";
                        String eventNoP4 = "";
                        for(int i = 0; i< v.size(); i++) {
                            Map<String, String> mm = v.get(i);
                            String eventPriority = mm.get("eventPriority");
                            if(StringUtils.isEmpty(eventPriority)) {
                                continue;
                            }
                            switch (eventPriority) {
                                case "P1":
                                    countP1 ++;
                                    if(i <= 10) {
                                        eventNoP1 += mm.get("imCode") + ",";
                                    } else if(i == 11) {
                                        eventNoP1 += mm.get("imCode") + ".......";
                                    }
                                    break;
                                case "P2":
                                    countP2 ++;
                                    if(i <= 10) {
                                        eventNoP2 += mm.get("imCode") + ",";
                                    } else if(i == 11) {
                                        eventNoP2 += mm.get("imCode") + ".......";
                                    }
                                    break;
                                case "P3":
                                    countP3 ++;
                                    if(i <= 10) {
                                        eventNoP3 += mm.get("imCode") + ",";
                                    } else if(i == 11) {
                                        eventNoP3 += mm.get("imCode") + ".......";
                                    }
                                    break;
                                case "P4":
                                    countP4 ++;
                                    if(i <= 10) {
                                        eventNoP4 += mm.get("imCode") + ",";
                                    } else if(i == 11) {
                                        eventNoP4 += mm.get("imCode") + ".......";
                                    }
                                    break;
                            }
                        }
                        if(StringUtils.isNotEmpty(eventNoP1)) {
                            eventNoP1 = eventNoP1.substring(0, eventNoP1.length() -1);
                        }
                        if(StringUtils.isNotEmpty(eventNoP2)) {
                            eventNoP2 = eventNoP2.substring(0, eventNoP2.length() -1);
                        }
                        if(StringUtils.isNotEmpty(eventNoP3)) {
                            eventNoP3 = eventNoP3.substring(0, eventNoP3.length() -1);
                        }
                        if(StringUtils.isNotEmpty(eventNoP4)) {
                            eventNoP4 = eventNoP4.substring(0, eventNoP4.length() -1);
                        }
                        String message = "以下事件需要您处理:<br/>";
                        if(countP1 > 0) {
                            message += "P1事件总数:" + countP1 + " 事件单号:" + " [" + eventNoP1 + "]" + "<br/>";
                        }
                        if(countP2 > 0) {
                            message += "P2事件总数:" + countP2 + " 事件单号:" + " [" + eventNoP2 + "]" + "<br/>";
                        }
                        if(countP3 > 0) {
                            message += "P3事件总数:" + countP3 + " 事件单号:" + " [" + eventNoP3 + "]" + "<br/>";
                        }
                        if(countP4 > 0) {
                            message += "P4事件总数:" + countP4 + " 事件单号:" + " [" + eventNoP4 + "]";
                        }
                        EventEmailMessage eventEmailMessage = new EventEmailMessage(k, ogPerson.getEmail(), title, message);
                        eventEmailMessageList.add(eventEmailMessage);
                        log.info("---发送邮件开始，邮件内容 message:{}", message);
                        //foreignService.sendComplexMail(k, ogPerson.getEmail(), title, message, null, null, null);
                    } else {
                        log.info("----人员userId:{},对应的人员信息或者邮箱有空，不发送邮件信息----", k);
                    }
                });
            }
        }

        // 定时发送事件日报
        final String dateMessage =
            "<html>\n" +
                    "   <head>\n" +
                    "      <title>事件日报</title>\n" +
                    "   </head>\n" +
                    "   <body>\n" +
                    "\t\t<p>\n" +
                    "各位领导，老师，下午好！<br/>\n" +
                    "<br/>"+
                    "&nbsp;&nbsp;以下链接为今日上海银行生产运行情况汇总展示，历史数据可通过日期进行选择性浏览，点击看板可查看未处理完成事件清单，请审阅，谢谢!<br/>\n" +
                    "<br/>"+
                    "&nbsp;&nbsp;看板中每日识别出的可能影响分支行服务质量的关注事件、客诉事件、超5日未解决事件，请各部室尽快解决，如有本报告未提及之关注事件，请相关部室负责人邮件回复说明，截止时间明日上午九点半。<br/>\n" +
                    "<br/>"+
                    "&nbsp;&nbsp;如对相关数据有疑问，请反馈至生产管理部王珏。<br/>\n" +
                    "</p>\t\n" +
                    "&nbsp;&nbsp;<p>事件日报: <a href='http://10.232.50.31:8080/webroot/decision/link/T5zf'>http://10.232.50.31:8080/webroot/decision/link/T5zf</a></p>\n" +
                    "   </body>\n" +
                    "</html>";

        // 异步发送邮件信息
        final String totalTitle = DateUtils.getDate() + "事件日报";
        if(!CollectionUtils.isEmpty(eventEmailMessageList)) {
            eventEmailMessageList.forEach(e-> {
                //CompletableFuture.runAsync(() -> {
                log.info("----批量发送邮件开始，发送对象 data:{}.......", e);
                String result = foreignService.sendComplexMail(e.getUserId(), e.getEmail(), null, e.getTitle(), e.getMessage(), null, null, null);
                /*// 事件列表发送成功，定时发送日报
                if(MailUtils.MESSAGE_SEND_SUCCESS.equals(result)) {
                    foreignService.sendComplexMail(e.getUserId(), e.getEmail(), totalTitle, dateMessage, null, null, null);
                }*/
                //}, threadPoolTaskExecutor);
            });
        }
        // 定时发送日报  指定邮箱发送
        List<PubParaValue> emailNameList = DictUtils.getParaValueCache("message_email_name_list");
        List<PubParaValue> emailNameCopyList = DictUtils.getParaValueCache("message_email_name_copy_list");
        if(!CollectionUtils.isEmpty(emailNameList) && !CollectionUtils.isEmpty(emailNameCopyList)) {
            List<String> list1 = emailNameList.stream().map(pubParaValue -> pubParaValue.getValueDetail()).collect(Collectors.toList());
            List<String> list2 = emailNameCopyList.stream().map(pubParaValue -> pubParaValue.getValueDetail()).collect(Collectors.toList());
            foreignService.sendComplexMail(CustomerFlowConstants.ADMIN_USER_ID, String.join(",", list1), String.join(",", list2), totalTitle, dateMessage, null, null, null);
        }
    }

    /**
     * 发邮件对象
     */
    @Data
    @AllArgsConstructor
    @ToString
    static class EventEmailMessage {
        private String userId;
        private String email;
        private String title;
        private String message;
    }

    @Override
    public AjaxResult receiveEvent(Map<String, Object> params) {
        AjaxResult ajaxResult = checkEventProcess(params);
        if(Integer.valueOf(ajaxResult.get(AjaxResult.CODE_TAG).toString()) != 0) {
            return ajaxResult;
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("reCode", "1");
        variables.put("dealId", ((OgUser) ajaxResult.get("ogUser")).getUserId());
        CustomerVo customerVo = createCustomerVo(Long.valueOf(ajaxResult.get("id").toString()), String.valueOf(ajaxResult.get("instanceId")), false, null, variables);
        return customerFormService.complete(code, String.valueOf(ajaxResult.get("taskId")), String.valueOf(ajaxResult.get("instanceId")), "", customerVo);
    }

    @Override
    public AjaxResult eventTransfer(Map<String, Object> params) {
        AjaxResult ajaxResult = checkEventProcess(params);
        if(Integer.valueOf(ajaxResult.get(AjaxResult.CODE_TAG).toString()) != 0) {
            return ajaxResult;
        }
        String assigenReason = (String) params.get("assigenReason");
        Map<String, Object> variables = new HashMap<>();
        variables.put("reCode", "5");
        variables.put("forwardOneLineReceive", ((OgUser) ajaxResult.get("ogUser")).getUserId());
        Map<String, Object> fieldsMap = new HashMap<>();
        fieldsMap.put(EventFieldConstants.ASSIGNED_GROUP, params.get("assigenGroup"));
        fieldsMap.put(EventFieldConstants.ASSIGNED_PERSON, params.get("assigenPerson"));
        CustomerVo customerVo = createCustomerVo(Long.valueOf(ajaxResult.get("id").toString()), String.valueOf(ajaxResult.get("instanceId")), true, fieldsMap, variables);
        return customerFormService.complete(code, String.valueOf(ajaxResult.get("taskId")), String.valueOf(ajaxResult.get("instanceId")), assigenReason, customerVo);
    }

    @Override
    public AjaxResult solveEvent(Map<String, Object> params) {
        AjaxResult ajaxResult = checkEventProcess(params);
        if(Integer.valueOf(ajaxResult.get(AjaxResult.CODE_TAG).toString()) != 0) {
            return ajaxResult;
        }
        Map<String, Object> variables = new HashMap<>();
        boolean flag = judgeMonitorCreateIncident((String) params.get("eventNo"));
        if(flag) {
            variables.put("reCode", "3");
        } else {
            variables.put("reCode", "4");
            variables.put("closeId", ajaxResult.get("createBy"));
        }
        Map<String, Object> paramsKV = new HashMap<>();
        params.forEach((k,v) -> {
            if(!k.equals("eventNo") && !k.equals("workNo")) {
                paramsKV.put(StringUtils.humpToUnderline(k), v);
            }
        });
        Map<String, Object> fieldsMap = copyEventSolveFields(paramsKV);
        CustomerVo customerVo = createCustomerVo(Long.valueOf(ajaxResult.get("id").toString()), String.valueOf(ajaxResult.get("instanceId")), true, fieldsMap, variables);
        return customerFormService.complete(code, String.valueOf(ajaxResult.get("taskId")), String.valueOf(ajaxResult.get("instanceId")), "", customerVo);
    }

    @Override
    public void clearCurrentDealByStatus() {
        designBizIncidentMapper.clearCurrentDealByStatus();
    }

    private AjaxResult checkEventProcess(Map<String, Object> params) {
        String workNo = (String) params.get("workNo");
        String eventNo = (String) params.get("eventNo");
        if(StringUtils.isEmpty(workNo) || StringUtils.isEmpty(eventNo)) {
            return AjaxResult.error("事件单号或操作人工号不可为空！");
        }
        OgUser ogUser = ogUserService.selectOgUserByUsername(workNo);
        if(ogUser == null) {
            return AjaxResult.error("根据工号workNo=[" + workNo + "],未查询到人员信息！");
        }
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        DesignBizIncident incident = designBizIncidentMapper.selectOne(new QueryWrapper<DesignBizIncident>().eq("extra1", eventNo));
        if(incident == null) {
            return AjaxResult.error("根据事件单号eventNo=[" + eventNo + "],未查询到事件信息！");
        }
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(incident.getInstanceId()).list();
        if(CollectionUtils.isEmpty(taskList)) {
            return AjaxResult.error("根据事件单号eventNo=[" + eventNo + "],未查询到流程任务信息！");
        }
        AjaxResult success = AjaxResult.success();
        success.put("ogUser", ogUser);
        success.put("id", incident.getId());
        success.put("instanceId", incident.getInstanceId());
        success.put("taskId", taskList.get(0).getId());
        success.put("createBy", incident.getCreatedBy());
        return success;
    }

    /**
     * 构造CustomerVo对象
     * 接单不需要替换json，字段map值没有变化
     * @param bizId           主键id
     * @param instanceId      流程实例id
     * @param replaceJsonFlag 替换json标识
     * @param params          字段map
     * @param variables       流程参数map
     * @return
     */
    private CustomerVo createCustomerVo(Long bizId, String instanceId, boolean replaceJsonFlag, Map<String, Object> params, Map<String, Object> variables) {
        CustomerVo customerVo = new CustomerVo();
        CustomerFormContent customerFormContent = new CustomerFormContent();
        List<Map<String, Object>> mapList = designBizIncidentMapper.selectMaps(new QueryWrapper<DesignBizIncident>().eq("instance_id", instanceId));
        if(!CollectionUtils.isEmpty(mapList)) {
            Map<String, Object> fieldsMap = mapList.get(0);
            customerFormContent.setId(bizId);
            customerFormContent.setCode(code);
            DesignBizJsonData designBizJsonData = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                    .eq(DesignBizJsonData.COL_BIZ_ID, bizId)
                    .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", code)));
            if(replaceJsonFlag) {
                customerFormContent.setFields(params);
                String dataJson = VueDataJsonUtil.analysisDataJson(designBizJsonData.getJsonData(), params);
                customerFormContent.setDataJson(dataJson);
            } else {
                customerFormContent.setFields(fieldsMap);
                customerFormContent.setDataJson(designBizJsonData.getJsonData());
            }
            customerVo.setCustomerFormContent(customerFormContent);
            customerVo.setVariables(variables);
        } else {
            throw new BusinessException("事件单查询信息为空，请检查事件单号！");
        }
        return customerVo;
    }

    /**
     * 关闭附单方法，使用线程池
     * @param code
     * @param instanceId
     * @param statusStr
     * @param customerVo
     * @param ogUser
     * @param eventNoList
     */
    public void batchCloseEventAsync(String code, String instanceId,String statusStr, CustomerVo customerVo, OgUser ogUser, List<String> eventNoList) {
        DesignBizIncident currentIncident = designBizIncidentMapper.selectOne(new QueryWrapper<DesignBizIncident>().eq("instance_id", instanceId));
        final Long currentId = currentIncident.getId();
        final String currentNo = currentIncident.getExtra1();
        final Map<String, Object> newFieldsMap = this.copyEventSolveFields(customerVo.getCustomerFormContent().getFields());
        eventNoList.forEach(eventNo -> {
            CompletableFuture.runAsync(() -> {
                DesignBizIncident incident = designBizIncidentMapper.selectOne(new QueryWrapper<DesignBizIncident>().eq("extra1", eventNo));
                // 2.排除当前事件单号
                if(incident != null && !currentId.equals(incident.getId())) {
                    List<Task> taskList = taskService.createTaskQuery().processInstanceId(incident.getInstanceId()).list();
                    if(!CollectionUtils.isEmpty(taskList)) {
                        // 设置主单号  向线程池传递工作详情描述信息
                        CustomerVo customerVo1 = new CustomerVo();
                        BeanUtils.copyProperties(customerVo, customerVo1);
                        customerVo1.setDescMsg("批量关单，关联关单主单号:["+currentNo+"]");
                        // 1.批量关闭之前先赋值主单的一线解决的字段内容到其他单子上
                        CustomerFormContent customerFormContent = customerVo1.getCustomerFormContent();
                        /*Map<String, Object> fields = customerFormContent.getFields();*/
                        Map<String, Object> solveFields = newFieldsMap;
                        customerFormContent.setFields(solveFields);
                        // 3.组装json数据
                        CustomerBizInterceptor.currentUserThread.set(ogUser);
                        DesignBizJsonData designBizJsonData = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                                .eq(DesignBizJsonData.COL_BIZ_ID, incident.getId())
                                .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", code)));
                        log.info("-----解决环节json:{}-----", solveFields);
                        String dataJson = VueDataJsonUtil.analysisDataJson(designBizJsonData.getJsonData(), solveFields);
                        customerFormContent.setDataJson(dataJson);
                        customerFormContent.setId(incident.getId());
                        customerFormContent.getFields().put("id", incident.getId());
                        log.info("----事件单批量关闭接口，执行参数 eventNo:{}, taskId:{}, instanceId:{}", eventNo, taskList.get(0).getId(), incident.getInstanceId());
                        // 4.调用complete接口 提交流程并且内部提交保存json数据
                        AjaxResult ajaxResult = customerFormService.complete(code, taskList.get(0).getId(), incident.getInstanceId(), statusStr, customerVo1);
                        log.info("----事件单批量关闭接口，执行结果 eventNo:{}, ajaxResult:{}", eventNo, ajaxResult);
                        /*incidentBatchService.updateIncidentBatchTempByEventNo(IncidentBatchTemp.INCIDENT_BATCH_SUCCESS, incident.getExtra1());
                        // 置空事件单主表标识
                        designBizIncidentMapper.updateDesignBizIncidentBatchStatusByMonitor(eventNo, "");*/
                        updateDesignBizIncidentById(incident.getId(), solveFields);
                    }
                }

            }, threadPoolTaskExecutor).exceptionally((e) -> {
                log.error("----批量关闭方法执行异常，异常原因 message:{}---", e.getMessage());
                e.printStackTrace();
                return null;
            });
        });
    }

    private void updateDesignBizIncidentById(Long id, Map<String,Object> solveFields) {
        DesignBizIncident designBizIncident = new DesignBizIncident();
        designBizIncident.setId(id);
        designBizIncident.setRelationalSystem(solveFields.get(EventFieldConstants.RELATIONAL_SYSTEM).toString());// 影响系统
        designBizIncident.setEventSourceSystem(solveFields.get(EventFieldConstants.EVENT_SOURCE_SYSTEM).toString());// 事件原系统
        designBizIncident.setSolvePlan(solveFields.get(EventFieldConstants.SOLVE_PLAN).toString());// 解决方案
        designBizIncident.setEventCategory(solveFields.get(EventFieldConstants.EVENT_CATEGORY).toString());// 事件类别
        designBizIncident.setEventSubclass(solveFields.get(EventFieldConstants.EVENT_SUBCLASS).toString());// 事件子类
        designBizIncident.setFinalFirstLevel(solveFields.get(EventFieldConstants.FINAL_FIRST_LEVEL).toString());// 最终事件类型一级
        designBizIncident.setEventReasonCategory(solveFields.get(EventFieldConstants.EVENT_REASON_CATEGORY).toString());// 事件原因分类
        designBizIncident.setInfFace(solveFields.get(EventFieldConstants.INF_FACE).toString());// 影响面
        designBizIncident.setCloseCode(solveFields.get(EventFieldConstants.CLOSE_CODE).toString());// 关闭代码
        designBizIncident.setUpdatedTime(new Date());
        designBizIncident.setExtra5(null);// 置空当前处理人
        /**
         * Map<String, Object> newFields = new HashMap<>();
         *         newFields.put(EventFieldConstants.SECOND_SOLVE_PLAN, fields.get(EventFieldConstants.SECOND_SOLVE_PLAN));
         *         newFields.put(EventFieldConstants.SOLUTION_VALID_FLAG, fields.get(EventFieldConstants.SOLUTION_VALID_FLAG));
         *         newFields.put(EventFieldConstants.SOLVE_PLAN, fields.get(EventFieldConstants.SOLVE_PLAN));
         *         newFields.put(EventFieldConstants.MOINTOR_INVALID, fields.get(EventFieldConstants.MOINTOR_INVALID));
         *         newFields.put(EventFieldConstants.MONITOR_INVALID_REASON, fields.get(EventFieldConstants.MONITOR_INVALID_REASON));
         *         newFields.put(EventFieldConstants.EVENT_CATEGORY, fields.get(EventFieldConstants.EVENT_CATEGORY));
         *         newFields.put(EventFieldConstants.EVENT_SUBCLASS, fields.get(EventFieldConstants.EVENT_SUBCLASS));
         *         newFields.put(EventFieldConstants.EVENT_ENTRY, fields.get(EventFieldConstants.EVENT_ENTRY));
         *         newFields.put(EventFieldConstants.EVENT_SUBENTRY1, fields.get(EventFieldConstants.EVENT_SUBENTRY1));
         *         newFields.put(EventFieldConstants.EVENT_SUBENTRY2, fields.get(EventFieldConstants.EVENT_SUBENTRY2));
         *         newFields.put(EventFieldConstants.FINAL_FIRST_LEVEL, fields.get(EventFieldConstants.FINAL_FIRST_LEVEL));
         *         newFields.put(EventFieldConstants.FINAL_SECOND_LEVEL, fields.get(EventFieldConstants.FINAL_SECOND_LEVEL));
         *         newFields.put(EventFieldConstants.FINAL_THREE_LEVEL, fields.get(EventFieldConstants.FINAL_THREE_LEVEL));
         *         newFields.put(EventFieldConstants.EVENT_REASON_CATEGORY, fields.get(EventFieldConstants.EVENT_REASON_CATEGORY));
         *         newFields.put(EventFieldConstants.INF_FACE, fields.get(EventFieldConstants.INF_FACE));
         *         newFields.put(EventFieldConstants.CLOSE_CODE, fields.get(EventFieldConstants.CLOSE_CODE));
         *         newFields.put(EventFieldConstants.INIT_FIRST_LEVEL, fields.get(EventFieldConstants.INIT_FIRST_LEVEL));
         *         newFields.put(EventFieldConstants.INIT_SECOND_LEVEL, fields.get(EventFieldConstants.INIT_SECOND_LEVEL));
         *         newFields.put(EventFieldConstants.INIT_THREE_LEVEL, fields.get(EventFieldConstants.INIT_THREE_LEVEL));
         *         newFields.put(EventFieldConstants.EVENT_LEVEL, fields.get(EventFieldConstants.EVENT_LEVEL));
         */
        designBizIncidentMapper.updateById(designBizIncident);
    }

    private void batchDealEventAsync(String code, String instanceId,String statusStr, CustomerVo customerVo, OgUser ogUser, List<String> eventNoList) {
        eventNoList.forEach(eventNo -> {
            CompletableFuture.runAsync(() -> {
                DesignBizIncident incident = designBizIncidentMapper.selectOne(new QueryWrapper<DesignBizIncident>().eq("extra1", eventNo));
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(incident.getInstanceId()).list();
                if(!CollectionUtils.isEmpty(taskList)) {
                    CustomerBizInterceptor.currentUserThread.set(ogUser);
                    log.info("----事件单批量处理接口，执行参数 eventNo:{}, taskId:{}, instanceId:{}", eventNo, taskList.get(0).getId(), incident.getInstanceId());
                    // 调用complete接口 流转流程
                    AjaxResult ajaxResult = customerFormService.complete(code, taskList.get(0).getId(), incident.getInstanceId(), statusStr, customerVo);
                    log.info("----事件单批量处理接口，执行结果 eventNo:{}, ajaxResult:{}", eventNo, ajaxResult);
                }

            }, threadPoolTaskExecutor).exceptionally((e) -> {
                log.error("----批量处理方法执行异常，异常原因 message:{}---", e.getMessage());
                e.printStackTrace();
                return null;
            });
        });
    }

    /**
     * 保存事件单解决环节字段
     * @param fields
     * @return
     */
    private Map<String, Object> copyEventSolveFields(Map<String, Object> fields) {
        Map<String, Object> newFields = new HashMap<>();
        newFields.put(EventFieldConstants.SECOND_SOLVE_PLAN, fields.get(EventFieldConstants.SECOND_SOLVE_PLAN));
        newFields.put(EventFieldConstants.SOLUTION_VALID_FLAG, fields.get(EventFieldConstants.SOLUTION_VALID_FLAG));
        newFields.put(EventFieldConstants.SOLVE_PLAN, fields.get(EventFieldConstants.SOLVE_PLAN));
        newFields.put(EventFieldConstants.MOINTOR_INVALID, fields.get(EventFieldConstants.MOINTOR_INVALID));
        newFields.put(EventFieldConstants.MONITOR_INVALID_REASON, fields.get(EventFieldConstants.MONITOR_INVALID_REASON));
        newFields.put(EventFieldConstants.EVENT_CATEGORY, fields.get(EventFieldConstants.EVENT_CATEGORY));
        newFields.put(EventFieldConstants.EVENT_SUBCLASS, fields.get(EventFieldConstants.EVENT_SUBCLASS));
        newFields.put(EventFieldConstants.EVENT_ENTRY, fields.get(EventFieldConstants.EVENT_ENTRY));
        newFields.put(EventFieldConstants.EVENT_SUBENTRY1, fields.get(EventFieldConstants.EVENT_SUBENTRY1));
        newFields.put(EventFieldConstants.EVENT_SUBENTRY2, fields.get(EventFieldConstants.EVENT_SUBENTRY2));
        newFields.put(EventFieldConstants.FINAL_FIRST_LEVEL, fields.get(EventFieldConstants.FINAL_FIRST_LEVEL));
        newFields.put(EventFieldConstants.FINAL_SECOND_LEVEL, fields.get(EventFieldConstants.FINAL_SECOND_LEVEL));
        newFields.put(EventFieldConstants.FINAL_THREE_LEVEL, fields.get(EventFieldConstants.FINAL_THREE_LEVEL));
        newFields.put(EventFieldConstants.EVENT_REASON_CATEGORY, fields.get(EventFieldConstants.EVENT_REASON_CATEGORY));
        newFields.put(EventFieldConstants.INF_FACE, fields.get(EventFieldConstants.INF_FACE));
        newFields.put(EventFieldConstants.CLOSE_CODE, fields.get(EventFieldConstants.CLOSE_CODE));
        newFields.put(EventFieldConstants.INIT_FIRST_LEVEL, fields.get(EventFieldConstants.INIT_FIRST_LEVEL));
        newFields.put(EventFieldConstants.INIT_SECOND_LEVEL, fields.get(EventFieldConstants.INIT_SECOND_LEVEL));
        newFields.put(EventFieldConstants.INIT_THREE_LEVEL, fields.get(EventFieldConstants.INIT_THREE_LEVEL));
        newFields.put(EventFieldConstants.EVENT_LEVEL, fields.get(EventFieldConstants.EVENT_LEVEL));
        newFields.put(EventFieldConstants.EVENT_SOURCE_SYSTEM, fields.get(EventFieldConstants.EVENT_SOURCE_SYSTEM));
        newFields.put(EventFieldConstants.RELATIONAL_SYSTEM, fields.get(EventFieldConstants.RELATIONAL_SYSTEM));
        return newFields;
    }
}

