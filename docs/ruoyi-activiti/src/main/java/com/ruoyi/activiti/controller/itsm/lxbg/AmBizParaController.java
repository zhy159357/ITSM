package com.ruoyi.activiti.controller.itsm.lxbg;

import java.util.List;

import com.ruoyi.activiti.service.IAmBizParaService;
import com.ruoyi.common.core.domain.entity.AmBizPara;
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
 * 【接受范围】Controller
 *
 * @author ruoyi
 * @date 2021-01-19
 */
@Controller
@RequestMapping("/system/para")
public class AmBizParaController extends BaseController
{
    private String prefix = "system/para";

    private String add_prefix = "lxbg/addlxbg";


    @Autowired
    private IAmBizParaService amBizParaService;

    /**
     * 转到接受范围页面
     * @return
     */
    @GetMapping()
    public String para()
    {
        return prefix + "/para";
    }

    /**
     * 查询【接受范围】列表
     * @param amBizPara
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AmBizPara amBizPara)
    {
        startPage();
        List<AmBizPara> list = amBizParaService.selectAmBizParaList(amBizPara);
        return getDataTable(list);
    }

    /**
     * 导出列表
     * @param amBizPara
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AmBizPara amBizPara)
    {
        List<AmBizPara> list = amBizParaService.selectAmBizParaList(amBizPara);
        ExcelUtil<AmBizPara> util = new ExcelUtil<AmBizPara>(AmBizPara.class);
        return util.exportExcel(list, "para");
    }

    /**
     * 转到新增页面
     * @return
     */
    @GetMapping("/labeladd")
    public String add()
    {

        return add_prefix + "/labeladd";
    }

    /**
     * 新增保存
     * @param amBizPara
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AmBizPara amBizPara)
    {
        return toAjax(amBizParaService.insertAmBizPara(amBizPara));
    }

    /**
     * 转到修改页面
     * @param amParaId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{amParaId}")
    public String edit(@PathVariable("amParaId") String amParaId, ModelMap mmap)
    {
        AmBizPara amBizPara = amBizParaService.selectAmBizParaById(amParaId);
        mmap.put("amBizPara", amBizPara);
        return prefix + "/edit";
    }

    /**
     * 修改保存
     * @param amBizPara
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AmBizPara amBizPara)
    {
        return toAjax(amBizParaService.updateAmBizPara(amBizPara));
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(amBizParaService.deleteAmBizParaByIds(ids));
    }
}
