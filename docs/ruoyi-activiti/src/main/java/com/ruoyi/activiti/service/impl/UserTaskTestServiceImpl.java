package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.OrgTaskTest;
import com.ruoyi.activiti.mapper.UserTaskTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.activiti.domain.UserTaskTest;
import com.ruoyi.activiti.service.IUserTaskTestService;
import com.ruoyi.common.core.text.Convert;

/**
 * 用户考核Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-01-13
 */
@Service
public class UserTaskTestServiceImpl implements IUserTaskTestService 
{
    @Autowired
    private UserTaskTestMapper userTaskTestMapper;

    /**
     * 查询用户考核
     * 
     * @param id 用户考核ID
     * @return 用户考核
     */
    @Override
    public UserTaskTest selectUserTaskTestById(String id)
    {
        return userTaskTestMapper.selectUserTaskTestById(id);
    }

    /**
     * 查询用户考核列表
     * 
     * @param userTaskTest 用户考核
     * @return 用户考核
     */
    @Override
    public List<UserTaskTest> selectUserTaskTestList(UserTaskTest userTaskTest)
    {
        return userTaskTestMapper.selectUserTaskTestList(userTaskTest);
    }

    /**
     * 新增用户考核
     * 
     * @param userTaskTest 用户考核
     * @return 结果
     */
    @Override
    public int insertUserTaskTest(UserTaskTest userTaskTest)
    {
        return userTaskTestMapper.insertUserTaskTest(userTaskTest);
    }

    /**
     * 修改用户考核
     * 
     * @param userTaskTest 用户考核
     * @return 结果
     */
    @Override
    public int updateUserTaskTest(UserTaskTest userTaskTest)
    {
        return userTaskTestMapper.updateUserTaskTest(userTaskTest);
    }

    /**
     * 删除用户考核对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserTaskTestByIds(String ids)
    {
        return userTaskTestMapper.deleteUserTaskTestByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户考核信息
     * 
     * @param id 用户考核ID
     * @return 结果
     */
    @Override
    public int deleteUserTaskTestById(String id)
    {
        return userTaskTestMapper.deleteUserTaskTestById(id);
    }
    @Override
    public List<OrgTaskTest> selectOrgTaskTestList(UserTaskTest userTaskTest){
        return userTaskTestMapper.selectOrgTaskTestList(userTaskTest);
    }
}
