package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.OgPersonDuty;

import java.util.List;

/**
 * 监控值班Mapper接口
 * 
 * @author ruoyi
 * @date 2021-06-25
 */
public interface OgPersonDutyMapper 
{
    /**
     * 查询监控值班
     * 
     * @param dutyId 监控值班ID
     * @return 监控值班
     */
    public OgPersonDuty selectOgPersonDutyById(String dutyId);

    /**
     * 查询监控值班列表
     * 
     * @param ogPersonDuty 监控值班
     * @return 监控值班集合
     */
    public List<OgPersonDuty> selectOgPersonDutyList(OgPersonDuty ogPersonDuty);

    /**
     * 新增监控值班
     * 
     * @param ogPersonDuty 监控值班
     * @return 结果
     */
    public int insertOgPersonDuty(OgPersonDuty ogPersonDuty);

    /**
     * 修改监控值班
     * 
     * @param ogPersonDuty 监控值班
     * @return 结果
     */
    public int updateOgPersonDuty(OgPersonDuty ogPersonDuty);

    /**
     * 删除监控值班
     * 
     * @param dutyId 监控值班ID
     * @return 结果
     */
    public int deleteOgPersonDutyById(String dutyId);

    /**
     * 批量删除监控值班
     * 
     * @param dutyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgPersonDutyByIds(String[] dutyIds);

    /**
     * 查询数量
     * @return
     */
    public int selectDutyCountByGroupOrMonitor(OgPersonDuty ogPersonDuty);

    public List<OgPersonDuty> selectOgPersonDutyGroupList(OgPersonDuty ogPersonDuty);
}
