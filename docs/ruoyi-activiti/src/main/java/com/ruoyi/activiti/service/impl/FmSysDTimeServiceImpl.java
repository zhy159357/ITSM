package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.mapper.FmSysDTimeMapper;
import com.ruoyi.activiti.service.IFmSysDTimeService;
import com.ruoyi.common.core.domain.entity.FmSysDTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 业务事件单系统处理用时Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-01-21
 */
@Service
public class FmSysDTimeServiceImpl implements IFmSysDTimeService
{
    @Autowired
    private FmSysDTimeMapper fmSysDTimeMapper;

    /**
     * 查询业务事件单系统处理用时
     * 
     * @param fmSysDTimeId 业务事件单系统处理用时ID
     * @return 业务事件单系统处理用时
     */
    @Override
    public FmSysDTime selectFmSysDTimeById(String fmSysDTimeId)
    {
        return fmSysDTimeMapper.selectFmSysDTimeById(fmSysDTimeId);
    }

    /**
     * 查询业务事件单系统处理用时列表
     * 
     * @param fmSysDTime 业务事件单系统处理用时
     * @return 业务事件单系统处理用时
     */
    @Override
    public List<FmSysDTime> selectFmSysDTimeList(FmSysDTime fmSysDTime)
    {
        return fmSysDTimeMapper.selectFmSysDTimeList(fmSysDTime);
    }

    /**
     * 新增业务事件单系统处理用时
     * 
     * @param fmSysDTime 业务事件单系统处理用时
     * @return 结果
     */
    @Override
    public int insertFmSysDTime(FmSysDTime fmSysDTime)
    {
        return fmSysDTimeMapper.insertFmSysDTime(fmSysDTime);
    }

    /**
     * 修改业务事件单系统处理用时
     * 
     * @param fmSysDTime 业务事件单系统处理用时
     * @return 结果
     */
    @Override
    public int updateFmSysDTime(FmSysDTime fmSysDTime)
    {
        return fmSysDTimeMapper.updateFmSysDTime(fmSysDTime);
    }

    /**
     * 删除业务事件单系统处理用时对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmSysDTimeByIds(String ids)
    {
        return fmSysDTimeMapper.deleteFmSysDTimeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务事件单系统处理用时信息
     * 
     * @param fmSysDTimeId 业务事件单系统处理用时ID
     * @return 结果
     */
    @Override
    public int deleteFmSysDTimeById(String fmSysDTimeId)
    {
        return fmSysDTimeMapper.deleteFmSysDTimeById(fmSysDTimeId);
    }
}
