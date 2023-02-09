package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.DutyVersion;
import com.ruoyi.common.core.domain.entity.OgOrg;

import java.util.List;
import java.util.Map;

/**
 * 版本业务层
 * 
 * @author ruoyi
 */
public interface IDutyVersionService
{
    /**
     * 根据条件分页查询版本数据
     * @param dutyVersion 版本信息
     * @return 版本数据集合信息
     */
    public List<DutyVersion> selectVersionList(DutyVersion dutyVersion);

    /**
     * 通过versionId查询版本
     * @param versionId 版本ID
     * @return 版本对象信息
     */
    public DutyVersion selectVersionById(String versionId);

    /**
     * 通过versionId预览
     * @param dutyVersion 版本ID
     */
    public Map<String,Object> selectPreview(DutyVersion dutyVersion);

    /**
     * 通过versionTypeinfoId查询版本
     * @param versionTypeinfoId 版本ID
     * @return 版本对象信息
     */
    public DutyVersion selectVersionTypeinfoById(String versionTypeinfoId);

    /**
     * 通过版本ID查询类别
     * @param dutyVersion
     * @return 岗位对象信息
     */
    public List<DutyVersion> selectTypeinfoByVersionId(DutyVersion dutyVersion);

    /**
     * 通过版本ID删除版本
     * @param versionId 版本ID
     * @return 结果
     */
    public boolean deleteVersionById(Long versionId);

    /**
     * 批量版本信息
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public AjaxResult deleteVersionByIds(String ids) throws Exception;

    /**
     * 批量类别信息
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public AjaxResult deleteVersionTypeinfos(String ids) throws Exception;

    /**
     * 新增版本信息
     * @param dutyVersion 版本信息
     * @return 结果
     */
    public int insertVersion(DutyVersion dutyVersion);

    /**
     * 修改版本信息
     * @param dutyVersion 版本信息
     * @return 结果
     */
    public int updateVersion(DutyVersion dutyVersion);

    /**
     * 修改类别信息
     * @param dutyVersion
     * @return 结果
     */
    public int updateVersionTypeinfo(DutyVersion dutyVersion);

    /**
     * 修改类别信息
     * @param versionId
     * @return 结果
     */
    public int versionClone(String versionId);

    /**
     * 校验版本代码是否唯一
     * @return 版本信息
     */
    public String checkVersionNoUnique(DutyVersion dutyVersion);

    /**
     * 校验版本代码是否唯一
     * @return 版本信息
     */
    public String checkDutyDateUnique(DutyVersion dutyVersion);

    /**
     * 根据类别ID查询信息
     * @param dutyVersion 类别ID
     * @return 类别信息
     */
    public DutyVersion selectTypeinfoById(DutyVersion dutyVersion);

    /**
     * 查询类别管理树
     * @param dutyVersion 部门信息
     * @return 所有部门信息
     */
    public List<Ztree> selectTypeinfoTree(DutyVersion dutyVersion);

}
