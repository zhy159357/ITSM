package com.ruoyi.system.service;


import com.ruoyi.common.core.domain.entity.OgPerson;

import java.util.List;
import java.util.Map;

/**
 * 用户 业务层
 */
public interface IConsumerService {

    public int insertOgPerson(OgPerson people);

    List<OgPerson> selectOgPersonList(OgPerson ogPerson);

    List<OgPerson> selectOgPersonAddList(OgPerson ogPerson);

    int updateOgPersonStatus(OgPerson ogPerson);

    OgPerson selectOgPersonById(String pId);

    /**
     * 禁用人员查询
     *
     * @param pId
     * @return
     */
    OgPerson selectOgPersonByPid(String pId);

    int updateOgPerson(OgPerson ogPerson);

    List<OgPerson> selectListByLevelCode(String levelCode, String rId);

    /**
     * 根据pid查询人员集合
     *
     * @param ids
     * @return
     */
    public List<OgPerson> selectPersonListByPIds(String[] ids);

    List<OgPerson> selectListByRoleId(OgPerson ogPerson);

    public List<OgPerson> selectCmbizPerson(OgPerson ogPerson);

    /**
     * 根据orgId和postId查询人员信息
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectOgPersonByOrgAndPostId(OgPerson ogPerson);

    /**
     * 根据groupId和roleId查询人员信息
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectPersonByGroupIdAndRoleId(OgPerson ogPerson);

    /**
     * 根据PostId查询人员信息
     *
     * @param map
     * @return
     */
    List<OgPerson> selectPersonByPostId(Map<String, String> map);

    /**
     * 根据手机号查询人员
     *
     * @param phone
     * @return
     */
    OgPerson selectOgPersonByPhone(String phone);

    /**
     * @param performUserId
     * @return
     */
    OgPerson selectOgPersonEvoById(String performUserId);


    /**
     * @param orgId
     * @return
     */
    List<OgPerson> selectOgPersonOrgById(String orgId);

    int deleteOgPersonByPIds(String pIds);

    OgPerson checkPhoneUnique(String phone);

    /**
     * 大数据根据手机号查询处理人
     *
     * @param phone
     * @return
     */
    OgPerson bigDataPersonByPhone(String phone);

    /**
     * 根据postIds的集合查询人员信息
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectOgPersonByPostIds(OgPerson ogPerson);

    /**
     * 根据账号查询对应的人员信息
     *
     * @param pIds
     * @return
     */
    List<OgPerson> selectBatchPersonList(OgPerson ogPerson);

    /**
     * 数据提取单进行人员查询
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectList(OgPerson ogPerson);

    /**
     * 根据岗位和角色进行人员信息的查询
     *
     * @param postId
     * @param roleId
     * @return
     */
    List<OgPerson> selectPersonListByPostAndRole(String postId, String roleId);

    /**
     * 根据机构层级和岗位查询人员信息
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectOgPersonByOrgLevAndPostIds(OgPerson ogPerson);

    //查询人员角色是：例行变更作业执行人的人员
    List<OgPerson> selectLxbgOgPersonOrgById(String orgId);

    /**
     * 查询事务事件单模板默认受理人
     *
     * @param nodeId
     * @param rId
     * @return
     */
    List<OgPerson> selectListByOrgIdAndRoleId(String nodeId, String rId);


    OgPerson selectRoleByIdTwo(String userId);

    /**
     * 人员管理页面
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectOgPersonJqList(OgPerson ogPerson);

    /**
     * 选择应用评估人
     * 根据岗位id加载
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectAppAssessPersonByPostId(OgPerson ogPerson);

    /**
     * 获取协同评估人
     * 系统/网络：网络管理一处+系统管理一处+系统管理二处+资源管理处（机构选择）
     * 开发：总行科技人员+总行科技处长（岗位）
     * 两部分数据使用并集
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectAssessList(OgPerson ogPerson);

    /**
     * 根据岗位ID查询人员信息
     *
     * @param person
     * @return
     */
    List<OgPerson> selectPostByuser(OgPerson person);

    /**
     * 应用复核人
     * 查询当前登录人机构下对应的处长+数据中心处长
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectAppCheck(OgPerson ogPerson);

    /**
     * 根据岗位ID和工作组ID查询对应的人
     *
     * @param map
     * @return
     */
    List<OgPerson> getOgPersonListByPostIdAndGroupId(Map map);

    /**
     * 根据角色ID查询对应的人
     * @param rId
     * @return
     */
    List<OgPerson> selectListByRoleId(String rId);
}
