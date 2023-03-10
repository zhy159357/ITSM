package com.ruoyi.activiti.controller.itsm.issueList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.constants.ImBizIssueConstants;
import com.ruoyi.activiti.domain.CmBizSingleSJVo;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.service.CmBizSingleSJService;
import com.ruoyi.activiti.service.EventRunService;
import com.ruoyi.activiti.service.ICmBizSingleService;
import com.ruoyi.activiti.service.ICommonService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IImBizIssuefxService;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.DictConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.CmBizSingle;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;

/**
 * ???????????????
 *
 * @author dayong_sun
 */
@Controller
@RequestMapping("issueList/build")
public class ImBizIssuefxController extends BaseController {
    private String prefix = "issueList/build";
    private String prefixClass = "issueList/build/wtdClass";
    private String prefixinside = "issueList/inside";
    private String prefix_public = "fmbiz";
    @Autowired
    private IImBizIssuefxService issueService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ICmBizSingleService cmBizSingleService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private IFmBizService iFmBizService;
    @Autowired
    private IIdGeneratorService idGeneratorService;
    @Autowired
    private EventRunService eventRunService;
    @Autowired
    private CmBizSingleSJService cmBizSingleSJService;
    @Autowired
    private IPubRelationService iPubRelationService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private ISysApplicationManagerService sysApplicationManagerService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private ISysUserService userService;
    @Autowired(required=false)
    private KnowledgeTitleService knowledgeTitleService;

    @GetMapping("go/{type}")
    public String operlog(@PathVariable("type") String type, ModelMap mmap) {
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        mmap.addAttribute("issueOrgs", issueOrgs);
        mmap.addAttribute("type", type);
        if ("dataDeal".equals(type)) {
            return prefixinside + "/issue";
        }
        return prefix + "/issue";
    }

    public Map<String, String> switchList(String paraName) {
        List<PubParaValue> list = iPubParaValueService.selectPubParaValueByParaName(paraName);
        Map<String, String> mp = new HashMap<>();
        for (PubParaValue pubParaValue : list) {
            mp.put(pubParaValue.getValue(), pubParaValue.getValueDetail());
        }
        return mp;
    }

    /**
     * ?????????????????????
     *
     * @param issue
     * @return
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ImBizIssuefx issue) {
        if (StringUtils.isNotEmpty(issue.getCurrentState())) {
            String[] state = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
            issue.getParams().put("states", statecollection);
        }
        String myFlag = "1";
        if (StringUtils.isNotNull(issue.getParams().get("myFlag"))) {
            myFlag = issue.getParams().get("myFlag").toString();
        }

        if (StringUtils.isNotEmpty(myFlag)) {
            //????????????
            if (ImBizIssueConstants.MYFLAG_CREAT.equals(myFlag)) {
                issue.setCreaterId(ShiroUtils.getOgUser().getUserId());
            }
            //????????????
            if (ImBizIssueConstants.MYFLAG_DEAL.equals(myFlag)) {
                startPage();
                issue.getParams().put("dealId", ShiroUtils.getUserId());
                List<ImBizIssuefx> listIssue = issueService.selectIssueHistroyList(issue);
                return getDataTable(listIssue);
            }
        }
        startPage();
        List<ImBizIssuefx> list = issueService.selectIssueList(issue);
        return getDataTable(list);
    }

    /**
     * ?????????????????????
     *
     * @param issue
     * @return
     */

    @PostMapping("/listAll")
    @ResponseBody
    public TableDataInfo listAll(ImBizIssuefx issue) {
        startPage();
        if (StringUtils.isNotEmpty(issue.getCurrentState())) {
            String[] state = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
            issue.getParams().put("states", statecollection);
        }
        List<ImBizIssuefx> list = issueService.selectIssueList(issue);
        return getDataTable(list);
    }

    /**
     * ?????????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ImBizIssuefx issue) {
        /*//???????????? issueFenlei
        Map<String,String> issueFenlei=switchList("WT_FENLEI");
        //????????????
        Map<String,String> issueType=switchList("WT_TYPE");
        //????????????
        Map<String,String> issuesource=switchList("WT_SOUCE");
        //????????????
        Map<String,String> status=switchList("issue_status");
        //???????????? seriousLev
        Map<String,String> seriousLev=switchList("issue_serious_lev");*/
        String isCurrentPage = (String) issue.getParams().get("currentPage");
        if (StringUtils.isNotEmpty(issue.getCurrentState())) {
            String[] state = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
//            if (statecollection.contains(ImBizIssueConstants.STATUS_YEWUFUHE)) {
//                issue.setHanguptask(ImBizIssueConstants.STATUS_YEWUFUHE);
//            }
            issue.getParams().put("states", statecollection);
        }
        OgPerson person = new OgPerson();
        person.setrId(ImBizIssueConstants.ROLE_JSJINGLI);
        List<OgPerson> listAudit = commonService.selectPersonByOrgAndRole(person);
        Map<String, String> mp = new HashMap<>();
        for (OgPerson pn : listAudit) {
            mp.put(pn.getpId(), pn.getpName());
        }
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<ImBizIssuefx> list = issueService.selectIssueList(issue);
        List<ImBizIssuefx> reList = new ArrayList<>();
        for (ImBizIssuefx imfx : list) {
            /*String status1=status.get(imfx.getCurrentState());
            imfx.setIssueFenlei(issueFenlei.get(imfx.getIssueFenlei()));
            imfx.setIssueType(issueType.get(imfx.getIssueType()));
            imfx.setIssuesource(issuesource.get(imfx.getIssuesource()));
            imfx.setCurrentState(status1);
            imfx.setSeriousLev(seriousLev.get(imfx.getSeriousLev()));*/
            imfx.setAuditId(mp.get(imfx.getAuditId()));
            //????????????
            PubRelation pubRelation = new PubRelation();
            pubRelation.setObj2Id(imfx.getIssuefxId());
            //???????????????
            pubRelation.setType(ImBizIssueConstants.TRANSFERRED_YW);
            List<PubRelation> puList = iPubRelationService.selectPubRelationList(pubRelation);
            if (!CollectionUtils.isEmpty(puList)) {
                String nos = "";
                for (PubRelation pn : puList) {
                    FmBiz fmBizs = iFmBizService.selectFmBizById(pn.getObj1Id());
                    if (fmBizs != null) {
                        nos += fmBizs.getFaultNo() + ",";
                    }
                }
                if (StringUtils.isNotEmpty(nos)) {
                    imfx.setFmNo(nos.substring(0, nos.length() - 1));
                }
            }
            //???????????????
            pubRelation.setType(ImBizIssueConstants.TRANSFERRED_EVENTRUN);
            List<PubRelation> runList = iPubRelationService.selectPubRelationList(pubRelation);
            if (!CollectionUtils.isEmpty(runList)) {
                String runNo = "";
                for (PubRelation pn : runList) {
                    EventRun event = eventRunService.selectEventRunById(pn.getObj1Id());
                    if (event != null) {
                        runNo += event.getEventNo() + ",";
                    }

                }
                if (StringUtils.isNotEmpty(runNo)) {
                    imfx.setFmjkNo(runNo.substring(0, runNo.length() - 1));
                }
            }
            //????????????
            pubRelation.setType(ImBizIssueConstants.TRANSFERRED_FMBIZ);
            List<PubRelation> jjList = iPubRelationService.selectPubRelationList(pubRelation);
            if (!CollectionUtils.isEmpty(jjList)) {
                String jjNo = "";
                for (PubRelation pn : jjList) {
                    CmBizSingleSJVo cm = cmBizSingleSJService.selectSjbgById(pn.getObj1Id());
                    if (cm != null) {
                        jjNo += cm.getChangeCode() + ",";
                    }
                }
                if (StringUtils.isNotEmpty(jjNo)) {
                    imfx.setCmNo(jjNo.substring(0, jjNo.length() - 1));
                }
            }
            //????????????
            pubRelation.setType(ImBizIssueConstants.TRANSFERRED_CMBIZ);
            List<PubRelation> cheetList = iPubRelationService.selectPubRelationList(pubRelation);
            if (!CollectionUtils.isEmpty(cheetList)) {
                String cheetNo = "";
                for (PubRelation pn : cheetList) {
                    CmBizSingle cmBizSingle = cmBizSingleService.selectCmBizSingleByIdById(pn.getObj1Id());
                    if (cmBizSingle != null) {
                        cheetNo += cmBizSingle.getChangeCode() + ",";
                    }
                }
                if (StringUtils.isNotEmpty(cheetNo)) {
                    imfx.setCsno(cheetNo.substring(0, cheetNo.length() - 1));
                }
            }
            reList.add(imfx);
        }
        ExcelUtil<ImBizIssuefx> util = new ExcelUtil<ImBizIssuefx>(ImBizIssuefx.class);
        return util.exportExcel(reList, "???????????????");
    }

    /**
     * ?????????????????????
     *
     * @param
     * @return zx
     */
    @PostMapping("/ywList")
    @ResponseBody
    public TableDataInfo swList(FmBiz fmBiz) {
        List<FmBiz> fmSwMbList = new ArrayList<>();
        PubRelation pubRelation = new PubRelation();
        pubRelation.setObj2Id(fmBiz.getFmId());
        pubRelation.setType(ImBizIssueConstants.TRANSFERRED_YW);
        startPage();
        List<PubRelation> puList = iPubRelationService.selectPubRelationList(pubRelation);
        if (puList.size() > 0) {
            for (PubRelation pn : puList) {
                FmBiz fmBizs = iFmBizService.selectFmBizById(pn.getObj1Id());
                if (fmBizs != null) {
                    fmBizs.getParams().put("relationId", pn.getRelationId());
                    fmSwMbList.add(fmBizs);
                }
            }
        }
        return getDataTable(fmSwMbList, puList);
    }

    /**
     * ?????????????????????
     *
     * @param
     * @return zx
     */
    @PostMapping("/runList")
    @ResponseBody
    public TableDataInfo runList(EventRun eventRun) {
        List<EventRun> runList = new ArrayList<>();
        PubRelation pubRelation = new PubRelation();
        pubRelation.setObj2Id(eventRun.getEventId());
        pubRelation.setType(ImBizIssueConstants.TRANSFERRED_EVENTRUN);
        startPage();
        List<PubRelation> puList = iPubRelationService.selectPubRelationList(pubRelation);
        if (puList.size() > 0) {
            for (PubRelation pn : puList) {
                if (StringUtils.isNotEmpty(pn.getObj1Id())) {
                    EventRun event = eventRunService.selectEventRunById(pn.getObj1Id());
                    if (event != null) {
                        event.getParams().put("relationId", pn.getRelationId());
                        runList.add(event);
                    }
                }
            }
        }
        return getDataTable(runList, puList);
    }

    /**
     * ?????????????????????
     *
     * @param
     * @return zx
     */
    @PostMapping("/jlauditlist")
    @ResponseBody
    public TableDataInfo jlauditlist(CmBizSingleSJVo cmBizSingleSJVo) {
        List<CmBizSingleSJVo> cmList = new ArrayList<>();
        PubRelation pubRelation = new PubRelation();
        pubRelation.setObj2Id(cmBizSingleSJVo.getChangeSingleId());
        pubRelation.setType(ImBizIssueConstants.TRANSFERRED_FMBIZ);
        startPage();
        List<PubRelation> puList = iPubRelationService.selectPubRelationList(pubRelation);
        if (puList.size() > 0) {
            for (PubRelation pn : puList) {
                CmBizSingleSJVo cm = cmBizSingleSJService.selectSjbgById(pn.getObj1Id());
                if (cm != null) {
                    cm.getParams().put("relationId", pn.getRelationId());
                    cmList.add(cm);
                }
            }
        }
        return getDataTable(cmList, puList);
    }

    /**
     * ???????????????????????????
     */
    @PostMapping("/CmBizListAll")
    @ResponseBody
    public TableDataInfo listAll(CmBizSingle cmBizSingle) {
        PubRelation pubRelation = new PubRelation();
        pubRelation.setObj2Id(cmBizSingle.getChangeId());
        pubRelation.setType(ImBizIssueConstants.TRANSFERRED_CMBIZ);
        startPage();
        List<PubRelation> puList = iPubRelationService.selectPubRelationList(pubRelation);
        List<CmBizSingle> list = new ArrayList<>();
        if (puList.size() > 0) {
            for (PubRelation pn : puList) {
                CmBizSingle ck = cmBizSingleService.selectCmBizSingleByIdById(pn.getObj1Id());
                if (ck != null) {
                    ck.getParams().put("relationId", pn.getRelationId());
                    list.add(ck);
                }
            }
        }

        return getDataTable(list, puList);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/gofmbiz")
    public String goSwList(String issuefxId, String fmNo, ModelMap map) {
        map.addAttribute("issuefxId", issuefxId);
        map.addAttribute("fmNo", fmNo);
        return prefix + "/fmbiz";
    }

    /**
     * ???????????????
     *
     * @return
     */
    @GetMapping("/goEventRun")
    public String goEventRun(String issuefxId, String fmjkNo, ModelMap map) {
        map.addAttribute("issuefxId", issuefxId);
        map.addAttribute("fmjkNo", fmjkNo);
        return prefix + "/eventRun";
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/goCm")
    public String goCm(String issuefxId, String cmNo, ModelMap map) {
        map.addAttribute("issuefxId", issuefxId);
        map.addAttribute("cmNo", cmNo);
        return prefix + "/jlaudit";
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @GetMapping("/goCmBiz")
    public String goCmBiz(String issuefxId, ModelMap map) {
        map.addAttribute("issuefxId", issuefxId);
        return prefix + "/assessor";
    }

    @Log(title = "???????????????", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            ImBizIssuefx issuefx = issueService.selectImBizIssuefxById(ids);

            if (issuefx != null && StringUtils.isNotEmpty(issuefx.getCurrentState()) && ImBizIssueConstants.STATUS_DAITIJIAO.equals(issuefx.getCurrentState())) {
                return toAjax(issueService.deleteImBizIssuefxByIds(ids));
            } else {
                return error("?????????????????????????????????????????????");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**
     * @param ids
     * @return
     */
    @Log(title = "???????????????", businessType = BusinessType.DELETE)
    @PostMapping("/toVoid")
    @ResponseBody
    public AjaxResult toVoid(String ids) {
        try {
            return toAjax(issueService.updateInBatchById(ids));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**
     * ???????????????
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        //???????????????
        List<PubParaValue> businessOrgs = issueService.selectIssuesourceList(DictConstants.VM_DEPT);//????????????
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        List<OgPerson> userlist = new ArrayList<>();
        List<OgPerson> buslist = new ArrayList<>();
        if (!CollectionUtils.isEmpty(businessOrgs)) {
            //???????????????
            OgPerson ogPerson = new OgPerson();
            ogPerson.setOrgname(businessOrgs.get(0).getValueDetail());
            ogPerson.setrId(ImBizIssueConstants.ROLE_YWFUHE);
            buslist = commonService.selectPersonByOrgAndRole(ogPerson);
        }
        if (!CollectionUtils.isEmpty(issueOrgs)) {
            //?????????
            OgPerson ogPerson = new OgPerson();
            ogPerson.setOrgId(issueOrgs.get(0).getOrgId());
            ogPerson.setrId(ImBizIssueConstants.ROLE_SHENGHE);
            userlist = commonService.selectPersonByOrgAndRole(ogPerson);
        }
        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        String createrName = userPerson.getpName();
        String createrPhone = userPerson.getMobilPhone();
        String bizType = "WT";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.addAttribute("userId", ShiroUtils.getUserId());
        mmap.addAttribute("userlist", userlist);
        mmap.addAttribute("buslist", buslist);
        mmap.addAttribute("issuefxNo", bizType + nowDateStr + numStr);
        mmap.addAttribute("createrName", createrName);
        mmap.addAttribute("createrPhone", createrPhone);
        mmap.addAttribute("businessOrgs", businessOrgs);
        mmap.addAttribute("issueOrgs", issueOrgs);
        mmap.addAttribute("issuefxId", IdUtils.fastSimpleUUID());
        mmap.addAttribute("issuesource", "1");//??????
        return prefix + "/add";
    }

    /**
     * ????????????????????????
     */
    @GetMapping("/ogSys/{appId}")
    public String selectOgSys(@PathVariable("appId") String appId, ModelMap mmap) {
        mmap.addAttribute("appId", appId);
        return prefix + "/ogSys";
    }

    /**
     * ????????????????????????
     */
    @PostMapping("/ogSysList/{appId}")
    @ResponseBody
    public TableDataInfo selectOgSysList(@PathVariable("appId") String appId, OgSys sys) {
        startPage();
        sys.setSysId(appId);
        List<OgSys> list = issueService.selectOgSysList(sys);
        return getDataTable(list);
    }

    /**
     * ???????????????????????????
     */
    @GetMapping("/multiusers/{putUnitDiv}")
    public String multiusers(@PathVariable("putUnitDiv") String putUnitDiv, ModelMap mmap) {
        mmap.addAttribute("putUnitDiv", putUnitDiv);
        return prefix + "/multiusers";
    }

    /**
     * ?????????????????????
     */
    @PostMapping("/selectMultiusers")
    @ResponseBody
    public TableDataInfo selectMultiusers(OgUser user) {
        startPage();
        user.setPostId(Long.valueOf(10));
        List<OgUser> list = userService.selectAllocatedListPost(user);
        return getDataTable(list);
    }

    /**
     * ????????????
     */
    @GetMapping("/reviewer/{userIds}")
    public String reviewerId(@PathVariable("userIds") String userIds, ModelMap mmap) {
        mmap.addAttribute("userIds", userIds);
        return prefix + "/reviewerUser";
    }

    /**
     * @return
     */
    @PostMapping("/selectIssueByReviewer")
    @ResponseBody
    public TableDataInfo selectIssueByReviewer(OgPerson ogPerson) {
        List<OgPerson> oglist = issueService.selectIssueByReviewer(ogPerson);
        return getDataTable(oglist);
    }

    /**
     * ????????????
     * zx
     */
    @PostMapping("/reviewerUser/{userIds}")
    @ResponseBody
    public TableDataInfo reviewerUser(@PathVariable("userIds") String userIds, OgPerson ogPerson) {
        OgPerson person1 = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        OgOrg org = deptService.selectDeptById(person1.getOrgId());
        String levelCode = org.getLevelCode();
        OgOrg org1 = new OgOrg();
        org1.setLevelCode(levelCode);
        ogPerson.setOrg(org1);
        ogPerson.setrId(ImBizIssueConstants.ROLE_JSJINGLI);
        startPage();
        List<OgPerson> list = issueService.selectMultiusers(userIds, ogPerson);
        return getDataTable(list);
    }

    /**
     * ????????????
     */
    @GetMapping("/audit/{userIds}")
    public String auditId(@PathVariable("userIds") String userIds, ModelMap mmap) {
        mmap.addAttribute("userIds", userIds);
        return prefix + "/auditUser";
    }

    /**
     * ????????????
     * zx
     */
    @PostMapping("/auditUser/{userIds}")
    @ResponseBody
    public TableDataInfo auditUser(@PathVariable("userIds") String userIds, OgPerson ogPerson) {
        OgPerson person1 = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        OgOrg org = deptService.selectDeptById(person1.getOrgId());
        String levelCode = org.getLevelCode();
        OgOrg org1 = new OgOrg();
        org1.setLevelCode(levelCode);
        ogPerson.setOrg(org1);
        ogPerson.setrId(ImBizIssueConstants.ROLE_JSJINGLI);
        startPage();
        List<OgPerson> list = issueService.selectMultiusers(userIds, ogPerson);
        return getDataTable(list);
    }

    /**
     * ????????????
     **/
    @GetMapping("/dataCenter")
    public String dataCenter() {

        return prefixinside + "/dataCenterUser";
    }

    /**
     * ????????????????????????
     *
     * @param ogPerson
     * @return
     */
    @PostMapping("/dataCenterUser")
    @ResponseBody
    public TableDataInfo dataCenterUserId(OgPerson ogPerson) {
        ogPerson.setPosition(ImBizIssueConstants.POST_DATACENTER);
        startPage();
        List<OgPerson> list = issueService.selectMultiusers(ogPerson);
        return getDataTable(list);
    }

    /**
     * ????????????
     * zx
     */
    @Log(title = "???????????????", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(@Validated ImBizIssuefx issue) {
        issue.setCreaterId(ShiroUtils.getUserId());
        if (StringUtils.isNotNull(issue.getReporttime())) {
            issue.setReporttime(DateUtils.handleTimeYYYYMMDDHHMMSS(issue.getReporttime()));
        }
        //????????????
        issue.setCurrentState("0");
        //??????????????????MULTICOUNT
        issue.setMulticount(0);
        return toAjax(issueService.insertIssue(issue));
    }

    /**
     * ???????????????
     */
    @GetMapping("/edit/{issuefxId}")
    public String edit(@PathVariable("issuefxId") String issuefxId, ModelMap mmap) {
        mmap = issueService.view(issuefxId, mmap);
        return prefix + "/edit";
    }

    /**
     * ???????????????
     */
    @GetMapping("/view/{issuefxId}")
    public String view(@PathVariable("issuefxId") String issuefxId, ModelMap mmap) {
        mmap = issueService.view(issuefxId, mmap);
        mmap.addAttribute("issuefxId", issuefxId);
        mmap.addAttribute("type", "view");
        return prefix + "/view";
    }

    /**
     * ??????????????????
     * zx
     */
    @Log(title = "???????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult editSave(@Validated ImBizIssuefx issue) {
        return toAjax(issueService.updateIssue(issue));
    }

    /**
     * ??????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(ImBizIssuefx issue) {
        return toAjax(issueService.changeStatus(issue));
    }

    /**
     * ????????????id??????????????????
     */
    @PostMapping("/orgPer")
    @ResponseBody
    public List selectOgPersonList(String orgId, String type) {
        OgPerson ogPerson = new OgPerson();
        if ("-1".equals(type)) {
            ogPerson.setrId(ImBizIssueConstants.ROLE_SHENGHE);
            ogPerson.setOrgId(orgId);
        } else {
            String name = iPubParaValueService.selectPubParaValueByNameValue(DictConstants.VM_DEPT, orgId);
            ogPerson.setrId(ImBizIssueConstants.ROLE_YWFUHE);
            ogPerson.setOrgname(name);
        }
        List<OgPerson> list = commonService.selectPersonByOrgAndRole(ogPerson);
        return list;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/selectOneApplication")
    public String selectOneApplication() {
        return prefix + "/selectOneApplication";
    }

    /**
     * flag ??????
     * bizId ??????ID
     * ??????????????????????????????
     */
    @GetMapping("/add/{flag}/{bizId}")
    public String add(ModelMap mmap, @PathVariable("flag") String flag, @PathVariable("bizId") String bizId) {
        //???????????????
        List<PubParaValue> businessOrgs = issueService.selectIssuesourceList(DictConstants.VM_DEPT);//????????????
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        List<OgPerson> userlist = new ArrayList<>();
        List<OgPerson> buslist = new ArrayList<>();
        if (null != businessOrgs && businessOrgs.size() > 0) {
            OgPerson ogPerson = new OgPerson();
            ogPerson.setOrgname(businessOrgs.get(0).getValueDetail());
            ogPerson.setrId(ImBizIssueConstants.ROLE_YWFUHE);
            buslist = commonService.selectPersonByOrgAndRole(ogPerson);
        }
        if (null != issueOrgs && issueOrgs.size() > 0) {
            OgPerson ogPerson = new OgPerson();
            ogPerson.setOrgId(issueOrgs.get(0).getOrgId());
            ogPerson.setrId(ImBizIssueConstants.ROLE_SHENGHE);
            userlist = commonService.selectPersonByOrgAndRole(ogPerson);
        }

        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());

        List<OgPerson> userList = issueService.selectMultiusers("0", userPerson);
        String createrName = "";
        if (null != userList && userList.size() > 0) {
            createrName = userList.get(0).getpName();
        }
        String bizType = "WT";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        //??????flag????????????
        if ("2".equals(flag)) {//???????????????
            FmBiz fmBiz = iFmBizService.selectFmBizById(bizId);
            String createrPhone = userPerson.getMobilPhone();
            String createrNamesj = userPerson.getpName();

            mmap.addAttribute("createrPhone", createrPhone);
            mmap.addAttribute("fmNo", fmBiz.getFaultNo());//??????
            mmap.addAttribute("fmId", fmBiz.getFmId());//?????????ID
            mmap.addAttribute("issuefxName", fmBiz.getFaultDecriptSummary());//??????
            mmap.addAttribute("sysId", fmBiz.getSysid());//??????ID
            mmap.addAttribute("sysName", sysApplicationManagerService.selectOgSysBySysId(fmBiz.getSysid()).getCaption());//????????????
            mmap.addAttribute("issuefxText", fmBiz.getFaultDecriptDetail());//??????
            mmap.addAttribute("dealDescription", fmBiz.getFaultDecriptDetail());//????????????
            mmap.addAttribute("issuesource", flag);//??????
            mmap.addAttribute("createrName", createrNamesj);//???????????????

        }
        if ("3".equals(flag)) {//???????????????
            /*
            ??????????????????????????????
             */
        }
        if ("5".equals(flag)) {//???????????????
            CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(bizId);

            String createrPhone = userPerson.getMobilPhone();
            String createrNamesj = userPerson.getpName();

            mmap.addAttribute("createrPhone", createrPhone);
            mmap.addAttribute("issuesource", flag);//??????
            mmap.addAttribute("issuefxName", cmBizSingleSJVo.getFmTitle());//??????
            mmap.addAttribute("createrName", createrNamesj);//???????????????
            mmap.addAttribute("cmNo", cmBizSingleSJVo.getChangeSingleId());//???????????????id

        }
      /*  if ("4".equals(flag)) {//??????????????????
            CheckSheet cs = checkSheetService.selectCheckSheetById(bizId);
            mmap.addAttribute("csNo", cs.getCsNo());//??????
            mmap.addAttribute("csId", cs.getCsId());//id
            mmap.addAttribute("sysId", cs.getSysid());//??????ID
            mmap.addAttribute("sysName", sysApplicationManagerService.selectOgSysBySysId(cs.getSysid()).getCaption());//????????????
            mmap.addAttribute("csNo", cs.getHiddenText());//????????????
            mmap.addAttribute("issuefxImpact", cs.getAffectBusiness());//????????????
            mmap.addAttribute("dealDescription", cs.getRectProp());//????????????
            mmap.addAttribute("issuesource", flag);//??????
        }*/
        mmap.addAttribute("userId", ShiroUtils.getUserId());
        mmap.addAttribute("userlist", userlist);
        mmap.addAttribute("buslist", buslist);
        mmap.addAttribute("issuefxNo", bizType + nowDateStr + numStr);
//        mmap.addAttribute("createrName", createrName);
        mmap.addAttribute("businessOrgs", businessOrgs);
        mmap.addAttribute("issueOrgs", issueOrgs);
        mmap.addAttribute("issuefxId", IdUtils.fastSimpleUUID());

        return prefix + "/add";
    }

    /**
     * ????????????ID??????????????????
     */
    @PostMapping("/selectFmKindBySysidForWTD")
    @ResponseBody
    public TableDataInfo selectFmKindBySysid(String category, String parentId) {
        KnowledgeTitle kdt = new KnowledgeTitle();
        kdt.setCategory(category);
        kdt.setParentId(parentId);
        kdt.setEventType("2");
        kdt.setStatus("0");
        List<KnowledgeTitle> list = knowledgeTitleService.selectKnowledgeTitleList(kdt);
        return getDataTable(list);
    }

    /**
     * ????????????????????? ??????????????????
     *
     * @param sysId
     * @return
     */
    @GetMapping("/listAllForEvent/{sysId}")
    public String listAllForEventHtml(@PathVariable("sysId") String sysId, ModelMap mmap) {
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        mmap.addAttribute("issueOrgs", issueOrgs);
        String sysName = "";
        if (StringUtils.isNotEmpty(sysId)) {
            OgSys ogSys = sysApplicationManagerService.selectOgSysBySysId(sysId);
            if (ogSys != null) {
                sysName = ogSys.getCaption();
            }
        }
        mmap.addAttribute("sysName", sysName);//????????????
        return prefix + "/issueForEventSheet";
    }

    /**
     * ?????????????????????list ??????????????????
     *
     * @param issue
     * @return
     */
    @PostMapping("/listAllForEventSheet")
    @ResponseBody
    public TableDataInfo listAllForEventSheet(ImBizIssuefx issue) {
        startPage();
        if (StringUtils.isNotEmpty(issue.getCurrentState())) {
            String[] state = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
            issue.getParams().put("states", statecollection);
        }
        List<ImBizIssuefx> list = issueService.listAllForEventSheet(issue);
        return getDataTable(list);
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @GetMapping("/classIteoator")
    public String classIteoator() {
        return prefixClass + "/classIteoatorForWTD";
    }

    /**
     * ???????????????
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(KnowledgeTitle knowledgeTitle) {
        knowledgeTitle.setEventType("2");
        List<Ztree> ztrees = issueService.selectTitleTree(knowledgeTitle);
        return ztrees;
    }

    /**
     * ??????????????????
     */
    @PostMapping("/classList")
    @ResponseBody
    public TableDataInfo classList(KnowledgeTitle knowledgeTitle) {
        if (StringUtils.isNotEmpty(knowledgeTitle.getParentId())) {
            KnowledgeTitle parent = knowledgeTitleService.selectKnowledgeTitleById(knowledgeTitle.getParentId());
            KnowledgeTitle kdt = new KnowledgeTitle();
            kdt.setParentId(knowledgeTitle.getParentId());
            kdt.setStatus(knowledgeTitle.getStatus());
            kdt.setName(knowledgeTitle.getName());
            kdt.setCategory(knowledgeTitle.getCategory());
            kdt.setEventType("2");
            startPage();
            List<KnowledgeTitle> list = knowledgeTitleService.selectKnowledgeTitleList(kdt);
            return getDataTable(list);
        } else {
            throw new BusinessException("????????????????????????????????????????????????");
        }
    }

    /**
     * ????????????
     */
    @GetMapping("/addClass/{id}")
    public String addClass(@PathVariable("id") String id, ModelMap mmap) {

        KnowledgeTitle knowledgeTitle = knowledgeTitleService.selectKnowledgeTitleById(id);

        if ("3".equals(knowledgeTitle.getCategory())) {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        mmap.put("parentName", knowledgeTitle.getName());
        mmap.put("parentId", knowledgeTitle.getId());
        mmap.put("knowledgeTitle", knowledgeTitle);
        return prefixClass + "/addClass";
    }

    /**
     * ???????????????????????????
     */
    @Log(title = "??????", businessType = BusinessType.INSERT)
    @PostMapping("/addClassData")
    @ResponseBody
    public AjaxResult addClassData(KnowledgeTitle knowledgeTitle) {
        knowledgeTitle.setId(UUID.getUUIDStr());
        knowledgeTitle.setEventType("2");
        knowledgeTitle.setCreateId(ShiroUtils.getOgUser().getpId());
        knowledgeTitle.setCreateTime(DateUtils.getTime());
        knowledgeTitle.setStatus("0");
        return toAjax(knowledgeTitleService.insertKnowledgeTitle(knowledgeTitle));
    }

    /**
     * ????????????
     */
    @GetMapping("/editClass/{id}")
    public String editClass(@PathVariable("id") String id, ModelMap mmap) {
        KnowledgeTitle knowledgeTitle = knowledgeTitleService.selectKnowledgeTitleById(id);
        mmap.put("parentName", knowledgeTitle.getParentName());
        mmap.put("parentId", knowledgeTitle.getParentId());
        mmap.put("knowledgeTitle", knowledgeTitle);
        return prefixClass + "/editClass";
    }

    /**
     * ??????????????????
     */
    @Log(title = "??????", businessType = BusinessType.UPDATE)
    @PostMapping("/editClassData")
    @ResponseBody
    public AjaxResult editSave(KnowledgeTitle knowledgeTitle) {
        KnowledgeTitle knowledgeTitle1 = new KnowledgeTitle();
        knowledgeTitle1.setId(knowledgeTitle.getId());
        knowledgeTitle1.setName(knowledgeTitle.getName());
        knowledgeTitle1.setStatus(knowledgeTitle.getStatus());
        return toAjax(knowledgeTitleService.updateKnowledgeTitle(knowledgeTitle1));
    }

    /**
     * ????????????
     */
    @GetMapping("/addZeroType")
    public String addSys(ModelMap mmap) {
        return prefixClass + "/addZeroType";
    }

    @GetMapping("/relationFmbiz")
    public String relationFmbizList(ModelMap mmap) {
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        mmap.addAttribute("issueOrgs", issueOrgs);

        return prefix + "/issueRelationFmbiz";
    }

    /**
     * ??????????????????????????????
     *
     * @param issue
     * @return
     */

    @PostMapping("/relationFmbizList")
    @ResponseBody
    public TableDataInfo relationFmbizList(ImBizIssuefx issue) {
        startPage();
        if (StringUtils.isNotEmpty(issue.getCurrentState())) {
            String[] state = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
            issue.getParams().put("states", statecollection);
        }
        List<ImBizIssuefx> list = issueService.selectRelationFmbizList(issue);
        return getDataTable(list);
    }

    /**
     * ?????????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/exportWTD")
    @ResponseBody
    public AjaxResult exportWTD(ImBizIssuefx issue) {

        String isCurrentPage = (String) issue.getParams().get("currentPage");
        if (StringUtils.isNotEmpty(issue.getCurrentState())) {
            String[] state = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
//            if (statecollection.contains(ImBizIssueConstants.STATUS_YEWUFUHE)) {
//                issue.setHanguptask(ImBizIssueConstants.STATUS_YEWUFUHE);
//            }
            issue.getParams().put("states", statecollection);
        }
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<ImBizIssuefx> list = issueService.selectRelationFmbizList(issue);
        ExcelUtil<ImBizIssuefx> util = new ExcelUtil<ImBizIssuefx>(ImBizIssuefx.class);
        return util.exportExcel(list, "???????????????");
    }

    /**
     * ???????????????
     */
    @GetMapping("/selectRelationFmbiz/{issuefxId}")
    public String selectRelationFmbiz(@PathVariable("issuefxId") String issuefxId, ModelMap mmap) {
        mmap.addAttribute("issuefxNo", "");
        ImBizIssuefx imBizIssuefx = issueService.selectImBizIssuefxById(issuefxId);
        if (imBizIssuefx != null) {
            mmap.addAttribute("issuefxNo", imBizIssuefx.getIssuefxNo());
        }
        return prefix + "/relationFmbizList";
    }

    @GetMapping("/query")
    public String query(ModelMap mmap) {
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        mmap.addAttribute("issueOrgs", issueOrgs);
        return prefix + "/issue_query";
    }

    /**
     * ?????????????????????
     *
     * @param issue
     * @return
     */

    @PostMapping("/selectMyList")
    @ResponseBody
    public TableDataInfo selectMyList(ImBizIssuefx issue) {
        ImBizIssuefx is = null;
        if (StringUtils.isNotEmpty(issue.getCurrentState())) {
            String[] state = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
            issue.getParams().put("states", statecollection);
        }
        PubFlowLog pub = new PubFlowLog();
        pub.setPerformerId(ShiroUtils.getUserId());
        pub.setLogType("PMYY");
        List<PubFlowLog> pgList = pubFlowLogService.selectPubFlowLogList(pub);
        List<ImBizIssuefx> listImBizIssuefx = new ArrayList<>();
        Set<String> idList = new HashSet<>();
        for (PubFlowLog pb : pgList) {
            if (!idList.contains(pb.getBizId())) {
                String taskName = pb.getTaskName();
                if (!"??????".equals(taskName) && !"????????????".equals(taskName)) {
                    is = issue;
                    is.setIssuefxId(pb.getBizId());
                    is.setCreaterId("");
                    startPage();
                    List<ImBizIssuefx> imBizIssuefxList = issueService.selectIssueList(is);
                    if (imBizIssuefxList.size() > 0) {
                        imBizIssuefxList.get(0).getParams().put("performerTime", pb.getPerformerTime());
                    }
                    listImBizIssuefx.addAll(imBizIssuefxList);
                    idList.add(pb.getBizId());
                }
            }
        }
        return getDataTable_ideal(listImBizIssuefx);
    }

    /**
     * ?????????????????????
     *
     * @param issue
     * @return
     */

    @PostMapping("/myExport")
    @ResponseBody
    public AjaxResult myExport(ImBizIssuefx issue) {
        ImBizIssuefx is = null;
        if (StringUtils.isNotEmpty(issue.getCurrentState())) {
            String[] state = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
            issue.getParams().put("states", statecollection);
        }
        String isCurrentPage = (String) issue.getParams().get("currentPage");
        PubFlowLog pub = new PubFlowLog();
        pub.setPerformerId(ShiroUtils.getUserId());
        pub.setLogType("PMYY");
        List<PubFlowLog> pgList = pubFlowLogService.selectPubFlowLogList(pub);
        List<ImBizIssuefx> listImBizIssuefx = new ArrayList<>();
        Set<String> idList = new HashSet<>();
        for (PubFlowLog pb : pgList) {
            if (!idList.contains(pb.getBizId())) {
                String taskName = pb.getTaskName();
                if (!"??????".equals(taskName) && !"????????????".equals(taskName)) {
                    is = issue;
                    is.setIssuefxId(pb.getBizId());
                    is.setCreaterId("");
                    List<ImBizIssuefx> imBizIssuefxList = issueService.selectIssueList(is);
                    if (imBizIssuefxList.size() > 0) {
                        imBizIssuefxList.get(0).getParams().put("performerTime", pb.getPerformerTime());
                    }
                    listImBizIssuefx.addAll(imBizIssuefxList);
                    idList.add(pb.getBizId());
                }
            }
        }
        OgPerson person = new OgPerson();
        person.setrId(ImBizIssueConstants.ROLE_JSJINGLI);
        List<OgPerson> listAudit = commonService.selectPersonByOrgAndRole(person);
        Map<String, String> mp = new HashMap<>();
        for (OgPerson pn : listAudit) {
            mp.put(pn.getpId(), pn.getpName());
        }
        listImBizIssuefx.forEach(p ->p.setAuditId(mp.get(p.getAuditId())));
        if ("currentPage".equals(isCurrentPage)) {
            PageDomain pageDomain = TableSupport.buildPageRequest();
            Integer pageNum = pageDomain.getPageNum();
            Integer pageSize = pageDomain.getPageSize();
            List<ImBizIssuefx> newList;
            if (pageNum * pageSize > listImBizIssuefx.size()) {
                if (pageSize > listImBizIssuefx.size()) {
                    newList = listImBizIssuefx;
                } else {
                    if ((pageNum - 1) * pageSize > listImBizIssuefx.size()) {
                        int freeV = listImBizIssuefx.size() % pageSize;
                        newList = listImBizIssuefx.subList(listImBizIssuefx.size() - freeV, listImBizIssuefx.size());
                    } else {
                        newList = listImBizIssuefx.subList((pageNum - 1) * pageSize, listImBizIssuefx.size());
                    }
                }
            } else {
                newList = listImBizIssuefx.subList((pageNum - 1) * pageSize, pageNum * pageSize);
            }
            ExcelUtil<ImBizIssuefx> util = new ExcelUtil<ImBizIssuefx>(ImBizIssuefx.class);
            return util.exportExcel(newList, "???????????????");
        } else {
            PageDomain pageDomain = TableSupport.buildPageRequest();
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
            ExcelUtil<ImBizIssuefx> util = new ExcelUtil<ImBizIssuefx>(ImBizIssuefx.class);
            return util.exportExcel(listImBizIssuefx, "???????????????");
        }
    }

}
