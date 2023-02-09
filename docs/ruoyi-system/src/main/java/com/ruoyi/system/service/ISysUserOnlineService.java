package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysUserOnline;

import java.util.Date;
import java.util.List;

/**
 * 在线用户 服务层
 * 
 * @author ruoyi
 */
public interface ISysUserOnlineService
{
    /**
     * 通过会话序号查询信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineById(String sessionId);

    /**
     * 通过会话序号删除信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    public void deleteOnlineById(String sessionId);

    /**
     * 通过会话序号删除信息
     * 
     * @param sessions 会话ID集合
     * @return 在线用户信息
     */
    public void batchDeleteOnline(List<String> sessions);

    /**
     * 保存会话信息
     * 
     * @param online 会话信息
     */
    public void saveOnline(SysUserOnline online);

    /**
     * 查询会话集合
     * 
     * @param userOnline 分页参数
     * @return 会话集合
     */
    public List<SysUserOnline> selectUserOnlineList(SysUserOnline userOnline);

    /**
     * 强退用户
     * 
     * @param sessionId 会话ID
     */
    public void forceLogout(String sessionId);

    /**
     * 清理用户缓存
     * 
     * @param loginName 登录名称
     * @param sessionId 会话ID
     */
    public void removeUserCache(String loginName, String sessionId);

    /**
     * 查询会话集合
     * 
     * @param expiredDate 有效期
     * @return 会话集合
     */
    public List<SysUserOnline> selectOnlineByExpired(Date expiredDate);

    /**
     * 根据userId查询最近一条访问时间的在线用户
     * @param userId
     * @return
     */
    public SysUserOnline selectOnlineByUserId(String userId);

    /**
     * 根据userId查询当前工作状态
     * @param userId
     * @return
     */
    public String selectWorkStatusByUserId(String userId);

    /**
     * 查询去重登录名称
     *
     * @return 去重登录名称列表
     */
    List<String> distinctLoginName(SysUserOnline userOnline);

    /**
     * 根据登录名称查询最近一条访问时间的在线用户
     * @param loginName
     * @return
     */
    SysUserOnline selectOnlineByLoginName(String loginName);

    /**
     * 查询在线用户数量
     * @return 在线用户数量
     */
    int getOnLineCount();

}
