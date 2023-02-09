package com.ruoyi.activiti.controller.itsm.dispatch;


import com.ruoyi.activiti.constants.FmDdConstants;
import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.ICmBizSingleService;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.activiti.service.FmDispatchService;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.utils.uuid.UUID;

import java.util.*;

/**
 * 我的调度请求
 */

@Controller
@RequestMapping("/dispatch/mydispatch")
@Transactional(rollbackFor = Exception.class)
public class FmDispatchController extends BaseController {

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FmDispatchController.class);


    @Autowired
    IOgPersonService iOgPersonService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    private FmDispatchService dispatchService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private IPubRelationService iPubRelationService;

    @Autowired
    private ICmBizSingleService iCmBizSingleService;

    private String prefix = "dispatch/mydispatch";



    /**
     * 转到 我的调度请求 页面
     * @return
     */
    @GetMapping()
    public String mydispatch(ModelMap map)
    {
        map.put("user", ShiroUtils.getOgUser());
        return prefix + "/mydispatch";
    }


    /**
     * 列表展示
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmDd fmDd)
    {


        startPage();
        //日期格式转换
        if(StringUtils.isNotEmpty(fmDd.getCreateTime())) {
            fmDd.setCreateTime(handleTimeYYYYMMDDHHMMSS(fmDd.getCreateTime()));
        }
        if(StringUtils.isNotEmpty(fmDd.getEndCreateTime())) {
            fmDd.setEndCreateTime(handleTimeYYYYMMDDHHMMSS(fmDd.getEndCreateTime()));
        }
        if(StringUtils.isNotEmpty(fmDd.getPlanTime())) {
            fmDd.setPlanTime(handleTimeYYYYMMDDHHMMSS(fmDd.getPlanTime()));
        }
        if(StringUtils.isNotEmpty(fmDd.getEndPlanTime())) {
            fmDd.setEndPlanTime(handleTimeYYYYMMDDHHMMSS(fmDd.getEndPlanTime()));
        }

        List<FmDd> fmDbListm = dispatchService.selectDispatchList(fmDd);
        return getDataTable(fmDbListm);


    }

    /**
     * 查看详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap map)
    {
        //获取页面的信息
        FmDd fmDd = dispatchService.selectFmddById(id);
        if(fmDd !=null){
            map.put("fmdd",fmDd);

            //获取审核人信息
            OgPerson person = ogPersonService.selectOgPersonById(fmDd.getCheckerId());
            if(person !=null){
                map.put("pname",person.getpName());

            }

            //创建时间进行日期回显
            String createTime = fmDd.getCreateTime();
            if(StringUtils.isNotEmpty(createTime)){
                fmDd.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            }

            //计划操作时间进行日期回显
            String planTime = fmDd.getPlanTime();
            if(StringUtils.isNotEmpty(planTime)){
                fmDd.setPlanTime(DateUtils.formatDateStr(planTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            }
        }


        return prefix + "/detail";
    }




    /**
     * 转到添加页面
     */
    @GetMapping("/add")
    public String add(ModelMap modelMap)
    {
        String bizType = "SJDD";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        modelMap.put("num", bizType + nowDateStr + numStr);

        //打开新建页面回显创建机构填报人及创建机构
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person= iOgPersonService.selectOgPersonById(pId);
        if(person != null){
            OgOrg o= iSysDeptService.selectDeptById(person.getOrgId());
            String orgName= o.getOrgName();
            modelMap.put("orgName", orgName);
            modelMap.put("createName", person.getpName());
            //获取机构ID
            String orgId = person.getOrgId();
            //当前登陆人的机构信息
            OgOrg ogOrg = deptService.selectDeptById(orgId);
            modelMap.put("ogOrg", ogOrg);
            String levelCode = ogOrg.getLevelCode();
            //审核人
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, FmDdConstants.auditRole);
            if(StringUtils.isNotEmpty(checkList)){
                modelMap.put("checkList", checkList);
            }
        }
        //路径回显

        modelMap.put("createId", pId);
        modelMap.put("createTime", DateUtils.getTime());
        modelMap.put("fmddId",UUID.getUUIDStr());

        return prefix + "/add";
    }


    /**
     * 添加保存
     */
    @Log(title = "调度请求单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult ddsave( FmDd fmDd)
    {

        Map<String, Object> reMap=new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());

        OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        fmDd.setCreateTime(fmDd.getCreateTime());
        fmDd.setCreateorgId(ogPerson.getOrgId());
        fmDd.setInvalidationMark("1");
        //判断当前是否为暂存还是提交状态
        if("commit".equals(fmDd.getLabel())){
            String businessKey=fmDd.getFmddId();
            String processDefinitionKey="FmDd";
            if(StringUtils.isNotEmpty(fmDd.getCheckerId())){//待审核
                reMap.put("reCode","0");
                reMap.put("createId",fmDd.getCreateId());
                reMap.put("checkId",fmDd.getCheckerId());
            }
            try{
                if (StringUtils.isEmpty(fmDd.getFmddId())) {
                    String fmDdId = UUID.getUUIDStr();
                    businessKey = fmDdId;
                    fmDd.setFmddId(fmDdId);
                    dispatchService.insertDispatch(fmDd);
                } else {
                    dispatchService.updateDispatch(fmDd);
                }
                activitiCommService.startProcess(businessKey,processDefinitionKey,reMap);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("调度请求单新增失败 "+e.getMessage());
                throw  new BusinessException("调度请求单新增失败,请刷新页面进行重试");
            }
            return AjaxResult.success("操作成功");

        }
        try{
            if (StringUtils.isEmpty(fmDd.getFmddId())) {
                fmDd.setFmddId(UUID.getUUIDStr());
                dispatchService.insertDispatch(fmDd);
            } else {
                dispatchService.updateDispatch(fmDd);
            }
        }catch (Exception e){
            log.error("调度请求单暂存失败: "+e.getMessage());
            return AjaxResult.error("调度请求单暂存失败");
        }

        return AjaxResult.success("调度请求单暂存成功");

    }



    /**
     * 转到修改页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String  id, ModelMap map)
    {
        //获取页面的信息
        FmDd fmDd = dispatchService.selectFmddById(id);
        if(fmDd != null){
            map.put("fmdd",fmDd);
            //获取当前节点对象的信息
            OgOrg ogOrg = deptService.selectDeptById(fmDd.getCreateorgId());
            if(ogOrg != null){
                String levelCode = ogOrg.getLevelCode();
                //审核人
                List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode, FmDdConstants.auditRole);
                if(StringUtils.isNotEmpty(checkList)){
                    map.put("checkList", checkList);

                }
            }

        //创建时间进行日期回显
        String createTime = fmDd.getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            fmDd.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //计划操作时间进行日期回显
        String planTime = fmDd.getPlanTime();
        if(StringUtils.isNotEmpty(planTime)){
            fmDd.setPlanTime(DateUtils.formatDateStr(planTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        }
        //状态为省内退回与全国中心退回进入作废修改页面
        if("2".equals(fmDd.getCurrentState()) || "3".equals(fmDd.getCurrentState()) ){
            return prefix + "/zfedit";
        }else {
            return prefix + "/edit";
        }
    }


    /**
     * 修改保存调度请求
     *
     */
    @Log(title = "我的调度请求单修改", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave( FmDd fmDd)
    {

        Map<String,Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());

        //判断当前是否为暂存还是提交状态
        if("commit".equals(fmDd.getLabel())){
            String businessKey=fmDd.getFmddId();
            String processDefinitionKey="FmDd";
            if(StringUtils.isNotEmpty(fmDd.getCheckerId())){//待审核
                reMap.put("reCode","0");
                reMap.put("checkId",fmDd.getCheckerId());
            }
            try{
                dispatchService.updateDispatch(fmDd);
                activitiCommService.startProcess(businessKey,processDefinitionKey,reMap);
            }catch (Exception e){
                logger.error("调度请求单修改失败 "+e.getMessage());
                throw  new BusinessException("调度请求单修改失败,请刷新页面进行重试");
            }
            return AjaxResult.success("操作成功");
        }

        try{
            dispatchService.updateDispatch(fmDd);
        }catch (Exception e){
            log.error("调度请求单暂存失败: "+e.getMessage());
            return AjaxResult.error("调度请求单暂存失败");
        }

        return AjaxResult.success("调度请求单暂存成功");

    }


    /**
     * 作废之后修改保存调度请求
     *
     */
    @Log(title = "我的调度请求单作废修改", businessType = BusinessType.UPDATE)
    @PostMapping("/zfedit")
    @ResponseBody
    public AjaxResult editZfSave( FmDd fmDd)
    {

        Map<String,Object> reMap = new HashMap<>();
        reMap.put("userId", ShiroUtils.getUserId());

        //判断当前是否为作废还是提交状态
        if("commit".equals(fmDd.getLabel())){
            if(StringUtils.isNotEmpty(fmDd.getCheckerId())){//待审核
                reMap.put("reCode","0");
                reMap.put("checkId",fmDd.getCheckerId());
            }
        }else {
            // 作废
            reMap.put("reCode", "1");
        }

        String businessKey = fmDd.getFmddId();
        reMap.put("businessKey",businessKey);
        String processDefinitionKey = "FmDd";
        reMap.put("processDefinitionKey", processDefinitionKey);
        activitiCommService.complete(reMap);

        return toAjax(dispatchService.updateDispatch(fmDd));
    }


    /**
     * 删除调度请求
     */
    @Log(title = "调度请求", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {

        return toAjax(dispatchService.deleteFmDdByIds(ids));
    }

    /**
     * 查询id
     * @param id
     * @return
     */
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        FmDd fmDd = dispatchService.selectFmddById(id);
        if(fmDd != null){
            ajaxResult.put("data",fmDd);

        }
        return  ajaxResult;
    }


    /**
     * 导出调度请求
     * @param
     * @return
     */
    @Log(title = "调度请求单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmDd fmDd)
    {
        //获取当前登录人
        String userid = ShiroUtils.getUserId();
        if(StringUtils.isNotEmpty(userid)){
            fmDd.setCreateId(userid);
        }
        String isCurrentPage = (String)fmDd.getParams().get("currentPage");
        if("currentPage".equals(isCurrentPage)){
            startPage();
            List<FmDd> list = dispatchService.selectDispatchList(fmDd);
            ExcelUtil<FmDd> util = new ExcelUtil<FmDd>(FmDd.class);
            return util.exportExcel(list, "调度请求单");
        }else {
            List<FmDd> list = dispatchService.selectDispatchList(fmDd);
            ExcelUtil<FmDd> util = new ExcelUtil<FmDd>(FmDd.class);
            return util.exportExcel(list, "调度请求单");
        }
    }


    /**
     * 查看流程
     */
    @GetMapping("/selectProcess/{id}")
    public String selectProcess(@PathVariable("id") String id, ModelMap mmap) {
        FmDd fmDd = dispatchService.selectFmddById(id);
        if(fmDd != null){
            mmap.put("fmDd", fmDd);

        }
        return prefix + "/processDetail";
    }


    /**
     * 根据调度请求id查询对应关联的资源变更单
     * @param id
     * @return
     */
    @PostMapping("/cmBizSingleList/{id}")
    @ResponseBody
    public TableDataInfo getCmBizSingleList(@PathVariable("id") String id) {
        PubRelation pr = new PubRelation();
        List<CmBizSingle>  cmBizSingles = new ArrayList<>();
        pr.setObj1Id(id);
        pr.setType("07");
        List<PubRelation> list = iPubRelationService.selectPubRelationList(pr);
        if (!list.isEmpty()) {
            for (PubRelation prtion : list) {
                CmBizSingle cmBizSingle = iCmBizSingleService.selectCmBizSingleById(prtion.getObj2Id());
                if (cmBizSingle != null) {
                    cmBizSingles.add(cmBizSingle);
                }
            }
        }
        return getDataTable(cmBizSingles);
    }


    /**
     * 删除附件
     * @return
     */
    @GetMapping("/selectOgUserByUserId")
    @ResponseBody
    public AjaxResult selectOgUserByUserId(){
        AjaxResult ajaxResult = new AjaxResult();
        String userId = ShiroUtils.getOgUser().getUserId();
        ajaxResult.put("data", ogUserService.selectOgUserByUserId(userId));
        return ajaxResult;
    }


    /**
     * 联系方式校验
     * @param telPhone
     * @return
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public AjaxResult checkPhoneUnique(String telPhone){
        AjaxResult ajaxResult = new AjaxResult();
        FmDd fmDd = dispatchService.checkPhoneUnique(telPhone);
        int flag;
        if(fmDd==null){
            flag = 0;
        }else{
            flag = 1;
        }
        ajaxResult.put("flag",flag);
        return ajaxResult;
    }

    /**
     * 附件暂存
     *
     * @return
     */
    @Log(title = "新增保存", businessType = BusinessType.INSERT)
    @PostMapping("/fileSave")
    @ResponseBody
    public AjaxResult fileSave(FmDd fmDd) {
        try {
            if (StringUtils.isEmpty(fmDd.getFmddId())) {
                OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
                fmDd.setCreateId(ShiroUtils.getOgUser().getpId());
                fmDd.setFmddId(UUID.getUUIDStr());
                fmDd.setCreateTime(fmDd.getCreateTime());
                fmDd.setCreateorgId(ogPerson.getOrgId());
                fmDd.setInvalidationMark("1");
                fmDd.setCurrentState("1");
                dispatchService.insertDispatch(fmDd);
                return AjaxResult.success("操作成功", fmDd.getFmddId());
            } else {
                dispatchService.updateDispatch(fmDd);
                return AjaxResult.success("操作成功", fmDd.getFmddId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("暂存失败，单号是：" + fmDd.getFaultNo());
        }

    }


}
