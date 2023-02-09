package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.SysAccount;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysAccountService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 账号信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/acc")
public class SysAccController extends BaseController
{
    private String prefix = "system/acc";

    @Autowired
    private ISysAccountService accountService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private SysPasswordService sysPasswordService;

    @Autowired
    private ISysPostService postService;


    @GetMapping()
    public String operlog()
    {
        return prefix + "/acc";
    }


    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OgUser ogUser)
    {
        startPage();
        List<OgUser> list = ogUserService.selectAccountList(ogUser);
        return getDataTable(list);
    }

    @Log(title = "账号管理", businessType = BusinessType.EXPORT)

    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OgUser ogUser)
    {
        List<OgUser> list = ogUserService.selectAccountList(ogUser);
        ExcelUtil<OgUser> util = new ExcelUtil<OgUser>(OgUser.class);
        return util.exportExcel(list, "账号数据");
    }

    @Log(title = "账号管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(accountService.deleteAccountByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 新增账号
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        return prefix + "/add";
    }

    @PostMapping("/userlist")
    @ResponseBody
    public TableDataInfo userlist(SysUser sysUser,String searchUsername,String searchPhonenumber)
    {
        startPage();
        SysUser user = new SysUser();
        if(StringUtils.isNotEmpty(searchUsername)){
            user.setUserName(searchUsername);
        }
        if(StringUtils.isNotEmpty(searchPhonenumber)){
            user.setPhonenumber(searchPhonenumber);
        }
        user.setUserId(sysUser.getUserId());
        List<SysAccount> userList = accountService.selectUserList(user);
        return getDataTable(userList);
    }

    /**
     * 新增保存账号
     */
    @Log(title = "账号管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated OgUser ogUser)
    {
        /*if (UserConstants.ACCOUNT_NAME_NOT_UNIQUE.equals(accountService.checkAccountNameUnique(account)))
        {
            return error("新增账号'" + account.getAccountName() + "'失败，账号名称已存在");
        }
        account.setCreateBy(ShiroUtils.getLoginName());
        accountService.insertAccount(account);*/
        ogUser.setUserId(ogUser.getpId());
        ogUser.setInvalidationMark("1");
        ogUser.setPassword("Bosc@1234");
        ogUser.setPassword(sysPasswordService.encryptPassword(ogUser.getUsername(), ogUser.getPassword(),ogUser.getUserId()));
        return toAjax(ogUserService.insertOgUser(ogUser));
    }

    /**
     * 修改账号
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") String userId, ModelMap mmap)
    {
        mmap.put("ogUser", ogUserService.selectOgUserByUserId(userId));
        return prefix + "/edit";
    }

    /**
     * 修改保存账号
     */
    @Log(title = "账号管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated OgUser ogUser)
    {
        if("".equals(ogUser.getPassword())){
            ogUser.setPassword(sysPasswordService.encryptPassword(ogUser.getUsername(),ogUser.getPassword(),ogUser.getUserId()));
        }

        return toAjax(ogUserService.updateOgUser(ogUser));
    }

    /**
     * 校验账号名称
     */
    @PostMapping("/checkAccountNameUnique")
    @ResponseBody
    public String checkAccountNameUnique(SysAccount account)
    {
        return accountService.checkAccountNameUnique(account);
    }

    /**
     * 账号状态修改
     */
    @Log(title = "账号管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysAccount account)
    {
        return toAjax(accountService.changeStatus(account));
    }

    /**
     * 进入授权岗位页
     */
    @GetMapping("/authPost/{id}")
    public String authRole(@PathVariable("id") String id, ModelMap mmap)
    {
        //SysAccount account = accountService.selectAccountById(id);
        //获取账号所属的列表
        //mmap.put("account", account);
        mmap.put("userId",id);
        return prefix + "/authPost";
    }

    /**
     * 查询岗位数据
     */
    @PostMapping("/postlist")
    @ResponseBody
    public TableDataInfo postlist(SysAccount account) {
        startPage();
        List<SysPost> list = accountService.selectAccountsByAccountId(account);
        return getDataTable(list);
    }

    /**
     * 账号分配岗位
     */
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PostMapping("/insertPostAccount")
    @ResponseBody
    public AjaxResult insertPostAccount(Long accountId, Long[] postIds)
    {
        accountService.insertPostAccount(accountId,postIds);
        return success();
    }

    /**
     * 设置账号IP段
     */
    @GetMapping("/authIp/{userId}")
    public String authIp(@PathVariable("userId")String userId, ModelMap mmap)
    {
        mmap.put("account",ogUserService.selectOgUserByUserId(userId));
        return prefix + "/authIp";
    }

    /**
     * 修改保存账号
     */
    @Log(title = "账号管理", businessType = BusinessType.UPDATE)
    @PostMapping("/autoIpSave")
    @ResponseBody
    public AjaxResult autoIpSave(@Validated SysAccount account)
    {
        return toAjax(accountService.updateAccount(account));
    }


}
