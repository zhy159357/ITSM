package com.ruoyi.activiti.mapper;


import com.ruoyi.common.core.domain.entity.AutomateMiddle;

import java.util.List;

/**
 * Mapper接口
 *
 * @author ruoyi
 * @date 2021-03-27
 */
public interface AutomateMiddleMapper {
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
     * 删除
     *
     * @param autoId
     * @return 结果
     */
    public int deleteAutomateMiddleById(String autoId);

    /**
     * 批量删除
     *
     * @param autoIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteAutomateMiddleByIds(String[] autoIds);
}
