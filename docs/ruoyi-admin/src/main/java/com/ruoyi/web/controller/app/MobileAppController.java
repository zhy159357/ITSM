package com.ruoyi.web.controller.app;

import com.ruoyi.activiti.domain.AmBizNotice;
import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appMobile/workorder")
public class MobileAppController extends BaseController {

    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private ICmBizSingleService iCmBizSingleService;
    @Autowired
    private ICmBizQingqiuService iCmBizQingqiuService;
    @Autowired
    private IVmBizInfoService iVmBizInfoService;
    @Autowired
    private IOgUserService iOgUserService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    private IAmBizNoticeService amBizNoticeService;

    @PostMapping("/getWorkOrderCount")
    public AjaxResult getWorkOrderCount(@RequestBody Map<String, String> map) {
        OgUser user = iOgUserService.selectOgUserByCustNo(map.get("custNo"));
        if (null == user) {
            return AjaxResult.success("根据柜员号未查询到对应人员信息。");
        }
        List<Map<String, Object>> vTaskList = iVmBizInfoService.getVmBnUserTask(user.getUserId(), "VmBn");
        int vCount = getCheckVmBn(vTaskList, "").size();
        List<Map<String, Object>> cTaskList = activitiCommService.userTaskUserId(user.getUserId(), "CmZy", "fucheck");
        int cCount = getCheckCmZy(cTaskList, "").size();
        List<Map<String, Object>> bTaskList = activitiCommService.userTaskUserId(user.getUserId(), "BGQQ", "");
        int bCount = getCheckBgqq(bTaskList, "").size();
        Map<String, Integer> countMap = new HashedMap();
        countMap.put("vCount", vCount);
        countMap.put("cCount", cCount);
        countMap.put("bCount", bCount);
        return AjaxResult.success("返回成功。", countMap);
    }

    /**
     * 根据传入参数查询待办任务列表
     *
     * @param map custNo 柜员号
     *            type   任务类型 VmBn-发布管理 CmZy-资源变更单 BGQQ-变更请求单
     * @return 待办任务列表
     */
    @PostMapping("/getToDoTasks")
    @ResponseBody
    public TableDataInfo getToDoTasks(@RequestBody Map<String, String> map) {
        List<?> resultList = new ArrayList<>();
        OgUser user = iOgUserService.selectOgUserByCustNo(map.get("custNo"));
        if (null == user) {
            return getDataTable(resultList);
        }
        String type = map.get("type");
        String flag = map.get("flag");
        String search = map.get("search");
        List<Map<String, Object>> taskList;
        if ("2".equals(flag)) {
            resultList = pubFlowLogService.getAlreadyTasks(user.getUserId(), type, search);
        } else {
            if ("VmBn".equals(type)) {
                taskList = iVmBizInfoService.getVmBnUserTask(user.getUserId(), type);
                resultList = getCheckVmBn(taskList, search);
            }
            if ("CmZy".equals(type)) {
                taskList = activitiCommService.userTaskUserId(user.getUserId(), type, "fucheck");
                resultList = getCheckCmZy(taskList, search);
            }
            if ("BGQQ".equals(type)) {
                taskList = activitiCommService.userTaskUserId(user.getUserId(), type, "分管领导审批变更请求");
                resultList = getCheckBgqq(taskList, search);
            }
        }
        return getDataTable_app(resultList, map.get("pageNum"), map.get("pageSize"));
    }

    /**
     * 根据传入类型和工单id展示工单详情
     *
     * @param map type  类型
     *            bizId 工单id
     * @return 返回该工单的详细信息
     */
    @PostMapping("/getDetails")
    public AjaxResult getDetails(@RequestBody Map<String, String> map) {
        String type = map.get("type");
        String bizId = map.get("bizId");
        if ("VmBn".equals(type)) {
            // 版本发布是否类型下拉框通用参数key
            final String versionCurrentParams = "VERSION_CURRENT_PARAMS";
            VmBizInfo vmBizInfo = iVmBizInfoService.selectVmBizInfoById(bizId);
            if (vmBizInfo == null) {
                return error("根据工单id未查询到对应的工单信息！");
            }
            String ifSafetyAudit = convertParaValueDetail(versionCurrentParams, vmBizInfo.getIfSafetyAudit());
            vmBizInfo.setIfSafetyAudit(ifSafetyAudit);// 是否安全审核
            String ifReportCallCenter = convertParaValueDetail(versionCurrentParams, vmBizInfo.getIfReportCallCenter());
            vmBizInfo.setIfReportCallCenter(ifReportCallCenter);// 是否通报客服中心
            String ifReportCbrc = convertParaValueDetail(versionCurrentParams, vmBizInfo.getIfReportCbrc());
            vmBizInfo.setIfReportCbrc(ifReportCbrc);// 是否报备银保监会
            String ifReportOffice = convertParaValueDetail(versionCurrentParams, vmBizInfo.getIfReportOffice());
            vmBizInfo.setIfReportOffice(ifReportOffice);// 是否通报办公室
            String ifIssueNotice = convertParaValueDetail(versionCurrentParams, vmBizInfo.getIfIssueNotice());
            vmBizInfo.setIfIssueNotice(ifIssueNotice);// 是否发布公告
            String ifStopBusiness = convertParaValueDetail(versionCurrentParams, vmBizInfo.getIfStopBusiness());
            vmBizInfo.setIfStopBusiness(ifStopBusiness);// 是否需停业升级
            String isAutomate = convertParaValueDetail(versionCurrentParams, vmBizInfo.getIsAutomate());
            vmBizInfo.setIsAutomate(isAutomate);// 是否自动化
            return AjaxResult.success(vmBizInfo);
        }
        if ("CmZy".equals(type)) {
            CmBizSingle cbs = iCmBizSingleService.selectCmBizSingleByIdApp(bizId);
            if (cbs == null) {
                return error("根据工单id未查询到对应的工单信息！");
            }
            return AjaxResult.success(cbs);
        }
        if ("BGQQ".equals(type)) {
            CmBizQingqiu cmBizQingqiu = iCmBizQingqiuService.selectCmBizQingqiuById(bizId);
            if (cmBizQingqiu == null) {
                return error("根据工单id未查询到对应的工单信息！");
            }
            //变更风险等级
            String issue_serious_lev = convertParaValueDetail("issue_serious_lev", cmBizQingqiu.getImportantLev());
            cmBizQingqiu.setImportantLev(issue_serious_lev);
            //变更所处阶段
            String change_stage = convertParaValueDetail("change_stage", cmBizQingqiu.getChangeStage());
            cmBizQingqiu.setChangeStage(change_stage);
            //变更请求来源
            String change_resource = convertParaValueDetail("change_resource", cmBizQingqiu.getChangeResource());
            cmBizQingqiu.setChangeResource(change_resource);
            //是否通知业务
            String isNotice = convertParaValueDetail("isNotice", cmBizQingqiu.getIsNotice());
            cmBizQingqiu.setIsNotice(isNotice);
            //是否影响业务连续性
            String isStop = convertParaValueDetail("isStop", cmBizQingqiu.getIsStop());
            cmBizQingqiu.setIsStop(isStop);
            return AjaxResult.success(cmBizQingqiu);
        }
        return AjaxResult.error("传入参数有误，无法进行查询。");
    }

    /**
     * 查询紧急审批人
     *
     * @param map custNo 柜员号
     * @return
     */
    @PostMapping("/selectJjspApproval")
    public AjaxResult selectJjspApproval(@RequestBody Map<String, String> map) {
        OgUser user = iOgUserService.selectOgUserByCustNo(map.get("custNo"));
        // 如果根据柜员号未查询到人员信息则直接返回空的集合
        if (null == user) {
            return error();
        }
        OgPerson person = ogPersonService.selectOgPersonById(user.getUserId());
        OgOrg org = deptService.selectDeptById(person.getOrgId());
        String orgId = "";
        while (true) {
            String level = org.getOrgLv();
            // 当前登录人为一级机构，邮政金融运维，返回一级机构id
            if ("2".equals(level) || "1".equals(level)) {
                orgId = org.getOrgId();
                break;
            } else {
                if (StringUtils.isNotEmpty(org.getParentId())) {
                    org = deptService.selectDeptById(org.getParentId());
                }
            }
        }
        OgOrg org2 = deptService.selectDeptById(orgId);

        String levelCode = org2.getLevelCode();
        OgOrg org1 = new OgOrg();
        org1.setLevelCode(levelCode);
        OgPerson p = new OgPerson();
        p.setOrg(org1);
        // 紧急审批人角色id为  9114
        p.setrId("9114");

        List<OgPerson> list = commonService.selectPersonByOrgAndRole(p);
        return AjaxResult.success(list);
    }

    /**
     * 移动运维app审批通用处理入口
     *
     * @param map custNo  柜员号
     *            type    工单类型
     *            bizId   工单id
     *            comment 流程意见
     *            trend   走向 0-通过 1-退回
     *            vmBnApprovalFlag  版本发布审批环节标识   1-技术主管  2-业务主管  3-运维审核  4-发布审核  5-紧急审核
     *            jspApprovalId  紧急审批人id   紧急审批环节传参，为空表示不需要紧急审批，不为空表示需要紧急审批
     * @return 审批处理结果
     */
    @PostMapping("/approveWorkOrder")
    @Transactional
    public AjaxResult approveWorkOrder(@RequestBody Map<String, String> map) {
        OgUser user = iOgUserService.selectOgUserByCustNo(map.get("custNo"));
        if (null == user) {
            return error("根据当前柜员号【" + map.get("custNo") + "】未匹配到账号信息，故不做流程审批处理！");
        }
        String type = map.get("type");
        String bizId = map.get("bizId");
        String comment = map.get("comment");
        String trend = map.get("trend");
        try {
            if ("VmBn".equals(type)) {
                String vmBnApprovalFlag = map.get("vmBnApprovalFlag");
                String jspApprovalId = map.get("jspApprovalId");
                if (StringUtils.isNotEmpty(vmBnApprovalFlag)) {
                    if ("5".equals(vmBnApprovalFlag) && StringUtils.isEmpty(jspApprovalId)) {
                        return error("版本审核【紧急审批】环节，紧急审批人不可为空！");
                    }
                }
                return iVmBizInfoService.vmBnAppApproval(user.getUserId(), bizId, comment, trend, vmBnApprovalFlag, jspApprovalId);
            }
            if ("CmZy".equals(type)) {
                CmBizSingle cbs = new CmBizSingle();
                cbs.setChangeId(bizId);
                cbs.getParams().put("comment", comment);
                if ("0".equals(trend)) {
                    return iCmBizSingleService.checkCmBizPass(cbs, user.getUserId());
                } else if ("1".equals(trend)) {
                    return iCmBizSingleService.checkCmBizNoPass(cbs, user.getUserId());
                }
            }
            if ("BGQQ".equals(type)) {
                //变更请求单审核代码 0为通过 1为退回
                CmBizQingqiu cmBizQingqiu = new CmBizQingqiu();
                cmBizQingqiu.setChangeId(bizId);
                cmBizQingqiu.getParams().put("comment", comment);
                return iCmBizQingqiuService.checkCmBizQingQiu(cmBizQingqiu, user.getUserId(), trend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("传入参数有误，审批工单失败。");
    }

    /**
     * 根据传入的待办任务列表返回发布管理封装后的对象
     *
     * @param list
     * @return
     */
    public List<VmBizInfo> getCheckVmBn(List<Map<String, Object>> list, String search) {
        List<VmBizInfo> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String business_key = (String) map.get("businesskey");
            String taskId = (String) map.get("taskId");
            if (StringUtils.isNotEmpty(business_key)) {
                VmBizInfo vm = iVmBizInfoService.selectVmBizInfoById(business_key);
                if (vm != null) {
                    if (StringUtils.isNotEmpty(search)) {
                        if (vm.getVersionInfoNo().contains(search) || vm.getSysName().contains(search)) {
                            vm.getParams().put("vmBnApprovalFlag", map.get("vmBnApprovalFlag"));
                            vm.getParams().put("taskId", taskId);
                            resultList.add(vm);
                        }
                    } else {
                        vm.getParams().put("vmBnApprovalFlag", map.get("vmBnApprovalFlag"));
                        vm.getParams().put("taskId", taskId);
                        resultList.add(vm);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 根据传入的待办任务列表返回资源变更单封装后的对象
     *
     * @param list
     * @return
     */
    public List<CmBizSingle> getCheckCmZy(List<Map<String, Object>> list, String search) {
        List<CmBizSingle> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String business_key = map.get("businesskey").toString();
            if (StringUtils.isNotEmpty(business_key)) {
                CmBizSingle cbs = iCmBizSingleService.selectCmBizSingleById(business_key);
                if (cbs != null) {
                    if (StringUtils.isNotEmpty(search)) {
                        if (cbs.getChangeCode().contains(search) || cbs.getChangeSingleName().contains(search)) {
                            resultList.add(cbs);
                        }
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 根据传入的待办任务列表返回变更请求单封装后的对象
     *
     * @param list
     * @return
     */
    public List<CmBizQingqiu> getCheckBgqq(List<Map<String, Object>> list, String search) {
        List<CmBizQingqiu> resultList = new ArrayList<>();
        CmBizQingqiu cmqq = new CmBizQingqiu();
        for (Map<String, Object> map : list) {
            String business_key = map.get("businesskey").toString();
            if (StringUtils.isNotEmpty(business_key)) {
                cmqq.setChangeId(business_key);
                cmqq.setStauts("0600");
                cmqq.getParams().put("search", search);
                CmBizQingqiu cu = iCmBizQingqiuService.selectBGQQVO(cmqq);
                if (cu != null) {
                    resultList.add(cu);
                }
            }
        }
        return resultList;
    }

    /**
     * 公用翻译app端字典项
     *
     * @param paraName
     * @param value
     * @return
     */
    public String convertParaValueDetail(String paraName, String value) {
        String valueDetail = iPubParaValueService.selectPubParaValueByParaNameApp(paraName, value);
        return valueDetail;
    }

    /**
     * 获取公告通知列表
     *
     * @param map custNo 柜员号
     * @return 查看公告通知列表
     */
    @PostMapping("/getNoticeList")
    @ResponseBody
    public TableDataInfo getNoticeList(@RequestBody Map<String, String> map) {
        List<AmBizNotice> noticeList = new ArrayList<>();
        OgUser user = iOgUserService.selectOgUserByCustNo(map.get("custNo"));
        if (null == user) {
            return getDataTable(noticeList);
        }
        String amTitle = "";
        if (StringUtils.isNotEmpty(map.get("amTitle"))) {
            amTitle = map.get("amTitle");
        }
        noticeList = amBizNoticeService.getNoticeListForApp(user.getUserId(), amTitle);
        return getDataTable_app(noticeList, map.get("pageNum"), map.get("pageSize"));
    }

    /**
     * 公告通知查看详情
     *
     * @param         map   amBizId 公告iD
     * @return 返回该工单的详细信息
     */
    @PostMapping("/getNoticeDetails")
    public AjaxResult getNoticeDetails(@RequestBody Map<String, String> map) {

        String amBizId = map.get("amBizId");
        if (StringUtils.isNotEmpty(amBizId)) {
            AmBizNotice amBizNotice = amBizNoticeService.selectAmBizNoticeById(amBizId);
            if (amBizNotice != null) {
                amBizNotice.setAddTime(handleTimeYYYY_MM_DD_HH_MM_SS(amBizNotice.getAddTime()));
                amBizNotice.setReleaseDate(handleTimeYYYY_MM_DD_HH_MM_SS(amBizNotice.getReleaseDate()));
                amBizNotice.setCheckerTime(handleTimeYYYY_MM_DD_HH_MM_SS(amBizNotice.getCheckerTime()));
                if ("1".equals(amBizNotice.getIsTop())) {
                    amBizNotice.setTopTime(handleTimeYYYY_MM_DD_HH_MM_SS(amBizNotice.getTopTime()));
                }
                return AjaxResult.success(amBizNotice);
            }
            return AjaxResult.error("未查到该公告通知的详细信息。");
        }
        return AjaxResult.error("传入参数有误，无法进行查询。");
    }

}
