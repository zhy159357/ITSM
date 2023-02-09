package com.ruoyi.form.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.form.domain.ItFormData;
import com.ruoyi.form.mapper.ItFormDataMapper;
import com.ruoyi.form.service.IItFormDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 单模版Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
@Service
public class ItFormDataServiceImpl implements IItFormDataService
{
    @Autowired
    private ItFormDataMapper itFormDataMapper;

    /**
     * 查询单模版
     * 
     * @param formId 单模版ID
     * @return 单模版
     */
    @Override
    public ItFormData selectItFormDataById(String formId)
    {
        return itFormDataMapper.selectItFormDataById(formId);
    }

    /**
     * 查询单模版列表
     * 
     * @param itFormData 单模版
     * @return 单模版
     */
    @Override
    public List<ItFormData> selectItFormDataList(ItFormData itFormData)
    {
        return itFormDataMapper.selectItFormDataList(itFormData);
    }

    /**
     * 新增单模版
     * 
     * @param itFormData 单模版
     * @return 结果
     */
    @Override
    public int insertItFormData(ItFormData itFormData)
    {
        itFormData.setCreateTime(DateUtils.getNowDate());
        return itFormDataMapper.insertItFormData(itFormData);
    }

    /**
     * 修改单模版
     * 
     * @param itFormData 单模版
     * @return 结果
     */
    @Override
    public int updateItFormData(ItFormData itFormData)
    {
        return itFormDataMapper.updateItFormData(itFormData);
    }

    /**
     * 删除单模版对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteItFormDataByIds(String ids)
    {
        return itFormDataMapper.deleteItFormDataByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除单模版信息
     * 
     * @param formId 单模版ID
     * @return 结果
     */
    @Override
    public int deleteItFormDataById(String formId)
    {
        return itFormDataMapper.deleteItFormDataById(formId);
    }
}
