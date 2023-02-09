package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.OrgTaskTest;
import com.ruoyi.activiti.domain.UserTaskTest;
import java.util.List;

/**
 * 用户考核Service接口
 * 
 * @author ruoyi
 * @date 2022-01-13
 */
public interface IUserTaskTestService 
{
    /**
     * 查询用户考核
     * 
     * @param id 用户考核ID
     * @return 用户考核
     */
    public UserTaskTest selectUserTaskTestById(String id);

    /**
     * 查询用户考核列表
     * 
     * @param userTaskTest 用户考核
     * @return 用户考核集合
     */
    public List<UserTaskTest> selectUserTaskTestList(UserTaskTest userTaskTest);

    /**
     * 新增用户考核
     * 
     * @param userTaskTest 用户考核
     * @return 结果
     */
    public int insertUserTaskTest(UserTaskTest userTaskTest);

    /**
     * 修改用户考核
     * 
     * @param userTaskTest 用户考核
     * @return 结果
     */
    public int updateUserTaskTest(UserTaskTest userTaskTest);

    /**
     * 批量删除用户考核
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserTaskTestByIds(String ids);

    /**
     * 删除用户考核信息
     * 
     * @param id 用户考核ID
     * @return 结果
     */
    public int deleteUserTaskTestById(String id);

    /**
     * 机构考核查询
     * @param userTaskTest
     * @return
     */
    List<OrgTaskTest> selectOrgTaskTestList(UserTaskTest userTaskTest);
}
