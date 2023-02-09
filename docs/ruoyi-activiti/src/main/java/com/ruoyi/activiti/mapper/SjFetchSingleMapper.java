package com.ruoyi.activiti.mapper;


import com.ruoyi.activiti.domain.SjFetchSingle;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-04-07
 */
public interface SjFetchSingleMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param fetchId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SjFetchSingle selectSjFetchSingleById(String fetchId);

    public SjFetchSingle selectSjFetchSingleByIdMysql(String fetchId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sjFetchSingle 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SjFetchSingle> selectSjFetchSingleList(SjFetchSingle sjFetchSingle);

    public List<SjFetchSingle> selectSjFetchSingleListMysql(SjFetchSingle sjFetchSingle);

    /**
     * 新增【请填写功能名称】
     * 
     * @param sjFetchSingle 【请填写功能名称】
     * @return 结果
     */
    public int insertSjFetchSingle(SjFetchSingle sjFetchSingle);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sjFetchSingle 【请填写功能名称】
     * @return 结果
     */
    public int updateSjFetchSingle(SjFetchSingle sjFetchSingle);

    /**
     * 删除【请填写功能名称】
     * 
     * @param fetchId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSjFetchSingleById(String fetchId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param fetchIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSjFetchSingleByIds(String[] fetchIds);

    SjFetchSingle selectSjFetchSingleByProcessId(String processId);

    SjFetchSingle selectSjFetchSingleByProcessIdMysql(String processId);

    List<SjFetchSingle> selectListByTask(SjFetchSingle sjFetchSingle);

    List<SjFetchSingle> selectListByTaskMysql(SjFetchSingle sjFetchSingle);

    public int updateSjFetchSingleStatus(SjFetchSingle sjFetchSingle);
}
