package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.mapper.SysUserOnlineMapper;
import com.ruoyi.system.service.ISysUserOnlineService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * 在线用户 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService
{
    @Autowired
    private SysUserOnlineMapper userOnlineDao;
    
    @Autowired
    private CacheManager cacheManager;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 通过会话序号查询信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineById(String sessionId)
    {
        Map<String,String> map=new HashMap<>();
        map.put("sessionId",sessionId);
        map.put("dbType",dbType);
        if("mysql".equals(dbType)){
            return userOnlineDao.selectOnlineByIdMysql(sessionId);
        }else{
            return userOnlineDao.selectOnlineById(sessionId);
        }

    }

    /**
     * 通过会话序号删除信息
     * 
     * @param sessionId 会话ID
     * @return 在线用户信息
     */
    @Override
    public void deleteOnlineById(String sessionId)
    {
        SysUserOnline userOnline = selectOnlineById(sessionId);
        if (StringUtils.isNotNull(userOnline))
        {
            userOnlineDao.deleteOnlineById(sessionId);
        }
    }

    /**
     * 通过会话序号删除信息
     * 
     * @param sessions 会话ID集合
     * @return 在线用户信息
     */
    @Override
    public void batchDeleteOnline(List<String> sessions)
    {
        for (String sessionId : sessions)
        {
            SysUserOnline userOnline = selectOnlineById(sessionId);
            if (StringUtils.isNotNull(userOnline))
            {
                userOnlineDao.deleteOnlineById(sessionId);
            }
        }
    }

    /**
     * 保存会话信息
     * 
     * @param online 会话信息
     */
    @Override
    public void saveOnline(SysUserOnline online)
    {
        SysUserOnline userOnline = selectOnlineById(online.getSessionId());
        // 会话sessionId在数据库不存在才保存，存在更新最后访问时间
        if(userOnline == null){
            if(StringUtils.isNotEmpty(online.getLoginName())){
                userOnlineDao.saveOnline(online);
            }
        } else {
            userOnlineDao.updateOnlineLastAccessTime(online);
        }
    }

    /**
     * 查询会话集合
     * 
     * @param userOnline 在线用户
     */
    @Override
    public List<SysUserOnline> selectUserOnlineList(SysUserOnline userOnline)
    {
        if("mysql".equals(dbType)){
            return userOnlineDao.selectUserOnlineListMysql(userOnline);

        }else{
            return userOnlineDao.selectUserOnlineList(userOnline);

        }
    }

    /**
     * 强退用户
     * 
     * @param sessionId 会话ID
     */
    @Override
    public void forceLogout(String sessionId)
    {
        userOnlineDao.deleteOnlineById(sessionId);
    }

    /**
     * 清理用户缓存
     * 
     * @param loginName 登录名称
     * @param sessionId 会话ID
     */
    @Override
    public void removeUserCache(String loginName, String sessionId)
    {
        Cache<String, Deque<Serializable>> cache = cacheManager.getCache(ShiroConstants.SYS_USERCACHE);
        Deque<Serializable> deque = cache.get(loginName);
        if (StringUtils.isEmpty(deque) || deque.size() == 0)
        {
            return;
        }
        deque.remove(sessionId);
    }

    /**
     * 查询会话集合
     * 
     * @param expiredDate 失效日期
     */
    @Override
    public List<SysUserOnline> selectOnlineByExpired(Date expiredDate)
    {
        String lastAccessTimeStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, expiredDate);
        Date lastAccessTime = DateUtils.parseDate(lastAccessTimeStr);

        return userOnlineDao.selectOnlineByExpired(lastAccessTime);
    }

    @Override
    public SysUserOnline selectOnlineByUserId(String userId) {
        if("mysql".equals(dbType)){
            return userOnlineDao.selectOnlineByUserIdMysql(userId);

        }else{
            return userOnlineDao.selectOnlineByUserId(userId);
        }
    }

    @Override
    public String selectWorkStatusByUserId(String userId) {
        return userOnlineDao.selectWorkStatusByUserId(userId);
    }

    /**
     * 查询去重登录名称
     *
     * @return 去重登录名称列表
     */
    @Override
    public List<String> distinctLoginName(SysUserOnline userOnline){
        return userOnlineDao.distinctLoginName(userOnline);
    }

    /**
     * 根据登录名称查询最近一条访问时间的在线用户
     * @param loginName
     * @return
     */
    @Override
    public SysUserOnline selectOnlineByLoginName(String loginName){
        if("mysql".equals(dbType)){
            return userOnlineDao.selectOnlineByLoginNameMysql(loginName);

        }else{
            return userOnlineDao.selectOnlineByLoginName(loginName);

        }
    }

    /**
     * 查询在线用户数量
     * @return 在线用户数量
     */
    @Override
    public int getOnLineCount(){
        return userOnlineDao.getOnLineCount();
    }

}
