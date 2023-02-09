package com.ruoyi.activiti.controller.itsm.cmbiz;

import com.ruoyi.activiti.domain.*;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 【资源变更单】Controller
 *
 * @author liul
 * @date 2021-01-13
 */
@Controller
@RequestMapping("/system/single")
public class CmBizSingleController extends BaseController {
    private String prefix = "system/single";
    private String prefix_public = "cmbiz";

    @Autowired
    private ICmBizSingleService cmBizSingleService;
    @Autowired
    private IIdGeneratorService idGeneratorService;
    @Autowired
    IOgPersonService iOgPersonService;
    @Autowired
    ISysDeptService iSysDeptService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private ISysWorkService iSysWorkService;
    @Autowired
    private IOgTypeinfoService iOgTypeinfoService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    private IImBizIssuefxService iImBizIssuefxService;
    @Autowired
    private IFmSwService iFmSwService;
    @Autowired
    private ICmBizQingqiuService iCmBizQingqiuService;
    @Autowired
    private FmDispatchService fmDispatchService;
    @Autowired
    private IPubRelationService iPubRelationService;
    @Autowired
    private IPubBizSmsService pubBizSmsService;
    @Autowired
    private IPubBizPresmsService pubBizPresmsService;
    @Autowired
    private ItsmToDevopsService itsmToDevopsService;
    @Autowired
    private IPubAttachmentService pubAttachmentService;

    @GetMapping()
    public String single() {
        return prefix + "/single";
    }

    /**
     * 我的变更单页面打开
     *
     * @return
     */
    @GetMapping("/cmbiz")
    public String cmBizPage() {
        return prefix_public + "/cmbiz";
    }

    /**
     * 评估变更单页面打开
     *
     * @return
     */
    @GetMapping("/assessor")
    public String assessor() {
        return prefix_public + "/assessor";
    }

    /**
     * 审核变更单页面打开
     *
     * @return
     */
    @GetMapping("/check")
    public String check() {
        return prefix_public + "/check";
    }

    /**
     * 实施变更单页面打开
     *
     * @return
     */
    @GetMapping("/implementor")
    public String implementor() {
        return prefix_public + "/implementor";
    }

    /**
     * 我的变更单页面打开
     *
     * @return
     */
    @GetMapping("/assessedCmbiz")
    public String assessedCmbizPage() {
        return prefix_public + "/assessedCmbiz";
    }

    /**
     * 分管领导审阅变更单页面打开
     *
     * @return
     */
    @GetMapping("/review")
    public String reviewDetail() {
        return prefix_public + "/review";
    }

    /**
     * 查询变更单页面打开
     *
     * @return
     */
    @GetMapping("/search")
    public String search(ModelMap mmap) {
        String pId = ShiroUtils.getUserId();
        String orgId = iOgPersonService.selectOgPersonEvoById(pId).getOrgId();
        mmap.put("orgId", orgId);
        return prefix_public + "/search";
    }

    /**
     * 变更及时效率页面打开
     *
     * @return
     */
    @GetMapping("/bgjsfficiency")
    public String bgjsfficiency() {
        return prefix_public + "/appr/BgJSEfficiency";
    }

    /**
     * 紧急变更占比报表页面打开
     *
     * @return
     */
    @GetMapping("/bgjjfficiency")
    public String bgjjfficiency() {
        return prefix_public + "/appr/BgJJEfficiency";
    }

    /**
     * 查询【我的变更单】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CmBizSingle cmBizSingle) {
        startPage();
        OgUser u = ShiroUtils.getOgUser();
        cmBizSingle.setApplicantId(u.getpId());
        String startCreatTime = cmBizSingle.getParams().get("startCreatTime").toString();
        String endCreatTime = cmBizSingle.getParams().get("endCreatTime").toString();
        if (StringUtils.isNotEmpty(startCreatTime)) {
            cmBizSingle.getParams().put("startCreatTime", handleTimeYYYYMMDD(startCreatTime) + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            cmBizSingle.getParams().put("endCreatTime", handleTimeYYYYMMDD(endCreatTime) + "240000");
        }
        List<CmBizSingle> list = cmBizSingleService.selectMyCmBizSingleList(cmBizSingle);
        return getDataTable(list);
    }

    /**
     * 查询【我的变更单】列表
     */
    @PostMapping("/assessedCmbizlist")
    @ResponseBody
    public TableDataInfo assessedCmbizlist(CmBizSingle cmBizSingle) {
        startPage();
        String startCreatTime = cmBizSingle.getParams().get("startCreatTime").toString();
        String endCreatTime = cmBizSingle.getParams().get("endCreatTime").toString();
        if (StringUtils.isNotEmpty(startCreatTime)) {
            cmBizSingle.getParams().put("startCreatTime", handleTimeYYYYMMDD(startCreatTime) + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            cmBizSingle.getParams().put("endCreatTime", handleTimeYYYYMMDD(endCreatTime) + "240000");
        }
        OgUser user = ShiroUtils.getOgUser();
        List<CmBizSingle> list = new ArrayList<>();
        cmBizSingle.setChangeManagerId(user.getpId());
        cmBizSingle.setChangeSingleStauts("0400");
        list.addAll(cmBizSingleService.selectMyCmBizSingleList(cmBizSingle));
        cmBizSingle.setChangeManagerId("");

        cmBizSingle.setCheckerId(user.getpId());
        cmBizSingle.setChangeSingleStauts("0401");
        list.addAll(cmBizSingleService.selectMyCmBizSingleList(cmBizSingle));
        String cmbizId = "";
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                cmbizId += list.get(i).getChangeId() + ",";
            }
        } else {
            return getDataTable(list);
        }
        String[] cmbizIds = cmbizId.split(",");
        CmBizSingle cmBizSingle1 = new CmBizSingle();
        cmBizSingle1.getParams().put("cmbizIds", cmbizIds);
        return getDataTable(cmBizSingleService.selectMyCmBizSingleList(cmBizSingle1));
    }

    /**
     * 导出【我的变更单】列表
     */
    @Log(title = "【资源变更单】", businessType = BusinessType.EXPORT)
    @PostMapping("/myExport")
    @ResponseBody
    public AjaxResult myExport(CmBizSingle cmBizSingle) {
        OgUser u = ShiroUtils.getOgUser();
        cmBizSingle.setApplicantId(u.getpId());
        String startCreatTime = cmBizSingle.getParams().get("startCreatTime").toString();
        String endCreatTime = cmBizSingle.getParams().get("endCreatTime").toString();
        if (StringUtils.isNotEmpty(startCreatTime)) {
            cmBizSingle.getParams().put("startCreatTime", handleTimeYYYYMMDD(startCreatTime) + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            cmBizSingle.getParams().put("endCreatTime", handleTimeYYYYMMDD(endCreatTime) + "240000");
        }
        List<CmBizSingle> list = cmBizSingleService.selectMyCmBizSingleList(cmBizSingle);

        ExcelUtil<CmBizSingle> util = new ExcelUtil<CmBizSingle>(CmBizSingle.class);
        return util.exportExcel(list, "资源变更单");
    }

    /**
     * 查询【查询变更单】列表
     */
    @PostMapping("/pagelist")
    @ResponseBody
    public TableDataInfo pagelist(CmBizSingle cmBizSingle) {

        if (StringUtils.isNotEmpty(cmBizSingle.getExpectStartTime())) {
            String expectStartTime = handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectStartTime());
            cmBizSingle.setExpectStartTime(expectStartTime);
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getExpectEndTime())) {
            String expectEndTime = handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectEndTime());
            cmBizSingle.setExpectEndTime(expectEndTime);
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getPracticleStart())) {
            String practicleStart = handleTimeYYYYMMDDHHMMSS(cmBizSingle.getPracticleStart());
            cmBizSingle.setPracticleStart(practicleStart);
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getPracticleEnd())) {
            String practicleEnd = handleTimeYYYYMMDDHHMMSS(cmBizSingle.getPracticleEnd());
            cmBizSingle.setPracticleEnd(practicleEnd);
        }
        //默认查询条件
        String pid = ShiroUtils.getOgUser().getpId();

        OgOrg agency = iSysDeptService.selectDeptById(iOgPersonService.selectOgPersonEvoById(pid).getOrgId());
        if ("1".equals(agency.getOrgLv())) {

        } else if (agency.getLevelCode().startsWith("/000000000/010000888/")
                && !"5".equals(agency.getOrgLv())) {

        } else {
            OgOrg org = iSysDeptService.selectDeptByCode(iSysDeptService.getpCode(pid));
//            System.out.println("省内机构是："+org.getOrgName());
            cmBizSingle.setCreaterOrgId(org.getLevelCode());//对于省里修改为查询自己所在省的
        }
        startPage();
        List<CmBizSingle> list = cmBizSingleService.selectCmBizSingleList(cmBizSingle);
        return getDataTable(list);
    }

    /**
     * 查询【代办任务】列表
     */
    @PostMapping("/getassessor")
    @ResponseBody
    public TableDataInfo getassessor(CmBizSingle cmBizSingle) {
        if (StringUtils.isNotEmpty(cmBizSingle.getCreatetime())) {
            cmBizSingle.setCreatetime(handleTimeYYYYMMDD(cmBizSingle.getCreatetime()));
        }
        startPage();
        List<Map<String, Object>> reList = activitiCommService.userTask("CmZy", cmBizSingle.getParams().get("description").toString());
        List<OgGroup> userGroupList = iSysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String, Object>> regList = new ArrayList<>();
        if (!userGroupList.isEmpty()) {
            regList = activitiCommService.groupTasks("CmZy", cmBizSingle.getParams().get("description").toString());
            reList.addAll(regList);
        }
        List<CmBizSingle> resultlist2 = getResultList(cmBizSingle, reList);
        return getDataTable_ideal(resultlist2);
    }

    /**
     * 查询【查询待审核】列表
     */
    @PostMapping("/getcheck")
    @ResponseBody
    public TableDataInfo getcheck(CmBizSingle cmBizSingle) {

        if (StringUtils.isNotEmpty(cmBizSingle.getCreatetime())) {
            cmBizSingle.setCreatetime(handleTimeYYYYMMDD(cmBizSingle.getCreatetime()));
        }
        List<Map<String, Object>> reList1 = activitiCommService.userTask("CmZy", "check");
        List<Map<String, Object>> reList2 = activitiCommService.userTask("CmZy", "fucheck");
        if (!reList1.isEmpty()) {
            reList1.addAll(reList2);
        } else {
            reList1 = reList2;
        }
        List<CmBizSingle> resultlist2 = getResultList(cmBizSingle, reList1);
        return getDataTable_ideal(resultlist2);
    }

    public List<CmBizSingle> getResultList(CmBizSingle cmBizSingle, List<Map<String, Object>> reList1) {
        List<CmBizSingle> resultlist = new ArrayList<>();
        for (Map<String, Object> map : reList1) {
            String business_key = map.get("businesskey").toString();
            if (StringUtils.isNotEmpty(business_key)) {
                cmBizSingle.setChangeId(business_key);
                CmBizSingle cbs = cmBizSingleService.getFlowCmBiz(cmBizSingle);
                if (cbs != null) {
                    resultlist.add(cbs);
                }
            }
        }
        return resultlist;
    }

    /**
     * 导出【查询变更单】列表
     */
    @Log(title = "【资源变更单】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CmBizSingle cmBizSingle) {
        String isCurrentPage = (String) cmBizSingle.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getExpectStartTime())) {
            String expectStartTime = handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectStartTime());
            cmBizSingle.setExpectStartTime(expectStartTime);
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getExpectEndTime())) {
            String expectEndTime = handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectEndTime());
            cmBizSingle.setExpectEndTime(expectEndTime);
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getPracticleStart())) {
            String practicleStart = handleTimeYYYYMMDDHHMMSS(cmBizSingle.getPracticleStart());
            cmBizSingle.setPracticleStart(practicleStart);
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getPracticleEnd())) {
            String practicleEnd = handleTimeYYYYMMDDHHMMSS(cmBizSingle.getPracticleEnd());
            cmBizSingle.setPracticleEnd(practicleEnd);
        }
        List<CmBizSingle> list = cmBizSingleService.selectCmBizSingleList(cmBizSingle);

        ExcelUtil<CmBizSingle> util = new ExcelUtil<CmBizSingle>(CmBizSingle.class);
        return util.exportExcel(list, "资源变更单");
    }

    /**
     * 【我的变更单添加按钮打开页面】
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        //打开新建页面生成单号
        String bizType = "BG";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);//默认回传单号
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = iOgPersonService.selectOgPersonEvoById(pId);
        OgOrg org = iSysDeptService.selectDeptById(person.getOrgId());
        mmap.put("createrOrgName", org.getOrgName());//回传创建机构
        mmap.put("createrOrgId", person.getOrgId());
        String isCenter = iSysDeptService.getIsCenter();//判断创建人是否为全国中心机构
        if (!"1".equals(isCenter)) {//为省中心
            mmap.put("issn", "0");
        }
        mmap.addAttribute("changeId", IdUtils.fastSimpleUUID());
        return prefix_public + "/flow/start";
    }

    /**
     * flag 类型 1调度事件单转 2事务事件单 3应用问题单转 4变更请求单转
     * id 对应工单id
     * 【其他工单转资源变更单打开页面】
     */
    @GetMapping("/add/{flag}/{bizId}")
    public String add(ModelMap mmap, @PathVariable("flag") String flag, @PathVariable("bizId") String bizId) {
        //打开新建页面生成单号
        String bizType = "BG";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);//默认回传单号
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = iOgPersonService.selectOgPersonEvoById(pId);
        OgOrg org = iSysDeptService.selectDeptById(person.getOrgId());
        mmap.put("createrOrgName", org.getOrgName());//回传创建机构
        mmap.put("createrOrgId", person.getOrgId());
        mmap.put("flag", flag);
        mmap.addAttribute("changeId", UUID.getUUIDStr());
        //如果为其他工单转判断flag
        if ("1".equals(flag)) {//1调度事件单转
            FmDd fmdd = fmDispatchService.selectFmddById(bizId);
            mmap.put("bizId", bizId);
            mmap.put("changeSingleName", fmdd.getFaultTitle());
        }
        if ("2".equals(flag)) {//2事务事件单转
            FmSw fmSw = iFmSwService.selectFmSwById(bizId);
            mmap.put("bizId", bizId);
            mmap.put("changeSingleName", fmSw.getFaultTitle());//回显问题标题为变更主题
        }
        if ("3".equals(flag)) {//3应用问题单转
            ImBizIssuefx imBizIssuefx = iImBizIssuefxService.selectImBizIssuefxById(bizId);
            mmap.put("bizId", bizId);
            mmap.put("changeSingleName", imBizIssuefx.getIssuefxName());//回显问题标题为变更主题
            mmap.put("changeDetails", imBizIssuefx.getIssuefxText());//回显问题描述为变更内容
            mmap.put("changeProgram", imBizIssuefx.getDealDetail());//回显问题解决情况为变更实施方案
        }
        if ("4".equals(flag)) {//4变更请求单转
            CmBizQingqiu cmBizQingqiu = iCmBizQingqiuService.selectCmBizQingqiuById(bizId);
            mmap.put("bizId", bizId);
            if (StringUtils.isNotEmpty(cmBizQingqiu.getChangeCategoryId())) {
                mmap.put("changeCategoryId", cmBizQingqiu.getChangeCategoryId());//回显分类
                mmap.put("changeCategoryName", iOgTypeinfoService.selectOgTypeinfoById(cmBizQingqiu.getChangeCategoryId()).getTypeName());//回显分类名称
            }
            mmap.put("importantLev", cmBizQingqiu.getImportantLev());//回显变更级别
            mmap.put("sysid", cmBizQingqiu.getSysid());//回显系统ID
            mmap.put("sysname", cmBizQingqiu.getSysname());//回显系统名
            mmap.put("changeStage", cmBizQingqiu.getChangeStage());//回显变更所处阶段
            mmap.put("changeResource", cmBizQingqiu.getChangeResource());//回显变更来源
            mmap.put("isNotice", cmBizQingqiu.getIsNotice());//回显是否通知业务
            mmap.put("isStop", cmBizQingqiu.getIsStop());//回显是否影响业务连续性
            mmap.put("changeSingleName", cmBizQingqiu.getChangeSingleName());//回显变更标题为变更主题
            mmap.put("changeCauseText", cmBizQingqiu.getChangeCauseText());//回显变更原因为变更原因
            mmap.put("changeDetails", cmBizQingqiu.getChangeDetails());//回显问题解决情况为变更内容
        }
        return prefix_public + "/flow/start";
    }

    /**
     * 【新建暂存资源变更单】
     */
    @Log(title = "【暂存变更单】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(CmBizSingle cmBizSingle) {
        cmBizSingle.setExpectStartTime(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectStartTime()));
        cmBizSingle.setExpectEndTime(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectEndTime()));
        String userId = ShiroUtils.getUserId();
        cmBizSingle.setApplicantId(userId);//变更申请人
        if (StringUtils.isNotEmpty(cmBizSingle.getChangeResource()) && !"1".equals(cmBizSingle.getChangeResource())) {
            cmBizSingle.setChangeReason("0300");
        }
        try {
            if (StringUtils.isEmpty(cmBizSingle.getChangeSingleStauts())) {//新增
                cmBizSingle.setChangeSingleStauts("0100");
                cmBizSingleService.insertCmBizSingle(cmBizSingle);
                //如果为其他工单转需要保存关联信息
                if (cmBizSingle.getParams().containsKey("flag")) {
                    String flag = cmBizSingle.getParams().get("flag").toString();
                    if (StringUtils.isNotEmpty(flag) && ("1".equals(flag) || "2".equals(flag) || "3".equals(flag) || "4".equals(flag))) {
                        PubRelation p = new PubRelation();
                        if ("1".equals(flag)) {//调度
                            p.setType("10");
                        }
                        if ("2".equals(flag)) {//事务
                            p.setType("07");
                        }
                        if ("3".equals(flag)) {//问题
                            p.setType("04");
                        }
                        if ("4".equals(flag)) {//变更请求
                            p.setType("12");
                        }
                        PubRelation p2 = new PubRelation();
                        p2.setObj1Id(cmBizSingle.getBizId());
                        p2.setObj2Id(cmBizSingle.getChangeId());
                        List<PubRelation> pr = iPubRelationService.selectPubRelationList(p2);
                        if (!pr.isEmpty()) {
                            for (PubRelation prr : pr) {
                                iPubRelationService.deletePubRelationById(prr.getRelationId());
                            }
                        }
                        p.setObj1Id(cmBizSingle.getBizId());
                        p.setObj2Id(cmBizSingle.getChangeId());
                        p.setRelationId(UUID.getUUIDStr());
                        iPubRelationService.insertPubRelation(p);
                    }
                }
            } else {//修改
                cmBizSingleService.updateCmBizSingle(cmBizSingle);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("暂存变更单操作失败，请刷新列表后重新操作。");
        }
        return AjaxResult.success("暂存变更单操作成功", cmBizSingle);
    }

    /**
     * 变更单提交
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/saveflowdata")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowdata(CmBizSingle cmBizSingle) {
        int i = 0;
        if (StringUtils.isEmpty(cmBizSingle.getChangeSingleStauts())) {//如果没有第一次提交

        } else if ("0100".equals(cmBizSingle.getChangeSingleStauts())) {//如果有0100是待提交修改后提交
            i = 2;
        } else if ("0200".equals(cmBizSingle.getChangeSingleStauts())) {//退回修改提交
            i = 1;
        } else {
            throw new BusinessException("该变更单已被处理，请刷新列表检查后继续操作!");
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getChangeResource()) && !"1".equals(cmBizSingle.getChangeResource())) {
            cmBizSingle.setChangeReason("0300");
        }
        if ("1".equals(cmBizSingle.getIfAuto()) && "1".equals(cmBizSingleService.selectByTypeId(cmBizSingle.getChangeCategoryId()))) {//判断是否自动化变更
            Attachment att2 = new Attachment();
            att2.setOwnerId(cmBizSingle.getChangeId());
            att2.setFileType("3");
            List<Attachment> list2 = pubAttachmentService.selectAttachmentList(att2);
            if (list2.isEmpty()) {
                throw new BusinessException("未上传网络自动化步骤excl，无法提交自动化变更。");
            }
            //如果是调用自动化查询状态接口
            Map map = itsmToDevopsService.getFinalPdfPath(cmBizSingle.getChangeCode());
            if (map.containsKey("filePath") && StringUtils.isNotEmpty(map.get("filePath").toString())) {// 判断编排任务生成pdf文件
                Attachment am = new Attachment();
                am.setOwnerId(cmBizSingle.getChangeId());
                am.setFilePath(map.get("filePath").toString());
                am.setFileName(map.get("pdfName").toString());//文件名需要单独传
                am.setFileTime(DateUtils.dateTimeNow());//时间
                am.setGroupName(map.get("group").toString());
                am.setAtId(UUID.getUUIDStr());

                Attachment att = new Attachment();
                att.setOwnerId(cmBizSingle.getChangeId());
                att.setFileType("3");
                att.setAutomateStatus("0");
                List<Attachment> list = pubAttachmentService.selectAttachmentList(att);

                if (!list.isEmpty()) {//有
                    list.get(0).setAutomateResultMsg(am.getAtId());
                }
                am.setAutomateResultMsg(list.get(0).getAtId());
                am.setCreateId(list.get(0).getCreateId());

                pubAttachmentService.insertAttachment(am);
            } else {//如果是自动化拿到状态不正确或者没有取到
                throw new BusinessException("网络自动化平台编排任务未完成，变更单无法提交。");
            }
        }
        cmBizSingle.setExpectStartTime(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectStartTime()));
        cmBizSingle.setExpectEndTime(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectEndTime()));
        Map<String, Object> reMap = new HashMap<>();
        OgUser u = ShiroUtils.getOgUser();//获取当前登陆人
        cmBizSingle.setApplicationSubmitTime(DateUtils.dateTimeNow());//提交时间
        cmBizSingle.setApplicantId(u.getpId());//变更申请人
        cmBizSingle.setImplementorId(u.getpId());//变更实施人
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(u.getpId());
        cmBizSingle.setImplementorOrgid(ogPerson.getOrgId());//实施机构
        reMap.put("createId", u.getpId());//流程发起人
        reMap.put("userId", u.getpId());//流程发起人
        reMap.put("assessorId", cmBizSingle.getChangeManagerId());//下一步处理人[评估人]
        reMap.put("checkId", cmBizSingle.getCheckerId());//审核人放进流程中
        cmBizSingle.setChangeSingleStauts("0300");//待评估状态
        try {
            if (i == 1) {
                cmBizSingleService.updateCmBizSingle(cmBizSingle);
                reMap.put("processDefinitionKey", "CmZy");
                reMap.put("businessKey", cmBizSingle.getChangeId());
                reMap.put("reCode", "0");//流程走向[待评估]
                activitiCommService.complete(reMap);
            } else {
                //计算是否紧急变更
                List<PubParaValue> pp = iPubParaValueService.selectPubParaValueByParaName("Cm_Day");//拿到参数变更紧急规则天数
                String StartTime2 = cmBizSingle.getExpectStartTime();//拿到变更开始时间
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
                long day1 = 0;
                Date d2 = fmt.parse(StartTime2);
                Long l = d2.getTime() - new Date().getTime();
                day1 = l / 1000 / 3600 / 24;
                String sysId = cmBizSingle.getSysid();
                if (StringUtils.isNotEmpty(sysId)) {
                    String[] sys = sysId.split(",");//拿到所有系统ID并分组
                    String iks = "";
                    for (int y = 0; y < sys.length; y++) {//遍历所有系统拿到重要系统标志并分组
                        OgSys sys2 = iSysApplicationManagerService.selectOgSysBySysId(sys[y]);
                        iks = sys2.getIsKeySys() + ",";
                    }
                    if (!pp.isEmpty()) {
                        if (iks.indexOf("1") != -1) {//包含重点系统
                            int day2 = Integer.parseInt(pp.get(1).getValue());
                            if (day1 > day2) {//如果计划开始时间大于参数规定天数
                                cmBizSingle.setIsJjbg("0");
                            } else {
                                cmBizSingle.setIsJjbg("1");
                            }
                        } else {
                            int day2 = Integer.parseInt(pp.get(0).getValue());
                            if (day1 > day2) {//如果计划开始时间大于参数规定天数
                                cmBizSingle.setIsJjbg("0");
                            } else {
                                cmBizSingle.setIsJjbg("1");
                            }
                        }
                    }
                } else {
                    if ("1".equals(cmBizSingle.getChangeType())) {//类型为重大变更
                        int day2 = Integer.parseInt(pp.get(1).getValue());
                        if (day1 > day2) {//如果计划开始时间大于参数规定天数
                            cmBizSingle.setIsJjbg("0");
                        } else {
                            cmBizSingle.setIsJjbg("1");
                        }
                    } else {
                        int day2 = Integer.parseInt(pp.get(0).getValue());
                        if (day1 > day2) {//如果计划开始时间大于参数规定天数
                            cmBizSingle.setIsJjbg("0");
                        } else {
                            cmBizSingle.setIsJjbg("1");
                        }
                    }
                }
                if (i == 0) {
                    cmBizSingleService.insertCmBizSingle(cmBizSingle);
                    activitiCommService.startProcess(cmBizSingle.getChangeId(), "CmZy", reMap);
                } else {
                    cmBizSingleService.updateCmBizSingle(cmBizSingle);
                    activitiCommService.startProcess(cmBizSingle.getChangeId(), "CmZy", reMap);
                }
                //如果为其他工单转需要保存关联信息
                if (cmBizSingle.getParams().containsKey("flag")) {
                    String flag = cmBizSingle.getParams().get("flag").toString();
                    if (StringUtils.isNotEmpty(flag) && ("1".equals(flag) || "2".equals(flag) || "3".equals(flag) || "4".equals(flag))) {
                        PubRelation p = new PubRelation();
                        if ("1".equals(flag)) {//调度
                            p.setType("10");
                        }
                        if ("2".equals(flag)) {//事务
                            p.setType("07");
                        }
                        if ("3".equals(flag)) {//问题
                            p.setType("04");
                        }
                        if ("4".equals(flag)) {//变更请求
                            p.setType("12");
                        }
                        PubRelation p2 = new PubRelation();
                        p2.setObj1Id(cmBizSingle.getBizId());
                        p2.setObj2Id(cmBizSingle.getChangeId());
                        List<PubRelation> pr = iPubRelationService.selectPubRelationList(p2);
                        if (!pr.isEmpty()) {
                            for (PubRelation prr : pr) {
                                iPubRelationService.deletePubRelationById(prr.getRelationId());
                            }
                        }
                        p.setObj1Id(cmBizSingle.getBizId());
                        p.setObj2Id(cmBizSingle.getChangeId());
                        p.setRelationId(UUID.getUUIDStr());
                        iPubRelationService.insertPubRelation(p);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("资源变更单提交操作失败，请刷新列表后重新操作。");
        }
        return AjaxResult.success("资源变更单提交成功", cmBizSingle.getChangeId());
    }

    /**
     * 跳转克隆公告页面
     */
    @GetMapping("/clone/{changeId}")
    public String noticeClone(@PathVariable("changeId") String changeId, ModelMap mmap)
    {
        String bizType = "BG";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);

        //获取当前页面的信息
        CmBizSingle cmBizSingle = cmBizSingleService.selectCmBizSingleById(changeId);

        //生成计划表主键id
        cmBizSingle.setChangeId(UUID.getUUIDStr());
        cmBizSingle.setChangeCode(bizType + nowDateStr + numStr);

        if (StringUtils.isNotEmpty(cmBizSingle.getCreaterOrgId())) {
            mmap.put("createOrgName", iSysDeptService.selectDeptById(cmBizSingle.getCreaterOrgId()).getOrgName());//回显创建机构
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getChangeCategoryId())) {
            mmap.put("typeName", iOgTypeinfoService.selectOgTypeinfoById(cmBizSingle.getChangeCategoryId()).getTypeName());//回显变更分类
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getChangeOrg())) {
            mmap.put("changeOrgName", iSysDeptService.selectDeptById(cmBizSingle.getChangeOrg()).getOrgName());//回显评估机构
            OgOrg ogOrg = iSysDeptService.selectDeptById(cmBizSingle.getChangeOrg());//回显评估人
            List<OgPerson> list = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg.getOrgId(), "2101");
            mmap.put("managerList", list);
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getCheckOrg())) {
            mmap.put("checkOrgName", iSysDeptService.selectDeptById(cmBizSingle.getCheckOrg()).getOrgName());//回显审批机构
            OgOrg ogOrg2 = iSysDeptService.selectDeptById(cmBizSingle.getCheckOrg());//回显审批人
            List<OgPerson> list2 = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg2.getOrgId(), "2102");
            mmap.put("checkList", list2);
        }
        String isCenter = iSysDeptService.getIsCenter();//判断创建人是否为全国中心机构
        if (!"1".equals(isCenter)) {//为省中心
            mmap.put("issn", "0");
        }

        mmap.put("cmBizSingle", cmBizSingle);

        return prefix_public + "/clone";
    }

    /**
     * 克隆保存
     */
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/clone")
    @ResponseBody
    public AjaxResult noticeClonesave(CmBizSingle cmBizSingle)
    {
        return toAjax(cmBizSingleService.insertCmBizSingle(cmBizSingle));
    }

    /**
     * 修改【点击修改按钮后】
     */
    @GetMapping("/edit/{changeId}")
    public String edit(@PathVariable("changeId") String changeId, ModelMap mmap) {
        CmBizSingle cmBizSingle = cmBizSingleService.selectCmBizSingleById(changeId);
        String status = cmBizSingle.getChangeSingleStauts();
        String url = prefix_public + "/edit";
        if ("0200".equals(status)) {//如果为退回待修改状态
            url = prefix_public + "/edit2";
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getCreaterOrgId())) {
            mmap.put("createOrgName", iSysDeptService.selectDeptById(cmBizSingle.getCreaterOrgId()).getOrgName());//回显创建机构
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getChangeCategoryId())) {
            mmap.put("typeName", iOgTypeinfoService.selectOgTypeinfoById(cmBizSingle.getChangeCategoryId()).getTypeName());//回显变更分类
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getChangeOrg())) {
            mmap.put("changeOrgName", iSysDeptService.selectDeptById(cmBizSingle.getChangeOrg()).getOrgName());//回显评估机构
            OgOrg ogOrg = iSysDeptService.selectDeptById(cmBizSingle.getChangeOrg());//回显评估人
            List<OgPerson> list = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg.getOrgId(), "2101");
            mmap.put("managerList", list);
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getCheckOrg())) {
            mmap.put("checkOrgName", iSysDeptService.selectDeptById(cmBizSingle.getCheckOrg()).getOrgName());//回显审批机构
            OgOrg ogOrg2 = iSysDeptService.selectDeptById(cmBizSingle.getCheckOrg());//回显审批人
            List<OgPerson> list2 = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg2.getOrgId(), "2102");
            mmap.put("checkList", list2);
        }
        String isCenter = iSysDeptService.getIsCenter();//判断创建人是否为全国中心机构
        if (!"1".equals(isCenter)) {//为省中心
            mmap.put("issn", "0");
        }
        mmap.put("cmBizSingle", cmBizSingle);
        return url;
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult editSave(CmBizSingle cmBizSingle) {
        return toAjax(cmBizSingleService.updateCmBizSingle(cmBizSingle));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【资源变更单】", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String changeId) {
        PubRelation pr = new PubRelation();
        pr.setObj2Id(changeId);
        List<PubRelation> list = iPubRelationService.selectPubRelationList(pr);
        if (!list.isEmpty()) {
            for (PubRelation prl : list) {
                iPubRelationService.deletePubRelationById(prl.getRelationId());
            }
        }
        return toAjax(cmBizSingleService.deleteCmBizSingleById(changeId));
    }

    /**
     * 点击评估变更单菜单页面的评估按钮对应的页面
     */
    @GetMapping("/assessor/{changeId}")
    public String passign(@PathVariable("changeId") String changeId, ModelMap mmap) {
        String url = prefix_public + "/flow/assessor";
        CmBizSingle cmBizSingle = cmBizSingleService.selectCmBizSingleByIdById(changeId);
        OgUser u = ShiroUtils.getOgUser();
        if (!u.getpId().equals(cmBizSingle.getChangeManagerId())) {
            url = prefix_public + "/flow/xtassessor";
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getApplicantId())) {
            mmap.put("createName", iOgPersonService.selectOgPersonEvoById(cmBizSingle.getApplicantId()).getpName());//回显申请人
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getCreaterOrgId())) {
            mmap.put("createOrgName", iSysDeptService.selectDeptById(cmBizSingle.getCreaterOrgId()).getOrgName());//回显创建机构
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getChangeCategoryId())) {
            mmap.put("typeName", iOgTypeinfoService.selectOgTypeinfoById(cmBizSingle.getChangeCategoryId()).getTypeName());//回显变更分类
        }
        mmap.put("cmBizSingle", cmBizSingle);
        return url;
    }


    /**
     * 实施机构选择打开页面
     *
     * @return
     */
    @GetMapping("/selectogOrg4")
    public String selectogOrg4() {
        return prefix_public + "/flow/subpage/selectOgorg4";
    }

    /**
     * 涉及系统选择页面
     *
     * @return
     */
    @GetMapping("/selectApplication")
    public String selectApplication() {
        return prefix_public + "/flow/subpage/selectApplication";
    }

    /**
     * 查看详情页面
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        CmBizSingle cmBizSingle = cmBizSingleService.selectCmBizSingleByIdById(id);
        if (cmBizSingle != null) {
            if (StringUtils.isNotEmpty(cmBizSingle.getApplicantId())) {
                mmap.put("createName", iOgPersonService.selectOgPersonEvoById(cmBizSingle.getApplicantId()).getpName());//回显申请人
            }
            if (StringUtils.isNotEmpty(cmBizSingle.getCreaterOrgId())) {
                mmap.put("createOrgName", iSysDeptService.selectDeptById(cmBizSingle.getCreaterOrgId()).getOrgName());//回显创建机构
            }
            if (StringUtils.isNotEmpty(cmBizSingle.getImplementorId())) {
                mmap.put("implementorName", iOgPersonService.selectOgPersonEvoById(cmBizSingle.getImplementorId()).getpName());//回显实施人
            }
            if (StringUtils.isNotEmpty(cmBizSingle.getChangeCategoryId())) {
                mmap.put("typeName", iOgTypeinfoService.selectOgTypeinfoById(cmBizSingle.getChangeCategoryId()).getTypeName());//回显变更分类
            }
        }
        mmap.put("cmBizSingle", cmBizSingle);
        return "cmbiz/view";
    }

    /**
     * 协同处理页面
     *
     * @return
     */
    @GetMapping("/synergy/{changeId}")
    public String synergy(@PathVariable("changeId") String changeId, ModelMap mmap) {
        CmBizSingle cmBizSingle = cmBizSingleService.selectCmBizSingleById(changeId);
        mmap.put("cmBizSingle", cmBizSingle);
        return prefix_public + "/flow/subpage/synergy";
    }

    /**
     * 协同处理添加页面
     *
     * @return
     */
    @GetMapping("/selectAccount")
    public String selectAccount() {
        return prefix_public + "/flow/subpage/selectAccount";
    }

    /**
     * 查询协同处理人
     *
     * @return
     */
    @PostMapping("/assessorperson")
    @ResponseBody
    public TableDataInfo assessorperson(OgPerson ogPerson) {
        startPage();
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("rId", "2101");
        ogPerson.setParams(reMap);
        List<OgPerson> checkList = iOgPersonService.selectListByRoleId(ogPerson);
        return getDataTable(checkList);
    }

    /**
     * 协同评估
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/saveflowassessor")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowassessor(CmBizSingle cmBizSingle) {
        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleById(cmBizSingle.getChangeId());
        if (!"0300".equals(cbs.getChangeSingleStauts())) {
            throw new BusinessException("该变更单已被处理，请刷新列表检查后继续操作!");
        }
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", cbs.getChangeId());
        if (cmBizSingle.getParams().containsKey("comment")) {
            reMap.put("comment", cmBizSingle.getParams().get("comment").toString());
        }
        if (cmBizSingle.getParams().containsKey("userId")) {
            reMap.put("fuassossorId", cmBizSingle.getParams().get("userId").toString());
        } else {
            throw new BusinessException("协同评估人不能为空。");
        }
        reMap.put("processDefinitionKey", "CmZy");
        reMap.put("reCode", "1");
        reMap.put("userId", ShiroUtils.getUserId());//流程发起人
        try {
            cmBizSingleService.updateCmBizSingle(cbs);
            activitiCommService.usersComplete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("协同评估操作失败，请刷新列表后重新操作。");
        }
        return success("协同评估操作成功。");
    }

    /**
     * 评估通过
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/saveflowmult")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowmult(CmBizSingle cmBizSingle) {
        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleById(cmBizSingle.getChangeId());
        if (cbs == null) {
            throw new BusinessException("单号不存在请刷新列表后重新操作。");
        }
        if (!"0300".equals(cbs.getChangeSingleStauts())) {
            throw new BusinessException("该变更单已被处理，请刷新列表检查后继续操作!");
        }
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", cbs.getChangeId());
        if (cmBizSingle.getParams().containsKey("comment")) {
            reMap.put("comment", cmBizSingle.getParams().get("comment").toString());
        }
        reMap.put("processDefinitionKey", "CmZy");
        reMap.put("reCode", "0");
        String userId = ShiroUtils.getUserId();
        reMap.put("userId", userId);
        cbs.setChangeSingleStauts("0400");
        try {
            activitiCommService.complete(reMap);
            cmBizSingleService.updateCmBizSingle(cbs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("评估通过操作失败，请刷新列表后重新操作。");
        }
        //评估通过待审批发送短信通知审批人
        String smsText = "变更单号：" + cbs.getChangeCode() + ",标题：" + cbs.getChangeSingleName() + "的变更单已经评估完成，请登录运维管理平台审批。";
        OgPerson person = iOgPersonService.selectOgPersonEvoById(cbs.getCheckerId());//获取短信接收人 发起人
        sendSms(smsText, person);
        return success("评估通过操作成功。");
    }

    /**
     * 协同评估通过
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/saveflowXtmult")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowXtmult(CmBizSingle cmBizSingle) {
        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleById(cmBizSingle.getChangeId());
        if (cbs == null) {
            throw new BusinessException("单号不存在请刷新列表后重新操作。");
        }
        if (!"0300".equals(cbs.getChangeSingleStauts())) {
            throw new BusinessException("该变更单已被处理，请刷新列表检查后继续操作!");
        }
        Map<String, Object> reMap = new HashMap<>();
        if (cmBizSingle.getParams().containsKey("flag")) {
            String flag = cmBizSingle.getParams().get("flag").toString();
            if (StringUtils.isNotEmpty(flag)) {
                if ("0".equals(flag)) {
                    if (cmBizSingle.getParams().containsKey("comment")) {
                        reMap.put("comment", "不通过：" + cmBizSingle.getParams().get("comment").toString());
                    }
                } else {
                    if (cmBizSingle.getParams().containsKey("comment")) {
                        reMap.put("comment", "通过：" + cmBizSingle.getParams().get("comment").toString());
                    }
                }
            }
        }
        reMap.put("businessKey", cbs.getChangeId());
        reMap.put("processDefinitionKey", "CmZy");
        String userId = ShiroUtils.getUserId();
        reMap.put("userId", userId);
        try {
            activitiCommService.usersComplete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("协同评估通过操作失败，请刷新列表后重新操作。");
        }
        return success("评估通过操作成功。");
    }

    /**
     * 退回
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/saveflowedit")
    @ResponseBody
    @Transactional
    public AjaxResult saveflowedit(CmBizSingle cmBizSingle) {
        String userId = ShiroUtils.getUserId();
        cmBizSingleService.checkCmBizNoPass(cmBizSingle, userId);
        return success("资源变更单退回操作成功。");
    }

    /**
     * 否决
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/saveflowpass")
    @ResponseBody
    @Transactional
    public AjaxResult saveflowpass(CmBizSingle cmBizSingle) {
        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleById(cmBizSingle.getChangeId());
        if (!"0400".equals(cbs.getChangeSingleStauts()) && !"0401".equals(cbs.getChangeSingleStauts())) {
            throw new BusinessException("该变更单状态不正确或已被否决，请刷新列表检查后继续操作!");
        }
        cbs.setChangeSingleStauts("0804");//赋值否决状态
        Map<String, Object> reMap = new HashMap<>();
        if (cmBizSingle.getParams().containsKey("comment")) {
            reMap.put("comment", cmBizSingle.getParams().get("comment").toString());
        }
        reMap.put("businessKey", cbs.getChangeId());
        reMap.put("processDefinitionKey", "CmZy");
        reMap.put("reCode", "3");
        String userId = ShiroUtils.getUserId();
        reMap.put("userId", userId);
        try {
            cmBizSingleService.updateCmBizSingle(cbs);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("变更单否决操作失败，请刷新列表后重新操作。");
        }
        return success("变更单否决操作成功。");
    }

    /**
     * 作废
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/savepass")
    @ResponseBody
    @Transactional
    public AjaxResult savepass(CmBizSingle cmBizSingle) {
        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleById(cmBizSingle.getChangeId());
        if (!"0200".equals(cbs.getChangeSingleStauts())) {
            throw new BusinessException("该变更单状态不正确或已被否决，请刷新列表检查后继续操作!");
        }
        cbs.setChangeSingleStauts("0600");//赋值否决状态
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", cbs.getChangeId());
        reMap.put("processDefinitionKey", "CmZy");
        reMap.put("reCode", "3");
        String userId = ShiroUtils.getUserId();
        reMap.put("userId", userId);
        try {
            cmBizSingleService.updateCmBizSingle(cbs);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("变更单作废操作失败，请刷新列表后重新操作。");
        }
        if ("1".equals(cbs.getIfAuto())) {//是自动化平台相关撤销自动化编排任务
            Map map = itsmToDevopsService.toAutoByExcl(cbs.getChangeCode(), "cancel", "", "", "", "", "", "", "");
            if (!"0".equals(map.get("state"))) {
                logger.debug("调用网络自动化撤销任务失败，单号：。" + cbs.getChangeCode());
            } else {
                logger.info("调用网络自动化撤销任务成功，单号：。" + cbs.getChangeCode());
            }
        }
        return success("变更单作废操作成功。");
    }

    /**
     * 点击审核变更单菜单页面的审核按钮对应的页面
     */
    @GetMapping("/check/{changeId}")
    public String check(@PathVariable("changeId") String changeId, ModelMap mmap) {
        CmBizSingle cmBizSingle = cmBizSingleService.selectCmBizSingleByIdById(changeId);
        if (StringUtils.isNotEmpty(cmBizSingle.getApplicantId())) {
            mmap.put("createName", iOgPersonService.selectOgPersonEvoById(cmBizSingle.getApplicantId()).getpName());//回显申请人
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getCreaterOrgId())) {
            mmap.put("createOrgName", iSysDeptService.selectDeptById(cmBizSingle.getCreaterOrgId()).getOrgName());//回显创建机构
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getChangeCategoryId())) {
            mmap.put("typeName", iOgTypeinfoService.selectOgTypeinfoById(cmBizSingle.getChangeCategoryId()).getTypeName());//回显变更分类
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getImplementorOrgid())) {
            mmap.put("implementorOrgName", iSysDeptService.selectDeptById(cmBizSingle.getImplementorOrgid()).getOrgName());//回显审批机构
            OgOrg ogOrg2 = iSysDeptService.selectDeptById(cmBizSingle.getCheckOrg());//回显审批人
            List<OgPerson> list2 = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg2.getOrgId(), "2103");
            mmap.put("personList", list2);
        }
        //判断是否为省内创建
        OgOrg org = iSysDeptService.selectDeptById(cmBizSingle.getCreaterOrgId());
        if (org.getOrgCode().indexOf("010") == 0 || org.getOrgCode().indexOf("100000") == 0 || "000000000".equals(org.getOrgCode())) {
            if (StringUtils.isNotEmpty(cmBizSingle.getChangeType()) && "2".equals(cmBizSingle.getChangeType())) {//为流程选项赋值
                mmap.put("flowSelect", "00");
            } else {
                if ("1".equals(cmBizSingle.getChangeType())) {
                    mmap.put("flowSelect", "01");
                } else {
                    mmap.put("flowSelect", "00");
                }
            }
        }
        mmap.put("cmBizSingle", cmBizSingle);
        if ("0400".equals(cmBizSingle.getChangeSingleStauts())) {
            return prefix_public + "/flow/check";
        } else {
            return prefix_public + "/flow/fucheck";
        }
    }

    /**
     * 审核通过
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/saveflowcheck")
    @ResponseBody
    @Transactional
    public AjaxResult saveflowcheck(CmBizSingle cmBizSingle) {
        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleById(cmBizSingle.getChangeId());
        if (!"0400".equals(cbs.getChangeSingleStauts())) {
            throw new BusinessException("该变更单已被处理，请刷新列表检查后继续操作!");
        }
        if ("1".equals(cbs.getIfAuto()) && "1".equals(cmBizSingleService.selectByTypeId(cbs.getChangeCategoryId()))) {//是自动化进行判断
            boolean flag = getFlag(cbs);//判断审批时间是否超过计划变更开始时间
            if (flag) {
                throw new BusinessException("自动化变更已超过变更计划开始时间，请点击退回按钮退回给发起人重新发起变更。");
            }
        }
        cbs.setImplementorId(cmBizSingle.getImplementorId());//回填实施人
        cbs.setImplementorOrgid(cmBizSingle.getImplementorOrgid());//回填实施机构
        cbs.setFucheckerId(cmBizSingle.getFucheckerId());//回填分管领导
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", cbs.getChangeId());
        reMap.put("implementorId", cbs.getImplementorId());
        String userId = ShiroUtils.getUserId();
        reMap.put("userId", userId);
        if (cmBizSingle.getParams().containsKey("flowSelect")) {//流程选项
            String flowSelect = cmBizSingle.getParams().get("flowSelect").toString();
            if (StringUtils.isEmpty(flowSelect)) {//省里提交
                reMap.put("reCode", "0");
                cbs.setChangeSingleStauts("0500");
            } else {//全国中心提交
                if ("00".equals(flowSelect)) {//通知 需要发短信
                    reMap.put("reCode", "0");
                    cbs.setChangeSingleStauts("0500");
                    cbs.setFucheckerFlag("1");// 1 为待审阅 2 为以审阅
                } else {//审批
                    cbs.setFucheckerFlag("0");//设置 0 为待审批的
                    reMap.put("reCode", "1");
                    reMap.put("fucheckerId", cbs.getFucheckerId());//给流程传分管领导
                    cbs.setChangeSingleStauts("0401");
                }
            }
        }
        if (cmBizSingle.getParams().containsKey("comment")) {
            reMap.put("comment", cmBizSingle.getParams().get("comment").toString());
        }
        try {
            reMap.put("processDefinitionKey", "CmZy");
            cmBizSingleService.updateCmBizSingle(cbs);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("资源变更单操作失败，请刷新列表后重新操作。");
        }
        if ("0500".equals(cbs.getChangeSingleStauts())) {//如果是待实施状态
            cmBizSingleService.sendCheck(cbs);
        }
        return success("资源变更单审核操作成功，单号：" + cbs.getChangeCode());
    }

    /**
     * 分管领导审核通过
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/saveflowfucheck")
    @ResponseBody
    @Transactional
    public AjaxResult saveflowfucheck(CmBizSingle cmBizSingle) {
        String userId = ShiroUtils.getUserId();
        return cmBizSingleService.checkCmBizPass(cmBizSingle, userId);
    }

    public boolean getFlag(CmBizSingle cbs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boolean flag = true;
        try {
            Date date1 = sdf.parse(DateUtils.timeToTimeMillis(cbs.getExpectStartTime()));
            flag = DateUtils.getNowDate().getTime() >= date1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("资源变更单计划开始时间转换日期格式有误。");
        }
        return flag;
    }

    /**
     * 点击实施变更单菜单页面的实施按钮对应的页面
     */
    @GetMapping("/implementor/{changeId}")
    public String implementor(@PathVariable("changeId") String changeId, ModelMap mmap) {
        CmBizSingle cmBizSingle = cmBizSingleService.selectCmBizSingleByIdById(changeId);
        if (StringUtils.isNotEmpty(cmBizSingle.getApplicantId())) {
            mmap.put("createName", iOgPersonService.selectOgPersonEvoById(cmBizSingle.getApplicantId()).getpName());//回显申请人
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getImplementorId())) {
            mmap.put("implementorName", iOgPersonService.selectOgPersonEvoById(cmBizSingle.getImplementorId()).getpName());//回显实施人
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getCreaterOrgId())) {
            mmap.put("createOrgName", iSysDeptService.selectDeptById(cmBizSingle.getCreaterOrgId()).getOrgName());//回显创建机构
        }
        if (StringUtils.isNotEmpty(cmBizSingle.getChangeCategoryId())) {
            mmap.put("typeName", iOgTypeinfoService.selectOgTypeinfoById(cmBizSingle.getChangeCategoryId()).getTypeName());//回显变更分类
        }
        mmap.put("cmBizSingle", cmBizSingle);
        return prefix_public + "/flow/implementor";
    }

    /**
     * 实施
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/saveflowimple")
    @ResponseBody
    @Transactional
    public AjaxResult saveflowimple(CmBizSingle cmBizSingle) {
        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleById(cmBizSingle.getChangeId());
        if (!"0500".equals(cbs.getChangeSingleStauts())) {
            throw new BusinessException("该变更单已被实施，请刷新列表检查后继续操作!");
        }
        cbs.setPracticleStart(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getPracticleStart()));
        cbs.setPracticleEnd(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getPracticleEnd()));
        cbs.setPerformResult(cmBizSingle.getPerformResult());//回填实施结果
        cbs.setIsStopReal(cmBizSingle.getIsStopReal());//回填实际是否影响业务
        cbs.setNotesText(cmBizSingle.getNotesText());//回填备注
        Map<String, Object> reMap = new HashMap<>();
        if ("0".equals(cmBizSingle.getPerformResult())) {//根据实施结果赋值状态
            cbs.setChangeSingleStauts("0801");
        }
        if ("1".equals(cmBizSingle.getPerformResult())) {
            cbs.setChangeSingleStauts("0802");
        }
        if ("2".equals(cmBizSingle.getPerformResult())) {
            cbs.setChangeSingleStauts("0803");
        }
        if ("3".equals(cmBizSingle.getPerformResult())) {
            cbs.setChangeSingleStauts("0805");
        }
        //判断是否及时变更
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date endDate = sdf.parse(cbs.getPracticleEnd());
            if (nowDate.getTime() - endDate.getTime() <= (60 * 60 * 24 * 1000)) {
                cbs.setIsJs("1");
            } else {
                cbs.setIsJs("0");
            }
        } catch (ParseException e) {
            logger.debug("资源变更单计算是否及时变更日期转换失败，实施结束时间为：" + cbs.getPracticleEnd());
        }
        reMap.put("businessKey", cbs.getChangeId());
        reMap.put("processDefinitionKey", "CmZy");
        String userId = ShiroUtils.getUserId();
        reMap.put("userId", userId);
        try {
            cmBizSingleService.updateCmBizSingle(cbs);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("资源变更单实施失败，请刷新列表后重新操作。");
        }
        return success("资源变更单实施成功。");
    }

    /**
     * 资源变更单及时统计
     */
    @PostMapping("/selectCmBizJs")
    @ResponseBody
    public TableDataInfo selectCmBizJs(BgJSEfficiency bgJSEfficiency) throws ParseException {
        String startCreatTime = bgJSEfficiency.getParams().get("startCreatTime").toString();
        String endCreatTime = bgJSEfficiency.getParams().get("endCreatTime").toString();
        if (StringUtils.isNotEmpty(startCreatTime)) {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startCreatTime);
            String d1 = new SimpleDateFormat("yyyyMMdd").format(date1);
            bgJSEfficiency.getParams().put("startCreatTime", d1 + "000000");
        } else {
            Date date = new Date();//获取当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间
            String time = DateUtils.handleTimeYYYYMMDDHHMMSS(calendar.getTime());
            bgJSEfficiency.getParams().put("startCreatTime", time.substring(0, 8) + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endCreatTime);
            String d2 = new SimpleDateFormat("yyyyMMdd").format(date2);
            bgJSEfficiency.getParams().put("endCreatTime", d2 + "240000");
        } else {
            bgJSEfficiency.getParams().put("endCreatTime", DateUtils.dateTimeNow("yyyyMMdd") + 240000);
        }
        List<BgJSEfficiency> list = cmBizSingleService.selectCmBizJs(bgJSEfficiency);
        return getDataTable(list);
    }

    /**
     * 资源变更单紧急统计
     */
    @PostMapping("/selectCmBizJJ")
    @ResponseBody
    public TableDataInfo selectCmBizJJ(BgJJEfficiency bgJJEfficiency) throws ParseException {
        String startCreatTime = bgJJEfficiency.getParams().get("startCreatTime").toString();
        String endCreatTime = bgJJEfficiency.getParams().get("endCreatTime").toString();
        if (StringUtils.isNotEmpty(startCreatTime)) {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startCreatTime);
            String d1 = new SimpleDateFormat("yyyyMMdd").format(date1);
            bgJJEfficiency.getParams().put("startCreatTime", d1 + "000000");
        } else {
            Date date = new Date();//获取当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间
            String time = DateUtils.handleTimeYYYYMMDDHHMMSS(calendar.getTime());
            bgJJEfficiency.getParams().put("startCreatTime", time.substring(0, 8) + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endCreatTime);
            String d2 = new SimpleDateFormat("yyyyMMdd").format(date2);
            bgJJEfficiency.getParams().put("endCreatTime", d2 + "240000");
        } else {
            bgJJEfficiency.getParams().put("endCreatTime", DateUtils.dateTimeNow("yyyyMMdd") + 240000);
        }
        List<BgJJEfficiency> list = cmBizSingleService.selectCmBizJJ(bgJJEfficiency);
        return getDataTable(list);
    }

    /**
     * 导出资源变更单及时统计
     */
    @Log(title = "变更及时效率报表", businessType = BusinessType.EXPORT)
    @PostMapping("/exportjs")
    @ResponseBody
    public AjaxResult exportjs(BgJSEfficiency bgJSEfficiency) throws Exception {
        String startCreatTime = bgJSEfficiency.getParams().get("startCreatTime").toString();
        String endCreatTime = bgJSEfficiency.getParams().get("endCreatTime").toString();
        if (StringUtils.isNotEmpty(startCreatTime)) {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startCreatTime);
            String d1 = new SimpleDateFormat("yyyyMMdd").format(date1);
            bgJSEfficiency.getParams().put("startCreatTime", d1 + "000000");
        } else {
            Date date = new Date();//获取当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间
            String time = DateUtils.handleTimeYYYYMMDDHHMMSS(calendar.getTime());
            bgJSEfficiency.getParams().put("startCreatTime", time.substring(0, 8) + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endCreatTime);
            String d2 = new SimpleDateFormat("yyyyMMdd").format(date2);
            bgJSEfficiency.getParams().put("endCreatTime", d2 + "240000");
        } else {
            bgJSEfficiency.getParams().put("endCreatTime", DateUtils.dateTimeNow("yyyyMMdd") + 240000);
        }
        List<BgJSEfficiency> list = cmBizSingleService.selectCmBizJs(bgJSEfficiency);
        ExcelUtil<BgJSEfficiency> util = new ExcelUtil<BgJSEfficiency>(BgJSEfficiency.class);
        return util.exportExcel(list, "变更及时效率报表");
    }

    /**
     * 导出资源变更单紧急统计
     */
    @Log(title = "紧急变更占比报表", businessType = BusinessType.EXPORT)
    @PostMapping("/exportjj")
    @ResponseBody
    public AjaxResult exportjj(BgJJEfficiency bgJJEfficiency) throws Exception {
        String startCreatTime = bgJJEfficiency.getParams().get("startCreatTime").toString();
        String endCreatTime = bgJJEfficiency.getParams().get("endCreatTime").toString();
        if (StringUtils.isNotEmpty(startCreatTime)) {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startCreatTime);
            String d1 = new SimpleDateFormat("yyyyMMdd").format(date1);
            bgJJEfficiency.getParams().put("startCreatTime", d1 + "000000");
        } else {
            Date date = new Date();//获取当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间
            String time = DateUtils.handleTimeYYYYMMDDHHMMSS(calendar.getTime());
            bgJJEfficiency.getParams().put("startCreatTime", time.substring(0, 8) + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endCreatTime);
            String d2 = new SimpleDateFormat("yyyyMMdd").format(date2);
            bgJJEfficiency.getParams().put("endCreatTime", d2 + "240000");
        } else {
            bgJJEfficiency.getParams().put("endCreatTime", DateUtils.dateTimeNow("yyyyMMdd") + 240000);
        }
        List<BgJJEfficiency> list = cmBizSingleService.selectCmBizJJ(bgJJEfficiency);
        ExcelUtil<BgJJEfficiency> util = new ExcelUtil<BgJJEfficiency>(BgJJEfficiency.class);
        return util.exportExcel(list, "紧急变更占比报表");
    }

    /**
     * 根据变更单ID查询调度事件单
     */
    @PostMapping("/fmddList/{id}")
    @ResponseBody
    public TableDataInfo getCmAndFmdd(@PathVariable("id") String id) {
        PubRelation pr = new PubRelation();
        List<FmDd> fmdd = new ArrayList<>();
        pr.setObj2Id(id);
        pr.setType("10");
        List<PubRelation> list = iPubRelationService.selectPubRelationList(pr);
        if (!list.isEmpty()) {
            for (PubRelation prtion : list) {
                FmDd fd = fmDispatchService.selectFmddById(prtion.getObj1Id());
                if (fd != null) {
                    fd.getParams().put("relationId", prtion.getRelationId());
                    fmdd.add(fd);
                }
            }
        }
        return getDataTable(fmdd);
    }

    /**
     * 根据变更单ID查询事务事件单
     */
    @PostMapping("/fmswList/{id}")
    @ResponseBody
    public TableDataInfo getCmAndFmsw(@PathVariable("id") String id) {
        PubRelation pr = new PubRelation();
        List<FmSw> fmSw = new ArrayList<>();
        pr.setObj2Id(id);
        pr.setType("07");
        List<PubRelation> list = iPubRelationService.selectPubRelationList(pr);
        if (!list.isEmpty()) {
            for (PubRelation prtion : list) {
                FmSw fs = iFmSwService.selectFmSwById(prtion.getObj1Id());
                if (fs != null) {
                    fs.getParams().put("relationId", prtion.getRelationId());
                    fmSw.add(fs);
                }
            }
        }
        return getDataTable(fmSw);
    }

    /**
     * 根据变更单ID查询问题单
     */
    @PostMapping("/imList/{id}")
    @ResponseBody
    public TableDataInfo getCmAndIm(@PathVariable("id") String id) {
        PubRelation pr = new PubRelation();
        List<ImBizIssuefx> imbiz = new ArrayList<>();
        pr.setObj2Id(id);
        pr.setType("04");
        List<PubRelation> list = iPubRelationService.selectPubRelationList(pr);
        if (!list.isEmpty()) {
            for (PubRelation prtion : list) {
                ImBizIssuefx ib = iImBizIssuefxService.selectImBizIssuefxById(prtion.getObj1Id());
                if (ib != null) {
                    ib.getParams().put("relationId", prtion.getRelationId());
                    imbiz.add(ib);
                }
            }
        }
        return getDataTable(imbiz);
    }

    /**
     * 根据变更单ID查询变更请求单
     */
    @PostMapping("/cmqqList/{id}")
    @ResponseBody
    public TableDataInfo getCmAndCpqq(@PathVariable("id") String id) {
        PubRelation pr = new PubRelation();
        List<CmBizQingqiu> fmbiz = new ArrayList<>();
        pr.setObj2Id(id);
        pr.setType("12");
        List<PubRelation> list = iPubRelationService.selectPubRelationList(pr);
        if (!list.isEmpty()) {
            for (PubRelation prtion : list) {
                CmBizQingqiu cb = iCmBizQingqiuService.selectCmBizQingqiuById(prtion.getObj1Id());
                if (cb != null) {
                    OgPerson person = iOgPersonService.selectOgPersonEvoById(cb.getApplicantId());
                    if (person != null) {
                        cb.getParams().put("createName", person.getpName());
                    }
                    cb.getParams().put("relationId", prtion.getRelationId());
                    fmbiz.add(cb);
                }
            }
        }
        return getDataTable(fmbiz);
    }

    /**
     * 发送短信
     *
     * @param setSmsText 短信内容
     * @param person     短信接收人
     */
    public void sendSms(String setSmsText, OgPerson person) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(setSmsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("111103");// 检查对象
        p.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    /**
     * 关联工单
     *
     * @return
     */
    @GetMapping("/goBizList")
    public String goBizList(String changeId, String type, ModelMap map) {
        map.addAttribute("changeId", changeId);
        String url = "";
        if ("fmdd".equals(type)) {
            url = prefix_public + "/relevance/fmdd";
        } else if ("fmsw".equals(type)) {
            url = prefix_public + "/relevance/fmsw";
        } else if ("imbiz".equals(type)) {
            url = prefix_public + "/relevance/imbiz";
        } else if ("cmqq".equals(type)) {
            url = prefix_public + "/relevance/cmqq";
        }
        return url;
    }

    /**
     * 发送自动化调用
     */
    @PostMapping("/toAuto")
    @ResponseBody
    public AjaxResult toAuto(CmBizSingle cmBizSingle) {

        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleByIdById(cmBizSingle.getChangeId());
        Attachment att2 = new Attachment();
        att2.setOwnerId(cbs.getChangeId());
        att2.setFileType("3");
        List<Attachment> list2 = pubAttachmentService.selectAttachmentList(att2);
        if (!list2.isEmpty()) {
            String planImplPer = iOgPersonService.selectOgPersonEvoById(cbs.getApplicantId()).getMobilPhone();
            String planReiwPer = iOgPersonService.selectOgPersonEvoById(cbs.getChangeManagerId()).getMobilPhone();
            Map map = itsmToDevopsService.toAutoByExcl(cbs.getChangeCode(), cbs.getChangeSingleName(), planImplPer, planReiwPer, cbs.getIfAuto(), cbs.getExpectStartTime(), cbs.getExpectEndTime(), list2.get(0).getGroupName(), list2.get(0).getFilePath());
            if (!"0".equals(map.get("state"))) {
                throw new BusinessException("发送网络自动化平台调用失败，请联系网络自动化平台。");
            } else {
                list2.get(0).setAutomateStatus("0");//已发送自动化的
                list2.get(0).setFileTime(DateUtils.dateTimeNow());
                pubAttachmentService.updateAttachment(list2.get(0));
            }
        } else {
            throw new BusinessException("未上传附件类型为网络自动化步骤的附件不允许发送。");
        }
        return success("发送自动化成功。");
    }

    /**
     * 编排自动模板
     */
    @PostMapping("/editAuto")
    @ResponseBody
    public AjaxResult editAuto(CmBizSingle cmBizSingle) {
        String url = "跳转错误请刷新或登录自动化平台查看。";
        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleByIdById(cmBizSingle.getChangeId());
        if (cbs != null) {
            Attachment att2 = new Attachment();
            att2.setOwnerId(cbs.getChangeId());
            att2.setFileType("3");
            att2.setAutomateStatus("0");
            List<Attachment> list2 = pubAttachmentService.selectAttachmentList(att2);
            if (!list2.isEmpty()) {
                String planImplPer = iOgPersonService.selectOgPersonEvoById(cbs.getApplicantId()).getMobilPhone();
                String planReiwPer = iOgPersonService.selectOgPersonEvoById(cbs.getChangeManagerId()).getMobilPhone();
                url = itsmToDevopsService.toPage(cbs.getChangeCode(), cbs.getChangeSingleName(), planImplPer, planReiwPer, cbs.getIfAuto(), cbs.getExpectStartTime(), cbs.getExpectEndTime());//调用跳转自动化页面
            } else {
                throw new BusinessException("未发送excl给自动化或自动化未完成入库，无法编排模板,请先点击发送自动化将自动化步骤excl传给自动化，如果已发送excl请稍等自动化入库完成后点击编排任务。");
            }
        } else {
            throw new BusinessException("未发送自动化步骤excl给自动化。");
        }
        return AjaxResult.success("编辑模板打开成功", url);
    }

    /**
     * 流程撤回
     *
     * @param id
     * @return
     */
    @PostMapping("/rollBack")
    @ResponseBody
    @Transactional
    public AjaxResult rollBack(String id) {
        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleById(id);
        if (cbs != null && "0300".equals(cbs.getChangeSingleStauts()) || "0400".equals(cbs.getChangeSingleStauts())) {
            String userId = ShiroUtils.getUserId();
            if (userId.equals(cbs.getApplicantId())) {
                try {
                    activitiCommService.revokeCreateTask(id, userId, "CmZy");
                    cbs.setChangeSingleStauts("0100");
                    cmBizSingleService.updateCmBizSingle(cbs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return error("变更单创建人非当前登陆人不可撤回。");
            }
        } else {
            return error("变更单不存在或者流程已被审批。");
        }
        return success("变更单撤回成功。");
    }

    /**
     * 流程撤回(以评估 已审批)
     *
     * @param id
     * @return
     */
    @PostMapping("/assessedRollBack")
    @ResponseBody
    @Transactional
    public AjaxResult assessedRollBack(String id) {
        CmBizSingle cbs = cmBizSingleService.selectCmBizSingleById(id);
        String userId = ShiroUtils.getUserId();
        if (cbs != null && userId.equals(cbs.getChangeManagerId())) {
            if ("0400".equals(cbs.getChangeSingleStauts())) {
                try {
                    activitiCommService.revokeTask(id, userId, "CmZy");
                    cbs.setChangeSingleStauts("0300");
                    cmBizSingleService.updateCmBizSingle(cbs);
                    return success("变更单撤回成功。");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return error("变更单不存在或者流程已被审批。");
            }
        } else if (cbs != null && userId.equals(cbs.getCheckerId())) {
            if ("0401".equals(cbs.getChangeSingleStauts())) {
                try {
                    activitiCommService.revokeTask(id, userId, "CmZy");
                    cbs.setChangeSingleStauts("0400");
                    cmBizSingleService.updateCmBizSingle(cbs);
                    //撤回成功给分管领导发个短信
                    OgPerson person = iOgPersonService.selectOgPersonEvoById(cbs.getCheckerId());//审批人
                    String smsText = "资源变更单号：" + cbs.getChangeCode() + "，标题：" + cbs.getChangeSingleName() + "，审批人：" + person.getpName() + "已撤回，请忽略该资源变更单的审批，此条短信不用回复。";
                    OgPerson person1 = iOgPersonService.selectOgPersonEvoById(cbs.getFucheckerId());//获取短信接收人 发起人
                    sendSms(smsText, person1);
                    return success("变更单撤回成功。");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return error("变更单不存在或者流程已被审批。");
            }
        }
        return error("变更单不存在");
    }

    /**
     * 评估机构选择打开页面
     *
     * @return
     */
    @GetMapping("/selectogOrg2")
    public String selectogOrg2() {
        return prefix_public + "/flow/subpage/selectOgorg2";
    }

    /**
     * ID查询变更分类是否为网络或者网络下级
     */
    @PostMapping("/selectByTypeId")
    @ResponseBody
    public AjaxResult selectByTypeId(String id) {
        AjaxResult ajaxResult = new AjaxResult();
        String flag = cmBizSingleService.selectByTypeId(id);
        ajaxResult.put("flag", flag);
        return ajaxResult;
    }

    /**
     * 分管领导审阅变更单列表
     */
    @PostMapping("/reviewList")
    @ResponseBody
    public TableDataInfo reviewList(CmBizSingle cmBizSingle) {
        startPage();
        String flag = cmBizSingle.getFucheckerFlag();
        String pId = ShiroUtils.getUserId();
        cmBizSingle.setFucheckerId(pId);
        String startCreatTime = cmBizSingle.getParams().get("startCreatTime").toString();
        String endCreatTime = cmBizSingle.getParams().get("endCreatTime").toString();
        if (StringUtils.isNotEmpty(startCreatTime)) {
            cmBizSingle.getParams().put("startCreatTime", handleTimeYYYYMMDD(startCreatTime) + "000000");
        }
        if (StringUtils.isNotEmpty(endCreatTime)) {
            cmBizSingle.getParams().put("endCreatTime", handleTimeYYYYMMDD(endCreatTime) + "240000");
        }
        List<CmBizSingle> list = cmBizSingleService.selectMyCmBizSingleList(cmBizSingle);
        return getDataTable(list);
    }

    /**
     * 分管领导审阅变更单
     */
    @GetMapping("/reviewDetail/{id}")
    public String reviewDetail(@PathVariable("id") String id, ModelMap mmap) {
        CmBizSingle cmBizSingle = cmBizSingleService.selectCmBizSingleByIdById(id);
        if (cmBizSingle != null) {
            if (StringUtils.isNotEmpty(cmBizSingle.getApplicantId())) {
                mmap.put("createName", iOgPersonService.selectOgPersonEvoById(cmBizSingle.getApplicantId()).getpName());//回显申请人
            }
            if (StringUtils.isNotEmpty(cmBizSingle.getCreaterOrgId())) {
                mmap.put("createOrgName", iSysDeptService.selectDeptById(cmBizSingle.getCreaterOrgId()).getOrgName());//回显创建机构
            }
            if (StringUtils.isNotEmpty(cmBizSingle.getImplementorId())) {
                mmap.put("implementorName", iOgPersonService.selectOgPersonEvoById(cmBizSingle.getImplementorId()).getpName());//回显实施人
            }
            if (StringUtils.isNotEmpty(cmBizSingle.getChangeCategoryId())) {
                mmap.put("typeName", iOgTypeinfoService.selectOgTypeinfoById(cmBizSingle.getChangeCategoryId()).getTypeName());//回显变更分类
            }
        }
        mmap.put("cmBizSingle", cmBizSingle);
        CmBizSingle cmBizSingle1 = new CmBizSingle();
        cmBizSingle1.setChangeId(id);
        cmBizSingle1.setFucheckerFlag("2");//1 为待审阅 2 为以审阅
        cmBizSingleService.updateCmBizSingle(cmBizSingle1);
        return "cmbiz/reviewDetail";
    }

    /**
     * 变更单审阅
     *
     * @param cmBizSingle
     * @return
     */
    @PostMapping("/updateFucheckerFlag")
    @ResponseBody
    @Transactional
    public AjaxResult updateFucheckerFlag(CmBizSingle cmBizSingle) {
        CmBizSingle cmBizSingle1 = new CmBizSingle();
        cmBizSingle1.setChangeId(cmBizSingle.getChangeId());
        cmBizSingle1.setFucheckerFlag("2");//1 为待审阅 2 为以审阅
        //cmBizSingleService.updateCmBizSingle(cmBizSingle1);
        return AjaxResult.success("已审阅", cmBizSingle.getChangeCode());
    }

}
