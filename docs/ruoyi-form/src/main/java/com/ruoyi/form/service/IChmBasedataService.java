package com.ruoyi.form.service;

import com.ruoyi.common.core.domain.entity.SysDeptVo;
import com.ruoyi.form.domain.ChmBasedata;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * chmDataService接口
 * 
 * @author zhangxu
 * @date 2022-11-19
 */
public interface IChmBasedataService
{
    /**
     * 查询chmData
     * 
     * @param id chmDataID
     * @return chmData
     */
    public ChmBasedata selectChmBasedataById(Long id);
    public ChmBasedata selectChmBasedataByName(String orgName);
    /**
     * 查询chmData列表
     * 
     * @param chmBasedata chmData
     * @return chmData集合
     */
    public List<SysDeptVo> selectChmBasedataListSelect();

    /**
     * 查询chmData列表
     *
     * @param chmBasedata chmData
     * @return chmData集合
     */
    public List<ChmBasedata> selectChmBasedataList(ChmBasedata chmBasedata);
    /**
     * 新增chmData
     * 
     * @param chmBasedata chmData
     * @return 结果
     */
    public int insertChmBasedata(ChmBasedata chmBasedata);

    /**
     * 修改chmData
     * 
     * @param chmBasedata chmData
     * @return 结果
     */
    public int updateChmBasedata(ChmBasedata chmBasedata);

    /**
     * 批量删除chmData
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteChmBasedataByIds(String ids);

    /**
     * 删除chmData信息
     * 
     * @param id chmDataID
     * @return 结果
     */
    public int deleteChmBasedataById(Long id);
}
