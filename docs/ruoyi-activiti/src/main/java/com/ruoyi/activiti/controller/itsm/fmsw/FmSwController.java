package com.ruoyi.activiti.controller.itsm.fmsw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.constants.FmSwConstants;
import com.ruoyi.activiti.domain.FmSw;
import com.ruoyi.activiti.domain.FmSwMb;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.ICmBizSingleService;
import com.ruoyi.activiti.service.IFmSwMbService;
import com.ruoyi.activiti.service.IFmSwService;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.CmBizSingle;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.domain.entity.OgUser;
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
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ???????????????Controller
 *
 * @author luyankun
 * @date 2020-12-16
 */
@Controller
@RequestMapping("/trans/sw")
@Transactional(rollbackFor = Exception.class)
public class FmSwController extends BaseController {

    //????????????
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FmSwController.class);

    private String prefix = "trans/sw";

    @Autowired
    private IFmSwService fmSwService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IFmSwMbService fmSwMbService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private ISysWorkService sysWorkService;

    @Autowired
    private IPubRelationService iPubRelationService;

    @Autowired
    private ICmBizSingleService iCmBizSingleService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private IPubFlowLogService pubFlowLogService;

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * ???????????????????????????
     *
     * @param modelMap
     * @return
     */
    @GetMapping()
    public String sw(ModelMap modelMap) {
        modelMap.put("user", ShiroUtils.getOgUser());
        return prefix + "/sw";
    }

    /**
     * ?????????????????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmSw fmSw) {

        FmSw is = null;

        if (!StringUtils.isEmpty(fmSw.getStartCreateTime())) {
            fmSw.setStartCreateTime(fmSw.getStartCreateTime().replaceAll("-", "") + FmSwConstants.start_suffix);
        }

        if (!StringUtils.isEmpty(fmSw.getEndCreateTime())) {
            fmSw.setEndCreateTime(fmSw.getEndCreateTime().replaceAll("-", "") + FmSwConstants.end_suffix);
        }

        if (!StringUtils.isEmpty(fmSw.getFaultKind())) {
            FmSwMb fmswmb = fmSwMbService.selectById(fmSw.getFaultKind());
            fmSw.setFaultKind(fmswmb.getFaultKind());
        }

        OgUser u = ShiroUtils.getOgUser();
        if (!StringUtils.isEmpty(fmSw.getLabel())) {
            String myFlag = fmSw.getLabel();
            if ("1".equals(myFlag)) {//????????????
                fmSw.setCreateId(u.getpId());//?????????????????? ???????????????????????????
            }
            //????????????
            if ("2".equals(fmSw.getLabel())) {
                PubFlowLog pub = new PubFlowLog();
                pub.setPerformerId(ShiroUtils.getUserId());
                pub.setLogType("FmSw");
                List<PubFlowLog> pgList = pubFlowLogService.selectPubFlowLogList(pub);
                List<FmSw> listFmSw = new ArrayList<>();
                Set<String> idList = new HashSet<>();
                for (PubFlowLog pb : pgList) {
                    if (!idList.contains(pb.getBizId())) {
                        String taskName = pb.getTaskName();
                        if (!"????????????".equals(taskName) && !"??????".equals(taskName) && !"????????????".equals(taskName)) {
                            is = fmSw;
                            is.setFmSwId(pb.getBizId());
                            is.setCreateId("");
                            startPage();
                            List<FmSw> fmSws = fmSwService.selectFmSwList(is);
                            if (fmSws.size() > 0) {
                                fmSws.get(0).getParams().put("performerTime", pb.getPerformerTime());
                            }
                            listFmSw.addAll(fmSws);
                            idList.add(pb.getBizId());
                        }
                    }
                }
                return getDataTable_ideal(listFmSw);
            }
        }
        startPage();
        List<FmSw> list = fmSwService.selectFmSwList(fmSw);
        return getDataTable(list);
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/fmList")
    @ResponseBody
    public TableDataInfo fmList(FmSw fmSw) {

        if (!StringUtils.isEmpty(fmSw.getStartCreateTime())) {
            String startCreateTime = DateUtils.formatDateStr(fmSw.getStartCreateTime(), DateUtils.YYYY_MM_DD,
                DateUtils.YYYYMMDD);
            fmSw.setStartCreateTime(startCreateTime + FmSwConstants.start_suffix);
        }

        if (!StringUtils.isEmpty(fmSw.getEndCreateTime())) {
            String endCreateTime = DateUtils.formatDateStr(fmSw.getEndCreateTime(), DateUtils.YYYY_MM_DD,
                DateUtils.YYYYMMDD);
            fmSw.setEndCreateTime(endCreateTime + FmSwConstants.end_suffix);
        }

        if (!StringUtils.isEmpty(fmSw.getFaultKind())) {
            FmSwMb fmswmb = fmSwMbService.selectById(fmSw.getFaultKind());
            fmSw.setFaultKind(fmswmb.getFaultKind());
        }
        //???????????????????????????????????????
        String personId = ShiroUtils.getOgUser().getpId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(personId);
        OgOrg ogOrg = deptService.selectDeptById(ogPerson.getOrgId());
        String orgLv = ogOrg.getOrgLv();
        if (!"1".equals(orgLv)) {
            fmSw.setLabel(orgLv);
            fmSw.setCreateOrgId(ogOrg.getOrgId());
            fmSw.setDealOrgId(ogOrg.getOrgId());
        }
        startPage();
        List<FmSw> list = fmSwService.selectList(fmSw);
        return getDataTable(list);
    }

    /**
     * ???????????????????????????
     *
     * @param fmSw
     * @return
     */
    @Log(title = "???????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmSw fmSw) {
        FmSw is = null;

        if (StringUtils.isNotEmpty(fmSw.getStartCreateTime())) {
            String parseStart = DateUtils.handleTimeYYYYMMDD(fmSw.getStartCreateTime());
            fmSw.setStartCreateTime(parseStart + FmSwConstants.start_suffix);
        }
        if (StringUtils.isNotEmpty(fmSw.getEndCreateTime())) {
            String parseEnd = DateUtils.handleTimeYYYYMMDD(fmSw.getEndCreateTime());
            fmSw.setEndCreateTime(parseEnd + FmSwConstants.end_suffix);
        }

        String isCurrentPage = (String)fmSw.getParams().get("currentPage");
        OgUser u = ShiroUtils.getOgUser();
        fmSw.setCreateId(u.getpId());//?????????????????? ???????????????????????????
        if (!StringUtils.isEmpty(fmSw.getLabel())) {
            String myFlag = fmSw.getLabel();
            //????????????
            if ("2".equals(fmSw.getLabel())) {
                PubFlowLog pub = new PubFlowLog();
                pub.setPerformerId(ShiroUtils.getUserId());
                pub.setLogType("FmSw");
                List<PubFlowLog> pgList = pubFlowLogService.selectPubFlowLogList(pub);
                List<FmSw> listFmSw = new ArrayList<>();
                Set<String> idList = new HashSet<>();
                for (PubFlowLog pb : pgList) {
                    if (!idList.contains(pb.getBizId())) {
                        String taskName = pb.getTaskName();
                        if (!"????????????".equals(taskName) && !"??????".equals(taskName) && !"????????????".equals(taskName)) {
                            is = fmSw;
                            is.setFmSwId(pb.getBizId());
                            is.setCreateId("");
                            startPage();
                            List<FmSw> fmSws = fmSwService.selectFmSwList(is);
                            if (fmSws.size() > 0) {
                                fmSws.get(0).getParams().put("performerTime", pb.getPerformerTime());
                            }
                            listFmSw.addAll(fmSws);
                            idList.add(pb.getBizId());
                        }
                    }
                }
                if ("currentPage".equals(isCurrentPage)) {
                    startPage();
                    ExcelUtil<FmSw> util = new ExcelUtil<FmSw>(FmSw.class);
                    return util.exportExcel(listFmSw, "???????????????");
                } else {
                    PageDomain pageDomain = TableSupport.buildPageRequest();
                    String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
                    PageHelper.orderBy(orderBy);
                    ExcelUtil<FmSw> util = new ExcelUtil<FmSw>(FmSw.class);
                    return util.exportExcel(listFmSw, "???????????????");
                }
            }
        }
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<FmSw> list = fmSwService.selectFmSwList(fmSw);
            ExcelUtil<FmSw> util = new ExcelUtil<FmSw>(FmSw.class);
            return util.exportExcel(list, "???????????????");
        } else {
            PageDomain pageDomain = TableSupport.buildPageRequest();
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
            List<FmSw> list = fmSwService.selectFmSwList(fmSw);
            ExcelUtil<FmSw> util = new ExcelUtil<FmSw>(FmSw.class);
            return util.exportExcel(list, "???????????????");
        }

    }

    /**
     * ??????????????????????????????
     *
     * @param userId
     * @param modelMap
     * @return
     */
    @GetMapping("/add/{userId}")
    public String add(@PathVariable("userId") String userId, ModelMap modelMap) {
        String bizType = "SJSW";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        modelMap.put("num", bizType + nowDateStr + numStr);
        //???????????????????????????????????????
        OgUser ogUser = new OgUser();
        ogUser.setUserId(userId);
        List<OgUser> ogUsers = ogUserService.selectAccountList(ogUser);
        modelMap.put("org", ogUsers.get(0));

        String uid = ogUser.getUserId();
        OgPerson ogPerson = ogPersonService.selectOgPersonById(uid);
        String orgId = ogPerson.getOrgId();
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        if (!StringUtils.isEmpty(ogOrg)) {
            String orgLv = ogOrg.getOrgLv();
            if (Integer.valueOf(orgLv) > 3) {
                String s = deptService.getpCode(uid);
                if (!s.equals("010000888")) {
                    String orgName = ogOrg.getOrgName();
                    modelMap.put("treeName", orgName);
                    modelMap.put("createOrgId", ogOrg.getOrgId());
                }
            }
        }

        modelMap.put("pId", ShiroUtils.getUserId());
        modelMap.put("fmSwId", UUID.getUUIDStr());
        return prefix + "/add";
    }

    /**
     * ??????????????????????????? ?????????????????????
     *
     * @param fmSw
     * @return
     */
    @Log(title = "???????????????", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FmSw fmSw) {

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());

        fmSw.setCreateId(ShiroUtils.getUserId());
        fmSw.setCreateTime(DateUtils.dateTimeNow());
        String processStatus = "1";
        //???????????????????????????id
        if (StringUtils.isNotEmpty(fmSw.getFaultKind())) {
            String faultKind = fmSw.getFaultKind();
            FmSwMb fmSwMb = fmSwMbService.selectById(faultKind);
            processStatus = fmSwMb.getProcessStatus();
            fmSw.setFaultKind(fmSwMb.getFaultKind());
        }

        String personId = "";
        fmSw.setInvalidationMark("1");
        if (processStatus.equals("1")) {
            fmSw.setDealerFiveId("");
            fmSw.setCheckerTwoId("");
            fmSw.setCheckerThreeId("");
            fmSw.setCheckerFourId("");
            //?????????????????????????????????????????????
            if ("commit".equals(fmSw.getLabel())) {
                if (StringUtils.isNotEmpty(fmSw.getCheckedId())) {//?????????
                    reMap.put("reCode", "0");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkId", fmSw.getCheckedId());
                    personId = fmSw.getCheckedId();
                } else {
                    //???????????????????????????????????????
                    if (StringUtils.isNotEmpty(fmSw.getAuthId())) {//?????????
                        reMap.put("reCode", "1");
                        reMap.put("createId", ShiroUtils.getUserId());
                        reMap.put("autherId", fmSw.getAuthId());
                        personId = fmSw.getAuthId();
                    } else {
                        //????????? ?????????????????????
                        if (StringUtils.isNotEmpty(fmSw.getDealerId())) {//?????????
                            reMap.put("reCode", "2");
                            reMap.put("createId", ShiroUtils.getUserId());
                            reMap.put("dealId", fmSw.getDealerId());
                            reMap.put("userId", ShiroUtils.getUserId());
                            personId = fmSw.getDealerId();
                        }
                    }
                }

                try {
                    if (StringUtils.isEmpty(fmSw.getFmSwId())) {
                        fmSw.setFmSwId(UUID.getUUIDStr());
                        fmSwService.insertFmSw(fmSw);
                    } else {
                        fmSwService.updateFmSw(fmSw);
                    }
                    activitiCommService.startProcess(fmSw.getFmSwId(), "FmSw", reMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("???????????????????????????: {} ", e);
                    throw new BusinessException("???????????????????????????,???????????????????????????");
                }
                FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
                String smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
                OgPerson person = ogPersonService.selectOgPersonById(personId);// ?????????????????????

                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("?????????????????????????????????:{} ", e);
                }
                return AjaxResult.success("????????????");
            }
        } else {
            fmSw.setDealerId("");
            fmSw.setAuthId("");
            fmSw.setCheckedId("");
            //?????????????????????????????????????????????
            if ("commit".equals(fmSw.getLabel())) {
                if (StringUtils.isNotEmpty(fmSw.getCheckerTwoId())) {//?????????2
                    reMap.put("reCode", "0");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerTwoId", fmSw.getCheckerTwoId());
                    personId = fmSw.getCheckerTwoId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerThreeId())) {//?????????3
                    reMap.put("reCode", "1");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerThreeId", fmSw.getCheckerThreeId());
                    personId = fmSw.getCheckerThreeId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerFourId())) {//?????????4{
                    reMap.put("reCode", "2");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerFourId", fmSw.getCheckerFourId());
                    personId = fmSw.getCheckerFourId();
                } else if (StringUtils.isNotEmpty(fmSw.getDealerFiveId())) {
                    //?????????2 ?????????3 ?????????4????????????
                    reMap.put("reCode", "3");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("dealerFiveId", fmSw.getDealerFiveId());
                    reMap.put("userId", ShiroUtils.getUserId());
                    personId = fmSw.getDealerId();
                }

                try {
                    if (StringUtils.isEmpty(fmSw.getFmSwId())) {
                        fmSw.setFmSwId(UUID.getUUIDStr());
                        fmSwService.insertFmSw(fmSw);
                    } else {
                        fmSwService.updateFmSw(fmSw);
                    }
                    activitiCommService.startProcess(fmSw.getFmSwId(), "FmSwBf", reMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("???????????????????????????: {} ", e);
                    throw new BusinessException("???????????????????????????,???????????????????????????");
                }
                FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
                String smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
                OgPerson person = ogPersonService.selectOgPersonById(personId);// ?????????????????????

                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("?????????????????????????????????:{} ", e);
                }
                return AjaxResult.success("????????????");
            }
        }

        try {
            if (StringUtils.isEmpty(fmSw.getFmSwId())) {
                fmSw.setFmSwId(UUID.getUUIDStr());
                fmSwService.insertFmSw(fmSw);
            } else {
                fmSwService.updateFmSw(fmSw);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("???????????????????????????: {} ", e);
            return AjaxResult.error("???????????????????????????");
        }
        return AjaxResult.success("???????????????????????????");
    }

    /**
     * ????????????????????????????????????
     *
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        FmSw fmSw = new FmSw();
        fmSw.setFmSwId(id);
        List<FmSw> fmSws = fmSwService.selectFmSwList(fmSw);
        FmSw fm_sw = fmSws.get(0);
        //??????????????????????????????????????????
        if (StringUtils.isNotEmpty(fm_sw.getDealOrgId())) {
            String dealOrgId = fm_sw.getDealOrgId();
            FmSwMb fmSwMb = new FmSwMb();
            fmSwMb.setDealOrgId(dealOrgId);
            List<FmSwMb> fmSwMbList = fmSwMbService.selectFmSwMbList(fmSwMb);
            if (StringUtils.isNotEmpty(fmSwMbList)) {
                mmap.put("fmSwMbList", fmSwMbList);
            }
            if (StringUtils.isNotEmpty(fm_sw.getFaultKind()) && StringUtils.isNotNull(fm_sw.getFaultKind())) {
                fmSwMb.setFaultKind(fm_sw.getFaultKind());
                //???????????????????????????????????????Id
                FmSwMb fm_sw_mb = fmSwMbService.selectFmSwMbByDealOrgIdAndFaultKind(fmSwMb);
                if (StringUtils.isNotNull(fm_sw_mb)) {
                    fm_sw.setFaultKind(fm_sw_mb.getSwmdId());
                }
            }

        }
        if (fm_sw.getProcessStatus().equals("1")) {
            if (StringUtils.isNotEmpty(fm_sw.getCreateOrgId()) && StringUtils.isNotNull(fm_sw.getCreateOrgId())) {
                //??????????????????????????????
                OgOrg ogOrg = deptService.selectDeptById(fm_sw.getCreateOrgId());
                mmap.put("ogOrg", ogOrg);
                String levelCode = ogOrg.getLevelCode();
                //?????????
                List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, FmSwConstants.checkRole);
                mmap.put("checkList", checkList);
            }

            if (StringUtils.isNotEmpty(fm_sw.getDealOrgId()) && StringUtils.isNotNull(fm_sw.getDealOrgId())) {

                String rId = getConnectRole();
                //???????????????
                List<Object> objects = levelcodeAndMbInfo(fm_sw.getDealOrgId(), rId);
                mmap.put("authorList", objects.get(0));
                //???????????????
                mmap.put("dealList", objects.get(1));
            }
        } else {
            if (StringUtils.isNotEmpty(fm_sw.getCreateOrgId()) && StringUtils.isNotNull(fm_sw.getCreateOrgId())) {
                //??????????????????????????????
                OgOrg ogOrg = deptService.selectDeptById(fm_sw.getCreateOrgId());
                mmap.put("ogOrg", ogOrg);
                String levelCode = ogOrg.getLevelCode();
                //?????????
                List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, FmSwConstants.checkRole);
                mmap.put("checkList", checkList);
            }

            if (StringUtils.isNotEmpty(fm_sw.getDealOrgId()) && StringUtils.isNotNull(fm_sw.getDealOrgId())) {

                String rId = getConnectRole();
                //???????????????
                List<Object> objects = levelcodeAndMbInfo(fm_sw.getDealOrgId(), rId);
                mmap.put("authorList", objects.get(0));
            }
            if (StringUtils.isNotEmpty(fm_sw.getDealOrgId()) && StringUtils.isNotNull(fm_sw.getDealOrgId())) {

                String rId = getConnectRole();
                //???????????????
                List<Object> objects = levelcodeAndMbInfo(fm_sw.getDealOrgId(), rId);
                //???????????????
                mmap.put("dealList", objects.get(1));
            }
        }

        mmap.put("fmSw", fm_sw);
        mmap.put("pId", ShiroUtils.getUserId());

        return prefix + "/edit";
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/returnedEdit/{id}")
    public String returnedEdit(@PathVariable("id") String id, ModelMap mmap) {
        FmSw fmSw = new FmSw();
        fmSw.setFmSwId(id);
        List<FmSw> fmSws = fmSwService.selectFmSwList(fmSw);
        FmSw fm_sw = fmSws.get(0);
        //??????????????????????????????????????????
        if (StringUtils.isNotEmpty(fm_sw.getDealOrgId()) && StringUtils.isNotNull(fm_sw.getDealOrgId())) {
            String dealOrgId = fm_sw.getDealOrgId();
            FmSwMb fmSwMb = new FmSwMb();
            fmSwMb.setDealOrgId(dealOrgId);
            List<FmSwMb> fmSwMbList = fmSwMbService.selectFmSwMbList(fmSwMb);
            if (fmSwMbList != null && fmSwMbList.size() > 0) {
                mmap.put("fmSwMbList", fmSwMbList);
            }
            if (StringUtils.isNotEmpty(fm_sw.getFaultKind()) && StringUtils.isNotNull(fm_sw.getFaultKind())) {
                fmSwMb.setFaultKind(fm_sw.getFaultKind());
                //???????????????????????????????????????Id
                FmSwMb fm_sw_mb = fmSwMbService.selectFmSwMbByDealOrgIdAndFaultKind(fmSwMb);
                if (StringUtils.isNotNull(fm_sw_mb)) {
                    fm_sw.setFaultKind(fm_sw_mb.getSwmdId());
                }

            }

        }

        if (StringUtils.isNotEmpty(fm_sw.getCreateOrgId()) && StringUtils.isNotNull(fm_sw.getCreateOrgId())) {
            String pId = ShiroUtils.getUserId();
            //??????????????????????????????
            OgOrg ogOrg = deptService.selectDeptById(fm_sw.getCreateOrgId());
            mmap.put("ogOrg", ogOrg);
            String levelCode = ogOrg.getLevelCode();
            //?????????
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, FmSwConstants.checkRole);
            mmap.put("checkList", checkList);
        }

        if (StringUtils.isNotEmpty(fm_sw.getDealOrgId()) && StringUtils.isNotNull(fm_sw.getDealOrgId())) {
            String rId = getConnectRole();
            //???????????????
            List<Object> objects = levelcodeAndMbInfo(fm_sw.getDealOrgId(), rId);
            mmap.put("authorList", objects.get(0));
            //???????????????
            mmap.put("dealList", objects.get(1));
        }

        //??????????????????
        String createTime = fm_sw.getCreateTime();
        if (StringUtils.isNotEmpty(createTime)) {
            fm_sw.setCreateTime(
                DateUtils.formatDateStr(createTime, DateUtils.YYYYMMDDHHMMSS, DateUtils.YYYY_MM_DD_HH_MM));
        }

        mmap.put("fmSw", fm_sw);
        mmap.put("pId", ShiroUtils.getUserId());

        return prefix + "/returned_edit";
    }

    /**
     * ????????????/?????????????????????
     *
     * @param fmSw
     * @return
     */
    @Log(title = "???????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmSw fmSw) {
        FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
        String processStatus = fmSw.getProcessStatus();
        if (StringUtils.isNotEmpty(fmSw.getFaultKind())) {
            String faultKind = fmSw.getFaultKind();
            FmSwMb fmSwMb = fmSwMbService.selectById(faultKind);
            fmSw.setFaultKind(fmSwMb.getFaultKind());
        }
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());

        String personId = "";
        if ("1".equals(fmSw.getProcessStatus())) {
            fmSw.setDealerFiveId("");
            fmSw.setCheckerTwoId("");
            fmSw.setCheckerThreeId("");
            fmSw.setCheckerFourId("");
            //?????????????????????????????????????????????
            if ("commit".equals(fmSw.getLabel())) {
                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSw";
                //?????????????????????
                if (StringUtils.isNotEmpty(fmSw.getCheckedId())) {
                    reMap.put("reCode", "0");
                    reMap.put("checkId", fmSw.getCheckedId());
                    personId = fmSw.getCheckedId();
                } else {
                    //???????????????????????????????????????
                    if (StringUtils.isNotEmpty(fmSw.getAuthId())) {//?????????
                        reMap.put("reCode", "1");
                        reMap.put("createId", ShiroUtils.getUserId());
                        reMap.put("autherId", fmSw.getAuthId());
                        personId = fmSw.getAuthId();
                    } else {
                        //????????? ?????????????????????
                        if (StringUtils.isNotEmpty(fmSw.getDealerId())) {
                            reMap.put("reCode", "2");
                            reMap.put("dealId", fmSw.getDealerId());
                            personId = fmSw.getDealerId();
                        }
                    }
                }

                try {
                    fmSwService.updateFmSw(fmSw);
                    activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("?????????????????????????????????: {} ", e);
                    throw new BusinessException("?????????????????????????????????,???????????????????????????");
                }

                String smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
                OgPerson person = ogPersonService.selectOgPersonById(personId);// ?????????????????????
                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("???????????????????????????????????????:{} ", e);
                }
                return AjaxResult.success("????????????");
            }
        } else {
            fmSw.setDealerId("");
            fmSw.setAuthId("");
            fmSw.setCheckedId("");
            //?????????????????????????????????????????????
            if ("commit".equals(fmSw.getLabel())) {
                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSwBf";
                if (StringUtils.isNotEmpty(fmSw.getCheckerTwoId())) {//?????????2
                    reMap.put("reCode", "0");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerTwoId", fmSw.getCheckerTwoId());
                    personId = fmSw.getCheckerTwoId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerThreeId())) {//?????????3
                    reMap.put("reCode", "1");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerThreeId", fmSw.getCheckerThreeId());
                    personId = fmSw.getCheckerThreeId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerFourId())) {//?????????4{
                    reMap.put("reCode", "2");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerFourId", fmSw.getCheckerFourId());
                    personId = fmSw.getCheckerFourId();
                } else if (StringUtils.isNotEmpty(fmSw.getDealerFiveId())) {
                    //?????????2 ?????????3 ?????????4????????????
                    reMap.put("reCode", "3");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("dealerFiveId", fmSw.getDealerFiveId());
                    reMap.put("userId", ShiroUtils.getUserId());
                    personId = fmSw.getDealerId();
                }

                try {
                    fmSwService.updateFmSw(fmSw);
                    activitiCommService.startProcess(businessKey, processDefinitionKey, reMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("?????????????????????????????????: {} ", e);
                    throw new BusinessException("?????????????????????????????????,???????????????????????????");
                }

                String smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
                OgPerson person = ogPersonService.selectOgPersonById(personId);// ?????????????????????
                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("???????????????????????????????????????:{} ", e);
                }
                return AjaxResult.success("????????????");
            }
        }

        try {
            fmSwService.updateFmSw(fmSw);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("?????????????????????????????????: {} ", e);
            return AjaxResult.error("?????????????????????????????????");
        }
        return AjaxResult.success("?????????????????????????????????");
    }

    /**
     * ????????????????????????????????? ????????????/?????????????????????
     *
     * @param fmSw
     * @return
     */
    @Log(title = "???????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/returnedEdit")
    @ResponseBody
    public AjaxResult returnedEditSave(FmSw fmSw) {

        FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
        String processStatus = null;
        if (StringUtils.isNotEmpty(fmSw.getFaultKind())) {
            String faultKind = fmSw.getFaultKind();
            FmSwMb fmSwMb = fmSwMbService.selectById(faultKind);
            processStatus = fmSwMb.getProcessStatus();
            fmSw.setFaultKind(fmSwMb.getFaultKind());
            fmSw.setProcessStatus(processStatus);
        } else {
            processStatus = fmSw.getProcessStatus();
        }
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());
        String personId = "";
        if (processStatus.equals("1")) {
            fmSw.setDealerFiveId("");
            fmSw.setCheckerTwoId("");
            fmSw.setCheckerThreeId("");
            fmSw.setCheckerFourId("");
            //?????????????????????????????????????????????
            if ("recommit".equals(fmSw.getLabel())) {
                //?????????????????????
                String businessKey = fmSw.getFmSwId();

                String processDefinitionKey = "FmSw";
                if (StringUtils.isNotEmpty(fmSw.getCheckedId())) {//?????????
                    reMap.put("businessKey", businessKey);
                    reMap.put("processDefinitionKey", processDefinitionKey);
                    reMap.put("reCode", "0");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkId", fmSw.getCheckedId());
                    personId = fmSw.getCheckedId();
                } else {
                    //???????????????????????????????????????
                    if (StringUtils.isNotEmpty(fmSw.getAuthId())) {//?????????
                        reMap.put("businessKey", businessKey);
                        reMap.put("processDefinitionKey", processDefinitionKey);
                        reMap.put("reCode", "1");
                        reMap.put("createId", ShiroUtils.getUserId());
                        reMap.put("autherId", fmSw.getAuthId());
                        personId = fmSw.getAuthId();
                    } else {
                        //????????? ?????????????????????
                        if (StringUtils.isNotEmpty(fmSw.getDealerId())) {//?????????
                            reMap.put("businessKey", businessKey);
                            reMap.put("processDefinitionKey", processDefinitionKey);
                            reMap.put("reCode", "2");
                            reMap.put("createId", ShiroUtils.getUserId());
                            reMap.put("dealId", fmSw.getDealerId());
                            personId = fmSw.getDealerId();
                        }
                    }

                }
                reMap.put("comment", "??????");
                activitiCommService.complete(reMap);
                //????????????

                String smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
                OgPerson person = ogPersonService.selectOgPersonById(personId);// ?????????????????????
                sendSms(smsText, person);
                return toAjax(fmSwService.returnEditUpdateFmSw(fmSw));
            } else {
                //????????????
                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSw";
                reMap.put("businessKey", businessKey);
                reMap.put("processDefinitionKey", processDefinitionKey);
                reMap.put("reCode", "3");
                activitiCommService.complete(reMap);
                return toAjax(fmSwService.invalidateUpdateFmSw(fmSw));
            }
        } else {
            fmSw.setDealerId("");
            fmSw.setAuthId("");
            fmSw.setCheckedId("");
            //?????????????????????????????????????????????
            if ("recommit".equals(fmSw.getLabel())) {
                //?????????????????????
                String businessKey = fmSw.getFmSwId();

                String processDefinitionKey = "FmSwBf";
                if (StringUtils.isNotEmpty(fmSw.getCheckerTwoId())) {//?????????2
                    reMap.put("reCode", "0");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerTwoId", fmSw.getCheckerTwoId());
                    personId = fmSw.getCheckerTwoId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerThreeId())) {//?????????3
                    reMap.put("reCode", "1");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerThreeId", fmSw.getCheckerThreeId());
                    personId = fmSw.getCheckerThreeId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerFourId())) {//?????????4{
                    reMap.put("reCode", "2");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerFourId", fmSw.getCheckerFourId());
                    personId = fmSw.getCheckerFourId();
                } else if (StringUtils.isNotEmpty(fmSw.getDealerFiveId())) {
                    //?????????2 ?????????3 ?????????4????????????
                    reMap.put("reCode", "3");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("dealerFiveId", fmSw.getDealerFiveId());
                    reMap.put("userId", ShiroUtils.getUserId());
                    personId = fmSw.getDealerFiveId();
                }

                reMap.put("processDefinitionKey", processDefinitionKey);
                reMap.put("businessKey", businessKey);
                reMap.put("comment", "??????");
                activitiCommService.complete(reMap);
                //????????????

                String smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
                OgPerson person = ogPersonService.selectOgPersonById(personId);// ?????????????????????
                sendSms(smsText, person);
                return toAjax(fmSwService.returnEditUpdateFmSw(fmSw));
            } else {
                //????????????
                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSwBf";
                reMap.put("businessKey", businessKey);
                reMap.put("processDefinitionKey", processDefinitionKey);
                reMap.put("reCode", "4");
                activitiCommService.complete(reMap);
                return toAjax(fmSwService.invalidateUpdateFmSw(fmSw));
            }
        }

    }

    /**
     * ?????????????????????
     *
     * @param ids
     * @return
     */
    @Log(title = "???????????????", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(fmSwService.deleteFmSwByIds(ids));
    }

    /**
     * ??????id?????????????????????
     *
     * @param id
     * @return
     */
    @PostMapping("/selectById")
    @ResponseBody
    public AjaxResult selectById(String id) {
        AjaxResult ajaxResult = new AjaxResult();
        FmSw fmSw = fmSwService.selectFmSwById(id);
        ajaxResult.put("data", fmSw);
        return ajaxResult;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param dealOrgId
     * @param rId
     * @return
     */
    private List<Object> levelcodeAndMbInfo(String dealOrgId, String rId) {
        //?????????????????????????????????
        OgOrg ogOrg = deptService.selectDeptById(dealOrgId);
        String levelCode = ogOrg.getLevelCode();
        String[] split = rId.split(",");

        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        //?????????
        List<OgPerson> authorList = ogPersonService.selectListByLevelCode(oneLvOrTwoLv.getLevelCode(), split[0]);
        //?????????
        List<OgPerson> dealList = ogPersonService.selectListByLevelCode(levelCode, split[1]);
        //????????????
        FmSwMb fmSwMb = new FmSwMb();
        fmSwMb.setDealOrgId(dealOrgId);
        List<FmSwMb> fmSwMbList = fmSwMbService.selectFmSwMbList(fmSwMb);
        List<Object> list = new ArrayList<>();
        list.add(authorList);
        list.add(dealList);
        list.add(fmSwMbList);
        return list;
    }

    /**
     * ???????????????????????????
     *
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") String id, ModelMap mmap) {
        FmSw fmSw = new FmSw();
        fmSw.setFmSwId(id);
        List<FmSw> fmSws = fmSwService.selectFmSwList(fmSw);
        FmSw fm_sw = fmSws.get(0);
        mmap.put("fmSw", fm_sw);
        return prefix + "/details";
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/detailsNoBtn/{id}")
    public String detailsNoBtn(@PathVariable("id") String id, ModelMap mmap) {
        FmSw fmSw = new FmSw();
        fmSw.setFmSwId(id);
        List<FmSw> fmSws = fmSwService.selectFmSwList(fmSw);
        FmSw fm_sw = fmSws.get(0);
        mmap.put("fmSw", fm_sw);
        return prefix + "/detailsNoBtn";
    }

    /**
     * ???????????????????????????
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/check")
    public String smCheck(ModelMap modelMap) {
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_check";
    }

    /**
     * ????????????
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/checkEdit/{id}")
    public String checkEdit(@PathVariable("id") String id, ModelMap modelMap) {
        Map<String, Object> map = getList(id);
        FmSw fmSw=fmSwService.getProcessStatus(id);
        modelMap.put("fmSwMbList", map.get("fmSwMbList"));
        modelMap.put("authorList", map.get("authorList"));
        modelMap.put("dealList", map.get("dealList"));
        modelMap.put("ogOrg", map.get("ogOrg"));
        modelMap.put("checkList", map.get("checkList"));
        modelMap.put("fmSw", map.get("fmSw"));
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        if("2".equals(fmSw.getProcessStatus())){
            if("11".equals(fmSw.getCurrentState())){
                return prefix + "/sw_check_two_option";
            }else if("12".equals(fmSw.getCurrentState())){
                return prefix + "/sw_check_three_option";
            }else if("13".equals(fmSw.getCurrentState())){
                return prefix + "/sw_check_four_option";
            }
        }
        return prefix + "/sw_check_option";
    }

    private Map<String, Object> getList(String id) {
        Map<String, Object> map = new HashMap();
        //????????????Id????????????????????????
        FmSw fmSw = new FmSw();
        fmSw.setFmSwId(id);
        List<FmSw> fmSws = fmSwService.selectFmSwList(fmSw);
        if (!fmSws.isEmpty() && fmSws.size() > 0) {
            fmSw = fmSws.get(0);

        }

        //??????????????????????????????????????????
        if (!StringUtils.isEmpty(fmSw.getDealOrgId())) {
            String dealOrgId = fmSw.getDealOrgId();
            FmSwMb fmSwMb = new FmSwMb();
            fmSwMb.setDealOrgId(dealOrgId);
            List<FmSwMb> fmSwMbList = fmSwMbService.selectFmSwMbList(fmSwMb);
            if (fmSwMbList != null && fmSwMbList.size() > 0) {
                map.put("fmSwMbList", fmSwMbList);

            }
            if (!StringUtils.isEmpty(fmSw.getFaultKind())) {
                fmSwMb.setFaultKind(fmSw.getFaultKind());
                //???????????????????????????????????????Id
                FmSwMb fm_sw_mb = fmSwMbService.selectFmSwMbByDealOrgIdAndFaultKind(fmSwMb);
                if (StringUtils.isNotNull(fm_sw_mb)) {
                    fmSw.setFaultKind(fm_sw_mb.getSwmdId());
                }
            }

        }

        if (StringUtils.isNotEmpty(fmSw.getDealOrgId())) {
            String rId = getConnectRole();
            //???????????????
            List<Object> objects = levelcodeAndMbInfo(fmSw.getDealOrgId(), rId);
            map.put("authorList", objects.get(0));
            //???????????????
            map.put("dealList", objects.get(1));
        }

        String auditTime = fmSw.getAuditTime();
        if (StringUtils.isNotEmpty(auditTime)) {
            fmSw.setAuditTime(DateUtils.formatDateStr(auditTime, DateUtils.YYYYMMDDHHMMSS, DateUtils.YYYY_MM_DD_HH_MM));
        }

        if (!StringUtils.isEmpty(fmSw.getCreateOrgId())) {
            //??????????????????????????????
            OgOrg ogOrg = deptService.selectDeptById(fmSw.getCreateOrgId());
            map.put("ogOrg", ogOrg);
            String levelCode = ogOrg.getLevelCode();
            //?????????
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, FmSwConstants.checkRole);
            map.put("checkList", checkList);
        }
        //auditorId
        if (!StringUtils.isEmpty(fmSw.getAuditorId())) {
            OgPerson ogPerson = ogPersonService.selectOgPersonById(fmSw.getAuditorId());
            fmSw.setAuditorId(ogPerson.getpName());
        }
        map.put("fmSw", fmSw);
        return map;
    }

    /**
     * ?????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/auditPass")
    @ResponseBody
    public AjaxResult auditPass(FmSw fmSw) {
        //???????????????
        String pId = ShiroUtils.getUserId();
        fmSw.setAuditorId(pId);
        //??????????????????
        String auditTime = DateUtils.dateTimeNow();
        fmSw.setAuditTime(auditTime);
        return toAjax(fmSwService.auditUpdateFmSw(fmSw));
    }

    /**
     * ????????????????????????
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/author")
    public String smAuthor(ModelMap modelMap) {
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_author";
    }

    /**
     * ????????????
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/authorEdit/{id}")
    public String authorEdit(@PathVariable("id") String id, ModelMap modelMap) {
        Map<String, Object> map = getList(id);
        modelMap.put("fmSwMbList", map.get("fmSwMbList"));
        modelMap.put("authorList", map.get("authorList"));
        modelMap.put("dealList", map.get("dealList"));
        modelMap.put("ogOrg", map.get("ogOrg"));
        modelMap.put("checkList", map.get("checkList"));
        modelMap.put("fmSw", map.get("fmSw"));
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_author_option";
    }

    /**
     * ????????????????????????
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/deal")
    public String smDeal(ModelMap modelMap) {
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_deal";
    }

    /**
     * ????????????
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/dealEdit/{id}")
    public String dealEdit(@PathVariable("id") String id, ModelMap modelMap) {
        Map<String, Object> map = getList(id);
        FmSw fmSw=fmSwService.getProcessStatus(id);
        modelMap.put("fmSwMbList", map.get("fmSwMbList"));
        modelMap.put("authorList", map.get("authorList"));
        modelMap.put("dealList", map.get("dealList"));
        modelMap.put("ogOrg", map.get("ogOrg"));
        modelMap.put("checkList", map.get("checkList"));
        modelMap.put("fmSw", map.get("fmSw"));
        if("2".equals(fmSw.getProcessStatus())&&"14".equals(fmSw.getCurrentState())){
            return prefix + "/sw_deal_person_option";
        }
        return prefix + "/sw_deal_option";
    }

    /**
     * ???????????????????????????
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/closed")
    public String smClosed(ModelMap modelMap) {
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_closed";
    }

    /**
     * ????????????
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/auditEdit/{id}")
    public String auditEdit(@PathVariable("id") String id, ModelMap modelMap) {
        Map<String, Object> map = getList(id);
        modelMap.put("fmSwMbList", map.get("fmSwMbList"));
        modelMap.put("authorList", map.get("authorList"));
        modelMap.put("dealList", map.get("dealList"));
        modelMap.put("ogOrg", map.get("ogOrg"));
        modelMap.put("checkList", map.get("checkList"));
        modelMap.put("fmSw", map.get("fmSw"));
        return prefix + "/sw_closed_audit_option";
    }

    /**
     * ???????????????????????????????????????
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/query")
    public String smQuery(ModelMap modelMap) {
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_query";
    }

    /**
     * ???????????????????????????????????????
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/query_sw")
    public String querySm(ModelMap modelMap) {
        //?????????????????????????????????
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getUserId());
        OgOrg ogOrg = deptService.selectDeptById(ogPerson.getOrgId());
        modelMap.put("ogOrg", ogOrg);
        //?????????????????????????????????????????????
        List<OgRole> ogRoles = sysRoleService.selectRolesByUserId(ShiroUtils.getUserId());
        String isHasRole = "false";
        for (OgRole role : ogRoles) {
            if (FmSwConstants.sourceRole.equals(role.getRid())) {
                isHasRole = "true";
            }
        }
        modelMap.put("isHasRole", isHasRole);
        //??????????????????
        return prefix + "/query_sw";
    }

    /**
     * ???????????????????????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/deallist")
    @ResponseBody
    public TableDataInfo deallist(FmSw fmSw) {

        List<Map<String, Object>> reList = activitiCommService.userTask("FmSw", null);
        List<Map<String, Object>> reBfList = activitiCommService.userTask("FmSwBf", null);
        List<OgGroup> userGroupList = sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String, Object>> regList = new ArrayList<>();
        reList.addAll(reBfList);
        if (userGroupList.size() > 0) {
            regList = activitiCommService.groupTasks("FmSw", "deal");
            reList.addAll(regList);
            reList.addAll(activitiCommService.groupTasks("FmSw", "deal"));
        }
        List<String> fmSwIds = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            if ("deal".equals(map.get("description"))) {
                Object businesskey = map.get("businesskey");
                if (StringUtils.isNotNull(businesskey)) {
                    fmSwIds.add(businesskey.toString());
                }
            }

        }
        List<FmSw> resultlist = null;
        if (fmSwIds.size() > 0) {
            fmSw.setIds(fmSwIds);
            startPage();
            resultlist = fmSwService.selectFmSwListByTask(fmSw);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());
    }

    /**
     * ?????????????????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/dealPass")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult dealPassOrReturnOrAuth(FmSw fmSw) {
        String smsText = "";
        FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
        OgPerson ogPerson = ogPersonService.selectOgPersonById(fm.getCreateId());
        Map<String, Object> reMap = new HashMap<>();
        String businessKey = fmSw.getFmSwId();
        String processDefinitionKey = "FmSw";
        //???????????????
        reMap.put("userId", ShiroUtils.getUserId());

        if ("05".equals(fmSw.getCurrentState())) {
            //????????????
            reMap.put("reCode", "0");
            reMap.put("businessKey", businessKey);
            reMap.put("processDefinitionKey", processDefinitionKey);
            reMap.put("dealId", fmSw.getDealerId());
            reMap.put("comment", fmSw.getLogPerformDesc());
            try {
                fmSwService.dealPassUpdateFmSw(fmSw);
                activitiCommService.complete(reMap);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("?????????????????????????????????: {}", e);
                throw new BusinessException("?????????????????????????????????,???????????????????????????");
            }
            smsText = "?????????????????????" + fm.getFaultNo() + "?????????????????????????????????????????????????????????";

            try {
                sendSms(smsText, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("?????????????????????????????????????????????: {}", e);
            }

            return AjaxResult.success("????????????");
        } else if ("08".equals(fmSw.getCurrentState())) {
            //???????????????
            reMap.put("reCode", "2");
            reMap.put("businessKey", businessKey);
            reMap.put("processDefinitionKey", processDefinitionKey);
            reMap.put("createId", fmSw.getCreateId());
            reMap.put("comment", fmSw.getLogPerformDesc());
            try {
                fmSwService.checkReturnUpdateFmSw(fmSw);
                activitiCommService.complete(reMap);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("?????????????????????????????????: {} ", e);
                throw new BusinessException("?????????????????????????????????,???????????????????????????");
            }
            smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
            try {
                sendSms(smsText, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("?????????????????????????????????????????????: {}", e);
            }
            return AjaxResult.success("????????????");
        } else {
            //????????????
            reMap.put("reCode", "1");
            reMap.put("businessKey", businessKey);
            reMap.put("processDefinitionKey", processDefinitionKey);
            reMap.put("autherId", fmSw.getAuthId());
            reMap.put("comment", fmSw.getLogPerformDesc());
            try {
                fmSwService.dealUpdateFmSw(fmSw);
                activitiCommService.complete(reMap);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("?????????????????????????????????: {}", e);
                throw new BusinessException("?????????????????????????????????,???????????????????????????");
            }
            smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
            ogPerson = ogPersonService.selectOgPersonById(fmSw.getAuthId());

            try {
                sendSms(smsText, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("?????????????????????????????????????????????: {} ", e);
            }
            return toAjax(fmSwService.dealUpdateFmSw(fmSw));
        }

    }

    /**
     * ?????????????????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/dealPersonPass")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult dealPersonPassOrReturnOrAuth(FmSw fmSw) {
        String smsText = "";
        FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
        OgPerson ogPerson = ogPersonService.selectOgPersonById(fm.getCreateId());
        Map<String, Object> reMap = new HashMap<>();
        String businessKey = fmSw.getFmSwId();
        String processDefinitionKey = "FmSw";
        //???????????????
        reMap.put("userId", ShiroUtils.getUserId());

        if ("15".equals(fmSw.getCurrentState())) {
            //????????????
            reMap.put("reCode", "0");
            reMap.put("businessKey", businessKey);
            reMap.put("processDefinitionKey", processDefinitionKey);
            reMap.put("dealId", fmSw.getDealerId());
            reMap.put("comment", fmSw.getLogPerformDesc());
            try {
                fmSwService.dealPassUpdateFmSw(fmSw);
                activitiCommService.complete(reMap);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("????????????????????????5????????????: {}", e);
                throw new BusinessException("????????????????????????5????????????,???????????????????????????");
            }
            smsText = "?????????????????????" + fm.getFaultNo() + "???????????????5?????????????????????????????????????????????";

            try {
                sendSms(smsText, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("????????????????????????5????????????????????????: {}", e);
            }

            return AjaxResult.success("????????????");
        } else if ("08".equals(fmSw.getCurrentState())) {
            //???????????????
            reMap.put("reCode", "2");
            reMap.put("businessKey", businessKey);
            reMap.put("processDefinitionKey", processDefinitionKey);
            reMap.put("createId", fmSw.getCreateId());
            reMap.put("comment", fmSw.getLogPerformDesc());
            try {
                fmSwService.checkReturnUpdateFmSw(fmSw);
                activitiCommService.complete(reMap);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("????????????????????????5????????????: {} ", e);
                throw new BusinessException("????????????????????????5????????????,???????????????????????????");
            }
            smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
            try {
                sendSms(smsText, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("????????????????????????5????????????????????????: {}", e);
            }
            return AjaxResult.success("????????????");
        } else {
            return AjaxResult.success("????????????");
        }

    }

    /**
     * ???????????????????????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/checklist")
    @ResponseBody
    public TableDataInfo checklist(FmSw fmSw) {
        if (StringUtils.isNotEmpty(fmSw.getStartCreateTime())) {
            String startCreateTime = DateUtils.formatDateStr(fmSw.getStartCreateTime(), DateUtils.YYYY_MM_DD,
                DateUtils.YYYYMMDD);
            fmSw.setStartCreateTime(startCreateTime + FmSwConstants.start_suffix);
        }

        if (StringUtils.isNotEmpty(fmSw.getEndCreateTime())) {
            String endCreateTime = DateUtils.formatDateStr(fmSw.getEndCreateTime(), DateUtils.YYYY_MM_DD,
                DateUtils.YYYYMMDD);
            fmSw.setEndCreateTime(endCreateTime + FmSwConstants.end_suffix);
        }
        List<Map<String, Object>> reList = activitiCommService.userTask("FmSw", null);
        List<Map<String, Object>> reBfList = activitiCommService.userTask("FmSwBf", null);
        List<OgGroup> userGroupList = sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String, Object>> regList = new ArrayList<>();
        List<Map<String, Object>> regBfList = new ArrayList<>();
        reList.addAll(reBfList);
        if (userGroupList.size() > 0) {
            regList = activitiCommService.groupTasks("FmSw", "check");
            reList.addAll(regList);
            reList.addAll(activitiCommService.groupTasks("FmSwBf", "audit2"));
            reList.addAll(activitiCommService.groupTasks("FmSwBf", "audit3"));
            reList.addAll(activitiCommService.groupTasks("FmSwBf", "audit4"));
        }
        List<String> fmSwIds = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            if ("check".equals(map.get("description"))||"audit2".equals(map.get("description"))||"audit3".equals(map.get("description"))||"audit4".equals(map.get("description"))) {
                String business_key = map.get("businesskey").toString();
                if (StringUtils.isNotEmpty(business_key)) {
                    fmSwIds.add(business_key);
                }
            }

        }
        List<FmSw> resultlist = null;
        if (fmSwIds.size() > 0) {
            fmSw.setIds(fmSwIds);
            startPage();
            resultlist = fmSwService.selectFmSwListByTask(fmSw);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());

    }

    /**
     * ?????????????????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/checkPass")
    @ResponseBody
    public AjaxResult checkPassOrReturn(FmSw fmSw) {
        FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
        String smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
        OgPerson person;
        if (fm.getProcessStatus().equals("1")) {
            //??????????????? ??????????????????
            if ("checkPass".equals(fmSw.getLabel())) {
                Map<String, Object> reMap = new HashMap<>();
                reMap.put("userId", ShiroUtils.getUserId());

                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSw";
                String personId = "";
                //1.??????????????????????????????
                if (StringUtils.isNotEmpty(fmSw.getAuthId())) {
                    //??????????????????
                    reMap.put("reCode", "0");
                    reMap.put("businessKey", businessKey);
                    reMap.put("processDefinitionKey", processDefinitionKey);
                    reMap.put("autherId", fmSw.getAuthId());
                    reMap.put("comment", fmSw.getLogPerformDesc());
                    personId = fmSw.getAuthId();
                } else {
                    //2.???????????????????????????????????????????????????
                    if (StringUtils.isNotEmpty(fmSw.getDealerId())) {
                        reMap.put("reCode", "1");
                        reMap.put("businessKey", businessKey);
                        reMap.put("processDefinitionKey", processDefinitionKey);
                        reMap.put("dealId", fmSw.getDealerId());
                        reMap.put("comment", fmSw.getLogPerformDesc());
                        personId = fmSw.getDealerId();
                    }
                }
                if (StringUtils.isNotEmpty(fmSw.getAuthId())) {
                    try {
                        fmSwService.checkUpdateFmSw(fmSw);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("?????????????????????????????????:{} ", e);
                        throw new BusinessException("?????????????????????????????????", "???????????????????????????");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// ?????????????????????

                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("?????????????????????????????????????????????:{} ", e);
                    }
                    return AjaxResult.success("????????????");
                } else {
                    try {
                        fmSwService.checkNoAuthUpdateFmSw(fmSw);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("?????????????????????????????????:{} ", e);
                        throw new BusinessException("?????????????????????????????????", "???????????????????????????");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// ?????????????????????
                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("?????????????????????????????????????????????:{}", e);
                    }
                    return AjaxResult.success("????????????");
                }

            } else { //?????????????????????
                //???????????????????????????
                FmSw fmsw = fmSwService.selectFmSwById(fmSw.getFmSwId());
                String createId = fmsw.getCreateId();
                person = ogPersonService.selectOgPersonById(createId);// ?????????????????????

                Map<String, Object> reMap = new HashMap<>();
                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSw";
                reMap.put("reCode", "2");
                reMap.put("businessKey", businessKey);
                reMap.put("processDefinitionKey", processDefinitionKey);
                reMap.put("createId", createId);
                reMap.put("comment", fmSw.getLogPerformDesc());
                //???????????????
                reMap.put("userId", ShiroUtils.getUserId());
                //????????????????????????????????????????????????
                fmsw = new FmSw();
                fmsw.setFmSwId(fmSw.getFmSwId());
                fmsw.setCurrentState(fmSw.getCurrentState());
                try {
                    fmSwService.checkReturnUpdateFmSw(fmsw);
                    activitiCommService.complete(reMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("?????????????????????????????????:{} ", e);
                    throw new BusinessException("?????????????????????????????????,???????????????????????????");
                }
                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("???????????????????????????????????????:{}", e);
                }
                return AjaxResult.success("????????????");
            }
        } else {
            //??????????????? ??????????????????
            if ("checkPass".equals(fmSw.getLabel())) {
                Map<String, Object> reMap = new HashMap<>();
                reMap.put("userId", ShiroUtils.getUserId());

                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSwBf";
                String personId = "";
                //1.??????????????????????????????3
                if (StringUtils.isNotEmpty(fmSw.getCheckerThreeId())) {
                    //???????????????3??????
                    reMap.put("reCode", "0");
                    reMap.put("businessKey", businessKey);
                    reMap.put("processDefinitionKey", processDefinitionKey);
                    reMap.put("checkerThreeId", fmSw.getCheckerThreeId());
                    reMap.put("comment", fmSw.getLogPerformDesc());
                    personId = fmSw.getCheckerThreeId();
                    //2.??????????????????????????????4
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerFourId())) {
                    //???????????????4??????
                    reMap.put("reCode", "0");
                    reMap.put("businessKey", businessKey);
                    reMap.put("processDefinitionKey", processDefinitionKey);
                    reMap.put("checkerFourId", fmSw.getCheckerFourId());
                    reMap.put("comment", fmSw.getLogPerformDesc());
                    personId = fmSw.getCheckerFourId();
                } else if (StringUtils.isNotEmpty(fmSw.getDealerFiveId())) {
                    reMap.put("reCode", "0");
                    reMap.put("businessKey", businessKey);
                    reMap.put("processDefinitionKey", processDefinitionKey);
                    reMap.put("dealerFiveId", fmSw.getDealerFiveId());
                    reMap.put("comment", fmSw.getLogPerformDesc());
                    personId = fmSw.getDealerId();
                }

                if (StringUtils.isNotEmpty(fmSw.getCheckerTwoId())) {
                    try {
                        fmSwService.checkUpdateFmSw(fmSw);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("????????????????????????2????????????:{} ", e);
                        throw new BusinessException("????????????????????????2????????????", "???????????????????????????");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// ?????????????????????

                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("????????????????????????2????????????????????????:{} ", e);
                    }
                    return AjaxResult.success("????????????");
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerThreeId())) {
                    try {
                        fmSwService.checkUpdateFmSw(fmSw);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("????????????????????????3????????????:{} ", e);
                        throw new BusinessException("????????????????????????3????????????", "???????????????????????????");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// ?????????????????????

                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("????????????????????????3????????????????????????:{} ", e);
                    }
                    return AjaxResult.success("????????????");
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerFourId())) {
                    try {
                        fmSwService.checkUpdateFmSw(fmSw);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("????????????????????????4????????????:{} ", e);
                        throw new BusinessException("????????????????????????4????????????", "???????????????????????????");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// ?????????????????????

                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("????????????????????????4????????????????????????:{} ", e);
                    }
                    return AjaxResult.success("????????????");
                } else {
                    try {
                        fmSwService.checkUpdateFmSw(fmSw);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("????????????????????????5????????????:{} ", e);
                        throw new BusinessException("????????????????????????5????????????", "???????????????????????????");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// ?????????????????????

                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("????????????????????????5????????????????????????:{} ", e);
                    }
                    return AjaxResult.success("????????????");
                }

            } else { //?????????????????????
                //???????????????????????????
                FmSw fmsw = fmSwService.selectFmSwById(fmSw.getFmSwId());
                String createId = fmsw.getCreateId();
                person = ogPersonService.selectOgPersonById(createId);// ?????????????????????

                Map<String, Object> reMap = new HashMap<>();
                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSw";
                reMap.put("reCode", "1");
                reMap.put("businessKey", businessKey);
                reMap.put("processDefinitionKey", processDefinitionKey);
                reMap.put("createId", createId);
                reMap.put("comment", fmSw.getLogPerformDesc());
                //???????????????
                reMap.put("userId", ShiroUtils.getUserId());
                //????????????????????????????????????????????????
                fmsw = new FmSw();
                fmsw.setFmSwId(fmSw.getFmSwId());
                fmsw.setCurrentState(fmSw.getCurrentState());
                fmsw.setCheckerTwoId(fmSw.getCheckerTwoId());
                fmsw.setCheckerThreeId(fmSw.getCheckerThreeId());
                fmsw.setCheckerFourId(fmSw.getCheckerFourId());
                fmsw.setDealerFiveId(fmSw.getDealerFiveId());
                try {
                    fmSwService.checkReturnUpdateFmSw(fmsw);
                    activitiCommService.complete(reMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("?????????????????????????????????:{} ", e);
                    throw new BusinessException("?????????????????????????????????,???????????????????????????");
                }
                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("???????????????????????????????????????:{}", e);
                }
                return AjaxResult.success("????????????");
            }
        }

    }

    /**
     * ???????????????????????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/authlist")
    @ResponseBody
    public TableDataInfo authlist(FmSw fmSw) {

        List<Map<String, Object>> reList = activitiCommService.userTask("FmSw", null);
        List<OgGroup> userGroupList = sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String, Object>> regList = new ArrayList<>();
        if (userGroupList.size() > 0) {
            regList = activitiCommService.groupTasks("FmSw", "auther");
            reList.addAll(regList);
        }
        List<String> fmSwIds = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            if ("auther".equals(map.get("description"))) {
                String business_key = map.get("businesskey").toString();
                if (StringUtils.isNotEmpty(business_key)) {
                    fmSwIds.add(business_key);
                }
            }

        }
        List<FmSw> resultlist = null;
        if (fmSwIds.size() > 0) {
            fmSw.setIds(fmSwIds);
            startPage();
            resultlist = fmSwService.selectFmSwListByTask(fmSw);
            return getDataTable(resultlist);
        }
        return getDataTable(new ArrayList<>());
    }

    /**
     * ?????????????????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/authPass")
    @ResponseBody
    public AjaxResult authPassOrReturn(FmSw fmSw) {
        FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
        String smsText = "?????????????????????????????????????????????" + fm.getFaultNo();//????????????
        String personId = "";
        //??????????????????????????????????????????????????????
        //1.?????????????????????????????????
        Map<String, Object> reMap = new HashMap<>();
        String businessKey = fmSw.getFmSwId();
        String processDefinitionKey = "FmSw";
        reMap.put("userId", ShiroUtils.getUserId());

        if ("04".equals(fmSw.getCurrentState())) {
            if (StringUtils.isNotEmpty(fmSw.getDealerId())) {
                reMap.put("reCode", "0");
                reMap.put("businessKey", businessKey);
                reMap.put("processDefinitionKey", processDefinitionKey);
                reMap.put("dealId", fmSw.getDealerId());
                reMap.put("comment", fmSw.getLogPerformDesc());
                personId = fmSw.getDealerId();
            }
        } else {
            //2.??????????????????????????????
            reMap.put("reCode", "2");
            reMap.put("businessKey", businessKey);
            reMap.put("processDefinitionKey", processDefinitionKey);
            reMap.put("createId", fmSw.getCreateId());
            reMap.put("comment", fmSw.getLogPerformDesc());
            personId = fmSw.getCheckedId();
            fmSw.setDealerId("");
        }

        try {
            fmSwService.checkReturnUpdateFmSw(fmSw);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("?????????????????????????????????:{} ", e);
            throw new BusinessException("?????????????????????????????????,???????????????????????????");
        }
        OgPerson person = ogPersonService.selectOgPersonById(personId);// ?????????????????????

        try {
            sendSms(smsText, person);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("?????????????????????????????????????????????: {}", e);
        }
        return AjaxResult.success("????????????");
    }

    /**
     * ?????????????????????id????????????????????????????????????
     *
     * @param id
     * @return
     */
    @PostMapping("/cmBizSingleList/{id}")
    @ResponseBody
    public TableDataInfo getCmBizSingleList(@PathVariable("id") String id) {
        PubRelation pr = new PubRelation();
        List<CmBizSingle> cmBizSingles = new ArrayList<>();
        pr.setObj1Id(id);
        pr.setType("07");
        List<PubRelation> list = iPubRelationService.selectPubRelationList(pr);
        if (!list.isEmpty()) {
            for (PubRelation prtion : list) {
                CmBizSingle cmBizSingle = iCmBizSingleService.selectCmBizSingleById(prtion.getObj2Id());
                if (StringUtils.isNotNull(cmBizSingle)) {
                    cmBizSingles.add(cmBizSingle);
                }
            }
        }
        return getDataTable(cmBizSingles);
    }

    /**
     * ??????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/fmListByStatus")
    @ResponseBody
    public TableDataInfo fmListByStatus(FmSw fmSw) {
        startPage();
        List<FmSw> fmListByStatus = fmSwService.fmListByStatus(fmSw);
        return getDataTable(fmListByStatus);
    }

    /**
     * ????????????
     *
     * @param setSmsText ????????????
     * @param person     ???????????????
     */
    public void sendSms(String setSmsText, OgPerson person) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// ?????????
        p.setName(person.getpName());// ??????
        p.setSmsType("4");// ???????????????3???4????????????,2????????????
        p.setSmsText(setSmsText);// ????????????
        p.setInspectTime(DateUtils.dateTimeNow());// ????????????
        p.setInspectObject("050100");// ????????????
        p.setCreaterId(ShiroUtils.getUserId());// ?????????
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//????????????
    }

    /**
     * ??????
     *
     * @return
     */
    @Log(title = "????????????", businessType = BusinessType.INSERT)
    @PostMapping("/fileSave")
    @ResponseBody
    public AjaxResult fileSave(FmSw fmSw) {
        try {
            if (StringUtils.isEmpty(fmSw.getFmSwId())) {
                fmSw.setCurrentState("01");
                fmSw.setCreateId(ShiroUtils.getUserId());
                fmSw.setCreateTime(DateUtils.dateTimeNow());
                fmSw.setFmSwId(UUID.getUUIDStr());
                fmSw.setInvalidationMark("1");
                fmSwService.insertFmSw(fmSw);
                return AjaxResult.success("????????????", fmSw.getFmSwId());
            } else {
                fmSwService.updateFmSw(fmSw);
                return AjaxResult.success("????????????", fmSw.getFmSwId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("???????????????????????????: {}", e);
            throw new BusinessException("???????????????????????????" + fmSw.getFaultNo());
        }

    }

    /**
     * ???????????????
     *
     * @param deptId    ??????ID
     * @param excludeId ??????ID
     */
    @GetMapping(value = {"/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}"})
    public String selectDeptTree(@PathVariable("deptId") String deptId,
        @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix + "/tree";
    }

    /**
     * ?????????????????????
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> collect = null;
        List<Ztree> ztreesOne = null;
        List<Ztree> ztreesTwo = null;
        //?????????????????????ID
        String pId = ShiroUtils.getUserId();
        //??????????????????
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        //????????????ID
        String orgId = ogPerson.getOrgId();
        //??????????????????????????????
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //????????????????????????????????????????????????????????????
        OgOrg org = getOneLvOrTwoLv(ogOrg);
        if ("1".equals(org.getOrgLv()) || ("2".equals(org.getOrgLv()) && "/000000000/010000888/".equals(
            org.getLevelCode()))) {
            OgOrg orgOne = new OgOrg();
            orgOne.setLevelCode(org.getLevelCode());
            collect = deptService.selectDeptTree(orgOne);
        } else {
            //???????????????????????????????????????????????????
            String orgLevelCode = "/000000000/010000888/";
            OgOrg orgTwo = new OgOrg();
            orgTwo.setLevelCode(orgLevelCode);
            collect = deptService.selectDeptTree(orgTwo);
            OgOrg orgThree = new OgOrg();
            orgThree.setLevelCode(org.getLevelCode());
            ztreesTwo = deptService.selectDeptTree(orgThree);
            collect.addAll(ztreesTwo);
        }

        return collect;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param ogOrg
     * @return
     */
    private OgOrg getOneLvOrTwoLv(OgOrg ogOrg) {
        //1.????????????????????????????????????????????????????????????
        if ("1".equals(ogOrg.getOrgLv()) || "2".equals(ogOrg.getOrgLv())) {
            return ogOrg;
        } else {
            String levelCode = ogOrg.getLevelCode();
            String[] split = levelCode.split("/");
            String twoLevelCode = split[2];
            return deptService.selectDeptByCode(twoLevelCode);
        }

    }

    /**
     * ??????????????????????????????
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
     * ?????????2??????
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/checkEditTwo/{id}")
    public String checkEditTwo(@PathVariable("id") String id, ModelMap modelMap) {
        Map<String, Object> map = getList(id);
        modelMap.put("fmSwMbList", map.get("fmSwMbList"));
        modelMap.put("authorList", map.get("authorList"));
        modelMap.put("dealList", map.get("dealList"));
        modelMap.put("ogOrg", map.get("ogOrg"));
        modelMap.put("checkList", map.get("checkList"));
        modelMap.put("fmSw", map.get("fmSw"));
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_check_two_option";
    }

    /**
     * ?????????3??????
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/checkEditThree/{id}")
    public String checkEditThree(@PathVariable("id") String id, ModelMap modelMap) {
        Map<String, Object> map = getList(id);
        modelMap.put("fmSwMbList", map.get("fmSwMbList"));
        modelMap.put("authorList", map.get("authorList"));
        modelMap.put("dealList", map.get("dealList"));
        modelMap.put("ogOrg", map.get("ogOrg"));
        modelMap.put("checkList", map.get("checkList"));
        modelMap.put("fmSw", map.get("fmSw"));
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_check_three_option";
    }

    /**
     * ?????????4??????
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/checkEditFour/{id}")
    public String checkEditFour(@PathVariable("id") String id, ModelMap modelMap) {
        Map<String, Object> map = getList(id);
        modelMap.put("fmSwMbList", map.get("fmSwMbList"));
        modelMap.put("authorList", map.get("authorList"));
        modelMap.put("dealList", map.get("dealList"));
        modelMap.put("ogOrg", map.get("ogOrg"));
        modelMap.put("checkList", map.get("checkList"));
        modelMap.put("fmSw", map.get("fmSw"));
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_check_four_option";
    }

    /**
     * ???????????????
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/dealPersonEdit/{id}")
    public String dealPersonEdit(@PathVariable("id") String id, ModelMap modelMap) {
        Map<String, Object> map = getList(id);
        modelMap.put("fmSwMbList", map.get("fmSwMbList"));
        modelMap.put("authorList", map.get("authorList"));
        modelMap.put("dealList", map.get("dealList"));
        modelMap.put("ogOrg", map.get("ogOrg"));
        modelMap.put("checkList", map.get("checkList"));
        modelMap.put("fmSw", map.get("fmSw"));
        return prefix + "/sw_deal_person_option";
    }

    /**
     * ?????????????????????
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/applicantPersonEdit/{id}")
    public String applicantPersonEdit(@PathVariable("id") String id, ModelMap modelMap) {
        Map<String, Object> map = getList(id);
        modelMap.put("fmSwMbList", map.get("fmSwMbList"));
        modelMap.put("authorList", map.get("authorList"));
        modelMap.put("dealList", map.get("dealList"));
        modelMap.put("ogOrg", map.get("ogOrg"));
        modelMap.put("checkList", map.get("checkList"));
        modelMap.put("fmSw", map.get("fmSw"));
        return prefix + "/sw_applicant_person_option";
    }

    /**
     * ?????????????????????????????????
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/application")
    public String smApplication(ModelMap modelMap) {
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_application";
    }

    /**
     * ???????????????????????????????????????
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/applicationlist")
    @ResponseBody
    public TableDataInfo applicationlist(FmSw fmSw) {

        List<Map<String, Object>> reList = activitiCommService.userTask("FmSwBf", null);
        List<OgGroup> userGroupList = sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String, Object>> regList = new ArrayList<>();
        if (userGroupList.size() > 0) {
            regList = activitiCommService.groupTasks("FmSwBf", "create");
            reList.addAll(regList);
        }
        List<String> fmSwIds = new ArrayList<>();
        for (Map<String, Object> map : reList) {
            if ("create".equals(map.get("description"))) {
                Object businesskey = map.get("businesskey");
                if (StringUtils.isNotNull(businesskey)) {
                    fmSwIds.add(businesskey.toString());
                }
            }

        }
        List<FmSw> resultlist = null;
        if (fmSwIds.size() > 0) {
            fmSw.setIds(fmSwIds);
            startPage();
            resultlist = fmSwService.selectFmSwListByTask(fmSw);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());
    }

    /**
     * ?????????????????????
     *
     * @param id
     * @param modelMap
     * @return
     */
    @GetMapping("/applicationEdit/{id}")
    public String applicationEdit(@PathVariable("id") String id, ModelMap modelMap) {
        Map<String, Object> map = getList(id);
        modelMap.put("fmSwMbList", map.get("fmSwMbList"));
        modelMap.put("authorList", map.get("authorList"));
        modelMap.put("dealList", map.get("dealList"));
        modelMap.put("ogOrg", map.get("ogOrg"));
        modelMap.put("checkList", map.get("checkList"));
        modelMap.put("fmSw", map.get("fmSw"));
        //?????????????????????????????????
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_applicant_person_option";
    }

    private String getConnectRole() {
        StringBuffer sb = new StringBuffer();
        String authRole = FmSwConstants.authRole;
        String dealRole = FmSwConstants.dealRole;
        sb.append(authRole);
        sb.append(",");
        sb.append(dealRole);
        String rId = sb.toString();
        return rId;
    }
}



