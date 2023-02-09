package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.TelFlowLog;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-06-18
 */
public interface ITelFlowLogService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param logId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TelFlowLog selectTelFlowLogById(String logId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param telFlowLog 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TelFlowLog> selectTelFlowLogList(TelFlowLog telFlowLog);

    /**
     * 新增【请填写功能名称】
     * 
     * @param telFlowLog 【请填写功能名称】
     * @return 结果
     */
    public int insertTelFlowLog(TelFlowLog telFlowLog);

    /**
     * 修改【请填写功能名称】
     * 
     * @param telFlowLog 【请填写功能名称】
     * @return 结果
     */
    public int updateTelFlowLog(TelFlowLog telFlowLog);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTelFlowLogByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param logId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTelFlowLogById(String logId);
}
