package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysAddlist;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.system.mapper.SysYWMapper;
import com.ruoyi.system.service.ISysYWService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ruoyi.common.utils.DictUtils.getCacheName;

/**
 * 话机绑定业务层处理
 */
@Service
public class SysYWServiceImpl implements ISysYWService
{
    @Autowired
    private SysYWMapper ywMapper;

    /**
     * 根据条件分页查询角色数据
     * @return 角色数据集合信息
     */
    @Override
    public List<SysAddlist> selectYWList(SysAddlist yw)
    {
        return ywMapper.selectYWList(yw);
    }
    /**
     * 新增保存信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertYW(SysAddlist yw)
    {
        // 新增角色信息
        return ywMapper.insertYW(yw);
    }
    /**
     * 通过服务ID查询信息
     * @param address_list_id 话机绑定ID
     * @return 话机绑定对象信息
     */
    @Override
    public SysAddlist selectYWById(String address_list_id)
    {
        return ywMapper.selectYWById(address_list_id);
    }
    @Override
    @Transactional
    public int updateYW(SysAddlist yw)
    {
        // 修改角色信息
        return ywMapper.updateYW(yw);
    }

    @Override
    public int deleteYWByIds(String ids) {
        /*int count = ywMapper.deleteYWByIds(Convert.toStrArray(ids));
        if (count > 0) {
            CacheUtils.removeAll(getCacheName());
        }
        return count;*/
        String[] list= Convert.toStrArray(",",ids);
        return ywMapper.deleteYWByIds(list);
    }
}