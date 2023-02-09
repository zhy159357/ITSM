package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.SysNoticeReceive;
import com.ruoyi.system.domain.SysNoticeReceiveLog;
import com.ruoyi.system.mapper.SysNoticeMapper;
import com.ruoyi.system.mapper.SysNoticeReceiveLogMapper;
import com.ruoyi.system.mapper.SysNoticeReceiveMapper;
import com.ruoyi.system.service.ISysNoticeReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公告接收 服务层实现
 *
 * @author ruoyi
 * @date 2021-03-01
 */
@Service
public class SysNoticeReceiveServiceImpl implements ISysNoticeReceiveService{

    @Autowired
    private SysNoticeReceiveMapper sysNoticeReceiveMapper;

    @Autowired
    private SysNoticeMapper sysNoticeMapper;

    @Autowired
    private SysNoticeReceiveLogMapper sysNoticeReceiveLogMapper;

    /**
     * 查询接收公告信息
     *
     * @param amReceiveId 接收公告ID
     * @return 公告信息
     */
    public SysNoticeReceive selectNoticeReceiveById(String amReceiveId){

        SysNoticeReceive sysNoticeReceive = new SysNoticeReceive();
        sysNoticeReceive = sysNoticeReceiveMapper.selectNoticeReceiveById(amReceiveId);
        if (sysNoticeReceive.getNoticeDeptName() != null) {
            sysNoticeReceive.setReceiveDeptGroupName(sysNoticeReceive.getNoticeDeptName());
        } else {
            sysNoticeReceive.setReceiveDeptGroupName(sysNoticeReceive.getNoticeGroupName());
        }
        return sysNoticeReceive;
    }

    /**
     * 查询待处理公告列表
     *
     * @param sysNoticeReceive 公告信息
     * @return 公告集合
     */
    public List<SysNoticeReceive> selectNoticeReceiveList(SysNoticeReceive sysNoticeReceive){

        List<SysNoticeReceive> list = sysNoticeReceiveMapper.selectNoticeReceiveList(sysNoticeReceive);
        List<SysNoticeReceive> dateList = new ArrayList<>();
        String pid = ShiroUtils.getOgUser().getpId();
        int falg = 0;
        for (int i = 0 ; i < list.size() ; i ++) {//查询当前登录人需要处理的公告
            if (StringUtils.isNotEmpty(list.get(i).getReceivePersonList())) {
                String[] ids = list.get(i).getReceivePersonList().split(",");
                for (int j = 0; j < ids.length; j++) {
                    if (pid.equals(ids[j])) {
                        falg = 1;
                        break;
                    }
                }
            }
            if ( falg == 1) {
                if (StringUtils.isEmpty(list.get(i).getReceiveTime())) {
                    insertReceiveLog(list.get(i));//接收日志
                    SysNoticeReceive noticeReceive = new SysNoticeReceive();
                    noticeReceive.setAmReceiveId(list.get(i).getAmReceiveId());
                    noticeReceive.setReceiveTime(DateUtils.getTime());
                    sysNoticeReceiveMapper.updateNoticeReceiveReceiveTime(noticeReceive);//接收时间
                }
                SysNoticeReceiveLog sysNoticeReceiveLog = new SysNoticeReceiveLog();
                sysNoticeReceiveLog.setPerformerId(pid);
                sysNoticeReceiveLog.setAmReceiveId(list.get(i).getAmReceiveId());
                List<SysNoticeReceiveLog> receiveLogList = sysNoticeReceiveLogMapper.selectNoticeReceiveLogList(sysNoticeReceiveLog);
                if (receiveLogList.size() == 0) {
                    insertReceiveLog(list.get(i));//接收日志
                }
                dateList.add(list.get(i));
                falg = 0;
            }
        }
        return dateList;
    }

    /**
     * 查询接收公告列表查询公告通知，监控公告通知接口
     *
     * @param sysNoticeReceive 公告信息
     * @return 公告集合
     */
    public List<SysNoticeReceive> selectNoticeReceives(SysNoticeReceive sysNoticeReceive){

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("beginTime", sysNoticeReceive.getParams().get("beginTime"));
        String endTime = (String) sysNoticeReceive.getParams().get("endTime");
        if (StringUtils.isNotEmpty(endTime)) {
            endTime += " " + "23:59:59";
        }
        map.put("endTime", endTime);
        sysNoticeReceive.setParams(map);
        List<SysNoticeReceive> list = sysNoticeReceiveMapper.selectNoticeReceives(sysNoticeReceive);
        return list;
    }

    /**
     * 处理公告
     *
     * @param sysNoticeReceive 公告信息
     * @return 结果
     */
    public int updateNoticeReceive(SysNoticeReceive sysNoticeReceive) {

        SysNoticeReceive noticereceive = new SysNoticeReceive();
        noticereceive.setAmReceiveId(sysNoticeReceive.getAmReceiveId());//Id
        noticereceive.setEditTime(DateUtils.getTime());//更新时间
        noticereceive.setIfReceive(sysNoticeReceive.getIfReceive());//处理状态
        String pId = ShiroUtils.getOgUser().getpId();
        noticereceive.setDealer(pId);//处理人
        noticereceive.setIfReceive("1");
        SysNotice notice = new SysNotice();
        notice = sysNoticeMapper.selectNoticeById(sysNoticeReceive.getAmBizId());
        notice.setCurrentStatus("05");//处理中
        String receiveReply = sysNoticeReceive.getReceiveReply();
        int flag = 0;
        if (sysNoticeReceive.getAmType().equals("1") || receiveReply != null && !receiveReply.equals("")) {
            flag = 1;
        }
        //处理完成 进度加1
        if (flag == 1) {
            noticereceive.setReceiveReply(receiveReply);//处理反馈
            noticereceive.setIfReceive("2");
            //公告处理进度
            String dealSchdule = notice.getDealSchdule();
            String[] arr = dealSchdule.split("/");
            int index = Integer.valueOf(arr[0]) + 1;
            int num = Integer.valueOf(arr[1]);
            if (index == num) {
                notice.setCurrentStatus("06");//处理完毕
                notice.setEndTime(DateUtils.getTime());
            }
            String s = String.valueOf(index);
            dealSchdule = s + "/" + arr[1];
            notice.setDealSchdule(dealSchdule);
        }
        sysNoticeMapper.checkNotice(notice);
        int num = sysNoticeReceiveMapper.updateNoticeReceive(noticereceive);
        insertReceiveLog(noticereceive);
        return num;
    }

    /**
     * 接收日志
     *
     * @param sysNoticeReceive 公告信息
     * @return 结果
     */
    private int insertReceiveLog(SysNoticeReceive sysNoticeReceive) {

        SysNoticeReceiveLog sysNoticeReceiveLog = new SysNoticeReceiveLog();
        sysNoticeReceiveLog.setAmActlogId(UUID.getUUIDStr());
        sysNoticeReceiveLog.setAmReceiveId(sysNoticeReceive.getAmReceiveId());
        sysNoticeReceiveLog.setPerformerId(ShiroUtils.getOgUser().getpId());
        sysNoticeReceiveLog.setEditTime(DateUtils.getTime());
        sysNoticeReceiveLog.setDescription(sysNoticeReceive.getReceiveReply());
        sysNoticeReceiveLog.setCurrentState(sysNoticeReceive.getIfReceive());
        return sysNoticeReceiveLogMapper.insertNoticeReceiveLog(sysNoticeReceiveLog);
    }

    /**
     * 查询接收公告日志List
     * @param sysNoticeReceiveLog 公告接受日志
     * @return 结果
     */
    public List<SysNoticeReceiveLog> selectNoticeReceiveLogList(SysNoticeReceiveLog sysNoticeReceiveLog){

        List<SysNoticeReceiveLog> list = sysNoticeReceiveLogMapper.selectNoticeReceiveLogList(sysNoticeReceiveLog);
        return list;
    }

    /**
     * 接收详情
     * @param sysNoticeReceiveLog 公告接受日志
     * @return 结果
     */
    public List<SysNoticeReceiveLog> receiveLogList(SysNoticeReceiveLog sysNoticeReceiveLog){

        List<SysNoticeReceiveLog> list = sysNoticeReceiveLogMapper.receiveLogList(sysNoticeReceiveLog);
        return list;
    }

    /**
     * 短信通知接收人
     *
     * @param pid 人员id
     * @return 结果
     */
    public SysNoticeReceive smsNoticePersonById(String pid){

        SysNoticeReceive sysNoticeReceive = sysNoticeReceiveMapper.smsNoticePersonById(pid);
        return  sysNoticeReceive;
    }

}
