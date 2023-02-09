package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysUserOnline;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 在线用户 数据层
 * 
 * @author ruoyi
 */
public interface SysUserOnlineMapper
{
    /**
     * 通过会话序号查询信息
     * @param map
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineById(String sessionId);

    public SysUserOnline selectOnlineByIdMysql(String sessionId);
    /**
     * 通过会话序号删除信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    public int deleteOnlineById(String sessionId);

    /**
     * 保存会话信息
     * 
     * @param online 会话信息
     * @return 结果
     */
    public int saveOnline(SysUserOnline online);

    /**
     * 更新会话消息最后访问时间
     * @param online
     * @return
     */
    public int updateOnlineLastAccessTime(SysUserOnline online);

    /**
     * 查询会话集合
     * 
     * @param userOnline 会话参数
     * @return 会话集合
     */
    public List<SysUserOnline> selectUserOnlineList(SysUserOnline userOnline);

    public List<SysUserOnline> selectUserOnlineListMysql(SysUserOnline userOnline);

    /**
     * 查询过期会话集合
     * 
     * @param lastAccessTime 过期时间
     * @return 会话集合
     */
    public List<SysUserOnline> selectOnlineByExpired(Date lastAccessTime);

    /**
     * 根据userId查询最近一条访问时间的在线用户
     * @param userId
     * @return
     */
    public SysUserOnline selectOnlineByUserId(String userId);

    public SysUserOnline selectOnlineByUserIdMysql(String userId);


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

    SysUserOnline selectOnlineByLoginNameMysql(String loginName);

    /**
     * 查询在线用户数量
     * @return 在线用户数量
     */
    int getOnLineCount();

}
