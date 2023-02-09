package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysNoticeReceive;
import com.ruoyi.system.domain.SysNoticeReceiveLog;

import java.util.List;

public interface ISysNoticeReceiveService {

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
     * 处理公告
     *
     * @param sysNoticeReceive 公告信息
     * @return 结果
     */
    public int updateNoticeReceive(SysNoticeReceive sysNoticeReceive);

    /**
     * 查询接收公告日志List
     * @param sysNoticeReceiveLog 公告接受日志
     * @return 结果
     */
    public List<SysNoticeReceiveLog> selectNoticeReceiveLogList(SysNoticeReceiveLog sysNoticeReceiveLog);

    /**
     * 接收详情
     * @param sysNoticeReceiveLog 公告接受日志
     * @return 结果
     */
    public List<SysNoticeReceiveLog> receiveLogList(SysNoticeReceiveLog sysNoticeReceiveLog);

    /**
     * 短信通知接收人
     *
     * @param pid 人员id
     * @return 结果
     */
    public SysNoticeReceive smsNoticePersonById(String pid);
}
