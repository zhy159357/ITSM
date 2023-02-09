package com.ruoyi.activiti.controller.itsm.fmbiz;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.activiti.mapper.FmBizMapper;
import com.ruoyi.activiti.service.IFmbizAndIssueService;
import com.ruoyi.activiti.service.IOgGroupPersonService;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmbizAndIssue;
import com.ruoyi.common.core.domain.entity.OgGroupPerson;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.service.IOgPersonService;
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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 事件单关联问题单关联关系Controller
 *
 * @author liul
 * @date 2021-10-15
 */
@Controller
@RequestMapping("/system/issue")
public class FmbizAndIssueController extends BaseController {
    private String prefix = "system/issue";

    @Autowired
    private IFmbizAndIssueService fmbizAndIssueService;
    @Autowired
    private FmBizMapper fmBizMapper;
    @Autowired
    private IOgGroupPersonService iOgGroupPersonService;
    @Autowired
    private IOgPersonService personService;

    @GetMapping()
    public String issue() {
        return prefix + "/issue";
    }

    /**
     * 查询事件单关联问题单关联关系列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FmbizAndIssue fmbizAndIssue) {
        startPage();
        List<FmbizAndIssue> list = fmbizAndIssueService.selectFmbizAndIssueList(fmbizAndIssue);
        return getDataTable(list);
    }

    /**
     * 导出事件单关联问题单关联关系列表
     */
    @Log(title = "事件单关联问题单关联关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FmbizAndIssue fmbizAndIssue) {
        List<FmbizAndIssue> list = fmbizAndIssueService.selectFmbizAndIssueList(fmbizAndIssue);
        ExcelUtil<FmbizAndIssue> util = new ExcelUtil<FmbizAndIssue>(FmbizAndIssue.class);
        return util.exportExcel(list, "issue");
    }

    /**
     * 新增事件单关联问题单关联关系
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存事件单关联问题单关联关系
     */
    @Log(title = "事件单关联问题单关联关系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FmbizAndIssue fmbizAndIssue) {
        return toAjax(fmbizAndIssueService.insertFmbizAndIssue(fmbizAndIssue));
    }

    /**
     * 修改事件单关联问题单关联关系
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        FmbizAndIssue fmbizAndIssue = fmbizAndIssueService.selectFmbizAndIssueById(id);
        mmap.put("fmbizAndIssue", fmbizAndIssue);
        return prefix + "/edit";
    }

    /**
     * 修改保存事件单关联问题单关联关系
     */
    @Log(title = "事件单关联问题单关联关系", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FmbizAndIssue fmbizAndIssue) {
        return toAjax(fmbizAndIssueService.updateFmbizAndIssue(fmbizAndIssue));
    }

    /**
     * 删除事件单关联问题单关联关系
     */
    @Log(title = "事件单关联问题单关联关系", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(fmbizAndIssueService.deleteFmbizAndIssueByIds(ids));
    }

    @PostMapping("/getFmBizAndIssueList")
    @ResponseBody
    public TableDataInfo getFmBizAndIssueList(FmBiz fmBiz) {
        List<FmBiz> fmBizs = new ArrayList<>();
        OgGroupPerson ogGroupPerson = new OgGroupPerson();
        ogGroupPerson.setPid(ShiroUtils.getUserId());
        List<OgGroupPerson> list = iOgGroupPersonService.selectOgGroupPersonList(ogGroupPerson);//拿到登陆人所有的工作组
        if (!list.isEmpty()) {
            List<String> groupId = new ArrayList<>();
            for (OgGroupPerson groupPerson : list) {
                groupId.add(groupPerson.getGroupId());
            }
            fmBiz.getParams().put("sGroupid", groupId);
            OgPerson person = personService.selectOgPersonById(ShiroUtils.getUserId());
            fmBiz.setDealerId(person.getpName());
            fmBizs = fmBizMapper.getFmBizAndIssueList(fmBiz);
        }
        return getDataTable_ideal(fmBizs);
    }
}
