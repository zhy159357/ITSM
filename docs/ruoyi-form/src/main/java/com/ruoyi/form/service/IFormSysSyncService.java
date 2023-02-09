package com.ruoyi.form.service;

import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2022-10-31
 */
public interface IFormSysSyncService {
	/**
	 * 查询【请填写功能名称】
	 * 
	 * @param id 【请填写功能名称】ID
	 * @return 【请填写功能名称】
	 */
	AjaxResult sysSyncSh(String adpmSysUrl,String valueDate);
}
