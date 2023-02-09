package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.TaskAssessSystem;
import java.util.List;

/**
 * 厂商考核Mapper接口
 * 
 * @author ruoyi
 * @date 2022-01-20
 */
public interface TaskAssessSystemMapper 
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
     * 删除厂商考核
     * 
     * @param manyidu 厂商考核ID
     * @return 结果
     */
    public int deleteTaskAssessSystemById(String manyidu);

    /**
     * 批量删除厂商考核
     * 
     * @param manyidus 需要删除的数据ID
     * @return 结果
     */
    public int deleteTaskAssessSystemByIds(String[] manyidus);
}
