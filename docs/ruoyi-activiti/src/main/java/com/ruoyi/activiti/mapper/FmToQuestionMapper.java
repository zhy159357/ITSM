package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.FmToQuestion;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-08-17
 */
public interface FmToQuestionMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public FmToQuestion selectFmToQuestionById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param fmToQuestion 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<FmToQuestion> selectFmToQuestionList(FmToQuestion fmToQuestion);

    /**
     * 新增【请填写功能名称】
     * 
     * @param fmToQuestion 【请填写功能名称】
     * @return 结果
     */
    public int insertFmToQuestion(FmToQuestion fmToQuestion);

    /**
     * 修改【请填写功能名称】
     * 
     * @param fmToQuestion 【请填写功能名称】
     * @return 结果
     */
    public int updateFmToQuestion(FmToQuestion fmToQuestion);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteFmToQuestionById(String id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmToQuestionByIds(String[] ids);
}
