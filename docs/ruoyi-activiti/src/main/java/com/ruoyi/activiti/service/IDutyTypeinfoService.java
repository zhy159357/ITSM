package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.*;

import java.util.List;

/**
 * 参数列别Service接口
 * @author dayong_sun
 * @date 2020-01-19
 */
public interface IDutyTypeinfoService
{
    /**
     * 查询参数列别
     *
     * @param typeinfoId 参数列别ID
     * @return 参数列别
     */
    public DutyTypeinfo selectDutyTypeinfoById(String typeinfoId);

    /**
     * 查询值班人员
     * @param rid 参数列别ID
     * @return 参数列别
     */
    public List<OgPerson> selectUserList(String rid);

    /**
     * 查询参数列别
     * @param parentId 父参数id
     * @return 参数列别
     */
    public DutyTypeinfo selectDutyTypeinfoByParentId(String parentId);

    /**
     * 查询参数列别列表
     *
     * @param dutyTypeinfo 参数列别
     * @return 参数列别集合
     */
    public List<DutyTypeinfo> selectDutyTypeinfoList(DutyTypeinfo dutyTypeinfo);

    /**
     * 新增参数列别
     *
     * @param dutyTypeinfo 参数列别
     * @return 结果
     */
    public int insertDutyTypeinfo(DutyTypeinfo dutyTypeinfo);

    /**
     * 修改参数列别
     *
     * @param dutyTypeinfo 参数列别
     * @return 结果
     */
    public int updateDutyTypeinfo(DutyTypeinfo dutyTypeinfo);

    /**
     * 批量删除参数列别
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDutyTypeinfoByIds(String ids);

    /**
     * 删除参数列别信息
     *
     * @param typeinfoId 参数列别ID
     * @return 结果
     */
    public int deleteDutyTypeinfoById(String typeinfoId);

    /**
     * 查询参数列别列表
     *
     * @param dutyTypeinfo 参数实体对象
     * @return 结果
     */
    public List<ZtreeStr> selectTypeinfoTree(DutyTypeinfo dutyTypeinfo);

    public List<Ztree> selectTypeTreeExcludeChild(DutyTypeinfo dutyTypeinfo);

    /**
     * 校验类别编码是否唯一
     * @param dutyTypeinfo 角色信息
     * @return 结果
     */
    public String checkTypeNoUnique(DutyTypeinfo dutyTypeinfo);

    /**
     * 查询类别编码
     * @param dutyTypeinfo 类别信息
     * @return 结果
     */
    public List<DutyTypeinfo> selectTypeNo(DutyTypeinfo dutyTypeinfo);
}
