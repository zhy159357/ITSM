package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.Leadingdepartment;

import java.util.List;


public interface ILeadingdepartmentService {

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    int insert(Leadingdepartment entity);

    //
    public int deletelpByIds(String ids);


    //列表页面展示
    List<Leadingdepartment> selectLpList(Leadingdepartment leadingdepartment);


}
