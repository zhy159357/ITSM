package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgSysLog;

import java.util.List;

public interface OgSysLogMapper {

    /**
     * 查询
     *
     * @param logId ID
     * @return
     */
    public OgSysLog selectOgSysLogById(String logId);

    /**
     * 查询列表
     *
     * @param ogSysLog
     * @return 集合
     */
    public List<OgSysLog> selectOgSysLogList(OgSysLog ogSysLog);

    /**
     * 新增
     *
     * @param ogSysLog
     * @return 结果
     */
    public int insertOgSysLog(OgSysLog ogSysLog);

    /**
     * 修改
     *
     * @param ogSysLog
     * @return 结果
     */
    public int updateOgSysLog(OgSysLog ogSysLog);

    /**
     * 删除
     *
     * @param logId ID
     * @return 结果
     */
    public int deleteOgSysLogById(String logId);

    /**
     * 批量删除
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgSysLogByIds(String[] logIds);
}
