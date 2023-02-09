package com.ruoyi.activiti.mapper;


import com.ruoyi.common.core.domain.entity.GzFaultReportLog;

import java.util.List;

/**
 * 故障报告记录Mapper接口
 * 
 * @author ruoyi
 * @date 2021-08-09
 */
public interface GzFaultReportLogMapper 
{
    /**
     * 查询故障报告记录
     * 
     * @param gzLogId 故障报告记录ID
     * @return 故障报告记录
     */
    public GzFaultReportLog selectGzFaultReportLogById(String gzLogId);

    /**
     * 查询故障报告记录列表
     * 
     * @param gzFaultReportLog 故障报告记录
     * @return 故障报告记录集合
     */
    public List<GzFaultReportLog> selectGzFaultReportLogList(GzFaultReportLog gzFaultReportLog);

    /**
     * 新增故障报告记录
     * 
     * @param gzFaultReportLog 故障报告记录
     * @return 结果
     */
    public int insertGzFaultReportLog(GzFaultReportLog gzFaultReportLog);

    /**
     * 修改故障报告记录
     * 
     * @param gzFaultReportLog 故障报告记录
     * @return 结果
     */
    public int updateGzFaultReportLog(GzFaultReportLog gzFaultReportLog);

    /**
     * 删除故障报告记录
     * 
     * @param gzLogId 故障报告记录ID
     * @return 结果
     */
    public int deleteGzFaultReportLogById(String gzLogId);

    /**
     * 批量删除故障报告记录
     * 
     * @param gzLogIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteGzFaultReportLogByIds(String[] gzLogIds);
}
