package com.ruoyi.form.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.constants.EventFieldConstants;
import com.ruoyi.form.constants.TinywebConstants;
import com.ruoyi.form.domain.CustomerFormSearchDTO;
import com.ruoyi.form.enums.CustomerBusinessEnum;
import com.ruoyi.form.enums.ProblemStatus;
import com.ruoyi.form.enums.RedundancyFieldEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.service.ComprehensiveQueryService;
import com.ruoyi.form.service.IChangePersonService;
import com.ruoyi.form.vo.ComprehensiveQuery;
import com.ruoyi.form.vo.ExportExcelParams;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.system.mapper.PubParaValueMapper;
import com.ruoyi.system.service.*;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.form.constants.ProblemConstant.*;

/**
 * @ClassName ComprehensiveQueryServiceImpl
 * @Description 综合查询
 * @Author JiaQi Zhang
 * @Date 2022/9/20 13:44
 */
@Service
@RequiredArgsConstructor
public class ComprehensiveQueryServiceImpl implements ComprehensiveQueryService {

    private static final String bizTablePrefix="design_biz";

    private  final CustomerFormMapper customerFormMapper;
    private final PubParaValueMapper pubParaValueMapper;
    private final ISysDeptService iSysDeptService;
    private final IOgPersonService ogPersonService;
    private final IOgTypeinfoService ogTypeinfoService;
    private final IOgGroupPersonService ogGroupPersonService;
    private final ISysApplicationManagerService applicationManagerService;
    private final ISysWorkService sysWorkService;
    private final IPubParaValueService pubParaValueService;
    private final IChangePersonService changePersonService;
    @Resource
    TaskService taskService;

    @Resource
    RuntimeService runtimeService;

    @Override
    public AjaxResult comprehensiveQuery(Page page,ComprehensiveQuery comprehensiveQuery) {
        QueryWrapper queryWrapper=new QueryWrapper();
        //   获取表单版本ID、表单版本号
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, comprehensiveQuery.getCode()), null);
        if (ObjectUtil.isEmpty(currentTableInfo)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getDesc());
        }
        //根据表单版本ID获取当前表单当前版本的字段
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        if (CollectionUtil.isEmpty(formFieldInfos)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getDesc());
        }

        //获取表名的中文名
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));

        extracted(queryWrapper,formName, currentTableInfo,  formFieldInfos);

        comprehensiveQuery.getSearchDTOList().forEach(a->{
            build(queryWrapper,a);
        });

        // 问题单和问题任务单要根据当前登录人所在机构是总行或分行,总行管理员可查询到所有问题单的数据但总行普通人员只能查询到总行数据,分行管理员及普通人员只能查询到对应分行的数据
        if (Arrays.asList(WorkOrderInformation.problem.getCode(), WorkOrderInformation.problem_task.getCode()).contains(comprehensiveQuery.getCode())) {
            OgOrg ogOrg = iSysDeptService.selectDeptById(ogPersonService.selectOgPersonById(CustomerBizInterceptor.currentUserThread.get().getUserId()).getOrgId());
            String orgId = changePersonService.selectDept(ogOrg.getOrgId());
            if (StringUtils.isBlank(orgId)) {
                throw  new BusinessException("问题发起人所在机构不正确,非总行或分行人员!");
            }
            // 查询总行管理员
            List<OgGroupPerson> ogGroupPeopleList = ogGroupPersonService.selectOgGroupPersonById("f3ec04830847402288b5cf45a263c412");
            List<OgGroupPerson> ogGroupPeople = ogGroupPeopleList.stream().filter(ogGroupPerson -> {
                return ogGroupPerson.getPid().equals(CustomerBizInterceptor.currentUserThread.get().getUserId());
            }).collect(Collectors.toList());

            // 不是总行管理员
            if (CollectionUtils.isEmpty(ogGroupPeople)) {
                queryWrapper.eq(ORG_ID, orgId);
            }
        }

        queryWrapper.orderByDesc("id");
        dynamicTableName(comprehensiveQuery.getCode());
        Page page1 = customerFormMapper.selectMapsPage(page, queryWrapper);
        convertIdToName(comprehensiveQuery.getCode(),page1.getRecords());
        if (Arrays.asList(WorkOrderInformation.problem.getCode(), WorkOrderInformation.problem_task.getCode()).contains(comprehensiveQuery.getCode())) {
            // 构造返回结果,待办的数据需要返回taskId
            buildResultList(comprehensiveQuery.getCode(), page1);
        }
        MybatisPlusConfig.customerTableName.remove();
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("pageListInfo", page1);
        resultMap.put("fieldInfo", formFieldInfos);
        return AjaxResult.success(resultMap);
    }

    /***
     * 构建数据
     *
     * @param page1
     */
    private void buildResultList(String code, Page page1) {
        List<Map<String, Object>> records = (List<Map<String, Object>>) page1.getRecords();
        records.forEach(a -> {
            if (!Arrays.asList(ProblemStatus.WAIT_SUBMIT.getInfo(), ProblemStatus.CANCEL.getInfo(), ProblemStatus.CLOSE.getInfo()).contains(a.get(STATUS).toString())) {
                List<Task> taskList;
                // 如果是问题单,且状态是提交总经理室审核
                if (code.equals(WorkOrderInformation.problem.getCode()) && ProblemStatus.GENERAL_MANAGER_AUDIT.getInfo().equals(a.get(STATUS).toString())) {
                    taskList = taskService.createTaskQuery().taskCandidateOrAssigned(CustomerBizInterceptor.currentUserThread.get().getUserId())
                            .processInstanceId(a.get("instance_id").toString())
                            .list();    // 总经理室审核，会同时拥有多个任务(选择了几个人同时审批)
                    // 如果为空证明当前登录人不是总经理室选定人
                    if (CollectionUtils.isEmpty(taskList)) {
                        // 查询总经理室组的待办人员id
                            Map<String, Object> managerMap = runtimeService.getVariables(String.valueOf(a.get("instance_id")));
                            List<String> generalManagerIds = (List<String>) managerMap.get("generalManagerIds");
                            for (String generalManagerId : generalManagerIds) {
                                // 如果有任务,则跳循环
                                taskList = taskService.createTaskQuery().taskCandidateOrAssigned(generalManagerId)
                                        .processInstanceId(a.get("instance_id").toString())
                                        .list();
                                if (!CollectionUtils.isEmpty(taskList)) {
                                    break;
                                }
                            }
                    }
                } else {
                    taskList = taskService.createTaskQuery().processInstanceId(String.valueOf(a.get("instance_id"))).list();
                }
                //TaskEntityImpl task1 = (TaskEntityImpl) unfinished.get(0);
                if (!org.springframework.util.CollectionUtils.isEmpty(taskList)) {
                    TaskEntityImpl task = (TaskEntityImpl) taskList.get(0);
                    a.put("taskId", task.getId());
                }
            } else {
                a.put("taskId", null);
            }
        });
        page1.setRecords(records);
    }


    @Override
    public AjaxResult getBizCode() {
        List<PubParaValue> pubParaValues = pubParaValueMapper.selectPubParaValueByParaName("formCode");
        return AjaxResult.success(pubParaValues);
    }

    @Override
    public void exportExcel(ExportExcelParams params,HttpServletResponse response) {
        QueryWrapper queryWrapper=new QueryWrapper();
        Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, params.getCode()), null);
        if (ObjectUtil.isEmpty(currentTableInfo)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_VERSION_EXCEPTION.getDesc());
        }
        //根据表单版本ID获取当前表单当前版本的字段
        List<Map<String, String>> formFieldInfos = customerFormMapper.getFormFieldInfo(currentTableInfo.get("id"));
        if (CollectionUtil.isEmpty(formFieldInfos)) {
            throw new BusinessException(CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getCode(), CustomerBusinessEnum.FORM_FIELD_EXCEPTION.getDesc());
        }

        //获取表名的中文名
        String formName = customerFormMapper.getFormName(currentTableInfo.get("id"));
        //过滤需要导出字段的数据
        List<Map<String, String>> exportable = formFieldInfos.stream().filter(a -> String.valueOf(a.get("exportable")).equals("1")).collect(Collectors.toList());

        extracted(queryWrapper, formName,currentTableInfo, exportable);

        if (!params.getType().equals("1")&&CollectionUtil.isNotEmpty(params.getIds())){
            queryWrapper.in("id",params.getIds());
        }

        dynamicTableName(params.getCode());
        List<Map<String, Object>> resultList = customerFormMapper.selectMaps(queryWrapper);


        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(formName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(response.getOutputStream()).head(head(exportable)).sheet("模板").doWrite(dataList(params.getCode(),resultList,exportable));
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    private void extracted(QueryWrapper queryWrapper, String formName,Map<String, Long> currentTableInfo, List<Map<String, String>> exportable) {

        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("name", "status");
        statusMap.put("description", "工单状态");
        statusMap.put("display", "1");
        statusMap.put("exportable", "0");
        statusMap.put("editable", "0");
        statusMap.put("record_data_change", "0");
        exportable.add(0, statusMap);
        Map<String, String> bizNo = new HashMap<>();
        bizNo.put("name", RedundancyFieldEnum.extra1.name);
        bizNo.put("description", formName + "编号");
        bizNo.put("display", "1");
        bizNo.put("exportable", "0");
        bizNo.put("editable", "0");
        bizNo.put("record_data_change", "0");
        exportable.add(0, bizNo);
        String name ="id,"+"instance_id,"+ exportable.stream().map(a -> a.get("name")).collect(Collectors.joining(","));
        queryWrapper.select(name).orderByDesc("id");
    }

    /**
     * 构造表头
     * @param exportable 需要导出的字段
     * @return
     */
    private List<List<String>> head(List<Map<String, String>> exportable) {
        List<List<String>> list = ListUtils.newArrayList();
        exportable.stream().forEach(a->{
            List<String> head = ListUtils.newArrayList();
            head.add(a.get("description"));
            list.add(head);
        });
        return list;
    }

    /**
     * 构造导出的内容
     * @param resultList
     * @param exportable
     * @return
     */
    private List<List<Object>> dataList(String code,List<Map<String, Object>> resultList,List<Map<String, String>> exportable) {
        List<List<Object>> list = ListUtils.newArrayList();
        List<String> keys=new ArrayList<>();
        convertIdToName(code,resultList);

        //key按照表头顺序添加到集合
        exportable.forEach(a-> keys.add(a.get("name")));

        //TODO  根据需要转人名的字段去转中文
        resultList.forEach(a->{
            List<Object> data = ListUtils.newArrayList();
            keys.forEach(b->{
                data.add(a.get(b));
            });
            list.add(data);

        });

        return list;
    }


    // 人员 部门 基础数据id转名称
    private void convertIdToName(String code, List<Map<String, Object>> records) {
        if (code.equals(WorkOrderInformation.problem.getCode())) {
            records.forEach(problem -> {
                if (ObjectUtil.isNotEmpty(problem.get(ORIGINATOR_ID))) {
                    problem.put(ORIGINATOR_ID, ogPersonService.selectOgPersonById(problem.get(ORIGINATOR_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(SOLVER_ID))) {
                    problem.put(SOLVER_ID, ogPersonService.selectOgPersonById(problem.get(SOLVER_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(AUDITOR_ID))) {
                    problem.put(AUDITOR_ID, ogPersonService.selectOgPersonById(problem.get(AUDITOR_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(ORI_DEP_MANAGER_ID))) {
                    problem.put(ORI_DEP_MANAGER_ID, ogPersonService.selectOgPersonById(problem.get(ORI_DEP_MANAGER_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(MANAGER_ID))) {
                    problem.put(MANAGER_ID, ogPersonService.selectOgPersonById(problem.get(MANAGER_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(CURRENT_HANDLER_ID))) {
                    problem.put(CURRENT_HANDLER_ID, ogPersonService.selectOgPersonById(problem.get(CURRENT_HANDLER_ID).toString()).getpName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(SOLVER_DEP_ID))) {
                    problem.put(SOLVER_DEP_ID, iSysDeptService.selectDeptById(problem.get(SOLVER_DEP_ID).toString()).getOrgName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(INTERRUPT_FLAG))) {
                    problem.put(INTERRUPT_FLAG, StringUtils.equals(InterruptFlag.ONE, problem.get(INTERRUPT_FLAG).toString())
                            ? InterruptFlag.CH_ZERO
                            : InterruptFlag.CH_ONE);
                }
                if (ObjectUtil.isNotEmpty(problem.get(CAUSE_CLZ1))) {
                    problem.put(CAUSE_CLZ1, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(CAUSE_CLZ1).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(CAUSE_CLZ2))) {
                    problem.put(CAUSE_CLZ2, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(CAUSE_CLZ2).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_CATEGORY))) {
                    problem.put(PROBLEM_CATEGORY, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_CATEGORY).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_SUBCLZ))) {
                    problem.put(PROBLEM_SUBCLZ, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_SUBCLZ).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_ENTRY))) {
                    problem.put(PROBLEM_ENTRY, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_ENTRY).toString()).getTypeName());
                }
                if (ObjectUtil.isNotEmpty(problem.get(PROBLEM_SUBENTRY1))) {
                    problem.put(PROBLEM_SUBENTRY1, ogTypeinfoService.selectOgTypeInfoByTypeNo(problem.get(PROBLEM_SUBENTRY1).toString()).getTypeName());
                }
            });
        } else if (code.equals(WorkOrderInformation.problem_task.getCode())) {
            records.forEach(task -> {
                if (StringUtils.isNotEmpty(task.get(CURRENT_HANDLER_ID_TASK))) {
                    OgPerson person = ogPersonService.selectOgPersonById(task.get(CURRENT_HANDLER_ID_TASK).toString());
                    if (person != null)
                        task.put(CURRENT_HANDLER_ID_TASK, person.getpName());
                }
                if (StringUtils.isNotEmpty(task.get(ORIGINATOR_ID))) {
                    OgPerson person = ogPersonService.selectOgPersonById(task.get(ORIGINATOR_ID).toString());
                    if (person != null)
                        task.put(ORIGINATOR_ID, person.getpName());
                }
            });
        } else if (code.equals(WorkOrderInformation.incident.getCode())) {
            records.forEach(event -> {
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.REPORT_ORG))) {
                    OgOrg reportOrg = iSysDeptService.selectDeptByCode(event.get(EventFieldConstants.REPORT_ORG).toString());
                    if (reportOrg != null) {
                        event.put(EventFieldConstants.REPORT_ORG, reportOrg.getOrgName());
                    }
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.REPORT_PERSON))) {
                    OgPerson person = ogPersonService.selectOgPersonById(event.get(EventFieldConstants.REPORT_PERSON).toString());
                    if (person != null)
                        event.put(EventFieldConstants.REPORT_PERSON, person.getpName());
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.ASSIGNED_GROUP))) {
                    OgGroup group = sysWorkService.selectOgGroupById(event.get(EventFieldConstants.ASSIGNED_GROUP).toString());
                    if (group != null)
                        event.put(EventFieldConstants.ASSIGNED_GROUP, group.getGrpName());
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.ASSIGNED_PERSON))) {
                    OgPerson person = ogPersonService.selectOgPersonById(event.get(EventFieldConstants.ASSIGNED_PERSON).toString());
                    if (person != null)
                        event.put(EventFieldConstants.ASSIGNED_PERSON, person.getpName());
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.SIDE_FLAG))) {
                    List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName("side_flag");
                    event.put(EventFieldConstants.SIDE_FLAG, EventFieldConstants.convertParaList(list, event.get(EventFieldConstants.SIDE_FLAG).toString()));
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.FINANCE_FLAG))) {
                    event.put(EventFieldConstants.FINANCE_FLAG, EventFieldConstants.convertFinanceFlag(event.get(EventFieldConstants.FINANCE_FLAG).toString()));
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.URGENT_FLAG))) {
                    event.put(EventFieldConstants.URGENT_FLAG, EventFieldConstants.convertFinanceFlag(event.get(EventFieldConstants.URGENT_FLAG).toString()));
                }
                if (StringUtils.isNotEmpty(event.get(EventFieldConstants.ORG_FLAG))) {
                    List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName("org_flag");
                    event.put(EventFieldConstants.ORG_FLAG, EventFieldConstants.convertParaList(list, event.get(EventFieldConstants.ORG_FLAG).toString()));
                }
            });
        } else if (code.equals(WorkOrderInformation.TINYWEB_DB_RECOVER.getCode())) {
            //tinyWeb 数据库恢复 列表 部分字段ID转译
            records.forEach(dbRecover -> {
                if (StringUtils.isNotEmpty(dbRecover.get(TinywebConstants.APP_SYSTEM))) {
                    OgSys ogSys = applicationManagerService.selectOgSysBySysId(dbRecover.get(TinywebConstants.APP_SYSTEM).toString());
                    dbRecover.put(TinywebConstants.APP_SYSTEM, ogSys.getCaption());
                }
                if (StringUtils.isNotEmpty(dbRecover.get(TinywebConstants.CHECK_PEOPLE))) {
                    OgPerson person = ogPersonService.selectOgPersonEvoById(dbRecover.get(TinywebConstants.CHECK_PEOPLE).toString());
                    dbRecover.put(TinywebConstants.CHECK_PEOPLE, person.getpName());
                }
                if (StringUtils.isNotEmpty(dbRecover.get(TinywebConstants.DESENSITIZATION_TYPE))) {

                    List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName(TinywebConstants.DESENSITIZATION_TYPE);
                    for (PubParaValue pubParaValue : list) {
                        if (pubParaValue.getValue().equals(dbRecover.get(TinywebConstants.DESENSITIZATION_TYPE))) {
                            dbRecover.put(TinywebConstants.DESENSITIZATION_TYPE, pubParaValue.getValueDetail());
                            break;
                        }
                    }
                }
            });
        } else if (code.equals(WorkOrderInformation.TINYWEB_FAULT_SOLVE.getCode())) {
            //tinyWeb 故障解决 列表 部分字段ID转译
            records.forEach(faultSolve -> {
                if (StringUtils.isNotEmpty(faultSolve.get(TinywebConstants.APP_SYSTEM))) {
                    OgSys ogSys = applicationManagerService.selectOgSysBySysId(faultSolve.get(TinywebConstants.APP_SYSTEM).toString());
                    faultSolve.put(TinywebConstants.APP_SYSTEM, ogSys.getCaption());
                }
                if (StringUtils.isNotEmpty(faultSolve.get(TinywebConstants.CHECK_PEOPLE))) {
                    OgPerson person = ogPersonService.selectOgPersonEvoById(faultSolve.get(TinywebConstants.CHECK_PEOPLE).toString());
                    faultSolve.put(TinywebConstants.CHECK_PEOPLE, person.getpName());
                }
                if (StringUtils.isNotEmpty(faultSolve.get(TinywebConstants.MALFUNCTION_TYPE))) {

                    List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName(TinywebConstants.FAULT_TYPE);
                    for (PubParaValue pubParaValue : list) {
                        String type = (String) faultSolve.get(TinywebConstants.MALFUNCTION_TYPE);
                        if (pubParaValue.getValue().equals(type)) {
                            faultSolve.put(TinywebConstants.MALFUNCTION_TYPE, pubParaValue.getValueDetail());
                            break;
                        }
                    }
                }
            });
        } else if (code.equals(WorkOrderInformation.TINYWEB_SERVER.getCode())) {
            //tinyWeb 服务请求 列表 部分字段ID转译
            records.forEach(server -> {
                if (StringUtils.isNotEmpty(server.get(TinywebConstants.APP_SYSTEM))) {
                    OgSys ogSys = applicationManagerService.selectOgSysBySysId(server.get(TinywebConstants.APP_SYSTEM).toString());
                    server.put(TinywebConstants.APP_SYSTEM, ogSys.getCaption());
                }
                if (StringUtils.isNotEmpty(server.get(TinywebConstants.CHECK_PEOPLE))) {
                    OgPerson person = ogPersonService.selectOgPersonEvoById(server.get(TinywebConstants.CHECK_PEOPLE).toString());
                    server.put(TinywebConstants.CHECK_PEOPLE, person.getpName());
                }
            });
        }else if(code.equals(WorkOrderInformation.problem_task.getCode())){
            records.forEach(cell->{
                if(StringUtils.isNotEmpty(cell.get("current_handler_id"))){
                    OgPerson person = ogPersonService.selectOgPersonEvoById(cell.get("current_handler_id").toString());
                    cell.put("current_handler_id" , person.getpName());
                }
                if(StringUtils.isNotEmpty(cell.get("originator_id"))){
                    OgPerson person = ogPersonService.selectOgPersonEvoById(cell.get("originator_id").toString());
                    cell.put("originator_id", person.getpName());
                }
            });
        }
    }

    /**
     * 构造动态表名
     *
     * @param code 表名
     */
    private void dynamicTableName(String code) {
        MybatisPlusConfig.customerTableName.set(String.format("%s_%s", bizTablePrefix, code));
    }

    public QueryWrapper build(QueryWrapper queryWrapper, CustomerFormSearchDTO customerFormSearchDTO) {
        switch (customerFormSearchDTO.getSearchCondition()) {
            case "eq":
                queryWrapper.eq(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
            case "ne":
                queryWrapper.ne(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
            case "like":
                queryWrapper.like(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
            case "gt":
                queryWrapper.gt(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
            case "ge":
                queryWrapper.ge(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
            case "lt":
                queryWrapper.lt(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
            case "le":
                queryWrapper.le(customerFormSearchDTO.getSearchKey(), customerFormSearchDTO.getSearchValue());
                break;
        }
        return queryWrapper;
    }
}
