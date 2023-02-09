package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.system.http.entegorserver.entity.LabelValue;

import java.util.List;

public interface ISysApplicationManagerService {

    /**
     * 根据条件查询系统集合
     *
     * @param ogSys
     * @return
     */
    public List<OgSys> selectOgSysList(OgSys ogSys);

    public List<OgPerson> selectOgPersonList(OgPerson ogPerson);

    /**
     * 根据id查询应用系统
     *
     * @param sysId
     * @return
     */
    public OgSys selectOgSysBySysId(String sysId);

    /**
     * 根据系统名称查询应用系统
     *
     * @param sysName
     * @return
     */
    public OgSys selectOgSysBySysName(String sysName);

    /**
     * 保存应用系统
     *
     * @param ogSys
     * @return
     */
    public int insertOgSys(OgSys ogSys);

    /**
     * 修改应用系统
     *
     * @param ogSys
     * @return
     */
    public int updateOgSys(OgSys ogSys);

    /**
     * 删除应用系统
     *
     * @param ids
     * @return
     */
    public int deleteOgSysByIds(String ids);

    /**
     * 根据条件查询系统集合关联工作组中的sysid不为空
     *
     * @param
     * @return
     */
    public List<OgSys> selectOgSysListByCondition(OgSys sys);

    /**
     * 根据所属处室查询系统负责人
     *
     * @param orgId
     * @return
     */
    public List<OgPerson> selectOgPersonForOrgIdList(String orgId);

    /**
     * 同步更新
     *
     * @param ogSys
     */
    void updateOgSysAndCmdb(OgSys ogSys);


    List<OgSys> selectOgSysListWork(OgSys sys);

    /**
     * 通过系统编码查询
     *
     * @param sysCode
     * @return
     */
    public OgSys selectOgSysBySysCode(String sysCode);
    public OgSys selectOgSysBySysCodeForProblem(String sysCode);

    /**
     * 通过系统名称模糊查询
     *
     * @param s
     * @return
     */
    List<OgSys> selectOgSysByName(String s);

    /**
     * 根据多个系统ID批量查询系统
     *
     * @param ids
     * @return
     */
    List<OgSys> selectOgSysByIds(List<String> ids);


    /**
     * 查询系统 flag == true 查当前登陆人所负责的系统 : 查所有
     * @param flag
     * @param userId
     * @return
     */
    List<LabelValue> selectOgSysForTinyWeb(String flag, String userId);

}
