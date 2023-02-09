package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.shiro.util.AuthorizationUtils;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 角色信息
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/role")
public class SysRoleController extends BaseController
{
    private String prefix = "system/role";

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysUserService userService;

    @GetMapping()
    public String role()
    {
        return prefix + "/role";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgRole role)
    {
        startPage();
        List<OgRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OgRole role)
    {
        Map<String, Object> params = role.getParams();
        String currentPage = (String) params.get("currentPage");
        if ("currentPage".equals(currentPage)) {
            startPage();
        } else if ("all".equals(currentPage)) {
        }
        List<OgRole> list = roleService.selectRoleList(role);
        ExcelUtil<OgRole> util = new ExcelUtil<OgRole>(OgRole.class);
        return util.exportExcel(list, "角色数据");
    }

    /**
     * 新增角色
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        /*String roleCode = String.valueOf(new Random().nextInt(999999999));
        mmap.put("rid",roleCode);*/
        return prefix + "/add";
    }

    /**
     * 新增保存角色
     */
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated OgRole role)
    {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
        {
            return error("新增角色'" + role.getrName() + "'失败，角色名称已存在");
        }
        else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
        {
            return error("新增角色'" + role.getrName() + "'失败，角色权限已存在");
        }
        role.setAdder(ShiroUtils.getLoginName());
        role.setAddTime(DateUtils.dateTimeNow());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改角色
     */
    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") String roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/edit";
    }

    /**
     * 授权菜单
     */
    @GetMapping("/authMenu/{roleId}")
    public String authMenu(@PathVariable("roleId") String roleId, ModelMap map){
        map.put("role", roleService.selectRoleById(roleId));
        return prefix + "/authMenu";
    }

    /**
     * 修改保存角色
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated OgRole role)
    {
        role.setUpdateBy(ShiroUtils.getLoginName());
        role.setUpdatetime(DateUtils.dateTimeNow());
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 角色分配数据权限
     */
    @GetMapping("/authDataScope/{roleId}")
    public String authDataScope(@PathVariable("roleId") String roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/dataScope";
    }

    /**
     * 保存角色分配数据权限
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/authDataScope")
    @ResponseBody
    public AjaxResult authDataScopeSave(OgRole role)
    {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(ShiroUtils.getLoginName());
        if (roleService.authDataScope(role) > 0)
        {
            ShiroUtils.setSysUser(userService.selectUserById(ShiroUtils.getSysUser().getUserId()));
            return success();
        }
        return error();
    }

    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(roleService.deleteRoleByIds(ids));
        }
        catch (Exception e)
        {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    public String checkRoleNameUnique(OgRole role)
    {
        return roleService.checkRoleNameUnique(role);
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkRoleKeyUnique")
    @ResponseBody
    public String checkRoleKeyUnique(OgRole role)
    {
        return roleService.checkRoleKeyUnique(role);
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree()
    {
        return prefix + "/tree";
    }

    /**
     * 角色状态修改
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(OgRole role)
    {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.changeStatus(role));
    }

    /**
     * 分配用户
     */
    @GetMapping("/authUser/{roleId}")
    public String authUser(@PathVariable("roleId") String roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/authUser";
    }

    /**
     * 查看岗位
     */
    @GetMapping("/authPost/{roleId}")
    public String authPost(@PathVariable("roleId") String roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/authPost";
    }

    /**
     * 查询岗位数据
     */
    @PostMapping("/postList")
    @ResponseBody
    public TableDataInfo postList(OgPost post) {
        startPage();
        List<OgPost> list = roleService.selectPostsByRoleId(post);
        return getDataTable(list);
    }

    /**
     * 查询已分配用户角色列表
     */
    @PostMapping("/authUser/allocatedList")
    @ResponseBody
    public TableDataInfo allocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancel")
    @ResponseBody
    public AjaxResult cancelAuthUser(SysUserRole userRole)
    {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancelAll")
    @ResponseBody
    public AjaxResult cancelAuthUserAll(Long roleId, String userIds)
    {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 选择用户
     */
    @GetMapping("/authUser/selectUser/{roleId}")
    public String selectUser(@PathVariable("roleId") String roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/selectUser";
    }

    /**
     * 查询未分配用户角色列表
     */
    @PostMapping("/authUser/unallocatedList")
    @ResponseBody
    public TableDataInfo unallocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 批量选择用户授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/selectAll")
    @ResponseBody
    public AjaxResult selectAuthUserAll(Long roleId, String userIds)
    {
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    public static void main(String[] args) {
        int radomInt = new Random().nextInt(99999999);
        System.out.println(radomInt);
    }

}