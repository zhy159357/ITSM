package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.DutyTypeinfo;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgRole;

import java.util.List;

/**
 * 参数类表Mapper接口
 * 
 * @author ruoyi
 * @date 2020-12-06
 */
public interface DutyTypeinfoMapper
{
    /**
     * 查询参数列表
     * @param typeinfoId 参数类表ID
     * @return 参数类表
     */
    public DutyTypeinfo selectDutyTypeinfoById(String typeinfoId);

    /**
     * 查询值班人员
     * @param rid 参数列别ID
     * @return 参数列别
     */
    public  List<OgPerson> selectUserList(String rid);

    /**
     * 查询参数列表
     * @param parentId 父参数ID
     * @return 参数类表
     */
    public DutyTypeinfo selectDutyTypeinfoByParentId(String parentId);

    /**
     * 查询参数列表列表
     * @param dutyTypeinfo 参数类表
     * @return 参数类表集合
     */
    public List<DutyTypeinfo> selectDutyTypeinfoList(DutyTypeinfo dutyTypeinfo);

    /**
     * 通过版本ID查询类别
     * @param dutyTypeinfo
     * @return 版本对象信息
     */
    public List<DutyTypeinfo> selectVersionTypeinfoByParentId(DutyTypeinfo dutyTypeinfo);

    /**
     * 新增参数列表
     * 
     * @param dutyTypeinfo 参数列表
     * @return 结果
     */
    public int insertDutyTypeinfo(DutyTypeinfo dutyTypeinfo);

    /**
     * 修改参数列表
     * @param dutyTypeinfo 参数列表
     * @return 结果
     */
    public int updateDutyTypeinfo(DutyTypeinfo dutyTypeinfo);

    /**
     * 删除参数列表
     * @param typeinfoId 参数列表ID
     * @return 结果
     */
    public int deleteDutyTypeinfoById(String typeinfoId);

    /**
     * 批量删除参数列表
     * @param typeinfoIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDutyTypeinfoByIds(String[] typeinfoIds);

    /**
     * 查询参数列表
     *
     * @param dutyTypeinfo 参数类别
     * @return 结果
     */
    public List<DutyTypeinfo> selectTypeinfoTree(DutyTypeinfo dutyTypeinfo);

    /**
     * 校验类别编码是否唯一
     * @param typeNo 类别信息
     * @return 结果
     */
    public DutyTypeinfo checkTypeNoUnique(String typeNo);
    
    /**
     * 查询类别编码
     * @param dutyTypeinfo 类别信息
     * @return 结果
     */
    public List<DutyTypeinfo> selectTypeNo(DutyTypeinfo dutyTypeinfo);

    /**
     * 根据parentID查询对应值班类别集合
     * @param typeinfoId
     * @return
     */
    List<DutyTypeinfo> selectDutyTypeInfoListByParentId(String typeinfoId);
}
