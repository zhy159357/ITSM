package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 岗位信息操作处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/post2")
public class SysPostOneController extends BaseController
{
    private String prefix = "system/post2";

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private OgPostService ogPostService;

    @Autowired
    private IOgUserPostService ogUserPostService;

    @GetMapping()
    public String post()
    {
        return prefix + "/post";
    }

    /**
     *
     * @param userId
     * @param modelMap
     * @return
     */
    @GetMapping("/accountPost/{userId}")
    public String openAccountPost(@PathVariable("userId") String userId,ModelMap modelMap)
    {
        modelMap.put("userId",userId);
        return prefix + "/accountPost";
    }

    /**
     * 对当前用户岗位的查询
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo selectAllPostByUserId(String postName)
    {
        startPage();
        OgUser ogUser = ShiroUtils.getOgUser();
        List<OgPost> list = ogPostService.selectAllPostByUserId(ogUser.getUserId(),postName);
        return getDataTable(list);
    }


    /**
     * 导出岗位
     * @param
     * @return
     */
    @Log(title = "例行变更计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OgPost post)
    {
        String isCurrentPage = (String)post.getParams().get("currentPage");
        if("currentPage".equals(isCurrentPage)){
            startPage();
        }else if("all".equals(isCurrentPage)){
            post = new OgPost();
        }
        List<OgPost> list = ogPostService.selectPostList(post);
        ExcelUtil<OgPost> util = new ExcelUtil<OgPost>(OgPost.class);
        return util.exportExcel(list, "岗位信息");
    }


    /**
     * 删除前查询id
     * @param id
     * @return
     */
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        OgPost ogPost =ogPostService.selectPostById(id);

        //查询当前所选岗位下的角色
        List<OgPost> postRole = ogPostService.selectPostRoleById(id);
        //查询当前所选岗位下的账号
        List<OgPost> postUser = ogPostService.selectPostUserById(id);

        ajaxResult.put("data",ogPost);
        ajaxResult.put("postRole",postRole);
        ajaxResult.put("postUser",postUser);

        return  ajaxResult;
    }

    /**
     * 删除岗位
     * @param ids
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(ogPostService.deletePostByIds(ids));


    }

    /**
     * 根据用户Id删除岗位信息
     * @param ids
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @PostMapping("/deletePostsByUserId")
    @ResponseBody
    public AjaxResult removePostByUserId(String userId,String ids)
    {
        return toAjax(ogUserPostService.deletePostsByUserId(userId,ids));


    }


    /**
     * 转到新增岗位页面
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }


    /**
     * 新增保存岗位
     */
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated OgPost post)
    {
        if (UserConstants.POST_NAME_NOT_UNIQUE.equals(ogPostService.checkPostNameUnique(post)))
        {
            return error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        else if (UserConstants.POST_IDPOST_NOT_UNIQUE.equals(ogPostService.checkPostCodeUnique(post)))
        {
            return error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        String pname = ogPerson.getpName();
        post.setAddEr(pname);
        return toAjax(ogPostService.insertPost(post));
    }

    /**
     * 修改岗位
     */
    @GetMapping("/edit/{postId}")
    public String edit(@PathVariable("postId") String  postId, ModelMap mmap)
    {
        mmap.put("post", ogPostService.selectPostById(postId));
        return prefix + "/edit";
    }

    /**
     * 修改保存岗位
     */
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated OgPost post)
    {

        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = ogPersonService.selectOgPersonById(pId);
        String pname = ogPerson.getpName();
        post.setModer(pname);
        post.setUpdateTime(DateUtils.dateTimeNow("yyyyMMdd"));
        return toAjax(ogPostService.updatePost(post));
    }


    /**
     * 查看角色
     */
    @GetMapping("/authRolePost/{postId}")
    public String authRolePost(@PathVariable("postId") String postId, ModelMap mmap)
    {
        mmap.put("post", ogPostService.selectPostById(postId));
        return prefix + "/authRolePost";
    }

    /**
     * 查询已分配角色岗位列表
     * @param role
     * @return
     */
    @PostMapping("/authRolePost/allocatedListRolePost")
    @ResponseBody
    public TableDataInfo allocatedListRolePost(OgRole role)
    {
        startPage();
        List<OgRole> list = roleService.selectAllocatedListPost(role);
        return getDataTable(list);
    }


    /**
     * 查看用户
     */
    @GetMapping("/authUserPost/{postId}")
    public String authUserPost(@PathVariable("postId") String postId, ModelMap mmap)
    {
        mmap.put("post", ogPostService.selectPostById(postId));
        return prefix + "/authUserPost";
    }

    /**
     * 查询已分配账号的岗位列表
     * @param user
     * @return
     */
    @PostMapping("/authUserPost/allocatedListPost")
    @ResponseBody
    public TableDataInfo allocatedListPost(OgUser user)
    {
        startPage();
        List<OgUser> list = userService.selectAllocatedListPost(user);
        return getDataTable(list);
    }


    /**
     * 选择角色
     */
    @GetMapping("/authRolePost/selectRolePost/{postId}")
    public String selectRolePost(@PathVariable("postId") String postId, ModelMap mmap)
    {
        mmap.put("post", ogPostService.selectPostById(postId));
        return prefix + "/selectRolePost";
    }

    /**
     * 查询未分配岗位的角色列表
     */
    @PostMapping("/authRolePost/unallocatedListRolePost")
    @ResponseBody
    public TableDataInfo unallocatedListPost(OgRole role)
    {
        startPage();
        List<OgRole> list = roleService.selectUnallocatedListPost(role);
        return getDataTable(list);
    }



    /**
     * 添加角色
     */
    @PostMapping("/authRolePost/selectAll")
    @ResponseBody
    public AjaxResult selectAuthRoleAll(String postId, String rids)
    {

        return toAjax(ogPostService.insertAuthRoles(postId, rids));


    }


    /**
     * 删除角色
     * @param rids
     * @return
     */
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @PostMapping("/authRolePost/removeRole")
    @ResponseBody
    public AjaxResult deleteRolePost(String rids,String postId)
    {
        try
        {
            return toAjax(ogPostService.deleteRolePost(rids,postId));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }



    /**
     * 查询已分配账号的岗位列表
     */
    /*@PostMapping("/authUserPost/{userId}")
    @ResponseBody
    public TableDataInfo selectAllPostByUserId(@PathVariable("userId") String userId,String postName)
    {
        startPage();
        List<OgPost> list = ogPostService.selectAllPostByUserId(userId,postName);
        return getDataTable(list);
    }*/

    /**
     *
     * @param userId
     * @param ids
     * @return
     */
    @PostMapping("/batch")
    @ResponseBody
    public AjaxResult batchAdd(String userId,String ids){
        List<OgUserPost> list = new ArrayList<OgUserPost>();
        String[] postIds = ids.split(",");
        OgUserPost ogUserPost =  null;
        for(String postId : postIds){
            ogUserPost = new OgUserPost();
            ogUserPost.setUserId(userId);
            ogUserPost.setPostId(postId);
            list.add(ogUserPost);
        }

        return  toAjax(ogUserPostService.batchAdd(list));
    }

    /**
     *查询账号id
     * @param userId
     * @param postName
     * @return
     */
    @PostMapping("/getPostByUserId")
    @ResponseBody
    public AjaxResult getPostByUserId(String userId,String postName){
        AjaxResult ajaxResult = new AjaxResult();
        List<OgPost> list = ogPostService.selectAllPostByUserId(userId, postName);
        ajaxResult.put("list",list);
        return ajaxResult;
    }


    /**
     * 用户岗位关联列表展示
     * @param ogUserPost
     * @return
     */
    @PostMapping("/selectAllPostByUserId")
    @ResponseBody
    public TableDataInfo selectAllPostByUserId(OgUserPost ogUserPost)
    {
        startPage();
        List<OgUserPost> list = ogUserPostService.selectPostByUserId(ogUserPost);
        return getDataTable(list);
    }

    /**
     * 列表展示
     * @param ogUserPost
     * @return
     */
    @PostMapping("/selectListPostByUserId")
    @ResponseBody
    public AjaxResult selectListPostByUserId(OgUserPost ogUserPost)
    {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("data",ogUserPostService.selectPostByUserId(ogUserPost));
        return ajaxResult;
    }


}
