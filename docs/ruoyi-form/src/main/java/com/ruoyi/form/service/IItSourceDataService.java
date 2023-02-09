package com.ruoyi.form.service;

import com.ruoyi.form.domain.ItSourceData;

import java.util.List;

/**
 * 元数据Service接口
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
public interface  IItSourceDataService 
{
    /**
     * 查询元数据
     * 
     * @param sourceDataId 元数据ID
     * @return 元数据
     */
    public ItSourceData selectItSourceDataById(String sourceDataId);

    /**
     * 查询元数据列表
     * 
     * @param itSourceData 元数据
     * @return 元数据集合
     */
    public List<ItSourceData> selectItSourceDataList(ItSourceData itSourceData);

    /**
     * 新增元数据
     * 
     * @param itSourceData 元数据
     * @return 结果
     */
    public int insertItSourceData(ItSourceData itSourceData);

    /**
     * 修改元数据
     * 
     * @param itSourceData 元数据
     * @return 结果
     */
    public int updateItSourceData(ItSourceData itSourceData);

    /**
     * 批量删除元数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteItSourceDataByIds(String ids);

    /**
     * 删除元数据信息
     * 
     * @param sourceDataId 元数据ID
     * @return 结果
     */
    public int deleteItSourceDataById(String sourceDataId);
}
