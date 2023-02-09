package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.ChmBasedata;
import java.util.List;

/**
 * chmDataMapper接口
 * 
 * @author zhangxu
 * @date 2022-11-19
 */
public interface ChmBasedataMapper 
{
    /**
     * 查询chmData
     * 
     * @param id chmDataID
     * @return chmData
     */
    public ChmBasedata selectChmBasedataById(Long id);
    /**
     * 查询chmData
     *
     * @param orgName chmDataID
     * @return chmData
     */
    public ChmBasedata selectChmBasedataByName(String orgName);
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
     * 删除chmData
     * 
     * @param id chmDataID
     * @return 结果
     */
    public int deleteChmBasedataById(Long id);

    /**
     * 批量删除chmData
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteChmBasedataByIds(String[] ids);
}
