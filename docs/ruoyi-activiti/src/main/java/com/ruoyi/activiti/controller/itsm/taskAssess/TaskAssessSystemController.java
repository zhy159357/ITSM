package com.ruoyi.activiti.controller.itsm.taskAssess;

import java.util.List;

import com.ruoyi.activiti.service.ITaskAssessSystemService;
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
import com.ruoyi.activiti.domain.TaskAssessSystem;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 厂商考核Controller
 * 
 * @author ruoyi
 * @date 2022-01-20
 */
@Controller
@RequestMapping("/taskAssess")
public class TaskAssessSystemController extends BaseController
{
    private String prefix = "taskAssess";

    @Autowired
    private ITaskAssessSystemService taskAssessSystemService;

    @GetMapping()
    public String tasfAssess()
    {
        return prefix + "/taskAssess";
    }

    /**
     * 查询厂商考核列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TaskAssessSystem taskAssessSystem)
    {
        startPage();
        List<TaskAssessSystem> list = taskAssessSystemService.selectTaskAssessSystemList(taskAssessSystem);
        return getDataTable(list);
    }

    /**
     * 导出厂商考核列表
     */
    @Log(title = "厂商考核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TaskAssessSystem taskAssessSystem)
    {
        List<TaskAssessSystem> list = taskAssessSystemService.selectTaskAssessSystemList(taskAssessSystem);
        ExcelUtil<TaskAssessSystem> util = new ExcelUtil<TaskAssessSystem>(TaskAssessSystem.class);
        return util.exportExcel(list, "taskAssess");
    }

    /**
     * 新增厂商考核
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存厂商考核
     */
    @Log(title = "厂商考核", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TaskAssessSystem taskAssessSystem)
    {
        return toAjax(taskAssessSystemService.insertTaskAssessSystem(taskAssessSystem));
    }

    /**
     * 修改厂商考核
     */
    @GetMapping("/edit/{manyidu}")
    public String edit(@PathVariable("manyidu") String manyidu, ModelMap mmap)
    {
        TaskAssessSystem taskAssessSystem = taskAssessSystemService.selectTaskAssessSystemById(manyidu);
        mmap.put("taskAssessSystem", taskAssessSystem);
        return prefix + "/edit";
    }

    /**
     * 修改保存厂商考核
     */
    @Log(title = "厂商考核", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TaskAssessSystem taskAssessSystem)
    {
        return toAjax(taskAssessSystemService.updateTaskAssessSystem(taskAssessSystem));
    }

    /**
     * 删除厂商考核
     */
    @Log(title = "厂商考核", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(taskAssessSystemService.deleteTaskAssessSystemByIds(ids));
    }
}
