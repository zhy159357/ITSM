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
 * 问题单新建
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
     * 我的问题单列表
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
            //我创建的
            if (ImBizIssueConstants.MYFLAG_CREAT.equals(myFlag)) {
                issue.setCreaterId(ShiroUtils.getOgUser().getUserId());
            }
            //我处理的
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
     * 所有问题单列表
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
     * 导出应用问题单
     */
    @Log(title = "应用问题单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ImBizIssuefx issue) {
        /*//问题分类 issueFenlei
        Map<String,String> issueFenlei=switchList("WT_FENLEI");
        //问题类型
        Map<String,String> issueType=switchList("WT_TYPE");
        //问题来源
        Map<String,String> issuesource=switchList("WT_SOUCE");
        //问题状态
        Map<String,String> status=switchList("issue_status");
        //问题等级 seriousLev
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
            //关联关系
            PubRelation pubRelation = new PubRelation();
            pubRelation.setObj2Id(imfx.getIssuefxId());
            //业务事件单
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
            //监控事件单
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
            //数据变更
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
            //资源变更
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
        return util.exportExcel(reList, "应用问题单");
    }

    /**
     * 业务事件单查询
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
     * 监控事件单查询
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
     * 数据变更事件单
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
     * 查询资源变更单列表
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
     * 跳转事务列表
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
     * 跳运行列表
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
     * 跳转数据变更
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
     * 跳转资源变更单列表
     *
     * @return
     */
    @GetMapping("/goCmBiz")
    public String goCmBiz(String issuefxId, ModelMap map) {
        map.addAttribute("issuefxId", issuefxId);
        return prefix + "/assessor";
    }

    @Log(title = "删除问题单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            ImBizIssuefx issuefx = issueService.selectImBizIssuefxById(ids);

            if (issuefx != null && StringUtils.isNotEmpty(issuefx.getCurrentState()) && ImBizIssueConstants.STATUS_DAITIJIAO.equals(issuefx.getCurrentState())) {
                return toAjax(issueService.deleteImBizIssuefxByIds(ids));
            } else {
                return error("只有待提交状态问题单可以删除！");
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
    @Log(title = "作废问题单", businessType = BusinessType.DELETE)
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
     * 新增问题单
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        //获取字典项
        List<PubParaValue> businessOrgs = issueService.selectIssuesourceList(DictConstants.VM_DEPT);//业务部门
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        List<OgPerson> userlist = new ArrayList<>();
        List<OgPerson> buslist = new ArrayList<>();
        if (!CollectionUtils.isEmpty(businessOrgs)) {
            //业务复核人
            OgPerson ogPerson = new OgPerson();
            ogPerson.setOrgname(businessOrgs.get(0).getValueDetail());
            ogPerson.setrId(ImBizIssueConstants.ROLE_YWFUHE);
            buslist = commonService.selectPersonByOrgAndRole(ogPerson);
        }
        if (!CollectionUtils.isEmpty(issueOrgs)) {
            //审核人
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
        mmap.addAttribute("issuesource", "1");//来源
        return prefix + "/add";
    }

    /**
     * 查询所属应用系统
     */
    @GetMapping("/ogSys/{appId}")
    public String selectOgSys(@PathVariable("appId") String appId, ModelMap mmap) {
        mmap.addAttribute("appId", appId);
        return prefix + "/ogSys";
    }

    /**
     * 查询所属应用系统
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
     * 跳转协同评估人页面
     */
    @GetMapping("/multiusers/{putUnitDiv}")
    public String multiusers(@PathVariable("putUnitDiv") String putUnitDiv, ModelMap mmap) {
        mmap.addAttribute("putUnitDiv", putUnitDiv);
        return prefix + "/multiusers";
    }

    /**
     * 查询协同评估人
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
     * 技术经理
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
     * 技术经理
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
     * 技术经理
     */
    @GetMapping("/audit/{userIds}")
    public String auditId(@PathVariable("userIds") String userIds, ModelMap mmap) {
        mmap.addAttribute("userIds", userIds);
        return prefix + "/auditUser";
    }

    /**
     * 技术经理
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
     * 技术经理
     **/
    @GetMapping("/dataCenter")
    public String dataCenter() {

        return prefixinside + "/dataCenterUser";
    }

    /**
     * 数据中心人员查询
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
     * 新增保存
     * zx
     */
    @Log(title = "保存问题单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult addSave(@Validated ImBizIssuefx issue) {
        issue.setCreaterId(ShiroUtils.getUserId());
        if (StringUtils.isNotNull(issue.getReporttime())) {
            issue.setReporttime(DateUtils.handleTimeYYYYMMDDHHMMSS(issue.getReporttime()));
        }
        //初始状态
        issue.setCurrentState("0");
        //初始转发次数MULTICOUNT
        issue.setMulticount(0);
        return toAjax(issueService.insertIssue(issue));
    }

    /**
     * 修改问题单
     */
    @GetMapping("/edit/{issuefxId}")
    public String edit(@PathVariable("issuefxId") String issuefxId, ModelMap mmap) {
        mmap = issueService.view(issuefxId, mmap);
        return prefix + "/edit";
    }

    /**
     * 查看问题单
     */
    @GetMapping("/view/{issuefxId}")
    public String view(@PathVariable("issuefxId") String issuefxId, ModelMap mmap) {
        mmap = issueService.view(issuefxId, mmap);
        mmap.addAttribute("issuefxId", issuefxId);
        mmap.addAttribute("type", "view");
        return prefix + "/view";
    }

    /**
     * 修改保存问题
     * zx
     */
    @Log(title = "修改问题单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult editSave(@Validated ImBizIssuefx issue) {
        return toAjax(issueService.updateIssue(issue));
    }

    /**
     * 账号状态修改
     */
    @Log(title = "问题单修改", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(ImBizIssuefx issue) {
        return toAjax(issueService.changeStatus(issue));
    }

    /**
     * 根据系统id查询人员信息
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
     * 系统选择页面
     *
     * @return
     */
    @GetMapping("/selectOneApplication")
    public String selectOneApplication() {
        return prefix + "/selectOneApplication";
    }

    /**
     * flag 来源
     * bizId 工单ID
     * 其他工单转问题单专用
     */
    @GetMapping("/add/{flag}/{bizId}")
    public String add(ModelMap mmap, @PathVariable("flag") String flag, @PathVariable("bizId") String bizId) {
        //获取字典项
        List<PubParaValue> businessOrgs = issueService.selectIssuesourceList(DictConstants.VM_DEPT);//业务部门
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
        //根据flag决定调用
        if ("2".equals(flag)) {//业务事件单
            FmBiz fmBiz = iFmBizService.selectFmBizById(bizId);
            String createrPhone = userPerson.getMobilPhone();
            String createrNamesj = userPerson.getpName();

            mmap.addAttribute("createrPhone", createrPhone);
            mmap.addAttribute("fmNo", fmBiz.getFaultNo());//单号
            mmap.addAttribute("fmId", fmBiz.getFmId());//事件单ID
            mmap.addAttribute("issuefxName", fmBiz.getFaultDecriptSummary());//标题
            mmap.addAttribute("sysId", fmBiz.getSysid());//系统ID
            mmap.addAttribute("sysName", sysApplicationManagerService.selectOgSysBySysId(fmBiz.getSysid()).getCaption());//系统名称
            mmap.addAttribute("issuefxText", fmBiz.getFaultDecriptDetail());//描述
            mmap.addAttribute("dealDescription", fmBiz.getFaultDecriptDetail());//处理描述
            mmap.addAttribute("issuesource", flag);//来源
            mmap.addAttribute("createrName", createrNamesj);//问题发现人

        }
        if ("3".equals(flag)) {//运行事件单
            /*
            待确认需要回显的字段
             */
        }
        if ("5".equals(flag)) {//数据事件单
            CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(bizId);

            String createrPhone = userPerson.getMobilPhone();
            String createrNamesj = userPerson.getpName();

            mmap.addAttribute("createrPhone", createrPhone);
            mmap.addAttribute("issuesource", flag);//来源
            mmap.addAttribute("issuefxName", cmBizSingleSJVo.getFmTitle());//标题
            mmap.addAttribute("createrName", createrNamesj);//问题发现人
            mmap.addAttribute("cmNo", cmBizSingleSJVo.getChangeSingleId());//数据变更单id

        }
      /*  if ("4".equals(flag)) {//隐患排查单转
            CheckSheet cs = checkSheetService.selectCheckSheetById(bizId);
            mmap.addAttribute("csNo", cs.getCsNo());//单号
            mmap.addAttribute("csId", cs.getCsId());//id
            mmap.addAttribute("sysId", cs.getSysid());//系统ID
            mmap.addAttribute("sysName", sysApplicationManagerService.selectOgSysBySysId(cs.getSysid()).getCaption());//系统名称
            mmap.addAttribute("csNo", cs.getHiddenText());//问题描述
            mmap.addAttribute("issuefxImpact", cs.getAffectBusiness());//影响描述
            mmap.addAttribute("dealDescription", cs.getRectProp());//处理建议
            mmap.addAttribute("issuesource", flag);//来源
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
     * 根据系统ID查询分类信息
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
     * 所有问题单列表 关联事件单用
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
        mmap.addAttribute("sysName", sysName);//系统名称
        return prefix + "/issueForEventSheet";
    }

    /**
     * 所有问题单列表list 关联事件单用
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
     * 问题单分类维护页面
     *
     * @return
     */
    @GetMapping("/classIteoator")
    public String classIteoator() {
        return prefixClass + "/classIteoatorForWTD";
    }

    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(KnowledgeTitle knowledgeTitle) {
        knowledgeTitle.setEventType("2");
        List<Ztree> ztrees = issueService.selectTitleTree(knowledgeTitle);
        return ztrees;
    }

    /**
     * 查询分类列表
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
            throw new BusinessException("请选则左侧系统后再点击查询按钮。");
        }
    }

    /**
     * 新增分类
     */
    @GetMapping("/addClass/{id}")
    public String addClass(@PathVariable("id") String id, ModelMap mmap) {

        KnowledgeTitle knowledgeTitle = knowledgeTitleService.selectKnowledgeTitleById(id);

        if ("3".equals(knowledgeTitle.getCategory())) {
            throw new BusinessException("问题单三级分类为最后一级，无法继续向下添加。");
        }
        mmap.put("parentName", knowledgeTitle.getName());
        mmap.put("parentId", knowledgeTitle.getId());
        mmap.put("knowledgeTitle", knowledgeTitle);
        return prefixClass + "/addClass";
    }

    /**
     * 新增保存问题单分类
     */
    @Log(title = "标题", businessType = BusinessType.INSERT)
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
     * 修改分类
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
     * 修改保存分类
     */
    @Log(title = "类别", businessType = BusinessType.UPDATE)
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
     * 新增标题
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
     * 问题单关联事件单列表
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
     * 导出应用问题单
     */
    @Log(title = "应用问题单", businessType = BusinessType.EXPORT)
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
        return util.exportExcel(list, "应用问题单");
    }

    /**
     * 查看问题单
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
     * 我的问题单列表
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
                if (!"修改".equals(taskName) && !"撤销流程".equals(taskName)) {
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
     * 我的问题单列表
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
                if (!"修改".equals(taskName) && !"撤销流程".equals(taskName)) {
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
            return util.exportExcel(newList, "应用问题单");
        } else {
            PageDomain pageDomain = TableSupport.buildPageRequest();
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
            ExcelUtil<ImBizIssuefx> util = new ExcelUtil<ImBizIssuefx>(ImBizIssuefx.class);
            return util.exportExcel(listImBizIssuefx, "应用问题单");
        }
    }

}
