package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.DutyTypeinfo;
import com.ruoyi.common.core.domain.entity.DutyVersion;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.SysAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 版本表 数据层
 * @author dayong_sun
 */
public interface DutyVersionMapper
{
    /**
     * 根据条件分页查询版本数据
     * @param dutyVersion 版本信息
     * @return 版本数据集合信息
     */
    public List<DutyVersion> selectVersionList(DutyVersion dutyVersion);
    public List<DutyVersion> selectVersionMysqlList(DutyVersion dutyVersion);

    /**
     * 通过versionId查询版本
     * @param versionId 版本ID
     * @return 版本对象信息
     */
    public DutyVersion selectVersionById(String versionId);
    public DutyVersion selectVersionByIdMysql(String versionId);

    /**
     * 通过versionId查询版本
     * @param versionTypeinfoId 版本ID
     * @return 版本对象信息
     */
    public DutyVersion selectVersionTypeinfoById(String versionTypeinfoId);
    public DutyVersion selectVersionTypeinfoMysqlById(String versionTypeinfoId);

    /**
     * 通过版本ID查询类别
     * @param dutyVersion
     * @return 版本对象信息
     */
    public List<DutyVersion> selectTypeinfoByVersionId(DutyVersion dutyVersion);
    public List<DutyVersion> selectTypeinfoByVersionIdMysql(DutyVersion dutyVersion);

    /**
     * 通过版本ID查询类别
     * @param dutyVersion
     * @return 版本对象信息
     */
    public List<DutyVersion> selectTypeinfoByVersionTypeinfo(DutyVersion dutyVersion);

    /**
     * 插入版本和类别对应关系表数据
     * @param list
     */
    public int insertVersionTypeinfo(List<DutyVersion> list);
    public int insertVersionTypeinfoMysql(List<DutyVersion> list);

    /**
     * 通过版本ID删除版本
     * @param versionId 版本ID
     * @return 结果
     */
    public int deleteVersionById(Long versionId);

    /**
     * 版本信息校验
     * @param ids 版本信息
     * @return 结果
     */
    public List<DutyVersion> selectVersionByIds(String[] ids);

    /**
     * 版本信息校验
     * @param ids 版本信息
     * @return 结果
     */
    public List<DutyVersion> selectVersionByVersionTypeinfoIds(String[] ids);

    /**
     * 批量版本信息
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteVersionByIds(String[] ids);

    /**
     * 批量类别信息
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteVersionTypeinfos(String[] ids);

    /**
     * 修改版本信息
     * @param dutyVersion 版本信息
     * @return 结果
     */
    public int updateVersion(DutyVersion dutyVersion);
    public int updateVersionMysql(DutyVersion dutyVersion);

    /**
     * 修改类别信息
     * @param dutyVersion 版本信息
     * @return 结果
     */
    public int updateVersionTypeinfo(DutyVersion dutyVersion);

    /**
     * 新增版本信息
     * @param dutyVersion 版本信息
     * @return 结果
     */
    public int insertVersion(DutyVersion dutyVersion);

    /**
     * 校验版本代码是否唯一
     * @return 版本信息
     */
    public DutyVersion checkVersionNoUnique(String versionNo);
    public DutyVersion checkVersionNoUniqueMysql(String versionNo);

    /**
     * 校验月份是否唯一
     * @return 版本信息
     */
    public DutyVersion checkDutyDateUnique(String versionNo);
    public DutyVersion checkDutyDateUniqueMysql(String versionNo);

    /**
     * 根据类别ID查询信息
     * @param dutyVersion 类别ID
     * @return 类别信息
     */
    public DutyVersion selectTypeinfoById(DutyVersion dutyVersion);

    /**
     * 查询信息
     * @param dutyVersion 类别ID
     * @return 类别信息
     */
    public List<DutyVersion> selectTypeinfoList(DutyVersion dutyVersion);

    /**
     * 查询最大版本号
     * @return 版本号
     */
    public DutyVersion selectMaxTypeNo();

    /**
     * 克隆版本信息
     * @param dutyVersion 版本信息
     * @return 结果
     */
    public int cloneVersion(DutyVersion dutyVersion);

    /**
     * 根据条件分页查询版本数据
     * @param dutyVersion 版本信息
     * @return 版本数据集合信息
     */
    public List<DutyVersion> selectVersionByDutyDate(DutyVersion dutyVersion);

    /**
     * 查询版本列表
     * @param dutyVersion 版本
     * @return 集合
     */
    public List<DutyVersion> selectVersionByParentId(DutyVersion dutyVersion);

    /**
     * 根据时间和类型递归查询所以子类
     * @param dutyVersion
     * @return
     */
    public List<String> selectVersionByDutyDateAndTypeNo(DutyVersion dutyVersion);

    public List<DutyVersion> findDutyVersionByTypeInfoId(@Param("typeInfoId") String typeInfoId,@Param("dutyDate")String dutyDate);

    public DutyVersion findDutyVersionByTypeNoAndDate(@Param("typeNo")String s, @Param("dutyDate")String dutyDate);

    public DutyVersion selectDutyTypeInfoById(@Param("typeInfoId") String typeInfoId,@Param("dutyDate")String dutyDate);

    List<DutyVersion>  selectDutyVersionList(@Param("dutyDate")String dutyDate);
}
