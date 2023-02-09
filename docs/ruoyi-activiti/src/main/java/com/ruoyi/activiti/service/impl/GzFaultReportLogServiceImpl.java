package com.ruoyi.activiti.service.impl;


import com.ruoyi.activiti.mapper.GzFaultReportLogMapper;
import com.ruoyi.activiti.service.IGzFaultReportLogService;
import com.ruoyi.common.core.domain.entity.GzFaultReport;
import com.ruoyi.common.core.domain.entity.GzFaultReportLog;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 故障报告记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-08-09
 */
@Service
public class GzFaultReportLogServiceImpl implements IGzFaultReportLogService
{
    @Autowired
    private GzFaultReportLogMapper gzFaultReportLogMapper;

    /**
     * 查询故障报告记录
     * 
     * @param gzLogId 故障报告记录ID
     * @return 故障报告记录
     */
    @Override
    public GzFaultReportLog selectGzFaultReportLogById(String gzLogId)
    {
        return gzFaultReportLogMapper.selectGzFaultReportLogById(gzLogId);
    }

    /**
     * 查询故障报告记录列表
     * 
     * @param gzFaultReportLog 故障报告记录
     * @return 故障报告记录
     */
    @Override
    public List<GzFaultReportLog> selectGzFaultReportLogList(GzFaultReportLog gzFaultReportLog)
    {
        return gzFaultReportLogMapper.selectGzFaultReportLogList(gzFaultReportLog);
    }

    /**
     * 新增故障报告记录
     * 
     * @param gzFaultReportLog 故障报告记录
     * @return 结果
     */
    @Override
    public int insertGzFaultReportLog(GzFaultReportLog gzFaultReportLog)
    {
        gzFaultReportLog.setCreateTime(DateUtils.getNowDate());
        return gzFaultReportLogMapper.insertGzFaultReportLog(gzFaultReportLog);
    }

    /**
     * 新增故障报告记录
     *
     * @param report 故障报告
     * @return 结果
     */
    @Override
    public int insertGzFaultReportLog(GzFaultReport report)
    {
        // 插入故障报告单记录表
        GzFaultReportLog gzFaultReportLog = new GzFaultReportLog();
        BeanUtils.copyBeanProp(gzFaultReportLog, report);
        gzFaultReportLog.setGzLogId(UUID.getUUIDStr());
        gzFaultReportLog.setCreateTime(new Date());
        gzFaultReportLog.setCreateBy(ShiroUtils.getUserId());
        return gzFaultReportLogMapper.insertGzFaultReportLog(gzFaultReportLog);
    }

    /**
     * 修改故障报告记录
     * 
     * @param gzFaultReportLog 故障报告记录
     * @return 结果
     */
    @Override
    public int updateGzFaultReportLog(GzFaultReportLog gzFaultReportLog)
    {
        return gzFaultReportLogMapper.updateGzFaultReportLog(gzFaultReportLog);
    }

    /**
     * 删除故障报告记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGzFaultReportLogByIds(String ids)
    {
        return gzFaultReportLogMapper.deleteGzFaultReportLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除故障报告记录信息
     * 
     * @param gzLogId 故障报告记录ID
     * @return 结果
     */
    @Override
    public int deleteGzFaultReportLogById(String gzLogId)
    {
        return gzFaultReportLogMapper.deleteGzFaultReportLogById(gzLogId);
    }
}
