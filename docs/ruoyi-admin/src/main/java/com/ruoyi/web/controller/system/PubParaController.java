package com.ruoyi.web.controller.system;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.PubPara;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IPubParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/system/dict")
@Profile("duct")
public class PubParaController  extends BaseController {

    private String prefix = "system/dict/type";
    @Autowired
    private IPubParaService pubParaService;


    @GetMapping()
    public String dictType()
    {
        return prefix + "/type";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PubPara pubPara)
    {
        startPage();
        List<PubPara> list = pubParaService.selectPubParaList(pubPara);
        return getDataTable(list);
    }

    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated PubPara pubPara)
    {
        if (UserConstants.DICT_TYPE_NOT_UNIQUE.equals(pubParaService.checkParaNameUnique(pubPara)))
        {
            return error("新增参数项'" + pubPara.getParaName() + "'失败，参数项代码已存在");
        }
        pubPara.setParaId(UUID.getUUIDStr());
        pubPara.setState("1");
        return toAjax(pubParaService.insertPubPara(pubPara));
    }

    /**
     * 修改参数页面
     * @param paraId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{dictId}")
    public String edit(@PathVariable("dictId") String  paraId, ModelMap mmap)
    {
        mmap.put("pubPara", pubParaService.selectPubParaById(paraId));
        return prefix + "/edit";
    }

    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult addEdit(PubPara pubPara){
        return toAjax(pubParaService.updatePubParaById(pubPara));
    }

}
