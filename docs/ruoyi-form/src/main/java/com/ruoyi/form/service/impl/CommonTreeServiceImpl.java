package com.ruoyi.form.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysDeptVo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.form.domain.CommonTree;
import com.ruoyi.form.mapper.CommonTreeMapper;
import com.ruoyi.form.service.ICommonTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * commonTreeService业务层处理
 *
 * @author ruoyi
 * @date 2022-11-04
 */
@Service
public class CommonTreeServiceImpl implements ICommonTreeService {
    @Autowired
    private CommonTreeMapper commonTreeMapper;

    /**
     * 查询commonTree
     *
     * @param id commonTreeID
     * @return commonTree
     */
    @Override
    public CommonTree selectCommonTreeById(Long id) {
        return commonTreeMapper.selectCommonTreeById(id);
    }
    @Override
    public CommonTree selectCommonTreeByStrId(String id)
    {
        return commonTreeMapper.selectCommonTreeByStrId(id);
    }
    /**
     * 查询commonTree列表
     *
     * @param commonTree commonTree
     * @return commonTree
     */
    @Override
    public List<SysDeptVo> selectCommonTreeList(CommonTree commonTree) {
        List<SysDeptVo> sys = getChild("0");
        return sys;
    }

    private List<SysDeptVo> getChild(String parentId) {
        List<SysDeptVo> childrenList = new LinkedList();
        CommonTree commonTree = new CommonTree();
        commonTree.setParentId(parentId);
        List<CommonTree> list = commonTreeMapper.selectCommonTreeList(commonTree);
        if (!CollectionUtil.isEmpty(list)) {
            for (CommonTree commonTree1 : list) {
                SysDeptVo sysDeptVo = new SysDeptVo();
                sysDeptVo.setValue(commonTree1.getId().toString());
                sysDeptVo.setLabel(commonTree1.getName());
                sysDeptVo.setChildren(getChild(commonTree1.getId().toString()));
                childrenList.add(sysDeptVo);
            }
        }
        return childrenList;
    }

    /**
     * 新增commonTree
     *
     * @param commonTree commonTree
     * @return 结果
     */
    @Override
    public int insertCommonTree(CommonTree commonTree) {
        return commonTreeMapper.insertCommonTree(commonTree);
    }

    /**
     * 修改commonTree
     *
     * @param commonTree commonTree
     * @return 结果
     */
    @Override
    public int updateCommonTree(CommonTree commonTree) {
        return commonTreeMapper.updateCommonTree(commonTree);
    }

    /**
     * 删除commonTree对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCommonTreeByIds(String ids) {
        return commonTreeMapper.deleteCommonTreeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除commonTree信息
     *
     * @param id commonTreeID
     * @return 结果
     */
    @Override
    public int deleteCommonTreeById(Long id) {
        return commonTreeMapper.deleteCommonTreeById(id);
    }

    /**
     * 机构树
     *
     * @return
     */
    @Override
    public List<Ztree> selectCommonTreeListData() {
        CommonTree commonTree = new CommonTree();
        List<CommonTree> list = commonTreeMapper.selectCommonTreeList(commonTree);
        List<Ztree> zList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(commonTree1 -> {
                Ztree ztree = new Ztree();
                ztree.setId(commonTree1.getId().toString());
                ztree.setpId(commonTree1.getParentId());
                ztree.setName(commonTree1.getName());
                ztree.setTitle(commonTree1.getName());
                zList.add(ztree);
            });
        }
        return zList;
    }
    /**
     * 分行机构树
     *
     * @return
     */
    @Override
    public List<Object> selectCommonTreeListDataChild() {
        CommonTree commonTree = new CommonTree();
        commonTree.setParentId("2");
        List<CommonTree> list = commonTreeMapper.selectCommonTreeList(commonTree);
        List<Object> zList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(commonTree1 -> {
                Map<String, String> mapOrg = new HashMap<>();
                mapOrg.put("value", commonTree1.getOgId());
                mapOrg.put("label", commonTree1.getName());
                zList.add(mapOrg);
            });
        }
        return zList;
    }
    @Override
    public List<CommonTree> selectCommonTreeByOrgFlag(String orgFlag) {
        CommonTree commonTree = new CommonTree();
        commonTree.setOrgFlag(orgFlag);
        return commonTreeMapper.selectCommonTreeList(commonTree);
    }

    @Override
    public CommonTree selectCommonTreeByOgId(String ogId) {
        return commonTreeMapper.selectCommonTreeByOgId(ogId);
    }

    @Override
    public CommonTree selectCommonTreeByName(String name) {
        return commonTreeMapper.selectCommonTreeByName(name);
    }

    /**
     * 查询commonTree列表
     *
     * @param commonTree commonTree
     * @return commonTree
     */
    @Override
    public List<CommonTree> selectCommonTreeListPage(CommonTree commonTree) {
        return commonTreeMapper.selectCommonTreeList(commonTree);
    }

}
