package com.ruoyi.activiti.controller;

import com.github.pagehelper.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.activiti.domain.ActIdGroup;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysWorkService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程用户组Controller
 *
 * @author gggggg Tech
 * @date 2019-10-02
 */
@Controller
@RequestMapping("/group")
public class ActIdGroupController extends BaseController
{
    private String prefix = "group";

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ISysWorkService workService;

    @GetMapping()
    public String group()
    {
        return prefix + "/group";
    }

    /**
     * 查询流程用户组列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActIdGroup query)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        GroupQuery groupQuery = identityService.createGroupQuery();
        if (StringUtils.isNotBlank(query.getId())) {
            groupQuery.groupId(query.getId());
        }
        if (StringUtils.isNotBlank(query.getName())) {
            groupQuery.groupNameLike("%" + query.getName() + "%");
        }
        List<Group> groupList = groupQuery.listPage((pageNum - 1) * pageSize, pageSize);
        Page<ActIdGroup> list = new Page<>();
        list.setTotal(groupQuery.count());
        list.setPageNum(pageNum);
        list.setPageSize(pageSize);
        for (Group group: groupList) {
            ActIdGroup idGroup = new ActIdGroup();
            idGroup.setId(group.getId());
            idGroup.setName(group.getName());
            list.add(idGroup);
        }
        return getDataTable(list);
    }

    @GetMapping("/addGroup")
    public String selectSystemGroup() {
        return prefix + "/addGroup";
    }

    @PostMapping("/selectSystemGroup")
    @ResponseBody
    public TableDataInfo selectSystemGroup(OgGroup group) {
        startPage();
        List<OgGroup> groupList = workService.selectOgGroupList(group);
        return getDataTable(groupList);
    }

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(OgGroup group) {
        GroupQuery groupQuery = identityService.createGroupQuery();
        Group result = groupQuery.groupId(group.getGroupId()).singleResult();
        if(result != null) {
            return AjaxResult.error("用户组【"+group.getGrpName()+"】在流程用户组中已存在，不可重复添加！");
        }
        Group newGroup = identityService.newGroup(group.getGroupId());
        newGroup.setId(group.getGroupId());
        newGroup.setName(group.getGrpName());
        identityService.saveGroup(newGroup);
        return AjaxResult.success();
    }

    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String groupId) {
        identityService.deleteGroup(groupId);
        return AjaxResult.success();
    }

}
