package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.mapper.GzFaultReportMapper;
import com.ruoyi.activiti.service.IGzFaultReportService;
import com.ruoyi.common.core.domain.entity.GzFaultReport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【故障报告单】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-08-09
 */
@Service
public class GzFaultReportServiceImpl implements IGzFaultReportService
{
    @Autowired
    private GzFaultReportMapper gzFaultReportMapper;

    /**
     * 查询【故障报告单】
     * 
     * @param gzId 【故障报告单】ID
     * @return 【故障报告单】
     */
    @Override
    public GzFaultReport selectGzFaultReportById(String gzId)
    {
        return gzFaultReportMapper.selectGzFaultReportById(gzId);
    }

    @Override
    public GzFaultReport selectGzFaultReportByCondition(GzFaultReport gzFaultReport)
    {
        return gzFaultReportMapper.selectGzFaultReportByCondition(gzFaultReport);
    }

    /**
     * 查询【故障报告单】列表
     * 
     * @param gzFaultReport 【故障报告单】
     * @return 【故障报告单】
     */
    @Override
    public List<GzFaultReport> selectGzFaultReportList(GzFaultReport gzFaultReport)
    {
        return gzFaultReportMapper.selectGzFaultReportList(gzFaultReport);
    }

    /**
     * 新增【故障报告单】
     * 
     * @param gzFaultReport 【故障报告单】
     * @return 结果
     */
    @Override
    public int insertGzFaultReport(GzFaultReport gzFaultReport)
    {
        gzFaultReport.setCreateTime(DateUtils.getNowDate());
        return gzFaultReportMapper.insertGzFaultReport(gzFaultReport);
    }

    /**
     * 修改【故障报告单】
     * 
     * @param gzFaultReport 【故障报告单】
     * @return 结果
     */
    @Override
    public int updateGzFaultReport(GzFaultReport gzFaultReport)
    {
        gzFaultReport.setUpdateTime(DateUtils.getNowDate());
        return gzFaultReportMapper.updateGzFaultReport(gzFaultReport);
    }

    /**
     * 删除【故障报告单】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGzFaultReportByIds(String ids)
    {
        return gzFaultReportMapper.deleteGzFaultReportByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【故障报告单】信息
     * 
     * @param gzId 【故障报告单】ID
     * @return 结果
     */
    @Override
    public int deleteGzFaultReportById(String gzId)
    {
        return gzFaultReportMapper.deleteGzFaultReportById(gzId);
    }

    @Override
    public GzFaultReport selectGzFaultReportByIdOtherCondition(GzFaultReport gzFaultReport) {
        return gzFaultReportMapper.selectGzFaultReportByIdOtherCondition(gzFaultReport);
    }
}
