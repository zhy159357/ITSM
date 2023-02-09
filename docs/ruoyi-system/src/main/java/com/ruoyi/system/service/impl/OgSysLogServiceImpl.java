package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.OgSysLog;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.mapper.OgSysLogMapper;
import com.ruoyi.system.service.OgSysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用系统管理日志业务层处理
 */
@Service
public class OgSysLogServiceImpl implements OgSysLogService {

    @Autowired
    private OgSysLogMapper ogSysLogMapper;

    /**
     * 查询
     *
     * @param logId ID
     * @return
     */
    @Override
    public OgSysLog selectOgSysLogById(String logId)
    {
        return ogSysLogMapper.selectOgSysLogById(logId);
    }

    /**
     * 查询列表
     *
     * @param ogSysLog
     * @return
     */
    @Override
    public List<OgSysLog> selectOgSysLogList(OgSysLog ogSysLog)
    {
        return ogSysLogMapper.selectOgSysLogList(ogSysLog);
    }

    /**
     * 新增
     *
     * @param ogSysLog
     * @return 结果
     */
    @Override
    public int insertOgSysLog(OgSysLog ogSysLog)
    {
        return ogSysLogMapper.insertOgSysLog(ogSysLog);
    }

    /**
     * 修改
     *
     * @param ogSysLog
     * @return 结果
     */
    @Override
    public int updateOgSysLog(OgSysLog ogSysLog)
    {
        return ogSysLogMapper.updateOgSysLog(ogSysLog);
    }

    /**
     * 删除对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOgSysLogByIds(String ids)
    {
        return ogSysLogMapper.deleteOgSysLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除信息
     *
     * @param logId ID
     * @return 结果
     */
    @Override
    public int deleteOgSysLogById(String logId)
    {
        return ogSysLogMapper.deleteOgSysLogById(logId);
    }

}
