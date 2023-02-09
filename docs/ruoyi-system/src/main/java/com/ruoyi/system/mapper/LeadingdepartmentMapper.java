package com.ruoyi.system.mapper;


import com.ruoyi.common.core.domain.entity.Leadingdepartment;

import java.util.List;

/**
 * 牵头部门
 */
public interface LeadingdepartmentMapper {


    public List<Leadingdepartment> selectLpList(Leadingdepartment leadingdepartment);


    /**
     * 新增
     * @param leadingdepartment
     * @return
     */
    int insert(Leadingdepartment leadingdepartment);

    //批量删除
    int deleteHolidayByIds(String[] lpids);


}
