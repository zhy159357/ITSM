package com.ruoyi.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.CustomFormJsonVo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ICustomFormJsonService;

@Controller
@RequestMapping("/system/formjson")
public class CustomFormJsonController extends BaseController {

	private String prefix = "system/formjson";
	private String prefix_temp = "system/formjson/template";

	@Autowired
	private ICustomFormJsonService customFormJsonService;

    @GetMapping()
    public String formjson()
    {
        return prefix + "/list";
    }
/**
 * 修改design_form_version数据入口
 * @param customFormJsonVo
 * @return
 */
	
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CustomFormJsonVo customFormJsonVo) {
		startPage();
		List<CustomFormJsonVo> list = customFormJsonService.select(customFormJsonVo);
		return getDataTable(list);
	}
/**
 * 修改design_form_version
 * @param id
 * @param mmap
 * @return
 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		CustomFormJsonVo customFormJsonVo = new CustomFormJsonVo();
		customFormJsonVo.setId(id);
		mmap.put("customForm", customFormJsonService.selectById(customFormJsonVo));
		return prefix + "/edit";
	}

	@Log(title = "修改", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated CustomFormJsonVo customFormJsonVo) {
		return toAjax(customFormJsonService.edit(customFormJsonVo));

	}
/**
 * 修改approve_template入口
 * @return
 */
    @GetMapping("/template")
    public String approtemplate()
    {
        return prefix_temp + "/list";
    }

	
	@PostMapping("/template/list")
	@ResponseBody
	public TableDataInfo templatelist(CustomFormJsonVo customFormJsonVo) {
		startPage();
		List<CustomFormJsonVo> list = customFormJsonService.selectTemplate(customFormJsonVo);
		return getDataTable(list);
	}
/**
 * 修改approve_template
 * @param id
 * @param mmap
 * @return
 */
	@GetMapping("/template/edit/{id}")
	public String templateedit(@PathVariable("id") String id, ModelMap mmap) {
		CustomFormJsonVo customFormJsonVo = new CustomFormJsonVo();
		customFormJsonVo.setId(id);
		mmap.put("customTemplateForm", customFormJsonService.selectByIdTemplate(customFormJsonVo));
		return prefix_temp + "/edit";
	}

	@Log(title = "修改", businessType = BusinessType.UPDATE)
	@PostMapping("/template/edit")
	@ResponseBody
	public AjaxResult templatesave(@Validated CustomFormJsonVo customFormJsonVo) {
		return toAjax(customFormJsonService.editTemplate(customFormJsonVo));

	}
}
