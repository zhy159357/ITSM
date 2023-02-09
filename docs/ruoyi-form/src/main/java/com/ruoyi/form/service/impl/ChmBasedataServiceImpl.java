package com.ruoyi.form.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysDeptVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.form.mapper.ChmBasedataMapper;
import com.ruoyi.form.domain.ChmBasedata;
import com.ruoyi.form.service.IChmBasedataService;
import com.ruoyi.common.core.text.Convert;

/**
 * chmDataService业务层处理
 * 
 * @author zhangxu
 * @date 2022-11-19
 */
@Service("IChmBasedataService")
public class ChmBasedataServiceImpl implements IChmBasedataService 
{
    @Autowired
    private ChmBasedataMapper chmBasedataMapper;

    /**
     * 查询chmData
     * 
     * @param id chmDataID
     * @return chmData
     */
    @Override
    public ChmBasedata selectChmBasedataById(Long id)
    {
        return chmBasedataMapper.selectChmBasedataById(id);
    }
    @Override
    public ChmBasedata selectChmBasedataByName(String orgName)
    {
        return chmBasedataMapper.selectChmBasedataByName(orgName);
    }


    /**
     * 查询chmData列表下拉框列表
     * 
     * @param chmBasedata chmData
     * @return chmData
     */
    @Override
    public List<SysDeptVo> selectChmBasedataListSelect()
    {
        ChmBasedata chmBasedata=new ChmBasedata();
       List<ChmBasedata> list=chmBasedataMapper.selectChmBasedataList(chmBasedata);
       List<SysDeptVo> reLits=new ArrayList<>();
       list.forEach(ch->{
           SysDeptVo sysDeptVo=new SysDeptVo();
           sysDeptVo.setValue(String.valueOf(ch.getId()));
           sysDeptVo.setLabel(ch.getOrgName());
           reLits.add(sysDeptVo);
       });
        return reLits;
    }
    /**
     * 查询chmData列表
     *
     * @param chmBasedata chmData
     * @return chmData
     */
    @Override
    public List<ChmBasedata> selectChmBasedataList(ChmBasedata chmBasedata) {
        return chmBasedataMapper.selectChmBasedataList(chmBasedata);
    }

        /**
         * 新增chmData
         *
         * @param chmBasedata chmData
         * @return 结果
         */
    @Override
    public int insertChmBasedata(ChmBasedata chmBasedata)
    {
        return chmBasedataMapper.insertChmBasedata(chmBasedata);
    }

    /**
     * 修改chmData
     * 
     * @param chmBasedata chmData
     * @return 结果
     */
    @Override
    public int updateChmBasedata(ChmBasedata chmBasedata)
    {
        return chmBasedataMapper.updateChmBasedata(chmBasedata);
    }

    /**
     * 删除chmData对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteChmBasedataByIds(String ids)
    {
        return chmBasedataMapper.deleteChmBasedataByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除chmData信息
     * 
     * @param id chmDataID
     * @return 结果
     */
    @Override
    public int deleteChmBasedataById(Long id)
    {
        return chmBasedataMapper.deleteChmBasedataById(id);
    }
}
