package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.*;

/**
 * 部门管理 服务层
 *
 * @author ruoyi
 */
public interface ISysDeptService
{
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<OgOrg> selectDeptList(OgOrg dept);

    public List<OgOrg> selectDeptListByOrgId(OgOrg dept);

    /**
     * 查询部门管理数据 （数据变更单）
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<OgOrg> selectDeptListTwo(OgOrg dept);

    /**
     * 查询部门管理树
     *
     * @param dept 部门信息
     * @return 所有部门信息
     */
    public List<Ztree> selectDeptTree(OgOrg dept);

    /**
     * 人员查询部门管理树
     *
     * @param dept 部门信息
     * @return 所有部门信息
     */
    public List<Ztree> selectDeptTreePeople(OgOrg dept);

    /**
     * 查询部门管理树（排除下级）
     *
     * @param dept 部门信息
     * @return 所有部门信息
     */
    public List<Ztree> selectDeptTreeExcludeChild(OgOrg dept);

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    public List<Ztree> roleDeptTreeData(SysRole role);

    /**
     * 查询部门人数
     *
     * @param parentId 父部门ID
     * @return 结果
     */
    public int selectDeptCount(String parentId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(String deptId);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(OgOrg dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(OgOrg dept);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public OgOrg selectDeptById(String deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(String deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String checkDeptNameUnique(OgOrg dept);

    /**
     * 根据部门编码更新层级和级别信息
     * @param dept
     * @return
     */
    public int updateDeptByTierAndLevel(OgOrg dept);

    /**
     * 根据机构编码查询
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
     * 根据机构名称查询
     * @param orgName
     * @return
     */
    public OgOrg selectDeptByOrgName(String orgName);
    /**
     * 查询登陆人是否全国中心
     * @return
     */
    public String getIsCenter();
    /**
     * 查询当前登陆人2级机构
     */
    public String getpCode(String id);
    /**
     * 查询当前登录人机构号
     */
    public String getLoginOrgCode(String id);

    /**
     *
     * @param orgIds
     * @param ogOrg
     * @return
     */
    List<OgOrg> selectDeptLxbgList(String orgIds, OgOrg ogOrg);

    /**
     * 系统条线故障 故障登记查询
     * @return
     */
    List<Ztree> selectDeptListByDJ();

    /**
     * 根据机构id获取下级机构（包括父级机构）
     * @param orgId
     * @return
     */
    List<OgOrg> selectAllChildOrg(String orgId);

    List<OgOrg> selectAllChildOrgone(String orgId);

    /**
     * 查询指定机构下的所有部室及子部室
     *
     * @param orgId
     * @return
     */
    List<OgOrg> selectOrgList(String orgId);

    /**
     * 查询部门管理树
     *
     * @param dept 部门信息
     * @return 所有部门信息
     */
    public List<Ztree> selectAllDeptTree(OgOrg dept);

    /**
     * type 赋值 0-总行  1-分行 2-子公司
     *
     * @return 所有部门信息
     */
    public int isHeadOffice();

    /**
     * 查询所有参与机构(返回键值对)
     *
     * @return 查询所有参与机构
     */
    public List<SysDeptVo> treeDataForPartakeOrg();

    /**
     * 获取指定机构的参与机构（添加人员时用）
     * @param orgId
     * @return
     */
    public OgOrg getPartakeOrg(String orgId);

    /**
     * 判断是否总行  上海银行
     * @param orgId
     * @return
     */
    boolean judgeCurrentOrgIfAll(String orgId);
}
