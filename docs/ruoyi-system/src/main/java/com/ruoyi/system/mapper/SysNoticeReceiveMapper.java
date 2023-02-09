package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.SysNoticeReceive;

import java.util.List;

public interface SysNoticeReceiveMapper {

    /**
     * 查询接收公告信息
     *
     * @param amReceiveId 接收公告ID
     * @return 公告信息
     */
    public SysNoticeReceive selectNoticeReceiveById(String amReceiveId);

    /**
     * 查询接收公告列表
     *
     * @param sysNoticeReceive 公告信息
     * @return 公告集合
     */
    public List<SysNoticeReceive> selectNoticeReceiveList(SysNoticeReceive sysNoticeReceive);

    /**
     * 查询接收公告列表查询公告通知，监控公告通知接口
     *
     * @param sysNoticeReceive 公告信息
     * @return 公告集合
     */
    public List<SysNoticeReceive> selectNoticeReceives(SysNoticeReceive sysNoticeReceive);

    /**
     * 新增接收公告
     *
     * @param sysNoticeReceive 接受公告
     * @return 结果
     */
    public int insertNoticeReceive(SysNoticeReceive sysNoticeReceive);

    /**
     * 处理公告
     *
     * @param sysNoticeReceive 公告信息
     * @return 结果
     */
    public int updateNoticeReceive(SysNoticeReceive sysNoticeReceive);

    /**
     * 接收公告接收时间
     *
     * @param sysNoticeReceive 公告信息
     * @return 结果
     */
    public int updateNoticeReceiveReceiveTime(SysNoticeReceive sysNoticeReceive);

    /**
     * 短信通知接收人
     *
     * @param pid 人员id
     * @return 结果
     */
    public SysNoticeReceive smsNoticePersonById(String pid);
}
