package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.FmbizAndIssue;

import java.util.List;

/**
 * 事件单关联问题单关联关系Mapper接口
 *
 * @author liul
 * @date 2021-10-15
 */
public interface FmbizAndIssueMapper {
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
     * 删除事件单关联问题单关联关系
     *
     * @param id 事件单关联问题单关联关系ID
     * @return 结果
     */
    public int deleteFmbizAndIssueById(String id);

    /**
     * 批量删除事件单关联问题单关联关系
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmbizAndIssueByIds(String[] ids);

    public List<FmbizAndIssue> getFmBizAndIssueByDay(String day);

    public List<FmbizAndIssue> getRelationByDay(String day);

    /**
     * 通过问题单ID来查询相关时间内的事件单
     *
     * @param fmbizAndIssue
     * @return 结果
     */
    public List<FmbizAndIssue> selectFmbizIdList(FmbizAndIssue fmbizAndIssue);
}
