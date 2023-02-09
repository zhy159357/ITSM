package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.OgSysEmergencyphone;
import com.ruoyi.common.core.domain.entity.OgSys;

import java.util.List;

/**
 * 【应急通讯录】Mapper接口
 *
 * @author ruoyi
 * @date 2021-07-28
 */
public interface OgSysEmergencyphoneMapper
{
    /**
     * 查询【应急通讯录】
     *
     * @param emId 【应急通讯录】ID
     * @return 【应急通讯录list】
     */
    public OgSysEmergencyphone selectOgSysEmergencyphoneById(String emId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ogSysEmergencyphone 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OgSysEmergencyphone> selectOgSysEmergencyphoneList(OgSysEmergencyphone ogSysEmergencyphone);

    /**
     * 新增【请填写功能名称】
     *
     * @param ogSysEmergencyphone 【请填写功能名称】
     * @return 结果
     */
    public int insertOgSysEmergencyphone(OgSysEmergencyphone ogSysEmergencyphone);

    /**
     * 修改【请填写功能名称】
     *
     * @param ogSysEmergencyphone 【请填写功能名称】
     * @return 结果
     */
    public int updateOgSysEmergencyphone(OgSysEmergencyphone ogSysEmergencyphone);

    /**
     * 删除【请填写功能名称】
     *
     * @param emId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOgSysEmergencyphoneById(String emId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param emIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgSysEmergencyphoneByIds(String[] emIds);

    /**
     * 查询拥有工作组的系统
     *
     * @return 结果
     */
    public List<OgSys> selectOgSysTreeForEmergencyPhone();

    /**
     * 是否是组长
     *
     * @return 结果
     */
    public List<String> ifGroupLeader(OgSysEmergencyphone ogSysEmergencyphone);

    /**
     * 插入应急联系人
     * @param list
     */
    public int importEmergencyphone(List<OgSysEmergencyphone> list);
}
