package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.domain.entity.CustomFormJsonVo;
import com.ruoyi.system.mapper.CustomFormJsonMapper;
import com.ruoyi.system.service.ICustomFormJsonService;

@Service
public class CustomFormJsonServiceImpl implements ICustomFormJsonService {

	@Autowired
	private CustomFormJsonMapper customFormJsonMapper;

	public List<CustomFormJsonVo> select(CustomFormJsonVo customFormJsonVo) {
		return customFormJsonMapper.select(customFormJsonVo);
	}

	public CustomFormJsonVo selectById(CustomFormJsonVo customFormJsonVo) {
		return customFormJsonMapper.selectById(customFormJsonVo);
	}

	/**
	 * 修改
	 *
	 * @param shdutyMapper
	 * @return
	 */
	@Override
	public int edit(CustomFormJsonVo customFormJsonVo) {
		CustomFormJsonVo customFormJsonVo1 = customFormJsonMapper.selectById(customFormJsonVo);
		customFormJsonMapper.insert(customFormJsonVo1);
		return customFormJsonMapper.edit(customFormJsonVo);
	}

	public List<CustomFormJsonVo> selectTemplate(CustomFormJsonVo customFormJsonVo) {
		return customFormJsonMapper.selectTemplate(customFormJsonVo);
	}

	public CustomFormJsonVo selectByIdTemplate(CustomFormJsonVo customFormJsonVo) {
		return customFormJsonMapper.selectByIdTemplate(customFormJsonVo);
	}

	/**
	 * 修改
	 *
	 * @param shdutyMapper
	 * @return
	 */
	@Override
	public int editTemplate(CustomFormJsonVo customFormJsonVo) {
		CustomFormJsonVo customFormJsonVo1 = customFormJsonMapper.selectByIdTemplate(customFormJsonVo);
		customFormJsonMapper.insertTemplate(customFormJsonVo1);
		return customFormJsonMapper.editTemplate(customFormJsonVo);
	}
}