package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.DesignBizChm;

import java.util.List;

public interface DesignBizChmMapper  extends BaseMapper<DesignBizChm> {
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
     * 删除硬件保障
     *
     * @param id 硬件保障ID
     * @return 结果
     */
    public int deleteDesignBizChmById(Long id);

    /**
     * 批量删除硬件保障
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDesignBizChmByIds(String[] ids);
}
