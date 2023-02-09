package com.ruoyi.system.service;

import com.ruoyi.system.http.entegorserver.entity.InstanceStartup;
import com.ruoyi.system.http.entegorserver.entity.UploadMsg;

import java.util.List;
import java.util.Map;

/**
 * 与自动化对接接口
 *
 * @author 14735
 */
public interface IEntegorServer {

    /**
     * 上传审批启动
     *
     * @param msg
     * @return
     */
    Map<String, Object> sendMessageUpload(UploadMsg msg);

    /**
     * 实例启动(仅仅启动)
     *
     * @param instances
     * @return
     */
    Map<String, Object> sendMessageInstanceStartup(List<InstanceStartup> instances, String mobilPhone);

    /**
     * 查询自动化结果
     *
     * @param map
     * @return
     */
    Map<String, Object> selectResultMsg(Map map);

    /**
     * 通知自动化禁用版本任务
     *
     * @param map
     */

    void forbiddenVersion(Map map);

    /**
     * 根据系统编码查询对应脚本列表
     *
     * @param sysCode-系统编码
     * @return 脚本列表
     */
    List<Map<String, String>> getCommonTaskList(String sysCode);

    /**
     * 根据脚本id查询对应的参数列表
     *
     * @param commonTaskId-脚本ID
     * @return 参数列表
     */
    List<Map<String, Object>> getCommonTaskInfo(String commonTaskId);

    /**
     * 根据脚本id和脚本所需参数启动脚本
     *
     * @param type 参数
     * @return
     */
    Map<String, Object> startCommonTaskByConfig(String type);

    /**
     * 根据步骤ID查询对应步骤脚本执行状态
     *
     * @param stepId-步骤ID
     * @return 状态查询结果
     */
    Map<String, Object> monitorActivityForFlowIdLog(String stepId);

    /**
     * （数据变更单）根据系统编码和系统名称查询对应服务列表
     *
     * @param sysFlag
     * @param sysName
     * @return
     */
    List<Map<String, String>> getDataCommonTaskList(String sysFlag, String sysName);

    /**
     * (数据变更单)根据服务id和服务name进行对应参数信息的列表数据查询
     *
     * @param paramMap
     * @return
     */
    List<Map<String, String>> getServiceParamList(List<Map<String, Object>> paramMap);

    /**
     * 根据任务名称查询脚本列表
     *
     * @param taskName
     * @return
     */
    List<Map<String, String>> getCommonTaskListByQuery(String taskName);
}
