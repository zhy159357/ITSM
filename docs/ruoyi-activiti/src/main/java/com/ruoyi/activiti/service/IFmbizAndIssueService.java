package com.ruoyi.activiti.service;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmbizAndIssue;

import java.util.List;

/**
 * 事件单关联问题单关联关系Service接口
 *
 * @author ruoyi
 * @date 2021-10-15
 */
public interface IFmbizAndIssueService {
    /**
     * 查询事件单关联问题单关联关系
     *
     * @param id 事件单关联问题单关联关系ID
     * @return 事件单关联问题单关联关系
     */
    public FmbizAndIssue selectFmbizAndIssueById(String id);

    /**
     * 查询事件单关联问题单关联关系列表
     *
     * @param fmbizAndIssue 事件单关联问题单关联关系
     * @return 事件单关联问题单关联关系集合
     */
    public List<FmbizAndIssue> selectFmbizAndIssueList(FmbizAndIssue fmbizAndIssue);

    /**
     * 新增事件单关联问题单关联关系
     *
     * @param fmbizAndIssue 事件单关联问题单关联关系
     * @return 结果
     */
    public int insertFmbizAndIssue(FmbizAndIssue fmbizAndIssue);

    /**
     * 修改事件单关联问题单关联关系
     *
     * @param fmbizAndIssue 事件单关联问题单关联关系
     * @return 结果
     */
    public int updateFmbizAndIssue(FmbizAndIssue fmbizAndIssue);

    /**
     * 批量删除事件单关联问题单关联关系
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmbizAndIssueByIds(String ids);

    /**
     * 删除事件单关联问题单关联关系信息
     *
     * @param id 事件单关联问题单关联关系ID
     * @return 结果
     */
    public int deleteFmbizAndIssueById(String id);

    /**
     * 事件单处理提交时调用
     *
     * @param fmBiz
     * @return
     */
    public void saveFmbizAndIssue(FmBiz fmBiz);

    /**
     * 事件单关联问题单时调用
     */
    public AjaxResult relationIssue(FmBiz fmBiz);

    /**
     * 根据事件单ID删除对应的绑定记录
     *
     * @param fmId
     * @return
     */
    public void deleteFmbizAndIssueByFmId(String fmId);

    /**
     * 根据传入时间查询满足范围的未关联事件单
     */
    public List<FmbizAndIssue> getFmBizAndIssueByDay(int day);

    /**
     * 一线关联问题单
     *
     * @param fmBiz
     */
    public void relationIssueOne(FmBiz fmBiz);

    public List<FmbizAndIssue> getRelationByDay(String day);

    /**
     * 通过问题单ID来查询相关时间内的事件单
     *
     * @param fmbizAndIssue
     * @return 结果
     */
    public List<FmbizAndIssue> selectFmbizIdList(FmbizAndIssue fmbizAndIssue);

    /**
     * 修改关联问题单
     * @param fmBiz
     * @return
     */
    public AjaxResult editRelationIssue(FmBiz fmBiz);
}
