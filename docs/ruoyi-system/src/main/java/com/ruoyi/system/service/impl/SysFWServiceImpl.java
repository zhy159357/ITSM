package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysFWlist;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.system.mapper.SysFWMapper;
import com.ruoyi.system.service.ISysFWService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ruoyi.common.utils.DictUtils.getCacheName;

/**
 * 话机绑定业务层处理
 */
@Service
public class SysFWServiceImpl implements ISysFWService
{
    @Autowired
    private SysFWMapper fwMapper;
    @Value("${pagehelper.helperDialect}")
    private String dbType;
    /**
     * 根据条件分页查询角色数据
     * @return 角色数据集合信息
     */
    @Override
    public List<SysFWlist> selectFWList(SysFWlist fw)
    {
        if("mysql".equals(dbType)){
            return fwMapper.selectFWMysqlList(fw);
        }else{
            return fwMapper.selectFWList(fw);
        }

    }
    /**
     * 新增保存信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertFW(SysFWlist fw)
    {
        // 新增角色信息
        return fwMapper.insertFW(fw);
    }
    /**
     * 通过服务ID查询信息
     * @param address_list_id 话机绑定ID
     * @return 话机绑定对象信息
     */
    @Override
    public SysFWlist selectFWById(String address_list_id)
    {
        return fwMapper.selectFWById(address_list_id);
    }
    @Override
    @Transactional
    public int updateFW(SysFWlist fw)
    {
        // 修改角色信息
        return fwMapper.updateFW(fw);
    }

    @Override
    public int deleteFWByIds(String ids) {
        /*int count = fwMapper.deleteFWByIds(Convert.toStrArray(ids));
        if (count > 0) {
            CacheUtils.removeAll(getCacheName());
        }
        return count;*/
        String[] list= Convert.toStrArray(",",ids);
        return fwMapper.deleteFWByIds(list);
    }
}