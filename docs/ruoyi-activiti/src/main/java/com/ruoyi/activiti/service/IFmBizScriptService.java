package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.FmBizScript;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-03-30
 */
public interface IFmBizScriptService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param fbsId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public FmBizScript selectFmBizScriptById(String fbsId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param fmBizScript 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<FmBizScript> selectFmBizScriptList(FmBizScript fmBizScript);

    /**
     * 新增【请填写功能名称】
     * 
     * @param fmBizScript 【请填写功能名称】
     * @return 结果
     */
    public int insertFmBizScript(FmBizScript fmBizScript);

    /**
     * 修改【请填写功能名称】
     * 
     * @param fmBizScript 【请填写功能名称】
     * @return 结果
     */
    public int updateFmBizScript(FmBizScript fmBizScript);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizScriptByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param fbsId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteFmBizScriptById(String fbsId);
    /**
     * 根据条件搜索未执行完成的脚本进行结果获取
     * @param fbs
     * @return
     */
    public FmBizScript getAutoResult(FmBizScript fbs);
}
