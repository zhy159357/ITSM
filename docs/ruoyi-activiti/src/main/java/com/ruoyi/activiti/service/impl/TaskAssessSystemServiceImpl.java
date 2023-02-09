package com.ruoyi.activiti.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.activiti.mapper.TaskAssessSystemMapper;
import com.ruoyi.activiti.domain.TaskAssessSystem;
import com.ruoyi.activiti.service.ITaskAssessSystemService;
import com.ruoyi.common.core.text.Convert;

/**
 * 厂商考核Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-01-20
 */
@Service
public class TaskAssessSystemServiceImpl implements ITaskAssessSystemService 
{
    @Autowired
    private TaskAssessSystemMapper taskAssessSystemMapper;

    /**
     * 查询厂商考核
     * 
     * @param manyidu 厂商考核ID
     * @return 厂商考核
     */
    @Override
    public TaskAssessSystem selectTaskAssessSystemById(String manyidu)
    {
        return taskAssessSystemMapper.selectTaskAssessSystemById(manyidu);
    }

    /**
     * 查询厂商考核列表
     * 
     * @param taskAssessSystem 厂商考核
     * @return 厂商考核
     */
    @Override
    public List<TaskAssessSystem> selectTaskAssessSystemList(TaskAssessSystem taskAssessSystem)
    {
        return taskAssessSystemMapper.selectTaskAssessSystemList(taskAssessSystem);
    }

    /**
     * 新增厂商考核
     * 
     * @param taskAssessSystem 厂商考核
     * @return 结果
     */
    @Override
    public int insertTaskAssessSystem(TaskAssessSystem taskAssessSystem)
    {
        return taskAssessSystemMapper.insertTaskAssessSystem(taskAssessSystem);
    }

    /**
     * 修改厂商考核
     * 
     * @param taskAssessSystem 厂商考核
     * @return 结果
     */
    @Override
    public int updateTaskAssessSystem(TaskAssessSystem taskAssessSystem)
    {
        return taskAssessSystemMapper.updateTaskAssessSystem(taskAssessSystem);
    }

    /**
     * 删除厂商考核对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTaskAssessSystemByIds(String ids)
    {
        return taskAssessSystemMapper.deleteTaskAssessSystemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除厂商考核信息
     * 
     * @param manyidu 厂商考核ID
     * @return 结果
     */
    @Override
    public int deleteTaskAssessSystemById(String manyidu)
    {
        return taskAssessSystemMapper.deleteTaskAssessSystemById(manyidu);
    }
}
