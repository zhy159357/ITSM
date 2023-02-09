package com.ruoyi.form.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.ItBusinessData;
import com.ruoyi.form.mapper.ItBusinessDataMapper;
import com.ruoyi.form.service.IItBusinessDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 业务-通用Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
@Service
public class ItBusinessDataServiceImpl implements IItBusinessDataService
{
    @Autowired
    private ItBusinessDataMapper itBusinessDataMapper;

    /**
     * 查询业务-通用
     * 
     * @param businessId 业务-通用ID
     * @return 业务-通用
     */
    //@DataSource(DataSourceType.SLAVE)
    @Override
    public ItBusinessData selectItBusinessDataById(String businessId)
    {
        return itBusinessDataMapper.selectItBusinessDataById(businessId);
    }

    /**
     * 查询业务-通用列表
     * 
     * @param itBusinessData 业务-通用
     * @return 业务-通用
     */
    @Override
    public List<ItBusinessData> selectItBusinessDataList(ItBusinessData itBusinessData)
    {
        return itBusinessDataMapper.selectItBusinessDataList(itBusinessData);
    }

    /**
     * 新增业务-通用
     * 
     * @param itBusinessData 业务-通用
     * @return 结果
     */
    @Override
    public int insertItBusinessData(ItBusinessData itBusinessData)
    {
        itBusinessData.setCreateTime(DateUtils.getNowDate());
        return itBusinessDataMapper.insertItBusinessData(itBusinessData);
    }

    /**
     * 修改业务-通用
     * 
     * @param itBusinessData 业务-通用
     * @return 结果
     */
    @Override
    public int updateItBusinessData(ItBusinessData itBusinessData)
    {
        itBusinessData.setUpdateTime(DateUtils.getNowDate());
        return itBusinessDataMapper.updateItBusinessData(itBusinessData);
    }

    /**
     * 删除业务-通用对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteItBusinessDataByIds(String ids)
    {
        return itBusinessDataMapper.deleteItBusinessDataByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务-通用信息
     * 
     * @param businessId 业务-通用ID
     * @return 结果
     */
    @Override
    public int deleteItBusinessDataById(String businessId)
    {
        return itBusinessDataMapper.deleteItBusinessDataById(businessId);
    }

    @Override
    public ItBusinessData selectItBusinessDataByCondition(ItBusinessData businessData) {
        if(!StringUtils.isEmpty(businessData.getCreateTime())) {
            String createTimeStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, businessData.getCreateTime());
            businessData.getParams().put("createTimeStr", createTimeStr);
        }
        return itBusinessDataMapper.selectItBusinessDataByCondition(businessData);
    }

    @Override
    public List<ItBusinessData> selectList() {
        //return itBusinessDataMapper.selectList(null);
        return null;
    }
}
