package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgPerson;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 用户表表 数据层
 *
 * @author ruoyi
 */
public interface OgPersonMapper {


    /**
     * 新增人员信息
     *
     * @param ogPerson 人员信息
     * @return 结果
     */
    public int insertOgPerson(OgPerson ogPerson);


    List<OgPerson> selectOgPersonList(OgPerson ogPerson);
    List<Map<String, String>> selectOgPersons(OgPerson ogPerson);

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

    List<OgPerson> selectPersonListByPIdsMysql(String ids[]);

    List<OgPerson> selectListByRoleId(OgPerson ogPerson);

    List<OgPerson> selectListByRoleIdMysql(OgPerson ogPerson);

    List<OgPerson> selectCmBizPerson(OgPerson ogPerson);

    /**
     * 根据orgId和postId查询人员信息
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectOgPersonByOrgAndPostId(OgPerson ogPerson);

    List<OgPerson> selectOgPersonForOrgIdList(String orgId);

    List<OgPerson> selectLeaderByOrgid(String orgid);

    List<OgPerson> selectPersonsByOrgid(String orgid);

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
    List<OgPerson> selectPersonByPostId(Map<String,String> map);


    OgPerson selectOgPersonEvoById(String performUserId);


    List<OgPerson> selectOgPersonOrgById(String orgId);

    OgPerson checkPhoneUnique(String phone);
    OgPerson checkPhone(String phone);

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
    List<OgPerson> selectBatchPersonList(Map<String,Object> map);

    List<OgPerson> selectPersonListByLevelCode(OgPerson ogPerson);

    List<OgPerson> selectList(OgPerson ogPerson);

    List<OgPerson> selectPersonListByPostAndRole(Map<String,Object> map);

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

    List<OgPerson> selectListByOrgIdAllAndPostId(@Param("orgId") String nodeId, @Param("postId") String postId);
    List<OgPerson> selectListByOrgIdAllAndPostIdBosc(@Param("orgId") String orgId,@Param("postId") String postId);

    OgPerson selectRoleByIdTwo(String userId);

    List<OgPerson> selectOgPersonJqList(OgPerson ogPerson);

    List<OgPerson> selectAssessList(OgPerson ogPerson);

    List<OgPerson> selectPostByuser(OgPerson person);

    List<OgPerson> selectAppCheck(OgPerson ogPerson);

    /**
     * 查询指定机构下的所有人员
     *
     * @param orgId
     * @return
     */
    List<OgPerson> selectOgPersonListByOrgId(@Param("orgId") String orgId);

    /**
     * 通过角色ID或者岗位ID查询本级及上级机构人员
     *
     * @param ogPerson
     * @return
     */
    List<OgPerson> selectOgPersonByRoleIdORByPostId(OgPerson ogPerson);

    int updateAgencyIsNull(OgPerson ogPerson);

    /**
     * tinyWeb查询审核人
     * @return
     */
    List<OgPerson> selectCheckForTinyWeb(OgPerson ogPerson);

    /**
     * tinyWeb查询环境新建单 接口人 系统设备部全部人员
     * @return
     */
    List<OgPerson> selectContactPersonForTinyWeb(OgPerson ogPerson);

    List<OgPerson> selectCheckForTinyWebPerson(OgPerson persons);

    int insertOgPersonBatch(@Param("list") List<OgPerson> list);

    OgPerson selectUserInfo(OgPerson ogPerson);
    List<OgPerson> selectListByOrgIdAllAndGroupId(@Param("orgId") String orgId,@Param("groupId") String groupId);

    OgPerson selectOgPersonByPidForUpdateStatus(String pId);
}
