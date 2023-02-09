package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.SysFolder;
import com.ruoyi.common.core.domain.entity.SysInfo;
import com.ruoyi.common.core.domain.entity.SysRole;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author ruoyi
 */
public interface SysInfoMapper
{
    /**
     * 根据条件分页查询角色数据
     *
     * @param info 应用话机绑定信息
     * @return 角色数据集合信息
     */
    public List<SysInfo> selectInfoList(SysInfo info);

    public List<SysInfo> selectInfoMysqlList(SysInfo info);

    public List<SysInfo> selectIFList(SysInfo info);

    public SysInfo selectInfoById(String regime_info_id);

    public SysInfo selectState(String regime_info_id);

    public SysFolder selectTreeById(String folder);
    public String selectTreeNameById(String selecttreeId);

    /**
     * 通过角色ID删除角色
     * @return 结果
     */
    public int deleteInfoById(String regime_info_id);

    /**
     * 批量角色用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteInfoByIds(String[] ids);

    /**
     * 根据版本号查询
     *
     * @param regime_title
     */
    public SysInfo selectInfoByNo(String regime_title);


    /**
     * 修改角色信息
     *
     * @param info 角色信息
     * @return 结果
     */
    public int updateInfo(SysInfo info);

    public int deleteInfo(SysInfo info);
    /**
     * 查询部门管理数据
     * @param folder 部门信息
     */
    public List<SysFolder> selectFolderList(SysFolder folder);
    /**
     * 新增角色信息
     * @param info 角色信息
     * @return 结果
     */
    public int insertInfo(SysInfo info);

    /**
     * 校验角色名称是否唯一
     * @param infoName 角色名称
     * @return 角色信息
     */
    public SysInfo checkInfoNameUnique(String infoName);

    /**
     * 校验角色权限是否唯一
     *
     * @param infoKey 角色权限
     * @return 角色信息
     */
    public SysRole checkRoleKeyUnique(String infoKey);

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysInfo> selectInfosByUserId(Long userId);

    /**
     * 根据审核人查询名称
     * @param ogPerson
     * @return
     */
    public List<OgPerson> selectCheckerList(OgPerson ogPerson);

     public List<SysInfo> selectAuditList(SysInfo info);

     public List<SysInfo> selectAuditMysqlList(SysInfo info);

}