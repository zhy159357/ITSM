package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.TaskMapper;
import com.ruoyi.activiti.service.TaskService;
import com.ruoyi.common.core.domain.entity.SmBizTask;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Component
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;


    /**
     *
     * @param id
     * @return
     */
    @Override
    public SmBizTask selectTaskById(String id) {
        return taskMapper.selectTaskById(id);
    }
    /**
     *
     * @param id
     * @return
     */
    @Override
    public SmBizTask selectSchedulingId(String id) {
        return taskMapper.selectSchedulingId(id);
    }
    /**
     *
     * @param smBizTask
     */
    @Override
    public int insertTask(SmBizTask smBizTask) {

        if(StringUtils.isNotEmpty(smBizTask.getStartTime())){
            smBizTask.setStartTime(handleTimeYYYYMMDDHHMMSS(smBizTask.getStartTime()));
        }
        return taskMapper.insertTask(smBizTask);
    }

    /**
     *
     * @param smBizTask
     */
    @Override
    public int updateTask(SmBizTask smBizTask) {
        if(StringUtils.isNotEmpty(smBizTask.getStartTime())){
            smBizTask.setStartTime(handleTimeYYYYMMDDHHMMSS(smBizTask.getStartTime()));
        }
        return taskMapper.updateTask(smBizTask);
    }

    /**
     * 根据id删除
     * @param taskId
     * @return
     */
    @Override
    public int deleteById(String taskId) {

        return taskMapper.deleteById(taskId);
    }

    @Override
    public int updateTaskByDelay(SmBizTask task) {
        return taskMapper.updateTaskByDelay(task);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss类型的时间格式化为yyyyMMddHHmmss
     * @param dateStr
     * @return
     */
    public String handleTimeYYYYMMDDHHMMSS(String dateStr){
        if(StringUtils.isEmpty(dateStr)){
            return dateStr;
        }
        Date date = DateUtils.parseDate(dateStr);
        String formatDateStr = DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, date);
        return formatDateStr;
    }
}
