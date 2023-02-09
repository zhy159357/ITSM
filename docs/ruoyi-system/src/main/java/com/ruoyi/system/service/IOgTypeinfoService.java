package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.OgTypeInfoExcelHeader;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;

import java.util.List;

/**
 * 参数列别Service接口
 * 
 * @author ruoyi
 * @date 2020-12-06
 */
public interface IOgTypeinfoService extends IService<OgTypeinfo>
{
    /**
     * 查询参数列别
     * 
     * @param typeinfoId 参数列别ID
     * @return 参数列别
     */
    public OgTypeinfo selectOgTypeinfoById(String typeinfoId);

    /**
     * 查询参数列别
     *
     * @param parentId 父参数id
     * @return 参数列别
     */
    public OgTypeinfo selectOgTypeinfoByParentId(String parentId);

    /**
     * 查询参数列别列表
     * 
     * @param ogTypeinfo 参数列别
     * @return 参数列别集合
     */
    public List<OgTypeinfo> selectOgTypeinfoList(OgTypeinfo ogTypeinfo);

    /**
     * 新增参数列别
     * 
     * @param ogTypeinfo 参数列别
     * @return 结果
     */
    public int insertOgTypeinfo(OgTypeinfo ogTypeinfo);

    /**
     * 修改参数列别
     * 
     * @param ogTypeinfo 参数列别
     * @return 结果
     */
    public int updateOgTypeinfo(OgTypeinfo ogTypeinfo);

    /**
     * 批量删除参数列别
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgTypeinfoByIds(String ids);

    /**
     * 删除参数列别信息
     * 
     * @param typeinfoId 参数列别ID
     * @return 结果
     */
    public int deleteOgTypeinfoById(String typeinfoId);

    /**
     * 查询参数列别列表
     *
     * @param ogTypeinfo 参数实体对象
     * @return 结果
     */
    public List<ZtreeStr> selectTypeinfoTree(OgTypeinfo ogTypeinfo);
    /**
     * 查询分类父类为空的
     */
    public List<OgTypeinfo> selectCmbizType();


    public List<Ztree> selectTypeTreeExcludeChild(OgTypeinfo ogTypeinfo);

    /**
     * 导入ogType
     * @param list
     */
     void importOgTypeData(List<OgTypeInfoExcelHeader> list,String typeinfoId);

    /**
     * 页面查询事件单分类
     * @param typeNo
     * @param level
     * @return
     */
    List<OgTypeinfo> selectOgTypeInfoByEvent(String typeNo, String level);

    /**
     * 根据类型编号查询
     * @param typeNo
     * @return
     */
    OgTypeinfo selectOgTypeInfoByTypeNo(String typeNo);
    List<OgTypeinfo> selectOgTypeInfoByTypeNoForProblem(String typeNo);

    /**
     * 根据编号和类型查询当前类所在的所有兄弟类别数据
     * 查询思路：获取当前类别的父类id，查询该父类id下的所有类别数据集合
     * @param typeTypeNo
     * @param typeNo
     * @return
     */
    List<OgTypeinfo> selectCurrentOgTypeInfoList(String typeTypeNo, String typeNo);
}
