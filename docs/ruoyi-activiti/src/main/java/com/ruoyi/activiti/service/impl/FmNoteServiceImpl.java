package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.FmNote;
import com.ruoyi.activiti.mapper.FmNoteMapper;
import com.ruoyi.activiti.service.IFmNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-11-29
 */
@Service
public class FmNoteServiceImpl implements IFmNoteService
{
    @Autowired
    private FmNoteMapper fmNoteMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param noteId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public FmNote selectFmNoteById(String noteId)
    {
        return fmNoteMapper.selectFmNoteById(noteId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param fmNote 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<FmNote> selectFmNoteList(FmNote fmNote)
    {
        return fmNoteMapper.selectFmNoteList(fmNote);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param fmNote 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFmNote(FmNote fmNote)
    {
        return fmNoteMapper.insertFmNote(fmNote);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param fmNote 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFmNote(FmNote fmNote)
    {
        return fmNoteMapper.updateFmNote(fmNote);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmNoteByIds(String ids)
    {
        return fmNoteMapper.deleteFmNoteByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param noteId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteFmNoteById(String noteId)
    {
        return fmNoteMapper.deleteFmNoteById(noteId);
    }
}
