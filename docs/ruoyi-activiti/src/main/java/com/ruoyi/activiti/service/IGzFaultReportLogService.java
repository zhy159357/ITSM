package com.ruoyi.activiti.service;


import com.ruoyi.common.core.domain.entity.GzFaultReport;
import com.ruoyi.common.core.domain.entity.GzFaultReportLog;

import java.util.List;

/**
 * 故障报告记录Service接口
 * 
 * @author ruoyi
 * @date 2021-08-09
 */
public interface IGzFaultReportLogService 
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
     * 新增故障报告记录
     *
     * @param report 故障报告
     * @return 结果
     */
    public int insertGzFaultReportLog(GzFaultReport report);

    /**
     * 修改故障报告记录
     * 
     * @param gzFaultReportLog 故障报告记录
     * @return 结果
     */
    public int updateGzFaultReportLog(GzFaultReportLog gzFaultReportLog);

    /**
     * 批量删除故障报告记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGzFaultReportLogByIds(String ids);

    /**
     * 删除故障报告记录信息
     * 
     * @param gzLogId 故障报告记录ID
     * @return 结果
     */
    public int deleteGzFaultReportLogById(String gzLogId);
}
