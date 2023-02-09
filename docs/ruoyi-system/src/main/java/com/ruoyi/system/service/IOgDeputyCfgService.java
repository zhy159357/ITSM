package com.ruoyi.system.service;

import com.ruoyi.system.domain.OgDeputyCfg;
import java.util.List;

/**
 * 代理人配置Service接口
 * 
 * @author admin
 * @date 2022-12-12
 */
public interface IOgDeputyCfgService 
{
    /**
     * 查询代理人配置
     * 
     * @param id 代理人配置ID
     * @return 代理人配置
     */
    public OgDeputyCfg selectOgDeputyCfgById(Long id);

    /**
     * 查询代理人配置列表
     * 
     * @param ogDeputyCfg 代理人配置
     * @return 代理人配置集合
     */
    public List<OgDeputyCfg> selectOgDeputyCfgList(OgDeputyCfg ogDeputyCfg);
    
    public List<OgDeputyCfg> isOgDeputyCfgList(OgDeputyCfg ogDeputyCfg);

    /**
     * 新增代理人配置
     * 
     * @param ogDeputyCfg 代理人配置
     * @return 结果
     */
    public int insertOgDeputyCfg(OgDeputyCfg ogDeputyCfg);

    /**
     * 修改代理人配置
     * 
     * @param ogDeputyCfg 代理人配置
     * @return 结果
     */
    public int updateOgDeputyCfg(OgDeputyCfg ogDeputyCfg);

    /**
     * 批量删除代理人配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgDeputyCfgByIds(String ids);

    /**
     * 删除代理人配置信息
     * 
     * @param id 代理人配置ID
     * @return 结果
     */
    public int deleteOgDeputyCfgById(Long id);
}
