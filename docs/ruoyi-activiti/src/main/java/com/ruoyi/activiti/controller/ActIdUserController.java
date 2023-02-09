package com.ruoyi.activiti.controller;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.system.service.IOgUserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.ruoyi.activiti.domain.ActIdUser;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUserService;

/**
 * 流程用户Controller
 *
 * @author gggggg Tech
 * @date 2019-10-02
 */
@Controller
@RequestMapping("/user")
public class ActIdUserController extends BaseController {
	private String prefix = "user";

	@Autowired
	private IdentityService identityService;
	@Autowired
	private ISysUserService userService;
	@Autowired
	private IOgUserService ogUserService;

	@GetMapping()
	public String user() {
		return prefix + "/user";
	}

	/**
	 * 查询流程用户列表
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ActIdUser query) {
		PageDomain pageDomain = TableSupport.buildPageRequest();
		Integer pageNum = pageDomain.getPageNum();
		Integer pageSize = pageDomain.getPageSize();

		UserQuery userQuery = identityService.createUserQuery();
		if (StringUtils.isNotBlank(query.getId())) {
			userQuery.userId(query.getId());
		}
		if (StringUtils.isNotBlank(query.getFirst())) {
			userQuery.userFirstNameLike("%" + query.getFirst() + "%");
		}
		if (StringUtils.isNotBlank(query.getEmail())) {
			userQuery.userEmailLike("%" + query.getEmail() + "%");
		}
		List<User> userList = userQuery.listPage((pageNum - 1) * pageSize, pageSize);
		Page<ActIdUser> list = new Page<>();
		list.setTotal(userQuery.count());
		list.setPageNum(pageNum);
		list.setPageSize(pageSize);
		for (User user : userList) {
			ActIdUser idUser = new ActIdUser();
			idUser.setId(user.getId());
			idUser.setFirst(user.getFirstName());
			idUser.setLast(user.getLastName());
			idUser.setEmail(user.getEmail());
			list.add(idUser);
		}
		return getDataTable(list);
	}

	@GetMapping("/addUser")
	public String selectSystemUser() {
		return prefix + "/addUser";
	}

	@PostMapping("/selectSystemUser")
	@ResponseBody
	public TableDataInfo selectSystemUser(OgUser ogUser) {
        startPage();
        List<OgUser> userList = ogUserService.selectOgUserList(ogUser);
        return getDataTable(userList);
    }

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(OgUser ogUser) {
        UserQuery userQuery = identityService.createUserQuery();
        User result = userQuery.userId(ogUser.getUserId()).singleResult();
        if(result != null) {
            return AjaxResult.error("用户【"+ogUser.getUsername()+"】在流程用户中已存在，不可重复添加！");
        }
        User user = identityService.newUser(ogUser.getUserId());
        user.setId(ogUser.getUserId());
        user.setEmail(ogUser.getEmail());
        user.setFirstName(ogUser.getUsername());// 此处保存该用户的用户名称
		user.setLastName(ogUser.getPname());// 此处保存该用户的真实名称
        identityService.saveUser(user);
        return AjaxResult.success();
    }

    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String userId) {
        identityService.deleteUser(userId);
        return AjaxResult.success();
    }

	/**
	 * 选择系统用户
	 */
	@GetMapping("/authUser/selectUser")
	public String selectUser(String taskId, ModelMap mmap) {
		mmap.put("taskId", taskId);
		return prefix + "/selectUser";
	}

	@PostMapping("/systemUserList")
	@ResponseBody
	public TableDataInfo systemUserList(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectUserList(user);
		return getDataTable(list);
	}

}
