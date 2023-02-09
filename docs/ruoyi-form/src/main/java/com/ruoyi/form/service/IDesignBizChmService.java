package com.ruoyi.form.service;

import com.ruoyi.form.domain.DesignBizChm;
import java.util.List;

/**
 * 硬件保障Service接口
 * 
 * @author zx
 * @date 2022-11-26
 */
public interface IDesignBizChmService 
{
    /**
     * 查询硬件保障
     * 
     * @param id 硬件保障ID
     * @return 硬件保障
     */
    public DesignBizChm selectDesignBizChmById(Long id);

    /**
     * 查询硬件保障列表
     * 
     * @param designBizChm 硬件保障
     * @return 硬件保障集合
     */
    public List<DesignBizChm> selectDesignBizChmList(DesignBizChm designBizChm);

    /**
     * 新增硬件保障
     * 
     * @param designBizChm 硬件保障
     * @return 结果
     */
    public int insertDesignBizChm(DesignBizChm designBizChm);

    /**
     * 修改硬件保障
     * 
     * @param designBizChm 硬件保障
     * @return 结果
     */
    public int updateDesignBizChm(DesignBizChm designBizChm);

    /**
     * 批量删除硬件保障
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDesignBizChmByIds(String ids);

    /**
     * 删除硬件保障信息
     * 
     * @param id 硬件保障ID
     * @return 结果
     */
    public int deleteDesignBizChmById(Long id);
}
