package com.ruoyi.form.service.impl;

import java.util.List;

import com.ruoyi.form.enums.ChmStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.form.mapper.DesignBizChmMapper;
import com.ruoyi.form.domain.DesignBizChm;
import com.ruoyi.form.service.IDesignBizChmService;
import com.ruoyi.common.core.text.Convert;

/**
 * 硬件保障Service业务层处理
 * 
 * @author zx
 * @date 2022-11-26
 */
@Service
public class DesignBizChmServiceImpl implements IDesignBizChmService 
{
    @Autowired
    private DesignBizChmMapper designBizChmMapper;

    /**
     * 查询硬件保障
     * 
     * @param id 硬件保障ID
     * @return 硬件保障
     */
    @Override
    public DesignBizChm selectDesignBizChmById(Long id)
    {
        return designBizChmMapper.selectDesignBizChmById(id);
    }

    /**
     * 查询硬件保障列表
     * 
     * @param designBizChm 硬件保障
     * @return 硬件保障
     */
    @Override
    public List<DesignBizChm> selectDesignBizChmList(DesignBizChm designBizChm)
    {
        String status=designBizChm.getStatus();
        designBizChm.setStatus(ChmStatusEnum.getInfo(status));
        return designBizChmMapper.selectDesignBizChmList(designBizChm);
    }

    /**
     * 新增硬件保障
     * 
     * @param designBizChm 硬件保障
     * @return 结果
     */
    @Override
    public int insertDesignBizChm(DesignBizChm designBizChm)
    {
        return designBizChmMapper.insertDesignBizChm(designBizChm);
    }

    /**
     * 修改硬件保障
     * 
     * @param designBizChm 硬件保障
     * @return 结果
     */
    @Override
    public int updateDesignBizChm(DesignBizChm designBizChm)
    {
        return designBizChmMapper.updateDesignBizChm(designBizChm);
    }

    /**
     * 删除硬件保障对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDesignBizChmByIds(String ids)
    {
        return designBizChmMapper.deleteDesignBizChmByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除硬件保障信息
     * 
     * @param id 硬件保障ID
     * @return 结果
     */
    @Override
    public int deleteDesignBizChmById(Long id)
    {
        return designBizChmMapper.deleteDesignBizChmById(id);
    }
}
