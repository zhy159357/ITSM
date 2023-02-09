package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 查询公告通知
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/selectNotice")
public class SysNoticeSelectController extends BaseController {

    private String prefix = "system/notice";

    @Autowired
    private ISysNoticeService noticeService;

    @GetMapping()
    public String notice()
    {

          return prefix + "/selectNotice";
    }
}
