package com.ruoyi.activiti.controller.itsm.ext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.activiti.domain.TelSupportPeropleExtension;
import com.ruoyi.activiti.service.IOgPersonExtQueryService;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2021-05-19
 */
@Controller
@RequestMapping("/ext/person")
public class OgPersonExtQueryController extends BaseController
{
    private String prefix = "ext/person";

    @Autowired
    private IOgPersonExtQueryService ogPersonExtQueryService;
    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    private IOgPersonService iOgPersonService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private OgPostService ogPostService;

    @GetMapping()
    public String query(ModelMap mmap)
    {
        OgUser ogUser = ShiroUtils.getOgUser();
        String s = ogUser.getpId();
        String userId = ogUser.getUserId();

        OgPerson personByUserId = iOgPersonService.selectRoleByIdTwo(userId);
        if (personByUserId!=null){
            mmap.put("orgIdTwo","");
        }else{
            OgPerson person = iOgPersonService.selectOgPersonById(s);
            String orgId = person.getOrgId();
            mmap.put("orgIdTwo",orgId);
        }
        return prefix + "/person";
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TelSupportPeropleExtension telSupportPeropleExtension)
    {
        /*String type="1";
        //判断当前登陆人是不是管理员
        OgUser ogUser = ShiroUtils.getOgUser();
        String userId = ogUser.getUserId();
        OgPerson person = iOgPersonService.selectRoleByIdTwo(userId);
        List<TelSupportPeropleExtension> list=new ArrayList<TelSupportPeropleExtension>();
        if(person==null){// 非管理员
            String pId = ogUser.getpId();
            OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
            // 查询指定机构
            telSupportPeropleExtension.setOrgid(ogPerson.getOrgId());
            startPage();
           list = ogPersonExtQueryService.selectOgPersonExtQueryList(telSupportPeropleExtension);
        }else{ // 管理员
            startPage();
           list = ogPersonExtQueryService.selectOgPersonExtQueryList(telSupportPeropleExtension);
        }*/
        startPage();
        List<TelSupportPeropleExtension> list = ogPersonExtQueryService.selectOgPersonExtQueryList(telSupportPeropleExtension);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TelSupportPeropleExtension telSupportPeropleExtension)
    {
        List<TelSupportPeropleExtension> list = ogPersonExtQueryService.selectOgPersonExtQueryList(telSupportPeropleExtension);
        ExcelUtil<TelSupportPeropleExtension> util = new ExcelUtil<TelSupportPeropleExtension>(TelSupportPeropleExtension.class);
        return util.exportExcel(list, "query");
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TelSupportPeropleExtension telSupportPeropleExtension)
    {
        telSupportPeropleExtension.setId(UUID.getUUIDStr());
        //String orgid = telSupportPeropleExtension.getOrgid();
        //添加的时候时间自动添加
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        telSupportPeropleExtension.setCreateTime(nowTime);
        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String s = ogUser.getpId();
        OgPerson person = iOgPersonService.selectOgPersonById(s);
        //String userName = ogUser.getUserName();
        telSupportPeropleExtension.setCreatePerson(person.getpName());
        //当前登录人id
        //telSupportPeropleExtension.setCreatePerson(userName);
        //String pId = ogUser.getpId();
        //OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        //telSupportPeropleExtension.setOrgid(ogPerson.getOrgId());
        return toAjax(ogPersonExtQueryService.insertOgPersonExtQuery(telSupportPeropleExtension));
    }

    //选择公司
    @GetMapping("/selectOneApplication")
    public String selectApplication() {
        return prefix + "/selectOneApplication";
    }

    //创建机构选择页面
    @GetMapping("/selectogOrg")
    public String selectogOrg(ModelMap mmap) {
        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String userId = ogUser.getUserId();
        //判断当前登陆人是不是管理员
        OgPerson ogPerson = iOgPersonService.selectOgPersonEvoById(userId);
        List<OgRole> ogRolesList = iSysRoleService.selectRolesByUserId(userId);
        //Java8里面的方法 遍历
        //List<String> ridList=ogRolesList.stream().map(a -> new String(a.getRid())).collect(Collectors.toList());
//        List<Object> stringList = ogRolesList.stream().map(ogRole -> ogRole.getRid()).collect(Collectors.toList());
        List list =  ogRolesList.stream().filter(ogRole -> ogRole.getRid().equals("1202")).collect(Collectors.toList());
        if (list.isEmpty()){
            mmap.put("orgId",ogPerson.getOrgId());
        }
        /*OgPerson ogPerson = iOgPersonService.selectRoleByIdTwo(userId);
        if(ogPerson==null){// 非管理员
            mmap.put("orgId",ogPerson.getOrgId());
        }else{ // 管理员
            mmap.put("orgId",ogPerson.getOrgId());
        }*/
        //mmap.put("orgId",ogPerson.getOrgId());
        return prefix + "/selectOgorg";
    }

    @Autowired
    ISysDeptService iSysDeptService;

    /**
     * 查询机构
     */
    @PostMapping("/selectOrgList")
    @ResponseBody
    public TableDataInfo selectOrgList(OgOrg org) {
        startPage();
        return getDataTable(iSysDeptService.selectDeptListByOrgId(org));
    }

    @Autowired
    private ISysApplicationManagerService applicationManagerService;

    //应急系统的存储
    @RequestMapping("/getAllSys")
    @ResponseBody
    public AjaxResult getAllSys(){
        AjaxResult ajaxResult = new AjaxResult();
        List<OgSys> ogSys = applicationManagerService.selectOgSysList(null);
        List<OgSys> list = ogSys.stream().filter(sys -> "1".equals(sys.getInvalidationMark())).collect(Collectors.toList());
        ajaxResult.put("data",list);
        return ajaxResult;
    }

    //selectListByOrgIdAndRoleId这个方法查询角色的rid和机构的orgid

    //是不是当前公司的
    /*@PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        OgUser ogUser = ShiroUtils.getOgUser();
        String userId = ogUser.getUserId();
        OgPerson person = iOgPersonService.selectRoleByIdTwo(userId);
        if(person==null){
            // 当前登陆人不是管理员 根据机构id进行判断

        }
        String orgId = person.getOrgId();
        *//*String pId = ogUser.getpId();
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        ogPerson.getOrgId();//组织机构
        TelSupportPeropleExtension telSupportPeropleExtension = ogPersonExtQueryService.selectOgPersonExtQueryById(id);
        if(!telSupportPeropleExtension.getOrgid().equals(ogPerson.getOrgId())&& !ogUser.getUsername().equals("admin")){
            ajaxResult.put("flag",1);
        }*//*
        return  ajaxResult;
    }*/

    //查询出所当前用户所在的角色下
    /*@PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectRoleByIdTwo(String roleId)
    {
        OgUser ogUser = ShiroUtils.getOgUser();
        String userId = ogUser.getUserId();
        OgPerson ogPerson = iOgPersonService.selectRoleByIdTwo(userId);
        return  null;
    }*/

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        /*String type="1";
        //判断当前登陆人是不是管理员
        OgUser ogUser = ShiroUtils.getOgUser();
        String userId = ogUser.getUserId();
        OgPerson person = iOgPersonService.selectRoleByIdTwo(userId);
        List<TelSupportPeropleExtension> list=new ArrayList<TelSupportPeropleExtension>();

        if(person==null){// 非管理员
            String pId = ogUser.getpId();
            OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
            // 查询指定机构
            telSupportPeropleExtension.setOrgid(ogPerson.getOrgId());
            startPage();
           list = ogPersonExtQueryService.selectOgPersonExtQueryList(telSupportPeropleExtension);
        }else{ // 管理员
            startPage();
           list = ogPersonExtQueryService.selectOgPersonExtQueryList(telSupportPeropleExtension);
        }*/
        TelSupportPeropleExtension telSupportPeropleExtension = ogPersonExtQueryService.selectOgPersonExtQueryById(id);
        mmap.put("ogPersonExtQuery", telSupportPeropleExtension);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TelSupportPeropleExtension telSupportPeropleExtension)
    {
        //获取当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        String userId = ogUser.getUserId();

        //修改时间回显
        Date nowDate = DateUtils.getNowDate();
        String nowTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowDate);
        telSupportPeropleExtension.setUpdateTime(nowTime);
        String s = ogUser.getpId();
        OgPerson person = iOgPersonService.selectOgPersonById(s);
        //String userName = ogUser.getUserName();
        telSupportPeropleExtension.setUpdatePerson(person.getpName());
        return toAjax(ogPersonExtQueryService.updateOgPersonExtQuery(telSupportPeropleExtension));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/delete")
    @ResponseBody
    public AjaxResult delete(String id)
    {
        return toAjax(ogPersonExtQueryService.deleteOgPersonExtQueryById(id));

    }
}
