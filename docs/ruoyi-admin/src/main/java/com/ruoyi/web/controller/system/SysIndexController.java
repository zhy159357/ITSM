package com.ruoyi.web.controller.system;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ruoyi.activiti.domain.ItsmWorkStatus;
import com.ruoyi.activiti.service.ItsmWorkStatusService;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.utils.*;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysAppService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysMenuService;

/**
 * 首页 业务处理
 * 
 * @author ruoyi
 */
@Controller
public class SysIndexController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysAppService appService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private ItsmWorkStatusService itsmWorkStatusService;

    @Value("${cntxtag.enabled}")
    private String cntxtag;

    /**
     * 首页是否显示工作标记状态和值班组按钮开关
     * 现阶段测试环境便于调试设置为true,生产环境设置为false,功能开发完毕设置为true
     */
    @Value("${shiro.user.workDutyEnabled}")
    private boolean workDutyEnabled;

    /**默认密码过期时间90天*/
    private long passwordExpiredDays = 90L;

    /**一天的毫秒数*/
    private long oneDayMillis = 24 * 60 * 60 * 1000L;

    // 系统首页
    @GetMapping("/index")
    public String index(HttpServletRequest request, ModelMap mmap)
    {
        // 取身份信息
        OgUser ogUser = ShiroUtils.getOgUser();
        String flag = "0";
        try {
            if(StringUtils.isNotEmpty(ogUser.getUpdateTime())){
                SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
                String time = DateUtils.dateTimeNow();
                Date d1 = sd.parse(time);
                Date d2 = sd.parse(StringUtils.formatDateStr(ogUser.getUpdateTime()));
                long diff =d1.getTime()-d2.getTime();
                List<PubParaValue> values = pubParaValueService.selectPubParaValueByParaName("password_expiredDays");
                if(!CollectionUtils.isEmpty(values)){
                    passwordExpiredDays = Long.valueOf(values.get(0).getValue());
                }
                if(diff >= passwordExpiredDays * oneDayMillis){
                    flag = "1";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String firstload=(String)request.getSession().getAttribute("firstload");
        //第一次登录
        if(StringUtils.isEmpty(firstload)){
            HttpSession httpSession=request.getSession();
            httpSession.setAttribute("firstload","no");

            // 第一次登录如果密码过期flag=1,会弹框提示，此时不需要判断，如果密码未过期，则校验秘密强度
            if("0".equals(flag)){
                try {
                    Subject subject = SecurityUtils.getSubject();
                    Serializable id = subject.getSession().getId();
                    String password = (String)CacheUtils.get("sys:login:password:"+id);
                    boolean check = checkPasswordStrength(password);
                    if(!check){
                        flag = "1";
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else {
            flag="0";
        }

        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(ogUser);

        //根据当前登录人查出他的工作状态
        if(workDutyEnabled){
            ItsmWorkStatus workStatus = itsmWorkStatusService.selectItsmWorkStatusNameById(ogUser.getpId());
            if(workStatus!=null){
                mmap.put("workStatus", workStatus.getWorkStatus());

            }
        }
        // 判断当前用户是否为数据中心或厂商人员  是-显示工作状态和值班组按钮  不是-则不显示
        mmap.put("dataCenter", workDutyEnabled && itsmWorkStatusService.isDataCenter(ogUser.getpId()) ? true : false);

        mmap.put("menus", menus);
        mmap.put("user", ogUser);
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.esideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        mmap.put("ignoreFooter", configService.selectConfigByKey("sys.index.ignoreFooter"));
        mmap.put("copyrightYear", RuoYiConfig.getCopyrightYear());
        mmap.put("demoEnabled", RuoYiConfig.isDemoEnabled());
        mmap.put("flag",flag);
//        mmap.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
//        mmap.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));

        // 菜单导航显示风格
        String menuStyle = configService.selectConfigByKey("sys.index.menuStyle");
        // 移动端，默认使左侧导航菜单，否则取默认配置
        String indexStyle = ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent")) ? "index" : menuStyle;

        // 优先Cookie配置导航菜单
        Cookie[] cookies = ServletUtils.getRequest().getCookies();
        for (Cookie cookie : cookies)
        {
            if (StringUtils.isNotEmpty(cookie.getName()) && "nav-style".equalsIgnoreCase(cookie.getName()))
            {
                indexStyle = cookie.getValue();
                break;
            }
        }
        return "topnav".equalsIgnoreCase(indexStyle) ? "index-topnav" : "index";

    }

    // 切换主题
    @GetMapping("/system/switchSkin")
    public String switchSkin()
    {
        return "skin";
    }

    // 切换菜单
    @GetMapping("/system/menuStyle/{style}")
    public void menuStyle(@PathVariable String style, HttpServletResponse response)
    {
        CookieUtils.setCookie(response, "nav-style", style);
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        mmap.put("version", RuoYiConfig.getVersion());
        mmap.put("cntxtag", cntxtag);
        return "main";
    }
    @GetMapping("/system/mainP")
    public String mainP(ModelMap mmap)
    {
        // 取身份信息
        String userId = ShiroUtils.getUserId();
        // 获取用户所属的角色列表
        List<SysApp> apps = appService.selectAppsByUserId(userId);
        mmap.put("version", RuoYiConfig.getVersion());
        mmap.put("appList", apps);
        if(null==apps||apps.size()==0){
            mmap.put("appHide",0);
        }
        return "main_p";
    }
    // 检查初始密码是否提醒修改
    public boolean initPasswordIsModify(Date pwdUpdateDate)
    {
        int initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify == 1 && pwdUpdateDate == null;
    }

    // 检查密码是否过期
    public boolean passwordIsExpiration(Date pwdUpdateDate)
    {
        int passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays > 0)
        {
            if (StringUtils.isNull(pwdUpdateDate))
            {
                // 如果从未修改过初始密码，直接提醒过期
                return true;
            }
            Date nowDate = DateUtils.getNowDate();
            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
        }
        return false;
    }

    /**
     * 校验密码强度
     */
    public boolean checkPasswordStrength(String password){
        // 如果缓存密码为空，不弹框提示
        if(StringUtils.isEmpty(password)){
            return true;
        }
        String regex = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,}$";
        return StringUtils.match(regex, password);
    }

}
