package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.AmBizPara;
import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-01-19
 */
public interface AmBizParaMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param amParaId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public AmBizPara selectAmBizParaById(String amParaId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param amBizPara 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<AmBizPara> selectAmBizParaList(AmBizPara amBizPara);

    /**
     * 新增【请填写功能名称】
     * 
     * @param amBizPara 【请填写功能名称】
     * @return 结果
     */
    public int insertAmBizPara(AmBizPara amBizPara);

    /**
     * 修改【请填写功能名称】
     * 
     * @param amBizPara 【请填写功能名称】
     * @return 结果
     */
    public int updateAmBizPara(AmBizPara amBizPara);

    /**
     * 删除【请填写功能名称】
     * 
     * @param amParaId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteAmBizParaById(String amParaId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param amParaIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteAmBizParaByIds(String[] amParaIds);
}
