package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.ItsmWorkStatusLog;
import com.ruoyi.activiti.mapper.ItsmWorkStatusLogMapper;
import com.ruoyi.activiti.service.ItsmWorkStatusLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * Service业务层处理
 *
 * @author ruoyi
 * @date 2021-06-25
 */
@Service
public class ItsmWorkStatusLogServiceImpl implements ItsmWorkStatusLogService
{
    @Autowired
    private ItsmWorkStatusLogMapper itsmWorkStatusLogMapper;

    /**
     * 查询
     *
     * @param logId ID
     * @return
     */
    @Override
    public ItsmWorkStatusLog selectItsmWorkStatusLogById(String logId)
    {
        return itsmWorkStatusLogMapper.selectItsmWorkStatusLogById(logId);
    }

    /**
     * 查询列表
     *
     * @param itsmWorkStatusLog
     * @return 【请填写功能名称】
     */
    @Override
    public List<ItsmWorkStatusLog> selectItsmWorkStatusLogList(ItsmWorkStatusLog itsmWorkStatusLog)
    {
        return itsmWorkStatusLogMapper.selectItsmWorkStatusLogList(itsmWorkStatusLog);
    }

    /**
     * 新增
     *
     * @param itsmWorkStatusLog
     * @return 结果
     */
    @Override
    public int insertItsmWorkStatusLog(ItsmWorkStatusLog itsmWorkStatusLog)
    {
        return itsmWorkStatusLogMapper.insertItsmWorkStatusLog(itsmWorkStatusLog);
    }

    /**
     * 修改
     *
     * @param itsmWorkStatusLog
     * @return 结果
     */
    @Override
    public int updateItsmWorkStatusLog(ItsmWorkStatusLog itsmWorkStatusLog)
    {
        return itsmWorkStatusLogMapper.updateItsmWorkStatusLog(itsmWorkStatusLog);
    }

    /**
     * 删除对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteItsmWorkStatusLogByIds(String ids)
    {
        return itsmWorkStatusLogMapper.deleteItsmWorkStatusLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除信息
     *
     * @param logId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteItsmWorkStatusLogById(String logId)
    {
        return itsmWorkStatusLogMapper.deleteItsmWorkStatusLogById(logId);
    }
}
