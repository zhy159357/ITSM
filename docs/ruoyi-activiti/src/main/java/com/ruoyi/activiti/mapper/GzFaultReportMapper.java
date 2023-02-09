package com.ruoyi.activiti.mapper;


import com.ruoyi.common.core.domain.entity.GzFaultReport;

import java.util.List;

/**
 * 【故障报告单】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-08-09
 */
public interface GzFaultReportMapper 
{
    /**
     * 查询【故障报告单】
     * 
     * @param gzId 【故障报告单】ID
     * @return 【故障报告单】
     */
    public GzFaultReport selectGzFaultReportById(String gzId);

    public GzFaultReport selectGzFaultReportByCondition(GzFaultReport gzFaultReport);

    /**
     * 查询【故障报告单】列表
     * 
     * @param gzFaultReport 【故障报告单】
     * @return 【故障报告单】集合
     */
    public List<GzFaultReport> selectGzFaultReportList(GzFaultReport gzFaultReport);

    /**
     * 新增【故障报告单】
     * 
     * @param gzFaultReport 【故障报告单】
     * @return 结果
     */
    public int insertGzFaultReport(GzFaultReport gzFaultReport);

    /**
     * 修改【故障报告单】
     * 
     * @param gzFaultReport 【故障报告单】
     * @return 结果
     */
    public int updateGzFaultReport(GzFaultReport gzFaultReport);

    /**
     * 删除【故障报告单】
     * 
     * @param gzId 【故障报告单】ID
     * @return 结果
     */
    public int deleteGzFaultReportById(String gzId);

    /**
     * 批量删除【故障报告单】
     * 
     * @param gzIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteGzFaultReportByIds(String[] gzIds);

    public GzFaultReport selectGzFaultReportByIdOtherCondition(GzFaultReport gzFaultReport);
}
