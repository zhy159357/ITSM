package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.mapper.FmToQuestionMapper;
import com.ruoyi.activiti.service.IFmToQuestionService;
import com.ruoyi.common.core.domain.entity.FmToQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-08-17
 */
@Service
public class FmToQuestionServiceImpl implements IFmToQuestionService
{
    @Autowired
    private FmToQuestionMapper fmToQuestionMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public FmToQuestion selectFmToQuestionById(String id)
    {
        return fmToQuestionMapper.selectFmToQuestionById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param fmToQuestion 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<FmToQuestion> selectFmToQuestionList(FmToQuestion fmToQuestion)
    {
        return fmToQuestionMapper.selectFmToQuestionList(fmToQuestion);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param fmToQuestion 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFmToQuestion(FmToQuestion fmToQuestion)
    {
        return fmToQuestionMapper.insertFmToQuestion(fmToQuestion);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param fmToQuestion 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFmToQuestion(FmToQuestion fmToQuestion)
    {
        return fmToQuestionMapper.updateFmToQuestion(fmToQuestion);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmToQuestionByIds(String ids)
    {
        return fmToQuestionMapper.deleteFmToQuestionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteFmToQuestionById(String id)
    {
        return fmToQuestionMapper.deleteFmToQuestionById(id);
    }
}
