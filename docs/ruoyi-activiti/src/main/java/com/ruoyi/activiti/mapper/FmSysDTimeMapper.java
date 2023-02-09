package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.FmSysDTime;
import java.util.List;

/**
 * 业务事件单系统处理用时Mapper接口
 * 
 * @author ruoyi
 * @date 2021-01-21
 */
public interface FmSysDTimeMapper 
{
    /**
     * 查询业务事件单系统处理用时
     * 
     * @param fmSysDTimeId 业务事件单系统处理用时ID
     * @return 业务事件单系统处理用时
     */
    public FmSysDTime selectFmSysDTimeById(String fmSysDTimeId);

    /**
     * 查询业务事件单系统处理用时列表
     * 
     * @param fmSysDTime 业务事件单系统处理用时
     * @return 业务事件单系统处理用时集合
     */
    public List<FmSysDTime> selectFmSysDTimeList(FmSysDTime fmSysDTime);

    /**
     * 新增业务事件单系统处理用时
     * 
     * @param fmSysDTime 业务事件单系统处理用时
     * @return 结果
     */
    public int insertFmSysDTime(FmSysDTime fmSysDTime);

    /**
     * 修改业务事件单系统处理用时
     * 
     * @param fmSysDTime 业务事件单系统处理用时
     * @return 结果
     */
    public int updateFmSysDTime(FmSysDTime fmSysDTime);

    /**
     * 删除业务事件单系统处理用时
     * 
     * @param fmSysDTimeId 业务事件单系统处理用时ID
     * @return 结果
     */
    public int deleteFmSysDTimeById(String fmSysDTimeId);

    /**
     * 批量删除业务事件单系统处理用时
     * 
     * @param fmSysDTimeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmSysDTimeByIds(String[] fmSysDTimeIds);
}
