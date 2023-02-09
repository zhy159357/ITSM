package com.ruoyi.web.controller.system;

import com.ruoyi.activiti.constants.WorkStatusConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.ISysUserOnlineService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class SysLogoutController extends BaseController {

    @Autowired
    private ISysUserOnlineService sysUserOnlineService;

    /**
     * 强制退出后重定向的地址
     */
    @Value("${shiro.user.loginUrl}")
    private String loginUrl;

    /**
     * 退出登录错误提示地址
     */
    private final String logoutErrorUrl = "error/logout";

    @GetMapping("/logout")
    public String logout(ModelMap map) {
        Subject subject = SecurityUtils.getSubject();
        OgUser ogUser = ShiroUtils.getOgUser();
        String status = sysUserOnlineService.selectWorkStatusByUserId(ogUser.getUserId());
        // 工作状态为空或者是签退状态才允许退出登录，其他情况跳转到错误页面
        if (StringUtils.isEmpty(status) || WorkStatusConstants.qtzt.equals(status)) {
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(ogUser.getUsername(), Constants.LOGOUT, MessageUtils.message("user.logout.success")));
            // 清理缓存
            sysUserOnlineService.removeUserCache(ogUser.getUsername(), ShiroUtils.getSessionId());
            if (subject.isAuthenticated()) {
                subject.logout();
            }
            return redirect(loginUrl);
        } else {
            map.put("errorMessage", "尊敬的用户【" + ogUser.getUsername() + "】您好，您当前工作状态不是【已签退】，不能退出登录！");
            return logoutErrorUrl;
        }
    }
}
