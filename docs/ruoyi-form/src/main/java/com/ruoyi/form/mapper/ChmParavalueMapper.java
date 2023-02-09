package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.ChmParavalue;
import java.util.List;

/**
 * 基础数据Mapper接口
 * 
 * @author ruoyi
 * @date 2022-10-24
 */
public interface ChmParavalueMapper 
{
    /**
     * 查询基础数据
     * 
     * @param id 基础数据ID
     * @return 基础数据
     */
    public ChmParavalue selectChmParavalueById(Long id);

    /**
     * 查询基础数据列表
     * 
     * @param chmParavalue 基础数据
     * @return 基础数据集合
     */
    public List<ChmParavalue> selectChmParavalueList(ChmParavalue chmParavalue);

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
     * 删除基础数据
     * 
     * @param id 基础数据ID
     * @return 结果
     */
    public int deleteChmParavalueById(Long id);

    /**
     * 批量删除基础数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteChmParavalueByIds(String[] ids);

    public ChmParavalue selectChmParavalueByName(String name);
}
