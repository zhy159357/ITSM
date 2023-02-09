package com.ruoyi.activiti.controller.itsm.sjbg;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.constants.FmSwConstants;
import com.ruoyi.activiti.domain.*;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
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
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.framework.web.service.DictService;
import com.ruoyi.system.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.DateUtils.timeToTimeMillis;

@Controller
@RequestMapping("/bg/sjbg")
@Transactional(rollbackFor = Exception.class)
public class SJBGController extends BaseController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SJBGController.class);

    private String prefix = "bg/sjbg";


    @Autowired
    private CmBizSingleSJService cmBizSingleSJService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private ISysWorkService sysWorkService;

    @Autowired
    private IFmBizService fmBizService;

    @Autowired
    private DictService dictService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private IPubFlowLogService pubFlowLogService;

    @Autowired
    private IPubAttachmentService iPubAttachmentService;

    @Value("${cntxtag.enabled}")
    private String cntxtag;

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
     * 我的数据变更单页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping()
    public String sjbg(ModelMap modelMap) {
        //获取当前登陆账号的关联的人员Id
        String personId = ShiroUtils.getOgUser().getpId();
        modelMap.addAttribute("personId", personId);
        Map<String, Object> map = sjbgAddPage();
        modelMap.put("list", map.get("list"));
        modelMap.put("cntxtag", cntxtag);
        return prefix + "/sjbg";
    }

    /**
     * 我的数据变更单页面列表数据
     *
     * @param cmBizSingleSJVo
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CmBizSingleSJVo cmBizSingleSJVo) {
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getStartCreateTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(cmBizSingleSJVo.getStartCreateTime());
            cmBizSingleSJVo.setStartCreateTime(parseStart+"000000");
        }
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getEndCreateTime())){
            String parseEnd = DateUtils.handleTimeYYYYMMDD(cmBizSingleSJVo.getEndCreateTime());
            cmBizSingleSJVo.setEndCreateTime(parseEnd+"240000");
        }

        if(StringUtils.isNotNull(cmBizSingleSJVo.getParams().get("myFlag"))){
            String myFlag = cmBizSingleSJVo.getParams().get("myFlag").toString();

            if (!StringUtils.isEmpty(myFlag)) {
                if ("1".equals(myFlag)) {//我的标识
                    cmBizSingleSJVo.setChangeApplicant(ShiroUtils.getOgUser().getpId());//默认我创建的 创建人是当前登陆人
                }
                //我处理的
                if ("2".equals(myFlag)) {
                    PubFlowLog pub = new PubFlowLog();
                    pub.setPerformerId(ShiroUtils.getOgUser().getUserId());
                    pub.setLogType("FmSJBG");
                    pub.setTaskName("实施");
                    List<PubFlowLog> pgList = pubFlowLogService.selectPubFlowLogList(pub);
                    List<CmBizSingleSJVo> listFmSw = new ArrayList<>();
                    Set<String> idList=new HashSet<>();
                    CmBizSingleSJVo is =  cmBizSingleSJVo;
                    for (PubFlowLog pb : pgList) {
                        //去重
                        if (!idList.contains(pb.getBizId())) {
                            idList.add(pb.getBizId());
                            if(StringUtils.isNotEmpty(pb.getBizId())){
                                is.setChangeSingleId(pb.getBizId());
                                is.setChangeApplicant("");
                            }
                            listFmSw.addAll(cmBizSingleSJService.list(is));
                        }
                    }
                    return getDataTable_ideal(listFmSw);
                }
            }
        }

        startPage();
        List<CmBizSingleSJVo> list = cmBizSingleSJService.list(cmBizSingleSJVo);
        return getDataTable(list);
    }



    /**
     * 我的数据变更单添加页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/add/{flag}")
    public String add(ModelMap modelMap,@PathVariable("flag") String flag) {
        Map<String, Object> map = sjbgAddPage();
        modelMap.put("sjbgNum", map.get("sjbgNum")); //数据变更单号
        modelMap.put("list", map.get("list"));
        modelMap.put("changeApplicant", map.get("changeApplicant"));
        modelMap.put("changeSingleId",UUID.getUUIDStr());
        modelMap.put("flag", flag);
        return prefix + "/add";
    }

    /**
     * 我的数据变更单业务事件单转数据变更单添加页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/parseAdd/{flag}/{fmId}")
    public String parseAdd(ModelMap modelMap,@PathVariable("flag") String flag,@PathVariable("fmId") String fmId) {
        //事件单单号
        FmBiz fmBiz = fmBizService.selectFmBizById(fmId);
        if(fmBiz != null){
            if(!"04".equals(fmBiz.getCurrentState())){
                throw new BusinessException("该事件单已处理请刷新列表后重新操作");
            }else{
                String sysid = fmBiz.getSysid();
                List<OgGroup> ogGroups = sysWorkService.selectGroupBySysId(sysid);
                Map<String, Object> map = sjbgAddPage();
                modelMap.put("changeSingleId",UUID.getUUIDStr());
                modelMap.put("sjbgNum", map.get("sjbgNum")); //数据变更单号
                modelMap.put("list", map.get("list"));
                modelMap.put("changeApplicant", map.get("changeApplicant"));
                modelMap.put("flag", flag);
                modelMap.put("fmBiz", fmBiz);
                modelMap.put("ogGroups", ogGroups);
            }
        }else{
            throw new BusinessException("该事件单已处理请刷新列表后重新操作");
        }
        return prefix + "/add";
    }


    /**
     * 添加页面数据获取
     * @return
     */
    private Map<String,Object> sjbgAddPage(){
        Map<String,Object> resMap = new HashMap<>();
        String bizType = "SJBG";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        String sjbgNum = bizType + nowDateStr + numStr;
        resMap.put("sjbgNum",sjbgNum);

        List<OgOrg> ogOrgs = sysDeptService.selectDeptList(null);
        List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());
        resMap.put("list",list);

        String pid = ShiroUtils.getOgUser().getpId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pid);
        resMap.put("changeApplicant",ogPerson.getpName());
        return  resMap;

    }

    /**
     * 数据变list更单添加操作
     */
    @Log(title = "数据变更单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CmBizSingleSJVo cmBizSingleSJVo) {
        String smsText = "";
        OgPerson person;
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());

        CmBizSingleSJ cmBizSingleSJ = new CmBizSingleSJ();
        BeanUtils.copyProperties(cmBizSingleSJVo,cmBizSingleSJ);
        String personId = ShiroUtils.getOgUser().getpId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(personId);
        if(ogPerson != null ){
            cmBizSingleSJ.setCreateOrgId(ogPerson.getOrgId());
        }
        //变更申请人
        cmBizSingleSJ.setChangeApplicant(personId);
        cmBizSingleSJ.setCreateTime(DateUtils.dateTimeNow());
        cmBizSingleSJ.setInvalidationMark("1");
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getExpectStartTime())){
            cmBizSingleSJ.setExpectStartTime(handleTimeYYYYMMDDHHMMSS(cmBizSingleSJVo.getExpectStartTime()));
        }
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getExpectEndTime())){
            cmBizSingleSJ.setExpectEndTime(handleTimeYYYYMMDDHHMMSS(cmBizSingleSJVo.getExpectEndTime()));
        }

        //判断当前是否为暂存还是提交状态
        if("commit".equals(cmBizSingleSJVo.getFlag())){
            String businessKey=cmBizSingleSJ.getChangeSingleId();
            String processDefinitionKey="FmSJBG";
            reMap.put("createId",ShiroUtils.getUserId());
            //判断当前是手工创建还是业务事件单转数据变更
            if("1".equals(cmBizSingleSJVo.getChangeSingleType())){
                //业务单转数据变更
                reMap.put("reCode","2");
                reMap.put("dealId",cmBizSingleSJ.getProcheckManager());
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子已提交，请登录运维管理平台审核。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getProcheckManager());// 获取短信接收人  省处理人  procheckManager
            }else{
                //手工创建
                reMap.put("reCode","1");
                reMap.put("auditId",cmBizSingleSJ.getCheckManager());
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子待审核，请登录运维管理平台审核。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getCheckManager());// 获取短信接收人   业务审核人  checkManager
            }

            try{
                cmBizSingleSJService.insertSjbg(cmBizSingleSJ);
                activitiCommService.startProcess(businessKey,processDefinitionKey,reMap);
                if("1".equals(cmBizSingleSJVo.getChangeSingleType())) {
                    //如果为业务事件单转数据变更则修改事件单处理方式
                    if ("1".equals(cmBizSingleSJ.getChangeSingleType())) {
                        FmBiz fmBiz = fmBizService.selectFmBizByFaultNo(cmBizSingleSJ.getFmNo());
                        if (fmBiz != null) {
                            fmBiz.setDealMode("04");//修改事件单处理方式为数据变更处理
                            fmBizService.updateFmBiz(fmBiz);
                        }
                    }
                }
            }catch (Exception e){
                log.error("数据变更单流程创建失败: "+e.getMessage());
                throw  new BusinessException("数据变更单流程创建失败,请刷新页面进行重试");
            }
            try{
                sendSms(smsText, person);
            }catch (Exception e){
                log.error("新增数据变更单短信发送失败: "+e.getMessage());
            }
        }else {

            try {
                //暂存
                cmBizSingleSJService.insertSjbg(cmBizSingleSJ);
                //如果为业务事件单转数据变更则修改事件单处理方式
                if ("1".equals(cmBizSingleSJ.getChangeSingleType())) {
                    FmBiz fmBiz = fmBizService.selectFmBizByFaultNo(cmBizSingleSJ.getFmNo());
                    if (fmBiz != null) {
                        fmBiz.setDealMode("04");//修改事件单处理方式为数据变更处理
                        fmBizService.updateFmBiz(fmBiz);
                    }
                }
            } catch (Exception e) {
                log.error("数据变更单暂存失败: " + e.getMessage());
                return AjaxResult.error("数据变更单暂存失败,请刷新页面进行重试");
            }

        }
        return  AjaxResult.success("数据变更单创建成功");
    }

    /**
     * 数据变更单查看详情页面
     *
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/detailsNoClose/{id}")
    public String detailsNoClose(@PathVariable("id") String id, ModelMap mmap) {
        //获取省份信息
        List<OgOrg> ogOrgs = sysDeptService.selectDeptList(null);
        List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());
        mmap.addAttribute("list", list);
        //根据数据变更单查询对应的变更单信息
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);

        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getChangeOrgtype())){
            String[] orgs = cmBizSingleSJVo.getChangeOrgtype().split(",");
            List<OgOrg> arr1 = new ArrayList<>();
            for (String orgId : orgs) {
                OgOrg ogOrg = sysDeptService.selectDeptById(orgId);
                arr1.add(ogOrg);
            }
            String s = JSON.toJSONString(arr1);
            mmap.addAttribute("arr1", s);
        }

        mmap.addAttribute("isChangeOPSFlag", cmBizSingleSJVo.getIsChangeOPS());
        mmap.addAttribute("sjbg", cmBizSingleSJVo);
        //根据对应的数据变更单创建类型加载不同的详情信息
        String flagShow = cmBizSingleSJVo.getChangeSingleType();
        if("1".equals(flagShow)){
            //当前为业务单转问题单
            mmap.addAttribute("flagShow", flagShow);
        }else{
            //当前为手工创建
            mmap.addAttribute("flagShow", flagShow);
        }
        return prefix + "/detailsNoClose";
    }

    /**
     * 数据变单查看详情页面
     *
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") String id, ModelMap mmap) {
        //获取省份信息
        List<OgOrg> ogOrgs = sysDeptService.selectDeptList(null);
        List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());
        mmap.addAttribute("list", list);
        //根据数据变更单查询对应的变更单信息
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);

        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getChangeOrgtype())){
            String[] orgs = cmBizSingleSJVo.getChangeOrgtype().split(",");
            List<OgOrg> arr1 = new ArrayList<>();
            for (String orgId : orgs) {
                OgOrg ogOrg = sysDeptService.selectDeptById(orgId);
                arr1.add(ogOrg);
            }
            String s = JSON.toJSONString(arr1);
            mmap.addAttribute("arr1", s);
        }
        //计划变更开始时间与结束时间转换
        String expectStartTime = cmBizSingleSJVo.getExpectStartTime();
        if(StringUtils.isNotEmpty(expectStartTime)){
            cmBizSingleSJVo.setExpectStartTime(DateUtils.formatDateStr(expectStartTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectStartTime",cmBizSingleSJVo.getExpectStartTime());
        }
        String expectEndTime = cmBizSingleSJVo.getExpectEndTime();
        if(StringUtils.isNotEmpty(expectEndTime)){
            cmBizSingleSJVo.setExpectEndTime(DateUtils.formatDateStr(expectEndTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectEndTime",cmBizSingleSJVo.getExpectEndTime());
        }

        mmap.addAttribute("isChangeOPSFlag", cmBizSingleSJVo.getIsChangeOPS());
        mmap.addAttribute("sjbg", cmBizSingleSJVo);
        //根据对应的数据变更单创建类型加载不同的详情信息
        String flagShow = cmBizSingleSJVo.getChangeSingleType();
        if("1".equals(flagShow)){
            //当前为业务单转问题单
            mmap.addAttribute("flagShow", flagShow);
        }else{
            //当前为手工创建
            mmap.addAttribute("flagShow", flagShow);
        }
        return prefix + "/details";
    }

    /**
     * 逻辑删除我的数据变更单
     *
     * @param id
     * @return
     */
    @PostMapping("/updateById")
    @ResponseBody
    public AjaxResult updateById(String id) {
        return toAjax(cmBizSingleSJService.updateById(id));
    }

    /**
     * 我的数据变更单导出
     */
    @Log(title = "我的数据变更单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CmBizSingleSJVo cmBizSingleSJVo) {
        String isCurrentPage = (String) cmBizSingleSJVo.getParams().get("currentPage");
        cmBizSingleSJVo.setChangeApplicant(ShiroUtils.getUserId());
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getStartCreateTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(cmBizSingleSJVo.getStartCreateTime());
            cmBizSingleSJVo.setStartCreateTime(parseStart+"000000");
        }
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getEndCreateTime())){
            String parseEnd = DateUtils.handleTimeYYYYMMDD(cmBizSingleSJVo.getEndCreateTime());
            cmBizSingleSJVo.setEndCreateTime(parseEnd+"240000");
        }

        if(StringUtils.isNotNull(cmBizSingleSJVo.getParams().get("myFlag"))){
            String myFlag = cmBizSingleSJVo.getParams().get("myFlag").toString();

            if (!StringUtils.isEmpty(myFlag)) {
                if ("1".equals(myFlag)) {//我的标识
                    cmBizSingleSJVo.setChangeApplicant(ShiroUtils.getOgUser().getpId());//默认我创建的 创建人是当前登陆人
                }
                //我处理的
                if ("2".equals(myFlag)) {
                    PubFlowLog pub = new PubFlowLog();
                    pub.setPerformerId(ShiroUtils.getOgUser().getUserId());
                    pub.setLogType("FmSJBG");
                    pub.setTaskName("实施");
                    List<PubFlowLog> pgList = pubFlowLogService.selectPubFlowLogList(pub);
                    List<CmBizSingleSJVo> listFmSw = new ArrayList<>();
                    Set<String> idList=new HashSet<>();
                    CmBizSingleSJVo is =  cmBizSingleSJVo;
                    for (PubFlowLog pb : pgList) {
                        //去重
                        if (!idList.contains(pb.getBizId())) {
                            idList.add(pb.getBizId());
                            if(StringUtils.isNotEmpty(pb.getBizId())){
                                is.setChangeSingleId(pb.getBizId());
                                is.setChangeApplicant("");
                            }
                            listFmSw.addAll(cmBizSingleSJService.list(is));
                        }
                    }
                    if ("currentPage".equals(isCurrentPage)) {
                        startPage();
                        ExcelUtil<CmBizSingleSJVo> util = new ExcelUtil<CmBizSingleSJVo>(CmBizSingleSJVo.class);
                        return util.exportExcel(listFmSw, "数据变更单");
                    } else {
                        PageDomain pageDomain = TableSupport.buildPageRequest();
                        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
                        PageHelper.orderBy(orderBy);
                        ExcelUtil<CmBizSingleSJVo> util = new ExcelUtil<CmBizSingleSJVo>(CmBizSingleSJVo.class);
                        return util.exportExcel(listFmSw, "数据变更单");
                    }

                }
            }
        }

        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<CmBizSingleSJVo> list = cmBizSingleSJService.list(cmBizSingleSJVo);
            ExcelUtil<CmBizSingleSJVo> util = new ExcelUtil<CmBizSingleSJVo>(CmBizSingleSJVo.class);
            return util.exportExcel(list, "数据变更单");
        } else {
            PageDomain pageDomain = TableSupport.buildPageRequest();
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
            List<CmBizSingleSJVo> list = cmBizSingleSJService.list(cmBizSingleSJVo);
            ExcelUtil<CmBizSingleSJVo> util = new ExcelUtil<CmBizSingleSJVo>(CmBizSingleSJVo.class);
            return util.exportExcel(list, "数据变更单");
        }



    }



    /**
     * 查询数据变更单导出
     */
    @Log(title = "我的数据变更单", businessType = BusinessType.EXPORT)
    @PostMapping("/queryExport")
    @ResponseBody
    public AjaxResult queryExport(CmBizSingleSJVo cmBizSingleSJVo) {
        String isCurrentPage = (String) cmBizSingleSJVo.getParams().get("currentPage");
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getStartCreateTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(cmBizSingleSJVo.getStartCreateTime());
            cmBizSingleSJVo.setStartCreateTime(parseStart+"000000");
        }
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getEndCreateTime())){
            String parseEnd = DateUtils.handleTimeYYYYMMDD(cmBizSingleSJVo.getEndCreateTime());
            cmBizSingleSJVo.setEndCreateTime(parseEnd+"240000");
        }
        if (!StringUtils.isEmpty(cmBizSingleSJVo.getStartcloseTime())){
            cmBizSingleSJVo.setStartcloseTime(cmBizSingleSJVo.getStartcloseTime().replaceAll("-", "")+"000000");
        }

        if (!StringUtils.isEmpty(cmBizSingleSJVo.getEndcloseTime())) {
            cmBizSingleSJVo.setEndcloseTime(cmBizSingleSJVo.getEndcloseTime().replaceAll("-", "")+"240000");
        }
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        OgOrg ogOrg = sysDeptService.selectDeptById(ogPerson.getOrgId());
        Map map = new HashMap<>();
        map.put("userId",pId);
        List<OgGroup> groupList = workService.selectGroupByGposition(map);
        if ("/000000000/".equals(ogOrg.getLevelCode())) {// 邮政金融运维人员，查看全部

        } else if (ogOrg.getLevelCode().startsWith("/000000000/010000888/")) {// 全国中心，查看全部

            if (ogOrg.getLevelCode().startsWith("/000000000/010000888/010900888/")) {//代维公司
                cmBizSingleSJVo.setCreateOrgId(ogOrg.getOrgId());
                //判断当前登录人的工作组与添加时选择的工作组是否相同
                if(StringUtils.isNotEmpty(groupList)){
                    cmBizSingleSJVo.getParams().put("groupList",groupList);
                }
            }
        } else {
            cmBizSingleSJVo.setLevelCode(ogOrg.getLevelCode().substring(0,20));

        }
        if (StringUtils.isNotEmpty(cmBizSingleSJVo.getChangeSingleStatus())) {
            String[] ChangeSingleStatus = cmBizSingleSJVo.getChangeSingleStatus().split(",");
            cmBizSingleSJVo.getParams().put("state", ChangeSingleStatus);
        }


        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }

        List<CmBizSingleSJVo> list = cmBizSingleSJService.listByOrg(cmBizSingleSJVo);
        ExcelUtil<CmBizSingleSJVo> util = new ExcelUtil<CmBizSingleSJVo>(CmBizSingleSJVo.class);
        return util.exportExcel(list, "数据变更单");
    }


    /**
     * 我的数据变更单修改页面
     * @param modelMap
     * @return
     */
    /**
     * 待提交修改事务事件单页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {

        //获取我的数据变更单当前的数据单信息
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);

        //当前用户的机构信息
        String pid = ShiroUtils.getOgUser().getpId();

        OgPerson ogPerson = ogPersonService.selectOgPersonById(pid);

        modelMap.put("changeApplicant", ogPerson.getpName());
        OgOrg ogOrg = sysDeptService.selectDeptById(ogPerson.getOrgId());
        String levelCode = ogOrg.getLevelCode();
        //审核人
        List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, "0101");
        modelMap.put("checkList", checkList);
        modelMap.put("cmBizSingleSJVo", cmBizSingleSJVo);
        //计划变更开始时间与结束时间转换
        String expectStartTime = cmBizSingleSJVo.getExpectStartTime();
        if(StringUtils.isNotEmpty(expectStartTime)){
            cmBizSingleSJVo.setExpectStartTime(DateUtils.formatDateStr(expectStartTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            modelMap.addAttribute("expectStartTime",cmBizSingleSJVo.getExpectStartTime());
        }
        String expectEndTime = cmBizSingleSJVo.getExpectEndTime();
        if(StringUtils.isNotEmpty(expectEndTime)){
            cmBizSingleSJVo.setExpectEndTime(DateUtils.formatDateStr(expectEndTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            modelMap.addAttribute("expectEndTime",cmBizSingleSJVo.getExpectEndTime());
        }


        //获取当前所属系统的所有工作组
        List<OgGroup> groupList = sysWorkService.selectGroupBySysId(cmBizSingleSJVo.getSysId());
        modelMap.put("groupList", groupList);
        //根据对应的数据变更单创建类型加载不同的详情信息
        String flagShow = cmBizSingleSJVo.getChangeSingleType();
        if(StringUtils.isNotEmpty(flagShow)){
            if("1".equals(flagShow)){
                //当前为业务单转问题单
                modelMap.addAttribute("flagShow", flagShow);
                FmBiz fmBiz = fmBizService.selectFmBizByFaultNo(cmBizSingleSJVo.getFmNo());
                if(fmBiz != null){
                    modelMap.addAttribute("fmBiz", fmBiz);
                }
            }else{
                //当前为手工创建
                modelMap.addAttribute("flagShow", flagShow);
            }
        }

        return prefix + "/edit";
    }


    /**
     * 我的数据变更单修改保存
     */
    @Log(title = "数据变更单", businessType = BusinessType.INSERT)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CmBizSingleSJVo cmBizSingleSJVo) {
        String smsText = "";
        OgPerson person;
        Map<String,Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());

        CmBizSingleSJ cmBizSingleSJ = new CmBizSingleSJ();
        BeanUtils.copyProperties(cmBizSingleSJVo,cmBizSingleSJ);
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getExpectStartTime())){
            cmBizSingleSJ.setExpectStartTime(handleTimeYYYYMMDDHHMMSS(cmBizSingleSJVo.getExpectStartTime()));
        }
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getExpectEndTime())){
            cmBizSingleSJ.setExpectEndTime(handleTimeYYYYMMDDHHMMSS(cmBizSingleSJVo.getExpectEndTime()));
        }

        //判断当前是否为暂存还是提交状态
        if("commit".equals(cmBizSingleSJVo.getFlag())){
            String businessKey=cmBizSingleSJ.getChangeSingleId();
            String processDefinitionKey="FmSJBG";
            reMap.put("createId",ShiroUtils.getOgUser().getUserId());
            //判断当前是手工创建还是业务事件单转数据变更
            if("1".equals(cmBizSingleSJVo.getChangeSingleType())){
                //业务单转数据变更
                reMap.put("reCode","2");
                reMap.put("dealId",cmBizSingleSJ.getProcheckManager());
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子已提交，请登录运维管理平台审核。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getProcheckManager());// 获取短信接收人   省处理人  procheckManager
            }else{
                //手工创建
                reMap.put("reCode","1");
                reMap.put("auditId",cmBizSingleSJ.getCheckManager());
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子待审核，请登录运维管理平台审核。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getCheckManager());// 获取短信接收人  业务审核人   checkManager
            }
            try{
                cmBizSingleSJService.updateEditSjbg(cmBizSingleSJ);
                activitiCommService.startProcess(businessKey,processDefinitionKey,reMap);
            }catch (Exception e){
                log.error("数据变更单修改提交失败: "+e.getMessage());
                throw new BusinessException("数据变更单修改提交失败,请刷新页面进行重试");
            }
            try{
                sendSms(smsText, person);
            }catch (Exception e){
                log.error("修改数据变更单短信发送失败: "+e.getMessage());
            }
            return AjaxResult.success("数据变更单提交成功", cmBizSingleSJ.getChangeCode());
        }

        try{
            cmBizSingleSJService.updateEditSjbg(cmBizSingleSJ);
        }catch (Exception e){
            log.error("修改数据变更单暂存失败: "+e.getMessage());
            return AjaxResult.error("修改数据变更单暂存失败,请刷新页面进行重试");
        }
        return AjaxResult.success("修改数据变更单暂存成功", cmBizSingleSJ.getChangeSingleId());
    }


    /**
     * 手动创建的变更单进行所属机构信息的查询
     * @param changeSingleId
     * @return
     */
    @GetMapping("/getChangeOrgType/{changeSingleId}")
    @ResponseBody
    public AjaxResult getChangeOrgType(@PathVariable("changeSingleId") String changeSingleId) {
        AjaxResult ajaxResult = new AjaxResult();
        Map map = new HashMap();

        //获取所有的省份信息
        //获取省份信息
        List<OgOrg> ogOrgs = sysDeptService.selectDeptList(null);
        List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());
        map.put("listdata",list);
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(changeSingleId);
        String changeOrgtypes = cmBizSingleSJVo.getChangeOrgtype();
        if(StringUtils.isNotEmpty(changeOrgtypes)){
            map.put("changeOrgtypes",changeOrgtypes);
        }

        ajaxResult.put("map",map);
        return ajaxResult;
    }

    /**
     * 处理数据变更单列表页面
     * @param modelMap
     * @return
     */

    @GetMapping("/deal")
    public String deal(ModelMap modelMap){
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        modelMap.put("pId",ogUser.getpId());
        return prefix+"/deal";
    }

    /**
     * 查询当前用户的待处理的任务
     * @param cmBizSingleSJVo
     * @return
     */
    @PostMapping("/deallist")
    @ResponseBody
    public TableDataInfo deallist(CmBizSingleSJVo cmBizSingleSJVo) {
        List<Map<String,Object>> reList =activitiCommService.userTask("FmSJBG","deal");
        List<OgGroup> userGroupList= sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String,Object>> regList=new ArrayList<>();
        if(userGroupList.size()>0){
            regList=activitiCommService.groupTasks("FmSJBG","deal");
            reList.addAll(regList);
        }
        List<String> changeSingleIds = new ArrayList<>();
        for(Map<String,Object> map:reList){
            if("deal".equals(map.get("description"))){
                String business_key=map.get("businesskey").toString();
                if(business_key!=null){
                    if(business_key!=null){
                        changeSingleIds.add(business_key);
                    }
                }
            }

        }
        List<CmBizSingleSJVo> resultlist = null;
        if(changeSingleIds!=null && changeSingleIds.size()>0){
            cmBizSingleSJVo.setIds(changeSingleIds);
            startPage();
            resultlist =  cmBizSingleSJService.selectListByTask(cmBizSingleSJVo);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());
    }

    /**
     * 当前用户的待处理的任务处理页面
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/dealEdit/{id}")
    public String dealEdit(@PathVariable("id") String id, ModelMap mmap){
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);
        //计划变更开始时间与结束时间转换
        String expectStartTime = cmBizSingleSJVo.getExpectStartTime();
        if(StringUtils.isNotEmpty(expectStartTime)){
            cmBizSingleSJVo.setExpectStartTime(DateUtils.formatDateStr(expectStartTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectStartTime",cmBizSingleSJVo.getExpectStartTime());
        }
        String expectEndTime = cmBizSingleSJVo.getExpectEndTime();
        if(StringUtils.isNotEmpty(expectEndTime)){
            cmBizSingleSJVo.setExpectEndTime(DateUtils.formatDateStr(expectEndTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectEndTime",cmBizSingleSJVo.getExpectEndTime());
        }

        //获取省份信息
        List<OgOrg> ogOrgs = sysDeptService.selectDeptList(null);
        List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());
        mmap.addAttribute("list", list);
        //根据数据变更单查询对应的变更单信息
        String[] orgs = cmBizSingleSJVo.getChangeOrgtype().split(",");
        List<OgOrg> arr1 = new ArrayList<>();
        for (String orgId : orgs) {
            OgOrg ogOrg = sysDeptService.selectDeptById(orgId);
            arr1.add(ogOrg);
        }

        mmap.addAttribute("sjbg", cmBizSingleSJVo);
        String s = JSON.toJSONString(arr1);
        mmap.addAttribute("arr1", s);
        mmap.addAttribute("flagShow", cmBizSingleSJVo.getChangeSingleType());
        mmap.addAttribute("isChangeOPSFlag", cmBizSingleSJVo.getIsChangeOPS());
        return prefix + "/deal_option";
    }


    /**
     * 处理数据变更单 处理通过或退出或否决
     * @param cmBizSingleSJ
     * @return
     */
    @PostMapping("/dealPassOrReturn")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult dealPassOrReturn(CmBizSingleSJ cmBizSingleSJ){
        String smsText = "";
        OgPerson person;
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());

        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(cmBizSingleSJ.getChangeSingleId());
        String businessKey=cmBizSingleSJVo.getChangeSingleId();
        String processDefinitionKey="FmSJBG";
        reMap.put("businessKey",businessKey);
        reMap.put("processDefinitionKey",processDefinitionKey);
        //状态判断
        if("14".equals(cmBizSingleSJVo.getChangeSingleStatus()) || "16".equals(cmBizSingleSJVo.getChangeSingleStatus())){

            //1.当前状态处理完成待审核
            if("02".equals(cmBizSingleSJ.getChangeSingleStatus())){
                //处理完成
                reMap.put("reCode","1");
                reMap.put("auditId",cmBizSingleSJVo.getCheckManager());
                String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
                reMap.put("comment",logPerformDesc);

                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子待审核，请登录运维管理平台审核。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getCheckManager());// 获取短信接收人    业务审核  checkManager
                try{
                    cmBizSingleSJService.updateSjbg(cmBizSingleSJ);
                    activitiCommService.complete(reMap);
                }catch (Exception e){
                    log.error("数据变更单处理通过失败: "+e.getMessage());
                    throw new BusinessException("数据变更单处理通过失败,请刷新页面进行重试");
                }

                try{
                    sendSms(smsText, person);
                }catch (Exception e){
                    log.error("处理通过短信发送失败: "+e.getMessage());
                }
                return AjaxResult.success("处理成功");

            }else if("06".equals(cmBizSingleSJ.getChangeSingleStatus())) {
                //2.当前操作退回操作
                reMap.put("reCode","2");
                reMap.put("createId",cmBizSingleSJVo.getChangeApplicant());
                String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
                reMap.put("comment",logPerformDesc);
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子已退回，请登录运维管理平台查看。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getChangeApplicant());// 获取短信接收人  创建人  changeApplicant
                try{
                    cmBizSingleSJService.updateSjbg(cmBizSingleSJ);
                    activitiCommService.complete(reMap);
                }catch (Exception e){
                    log.error("数据变更单处理退回通过失败: "+e.getMessage());
                    throw new BusinessException("数据变更单处理退回失败,请刷新页面进行重试");
                }
                try{
                    sendSms(smsText, person);
                }catch (Exception e){
                    log.error("处理退回短信发送失败: "+e.getMessage());
                }
                return AjaxResult.success("退回成功");
            }else{
                if("21".equals(cmBizSingleSJVo.getChangeSingleStatus())){
                    return AjaxResult.error("该数据变更单已否决，请刷新列表");
                }
                //否决18
                reMap.put("reCode","0");
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子已否决，请登录运维管理平台查看。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getChangeApplicant());// 获取短信接收人   创建人   changeApplicant
            }

            try{
                cmBizSingleSJService.updateSjbg(cmBizSingleSJ);
                activitiCommService.complete(reMap);
            }catch (Exception e){
                e.printStackTrace();
                log.error("处理数据变更单操作失败: "+e.getMessage());
                throw new BusinessException("处理数据变更单操作失败,请刷新页面重试");
            }
        }else {
            return AjaxResult.error("该数据变更单状态已更新，请刷新列表或页面");
        }
        try{
            sendSms(smsText, person);
        }catch (Exception e){
            log.error("处理数据变更单短信发送失败: "+e.getMessage());
        }

        return AjaxResult.success("操作成功");
    }

    /**
     * 我的数据变更单处理退回修改页面
     * @param modelMap
     * @return
     */
    @GetMapping("/returnedEdit/{id}")
    public String returnedEdit(@PathVariable("id") String id, ModelMap modelMap) {

        //获取我的数据变更单当前的数据单信息
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);
        //计划变更开始时间与结束时间转换
        String expectStartTime = cmBizSingleSJVo.getExpectStartTime();
        if(StringUtils.isNotEmpty(expectStartTime)){
            cmBizSingleSJVo.setExpectStartTime(DateUtils.formatDateStr(expectStartTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            modelMap.addAttribute("expectStartTime",cmBizSingleSJVo.getExpectStartTime());
        }
        String expectEndTime = cmBizSingleSJVo.getExpectEndTime();
        if(StringUtils.isNotEmpty(expectEndTime)){
            cmBizSingleSJVo.setExpectEndTime(DateUtils.formatDateStr(expectEndTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            modelMap.addAttribute("expectEndTime",cmBizSingleSJVo.getExpectEndTime());
        }


        //当前用户的机构信息
        String pid = ShiroUtils.getOgUser().getpId();

        OgPerson ogPerson = ogPersonService.selectOgPersonById(pid);

        modelMap.put("changeApplicant", ogPerson.getpName());
        OgOrg ogOrg = sysDeptService.selectDeptById(ogPerson.getOrgId());
        String levelCode = ogOrg.getLevelCode();
        //审核人
        List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, "0101");
        modelMap.put("checkList", checkList);
        modelMap.put("cmBizSingleSJVo", cmBizSingleSJVo);

        //获取当前所属系统的所有工作组
        List<OgGroup> groupList = sysWorkService.selectGroupBySysId(cmBizSingleSJVo.getSysId());
        modelMap.put("groupList", groupList);
        //根据对应的数据变更单创建类型加载不同的详情信息
        String flagShow = cmBizSingleSJVo.getChangeSingleType();
        if("1".equals(flagShow)){
            //当前为业务单转问题单
            modelMap.addAttribute("flagShow", flagShow);
            FmBiz fmBiz = fmBizService.selectFmBizByFaultNo(cmBizSingleSJVo.getFmNo());
            modelMap.addAttribute("fmBiz", fmBiz);
            modelMap.addAttribute("currentstate", fmBiz.getCurrentState());
        }else{
            //当前为手工创建
            modelMap.addAttribute("flagShow", flagShow);
        }
        return prefix + "/returned_edit";
    }

    /**
     * 退回待修改的数据变更单 修改提交/暂存数据变更单
     */
    @Log(title = "数据变更单", businessType = BusinessType.UPDATE)
    @PostMapping("/returnedEdit")
    @ResponseBody
    public AjaxResult returnedEditSave(CmBizSingleSJ cmBizSingleSJ) {
        //根据数据变更单查询对应的变更单信息
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(cmBizSingleSJ.getChangeSingleId());
        String smsText = "";
        OgPerson person;
        Map<String,Object> repMap = new HashMap<>();
        repMap.put("userId", ShiroUtils.getUserId());

        String businessKey=cmBizSingleSJ.getChangeSingleId();
        String processDefinitionKey="FmSJBG";
        repMap.put("businessKey",businessKey);
        repMap.put("processDefinitionKey",processDefinitionKey);
        if(StringUtils.isNotEmpty(cmBizSingleSJ.getExpectStartTime())){
            cmBizSingleSJ.setExpectStartTime(handleTimeYYYYMMDDHHMMSS(cmBizSingleSJ.getExpectStartTime()));
        }
        if(StringUtils.isNotEmpty(cmBizSingleSJ.getExpectEndTime())){
            cmBizSingleSJ.setExpectEndTime(handleTimeYYYYMMDDHHMMSS(cmBizSingleSJ.getExpectEndTime()));
        }
        if(!"06".equals(cmBizSingleSJVo.getChangeSingleStatus())){
            return AjaxResult.error("该数据变更单状态已更新，请刷新列表或页面");
        }
        //14 待处理     省中心退回后进行重新提交操作
        if("14".equals(cmBizSingleSJ.getChangeSingleStatus())){

            repMap.put("reCode","1");
            repMap.put("dealId",cmBizSingleSJ.getProcheckManager());

            smsText = "数据变更单号：" + cmBizSingleSJ.getChangeCode()
                    + ",标题:"+cmBizSingleSJ.getChangeSingleName()+"的单子已提交，请登录运维管理平台审核。";//短信内容
            person = ogPersonService.selectOgPersonById(cmBizSingleSJ.getProcheckManager());// 获取短信接收人   省处理人  procheckManager
            try{
                cmBizSingleSJService.updateEditSjbg(cmBizSingleSJ);
                activitiCommService.complete(repMap);
            }catch (Exception e){
                log.error("数据变更单退回待处理,重新提交待处理失败: "+e.getMessage());
                throw new BusinessException("数据变更单提交失败，请刷新页面进行重试 ");
            }

            try{
                sendSms(smsText, person);
            }catch (Exception e){
                log.error("数据变更单退回待处理,重新提交待处理失败: "+e.getMessage());
            }
            return AjaxResult.success("操作成功");
            //待审核      总行业务部门审核退回后  重新提交
        }else if("02".equals(cmBizSingleSJ.getChangeSingleStatus())){

            repMap.put("reCode","2");
            repMap.put("auditId",cmBizSingleSJ.getCheckManager());

            smsText = "数据变更单号：" + cmBizSingleSJ.getChangeCode()
                    + ",标题:"+cmBizSingleSJ.getChangeSingleName()+"的单子待审核，请登录运维管理平台审核。";//短信内容
            person = ogPersonService.selectOgPersonById(cmBizSingleSJ.getCheckManager());// 获取短信接收人   审核人  checkManager
            try{
                cmBizSingleSJService.updateEditSjbg(cmBizSingleSJ);
                activitiCommService.complete(repMap);
            }catch (Exception e){
                log.error("数据变更单退回待修改,重新提交待审核失败: "+e.getMessage());
                throw new BusinessException("数据变更单提交失败，请刷新页面进行重试 ");
            }

            try{
                sendSms(smsText, person);
            }catch (Exception e){
                log.error("数据变更单退回待修改重新提交待审核失败: "+e.getMessage());
            }
            return AjaxResult.success("操作成功");
            //作废处理
        }else if("20".equals(cmBizSingleSJ.getChangeSingleStatus())){

            repMap.put("reCode","0");
            try{
                cmBizSingleSJService.updateEditSjbg(cmBizSingleSJ);
                activitiCommService.complete(repMap);
            }catch (Exception e){
                log.error("数据变更单作废失败 "+e.getMessage());
                throw new BusinessException("数据变更单作废失败，请刷新页面进行重试 ");
            }
            return AjaxResult.success("操作成功");
        }
        try{
            cmBizSingleSJService.updateEditSjbg(cmBizSingleSJ);
        }catch (Exception e){
            log.error("数据变更单退回待修改暂存失败: "+e.getMessage());
            return AjaxResult.error("数据变更单退回待修改暂存失败,请刷新页面进行重试");
        }
        return AjaxResult.success("操作成功");
    }



    /**
     * 待业务审核数据变更单列表页面
     * @param modelMap
     * @return
     */

    @GetMapping("/audit")
    public String audit(ModelMap modelMap){
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        modelMap.put("pId",ogUser.getpId());
        return prefix+"/audit";
    }


    /**
     * 查询当前用户的待业务审核的任务
     * @param cmBizSingleSJVo
     * @return
     */
    @PostMapping("/auditlist")
    @ResponseBody
    public TableDataInfo auditlist(CmBizSingleSJVo cmBizSingleSJVo) {
        List<Map<String,Object>> reList =activitiCommService.userTask("FmSJBG","audit");
        List<OgGroup> userGroupList= sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String,Object>> regList=new ArrayList<>();
        if(userGroupList.size()>0){
            regList=activitiCommService.groupTasks("FmSJBG","audit");
            reList.addAll(regList);
        }

        List<String> changeSingleIds = new ArrayList<>();
        for(Map<String,Object> map:reList){
            if("audit".equals(map.get("description"))){
                String business_key=map.get("businesskey").toString();
                if(business_key!=null){
                    if(business_key!=null){
                        changeSingleIds.add(business_key);
                    }
                }
            }

        }
        List<CmBizSingleSJVo> resultlist = null;
        if(changeSingleIds!=null && changeSingleIds.size()>0){
            cmBizSingleSJVo.setIds(changeSingleIds);
            startPage();
            resultlist =  cmBizSingleSJService.selectListByTask(cmBizSingleSJVo);
            return getDataTable(resultlist);
        }
        return getDataTable(new ArrayList<>());
    }


    /**
     * 当前用户的待业务审核的任务处理页面
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/auditEdit/{id}")
    public String auditEdit(@PathVariable("id") String id, ModelMap mmap){
        //获取省份信息
        List<OgOrg> ogOrgs = sysDeptService.selectDeptList(null);
        List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());
        mmap.addAttribute("list", list);
        //根据数据变更单查询对应的变更单信息
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);
        String[] orgs = cmBizSingleSJVo.getChangeOrgtype().split(",");
        List<OgOrg> arr1 = new ArrayList<>();
        for (String orgId : orgs) {
            OgOrg ogOrg = sysDeptService.selectDeptById(orgId);
            arr1.add(ogOrg);
        }
        //计划变更开始时间与结束时间转换
        String expectStartTime = cmBizSingleSJVo.getExpectStartTime();
        if(StringUtils.isNotEmpty(expectStartTime)){
            cmBizSingleSJVo.setExpectStartTime(DateUtils.formatDateStr(expectStartTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectStartTime",cmBizSingleSJVo.getExpectStartTime());
        }
        String expectEndTime = cmBizSingleSJVo.getExpectEndTime();
        if(StringUtils.isNotEmpty(expectEndTime)){
            cmBizSingleSJVo.setExpectEndTime(DateUtils.formatDateStr(expectEndTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectEndTime",cmBizSingleSJVo.getExpectEndTime());
        }

        mmap.addAttribute("sjbg", cmBizSingleSJVo);
        String s = JSON.toJSONString(arr1);
        mmap.addAttribute("arr1", s);
        mmap.addAttribute("flagShow", cmBizSingleSJVo.getChangeSingleType());
        if("1".equals(cmBizSingleSJVo.getChangeSingleType())){
            FmBiz fmBiz = fmBizService.selectFmBizByFaultNo(cmBizSingleSJVo.getFmNo());
            mmap.addAttribute("fmBiz", fmBiz);
        }
        mmap.addAttribute("isChangeOPSFlag", cmBizSingleSJVo.getIsChangeOPS());
        mmap.addAttribute("pId",ShiroUtils.getOgUser().getpId());
        return prefix + "/audit_option";
    }

    /**
     * 详情页面
     * @return
     */
    @GetMapping("/selectForWardPerson")
    public String detail(String changeSingleId, String groupId, ModelMap map)
    {
        map.put("changeSingleId",changeSingleId);
        map.put("currentGroupId",groupId);
        return prefix + "/selectForWardPerson";
    }

    /**
     * 业务审核数据变更单 处理通过或否决或退回
     * @param cmBizSingleSJ
     * @return
     */
    @PostMapping("/auditPassOrReturnOrVeto")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult auditPassOrReturnOrVeto(CmBizSingleSJ cmBizSingleSJ){
        OgPerson person;
        String smsText ="";
        String changeSingleStatus = cmBizSingleSJ.getChangeSingleStatus();
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(cmBizSingleSJ.getChangeSingleId());
        Map<String,Object> repMap = new HashMap<>();
        repMap.put("userId", ShiroUtils.getUserId());

        String businessKey=cmBizSingleSJ.getChangeSingleId();
        String processDefinitionKey="FmSJBG";
        repMap.put("businessKey",businessKey);
        repMap.put("processDefinitionKey",processDefinitionKey);
        //状态判断
        if("02".equals(cmBizSingleSJVo.getChangeSingleStatus()) || "17".equals(cmBizSingleSJVo.getChangeSingleStatus())){

            if("12".equals(changeSingleStatus)){
                //通过

                repMap.put("reCode","1");
                repMap.put("czauditId",cmBizSingleSJVo.getChiefyManager());
                String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
                repMap.put("comment",logPerformDesc);
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子已审批，请登录运维管理平台审核。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getChiefyManager());// 获取短信接收人   处长  chiefyManager

            }else if("16".equals(changeSingleStatus)){
                //退回待处理
                repMap.put("reCode","2");
                repMap.put("dealId",cmBizSingleSJVo.getProcheckManager());
                String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
                repMap.put("comment",logPerformDesc);
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子退回待处理，请登录运维管理平台处理。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getProcheckManager());// 获取短信接收人   省处理人  procheckManager
            }else if("02".equals(changeSingleStatus)){
                //业务转发
                repMap.put("reCode","3");
                repMap.put("auditId",cmBizSingleSJ.getCheckManager());
                String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
                repMap.put("comment",logPerformDesc);
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子待审核,请登录运维管理平台审核。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJ.getCheckManager());// 获取短信接收人
            }else if("06".equals(changeSingleStatus)){
                //退回待修改
                repMap.put("reCode","5");
                repMap.put("createId",cmBizSingleSJVo.getChangeApplicant());
                String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
                repMap.put("comment",logPerformDesc);
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子已退回,请登录运维管理平台查看。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getChangeApplicant());// 获取短信接收人  创建人  changeApplicant
            }else{
                //否决18
                repMap.put("reCode","0");
                String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
                repMap.put("comment",logPerformDesc);
                smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                        + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子已被否决,请登录运维管理平台查看。";//短信内容
                person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getChangeApplicant());// 获取短信接收人  创建人 changeApplicant
            }
        }else {
            return AjaxResult.error("该数据变更单状态已更新，请刷新列表或页面");

        }

        try{
            cmBizSingleSJService.updateSjbg(cmBizSingleSJ);
            activitiCommService.complete(repMap);
        }catch (Exception e){
            log.error("数据变更单审核操作失败: "+e.getMessage());
            throw  new BusinessException("数据变更单审核操作失败,请刷新页面进行重试 ");
        }
        try{
            sendSms(smsText, person);
        }catch (Exception e){
            log.error("业务审核短信发送失败: "+e.getMessage());
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 处长审核数据变更单列表页面
     * @param modelMap
     * @return
     */

    @GetMapping("/czaudit")
    public String czaudit(ModelMap modelMap){
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        modelMap.put("pId",ogUser.getpId());
        return prefix+"/czaudit";
    }

    /**
     * 查询当前用户的待处长审核的任务
     * @param cmBizSingleSJVo
     * @return
     */
    @PostMapping("/czauditlist")
    @ResponseBody
    public TableDataInfo czauditlist(CmBizSingleSJVo cmBizSingleSJVo) {
        List<Map<String,Object>> reList =activitiCommService.userTask("FmSJBG",null);
        List<OgGroup> userGroupList= sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String,Object>> regList=new ArrayList<>();
        if(userGroupList.size()>0){
            regList=activitiCommService.groupTasks("FmSJBG","czaudit");
            reList.addAll(regList);
        }
        List<String> changeSingleIds = new ArrayList<>();
        for(Map<String,Object> map:reList){
            if("czaudit".equals(map.get("description"))){
                String business_key=map.get("businesskey").toString();
                if(business_key!=null){
                    if(business_key!=null){
                        changeSingleIds.add(business_key);
                    }
                }
            }

        }
        List<CmBizSingleSJVo> resultlist = null;
        if(changeSingleIds!=null && changeSingleIds.size()>0){
            cmBizSingleSJVo.setIds(changeSingleIds);
            startPage();
            resultlist =  cmBizSingleSJService.selectListByTask(cmBizSingleSJVo);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());
    }


    /**
     * 当前用户的待处长审核的任务处理页面
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/czauditEdit/{id}")
    public String czauditEdit(@PathVariable("id") String id, ModelMap mmap){
        //获取省份信息
        List<OgOrg> ogOrgs = sysDeptService.selectDeptList(null);
        List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());
        mmap.addAttribute("list", list);
        //根据数据变更单查询对应的变更单信息
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);
        String[] orgs = cmBizSingleSJVo.getChangeOrgtype().split(",");
        List<OgOrg> arr1 = new ArrayList<>();
        for (String orgId : orgs) {
            OgOrg ogOrg = sysDeptService.selectDeptById(orgId);
            arr1.add(ogOrg);
        }
        //计划变更开始时间与结束时间转换
        String expectStartTime = cmBizSingleSJVo.getExpectStartTime();
        if(StringUtils.isNotEmpty(expectStartTime)){
            cmBizSingleSJVo.setExpectStartTime(DateUtils.formatDateStr(expectStartTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectStartTime",cmBizSingleSJVo.getExpectStartTime());
        }
        String expectEndTime = cmBizSingleSJVo.getExpectEndTime();
        if(StringUtils.isNotEmpty(expectEndTime)){
            cmBizSingleSJVo.setExpectEndTime(DateUtils.formatDateStr(expectEndTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectEndTime",cmBizSingleSJVo.getExpectEndTime());
        }

        mmap.addAttribute("sjbg", cmBizSingleSJVo);
        String s = JSON.toJSONString(arr1);
        mmap.addAttribute("arr1", s);
        mmap.addAttribute("flagShow", cmBizSingleSJVo.getChangeSingleType());
        mmap.addAttribute("isChangeOPSFlag", cmBizSingleSJVo.getIsChangeOPS());
        return prefix + "/czaudit_option";
    }

    /**
     * 处长审核数据变更单 处理通过或否决或退回
     * @param cmBizSingleSJ
     * @return
     */
    @PostMapping("/czauditPassOrReturnOrVeto")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult czauditPassOrReturnOrVeto(CmBizSingleSJ cmBizSingleSJ){
        OgPerson person;
        String smsText = "";
        String changeSingleStatus = cmBizSingleSJ.getChangeSingleStatus();
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(cmBizSingleSJ.getChangeSingleId());
        Map<String,Object> repMap = new HashMap<>();
        repMap.put("userId", ShiroUtils.getUserId());

        String businessKey=cmBizSingleSJ.getChangeSingleId();
        String processDefinitionKey="FmSJBG";
        repMap.put("businessKey",businessKey);
        repMap.put("processDefinitionKey",processDefinitionKey);
        //状态判断
        if(!"12".equals(cmBizSingleSJVo.getChangeSingleStatus())){
            return AjaxResult.error("该数据变更单状态已变更，请刷新列表或页面");
        }
        if("18".equals(changeSingleStatus)){
            //通过
            repMap.put("reCode","1");
            repMap.put("jlauditId",cmBizSingleSJVo.getTechnologyAuditId());
            String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
            repMap.put("comment",logPerformDesc);
            smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                    + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子审核通过,请登录运维管理平台查看处理。";//短信内容
            person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getTechnologyAuditId());// 获取短信接收人 方案提交人 technologyAuditId

        }else if("17".equals(changeSingleStatus)){
            //退回待审核
            repMap.put("reCode","2");
            repMap.put("auditId",cmBizSingleSJVo.getCheckManager());
            String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
            repMap.put("comment",logPerformDesc);
            smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                    + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子审核退回,请登录运维管理平台查看处理。";//短信内容
            person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getCheckManager());// 获取短信接收人    审核人  checkManager

        }else{
            //否决
            repMap.put("reCode","0");
            String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
            repMap.put("comment",logPerformDesc);
            smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                    + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子已被否决,请登录运维管理平台查看。";//短信内容
            person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getChangeApplicant());// 获取短信接收人    创建人   changeApplicant
        }
        try{
            cmBizSingleSJService.updateSjbg(cmBizSingleSJ);
            activitiCommService.complete(repMap);
        }catch (Exception e){
            log.error("处长审核操作失败: "+e.getMessage());
            throw  new BusinessException("处长审核操作失败,请刷新页面进行重试");
        }

        try{
            sendSms(smsText, person);
        }catch (Exception e){
            log.error("处长审核操作短信发送失败: "+e.getMessage());
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 项目经理审核数据变更单列表页面
     * @param modelMap
     * @return
     */

    @GetMapping("/jlaudit")
    public String jlaudit(ModelMap modelMap){
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        modelMap.put("pId",ogUser.getpId());
        return prefix+"/jlaudit";
    }

    /**
     * 查询当前用户的待经理审核的任务
     * @param cmBizSingleSJVo
     * @return
     */
    @PostMapping("/jlauditlist")
    @ResponseBody
    public TableDataInfo jlauditlist(CmBizSingleSJVo cmBizSingleSJVo) {


        List<Map<String,Object>> reList =activitiCommService.userTask("FmSJBG",null);
        List<OgGroup> userGroupList= sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String,Object>> regList=new ArrayList<>();
        if(userGroupList.size()>0){
            regList=activitiCommService.groupTasks("FmSJBG","jlaudit");
            reList.addAll(regList);
        }
        List<String> changeSingleIds = new ArrayList<>();
        for(Map<String,Object> map:reList){
            if("jlaudit".equals(map.get("description"))){
                String business_key=map.get("businesskey").toString();
                if(business_key!=null){
                    if(business_key!=null){
                        changeSingleIds.add(business_key);
                    }
                }
            }
        }
        List<CmBizSingleSJVo> resultlist = null;
        if(changeSingleIds!=null && changeSingleIds.size()>0){
            cmBizSingleSJVo.setIds(changeSingleIds);
            startPage();
            resultlist =  cmBizSingleSJService.selectListByTask(cmBizSingleSJVo);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());
    }


    /**
     * 当前用户的待处长审核的任务处理页面
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/jlauditEdit/{id}")
    public String jlauditEdit(@PathVariable("id") String id, ModelMap mmap){
        //获取省份信息
        List<OgOrg> ogOrgs = sysDeptService.selectDeptList(null);
        List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());
        mmap.addAttribute("list", list);
        //根据数据变更单查询对应的变更单信息
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);
        String[] orgs = cmBizSingleSJVo.getChangeOrgtype().split(",");
        List<OgOrg> arr1 = new ArrayList<>();
        for (String orgId : orgs) {
            OgOrg ogOrg = sysDeptService.selectDeptById(orgId);
            arr1.add(ogOrg);
        }
        //计划变更开始时间与结束时间转换
        String expectStartTime = cmBizSingleSJVo.getExpectStartTime();
        if(StringUtils.isNotEmpty(expectStartTime)){
            cmBizSingleSJVo.setExpectStartTime(DateUtils.formatDateStr(expectStartTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectStartTime",cmBizSingleSJVo.getExpectStartTime());
        }
        String expectEndTime = cmBizSingleSJVo.getExpectEndTime();
        if(StringUtils.isNotEmpty(expectEndTime)){
            cmBizSingleSJVo.setExpectEndTime(DateUtils.formatDateStr(expectEndTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectEndTime",cmBizSingleSJVo.getExpectEndTime());
        }


        mmap.addAttribute("sjbg", cmBizSingleSJVo);
        String s = JSON.toJSONString(arr1);
        mmap.addAttribute("arr1", s);
        mmap.addAttribute("flagShow", cmBizSingleSJVo.getChangeSingleType());
        mmap.addAttribute("isChangeOPSFlag", cmBizSingleSJVo.getIsChangeOPS());
        return prefix + "/jlaudit_option";
    }

    /**
     * 项目经理提供方案 通过或否决
     *
     * @param cmBizSingleSJ
     * @return
     */
    @PostMapping("/jlauditPassOrVeto")
    @ResponseBody
    public AjaxResult jlauditPassOrVeto(CmBizSingleSJ cmBizSingleSJ) {
        OgPerson person;
        String smsText = "";
        String changeSingleStatus = cmBizSingleSJ.getChangeSingleStatus();
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(cmBizSingleSJ.getChangeSingleId());
        Map<String, Object> repMap = new HashMap<>();
        repMap.put("userId", ShiroUtils.getUserId());

        String businessKey = cmBizSingleSJ.getChangeSingleId();
        String processDefinitionKey = "FmSJBG";
        repMap.put("businessKey", businessKey);
        repMap.put("processDefinitionKey", processDefinitionKey);
        String approvalId;
        //状态判断
        if (!"18".equals(cmBizSingleSJVo.getChangeSingleStatus())) {
            return AjaxResult.error("该数据变更单状态已变更，请刷新列表或页面");
        }
        if ("03".equals(changeSingleStatus)) {
            //判断当前登录人是否上传附件
            String userId = ShiroUtils.getUserId();
            //获取数据变更单ID
            String singleId = cmBizSingleSJ.getChangeSingleId();
            Attachment attachment = new Attachment();
            attachment.setOwnerId(singleId);
            attachment.setCreateId(userId);
            List<Attachment> list = iPubAttachmentService.selectAttachmentList(attachment);
            if(StringUtils.isEmpty(list)){
                return AjaxResult.error("请提交测试报告");
            }

            repMap.put("reCode", "1");
            //通过
            repMap.put("approvalId", cmBizSingleSJVo.getImplementor());
            smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                    + ",标题:" + cmBizSingleSJVo.getChangeSingleName() + "的单子待实施，请登录运维管理平台实施。";//短信内容
            person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getImplementor());// 获取短信接收人   实施人 implementor

        } else {
            //否决18
            repMap.put("reCode", "0");
            smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                    + ",标题:" + cmBizSingleSJVo.getChangeSingleName() + "的单子已否决，请登录运维管理平台查看。";//短信内容
            person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getChangeApplicant());// 获取短信接收人   创建人   changeApplicant

        }
        try {
            cmBizSingleSJService.updateSjbg(cmBizSingleSJ);
            activitiCommService.complete(repMap);
        } catch (Exception e) {
            log.error("技术提供实施方案操作失败: " + e.getMessage());
            throw new BusinessException("技术提供实施方案操作失败,请刷新页面重试");
        }

        try {
            sendSms(smsText, person);
        } catch (Exception e) {
            log.error("技术提供实施方案短信发送失败: " + e.getMessage());
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 实施审核数据变更单列表页面
     * @param modelMap
     * @return
     */

    @GetMapping("/approval")
    public String approval(ModelMap modelMap){
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        modelMap.put("pId",ogUser.getpId());
        return  prefix+"/approvals";
    }

    /**
     * 实施审核数据变更单列表页面
     * @param cmBizSingleSJVo
     * @return
     */
    @PostMapping("/approvallist")
    @ResponseBody
    public TableDataInfo approvallist(CmBizSingleSJVo cmBizSingleSJVo) {
        List<Map<String,Object>> reList =activitiCommService.userTask("FmSJBG",null);
        List<OgGroup> userGroupList= sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String,Object>> regList=new ArrayList<>();
        if(userGroupList.size()>0){
            regList=activitiCommService.groupTasks("FmSJBG","approval");
            reList.addAll(regList);
        }
        List<String> changeSingleIds = new ArrayList<>();
        for(Map<String,Object> map:reList){
            if("approval".equals(map.get("description"))){
                String business_key=map.get("businesskey").toString();
                if(business_key!=null){
                    if(business_key!=null){
                        changeSingleIds.add(business_key);
                    }
                }
            }

        }
        List<CmBizSingleSJVo> resultlist = null;
        if(changeSingleIds!=null && changeSingleIds.size()>0){
            cmBizSingleSJVo.setIds(changeSingleIds);
            startPage();
            resultlist =  cmBizSingleSJService.selectListByTask(cmBizSingleSJVo);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());
    }

    /**
     * 实施审核数据变更单列表页面的任务处理页面
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/approvalEdit/{id}")
    public String approvalEdit(@PathVariable("id") String id, ModelMap mmap){
        //获取省份信息
        List<OgOrg> ogOrgs = sysDeptService.selectDeptList(null);
        List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());
        mmap.addAttribute("list", list);
        //根据数据变更单查询对应的变更单信息
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);
        String[] orgs = cmBizSingleSJVo.getChangeOrgtype().split(",");
        List<OgOrg> arr1 = new ArrayList<>();
        for (String orgId : orgs) {
            OgOrg ogOrg = sysDeptService.selectDeptById(orgId);
            arr1.add(ogOrg);
        }
        if(StringUtils.isNotNull(cmBizSingleSJVo)){
            //获取当前时间


            cmBizSingleSJVo.setExpectStartTime(cmBizSingleSJVo.getExpectStartTime());
            cmBizSingleSJVo.setExpectEndTime(cmBizSingleSJVo.getExpectEndTime());
        }

        mmap.addAttribute("sjbg", cmBizSingleSJVo);
        String s = JSON.toJSONString(arr1);

        //查询复合人
        OgPerson ogPerson = new OgPerson();
        ogPerson.setpId(ShiroUtils.getOgUser().getpId());
        ogPerson.setrId("2204");
        ogPerson.setGroupId(cmBizSingleSJVo.getGroupId());
        List<OgPerson> ogPeoples = ogPersonService.selectPersonByGroupIdAndRoleId(ogPerson);
        mmap.addAttribute("arr1", s);
        mmap.addAttribute("ogPeoples", ogPeoples);
        mmap.addAttribute("flagShow", cmBizSingleSJVo.getChangeSingleType());
        mmap.addAttribute("isChangeOPSFlag", cmBizSingleSJVo.getIsChangeOPS());
        //计划变更开始时间与结束时间转换
        String expectStartTime =DateUtils.getTime();
        mmap.addAttribute("expectStartTime", expectStartTime);
        String expectEndTime = DateUtils.getTime();
        mmap.addAttribute("expectEndTime", expectEndTime);

        return prefix + "/approval_option";
    }


    /**
     *实施人员通过或否决
     * @param cmBizSingleSJ
     * @return
     */
    @PostMapping("/approvalPassOrReturn")
    @ResponseBody
    public AjaxResult approvalPassOrReturn(CmBizSingleSJ cmBizSingleSJ){
        OgPerson person;
        String smsText = "";
        String changeSingleStatus = cmBizSingleSJ.getChangeSingleStatus();
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(cmBizSingleSJ.getChangeSingleId());
        Map<String,Object> repMap = new HashMap<>();
        repMap.put("userId", ShiroUtils.getUserId());

        String businessKey=cmBizSingleSJ.getChangeSingleId();
        String processDefinitionKey="FmSJBG";
        repMap.put("businessKey",businessKey);
        repMap.put("processDefinitionKey",processDefinitionKey);
        //实施开始时间与结束时间日期转换
        if(StringUtils.isNotEmpty(cmBizSingleSJ.getPracticalStart())){
            cmBizSingleSJ.setPracticalStart(handleTimeYYYYMMDDHHMMSS(cmBizSingleSJ.getPracticalStart()));
        }
        if(StringUtils.isNotEmpty(cmBizSingleSJ.getPracticalEnd())){
            cmBizSingleSJ.setPracticalEnd(handleTimeYYYYMMDDHHMMSS(cmBizSingleSJ.getPracticalEnd()));
        }
        //状态判断
        if(!"03".equals(cmBizSingleSJVo.getChangeSingleStatus())){
            return AjaxResult.error("该数据变更单状态已变更，请刷新列表或页面");
        }
        if("05".equals(changeSingleStatus)){ //待关闭
            //通过
            if("05".equals(cmBizSingleSJVo.getChangeSingleStatus())){
                return AjaxResult.error("该数据变更单已关闭，请刷新列表");
            }
            repMap.put("reCode","1");
            repMap.put("closedId",cmBizSingleSJVo.getChangeApplicant());
            smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                    + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子待关闭,请登录运维管理平台查看并关闭。";//短信内容
            person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getChangeApplicant());// 获取短信接收人    创建人  changeApplicant

        }else{
            //退回
            if("18".equals(cmBizSingleSJVo.getChangeSingleStatus())){
                return AjaxResult.error("该数据变更单已退回，请刷新列表");
            }
            repMap.put("reCode","2");
            repMap.put("jlauditId",cmBizSingleSJVo.getTechnologyAuditId());
            String logPerformDesc = cmBizSingleSJ.getLOG_performDesc();
            repMap.put("comment",logPerformDesc);
            smsText = "数据变更单号：" + cmBizSingleSJVo.getChangeCode()
                    + ",标题:"+cmBizSingleSJVo.getChangeSingleName()+"的单子实施退回,请登录运维管理平台处理。";//短信内容
            person = ogPersonService.selectOgPersonById(cmBizSingleSJVo.getTechnologyAuditId());// 获取短信接收人   方案提交人   technologyAuditId
        }

        try{
            cmBizSingleSJService.updateSjbg(cmBizSingleSJ);
            activitiCommService.complete(repMap);
        }catch (Exception e){
            log.error("数据变更单实施操作失败: "+e.getMessage());
            throw new BusinessException("数据变更单实施操作失败,请刷新页面进行重试");
        }

        try{
            sendSms(smsText, person);
        }catch (Exception e){
            log.error("数据变更单实施操作短信发送失败: "+e.getMessage());
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 关闭数据变更单列表页面
     * @param modelMap
     * @return
     */

    @GetMapping("/sjbggb")
    public String sjbggb(ModelMap modelMap){
        //获取当前用户的人员信息
        OgUser ogUser = ShiroUtils.getOgUser();
        modelMap.put("pId",ogUser.getpId());
        return  prefix+"/sjbggb";
    }

    /**d
     * 关闭数据变更单列表页面
     * @param cmBizSingleSJVo
     * @return
     */
    @PostMapping("/sjbggblist")
    @ResponseBody
    public TableDataInfo sjbggblist(CmBizSingleSJVo cmBizSingleSJVo) {
        List<Map<String,Object>> reList =activitiCommService.userTask("FmSJBG",null);
        List<OgGroup> userGroupList= sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String,Object>> regList=new ArrayList<>();
        if(userGroupList.size()>0){
            regList=activitiCommService.groupTasks("FmSJBG","closed");
            reList.addAll(regList);
        }
        List<String> changeSingleIds = new ArrayList<>();
        for(Map<String,Object> map:reList){
            if("closed".equals(map.get("description"))){
                String business_key=map.get("businesskey").toString();
                if(business_key!=null){
                    if(business_key!=null){
                        changeSingleIds.add(business_key);
                    }
                }
            }

        }
        List<CmBizSingleSJVo> resultlist = null;
        if(changeSingleIds!=null && changeSingleIds.size()>0){
            cmBizSingleSJVo.setIds(changeSingleIds);
            startPage();
            resultlist =  cmBizSingleSJService.selectListByTask(cmBizSingleSJVo);
            return getDataTable(resultlist);
        }

        return  getDataTable(new ArrayList<>());


    }


    /**
     * 关闭数据变更单列表页面的任务处理页面
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/sjbggbEdit/{id}/{flag}")
    public String sjbggbEdit(@PathVariable("id") String id,@PathVariable("flag") String flag, ModelMap mmap){
        //获取省份信息
        List<OgOrg> ogOrgs = sysDeptService.selectDeptList(null);
        List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());
        mmap.addAttribute("list", list);
        //根据数据变更单查询对应的变更单信息
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);
        String[] orgs = cmBizSingleSJVo.getChangeOrgtype().split(",");
        List<OgOrg> arr1 = new ArrayList<>();
        for (String orgId : orgs) {
            OgOrg ogOrg = sysDeptService.selectDeptById(orgId);
            arr1.add(ogOrg);
        }

        mmap.addAttribute("sjbg", cmBizSingleSJVo);
        String s = JSON.toJSONString(arr1);
        mmap.addAttribute("arr1", s);
        mmap.addAttribute("flagShow", cmBizSingleSJVo.getChangeSingleType());
        mmap.addAttribute("isChangeOPSFlag", cmBizSingleSJVo.getIsChangeOPS());
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getFmNo())){
            FmBiz fmBiz = fmBizService.selectFmBizByFaultNo(cmBizSingleSJVo.getFmNo());
            mmap.addAttribute("fmBiz",fmBiz);
        }
        //计划变更开始时间与结束时间转换
        String expectStartTime = cmBizSingleSJVo.getExpectStartTime();
        if(StringUtils.isNotEmpty(expectStartTime)){
            cmBizSingleSJVo.setExpectStartTime(DateUtils.formatDateStr(expectStartTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectStartTime",cmBizSingleSJVo.getExpectStartTime());
        }
        String expectEndTime = cmBizSingleSJVo.getExpectEndTime();
        if(StringUtils.isNotEmpty(expectEndTime)){
            cmBizSingleSJVo.setExpectEndTime(DateUtils.formatDateStr(expectEndTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            mmap.addAttribute("expectEndTime",cmBizSingleSJVo.getExpectEndTime());
        }
        mmap.put("flag", flag);

        return prefix + "/sjbggb_option";
    }


    /**
     *数据变更单确认关闭
     * @param cmBizSingleSJ
     * @return
     */
    @PostMapping("/confirmationClose")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult confirmationClose(CmBizSingleSJ cmBizSingleSJ){
        CmBizSingleSJVo singleSJVo = cmBizSingleSJService.selectSjbgById(cmBizSingleSJ.getChangeSingleId());
        if(singleSJVo != null){
            if(!"05".equals(singleSJVo.getChangeSingleStatus())){
                throw new BusinessException("该数据变更单已关闭，请刷新列表检查后继续操作。");
            }
        }
        Map<String,Object> repMap = new HashMap<>();
        repMap.put("userId", ShiroUtils.getUserId());
        String businessKey=cmBizSingleSJ.getChangeSingleId();
        String processDefinitionKey="FmSJBG";
        repMap.put("businessKey",businessKey);
        repMap.put("processDefinitionKey",processDefinitionKey);
        String logPerformDesc = cmBizSingleSJ.getLogPerformDesc();
        repMap.put("comment",logPerformDesc);
        //关闭时间
        String date = DateUtils.dateTimeNow();
        cmBizSingleSJ.setCloseTime(date);
        try{
            cmBizSingleSJService.updateSjbg(cmBizSingleSJ);
            activitiCommService.complete(repMap);
        }catch (Exception e){
            log.error("数据变更单关闭失败: "+e.getMessage());
            throw  new BusinessException("数据变更单关闭失败,请刷新页面进行重试");
        }
        return AjaxResult.success("操作成功");
    }


    /**
     * 我的数据变更单页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/sjbg_query")
    public String sjbgQuery(ModelMap modelMap) {
        //获取当前登陆账号的关联的人员Id
        String personId = ShiroUtils.getOgUser().getpId();
        modelMap.addAttribute("personId", personId);
        Map<String, Object> map = sjbgAddPage();
        modelMap.put("list", map.get("list"));
        return prefix + "/sjbg_query";
    }

    @GetMapping("/selectPersonByGroupId")
    @ResponseBody()
    public AjaxResult selectPersonByGroupId(String groupId){
        AjaxResult ajaxResult = new AjaxResult();
        //当前用户的机构信息
        String pid = ShiroUtils.getOgUser().getpId();

        OgPerson ogPerson = ogPersonService.selectOgPersonById(pid);
        OgOrg ogOrg = sysDeptService.selectDeptById(ogPerson.getOrgId());
        String levelCode = ogOrg.getLevelCode();
        //审核人
        List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, "0101");
        ajaxResult.put("data",checkList);
        if(Integer.parseInt("1")>=Integer.parseInt("2")){

        }
        return  ajaxResult;

    }

    /**
     * 选择用户 flag 0代表数据变更单添加选择  1，代表当前为业务转发选择
     * @param flag
     * @param modelMap
     * @return
     */
    @GetMapping("/selectUser/{flag}")
    public String selectUser(@PathVariable("flag") String flag,ModelMap modelMap)
    {
        modelMap.put("flag",flag);
        return prefix + "/selectUser";
    }


    @ResponseBody
    @PostMapping("/getParaType")
    public AjaxResult getParaType(String type)
    {

        AjaxResult ajaxResult = new AjaxResult();
        List<PubParaValue> paraType = dictService.getParaType(type);
        ajaxResult.put("data",paraType);
        return ajaxResult;
    }



    @GetMapping("/selectOgUserByUserId")
    @ResponseBody
    public AjaxResult selectOgUserByUserId(){
        AjaxResult ajaxResult = new AjaxResult();
        String userId = ShiroUtils.getOgUser().getUserId();
        ajaxResult.put("data", ogUserService.selectOgUserByUserId(userId));
        return ajaxResult;
    }


    @PostMapping("/fmbiz/list")
    @ResponseBody
    public TableDataInfo list(FmBiz fmBiz){
        startPage();
        List<FmBiz> list = fmBizService.selectMylist(fmBiz);
        return getDataTable(list);
    }

    @PostMapping("/listByOrg")
    @ResponseBody
    public TableDataInfo listByOrg(CmBizSingleSJVo cmBizSingleSJVo) {
        startPage();
        List<CmBizSingleSJVo> list = cmBizSingleSJService.listByOrg(cmBizSingleSJVo);
        return getDataTable(list);
    }

    /**
     * 查询数据变更单页面列表数据
     *
     * @param cmBizSingleSJVo
     * @return
     */
    @PostMapping("/listTwo")
    @ResponseBody
    public TableDataInfo listTwo(CmBizSingleSJVo cmBizSingleSJVo) {



        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getStartCreateTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(cmBizSingleSJVo.getStartCreateTime());
            cmBizSingleSJVo.setStartCreateTime(parseStart+"000000");
        }
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getEndCreateTime())){
            String parseEnd = DateUtils.handleTimeYYYYMMDD(cmBizSingleSJVo.getEndCreateTime());
            cmBizSingleSJVo.setEndCreateTime(parseEnd+"240000");
        }

        if (!StringUtils.isEmpty(cmBizSingleSJVo.getStartcloseTime())){
            cmBizSingleSJVo.setStartcloseTime(cmBizSingleSJVo.getStartcloseTime().replaceAll("-", "")+"000000");
        }

        if (!StringUtils.isEmpty(cmBizSingleSJVo.getEndcloseTime())) {
            cmBizSingleSJVo.setEndcloseTime(cmBizSingleSJVo.getEndcloseTime().replaceAll("-", "")+"240000");
        }


        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        OgOrg ogOrg = sysDeptService.selectDeptById(ogPerson.getOrgId());
        Map map = new HashMap<>();
        map.put("userId",pId);
        List<OgGroup> groupList = workService.selectGroupByGposition(map);
        if ("/000000000/".equals(ogOrg.getLevelCode())) {// 邮政金融运维人员，查看全部

        } else if (ogOrg.getLevelCode().startsWith("/000000000/010000888/")) {// 全国中心，查看全部

            if (ogOrg.getLevelCode().startsWith("/000000000/010000888/010900888/")) {//代维公司
                cmBizSingleSJVo.setCreateOrgId(ogOrg.getOrgId());
                //判断当前登录人的工作组与添加时选择的工作组是否相同
                if(StringUtils.isNotEmpty(groupList)){
                    cmBizSingleSJVo.getParams().put("groupList",groupList);
                }
            }

        } else {
            cmBizSingleSJVo.setLevelCode(ogOrg.getLevelCode().substring(0,20));

        }
        startPage();
        if (StringUtils.isNotEmpty(cmBizSingleSJVo.getChangeSingleStatus())) {
            String[] ChangeSingleStatus = cmBizSingleSJVo.getChangeSingleStatus().split(",");
            cmBizSingleSJVo.getParams().put("state", ChangeSingleStatus);
        }
        List<CmBizSingleSJVo> list = cmBizSingleSJService.listByOrg(cmBizSingleSJVo);

        return getDataTable(list);
    }

    private CmBizSingleSJVo setPara(CmBizSingleSJVo cmBizSingleSJVo) {
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        OgOrg ogOrg = sysDeptService.selectDeptById(ogPerson.getOrgId());
        if ("/000000000/".equals(ogOrg.getLevelCode())) {
            // 邮政金融运维人员，查看全部
        } else if (ogOrg.getLevelCode().startsWith("/000000000/010000888/")) {
            // 全国中心，查看全部
            if (ogOrg.getLevelCode().startsWith("/000000000/010000888/010900888/")) {
                cmBizSingleSJVo.setCreateOrgId(ogOrg.getOrgId());
            }
        } else {
            cmBizSingleSJVo.setLevelCode(ogOrg.getLevelCode());
        }
        return cmBizSingleSJVo;
    }


    @PostMapping("/selectListByFmNo")
    @ResponseBody
    public List<CmBizSingleSJ> selectListByFmNo(String fmNo){
        return cmBizSingleSJService.selectListByFmNo(fmNo);
    }

    /**
     * 响应请求分页数据
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 空数据响应请求分页数据
     */
    private TableDataInfo getDataTable()
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(null);
        rspData.setTotal(0);
        return rspData;
    }

    /**
     * 设置请求分页数据
     */
    @Override
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    @RequestMapping("/getOneAndTwoLvOrg")
    @ResponseBody
    public AjaxResult getOneAndTwoLvOrg(){
        AjaxResult ajaxResult = new AjaxResult();

        List<OgOrg> list = sysDeptService.selectDeptListTwo(null);

        //List<OgOrg> list = ogOrgs.stream().filter(ogOrg -> "2".equals(ogOrg.getOrgLv()) || "1".equals(ogOrg.getOrgLv())).collect(Collectors.toList());

        ajaxResult.put("data",list);

        return ajaxResult;
    }


    @GetMapping("/tools")
    public String toolsPage(ModelMap modelMap) throws ParseException {
        //初始化当前时间
        Date date = new Date();//获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String endTime = DateUtils.handleTimeYYYYMMDDHHMMSS(calendar.getTime());
        calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间
        String startTime = DateUtils.handleTimeYYYYMMDDHHMMSS(calendar.getTime());
        String  toolsEndTime = parseInitDate(endTime.substring(0, 8));
        String  toolsStartTime = parseInitDate(startTime.substring(0, 8));
        modelMap.put("toolsEndTime",toolsEndTime);
        modelMap.put("toolsStartTime",toolsStartTime);
        return  prefix + "/tools";
    }

    private String parseInitDate(String str){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        formatter.setLenient(false);
        Date newDate= null;
        try {
            newDate = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(newDate);
    }
    /**
     * 工具统计
     * @param cmBizSingleSJTools
     * @return
     */
    @PostMapping("/tools/list")
    @ResponseBody
    public TableDataInfo tools(CmBizSingleSJTools cmBizSingleSJTools) {
        //参数转换
        String startCreateTime = cmBizSingleSJTools.getStartCreateTime();
        String time1 = DateUtils.formatDateStr(startCreateTime,"yyyy-MM-dd","yyyyMMdd");
        cmBizSingleSJTools.setStartCreateTime(time1+"000000");
        String endCreateTime = cmBizSingleSJTools.getEndCreateTime();
        String time2 =  DateUtils.formatDateStr(endCreateTime,"yyyy-MM-dd","yyyyMMdd");
        cmBizSingleSJTools.setEndCreateTime(time2+"240000");
        if("1".equals(cmBizSingleSJTools.getCmBizSjCountType())){
            startPage();
            List<CmBizSingleSJTools> list = cmBizSingleSJService.selectToolsCountBySys(cmBizSingleSJTools);
            return getDataTable(list);
        }
        startPage();
        List<CmBizSingleSJTools> list = cmBizSingleSJService.selectToolsCountByCaption(cmBizSingleSJTools);
        return getDataTable(list);

    }

    /**
     * 工具使用情况
     */
    @Log(title = "工具使用情况", businessType = BusinessType.EXPORT)
    @PostMapping("/tools/export")
    @ResponseBody
    public AjaxResult toolsExport(CmBizSingleSJTools cmBizSingleSJTools) {
        //参数转换
        String startCreateTime = cmBizSingleSJTools.getStartCreateTime();
        String time1 = DateUtils.formatDateStr(startCreateTime,"yyyy-MM-dd","yyyyMMdd");
        cmBizSingleSJTools.setStartCreateTime(time1+"000000");
        String endCreateTime = cmBizSingleSJTools.getEndCreateTime();
        String time2 =  DateUtils.formatDateStr(endCreateTime,"yyyy-MM-dd","yyyyMMdd");
        cmBizSingleSJTools.setEndCreateTime(time2+"240000");

        String isCurrentPage = (String) cmBizSingleSJTools.getParams().get("currentPage");
        if("1".equals(cmBizSingleSJTools.getCmBizSjCountType())){
            if ("currentPage".equals(isCurrentPage)) {
                startPage();
            }

            List<CmBizSingleSJTools> list = cmBizSingleSJService.selectToolsCountBySys(cmBizSingleSJTools);
            ExcelUtil<CmBizSingleSJTools> util = new ExcelUtil<CmBizSingleSJTools>(CmBizSingleSJTools.class);
            return util.exportExcel(list, "工具使用情况统计");
        }else{
            if ("currentPage".equals(isCurrentPage)) {
                startPage();
            }
            List<CmBizSingleSJTools> list = cmBizSingleSJService.selectToolsCountByCaption(cmBizSingleSJTools);
            ExcelUtil<CmBizSingleSJTools> util = new ExcelUtil<CmBizSingleSJTools>(CmBizSingleSJTools.class);
            return util.exportExcel(list, "工具使用情况统计");
        }
    }

    @GetMapping("/selectById/{id}")
    @ResponseBody
    public AjaxResult selectById(@PathVariable("id") String id){
        AjaxResult ajaxResult  = new AjaxResult();
        CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgById(id);
        if(StringUtils.isNotEmpty(cmBizSingleSJVo.getChangeOrgtype())){
            String[] split = cmBizSingleSJVo.getChangeOrgtype().split(",");
            ajaxResult.put("data",split);
        }else{
            ajaxResult.put("data","0");
        }
        return ajaxResult;
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
        p.setInspectObject("050100");// 检查对象
        p.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }
}

