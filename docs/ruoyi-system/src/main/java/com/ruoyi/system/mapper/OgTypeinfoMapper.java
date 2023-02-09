package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import java.util.List;

/**
 * 参数类表Mapper接口
 * 
 * @author ruoyi
 * @date 2020-12-06
 */
public interface OgTypeinfoMapper extends BaseMapper<OgTypeinfo>
{
    /**
     * 查询参数列表
     * 
     * @param typeinfoId 参数类表ID
     * @return 参数类表
     */
    public OgTypeinfo selectOgTypeinfoById(String typeinfoId);
    public OgTypeinfo selectOgTypeinfoById2(String typeinfoId);

    /**
     * 查询参数列表
     *
     * @param parentId 父参数ID
     * @return 参数类表
     */
    public OgTypeinfo selectOgTypeinfoByParentId(String parentId);

    /**
     * 查询参数列表列表
     * 
     * @param ogTypeinfo 参数类表
     * @return 参数类表集合
     */
    public List<OgTypeinfo> selectOgTypeinfoList(OgTypeinfo ogTypeinfo);

    /**
     * 新增参数列表
     * 
     * @param ogTypeinfo 参数列表
     * @return 结果
     */
    public int insertOgTypeinfo(OgTypeinfo ogTypeinfo);

    /**
     * 修改参数列表
     * 
     * @param ogTypeinfo 参数列表
     * @return 结果
     */
    public int updateOgTypeinfo(OgTypeinfo ogTypeinfo);

    /**
     * 删除参数列表
     * 
     * @param typeinfoId 参数列表ID
     * @return 结果
     */
    public int deleteOgTypeinfoById(String typeinfoId);

    /**
     * 批量删除参数列表
     * 
     * @param typeinfoIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgTypeinfoByIds(String[] typeinfoIds);

    /**
     * 查询参数列表
     *
     * @param ogTypeinfo 参数类别
     * @return 结果
     */
    public List<OgTypeinfo> selectTypeinfoTree(OgTypeinfo ogTypeinfo);
    /**
     * 查询分类父类为空的
     */
    public List<OgTypeinfo> selectCmbizType();
}
