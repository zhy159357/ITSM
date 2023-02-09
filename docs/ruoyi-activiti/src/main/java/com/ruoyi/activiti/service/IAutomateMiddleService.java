package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.AutomateMiddle;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-03-27
 */
public interface IAutomateMiddleService 
{
    /**
     * 查询
     * 
     * @param autoId ID
     * @return
     */
    public AutomateMiddle selectAutomateMiddleById(String autoId);

    /**
     * 查询列表
     * 
     * @param automateMiddle
     * @return 集合
     */
    public List<AutomateMiddle> selectAutomateMiddleList(AutomateMiddle automateMiddle);

    /**
     * 新增
     * 
     * @param automateMiddle
     * @return 结果
     */
    public int insertAutomateMiddle(AutomateMiddle automateMiddle);

    /**
     * 修改
     * 
     * @param automateMiddle
     * @return 结果
     */
    public int updateAutomateMiddle(AutomateMiddle automateMiddle);

    /**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAutomateMiddleByIds(String ids);

    /**
     * 删除信息
     * 
     * @param autoId
     * @return 结果
     */
    public int deleteAutomateMiddleById(String autoId);
}
