package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OgDeputyCfgMapper;
import com.ruoyi.system.domain.OgDeputyCfg;
import com.ruoyi.system.service.IOgDeputyCfgService;
import com.ruoyi.common.core.text.Convert;

/**
 * 代理人配置Service业务层处理
 * 
 * @author admin
 * @date 2022-12-12
 */
@Service
public class OgDeputyCfgServiceImpl implements IOgDeputyCfgService 
{
    @Autowired
    private OgDeputyCfgMapper ogDeputyCfgMapper;

    /**
     * 查询代理人配置
     * 
     * @param id 代理人配置ID
     * @return 代理人配置
     */
    @Override
    public OgDeputyCfg selectOgDeputyCfgById(Long id)
    {
        return ogDeputyCfgMapper.selectOgDeputyCfgById(id);
    }

    /**
     * 查询代理人配置列表
     * 
     * @param ogDeputyCfg 代理人配置
     * @return 代理人配置
     */
    @Override
    public List<OgDeputyCfg> selectOgDeputyCfgList(OgDeputyCfg ogDeputyCfg)
    {
        return ogDeputyCfgMapper.selectOgDeputyCfgList(ogDeputyCfg);
    }
    @Override
    public List<OgDeputyCfg> isOgDeputyCfgList(OgDeputyCfg ogDeputyCfg)
    {
    	return ogDeputyCfgMapper.isOgDeputyCfgList(ogDeputyCfg);
    }

    /**
     * 新增代理人配置
     * 
     * @param ogDeputyCfg 代理人配置
     * @return 结果
     */
    @Override
    public int insertOgDeputyCfg(OgDeputyCfg ogDeputyCfg)
    {
        return ogDeputyCfgMapper.insertOgDeputyCfg(ogDeputyCfg);
    }

    /**
     * 修改代理人配置
     * 
     * @param ogDeputyCfg 代理人配置
     * @return 结果
     */
    @Override
    public int updateOgDeputyCfg(OgDeputyCfg ogDeputyCfg)
    {
        return ogDeputyCfgMapper.updateOgDeputyCfg(ogDeputyCfg);
    }

    /**
     * 删除代理人配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOgDeputyCfgByIds(String ids)
    {
        return ogDeputyCfgMapper.deleteOgDeputyCfgByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除代理人配置信息
     * 
     * @param id 代理人配置ID
     * @return 结果
     */
    @Override
    public int deleteOgDeputyCfgById(Long id)
    {
        return ogDeputyCfgMapper.deleteOgDeputyCfgById(id);
    }
}
