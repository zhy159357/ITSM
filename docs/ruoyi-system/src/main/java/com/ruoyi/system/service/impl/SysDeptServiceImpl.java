package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.util.CollectionUtils;

/**
 * 部门管理 服务实现
 *
 * @author ruoyi
 */
@Service("deptService")
public class SysDeptServiceImpl implements ISysDeptService {
    @Autowired
    private SysDeptMapper deptMapper;
    final
    IOgPersonService iOgPersonService;
    private final String SUCCESS = "1";// 启用状态
    private final String FAIL = "0";// 停用状态
    @Value("${pagehelper.helperDialect}")
    private String dbType;

    public SysDeptServiceImpl(IOgPersonService iOgPersonService) {
        this.iOgPersonService = iOgPersonService;
    }

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<OgOrg> selectDeptList(OgOrg dept) {
        return deptMapper.selectDeptList(dept);
    }

    @Override
    public List<OgOrg> selectDeptListByOrgId(OgOrg dept) {
        return deptMapper.selectDeptListByOrgId(dept);
    }

    /**
     * 查询部门管理数据 （数据变更单）
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<OgOrg> selectDeptListTwo(OgOrg dept) {
        return deptMapper.selectDeptListTwo(dept);
    }

    /**
     * 查询部门管理树
     *
     * @param dept 部门信息
     * @return 所有部门信息
     */
    @Override
    public List<Ztree> selectDeptTree(OgOrg dept) {
        List<OgOrg> deptList = deptMapper.selectDeptList(dept);
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    @Override
    public List<Ztree> selectDeptTreePeople(OgOrg dept) {
        List<OgOrg> deptList = deptMapper.selectDeptList(dept);
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    /**
     * 查询部门管理树（排除下级）
     *
     * @param dept 部门ID
     * @return 所有部门信息
     */
    @Override
    public List<Ztree> selectDeptTreeExcludeChild(OgOrg dept) {
        String deptId = dept.getOrgId();
        List<OgOrg> deptList = deptMapper.selectDeptList(dept);
        Iterator<OgOrg> it = deptList.iterator();
        while (it.hasNext()) {
            OgOrg d = (OgOrg) it.next();
            if (d.getOrgId().equals(deptId)
                    || ArrayUtils.contains(StringUtils.split(d.getLevelCode(), "/"), deptId + "")) {
                it.remove();
            }
        }
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    /**
     * 根据角色ID查询部门（数据权限）
     *
     * @param role 角色对象
     * @return 部门列表（数据权限）
     */
    @Override
    public List<Ztree> roleDeptTreeData(SysRole role) {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<OgOrg> deptList = selectDeptList(new OgOrg());
        if (StringUtils.isNotNull(roleId)) {
            List<String> roleDeptList = deptMapper.selectRoleDeptTree(roleId);
            ztrees = initZtree(deptList, roleDeptList);
        } else {
            ztrees = initZtree(deptList);
        }
        return ztrees;
    }

    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<OgOrg> deptList) {
        return initZtree(deptList, null);
    }

    /**
     * 对象转部门树
     *
     * @param deptList     部门列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<OgOrg> deptList, List<String> roleDeptList) {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (OgOrg dept : deptList) {
            if (SUCCESS.equals(dept.getInvalidationMark())) {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getOrgId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getOrgName());
                ztree.setTitle(dept.getOrgName());
                if (isCheck) {
                    ztree.setChecked(roleDeptList.contains(dept.getOrgId() + dept.getOrgName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    /**
     * 查询部门人数
     *
     * @param parentId 部门ID
     * @return 结果
     */
    @Override
    public int selectDeptCount(String parentId) {
        OgOrg dept = new OgOrg();
        dept.setParentId(parentId);
        return deptMapper.selectDeptCount(dept);
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(String deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(String deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(OgOrg dept) {
        OgOrg info = deptMapper.selectDeptById(dept.getParentId());
        // 如果父节点不为"正常"状态,则不允许新增子节点
        if (!SUCCESS.equals(info.getInvalidationMark())) {
            throw new BusinessException("部门停用，不允许新增");
        }
        String ancestors = info.getLevelCode() + dept.getOrgCode() + "/";
        dept.setLevelCode(ancestors);
        String[] anceArrys = ancestors.split("/");
        Integer anceLen = anceArrys.length - 1;
        dept.setOrgLv(anceLen.toString());
        dept.setOrgId(UUID.getUUIDStr());// 设置主键id
        int row = 0;
        if("mysql".equals(dbType)){
            row = deptMapper.insertDeptMysql(dept);
        }else{
            row = deptMapper.insertDept(dept);
        }

        return row;
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    /*@Override
    @Transactional
    public int updateDept(OgOrg dept)
    {
        OgOrg newParentDept = deptMapper.selectDeptById(dept.getParentId());
        OgOrg oldDept = selectDeptById(dept.getOrgId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            String newAncestors = newParentDept.getLevelCode() + newParentDept.getOrgId() + "/";
            String oldAncestors = oldDept.getLevelCode();
            dept.setLevelCode(newAncestors);
            updateDeptChildren(dept.getOrgId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (SUCCESS.equals(dept.getInvalidationMark()))
        {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatus(dept);
        }
        return result;
    }*/

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDept(OgOrg dept) {
        OgOrg oldDept = selectDeptById(dept.getOrgId());
        String orgCode = oldDept.getOrgCode();
        String oldLevelCode = oldDept.getLevelCode();
        String newLevelCode = oldLevelCode.replace(orgCode, dept.getOrgCode());
        dept.setLevelCode(newLevelCode);

        // 如果有子部门，需要修所有改子部门的层级码
        String deptId = dept.getOrgId();
        List<OgOrg> orgs = deptMapper.selectChildrenDeptById(deptId);
        if (orgs != null && !orgs.isEmpty()) {
            for (OgOrg org : orgs) {
                org.setLevelCode(org.getLevelCode().replace(org.getOrgCode(), dept.getOrgCode()));
                deptMapper.updateDeptChildren(org);
            }
        }
        if("mysql".equals(dbType)){
            return deptMapper.updateDeptMysql(dept);
        }else{
            return deptMapper.updateDept(dept);
        }
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatus(OgOrg dept) {
        String updateBy = dept.getUpdateBy();
        dept = deptMapper.selectDeptById(dept.getOrgId());
        dept.setUpdateBy(updateBy);
        if("mysql".equals(dbType)){
            deptMapper.updateDeptStatusMysql(dept);
        }else{
            deptMapper.updateDeptStatus(dept);
        }
    }

    /**
     * 修改子元素关系
     *
     * @param deptId 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    /*public void updateDeptChildren(String deptId, String newAncestors, String oldAncestors)
    {
        List<OgOrg> children = deptMapper.selectChildrenDeptById(deptId);
        for (OgOrg child : children)
        {
            child.setLevelCode(child.getLevelCode().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            deptMapper.updateDeptChildren(children);
        }
    }*/

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public OgOrg selectDeptById(String deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(String deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(OgOrg dept) {
        String deptId = dept.getOrgId();
        OgOrg info = deptMapper.checkDeptNameUnique(dept.getOrgName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getOrgId().equals(deptId)) {
            return UserConstants.DEPT_NAME_NOT_UNIQUE;
        }
        return UserConstants.DEPT_NAME_UNIQUE;
    }

    /**
     * 根据部门编码更新层级和级别信息
     *
     * @param dept
     * @return
     */
    @Override
    public int updateDeptByTierAndLevel(OgOrg dept) {
        return deptMapper.updateDeptByTierAndLevel(dept);
    }

    @Override
    public OgOrg selectDeptByCode(String deptCode) {
        return deptMapper.selectDeptByCode(deptCode);
    }

    @Override
    public List<OgOrg> selectDeptByOrgCodeLike(OgOrg org) {
        return deptMapper.selectDeptByOrgCodeLike(org);
    }

    @Override
    public OgOrg selectDeptByOrgName(String orgName) {
        return deptMapper.selectDeptByOrgName(orgName);
    }

    @Override
    public String getIsCenter() {
        String isCenter = "0";
        OgUser u = ShiroUtils.getOgUser();
        OgPerson person = iOgPersonService.selectOgPersonById(u.getpId());
        OgOrg org = this.selectDeptById(person.getOrgId());
        String levelCode = org.getLevelCode();
        if ((levelCode != null && levelCode.indexOf("010000888") > 0) || "000000000".equals(org.getOrgCode())) {
            isCenter = "1";
        }
        return isCenter;
    }

    @Override
    public String getpCode(String id) {
        OgPerson person = iOgPersonService.selectOgPersonById(id);
        OgOrg org = this.selectDeptById(person.getOrgId());
        String levelCode = org.getLevelCode();
        String pCode = "";
        if (levelCode != null) {
            String[] codes = levelCode.split("/");
            if (codes.length > 2) {
                pCode = codes[2];
            }
        }
        return pCode;
    }

    @Override
    public String getLoginOrgCode(String id) {
        OgPerson person = iOgPersonService.selectOgPersonById(id);
        OgOrg org = this.selectDeptById(person.getOrgId());
        String code = org.getOrgCode();
        return code;
    }


    @Override
    public List<OgOrg> selectDeptLxbgList(String orgIds, OgOrg ogOrg) {


        List<OgOrg> ogOrgs = deptMapper.selectDeptList(ogOrg);

        if(!"0".equals(orgIds)) {
            String[] org = Convert.toStrArray(orgIds);
            for (OgOrg og : ogOrgs) {
                for (String orgids : org) {
                    if (og.getOrgId().equals(orgids)) {
                        og.setFlag(true);
                        break;
                    }
                }
            }
        }

        return ogOrgs;
    }

    @Override
    public List<Ztree> selectDeptListByDJ() {
        List<OgOrg> ogOrgs = deptMapper.selectDeptListByDJ();
        List<Ztree> ztrees = initZtree(ogOrgs);
        return ztrees;
    }
    /**
     * 根据机构id获取下级机构（包括父级机构）
     * @param orgId
     * @return
     */
    @Override
   public List<OgOrg> selectAllChildOrg(String orgId){
        List<OgOrg> ogOrgs = deptMapper.selectAllChildOrg(orgId);
        ogOrgs.add(this.selectDeptById(orgId));
        return ogOrgs;

    }

    @Override
    public List<OgOrg> selectAllChildOrgone(String orgId) {
        List<OgOrg> ogOrgs = deptMapper.selectAllChildOrg(orgId);
        return ogOrgs;
    }

    /**
     * 查询指定机构下所有子部门
     *
     * @param orgId
     * @return
     */
    @Override
    public List<OgOrg> selectOrgList(String orgId) {
        return deptMapper.selectOrgList(orgId);
    }


    @Override
    public List<Ztree> selectAllDeptTree(OgOrg dept) {
        List<OgOrg> deptList = deptMapper.selectDeptList(dept);
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    //public int isHeadOffice() {
    //    OgOrg ogOrg = new OgOrg();
    //    int num = 0;
    //    List<OgOrg> deptList = deptMapper.selectDeptList(ogOrg);
    //    for (OgOrg org : deptList) {
    //        String orgId = org.getOrgId();
    //        while (!"310100003".equals(org.getOrgId())) {
    //            if (org.getOrgId().equals("310100001") || StringUtils.isEmpty(org.getParentId())) {
    //                break;
    //            }
    //            org = deptMapper.selectDeptById(org.getParentId());
    //        }
    //
    //        OgOrg ogOrg2 = new OgOrg();
    //        ogOrg2.setOrgId(orgId);
    //        if (org.getOrgName().equals("总行")) {
    //            ogOrg2.setType("0");
    //        } else {
    //            ogOrg2.setType("1");
    //        }
    //        deptMapper.updateDeptMysql(ogOrg2);
    //        num ++;
    //    }
    //    return num;
    //}

    public int isHeadOffice() {
        OgOrg ogOrg = new OgOrg();
        int num = 0;
        List<OgOrg> deptList = deptMapper.selectDeptList(ogOrg);
        for (OgOrg org : deptList) {
            List<String> orgLevCodeList = new ArrayList<>();
            String orgId = org.getOrgId();
            if (StringUtils.isNotEmpty(org.getParentId())) {
                while (!"310100001".equals(org.getOrgId())) {
                    orgLevCodeList.add(org.getOrgCode());
                    org = deptMapper.selectDeptById(org.getParentId());
                }
            }

            String orgLevCode = "/310100001";
            for (int i = orgLevCodeList.size() - 1 ; i >= 0 ; i --) {
                orgLevCode += "/" + orgLevCodeList.get(i);
            }

            OgOrg ogOrg2 = new OgOrg();
            ogOrg2.setOrgId(orgId);
            ogOrg2.setLevelCode(orgLevCode);
            deptMapper.updateDeptMysql(ogOrg2);
            num ++;
        }
        return num;
    }

    @Override
    public List<SysDeptVo> treeDataForPartakeOrg() {

        List<SysDeptVo> deptVoList = new ArrayList<>();
        List<OgOrg> list = selectParTaskOrg();
        if (!CollectionUtils.isEmpty(list)) {
            for (OgOrg org : list) {
                SysDeptVo sysDeptVo = new SysDeptVo();
                sysDeptVo.setLabel(org.getOrgName());
                sysDeptVo.setValue(org.getOrgId());
                deptVoList.add(sysDeptVo);
            }
        }
        //单独把苏州开发中心放进去 暂时
        SysDeptVo sysDeptVo = new SysDeptVo();
        sysDeptVo.setValue("310201107");
        sysDeptVo.setLabel("苏州开发测试中心");
        deptVoList.add(sysDeptVo);
        return deptVoList;
    }

    private List<OgOrg> selectParTaskOrg() {

        OgOrg dept = new OgOrg();
        List<OgOrg> list = new ArrayList<>();
        List<OgOrg> deptList = deptMapper.selectDeptList(dept);
        if (!CollectionUtils.isEmpty(deptList)) {
            for (OgOrg org : deptList) {
                if (StringUtils.isNotEmpty(org.getType()) && StringUtils.isNotEmpty(org.getLevelCode())) {
                    //总行
                    if ("0".equals(org.getType()) && 40 == org.getLevelCode().length()) {
                        list.add(org);
                    } else if ("1".equals(org.getType()) && 30 == org.getLevelCode().length()) {
                        list.add(org);
                    }
                }
            }
        }
        return list;
    }

    public OgOrg getPartakeOrg(String orgId) {

        OgOrg org = new OgOrg();

        OgOrg ogOrg = deptMapper.selectDeptById(orgId);
        if (null != ogOrg) {

            String levelCode = "";
            //如果是总行
            if ("0".equals(ogOrg.getType()) && ogOrg.getLevelCode().length() > 39) {
                levelCode = ogOrg.getLevelCode().substring(0,40);
            } else if ("1".equals(ogOrg.getType()) && ogOrg.getLevelCode().length() > 29) {
                levelCode = ogOrg.getLevelCode().substring(0,30);
            }
            if (StringUtils.isNotEmpty(levelCode)) {
                OgOrg org1 = new OgOrg();
                org1.getParams().put("levelCode", levelCode);
                List<OgOrg> list = deptMapper.selectDeptList(org1);
                if (!CollectionUtils.isEmpty(list)) {
                    org = list.get(0);
                }
            }
        }
        //如果是金融科技部或者是数管 参与机构是自己本机构
        if ("310200176".equals(org.getOrgId()) || "310200958".equals(org.getOrgId())) {
            org = deptMapper.selectDeptById(orgId);
            //如果是苏州开发中心下的开发管理部 参与机构是苏州开发中心
            if ("310201107".equals(org.getOrgId())) {
                org = deptMapper.selectDeptById("310200811");
            }
        }
        return org;
    }

    private final String allLevelCode = "/310100001/310100002/310100003";

    @Override
    public boolean judgeCurrentOrgIfAll(String orgId) {
        OgOrg ogOrg = this.selectDeptById(orgId);
        if(ogOrg != null) {
            if(ogOrg.getLevelCode().contains(allLevelCode)) {
                return true;
            }
        }
        return false;
    }
}
