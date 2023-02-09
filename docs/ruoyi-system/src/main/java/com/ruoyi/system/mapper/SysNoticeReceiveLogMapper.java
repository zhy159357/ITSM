package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysNoticeReceiveLog;

import java.util.List;

public interface SysNoticeReceiveLogMapper {

    /**
     * 新增公告 公告接受日志
     *
     * @param sysNoticeReceiveLog 公告接受日志
     * @return 结果
     */
    public int insertNoticeReceiveLog(SysNoticeReceiveLog sysNoticeReceiveLog);

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
}
