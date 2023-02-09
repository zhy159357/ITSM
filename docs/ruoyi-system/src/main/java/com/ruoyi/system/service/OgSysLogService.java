package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgSysLog;

import java.util.List;

public interface OgSysLogService {

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
     * 批量删除
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgSysLogByIds(String ids);

    /**
     * 删除信息
     *
     * @param logId ID
     * @return 结果
     */
    public int deleteOgSysLogById(String logId);

}
