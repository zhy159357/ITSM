package com.ruoyi.form.service;

import com.ruoyi.form.domain.ItFormData;

import java.util.List;

/**
 * 单模版Service接口
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
public interface IItFormDataService 
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
     * 批量删除单模版
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteItFormDataByIds(String ids);

    /**
     * 删除单模版信息
     * 
     * @param formId 单模版ID
     * @return 结果
     */
    public int deleteItFormDataById(String formId);
}
