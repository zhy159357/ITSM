package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.mapper.FmBizToolMapper;
import com.ruoyi.activiti.service.IFmBizToolService;
import com.ruoyi.common.core.domain.entity.FmBizTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 工具使用情况Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-01-21
 */
@Service
public class FmBizToolServiceImpl implements IFmBizToolService
{
    @Autowired
    private FmBizToolMapper fmBizToolMapper;

    /**
     * 查询工具使用情况
     * 
     * @param fbtId 工具使用情况ID
     * @return 工具使用情况
     */
    @Override
    public FmBizTool selectFmBizToolById(String fbtId)
    {
        return fmBizToolMapper.selectFmBizToolById(fbtId);
    }

    /**
     * 查询工具使用情况列表
     * 
     * @param fmBizTool 工具使用情况
     * @return 工具使用情况
     */
    @Override
    public List<FmBizTool> selectFmBizToolList(FmBizTool fmBizTool)
    {
        return fmBizToolMapper.selectFmBizToolList(fmBizTool);
    }

    /**
     * 新增工具使用情况
     * 
     * @param fmBizTool 工具使用情况
     * @return 结果
     */
    @Override
    public int insertFmBizTool(FmBizTool fmBizTool)
    {
        return fmBizToolMapper.insertFmBizTool(fmBizTool);
    }

    /**
     * 修改工具使用情况
     * 
     * @param fmBizTool 工具使用情况
     * @return 结果
     */
    @Override
    public int updateFmBizTool(FmBizTool fmBizTool)
    {
        return fmBizToolMapper.updateFmBizTool(fmBizTool);
    }

    /**
     * 删除工具使用情况对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmBizToolByIds(String ids)
    {
        return fmBizToolMapper.deleteFmBizToolByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除工具使用情况信息
     * 
     * @param fbtId 工具使用情况ID
     * @return 结果
     */
    @Override
    public int deleteFmBizToolById(String fbtId)
    {
        return fmBizToolMapper.deleteFmBizToolById(fbtId);
    }

    @Override
    public List<FmBizTool> selectFmBizToolAppr(FmBizTool fmBizTool) {
        return fmBizToolMapper.selectFmBizToolAppr(fmBizTool);
    }

    @Override
    public List<FmBizTool> selectFmBizToolAppr2(FmBizTool fmBizTool) {
        return fmBizToolMapper.selectFmBizToolAppr2(fmBizTool);
    }
}
