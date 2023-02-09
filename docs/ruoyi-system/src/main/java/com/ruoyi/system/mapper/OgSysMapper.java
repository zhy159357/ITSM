package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgSys;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OgSysMapper {

    /**
     * 根据条件查询系统集合
     *
     * @param ogSys
     * @return
     */
    public List<OgSys> selectOgSysList(OgSys ogSys);

    /**
     * 根据id查询应用系统
     *
     * @param sysId
     * @return
     */
    public OgSys selectOgSysBySysId(String sysId);

    /**
     * 根据名称查询应用系统
     *
     * @param caption
     * @return
     */
    public OgSys selectOgSysBySysName(String caption);

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
     * @param sysIds
     * @return
     */
    public int deleteOgSysByIds(String[] sysIds);

    /**
     * 根据条件查询系统集合
     *
     * @param
     * @return
     */
    public List<OgSys> selectOgSysListByCondition(OgSys sys);

    void updateOgSysAndCmdb(OgSys ogSys);


    List<OgSys> selectOgSysListWork(OgSys sys);

    /**
     * 通过系统编码查询
     *
     * @param sysCode
     * @return
     */
    OgSys selectOgSysBySysCode(String sysCode);
    OgSys selectOgSysBySysCodeForProblem(String sysCode);

    List<OgSys> selectOgSysByName(String s);

    List<OgSys> selectOgSysByIds(List<String> ids);

    /**
     * 根据条件查询系统集合
     * 根据系统名称精准查询
     *
     * @param ogSys
     * @return
     */
    public List<OgSys> selectOgSysListForCaption(OgSys ogSys);


    /**
     * 根据当前登陆用户查询应用系统
     *
     * @param
     * @return
     */
     List<OgSys> selectOgSysListByName(OgSys sys);

    /**
     *
     * @param userId
     * @return
     */
     List<String> getSysId(@Param("userId") String userId);
}
