package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.DutySubstitute;

import java.util.List;

/**
 * 参数类表Mapper接口
 * @author dayong_sun
 * @date 2020-12-06
 */
public interface DutySubstituteMapper
{

    /**
     * 查询列表
     * @param dutySubstitute 替班信息
     * @return 参数类表集合
     */
    public List<DutySubstitute> selectSubstituteList(DutySubstitute dutySubstitute);


    /**
     * 根据id查询
     * @param substituteId 替班信息
     * @return 版本数据集合信息
     */
    public DutySubstitute selectSubstituteById(String substituteId);

    /**
     * 新增替班申请
     * @param dutySubstitute 替班信息
     * @return 结果
     */
    public DutySubstitute selectSubstituteBySchedulingId(DutySubstitute dutySubstitute);

    /**
     * 新增替班申请
     * @param dutySubstitute 替班信息
     * @return 结果
     */
    public int insertDutySubstitute(DutySubstitute dutySubstitute);

    /**
     * 新增替班申请
     * @param dutySubstitute 替班信息
     * @return 结果
     */
    public int updateDutySubstitute(DutySubstitute dutySubstitute);
}
