package com.ruoyi.form.service;

import com.ruoyi.form.domain.ChmParavalue;
import java.util.List;

/**
 * 基础数据Service接口
 * 
 * @author ruoyi
 * @date 2022-10-24
 */
public interface IChmParavalueService 
{
    /**
     * 查询基础数据
     *
     * @param id 基础数据ID
     * @return 基础数据
     */
    public ChmParavalue selectChmParavalueById(Long id);
    /**
     * 查询基础数据
     *
     * @param name 基础数据名称
     * @return 基础数据
     */
    public ChmParavalue selectChmParavalueByName(String name);

    /**
     * 查询基础数据列表
     * 
     * @param chmParavalue 基础数据
     * @return 基础数据集合
     */
    public List<ChmParavalue> selectChmParavalueList(ChmParavalue chmParavalue);
    public List<ChmParavalue> selectChmParavalueListRy(String parentId);
    /**
     * 新增基础数据
     * 
     * @param chmParavalue 基础数据
     * @return 结果
     */
    public int insertChmParavalue(ChmParavalue chmParavalue);

    /**
     * 修改基础数据
     * 
     * @param chmParavalue 基础数据
     * @return 结果
     */
    public int updateChmParavalue(ChmParavalue chmParavalue);

    /**
     * 批量删除基础数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteChmParavalueByIds(String ids);

    /**
     * 删除基础数据信息
     * 
     * @param id 基础数据ID
     * @return 结果
     */
    public int deleteChmParavalueById(Long id);
}
