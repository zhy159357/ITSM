package com.ruoyi.form.service.impl;

import java.util.List;

import com.ruoyi.form.domain.ItSourceData;
import com.ruoyi.form.mapper.ItSourceDataMapper;
import com.ruoyi.form.service.IItSourceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 元数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
@Service
public class ItSourceDataServiceImpl implements IItSourceDataService
{
    @Autowired
    private ItSourceDataMapper itSourceDataMapper;

    /**
     * 查询元数据
     * 
     * @param sourceDataId 元数据ID
     * @return 元数据
     */
    @Override
    public ItSourceData selectItSourceDataById(String sourceDataId)
    {
        return itSourceDataMapper.selectItSourceDataById(sourceDataId);
    }

    /**
     * 查询元数据列表
     * 
     * @param itSourceData 元数据
     * @return 元数据
     */
    @Override
    public List<ItSourceData> selectItSourceDataList(ItSourceData itSourceData)
    {
        return itSourceDataMapper.selectItSourceDataList(itSourceData);
    }

    /**
     * 新增元数据
     * 
     * @param itSourceData 元数据
     * @return 结果
     */
    @Override
    public int insertItSourceData(ItSourceData itSourceData)
    {
        return itSourceDataMapper.insertItSourceData(itSourceData);
    }

    /**
     * 修改元数据
     * 
     * @param itSourceData 元数据
     * @return 结果
     */
    @Override
    public int updateItSourceData(ItSourceData itSourceData)
    {
        return itSourceDataMapper.updateItSourceData(itSourceData);
    }

    /**
     * 删除元数据对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteItSourceDataByIds(String ids)
    {
        return itSourceDataMapper.deleteItSourceDataByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除元数据信息
     * 
     * @param sourceDataId 元数据ID
     * @return 结果
     */
    @Override
    public int deleteItSourceDataById(String sourceDataId)
    {
        return itSourceDataMapper.deleteItSourceDataById(sourceDataId);
    }
}
