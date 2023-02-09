package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.PubFlowLog;

import java.util.List;

/**
 * 流程记录Service接口
 *
 * @author ruoyi
 * @date 2020-12-25
 */
public interface IPubFlowLogService {
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
     * 批量删除流程记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePubFlowLogByIds(String ids);

    /**
     * 删除流程记录信息
     *
     * @param logId 流程记录ID
     * @return 结果
     */
    public int deletePubFlowLogById(String logId);

    /**
     * 查询流程记录biz_id
     *
     * @param pubFlowLog 流程记录
     * @return 流程记录集合
     */
    public List<String> selectPubFlowLogBizList(PubFlowLog pubFlowLog);

    /**
     * 根据业务ID查询流程记录 并且根据操作时间倒叙
     */
    public List<PubFlowLog> selectPubFlowLogAll(String bizId);

    /**
     * 根据业务ID正序查询所有相关流程记录
     */
    public List<PubFlowLog> selectPubFlowLogAsc(String bizId);

    /**
     * 根据bizid查询相关流程记录
     *
     * @param bizId
     * @return
     */
    public List<PubFlowLog> selectPubFlowLogDesc(String bizId);

    public List<PubFlowLog> findFmIds(String performerTime);

    /**
     * 根据传入参数查询已办任务列表
     *
     * @param pId 柜员号
     *            type   任务类型 VmBn-发布管理 CmZy-资源变更单 BGQQ-变更请求单
     * @return 已办列表
     */
    public List<Object> getAlreadyTasks(String pId, String logType, String search);

    public int selectPubFlowLogByBusinessKey(String businessKey);

    public List<PubFlowLog> selectPubFlowLogEmpList(PubFlowLog pubFlowLog);
}
