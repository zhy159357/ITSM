package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.entity.ShDuty;

public interface IShDutyService {

	// 列表页面展示
	List<ShDuty> selectDutyList(ShDuty shDuty);

	Object selectDutyById(ShDuty shDuty);
	Object selectDutyByIdV(ShDuty shDuty);

	public int deleteDutyByIds(String ids);

	/**
	 * 删除某天之前的数据
	 * @param chddStr
	 * @return
	 */
	public int deleteDutyBeforeDays(String chddStr);
	/**
	 * 新增
	 *
	 * @param ShDuty
	 * @return
	 */
	int insert(ShDuty shDuty);

	/**
	 * 修改节假日
	 *
	 * @param holiday
	 * @return
	 */
	int edit(ShDuty shDuty);

	int sync();
	
	ShDuty queryDutyPerson(String startTime,String groupName);

}
