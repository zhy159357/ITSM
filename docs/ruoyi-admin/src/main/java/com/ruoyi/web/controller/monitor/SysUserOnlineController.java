package com.ruoyi.web.controller.monitor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OnlineStatus;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.session.OnlineSession;
import com.ruoyi.framework.shiro.session.SessionDao;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.service.ISysUserOnlineService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在线用户监控
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController
{
    private String prefix = "monitor/online";

    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
//    private OnlineSessionDAO onlineSessionDAO;
    private SessionDao sessionDao;
    @RequiresPermissions("monitor:online:view")
    @GetMapping()
    public String online()
    {
        return prefix + "/online";
    }

    @RequiresPermissions("monitor:online:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUserOnline userOnline)
    {
        List<String> loginNameList= userOnlineService.distinctLoginName(userOnline);
        List<SysUserOnline> list=new ArrayList<>();
        for(String loginName:loginNameList){
            SysUserOnline sysUserOnline=userOnlineService.selectOnlineByLoginName(loginName);
            list.add(sysUserOnline);
        }
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<SysUserOnline> newList;
        if("loginName".equals(pageDomain.getOrderByColumn())){
            if("desc".equals(pageDomain.getIsAsc())){
                list=list.stream().sorted(Comparator.comparing(SysUserOnline::getLoginName).reversed()).collect(Collectors.toList());
            }else{
                list=list.stream().sorted(Comparator.comparing(SysUserOnline::getLoginName)).collect(Collectors.toList());
            }
        }else if("startTimestamp".equals(pageDomain.getOrderByColumn())){
            if("desc".equals(pageDomain.getIsAsc())){
                list=list.stream().sorted(Comparator.comparing(SysUserOnline::getStartTimestamp).reversed()).collect(Collectors.toList());
            }else{
                list=list.stream().sorted(Comparator.comparing(SysUserOnline::getStartTimestamp)).collect(Collectors.toList());
            }
        }else if("lastAccessTime".equals(pageDomain.getOrderByColumn())){
            if("desc".equals(pageDomain.getIsAsc())){
                list=list.stream().sorted(Comparator.comparing(SysUserOnline::getLastAccessTime).reversed()).collect(Collectors.toList());
            }else{
                list=list.stream().sorted(Comparator.comparing(SysUserOnline::getLastAccessTime)).collect(Collectors.toList());
            }
        }
        if(StringUtils.isNotBlank(userOnline.getIpaddr())){
            list=list.stream().filter(sysUserOnline -> sysUserOnline.getIpaddr().contains(userOnline.getIpaddr())).collect(Collectors.toList());
        }
        if (pageNum * pageSize > list.size()) {
            if (pageSize > list.size()) {
                newList = list;
            } else {
                if((pageNum - 1) * pageSize>list.size()){
                    int freeV=list.size()%pageSize;
                    newList = list.subList(list.size()-freeV, list.size());
                }else{
                    newList = list.subList((pageNum - 1) * pageSize, list.size());
                }
            }
        } else {
            newList = list.subList((pageNum - 1) * pageSize, pageNum * pageSize);
        }
        return getDataTable(newList,list);
    }

    @RequiresPermissions(value = { "monitor:online:batchForceLogout", "monitor:online:forceLogout" }, logical = Logical.OR)
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("/batchForceLogout")
    @ResponseBody
    public AjaxResult batchForceLogout(String ids)
    {
        for (String sessionId : Convert.toStrArray(ids))
        {
            SysUserOnline online = userOnlineService.selectOnlineById(sessionId);
            if (online == null)
            {
                return error("用户已下线");
            }
//            OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
            OnlineSession onlineSession = (OnlineSession) sessionDao.readSession(online.getSessionId());
            if (onlineSession == null)
            {
                return error("用户已下线");
            }
            if (sessionId.equals(ShiroUtils.getSessionId()))
            {
                return error("当前登录用户无法强退");
            }
            sessionDao.deleteSession(onlineSession);
            online.setStatus(OnlineStatus.off_line);
            userOnlineService.saveOnline(online);
//            userOnlineService.removeUserCache(online.getLoginName(), sessionId);
        }
        return success();
    }

    @PostMapping("/getOnLineCount")
    @ResponseBody
    public AjaxResult getOnLineCount()
    {
        AjaxResult ajaxResult=new AjaxResult();
        int getOnLineCount= userOnlineService.getOnLineCount();
        ajaxResult.put("onLineCount",getOnLineCount);
        return ajaxResult;
    }
}
