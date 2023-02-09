package com.ruoyi.activiti.mapper;


import com.ruoyi.activiti.domain.PubFlowLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 流程记录Mapper接口
 *
 * @author ruoyi
 * @date 2020-12-25
 */
@Component
public interface PubFlowLogMapper {
    /**
     * 查询流程记录
     *
     * @param logId 流程记录ID
     * @return 流程记录
     */
    public PubFlowLog selectPubFlowLogById(String logId);

    /**
     * 查询流程记录列表
     *
     * @param pubFlowLog 流程记录
     * @return 流程记录集合
     */
    public List<PubFlowLog> selectPubFlowLogList(PubFlowLog pubFlowLog);

    /**
     * 查询流程记录biz_id
     *
     * @param pubFlowLog 流程记录
     * @return 流程记录集合
     */
    public List<String> selectPubFlowLogBizList(PubFlowLog pubFlowLog);

    /**
     * 新增流程记录
     *
     * @param pubFlowLog 流程记录
     * @return 结果
     */
    public int insertPubFlowLog(PubFlowLog pubFlowLog);

    /**
     * 修改流程记录
     *
     * @param pubFlowLog 流程记录
     * @return 结果
     */
    public int updatePubFlowLog(PubFlowLog pubFlowLog);

    /**
     * 删除流程记录
     *
     * @param logId 流程记录ID
     * @return 结果
     */
    public int deletePubFlowLogById(String logId);

    /**
     * 批量删除流程记录
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePubFlowLogByIds(String[] logIds);

    public List<PubFlowLog> selectPubFlowLogAll(String bizId);

    public List<PubFlowLog> selectPubFlowLogAsc(String bizId);

    public List<PubFlowLog> selectPubFlowLogDesc(String bizId);

    List<PubFlowLog> findFmIds(String performerTime);

    int selectPubFlowLogByBusinessKey(String businessKey);

    public List<PubFlowLog> selectPubFlowLogEmpList(PubFlowLog pubFlowLog);


    PubFlowLog selectPubFlowLogByCondition(@Param("logType") String logType,@Param("bizId") String bizId,@Param("taskName") String taskName);
}
