package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.AmBizNotice;
import java.util.List;

/**
 * 【公告通知 数据中心】Mapper接口
 *
 * @author ruoyi
 * @date 2021-10-19
 */
public interface AmBizNoticeMapper
{
    /**
     * 查询【公告通知】
     *
     * @param amBizId 【公告通知】ID
     * @return 【公告通知vo】
     */
    public AmBizNotice selectAmBizNoticeById(String amBizId);

    /**
     * 查询【公告通知】列表
     *
     * @param amBizNotice 【公告通知】
     * @return 【公告通知】集合
     */
    public List<AmBizNotice> selectAmBizNoticeList(AmBizNotice amBizNotice);

    public List<AmBizNotice> selectAmBizNoticeListMysql(AmBizNotice amBizNotice);

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
     * 删除【公告通知】
     *
     * @param amBizId 【公告通知】ID
     * @return 结果
     */
    public int deleteAmBizNoticeById(String amBizId);

    /**
     * 批量删除【公告通知】
     *
     * @param amBizIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteAmBizNoticeByIds(String[] amBizIds);
}
