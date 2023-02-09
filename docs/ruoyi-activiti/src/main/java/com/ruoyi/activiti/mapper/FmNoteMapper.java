package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.FmNote;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-11-29
 */
public interface FmNoteMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param noteId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public FmNote selectFmNoteById(String noteId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param fmNote 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<FmNote> selectFmNoteList(FmNote fmNote);

    /**
     * 新增【请填写功能名称】
     * 
     * @param fmNote 【请填写功能名称】
     * @return 结果
     */
    public int insertFmNote(FmNote fmNote);

    /**
     * 修改【请填写功能名称】
     * 
     * @param fmNote 【请填写功能名称】
     * @return 结果
     */
    public int updateFmNote(FmNote fmNote);

    /**
     * 删除【请填写功能名称】
     * 
     * @param noteId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteFmNoteById(String noteId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param noteIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmNoteByIds(String[] noteIds);
}
