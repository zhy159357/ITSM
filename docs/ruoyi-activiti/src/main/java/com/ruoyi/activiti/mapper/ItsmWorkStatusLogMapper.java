package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.ItsmWorkStatusLog;

import java.util.List;

/**
 * Mapper接口
 *
 * @author ruoyi
 * @date 2021-06-25
 */
public interface ItsmWorkStatusLogMapper
{
    /**
     * 查询
     *
     * @param logId ID
     * @return
     */
    public ItsmWorkStatusLog selectItsmWorkStatusLogById(String logId);

    /**
     * 查询列表
     *
     * @param itsmWorkStatusLog
     * @return 集合
     */
    public List<ItsmWorkStatusLog> selectItsmWorkStatusLogList(ItsmWorkStatusLog itsmWorkStatusLog);

    /**
     * 新增
     *
     * @param itsmWorkStatusLog
     * @return 结果
     */
    public int insertItsmWorkStatusLog(ItsmWorkStatusLog itsmWorkStatusLog);

    /**
     * 修改
     *
     * @param itsmWorkStatusLog
     * @return 结果
     */
    public int updateItsmWorkStatusLog(ItsmWorkStatusLog itsmWorkStatusLog);

    /**
     * 删除
     *
     * @param logId ID
     * @return 结果
     */
    public int deleteItsmWorkStatusLogById(String logId);

    /**
     * 批量删除
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteItsmWorkStatusLogByIds(String[] logIds);
}
