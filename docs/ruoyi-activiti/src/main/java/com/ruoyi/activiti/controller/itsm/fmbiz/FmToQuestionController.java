package com.ruoyi.activiti.controller.itsm.fmbiz;

import java.util.List;

import com.ruoyi.activiti.service.IFmToQuestionService;
import com.ruoyi.common.core.domain.entity.FmToQuestion;
import com.ruoyi.common.utils.poi.ExcelUtil;
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
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2021-08-17
 */
@Controller
@RequestMapping("/fmtoquestion")
public class FmToQuestionController extends BaseController
{
    private String prefix = "/fmtoquestion";

    @Autowired
    private IFmToQuestionService fmToQuestionService;

    @GetMapping()
    public String question()
    {
        return prefix + "/question";
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmToQuestion fmToQuestion)
    {
        startPage();
        List<FmToQuestion> list = fmToQuestionService.selectFmToQuestionList(fmToQuestion);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmToQuestion fmToQuestion)
    {
        List<FmToQuestion> list = fmToQuestionService.selectFmToQuestionList(fmToQuestion);
        ExcelUtil<FmToQuestion> util = new ExcelUtil<FmToQuestion>(FmToQuestion.class);
        return util.exportExcel(list, "question");
    }

    //选择系统
    @GetMapping("/selectOneApplication")
    public String selectOneApplication() {
        return prefix + "/selectOneApplication";
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
    public AjaxResult addSave(FmToQuestion fmToQuestion)
    {
        return toAjax(fmToQuestionService.insertFmToQuestion(fmToQuestion));
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        FmToQuestion fmToQuestion = fmToQuestionService.selectFmToQuestionById(id);
        mmap.put("fmToQuestion", fmToQuestion);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmToQuestion fmToQuestion)
    {
        return toAjax(fmToQuestionService.updateFmToQuestion(fmToQuestion));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(fmToQuestionService.deleteFmToQuestionByIds(ids));
    }
}
