package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.TaskAssessSystem;
import java.util.List;

/**
 * 厂商考核Service接口
 * 
 * @author ruoyi
 * @date 2022-01-20
 */
public interface ITaskAssessSystemService 
{
    /**
     * 查询厂商考核
     * 
     * @param manyidu 厂商考核ID
     * @return 厂商考核
     */
    public TaskAssessSystem selectTaskAssessSystemById(String manyidu);

    /**
     * 查询厂商考核列表
     * 
     * @param taskAssessSystem 厂商考核
     * @return 厂商考核集合
     */
    public List<TaskAssessSystem> selectTaskAssessSystemList(TaskAssessSystem taskAssessSystem);

    /**
     * 新增厂商考核
     * 
     * @param taskAssessSystem 厂商考核
     * @return 结果
     */
    public int insertTaskAssessSystem(TaskAssessSystem taskAssessSystem);

    /**
     * 修改厂商考核
     * 
     * @param taskAssessSystem 厂商考核
     * @return 结果
     */
    public int updateTaskAssessSystem(TaskAssessSystem taskAssessSystem);

    /**
     * 批量删除厂商考核
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTaskAssessSystemByIds(String ids);

    /**
     * 删除厂商考核信息
     * 
     * @param manyidu 厂商考核ID
     * @return 结果
     */
    public int deleteTaskAssessSystemById(String manyidu);
}
