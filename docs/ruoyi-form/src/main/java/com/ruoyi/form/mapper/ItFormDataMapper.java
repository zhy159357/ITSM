package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.ItFormData;

import java.util.List;

/**
 * 单模版Mapper接口
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
public interface ItFormDataMapper 
{
    /**
     * 查询单模版
     * 
     * @param formId 单模版ID
     * @return 单模版
     */
    public ItFormData selectItFormDataById(String formId);

    /**
     * 查询单模版列表
     * 
     * @param itFormData 单模版
     * @return 单模版集合
     */
    public List<ItFormData> selectItFormDataList(ItFormData itFormData);

    /**
     * 新增单模版
     * 
     * @param itFormData 单模版
     * @return 结果
     */
    public int insertItFormData(ItFormData itFormData);

    /**
     * 修改单模版
     * 
     * @param itFormData 单模版
     * @return 结果
     */
    public int updateItFormData(ItFormData itFormData);

    /**
     * 删除单模版
     * 
     * @param formId 单模版ID
     * @return 结果
     */
    public int deleteItFormDataById(String formId);

    /**
     * 批量删除单模版
     * 
     * @param formIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteItFormDataByIds(String[] formIds);
}
