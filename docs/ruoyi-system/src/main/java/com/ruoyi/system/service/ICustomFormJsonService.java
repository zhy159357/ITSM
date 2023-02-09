package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.core.domain.entity.CustomFormJsonVo;

public interface ICustomFormJsonService {

	// 列表页面展示
	List<CustomFormJsonVo> select(CustomFormJsonVo customFormJsonVo);

	CustomFormJsonVo selectById(CustomFormJsonVo customFormJsonVo);

	int edit(CustomFormJsonVo customFormJsonVo);
	
	List<CustomFormJsonVo> selectTemplate(CustomFormJsonVo customFormJsonVo);
	
	CustomFormJsonVo selectByIdTemplate(CustomFormJsonVo customFormJsonVo);
	
	int editTemplate(CustomFormJsonVo customFormJsonVo);

}
