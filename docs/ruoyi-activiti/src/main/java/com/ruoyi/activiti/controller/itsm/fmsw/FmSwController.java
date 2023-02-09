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
 * 事务事件单Controller
 *
 * @author luyankun
 * @date 2020-12-16
 */
@Controller
@RequestMapping("/trans/sw")
@Transactional(rollbackFor = Exception.class)
public class FmSwController extends BaseController {

    //日志记录
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
     * 我的事务事件单页面
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
     * 我的事务事件单列表数据
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
            if ("1".equals(myFlag)) {//我的标识
                fmSw.setCreateId(u.getpId());//默认我创建的 创建人是当前登陆人
            }
            //我处理的
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
                        if (!"启动流程".equals(taskName) && !"修改".equals(taskName) && !"撤销流程".equals(taskName)) {
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
     * 查询事务事件单菜单根据当前用户登陆的机构信息
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
        //获取当前登陆用户的机构信息
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
     * 导出事务事件单列表
     *
     * @param fmSw
     * @return
     */
    @Log(title = "事务事件单", businessType = BusinessType.EXPORT)
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
        fmSw.setCreateId(u.getpId());//默认我创建的 创建人是当前登陆人
        if (!StringUtils.isEmpty(fmSw.getLabel())) {
            String myFlag = fmSw.getLabel();
            //我处理的
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
                        if (!"启动流程".equals(taskName) && !"修改".equals(taskName) && !"撤销流程".equals(taskName)) {
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
                    return util.exportExcel(listFmSw, "事务事件单");
                } else {
                    PageDomain pageDomain = TableSupport.buildPageRequest();
                    String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
                    PageHelper.orderBy(orderBy);
                    ExcelUtil<FmSw> util = new ExcelUtil<FmSw>(FmSw.class);
                    return util.exportExcel(listFmSw, "事务事件单");
                }
            }
        }
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<FmSw> list = fmSwService.selectFmSwList(fmSw);
            ExcelUtil<FmSw> util = new ExcelUtil<FmSw>(FmSw.class);
            return util.exportExcel(list, "事务事件单");
        } else {
            PageDomain pageDomain = TableSupport.buildPageRequest();
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
            List<FmSw> list = fmSwService.selectFmSwList(fmSw);
            ExcelUtil<FmSw> util = new ExcelUtil<FmSw>(FmSw.class);
            return util.exportExcel(list, "事务事件单");
        }

    }

    /**
     * 事务事件单单新增页面
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
        //查询当前用户所在的机构信息
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
     * 新增保存事务事件单 如果为暂存状态
     *
     * @param fmSw
     * @return
     */
    @Log(title = "事务事件单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FmSw fmSw) {

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());

        fmSw.setCreateId(ShiroUtils.getUserId());
        fmSw.setCreateTime(DateUtils.dateTimeNow());
        String processStatus = "1";
        //获取当前请求事项的id
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
            //判断当前是否为暂存还是提交状态
            if ("commit".equals(fmSw.getLabel())) {
                if (StringUtils.isNotEmpty(fmSw.getCheckedId())) {//待审核
                    reMap.put("reCode", "0");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkId", fmSw.getCheckedId());
                    personId = fmSw.getCheckedId();
                } else {
                    //判断是否进行了授权人的选择
                    if (StringUtils.isNotEmpty(fmSw.getAuthId())) {//待授权
                        reMap.put("reCode", "1");
                        reMap.put("createId", ShiroUtils.getUserId());
                        reMap.put("autherId", fmSw.getAuthId());
                        personId = fmSw.getAuthId();
                    } else {
                        //审核人 授权人都没有选
                        if (StringUtils.isNotEmpty(fmSw.getDealerId())) {//待处理
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
                    logger.error("事务事件单新增失败: {} ", e);
                    throw new BusinessException("事务事件单新增失败,请刷新页面进行重试");
                }
                FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
                String smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
                OgPerson person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人

                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("事务事件单新增短信失败:{} ", e);
                }
                return AjaxResult.success("操作成功");
            }
        } else {
            fmSw.setDealerId("");
            fmSw.setAuthId("");
            fmSw.setCheckedId("");
            //判断当前是否为暂存还是提交状态
            if ("commit".equals(fmSw.getLabel())) {
                if (StringUtils.isNotEmpty(fmSw.getCheckerTwoId())) {//待审核2
                    reMap.put("reCode", "0");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerTwoId", fmSw.getCheckerTwoId());
                    personId = fmSw.getCheckerTwoId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerThreeId())) {//待审核3
                    reMap.put("reCode", "1");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerThreeId", fmSw.getCheckerThreeId());
                    personId = fmSw.getCheckerThreeId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerFourId())) {//待审核4{
                    reMap.put("reCode", "2");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerFourId", fmSw.getCheckerFourId());
                    personId = fmSw.getCheckerFourId();
                } else if (StringUtils.isNotEmpty(fmSw.getDealerFiveId())) {
                    //审核人2 审核人3 审核人4都没有选
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
                    logger.error("事务事件单新增失败: {} ", e);
                    throw new BusinessException("事务事件单新增失败,请刷新页面进行重试");
                }
                FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
                String smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
                OgPerson person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人

                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("事务事件单新增短信失败:{} ", e);
                }
                return AjaxResult.success("操作成功");
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
            log.error("事务事件单暂存失败: {} ", e);
            return AjaxResult.error("事务事件单暂存失败");
        }
        return AjaxResult.success("事务事件单暂存成功");
    }

    /**
     * 待提交修改事务事件单页面
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
        //获取当前处室的对应的请求事项
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
                //获取当前请求事项对应的模板Id
                FmSwMb fm_sw_mb = fmSwMbService.selectFmSwMbByDealOrgIdAndFaultKind(fmSwMb);
                if (StringUtils.isNotNull(fm_sw_mb)) {
                    fm_sw.setFaultKind(fm_sw_mb.getSwmdId());
                }
            }

        }
        if (fm_sw.getProcessStatus().equals("1")) {
            if (StringUtils.isNotEmpty(fm_sw.getCreateOrgId()) && StringUtils.isNotNull(fm_sw.getCreateOrgId())) {
                //获取申请处室的审核人
                OgOrg ogOrg = deptService.selectDeptById(fm_sw.getCreateOrgId());
                mmap.put("ogOrg", ogOrg);
                String levelCode = ogOrg.getLevelCode();
                //审核人
                List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, FmSwConstants.checkRole);
                mmap.put("checkList", checkList);
            }

            if (StringUtils.isNotEmpty(fm_sw.getDealOrgId()) && StringUtils.isNotNull(fm_sw.getDealOrgId())) {

                String rId = getConnectRole();
                //获取授权人
                List<Object> objects = levelcodeAndMbInfo(fm_sw.getDealOrgId(), rId);
                mmap.put("authorList", objects.get(0));
                //获取处理人
                mmap.put("dealList", objects.get(1));
            }
        } else {
            if (StringUtils.isNotEmpty(fm_sw.getCreateOrgId()) && StringUtils.isNotNull(fm_sw.getCreateOrgId())) {
                //获取申请处室的审核人
                OgOrg ogOrg = deptService.selectDeptById(fm_sw.getCreateOrgId());
                mmap.put("ogOrg", ogOrg);
                String levelCode = ogOrg.getLevelCode();
                //审核人
                List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, FmSwConstants.checkRole);
                mmap.put("checkList", checkList);
            }

            if (StringUtils.isNotEmpty(fm_sw.getDealOrgId()) && StringUtils.isNotNull(fm_sw.getDealOrgId())) {

                String rId = getConnectRole();
                //获取授权人
                List<Object> objects = levelcodeAndMbInfo(fm_sw.getDealOrgId(), rId);
                mmap.put("authorList", objects.get(0));
            }
            if (StringUtils.isNotEmpty(fm_sw.getDealOrgId()) && StringUtils.isNotNull(fm_sw.getDealOrgId())) {

                String rId = getConnectRole();
                //获取授权人
                List<Object> objects = levelcodeAndMbInfo(fm_sw.getDealOrgId(), rId);
                //获取处理人
                mmap.put("dealList", objects.get(1));
            }
        }

        mmap.put("fmSw", fm_sw);
        mmap.put("pId", ShiroUtils.getUserId());

        return prefix + "/edit";
    }

    /**
     * 退回待修改修改事务事件单页面
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
        //获取当前处室的对应的请求事项
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
                //获取当前请求事项对应的模板Id
                FmSwMb fm_sw_mb = fmSwMbService.selectFmSwMbByDealOrgIdAndFaultKind(fmSwMb);
                if (StringUtils.isNotNull(fm_sw_mb)) {
                    fm_sw.setFaultKind(fm_sw_mb.getSwmdId());
                }

            }

        }

        if (StringUtils.isNotEmpty(fm_sw.getCreateOrgId()) && StringUtils.isNotNull(fm_sw.getCreateOrgId())) {
            String pId = ShiroUtils.getUserId();
            //获取申请处室的审核人
            OgOrg ogOrg = deptService.selectDeptById(fm_sw.getCreateOrgId());
            mmap.put("ogOrg", ogOrg);
            String levelCode = ogOrg.getLevelCode();
            //审核人
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, FmSwConstants.checkRole);
            mmap.put("checkList", checkList);
        }

        if (StringUtils.isNotEmpty(fm_sw.getDealOrgId()) && StringUtils.isNotNull(fm_sw.getDealOrgId())) {
            String rId = getConnectRole();
            //获取授权人
            List<Object> objects = levelcodeAndMbInfo(fm_sw.getDealOrgId(), rId);
            mmap.put("authorList", objects.get(0));
            //获取处理人
            mmap.put("dealList", objects.get(1));
        }

        //进行日期回显
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
     * 修改提交/暂存事务事件单
     *
     * @param fmSw
     * @return
     */
    @Log(title = "事务事件单", businessType = BusinessType.UPDATE)
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
            //判断当前是否为暂存还是提交状态
            if ("commit".equals(fmSw.getLabel())) {
                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSw";
                //提交之后的任务
                if (StringUtils.isNotEmpty(fmSw.getCheckedId())) {
                    reMap.put("reCode", "0");
                    reMap.put("checkId", fmSw.getCheckedId());
                    personId = fmSw.getCheckedId();
                } else {
                    //判断是否进行了授权人的选择
                    if (StringUtils.isNotEmpty(fmSw.getAuthId())) {//待授权
                        reMap.put("reCode", "1");
                        reMap.put("createId", ShiroUtils.getUserId());
                        reMap.put("autherId", fmSw.getAuthId());
                        personId = fmSw.getAuthId();
                    } else {
                        //审核人 授权人都没有选
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
                    logger.error("事务事件单修改保存失败: {} ", e);
                    throw new BusinessException("事务事件单修改保存失败,请刷新页面进行重试");
                }

                String smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
                OgPerson person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人
                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("事务事件单修改发送短信失败:{} ", e);
                }
                return AjaxResult.success("操作成功");
            }
        } else {
            fmSw.setDealerId("");
            fmSw.setAuthId("");
            fmSw.setCheckedId("");
            //判断当前是否为暂存还是提交状态
            if ("commit".equals(fmSw.getLabel())) {
                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSwBf";
                if (StringUtils.isNotEmpty(fmSw.getCheckerTwoId())) {//待审核2
                    reMap.put("reCode", "0");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerTwoId", fmSw.getCheckerTwoId());
                    personId = fmSw.getCheckerTwoId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerThreeId())) {//待审核3
                    reMap.put("reCode", "1");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerThreeId", fmSw.getCheckerThreeId());
                    personId = fmSw.getCheckerThreeId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerFourId())) {//待审核4{
                    reMap.put("reCode", "2");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerFourId", fmSw.getCheckerFourId());
                    personId = fmSw.getCheckerFourId();
                } else if (StringUtils.isNotEmpty(fmSw.getDealerFiveId())) {
                    //审核人2 审核人3 审核人4都没有选
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
                    logger.error("事务事件单修改保存失败: {} ", e);
                    throw new BusinessException("事务事件单修改保存失败,请刷新页面进行重试");
                }

                String smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
                OgPerson person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人
                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("事务事件单修改发送短信失败:{} ", e);
                }
                return AjaxResult.success("操作成功");
            }
        }

        try {
            fmSwService.updateFmSw(fmSw);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("事务事件单修改暂存失败: {} ", e);
            return AjaxResult.error("事务事件单修改暂存失败");
        }
        return AjaxResult.success("事务事件单修改暂存成功");
    }

    /**
     * 退回待修改的事务事件单 修改提交/暂存事务事件单
     *
     * @param fmSw
     * @return
     */
    @Log(title = "事务事件单", businessType = BusinessType.UPDATE)
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
            //判断当前是否为暂存还是提交状态
            if ("recommit".equals(fmSw.getLabel())) {
                //提交之后的任务
                String businessKey = fmSw.getFmSwId();

                String processDefinitionKey = "FmSw";
                if (StringUtils.isNotEmpty(fmSw.getCheckedId())) {//待审核
                    reMap.put("businessKey", businessKey);
                    reMap.put("processDefinitionKey", processDefinitionKey);
                    reMap.put("reCode", "0");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkId", fmSw.getCheckedId());
                    personId = fmSw.getCheckedId();
                } else {
                    //判断是否进行了授权人的选择
                    if (StringUtils.isNotEmpty(fmSw.getAuthId())) {//待授权
                        reMap.put("businessKey", businessKey);
                        reMap.put("processDefinitionKey", processDefinitionKey);
                        reMap.put("reCode", "1");
                        reMap.put("createId", ShiroUtils.getUserId());
                        reMap.put("autherId", fmSw.getAuthId());
                        personId = fmSw.getAuthId();
                    } else {
                        //审核人 授权人都没有选
                        if (StringUtils.isNotEmpty(fmSw.getDealerId())) {//待处理
                            reMap.put("businessKey", businessKey);
                            reMap.put("processDefinitionKey", processDefinitionKey);
                            reMap.put("reCode", "2");
                            reMap.put("createId", ShiroUtils.getUserId());
                            reMap.put("dealId", fmSw.getDealerId());
                            personId = fmSw.getDealerId();
                        }
                    }

                }
                reMap.put("comment", "提交");
                activitiCommService.complete(reMap);
                //发送短信

                String smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
                OgPerson person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人
                sendSms(smsText, person);
                return toAjax(fmSwService.returnEditUpdateFmSw(fmSw));
            } else {
                //作废状态
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
            //判断当前是否为暂存还是提交状态
            if ("recommit".equals(fmSw.getLabel())) {
                //提交之后的任务
                String businessKey = fmSw.getFmSwId();

                String processDefinitionKey = "FmSwBf";
                if (StringUtils.isNotEmpty(fmSw.getCheckerTwoId())) {//待审核2
                    reMap.put("reCode", "0");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerTwoId", fmSw.getCheckerTwoId());
                    personId = fmSw.getCheckerTwoId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerThreeId())) {//待审核3
                    reMap.put("reCode", "1");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerThreeId", fmSw.getCheckerThreeId());
                    personId = fmSw.getCheckerThreeId();
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerFourId())) {//待审核4{
                    reMap.put("reCode", "2");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("checkerFourId", fmSw.getCheckerFourId());
                    personId = fmSw.getCheckerFourId();
                } else if (StringUtils.isNotEmpty(fmSw.getDealerFiveId())) {
                    //审核人2 审核人3 审核人4都没有选
                    reMap.put("reCode", "3");
                    reMap.put("createId", ShiroUtils.getUserId());
                    reMap.put("dealerFiveId", fmSw.getDealerFiveId());
                    reMap.put("userId", ShiroUtils.getUserId());
                    personId = fmSw.getDealerFiveId();
                }

                reMap.put("processDefinitionKey", processDefinitionKey);
                reMap.put("businessKey", businessKey);
                reMap.put("comment", "提交");
                activitiCommService.complete(reMap);
                //发送短信

                String smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
                OgPerson person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人
                sendSms(smsText, person);
                return toAjax(fmSwService.returnEditUpdateFmSw(fmSw));
            } else {
                //作废状态
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
     * 删除事务事件单
     *
     * @param ids
     * @return
     */
    @Log(title = "事务事件单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(fmSwService.deleteFmSwByIds(ids));
    }

    /**
     * 根据id查询事务事件单
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
     * 根据机构获取对应的模板信息
     *
     * @param dealOrgId
     * @param rId
     * @return
     */
    private List<Object> levelcodeAndMbInfo(String dealOrgId, String rId) {
        //获取当前节点对象的信息
        OgOrg ogOrg = deptService.selectDeptById(dealOrgId);
        String levelCode = ogOrg.getLevelCode();
        String[] split = rId.split(",");

        OgOrg oneLvOrTwoLv = getOneLvOrTwoLv(ogOrg);
        //授权人
        List<OgPerson> authorList = ogPersonService.selectListByLevelCode(oneLvOrTwoLv.getLevelCode(), split[0]);
        //处理人
        List<OgPerson> dealList = ogPersonService.selectListByLevelCode(levelCode, split[1]);
        //模板列表
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
     * 事务事件单详情页面
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
     * 事务事件单详情没有操作的页面
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
     * 审核事务单列表页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/check")
    public String smCheck(ModelMap modelMap) {
        //获取当前用户的人员信息
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_check";
    }

    /**
     * 审核页面
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
        //获取当前用户的人员信息
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
        //查询当前Id对象的事务单信息
        FmSw fmSw = new FmSw();
        fmSw.setFmSwId(id);
        List<FmSw> fmSws = fmSwService.selectFmSwList(fmSw);
        if (!fmSws.isEmpty() && fmSws.size() > 0) {
            fmSw = fmSws.get(0);

        }

        //获取当前处室的对应的请求事项
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
                //获取当前请求事项对应的模板Id
                FmSwMb fm_sw_mb = fmSwMbService.selectFmSwMbByDealOrgIdAndFaultKind(fmSwMb);
                if (StringUtils.isNotNull(fm_sw_mb)) {
                    fmSw.setFaultKind(fm_sw_mb.getSwmdId());
                }
            }

        }

        if (StringUtils.isNotEmpty(fmSw.getDealOrgId())) {
            String rId = getConnectRole();
            //获取授权人
            List<Object> objects = levelcodeAndMbInfo(fmSw.getDealOrgId(), rId);
            map.put("authorList", objects.get(0));
            //获取处理人
            map.put("dealList", objects.get(1));
        }

        String auditTime = fmSw.getAuditTime();
        if (StringUtils.isNotEmpty(auditTime)) {
            fmSw.setAuditTime(DateUtils.formatDateStr(auditTime, DateUtils.YYYYMMDDHHMMSS, DateUtils.YYYY_MM_DD_HH_MM));
        }

        if (!StringUtils.isEmpty(fmSw.getCreateOrgId())) {
            //获取申请处室的审核人
            OgOrg ogOrg = deptService.selectDeptById(fmSw.getCreateOrgId());
            map.put("ogOrg", ogOrg);
            String levelCode = ogOrg.getLevelCode();
            //审核人
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
     * 审核通过不通过
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/auditPass")
    @ResponseBody
    public AjaxResult auditPass(FmSw fmSw) {
        //获取审计人
        String pId = ShiroUtils.getUserId();
        fmSw.setAuditorId(pId);
        //获取审计时间
        String auditTime = DateUtils.dateTimeNow();
        fmSw.setAuditTime(auditTime);
        return toAjax(fmSwService.auditUpdateFmSw(fmSw));
    }

    /**
     * 授权数据列表页面
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/author")
    public String smAuthor(ModelMap modelMap) {
        //获取当前用户的人员信息
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_author";
    }

    /**
     * 授权页面
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
     * 处理数据列表页面
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/deal")
    public String smDeal(ModelMap modelMap) {
        //获取当前用户的人员信息
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_deal";
    }

    /**
     * 处理页面
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
     * 已关闭数据列表页面
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/closed")
    public String smClosed(ModelMap modelMap) {
        //获取当前用户的人员信息
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_closed";
    }

    /**
     * 审计页面
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
     * 事务事件单查询数据列表页面
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/query")
    public String smQuery(ModelMap modelMap) {
        //获取当前用户的人员信息
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_query";
    }

    /**
     * 事务事件单查询数据列表页面
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/query_sw")
    public String querySm(ModelMap modelMap) {
        //获取当前用户的人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getUserId());
        OgOrg ogOrg = deptService.selectDeptById(ogPerson.getOrgId());
        modelMap.put("ogOrg", ogOrg);
        //根据当前用户获取对应的角色信息
        List<OgRole> ogRoles = sysRoleService.selectRolesByUserId(ShiroUtils.getUserId());
        String isHasRole = "false";
        for (OgRole role : ogRoles) {
            if (FmSwConstants.sourceRole.equals(role.getRid())) {
                isHasRole = "true";
            }
        }
        modelMap.put("isHasRole", isHasRole);
        //获取机构信息
        return prefix + "/query_sw";
    }

    /**
     * 查询当前用户的待处理的任务
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
     * 当前待处理任务处理通过
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
        //添加参与人
        reMap.put("userId", ShiroUtils.getUserId());

        if ("05".equals(fmSw.getCurrentState())) {
            //处理完成
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
                log.error("事务事件单处理操作失败: {}", e);
                throw new BusinessException("事务事件单处理操作失败,请刷新页面进行重试");
            }
            smsText = "事务事件单号：" + fm.getFaultNo() + "已经处理完成，请登录运维管理平台查看。";

            try {
                sendSms(smsText, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("事务事件单处理操作短信发送失败: {}", e);
            }

            return AjaxResult.success("操作成功");
        } else if ("08".equals(fmSw.getCurrentState())) {
            //退回待修改
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
                log.error("事务事件单处理退回失败: {} ", e);
                throw new BusinessException("事务事件单处理退回失败,请刷新页面进行重试");
            }
            smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
            try {
                sendSms(smsText, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("事务事件单处理退回短信发送失败: {}", e);
            }
            return AjaxResult.success("操作成功");
        } else {
            //申请授权
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
                log.error("事务事件单处理授权失败: {}", e);
                throw new BusinessException("事务事件单处理授权失败,请刷新页面进行重试");
            }
            smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
            ogPerson = ogPersonService.selectOgPersonById(fmSw.getAuthId());

            try {
                sendSms(smsText, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("事务事件单处理授权短信发送失败: {} ", e);
            }
            return toAjax(fmSwService.dealUpdateFmSw(fmSw));
        }

    }

    /**
     * 当前待处理任务处理通过
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
        //添加参与人
        reMap.put("userId", ShiroUtils.getUserId());

        if ("15".equals(fmSw.getCurrentState())) {
            //处理完成
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
                log.error("事务事件单处理人5操作失败: {}", e);
                throw new BusinessException("事务事件单处理人5操作失败,请刷新页面进行重试");
            }
            smsText = "事务事件单号：" + fm.getFaultNo() + "已经处理人5完成，请登录运维管理平台查看。";

            try {
                sendSms(smsText, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("事务事件单处理人5操作短信发送失败: {}", e);
            }

            return AjaxResult.success("操作成功");
        } else if ("08".equals(fmSw.getCurrentState())) {
            //退回待修改
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
                log.error("事务事件单处理人5退回失败: {} ", e);
                throw new BusinessException("事务事件单处理人5退回失败,请刷新页面进行重试");
            }
            smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
            try {
                sendSms(smsText, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("事务事件单处理人5退回短信发送失败: {}", e);
            }
            return AjaxResult.success("操作成功");
        } else {
            return AjaxResult.success("操作成功");
        }

    }

    /**
     * 查询当前用户的待审核的任务
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
     * 当前待处理任务处理通过
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/checkPass")
    @ResponseBody
    public AjaxResult checkPassOrReturn(FmSw fmSw) {
        FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
        String smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
        OgPerson person;
        if (fm.getProcessStatus().equals("1")) {
            //判断当前是 通过还是退回
            if ("checkPass".equals(fmSw.getLabel())) {
                Map<String, Object> reMap = new HashMap<>();
                reMap.put("userId", ShiroUtils.getUserId());

                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSw";
                String personId = "";
                //1.当前审核选择了授权人
                if (StringUtils.isNotEmpty(fmSw.getAuthId())) {
                    //进入授权环节
                    reMap.put("reCode", "0");
                    reMap.put("businessKey", businessKey);
                    reMap.put("processDefinitionKey", processDefinitionKey);
                    reMap.put("autherId", fmSw.getAuthId());
                    reMap.put("comment", fmSw.getLogPerformDesc());
                    personId = fmSw.getAuthId();
                } else {
                    //2.当前审核没有选择授权人选择了处理人
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
                        log.error("事务事件单授权操作失败:{} ", e);
                        throw new BusinessException("事务事件单审核操作失败", "请刷新页面进行重试");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人

                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("事务事件单审核短信发送发送失败:{} ", e);
                    }
                    return AjaxResult.success("操作成功");
                } else {
                    try {
                        fmSwService.checkNoAuthUpdateFmSw(fmSw);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("事务事件单审核操作失败:{} ", e);
                        throw new BusinessException("事务事件单审核操作失败", "请刷新页面进行重试");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人
                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("事务事件单审核短信发送发送失败:{}", e);
                    }
                    return AjaxResult.success("操作成功");
                }

            } else { //审核退回待修改
                //获取事件单的创建人
                FmSw fmsw = fmSwService.selectFmSwById(fmSw.getFmSwId());
                String createId = fmsw.getCreateId();
                person = ogPersonService.selectOgPersonById(createId);// 获取短信接收人

                Map<String, Object> reMap = new HashMap<>();
                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSw";
                reMap.put("reCode", "2");
                reMap.put("businessKey", businessKey);
                reMap.put("processDefinitionKey", processDefinitionKey);
                reMap.put("createId", createId);
                reMap.put("comment", fmSw.getLogPerformDesc());
                //添加参与人
                reMap.put("userId", ShiroUtils.getUserId());
                //创建新对象只更新状态值和退回说明
                fmsw = new FmSw();
                fmsw.setFmSwId(fmSw.getFmSwId());
                fmsw.setCurrentState(fmSw.getCurrentState());
                try {
                    fmSwService.checkReturnUpdateFmSw(fmsw);
                    activitiCommService.complete(reMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("事务事件单审核退回失败:{} ", e);
                    throw new BusinessException("事务事件单审核退回失败,请刷新页面进行重试");
                }
                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("事务事件单退回短信发送失败:{}", e);
                }
                return AjaxResult.success("操作成功");
            }
        } else {
            //判断当前是 通过还是退回
            if ("checkPass".equals(fmSw.getLabel())) {
                Map<String, Object> reMap = new HashMap<>();
                reMap.put("userId", ShiroUtils.getUserId());

                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSwBf";
                String personId = "";
                //1.当前审核选择了审核人3
                if (StringUtils.isNotEmpty(fmSw.getCheckerThreeId())) {
                    //进入审核人3环节
                    reMap.put("reCode", "0");
                    reMap.put("businessKey", businessKey);
                    reMap.put("processDefinitionKey", processDefinitionKey);
                    reMap.put("checkerThreeId", fmSw.getCheckerThreeId());
                    reMap.put("comment", fmSw.getLogPerformDesc());
                    personId = fmSw.getCheckerThreeId();
                    //2.当前审核选择了审核人4
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerFourId())) {
                    //进入审核人4环节
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
                        log.error("事务事件单审核人2操作失败:{} ", e);
                        throw new BusinessException("事务事件单审核人2操作失败", "请刷新页面进行重试");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人

                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("事务事件单审核人2短信发送发送失败:{} ", e);
                    }
                    return AjaxResult.success("操作成功");
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerThreeId())) {
                    try {
                        fmSwService.checkUpdateFmSw(fmSw);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("事务事件单审核人3操作失败:{} ", e);
                        throw new BusinessException("事务事件单审核人3操作失败", "请刷新页面进行重试");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人

                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("事务事件单审核人3短信发送发送失败:{} ", e);
                    }
                    return AjaxResult.success("操作成功");
                } else if (StringUtils.isNotEmpty(fmSw.getCheckerFourId())) {
                    try {
                        fmSwService.checkUpdateFmSw(fmSw);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("事务事件单审核人4操作失败:{} ", e);
                        throw new BusinessException("事务事件单审核人4操作失败", "请刷新页面进行重试");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人

                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("事务事件单审核人4短信发送发送失败:{} ", e);
                    }
                    return AjaxResult.success("操作成功");
                } else {
                    try {
                        fmSwService.checkUpdateFmSw(fmSw);
                        activitiCommService.complete(reMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("事务事件单处理人5操作失败:{} ", e);
                        throw new BusinessException("事务事件单处理人5操作失败", "请刷新页面进行重试");
                    }
                    person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人

                    try {
                        sendSms(smsText, person);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("事务事件单处理人5短信发送发送失败:{} ", e);
                    }
                    return AjaxResult.success("操作成功");
                }

            } else { //审核退回待修改
                //获取事件单的创建人
                FmSw fmsw = fmSwService.selectFmSwById(fmSw.getFmSwId());
                String createId = fmsw.getCreateId();
                person = ogPersonService.selectOgPersonById(createId);// 获取短信接收人

                Map<String, Object> reMap = new HashMap<>();
                String businessKey = fmSw.getFmSwId();
                String processDefinitionKey = "FmSw";
                reMap.put("reCode", "1");
                reMap.put("businessKey", businessKey);
                reMap.put("processDefinitionKey", processDefinitionKey);
                reMap.put("createId", createId);
                reMap.put("comment", fmSw.getLogPerformDesc());
                //添加参与人
                reMap.put("userId", ShiroUtils.getUserId());
                //创建新对象只更新状态值和退回说明
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
                    log.error("事务事件单审核退回失败:{} ", e);
                    throw new BusinessException("事务事件单审核退回失败,请刷新页面进行重试");
                }
                try {
                    sendSms(smsText, person);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("事务事件单退回短信发送失败:{}", e);
                }
                return AjaxResult.success("操作成功");
            }
        }

    }

    /**
     * 查询当前用户的待授权的任务
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
     * 当前待处理任务处理通过
     *
     * @param fmSw
     * @return
     */
    @PostMapping("/authPass")
    @ResponseBody
    public AjaxResult authPassOrReturn(FmSw fmSw) {
        FmSw fm = fmSwService.selectFmSwById(fmSw.getFmSwId());
        String smsText = "您有事务事件单需要处理，单号：" + fm.getFaultNo();//短信内容
        String personId = "";
        //获取当前用户所作操作根据状态进行区别
        //1.用户进行了授权通过操作
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
            //2.用户进行了退回待修改
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
            log.error("事务事件单处理操作失败:{} ", e);
            throw new BusinessException("事务事件单处理操作失败,请刷新页面进行重试");
        }
        OgPerson person = ogPersonService.selectOgPersonById(personId);// 获取短信接收人

        try {
            sendSms(smsText, person);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("事务事件单处理操作短信发送失败: {}", e);
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 根据事务事件单id查询对应关联的资源变更单
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
     * 根据状态查询
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
        p.setCreaterId(ShiroUtils.getUserId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    /**
     * 暂存
     *
     * @return
     */
    @Log(title = "新增保存", businessType = BusinessType.INSERT)
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
                return AjaxResult.success("操作成功", fmSw.getFmSwId());
            } else {
                fmSwService.updateFmSw(fmSw);
                return AjaxResult.success("操作成功", fmSw.getFmSwId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("事务事件单暂存失败: {}", e);
            throw new BusinessException("暂存失败，单号是：" + fmSw.getFaultNo());
        }

    }

    /**
     * 选择部门树
     *
     * @param deptId    部门ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = {"/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}"})
    public String selectDeptTree(@PathVariable("deptId") String deptId,
        @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> collect = null;
        List<Ztree> ztreesOne = null;
        List<Ztree> ztreesTwo = null;
        //当前用户的人员ID
        String pId = ShiroUtils.getUserId();
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构还是二级机构
        OgOrg org = getOneLvOrTwoLv(ogOrg);
        if ("1".equals(org.getOrgLv()) || ("2".equals(org.getOrgLv()) && "/000000000/010000888/".equals(
            org.getLevelCode()))) {
            OgOrg orgOne = new OgOrg();
            orgOne.setLevelCode(org.getLevelCode());
            collect = deptService.selectDeptTree(orgOne);
        } else {
            //如果是省内查询当前省内及全国中心的
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
     * 获取当前登陆人的二级或者是一级机构
     *
     * @param ogOrg
     * @return
     */
    private OgOrg getOneLvOrTwoLv(OgOrg ogOrg) {
        //1.当前登陆用户的机构就是一级或者是二级机构
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
     * 获取当前登陆人员信息
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
     * 审核人2页面
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
        //获取当前用户的人员信息
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_check_two_option";
    }

    /**
     * 审核人3页面
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
        //获取当前用户的人员信息
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_check_three_option";
    }

    /**
     * 审核人4页面
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
        //获取当前用户的人员信息
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_check_four_option";
    }

    /**
     * 处理人页面
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
     * 申请人确认页面
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
     * 申请人确认数据列表页面
     *
     * @param modelMap
     * @return
     */

    @GetMapping("/application")
    public String smApplication(ModelMap modelMap) {
        //获取当前用户的人员信息
        modelMap.put("pId", ShiroUtils.getUserId());
        return prefix + "/sw_application";
    }

    /**
     * 查询当前用户的待处理的任务
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
     * 申请人确认页面
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
        //获取当前用户的人员信息
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



