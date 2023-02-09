package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.AutomateMiddleMapper;
import com.ruoyi.activiti.service.IAutomateMiddleService;
import com.ruoyi.common.core.domain.entity.AutomateMiddle;
import com.ruoyi.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-03-27
 */
@Service
public class AutomateMiddleServiceImpl implements IAutomateMiddleService
{
    @Autowired
    private AutomateMiddleMapper automateMiddleMapper;

    /**
     * 查询
     * 
     * @param autoId
     * @return
     */
    @Override
    public AutomateMiddle selectAutomateMiddleById(String autoId)
    {
        return automateMiddleMapper.selectAutomateMiddleById(autoId);
    }

    /**
     * 查询列表
     * 
     * @param automateMiddle
     * @return
     */
    @Override
    public List<AutomateMiddle> selectAutomateMiddleList(AutomateMiddle automateMiddle)
    {
        return automateMiddleMapper.selectAutomateMiddleList(automateMiddle);
    }

    /**
     * 新增
     * 
     * @param automateMiddle
     * @return 结果
     */
    @Override
    public int insertAutomateMiddle(AutomateMiddle automateMiddle)
    {
        return automateMiddleMapper.insertAutomateMiddle(automateMiddle);
    }

    /**
     * 修改
     * 
     * @param automateMiddle
     * @return 结果
     */
    @Override
    public int updateAutomateMiddle(AutomateMiddle automateMiddle)
    {
        return automateMiddleMapper.updateAutomateMiddle(automateMiddle);
    }

    /**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAutomateMiddleByIds(String ids)
    {
        return automateMiddleMapper.deleteAutomateMiddleByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除信息
     * 
     * @param autoId
     * @return 结果
     */
    @Override
    public int deleteAutomateMiddleById(String autoId)
    {
        return automateMiddleMapper.deleteAutomateMiddleById(autoId);
    }
}
