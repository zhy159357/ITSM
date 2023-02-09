package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.AbstractBenchAdapter;
import com.ruoyi.activiti.domain.BenchTask;
import com.ruoyi.activiti.domain.CmBizQingqiu;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.SysNoticeReceive;
import com.ruoyi.system.service.ISysNoticeReceiveService;
import com.ruoyi.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 公告通知
 */
@Service("benchAdapterImplSysNotice")
public class BenchAdapterImplSysNotice extends AbstractBenchAdapter {

    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private ISysNoticeReceiveService sysNoticeReceiveService;

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
        return "VM";
    }

    @Override
    public TableDataInfo getBenchPagerTasks(String userId, String type) {

        SysNotice notice1 = new SysNotice();
        notice1.setMyIdentity("1");
        notice1.setCurrentStatus("03");
        SysNotice notice2 = new SysNotice();
        SysNoticeReceive sysNoticeReceive = new SysNoticeReceive();
        Map<String, Object> map = new HashMap<>();
        map.put("currentStatus", "03");
        sysNoticeReceive.setParams(map);
        sysNoticeReceive.setAmBizIds(noticeService.receiveAmBizIds());
        List<SysNotice> list1 = noticeService.selectNoticeList(notice1);//我创建的退回的
        List<SysNotice> list2 = noticeService.selectNoticeCheckList(notice2);//待审核的
        List<SysNoticeReceive> listRecceive = sysNoticeReceiveService.selectNoticeReceiveList(sysNoticeReceive);//待处理的
        List<SysNotice> list = new ArrayList<>();
        if (list1 != null && list1.size() > 0) {
            for (SysNotice sysNotice : list1) {
                list.add(sysNotice);
            }
        }
        if (list2 != null && list2.size() > 0) {
            for (SysNotice sysNotice : list2) {
                list.add(sysNotice);
            }
        }
        if (listRecceive != null && listRecceive.size() > 0) {
            for (SysNoticeReceive sysNoticeReceive1 : listRecceive) {
                if (!"2".equals(sysNoticeReceive1.getIfReceive())) {
                SysNotice sysNotice1 = new SysNotice();
                sysNotice1.setAmBizId(sysNoticeReceive1.getAmBizId());
                sysNotice1.setAmCode(sysNoticeReceive1.getAmCode());
                sysNotice1.setAmTitle(sysNoticeReceive1.getAmTitle());
                sysNotice1.setAmType(sysNoticeReceive1.getAmType());
                sysNotice1.setpName(sysNoticeReceive1.getpName());
                sysNotice1.setReleaseDate(sysNoticeReceive1.getReleaseDate());
                sysNotice1.setCurrentStatus(sysNoticeReceive1.getCurrentStatus());
                list.add(sysNotice1);
                }
            }
        }
        String ambizids = "";
        if (list != null && list.size() > 0) {
            for (int i = 0 ; i < list.size() ; i ++) {
                ambizids += list.get(i).getAmBizId() + ",";
            }
            SysNotice notice = new SysNotice();
            notice.setAmBizIds(ambizids.split(","));
            notice.setMyIdentity("4");
            list = noticeService.selectNoticeList(notice);
        }
        return getDataTable_ideal(list);
    }
}
