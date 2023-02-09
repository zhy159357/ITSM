package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.OgPersonDuty;

import java.util.List;
import java.util.Map;

/**
 * 监控值班Service接口
 * 
 * @author ruoyi
 * @date 2021-06-25
 */
public interface IOgPersonDutyService 
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
     * 批量删除监控值班
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public Map deleteOgPersonDutyByIds(String ids, String userId);

    /**
     * 删除监控值班信息
     * 
     * @param dutyId 监控值班ID
     * @return 结果
     */
    public int deleteOgPersonDutyById(String dutyId);
    /**
     * 批量删除监控值班
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgPersonDutyByIds(String ids);
    /**
     * 判断当前值班组是否只剩下一个人
     * @param duty
     * @return
     */
    public boolean selectDutyCount(OgPersonDuty duty);

    /**
     * 判断值班组数据是否重复
     * @param ogPersonDuty
     * @return
     */
    boolean judgeDutyRepeat(OgPersonDuty ogPersonDuty);

    /**
     * 首页工作状态签退时增加校验值班组人数
     * @param userId
     * @return
     */
    public Map checkDutyPerson(String userId);

    /**
     * 条件查询所有人员和工作组
     * @param ogPersonDuty
     * @return
     */
    public List<OgPersonDuty> selectOgPersonDutyGroupList(OgPersonDuty ogPersonDuty);
}
