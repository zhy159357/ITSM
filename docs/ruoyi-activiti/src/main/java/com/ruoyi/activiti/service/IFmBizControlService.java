package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmBizControl;

import java.util.List;

/**
 * 业务事件单监控Service接口
 *
 * @author ruoyi
 * @date 2021-01-11
 */
public interface IFmBizControlService {
    /**
     * 查询业务事件单监控
     *
     * @param controlId 业务事件单监控ID
     * @return 业务事件单监控
     */
    public FmBizControl selectFmBizControlById(String controlId);

    /**
     * 查询业务事件单监控列表
     *
     * @param fmBizControl 业务事件单监控
     * @return 业务事件单监控集合
     */
    public List<FmBizControl> selectFmBizControlList(FmBizControl fmBizControl);

    /**
     * 新增业务事件单监控
     *
     * @param fmBizControl 业务事件单监控
     * @return 结果
     */
    public int insertFmBizControl(FmBizControl fmBizControl);

    /**
     * 修改业务事件单监控
     *
     * @param fmBizControl 业务事件单监控
     * @return 结果
     */
    public int updateFmBizControl(FmBizControl fmBizControl);

    /**
     * 批量删除业务事件单监控
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizControlByIds(String ids);

    /**
     * 删除业务事件单监控信息
     *
     * @param controlId 业务事件单监控ID
     * @return 结果
     */
    public int deleteFmBizControlById(String controlId);

    /**
     * 删除监控事件单表全部数据
     */
    public void deleteAll();

    /**
     * 查询不高效事件单
     */
    public List<FmBiz> getNoEfficientFms(int count);

    /**
     * 查询未处理完的事件单
     */
    public List<FmBiz> getUntimelyResolution();

    /**
     * 查看紧急/高级事件单统计
     */
    public List<FmBiz> getJjDealFmBizs();

    /**
     * 查看超过72小时事件单
     */
    public List<FmBiz> getTimeResolution(int day);
}
