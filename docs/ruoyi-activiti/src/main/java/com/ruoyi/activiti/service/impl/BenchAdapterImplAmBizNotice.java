package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.AbstractBenchAdapter;
import com.ruoyi.activiti.domain.AmBizNotice;
import com.ruoyi.activiti.domain.BenchTask;
import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.activiti.service.IAmBizNoticeService;
import com.ruoyi.common.core.domain.entity.OgUserPost;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.SysNoticeReceive;
import com.ruoyi.system.service.IOgUserPostService;
import com.ruoyi.system.service.ISysNoticeReceiveService;
import com.ruoyi.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 公告通知
 */
@Service("benchAdapterImplAmBizNotice")
public class BenchAdapterImplAmBizNotice extends AbstractBenchAdapter {

    @Autowired
    private IAmBizNoticeService amBizNoticeService;

    @Autowired
    private IOgUserPostService ogUserPostService;

    @Value("${pagehelper.helperDialect}")
    private String  dbType;

    @Override
    public List<String> getAuthRoles() {
        List<String> roles = new ArrayList<String>();
        roles.add("4100");
        roles.add("4200");
        roles.add("4201");
        return roles;
    }

    @Override
    public String getBenchType() {
        return "AM";
    }

    @Override
    public TableDataInfo getBenchPagerTasks(String userId, String type) {

        AmBizNotice amBizNotice = new AmBizNotice();
        OgUserPost ogUserPost = new OgUserPost();
        ogUserPost.setUserId(ShiroUtils.getUserId());
        String sendRanges = "3";//1 数据中心 2 厂商+数据中心（72号院） 3 所有用户
        List<OgUserPost> postslist = ogUserPostService.selectPostByUserId(ogUserPost);
        for (OgUserPost ogUserPost1 : postslist) {
            if ("0010".equals(ogUserPost1.getPostId()) || "0011".equals(ogUserPost1.getPostId()) ||"0012".equals(ogUserPost1.getPostId())) {
                sendRanges = "1,2,3";
                break;
            }
            if ("0002".equals(ogUserPost1.getPostId())) {
                sendRanges = "2,3";
            }
        }
        amBizNotice.setCurrentStatus("04");
        amBizNotice.setSendRanges(sendRanges.split(","));
        //工作台进来的
        if("AM".equals(type)){
            amBizNotice.setTag(type);
        }
        List<AmBizNotice> list = amBizNoticeService.selectAmBizNoticeList(amBizNotice);
        return getDataTable_ideal(list);
    }
}
