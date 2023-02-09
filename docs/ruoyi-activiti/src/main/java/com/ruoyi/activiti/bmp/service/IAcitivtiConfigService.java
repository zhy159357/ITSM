package com.ruoyi.activiti.bmp.service;

import java.util.List;
import java.util.Map;

public interface IAcitivtiConfigService {
    /**
     * 获取项目中所有的activiti流程监听器
     * 支持使用监听器作为搜索条件 key=listenerName
     * @return
     */
    List<Map<String, Object>> getActivityListeners(String listenerName);

    /**
     * 查询流程用户
     * @param params
     * @return
     */
    List<Map<String,Object>> selectActivityUsers(Map<String, Object> params);

    /**
     * 查询流程用户组
     * @param params
     * @return
     */
    List<Map<String, Object>> selectActivityGroups(Map<String, Object> params);

    /**
     * 查询用户或者工作组的id对应的名称
     */
    String selectUserGroupByIds(String ids, Integer type);

    /**
     * 初始化首选人列表时根据id反显对应的代理人、候选人、候选组名称
     * @param assignee
     * @param candidateUser
     * @param candidateGroup
     * @return
     */
    Map<String,String> showUserGroupName(String assignee, String candidateUser, String candidateGroup);
}
