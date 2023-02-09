package com.ruoyi.activiti.controller.itsm.sjwt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.service.IImBizDataqService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 【数据问题变更单】Controller
 *
 * @author liul
 * @date 2021-09-28
 */
@Controller
@RequestMapping("/system/dataq")
public class ImBizDataqController extends BaseController {
    private String prefix = "sjwt/mysjwt";

    private String fenfa_prefix = "sjwt/fenfa";

    private String shouli_prefix = "sjwt/slsjwt";

    private String pinggu_prefix = "sjwt/pgsjwt";

    private String close_prefix = "sjwt/gbsjwt";

    private String cx_prefix = "sjwt/cxsjwt";

    private String bg_prefix = "sjwt/problem";

    private String task_prefix = "sjwt/dealTask";

    /**
     * 版本发布查询对应的页面路径前缀
     */
    private String prefix_search = "version/search";
    /**
     * 公共页面路径前缀
     */
    private String prefix_common = "common";

    @Autowired
    private IImBizDataqService imBizDataqService;

    @Autowired
    private IOgPersonService personService;

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgUserService ogUserService;

    /**
     * 我的数据变更单按钮对应url
     *
     * @return
     */
    @GetMapping()
    public String myDataq() {
        return prefix + "/mysjwt";
    }

    /**
     * 我的数据问题单列表页面
     *
     * @param imBizDataq 根据传入对象中的参数进行查询
     * @return 返回符合查询条件的结果集
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ImBizDataq imBizDataq) {
        startPage();
        List<ImBizDataq> list = imBizDataqService.selectImBizDataqList(imBizDataq);
        return getDataTable(list);
    }

    /**
     * 查询数据问题单列表页面
     *
     * @param imBizDataq 根据传入对象中的参数进行查询
     * @return 返回符合查询条件的结果集
     */
    @PostMapping("/pageList")
    @ResponseBody
    public TableDataInfo pageList(ImBizDataq imBizDataq) {
        startPage();
        List<ImBizDataq> list = imBizDataqService.pageList(imBizDataq);
        return getDataTable(list);
    }

    /**
     * 数据变更单关联数据问题单页面，展示不为待提交的
     *
     * @param imBizDataq 根据传入对象中的参数进行查询
     * @return 返回符合查询条件的结果集
     */
    @PostMapping("/pageListBg")
    @ResponseBody
    public TableDataInfo pageListBg(ImBizDataq imBizDataq) {
        startPage();
        List<ImBizDataq> list = imBizDataqService.pageListBg(imBizDataq);
        return getDataTable(list);
    }

    /**
     * 导出问题单列表
     */
    @Log(title = "数据问题单导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ImBizDataq imBizDataq) {

        List<ImBizDataq> list = imBizDataqService.exprot(imBizDataq);
        ExcelUtil<ImBizDataq> util = new ExcelUtil<ImBizDataq>(ImBizDataq.class);
        return util.exportExcel(list, "数据问题单");
    }

    /**
     * 点击新建按钮对应URL
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 提交问题单
     */
    @Log(title = "【新增保存】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public AjaxResult addSave(ImBizDataq imBizDataq) {
        return imBizDataqService.addSave(imBizDataq);
    }

    /**
     * 审批页面数据问题单保存
     */
    @Log(title = "【审批页面数据问题单保存】", businessType = BusinessType.INSERT)
    @PostMapping("/addSjwtSh")
    @ResponseBody
    @Transactional
    public AjaxResult addSjwtSh(ImBizDataq imBizDataq) {
        return imBizDataqService.addSjwtSh(imBizDataq);
    }



    /**
     * 修改保存
     */
    @Log(title = "【修改保存】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ImBizDataq imBizDataq) {
        return toAjax(imBizDataqService.updateImBizDataq(imBizDataq));
    }

    /**
     * 删除数据问题单
     */
    @Log(title = "【删除】", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(imBizDataqService.deleteImBizDataqByIds(ids));
    }

    /**
     * 系统选择
     *
     * @return
     */
    @GetMapping("/application")
    public String applicationSysList() {
        return prefix + "/applicationSysList";
    }

    /**
     * 查看详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        imBizDataqService.view(id, mmap);
        return prefix + "/detail";
    }

    /**
     * 删除附件
     *
     * @return
     */
    @GetMapping("/selectOgUserByUserId")
    @ResponseBody
    public AjaxResult selectOgUserByUserId() {
        AjaxResult ajaxResult = new AjaxResult();
        String userId = ShiroUtils.getUserId();
        ajaxResult.put("data", ogUserService.selectOgUserByUserId(userId));
        return ajaxResult;
    }


    /**
     * 分发数据问题单页面
     *
     * @param
     * @return
     */
    @GetMapping("/fenfa")
    public String fenfasjwt() {

        return fenfa_prefix + "/fenfasjwt";
    }


    /**
     * 受理数据问题单页面
     *
     * @param
     * @return
     */
    @GetMapping("/shouli")
    public String shoulisjwt() {

        return shouli_prefix + "/slsjwt";
    }


    /**
     * 评估数据问题单页面
     *
     * @param
     * @return
     */
    @GetMapping("/pinggu")
    public String pinggusjwt() {

        return pinggu_prefix + "/pgsjwt";
    }

    /**
     * 查询待办任务列表
     */
    @PostMapping("/getBenchTaskList")
    @ResponseBody
    public TableDataInfo getBenchTaskList(ImBizDataq imBizDataq) {

        return getDataTable_ideal(imBizDataqService.getBenchTaskList(imBizDataq));
    }


    /**
     * 关闭数据问题单页面
     *
     * @param
     * @return
     */
    @GetMapping("/close")
    public String closesjwt() {

        return close_prefix + "/closesjwt";
    }


    /**
     * 查询数据问题单页面
     *
     * @param
     * @return
     */
    @GetMapping("/cxsjwt")
    public String cxsjwt() {

        return cx_prefix + "/cxsjwt";
    }


    /**
     * 新增数据问题单页面
     *
     * @return
     */
    @GetMapping("/xzsjwt")
    public String xzsjwt(ModelMap modelMap) {

        imBizDataqService.addview(modelMap);

        return bg_prefix + "/xzsjwt";
    }


    /**
     * 关联数据问题单页面
     *
     * @return
     */
    @GetMapping("/glsjwt")
    public String glsjwt() {
        return bg_prefix + "/glsjwt";
    }


    /**
     * 数据问题单添加保存
     */
    @Log(title = "数据问题单", businessType = BusinessType.INSERT)
    @PostMapping("/addsjwt")
    @ResponseBody
    public AjaxResult sjwtsave(ImBizDataq imBizDataq) {
        return toAjax(imBizDataqService.insertImBizDataq(imBizDataq));
    }


    /**
     * 进入修改页面
     *
     * @param imNo
     * @param modelMap
     * @return
     */
    @GetMapping("/xgsjwt/{imNo}")
    public String xgsjwt(@PathVariable("imNo") String imNo, ModelMap modelMap) {
        imBizDataqService.xgsjwt(imNo, modelMap);

        return bg_prefix + "/xgsjwt";
    }


    /**
     * 进入修改页面
     *
     * @param imNo
     * @param modelMap
     * @return
     */
    @GetMapping("/xgsjwtsh/{imNo}")
    public String xgsjwtsh(@PathVariable("imNo") String imNo, ModelMap modelMap) {

        imBizDataqService.xgsjwtsh(imNo, modelMap);

        return bg_prefix + "/xgsjwtsh";
    }

    /**
     * 修改保存数据问题单
     */
    @Log(title = "修改保存", businessType = BusinessType.UPDATE)
    @PostMapping("/editsjwt")
    @ResponseBody
    public AjaxResult editsjwt(ImBizDataq imBizDataq) {
        imBizDataq.setCreaterId(ShiroUtils.getUserId());
        return toAjax(imBizDataqService.updateImBizDataq(imBizDataq));
    }

    /**
     * 根据数据问题单ID查询数据变更单
     */
    @PostMapping("/sjWtList")
    @ResponseBody
    public TableDataInfo getsjWtList(ImBizDataq imBizDataq) {

        List<ImBizDataq> list = imBizDataqService.selectImBizDataqList(imBizDataq);
        return getDataTable(list);
    }

    private String businessAuditRid = "2302";// 业务经办人

    private String businessApprovalRid = "2303";// 业务领导

    private String technologyAuditRid = "2304";// 技术审核人

    private String businessPingGuRid = "6664";// 评估人

    /**
     * 选择数据问题单的各类审批人
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
        } else if (technologyAuditRid.equals(rId)) {
            // 技术审核
            return prefix_common + "/selectTechnologyAudit";
        } else if (businessPingGuRid.equals(rId)) {
            // 技术审核
            return prefix_common + "/selectTechnologyPingGu";
        }
        return "";
    }


    /**
     * 总行业务审批新增数据问题单页面
     *
     * @return
     */
    @GetMapping("/xzsjwtsh")
    public String xzsjwtsh(ModelMap modelMap) {

        imBizDataqService.addview(modelMap);

        return bg_prefix + "/xzsjwtsh";
    }

    /**
     * 修改按钮对应页面URL
     */
    @GetMapping("/edit/{imId}")
    public String edit(@PathVariable("imId") String imId, ModelMap mmap) {
        imBizDataqService.editView(imId, mmap);
        return prefix + "/edit";
    }

    /**
     * 评估按钮对应任务页面
     *
     * @param imId,map
     * @return
     */
    @GetMapping("/openPgTask/{imId}/{taskId}")
    public String openPgTask(@PathVariable("imId") String imId, @PathVariable("taskId") String taskId, ModelMap map) {
        map = imBizDataqService.view(imId, map);
        map.put("taskId", taskId);
        return task_prefix + "/sjwtTask";
    }

    /**
     * 分发、关闭按钮对应任务页面
     *
     * @param imId,map
     * @return
     */
    @GetMapping("/opencloseOrFenfaTask/{imId}/{taskId}")
    public String opencloseOrFenfaTask(@PathVariable("imId") String imId, @PathVariable("taskId") String taskId, ModelMap map) {
        map = imBizDataqService.view(imId, map);
        map.put("taskId", taskId);
        return task_prefix + "/closeOrFenfaTask";
    }


    /**
     * 批量分发页面
     *
     * @param imId,map
     * @return
     */
    @GetMapping("/openPlFenfaTask/{imId}/{taskId}")
    public String openPlFenfaTask(@PathVariable("imId") String imId, @PathVariable("taskId") String taskId, ModelMap map) {
        map.put("imId", imId);
        map.put("taskId", taskId);

        return fenfa_prefix + "/plFenfaTask";
    }

    /**
     * 执行流程任务
     *
     * @param imBizDataq
     * @return
     */
    @PostMapping("/saveflowAssessor")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowAssessor(ImBizDataq imBizDataq) {

        return imBizDataqService.saveflowAssessor(imBizDataq);
    }

    /**
     * 执行分发/关闭问题单
     *
     * @param imBizDataq
     * @return
     */
    @PostMapping("/saveflowCloseOrFenFa")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowCloseOrFenFa(ImBizDataq imBizDataq) {

        return imBizDataqService.saveflowCloseOrFenFa(imBizDataq);
    }

    /**
     * 修改前查询id
     *
     * @param id
     * @return
     */
    @PostMapping("/selectById")
    @ResponseBody
    public AjaxResult selectById(String id) {
        AjaxResult ajaxResult = new AjaxResult();
        ImBizDataq imBizDataq = imBizDataqService.selectImBizDataqById(id);
        if (imBizDataq != null) {
            ajaxResult.put("data", imBizDataq);
        }
        return ajaxResult;
    }


    /**
     * 受理按钮对应任务页面
     *
     * @param imId,map
     * @return
     */
    @GetMapping("/openAcceptTask/{imId}/{taskId}")
    public String openAcceptTask(@PathVariable("imId") String imId, @PathVariable("taskId") String taskId, ModelMap map) {
        map = imBizDataqService.view(imId, map);
        map.put("taskId", taskId);
        return shouli_prefix + "/AcceptTask";
    }


    /**
     * 关联版本单号页面
     */
    @GetMapping("/glbb")
    public String glbb() {
        return shouli_prefix + "/wtgl_versionList";
    }


    /**
     * 查询版本发布列表
     */
    @PostMapping("/wtgllist")
    @ResponseBody
    public TableDataInfo list(VmBizInfo vmBizInfo) {
        Map<String, Object> params = vmBizInfo.getParams();
        if (params != null) {
            convertTime(vmBizInfo);
        }
        vmBizInfo.setVersionStatus("12");

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
     * 受理页面打开转发页面
     *
     * @return
     */
    @GetMapping("/transmit/{imId}/{taskId}")
    public String transmit(@PathVariable("imId") String imId, @PathVariable("taskId") String taskId, ModelMap map) {
        ImBizDataq imBizDataq = imBizDataqService.selectImBizDataqById(imId);
        map.put("imId", imId);
        map.put("taskId", taskId);
        map.put("imBizDataq", imBizDataq);
        return shouli_prefix + "/transmit";
    }

    /**
     * 受理页面打开退回页面
     *
     * @return
     */
    @GetMapping("/sendIssue/{imId}/{taskId}")
    public String sendIssue(@PathVariable("imId") String imId, @PathVariable("taskId") String taskId, ModelMap map) {
        ImBizDataq imBizDataq = imBizDataqService.selectImBizDataqById(imId);
        map.put("imBizDataq", imBizDataq);
        map.put("taskId", taskId);
        return shouli_prefix + "/sendIssue";
    }


    /**
     * 受理问题单
     *
     * @param imBizDataq
     * @return
     */
    @PostMapping("/saveAccept")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveAccept(ImBizDataq imBizDataq) {

        return imBizDataqService.saveAccept(imBizDataq);
    }

    /**
     * 解决问题单
     *
     * @param imBizDataq
     * @return
     */
    @PostMapping("/saveFlowJj")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveFlowJj(ImBizDataq imBizDataq) {

        return imBizDataqService.saveFlowJj(imBizDataq);
    }

    /**
     * 解决问题单
     *
     * @param imBizDataq
     * @return
     */
    @PostMapping("/saveFlowZf")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveFlowZf(ImBizDataq imBizDataq) {

        return imBizDataqService.saveFlowZf(imBizDataq);
    }

    /**
     * 解决问题单
     *
     * @param imBizDataq
     * @return
     */
    @PostMapping("/saveFlowTuihui")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveFlowTuihui(ImBizDataq imBizDataq) {

        return imBizDataqService.saveFlowTuihui(imBizDataq);
    }
    /**
     * 批量分发问题单
     *
     * @param imBizDataq
     * @return
     */
    @PostMapping("/saveFlowPlFenFa")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveFlowPlFenFa(ImBizDataq imBizDataq) {

        return imBizDataqService.saveFlowPlFenFa(imBizDataq);
    }


    /**
     * 我的数据问题单修改保存数据问题单
     */
    @PostMapping("/saveOrCancellation")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveOrCancellation(ImBizDataq imBizDataq) {

        return imBizDataqService.saveOrCancellation(imBizDataq);
    }

}