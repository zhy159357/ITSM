package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.ComputerRoom;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.IComputerRoomService;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 机房进出入定时任务
 */
@Component("computerRoomTask")
public class ComputerRoomTask {
    private static final Logger logger = LoggerFactory.getLogger(ComputerRoomTask.class);
    @Autowired
    private TaskLockManager taskLockManager;
    @Autowired
    private IPubBizSmsService pubBizSmsService;
    @Autowired
    private IPubBizPresmsService pubBizPresmsService;
    @Autowired
    private IComputerRoomService iComputerRoomService;
    @Autowired
    private IOgPersonService iOgPersonService;


    public void sendComputerRoomTask() throws ParseException {
        if(taskLockManager.lock("computerRoomTask")) {
            logger.debug("--------定时任务【computerRoomTask】执行开始--------");
            long start = System.currentTimeMillis();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dates = new Date();
            String nowDate = dateFormat.format(dates);
            //当前时间
            Date now = dateFormat.parse(nowDate);
            try {
                //查询出审批通过的机房申请单
                ComputerRoom computerRoom = new ComputerRoom();
                computerRoom.setApplyState("1");
                List<ComputerRoom> computerRoomList = iComputerRoomService.computerRoomApplylist(computerRoom);
                String smsText = "";
                for (ComputerRoom room : computerRoomList) {
                    boolean booleans = judgmentDate(nowDate, room.getPredictOutTime());
                    System.out.println("booleans:"+booleans);
                    if (booleans){
                        //超过24小时了发送短信提醒

                        //获取陪同人员列表
                        List<String> accompanyList = new ArrayList<>();
                        accompanyList.add(room.getAccompanyUserId());
                        if(!StringUtils.isEmpty(room.getAccompanyUserIdTwo())){
                            accompanyList.add(room.getAccompanyUserIdTwo());
                        }

                        //审批通过发送短信
                        for (String id : accompanyList) {
                            //陪同人
                            OgPerson ogPerson = iOgPersonService.selectOgPersonById(id);
                            smsText = "您有机房出入登记待处理事项，机房出入编码："+room.getComputerApplyNo() + ",请登录运维管理平台及时处理。";
                            sendSms(ogPerson,smsText);
                        }
                    }
                }


            }catch (Exception e){
                logger.error("computerRoomTask", e);
            }finally {
                taskLockManager.unlock("computerRoomTask");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【sendDutyTask】执行总时长【" + (end - start) + "】毫秒");
        }else {
            logger.debug("computerRoomTask - 任务已有其他服务执行...");
        }
    }





    /**判断是否超过24小时
     *
     * @param date1
     * @param date2
     * @return boolean
     * @throws Exception
     */

    public static boolean judgmentDate(String date1, String date2) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        Date start = sdf.parse(date1);
        Date end = sdf.parse(date2);
        long cha = end.getTime() - start.getTime();
        if(cha<0){
            return true;
        }
        double result = cha * 1.0 / (1000 * 60 * 60);
        if(result<=24){
            return true;
        }else{
            return false;
        }

        //结束时间小于当前时间 为负数
//        if(result <= -24){
//            return true;
//        }
    }





        public void sendSms(OgPerson person, String smsText) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(smsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("111103");// 检查对象
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);

        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }


}
