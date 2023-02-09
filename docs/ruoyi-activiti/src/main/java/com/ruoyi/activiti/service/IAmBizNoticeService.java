package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.AmBizNotice;
import java.util.List;

/**
 * 【公告通知】Service接口
 *
 * @author ruoyi
 * @date 2021-10-19
 */
public interface IAmBizNoticeService
{
    /**
     * 查询【公告通知】
     *
     * @param amBizId 【公告通知】ID
     * @return 【公告通知】
     */
    public AmBizNotice selectAmBizNoticeById(String amBizId);

    /**
     * 查询我的【公告通知】列表
     *
     * @param amBizNotice 【公告通知】
     * @return 【公告通知】集合
     */
    public List<AmBizNotice> selectAmBizNoticeList(AmBizNotice amBizNotice);

    /**
     * 查询待审核公告通知
     *
     * @param amBizNotice 【公告通知】
     * @return 【公告通知】集合
     */
    public List<AmBizNotice> selectCheckNoticeList(AmBizNotice amBizNotice);

    /**
     * 查询所有公告通知
     *
     * @param amBizNotice 【公告通知】
     * @return 【公告通知】集合
     */
    public List<AmBizNotice> selectAllNoticeList(AmBizNotice amBizNotice);

    /**
     * 新增【公告通知】
     *
     * @param amBizNotice 【公告通知】
     * @return 结果
     */
    public int insertAmBizNotice(AmBizNotice amBizNotice);

    /**
     * 修改【公告通知】
     *
     * @param amBizNotice 【公告通知】
     * @return 结果
     */
    public int updateAmBizNotice(AmBizNotice amBizNotice);

    /**
     * 批量删除【公告通知】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAmBizNoticeByIds(String ids);

    /**
     * 删除【公告通知】信息
     *
     * @param amBizId 【公告通知】ID
     * @return 结果
     */
    public int deleteAmBizNoticeById(String amBizId);

    /**
     * 查询我的【公告通知】列表  移动运维
     *
     * @param userId 登录人ID
     * @param amTitle 公告标题模糊查询
     * @return 【公告通知】集合
     */
    public List<AmBizNotice> getNoticeListForApp(String userId , String amTitle);
}
