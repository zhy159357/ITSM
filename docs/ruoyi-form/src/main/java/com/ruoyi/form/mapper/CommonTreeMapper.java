package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.CommonTree;
import java.util.List;

/**
 * commonTreeMapper接口
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public interface CommonTreeMapper 
{
    /**
     * 查询commonTree
     * 
     * @param id commonTreeID
     * @return commonTree
     */
    public CommonTree selectCommonTreeById(Long id);

    public CommonTree selectCommonTreeByName(String name);

    /**
     * 查询commonTree列表
     * 
     * @param commonTree commonTree
     * @return commonTree集合
     */
    public List<CommonTree> selectCommonTreeList(CommonTree commonTree);

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
     * 删除commonTree
     * 
     * @param id commonTreeID
     * @return 结果
     */
    public int deleteCommonTreeById(Long id);

    /**
     * 批量删除commonTree
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCommonTreeByIds(String[] ids);
    CommonTree selectCommonTreeByOgId(String ogId);
    CommonTree selectCommonTreeByStrId(String id);
}
