package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgGroupPerson;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.system.http.entegorserver.entity.LabelValue;

import java.util.List;
import java.util.Map;

/**
 * 工作组
 *
 * @author 14735
 */
public interface ISysWorkService {

    /**
     * 根据条件查询工作组信息
     *
     * @param group
     * @return
     */
    public List<OgGroup> selectOgGroupList(OgGroup group);
    /**
     * 根据条件查询工作组信息 机构向下
     *
     * @param group
     * @return
     */
    public List<OgGroup> selectOgGroupAllList(OgGroup group);

    /**
     * 根据id查询工作组信息
     *
     * @param id
     * @return
     */
    public OgGroup selectOgGroupById(String id);

    /**
     * 根据用户id查询工作组
     *
     * @param userId
     * @return
     */
    public List<OgGroup> selectGroupByUserId(String userId);

    OgGroup selectOgGroupByGrpName(String grpName);

    /**
     * 根据用户id查询工作组
     *
     * @param map
     * @return
     */
    public List<OgGroup> selectGroupByGposition(Map map);

    public List<OgGroup> selectProblemGroupByGposition(Map map);

    /**
     * 根据工作组名称检查工作组是否唯一
     *
     * @param groupName
     * @return
     */
    public String checkGroupNameUnique(String groupName);

    /**
     * 新增工作组
     *
     * @param group
     * @return
     */
    public int insertOgGroup(OgGroup group);

    /**
     * 修改保存工作组
     *
     * @param group
     * @return
     */
    public int updateOgGroup(OgGroup group);

    /**
     * 删除工作组
     *
     * @param ids
     * @return
     */
    public int deleteOgGroupByIds(String ids);

    /**
     * 查询工作组成员
     *
     * @param person
     * @return
     */
    public List<OgGroupPerson> selectOgGroupPersonList(OgGroupPerson person);

    /**
     * 工作组管理添加筛选用户
     *
     * @param person
     * @return
     */
    public List<OgPerson> selectUserList(OgPerson person);

    /**
     * 保存信息到工作组成员表
     *
     * @param groupId
     * @param pIds
     * @return
     */
    public int insertOgGroupPerson(String groupId, String pIds);

    /**
     * 修改保存工作组成员信息
     *
     * @param person
     * @return
     */
    public int updateOgGroupPerson(OgGroupPerson person);

    /**
     * 删除工作组成员信息
     *
     * @param ids
     * @return
     */
    public int deleteOgGroupPersonByIds(String ids);

    /**
     * 根据id查询工作组成员信息
     *
     * @param gpId
     * @return
     */
    public OgGroupPerson selectOgGroupPersonById(String gpId);

    /**
     * 根据工作组名称查询人员
     *
     * @param groupName
     * @return
     */
    public List<OgPerson> selectOgPersonByGroupName(String groupName);

    /**
     * 根据当前登陆人机构查询所辖范围的工作组
     */
    public List<OgGroup> getOgProviceGroups();

    /**
     * 根据系统ID查询工作组
     */
    public List<OgGroup> selectOgGroupBySys(String sysid);

    /**
     * 根据工作组ID查询人员
     *
     * @param groupId
     * @return
     */
    public List<OgPerson> selectGroupByPerson(String groupId);

    /**
     * 业务事件单转发选择工作组
     */
    public List<OgGroup> selectOgGroupByFmbiz(OgGroup group);

    List<OgGroup> selectGroupBySysId(String sysId);

    /**
     * 根据登录人id查询工作组
     *
     * @param pId
     * @return
     */
    OgGroup selectOgPersonById(String pId);

    /**
     * @param groupIds
     * @param group
     * @return
     */
    List<OgGroup> selectOgGroupLxbgList(String groupIds, OgGroup group);

    /**
     * 根据工作组ID查询组内人员信息
     *
     * @param groupId
     * @return
     */
    public List<OgGroupPerson> selectGroupIdByPersonList(String groupId);

    List<OgGroupPerson> selectOgPersonByIdTwo(String getpId);

    /**
     * 根据工作组ID查询组内人员信息 角色为：例行变更执行人
     * @param groupId
     * @return
     */
    List<OgPerson> selectLxbgGroupByPerson(String groupId);


    List<OgGroup> selectGroupByGpositionTwo(OgGroup ogGroup);

    /**
     * 查询登陆人满足条件的2线工作组
     * @param userId
     * @return
     */
    public List<OgGroup> selectLoginGroups(String userId);

    public List<OgGroup> selectOgGroupListAll(OgGroup group);

    /**
     * sysid  in
     * @param group
     * @return
     */
    List<OgGroup> selectOgGroupListSysIdIn(OgGroup group);

    /**
     *
     * @param groupName
     * @return
     */
    List<LabelValue> selectOgGroupPersonListByGroupName(String groupName);
}
