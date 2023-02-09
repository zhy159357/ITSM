package com.ruoyi.form.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.form.mapper.ChmParavalueMapper;
import com.ruoyi.form.domain.ChmParavalue;
import com.ruoyi.form.service.IChmParavalueService;
import com.ruoyi.common.core.text.Convert;

/**
 * 基础数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-10-24
 */
@Service("IChmParavalueService")
public class ChmParavalueServiceImpl implements IChmParavalueService 
{
    @Autowired
    private ChmParavalueMapper chmParavalueMapper;

    /**
     * 查询基础数据
     * 
     * @param id 基础数据ID
     * @return 基础数据
     */
    @Override
    public ChmParavalue selectChmParavalueById(Long id)
    {
        return chmParavalueMapper.selectChmParavalueById(id);
    }
    /**
     * 查询基础数据
     *
     * @param name 基础数据名称
     * @return 基础数据
     */
    public ChmParavalue selectChmParavalueByName(String name){
        return chmParavalueMapper.selectChmParavalueByName(name);
    }
    /**
     * 查询基础数据列表
     * 
     * @param chmParavalue 基础数据
     * @return 基础数据
     */
    @Override
    public List<ChmParavalue> selectChmParavalueList(ChmParavalue chmParavalue)
    {
        return chmParavalueMapper.selectChmParavalueList(chmParavalue);
    }
    public List<ChmParavalue> selectChmParavalueListRy(String parentId)
    {
        ChmParavalue chmParavalue=new ChmParavalue();
        List<ChmParavalue> list=new ArrayList<>();
        if(StringUtils.isEmpty(parentId)){
            return list;
        }
        chmParavalue.setParentId(Long.valueOf(parentId));
        list= chmParavalueMapper.selectChmParavalueList(chmParavalue);
        return list;
    }
    /**
     * 新增基础数据
     * 
     * @param chmParavalue 基础数据
     * @return 结果
     */
    @Override
    public int insertChmParavalue(ChmParavalue chmParavalue)
    {
        return chmParavalueMapper.insertChmParavalue(chmParavalue);
    }

    /**
     * 修改基础数据
     * 
     * @param chmParavalue 基础数据
     * @return 结果
     */
    @Override
    public int updateChmParavalue(ChmParavalue chmParavalue)
    {
        return chmParavalueMapper.updateChmParavalue(chmParavalue);
    }

    /**
     * 删除基础数据对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteChmParavalueByIds(String ids)
    {
        return chmParavalueMapper.deleteChmParavalueByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除基础数据信息
     * 
     * @param id 基础数据ID
     * @return 结果
     */
    @Override
    public int deleteChmParavalueById(Long id)
    {
        return chmParavalueMapper.deleteChmParavalueById(id);
    }
}
