package com.ruoyi.activiti.controller.itsm.version;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.listener.VersionPublicProcessListener;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.*;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 版本发布申请Controller
 *
 * @author ruoyi
 * @date 2020-12-15
 */
@Controller
@RequestMapping("version/public")
public class VmBizInfoController extends BaseController {
    /**
     * 版本发布申请页面对应的页面路径前缀
     */
    private String prefix_public = "version/public";
    /**
     * 业务审核页面对应的页面路径前缀
     */
    private String prefix_business = "version/business";
    /**
     * 技术审核页面对应的页面路径前缀
     */
    private String prefix_technology = "version/technology";
    /**
     * 安全审核页面对应的页面路径前缀
     */
    private String prefix_security = "version/security";
    /**
     * 版本审核页面对应的页面路径前缀
     */
    private String prefix_version = "version/version";
    /**
     * 运维审核页面对应的页面路径前缀
     */
    private String prefix_operation = "version/operation";
    /**
     * 发布审核页面对应的页面路径前缀
     */
    private String prefix_release = "version/release";
    /**
     * 紧急审核页面对应的页面路径前缀
     */
    private String prefix_urgent = "version/urgent";
    /**
     * 版本发布查询对应的页面路径前缀
     */
    private String prefix_search = "version/search";
    /**
     * 公共页面路径前缀
     */
    private String prefix_common = "common";

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService personService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private ISysWorkService sysWorkService;

    @Autowired
    private VersionPublicProcessListener listener;

    /**
     * 版本发布申请页面
     */
    @GetMapping("/publicApply")
    public String publicApplyPage() {
        return prefix_public + "/publicApply";
    }

    /**
     * 版本单查询页面
     */
    @GetMapping("/versionList")
    public String versionListPage() {
        return prefix_search + "/versionList";
    }

    /**
     * 查询版本发布列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(VmBizInfo vmBizInfo) {
        Map<String, Object> params = vmBizInfo.getParams();
        if (params != null) {
            convertTime(vmBizInfo);
        }

        if (!"versionList".equals(vmBizInfo.getRemark())) {
            // 版本发布申请列表查询增加创建人控制
            vmBizInfo.setVersionProducerId(ShiroUtils.getUserId());
            params.put("businessOrgFlag", 1);
        } else {
            authControlVersion(vmBizInfo);
        }
        // 版本状态支持多选
        String versionStatus = vmBizInfo.getVersionStatus();
        if (StringUtils.isNotEmpty(versionStatus)) {
            params.put("versionStatusArray", Convert.toStrArray(versionStatus));
        }
        // 系统支持多选
        if (vmBizInfo.getOgSys() != null) {
            String caption = vmBizInfo.getOgSys().getCaption();
            if (StringUtils.isNotEmpty(caption)) {
                String[] arr = Convert.toStrArray(caption);
                // 如果系统选择的为多个，则使用数组IN查询
                if (arr.length > 1) {
                    params.put("captionArray", arr);
                }
            }
        }
        startPage();
        List<VmBizInfo> list = vmBizInfoService.selectVmBizInfoList(vmBizInfo);
        return getDataTable(list);
    }

    /**
     * 版本单查询以及导出增加是否全国中心控制
     */
    public void authControlVersion(VmBizInfo vmBizInfo) {
        Map<String, Object> params = vmBizInfo.getParams();
        OgPerson person = personService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        OgOrg org = deptService.selectDeptById(person.getOrgId());
        String lvCode = org.getLevelCode();
        String orgName = org.getOrgName();
        // 版本单查询需要控制以下条件，具体条件在VmBizInfoMapper.xml中的selectVmBizInfoList
        if ("/000000000/".equals(lvCode) || lvCode.startsWith("/000000000/010000888/010100888/")
                || lvCode.startsWith("/000000000/010000888/010300888/")
                || lvCode.startsWith("/000000000/010000888/010400888/")
                || "/000000000/010000888/".equals(lvCode)) {
        } else {
            boolean isBusiness = false;
            List<PubParaValue> values = pubParaValueService.selectPubParaValueByParaName("vm_dept");
            for(PubParaValue value : values){
                if(orgName.equals(value.getValueDetail())){
                    isBusiness = true;
                    break;
                }
            }
            // 如果是业务审核则查询
            if (isBusiness) {
                vmBizInfo.setBusinessOrg(person.getpId());
                //params.put("businessOrg", orgName);
            } else {
                params.put("businessOrgFlag", 0);
            }
            params.put("orgId", org.getOrgId());
        }
    }

    /**
     * 新增
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        String bizType = "BB";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);
        mmap.put("orgMap", vmBizInfoService.getOrgMap(new VmBizInfo()));
        return prefix_public + "/add";
    }

    /**
     * 新增保存
     */
    @Log(title = "新增保存", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(VmBizInfo vmBizInfo) {
        handleTime(vmBizInfo);
        Map<String, Object> params = vmBizInfo.getParams();
        // 纯技术改造去除业务审核数据
        if ("1".equals(vmBizInfo.getVersionType())) {
            params.put("cleanBusinessFlag", 1);
        } else {
            params.put("cleanBusinessFlag", 0);
        }
        if (StringUtils.isEmpty(vmBizInfo.getVersionInfoId())) {
            vmBizInfo.setVersionProducerId(ShiroUtils.getUserId());
            vmBizInfoService.insertVmBizInfo(vmBizInfo);
        } else {
            vmBizInfo.setVersionInfoId(vmBizInfo.getVersionInfoId());
            vmBizInfoService.updateVmBizInfo(vmBizInfo);
        }
        return AjaxResult.success("操作成功", vmBizInfo.getVersionInfoId());
    }

    /**
     * 导出版本发布列表
     */
    @Log(title = "版本发布", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(VmBizInfo vmBizInfo) {
        Map<String, Object> params = vmBizInfo.getParams();
        if (params != null) {
            convertTime(vmBizInfo);
        }
        authControlVersion(vmBizInfo);
        String currentPage = (String) params.get("currentPage");
        if ("currentPage".equals(currentPage)) {
            startPage();
        }
        List<VmBizInfo> list = vmBizInfoService.selectVmBizInfoList(vmBizInfo);
        ExcelUtil<VmBizInfo> util = new ExcelUtil<VmBizInfo>(VmBizInfo.class);
        return util.exportExcel(list, "版本发布");
    }

    /**
     * 拼接版本创建时间查询格式
     * @param vmBizInfo
     */
    public void convertTime(VmBizInfo vmBizInfo) {
        // versionCreateTime创建时间字段端参数格式化
        Map<String, Object> params = vmBizInfo.getParams();
        String versionCreateTimeStart = (String) params.get("versionCreateTimeStart");
        String versionCreateTimeEnd = (String) params.get("versionCreateTimeEnd");
        if (StringUtils.isNotEmpty(versionCreateTimeStart)) {
            params.put("versionCreateTimeStart", handleTimeYYYYMMDD(versionCreateTimeStart) + "000000");
        }
        if (StringUtils.isNotEmpty(versionCreateTimeEnd)) {
            params.put("versionCreateTimeEnd", handleTimeYYYYMMDD(versionCreateTimeEnd) + "240000");
        }

        if (StringUtils.isNotEmpty(vmBizInfo.getStartUpgradeTime())) {
            vmBizInfo.setStartUpgradeTime(handleTimeYYYYMMDD(vmBizInfo.getStartUpgradeTime()) + "000000");
        }
        if (StringUtils.isNotEmpty(vmBizInfo.getEndUpgradeTime())) {
            vmBizInfo.setEndUpgradeTime(handleTimeYYYYMMDD(vmBizInfo.getEndUpgradeTime()) + "240000");
        }

        // 版本状态支持多选
        String versionStatus = vmBizInfo.getVersionStatus();
        if (StringUtils.isNotEmpty(versionStatus)) {
            params.put("versionStatusArray", Convert.toStrArray(versionStatus));
        }
        // 系统支持多选
        if (vmBizInfo.getOgSys() != null) {
            String caption = vmBizInfo.getOgSys().getCaption();
            if (StringUtils.isNotEmpty(caption)) {
                String[] arr = Convert.toStrArray(caption);
                // 如果系统选择的为多个，则使用数组IN查询
                if (arr.length > 1) {
                    params.put("captionArray", arr);
                }
            }
        }
    }

    /**
     * 处理时间统一为不带-
     *
     * @param vmBizInfo
     */
    public void handleTime(VmBizInfo vmBizInfo) {
        if (StringUtils.isNotEmpty(vmBizInfo.getStartUpgradeTime())) {
            vmBizInfo.setStartUpgradeTime(handleTimeYYYYMMDDHHMMSS(vmBizInfo.getStartUpgradeTime()));
        }
        if (StringUtils.isNotEmpty(vmBizInfo.getEndUpgradeTime())) {
            vmBizInfo.setEndUpgradeTime(handleTimeYYYYMMDDHHMMSS(vmBizInfo.getEndUpgradeTime()));
        }
    }

    /**
     * 版本发布提交
     */
    @PostMapping("/submit")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult submit(VmBizInfo vmBizInfo) {
        // 发送自动化校验，如果自动化校验不成功则直接返回错误
        String mobilPhone = ShiroUtils.getOgUser().getMobilPhone();
        vmBizInfo.getParams().put("mobilPhone", mobilPhone);
        Map checkMap = vmBizInfoService.checkVersionAttachmentExcel(vmBizInfo);
        if (!(boolean) (checkMap.get("flag"))) {
            return error((String) checkMap.get("message"));
        }

        String msg = "";
        // 当前登录人从controller层向service层传递
        vmBizInfo.getParams().put("currentUserId", ShiroUtils.getOgUser().getUserId());
        try {
            vmBizInfoService.startProcess(vmBizInfo);
        } catch (Exception e) {
            e.printStackTrace();
            msg += "版本单号为:" + vmBizInfo.getVersionInfoNo() + "的流程提交失败。";
            throw new BusinessException(msg);
        }
        return success();
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        if (StringUtils.isNull(vmBizInfo.getOgSys())) {
            vmBizInfo.setOgSys(new OgSys());
        }
        List<Map> list = vmBizInfoService.getBusinessList(vmBizInfo);
        Map orgMap = vmBizInfoService.getOrgMap(vmBizInfo);
        if (StringUtils.isNotEmpty(vmBizInfo.getBusinessOrg())) {
            String businessOrg = vmBizInfo.getBusinessOrg();
            String[] orgs = Convert.toStrArray(businessOrg);
            vmBizInfo.setBusinessOrg(orgs[0]);
        }
        mmap.put("businessList", list);
        mmap.put("vmBizInfo", vmBizInfo);
        mmap.put("orgMap", orgMap);
        return prefix_public + "/edit";
    }

    /**
     * 修改保存
     */
    @Log(title = "修改保存", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(VmBizInfo vmBizInfo) {
        handleTime(vmBizInfo);
        Map<String, Object> params = vmBizInfo.getParams();
        // 纯技术改造去除业务审核数据
        if ("1".equals(vmBizInfo.getVersionType())) {
            params.put("cleanBusinessFlag", 1);
        } else {
            params.put("cleanBusinessFlag", 0);
        }
        return toAjax(vmBizInfoService.updateVmBizInfo(vmBizInfo));
    }

    /**
     * 删除
     */
    @Log(title = "【删除】", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String id) {
        return toAjax(vmBizInfoService.deleteVmBizInfoByIds(id));
    }

    /**
     * 查看详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        //List<Map> list = vmBizInfoService.getBusinessList(vmBizInfo);
        mmap.put("vmBizInfo", vmBizInfo);
        //mmap.put("businessList", list);
        return prefix_public + "/detail";
    }

    /**
     * 业务审核页面
     *
     * @return
     */
    @GetMapping("/businessAuthor")
    public String businessAuthorPage() {
        return prefix_business + "/author";
    }

    /**
     * 业务审核按钮对应的页面
     *
     * @return
     */
    @GetMapping("/business/authorApprove/{id}/{taskId}")
    public String businessAuthorApprove(@PathVariable("id") String id, @PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        vmBizInfo.getParams().put("taskId", taskId);
        mmap.put("vmBizInfo", vmBizInfo);
        return prefix_business + "/authorApprove";
    }

    /**
     * 业务主管审核页面
     *
     * @return
     */
    @GetMapping("/businessLeader")
    public String businessLeaderPage() {
        return prefix_business + "/leader";
    }

    /**
     * 业务主管审核按钮对应的页面
     *
     * @return
     */
    @GetMapping("/business/leaderApprove/{id}/{taskId}")
    public String businessLeaderApprove(@PathVariable("id") String id, @PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        vmBizInfo.getParams().put("taskId", taskId);
        mmap.put("vmBizInfo", vmBizInfo);
        return prefix_business + "/leaderApprove";
    }

    /**
     * 技术审核页面
     *
     * @return
     */
    @GetMapping("/technologyAuthor")
    public String technologyAuthorPage() {
        return prefix_technology + "/author";
    }

    /**
     * 技术审核按钮对应的页面
     *
     * @return
     */
    @GetMapping("/technology/authorApprove/{id}/{taskId}")
    public String technologyAuthorApprove(@PathVariable("id") String id, @PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        vmBizInfo.getParams().put("taskId", taskId);
        mmap.put("vmBizInfo", vmBizInfo);
        return prefix_technology + "/authorApprove";
    }

    /**
     * 技术主管审核页面
     *
     * @return
     */
    @GetMapping("/technologyLeader")
    public String technologyLeaderPage() {
        return prefix_technology + "/leader";
    }

    /**
     * 技术主管审核按钮对应的页面
     *
     * @return
     */
    @GetMapping("/technology/leaderApprove/{id}/{taskId}")
    public String technologyLeaderApprove(@PathVariable("id") String id, @PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        vmBizInfo.getParams().put("taskId", taskId);
        mmap.put("vmBizInfo", vmBizInfo);
        return prefix_technology + "/leaderApprove";
    }

    /**
     * 安全审核页面
     *
     * @return
     */
    @GetMapping("/security")
    public String securityPage() {
        return prefix_security + "/author";
    }

    /**
     * 安全审核按钮对应的页面
     *
     * @return
     */
    @GetMapping("/security/authorApprove/{id}/{taskId}")
    public String securityAuthorApprove(@PathVariable("id") String id, @PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        vmBizInfo.getParams().put("taskId", taskId);
        mmap.put("vmBizInfo", vmBizInfo);
        return prefix_security + "/authorApprove";
    }

    /**
     * 版本审核页面
     *
     * @return
     */
    @GetMapping("/version")
    public String versionPage() {
        return prefix_version + "/version";
    }

    /**
     * 版本审核按钮对应的页面
     *
     * @return
     */
    @GetMapping("/version/versionApproval/{id}/{taskId}")
    public String versionApproval(@PathVariable("id") String id, @PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        vmBizInfo.getParams().put("taskId", taskId);
        mmap.put("vmBizInfo", vmBizInfo);
        return prefix_version + "/versionApproval";
    }

    /**
     * 业务主管审核批量标识
     */
    private String businessApprovalFlag = "businessApprovalList";
    /**
     * 技术主管审核批量标识
     */
    private String technologyApprovalFlag = "technologyApprovalList";
    /**
     * 版本审核批量标识
     */
    private String versionApprovalFlag = "versionApprovalList";
    /**
     * 运维审核批量标识
     */
    private String operationApprovalFlag = "operationApprovalList";
    /**
     * 版本审核批量标识
     */
    private String releaseApprovalFlag = "releaseApprovalList";

    /**
     * 版本审核|运维审核｜发布审核批量审批
     *
     * @param versionInfoId
     * @param mmap
     * @return
     */
    @GetMapping("/versionApprovalList")
    public String versionApprovalList(String versionInfoId, String approvalListFlag, ModelMap mmap) {
        mmap.put("versionInfoId", versionInfoId);
        if (businessApprovalFlag.equals(approvalListFlag)) {
            return prefix_business + "/" + businessApprovalFlag;
        }
        if (technologyApprovalFlag.equals(approvalListFlag)) {
            return prefix_technology + "/" + technologyApprovalFlag;
        }
        if (versionApprovalFlag.equals(approvalListFlag)) {
            return prefix_version + "/" + versionApprovalFlag;
        }
        if (operationApprovalFlag.equals(approvalListFlag)) {
            return prefix_operation + "/" + operationApprovalFlag;
        }
        if (releaseApprovalFlag.equals(approvalListFlag)) {
            return prefix_release + "/" + releaseApprovalFlag;
        }
        return null;
    }

    /**
     * 运维审核页面
     *
     * @return
     */
    @GetMapping("/operation")
    public String operationPage() {
        return prefix_operation + "/operation";
    }

    /**
     * 运维审核按钮对应的页面
     *
     * @return
     */
    @GetMapping("/operation/operationApproval/{id}/{taskId}")
    public String operationApproval(@PathVariable("id") String id, @PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        vmBizInfo.getParams().put("taskId", taskId);
        mmap.put("vmBizInfo", vmBizInfo);
        return prefix_operation + "/operationApproval";
    }

    /**
     * 发布审核页面
     *
     * @return
     */
    @GetMapping("/release")
    public String releasePage() {
        return prefix_release + "/release";
    }

    /**
     * 发布审核按钮对应的页面
     *
     * @return
     */
    @GetMapping("/release/releaseApproval/{id}/{taskId}")
    public String releaseApproval(@PathVariable("id") String id, @PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        vmBizInfo.getParams().put("taskId", taskId);
        mmap.put("vmBizInfo", vmBizInfo);
        return prefix_release + "/releaseApproval";
    }

    /**
     * 紧急审核页面
     *
     * @return
     */
    @GetMapping("/urgent")
    public String urgentPage() {
        return prefix_urgent + "/urgent";
    }

    /**
     * 紧急审核按钮对应的页面
     *
     * @return
     */
    @GetMapping("/urgent/urgentApproval/{id}/{taskId}")
    public String urgentApproval(@PathVariable("id") String id, @PathVariable("taskId") String taskId, ModelMap mmap) {
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        vmBizInfo.getParams().put("taskId", taskId);
        mmap.put("vmBizInfo", vmBizInfo);
        return prefix_urgent + "/urgentApproval";
    }

    /**
     * 版本发布各节点待办列表查询
     *
     * @return
     */
    @PostMapping("/versionAgencyList")
    @ResponseBody
    public TableDataInfo versionAgencyList(VmBizInfo vmBizInfo) {
        //startPage();
        String description = (String) vmBizInfo.getParams().get("description");
        List<Map<String, Object>> reList = activitiCommService.userTask("VmBn", description);
        List<OgGroup> userGroupList = sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String, Object>> regList = new ArrayList<>();
        if (userGroupList.size() > 0) {
            regList = activitiCommService.groupTasks("VmBn", "");
            reList.addAll(regList);
        }
        List<VmBizInfo> resultList = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            String business_key = (String) map.get("businesskey");
            String taskId = (String) map.get("taskId");
            if (StringUtils.isNotEmpty(business_key)) {
                vmBizInfo.setVersionInfoId(business_key);
                if (StringUtils.isNotEmpty(vmBizInfo.getVersionCreateTime())) {
                    vmBizInfo.setVersionCreateTime(handleTimeYYYYMMDD(vmBizInfo.getVersionCreateTime()) + "000000");
                }
                VmBizInfo vm = vmBizInfoService.selectVmBizInfoByCondition(vmBizInfo);
                if (vm != null) {
                    vm.getParams().put("taskId", taskId);
                    resultList.add(vm);
                }
            }
        }
        // 待办列表根据生成时间倒序排序
        if (resultList.size() > 1) {
            resultList = resultList.stream().sorted(Comparator.comparing(VmBizInfo::getVersionCreateTime).reversed()).collect(Collectors.toList());
        }
        return getDataTable_ideal(resultList);
    }

    /**
     * 安全审核是否通过
     *
     * @param vmBizInfo
     * @return
     */
    @PostMapping("/securityApprovalPass")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult securityApprovalPass(VmBizInfo vmBizInfo) {
        OgUser ogUser = ShiroUtils.getOgUser();
        String comment = (String) vmBizInfo.getParams().get("comment");
        VmBizInfo vm = vmBizInfoService.selectVmBizInfoByNo(vmBizInfo.getVersionInfoNo());
        Map<String, Object> map = new HashMap<>();
        String status = "";
        String businessStatus = "";
        String technologyStatus = "";
        String msg = "";
        String versionType = vm.getVersionType();
        if (VersionStatusConstants.PASS_FLAG_1.equals(vmBizInfo.getPassFlag())) {
            if ("1".equals(versionType)) {
                // 此时流程走向为技术审核
                map.put("reCode", "0");
                status = VersionStatusConstants.VERSION_STATUS_6;
            } else {
                // 此时流程走向为业务审核｜技术审核
                map.put("reCode", "2");
                status = VersionStatusConstants.VERSION_STATUS_3;
                // 业务审核字段为待业务审核
                businessStatus = VersionStatusConstants.BUSINESS_STATUS_1;
                // 技术审核字段为待技术审核
                technologyStatus = VersionStatusConstants.TECHNOLOGY_STATUS_1;
            }
        } else {
            // 作废
            map.put("reCode", "1");
            status = VersionStatusConstants.VERSION_STATUS_14;
        }
        handleTime(vmBizInfo);
        vmBizInfo.setVersionStatus(status);
        vmBizInfo.setBusinessState(businessStatus);
        vmBizInfo.setTechnologyState(technologyStatus);

        String businessKey = vm.getVersionInfoId();
        map.put("businessKey", businessKey);
        map.put("taskId", vmBizInfo.getParams().get("taskId"));
        map.put("comment", comment);
        map.put("userId", ogUser.getUserId());
        String processDefinitionKey = "VmBn";
        map.put("processDefinitionKey", processDefinitionKey);

        try {
            activitiCommService.complete(map);
            vmBizInfoService.updateVmBizInfo(vmBizInfo);
        } catch (Exception e) {
            e.printStackTrace();
            msg += "版本单号为:" + vmBizInfo.getVersionInfoNo() + "的流程提交失败。";
            throw new BusinessException(msg);
        }
        return success();
    }

    /**
     * 业务审核|业务主管审核是否通过
     *
     * @param vmBizInfo
     * @return
     */
    @PostMapping("/businessApprovalPass")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult businessApprovalPass(VmBizInfo vmBizInfo) {
        String versionInfoId = vmBizInfo.getVersionInfoId();
        String[] versionInfoIds = Convert.toStrArray(versionInfoId);
        String msg = "";
        for (String id : versionInfoIds) {
            String comment = (String) vmBizInfo.getParams().get("comment");
            Map<String, Object> map = new HashMap<>();
            String status = "";
            if (VersionStatusConstants.PASS_FLAG_1.equals(vmBizInfo.getPassFlag())) {
                // 此时流程走向为通过到下一节点业务主管审核
                map.put("reCode", "0");
            } else {
                // 此时流程走向为作废
                map.put("reCode", "1");
                status = VersionStatusConstants.VERSION_STATUS_14;
            }

            String businessKey = id;
            map.put("businessKey", businessKey);
            map.put("comment", comment);
            OgUser ogUser = ShiroUtils.getOgUser();
            map.put("userId", ogUser.getUserId());
            String processDefinitionKey = "VmBn";
            map.put("processDefinitionKey", processDefinitionKey);
            String businessFlag = (String) vmBizInfo.getParams().get("businessFlag");
            // 是否业务审批标识
            boolean isBusinessPass = false;
            // 是否取消标识
            boolean isCancel = false;
            // 业务主管审核
            if ("1".equals(businessFlag)) {
                String businessBatchFlag = (String) vmBizInfo.getParams().get("businessBatchFlag");
                // 判断业务主管是否批量审批
                if ("1".equals(businessBatchFlag)) {
                    Task task = activitiCommService.getTask(businessKey, "业务主管审核");
                    map.put("taskId", task.getId());
                } else {
                    map.put("taskId", vmBizInfo.getParams().get("taskId"));
                }
            } else {
                // 是业务审核并且通过的情况下向项目经理发送短信，否决的情况也需要在这里发
                if(VersionStatusConstants.VERSION_STATUS_14.equals(status)){
                    // 否决
                    isCancel = true;
                } else {
                    // 通过
                    isBusinessPass = true;
                }
                // 如果是业务审核传递taskId
                map.put("taskId", vmBizInfo.getParams().get("taskId"));
            }
            handleTime(vmBizInfo);
            vmBizInfo.setVersionStatus(status);
            vmBizInfo.setVersionInfoId(id);

            try {
                activitiCommService.usersComplete(map);
                vmBizInfoService.updateVmBizInfo(vmBizInfo);
                // 业务审核通过｜不通过的情况都需要向项目经理发送短信
                if(!"1".equals(businessFlag)){
                    VmBizInfo vm = vmBizInfoService.selectVmBizInfoById(id);
                    OgPerson producePerson = personService.selectOgPersonById(vm.getVersionProducerId());
                    // 通过的情况
                    if(isBusinessPass){
                        // 每个业务审核向项目经理发送短信
                        OgPerson person = personService.selectOgPersonById(ogUser.getpId());
                        String message = "版本单号：" + vmBizInfo.getVersionInfoNo() + ",标题：" + vmBizInfo.getVersionInfoName() + "的版本业务审核：" + person.getpName() + ",已审批通过。";
                        vmBizInfoService.sendSms(message, producePerson);
                    }
                    // 否决的情况
                    if(isCancel){
                        // 应用负责人
                        OgPerson useApproval = personService.selectOgPersonById(vm.getUseApprovalId());
                        listener.sendCancelMsg(producePerson, useApproval, vm);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg += "版本单号为:" + vmBizInfo.getVersionInfoNo() + "的流程提交失败。";
                throw new BusinessException(msg);
            }
        }
        return success();
    }

    /**
     * 技术审核｜技术主管审核是否通过
     *
     * @param vmBizInfo
     * @return
     */
    @PostMapping("/technologyApprovalPass")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult technologyApprovalPass(VmBizInfo vmBizInfo) {
        String versionInfoId = vmBizInfo.getVersionInfoId();
        String[] versionInfoIds = Convert.toStrArray(versionInfoId);
        String msg = "";
        for (String id : versionInfoIds) {
            String comment = (String) vmBizInfo.getParams().get("comment");
            Map<String, Object> map = new HashMap<>();
            String status = "";
            String businessState = "";
            String technologyStatus = "";
            if (VersionStatusConstants.PASS_FLAG_1.equals(vmBizInfo.getPassFlag())) {
                // 此时流程走向为通过到下一节点技术主管审核
                map.put("reCode", "0");

            } else {
                // 此时流程走向为作废
                map.put("reCode", "1");
                status = VersionStatusConstants.VERSION_STATUS_14;
            }

            String businessKey = id;
            map.put("businessKey", businessKey);
            map.put("comment", comment);
            map.put("userId", ShiroUtils.getOgUser().getUserId());
            String processDefinitionKey = "VmBn";
            map.put("processDefinitionKey", processDefinitionKey);
            String technologyFlag = (String) vmBizInfo.getParams().get("technologyFlag");
            // 技术主管审核
            if ("1".equals(technologyFlag)) {
                if ("1".equals(vmBizInfo.getVersionType())) {
                    // 如果是纯技术，技术主管审核完成后将状态修改为待版本审核
                    status = VersionStatusConstants.VERSION_STATUS_9;
                    technologyStatus = VersionStatusConstants.TECHNOLOGY_STATUS_PASS;
                }
                // 判断技术主管是否批量审核
                String technologyBatchFlag = (String) vmBizInfo.getParams().get("technologyBatchFlag");
                if ("1".equals(technologyBatchFlag)) {
                    Task task = activitiCommService.getTask(businessKey, "技术主管审核");
                    map.put("taskId", task.getId());
                } else {
                    map.put("taskId", vmBizInfo.getParams().get("taskId"));
                }
            } else {
                // 技术审核增加技术主管
                map.put("technologyApproval", vmBizInfo.getTechnologyApprovalId());
                // 如果是纯技术审核，则默认将业务审核状态置为业务审核已通过,技术审核状态修改为待技术主管审核,版本状态修改为待技术主管审核
                if ("1".equals(vmBizInfo.getVersionType())) {
                    status = VersionStatusConstants.VERSION_STATUS_8;
                    businessState = VersionStatusConstants.BUSINESS_STATUS_PASS;
                    technologyStatus = VersionStatusConstants.TECHNOLOGY_STATUS_2;
                }
                map.put("taskId", vmBizInfo.getParams().get("taskId"));
            }
            handleTime(vmBizInfo);
            vmBizInfo.setVersionStatus(status);
            vmBizInfo.setBusinessState(businessState);
            vmBizInfo.setTechnologyState(technologyStatus);
            vmBizInfo.setVersionInfoId(id);

            try {
                vmBizInfoService.updateVmBizInfo(vmBizInfo);
                activitiCommService.complete(map);
            } catch (Exception e) {
                e.printStackTrace();
                msg += "版本单号为:" + vmBizInfo.getVersionInfoNo() + "的流程提交失败。";
                throw new BusinessException(msg);
            }
        }
        return success();
    }

    /**
     * 版本审核（应用审核人）|运维审核|发布审批|紧急发布审批
     * 各个节点审核根据传递过来的params标识区分
     *
     * @param vmBizInfo
     * @return
     */
    @PostMapping("/useApprovalPass")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult useApprovalPass(VmBizInfo vmBizInfo) {
        String versionInfoId = vmBizInfo.getVersionInfoId();
        String[] versionInfoIds = Convert.toStrArray(versionInfoId);
        String msg = "";
        for (String id : versionInfoIds) {
            String comment = (String) vmBizInfo.getParams().get("comment");
            Map<String, Object> map = new HashMap<>();
            String status = "";
            if (VersionStatusConstants.PASS_FLAG_1.equals(vmBizInfo.getPassFlag())) {
                // 此时流程走向为通过到下一节点
                map.put("reCode", "0");
            } else {
                // 此时流程走向为作废
                map.put("reCode", "1");
                status = VersionStatusConstants.VERSION_STATUS_14;
            }

            String businessKey = id;
            map.put("businessKey", businessKey);
            map.put("comment", comment);
            map.put("userId", ShiroUtils.getOgUser().getUserId());
            String processDefinitionKey = "VmBn";
            map.put("processDefinitionKey", processDefinitionKey);
            String versionFlag = (String) vmBizInfo.getParams().get("versionFlag");
            String operationFlag = (String) vmBizInfo.getParams().get("operationFlag");
            String releaseFlag = (String) vmBizInfo.getParams().get("releaseFlag");
            String jjspApprovalFlag = (String) vmBizInfo.getParams().get("jjspApprovalFlag");
            // 版本审核
            if ("1".equals(versionFlag)) {
                String versionBatchFlag = (String) vmBizInfo.getParams().get("versionBatchFlag");
                if ("1".equals(versionBatchFlag)) {
                    // 批量审核
                    Task task = activitiCommService.getTask(businessKey, "版本审核");
                    map.put("taskId", task.getId());
                } else {
                    // 单个审核
                    map.put("taskId", vmBizInfo.getParams().get("taskId"));
                }
                // 待运维审核
                status = VersionStatusConstants.VERSION_STATUS_10;
                map.put("useDivisionChiefApproval", vmBizInfo.getUseDivisionChiefApprovalId());// 运维审核（应用处长）
                map.put("uploaderApproval", vmBizInfo.getUploaderApprovalId());// 发布审核
            }
            // 运维审核
            if ("1".equals(operationFlag)) {
                String operationBatchFlag = (String) vmBizInfo.getParams().get("operationBatchFlag");
                if ("1".equals(operationBatchFlag)) {
                    // 批量审核
                    Task task = activitiCommService.getTask(businessKey, "运维审核");
                    map.put("taskId", task.getId());
                } else {
                    // 单个审核
                    map.put("taskId", vmBizInfo.getParams().get("taskId"));
                }
                // 待发布审核
                status = VersionStatusConstants.VERSION_STATUS_11;
            }
            // 发布审批
            if ("1".equals(releaseFlag)) {
                String releaseBatchFlag = (String) vmBizInfo.getParams().get("releaseBatchFlag");
                if (StringUtils.isNotEmpty(releaseBatchFlag) && "1".equals(releaseBatchFlag)) {
                    // 批量审核
                    Task task = activitiCommService.getTask(businessKey, "发布审核");
                    map.put("taskId", task.getId());
                } else {
                    // 单个审核
                    map.put("taskId", vmBizInfo.getParams().get("taskId"));
                }

                /**
                 * 版本发布状态三种情况
                 * 1.通过且没有紧急发布审批-状态修改为已发布
                 * 2.不通过-状态修改为已作废
                 * 3.通过且有紧急审批状态修改为待紧急审批
                 * 通过｜作废情况reCode=0业务状态不一样，待紧急审批reCode=1
                 */
                if (VersionStatusConstants.PASS_FLAG_1.equals(vmBizInfo.getPassFlag())) {
                    // 已发布
                    status = VersionStatusConstants.VERSION_STATUS_12;
                } else {
                    // 已作废
                    status = VersionStatusConstants.VERSION_STATUS_14;
                }
                // 如果是紧急审批，需要增加紧急审批人
                if ("1".equals(vmBizInfo.getAcceptRole())) {
                    // 状态为待紧急审批
                    status = VersionStatusConstants.VERSION_STATUS_13;
                    map.put("jjspApproval", vmBizInfo.getJjspApprovalId());// 紧急审批人
                    // reCode为1走到紧急审批
                    map.put("reCode", "1");
                    // 紧急审批时增加版本状态传入工作流处理类做判断
                    map.put("status", status);
                }
            }
            // 紧急发布审批
            if ("1".equals(jjspApprovalFlag)) {
                map.put("taskId", vmBizInfo.getParams().get("taskId"));
                /**
                 * 紧急发布审批状态三种情况
                 * 1.通过-状态修改为已发布
                 * 2.不通过-状态修改为已作废
                 * 通过｜作废情况reCode=0业务状态不一样
                 */
                if (VersionStatusConstants.PASS_FLAG_1.equals(vmBizInfo.getPassFlag())) {
                    // 已发布
                    status = VersionStatusConstants.VERSION_STATUS_12;
                } else {
                    // 已作废
                    status = VersionStatusConstants.VERSION_STATUS_14;
                }
            }
            handleTime(vmBizInfo);
            vmBizInfo.setVersionStatus(status);
            vmBizInfo.setVersionInfoId(id);

            try {
                vmBizInfoService.updateVmBizInfo(vmBizInfo);
                activitiCommService.complete(map);
                // 如果是已发布状态则需要生成版本任务
                if (VersionStatusConstants.VERSION_STATUS_12.equals(status) || VersionStatusConstants.VERSION_STATUS_13.equals(status)) {
                    vmBizInfoService.saveVmBizTaskInfo(vmBizInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg += "版本单号为:" + vmBizInfo.getVersionInfoNo() + "的流程提交失败。";
                throw new BusinessException(msg);
            }
        }
        return success();
    }

    /**
     * 应用系统选择页面
     *
     * @return
     */
    @GetMapping("/selectApplication")
    public String selectApplication() {
        return prefix_common + "/selectApplication";
    }

    /**
     * 外围系统选择页面
     *
     * @return
     */
    @GetMapping("/selectOneApplication")
    public String selectOneApplication() {
        return prefix_common + "/selectOneApplication";
    }

    private String securityAuditRid = "2301";// 安全审核人
    private String businessAuditRid = "2302";// 业务审核人
    private String businessApprovalRid = "2303";// 业务领导
    private String technologyAuditRid = "2304";// 技术审核人
    private String technologyApprovalRid = "2305";// 技术领导
    private String useApprovalRid = "2306";// 应用审核人（版本审核）
    private String uploaderApprovalRid = "2307";// 发布审批人
    private String useDivisionChiefApprovalRid = "9003";// 应用处长（运维审核）
    private String jjspApprovalRid = "9114";// 紧急审批人

    /**是否根据自动化实施人选择应用处长，现阶段经勾通暂时关闭为false*/
    private boolean useDivisionChiefApprovalFlag = false;

    /**
     * 选择版本的各类审批人
     *
     * @return
     */
    @GetMapping("/selectBusinessAudit")
    public String selectBusinessAudit(String orgName, String pflag, String rId, String num, String versionInfoId, ModelMap mmap) {
        try {
            if (StringUtils.isNotEmpty(orgName)) {
                orgName = URLDecoder.decode(orgName, "UTF-8");
                OgOrg org = deptService.selectDeptByOrgName(orgName);
                mmap.put("orgId", org.getOrgId());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mmap.put("rId", rId);
        mmap.put("pflag", pflag);
        mmap.put("num", num);
        if (businessAuditRid.equals(rId)) {
            // 业务审核
            return prefix_common + "/selectBusinessAudit";
        } else if (businessApprovalRid.equals(rId)) {
            // 业务领导
            return prefix_common + "/selectBusinessApproval";
        } else if (securityAuditRid.equals(rId)) {
            // 安全审核
            return prefix_common + "/selectSecurityAudit";
        } else if (technologyAuditRid.equals(rId)) {
            // 技术审核
            return prefix_common + "/selectTechnologyAudit";
        } else if (technologyApprovalRid.equals(rId)) {
            // 技术领导
            return prefix_common + "/selectTechnologyApproval";
        } else if (useApprovalRid.equals(rId)) {
            // 应用审批（版本审核）
            return prefix_common + "/selectUseApproval";
        } else if (uploaderApprovalRid.equals(rId)) {
            // 发布审批
            return prefix_common + "/selectUploaderApproval";
        } else if (useDivisionChiefApprovalRid.equals(rId)) {
            // 应用处长（运维审核）
            if(useDivisionChiefApprovalFlag) {
                VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoByIdNoConvert(versionInfoId);
                if(StringUtils.isNotNull(vmBizInfo) && StringUtils.isNotEmpty(vmBizInfo.getAutomateAuditId())){
                    // 如果是自动化版本需要把自动化实施人的机构传回，应用处长审批人必须与自动化实施人机构一致
                    OgPerson person = personService.selectOgPersonEvoById(vmBizInfo.getAutomateAuditId());
                    mmap.put("remark", person.getOrgId());
                }
            }
            return prefix_common + "/selectUseDivisionChiefApproval";
        } else if (jjspApprovalRid.equals(rId)) {
            // 紧急审批
            return prefix_common + "/selectJjspApproval";
        } else if ("automate".equals(pflag)) {
            // 自动化实施人页面
            return prefix_common + "/selectAutomateAudit";
        }
        return "";
    }

    /**
     * 选择版本类型
     */
    @GetMapping("/selectVersionType")
    public String selectVersionType(String versionType, ModelMap mmap) {
        mmap.put("versionType", versionType);
        return prefix_public + "/selectVersionType";
    }

    /**
     * 查询版本类型集合
     *
     * @param versionType
     * @return
     */
    @PostMapping("/selectVersionTypeList")
    @ResponseBody
    public TableDataInfo selectVersionTypeList(String versionType) {
        List<PubParaValue> values = pubParaValueService.selectPubParaValueByParaName(versionType);
        return getDataTable(values);
    }

    /**
     * 关联附件页面
     *
     * @return
     */
    @GetMapping("/relationAttachment/{ownerId}")
    public String relationAttachment(@PathVariable("ownerId") String ownerId, ModelMap map) {
        map.put("ownerId", ownerId);
        return prefix_common + "/relation";
    }

    /**
     * 撤回
     *
     * @param id
     * @return
     */
    @PostMapping("/rollBack")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult rollBack(String id) {
        String msg = "";
        VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoById(id);
        // 流程创建人撤回流程校验版本状态
        String versionType = vmBizInfo.getVersionType();
        String ifSafetyAudit = vmBizInfo.getIfSafetyAudit();
        String versionStatus = vmBizInfo.getVersionStatus();
        if ("1".equals(versionType) && !"1".equals(ifSafetyAudit)) {
            // 如果是纯技术审核并且没有安全审核判断版本单状态待技术审核
            if (!VersionStatusConstants.VERSION_STATUS_6.equals(versionStatus)) {
                msg = "该版本下一节点为技术审核，版本状态不符不能撤回.";
                return error(msg);
            }
        } else if ("1".equals(ifSafetyAudit)) {
            if (!VersionStatusConstants.VERSION_STATUS_2.equals(versionStatus)) {
                msg = "该版本下一节点为安全审核，版本状态不符不能撤回.";
                return error(msg);
            }
        } else {
            if (!VersionStatusConstants.VERSION_STATUS_3.equals(versionStatus)) {
                msg = "该版本下一节点为业务/技术审核，版本状态不符不能撤回.";
                return error(msg);
            }
        }

        // 校验当前登录人是否流程创建人
        String userId = ShiroUtils.getUserId();
        if (!userId.equals(vmBizInfo.getVersionProducerId())) {
            msg = "该版本单不属于自己创建不能撤回.";
            return error(msg);
        }

        int a = 0;
        try {
            activitiCommService.revokeTask(id, userId, "VmBn");
            a++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (a > 0) {
            VmBizInfo info = new VmBizInfo();
            // 将版本状态变为待提交
            info.setVersionStatus(VersionStatusConstants.VERSION_STATUS_1);
            info.setVersionInfoId(id);
            int rows = vmBizInfoService.updateVmBizInfo(info);
            /*if (rows > 0) {
                // 调用自动化撤回接口
                vmBizInfo.getParams().put("mobilePhone", ShiroUtils.getOgUser().getMobilPhone());
                vmBizInfoService.forbiddenVersion(vmBizInfo);
            }*/
        }
        return toAjax(a);
    }
}
