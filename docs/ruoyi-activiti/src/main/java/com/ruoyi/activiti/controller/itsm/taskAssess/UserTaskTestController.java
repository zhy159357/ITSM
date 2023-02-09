package com.ruoyi.activiti.controller.itsm.taskAssess;

import java.util.List;

import com.ruoyi.activiti.domain.OrgTaskTest;
import com.ruoyi.activiti.service.IUserTaskTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.activiti.domain.UserTaskTest;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户考核Controller
 * 
 * @author ruoyi
 * @date 2022-01-13
 */
@Controller
@RequestMapping("/userTaskAssess")
public class UserTaskTestController extends BaseController
{
    private String prefix = "taskAssess";

    @Autowired
    private IUserTaskTestService userTaskTestService;

    @GetMapping()
    public String taksAssess()
    {
        return prefix + "/userTaskAssess";
    }
    @GetMapping("/orgTaskAssess")
    /**
    　　* @description: 机构考核
    　　* @param ${tags}
    　　* @return ${return_type}
    　　* @throws
    　　* @author 张旭
    　　* @date 2022/1/18 10:31
    　　*/
    public String orgTaskAssess(){
        return prefix+"/orgTaskAssess";
    }
    /**
     * 查询用户考核列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserTaskTest userTaskTest)
    {
        startPage();
        List<UserTaskTest> list = userTaskTestService.selectUserTaskTestList(userTaskTest);
        return getDataTable(list);
    }
    /**
     * 查询机构考核列表
     */
    @PostMapping("/orglist")
    @ResponseBody
    public TableDataInfo orglist(UserTaskTest userTaskTest)
    {
        startPage();
        List<OrgTaskTest> list = userTaskTestService.selectOrgTaskTestList(userTaskTest);
        return getDataTable(list);
    }
    /**
     * 导出用户考核列表
     */
    @Log(title = "用户考核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(UserTaskTest userTaskTest)
    {
        List<UserTaskTest> list = userTaskTestService.selectUserTaskTestList(userTaskTest);
        ExcelUtil<UserTaskTest> util = new ExcelUtil<UserTaskTest>(UserTaskTest.class);
        return util.exportExcel(list, "test");
    }
    /**
     * 导出机构考核列表
     */
    @Log(title = "机构考核", businessType = BusinessType.EXPORT)
    @PostMapping("/orgexport")
    @ResponseBody
    public AjaxResult orgexport(UserTaskTest userTaskTest)
    {
        List<OrgTaskTest> list = userTaskTestService.selectOrgTaskTestList(userTaskTest);
        ExcelUtil<OrgTaskTest> util = new ExcelUtil<OrgTaskTest>(OrgTaskTest.class);
        return util.exportExcel(list, "test");
    }
    /**
     * 新增用户考核
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存用户考核
     */
    @Log(title = "用户考核", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(UserTaskTest userTaskTest)
    {
        return toAjax(userTaskTestService.insertUserTaskTest(userTaskTest));
    }

    /**
     * 修改用户考核
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        UserTaskTest userTaskTest = userTaskTestService.selectUserTaskTestById(id);
        mmap.put("userTaskTest", userTaskTest);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户考核
     */
    @Log(title = "用户考核", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(UserTaskTest userTaskTest)
    {
        return toAjax(userTaskTestService.updateUserTaskTest(userTaskTest));
    }

    /**
     * 删除用户考核
     */
    @Log(title = "用户考核", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(userTaskTestService.deleteUserTaskTestByIds(ids));
    }
}
