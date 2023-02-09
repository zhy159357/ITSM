package com.ruoyi.form.service.impl;

import static com.ruoyi.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;
import static com.ruoyi.form.constants.ProblemConstant.CREATED_TIME;
import static com.ruoyi.form.constants.ProblemConstant.PROBLEM_TITLE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.activiti.domain.PubBizSms;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.IpUtils;
import com.ruoyi.common.utils.MailUtils;
import com.ruoyi.common.utils.SmsUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.activiti.Base;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.enums.ChangeFieldEnum;
import com.ruoyi.form.enums.ChangeTableNameEnum;
import com.ruoyi.form.enums.ChangeTaskFieldEnum;
import com.ruoyi.form.enums.RedundancyFieldEnum;
import com.ruoyi.form.mapper.ForgineMapper;
import com.ruoyi.form.service.CustomerFormService;
import com.ruoyi.form.service.ForeignService;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysApplicationManagerService;

import cn.hutool.core.util.ObjectUtil;

@Service
public class ForeignServiceImpl implements ForeignService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ForeignServiceImpl.class);

    @Autowired
    private ForgineMapper customerFormMapper;
    @Autowired
    private CustomerFormService customerFormService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    Base base;
    @Autowired
    ISysApplicationManagerService applicationManagerService;
    @Autowired
    IPubParaValueService pubParaValueService;

    @Value("${foreign.adpm.customerServiceUrl}")
    private String adpm_customerServiceUrl;
    @Value("${foreign.monitor.alarmMaintenance.url}")
    private String alarmMaintenanceUrl;
    @Value("${cmdb.url}")
    private String cmdbUrl;
    @Value("${cmdb.apikey}")
    private String token;
    @Value("${cmdb.Field}")
    private String itsmPaso;

    /**
     * 接收adpm，change_state:变更单状态;change_task_info:变更单子任务信息;event_list:事件单子任务信息;problem_list:问题单子任务信息
     */
    @Override
    public Map<String, Object> get(String jsonData, String workOrderType) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> dataMap = JSON.parseObject(jsonData);
        // 获取分页参数,构造分页对象
        Page page = this.buildPageObject(dataMap);
        if ("change_state".equals(workOrderType)) {
            // 变更单状态
            String changeNos = String.valueOf(dataMap.get("changeNo"));// 变更单单号，可能存在多个，使用“,”分割
            // resCode 返回码
            // resMsg 返回信息
            // resData 返回数据
//			changeNo	变更单号					
//			completion AS changeExecute	实施情况				"1=成功,2=失败,3=缺陷,4=取消"	原ITSM_EXECUTE，运维管控平台里也是有这个字段和码值？
//			changeStatus AS changeStatus	变更单状态	String(20)		Y	"1=待提交,2=变更预审,3=变更准备,4=变更经理审批,5=变更审批,6=变更实施中,7=已完成,8=已关闭,9=取消"	7=已完成这个是变更单全部任务单完成实施
//			maxChildFinishTime	变更单完成时间，即最后一个子任务提交完成时间	String(20)				就是取 MAX（子任务变成待复核的时间），yyyy-MM-dd hh:mm:ss，没到完成状态就填空
//			changeFinishTime	变更单关闭时间	String(20)	 yyyy-MM-dd hh:mm:ss，没到关闭状态就填空
//			implementOverDate AS maxChildActualFinishTime	变更单实际完成时间，即子任务提交完成时手工填写的“实施结束日期” 就是取 MAX（子任务上人工填写的“实施结束日期”），yyyy-MM-dd hh:mm:ss，没到完成状态就填空
            try {
                List<Map<String, Object>> list = customerFormMapper.selectChangeStateList(changeNos);
                String json = JSON.toJSONString(list);
                map.put("code", "0");
                map.put("msg", "数据检索成功");
                map.put("resData", json);
            } catch (Exception e) {
                log.error("数据检索异常：", e);
                map.put("code", "-1");
                map.put("msg", "数据检索失败" + e.getMessage());
                map.put("resData", Lists.newArrayList());
            }
            
            return map;
        } else if ("change_task_info".equals(workOrderType)) {
            // 变更单子任务信息
            String changeNo = String.valueOf(dataMap.get("changeNo"));// 变更单单号，只能1个
            // resCode 返回码
            // resMsg 返回信息
            // resData 返回数据
//			itsmNo	变更单号	String(50)		Y		
//			itsmChildNo	变更单子任务单号	String(50)		Y		
//			sysId	涉及应用ID	String(50)		Y		
//			deployWay	部署方式	string/50		Y	是	"1=devops方式,2=非devops方式"
//			hasPretask	是否有前置任务标志字段 	String(4)		N		0：否，1：是
//			hasUnion	是否并包标志字段	String(4)		N		0：否，1：是
//			hasDepend	是否涉及前后依赖	String(4)		N		0：否，1：是
//			planStartDt	计划开始时间	String(20)		N		yyyy-MM-dd hh:mm:ss
//			actStartDt	实际实施开始时间	String(20)		N		yyyy-MM-dd hh:mm:ss
//			actEndDt	实际实施结束时间	String(20)		N		yyyy-MM-dd hh:mm:ss
//			execResult	实施结果	String(20)		N	"1=成功,2=失败,3=缺陷,4=取消"	
//			itsmChildStatus	变更子任务状态	String(20)		N	"1=已注册,2=方案审核完成,3=预审中,4=预审通过,5=待实施,6=实施中,7=复核中,8=已关闭,9=取消"	
//			createDt	创建时间	String(20)		Y		yyyy-MM-dd hh:mm:ss
//			lastUpStringDt	最后更新时间	String(20)		Y		yyyy-MM-dd hh:mm:ss
            try {
                List<Map<String, Object>> list = customerFormMapper.selectChangeTaskList(changeNo);
                String json = JSON.toJSONString(list);
                map.put("code", "0");
                map.put("msg", "数据检索成功");
                map.put("resData", json);
            } catch (Exception e) {
                log.error("数据检索异常：", e);
                map.put("code", "-1");
                map.put("msg", "数据检索失败" + e.getMessage());
                map.put("resData", Lists.newArrayList());
            }
            return map;
        } else if ("event_list".equals(workOrderType)) {
            // 事件单子任务信息
            String beginDT = String.valueOf(dataMap.get("beginDT"));// 变更单单号，只能1个
            String endDT = String.valueOf(dataMap.get("endDT"));// 变更单单号，只能1个
            // resCode 返回码 // resMsg 返回信息 // resData 返回数据eventNo 事件编号、eventTitle
            // 事件标题、createTime 创建时间
            SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                String start = df.format(df1.parse(beginDT + " 00:00:00"));
                String end = df.format(df1.parse(endDT + " 23:59:59"));
                List<Map<String, Object>> list = customerFormMapper.selectEventList(start, end);
                if(!org.springframework.util.CollectionUtils.isEmpty(list)) {
                    list.forEach(m -> {
                        if(StringUtils.isNotEmpty(m.get("createTime"))) {
                            m.put("createTime", DateUtils.formatDate((Date) m.get("createTime"), YYYY_MM_DD_HH_MM_SS));
                        }
                    });
                }
                //String json = JSON.toJSONString(list);
                map.put("code", "0");
                map.put("msg", "数据检索成功");
                map.put("resData", list);
            } catch (Exception e) {
                log.error("时间转换异常：", e);
                map.put("code", "-1");
                map.put("msg", "数据检索失败" + e.getMessage());
                map.put("resData", Lists.newArrayList());
            }
            return map;

        } else if ("problem_list".equals(workOrderType)) {
            // resCode 返回码 // resMsg 返回信息 // resData 返回数据quesNo 问题编号、 quesTitle
            // 问题标题、createTime 创建时间
            if (ObjectUtil.isEmpty(dataMap.get("beginDT")) || ObjectUtil.isEmpty(dataMap.get("endDT"))) {
                map.put("code", "-1");
                map.put("msg", "入参开始日期和结束日期不能为空!");
                map.put("resData", Lists.newArrayList());
                return map;
            }
            String beginDT = String.valueOf(dataMap.get("beginDT"));
            String endDT = String.valueOf(dataMap.get("endDT"));
            SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date bDate = df1.parse(beginDT + " 00:00:00");
                Date eDate = df1.parse(endDT + " 23:59:59");
                if (bDate.after(eDate)) {
                    map.put("code", "-1");
                    map.put("msg", "开始时间不能大于结束时间!");
                    map.put("resData", Lists.newArrayList());
                    return map;
                }
                String startTime = df.format(bDate);
                String endTime = df.format(eDate);
                MybatisPlusConfig.customerTableName.set("design_biz_problem");
                QueryWrapper<CustomerFormContent> queryWrapper = new QueryWrapper<>();
                queryWrapper.between(CREATED_TIME, startTime, endTime);
                if (ObjectUtil.isNotEmpty(dataMap.get("quesNo"))) {
                    queryWrapper.eq(RedundancyFieldEnum.extra1.name, String.valueOf(dataMap.get("quesNo")));
                }
                if (ObjectUtil.isNotEmpty(dataMap.get("quesTitle"))) {
                    queryWrapper.like(PROBLEM_TITLE, String.valueOf(dataMap.get("quesTitle")));
                }
                queryWrapper.select(RedundancyFieldEnum.extra1.name, PROBLEM_TITLE, CREATED_TIME)
                        .orderByDesc(CREATED_TIME);
                Page page1 = customerFormMapper.selectMapsPage(page, queryWrapper);
                MybatisPlusConfig.customerTableName.remove();
                if (CollectionUtils.isEmpty(page1.getRecords())) {
                    map.put("code", "0");
                    map.put("msg", "数据检索为空!");
                    map.put("resData", Lists.newArrayList());
                    return map;
                }
                List<Map<String, Object>> records = (List<Map<String, Object>>) page1.getRecords();
                List<Map<String, Object>> problemList = records.stream().map(problem -> {
                    Map<String, Object> problemMap = new HashMap<>();
                    problemMap.put("quesNo", problem.get(RedundancyFieldEnum.extra1.name));
                    problemMap.put("quesTitle", problem.get(PROBLEM_TITLE));
                    problemMap.put("createTime", DateUtils.formatDate((Date) problem.get(CREATED_TIME), YYYY_MM_DD_HH_MM_SS));
                    return problemMap;
                }).collect(Collectors.toList());
//				String json = JSON.toJSONString(problemList);
                map.put("code", "0");
                map.put("msg", "数据检索成功");
                map.put("resData", problemList);
                return map;
            } catch (Exception e) {
                log.error("时间转换异常：", e);
                map.put("code", "-1");
                map.put("msg", "数据检索失败" + e.getMessage());
                map.put("resData", Lists.newArrayList());
                return map;
            }
        }
        return map;
    }

    public Page buildPageObject(Map<String, Object> dataMap) {
        Integer pageNum = ObjectUtil.isNotEmpty(dataMap.get("page")) ? Integer.valueOf(String.valueOf(dataMap.get("page"))) : 1;
        Integer pageSize = ObjectUtil.isNotEmpty(dataMap.get("size")) ? Integer.valueOf(String.valueOf(dataMap.get("size"))) : 50;
        Page page = new Page<>(pageNum, pageSize);
        return page;
    }

    /**
     * 接收adpm锁定或解锁变更单
     */

    @Override
    public Map<String, Object> block(String jsonData) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> dataMap = JSON.parseObject(jsonData);
        log.info("根据工单号关闭对应告警 参数: {}", dataMap);
        if (StringUtils.isEmpty(dataMap.get("changNo"))) {
            map.put("resMsg", "变更单号不能为空");
            map.put("resCode", "-1");
            return map;
        }
        if (StringUtils.isEmpty(dataMap.get("sysId"))) {
            map.put("resMsg", "应用ID不能为空");
            map.put("resCode", "-1");
            return map;
        }
        if (StringUtils.isEmpty(dataMap.get("deployWay"))) {
            // 1=devops方式,2=非devops方式
            map.put("resMsg", "部署方式");
            map.put("resCode", "-1");
            return map;
        }
        if (StringUtils.isEmpty(dataMap.get("operateType"))) {
            // 1=解锁,2=锁定
            map.put("resMsg", "操作类型不能为空");
            map.put("resCode", "-1");
            return map;
        }
        try {
            // TODO 业务代码

            map.put("resMsg", "锁定或解锁变更单成功");
            map.put("resCode", "0");
        } catch (Exception e) {
            log.error("锁定或解锁变更单异常：", e);
            map.put("resMsg", "锁定或解锁变更单失败：" + e.getMessage());
            map.put("resCode", "-1");
        }
        return map;
    }

    /**
     * 调用adpm执行更新变更单信息，包括状态
     */
    /**
     * 调用adpm执行更新变更单信息，包括状态
     */
    @Override
    public AjaxResult modifyAdpmChange(String changeNo) {
        try {
            String url = adpm_customerServiceUrl + "/updateItsmNoInfo";
            log.info("url：" + url);
            List<Map<String, Object>> dataMapList = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE, ChangeFieldEnum.EXTRA1.getName(), changeNo);
            if (dataMapList.isEmpty()) {
                return AjaxResult.warn("无变更数据");
            }
            Map<String, Object> dataMap = dataMapList.get(0);
            //转换关闭代码与变更单状态为码值
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
            dataMap.forEach((key, value) -> {
                if (value != null && !"".equals(value.toString().trim())) {
                    if (ChangeFieldEnum.CLOSE_CODE.getName().equals(key)) {
                        dataMap.put(key, implResultMap.get(value.toString().trim()));
                    } else if (ChangeFieldEnum.CHANGE_STATUS.getName().equals(key)) {
                        dataMap.put(key, changeStatusMap.get(value.toString().trim()));
                    }
                }
                if (value == null) {
                    dataMap.put(key, "");
                }else {
                    dataMap.put(key,String.valueOf(dataMap.get(key)));
                }
            });
            dataMap.put("maxChildActualFinishTime", dataMap.get(ChangeFieldEnum.CHANGE_FINISH_TIME.getName()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(dataMap, headers);
            log.info("httpEntity：" + JSON.toJSONString(httpEntity));
            ResponseEntity<JSONObject> entity = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
            log.info("更新变更单信息，包括状态: {}", entity);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                log.error("更新变更单信息，包括状态失败：", entity);
                return AjaxResult.error("更新变更单信息，包括状态失败：" + entity);
            }
            JSONObject result = entity.getBody();
            if (!"0".equals(result.getString("resCode"))) {
                log.error("更新变更单信息，包括状态失败：", result);
                return AjaxResult.error("更新变更单信息，包括状态失败：" + result.getString("resMsg"));
            } else {
                return AjaxResult.success(result.getString("resMsg"));
            }
        } catch (Exception e) {
            log.error("更新变更单信息，包括状态异常：", e);
            return AjaxResult.error("更新变更单信息，包括状态异常：" + e);
        }
    }

    /**
     * 调用adpm执行创建/更新变更子任务信息
     */
    @Override
    public AjaxResult updateAdpmChangeTask(String changeTaskNo) {
        try {
            String url = adpm_customerServiceUrl + "/receiveCHMChildTask";
            log.info("url：" + url);
            List<Map<String, Object>> dataMapList = base.getAllColumnsValueByColumn(ChangeTableNameEnum.CHANGE_TASK, ChangeTaskFieldEnum.EXTRA1.getName(), changeTaskNo);
            if (dataMapList.isEmpty()) {
                return AjaxResult.warn("无任务数据");
            }
            Map<String, Object> dataMap = dataMapList.get(0);
            //转换所涉系统和任务状态
            List<PubParaValue> changeTaskStatus = pubParaValueService.selectPubParaValueByParaName("change_task_status");
            Map<String, Object> changeTaskStatusMap = new HashMap<>();
            changeTaskStatus.forEach(p -> {
                changeTaskStatusMap.put(p.getValueDetail(), p.getValue());
            });
            dataMap.forEach((key, value) -> {
                if (value != null && !"".equals(value.toString().trim())) {
                    if (ChangeTaskFieldEnum.TASK_STATUS.getName().equals(key)) {
                        dataMap.put(key, changeTaskStatusMap.get(value.toString().trim()));
                    } else if (ChangeTaskFieldEnum.REFER_SYS.getName().equals(key)) {
                        OgSys sys = applicationManagerService.selectOgSysBySysCode(value.toString());
                        String reSysId = "";
                        if (sys != null) {
                            reSysId = sys.getSysId();
                        }
                        dataMap.put(key, reSysId);
                    }
                }
                if (value == null) {
                    dataMap.put(key, "");
                }else {
                    dataMap.put(key,String.valueOf(dataMap.get(key)));
                }
            });
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(dataMap, headers);
            log.info("httpEntity：" + JSON.toJSONString(httpEntity));
            ResponseEntity<JSONObject> entity = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
            log.info("创建或更新变更子任务信息: {}", entity);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                return AjaxResult.warn("创建或更新变更子任务信息失败：" + entity);
            }
            JSONObject result = entity.getBody();
            if (!"0".equals(result.getString("resCode"))) {
                log.error("根据工单号[{}]创建或更新变更子任务信息失败：", entity);
                return AjaxResult.warn("创建或更新变更子任务信息失败：" + result.getString("resMsg"));
            } else {
                return AjaxResult.success(result.getString("resMsg"));
            }
        } catch (Exception e) {
            log.error("创建或更新变更子任务信息异常：", e);
            return AjaxResult.warn("创建或更新变更子任务信息异常：" + e);
        }
    }

    /**
     * 调用adpm，执行查询ITSM变更单号对应的ADPM任务完成情况
     */
    @Override
    public AjaxResult queryAdpmTask(String changeNo) {
        // 查询ITSM变更单号对应的ADPM任务完成情况
        try {
            String url = adpm_customerServiceUrl + "/checkAdpmTaskbyItsmNo";
            log.info("url：" + url);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("changeNo", changeNo);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(dataMap, headers);
            log.info("httpEntity：" + JSON.toJSONString(httpEntity));
            ResponseEntity<JSONObject> entity = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
            log.info("执行查询ITSM变更单号对应的ADPM任务完成情况: {}", entity);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                log.error("执行查询ITSM变更单号对应的ADPM任务完成情况失败：", entity);
                return AjaxResult.error("查询ADPM信息失败");
            }
            JSONObject result = entity.getBody();
            String msg = result.getString("resMsg");
            if (!"0".equals(result.get("resCode"))) {
                log.error("根据工单号[{}]执行查询ITSM变更单号对应的ADPM任务完成情况失败：", entity);
                return AjaxResult.error(msg);
            } else {
                Map<String, Object> resData = (Map<String, Object>) result.get("resData");
                String statusCode = resData.get("statusCode").toString();
                if ("1".equals(statusCode)|| "4".equals(statusCode)) {
                    return AjaxResult.success();
                } else if ("2".equals(statusCode)) {
                    return AjaxResult.warn(resData.get("desc").toString());
                }
                return AjaxResult.success();
            }
        } catch (Exception e) {
            log.error("执行查询ITSM变更单号对应的ADPM任务完成情况异常：", e);
            return AjaxResult.error("查询异常：" + e);
        }
    }

    @Override
    public Map<String, Object> queryDonelist(String jsonData) {
        Map<String, Object> data = JSON.parseObject(jsonData);
        Map<String, Object> map = new HashMap<>();
        // changeState2:代办3:已办
        // workerId
        String changeNo = String.valueOf(data.get("changeNo"));
        String userNo = String.valueOf(data.get("userNo"));
        String changeState = String.valueOf(data.get("type"));
        String startTime = String.valueOf(data.get("startDate"));
        String endTime = String.valueOf(data.get("endDate"));
        String state = changeState;
        if ("2".equals(changeState)) {
            state = changeState;
        } else if ("3".equals(changeState)) {
            state = changeState;
        } else {
            map.put("msg", "传递类型状态码不符合2 或3");
            map.put("code", "-1");
            return map;
        }
        List<Map<String, Object>> list = customerFormMapper.queryChangeList(changeNo, userNo, state, startTime, endTime);
        String json = JSON.toJSONString(list);
        return AjaxResult.success(json);
    }

    @Override
    public Map<String, Object> queryChangeDetail(String changeNo) {
        // changeNo 单号
        Map<String, Object> detail = customerFormMapper.queryChangeDetail(changeNo);
        return AjaxResult.success(detail);
    }

    @Override
    public Map<String, Object> queryworkdetaillist(String changeNo, Page page) {
        // changeNo 单号
//		Map<String, Object> detail = customerFormMapper.queryChangeInfo(changeNo);
        return customerFormService.getOperationDetails(changeNo, page);
    }

    @Override
    public Map<String, Object> queryChangeTaskDetail(String changeNo) {
        List<Map<String, Object>> detail = customerFormMapper.queryChangeTaskDetail(changeNo);
        return AjaxResult.success(detail);
    }

    @Override
    public Map<String, Object> queryChangeFiles(String changeNo) {
        // changeTaskNo
        List<Map<String, Object>> list = customerFormMapper.queryChangeFiles(changeNo);
//		String json = JSON.toJSONString(list);
        return AjaxResult.success(list);
    }

    @Override
    public Map<String, Object> confirmChangeTaskResult(String jsonData) {
        // changeTaskNo
        Map<String, Object> data = JSON.parseObject(jsonData);
        String changeTaskNo = String.valueOf(data.get("changeTaskNo"));
        List<Map<String, Object>> list = customerFormMapper.confirmChangeTaskResult(changeTaskNo);
        String json = JSON.toJSONString(list);
        return AjaxResult.success(json);
    }

    @Override
    public AjaxResult maintain(Map<String, Object> data) {
        // 監控告警維護期開始
        try {
            log.info("jsonData:" + JSONUtils.toJSONString(data));
            String url = alarmMaintenanceUrl + "/refiner/api/itsm/maintain";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(data, headers);
            log.info("url:" + url);
            ResponseEntity<JSONObject> entity = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
            log.info("执行告警维护期任务完成情况: {}", entity);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                log.error("执行告警维护期任务完成情况失败：", entity);
                return AjaxResult.error("执行告警维护期任务信息失败");
            }
            JSONObject result = entity.getBody();
            if (!"0000".equals(result.getString("retCode"))) {
                log.error("执行告警维护期完成情况失败：", entity);
                return AjaxResult.error("执行告警维护期务完成情况：" + result.getString("retMsg"));
            } else {
                return AjaxResult.success(result.getString("retMsg"));
            }
        } catch (Exception e) {
            log.error("执行告警维护期任务情况异常：", e);
            e.printStackTrace();
            return AjaxResult.error("执行告警维护期情况异常：" + e.getMessage());
        }
    }

    @Override
    public AjaxResult updateActive(String changeId, boolean active) {
        try {
            log.info("changeId:" + changeId + " active:" + active);
            String url = alarmMaintenanceUrl + "/refiner/api/itsm/maintain/updateActive?changeId=" + changeId + "&active=" + active;
            Map<String, Object> data = new HashMap<>();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(data, headers);
            ResponseEntity<JSONObject> entity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, JSONObject.class);
            log.info("执行维护窗口关闭接口情况: {}", entity);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                log.error("执行维护窗口关闭接口失败：", entity);
                return AjaxResult.error("执行维护窗口关闭接口失败");
            }
            JSONObject result = entity.getBody();
            if ("0000".equals(result.getString("retCode"))) {
                return AjaxResult.success(result.getString("retMsg"));
            } else if ("1002".equals(result.getString("retCode"))) {
                return AjaxResult.success("无告警屏蔽需要关闭");
            } else {
                log.error("执行维护窗口关闭接口失败：", entity);
                return AjaxResult.error("执行维护窗口关闭接口情况：" + result.getString("retMsg"));
            }
        } catch (Exception e) {
            log.error("执行维护窗口关闭接口异常：", e);
            e.printStackTrace();
            return AjaxResult.error("执行维护窗口关闭接口异常：" + e.getMessage());
        }
    }


    @Autowired
    private MailUtils mailUtils;
    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Override
    public String sendComplexMail(String taskNo, String receivers, String copys, String subject, String content,
                                  String imgPath, String filePath1, String filePath2) {
        String result = mailUtils.sendComplexMail(taskNo, receivers, copys, subject, content, imgPath, filePath1, filePath2);
        String state = "0";
        if (!MailUtils.MESSAGE_SEND_SUCCESS.equals(result)) {
            state = "1";
        }
        PubBizSms sms = new PubBizSms();
        sms.setSmsTime(DateUtils.getToday("yyyyMMddHHmmss"));
        sms.setName(receivers);
        sms.setTelephone(receivers.length() < 32 ? receivers : receivers.substring(0,32));
        sms.setSmsText("taskNo:" + taskNo + " state:" + state + " SUB:" + subject);
        sms.setPubBizSmsId(UUID.getUUIDStr());
        sms.setCreaterId(taskNo);
        sms.setSmsState(state);
        sms.setInvalidationMark("1");
        pubBizSmsService.insertPubBizSms(sms);
        return result;
    }

    @Override
    public String sendSmsMessageBySocket(String userNo, String taskNo, String mobiles, String contents) {
        String result = smsUtils.sendSmsMessageBySocket(taskNo, mobiles, contents);
        Map<String, Object> dataMap = JSON.parseObject(result);
        String state = "0";
        if (!"0".equals(dataMap.get("isSuccess"))) {
            state = "1";
        }
        PubBizSms sms = new PubBizSms();
        sms.setSmsTime(DateUtils.getToday("yyyyMMddHHmmss"));
        sms.setName(userNo);
        sms.setTelephone(mobiles);
        sms.setSmsText("taskNo:" + taskNo + " state:" + state + " contents:" + contents);
        sms.setPubBizSmsId(UUID.getUUIDStr());
        sms.setCreaterId(userNo);
        sms.setSmsState(state);
        sms.setInvalidationMark("1");
        pubBizSmsService.insertPubBizSms(sms);
        return result;
    }

    @Override
    public Map<String, Object> osUserApplication(String jsonData) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> data = this.cmdbDataChange(jsonData);
        String url = cmdbUrl + "/gateway/";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(data, headers);
            ResponseEntity<JSONObject> entity = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
            log.info("获取操作系统用户列表: {}", entity);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                log.error("获取操作系统用户列表：", entity);
                return AjaxResult.error("获取操作系统用户列表失败");
            }
            JSONObject result = entity.getBody();
            map.put("data", result.toString());
            map.put("msg", result.getString("retMsg"));
            map.put("code", "0");
            if (!"200".equals(result.get("RetCode"))) {
                map.put("data", "");
                log.error("获取操作系统用户列表失败：", entity);
                map.put("msg", "获取操作系统用户列表失敗：" + result.getString("RetMsg"));
                map.put("code", "-1");
            }
        } catch (Exception e) {
            log.error("获取操作系统用户列表异常：", e);
            map.put("data", "");
            map.put("msg", "获取操作系统用户列表异常：" + e.getMessage());
            map.put("code", "-1");
            return map;
        }
        return map;
    }

    @Override
    public Map<String, Object> osApplication(String jsonData) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> data = this.cmdbDataChange(jsonData);
        String url = cmdbUrl + "/gateway/";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(data, headers);
            ResponseEntity<JSONObject> entity = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
            log.info("获取操作系统应用列表情况: {}", entity);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                log.error("获取操作系统应用列表失败：", entity);
                return AjaxResult.error("获取操作系统应用列表失败");
            }
            JSONObject result = entity.getBody();
            map.put("data", result.toString());
            map.put("msg", result.getString("retMsg"));
            map.put("code", "0");
            if (!"200".equals(result.get("RetCode"))) {
                log.error("获取操作系统用户列表失败：", entity);
                map.put("data", "");
                map.put("msg", "获取操作系统用户列表失敗：" + result.getString("RetMsg"));
                map.put("code", "-1");
            }
        } catch (Exception e) {
            log.error("获取操作系统应用列表异常：", e);
            map.put("data", "");
            map.put("msg", "获取操作系统应用列表异常：" + e.getMessage());
            map.put("code", "-1");
            return map;
        }
        return map;
    }

    public Map<String, Object> cmdbDataChange(String jsonData) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> data = JSON.parseObject(jsonData);
        Map<String, Object> headMapData = new HashMap<>();
        Map<String, Object> bodyData = new HashMap<>();
        Map<String, Object> pmsMap = new HashMap<>();
        Map<String, Object> propsMap = new HashMap<>();
        headMapData.put("ApiId", data.get("ApiId"));
        headMapData.put("SvcVerNo", "1.0");
        headMapData.put("Token", token);
        headMapData.put("CnsmrSysId", itsmPaso);
        headMapData.put("OrgnlCnsmrSysId", data.get("orgnlCnsmrPaso"));
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyMMddHHmmss");
        headMapData.put("GlblSrlNo", DateUtils.dateTime() + "00100101"+sdf.format(new Date()));//(本系统交易日期（8位：YYYYMMDD)+应用编号（3位）+子应用编号（3位） +集群编号(2位) +流水序号（12位）)
        headMapData.put("CnsmrSrlNo", data.get("changeNo"));
        headMapData.put("MsgVerNo", "3.0");
        headMapData.put("TxnDt", DateUtils.dateTime());
        headMapData.put("TxnTm", DateUtils.dateTimeStr());
        headMapData.put("InttCnlCd", "C01");
        headMapData.put("CnlTxnCd", "9801");
        headMapData.put("CnsmrTxnCd", "9801");
        headMapData.put("MAC", "");
        headMapData.put("OrgnlCnsmrSvcNo", IpUtils.getHostIp());
        headMapData.put("CnsmrSvcNo", IpUtils.getHostIp());
        headMapData.put("SysRsrvFlgStr", "remark");
        headMapData.put("SysRsrvStr", "remark");
        headMapData.put("InstId", data.get("orgId"));
        headMapData.put("UsrNo", data.get("userNo"));
        pmsMap.put("paso", data.get("orgnlCnsmrPaso"));
        pmsMap.put("dept_name", data.get("ogOrgName"));
        propsMap.put("limit", "100");
        propsMap.put("start", "1");
        bodyData.put("pms", pmsMap);
        bodyData.put("props", propsMap);
        map.put("Head", headMapData);
        map.put("Body", bodyData);
        return map;
    }
	public String getTaskId(String taskNo,String taskType){
	  String ret="";
	   String tableName="design_biz_incident";
	   if("incident".equals(taskType)) {
		   tableName="design_biz_incident";
	   }else  if("problem".equals(taskType)) {
		   tableName="design_biz_problem";
	   }else  if("change".equals(taskType)) {
		   tableName="design_biz_change";
	   }
	   Long id= customerFormMapper.getTaskId(taskNo,tableName);
	   ret=taskType+id;
	   return ret;
	}
	
	@Override
	public List<Map<String, Object>> ittablesCol(String tableName) {
		return customerFormMapper.ittablesCol(tableName);
	}
	@Override
	public List<Map<String, Object>> ittablesCols(String tableName) {
		  Map<String, Object> data = JSON.parseObject(tableName);
		return customerFormMapper.ittablesCol(String.valueOf(data.get("tableName")));
	}
	@Override
	public List<Map<String, Object>> ittablesData(String itsmNo, String tableName) {
		return customerFormMapper.ittablesData(itsmNo,tableName);
	}

	@Override
	public int insertData(String itsmNo,String tableName,String jsonData) {
		customerFormMapper.del(itsmNo, tableName);
		Map<String, Object> datas = JSON.parseObject(jsonData);
//		for (Map<String, Object> iterable_element : datas) {
//			String sql="insert into "+tableName+" values()";
//			customerFormMapper.insertData(sql);
//		}
		return 0;
	}
}
