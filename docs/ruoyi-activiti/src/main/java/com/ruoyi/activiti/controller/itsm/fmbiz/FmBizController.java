package com.ruoyi.activiti.controller.itsm.fmbiz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.activiti.domain.CmBizSingleSJ;
import com.ruoyi.activiti.domain.FmBizJJAppr;
import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.domain.TelBiz;
import com.ruoyi.activiti.domain.TelFlowLog;
import com.ruoyi.activiti.mapper.CmBizSingleSJMapper;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.CmBizSingleSJService;
import com.ruoyi.activiti.service.CustInfoService;
import com.ruoyi.activiti.service.IFmBizScriptService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IFmbizAndIssueService;
import com.ruoyi.activiti.service.IImBizIssuefxService;
import com.ruoyi.activiti.service.IOgPhoneService;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.activiti.service.ITelFlowLogService;
import com.ruoyi.activiti.service.impl.FmBizOvertimeServiceImpl;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.CustInfo;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmBizCDealerD;
import com.ruoyi.common.core.domain.entity.FmBizScript;
import com.ruoyi.common.core.domain.entity.FmSysDTime;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.api.KnowledgeApi;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import com.ruoyi.es.service.KnowledgeContentService;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;

@Controller
@RequestMapping("/fmbiz")
public class FmBizController extends BaseController {
    /**
     * ???????????????????????????
     */
    private String prefix_public = "fmbiz";

    @Autowired
    private IFmBizService fmBizService;
    @Autowired
    private IIdGeneratorService idGeneratorService;
    @Autowired
    ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    IOgPersonService iOgPersonService;
    @Autowired
    ISysDeptService iSysDeptService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private ISysWorkService ISysWorkService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private IPubFlowLogService iPubFlowLogService;
    @Autowired
    private CmBizSingleSJMapper cmBizSingleSJMapper;
    @Autowired
    private IPubRelationService iPubRelationService;
    @Autowired
    private IImBizIssuefxService iImBizIssuefxService;
    @Autowired
    private CmBizSingleSJService cmBizSingleSJService;
    @Autowired
    private IFmBizScriptService fmBizScriptService;
    @Autowired(required=false)
    private KnowledgeTitleService knowledgeTitleService;
    @Autowired(required=false)
    private KnowledgeContentService knowledgeContentService;
    @Autowired(required=false)
    private KnowledgeApi knowledgeApi;
    @Autowired
    private IOgPhoneService iOgPhoneService;
    @Autowired
    private ITelFlowLogService iTelFlowLogService;
    @Autowired
    private IFmbizAndIssueService iFmbizAndIssueService;
    @Autowired
    private CustInfoService custInfoService;
    @Autowired(required=false)
    KnowledgeBusinessContentService businessContentService;

    @Value("${Antifraud.url}")
    private String AntifraudUrl;

    @Value("${pagehelper.helperDialect}")
    private String dataSourceType;

    @Value("${cntxtag.enabled}")
    private boolean menuEnable;

    /**
     * ???????????????????????????
     *
     * @return
     */
    @GetMapping("/fmbiz")
    public String publicApplyPage() {
        return prefix_public + "/fmbiz";
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @GetMapping("/passign")
    public String passignPage() {
        return prefix_public + "/passign";
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @GetMapping("/receive")
    public String receivePage() {
        return prefix_public + "/receive";
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @GetMapping("/deal")
    public String dealPage() {
        return prefix_public + "/deal";
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @GetMapping("/close")
    public String closePage() {
        return prefix_public + "/close";
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @GetMapping("/view")
    public String FmBizPage() {
        return prefix_public + "/view";
    }

    /**
     * ??????????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/searchAutoFmBiz")
    public String searchAutoFmBiz() {
        return prefix_public + "/serachAutoFmbiz";
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @GetMapping("/groupAnalysis")
    public String groupAnalysisList() {
        return prefix_public + "/group/groupAnalysis";
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @GetMapping("/groupHandle")
    public String groupHandleList() {
        return prefix_public + "/group/groupHandle";
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @GetMapping("/fmbizJJ")
    public String fmbizJJ() {
        return prefix_public + "/appr/fmbizJJ";
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @GetMapping("/DealerDCount")
    public String DealerDCount() {
        return prefix_public + "/appr/fmbizCount";
    }

    /**
     * ???????????????????????????
     */
    @PostMapping("/userlist")
    @ResponseBody
    public TableDataInfo list(FmBiz fmBiz) throws ParseException {
        startPage();
        OgUser u = ShiroUtils.getOgUser();
        if (fmBiz.getParams().containsKey("myFlag")) {
            if ("2".equals(fmBiz.getParams().get("myFlag").toString())) {//????????????
                fmBiz.setParticipatorIds(u.getpId());//???????????????
            } else {
                fmBiz.setCreateId(u.getpId());//??????????????????
            }
        } else {
            fmBiz.setCreateId(u.getpId());//??????????????????
        }
        String startCreatTime = fmBiz.getParams().get("startCreatTime").toString();
        String endCreatTime = fmBiz.getParams().get("endCreatTime").toString();
        String startDealTime = fmBiz.getParams().get("startDealTime").toString();
        String endDealTime = fmBiz.getParams().get("endDealTime").toString();

        if (!"".equals(startCreatTime) && null != startCreatTime) {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startCreatTime);
            String d1 = new SimpleDateFormat("yyyyMMdd").format(date1);
            fmBiz.getParams().put("startCreatTime", d1 + "000000");
        }

        if (!"".equals(endCreatTime) && null != endCreatTime) {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endCreatTime);
            String d2 = new SimpleDateFormat("yyyyMMdd").format(date2);
            fmBiz.getParams().put("endCreatTime", d2 + "240000");
        }
        if (!"".equals(startDealTime) && null != startDealTime) {
            Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(startDealTime);
            String d3 = new SimpleDateFormat("yyyyMMdd").format(date3);
            fmBiz.getParams().put("startDealTime", d3 + "000000");
        }

        if (!"".equals(endDealTime) && null != endDealTime) {
            Date date4 = new SimpleDateFormat("yyyy-MM-dd").parse(endDealTime);
            String d4 = new SimpleDateFormat("yyyyMMdd").format(date4);
            fmBiz.getParams().put("endDealTime", d4 + "240000");
        }
        List<FmBiz> list = fmBizService.selectMylist(fmBiz);
        return getDataTable(list);
    }

    /**
     * ????????????????????????
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        //????????????????????????????????????????????????????????????
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = iOgPersonService.selectOgPersonEvoById(pId);
        OgOrg org = iSysDeptService.selectDeptById(person.getOrgId());
        mmap.put("occurrenceOrgName", org.getOrgName());
        mmap.put("faultSource", "01");
        mmap.put("occurrenceOrgId", org.getOrgId());
        mmap.put("loginName", person.getpName());
        mmap.put("contactPhone", person.getPhone());
        mmap.put("reportTime", DateUtils.dateTimeNow());
        //??????????????????????????????
        String bizType = "SJ";
        Map<String,String> bizTypeMap = iOgPersonService.getBizTypeMap();
        for(Map.Entry<String,String> entry : bizTypeMap.entrySet()){
            if(entry.getKey().equals(org.getOrgName())){
                bizType = entry.getValue();
                break;
            }
        }
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);

        String returnUrl = "start";
        if(menuEnable){
            returnUrl = "start-ca";
        }
        return prefix_public + "/flow/" + returnUrl;
    }

    /**
     * ????????????????????????
     */
    @GetMapping("/edit/{fmId}/{flag}")
    public String edit(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmbiz = fmBizService.selectFmBizById(fmId);

        String url = prefix_public + "/flow/edit";
        //????????????????????????????????????
        if ("01".equals(fmbiz.getCurrentState())) {
            url = prefix_public + "/edit";
        }
        //???????????????
        if ("07".equals(fmbiz.getCurrentState())) {
            url = prefix_public + "/flow/edit2";
        }

        if(menuEnable){
            url += "-ca";
        }
        String loginName = iOgPersonService.selectOgPersonEvoById(fmbiz.getCreateId()).getpName();
        Map<String, Object> reMap = getReMap(fmbiz);
        fmbiz.setParams(reMap);
        mmap = fmBizService.getTypeList(fmbiz, mmap);
        mmap.put("loginName", loginName);
        mmap.put("fmBiz", fmbiz);
        mmap.put("flag", flag);
        return url;
    }

    /**
     * ????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxResult delete(String fmId) {
        return toAjax(fmBizService.deleteFmBizById(fmId));
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @PostMapping("/syslist")
    @ResponseBody
    public TableDataInfo syslist(OgSys sys) {
        startPage();
        List<OgSys> list = iSysApplicationManagerService.selectOgSysListByCondition(sys);
        return getDataTable(list);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @GetMapping("/sysGrouplist")
    public String selectOneApplication() {
        return prefix_public + "/flow/subpage/selectOneApplication";
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/OgSysList")
    public String OgSysList() {
        return prefix_public + "/flow/subpage/selectOgsys";
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @GetMapping("/selectogOrg")
    public String selectogOrg() {
        return prefix_public + "/flow/subpage/selectOgorg";
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @GetMapping("/savepbackEdit")
    public String savepbackEdit(String id, String taskId, String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(id);
        mmap.put("taskId", taskId);
        mmap.put("fmbiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/flow/subpage/pbackEdit";
    }

    /**
     * q
     * ??????????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/saveToAuto/{fmId}")
    public String saveToAuto(@PathVariable("fmId") String fmId, ModelMap mmap) {
        //??????????????????????????????????????????????????????????????????????????????
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        mmap.put("fmBiz", fmBiz);
        return prefix_public + "/flow/subpage/auto";
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @GetMapping("/savedeal/{fmId}/{flag}")
    public String savedeal(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        //??????????????????????????????????????????
        OgGroup group = ISysWorkService.selectOgGroupById(fmBiz.getGroupId());
        if ("1".equals(group.getGroupType())) {
            mmap.put("isOneGroup", "1");//????????????????????????????????? ????????????
        }
        List<CmBizSingleSJ> list2;
        if("mysql".equals(dataSourceType)){
            list2 = cmBizSingleSJMapper.selectListByFmNo2Mysql(fmBiz.getFaultNo());
        }else{
            list2 = cmBizSingleSJMapper.selectListByFmNo2(fmBiz.getFaultNo());
        }
        if (!list2.isEmpty()) {
            mmap.put("isSjbg", "1");//??????????????????????????????
        }
        KnowledgeTitle kt = knowledgeTitleService.selectKnowledgeTitleById(fmBiz.getOneType());
        if (kt != null) {
            if (fmBiz.getSysid().equals(kt.getSysId())) {
                mmap = fmBizService.getTypeList(fmBiz, mmap);
            } else {
                KnowledgeTitle kdt = new KnowledgeTitle();
                kdt.setCategory("1");
                kdt.setEventType("1");
                kdt.setSysId(fmBiz.getSysid());
                mmap.put("oneTypeList", knowledgeTitleService.selectKnowledgeTitleList(kdt));
            }
        }
        mmap.put("sysName", iSysApplicationManagerService.selectOgSysBySysId(fmBiz.getSysid()).getCaption());//????????????
        mmap.put("groupName", group.getGrpName());//????????????

        KnowledgeContent klc = new KnowledgeContent();
        klc.setSysId(fmBiz.getSysid());
        klc.setStatus("2");
        klc.setEventType("1");
        Map<String, Object> params = new HashMap();
        params.put("loginUser", "");
        klc.setParams(params);
        List<KnowledgeContent> list = knowledgeContentService.ContentList(klc);//???????????????????????????????????????
        mmap.put("knowledgeList", list);
        String str = fmBizService.ifKnowledgeRelationProblem(fmBiz);
        mmap.put("isProblem", str);
        mmap.put("fmBiz", fmBiz);
        mmap.put("flag", flag);
        String returnUrl = "";
        if(menuEnable){
            returnUrl = "-ca";
        }
        return prefix_public + "/flow/subpage/deal" + returnUrl;
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @GetMapping("/saveClose/{fmId}/{flag}")
    public String saveClose(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        mmap.put("fmbiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/flow/subpage/close";
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/saveremoveRecord/{fmId}/{flag}")
    public String saveremoveRecord(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        mmap.put("fmbiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/flow/subpage/removeRecord";
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/close/{fmId}/{flag}")
    public String close(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        Map<String, Object> reMap = getReMap(fmBiz);
        fmBiz.setParams(reMap);
        mmap.put("fmBiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/flow/close";
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @GetMapping("/saveprepeat/{fmId}/{flag}")
    public String saveprepeat(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        List<OgGroup> list = ISysWorkService.getOgProviceGroups();
        if (!list.isEmpty()) {
            mmap.addAttribute("dealGroup", list);
        }
        mmap.put("fmBiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/flow/subpage/prepeatIn";
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @GetMapping("/saverepeat/{fmId}/{flag}")
    public String saverepeat(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        mmap.put("fmBiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/flow/subpage/repeat";
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @GetMapping("/saverepeatIn/{fmId}/{flag}")
    public String saverepeatIn(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        OgUser u = ShiroUtils.getOgUser();//?????????????????????
        if (!u.getpId().equals(fmBiz.getDealerId())) {
            throw new BusinessException("????????????????????????????????????????????????????????????????????????");
        }
        List<OgPerson> list = ISysWorkService.selectGroupByPerson(fmBiz.getGroupId());
        if ("04".equals(fmBiz.getCurrentState())) {
        } else if ("11".equals(fmBiz.getCurrentState())) {
            list = ISysWorkService.selectGroupByPerson(fmBiz.getDealGroupId());
        } else {
            throw new BusinessException("????????????,?????????????????????????????????");
        }
        List<OgPerson> list2 = new ArrayList<>();
        if (!list.isEmpty()) {
            for (OgPerson person : list) {//???????????????????????????
                if (!u.getpId().equals(person.getpId())) {
                    list2.add(person);
                }
            }
            mmap.put("fmBiz", fmBiz);
            mmap.put("flag", flag);
            mmap.addAttribute("Person", list2);
        }
        return prefix_public + "/flow/subpage/repeatIn";
    }

    /**
     * ???????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/savebackEdit/{fmId}/{flag}")
    public String savebackEdit(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        mmap.put("fmbiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/flow/subpage/backEdit";
    }

    /**
     * ???????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/savebackPro/{fmId}/{flag}")
    public String savebackPro(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        mmap.put("fmbiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/flow/subpage/backPro";
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @GetMapping("/saveDifficult/{fmId}")
    public String saveDifficult(@PathVariable("fmId") String fmId, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        mmap.put("fmbiz", fmBiz);
        return prefix_public + "/flow/subpage/difficult";
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @PostMapping("/savebacktoYnSj")
    @ResponseBody
    public AjaxResult savebacktoYnSj(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        String pId = ShiroUtils.getOgUser().getpId();
        fmBizService.ifIdToDealId(pId, f.getDealerId());
        List<CmBizSingleSJ> list = cmBizSingleSJService.selectListByFmNo(f.getFaultNo());
        if (!list.isEmpty()) {
            throw new BusinessException("?????????????????????????????????????????????????????????????????????????????????");
        }
        if (fmBiz.getParams().containsKey("comment")) {
            f.setParams(fmBiz.getParams());
        }
        fmBizService.savebacktoYnSj(f, pId);
        return success("????????????????????????????????????" + f.getFaultNo());
    }

    /**
     * ??????
     *
     * @return
     */
    @Log(title = "????????????", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult saveDesc(FmBiz fmBiz) {
        fmBiz.setIniSys(fmBiz.getSysid());
        fmBiz.setIniOneType(fmBiz.getOneType());
        fmBiz.setIniTwoType(fmBiz.getTwoType());
        fmBiz.setIniThreeType(fmBiz.getThreeType());
        fmBiz.setIniKeywords(fmBiz.getKeywords());

        try {
            String cId = checkAddOrUpdate(fmBiz);
            fmBiz.setcId(cId);
            //??????????????????????????????????????????????????????????????????
            FmBiz fm = fmBizService.selectFmBizByFaultNo(fmBiz.getFaultNo());
            if (StringUtils.isEmpty(fmBiz.getFmId()) && ObjectUtils.isEmpty(fm)) {
                fmBiz.setCurrentState("01");
                fmBiz.setOccurTime(handleTimeYYYYMMDDHHMMSS(fmBiz.getOccurTime()));//????????????????????????????????????
                fmBiz.setFmId(UUID.getUUIDStr());
                fmBiz.setInvalidationMark("1");
                fmBizService.insertFmBiz(fmBiz);
                saveTelBiz(fmBiz);
                return AjaxResult.success("????????????", fmBiz.getFmId());

            } else {
                fmBizService.updateFmBiz(fmBiz);
                return AjaxResult.success("????????????", fmBiz.getFmId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("???????????????????????????" + fmBiz.getFaultNo());
        }

    }

    /**
     * ??????????????????
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        FmBiz FmBiz = fmBizService.selectFmBizById(id);
        Map<String, Object> reMap = getReMap(FmBiz);
        FmBiz.setParams(reMap);
        mmap.put("FmBiz", FmBiz);
        String returnUrl = "";
        if(menuEnable){
            returnUrl = "-ca";
        }
        return "fmbiz/detail" + returnUrl;
    }


    /**
     * ????????????????????????????????????
     */
    @GetMapping("/detailNoClose/{id}")
    public String detailNoClose(@PathVariable("id") String id, ModelMap mmap) {
        FmBiz FmBiz = fmBizService.selectFmBizById(id);
        Map<String, Object> reMap = getReMap(FmBiz);
        FmBiz.setParams(reMap);
        mmap.put("FmBiz", FmBiz);
        return "fmbiz/detailNoClose";
    }

    public Map<String, Object> getReMap(FmBiz FmBiz) {
        Map<String, Object> reMap = new HashMap<>();
        if (StringUtils.isNotEmpty(FmBiz.getCreateOrgId())) {
            OgOrg org = iSysDeptService.selectDeptById(FmBiz.getCreateOrgId());
            if (!ObjectUtils.isEmpty(org)) {
                reMap.put("createOrg", org.getOrgName());//????????????
            } else {
                reMap.put("createOrg", "");
            }
        } else {
            reMap.put("createOrg", "");
        }
        if (StringUtils.isNotEmpty(FmBiz.getIniSys())) {
            OgSys sys = iSysApplicationManagerService.selectOgSysBySysId(FmBiz.getIniSys());
            if (!ObjectUtils.isEmpty(sys)) {
                reMap.put("sysName", sys.getCaption());//????????????
            } else {
                reMap.put("sysName", "");
            }
        } else {
            reMap.put("sysName", "");
        }

        if (StringUtils.isNotEmpty(FmBiz.getGroupId())) {
            OgGroup group = ISysWorkService.selectOgGroupById(FmBiz.getGroupId());
            if (!ObjectUtils.isEmpty(group)) {
                reMap.put("groupName", group.getGrpName());//???????????????
            } else {
                reMap.put("groupName", "");
            }
        } else {
            reMap.put("groupName", "");
        }
        if (StringUtils.isNotEmpty(FmBiz.getDealerId())) {
            OgPerson person = iOgPersonService.selectOgPersonEvoById(FmBiz.getDealerId());
            if (!ObjectUtils.isEmpty(person)) {
                reMap.put("dealName", person.getpName());// ?????????
            } else {
                reMap.put("dealName", "");
            }
        } else {
            reMap.put("dealName", "");
        }
        if (StringUtils.isNotEmpty(FmBiz.getEvaluaterId())) {
            OgPerson person2 = iOgPersonService.selectOgPersonEvoById(FmBiz.getEvaluaterId());
            if (!ObjectUtils.isEmpty(person2)) {
                reMap.put("evaluaterName", person2.getpName());//?????????
            } else {
                reMap.put("evaluaterName", "");
            }
        } else {
            reMap.put("evaluaterName", "");
        }

        if (StringUtils.isNotEmpty(FmBiz.getIniOneType())) {//??????????????????
            KnowledgeTitle k1 = knowledgeTitleService.selectKnowledgeTitleById(FmBiz.getIniOneType());
            if (!ObjectUtils.isEmpty(k1)) {
                reMap.put("oneTypeName", k1.getName());
            } else {
                reMap.put("oneTypeName", "");
            }
        } else {
            reMap.put("oneTypeName", "");
        }

        if (StringUtils.isNotEmpty(FmBiz.getIniTwoType())) {//????????????
            KnowledgeTitle k2 = knowledgeTitleService.selectKnowledgeTitleById(FmBiz.getIniTwoType());
            if (!ObjectUtils.isEmpty(k2)) {
                reMap.put("twoTypeName", k2.getName());
            } else {
                reMap.put("twoTypeName", "");
            }
        } else {
            reMap.put("twoTypeName", "");
        }

        if (StringUtils.isNotEmpty(FmBiz.getIniThreeType())) {//????????????
            KnowledgeTitle k3 = knowledgeTitleService.selectKnowledgeTitleById(FmBiz.getIniThreeType());
            if (!ObjectUtils.isEmpty(k3)) {
                reMap.put("threeTypeName", k3.getName());
            } else {
                reMap.put("threeTypeName", "");
            }
        } else {
            reMap.put("threeTypeName", "");
        }

        if (StringUtils.isNotEmpty(FmBiz.getIniKeywords())) {//?????????
            KnowledgeTitle k4 = knowledgeTitleService.selectKnowledgeTitleById(FmBiz.getIniKeywords());
            if (!ObjectUtils.isEmpty(k4)) {
                reMap.put("keyWorksName", k4.getName());
            } else {
                reMap.put("keyWorksName", "");
            }
        } else {
            reMap.put("keyWorksName", "");
        }
        return reMap;
    }

    /**
     * ??????????????????????????????
     */
    public FmBiz getGroupIdByKnowledgek(FmBiz fmBiz) {
        String s = "2";
        List<OgGroup> list = new ArrayList<>();
        //???????????????????????????????????????????????????????????????
        List<String> nameList = new ArrayList<>();
        String str = fmBiz.getKeywords();
        if (StringUtils.isNotEmpty(str)) {
            String[] sName = str.split(",");
            if (sName.length > 0) {
                for (String ss : sName) {
                    nameList.add(ss);
                }
            }
        }
        List<KnowledgeContent> knowledgeList = knowledgeApi.searchKnowledge(fmBiz.getSysid(), "", fmBiz.getOneType(), fmBiz.getTwoType(), fmBiz.getThreeType(), nameList);
        if (!knowledgeList.isEmpty() && knowledgeList.size() == 1) {
            fmBiz.setKnowledgeId(knowledgeList.get(0).getId());
            s = "1";
        }
        OgGroup group = new OgGroup();
        group.setSysId(fmBiz.getSysid());
        group.setGroupType(s);
        list = ISysWorkService.selectOgGroupList(group);
        if (list.isEmpty()) {
            OgGroup group2 = new OgGroup();
            group2.setSysId(fmBiz.getSysid());
            group2.setGroupType("2");
            list = ISysWorkService.selectOgGroupList(group2);
        }
        fmBiz.setGroupId(list.get(0).getGroupId());
        return fmBiz;
    }

    /**
     * ???????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowdata")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowdata(FmBiz fmBiz) {
        String fmId = fmBiz.getFmId();
        int i = 0;
        //??????????????????????????????????????????????????????????????????
        FmBiz fm = fmBizService.selectFmBizByFaultNo(fmBiz.getFaultNo());
        if (ObjectUtils.isEmpty(fm) && StringUtils.isEmpty(fmId)) {
            fmBiz.setFmId(UUID.getUUIDStr());//??????ID
        } else {
            i = 1;
        }
        Map<String, Object> reMap = new HashMap<>();
        OgUser u = ShiroUtils.getOgUser();//?????????????????????
        String isCenter = iSysDeptService.getIsCenter();//??????????????????????????????????????????
        if ("1".equals(isCenter)) {//???
            reMap.put("reCode", "0");//????????????[???????????????????????????????????????]
            fmBiz.setCurrentState("03");//???????????????
            FmBiz fb = getGroupIdByKnowledgek(fmBiz);//??????????????????????????????????????????????????????
            reMap.put("groupId", fb.getGroupId());//????????????????????????????????????
            fmBiz.setKnowledgeId(fb.getKnowledgeId());//????????????
            fmBiz.setGroupId(fb.getGroupId());//?????????????????????
            fmBiz.setDealGroupId(fb.getGroupId());//?????????????????????
            fmBiz.setToQgzxTime(DateUtils.dateTimeNow());//???????????????????????????
        } else {//???
            reMap.put("reCode", "1");//????????????[??????????????????????????????]
            String pCode = iSysDeptService.getpCode(u.getpId());//?????????????????????????????????
            reMap.put("pCode", "0301" + "_" + pCode);//????????????+??????code
            fmBiz.setCurrentState("02");//???????????????
        }
        fmBiz.setOccurTime(handleTimeYYYYMMDDHHMMSS(fmBiz.getOccurTime()));//????????????????????????????????????
        fmBiz.setReportTime(handleTimeYYYYMMDDHHMMSS(fmBiz.getReportTime()));//????????????????????????????????????
        fmBiz.setInvalidationMark("1");//????????????1
        fmBiz.setDealMode("01");//??????????????????????????????
        fmBiz.setIfYn("0");
        //?????????????????????
        List<PubParaValue> list1 = iPubParaValueService.selectPubParaValueByParaName("fm_level_keywords");//???????????????
        List<PubParaValue> list2 = iPubParaValueService.selectPubParaValueByParaName("fm_level_intermediate");//???????????????
        fmBiz.setSeriousLev("1");//???????????????1[???]
        for (PubParaValue pp : list2) {
            if (fmBiz.getFaultDecriptDetail().contains(pp.getValueDetail())) {
                fmBiz.setSeriousLev("2");
                break;
            }
        }
        int isFmlevelHigh = 0;
        for (int y = 0; y < list1.size(); y++) {
            if (fmBiz.getFaultDecriptDetail().contains(list1.get(y).getValueDetail())) {
                isFmlevelHigh++;
            }
        }
        if (list1.size() == isFmlevelHigh) {
            fmBiz.setSeriousLev("3");
            fmBiz.setIfJj("1");

            if (isCenter != null) {
                if (isCenter.equals("1")) {
                    fmBizService.ifJJSendMessage(fmBiz.getGroupId(), fmBiz.getSysid(), fmBiz.getFaultNo());
                }
            }
        } else {
            fmBiz.setIfJj("2");
        }
        reMap.put("createId", u.getpId());//???????????????
        reMap.put("userId", u.getpId());//???????????????

        String cId = checkAddOrUpdate(fmBiz); //??????id
        fmBiz.setcId(cId);
        try {
            fmBiz.setIniSys(fmBiz.getSysid());
            fmBiz.setIniOneType(fmBiz.getOneType());
            fmBiz.setIniTwoType(fmBiz.getTwoType());
            fmBiz.setIniThreeType(fmBiz.getThreeType());
            fmBiz.setIniKeywords(fmBiz.getKeywords());
            if (i == 0) {
                fmBizService.insertFmBiz(fmBiz);
            } else {
                fmBizService.updateFmBiz(fmBiz);
            }
            activitiCommService.startProcess(fmBiz.getFmId(), "FmBiz", reMap);
            saveTelBiz(fmBiz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("?????????????????????????????????????????????????????????????????????");
        }
        return success("???????????????????????????.");
    }

    /**
     *  ??????or??????????????????
     * @param fmBiz
     * @return
     */
    private String checkAddOrUpdate(FmBiz fmBiz) {
        String cId = "";
        if("".equals(fmBiz.getCustomerName()) && "".equals(fmBiz.getcPhone())){
            return cId;
        }
        CustInfo custInfo = new CustInfo();
        custInfo.setcName(fmBiz.getCustomerName());
        custInfo.setcPhone(fmBiz.getcPhone());
        custInfo.setcDept(fmBiz.getcDept());
        custInfo.setcPost(fmBiz.getcPost());
        custInfo.setcAddress(fmBiz.getcAddress());
        custInfo.setCreateTime(new Date());
        custInfo.setcId(fmBiz.getcId());
        cId = fmBiz.getcId();
        try{
            //??????????????????????????????
            if(!"".equals(fmBiz.getcId())){
                //1.????????????
                if(!"".equals(fmBiz.getcPhone()) && fmBiz.getcPhone()!=null){
                    CustInfo custInfoObj = new CustInfo();
                    custInfoObj.setcPhone(fmBiz.getcPhone());
                    CustInfo obj = custInfoService.getByCustInfo(custInfoObj);
                    //1.1????????????????????????
                    if(obj!=null){
                        cId = obj.getcId();
                        custInfo.setcId(obj.getcId());
                    }
                }
                custInfoService.updateById(custInfo);

            }else{
                //??????????????? ?????????????????????????????????????????????
                int size = custInfoService.getByCount(custInfo.getcPhone());
                if(size>0){
                    //????????????????????????
                    custInfoService.updateById(custInfo);
                    CustInfo obj = custInfoService.getByCustInfo(custInfo);
                    cId = obj.getcId();
                }else{
                    //???????????????
                    custInfo.setcId(UUID.getUUIDStr());
                    custInfoService.insertCustInfo(custInfo);
                    cId = custInfo.getcId();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException("??????????????????");
        }

        return cId;
    }


    /**
     * ??????????????????????????????
     *
     * @param fmbiz
     * @return
     */
    @PostMapping("/getclosed")
    @ResponseBody
    public TableDataInfo getclosed(FmBiz fmbiz) {
        List<Map<String, Object>> reList = activitiCommService.userTask("FmBiz", "close");
        List<FmBiz> resultlist = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            String business_key = map.get("businesskey").toString();
            if (business_key != null) {
                FmBiz f = getTimeFormat(fmbiz);
                fmbiz.setParams(f.getParams());
                fmbiz.setFmId(business_key);
                FmBiz fb = fmBizService.getFlowFmBiz(fmbiz);
                if (fb != null) {
                    Map<String, Object> reMap = new HashMap<>();
                    reMap.put("taskId", map.get("taskId").toString());
                    reMap.put("createOrg", fb.getOgOrg().getOrgName());//????????????
                    reMap.put("sysName", fb.getOgSys().getCaption());//????????????
                    reMap.put("createTime", map.get("createTime"));
                    fb.setParams(reMap);
                    resultlist.add(fb);
                }
            }
        }
        if (!CollectionUtils.isEmpty(resultlist)) {
            Collections.sort(resultlist, new Comparator<FmBiz>() {
                @Override
                public int compare(FmBiz o1, FmBiz o2) {
                    Date createTime1 = (Date) o1.getParams().get("createTime");
                    Date createTime2 = (Date) o2.getParams().get("createTime");
                    if (createTime1.compareTo(createTime2) > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }
        return getDataTable_ideal(resultlist);
    }

    /**
     * ??????????????????????????????
     *
     * @param fmbiz
     * @return
     */
    @PostMapping("/fmbizlist")
    @ResponseBody
    public TableDataInfo fmbizlist(FmBiz fmbiz) {
        startPage();
        List<Map<String, Object>> reList = activitiCommService.userTask("FmBiz", "passign");
        List<Map<String, Object>> regList = new ArrayList<>();
        regList = activitiCommService.groupTasks("FmBiz", "passign");
        reList.addAll(regList);
        List<FmBiz> resultlist = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            if (!StringUtils.isEmpty(map.get("businesskey"))) {
                String business_key = map.get("businesskey").toString();
                if (business_key != null) {
                    FmBiz f = getTimeFormat(fmbiz);
                    fmbiz.setParams(f.getParams());
                    fmbiz.setFmId(business_key);
                    FmBiz fb = fmBizService.getFlowFmBiz(fmbiz);
                    if (fb != null) {
                        Map<String, Object> reMap = new HashMap<>();
                        reMap.put("taskId", map.get("taskId").toString());
                        reMap.put("createOrg", fb.getOgOrg().getOrgName());//????????????
                        reMap.put("sysName", fb.getOgSys().getCaption());//????????????
                        fb.setParams(reMap);
                        resultlist.add(fb);
                    }
                }
            }
        }
        return getDataTable_ideal(resultlist);
    }

    /**
     * ???????????????/???????????????????????????
     *
     * @param fmbiz
     * @return
     */
    @PostMapping("/receivelist")
    @ResponseBody
    public TableDataInfo receivelist(FmBiz fmbiz) {

        List<Map<String, Object>> reList1 = activitiCommService.userTask("FmBiz", "receive");
        List<Map<String, Object>> reList2 = activitiCommService.userTask("FmBiz", "preceive");
        List<Map<String, Object>> regList1 = new ArrayList<>();
        List<Map<String, Object>> regList2 = new ArrayList<>();
        regList1 = activitiCommService.groupTasks("FmBiz", "receive");
        regList2 = activitiCommService.groupTasks("FmBiz", "preceive");
        reList1.addAll(regList1);
        reList1.addAll(regList2);
        reList2.addAll(reList1);
        List<FmBiz> resultlist = new ArrayList<>();
        for (Map<String, Object> map : reList2) {
            String business_key = map.get("businesskey").toString();
            if (business_key != null) {
                FmBiz f = getTimeFormat(fmbiz);
                fmbiz.setParams(f.getParams());
                fmbiz.setFmId(business_key);
                if (StringUtils.isNotEmpty(fmbiz.getToQgzxTime())) {
                    if (fmbiz.getToQgzxTime().contains("-")) {
                        fmbiz.setToQgzxTime(handleTimeYYYYMMDD(fmbiz.getToQgzxTime()));
                    }
                }
                FmBiz fb = fmBizService.getFlowFmBiz(fmbiz);
                if (fb != null) {
                    Map<String, Object> reMap = new HashMap<>();
                    reMap.put("taskId", map.get("taskId").toString());
                    reMap.put("createOrg", fb.getOgOrg().getOrgName());//????????????
                    reMap.put("sysName", fb.getOgSys().getCaption());//????????????
                    reMap.put("createTime", map.get("createTime"));
                    fb.setParams(reMap);
                    resultlist.add(fb);
                }
            }
        }
        if ("1".equals(fmbiz.getRemark())) {
            resultlist = fmBizService.compareList(resultlist);
        } else {
            resultlist = fmBizService.compareList2(resultlist);
        }
        return getDataTable_ideal(resultlist);
    }


    /**
     * ???????????????????????????????????????????????????
     */
    @GetMapping("/passignss")
    public String passign(String id, String taskId, String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(id);
        Map<String, Object> reMap = getReMap(fmBiz);
        fmBiz.setParams(reMap);
        mmap.put("taskId", taskId);
        mmap.put("fmBiz", fmBiz);
        mmap.put("flag", flag);
        String returnUrl = "";
        if(menuEnable){
            returnUrl = "-ca";
        }
        return prefix_public + "/flow/passign" + returnUrl;
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @GetMapping("/saveassign")
    public String saveassign(String id, String taskId, String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(id);
        List<OgGroup> list = ISysWorkService.getOgProviceGroups();
        if (!list.isEmpty()) {
            mmap.addAttribute("dealGroup", list);
        }
        mmap.put("taskId", taskId);
        mmap.put("fmbiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/flow/subpage/assign";
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/savetoCenter")
    public String savetoCenter(String id, String taskId, String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(id);
        mmap.put("sysName", iSysApplicationManagerService.selectOgSysBySysId(fmBiz.getSysid()).getCaption());//????????????
        mmap.put("taskId", taskId);
        mmap.put("fmBiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/flow/subpage/tocenter";
    }

    /**
     * ??????????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/savefldeal")
    public String savefldeal(String id, String taskId, String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(id);
        String url = prefix_public + "/flow/deal2";
        if ("04".equals(fmBiz.getCurrentState())) {

        } else if ("11".equals(fmBiz.getCurrentState())) {
            url = prefix_public + "/flow/pdeal2";
        } else {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        if(menuEnable){
            url += "-ca";
        }
        Map<String, Object> reMap = getReMap(fmBiz);
        fmBiz.setParams(reMap);
        mmap.put("taskId", taskId);
        mmap.put("fmBiz", fmBiz);
        mmap.put("flag", flag);
        return url;
    }

    /**
     * 05???07?????????????????????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowedit")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowedit(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        if ("05".equals(f.getCurrentState())) {
            //?????????????????????????????????????????????????????????
            OgOrg org = iSysDeptService.selectDeptById(iOgPersonService.selectOgPersonEvoById(f.getDealerId()).getOrgId());
            if ("000000000".equals(org.getOrgCode()) || org.getLevelCode().contains("/000000000/010000888/")) {
                fmBiz.setCurrentState("04");//???????????????
                fmBiz.setToQgzxTime(DateUtils.dateTimeNow());//???????????????????????????
            } else {
                fmBiz.setCurrentState("11");
            }
        } else if ("07".equals(f.getCurrentState())) {
            fmBiz.setCurrentState("02");
        } else {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        /*
        ????????????????????????????????????????????????
         */
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("reCode", "0");
        reMap.put("businessKey", f.getFmId());
        reMap.put("processDefinitionKey", "FmBiz");
        //???????????????
        OgUser u = ShiroUtils.getOgUser();
        reMap.put("userId", u.getUserId());
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fmBiz.setParticipatorIds(u.getpId() + ",");
        } else {
            fmBiz.setParticipatorIds(f.getParticipatorIds() + u.getpId() + ",");
        }
        try {
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("??????????????????????????????????????????????????????" + f.getFaultNo());
        }
        if ("05".equals(f.getCurrentState())) {
            String smsText = "?????????????????????" + f.getFaultNo()
                    + "??????????????????????????????????????????????????????????????????";//????????????
            OgPerson person = iOgPersonService.selectOgPersonEvoById(f.getDealerId());// ?????????????????????
            fmBizService.sendSms(smsText, person);
        }
        return AjaxResult.success("???????????????????????????????????????", f.getFaultNo());
    }

    /**
     * ???????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowzuofei")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowzuofei(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        if (!"05".equals(f.getCurrentState())) {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        fmBiz.setEndTime(DateUtils.dateTimeNow());
        fmBiz.setCurrentState("10");
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("reCode", "1");
        reMap.put("businessKey", fmBiz.getFmId());
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("userId", ShiroUtils.getUserId());
        //???????????????
        String pId = ShiroUtils.getOgUser().getpId();
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fmBiz.setParticipatorIds(pId + ",");
        } else {
            fmBiz.setParticipatorIds(f.getParticipatorIds() + pId + ",");
        }
        try {
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("????????????????????????????????????????????????" + f.getFaultNo());
        }
        return AjaxResult.success("????????????????????????????????????", f.getFaultNo());
    }

    /**
     * ???????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowreceive")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowreceive(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        FmBiz fb = new FmBiz();
        fb.setFmId(fmBiz.getFmId());
        if ("03".equals(f.getCurrentState())) {
            fb.setCurrentState("04");
        } else if ("08".equals(f.getCurrentState())) {
            fb.setCurrentState("11");
        } else {
            return error("??????????????????????????????????????????????????????????????????");
        }

        OgUser u = ShiroUtils.getOgUser();
        fb.setDealerId(u.getpId());
        //???????????????
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fb.setParticipatorIds(u.getpId() + ",");
        } else {
            fb.setParticipatorIds(f.getParticipatorIds() + u.getpId() + ",");
        }
        //???????????????
        if (StringUtils.isEmpty(f.getParticipateGroupids())) {
            fb.setParticipateGroupids(f.getGroupId()
                    + ",");
        } else {
            fb.setParticipateGroupids(f
                    .getParticipateGroupids()
                    + f.getGroupId()
                    + ",");
        }
        // ??????????????????id???????????????????????????????????????
        if (StringUtils.isEmpty(f.getReceiverIds())) {
            fb.setReceiverIds(u.getpId() + ",");
        } else {
            fb.setReceiverIds(f.getReceiverIds()
                    + u.getpId() + ",");
        }

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", fmBiz.getFmId());
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("dealGroupId", f.getDealGroupId());
        reMap.put("groupId", f.getGroupId());
        reMap.put("dealId", u.getUserId());
        reMap.put("userId", u.getUserId());
        reMap.put("taskId", fmBiz.getParams().get("taskId"));
        try {
            fmBizService.updateFmBiz(fb);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("???????????????????????????????????????" + f.getFaultNo());
        }

        OgGroup group = ISysWorkService.selectOgGroupById(f.getGroupId());//?????????????????????
        if ("03".equals(f.getCurrentState()) && "1".equals(group.getGroupType())) {//??????????????????????????????????????????
            fmBizService.pushOneLine(f);//?????????????????????????????????
        }
        return AjaxResult.success("?????????????????????????????????", f.getFaultNo());
    }

    /**
     * ???????????????????????????????????????????????????
     */
    @GetMapping("/receivess")
    public String auditAuthor(String id, String taskId, String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(id);
        Map<String, Object> reMap = getReMap(fmBiz);
        mmap.put("taskId", taskId);
        fmBiz.setParams(reMap);
        mmap.put("fmBiz", fmBiz);
        mmap.put("flag", flag);
        String returnUrl = "";
        if(menuEnable){
            returnUrl = "-ca";
        }
        return prefix_public + "/flow/receive" + returnUrl;
    }

    /**
     * ??????????????????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowpbackEdit")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowpbackEdit(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        if (!"02".equals(f.getCurrentState())) {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        fmBiz.setCurrentState("07");
        Map<String, Object> reMap = new HashMap<>();
        String businessKey = fmBiz.getFmId();
        String processDefinitionKey = "FmBiz";
        reMap.put("businessKey", businessKey);
        reMap.put("reCode", "2");
        reMap.put("processDefinitionKey", processDefinitionKey);
        reMap.put("comment", fmBiz.getParams().get("comment"));
        //???????????????
        String pId = ShiroUtils.getOgUser().getpId();
        reMap.put("userId", pId);
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fmBiz.setParticipatorIds(pId + ",");
        } else {
            fmBiz.setParticipatorIds(f.getParticipatorIds() + pId + ",");
        }
        try {
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("???????????????????????????????????????????????????" + f.getFaultNo());
        }
        return AjaxResult.success("???????????????????????????????????????", f.getFaultNo());
    }

    /**
     * ???????????????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveFlowToQgZx")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveFlowToQgZx(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        if (!"02".equals(f.getCurrentState())) {
            throw new BusinessException("?????????????????????????????????????????????????????????????????????");
        }
        OgUser u = ShiroUtils.getOgUser();
        //???????????????
        if ("".equals(f.getParticipatorIds())) {
            fmBiz.setParticipatorIds(u.getpId() + ",");
        } else {
            fmBiz.setParticipatorIds(f.getParticipatorIds() + u.getpId() + ",");
        }
        FmBiz fb = getGroupIdByKnowledgek(f);//??????????????????????????????
        fmBiz.setGroupId(fb.getGroupId());//?????????????????????
        fmBiz.setDealGroupId(fb.getGroupId());//?????????????????????
        fmBiz.setKnowledgeId(fb.getKnowledgeId());//????????????
        fmBiz.setCurrentState("03");//????????????????????????
        fmBiz.setToQgzxTime(DateUtils.dateTimeNow());//???????????????????????????
        Map<String, Object> reMap = new HashMap<>();
        String businessKey = fmBiz.getFmId();
        reMap.put("reCode", "1");//??????????????????????????????
        reMap.put("businessKey", businessKey);
        reMap.put("groupId", fb.getGroupId());
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("taskId", fmBiz.getParams().get("taskId"));
        reMap.put("comment", fmBiz.getParams().get("comment"));
        reMap.put("userId", u.getUserId());
        try {
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("????????????????????????????????????????????????????????????????????????");
        }
        if ("1".equals(fmBiz.getIfJj())) {
            fmBizService.ifJJSendMessage(fmBiz.getGroupId(), f.getSysid(), f.getFaultNo());
        }
        return AjaxResult.success("??????????????????????????????", fmBiz.getFmId());
    }

    /**
     * ????????????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveFlowAssign")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveFlowAssign(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        if (!"02".equals(f.getCurrentState())) {
            throw new BusinessException("?????????????????????????????????????????????????????????????????????");
        }
        fmBiz.setCurrentState("08");
        fmBiz.setDealGroupId(fmBiz.getParams().get("flowGroup").toString());
        fmBiz.setGroupId(fmBiz.getParams().get("flowGroup").toString());
        Map<String, Object> reMap = new HashMap<>();
        //?????????????????????????????????
        reMap.put("groupId", fmBiz.getParams().get("flowGroup").toString());
        if (StringUtils.isNotEmpty(fmBiz.getDealerId())) {//???????????????????????????
            reMap.put("dealId", fmBiz.getDealerId());
        }
        reMap.put("reCode", "0");//????????????????????????
        reMap.put("businessKey", fmBiz.getFmId());
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("comment", fmBiz.getParams().get("comment"));//????????????
        reMap.put("taskId", fmBiz.getParams().get("taskId").toString());//??????ID
        //???????????????
        String pId = ShiroUtils.getOgUser().getpId();
        reMap.put("userId", pId);
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fmBiz.setParticipatorIds(pId + ",");
        } else {
            fmBiz.setParticipatorIds(f.getParticipatorIds() + pId + ",");
        }
        try {
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("???????????????????????????????????????" + f.getFaultNo());
        }
        if (StringUtils.isNotEmpty(fmBiz.getDealerId())) {
            String smsText = "?????????????????????" + f.getFaultNo()
                    + "????????????????????????????????????????????????????????????";//????????????
            OgPerson person = iOgPersonService.selectOgPersonEvoById(fmBiz.getDealerId());// ?????????????????????
            fmBizService.sendSms(smsText, person);
        }
        return AjaxResult.success("??????????????????????????????????????????", f.getFaultNo());
    }

    /**
     * ??????????????????????????????
     *
     * @param fmbiz
     * @return
     */
    @PostMapping("/deallist")
    @ResponseBody
    public TableDataInfo deallist(FmBiz fmbiz) {
        List<Map<String, Object>> reList1 = activitiCommService.userTask("FmBiz", "deal");
        List<Map<String, Object>> reList2 = activitiCommService.userTask("FmBiz", "pdeal");
        if (!reList1.isEmpty()) {
            reList1.addAll(reList2);
        } else {
            reList1 = reList2;
        }
        List<FmBiz> resultlist = new ArrayList<>();
        for (Map<String, Object> map : reList1) {
            String business_key = map.get("businesskey").toString();
            if (business_key != null) {
                FmBiz f = getTimeFormat(fmbiz);
                fmbiz.setParams(f.getParams());
                fmbiz.setFmId(business_key);
                if (StringUtils.isNotEmpty(fmbiz.getToQgzxTime())) {
                    if (fmbiz.getToQgzxTime().contains("-")) {
                        fmbiz.setToQgzxTime(handleTimeYYYYMMDD(fmbiz.getToQgzxTime()));
                    }
                }
                FmBiz fb = fmBizService.getFlowFmBiz(fmbiz);
                if (fb != null) {
                    Map<String, Object> reMap = new HashMap<>();
                    List<CmBizSingleSJ> list = cmBizSingleSJService.selectListByFmNo(fb.getFaultNo());
                    if (!list.isEmpty()) {
                        reMap.put("ifcmsj", "1");
                    } else {
                        reMap.put("ifcmsj", "0");
                    }
                    reMap.put("createOrg", fb.getOgOrg().getOrgName());//????????????
                    reMap.put("sysName", fb.getOgSys().getCaption());//????????????
                    fb.setParams(reMap);
                    fb.getParams().put("instanceId", map.get("processInstanceId").toString());
                    fb.getParams().put("createTime", map.get("createTime"));
                    resultlist.add(fb);
                }
            }
        }
        if ("1".equals(fmbiz.getRemark())) {
            resultlist = fmBizService.compareList(resultlist);
        } else {
            resultlist = fmBizService.compareList2(resultlist);
        }
        return getDataTable_ideal(resultlist);
    }

    /**
     * ???????????????????????????????????????????????????
     */
    @GetMapping("/deal/{id}/{flag}")
    public String deal(@PathVariable("id") String id, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(id);
        Map<String, Object> reMap = getReMap(fmBiz);
        /*//??????????????????????????????????????????????????????????????????????????????????????????
        OgOrg org = iSysDeptService.selectDeptById(fmBiz.getCreateOrgId());
        if ((org.getLevelCode() != null && org.getLevelCode().indexOf("010000888") > 0) || "000000000".equals(org.getOrgCode())) {
            reMap.put("agencySide", "1");
        } else {
            reMap.put("agencySide", "0");
        }*/
        fmBiz.setParams(reMap);
        mmap.put("fmBiz", fmBiz);
        mmap.put("flag", flag);
        String url = prefix_public + "/flow/deal";
        if ("11".equals(fmBiz.getCurrentState())) {
            url = prefix_public + "/flow/pdeal";
        }
        if(menuEnable){
            url += "-ca";
        }
        return url;
    }

    /**
     * ???????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowdeal")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowdeal(FmBiz fmBiz,String applicationName,String keywordName) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        FmBiz fb = new FmBiz();
        fb.setFmId(fmBiz.getFmId());
        String pId = ShiroUtils.getOgUser().getpId();
        fmBizService.ifIdToDealId(pId, f.getDealerId());
        if ("04".equals(f.getCurrentState())) {
            fb.setOneType(fmBiz.getOneType());
            fb.setTwoType(fmBiz.getTwoType());
            fb.setThreeType(fmBiz.getThreeType());
            fb.setKeywords(fmBiz.getKeywords());
            fb.setFmCause(fmBiz.getFmCause());
            fb.setKnowledgeId(fmBiz.getKnowledgeId());
            fb.setIsAntiFraud(fmBiz.getIsAntiFraud());
            if (fmBiz.getParams().containsKey("ifTool") && "1".equals(fmBiz.getParams().get("ifTool"))) {
                fb.setKnowledgeId(fmBiz.getKnowledgeId());//????????????
            }
        } else if ("11".equals(f.getCurrentState())) {

        } else {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        List<CmBizSingleSJ> list2 = cmBizSingleSJService.selectListByFmNo(f.getFaultNo());
        if (!list2.isEmpty()) {
            throw new BusinessException("?????????????????????????????????????????????????????????????????????????????????");
        }
        //???????????????
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fb.setParticipatorIds(pId + ",");
        } else {
            fb.setParticipatorIds(f.getParticipatorIds() + pId + ",");
        }
        fb.setDealDescription(fmBiz.getDealDescription());
        fb.setDealerId(pId);
        fb.setDealTime(DateUtils.dateTimeNow());//??????????????????
        fb.setCurrentState("06");//????????????????????????
        Map<String, Object> reMap = new HashMap<>();
        //????????????????????????
        reMap.put("comment", fmBiz.getDealDescription());
        reMap.put("dealGroupId", f.getDealGroupId());
        reMap.put("groupId", f.getGroupId());
        reMap.put("businessKey", fmBiz.getFmId());
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("reCode", "0");
        reMap.put("userId", pId);
        try {
            fmBizService.updateFmBiz(fb);
            activitiCommService.complete(reMap);
            setTelToDeal(f);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("?????????????????????????????????????????????" + f.getFaultNo());
        }
        //???????????????????????????????????????????????????
        String smsText = "?????????????????????" + f.getFaultNo() + "?????????????????????????????????????????????????????????";//????????????
        OgPerson person = iOgPersonService.selectOgPersonEvoById(f.getCreateId());// ?????????????????????
        fmBizService.sendSms(smsText, person);

        fmBizService.sendPhoneBankOrCard(f, "1", fmBiz.getDealDescription());
        if ("04".equals(f.getCurrentState())) {
            if (fmBiz.getParams().containsKey("immediately")) {
                iFmbizAndIssueService.saveFmbizAndIssue(fmBiz);
            } else {//??????/????????????
                List<CmBizSingleSJ> list3;
                if("mysql".equals(dataSourceType)){
                    list3 = cmBizSingleSJMapper.selectListByFmNo2Mysql(f.getFaultNo());
                }else{
                    list3 = cmBizSingleSJMapper.selectListByFmNo2(f.getFaultNo());
                }
                if (list3.isEmpty()) {//???????????????????????????????????????
                    iFmbizAndIssueService.relationIssueOne(f);
                }
            }
        }
        //??????????????????????????????????????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        KnowledgeContent knowledgeContent = new KnowledgeContent();
        knowledgeContent.setTitle(fmBiz.getFaultDecriptSummary());
        knowledgeContent.setDescribes(fmBiz.getDealDescription());
        knowledgeContent.setSysId(fmBiz.getSysid());
        knowledgeContent.setSystemName(applicationName);
        knowledgeContent.setContent(fmBiz.getOneType());
        knowledgeContent.setContentId(fmBiz.getOneType());
        knowledgeContent.setSectitle(fmBiz.getTwoType());
        knowledgeContent.setSectitleId(fmBiz.getTwoType());
        knowledgeContent.setThreetitle(fmBiz.getThreeType());
        knowledgeContent.setName(keywordName);
        knowledgeContent.setCreateTime(sdf.format(new Date()));
        knowledgeContent.setId(UUID.getUUIDStr());
        knowledgeContent.setCreateId(ShiroUtils.getOgUser().getUserId());
        knowledgeContent.setCreateName(ShiroUtils.getOgUser().getUsername());
        knowledgeContent.setPowerId("0");//?????????????????????
        knowledgeContent.setEventType("1");//??????????????????????????????
        knowledgeContent.setStatus("2");//??????????????????????????????
        businessContentService.saveSearchAndEs(knowledgeContent);
        businessContentService.insertContent(knowledgeContent);
        return AjaxResult.success("?????????????????????????????????", f.getFaultNo());
    }

    /**
     * ???????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowbackEdit")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowbackEdit(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        String pId = ShiroUtils.getOgUser().getpId();
        fmBizService.ifIdToDealId(pId, f.getDealerId());
        Map<String, Object> reMap = new HashMap<>();
        if ("04".equals(f.getCurrentState()) || "11".equals(f.getCurrentState())) {
            fmBiz.setCurrentState("05");
        } else {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        List<CmBizSingleSJ> list = cmBizSingleSJService.selectListByFmNo(f.getFaultNo());
        if (!list.isEmpty()) {
            throw new BusinessException("?????????????????????????????????????????????????????????????????????????????????");
        }
        reMap.put("dealGroupId", f.getDealGroupId());
        reMap.put("businessKey", fmBiz.getFmId());
        reMap.put("reCode", "1");
        reMap.put("comment", fmBiz.getParams().get("comment"));
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("userId", pId);
        try {
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("?????????????????????????????????????????????" + f.getFaultNo());
        }
        String smsText = "?????????????????????" + f.getFaultNo()
                + "????????????????????????????????????????????????????????????????????????";//????????????
        OgPerson person = iOgPersonService.selectOgPersonEvoById(f.getCreateId());// ?????????????????????
        fmBizService.sendSms(smsText, person);
        fmBizService.sendPhoneBankOrCard(f, "0", fmBiz.getParams().get("comment"));
        return AjaxResult.success("?????????????????????????????????", f.getFaultNo());
    }

    /**
     * ????????????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowbackPro")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowbackPro(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        String pId = ShiroUtils.getOgUser().getpId();
        fmBizService.ifIdToDealId(pId, f.getDealerId());
        if (!"04".equals(f.getCurrentState()) && !"11".equals(f.getCurrentState())) {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        List<CmBizSingleSJ> list = cmBizSingleSJService.selectListByFmNo(f.getFaultNo());
        if (!list.isEmpty()) {
            throw new BusinessException("?????????????????????????????????????????????????????????????????????????????????");
        }
        fmBiz.setCurrentState("02");
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", fmBiz.getFmId());
        reMap.put("reCode", "2");
        reMap.put("dealGroupId", f.getDealGroupId());
        reMap.put("comment", fmBiz.getParams().get("comment"));
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("userId", pId);
        try {
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("??????????????????????????????????????????????????????" + f.getFaultNo());
        }
        return AjaxResult.success("??????????????????????????????????????????", f.getFaultNo());
    }

    /**
     * ??????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowrepeat")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowrepeat(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        OgUser u = ShiroUtils.getOgUser();
        fmBizService.ifIdToDealId(u.getpId(), f.getDealerId());
        Map<String, Object> reMap = new HashMap<>();//????????????????????????
        if ("04".equals(f.getCurrentState())) {
            reMap.put("dealGroupId", f.getGroupId());
        } else {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        List<CmBizSingleSJ> list2 = cmBizSingleSJService.selectListByFmNo(f.getFaultNo());
        if (!list2.isEmpty()) {
            throw new BusinessException("?????????????????????????????????????????????????????????????????????????????????");
        }
        if ("1".equals(fmBiz.getRepeatMark())) {
            fmBiz.setDealMode("03");//??????????????????
        }
        fmBiz.setCurrentState("03");//?????????????????????????????????
        fmBiz.setDealGroupId(fmBiz.getGroupId());
        fmBiz.setSysid(ISysWorkService.selectOgGroupById(fmBiz.getGroupId()).getSysId());//??????????????????
        reMap.put("businessKey", f.getFmId());
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("reCode", "4");
        reMap.put("comment", fmBiz.getParams().get("comment"));
        reMap.put("groupId", fmBiz.getGroupId());
        reMap.put("userId", u.getUserId());
        //???????????????
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fmBiz.setParticipatorIds(u.getpId() + ",");
        } else {
            fmBiz.setParticipatorIds(f.getParticipatorIds() + u.getpId() + ",");
        }
        try {
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("?????????????????????????????????????????????????????????" + f.getFaultNo());
        }
        if ("1".equals(f.getIfJj())) {
            fmBizService.ifJJSendMessage(fmBiz.getGroupId(), fmBiz.getSysid(), f.getFaultNo());
        } else {
            try {
                //???????????????????????? ??????8:00~17:00 ??? ??????????????? 18:00~24:00  ??? ?????????  ????????????????????????????????????
                String toQgZxTime = f.getToQgzxTime();
                String format = "HH:mm:ss";
                assert toQgZxTime != null;
                Date creatTime = new SimpleDateFormat("HHmmss").parse(toQgZxTime.substring(8));
                Date startTime = new SimpleDateFormat(format).parse("08:00:00");
                Date endTime = new SimpleDateFormat(format).parse("17:00:00");
                boolean effectiveDate = FmBizOvertimeServiceImpl.isEffectiveDate(creatTime, startTime, endTime);
                Date nowTime = new SimpleDateFormat(format).parse(DateUtils.getToday("HH:mm:ss"));
                Date startNowTime = new SimpleDateFormat(format).parse("18:00:00");
                Date endNowTime = new SimpleDateFormat(format).parse("24:00:00");
                boolean effectiveNowDate = FmBizOvertimeServiceImpl.isEffectiveDate(nowTime, startNowTime, endNowTime);

                String substring = toQgZxTime.substring(0, 8);
                String replace = DateUtils.getToday().replace("-", "");
                if (effectiveDate && effectiveNowDate && substring.equals(replace)) {
                    fmBizService.ifJJToSendMessage("2", fmBiz.getGroupId(), fmBiz.getSysid(), f.getFaultNo());
                }
            } catch (Exception e) {
                logger.error("??????????????????????????????" + e.getMessage());
            }
        }

        return AjaxResult.success("?????????????????????????????????????????????", f.getFaultNo());
    }

    /**
     * ???????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowprepeat")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowprepeat(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        String pId = ShiroUtils.getOgUser().getpId();
        fmBizService.ifIdToDealId(pId, f.getDealerId());
        if (!"11".equals(f.getCurrentState())) {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        fmBiz.setDealGroupId(fmBiz.getParams().get("flowGroup").toString());//???????????????
        fmBiz.setGroupId(fmBiz.getParams().get("flowGroup").toString());//???????????????
        fmBiz.setCurrentState("08");//?????????????????????????????????
        Map<String, Object> reMap = new HashMap<>();//????????????????????????
        reMap.put("businessKey", f.getFmId());
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("reCode", "4");
        reMap.put("comment", fmBiz.getParams().get("comment"));
        reMap.put("groupId", fmBiz.getParams().get("flowGroup").toString());
        reMap.put("dealGroupId", fmBiz.getDealGroupId());
        reMap.put("userId", pId);
        //???????????????
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fmBiz.setParticipatorIds(pId + ",");
        } else {
            fmBiz.setParticipatorIds(f.getParticipatorIds() + pId + ",");
        }
        try {
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("??????????????????????????????????????????????????????" + f.getFaultNo());
        }
        return AjaxResult.success("??????????????????????????????????????????", f.getFaultNo());
    }

    /**
     * ????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowrepeatIn")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowrepeatIn(FmBiz fmBiz) {
        return fmBizService.saveflowrepeatIn(fmBiz);
    }

    /**
     * ????????????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowClose")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowClose(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        if (!"06".equals(f.getCurrentState())) {
            throw new BusinessException("??????????????????????????????????????????????????????????????????");
        }
        fmBiz.setEvaluaterId(f.getCreateId());//?????????
        fmBiz.setEvaluateTime(DateUtils.dateTimeNow("yyyyMMddHHmmss"));//????????????/????????????
        fmBiz.setCurrentState("09");//?????????????????????
        //???????????????????????????
        if (StringUtils.isNotEmpty(f.getToQgzxTime())) {
            SimpleDateFormat sd = new SimpleDateFormat(format);
            try {
                long data1 = sd.parse(f.getDealTime()).getTime();
                long data2 = sd.parse(f.getToQgzxTime()).getTime();
                long l = data1 - data2;
                fmBiz.setDealUseTime(l / 1000 + "");
            } catch (Exception e) {
                logger.info("????????????????????????????????????????????????");
            }
        }
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("businessKey", fmBiz.getFmId());
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("reCode", "0");

        String pId = ShiroUtils.getOgUser().getpId();
        reMap.put("userId", pId);
        //???????????????
        if (StringUtils.isEmpty(f.getParticipatorIds())) {
            fmBiz.setParticipatorIds(pId + ",");
        } else {
            fmBiz.setParticipatorIds(f.getParticipatorIds() + pId + ",");
        }
        try {
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            throw new BusinessException("???????????????????????????????????????????????????" + f.getFaultNo());
        }
        //?????????????????????????????????????????????http????????????????????????????????????
        String isAntiFraud = f.getIsAntiFraud();

        if ("1".equals(isAntiFraud)) {
            fmBizService.sendAntifraud(f, AntifraudUrl);
        }
        fmBizService.calculationDealTime(f);
        return AjaxResult.success("???????????????????????????????????????", f.getFaultNo());
    }

    /**
     * ????????????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowremoveRecord")
    @ResponseBody

    public AjaxResult saveflowremoveRecord(FmBiz fmBiz) {
        return fmBizService.saveflowremoveRecord(fmBiz);
    }

    /**
     * ???????????????
     */
    @PostMapping("/view")
    @ResponseBody
    public TableDataInfo getPageFmbizList(FmBiz fmBiz) {
        fmBiz = fmBizService.formatFmbiz(fmBiz);
        if (fmBiz.getParams().containsKey("ifAuto")) {
            fmBiz.getParams().put("ifAuto", "1");
        }
        if (StringUtils.isNotEmpty((String) fmBiz.getParams().get("relationTimeStart"))) {
            fmBiz.getParams().put("relationTimeStart", handleTimeYYYYMMDD((String) fmBiz.getParams().get("relationTimeStart")) + "000000");
        }
        if (StringUtils.isNotEmpty((String) fmBiz.getParams().get("relationTimeEnd"))) {
            fmBiz.getParams().put("relationTimeEnd", handleTimeYYYYMMDD((String) fmBiz.getParams().get("relationTimeEnd")) + "000000");
        }
        String[] issuefxIds = null;
        if (StringUtils.isNotEmpty((String) fmBiz.getParams().get("issuefxNo"))) {
            ImBizIssuefx imBizIssuefx = new ImBizIssuefx();
            imBizIssuefx.setIssuefxNo((String) fmBiz.getParams().get("issuefxNo"));
            List<ImBizIssuefx> list = iImBizIssuefxService.selectIssueList(imBizIssuefx);
            String issuefxId = "";
            if (list != null && list.size() > 0) {
                for (ImBizIssuefx imBizIssuefx1 : list) {
                    issuefxId += imBizIssuefx1.getIssuefxId() + ",";
                }
                issuefxIds = issuefxId.split(",");
            }
        }
        fmBiz.getParams().put("issuefxIds", issuefxIds);
        startPage();
        List<FmBiz> list = fmBizService.selectFmBizListPager(fmBiz);
        return getDataTable(list);
    }

    /**
     * ????????????ID?????????????????????
     */
    @PostMapping("/selectOgGroupBySys")
    @ResponseBody
    public TableDataInfo selectOgGroupBySys(String sysid) {
        List<OgGroup> list = ISysWorkService.selectOgGroupBySys(sysid);
        return getDataTable(list);
    }

    /**
     * ????????????ID??????????????????
     */
    @PostMapping("/selectFmKindBySysid")
    @ResponseBody
    public TableDataInfo selectFmKindBySysid(String sysid, String category, String parentId) {
        KnowledgeTitle kdt = new KnowledgeTitle();
        kdt.setSysId(sysid);
        kdt.setCategory(category);
        kdt.setParentId(parentId);
        kdt.setEventType("1");
        kdt.setStatus("0");
        List<KnowledgeTitle> list = knowledgeTitleService.selectKnowledgeTitleList(kdt);
        return getDataTable(list);
    }

    /**
     * ????????????ID?????????????????????
     */
    @PostMapping("/selectKnowledgeBySysId/{fmId}")
    @ResponseBody
    public TableDataInfo selectKnowledgeBySysId(@PathVariable("fmId") String fmId) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        String knowledgeId = fmBiz.getKnowledgeId();
        List<KnowledgeContent> list = new ArrayList<>();

        if (StringUtils.isNotEmpty(knowledgeId)) {
            String str[] = knowledgeId.split(",");
            for (String s : str) {
                KnowledgeContent kdc = knowledgeContentService.selectContById(s);
                list.add(kdc);
            }
        }
        return getDataTable(list);
    }

    /**
     * ???????????????ID????????????????????????
     */
    @PostMapping("/selectGroupByPerson")
    @ResponseBody
    public TableDataInfo selectGroupByPerson(String groupId) {
        List<OgPerson> list = ISysWorkService.selectGroupByPerson(groupId);
        return getDataTable(list);
    }

    /**
     * ????????????
     */
    @PostMapping("/selectOrgList")
    @ResponseBody
    public TableDataInfo selectOrgList(OgOrg org) {
        startPage();
        List<OgOrg> list = iSysDeptService.selectDeptList(org);
        return getDataTable(list);
    }

    /**
     * ???????????????????????????????????????????????????
     */
    @PostMapping("/selectOgGroupByFmbiz")
    @ResponseBody
    public TableDataInfo selectOgGroupByFmbiz(OgGroup group) {
        List<OgGroup> list = ISysWorkService.selectOgGroupByFmbiz(group);
        return getDataTable_ideal(list);
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @return
     */
    @GetMapping("Grouplist")
    public String Grouplist() {
        return prefix_public + "/flow/subpage/selectGroupBySys";
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @GetMapping("Grouplist2")
    public String Grouplist2() {
        return prefix_public + "/flow/subpage/selectGroup";
    }

    @GetMapping("searchGroup")
    public String searchGroup() {
        return prefix_public + "/searchGroup";
    }

    @PostMapping("/selectOgGroupByCount")
    @ResponseBody
    public TableDataInfo selectOgGroupByCount(OgGroup group) {
        startPage();
        List<OgGroup> list = ISysWorkService.selectOgGroupList(group);
        return getDataTable(list);
    }

    long nd = 1000 * 24 * 60 * 60;//??????????????????
    long nh = 1000 * 60 * 60;//?????????????????????
    long nn = 1000 * 60 * 60 * 2;
    long nr = 1000 * 60 * 60 * 8;
    long nm = 1000 * 60;//?????????????????????

    String format = "yyyyMMddHHmmss";

    /**
     * ?????????????????????
     */
    @PostMapping("/selectGroupHandleList")
    @ResponseBody
    public TableDataInfo selectGroupHandleList(FmBiz fmBiz) {
        startPage();
        OgUser u = ShiroUtils.getOgUser();
        Map<String, Object> reMap = new HashMap<>();
        if (StringUtils.isNotEmpty(fmBiz.getDealMode())) {
            String[] dealModes = fmBiz.getDealMode().split(",");
            reMap.put("dealMode", dealModes);
        }
        reMap.put("userId", u.getpId());
        fmBiz.setParams(reMap);
        List<FmBiz> list = fmBizService.selectGroupHandleList(fmBiz);
        SimpleDateFormat sd = new SimpleDateFormat(format);
        if (!list.isEmpty()) {
            try {
                for (FmBiz f : list) {
                    List<CmBizSingleSJ> list2 = cmBizSingleSJService.selectListByFmNo(f.getFaultNo());
                    if (list2.isEmpty()) {
                        if ("1".equals(f.getIfJj())) {
                            //??????????????????????????????
                            long diff = sd.parse(DateUtils.dateTimeNow()).getTime() - sd.parse(f.getToQgzxTime()).getTime();
                            diff = nn - diff;
                            long hour = diff / nh;//?????????????????????
                            long min = diff % nd % nh / nm;//?????????????????????
                            f.setFmbizCountdown(hour + "??????" + min + "??????");
                        }
                        if ("2".equals(f.getIfJj())) {
                            long diff = sd.parse(DateUtils.dateTimeNow()).getTime() - sd.parse(f.getToQgzxTime()).getTime();
                            diff = nr - diff;
                            long hour = diff / nh;//?????????????????????
                            long min = diff % nd % nh / nm;//?????????????????????
                            f.setFmbizCountdown(hour + "??????" + min + "??????");
                        }
                        //??????????????????????????????
                        /*
                        ???????????????ID????????????????????????????????????????????????????????????????????????????????????????????????
                        ?????????????????????????????????????????????????????????????????????
                         */
                        List<PubFlowLog> flowList = iPubFlowLogService.selectPubFlowLogAll(f.getFmId());
                        for (PubFlowLog flowlog : flowList) {
                            String performerTime = flowlog.getPerformerTime();
                            long diff = sd.parse(DateUtils.dateTimeNow()).getTime() - sd.parse(performerTime).getTime();
                            long hour = diff / nh;//?????????????????????
                            long min = diff % nd % nh / nm;//?????????????????????
                            f.setGroupCountdown(hour + "??????" + min + "??????");
                            break;
                        }
                    }
                }
            } catch (ParseException e) {
                System.out.println("???????????????????????????");
            }
        }
        return getDataTable(list);
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @GetMapping("/Transfer/{fmId}")
    public String Transfer(@PathVariable("fmId") String fmId, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        List<OgPerson> list = fmBizService.getPersonByTransfer(fmBiz);
        mmap.put("fmBiz", fmBiz);
        mmap.addAttribute("Person", list);
        return prefix_public + "/group/transfer";
    }

    /**
     * ?????????????????????
     */
    @PostMapping("/exportGroupHandleList")
    @ResponseBody
    public AjaxResult exportGroupHandleList(FmBiz fmBiz) {
        OgUser u = ShiroUtils.getOgUser();
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", u.getpId());
        fmBiz.setParams(reMap);
        List<FmBiz> list = fmBizService.selectGroupHandleList(fmBiz);
        ExcelUtil<FmBiz> util = new ExcelUtil<FmBiz>(FmBiz.class);
        return util.exportExcel(list, "???????????????");
    }

    /**
     * ????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/saveflowTransfer")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult saveflowTransfer(FmBiz fmBiz) {
        return fmBizService.saveflowTransfer(fmBiz);
    }

    /**
     * ?????????????????????
     */
    @PostMapping("/selectGroupAnalysisList")
    @ResponseBody
    public TableDataInfo selectGroupAnalysisList(FmBiz fmBiz) {
        startPage();

        OgUser u = ShiroUtils.getOgUser();
        Map<String, Object> reMap = new HashMap<>();
        try {
            if (fmBiz.getParams().containsKey("startCreatTime")) {
                String startCreatTime = fmBiz.getParams().get("startCreatTime").toString();
                if (StringUtils.isNotEmpty(startCreatTime)) {
                    String d1 = handleTimeYYYYMMDD(startCreatTime);
                    reMap.put("startCreatTime", d1 + "000000");
                }
            }
            if (fmBiz.getParams().containsKey("endCreatTime")) {
                String endCreatTime = fmBiz.getParams().get("endCreatTime").toString();
                if (StringUtils.isNotEmpty(endCreatTime)) {
                    String d2 = handleTimeYYYYMMDD(endCreatTime);
                    reMap.put("endCreatTime", d2 + "240000");
                }
            }
            if (fmBiz.getParams().containsKey("startDealTime")) {
                String startDealTime = fmBiz.getParams().get("startDealTime").toString();
                if (StringUtils.isNotEmpty(startDealTime)) {
                    String d1 = handleTimeYYYYMMDD(startDealTime);
                    reMap.put("startDealTime", d1 + "000000");
                }
            }
            if (fmBiz.getParams().containsKey("endDealTime")) {
                String endDealTime = fmBiz.getParams().get("endDealTime").toString();
                if (StringUtils.isNotEmpty(endDealTime)) {
                    String d1 = handleTimeYYYYMMDD(endDealTime);
                    reMap.put("endDealTime", d1 + "240000");
                }
            }
        } catch (Exception e) {
            throw new BusinessException("????????????????????????");
        }
        reMap.put("userId", u.getpId());
        fmBiz.setParams(reMap);
        List<FmSysDTime> list = fmBizService.selectGroupAnalysisList(fmBiz);

        return getDataTable(list);
    }

    /**
     * ?????????????????????
     */
    @PostMapping("/exroptGroupAnalysis")
    @ResponseBody
    public AjaxResult exroptGroupAnalysis(FmBiz fmBiz) {
        String isCurrentPage = (String) fmBiz.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        OgUser u = ShiroUtils.getOgUser();
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", u.getpId());
        fmBiz.setParams(reMap);
        List<FmSysDTime> list = fmBizService.selectGroupAnalysisList(fmBiz);
        ExcelUtil<FmSysDTime> util = new ExcelUtil<FmSysDTime>(FmSysDTime.class);
        return util.exportExcel(list, "???????????????");

    }

    /**
     * ?????????????????????????????????
     */
    @PostMapping("/selectFmBizCount")
    @ResponseBody
    public TableDataInfo selectFmBizCount(FmBiz fmBiz) throws ParseException {
        String startCreatTime = fmBiz.getParams().get("startCreatTime").toString();
        String endCreatTime = fmBiz.getParams().get("endCreatTime").toString();
        if (!"".equals(startCreatTime) && null != startCreatTime) {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startCreatTime);
            String d1 = new SimpleDateFormat("yyyyMMdd").format(date1);
            fmBiz.getParams().put("startCreatTime", d1 + "000000");
        } else {
            Date date = new Date();//??????????????????
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);//??????????????????????????????????????????????????????
            String time = DateUtils.handleTimeYYYYMMDDHHMMSS(calendar.getTime());
            fmBiz.getParams().put("startCreatTime", time.substring(0, 8) + "000000");
        }
        if (!"".equals(endCreatTime) && null != endCreatTime) {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endCreatTime);
            String d2 = new SimpleDateFormat("yyyyMMdd").format(date2);
            fmBiz.getParams().put("endCreatTime", d2 + "240000");
        } else {
            fmBiz.getParams().put("endCreatTime", DateUtils.dateTimeNow("yyyyMMdd") + 240000);
        }
        List<FmBizJJAppr> list = fmBizService.selectFmBizCount(fmBiz);
        return getDataTable(list);
    }

    /**
     * ?????????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmBiz fmBiz) throws Exception {
        String isCurrentPage = (String) fmBiz.getParams().get("currentPage");
        fmBizService.formatFmbiz(fmBiz);
        String[] issuefxIds = null;
        if (StringUtils.isNotEmpty((String) fmBiz.getParams().get("issuefxNo"))) {
            ImBizIssuefx imBizIssuefx = new ImBizIssuefx();
            imBizIssuefx.setIssuefxNo((String) fmBiz.getParams().get("issuefxNo"));
            List<ImBizIssuefx> list = iImBizIssuefxService.selectIssueList(imBizIssuefx);
            String issuefxId = "";
            if (list != null && list.size() > 0) {
                for (ImBizIssuefx imBizIssuefx1 : list) {
                    issuefxId += imBizIssuefx1.getIssuefxId() + ",";
                }
                issuefxIds = issuefxId.split(",");
            }
        }
        fmBiz.getParams().put("issuefxIds", issuefxIds);
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<FmBiz> list = fmBizService.selectFmBizListPager(fmBiz);
        ExcelUtil<FmBiz> util = new ExcelUtil<FmBiz>(FmBiz.class);
        return util.exportExcel(list, "???????????????");
    }

    /**
     * ???????????????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/exportjj")
    @ResponseBody
    public AjaxResult exportjj(FmBiz fmBiz) throws Exception {
        String startCreatTime = fmBiz.getParams().get("startCreatTime").toString();
        String endCreatTime = fmBiz.getParams().get("endCreatTime").toString();
        if (!"".equals(startCreatTime) && null != startCreatTime) {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(startCreatTime);
            String d1 = new SimpleDateFormat("yyyyMMdd").format(date1);
            fmBiz.getParams().put("startCreatTime", d1 + "000000");
        } else {
            Date date = new Date();//??????????????????
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -1);//?????????????????????
            String time = DateUtils.handleTimeYYYYMMDDHHMMSS(calendar.getTime());
            fmBiz.getParams().put("startCreatTime", time.substring(0, 8) + "000000");
        }
        if (!"".equals(endCreatTime) && null != endCreatTime) {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endCreatTime);
            String d2 = new SimpleDateFormat("yyyyMMdd").format(date2);
            fmBiz.getParams().put("endCreatTime", d2 + "240000");
        } else {
            fmBiz.getParams().put("endCreatTime", DateUtils.dateTimeNow("yyyyMMdd") + 240000);
        }
        List<FmBizJJAppr> list = fmBizService.selectFmBizCount(fmBiz);
        ExcelUtil<FmBizJJAppr> util = new ExcelUtil<FmBizJJAppr>(FmBizJJAppr.class);
        return util.exportExcel(list, "?????????????????????");
    }

    /**
     * ????????????????????????
     *
     * @param
     */
    @PostMapping("/getDealerDCount")
    @ResponseBody
    public TableDataInfo getDealerDCount(FmBiz fmBiz) throws Exception {
        List<FmBizCDealerD> list = new ArrayList<>();
        Map<String, Object> reMap = fmBiz.getParams();
        if (StringUtils.isNotEmpty(fmBiz.getParams().get("startDealTime").toString())) {
            String startDealTime = handleTimeYYYYMMDD(fmBiz.getParams().get("startDealTime").toString());
            reMap.put("startTime", startDealTime);
        } else {
            return getDataTable(list);
        }
        if (StringUtils.isNotEmpty(fmBiz.getParams().get("endDealTime").toString())) {
            String endDealTime = handleTimeYYYYMMDD(fmBiz.getParams().get("endDealTime").toString());
            reMap.put("endTime", endDealTime);
        } else {
            return getDataTable(list);
        }
        fmBiz.setParams(reMap);
        if (fmBiz.getParams().containsKey("type")) {
            String type = fmBiz.getParams().get("type").toString();
            if ("1".equals(type)) {//??????????????????
                list = fmBizService.getDealerDCount1(fmBiz);
            }
            if ("2".equals(type)) {//?????????????????????
                list = fmBizService.getDealerDCount2(fmBiz);
            }
        }
        return getDataTable(list);
    }

    /**
     * ????????????????????????
     */
    @Log(title = "??????????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/exportDcount")
    @ResponseBody
    public AjaxResult exportDcount(FmBiz fmBiz) {

        List<FmBizCDealerD> list = new ArrayList<>();
        Map<String, Object> reMap = fmBiz.getParams();
        String startDealTime = handleTimeYYYYMMDD(fmBiz.getParams().get("startDealTime").toString());
        String endDealTime = handleTimeYYYYMMDD(fmBiz.getParams().get("endDealTime").toString());
        reMap.put("startTime", startDealTime);
        reMap.put("endTime", endDealTime);
        fmBiz.setParams(reMap);

        String type = fmBiz.getParams().get("type").toString();

        if ("1".equals(type)) {//??????????????????
            list = fmBizService.getDealerDCount1(fmBiz);
        }
        if ("2".equals(type)) {//?????????????????????
            list = fmBizService.getDealerDCount2(fmBiz);
        }

        ExcelUtil<FmBizCDealerD> util = new ExcelUtil<FmBizCDealerD>(FmBizCDealerD.class);
        return util.exportExcel(list, "??????????????????");
    }

    /**
     * ???????????????ID?????????????????????
     */
    @PostMapping("/CmSjList/{id}")
    @ResponseBody
    public TableDataInfo getCmSj(@PathVariable("id") String id) {
        List<CmBizSingleSJ> list;
        if("mysql".equals(dataSourceType)){
            list = cmBizSingleSJMapper.selectListByFmNo2Mysql(id);
        }else{
            list = cmBizSingleSJMapper.selectListByFmNo2(id);
        }
        return getDataTable(list);
    }

    /**
     * ???????????????ID???????????????
     */
    @PostMapping("/ImWtList/{id}")
    @ResponseBody
    public TableDataInfo getImWt(@PathVariable("id") String id) {
        PubRelation pr = new PubRelation();
        List<ImBizIssuefx> imBizIssuefx = new ArrayList<>();
        pr.setObj1Id(id);
        pr.setType("14");
        List<PubRelation> list = iPubRelationService.selectPubRelationList(pr);
        if (!list.isEmpty()) {
            for (PubRelation prtion : list) {
                ImBizIssuefx cb = iImBizIssuefxService.selectImBizIssuefxById(prtion.getObj2Id());
                if (cb != null) {
                    imBizIssuefx.add(cb);
                }
            }
        }
        return getDataTable(imBizIssuefx);
    }

    /**
     * ?????????????????????????????????
     *
     * @param id ?????????ID
     * @return
     */
    @PostMapping("/getLimitNum")
    @ResponseBody
    public String getLimitNum(String id) {
        List<PubFlowLog> list = iPubFlowLogService.selectPubFlowLogAsc(id);//???????????????????????????????????????
        List<PubParaValue> list2 = iPubParaValueService.selectPubParaValueByParaName("Step_Number_Limit");//????????????????????????
        int num2 = Integer.parseInt(list2.get(0).getValue());
        String s = "0";
        if (list.size() > num2) {
            s = "1";
        }
        return s;
    }

    public FmBiz getTimeFormat(FmBiz fmBiz) {
        FmBiz fb = new FmBiz();
        Map<String, Object> reMap = new HashMap<>();
        if (fmBiz.getParams().containsKey("startCreatTime")) {
            String startCreatTime = fmBiz.getParams().get("startCreatTime").toString();
            if (StringUtils.isNotEmpty(startCreatTime)) {
                String d1 = handleTimeYYYYMMDD(startCreatTime);
                reMap.put("startCreatTime", d1 + "000000");
            }
        }
        if (fmBiz.getParams().containsKey("endCreatTime")) {
            String endCreatTime = fmBiz.getParams().get("endCreatTime").toString();
            if (StringUtils.isNotEmpty(endCreatTime)) {
                String d2 = handleTimeYYYYMMDD(endCreatTime);
                reMap.put("endCreatTime", d2 + "235959");
            }
        }
        fb.setParams(reMap);
        return fb;
    }

    /**
     * ?????????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/toAuto")
    @ResponseBody
    public AjaxResult toAuto(FmBiz fmBiz) {

        return success("????????????????????????");
    }

    /**
     * ???????????????ID????????????????????????
     */
    @PostMapping("/autoList/{id}")
    @ResponseBody
    public TableDataInfo autoList(@PathVariable("id") String id) {
        FmBizScript fb = new FmBizScript();
        fb.setFmId(id);
        List<FmBizScript> list = fmBizScriptService.selectFmBizScriptList(fb);
        List<FmBizScript> list2 = new ArrayList<>();
        if (!list.isEmpty()) {
            for (FmBizScript fbs : list) {
                fbs = fmBizScriptService.getAutoResult(fbs);
                list2.add(fbs);
            }
        }
        return getDataTable_ideal(list2);
    }

    public static void main(String[] args) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        long nd = 1000 * 24 * 60 * 60;//??????????????????
        long nh = 1000 * 60 * 60;//?????????????????????
        long nm = 1000 * 60;//?????????????????????
        long ns = 1000;//?????????????????????long diff;try {
        //???????????????????????????????????????
        try {
            long diff = sd.parse("20210120183500").getTime() - sd.parse("20210113102654").getTime();
            long hour = diff / nh;//?????????????????????
            long min = diff % nd % nh / nm;//?????????????????????
            System.out.println("???????????????" + hour + "??????" + min + "??????");

        } catch (ParseException e) {
            System.out.println("??????????????????");
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param fmBiz
     */
    public void saveTelBiz(FmBiz fmBiz) {
        if (fmBiz.getParams().containsKey("telId")) {
            if (StringUtils.isNotEmpty(fmBiz.getParams().get("telId").toString())) {
                String telId = fmBiz.getParams().get("telId").toString();
                PubRelation prt = new PubRelation();
                prt.setObj1Id(fmBiz.getFmId());
                prt.setObj2Id(telId);
                prt.setType("88");
                List<PubRelation> list = iPubRelationService.selectPubRelationList(prt);
                if (list.isEmpty()) {//??????????????????????????????(??????)
                    PubRelation pr = new PubRelation();
                    pr.setRelationId(UUID.getUUIDStr());
                    pr.setObj1Id(fmBiz.getFmId());//obj1???????????????ID
                    pr.setObj2Id(telId);//obj2???????????????ID
                    pr.setType("88");//????????????????????????
                    iPubRelationService.insertPubRelation(pr);//??????????????????
                    TelBiz tb = iOgPhoneService.selectPhoneById(telId);
                    tb.setFaultNo(fmBiz.getFaultNo());
                    tb.setState("4");//????????????
                    iOgPhoneService.updatePhone(tb);//???????????????????????????????????????
                    //?????????????????????????????????
                    TelFlowLog telBizFlowLog = new TelFlowLog();
                    telBizFlowLog.setLogId(UUID.getUUIDStr());
                    telBizFlowLog.setCreatTime(fmBiz.getCreatTime());
                    telBizFlowLog.setTelId(telId);
                    telBizFlowLog.setSerialNum("3");
                    telBizFlowLog.setOperationName("??????????????????");
                    OgPerson person = iOgPersonService.selectOgPersonById(fmBiz.getCreateId());
                    telBizFlowLog.setOperator(person.getpName());
                    telBizFlowLog.setOperatorTel(person.getMobilPhone());
                    telBizFlowLog.setState("?????????");
                    iTelFlowLogService.insertTelFlowLog(telBizFlowLog);
                }
            }
        }
    }

    public void setTelToDeal(FmBiz fmBiz) {
        PubRelation prt = new PubRelation();
        prt.setObj1Id(fmBiz.getFmId());
        prt.setType("88");
        List<PubRelation> list = iPubRelationService.selectPubRelationList(prt);
        if (!list.isEmpty()) {
            //?????????????????????????????????
            TelBiz tb = iOgPhoneService.selectPhoneById(list.get(0).getObj2Id());
            tb.setState("3");//???????????????????????????
            iOgPhoneService.updatePhone(tb);
            TelFlowLog telBizFlowLog = new TelFlowLog();
            telBizFlowLog.setLogId(UUID.getUUIDStr());
            telBizFlowLog.setCreatTime(DateUtils.dateTimeNow());
            telBizFlowLog.setTelId(tb.getTelid());
            telBizFlowLog.setSerialNum("4");
            telBizFlowLog.setOperationName("????????????");
            OgPerson person = iOgPersonService.selectOgPersonById(fmBiz.getCreateId());
            telBizFlowLog.setOperator(person.getpName());
            telBizFlowLog.setOperatorTel(person.getMobilPhone());
            telBizFlowLog.setState("?????????");
            iTelFlowLogService.insertTelFlowLog(telBizFlowLog);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @GetMapping("/fmbizAndIssueEdit")
    public String fmbizAndIssueEdit() {
        return prefix_public + "/fmbizAndIssueEdit";
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @GetMapping("/fmbizAndIssue")
    public String fmbizAndIssue() {
        return prefix_public + "/fmbizAndIssue";
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/relation/{fmId}/{flag}")
    public String relation(@PathVariable("fmId") String fmId, @PathVariable("flag") String flag, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        Map<String, Object> reMap = getReMap(fmBiz);
        fmBiz.setParams(reMap);
        mmap.put("fmBiz", fmBiz);
        mmap.put("flag", flag);
        return prefix_public + "/relation";
    }

    @PostMapping("/getFmBizAndIssueList")
    @ResponseBody
    public TableDataInfo getFmBizAndIssueList(FmBiz fmBiz) {
        return getDataTable(fmBizService.getFmBizAndIssueList(fmBiz));
    }

    @PostMapping("/relationIssue")
    @ResponseBody
    public AjaxResult relationIssue(FmBiz fmBiz) {
        FmBiz f = fmBizService.selectFmBizById(fmBiz.getFmId());
        fmBiz.setGroupId(f.getGroupId());
        return iFmbizAndIssueService.relationIssue(fmBiz);
    }

    /**
     * ????????????????????????
     */
    @Log(title = "??????????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/exportRelationIssue")
    @ResponseBody
    public AjaxResult exportRelationIssue(FmBiz fmBiz) throws Exception {
        String flag = "2";
        String isCurrentPage = (String) fmBiz.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            flag = "1";
        }
        List<FmBiz> list = fmBizService.getFmBizAndIssueListExport(fmBiz, flag);
        ExcelUtil<FmBiz> util = new ExcelUtil<FmBiz>(FmBiz.class);
        return util.exportExcel(list, "??????????????????");
    }

    @PostMapping("/ifynFmBiz")
    @ResponseBody
    public String ifynFmBiz(String id) {
        String s = "0";
        FmBiz fmBiz = fmBizService.selectFmBizById(id);
        if ("1".equals(fmBiz.getIfYn())) {
            s = "1";
        }
        return s;
    }

    /**
     * ????????????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/getFmBizAndIssueEdit")
    @ResponseBody
    public TableDataInfo getFmBizAndIssueEdit(FmBiz fmBiz) {
        return getDataTable(fmBizService.getFmBizAndIssueEdit(fmBiz));
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @return
     */
    @GetMapping("/relationEdit/{fmId}")
    public String relationEdit(@PathVariable("fmId") String fmId, ModelMap mmap) {
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        Map<String, Object> reMap = getReMap(fmBiz);
        fmBiz.setParams(reMap);
        mmap.put("fmBiz", fmBiz);
        return prefix_public + "/relationEdit";
    }

    /**
     * ????????????????????????
     *
     * @param fmBiz
     * @return
     */
    @PostMapping("/editRelationIssue")
    @ResponseBody
    public AjaxResult editRelationIssue(FmBiz fmBiz) {
        return iFmbizAndIssueService.editRelationIssue(fmBiz);
    }

    /**
     * ???????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.DELETE)
    @PostMapping("/updateState")
    @ResponseBody
    public AjaxResult updateState(String fmId) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String userName = ShiroUtils.getOgUser().getUsername();
        FmBiz fmBiz = new FmBiz();
        fmBiz.setFmId(fmId);
        fmBiz.setDealerId(ShiroUtils.getOgUser().getUserId());
        fmBiz.setDealName(ShiroUtils.getOgUser().getUsername());
        fmBiz.setDealTime(sdf.format(date));
        fmBiz.setEndTime(sdf.format(date));
        fmBiz.setCurrentState("09");
        fmBiz.setDealMode("01");
        fmBiz.setSeriousLev("1");
        fmBiz.setDealDescription(userName+"???????????????");
        return toAjax(fmBizService.updateFmBiz(fmBiz));
    }

}
