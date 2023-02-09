package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.DutySubstitute;

import java.util.List;

/**
 * 值班视图Service接口
 * @author dayong_sun
 * @date 2020-01-19
 */
public interface IDutySubstituteService
{
    /**
     * 查询替班信息
     * @param dutySubstitute
     * @return 替班信息
     */
    public List<DutySubstitute> selectSubstituteList(DutySubstitute dutySubstitute);

    public DutySubstitute selectSubstituteById(String substituteId);

    /**
     * 修改排班信息
     * @param dutySubstitute 排班信息
     * @return 结果
     */
    public int updateDutySubstitute(DutySubstitute dutySubstitute);

}
