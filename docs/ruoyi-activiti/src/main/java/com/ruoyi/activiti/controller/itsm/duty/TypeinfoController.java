package com.ruoyi.activiti.controller.itsm.duty;

import com.ruoyi.activiti.constants.VersionStatusConstants;
import com.ruoyi.activiti.service.IDutyTypeinfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.DutyTypeinfo;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TypeinfoNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数列别Controller
 * @author dayong_sun
 * @date 2020-12-06
 */
@Controller
@RequestMapping("duty/typeinfo")
public class TypeinfoController extends BaseController {
    private String prefix = "duty/typeinfo";

    @Autowired
    private IDutyTypeinfoService dutyTypeinfoService;

    @GetMapping()
    public String typeinfo() {
        return prefix + "/typeinfo";
    }

    /**
     * 查询参数列别列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DutyTypeinfo dutyTypeinfo) {
        startPage();
        List<DutyTypeinfo> list = dutyTypeinfoService.selectDutyTypeinfoList(dutyTypeinfo);
        return getDataTable(list);
    }

    /**
     * 新增参数列别
     */
    @PostMapping("/addCheck")
    @ResponseBody
    public int addCheck(String typeinfoId) {
        int result = 0;
        DutyTypeinfo info = dutyTypeinfoService.selectDutyTypeinfoById(typeinfoId);
        if (info == null) {// 防止没有parentId的情况报错空指针
            result = 1;
        }
        return result;
    }

    /**
     * 新增参数列别
     */
    @GetMapping("/addTypeInfo/{typeinfoId}")
    public String add(@PathVariable("typeinfoId") String typeinfoId, ModelMap mmap) {
        DutyTypeinfo info = dutyTypeinfoService.selectDutyTypeinfoById(typeinfoId);
        if (info == null) {// 防止没有parentId的情况报错空指针
            info = new DutyTypeinfo();
        }
        List<OgPerson> userlist = dutyTypeinfoService.selectUserList(VersionStatusConstants.ZBJS_NO);
        List numberList = TypeinfoNumber.getList();

        mmap.put("dutyTypeinfo", info);
        mmap.put("userlist", userlist);
        mmap.put("numberList", numberList);
        return prefix + "/add";
    }

    /**
     * 新增保存参数列别
     */
    @Log(title = "参数列别", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DutyTypeinfo dutyTypeinfo) {
        return toAjax(dutyTypeinfoService.insertDutyTypeinfo(dutyTypeinfo));
    }

    /**
     * 修改参数列别
     */
    @GetMapping("/edit/{typeinfoId}")
    public String edit(@PathVariable("typeinfoId") String typeinfoId, ModelMap mmap) {
        DutyTypeinfo dutyTypeinfo = dutyTypeinfoService.selectDutyTypeinfoById(typeinfoId);
        List<OgPerson> userlist = dutyTypeinfoService.selectUserList(VersionStatusConstants.ZBJS_NO);
        List numberList = TypeinfoNumber.getList();

        mmap.put("dutyTypeinfo", dutyTypeinfo);
        mmap.put("userlist", userlist);
        mmap.put("numberList", numberList);
        return prefix + "/edit";
    }

    /**
     * 修改保存参数列别
     */
    @Log(title = "参数列别", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DutyTypeinfo dutyTypeinfo) {
        return toAjax(dutyTypeinfoService.updateDutyTypeinfo(dutyTypeinfo));
    }

    /**
     * 删除参数列别
     */
    @Log(title = "参数列别", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(dutyTypeinfoService.deleteDutyTypeinfoByIds(ids));
    }

    /**
     * 加载参数设置列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<ZtreeStr> treeData() {
        List<ZtreeStr> ztrees = dutyTypeinfoService.selectTypeinfoTree(new DutyTypeinfo());
        return ztrees;
    }

    /**
     * 加载部门列表树（排除下级）
     */
    @GetMapping("/treeData/{typeinfoId}")
    @ResponseBody
    public List<Ztree> treeDataExcludeChild(@PathVariable(value = "typeinfoId", required = false) String typeinfoId) {
        DutyTypeinfo dutyTypeinfo = new DutyTypeinfo();
        dutyTypeinfo.setTypeinfoId(typeinfoId);
        List<Ztree> ztrees = dutyTypeinfoService.selectTypeTreeExcludeChild(dutyTypeinfo);
        return ztrees;
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkTypeNoUnique")
    @ResponseBody
    public String checkTypeNoUnique(DutyTypeinfo dutyTypeinfo)
    {
        return dutyTypeinfoService.checkTypeNoUnique(dutyTypeinfo);
    }
}
