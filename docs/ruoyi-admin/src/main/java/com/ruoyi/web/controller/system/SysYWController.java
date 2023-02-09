package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.SysAddlist;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.InvaMark;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysYWService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运维通讯录管理
 * @author Mr.Xy
 */
@Controller
@RequestMapping("/system/ywlist")
public class SysYWController extends BaseController {
    private String prefix = "system/ywlist";

    @Autowired
    private ISysYWService ywService;
    @Autowired
    private ISysDeptService deptService;

    @GetMapping()
    public String yw(ModelMap mmap)
    {
        List invaMarklist = InvaMark.getList();
        mmap.addAttribute("invaMarklist",invaMarklist);
        return prefix + "/ywlist";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysAddlist yw) {
        startPage();
        List<SysAddlist> list = ywService.selectYWList(yw);
        return getDataTable(list);
    }
    //导出
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysAddlist yw) {
        String isCurrentPage = (String) yw.getParams().get("currentPage");
//        fw.setCreateId(ShiroUtils.getOgUser().getpId());
        if ("currentPage".equals(isCurrentPage)) {
            startPage();
            List<SysAddlist> list = ywService.selectYWList(yw);
            ExcelUtil<SysAddlist> util = new ExcelUtil<SysAddlist>(SysAddlist.class);
            return util.exportExcel(list, "运维通讯录");
        } else {
            List<SysAddlist> list = ywService.selectYWList(yw);
            ExcelUtil<SysAddlist> util = new ExcelUtil<SysAddlist>(SysAddlist.class);
            return util.exportExcel(list, "运维通讯录");
        }
    }

    /**
     * 运维通讯录新增
     */
    @GetMapping("/add")
    public String add( ModelMap mmap) {
        return prefix + "/add";
    }
    /**
     * 新增保存
     */
    @Log(title = "运维通讯录管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysAddlist yw) {
        yw.setAddress_list_id(UUID.getUUIDStr());
        return toAjax(ywService.insertYW(yw));
    }
    /**
     * 查看详情
     */
    @GetMapping("/detail/{address_list_id}")
    public String detail(@PathVariable("address_list_id") String address_list_id, ModelMap mmap)
    {
        mmap.put("YW", ywService.selectYWById(address_list_id));
        return prefix + "/detail";
    }
    /**
     * 修改
     */
    @GetMapping("/edit/{address_list_id}")
    public String edit(@PathVariable("address_list_id") String address_list_id, ModelMap mmap) {
        mmap.put("YW",  ywService.selectYWById(address_list_id));
        return prefix + "/edit";
    }

    /**
     * 修改保存
     * @param yw
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysAddlist yw)
    {
        return toAjax( ywService.updateYW(yw));
    }

    @Log(title = "服务通讯录删除", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(ywService.deleteYWByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 加载列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = deptService.selectDeptTree(new OgOrg());
        return ztrees;
    }
}