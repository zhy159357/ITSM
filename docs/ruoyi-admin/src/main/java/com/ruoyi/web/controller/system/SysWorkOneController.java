package com.ruoyi.web.controller.system;

import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.ISysWorkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作组管理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/work2")
public class SysWorkOneController extends BaseController
{
    private String prefix = "system/work2";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysWorkService workService;

    @Autowired
    private IOgGroupPersonService ogGroupPersonService;

    @Autowired
    private IOgPersonService personService;

    @GetMapping()
    public String work()
    {
        return prefix + "/work";
    }

    @RequiresPermissions("system:work:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgGroup group)
    {
        startPage();
        //当前登录人
        OgUser ogUser = ShiroUtils.getOgUser();
        //中间表
        List<OgGroupPerson> ogGroupPeople = workService.selectOgPersonByIdTwo(ogUser.getpId());
        //新建的list集合
        List<OgGroup> list = new ArrayList();
        for(OgGroupPerson gp:ogGroupPeople){
            OgGroup ogGroup = workService.selectOgGroupById(gp.getGroupId());
            list.add(ogGroup);
        }
        return getDataTable(list);
    }

    @Log(title = "工作组管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUser user)
    {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "工作组数据");
    }

    @Log(title = "工作组管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = ShiroUtils.getSysUser().getLoginName();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("工作组数据");
    }

    /**
     *
     * 新增工作组
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        return prefix + "/add";
    }

    /**
     * 新增保存工作组
     */
    @Log(title = "工作组管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated OgGroup group)
    {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(workService.checkGroupNameUnique(group.getGrpName())))
        {
            return error("新增工作组'" + group.getGrpName() + "'失败，工作组已存在");
        }

        group.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(workService.insertOgGroup(group));
    }

    /**
     * 修改工作组
     */
    @GetMapping("/edit/{groupId}")
    public String edit(@PathVariable("groupId") String groupId, ModelMap mmap)
    {
        mmap.put("group", workService.selectOgGroupById(groupId));
        return prefix + "/edit";
    }
    @GetMapping("/editG/{groupId}")
    public String editG(@PathVariable("groupId") String groupId, ModelMap mmap)
    {
        mmap.put("group", workService.selectOgGroupById(groupId));
        return prefix + "/editleader";
    }

    /**
     * 修改保存工作组
     */
    @Log(title = "工作组管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated OgGroup group)
    {
        /*if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(workService.checkGroupNameUnique(group.getGrpName())))
        {
            return error("修改工作组管理'" + group.getGrpName() + "'失败，工作组名称已存在");
        }*/
        group.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(workService.updateOgGroup(group));
    }

    @Log(title = "工作组管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(workService.deleteOgGroupByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 校验工作组名称
     */
    @PostMapping("/checkGrpNameUnique")
    @ResponseBody
    public String checkPhoneUnique(OgGroup group)
    {
        return workService.checkGroupNameUnique(group.getGrpName());
    }

    /**
     * 工作组状态修改
     */
    @Log(title = "工作组管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysUser user)
    {
        userService.checkUserAllowed(user);
        return toAjax(userService.changeStatus(user));
    }

    /**
     * 增加工作组成员
     */
    @GetMapping("/addGroupPerson/{groupId}")
    public String addGroupPerson(@PathVariable("groupId") String groupId, ModelMap mmap)
    {
        mmap.put("group", workService.selectOgGroupById(groupId));
        return prefix + "/groupPerson";
    }

    /**
     * 保存工作组成员
     */
    @PostMapping("/groupPerson/add/{groupId}")
    public String addGroupPersonSave(@PathVariable("groupId") Long groupId)
    {
        return prefix + "/addGroupPerson";
    }

    @PostMapping("/groupPerson/groupPersonList")
    @ResponseBody
    public TableDataInfo groupPersonList(OgGroupPerson person)
    {
        startPage();
        person.setOrgId(resetOrgId(person.getOrgId(),person.getOrgName()));
        List<OgGroupPerson> list = workService.selectOgGroupPersonList(person);
        return getDataTable(list);
    }

    /**
     * 用户选择页面
     */
    @GetMapping("/groupPerson/selectUser/{groupId}")
    public String selectUser(@PathVariable("groupId") String groupId, ModelMap mmap)
    {
        mmap.put("group", workService.selectOgGroupById(groupId));
        return prefix + "/selectUser";
    }

    /**
     * 查询用户列表
     */
    @PostMapping("/groupPerson/userList")
    @ResponseBody
    public TableDataInfo userList(OgPerson user)
    {
        startPage();
        List<OgPerson> list = workService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 保存用户信息到工作成员
     */
    @PostMapping("/groupPerson/selectAll")
    @ResponseBody
    public AjaxResult saveGroupPerson(String groupId, String pIds)
    {
        return toAjax(workService.insertOgGroupPerson(groupId, pIds));
    }

    /**
     * 修改工作组成员信息（工作组职位、组内顺序）
     */
    @GetMapping("/groupPerson/editGroupPerson/{pid}")
    public String editGroupPerson(@PathVariable("pid") String pid, ModelMap mmap)
    {
        mmap.put("person", workService.selectOgGroupPersonById(pid));
        return prefix + "/editGroupPerson";
    }

    /**
     * 修改保存工作组管理成员信息
     */
    @Log(title = "工作组管理成员信息", businessType = BusinessType.UPDATE)
    @PostMapping("/groupPerson/editGroupPerson")
    @ResponseBody
    public AjaxResult editSaveGroupPerson(@Validated OgGroupPerson person)
    {
        person.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(workService.updateOgGroupPerson(person));
    }

    @Log(title = "工作组管理成员信息", businessType = BusinessType.DELETE)
    @PostMapping("/groupPerson/removeGroupPerson")
    @ResponseBody
    public AjaxResult removeGroupPerson(String ids)
    {
        try
        {
            return toAjax(workService.deleteOgGroupPersonByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    @PostMapping("/selectGroupByUserId")
    @ResponseBody
    public TableDataInfo selectGroupByUserId(String grpName)
    {
        Map map = new HashMap<>();
        map.put("userId",ShiroUtils.getUserId());
        map.put("grpName",grpName);
        map.put("invalidationMark","1");
        startPage();
        List<OgGroup> list = workService.selectGroupByGposition(map);
        if(!CollectionUtils.isEmpty(list)){
            for(OgGroup group : list){
                List<String> strList = new ArrayList<>();
                OgGroupPerson groupPerson = new OgGroupPerson();
                groupPerson.setGroupId(group.getGroupId());
                groupPerson.setGpOsition("1");
                // 查询该工作组的组长
                List<OgGroupPerson> groupPersonList = ogGroupPersonService.selectOgGroupPersonList(groupPerson);
                if(!CollectionUtils.isEmpty(groupPersonList)){
                    for(OgGroupPerson person : groupPersonList){
                        OgPerson ogPerson = personService.selectOgPersonById(person.getPid());
                        if(null != ogPerson){
                            strList.add(ogPerson.getpName());
                        }
                    }
                }
                // 获取该工作组下的所有组长名称赋值到remark字段（用逗号拼接），然后前端显示
                group.setRemark(String.join(",", strList));
            }
        }
        return getDataTable(list);
    }

    //我的运行事件单新增方法获取人员信息
    @PostMapping("/groupPerson/personListByGroupId")
    @ResponseBody
    public AjaxResult personListByGroupId(OgGroupPerson person)
    {
        AjaxResult ajaxResult = new AjaxResult();
        List<OgGroupPerson> list = workService.selectOgGroupPersonList(person);
        ajaxResult.put("data",list);
        return ajaxResult;
    }


    @PostMapping("/selectGroupBySysId")
    @ResponseBody
    public AjaxResult selectGroupBySysId(String sysId)
    {
        AjaxResult ajaxResult = new AjaxResult();
        List<OgGroup> ogGroups = workService.selectGroupBySysId(sysId);
        ajaxResult.put("data",ogGroups);
        return ajaxResult;
    }
}