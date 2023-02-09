package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.common.core.domain.entity.CustomFormJsonVo;

public interface CustomFormJsonMapper {

	public List<CustomFormJsonVo> select(CustomFormJsonVo customFormJsonVo);

	CustomFormJsonVo selectById(CustomFormJsonVo customFormJsonVo);

	int edit(CustomFormJsonVo customFormJsonVo);
	int insert(CustomFormJsonVo customFormJsonVo);
	
	public List<CustomFormJsonVo> selectTemplate(CustomFormJsonVo customFormJsonVo);
	
	CustomFormJsonVo selectByIdTemplate(CustomFormJsonVo customFormJsonVo);
	
	int editTemplate(CustomFormJsonVo customFormJsonVo);
	
	int insertTemplate(CustomFormJsonVo customFormJsonVo);
}
