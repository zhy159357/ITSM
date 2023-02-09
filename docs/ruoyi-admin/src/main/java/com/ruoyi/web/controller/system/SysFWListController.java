package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysFWlist;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.InvaMark;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.ISysFWService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 信息管理信息
 * @author Mr.Xy
 */
@Controller
@RequestMapping("/system/fwlist")
public class SysFWListController extends BaseController {
    private String prefix = "system/fwlist";

    @Autowired
    private ISysFWService fwService;

    @GetMapping()
    public String fw(ModelMap mmap)
    {
        List invaMarklist = InvaMark.getList();
        mmap.addAttribute("invaMarklist",invaMarklist);
        return prefix + "/fwlist";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFWlist fw) {
        startPage();
        List<SysFWlist> list = fwService.selectFWList(fw);
        return getDataTable(list);
    }

    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFWlist fw) {
        String isCurrentPage = (String) fw.getParams().get("currentPage");
//        fw.setCreateId(ShiroUtils.getOgUser().getpId());
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<SysFWlist> list = fwService.selectFWList(fw);

            ExcelUtil<SysFWlist> util = new ExcelUtil<SysFWlist>(SysFWlist.class);
            return util.exportExcel(list, "服务通讯录");
        } else {
            List<SysFWlist> list = fwService.selectFWList(fw);
            ExcelUtil<SysFWlist> util = new ExcelUtil<SysFWlist>(SysFWlist.class);
            return util.exportExcel(list, "服务通讯录");
        }
        
        
    }
    /**
     * 信息制度管理新增
     */
    @GetMapping("/add")
    public String add( ModelMap mmap) {
        return prefix + "/add";
    }
    /**
     * 新增保存
     */
    @Log(title = "服务通讯录管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysFWlist fw) {
        fw.setAddress_list_id(UUID.getUUIDStr());
        fw.setCreate_time(DateUtils.handleTimeYYYYMMDDHHMMSS(fw.getCreate_time()));
        return toAjax(fwService.insertFW(fw));
    }
    /**
     * 查看详情
     */
    @GetMapping("/detail/{address_list_id}")
    public String detail(@PathVariable("address_list_id") String address_list_id, ModelMap mmap)
    {
        mmap.put("info", fwService.selectFWById(address_list_id));
        return prefix + "/detail";
    }
    /**
     * 修改
     */
    @GetMapping("/edit/{address_list_id}")
    public String edit(@PathVariable("address_list_id") String address_list_id, ModelMap mmap) {
        mmap.put("info",  fwService.selectFWById(address_list_id));
        return prefix + "/edit";
    }

    /**
     * 修改保存
     * @param fw
     * @return
     */
    @Log(title = "信息制度管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysFWlist fw)
    {
        return toAjax( fwService.updateFW(fw));
    }
    @Log(title = "服务通讯录删除", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(fwService.deleteFWByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
}