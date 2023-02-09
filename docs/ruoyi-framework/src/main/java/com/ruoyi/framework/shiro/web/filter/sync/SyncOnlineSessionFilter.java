package com.ruoyi.framework.shiro.web.filter.sync;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.session.SessionDao;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.common.constant.ShiroConstants;
import com.ruoyi.framework.shiro.session.OnlineSession;

/**
 * 同步Session数据到Db
 *
 * @author ruoyi
 */
public class SyncOnlineSessionFilter extends PathMatchingFilter
{
    @Autowired
    // private OnlineSessionDAO onlineSessionDAO;
    private SessionDao sessionDao;
    /**
     * 同步会话数据到DB 一次请求最多同步一次 防止过多处理 需要放到Shiro过滤器之前
     */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception
    {
        boolean syncDbFlag = true;
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String uri = httpServletRequest.getRequestURI();
            if(StringUtils.isNotEmpty(uri)) {
                // 首页查询工单总数和在线用户人数涉及到定时任务，此类请求不算做主动操作系统故而不更新在线用户表的最后访问时间
                if(uri.contains("benchTask/getTaskCounts") || uri.contains("/monitor/online/getOnLineCount"))
                    syncDbFlag = false;
            }
        }
        OnlineSession session = (OnlineSession) request.getAttribute(ShiroConstants.ONLINE_SESSION);
        // 如果session stop了 也不同步
        // session停止时间，如果stopTimestamp不为null，则代表已停止
        if (session != null && session.getUserId() != null && session.getStopTimestamp() == null)
        {
            if(syncDbFlag){
                //  onlineSessionDAO.syncToDb(session);
                sessionDao.syncToDb(session);
            }
        }
        return true;
    }
}
