package com.ruoyi.activiti.controller.itsm.biangenqingqiu;

import com.ruoyi.activiti.constants.FmSwConstants;
import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.service.*;
import com.ruoyi.activiti.service.impl.EndTaskAdapterImplBGQQ;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 变更请求单Controller
 *
 * @author zhangxu
 * @date 2020-12-21
 */
@Controller
@RequestMapping("/activiti/qingqiu")
public class CmBizQingqiuController extends BaseController
{
    private String prefix = "qingqiu";

    @Autowired
    private TaskService taskService;

    @Autowired
    private ICmBizQingqiuService cmBizQingqiuService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService iOgPersonService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    private IOgTypeinfoService ogTypeinfoService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IPubRelationService iPubRelationService;

    @Autowired
    private EndTaskAdapterImplBGQQ endTaskAdapterImplBGQQ;

    @Autowired
    private ICmBizSingleService cmBizSingleService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private IPubParaValueService iPubParaValueService;

    @Autowired
    private ItsmToDevopsService itsmToDevopsService;

    @Autowired
    private IPubAttachmentService pubAttachmentService;

    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;

    @Autowired
    private IPubFlowLogService pubFlowLogService;

    @Autowired
    private ProcessEngine processEngine;

    private final String SUCCESS = "1";// 启用状态

    @Value("${cntxtag.enabled}")
    private String cntxtag;

    /**
     * 我的变更请求单首页初始化
     */
    @GetMapping("/listAll")
    public String qingqiu(Map modelMap)
    {
        String pId = ShiroUtils.getOgUser().getpId();
        List<OgRole> roleList = sysRoleService.selectRolesByUserId(pId);
        String isHasRole = "0";
        for(OgRole role : roleList){
            if(FmSwConstants.sourceRole.equals(role.getRid())){
                isHasRole = "1";
            }
        }
        modelMap.put("isHasRole", isHasRole);
        modelMap.put("cntxtag", cntxtag);
        return prefix + "/qingqiu";
    }

    /**
     * 审批变更请求单列表
     */
    @PostMapping("/listShenpi")
    @ResponseBody
    public TableDataInfo listShenpi(CmBizQingqiu cmBizQingqiu)
    {
        String type=cmBizQingqiu.getParams().get("type").toString();
        List<CmBizQingqiu> list = new ArrayList<>();
        List<Map<String,Object>> userTask=activitiCommService.userTask("BGQQ","");
        List<Map<String,Object>> groupTask=activitiCommService.groupTasks("BGQQ","");
        userTask.addAll(groupTask);
        for(Map<String, Object> mp:userTask){
            String id=mp.get("businesskey")==null?"":mp.get("businesskey").toString();
            String taskName=mp.get("taskName").toString();
            CmBizQingqiu vo = new CmBizQingqiu();
            vo.setChangeId(id);
            vo.setChangeCode(cmBizQingqiu.getChangeCode());//变更单号
            if (StringUtils.isNotEmpty(cmBizQingqiu.getParams().get("createTime").toString())) {
                vo.getParams().put("createTime",handleTimeYYYYMMDDHHMMSS(cmBizQingqiu.getParams().get("createTime").toString()));
            }
            if (StringUtils.isNotEmpty(cmBizQingqiu.getParams().get("endCreateTime").toString())) {
                vo.getParams().put("endCreateTime",handleTimeYYYYMMDD(cmBizQingqiu.getParams().get("endCreateTime").toString()) + "240000");
            }
            vo.setChangeCategoryId(cmBizQingqiu.getChangeCategoryId());//变更分类ID
            vo.setApplyName(cmBizQingqiu.getApplyName());//申请人
            vo.setChangeSingleName(cmBizQingqiu.getChangeSingleName());//标题
            if("shenpi".equals(type)) {
                vo.setStauts("0000");//状态
                if ("审批".equals(taskName)) {
                    vo.setStauts("0300");//状态
                } else if ("分管领导审批".equals(taskName)) {
                    vo.setStauts("0600");//状态
                }
            } else if ("shouli".equals(type)) {
                if ("受理".equals(taskName)) {}
                vo.setStauts("0400");//状态
            }
            CmBizQingqiu cu = cmBizQingqiuService.selectBGQQVO(vo);
            if (cu != null) {
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(cu.getApplicantId());
                cu.setApplicantId(ogPerson.getpName());
                cu.getParams().put("taskName", taskName);
                list.add(cu);
            }
        }
        return getDataTable_ideal(list);
    }

    /**
     * 转受理审批列表
     */
    @PostMapping("/turnCheckList")
    @ResponseBody
    public TableDataInfo turnCheckList(CmBizQingqiu cmBizQingqiu)
    {
        List<CmBizQingqiu> list = new ArrayList<>();
        List<Map<String,Object>> userTask=activitiCommService.userTask("BGQQ","审批转受理");
        List<Map<String,Object>> groupTask=activitiCommService.groupTasks("BGQQ","审批转受理");
        userTask.addAll(groupTask);
        for(Map<String, Object> mp:userTask){
            String id=mp.get("businesskey")==null?"":mp.get("businesskey").toString();
            String taskName=mp.get("taskName").toString();
            CmBizQingqiu vo = new CmBizQingqiu();
            vo.setChangeId(id);
            vo.setChangeCode(cmBizQingqiu.getChangeCode());//变更单号
            if (StringUtils.isNotEmpty(cmBizQingqiu.getParams().get("createTime").toString())) {
                vo.getParams().put("createTime",handleTimeYYYYMMDDHHMMSS(cmBizQingqiu.getParams().get("createTime").toString()));
            }
            if (StringUtils.isNotEmpty(cmBizQingqiu.getParams().get("endCreateTime").toString())) {
                vo.getParams().put("endCreateTime",handleTimeYYYYMMDD(cmBizQingqiu.getParams().get("endCreateTime").toString()) + "240000");
            }
            vo.setChangeCategoryId(cmBizQingqiu.getChangeCategoryId());//变更分类ID
            vo.setApplyName(cmBizQingqiu.getApplyName());//申请人
            vo.setChangeSingleName(cmBizQingqiu.getChangeSingleName());//标题
            CmBizQingqiu cu = cmBizQingqiuService.selectBGQQVO(vo);
            if (cu != null) {
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(cu.getApplicantId());                cu.setApplicantId(ogPerson.getpName());
                cu.getParams().put("taskName", taskName);
                list.add(cu);
            }
        }
        return getDataTable_ideal(list);
    }

    /**
     * 我的变更请求单列表
     */
    @PostMapping("/listMy")
    @ResponseBody
    public TableDataInfo listMy(CmBizQingqiu cmBizQingqiu)
    {
        if ("0".equals(cmBizQingqiu.getMyIdentity())) {
            cmBizQingqiu.setApplicantId(ShiroUtils.getUserId());
        } else if (StringUtils.isEmpty(cmBizQingqiu.getMyIdentity()) || "1".equals(cmBizQingqiu.getMyIdentity())) {
            //我处理的
            PubFlowLog pub = new PubFlowLog();
            pub.setPerformerId(ShiroUtils.getOgUser().getUserId());
            pub.setLogType("BGQQ");
            List<PubFlowLog> pgList = pubFlowLogService.selectPubFlowLogList(pub);
            List<CmBizQingqiu> cmBizQingqiuList = new ArrayList<>();
            Set<String> idList = new HashSet<>();
            for (PubFlowLog pb : pgList) {
                if(!idList.contains(pb.getBizId())){
                    String taskName = pb.getTaskName();
                    if (!"撤回".equals(taskName) && !"修改".equals(taskName)) {
                        CmBizQingqiu cmBizQingqiu1 = new CmBizQingqiu();
                        cmBizQingqiu1.setChangeId(pb.getBizId());
                        cmBizQingqiu1.setChangeCode(cmBizQingqiu.getChangeCode());
                        cmBizQingqiu1.setChangeCategoryId(cmBizQingqiu.getChangeCategoryId());//变更分类ID
                        cmBizQingqiu1.setApplyName(cmBizQingqiu.getApplyName());//申请人
                        cmBizQingqiu1.setChangeSingleName(cmBizQingqiu.getChangeSingleName());//标题
                        cmBizQingqiu1.setIsNotice(cmBizQingqiu.getIsNotice());
                        cmBizQingqiu1.setChangeResource(cmBizQingqiu.getChangeResource());
                        cmBizQingqiu1.setStauts(cmBizQingqiu.getStauts());
                        cmBizQingqiu1.setParams(cmBizQingqiu.getParams());
                        //cmBizQingqiu1.setStauts("0500");
                        CmBizQingqiu cu = cmBizQingqiuService.selectBGQQVO(cmBizQingqiu1);
                        if (cu != null) {
                            cmBizQingqiuList.add(cu);
                        }
                        idList.add(pb.getBizId());
                    }
                }
            }
            if ("1".equals(cmBizQingqiu.getMyIdentity())) {
                cmBizQingqiuList = cmBizQingqiuList.stream().sorted(Comparator.comparing(CmBizQingqiu::getAddTime).reversed()).collect(Collectors.toList());
                return getDataTable_ideal(cmBizQingqiuList);
            } else if (StringUtils.isEmpty(cmBizQingqiu.getMyIdentity())) {
                cmBizQingqiu.setApplicantId(ShiroUtils.getUserId());
                setTime(cmBizQingqiu);
                List<CmBizQingqiu> list = cmBizQingqiuService.selectCmBizQingqiuList(cmBizQingqiu);
                List<String> bizIds = new ArrayList<>();
                if (list == null && list.size() == 0) {
                    cmBizQingqiuList = cmBizQingqiuList.stream().sorted(Comparator.comparing(CmBizQingqiu::getAddTime).reversed()).collect(Collectors.toList());
                    return getDataTable_ideal(cmBizQingqiuList);
                } else if (list != null && list.size() > 0) {
                    for (int i = 0 ; i < list.size() ; i ++) {
                        bizIds.add(list.get(i).getChangeId());
                    }
                }
                if (cmBizQingqiuList != null && cmBizQingqiuList.size() > 0) {
                    for (int i = 0 ; i < cmBizQingqiuList.size() ; i ++) {
                        if (!bizIds.contains(cmBizQingqiuList.get(i).getChangeId())) {
                            list.add(cmBizQingqiuList.get(i));
                        }
                    }
                }
                list = list.stream().sorted(Comparator.comparing(CmBizQingqiu::getAddTime).reversed()).collect(Collectors.toList());
                return getDataTable_ideal(list);
            }
        }
        setTime(cmBizQingqiu);
        List<CmBizQingqiu> list = cmBizQingqiuService.selectCmBizQingqiuList(cmBizQingqiu);
        return getDataTable_ideal(list);
    }

    /**
     * 我的变更请求单列表
     */
    @PostMapping("/listMys")
    @ResponseBody
    public TableDataInfo listMys(CmBizQingqiu cmBizQingqiu)
    {
        setTime(cmBizQingqiu);
        if ("0".equals(cmBizQingqiu.getMyIdentity())) {
            cmBizQingqiu.getParams().put("createId", ShiroUtils.getUserId());
        } else if ("1".equals(cmBizQingqiu.getMyIdentity())) {
            cmBizQingqiu.getParams().put("dealIdList", ShiroUtils.getUserId());
        } else if (StringUtils.isEmpty(cmBizQingqiu.getMyIdentity())) {
            cmBizQingqiu.getParams().put("dealId", ShiroUtils.getUserId());
        }
        startPage();
        List<CmBizQingqiu> list = cmBizQingqiuService.selectQingqiuList(cmBizQingqiu);
        return getDataTable(list);
    }

    /**
     * 查询变更请求单列表关联工单
     */
    @PostMapping("/listMyImp")
    @ResponseBody
    public TableDataInfo listMyImp(CmBizQingqiu cmBizQingqiu)
    {
        String pid = ShiroUtils.getOgUser().getpId();
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pid);
        cmBizQingqiu.setCheckOrg(ogPerson.getOrgId());
        List<CmBizQingqiu> list = cmBizQingqiuService.selectCmBizQingqiuList(cmBizQingqiu);
        return getDataTable_ideal(list);
    }

    /**
     * 查询变更请求单列表
     */
    @PostMapping("/selectQingQiu")
    @ResponseBody
    public TableDataInfo selectQingQiu(CmBizQingqiu cmBizQingqiu) {
        setTime(cmBizQingqiu);
        setTimes(cmBizQingqiu);
        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //设置这个标识空为全国中心 1为省分行
        cmBizQingqiu.getParams().put("flag", "");
        //如果不是全国中心人员
        if (!"000000000".equals(ogOrg.getOrgCode())) {
            //按照郭总要求查看创建人是自己 受理机构是自己机构或自己机构下级机构
            cmBizQingqiu.setApplicantId(ShiroUtils.getUserId());
            List<OgOrg> orglist = cmBizQingqiuService.selectAllChildOrg(orgId);
            String userOrgId = "";
            for (int i = 0; i < orglist.size(); i++) {
                userOrgId += orglist.get(i).getOrgId() + ",";
            }
            cmBizQingqiu.getParams().put("orgIds", userOrgId.split(","));
        }
        startPage();
        List<CmBizQingqiu> list = cmBizQingqiuService.selectQingqiuList(cmBizQingqiu);
        return getDataTable(list);
    }

    //时间改格式
    private CmBizQingqiu setTime(CmBizQingqiu cmBizQingqiu) {
        if (cmBizQingqiu.getParams().containsKey("createTime")) {
            if (StringUtils.isNotEmpty(cmBizQingqiu.getParams().get("createTime").toString())) {
                cmBizQingqiu.getParams().put("createTime",handleTimeYYYYMMDDHHMMSS(cmBizQingqiu.getParams().get("createTime").toString()));
            }
        }
        if (cmBizQingqiu.getParams().containsKey("endCreateTime")) {
            if (StringUtils.isNotEmpty(cmBizQingqiu.getParams().get("endCreateTime").toString())) {
                cmBizQingqiu.getParams().put("endCreateTime",handleTimeYYYYMMDD(cmBizQingqiu.getParams().get("endCreateTime").toString()) + "240000");
            }
        }
        return cmBizQingqiu;
    }

    //时间改格式
    private CmBizQingqiu setTimes(CmBizQingqiu cmBizQingqiu) {

        if (cmBizQingqiu.getParams().containsKey("practicleTime")) {
            if (StringUtils.isNotEmpty(cmBizQingqiu.getParams().get("practicleTime").toString())) {
                cmBizQingqiu.getParams().put("practicleTime",handleTimeYYYYMMDDHHMMSS(cmBizQingqiu.getParams().get("practicleTime").toString()));
            }
        }
        if (cmBizQingqiu.getParams().containsKey("endpracticleTime")) {
            if (StringUtils.isNotEmpty(cmBizQingqiu.getParams().get("endpracticleTime").toString())) {
                cmBizQingqiu.getParams().put("endpracticleTime",handleTimeYYYYMMDD(cmBizQingqiu.getParams().get("endpracticleTime").toString()) + "240000");
            }
        }
        return cmBizQingqiu;
    }

    /**
     * 导出变更请求单列表
     */
    @Log(title = "变更请求单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export1(CmBizQingqiu cmBizQingqiu)
    {
        setTime(cmBizQingqiu);
        if ("0".equals(cmBizQingqiu.getMyIdentity())) {
            cmBizQingqiu.getParams().put("createId", ShiroUtils.getUserId());
        } else if ("1".equals(cmBizQingqiu.getMyIdentity())) {
            cmBizQingqiu.getParams().put("dealIdList", ShiroUtils.getUserId());
        } else if (StringUtils.isEmpty(cmBizQingqiu.getMyIdentity())) {
            cmBizQingqiu.getParams().put("dealId", ShiroUtils.getUserId());
        }
        String isCurrentPage = (String) cmBizQingqiu.getParams().get("currentPage");
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
        }
        List<CmBizQingqiu> listExport = cmBizQingqiuService.selectQingqiuList(cmBizQingqiu);
        if (listExport != null) {
            for (int i = 0 ; i < listExport.size() ; i ++) {
                //将yyyyMMDDHHMMSS格式转为YYYY_MM_DD_HH_MM_SS
                listExport.get(i).setAddTime(handleTimeYYYY_MM_DD_HH_MM_SS(listExport.get(i).getAddTime()));
                if (StringUtils.isNotEmpty(listExport.get(i).getSubmitTime())) {
                    listExport.get(i).setSubmitTime(handleTimeYYYY_MM_DD_HH_MM_SS(listExport.get(i).getSubmitTime()));
                }
            }
        }
        ExcelUtil<CmBizQingqiu> util = new ExcelUtil<CmBizQingqiu>(CmBizQingqiu.class);
        return util.exportExcel(listExport, "变更请求单");
    }

    /**
     * 导出变更请求单列表
     */
    @Log(title = "变更请求单", businessType = BusinessType.EXPORT)
    @PostMapping("/exportSelectQingQiu")
    @ResponseBody
    public AjaxResult export2(CmBizQingqiu cmBizQingqiu)
    {
        setTime(cmBizQingqiu);
        setTimes(cmBizQingqiu);
        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //设置这个标识空为全国中心 1为省分行
        cmBizQingqiu.getParams().put("flag", "");
        if (!"000000000".equals(ogOrg.getOrgCode())) {
            //按照郭总要求查看创建人是自己 受理机构是自己机构或自己机构下级机构
            cmBizQingqiu.setApplicantId(ShiroUtils.getUserId());
            List<OgOrg> orglist = cmBizQingqiuService.selectAllChildOrg(orgId);
            String userOrgId = "";
            for (int i = 0; i < orglist.size(); i++) {
                userOrgId += orglist.get(i).getOrgId() + ",";
            }
            cmBizQingqiu.getParams().put("orgIds", userOrgId.split(","));
        }
        List<CmBizQingqiu> listExport = cmBizQingqiuService.selectQingqiuList(cmBizQingqiu);
//        String isCurrentPage = (String) cmBizQingqiu.getParams().get("currentPage");
//        if ("currentPage".equals(isCurrentPage)) {
//            startPage();
//        }
        if (listExport != null) {
            for (int i = 0 ; i < listExport.size() ; i ++) {
                //将yyyyMMDDHHMMSS格式转为YYYY_MM_DD_HH_MM_SS
                listExport.get(i).setAddTime(handleTimeYYYY_MM_DD_HH_MM_SS(listExport.get(i).getAddTime()));
                if (StringUtils.isNotEmpty(listExport.get(i).getSubmitTime())) {
                    listExport.get(i).setSubmitTime(handleTimeYYYY_MM_DD_HH_MM_SS(listExport.get(i).getSubmitTime()));
                }
            }
        }
        ExcelUtil<CmBizQingqiu> util = new ExcelUtil<CmBizQingqiu>(CmBizQingqiu.class);
        return util.exportExcel(listExport, "变更请求单");
    }

    /**
     * 新增变更请求单
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        String bizType = "BGQQ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        String userId = ShiroUtils.getUserId();
        OgPerson op = iOgPersonService.selectOgPersonById(userId);
        OgOrg ogOrg = deptService.selectDeptById(op.getOrgId());
        String orgName = ogOrg.getOrgName();
        mmap.put("num", bizType + nowDateStr + numStr);
        mmap.put("orgName",orgName);
        mmap.put("orgId",op.getOrgId());
        mmap.put("changeId", UUID.getUUIDStr());
        return prefix + "/add";
    }

    /**
     * 新增保存变更请求单
     */
    @Log(title = "变更请求单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult addSave(CmBizQingqiu cmBizQingqiu)
    {
        cmBizQingqiu.setAddTime(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
        cmBizQingqiu.setInvalidationMark("1");
        cmBizQingqiu.setApplicantId(ShiroUtils.getUserId());
        if (StringUtils.isNotEmpty(cmBizQingqiu.getStauts()) && "0300".equals(cmBizQingqiu.getStauts())){
            cmBizQingqiu.setSubmitTime(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
            Map<String,Object> reMap=new HashMap<>();
            reMap.put("businesskey",cmBizQingqiu.getChangeId());
            reMap.put("CHECKER_ID",cmBizQingqiu.getImplementorId());
            reMap.put("dealId",cmBizQingqiu.getCheckerId());
            reMap.put("creatId",ShiroUtils.getUserId());
            reMap.put("userId",ShiroUtils.getUserId());
            try {
                reMap.put("processDefinitionKey", "BGQQ");
                activitiCommService.startProcess(cmBizQingqiu.getChangeId(),"BGQQ",reMap);
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(cmBizQingqiu.getImplementorId());
                String text = "变更请求单号："+ cmBizQingqiu.getChangeCode() +"，标题："+ cmBizQingqiu.getChangeSingleName() +"需要审批，请登录运维管理平台处理。";
                sendSms(text, ogPerson);
            } catch (Exception e) {
                throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
            }
        }
        CmBizQingqiu cmBizQingqiu1 = cmBizQingqiuService.selectCmBizQingqiuById(cmBizQingqiu.getChangeId());
        if (cmBizQingqiu1 != null) {
            cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu);
        } else {
            cmBizQingqiu.setDealIdList(ShiroUtils.getUserId() + ",");
            cmBizQingqiuService.insertCmBizQingqiu(cmBizQingqiu);
        }
        return success("新增成功");
    }

    /**
     * 修改变更请求单
     */
    @GetMapping("/edit/{changeId}")
    public String edit(@PathVariable("changeId") String changeId, ModelMap mmap)
    {
        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(changeId);
        mmap.put("cmBizQingqiu", cmBizQingqiu);
        return prefix + "/edit";
    }

    /**
     * 修改保存变更请求单
     */
    @Log(title = "变更请求单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    public AjaxResult editSave(CmBizQingqiu cmBizQingqiu)
    {
        if ("commit".equals(cmBizQingqiu.getParams().get("flag"))) {
            if ("0100".equals(cmBizQingqiu.getStauts())) {
                cmBizQingqiu.setSubmitTime(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
                Map<String,Object> reMap=new HashMap<>();
                reMap.put("businesskey",cmBizQingqiu.getChangeId());
                reMap.put("CHECKER_ID",cmBizQingqiu.getImplementorId());
                reMap.put("dealId",cmBizQingqiu.getCheckerId());
                reMap.put("creatId",ShiroUtils.getUserId());
                reMap.put("userId",ShiroUtils.getUserId());
                activitiCommService.startProcess(cmBizQingqiu.getChangeId(),"BGQQ",reMap);
            } else if ("0200".equals(cmBizQingqiu.getStauts())) {
                Map<String,Object> reMap=new HashMap<>();
                reMap.put("businessKey",cmBizQingqiu.getChangeId());
                reMap.put("comment", cmBizQingqiu.getParams().get("comment"));
                reMap.put("CHECKER_ID",cmBizQingqiu.getImplementorId());
                reMap.put("dealId",cmBizQingqiu.getCheckerId());
                reMap.put("userId",ShiroUtils.getUserId());
                try {
                    reMap.put("processDefinitionKey", "BGQQ");
                    activitiCommService.complete(reMap);
                } catch (Exception e) {
                    throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
                }
            }
            OgPerson ogPerson = iOgPersonService.selectOgPersonById(cmBizQingqiu.getImplementorId());
            String text = "变更请求单号："+ cmBizQingqiu.getChangeCode() +"，标题："+ cmBizQingqiu.getChangeSingleName() +"需要审批，请登录运维管理平台处理。";
            sendSms(text, ogPerson);
            cmBizQingqiu.setStauts("0300");
        }
        return toAjax(cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu));
    }

    /**
     * 删除变更请求单
     */
    @Log(title = "变更请求单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    @Transactional
    public AjaxResult remove(String ids)
    {
        return toAjax(cmBizQingqiuService.deleteCmBizQingqiuByIds(ids));
    }

    /**
     * 作废变更请求单
     */
    @Log(title = "变更请求单", businessType = BusinessType.UPDATE)
    @PostMapping( "/cancle")
    @ResponseBody
    @Transactional
    public AjaxResult cancle(String id, String stauts) {
        if ("0200".equals(stauts)) {
            try {
                endTaskAdapterImplBGQQ.close(id);
                cmBizQingqiuService.cmBizQingQiuToCancle(id);
            } catch (Exception e) {
                throw new BusinessException("变更单作废操作失败，请刷新列表后重新操作。");
            }
        } else if ("0100".equals(stauts)) {
            cmBizQingqiuService.cmBizQingQiuToCancle(id);
        }
        return success("变更单作废操作成功。");
    }

    /**
     * 选择审批人
     * @return
     */
    @GetMapping("/selectBusinessAudit")
    public ModelAndView selectBusinessAudit(String orgId,String pflag,String rId,ModelMap mmap) {
        try {
            if(StringUtils.isNotEmpty(orgId)){
                orgId = URLDecoder.decode(orgId,"UTF-8");
            } else {
                String pid = ShiroUtils.getOgUser().getpId();
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(pid);
                ModelAndView model = new ModelAndView(prefix + "/selectLeader");
                model.addObject("orgId",ogPerson.getOrgId());
                model.addObject("rId",rId);
                model.addObject("pflag",pflag);
                return model;
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        ModelAndView model = new ModelAndView(prefix+"/selectPerson");
        model.addObject("orgId",orgId);
        model.addObject("rId",rId);
        model.addObject("pflag",pflag);
        return model;
    }

    /**
     * 分管领导查询
     * @param person
     * @return
     */
    @PostMapping("/selectLeader")
    @ResponseBody
    public TableDataInfo selectLeader(OgPerson person) {
        String orgId = "";
        if (StringUtils.isNotEmpty(person.getOrgId())) {
            orgId = person.getOrgId();
        } else {
            String pid = ShiroUtils.getOgUser().getpId();
            orgId = iOgPersonService.selectOgPersonById(pid).getOrgId();
        }
        OgOrg org = deptService.selectDeptById(orgId);
        while (!"/000000000/010000888/".equals(org.getLevelCode())) {
            org = deptService.selectDeptById(org.getParentId());
            orgId += "," + org.getOrgId();
        }
        String[] orgIds = orgId.split(",");
        person.getParams().put("orgId", orgIds);
        List<OgPerson> list = new ArrayList<>();
        list = cmBizQingqiuService.selectLeader(person);
        return getDataTable(list);
    }

    /**
     * 详情页面
     * @return
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap map)
    {
        //获取页面的信息
        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(id);
        String changeCategoryId = cmBizQingqiu.getChangeCategoryId();
        if(StringUtils.isNotEmpty(changeCategoryId)){
            OgTypeinfo typeinfo = ogTypeinfoService.selectOgTypeinfoById(changeCategoryId);
            cmBizQingqiu.setChangeCategoryId(typeinfo.getTypeName());
        }
        map.put("cmBizQingqiu",cmBizQingqiu);
        return prefix + "/detail";
    }

    /**
     * 查询页面
     * @return
     */
    @GetMapping("/selectqingqiu")
    public String selectdetailPage(ModelMap modelMap)
    {
        String pId = ShiroUtils.getOgUser().getpId();
        List<OgRole> roleList = sysRoleService.selectRolesByUserId(pId);
        String isHasRole = "0";
        String tongbuButton = "0";
        for(OgRole role : roleList){
            if(FmSwConstants.sourceRole.equals(role.getRid())){
                isHasRole = "1";
            }
        }
        if ("8b8080f457fffe39015800015ce60006".equals(pId)) {
            tongbuButton = "1";
        }
        modelMap.put("isHasRole", isHasRole);
        modelMap.put("tongbuButton", tongbuButton);
        return prefix + "/selectqingqiu";
    }

    /**
     * 审批页面
     * @return
     */
    @GetMapping("/exa")
    public String exaPage(CmBizQingqiu cmBizQingqiu,ModelMap modelMap)
    {
        cmBizQingqiu.setStauts("0400");
        modelMap.put("pId",ShiroUtils.getOgUser().getpId());
        return prefix + "/exa";
    }

    /**
     * 审批按钮对应页面
     * @return
     */
    @GetMapping("/SpDetail")
    public String auditAuthor(String id,String taskName, ModelMap mmap)
    {
        try {
            taskName = URLDecoder.decode(taskName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CmBizQingqiu cmBizQingqiu=cmBizQingqiuService.selectCmBizQingqiuById(id);
        mmap.put("cmBizQingqiu", cmBizQingqiu);
        mmap.put("taskName", taskName);
        mmap.put("pId",ShiroUtils.getOgUser().getUserId());
        mmap.put("ifCenter","1");
        mmap.put("processOption", "1");
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());
        //如果不是全国中心人员
        if (!"/000000000/010000888/".equals(org.getLevelCode())) {
            mmap.put("ifCenter","2");
            mmap.put("processOption", "0");
        }
        return prefix + "/SpDetail";
    }

    /**
     * 审核是否通过
     *
     * @param
     * @return
     */
    @PostMapping("/auditPass")
    @ResponseBody
    @Transactional
    public AjaxResult auditPass(CmBizQingqiu cmBizQingqiu) {
        String comment = (String) cmBizQingqiu.getParams().get("comment");
        String taskName=cmBizQingqiu.getParams().get("taskName").toString();
        Map<String, Object> map = new HashMap<>();
        String businessKey = cmBizQingqiu.getChangeId();
        map.put("businessKey",businessKey);
        map.put("comment", comment);
        map.put("processDefinitionKey", "BGQQ");
        map.put("userId",ShiroUtils.getUserId());
        String status = "";
        String funcheckerFlag = "";
        CmBizQingqiu cmBizQingqiu1 = cmBizQingqiuService.selectCmBizQingqiuById(cmBizQingqiu.getChangeId());
        if("分管领导审批".equals(taskName)){
            if ("1".equals(cmBizQingqiu.getPassFlag())) {
                // 此时流程走向为调度处理
                map.put("reCode", "0");
                status = "0400";
                try {
                    activitiCommService.complete(map);
                    if ("1".equals(cmBizQingqiu.getIsNotice())) {
                        OgPerson ogPerson1 = iOgPersonService.selectOgPersonById(cmBizQingqiu.getCheckerId());
                        String text1 = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"已经审批完成，请登录运维管理平台受理。";
                        sendSms(text1, ogPerson1);
                    }
                } catch (Exception e) {
                    throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
                }
            }
            if ("0".equals(cmBizQingqiu.getPassFlag())) {
                // 此时流程走向为退回
                map.put("reCode", "1");
                status = "0200";
                try {
                    activitiCommService.complete(map);
                    OgPerson ogPerson = iOgPersonService.selectOgPersonById(cmBizQingqiu1.getImplementorId());
                    OgPerson ogPerson1 = iOgPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
                    String text = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"，审批人："+ ogPerson1.getpName() +"，请登录运维管理平台操作。";
                    sendSms(text, ogPerson);
                } catch (Exception e) {
                    throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
                }
            }
        }else {
            //审批
            if ("1".equals(cmBizQingqiu.getParams().get("processOption")) && "1".equals(cmBizQingqiu.getPassFlag())) {
                // 此时流程走向为分管领导审批
                map.put("reCode", "0");
                status = "0600";
                map.put("fucheckerId", cmBizQingqiu.getFucheckerId());
                try {
                    activitiCommService.complete(map);
                } catch (Exception e) {
                    throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
                }
            } else if ("0".equals(cmBizQingqiu.getPassFlag())) {
                // 此时流程走向为退回
                map.put("reCode", "1");
                status = "0200";
                try {
                    activitiCommService.complete(map);
                    OgPerson ogPerson = iOgPersonService.selectOgPersonById(cmBizQingqiu1.getApplicantId());
                    OgPerson ogPerson1 = iOgPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
                    String text = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"，审批人："+ ogPerson1.getpName() +"，请登录运维管理平台操作。";
                    sendSms(text, ogPerson);
                } catch (Exception e) {
                    throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
                }
            } else if ("0".equals(cmBizQingqiu.getParams().get("processOption")) && "1".equals(cmBizQingqiu.getPassFlag())) {
                // 此时流程走向受理
                map.put("reCode", "2");
                status = "0400";
                funcheckerFlag = "1";
                try {
                    activitiCommService.complete(map);
                    //通知分管领导
                    if (StringUtils.isNotEmpty(cmBizQingqiu.getFucheckerId())) {
                        OgPerson ogPerson = iOgPersonService.selectOgPersonById(cmBizQingqiu.getFucheckerId());
                        String text = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"已经审批完成，请登录运维管理平台查看。";
                        sendSms(text, ogPerson);
                    }
                    if ("1".equals(cmBizQingqiu.getIsNotice())) {
                        OgPerson ogPerson1 = iOgPersonService.selectOgPersonById(cmBizQingqiu.getCheckerId());
                        String text1 = "变更请求单号："+ cmBizQingqiu.getChangeCode()+ "，标题："+ cmBizQingqiu.getChangeSingleName() +"已经审批完成，请登录运维管理平台受理。";
                        sendSms(text1, ogPerson1);
                    }
                } catch (Exception e) {
                    throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
                }
            }
        }
        cmBizQingqiu.setStauts(status);
        cmBizQingqiu.setFucheckerFlag(funcheckerFlag);
        String dealIdList = cmBizQingqiu1.getDealIdList();
        cmBizQingqiu.setDealIdList(dealIdList + ShiroUtils.getUserId() + ",");
        return toAjax(cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu));
    }

    /**
     * 受理页面
     * @return
     */
    @GetMapping("/dispose")
    public String disposePage(CmBizQingqiu cmBizQingqiu,ModelMap modelMap)
    {
        modelMap.put("pId",ShiroUtils.getOgUser().getpId());
        return prefix + "/dispose";
    }

    /**
     * 受理按钮对应页面
     * @return
     */
    @GetMapping("/SlDetail")
    public String auditAuthorSl(String id,String taskName, ModelMap mmap)
    {
        CmBizQingqiu cmBizQingqiu=cmBizQingqiuService.selectCmBizQingqiuById(id);
        String pId = cmBizQingqiu.getCheckerId();//受理人Id
        String userId = ShiroUtils.getUserId();
        String XTflag = "0";//此标志为控制处理页面的“转受理”按钮的显隐 0为显示 1为隐藏
        String flag = "0";//是否工作台
        String flagold = "1";//老单子判断当前登录人是否是原受理人
        long addTime = Long.parseLong(cmBizQingqiu.getAddTime());
        long levelUpTime = 20211012160000L;
        if (addTime < levelUpTime) {
            if (!userId.equals(pId)) {
                flagold = "2";
            }
            mmap.put("cmBizQingqiu", cmBizQingqiu);
            mmap.put("pId",ShiroUtils.getOgUser().getUserId());
            mmap.put("taskName",taskName);
            mmap.put("flag",flagold);
            return prefix + "/SlDetailOld";
        }
        PubFlowLog pubFlowLog = new PubFlowLog();
        pubFlowLog.setBizId(cmBizQingqiu.getChangeId());
        List<PubFlowLog> pubFlowLogList = pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        String nextTaskName = "";
        if (pubFlowLogList != null && pubFlowLogList.size() > 0) {
            nextTaskName = pubFlowLogList.get(0).getNextTaskName();
        }
        if ("协同受理".equals(nextTaskName)) {
            XTflag = "1";
        }
        mmap.put("cmBizQingqiu", cmBizQingqiu);
        mmap.put("pId",ShiroUtils.getOgUser().getUserId());
        mmap.put("taskName",taskName);
        mmap.put("flag",flag);
        mmap.put("XTflag",XTflag);
        return prefix + "/SlDetail";
    }

    /**
     * 受理是否通过
     *
     * @param cmBizQingqiu
     * @return
     */
    @PostMapping("/SlauditPassOld")
    @ResponseBody
    @Transactional
    public AjaxResult auditPassSlOld(CmBizQingqiu cmBizQingqiu) {
        OgUser ogUser = ShiroUtils.getOgUser();
        String comment = (String) cmBizQingqiu.getParams().get("comment");
        Map<String, Object> map = new HashMap<>();
        String status = "";
        String businessKey = cmBizQingqiu.getChangeId();
        map.put("businessKey",businessKey);
        map.put("comment", comment);
        map.put("processDefinitionKey", "BGQQ");
        map.put("userId",ShiroUtils.getUserId());
        if ("1".equals(cmBizQingqiu.getPassFlag())) {
            try {
                activitiCommService.usersComplete(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 此时流程走向为退回
            map.put("reCode", "1");
            status = "0200";
            map.put("createId",ogUser.getpId());
            cmBizQingqiu.setStauts(status);
            cmBizQingqiu.setPracticleTime(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
            try {
                activitiCommService.complete(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CmBizQingqiu cmBizQingqiu1 = cmBizQingqiuService.selectCmBizQingqiuById(cmBizQingqiu.getChangeId());
        CmBizQingqiu qingqiu = new CmBizQingqiu();
        qingqiu.setChangeId(cmBizQingqiu1.getChangeId());
        if (StringUtils.isNotEmpty(status)) {
            qingqiu.setStauts(status);
        }
        String dealIdList = cmBizQingqiu1.getDealIdList();
        qingqiu.setDealIdList(dealIdList + ShiroUtils.getUserId() + ",");
        cmBizQingqiuService.updateCmBizQingqiu(qingqiu);
        return AjaxResult.success("操作成功");
    }

    /**
     * 受理是否通过
     *
     * @param cmBizQingqiu
     * @return
     */
    @PostMapping("/SlauditPass")
    @ResponseBody
    @Transactional
    public AjaxResult auditPassSl(CmBizQingqiu cmBizQingqiu) {
        OgUser ogUser = ShiroUtils.getOgUser();
        PubFlowLog pubFlowLog = new PubFlowLog();
        pubFlowLog.setBizId(cmBizQingqiu.getChangeId());
        List<PubFlowLog> logList = pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        String dealIdNow = logList.get(0).getNextPerformerDesc();
        if (StringUtils.isEmpty(dealIdNow) || !dealIdNow.equals(ogUser.getUserId())) {
            return AjaxResult.error("您已不是当前变更请求单的受理人，请刷新列表重试。");
        }
        String comment = (String) cmBizQingqiu.getParams().get("comment");
        Map<String, Object> map = new HashMap<>();
        String status = "";
        String businessKey = cmBizQingqiu.getChangeId();
        map.put("businessKey",businessKey);
        map.put("comment", comment);
        map.put("processDefinitionKey", "BGQQ");
        map.put("userId",ShiroUtils.getUserId());
        CmBizQingqiu qingqiu = cmBizQingqiuService.selectCmBizQingqiuById(cmBizQingqiu.getChangeId());
        if ("1".equals(cmBizQingqiu.getPassFlag())) {
            //通过 流程结束
            try {
                map.put("reCode", "0");
                activitiCommService.complete(map);
                status = "0500";
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("操作失败");
            }
        } else if ("0".equals(cmBizQingqiu.getPassFlag())){
            // 此时流程走向为退回
            try {
                map.put("reCode", "1");
                status = "0200";
                map.put("createId",qingqiu.getApplicantId());
                activitiCommService.complete(map);
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(qingqiu.getApplicantId());
                String text = "变更请求单号："+ qingqiu.getChangeCode()+ "，标题："+ qingqiu.getChangeSingleName() +"已退回，请登录运维管理平台处理。";
                sendSms(text, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("操作失败");
            }
        }
        CmBizQingqiu cmBizQingqiu1 = new CmBizQingqiu();
        cmBizQingqiu1.setChangeId(qingqiu.getChangeId());
        cmBizQingqiu1.setStauts(status);
        cmBizQingqiu1.setPracticleTime(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
        String dealIdList = qingqiu.getDealIdList();
        cmBizQingqiu1.setDealIdList(dealIdList + ShiroUtils.getUserId() + ",");
        cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu1);
        return AjaxResult.success("变更请求单操作成功");
    }

    /**
     * 协同受理页面
     *
     * @return
     */
    @GetMapping("/togetherAcceptance/{changeId}/{flag}")
    public String synergy(@PathVariable("changeId") String changeId, @PathVariable("flag") String flag, ModelMap mmap) {
        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(changeId);
        mmap.put("cmBizQingqiu", cmBizQingqiu);
        mmap.put("changeId", changeId);
        mmap.put("flag", flag);
        return prefix + "/togetherAcceptance/togetherAcceptance";
    }

    /**
     * 协同受理是否通过 通过到变更单受理人
     *
     * @param cmBizQingqiu
     * @return
     */
    @PostMapping("/XTSlauditPass")
    @ResponseBody
    @Transactional
    public AjaxResult auditXTPassSl(CmBizQingqiu cmBizQingqiu) {
        OgUser ogUser = ShiroUtils.getOgUser();
        PubFlowLog pubFlowLog = new PubFlowLog();
        pubFlowLog.setBizId(cmBizQingqiu.getChangeId());
        List<PubFlowLog> logList = pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        String dealIdNow = logList.get(0).getNextPerformerDesc();
        if (StringUtils.isEmpty(dealIdNow) || !dealIdNow.equals(ogUser.getUserId())) {
            return AjaxResult.error("您已不是当前变更请求单的受理人，请刷新列表重试。");
        }
        String comment = (String) cmBizQingqiu.getParams().get("comment");
        Map<String, Object> map = new HashMap<>();
        String status = "";
        String businessKey = cmBizQingqiu.getChangeId();
        map.put("businessKey",businessKey);
        map.put("comment", comment);
        map.put("processDefinitionKey", "BGQQ");
        map.put("userId",ShiroUtils.getUserId());
        CmBizQingqiu cmBizQingqiu1 = cmBizQingqiuService.selectCmBizQingqiuById(businessKey);
        // 此时流程走向为结束           //受理${dealId}
        if ("1".equals(cmBizQingqiu.getPassFlag())) {
            try {
//                map.put("dealId", cmBizQingqiu1.getCheckerId());//初始受理人ID
                map.put("reCode", "0");
                status = "0500";
                activitiCommService.complete(map);
//                OgPerson ogPerson = iOgPersonService.selectOgPersonById(cmBizQingqiu1.getCheckerId());
//                String text = "变更请求单号："+ cmBizQingqiu1.getChangeCode()+ "，标题："+ cmBizQingqiu1.getChangeSingleName() +"协同受理已通过，请登录运维管理平台继续处理。";
//                sendSms(text, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("操作失败");
            }
        } else{
            // 此时流程走向为退回${creatId}
            try {
                map.put("reCode", "1");
                map.put("creatId", cmBizQingqiu1.getApplicantId());//申请人ID
                status = "0200";
                map.put("createId",ogUser.getpId());
                cmBizQingqiu.setStauts(status);
                activitiCommService.complete(map);
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(cmBizQingqiu1.getApplicantId());
                String text = "变更请求单号："+ cmBizQingqiu1.getChangeCode()+ "，标题："+ cmBizQingqiu1.getChangeSingleName() +"协同受理已退回，请登录运维管理平台处理。";
                sendSms(text, ogPerson);
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("操作失败");
            }
        }
        CmBizQingqiu qingqiu = new CmBizQingqiu();
        qingqiu.setChangeId(cmBizQingqiu1.getChangeId());
        qingqiu.setStauts(status);
        String dealIdList = cmBizQingqiu1.getDealIdList();
        qingqiu.setDealIdList(dealIdList + ShiroUtils.getUserId() + ",");
        qingqiu.setPracticleTime(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
        cmBizQingqiuService.updateCmBizQingqiu(qingqiu);
        return AjaxResult.success("操作成功");
    }

    /**
     *协同处理
     * @param cmBizQingqiu ccAuthorText 意见 dealId 受理人 changeId   ID
     * @return
     */
    @PostMapping("/collaborativeAccept")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult collaborativeAccept(CmBizQingqiu cmBizQingqiu){
        if (StringUtils.isEmpty(cmBizQingqiu.getChangeId())) {
            return AjaxResult.error("ID不能为空");
        }
        if (StringUtils.isEmpty(cmBizQingqiu.getCheckerId())) {
            return AjaxResult.error("请选择受理人");
        }
        if (StringUtils.isEmpty(cmBizQingqiu.getParams().get("comment"))) {
            return AjaxResult.error("意见不能为空");
        }
        CmBizQingqiu qingqiu = cmBizQingqiuService.selectCmBizQingqiuById(cmBizQingqiu.getChangeId());
        if (qingqiu == null) {
            return AjaxResult.error("未找到该变更单");
        }
        PubFlowLog pubFlowLog = new PubFlowLog();
        pubFlowLog.setBizId(cmBizQingqiu.getChangeId());
        List<PubFlowLog> logList = pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        String dealIdNow = logList.get(0).getNextPerformerDesc();
        if (StringUtils.isEmpty(dealIdNow) || !dealIdNow.equals(ShiroUtils.getUserId())) {
            return AjaxResult.error("您已不是当前变更请求单的受理人，请刷新列表重试。");
        }
        try {
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("businessKey", cmBizQingqiu.getChangeId());
            reMap.put("comment", cmBizQingqiu.getParams().get("comment"));
            reMap.put("dealId", cmBizQingqiu.getCheckerId());
            reMap.put("processDefinitionKey", "BGQQ");
            reMap.put("userId", ShiroUtils.getUserId());
            reMap.put("reCode", "3");
            activitiCommService.complete(reMap);//添加协同受理人
            String text = "变更请求单号：" + qingqiu.getChangeCode() + "需要您帮忙协助处理，请登录运维管理平台查看";
            OgPerson person = iOgPersonService.selectOgPersonById(cmBizQingqiu.getCheckerId());// 获取短信接收人
            sendSms(text, person);
            //更新操作人集合
            CmBizQingqiu cmBizQingqiu1 = new CmBizQingqiu();
            cmBizQingqiu1.setChangeId(qingqiu.getChangeId());
            String dealIdList = qingqiu.getDealIdList();
            cmBizQingqiu1.setDealIdList(dealIdList + ShiroUtils.getUserId() + ",");
            cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success("操作成功", cmBizQingqiu.getChangeCode());
    }

    /**
     * 转受理页面
     *
     * @return
     */
    @GetMapping("/turnAcceptance/{changeId}/{flag}")
    public String turn(@PathVariable("changeId") String changeId, @PathVariable("flag") String flag, ModelMap mmap) {
        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(changeId);
        mmap.put("cmBizQingqiu", cmBizQingqiu);
        mmap.put("changeId", changeId);
        mmap.put("flag", flag);
        return prefix + "/turnAcceptance/turnAcceptance";
    }

    /**
     * 转受理
     * @param cmBizQingqiu
     * @return
     */
    @PostMapping("/turnAcceptanceDeal")
    @ResponseBody
    @Transactional
    public AjaxResult turnAcceptanceDeal(CmBizQingqiu cmBizQingqiu){
        if (StringUtils.isEmpty(cmBizQingqiu.getChangeId())) {
            return AjaxResult.error("ID不能为空");
        }
        if (StringUtils.isEmpty((String) cmBizQingqiu.getParams().get("checkerId"))) {
            return AjaxResult.error("请选择受理人");
        }
        if (StringUtils.isEmpty((String) cmBizQingqiu.getParams().get("firstCheckerId"))) {
            return AjaxResult.error("请选择一审审批人");
        }
        if (StringUtils.isEmpty((String) cmBizQingqiu.getParams().get("secondCheckId"))) {
            return AjaxResult.error("请选择二审审批人");
        }
        if (StringUtils.isEmpty((String) cmBizQingqiu.getParams().get("AuthorText"))) {
            return AjaxResult.error("意见不能为空");
        }
        String businessKey = cmBizQingqiu.getChangeId();
        String ccAuthorText = (String) cmBizQingqiu.getParams().get("AuthorText");
        String dealId = (String) cmBizQingqiu.getParams().get("checkerId");
        String firstCheckId = (String) cmBizQingqiu.getParams().get("firstCheckerId");
        String secondCheckId = (String) cmBizQingqiu.getParams().get("secondCheckId");
        CmBizQingqiu cmBizQingqiu1 = cmBizQingqiuService.selectCmBizQingqiuById(businessKey);
        if (cmBizQingqiu1 == null) {
            return AjaxResult.error("未找到该变更单");
        }
        try {
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("businessKey", businessKey);
            reMap.put("comment", ccAuthorText);
            reMap.put("dealId", dealId);//添加转受理人员
            reMap.put("first_checkId", firstCheckId);//添加一审人员
            reMap.put("second_checkId", secondCheckId);//添加二审人员
            reMap.put("processDefinitionKey", "BGQQ");
            reMap.put("userId", ShiroUtils.getUserId());
            reMap.put("reCode", "2");//转受理
            activitiCommService.complete(reMap);
            String text = "变更请求单号：" + cmBizQingqiu1.getChangeCode() + "已转受理，需要您审批，请登录运维管理平台查看";
            OgPerson person = iOgPersonService.selectOgPersonById(dealId);// 获取短信接收人
            sendSms(text, person);
            CmBizQingqiu qingqiu = new CmBizQingqiu();
            qingqiu.setChangeId(cmBizQingqiu1.getChangeId());
            qingqiu.setStauts("0301");//一审
            String dealIdList = cmBizQingqiu1.getDealIdList();
            qingqiu.setDealIdList(dealIdList + ShiroUtils.getUserId() + ",");
            cmBizQingqiuService.updateCmBizQingqiu(qingqiu);//0301状态为转受理待审批状态
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
        return AjaxResult.success("操作成功", cmBizQingqiu.getChangeCode());
    }

    /**
     * 转受理待审批列表
     * @param cmBizQingqiu
     * @param modelMap
     * @return
     */
    @GetMapping("/checkTurnAcceptance")
    public String turnAcceptance(CmBizQingqiu cmBizQingqiu, ModelMap modelMap)
    {
        return prefix + "/turnAcceptance/checkTurnAcceptance";
    }

    /**
     * 转受理审批按钮对应页面
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/turnAcceptanceDetail")
    public String turnAcceptanceDetail(String id, ModelMap mmap)
    {
        CmBizQingqiu cmBizQingqiu=cmBizQingqiuService.selectCmBizQingqiuById(id);
        mmap.put("cmBizQingqiu", cmBizQingqiu);
        return prefix + "/turnAcceptance/checkTurnAcceptanceDetail";
    }

    /**
     * 审批转受理是否通过
     *
     * @param
     * @return
     */
    @PostMapping("/auditPassturnAcceptance")
    @ResponseBody
    @Transactional
    public AjaxResult auditPassturnAcceptance(CmBizQingqiu cmBizQingqiu) {
        CmBizQingqiu qingqiu = cmBizQingqiuService.selectCmBizQingqiuById(cmBizQingqiu.getChangeId());
        String comment = (String) cmBizQingqiu.getParams().get("comment");//意见
        Map<String, Object> map = new HashMap<>();
        String businessKey = cmBizQingqiu.getChangeId();
        Task task = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(businessKey)
                .singleResult();
        map.put("businessKey",businessKey);
        map.put("comment", comment);
        map.put("processDefinitionKey", "BGQQ");
        map.put("userId",ShiroUtils.getUserId());
        String status = "";
        String passFlag = cmBizQingqiu.getPassFlag();
        if ("0".equals(passFlag) && "0301".equals(qingqiu.getStauts())) {//一审通过
            map.put("reCode", "0");
            status = "0302";//二审
            try {
                String variable = taskService.getVariable(task.getId(), "second_checkId") == null ? "" : taskService.getVariable(task.getId(), "second_checkId").toString();
                activitiCommService.complete(map);
                String[] xtIdsS = variable.split(",");
                for (int i = 0 ; i < xtIdsS.length ; i ++) {
                    OgPerson ogPerson = iOgPersonService.selectOgPersonById(xtIdsS[i]);
                    String text = "变更请求单号：" + cmBizQingqiu.getChangeCode() + "，标题：" + cmBizQingqiu.getChangeSingleName() + "需要审批，请登录运维管理平台处理。";
                    sendSms(text, ogPerson);
                }
            } catch (Exception e) {
                throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
            }
        } else if ("0".equals(passFlag) && "0302".equals(qingqiu.getStauts())) {//二审通过
            map.put("reCode", "0");
            status = "0400";
            try {
                String variable = taskService.getVariable(task.getId(), "dealId") == null ? "" : taskService.getVariable(task.getId(), "dealId").toString();
                activitiCommService.complete(map);
                String[] xtIdsS = variable.split(",");
                for (int i = 0 ; i < xtIdsS.length ; i ++) {
                    OgPerson ogPerson = iOgPersonService.selectOgPersonById(xtIdsS[i]);
                    String text = "变更请求单号：" + cmBizQingqiu.getChangeCode() + "，标题：" + cmBizQingqiu.getChangeSingleName() + "需要处理，请登录运维管理平台处理。";
                    sendSms(text, ogPerson);
                }
            } catch (Exception e) {
                throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
            }
        } else if ("1".equals(cmBizQingqiu.getPassFlag())) {//退回到发起人
            map.put("reCode", "1");
            status = "0200";
            try {
                activitiCommService.complete(map);
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(qingqiu.getApplicantId());
                String text = "变更请求单号：" + cmBizQingqiu.getChangeCode() + "，标题：" + cmBizQingqiu.getChangeSingleName() + "转受理被退回，请登录运维管理平台处理。";
                sendSms(text, ogPerson);
            } catch (Exception e) {
                throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
            }
        } else if ("2".equals(cmBizQingqiu.getPassFlag())) {//退回到原受理人
            map.put("reCode", "2");
            map.put("dealId", qingqiu.getCheckerId());
            status = "0400";
            try {
                activitiCommService.complete(map);
                OgPerson ogPerson = iOgPersonService.selectOgPersonById(qingqiu.getCheckerId());
                String text = "变更请求单号：" + cmBizQingqiu.getChangeCode() + "，标题：" + cmBizQingqiu.getChangeSingleName() + "转受理被退回，请登录运维管理平台处理。";
                sendSms(text, ogPerson);
            } catch (Exception e) {
                throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
            }
        }
        CmBizQingqiu cmBizQingqiu1 = new CmBizQingqiu();
        cmBizQingqiu1.setChangeId(qingqiu.getChangeId());
        cmBizQingqiu1.setStauts(status);
        String dealIdList = qingqiu.getDealIdList();
        cmBizQingqiu1.setDealIdList(dealIdList + ShiroUtils.getUserId() + ",");
        return toAjax(cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu1));
    }

    /**
     * 分管领导审阅列表
     * @param cmBizQingqiu
     * @return
     */
    @GetMapping("/review")
    public String review(CmBizQingqiu cmBizQingqiu)
    {
        return prefix + "/review";
    }

    /**
     * 分管领导审阅列表list
     */
    @PostMapping("/reviewList")
    @ResponseBody
    public TableDataInfo reviewList(CmBizQingqiu cmBizQingqiu)
    {
        setTime(cmBizQingqiu);
        cmBizQingqiu.setFucheckerId(ShiroUtils.getUserId());
        //cmBizQingqiu.setFucheckerFlag("1");
        startPage();
        List<CmBizQingqiu> list = cmBizQingqiuService.selectQingqiuList(cmBizQingqiu);
        return getDataTable(list);
    }

    /**
     * 分管领导审阅变更单
     */
    @GetMapping("/reviewDetail")
    public String reviewDetail(String id, ModelMap mmap) {
        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(id);
        mmap.put("cmBizQingqiu", cmBizQingqiu);
        CmBizQingqiu cmBizQingqiu1 = new CmBizQingqiu();
        cmBizQingqiu1.setChangeId(id);
        cmBizQingqiu1.setFucheckerFlag("2");//1 待审 2 已审
        cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu1);
        return prefix + "/reviewDetail";
    }

    /**
     * id查询
     */
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(id);
        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        String flag = "0";//当前登陆人与创建人一致 可修改
        if (pId.equals(cmBizQingqiu.getApplicantId())) {
            flag = "1";
        }
        ajaxResult.put("data",cmBizQingqiu);
        ajaxResult.put("flag",flag);
        return  ajaxResult;
    }

    /**
     * id查询
     */
    @PostMapping( "/tongbu")
    @ResponseBody
    public AjaxResult tongbu(String id, String createTime, String endCreateTime)
    {
        AjaxResult ajaxResult = new AjaxResult();
        CmBizQingqiu qingqiu = new CmBizQingqiu();
        qingqiu.getParams().put("createTime", createTime);
        qingqiu.getParams().put("endCreateTime", endCreateTime);
        setTime(qingqiu);
        int num = 0;
        List<CmBizQingqiu> list = cmBizQingqiuService.selectQingqiuList(qingqiu);
        if (list != null && list.size() > 0) {
            for (CmBizQingqiu qingqiu1 : list) {
                if (StringUtils.isEmpty(qingqiu1.getDealIdList())) {
                    PubFlowLog pub = new PubFlowLog();
                    pub.setBizId(qingqiu1.getChangeId());
                    pub.setLogType("BGQQ");
                    List<PubFlowLog> pgList = pubFlowLogService.selectPubFlowLogList(pub);
                    String dealIdList = "";
                    if (pgList != null && pgList.size() > 0) {
                        for (PubFlowLog pubFlowLog : pgList) {
                            dealIdList += pubFlowLog.getPerformerId() + ",";
                        }
                        CmBizQingqiu cmBizQingqiu1 = new CmBizQingqiu();
                        cmBizQingqiu1.setChangeId(qingqiu1.getChangeId());
                        cmBizQingqiu1.setDealIdList(dealIdList);
                        cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu1);
                        num ++;
                    }
                }
            }
        }
//        if (StringUtils.isNotEmpty(cmBizQingqiu.getDealIdList())) {
//            ajaxResult.put("res","0");
//            return ajaxResult;
//        }
        //当前用户的人员ID
        ajaxResult.put("num",num);
        return ajaxResult;
    }

    /**
     * 转资源变更单
     */
    @GetMapping("/addCmbiz")
    public String addCmbiz(String changeId,ModelMap mmap)
    {
        //打开新建页面生成单号
        String bizType = "BG";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);//默认回传单号
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person= iOgPersonService.selectOgPersonById(pId);
        OgOrg org= iSysDeptService.selectDeptById(person.getOrgId());
        mmap.put("createrOrgName", org.getOrgName());//回传创建机构
        mmap.put("createrOrgId", person.getOrgId());
        CmBizQingqiu cmBizQingqiu=cmBizQingqiuService.selectCmBizQingqiuById(changeId);
        mmap.put("cmBizQingqiu", cmBizQingqiu);
        mmap.put("changeId", changeId);
        return prefix + "/startCmbiz";
    }

    /**
     * 根据变更单ID查询变更请求单
     */
    @PostMapping("/cmddList/{id}")
    @ResponseBody
    public TableDataInfo getCmAndCpqq(@PathVariable("id") String id) {
        PubRelation pr = new PubRelation();
        List<CmBizSingle> fmbiz = new ArrayList<>();
        pr.setObj1Id(id);
        pr.setType("12");
        List<PubRelation> list = iPubRelationService.selectPubRelationList(pr);
        if (!list.isEmpty()) {
            for (PubRelation prtion : list) {
                CmBizSingle cb = cmBizSingleService.selectCmBizSingleById(prtion.getObj2Id());
                if (cb != null) {
                    OgPerson person = iOgPersonService.selectOgPersonById(cb.getApplicantId());
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
        p.setInspectObject("050100");// 检查对象
        p.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }

    /**
     * 选择受理部门树
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
     * 选择审批部门树
     *
     * @param deptId    部门ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = {"/selectCheckDeptTree/{deptId}", "/selectCheckDeptTree/{deptId}/{excludeId}"})
    public String selectCheckDeptTree(@PathVariable("deptId") String deptId,
                                      @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix + "/checkOrgTree";
    }

    /**
     * 加载部门列表树（排除下级）
     */
    @GetMapping("/treeData/{excludeId}")
    @ResponseBody
    public List<Ztree> treeDataExcludeChild(@PathVariable(value = "excludeId", required = false) String excludeId) {
        OgOrg dept = new OgOrg();
        dept.setOrgId(excludeId);
        List<Ztree> ztrees = deptService.selectDeptTreeExcludeChild(dept);
        return ztrees;
    }

    /**
     * 加载受理部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {

        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());
        List<OgOrg> list = new ArrayList<>();
        //如果不是全国中心人员
        if (!"/000000000/010000888/".equals(org.getLevelCode())) {
            OgOrg org1 = new OgOrg();
            org1.setLevelCode("/000000000/010000888/");
            list.addAll(deptService.selectDeptList(org1));
        }
        list.addAll(deptService.selectDeptList(org));
        List<Ztree> ztrees = initZtree(list);
        return ztrees;
    }

    /**
     * 加载审批部门列表树
     */
    @GetMapping("/treeDataCheck")
    @ResponseBody
    public List<Ztree> treeDataCheck() {

        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());
        List<OgOrg> list = new ArrayList<>();
        list.addAll(deptService.selectDeptList(org));
        List<Ztree> ztrees = initZtree(list);
        return ztrees;
    }

    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<OgOrg> deptList) {
        return initZtree(deptList, null);
    }

    /**
     * 对象转部门树
     *
     * @param deptList     部门列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<OgOrg> deptList, List<String> roleDeptList) {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (OgOrg dept : deptList) {
            if (SUCCESS.equals(dept.getInvalidationMark())) {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getOrgId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getOrgName());
                ztree.setTitle(dept.getOrgName());
                if (isCheck) {
                    ztree.setChecked(roleDeptList.contains(dept.getOrgId() + dept.getOrgName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
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
     * 协同受理页面
     *
     * @return
     */
    @GetMapping("/togetherAcceptanceOld")
    public String synergy() {
        return prefix + "/togetherAcceptanceOld";
    }

    /**
     * 协同受理添加页面
     *
     * @return
     */
    @GetMapping("/selectAccount")
    public String selectAccount() {
        return prefix + "/togetherAcceptancePerson";
    }

    /**
     * 查询协同受理人
     */
    @PostMapping("/togetherAcceptancePerson")
    @ResponseBody
    public TableDataInfo togetherAcceptancePerson(CmBizQingqiu cmBizQingqiu) {

        startPage();
        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());
        List<OgOrg> list = new ArrayList<>();
        //如果不是全国中心人员
        if (!"/000000000/010000888/".equals(org.getLevelCode())) {
            OgOrg org1 = new OgOrg();
            org1.setLevelCode("/000000000/010000888/");
            list.addAll(deptService.selectDeptList(org1));
        }
        list.addAll(deptService.selectDeptList(org));
        String ids = "";
        for (OgOrg org1 : list) {
            ids += org1.getOrgId() + ",";
        }
        cmBizQingqiu.setTogetherAcceptanceOrgIds(ids.split(","));
        return getDataTable(cmBizQingqiuService.togetherAcceptancePerson(cmBizQingqiu));
    }

    /**
     *协同处理
     * @param ccAuthorText
     * @param userId
     * @param businessKey
     * @return
     */
    @PostMapping("/xtAcceptOld")
    @ResponseBody
    @Transactional
    public AjaxResult xtAcceptOld(String ccAuthorText,String userId,String businessKey){
        if (userId.length() > 0) {
            try {
                Map<String, Object> reMap = new HashMap<>();
                reMap.put("businessKey", businessKey);
                reMap.put("comment", ccAuthorText);
                reMap.put("dealId", userId);
                reMap.put("processDefinitionKey", "BGQQ");
                reMap.put("activiti", "BGQQ");
                reMap.put("taskName", "添加协同受理人");
                reMap.put("userId",ShiroUtils.getUserId());
                activitiCommService.insertUser(reMap);//添加协同受理人
                CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(businessKey);
                String text = "变更请求单号：" + cmBizQingqiu.getChangeCode() + "需要您帮忙协助处理，请登录运维管理平台查看";
                String[] pIds = userId.split(",");
                Thread.sleep(2000);
                try {
                    activitiCommService.usersComplete(reMap);//当前变更单受理人受理
                    CmBizQingqiu qingqiu = new CmBizQingqiu();
                    qingqiu.setChangeId(cmBizQingqiu.getChangeId());
                    String dealIdList = cmBizQingqiu.getDealIdList();
                    qingqiu.setDealIdList(dealIdList + ShiroUtils.getUserId() + ",");
                    cmBizQingqiuService.updateCmBizQingqiu(qingqiu);
                } catch (Exception e) {
                    throw new BusinessException("变更请求单操作失败，请刷新列表后重新操作。");
                }
                for (int i = 0 ; i < pIds.length ; i ++) {
                    OgPerson person = iOgPersonService.selectOgPersonById(pIds[i]);// 获取短信接收人
                    sendSms(text,person);
                }
                return AjaxResult.success("操作成功");
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("操作失败");
            }
        }
        return  AjaxResult.error("请选择协同受理人");
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
        CmBizQingqiu cmBizQingqiu = cmBizQingqiuService.selectCmBizQingqiuById(id);
        String userId = ShiroUtils.getUserId();
        if (cmBizQingqiu != null) {
            if (userId.equals(cmBizQingqiu.getApplicantId())) {
                if ("0300".equals(cmBizQingqiu.getStauts())) {
                    try {
                        activitiCommService.revokeCreateTask(id, userId, "BGQQ");
                        cmBizQingqiu.setStauts("0100");
                        cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu);
                        OgPerson checker = iOgPersonService.selectOgPersonById(cmBizQingqiu.getImplementorId());
                        String text = "变更请求单号：" + cmBizQingqiu.getChangeCode() + "，标题：" + cmBizQingqiu.getChangeSingleName() + "，已由发起人" + cmBizQingqiu.getApplyName() + "撤回，请忽略该变更请求单的审批，此条短信不用回复。";
                        sendSms(text, checker);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if ("0600".equals(cmBizQingqiu.getStauts())) {
                    try {
                        activitiCommService.revokeCreateTask(id, userId, "BGQQ");
                        cmBizQingqiu.setStauts("0100");
                        cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu);
                        OgPerson funChecker = iOgPersonService.selectOgPersonById(cmBizQingqiu.getFucheckerId());
                        String text = "变更请求单号：" + cmBizQingqiu.getChangeCode() + "，标题：" + cmBizQingqiu.getChangeSingleName() + "，已由发起人" + cmBizQingqiu.getApplyName() + "撤回，请忽略该变更请求单的审批，此条短信不用回复。";
                        sendSms(text, funChecker);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if ("0400".equals(cmBizQingqiu.getStauts())) {
                    try {
                        Task task=activitiCommService.getTask(id,"受理变更请求");
                        task.getTaskLocalVariables();
                        //查询任务候选人
                        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(task.getId());
                        for(IdentityLink idk: identityLinkList){
                            String implementorId = idk.getUserId();
                            OgPerson person = iOgPersonService.selectOgPersonById(implementorId);
                            String text = "变更请求单号：" + cmBizQingqiu.getChangeCode() + "，标题：" + cmBizQingqiu.getChangeSingleName() + "，已由发起人" + cmBizQingqiu.getApplyName() + "撤回，请忽略该变更请求单的受理，此条短信不用回复。";
                            sendSms(text, person);
                        }
                        activitiCommService.revokeCreateTask(id, userId, "BGQQ");
                        cmBizQingqiu.setStauts("0100");
                        cmBizQingqiuService.updateCmBizQingqiu(cmBizQingqiu);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    return error("当前变更单状态不可撤回。");
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
     * 以下为资源变更单相关操作
     */
    /**
     * 【新建暂存资源变更单】
     */
    @Log(title = "【暂存变更单】", businessType = BusinessType.INSERT)
    @PostMapping("/addCmbiz")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult addSaveCmbiz(CmBizSingle cmBizSingle) {
        cmBizSingle.setExpectStartTime(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectStartTime()));
        cmBizSingle.setExpectEndTime(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectEndTime()));
        OgUser u = ShiroUtils.getOgUser();
        cmBizSingle.setApplicantId(u.getpId());//变更申请人
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
            att2.setAutomateStatus("0");
            List<Attachment> list2 = pubAttachmentService.selectAttachmentList(att2);
            if (list2.isEmpty()) {
                throw new BusinessException("未发送网络自动化步骤excl给自动化平台，无法提交变更。");
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
        reMap.put("userId", u.getpId());//流程发起人
        reMap.put("createId", u.getpId());//流程发起人
        reMap.put("assessorId", cmBizSingle.getChangeManagerId());//下一步处理人[评估人]
        reMap.put("checkId", cmBizSingle.getCheckerId());//审核人放进流程中
        cmBizSingle.setChangeSingleStauts("0300");//待评估状态
        //计算是否紧急变更
        List<PubParaValue> pp = iPubParaValueService.selectPubParaValueByParaName("Cm_Day");//拿到参数变更紧急规则天数
        String StartTime2 = cmBizSingle.getExpectStartTime();//拿到变更开始时间
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        long day1 = 0;
        try {
            Date d2 = fmt.parse(StartTime2);
            Date d1 = fmt.parse(StringUtils.isNotEmpty(cmBizSingle.getCreatetime()) ? cmBizSingle.getCreatetime() : DateUtils.dateTimeNow());
            Long l = d2.getTime() - d1.getTime();
            day1 = l / 1000 / 3600 / 24;
        } catch (Exception e) {
            logger.info("计算紧急变更日期转换失败。");
        }
        if ("1".equals(cmBizSingle.getChangeType())) {//类型为重大变更
            int day2 = Integer.parseInt(pp.get(1).getValue());
            if (day1 >= day2) {//如果计划开始时间大于参数规定天数
                cmBizSingle.setIsJjbg("0");
            } else {
                cmBizSingle.setIsJjbg("1");
            }
        } else {
            int day2 = Integer.parseInt(pp.get(0).getValue());
            if (day1 >= day2) {//如果计划开始时间大于参数规定天数
                cmBizSingle.setIsJjbg("0");
            } else {
                cmBizSingle.setIsJjbg("1");
            }
        }
        try {
            if (i == 1) {
                cmBizSingleService.updateCmBizSingle(cmBizSingle);
                reMap.put("processDefinitionKey", "CmZy");
                reMap.put("businessKey", cmBizSingle.getChangeId());
                reMap.put("reCode", "0");//流程走向[待评估]
                activitiCommService.complete(reMap);
            } else {
                List<ProcessInstance> pro = activitiCommService.processInfo(cmBizSingle.getChangeId());
                if (StringUtils.isNotEmpty(pro)) {
                    throw new BusinessException("该变更单已有流程启动。");
                }
                if (i == 0) {
                    cmBizSingleService.insertCmBizSingle(cmBizSingle);
                    activitiCommService.startProcess(cmBizSingle.getChangeId(), "CmZy", reMap);
                } else {
                    cmBizSingle.setCreatetime(DateUtils.dateTimeNow());
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
     * 选择审批部门树(资源变更单)
     *
     * @param deptId    部门ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = {"/selectCheckDeptTreeForCmbizSingle/{deptId}", "/selectCheckDeptTreeForCmbizSingle/{deptId}/{excludeId}"})
    public String selectCheckDeptTreeForCmbizSingle(@PathVariable("deptId") String deptId,
                                                    @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix + "/checkOrgTreeForCmbizSingle";
    }

    /**
     * 加载审批部门列表树(资源变更单)
     */
    @GetMapping("/treeDataCheckForCmbizSingle")
    @ResponseBody
    public List<Ztree> treeDataCheckForCmbizSingle() {

        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());
        List<OgOrg> list = new ArrayList<>();
        //如果不是全国中心人员
        if (!"/000000000/010000888/".equals(org.getLevelCode())) {
            list.addAll(deptService.selectDeptList(org));
        } else {
            org = null;
            list.addAll(deptService.selectDeptList(org));
        }
        List<Ztree> ztrees = initZtree(list);
        return ztrees;
    }
}
