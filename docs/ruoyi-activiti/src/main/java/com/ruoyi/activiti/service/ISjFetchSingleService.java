package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.SjFetchSingle;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-04-07
 */
public interface ISjFetchSingleService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param fetchId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SjFetchSingle selectSjFetchSingleById(String fetchId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sjFetchSingle 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SjFetchSingle> selectSjFetchSingleList(SjFetchSingle sjFetchSingle);

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
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSjFetchSingleByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param fetchId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSjFetchSingleById(String fetchId);

    public AjaxResult createFetchSingle(@Valid SjFetchSingle sjFetchSingle, BindingResult result);

    SjFetchSingle selectSjFetchSingleByProcessId(String processId);

    List<SjFetchSingle> selectListByTask(SjFetchSingle sjFetchSingle);

    public int updateSjFetchSingleStatus(SjFetchSingle sjFetchSingle);
}
