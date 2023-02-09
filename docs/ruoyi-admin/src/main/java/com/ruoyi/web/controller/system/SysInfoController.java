package com.ruoyi.web.controller.system;

import com.ruoyi.activiti.constants.FmDdConstants;
import com.ruoyi.activiti.constants.XxZdConstants;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.DeleteRea;
import com.ruoyi.common.enums.InfoStateList;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysInfoService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 信息管理信息
 * @author Mr.Xy
 */
@Controller
@RequestMapping("/system/info")
public class SysInfoController extends BaseController {
    private String prefix = "system/info";

    @Autowired
    private ISysInfoService infoService;
    @Autowired
    private IOgPersonService iOgPersonService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private ISysDeptService deptService;

    @GetMapping()
    public String info(ModelMap mmap)
    {
        List stateOfList = InfoStateList.getList();
        List deleList = DeleteRea.getList();
        mmap.addAttribute("stateOfList",stateOfList);
        mmap.addAttribute("deleList",deleList);
        return prefix + "/info";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysInfo info) {
        //根据目录树ID回显计划表信息
        SysFolder sysFolder = infoService.selectTreeById(info.getFolder_());
        if(sysFolder!=null){
            info.setFolder_(sysFolder.getId_());
        }else {
            info.setFolder_("1");
        }
        startPage();

        List<SysInfo> list = infoService.selectInfoList(info);
        return getDataTable(list);
    }


    /**
     * 信息制度管理新增
     */
    @GetMapping("/add/{selecttreeId}")
    public String add(@PathVariable("selecttreeId") String selecttreeId, ModelMap mmap) {
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person= iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = person.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        String levelCode = ogOrg.getLevelCode();
        //审核人
        List<OgPerson> checkList = iOgPersonService.selectListByLevelCode(levelCode, XxZdConstants.auditRole);
        mmap.put("checkList", checkList);

        SysFolder folder = infoService.selectTreeById(selecttreeId);
        mmap.addAttribute("folder", folder.getId_());
        mmap.addAttribute("checkerList",checkList);
        mmap.put("commit_id", pId);
        mmap.put("createName", person.getpName());
        return prefix + "/add";
    }

    /**
     * 附件添加
     * @return
     */
    @Log(title = "附件添加", businessType = BusinessType.INSERT)
    @PostMapping("/fileSave")
    @ResponseBody
    public AjaxResult saveDesc(SysInfo info){
        try {
            if (StringUtils.isEmpty(info.getRegime_info_id())) {
                //判断信息制度名称是否唯一
                SysInfo sysInfo = new SysInfo();
                List<SysInfo> list = infoService.selectInfoList(sysInfo);
                for (SysInfo sinfo : list) {
                    if(StringUtils.isNotEmpty(sinfo.getRegime_title())){
                        if (sinfo.getRegime_title().equals(info.getRegime_title())) {
                            return AjaxResult.error("标题已经存在，请重新检查！");
                        }
                    }
                }
                info.setPrint_time(DateUtils.handleTimeYYYYMMDDHHMMSS(info.getPrint_time()));
                info.setCommit_time(DateUtils.handleTimeYYYYMMDDHHMMSS(info.getCommit_time()));
                info.setCommit_id(ShiroUtils.getOgUser().getUserId());
                info.setRegime_info_id(UUID.getUUIDStr());
                info.setCurrent_state("3");//新增状态
                info.setIs_delete("0");
                infoService.insertInfo(info);
                return AjaxResult.success("操作成功", info.getRegime_info_id());

            } else {
                infoService.updateInfo(info);
                return AjaxResult.success("操作成功", info.getRegime_info_id());
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("暂存失败");
        }




    }

    /**
     * 新增保存
     */
    @Log(title = "信息制度添加管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysInfo info) {
        info.setPrint_time(DateUtils.handleTimeYYYYMMDDHHMMSS(info.getPrint_time()));
        info.setCommit_time(DateUtils.handleTimeYYYYMMDDHHMMSS(info.getCommit_time()));
        info.setCommit_id(ShiroUtils.getOgUser().getUserId());
        info.setIs_delete("0");//逻辑删除默认为0

        //判断当前是否为暂存还是提交状态
        if("commit".equals(info.getLabel())) {
            info.setCurrent_state("9");//待审核

            try {
                if (StringUtils.isEmpty(info.getRegime_info_id())) {
                    //判断信息制度名称是否唯一
                    SysInfo sysInfo = new SysInfo();
                    List<SysInfo> list = infoService.selectInfoList(sysInfo);
                    for (SysInfo sinfo : list) {
                        if(StringUtils.isNotEmpty(sinfo.getRegime_title())){
                            if (sinfo.getRegime_title().equals(info.getRegime_title())) {
                                return AjaxResult.error("标题已经存在，请重新检查！");
                            }
                        }
                    }
                    String regimeInfoId = UUID.getUUIDStr();
                    info.setRegime_info_id(regimeInfoId);
                    infoService.insertInfo(info);
                } else {
                    infoService.updateInfo(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("信息制度新增失败 " + e.getMessage());
                throw new BusinessException("信息制度新增失败,请刷新页面进行重试");
            }
            return AjaxResult.success("操作成功");
        }
        try{
            info.setCurrent_state("3");
            if (StringUtils.isEmpty(info.getRegime_info_id())) {
                //判断信息制度名称是否唯一
                SysInfo sysInfo = new SysInfo();
                List<SysInfo> list = infoService.selectInfoList(sysInfo);
                for (SysInfo sinfo : list) {
                    if(StringUtils.isNotEmpty(sinfo.getRegime_title())){
                        if (sinfo.getRegime_title().equals(info.getRegime_title())) {
                            return AjaxResult.error("标题已经存在，请重新检查！");
                        }
                    }
                }
                info.setRegime_info_id(UUID.getUUIDStr());
                infoService.insertInfo(info);
            } else {
                infoService.updateInfo(info);
            }
        }catch (Exception e){
            logger.error("信息制度暂存失败: "+e.getMessage());
            return AjaxResult.error("信息制度暂存失败");
        }

        return AjaxResult.success("操作成功");

    }
    /**
     * 修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person= iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = person.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        String levelCode = ogOrg.getLevelCode();
        //审核人
        List<OgPerson> checkList = iOgPersonService.selectListByLevelCode(levelCode, XxZdConstants.auditRole);
        if(checkList !=null){
            mmap.addAttribute("checkerList",checkList);
        }

        SysInfo info = infoService.selectInfoById(id);
        mmap.put("info",info );
        if(StringUtils.isNotEmpty(info.getPrint_time())){
            info.setPrint_time(DateUtils.timeMillis(info.getPrint_time()));
        }
        if(StringUtils.isNotEmpty(info.getCommit_time())){
            info.setCommit_time(DateUtils.timeToTimeMillis(info.getCommit_time()));
        }
        return prefix + "/edit";
    }

    /**
     * 修改保存
     * @param info
     * @return
     */
    @Log(title = "信息制度管理修改", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave( SysInfo info)
    {
        if(StringUtils.isNotEmpty(info.getPrint_time())){
            info.setPrint_time(DateUtils.handleTimeYYYYMMDDHHMMSS(info.getPrint_time()));
        }
        if(StringUtils.isNotEmpty(info.getCommit_id())){
            info.setCommit_time(DateUtils.handleTimeYYYYMMDDHHMMSS(info.getCommit_time()));
        }
        info.setCurrent_state("9");//待审核
        try{
            infoService.updateInfo(info);
        }catch (Exception e){
            logger.error("信息制度管理修改失败 "+e.getMessage());
            throw  new BusinessException("信息制度管理修改失败,请刷新页面进行重试");
        }
        return AjaxResult.success("操作成功");

    }


    /**
     * 查看详情
     */
    @GetMapping("/detail/{regime_info_id}")
    public String detail(@PathVariable("regime_info_id") String regime_info_id, ModelMap mmap)
    {
        SysInfo info = infoService.selectInfoById(regime_info_id);
        if(StringUtils.isNotEmpty(info.getPrint_time())){
            info.setPrint_time(DateUtils.timeMillis(info.getPrint_time()));
        }
        if(StringUtils.isNotEmpty(info.getCommit_time())){
            info.setCommit_time(DateUtils.timeToTimeMillis(info.getCommit_time()));
        }
        if(StringUtils.isNotEmpty(info.getDelete_time())){
            info.setDelete_time(DateUtils.timeToTimeMillis(info.getDelete_time()));
        }
        OgPerson userPerson = iOgPersonService.selectOgPersonById(info.getCommit_id());
        if(userPerson != null){
            String createrName = userPerson.getpName();
            mmap.addAttribute("commit_id", createrName);
        }
        mmap.put("info",info );

        return prefix + "/detail";
    }



    //删除
    @GetMapping("/remove/{regime_info_id}")
    public String remove(@PathVariable("regime_info_id") String regime_info_id, ModelMap mmap) {
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person= iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = person.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        String levelCode = ogOrg.getLevelCode();
        //审核人
        List<OgPerson> checkList = iOgPersonService.selectListByLevelCode(levelCode, XxZdConstants.auditRole);
        if(checkList !=null){
            mmap.addAttribute("checkerList",checkList);
        }

        SysInfo info = infoService.selectInfoById(regime_info_id);
        mmap.put("info", info);
        mmap.addAttribute("mark",info.getMark());
        mmap.addAttribute("regime_info_id",regime_info_id);
        mmap.addAttribute("regime_digest",info.getRegime_digest());
        return prefix + "/remove";
    }

    /**
     *  删除保存
     */
    @Log(title = "信息制度管理", businessType = BusinessType.UPDATE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult deleteSave(@Validated SysInfo info)
    {
        info.setCurrent_state("7");//状态变为待删除
        return toAjax(infoService.updateInfo(info));
    }


    //进入废止页面
    @GetMapping("/del/{regime_info_id}")
    public String del(@PathVariable("regime_info_id") String regime_info_id, ModelMap mmap) {
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person= iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = person.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        String levelCode = ogOrg.getLevelCode();
        //审核人
        List<OgPerson> checkList = iOgPersonService.selectListByLevelCode(levelCode, XxZdConstants.auditRole);
        if(checkList !=null){
            mmap.addAttribute("checkerList",checkList);
        }
        SysInfo info = infoService.selectInfoById(regime_info_id);
        mmap.put("info", info);
        mmap.addAttribute("regime_digest",info.getRegime_digest());
        mmap.addAttribute("regime_info_id",regime_info_id);
        return prefix + "/del";
    }

    /**
     * 废止保存
     */
    @Log(title = "废止信息制度管理", businessType = BusinessType.UPDATE)
    @PostMapping("/del")
    @ResponseBody
    public AjaxResult delSave(@Validated SysInfo info)
    {
        info.setCurrent_state("8");
        info.setDelete_time(DateUtils.handleTimeYYYYMMDDHHMMSS(info.getDelete_time()));
        return toAjax(infoService.updateInfo(info));
    }



    /**
     * 信息制度管理审
     */
    @GetMapping("/audit")
    public String audit(SysInfo info, ModelMap mmap) {
        List deleList = DeleteRea.getList();
        OgPerson userPerson = iOgPersonService.selectOgPersonById(info.getCommit_id());
        if(userPerson != null){
            String createrName = userPerson.getpName();
            mmap.addAttribute("commit_id", createrName);
        }
        mmap.addAttribute("deleList",deleList);

        return prefix + "/audit";
    }


    /**
     * 审核页面
     * @param info
     * @return
     */
    @PostMapping("/auditlist")
    @ResponseBody
    public TableDataInfo auditlist(SysInfo info) {
        //根据目录树ID回显计划表信息
        SysFolder sysFolder = infoService.selectTreeById(info.getFolder_());
        if(sysFolder!=null){
            info.setFolder_(sysFolder.getId_());
        }else {
            info.setFolder_("1");
        }
        startPage();
        //获取当前登录人id
        String pId = ShiroUtils.getOgUser().getpId();
        //审核页面查询
        List<SysInfo> list = infoService.selectAuditList(info);
        //过滤条件
        List<SysInfo> list1 = list.stream().filter((item) -> {
            return item.getChecker().equals(pId);
        }).collect(Collectors.toList());

        return getDataTable(list1);
    }

    /**
     * 审核按钮点击跳转
     */
    @GetMapping("/auditing/{regime_info_id}")
    public String audit(@PathVariable("regime_info_id") String regime_info_id, ModelMap mmap) {
        SysInfo info = infoService.selectInfoById(regime_info_id);
        if(StringUtils.isNotEmpty(info.getPrint_time())){
            info.setPrint_time(DateUtils.timeMillis(info.getPrint_time()));
        }
        if(StringUtils.isNotEmpty(info.getCommit_time())){
            info.setCommit_time(DateUtils.timeToTimeMillis(info.getCommit_time()));
        }
        if(StringUtils.isNotEmpty(info.getDelete_time())){
            info.setDelete_time(DateUtils.timeToTimeMillis(info.getDelete_time()));
        }
        mmap.put("info", info);
        OgPerson userPerson = iOgPersonService.selectOgPersonById(info.getCommit_id());
        if(userPerson != null){
            String createrName = userPerson.getpName();
            mmap.addAttribute("commit_id", createrName);
        }
        mmap.put("current_state",info.getCurrent_state());
        return prefix + "/auditing";
    }


    /**
     *审核通过
     * @param info
     * @return
     */
    @Log(title = "信息制度管理审核通过", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    @ResponseBody
    public AjaxResult auditSave(@Validated SysInfo info)
    {
        //根据id查询信息
        SysInfo sysInfo = infoService.selectInfoById(info.getRegime_info_id());
        //状态为待审核：
        if(("9").equals(info.getCurrent_state())){
            info.setCurrent_state(sysInfo.getRegime_info_type());
        }else if(("8").equals(info.getCurrent_state())){
            //判断传进来的废止原因
            if("1".equals(sysInfo.getDelete_reason())){
                info.setCurrent_state("5");
            }else if("2".equals(sysInfo.getDelete_reason())){
                info.setCurrent_state("4");
            }
        }else if(("7").equals(info.getCurrent_state())){
            info.setIs_delete("1");

        }
        return toAjax(infoService.updateInfo(info));
    }

    /**
     *审核bu通过
     * @param info
     * @return
     */
    @Log(title = "信息制度管理审核不通过", businessType = BusinessType.UPDATE)
    @PostMapping("/noPass")
    @ResponseBody
    public AjaxResult noPass(@Validated SysInfo info)
    {
        //不通过的话状态为退回待审核
        info.setCurrent_state("6");
        return toAjax(infoService.updateInfo(info));
    }


    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = infoService.selectFolderTree(new SysFolder());
        return ztrees;
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkInfoNameUnique")
    @ResponseBody
    public String checkInfoNameUnique(SysInfo info) {
        return infoService.checkInfoNameUnique(info);
    }


    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree() {
        return prefix + "/tree";
    }

    /**
     * 选择部门树
     */
    @GetMapping(value = { "/selectFolder/{folder}"})
    public String selectInfoTree(@PathVariable("folder") String folder,ModelMap mmap)
    {
        return prefix + "/foldertree";
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
        SysInfo info = infoService.selectInfoById(id);
        ajaxResult.put("data",info);
        return  ajaxResult;
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

}