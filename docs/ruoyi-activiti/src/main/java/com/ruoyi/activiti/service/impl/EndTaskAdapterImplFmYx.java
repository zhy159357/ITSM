package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.adapter.AbstractEndTaskAdapter;
import com.ruoyi.activiti.constants.ProcessKeyConstants;
import com.ruoyi.activiti.domain.EndTaskVo;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.EventRunService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("endTaskAdapterImplFmYx")
public class EndTaskAdapterImplFmYx extends AbstractEndTaskAdapter {


    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private EventRunService eventRunService;

    @Override
    public TableDataInfo getEndPagerTasks(String processKey, String number) {
        List<EndTaskVo> endTaskVos = new ArrayList<>();
        EndTaskVo endTaskVo;
        List<EventRun> eventRunList=new ArrayList<>();
        //通过业务单号查询业务是否存在
        if(StringUtils.isNotBlank(number)){
            EventRun eventRun=new EventRun();
            eventRun.setEventNo(number);
            eventRunList=eventRunService.selectEventRunList(eventRun);
        }
        if(eventRunList.size()>0){
            for(EventRun eventRun:eventRunList){
                List<Map<String, Object>> maps = activitiCommService.processBusinesskeyKeyTask(eventRun.getEventId());
                if(StringUtils.isNotEmpty(maps)){
                    for (Map<String, Object> map : maps) {
                            endTaskVo  = new EndTaskVo();
                            endTaskVo.setBusinesskey(eventRun.getEventId());
                            endTaskVo.setTaskType(ProcessKeyConstants.fmyx.getInfo());
                            endTaskVo.setTaskName(map.get("taskName").toString());
                            endTaskVo.setTaskNo(eventRun.getEventNo());
                            endTaskVo.setTaskTitle(eventRun.getEventTitle());
                            Date createTime =(Date) map.get("createTime");
                            String geneterTime = DateUtils.formatDate(createTime,DateUtils.YYYY_MM_DD_HH_MM_SS);
                            endTaskVo.setTaskGeneterTime(geneterTime);
                            endTaskVos.add(endTaskVo);
                    }
                }
            }
            return getDataTable_ideal(endTaskVos);
        }

        return getDataTable_ideal(new ArrayList<>());
    }

    @Override
    public int remove(String id) {
        try{
            EventRun eventRun = new EventRun();
            eventRun.setEventId(id);
            eventRun.setStatus("4");
            //逻辑删除运行事件
            eventRunService.updateEventRun(eventRun);
            //删除任务
            activitiCommService.deleteProcess(id,"强制删除运行事件单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int close(String id) {
        try{
            EventRun eventRun = new EventRun();
            eventRun.setEventId(id);
            eventRun.setStatus("8");
            //逻辑删除运行事件
            eventRunService.updateEventRun(eventRun);
            //关闭任务
            activitiCommService.deleteProcess(id,"强制关闭运行事件单");
            return AjaxResult.Type.SUCCESS.value();
        }catch (Exception e){
            throw e;
        }
    }
}
