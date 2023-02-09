package com.ruoyi.form.service;

import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysDeptVo;
import com.ruoyi.form.domain.CommonTree;
import java.util.List;

/**
 * commonTreeService接口
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public interface ICommonTreeService 
{
    /**
     * 查询commonTree
     * 
     * @param id commonTreeID
     * @return commonTree
     */
    public CommonTree selectCommonTreeById(Long id);
    /**
     * 查询commonTree
     *
     * @param id commonTreeID
     * @return commonTree
     */
    public CommonTree selectCommonTreeByStrId(String id);
    /**
     * 查询commonTree列表
     * 
     * @param commonTree commonTree
     * @return commonTree集合
     */
    public List<SysDeptVo> selectCommonTreeList(CommonTree commonTree);

    /**
     * 查询最下层机构｜工作组信息
     *
     * @return
     */
    List<CommonTree> selectCommonTreeByOrgFlag(String orgFlag);

    CommonTree selectCommonTreeByOgId(String ogId);

    CommonTree selectCommonTreeByName(String name);

    /**
     * 新增commonTree
     * 
     * @param commonTree commonTree
     * @return 结果
     */
    public int insertCommonTree(CommonTree commonTree);

    /**
     * 修改commonTree
     * 
     * @param commonTree commonTree
     * @return 结果
     */
    public int updateCommonTree(CommonTree commonTree);

    /**
     * 批量删除commonTree
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCommonTreeByIds(String ids);

    /**
     * 删除commonTree信息
     * 
     * @param id commonTreeID
     * @return 结果
     */
    public int deleteCommonTreeById(Long id);

    List<Ztree> selectCommonTreeListData();

    List<CommonTree> selectCommonTreeListPage(CommonTree commonTree);

    List<Object> selectCommonTreeListDataChild();
}
