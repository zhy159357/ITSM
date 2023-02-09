package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgGroupPerson;
import com.ruoyi.common.core.domain.entity.OgPerson;

import java.util.List;
import java.util.Map;

public interface SysWorkMapper {

    /**
     * 根据条件查询列表
     * @param group
     * @return
     */
    public List<OgGroup> selectOgGroupList(OgGroup group);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public OgGroup selectOgGroupById(String id);

    /**
     * 根据用户id查询工作组
     * @param userId
     * @return
     */
    public List<OgGroup> selectGroupByUserId(String userId);

    /**
     * 根据用户id查询工作组
     * @param map
     * @return
     */
    public List<OgGroup> selectGroupByGposition(Map map);

    OgGroup selectOgGroupByGrpName(String grpName);

    public List<OgGroup> selectProblemGroupByGposition(Map map);

    /**
     * 检查工作组名称是否重复
     * @param groupName
     * @return
     */
    public int checkGroupNameUnique(String groupName);

    /**
     * 新增工作组
     * @param group
     * @return
     */
    public int insertOgGroup(OgGroup group);

    public int insertOgGroupMysql(OgGroup group);

    /**
     * 修改工作组
     * @param group
     * @return
     */
    public int updateOgGroup(OgGroup group);

    public int updateOgGroupMysql(OgGroup group);

    /**
     * 根据删除工作组
     * @param ids
     * @return
     */
    public int deleteOgGroupByIds(String[] ids);

    /**
     * 查询工作组成员列表
     * @param person
     * @return
     */
    public List<OgGroupPerson> selectOgGroupPersonList(OgGroupPerson person);

    /**
     * 查询工作组成员（添加用户查询）
     * @param user
     * @return
     */
    public List<OgPerson> selectUserList(OgPerson person);

    /**
     * 增加工作组成员信息
     * @param persons
     * @return
     */
    public int insertOgGroupPerson(OgGroupPerson person);

    /**
     * 修改工作组成员
     * @param person
     * @return
     */
    public int updateOgGroupPerson(OgGroupPerson person);

    /**
     * 根据id删除工作组成员
     * @param ids
     * @return
     */
    public int deleteOgGroupPersonByIds(String[] ids);

    /**
     * 根据id查询工作组成员
     * @param gpId
     * @return
     */
    public OgGroupPerson selectOgGroupPersonById(String gpId);

    /**
     * 根据工作组名称查询人员
     * @param groupName
     * @return
     */
    public List<OgPerson> selectOgPersonByGroupName(String groupName);
    /**
     * 根据当前登陆人机构查询所辖范围的工作组
     */
    public List<OgGroup> getOgProviceGroups(String code);
    /**
     * 根据系统ID查询工作组
     */
    public List<OgGroup> selectOgGroupBySys(String id);
    /**
     * 根据工作组ID查询人员
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
     * @param pId
     * @return
     */
    OgGroup selectOgPersonById(String pId);
    /**
     * 根据工作组ID查询组内人员信息
     *
     * @param groupId
     * @return
     */
    public List<OgGroupPerson> selectGroupIdByPersonList(String groupId);

    List<OgGroupPerson> selectOgPersonByIdTwo(String pId);

    List<OgPerson> selectLxbgGroupByPerson(String groupId);

    List<OgGroup> selectGroupByGpositionTwo(OgGroup ogGroup);

    public List<OgGroup> selectLoginGroups(String userId);

    List<OgGroup> selectOgGroupAllList(OgGroup group);

    List<OgGroup> selectOgGroupListAll(OgGroup group);

    List<OgGroup> selectOgGroupListSysIdIn(OgGroup group);


    /**
     * 根据工作组名称查询用户
     * @param groupName
     * @return
     */
     List<OgGroupPerson> selectOgGroupPersonListByGroupName(String groupName);
}
