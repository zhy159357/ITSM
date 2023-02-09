package com.ruoyi.activiti.service;


import com.ruoyi.common.core.domain.entity.OgGroupPerson;

import java.util.List;
import java.util.Map;

/**
 * 工作组人员信息Service接口
 *
 * @author ruoyi
 * @date 2021-04-18
 */
public interface IOgGroupPersonService
{
    /**
     * 查询工作组人员信息
     *
     * @param gpid 工作组人员信息ID
     * @return 工作组人员信息
     */
    List<OgGroupPerson> selectOgGroupPersonById(String gpid);

    /**
     * 查询工作组人员信息列表
     *
     * @param ogGroupPerson 工作组人员信息
     * @return 工作组人员信息集合
     */
    public List<OgGroupPerson> selectOgGroupPersonList(OgGroupPerson ogGroupPerson);

    /**
     * 新增工作组人员信息
     *
     * @param ogGroupPerson 工作组人员信息
     * @return 结果
     */
    public int insertOgGroupPerson(OgGroupPerson ogGroupPerson);

    /**
     * 修改工作组人员信息
     *
     * @param ogGroupPerson 工作组人员信息
     * @return 结果
     */
    public int updateOgGroupPerson(OgGroupPerson ogGroupPerson);

    /**
     * 批量删除工作组人员信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgGroupPersonByIds(String ids);

    /**
     * 删除工作组人员信息信息
     *
     * @param gpid 工作组人员信息ID
     * @return 结果
     */
    public int deleteOgGroupPersonById(String gpid);

    /**
     * 根据人员id删除对应的工作组信息
     * @param pId
     * @return
     */
    int deleteGroupByPersonId(String pId);

    public List<Map<String, Object>> selectOgGroupPersons(String gpid);
    public List<Map<String, Object>> selectGeneralManagers(String gpid);
}
