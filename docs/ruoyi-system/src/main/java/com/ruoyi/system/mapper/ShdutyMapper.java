package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.common.core.domain.entity.ShDuty;

public interface ShdutyMapper {

	public List<ShDuty> selectShDutyList(ShDuty shDuty);

	ShDuty selectShDutyById(ShDuty shDuty);
	ShDuty selectDutyByIdV(ShDuty shDuty);

	int deleteDutyByIds(String[] ids);
	int deleteDutyBeforeDays(String chddStr);
	/**
	 * 新增
	 * 
	 * @param pubHoliday
	 * @return
	 */
	int insert(ShDuty shDuty);

	/**
	 * 修改
	 * 
	 * @param holidayoliday
	 * @return
	 */
	int edit(ShDuty shDuty);
	
	int editSync(ShDuty shDuty);

	Object selectShDutyByDutyId(String id);

	ShDuty queryDutyPerson(ShDuty sd);
	
	int deleteShDutyById(ShDuty shDuty);
}
