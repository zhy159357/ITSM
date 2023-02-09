package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.TelFlowLog;
import com.ruoyi.activiti.mapper.TelFlowLogMapper;
import com.ruoyi.activiti.service.ITelFlowLogService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-06-18
 */
@Service
public class TelFlowLogServiceImpl implements ITelFlowLogService
{
    @Autowired
    private TelFlowLogMapper telFlowLogMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param logId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public TelFlowLog selectTelFlowLogById(String logId)
    {
        return telFlowLogMapper.selectTelFlowLogById(logId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param telFlowLog 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TelFlowLog> selectTelFlowLogList(TelFlowLog telFlowLog)
    {
        return telFlowLogMapper.selectTelFlowLogList(telFlowLog);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param telFlowLog 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTelFlowLog(TelFlowLog telFlowLog)
    {
        telFlowLog.setCreateTime(DateUtils.getNowDate());
        return telFlowLogMapper.insertTelFlowLog(telFlowLog);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param telFlowLog 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTelFlowLog(TelFlowLog telFlowLog)
    {
        return telFlowLogMapper.updateTelFlowLog(telFlowLog);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTelFlowLogByIds(String ids)
    {
        return telFlowLogMapper.deleteTelFlowLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param logId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTelFlowLogById(String logId)
    {
        return telFlowLogMapper.deleteTelFlowLogById(logId);
    }
}
