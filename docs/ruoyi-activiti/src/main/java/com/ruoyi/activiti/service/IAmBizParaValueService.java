package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.AmBizParaValue;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-01-19
 */
public interface IAmBizParaValueService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param amParaValueId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public AmBizParaValue selectAmBizParaValueById(String amParaValueId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param amBizParaValue 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<AmBizParaValue> selectAmBizParaValueList(AmBizParaValue amBizParaValue);

    /**
     * 新增【请填写功能名称】
     * 
     * @param amBizParaValue 【请填写功能名称】
     * @return 结果
     */
    public int insertAmBizParaValue(AmBizParaValue amBizParaValue);

    /**
     * 修改【请填写功能名称】
     * 
     * @param amBizParaValue 【请填写功能名称】
     * @return 结果
     */
    public int updateAmBizParaValue(AmBizParaValue amBizParaValue);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAmBizParaValueByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param amParaValueId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteAmBizParaValueById(String amParaValueId);
}
