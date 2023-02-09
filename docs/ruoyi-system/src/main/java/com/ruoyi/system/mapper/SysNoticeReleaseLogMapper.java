package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysNoticeReleaseLog;

public interface SysNoticeReleaseLogMapper {

    /**
     *  公告发布日志 最大num
     *
     * @return 结果
     */
    public SysNoticeReleaseLog selectMaxNum();

    /**
     * 新增公告 公告发布日志
     *
     * @param sysNoticeReleaseLog 公告发布日志
     * @return 结果
     */
    public int insertNoticeReleaseLog(SysNoticeReleaseLog sysNoticeReleaseLog);


}
