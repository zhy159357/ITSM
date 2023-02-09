package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.ItBusinessData;

import java.util.List;

/**
 * 业务-通用Mapper接口
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
public interface ItBusinessDataMapper extends BaseMapper<ItBusinessData>
{
    /**
     * 查询业务-通用
     * 
     * @param businessId 业务-通用ID
     * @return 业务-通用
     */
    public ItBusinessData selectItBusinessDataById(String businessId);

    /**
     * 查询业务-通用列表
     * 
     * @param itBusinessData 业务-通用
     * @return 业务-通用集合
     */
    public List<ItBusinessData> selectItBusinessDataList(ItBusinessData itBusinessData);

    /**
     * 新增业务-通用
     * 
     * @param itBusinessData 业务-通用
     * @return 结果
     */
    public int insertItBusinessData(ItBusinessData itBusinessData);

    /**
     * 修改业务-通用
     * 
     * @param itBusinessData 业务-通用
     * @return 结果
     */
    public int updateItBusinessData(ItBusinessData itBusinessData);

    /**
     * 删除业务-通用
     * 
     * @param businessId 业务-通用ID
     * @return 结果
     */
    public int deleteItBusinessDataById(String businessId);

    /**
     * 批量删除业务-通用
     * 
     * @param businessIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteItBusinessDataByIds(String[] businessIds);

    /**
     * 根据条件查询
     *
     * @param businessData
     * @return
     */
    public ItBusinessData selectItBusinessDataByCondition(ItBusinessData businessData);
}
