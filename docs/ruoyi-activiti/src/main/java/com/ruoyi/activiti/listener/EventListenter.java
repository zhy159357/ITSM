package com.ruoyi.activiti.listener;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.activiti.domain.UserTaskTest;
import com.ruoyi.activiti.service.impl.EventRunServiceImpl;
import com.ruoyi.activiti.service.impl.ImBizIssuefxServiceImpl;
import com.ruoyi.activiti.service.impl.UserTaskTestServiceImpl;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 张旭
 * @title: EventListenter
 * @projectName ruoyi
 * @description: TODO
 * @date 2022/1/1014:06
 */
@Service("eventListenter")
@Transactional
public class EventListenter implements JavaDelegate, ExecutionListener {
    private EventRunServiceImpl eventService= SpringUtils.getBean(EventRunServiceImpl.class);
    private UserTaskTestServiceImpl userTaskTestService=SpringUtils.getBean(UserTaskTestServiceImpl.class);
    private String chnell="websocket.EventRun";
    @Override
    public void execute(DelegateExecution execution) {
    }

    @Override
    public void notify(DelegateExecution execution) {
    }
    public void sendMessage(DelegateExecution execution){
        DelegateExecution parent=execution.getParent();
        String eventId= (String) parent.getProcessInstanceBusinessKey();
        EventRun eventRun=eventService.selectEventRunById(eventId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("message","监控事件单任务："+eventRun.getEventNo()+" 已超时！");
        jsonObject.put("userId",eventRun.getCreateId());
        //发送上一步创建人
        eventService.sysNotifyTime(chnell,jsonObject);
        //发送审核人
        if(!eventRun.getCreateId().equals(eventRun.getHandlePerson())){
            jsonObject.put("userId",eventRun.getHandlePerson());
            eventService.sysNotifyTime(chnell,jsonObject);
        }
        UserTaskTest userTaskTest=new UserTaskTest();
        userTaskTest.setId(UUID.fastUUID().toString());
        userTaskTest.setTasktype("FmYx");
        userTaskTest.setUserid(eventRun.getHandlePerson());
        userTaskTestService.insertUserTaskTest(userTaskTest);
      //  userTaskTest.set
        //private RedisTemplate<String, String> redisTemplate;
    }
}
