package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.FmBizKh;
import com.ruoyi.activiti.domain.FmBizOverTime;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.mapper.FmBizOvertimeMapper;
import com.ruoyi.activiti.service.IFmBizKhService;
import com.ruoyi.activiti.service.IFmBizOvertimeService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IPubFlowLogService;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysWorkService;
import com.ruoyi.system.service.impl.PubHolidayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 业务事件单考核超时Service业务层处理
 *
 * @author ruoyi
 * @date 2021-03-15
 */
@Service
public class FmBizOvertimeServiceImpl implements IFmBizOvertimeService {
    private static final Logger logger = LoggerFactory.getLogger(FmBizOverTime.class);
    @Autowired
    private FmBizOvertimeMapper fmBizOvertimeMapper;
    @Autowired
    private IFmBizKhService fmBizKhService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private IFmBizService fmBizService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    private ISysApplicationManagerService sysApplicationManagerService;
    @Autowired
    private ISysWorkService sysWorkService;
    @Autowired
    private PubHolidayServiceImpl holidayService;

    private static SimpleDateFormat ymd_ = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 查询业务事件单考核超时
     *
     * @param singelNumber 业务事件单考核超时ID
     * @return 业务事件单考核超时
     */
    @Override
    public FmBizOverTime selectFmBizOvertimeById(String singelNumber) {
        return fmBizOvertimeMapper.selectFmBizOvertimeById(singelNumber);
    }

    /**
     * 查询业务事件单考核超时列表
     *
     * @param fmBizOvertime 业务事件单考核超时
     * @return 业务事件单考核超时
     */
    @Override
    public List<FmBizOverTime> selectFmBizOvertimeList(FmBizOverTime fmBizOvertime) {
        return fmBizOvertimeMapper.selectFmBizOvertimeList(fmBizOvertime);
    }

    /**
     * 新增业务事件单考核超时
     *
     * @param fmBizOvertime 业务事件单考核超时
     * @return 结果
     */
    @Override
    public int insertFmBizOvertime(FmBizOverTime fmBizOvertime) {
        return fmBizOvertimeMapper.insertFmBizOvertime(fmBizOvertime);
    }

    /**
     * 修改业务事件单考核超时
     *
     * @param fmBizOvertime 业务事件单考核超时
     * @return 结果
     */
    @Override
    public int updateFmBizOvertime(FmBizOverTime fmBizOvertime) {
        return fmBizOvertimeMapper.updateFmBizOvertime(fmBizOvertime);
    }

    /**
     * 删除业务事件单考核超时对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmBizOvertimeByIds(String ids) {
        return fmBizOvertimeMapper.deleteFmBizOvertimeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务事件单考核超时信息
     *
     * @param singelNumber 业务事件单考核超时ID
     * @return 结果
     */
    @Override
    public int deleteFmBizOvertimeById(String singelNumber) {
        return fmBizOvertimeMapper.deleteFmBizOvertimeById(singelNumber);
    }

    @Override
    public String existData(String monthStr) {
        String isExistData = "0";
        FmBizOverTime tbot = new FmBizOverTime();
        tbot.setDatetime(monthStr);
        List<FmBizOverTime> fmBizOvertimeList = fmBizOvertimeMapper.selectFmBizOvertimeList(tbot);
        if (!fmBizOvertimeList.isEmpty()) {
            isExistData = "1";//存在
        }
        return isExistData;
    }

    @Override
    public void createData(String monthStr) {

        FmBizOverTime fmBizOvertime = new FmBizOverTime();
        fmBizOvertime.setDatetime(monthStr);//设置考核年月
        //查询上月已完成的变更单 09-关闭状态 非转数据变更单
        //查询岗位为总行业务人员的所有person  postId为总行业务人员ID
        //未转运维的系统除外
        Map<String, String> map = new HashMap<>();
        map.put("postId", "0013");
        List<OgPerson> ogPersonList = ogPersonService.selectPersonByPostId(map);//拿到所有总行业务人员
        List<FmBiz> completedFmBizByDate = fmBizService.getCompletedFmBizByDate2(monthStr);//拿到已经关闭的事件单
        List<PubFlowLog> flowLogStringList = pubFlowLogService.findFmIds(monthStr);//拿到未解决退回后的时间
        OgGroup group = new OgGroup();
        group.setGroupType("1");
        List<OgGroup> groupList = sysWorkService.selectOgGroupList(group);//拿到1线工作组
        for (FmBiz fmBizFaultinfo : completedFmBizByDate) {
            try {
                String temp = "0";
                if (StringUtils.isNotEmpty(fmBizFaultinfo.getParticipatorIds())) {
                    String[] personIds = fmBizFaultinfo.getParticipatorIds().split(",");
                    //如果是总行业务人员处理过那么不计入考核
                    for (OgPerson ogPerson : ogPersonList) {
                        for (String personId : personIds) {
                            if (ogPerson.getpId().equals(personId)) {
                                temp = "1";
                                break;
                            }
                        }
                        if("1".equals(temp)) {//如果包含直接抛出
                            break;
                        }
                    }
                }
                if ("1".equals(temp)) {//如果该事件单包含业务人员直接抛出
                    continue;
                }
                String type="0";
                if (StringUtils.isNotEmpty(fmBizFaultinfo.getParticipateGroupids())) {//判断工作组是否包含一线组
                    String[] groups = fmBizFaultinfo.getParticipateGroupids().split(",");
                    //如果包含1线工作组不计入考核
                    for (OgGroup grp : groupList) {
                        for (String groupId : groups) {
                            if (grp.getGroupId().equals(groupId)) {
                                type="1";
                                break;
                            }
                        }
                        if("1".equals(type)) {//如果包含直接抛出
                            break;
                        }
                    }
                }
                if("1".equals(type)){//如果包含循环下个事件单
                    continue;
                }
                //开始时间
                String createTime = fmBizFaultinfo.getToQgzxTime();//拿到转全国中心时间
                String fmId = fmBizFaultinfo.getFmId();
                if (!flowLogStringList.isEmpty()) {
                    for (PubFlowLog pfl : flowLogStringList) {
                        if (pfl.getBizId().contains(fmId)) {
                            List<PubFlowLog> flowLogList = pubFlowLogService.selectPubFlowLogDesc(fmId);
                            createTime = flowLogList.get(0).getPerformerTime();//如果有未解决退回拿未解决退回的操作时间做为新的转全国中心时间
                        }
                    }
                }
                String endTime = fmBizFaultinfo.getDealTime();//拿最终处理时间做为结束时间
            /*
              判断是否是紧急事件单  1是，2否
              任何时间提交的紧急事件单，在提交后2小时内处理完成。
             */
                String ifjj = fmBizFaultinfo.getIfJj();
                if ("1".equals(ifjj)) {
                    //紧急事件单时间的处理
                    boolean urgentTimeDifference = urgentTimeDifference(createTime, endTime);
                    //true是大于两小时，需要记录当前事件单，写入库
                    saveFmBizOverTime(fmBizOvertime, fmBizFaultinfo, urgentTimeDifference, ifjj);
                } else {
                    //非紧急事件单时间的处理
                    boolean nonEmergencyTimeDifference = NonEmergencyTimeDifference(createTime, endTime);
                    /*
                     * true则代表：
                     * 1、工作日8点——17点之间，提交的事件单，在工作日的24点之前处理完成。
                     * 2、工作日17点之后——下个工作日8点之间，提交的事件单，在下个工作日的17点之前完成。
                     * 3、非工作日——下个工作日8点之间，提交的事件单，在下个工作日的17点之前完成。
                     */
                    saveFmBizOverTime(fmBizOvertime, fmBizFaultinfo, nonEmergencyTimeDifference, ifjj);
                }
            } catch (Exception e) {
                logger.error("事件单考核FmBizOverTime生成失败，失败单号为：" + fmBizFaultinfo.getFaultNo() + "=====" + e.getMessage());
                continue;

            }
        }
    }

    @Override
    public void getFmBizOvertimeDates(String monthStr) throws BusException {
        //查询当月关闭事件单
        List<FmBiz> completedFmBizByDate = fmBizService.getCompletedFmBizByDate(monthStr);
        List<String> list = new ArrayList<>();
        for (FmBiz fmBizFaultinfo : completedFmBizByDate) {
            String participateGroupIds = fmBizFaultinfo.getParticipateGroupids();
            String[] groupIds = new String[0];
            if (participateGroupIds != null) {
                groupIds = participateGroupIds.split(",");
                int length = groupIds.length;
                String groupId1 = groupIds[length - 1];
                list.add(groupId1);
            }
        }
        Map map = new HashMap();
        for (String s : list) {
            Integer count = (Integer) map.get(s);
            int i = (count == null) ? 1 : count + 1;
            map.put(s, i);
        }

        Set<String> keySet = map.keySet();
        OgSys sys = new OgSys();
        sys.setInvalidationMark("1");
        List<OgSys> ogSysList = sysApplicationManagerService.selectOgSysList(sys);
        for (OgSys ogSys : ogSysList) {
            String jjCount = "0";
            FmBizOverTime fbot = new FmBizOverTime();
            fbot.setSystemId(ogSys.getSysId());
            fbot.setIfjjtype("1");
            fbot.setDatetime(monthStr);
            FmBizOverTime list3 = fmBizOvertimeMapper.findIfjjCount(fbot);
            if (!ObjectUtils.isEmpty(list3)) {
                jjCount = list3.getProportion();//紧急数量
            }
            String fjjCount = "0";
            FmBizOverTime fbot2 = new FmBizOverTime();
            fbot2.setSystemId(ogSys.getSysId());
            fbot2.setIfjjtype("2");
            fbot2.setDatetime(monthStr);
            FmBizOverTime list4 = fmBizOvertimeMapper.findIfjjCount(fbot2);
            if (!ObjectUtils.isEmpty(list4)) {
                fjjCount = list4.getProportion();//非紧急事件单超时数量
            }
            FmBizKh fmBizKH = new FmBizKh();
            if (jjCount.startsWith(".")) {
                fmBizKH.setJjcount("0" + jjCount);
            } else {
                fmBizKH.setJjcount(jjCount);
            }
            if (fjjCount.startsWith(".")) {
                fmBizKH.setFjjcount("0" + fjjCount);
            } else {
                fmBizKH.setFjjcount(fjjCount);
            }
            fmBizKH.setSysid(ogSys.getSysId());
            fmBizKH.setCompletecount("0");
            fmBizKH.setDatetime(monthStr);
            fmBizKH.setSatisfaction("0");
            fmBizKH.setUnsatisfactory("0");
            fmBizKH.setId(UUID.getUUIDStr());
            fmBizKhService.insertFmBizKh(fmBizKH);
        }
        FmBizKh fbk = new FmBizKh();
        fbk.setDatetime(monthStr);
        List<FmBizKh> fmBizKHS = fmBizKhService.selectFmBizKhList(fbk);
        // 遍历keySet
        for (String key : keySet) {
            OgGroup ogGroup = sysWorkService.selectOgGroupById(key);
            String sysid = "";
            if (ogGroup != null) {
                if (ogGroup.getSysId() != null && !ogGroup.getSysId().contains(",")) {
                    sysid = ogGroup.getSysId();
                }
            }
            String closeCount = map.get(key).toString();//关闭事件单数量
            for (FmBizKh fmBizKHlist : fmBizKHS) {
                if (sysid.equals(fmBizKHlist.getSysid())) {
                    //保存数据
                    FmBizKh fmBizKH = new FmBizKh();
                    fmBizKH.setId(fmBizKHlist.getId());
                    fmBizKH.setSysid(sysid);
                    if (Integer.parseInt(fmBizKHlist.getCompletecount()) > 0) {
                        fmBizKH.setCompletecount((Integer.parseInt(closeCount) + Integer.parseInt(fmBizKHlist.getCompletecount())) + "");
                    } else {
                        fmBizKH.setCompletecount(closeCount);
                    }
                    fmBizKH.setDatetime(monthStr);
                    String jjcount = fmBizKHlist.getJjcount();
                    String fjjcount = fmBizKHlist.getFjjcount();
                    if (jjcount.startsWith(".")) {
                        fmBizKH.setJjcount("0" + jjcount);
                    } else {
                        fmBizKH.setJjcount(jjcount);
                    }
                    if (fjjcount.startsWith(".")) {
                        fmBizKH.setFjjcount("0" + fjjcount);
                    } else {
                        fmBizKH.setFjjcount(fjjcount);
                    }
                    FmBizKh fbkh = new FmBizKh();
                    fbkh.setSysid(sysid);
                    fbkh.setDatetime(monthStr);
                    FmBizKh fbizk = fmBizKhService.getFmBizSatisfaction(fbkh);//获取该系统满意率
                    fmBizKH.setSatisfaction(fbizk.getSatisfaction());
                    fmBizKH.setUnsatisfactory(fbizk.getUnsatisfactory());
                    fmBizKhService.updateFmBizKh(fmBizKH);
                }
            }
        }
    }

    /**
     * 紧急事件单时间判断
     *
     * @param createTimeStr 开始时间 20190312110746
     * @param endTimeStr    结束时间 20190312110746
     * @return 是否大于两小时
     */
    private boolean urgentTimeDifference(String createTimeStr, String endTimeStr) {
        createTimeStr = DateUtils.formatDateStr(createTimeStr, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
        endTimeStr = DateUtils.formatDateStr(endTimeStr, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startTime = hms.parse(createTimeStr);
            Date endTime = hms.parse(endTimeStr);
            //计算开始时间与结束时间段之间的时长（秒）
            Long usedTimes = (endTime.getTime() - startTime.getTime()) / 1000;
            //如果用时大于两小时
            return usedTimes > 7200;
        } catch (Exception e) {
            throw new BusinessException("日期转换失败");
        }
    }

    /**
     * 非紧急事件单时间判断
     *
     * @param createTimeStr 开始时间 20190312110746
     * @param endTimeStr    结束时间 20190312110746
     * @return true则需要记录
     */
    private boolean NonEmergencyTimeDifference(String createTimeStr, String endTimeStr) {
        try {
            //判断是否是节假日
            createTimeStr = DateUtils.formatDateStr(createTimeStr, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
            endTimeStr = DateUtils.formatDateStr(endTimeStr, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
            boolean isH = holidayService.isHoliday(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTimeStr));
            //判断开始时间是否在8-17点之间
            String format = "HH:mm:ss";
            assert createTimeStr != null;
            Date creatTime = new SimpleDateFormat(format).parse(createTimeStr.substring(11));
            Date startTime = new SimpleDateFormat(format).parse("08:00:00");
            Date endTime = new SimpleDateFormat(format).parse("17:00:00");
            boolean effectiveDate = isEffectiveDate(creatTime, startTime, endTime);
            //判断如果不是节假日并且在8-17点之间，那么完成时间需要在当天24点之前完成。
            if (!isH && effectiveDate) {
                //判断是否在当天24点之前完成
                return !isBeforeTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeStr), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTimeStr.substring(0, 11) + "24:00:00"));
            } else { //如果是节假日或者在非工作时间，那么提交的事件单在  下个工作日 17点之前完成。
                Date day = ymd_.parse(createTimeStr);
                //生成下个工作日17:00:00
                String workDayStartTime = processStartTime(createTimeStr);
//                String workDayStartTime = getWorkDayStartTime(ymd_.format(day));
                //判断是否在计算的时间之前
                return !isBeforeTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTimeStr), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(workDayStartTime));
            }
        } catch (Exception e) {
            throw new BusinessException("日期转换失败:createTimeStr" + createTimeStr + "------endTimeStr:" + endTimeStr);
        }
    }

    private void saveFmBizOverTime(FmBizOverTime fmBizOvertime, FmBiz fmBizFaultinfo, boolean nonEmergencyTimeDifference, String ifjj) {
        if (nonEmergencyTimeDifference) {
            String participateGroupIds = fmBizFaultinfo.getParticipateGroupids();
            String[] groupIds = new String[0];
            List<String> list = new ArrayList();
            if (participateGroupIds != null) {
                groupIds = participateGroupIds.split(",");
                //去重
                for (int i = 0; i < groupIds.length; i++) {
                    if (!list.contains(groupIds[i])) {
                        list.add(groupIds[i].toString());
                    }
                }
            }

            int count = 0;
            for (String groupId : list) {
                OgGroup ogGroup = sysWorkService.selectOgGroupById(groupId);
                if (ogGroup != null) {
                    if (ogGroup.getSysId() != null && !ogGroup.getSysId().contains(",")) {//去除一线组相关
                        count++;
                    } else {
                        return;
                    }
                }
            }
            for (String groupId : list) {
                OgGroup ogGroup = sysWorkService.selectOgGroupById(groupId);
                if (ogGroup != null) {
                    if (ogGroup.getSysId() != null && !ogGroup.getSysId().contains(",")) {
                        fmBizOvertime.setSingelNumber(fmBizFaultinfo.getFaultNo());//单号
                        fmBizOvertime.setSystemId(ogGroup.getSysId());
                        double f = 1.0 / count;
                        fmBizOvertime.setProportion(String.format("%.2f", f));//所占权重
                        fmBizOvertime.setIfjjtype(ifjj);
                        fmBizOvertime.setId(UUID.getUUIDStr());
                        insertFmBizOvertime(fmBizOvertime);
                    }
                }
            }
        }
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }

    /**
     * 判断是否在endTime 之前
     *
     * @param nowTime 当前时间
     * @param endTime 结束时间
     * @return 之前则为true
     */
    public static boolean isBeforeTime(Date nowTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        return date.before(end);
    }

    public String processStartTime(String startTimeStr) {
        try {
            //对开始时间进行预处理，使其处于工作日内的工作时间段内
            //String startTimeStr = "2016-09-29 08:00:00";
            Date day = ymd_.parse(startTimeStr);//当前日期2016-09-29
            String sTime = ymd_.format(day);//当前日期"2016-09-29"
            String timeStr1 = sTime + " 08:00:00";//上班时间2016-09-29 08:30:00
            String timeStr2 = sTime + " 17:00:00";//下班时间2016-09-29 17:30:00
            String timeStr3 = new PubHolidayServiceImpl().getNextDay(day) + " 17:00:00";//次日上班时间2016-09-30 08:30:00
            if (startTimeStr.compareTo(timeStr1) < 0) {
                //开始时间小于当日上班时间，将开始时间赋值为当日上班时间
                startTimeStr = timeStr2;
            } else if (startTimeStr.compareTo(timeStr2) >= 0) {
                //开始时间大于等于当日下班时间，将开始时间赋值为次日上班时间
                startTimeStr = timeStr3;
            }
            //如果开始时间为节假日或周末，那么将开始时间向后推移到最近的工作日的8:30。
            return getWorkDayStartTime(startTimeStr);

        } catch (Exception e) {
            throw new BusinessException("日期转换失败");
        }
    }

    /**
     * 如果开始时间为节假日或周末，那么将开始时间向后推移到最近的工作日的17:00
     *
     * @param startTimeStr 开始时间
     */
    public String getWorkDayStartTime(String startTimeStr) {
        String workDayStartTime = startTimeStr;
        try {
            Date startTime = ymd_.parse(startTimeStr);
            boolean isH = holidayService.isHoliday(startTime);//判断是否是节假日
            if (isH) {
                //是节假日，将日期向后移动一天，判断是否是节假日
                startTimeStr = new PubHolidayServiceImpl().getNextDay(startTime) + " 17:00:00";
                workDayStartTime = getWorkDayStartTime(startTimeStr);
            }
//            else{
//                //不是节假日，当天17点之前完成
//                String format = ymd_.format(startTime);
//                startTimeStr = format + " 17:00:00";
//                return startTimeStr;
//            }
        } catch (Exception e) {
            throw new BusinessException("日期转换失败");
        }
        return workDayStartTime;
    }
}
