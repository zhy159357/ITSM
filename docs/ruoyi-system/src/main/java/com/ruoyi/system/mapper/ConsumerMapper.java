package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgPerson;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 用户 数据层
 *
 * @author ruoyi
 */
public interface ConsumerMapper {


    /**
     * 新增人员信息
     *
     * @param ogPerson 人员信息
     * @return 结果
     */
    public int insertOgPerson(OgPerson ogPerson);


    List<OgPerson> selectOgPersonList(OgPerson ogPerson);

    List<OgPerson> selectOgPersonAddList(OgPerson ogPerson);

    int updateOgPersonStatus(OgPerson ogPerson);

    OgPerson selectOgPersonById(String pId);

    OgPerson selectOgPersonByPid(String pId);

    int updateOgPerson(OgPerson ogPerson);

    List<OgPerson> selectListByLevelCode(String levelCode, String rId);

    /**
     * 根据条件查询审核人员
     *
     * @return
     */
    public List<OgPerson> selectBusinessAuditList(OgPerson person);

    List<OgPerson> selectPersonListByPIds(String ids[]);

    List<OgPerson> selectListByRoleId(OgPerson ogPerson);

    List<OgPerson> selectCmBizPerson(OgPerson ogPerson);

    /**
     * 根据orgId和postId查询人员信息
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectOgPersonByOrgAndPostId(OgPerson ogPerson);

    List<OgPerson> selectOgPersonForOrgIdList(String orgId);

    /**
     * 根据groupId和roleId查询人员信息
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectPersonByGroupIdAndRoleId(OgPerson ogPerson);

    /**
     * 根据岗位查询对应的人员信息
     * @param map
     * @return
     */
    List<OgPerson> selectPersonByPostId(Map<String, String> map);


    OgPerson selectOgPersonEvoById(String performUserId);


    List<OgPerson> selectOgPersonOrgById(String orgId);

    OgPerson checkPhoneUnique(String phone);

    /**
     * 大数据查询处理人
     * @param phone
     * @return
     */
    OgPerson bigDataPersonByPhone(String phone);

    int deleteOgPersonByPIds(String[] strArray);

    /**
     * 根据postIds的集合查询人员信息
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectOgPersonByPostIds(OgPerson ogPerson);

    /**
     * 根据pIds的集合查询人员信息
     * @param map
     * @return
     */
    List<OgPerson> selectBatchPersonList(Map<String, Object> map);

    List<OgPerson> selectList(OgPerson ogPerson);

    List<OgPerson> selectPersonListByPostAndRole(Map<String, Object> map);

    /**
     * 根据机构层级和岗位查询人员信息
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectOgPersonByOrgLevAndPostIds(OgPerson ogPerson);

    List<OgPerson> selectLxbgOgPersonOrgById(String orgId);

    /**
     * 事务事件单模板默认受理人
     * @param nodeId
     * @param rId
     * @return
     */
    List<OgPerson> selectListByOrgIdAndRoleId(@Param("orgId") String nodeId, @Param("rId") String rId);

    OgPerson selectRoleByIdTwo(String userId);

    List<OgPerson> selectOgPersonJqList(OgPerson ogPerson);

    List<OgPerson> selectAssessList(OgPerson ogPerson);

    List<OgPerson> selectPostByuser(OgPerson person);

    List<OgPerson> selectAppCheck(OgPerson ogPerson);

    List<OgPerson> getOgPersonListByPostIdAndGroupId(Map map);

    List<OgPerson> selectListByRoleId(@Param("rId") String rId);
}
