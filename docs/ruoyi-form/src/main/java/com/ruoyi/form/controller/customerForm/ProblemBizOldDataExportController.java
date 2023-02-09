package com.ruoyi.form.controller.customerForm;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.form.domain.ChangeDeptPersonEntity;
import com.ruoyi.form.domain.DesignBizJsonData;
import com.ruoyi.form.domain.DesignBizProblemCopy;
import com.ruoyi.form.enums.ProblemStatus;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.service.DesignBizJsonDataService;
import com.ruoyi.form.service.DesignBizProblemCopyService;
import com.ruoyi.form.service.IChangePersonService;
import com.ruoyi.system.mapper.OgSysMapper;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.DateUtils.YYYY_MM_DD;
import static com.ruoyi.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;

@RestController
@RequestMapping("/excel")
@Api(tags = "导入问题单旧数据")
@RequiredArgsConstructor
public class ProblemBizOldDataExportController extends BaseController {
    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProblemBizOldDataExportController.class);

    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private ISysDeptService iSysDeptService;
    @Resource
    IChangePersonService iChangePersonService;
    @Autowired
    DesignBizProblemCopyService designBizProblemCopyService;
    @Resource
    private OgSysMapper ogSysMapper;
    @Resource
    private CustomerFormMapper customerFormMapper;

    @Autowired
    private DesignBizJsonDataService designBizJsonDataService;
    private static final String bizTablePrefix = "design_biz";

    @Autowired
    /**
     * 导入问题单旧数据
     *
     * @param
     * @return 返回结果
     */
    @GetMapping("/exportProblemOldData")
    @ApiOperation("导入问题单旧数据")
    public AjaxResult exportProblemOldData() throws FileNotFoundException {
        if (new File("D:\\problem_old.xls").exists()) {
            FileInputStream fileInputStream = new FileInputStream(new File("D:\\problem_old.xls"));
            ExcelUtil<DesignBizProblemCopy> util = new ExcelUtil<>(DesignBizProblemCopy.class);
            List<DesignBizProblemCopy> problemList = null;
            try {
                problemList = util.importExcel("problem_data", fileInputStream);
                // 处理数据
                buildAndSaveProblem(problemList);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("导入数据异常:{}", e.getMessage());
                return AjaxResult.warn("导入数据有误的！");
            }

            return AjaxResult.success();
        }
        return AjaxResult.error("找不到文件");
    }

    private void buildAndSaveProblem(List<DesignBizProblemCopy> problemList) {
        List<DesignBizProblemCopy> problems = Lists.newArrayList();
        for (DesignBizProblemCopy problem : problemList) {
            DesignBizProblemCopy designBizProblemNew = new DesignBizProblemCopy();
            // 编号
            designBizProblemNew.setExtra1(problem.getExtra1());
            // 已知错误
            designBizProblemNew.setForError(Arrays.asList("19000").contains(problem.getStatus()) ? "是" : "否");
            // 状态
            designBizProblemNew.setStatus(statusMap.get(problem.getStatus()));
            // 问题提交时间
            if (problem.getSubmitTime1() != null) {
//                designBizProblemNew.setSubmitTime(DateUtils.parseDate(DateUtils.formatDate(problem.getSubmitTime(), YYYY_MM_DD_HH_MM_SS), YYYY_MM_DD));
                designBizProblemNew.setSubmitTime(DateUtils.formatDate(problem.getSubmitTime1(), YYYY_MM_DD_HH_MM_SS));
                // 创建时间
                designBizProblemNew.setCreatedTime(problem.getSubmitTime1());
                designBizProblemNew.setProblemCreatedTime(DateUtils.formatDate(problem.getSubmitTime1(), YYYY_MM_DD));
            }

            // 根据人员名称和邮箱查询人员id
            if (StringUtils.isNotBlank(problem.getOriginatorId())) {
                String name = StringUtils.substringBeforeLast(problem.getOriginatorId(), "(");
                String email = StringUtils.substringBetween(problem.getOriginatorId(), "(", ")") + "@bosc.cn";
                List<OgPerson> ogPeoples = ogPersonService.selectOgPersonList(OgPerson.builder().email(email).pName(name).build());
                if (CollectionUtils.isEmpty(ogPeoples) || ogPeoples.size() > 1) {
                    log.info("根据邮箱:[" + email + "]和人名:" + name + "查询人员为空或查询到多人,请手动为编号:" + problem.getExtra1() + "的数据设置问题发起人和创建人!");
                } else {
                    // 创建人和发起人
                    designBizProblemNew.setCreatedBy(ogPeoples.get(0).getpId());
                    designBizProblemNew.setOriginatorId(ogPeoples.get(0).getpId());

                    // 设置问题发起部室及问题发起部室经理
                    // 根据问题发起人查询问题发起部室
                    // 判断是总行或分行人员,如果分行则需要特殊处理
                    List<PubParaValue> bankList = pubParaValueService.selectPubParaValueById("60a02cbc224344749c4d1d0ec65f6d5a");
                    // 获取到各分行id
                    List<String> orgidList = bankList.stream().map(pubParaValue -> pubParaValue.getValue()).collect(Collectors.toList());
                    String orgId = ogPeoples.get(0).getOrgId();
                    OgOrg ogOrg = iSysDeptService.selectDeptById(orgId);
                    if (orgidList.contains(ogOrg.getParentId())) {
                        orgId = ogOrg.getParentId();
                    }

                    QueryWrapper<ChangeDeptPersonEntity> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("dept_id", orgId);
                    List<ChangeDeptPersonEntity> changeDeptPersonEntityList = iChangePersonService.list(queryWrapper.orderByDesc("create_date"));
                    if (CollectionUtil.isNotEmpty(changeDeptPersonEntityList)) {
                        String deptPersonId = changeDeptPersonEntityList.get(0).getDeptPerson();
                        // 发起部室
                        designBizProblemNew.setOriDepId(changeDeptPersonEntityList.get(0).getOrgId());
                        OgPerson opdept = ogPersonService.selectOgPersonById(deptPersonId);
                        if (opdept != null) {
                            // 发起部室经理
                            designBizProblemNew.setOriDepManagerId(opdept.getpId());
                        }
                    }
                }
            }
            // 更新时间
            if (problem.getUpdatedTime() != null) {
                designBizProblemNew.setUpdatedTime(problem.getUpdatedTime());
                designBizProblemNew.setProblemUpdateTime(DateUtils.formatDate(problem.getUpdatedTime(), YYYY_MM_DD));
            }
            // 问题标题
            designBizProblemNew.setProblemTitle(problem.getProblemTitle());
            // 问题描述
            designBizProblemNew.setProblemDescription(problem.getProblemDescription());
            // 临时解决方案
            designBizProblemNew.setTempSolutions(problem.getTempSolutions());
            // 类别
//            designBizProblemNew.setProblemCategory(problem.getProblemCategory());
            // 来源
            designBizProblemNew.setProblemSource(problemSourceMap.get(problem.getProblemSource()));
            // 子类
//            designBizProblemNew.setProblemSubclz(problem.getProblemSubclz());
            // 问题类型
            designBizProblemNew.setProblemType(problemTypeMap.get(problem.getProblemType()));
            if (StringUtils.isNotBlank(problem.getProblemEntry())) {
                // 条目
//                designBizProblemNew.setProblemEntry(problem.getProblemEntry());
                // 相关应用系统 保存条目 根据条目查询
                String caption = StringUtils.substringBeforeLast(problem.getProblemEntry(), "(");
                String code = StringUtils.substringBetween(problem.getProblemEntry(), "(", ")");
                OgSys ogSys = new OgSys();
                ogSys.setCaption(caption);
                ogSys.setCode(code);
                List<OgSys> ogSysList = ogSysMapper.selectOgSysListByName(ogSys);
                if (ogSysList.size() == 1) {
                    designBizProblemNew.setSystemId(JSON.toJSONString(Arrays.asList(ogSysList.get(0).getCode())));
                }
            }

            // 阶段
            designBizProblemNew.setStage(ProblemStatus.stageMap.get(designBizProblemNew.getStatus()));
            // 优先级
            if (StringUtils.isNotBlank(problem.getPriority())) {
                designBizProblemNew.setPriority(Arrays.asList("0", "1").contains(problem.getPriority()) ? "1" : "5");
            }
            // 问题发生频率
            designBizProblemNew.setFrequency("4");
            // 风险程度
            designBizProblemNew.setRiskLevel("5");
            if (StringUtils.isNotEmpty(problem.getInterruptFlag())) {
                // 影响业务中断
                designBizProblemNew.setInterruptFlag(problem.getInterruptFlag().equals("1") ? "否" : "是");
            }

            if (StringUtils.isNotBlank(problem.getSolverId()) && StringUtils.isNotBlank(problem.getSolverDepId())) {
                // 问题牵头人
                String name = StringUtils.substringBeforeLast(problem.getSolverId(), "(");
                String email = StringUtils.substringBetween(problem.getSolverId(), "(", ")") + "@bosc.cn";
                List<OgPerson> ogPeoples = ogPersonService.selectOgPersonList(OgPerson.builder().email(email).pName(name).build());
                if (CollectionUtils.isEmpty(ogPeoples) || ogPeoples.size() > 1) {
                    log.info("根据邮箱:[" + email + "]和人名:" + name + "查询人员为空或查询到多人,请手动为编号:" + problem.getExtra1() + "的数据设置问题发起人和创建人!");
                } else {
                    // 问题牵头人
                    designBizProblemNew.setSolverId(ogPeoples.get(0).getpId());
                    // 根据牵头人查询
                    List<PubParaValue> bankList = pubParaValueService.selectPubParaValueById("60a02cbc224344749c4d1d0ec65f6d5a");
                    // 获取到各分行id
                    List<String> orgidList = bankList.stream().map(pubParaValue -> pubParaValue.getValue()).collect(Collectors.toList());
                    String orgId = ogPeoples.get(0).getOrgId();
                    OgOrg ogOrg = iSysDeptService.selectDeptById(orgId);
                    if (orgidList.contains(ogOrg.getParentId())) {
                        orgId = ogOrg.getParentId();
                    }

                    QueryWrapper<ChangeDeptPersonEntity> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("dept_id", orgId);
                    List<ChangeDeptPersonEntity> changeDeptPersonEntityList = iChangePersonService.list(queryWrapper.orderByDesc("create_date"));
                    if (CollectionUtil.isNotEmpty(changeDeptPersonEntityList)) {
                        // 问题牵头部室
                        designBizProblemNew.setSolverDepId(changeDeptPersonEntityList.get(0).getOrgId());
                        String deptPersonId = changeDeptPersonEntityList.get(0).getDeptPerson();
                        OgPerson opdept = ogPersonService.selectOgPersonById(deptPersonId);
                        designBizProblemNew.setAuditorId(opdept.getpId());
                    }
                }
            }

            if (StringUtils.isNotBlank(problem.getCauseClz1())) {
                // 根因分类一查询code码
                designBizProblemNew.setCauseClz1(causeClz1Map.get(problem.getCauseClz1()));
            }
            if (StringUtils.isNotBlank(problem.getCauseClz2())) {
                // 根因分类二查询code码
                designBizProblemNew.setCauseClz2(causeClz2Map.get(problem.getCauseClz2()));
            }
            if (StringUtils.isNotBlank(problem.getCurrentDealId())) {
                // 当前处理人
                String name = StringUtils.substringBeforeLast(problem.getCurrentDealId(), "(");
                String email = StringUtils.substringBetween(problem.getCurrentDealId(), "(", ")") + "@bosc.cn";
                List<OgPerson> ogPeoples = ogPersonService.selectOgPersonList(OgPerson.builder().email(email).pName(name).build());
                if (CollectionUtils.isEmpty(ogPeoples) || ogPeoples.size() > 1) {
                    log.info("根据邮箱:[" + email + "]和人名:" + name + "查询人员为空或查询到多人,请手动为编号:" + problem.getExtra1() + "的数据设置问题发起人和创建人!");
                } else {
                    // 设置当前处理人
                    designBizProblemNew.setCurrentDealId(ogPeoples.get(0).getpId());
                }
            }
            if (StringUtils.isNotBlank(problem.getAuditorId())) {
                // 问题审核人
                String name = StringUtils.substringBeforeLast(problem.getAuditorId(), "(");
                String email = StringUtils.substringBetween(problem.getAuditorId(), "(", ")") + "@bosc.cn";
                List<OgPerson> ogPeoples = ogPersonService.selectOgPersonList(OgPerson.builder().email(email).pName(name).build());
                if (CollectionUtils.isEmpty(ogPeoples) || ogPeoples.size() > 1) {
                    log.info("根据邮箱:[" + email + "]和人名:" + name + "查询人员为空或查询到多人,请手动为编号:" + problem.getExtra1() + "的数据设置问题发起人和创建人!");
                } else {
                    // 问题审核人
                    designBizProblemNew.setAuditorId(ogPeoples.get(0).getpId());
                }
            }
            if (StringUtils.isNotBlank(problem.getManagerId())) {
                // 问题管理员
                String name = StringUtils.substringBeforeLast(problem.getManagerId(), "(");
                String email = StringUtils.substringBetween(problem.getManagerId(), "(", ")") + "@bosc.cn";
                List<OgPerson> ogPeoples = ogPersonService.selectOgPersonList(OgPerson.builder().email(email).pName(name).build());
                if (CollectionUtils.isEmpty(ogPeoples) || ogPeoples.size() > 1) {
                    log.info("根据邮箱:[" + email + "]和人名:" + name + "查询人员为空或查询到多人,请手动为编号:" + problem.getExtra1() + "的数据设置问题发起人和创建人!");
                } else {
                    // 问题管理员
                    designBizProblemNew.setManagerId(ogPeoples.get(0).getpId());
                }
            }
            // 根因分析汇总
            designBizProblemNew.setCauseSummary(problem.getCauseSummary());
            // 解决方案汇总
            designBizProblemNew.setSolutionSummary(problem.getSolutionSummary());
            // 解决完成情况
            designBizProblemNew.setResolutionCompletion(problem.getResolutionCompletion());

            // 问题提交解决方案时间
            if (problem.getSubmitSolutionTime1() != null) {
//                designBizProblemNew.setSubmitSolutionTime(DateUtils.parseDate(DateUtils.formatDate(problem.getSubmitSolutionTime(), YYYY_MM_DD_HH_MM_SS), YYYY_MM_DD));
                designBizProblemNew.setSubmitSolutionTime(DateUtils.formatDate(problem.getSubmitSolutionTime1(), YYYY_MM_DD_HH_MM_SS));
            }
            // 问题解决时间
            if (problem.getSolveTime1() != null) {
//                designBizProblemNew.setSolveTime(DateUtils.parseDate(DateUtils.formatDate(problem.getSolveTime(), YYYY_MM_DD_HH_MM_SS), YYYY_MM_DD));
                designBizProblemNew.setSolveTime(DateUtils.formatDate(problem.getSolveTime1(), YYYY_MM_DD_HH_MM_SS));
            }
            // 计划完成时间
            if (problem.getPlanCompleteTime1() != null) {
                designBizProblemNew.setPlanCompleteTime(DateUtils.formatDate(problem.getPlanCompleteTime1(), YYYY_MM_DD_HH_MM_SS));
            }
            // 根因明确时间
            if (problem.getSolverClearTime1() != null) {
//                designBizProblemNew.setSolverClearTime(DateUtils.parseDate(DateUtils.formatDate(problem.getSolverClearTime(), YYYY_MM_DD_HH_MM_SS), YYYY_MM_DD));
                designBizProblemNew.setSolverClearTime(DateUtils.formatDate(problem.getSolverClearTime1(), YYYY_MM_DD_HH_MM_SS));
            }
            // 观察期限
            if (problem.getObserveTime1() != null) {
                designBizProblemNew.setObserveTime(DateUtils.formatDate(problem.getObserveTime1(), YYYY_MM_DD_HH_MM_SS));
//                designBizProblemNew.setObserveTime(DateUtils.parseDate(DateUtils.formatDate(problem.getObserveTime(), YYYY_MM_DD_HH_MM_SS), YYYY_MM_DD));
            }
            // 关闭或取消时间
            if (problem.getCloseTime1() != null || problem.getCancelTime() != null) {
                designBizProblemNew.setCloseTime(problem.getCloseTime1() != null ? DateUtils.formatDate(problem.getCloseTime1(), YYYY_MM_DD_HH_MM_SS) : DateUtils.formatDate(problem.getCancelTime(), YYYY_MM_DD_HH_MM_SS));
//                designBizProblemNew.setCloseTime(problem.getCloseTime() != null ? DateUtils.parseDate(DateUtils.formatDate(problem.getCloseTime(), YYYY_MM_DD_HH_MM_SS), YYYY_MM_DD) : DateUtils.parseDate(DateUtils.formatDate(problem.getCancelTime(), YYYY_MM_DD_HH_MM_SS), YYYY_MM_DD));
            }
            // 解决方案修改次数
            designBizProblemNew.setSolutionModifyNum(problem.getSolutionModifyNum());
            // 计划完成时间修改次数
            designBizProblemNew.setPlanCompleteTimeModifyNum(problem.getPlanCompleteTimeModifyNum());
            // 重新打开次数
            designBizProblemNew.setProblemReopenNum(problem.getProblemReopenNum());
            // 问题取消说明
            designBizProblemNew.setCancelNote(problem.getCancelNote());

            // 问题发起单位 默认总行
            designBizProblemNew.setOrgId("310100003");
            problems.add(designBizProblemNew);
        }
        if (CollectionUtils.isEmpty(problems)) {
            return;
        }
        // 分割数据保存
        List<List<DesignBizProblemCopy>> partition = ListUtils.partition(problems, 20);
        for (List<DesignBizProblemCopy> designBizProblemCopies : partition) {
            // 保存数据
            designBizProblemCopyService.saveBatch(designBizProblemCopies);
        }
        // 组装json数据
        for (List<DesignBizProblemCopy> designBizProblemCopies : partition) {
            List<String> noList = designBizProblemCopies.stream().map(problem -> problem.getExtra1()).collect(Collectors.toList());
            // 根据编号查询数据
            QueryWrapper<DesignBizProblemCopy> designBizProblemCopyQueryWrapper = new QueryWrapper<>();
            designBizProblemCopyQueryWrapper.in("extra1", noList);
            List<DesignBizProblemCopy> problemCopyList = designBizProblemCopyService.list(designBizProblemCopyQueryWrapper);
            buildAndSaveJsonData(problemCopyList);
        }

    }

    private void buildAndSaveJsonData(List<DesignBizProblemCopy> problems) {
        for (DesignBizProblemCopy problem: problems) {
            // 获取design_form_version表中的主键ID
            Map<String, Long> currentTableInfo = customerFormMapper.getCurrentTableInfo(String.format("%s_%s", bizTablePrefix, "problem"), null);
            //根据表单版本ID获取表单json数据
            Map<String, Object> formJsonInfo = customerFormMapper.getFormJsonInfo(currentTableInfo.get("id"));
            String json = (String)formJsonInfo.get("json");
            DesignBizJsonData designBizJsonData = DesignBizJsonData.builder().bizTable(String.format("%s_%s", bizTablePrefix, "problem")).bizId(problem.getId()).jsonData(json).build();
            // 设置json数据里的值并保存 todo
            designBizJsonDataService.save(designBizJsonData);
        }
    }

    public static Map<String, String> statusMap = new HashMap<>();
    public static Map<String, String> problemSourceMap = new HashMap<>();
    public static Map<String, String> problemTypeMap = new HashMap<>();
    public static Map<String, String> causeClz1Map = new HashMap<>();
    public static Map<String, String> causeClz2Map = new HashMap<>();

    static {
//        事件管理
//                变更管理
//        审计发现
//                容量管理
//        巡检发现
//                数据质量
//        配置管理
////
//        可用性管理
//                连续性管理
//        事件趋势分析
//                信息安全管理
//        其他
        problemSourceMap.put("事件管理", "1");
        problemSourceMap.put("变更管理", "2");
        problemSourceMap.put("审计发现", "3");
        problemSourceMap.put("容量管理", "4");
        problemSourceMap.put("巡检发现", "5");
        problemSourceMap.put("数据质量", "");
        problemSourceMap.put("配置管理", "7");
        problemSourceMap.put("重大事件", "8");
        problemSourceMap.put("可用性管理", "9");
        problemSourceMap.put("连续性管理", "10");
        problemSourceMap.put("事件趋势分析", "11");
        problemSourceMap.put("信息安全管理", "12");
        problemSourceMap.put("其他", "19");
    }
    static {
//        性能容量问题
//                可用性问题
//        财务类问题
//                批量问题
//        功能问题
//                流程管理类问题
//        其他
        problemTypeMap.put("性能容量问题", "1");
        problemTypeMap.put("可用性问题", "2");
        problemTypeMap.put("财务类问题", "3");
        problemTypeMap.put("批量问题", "4");
        problemTypeMap.put("功能问题", "5");
        problemTypeMap.put("流程管理类问题", "7");
        problemTypeMap.put("其他", "8");
    }
    static {
        causeClz1Map.put("业务相关", "business");
        causeClz1Map.put("人为错误", "human_error");
        causeClz1Map.put("原因不明", "unknown");
        causeClz1Map.put("基础环境", "environment");
        causeClz1Map.put("应用", "application");
        causeClz1Map.put("硬件", "hardware");
        causeClz1Map.put("网络", "net");
        causeClz1Map.put("软件", "software");
        causeClz1Map.put("其他", "ohter");
    }

    static {
        causeClz2Map.put("业务操作不当", "business_misoperate");
        causeClz2Map.put("业务需求问题", "business_require_problem");
        causeClz2Map.put("操作失误", "operate_miss");
        causeClz2Map.put("未遵循流程", "failure_process");
        causeClz2Map.put("容量不足", "performance_capacity");
        causeClz2Map.put("温度", "temperature");
        causeClz2Map.put("电力供应", "power_supply");
        causeClz2Map.put("自然灾害", "natural_hazard");
        causeClz2Map.put("代码缺陷", "code_debt");
        causeClz2Map.put("接口", "interface_design");
        causeClz2Map.put("第三方问题", "third_issues");
        causeClz2Map.put("配置", "configuration_issues");
        causeClz2Map.put("淘汰产品", "disused_product");
        causeClz2Map.put("硬件故障", "hardware_failure");
        causeClz2Map.put("硬件缺陷", "hardware_deficiency");
        causeClz2Map.put("驱动或固件缺陷", "driver_firmware_defects");
        causeClz2Map.put("对端或第三方问题", "end_on_third_issues");
        causeClz2Map.put("线路故障", "line_fault");
        causeClz2Map.put("网络设备硬件故障", "network_device_faulty");
        causeClz2Map.put("网络设备软件故障", "software_device_faulty");
        causeClz2Map.put("产品缺陷", "product_defect");
    }

    static {
//        草稿：Support Contact Hold:10000
//        观察中：Third Party Vendor Action Reqd:11000
//        审核中：Pending Original Problem:12000
//        已指派：Supplier Delivery:13000
//        调查中：Request:14000
//        根因未明，分析中：Client Action Required:15000
//        根因已明，解决方案未定：Client Hold:16000
//        根因已明，解决中：Monitoring Problem:17000
//        已解决：Future Enhancement:18000
//        已关闭：Automated Resolution Reported:19000
//        已取消：已取消:20000
        statusMap.put("10000", ProblemStatus.WAIT_SUBMIT.getInfo());
        statusMap.put("11000", ProblemStatus.WAIT_SUBMIT.getInfo());
        statusMap.put("12000", ProblemStatus.WAIT_SUBMIT.getInfo());
        statusMap.put("13000", ProblemStatus.WAIT_SUBMIT.getInfo());
        statusMap.put("14000", ProblemStatus.WAIT_SUBMIT.getInfo());
        statusMap.put("15000", ProblemStatus.WAIT_SUBMIT.getInfo());
        statusMap.put("16000", ProblemStatus.WAIT_SUBMIT.getInfo());
        statusMap.put("17000", ProblemStatus.WAIT_SUBMIT.getInfo());
        statusMap.put("18000", ProblemStatus.WAIT_SUBMIT.getInfo());
        statusMap.put("19000", ProblemStatus.CLOSE.getInfo());
        statusMap.put("20000", ProblemStatus.CANCEL.getInfo());
    }
}
