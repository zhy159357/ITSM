package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgOrg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author ruoyi
 */
@Component
public interface SysDeptMapper
{
    /**
     * 查询部门人数
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int selectDeptCount(OgOrg dept);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(String deptId);

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<OgOrg> selectDeptList(OgOrg dept);

    /**
     * 查询部门管理数据 （数据变更单）
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<OgOrg> selectDeptListTwo(OgOrg dept);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(OgOrg dept);

    public int insertDeptMysql(OgOrg dept);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(OgOrg dept);

    public int updateDeptMysql(OgOrg dept);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(OgOrg dept);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public OgOrg selectDeptById(String deptId);

    /**
     * 根据部门ID数组查询信息
     *
     * @param orgIds 机构id数组
     * @return 部门信息
     */
    public List<OgOrg> selectDeptByIds(String[] orgIds);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    public OgOrg checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") String parentId);

    /**
     * 根据角色ID查询部门
     *
     * @param roleId 角色ID
     * @return 部门列表
     */
    public List<String> selectRoleDeptTree(Long roleId);

    /**
     * 修改所在部门的父级部门状态
     *
     * @param dept 部门
     */
    public void updateDeptStatus(OgOrg dept);

    public void updateDeptStatusMysql(OgOrg dept);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<OgOrg> selectChildrenDeptById(String deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(String deptId);

    /**
     * 根据部门编码更新层级和级别信息
     * @param sysDept
     * @return 结果
     */
    public int updateDeptByTierAndLevel(OgOrg sysDept);

    /**
     * 根据结构编码查询
     * @param deptCode
     * @return
     */
    public OgOrg selectDeptByCode(String deptCode);

    /**
     * 根据机构编码模糊查询
     * 主要涉及总行中心｜下发分行｜下发技术局查询
     * @param org
     * @return
     */
    public List<OgOrg> selectDeptByOrgCodeLike(OgOrg org);

    /**
     * 根据结构名称查询
     * @param orgName
     * @return
     */
    public OgOrg selectDeptByOrgName(String orgName);

    /**
     * 系统条线故障 故障登记查询
     * @return
     */
    List<OgOrg> selectDeptListByDJ();

    public List<OgOrg> selectAllChildOrg(String orgId);

    List<OgOrg> selectDeptListByOrgId(OgOrg dept);

    List<OgOrg> selectOrgList(String orgid);

}
