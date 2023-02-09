package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.AbstractBenchAdapter;
import com.ruoyi.activiti.domain.BenchTask;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.EventRunService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("benchAdapterImplFmYx")
public class BenchAdapterImplFmYx extends AbstractBenchAdapter {

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private EventRunService eventRunService;

    @Override
    public List<String> getAuthRoles() {
        List<String> roles = new ArrayList<String>();
        roles.add("0200");
        roles.add("0202");
        roles.add("0003");
        roles.add("00128");
        roles.add("6211");
        return roles;
    }

    @Override
    public String getBenchType() {
        return "FmYx";
    }

    @Override
    public TableDataInfo getBenchPagerTasks(String userId, String type) {
        List<BenchTask> records = new ArrayList<BenchTask>();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> reList = activitiCommService.userTaskUserId(userId, type, "");
        if (!reList.isEmpty()) {
            BenchTask benchTask = null;
            for (Map<String, Object> map : reList) {
                String business_key = map.get("businesskey").toString();
                if (StringUtils.isNotEmpty(business_key)) {
                    EventRun eventRun = eventRunService.selectEventRunById(business_key);
                    if (eventRun != null) {
                        benchTask = new BenchTask();
                        benchTask.setType("FmYx");//任务类型
                        benchTask.setTaskId(map.get("taskId").toString());//任务ID
                        benchTask.setBizId(business_key);//业务ID
                        benchTask.setNumber(eventRun.getEventNo());//单号
                        benchTask.setTitle(eventRun.getEventTitle());//标题
                        benchTask.setTaskName(map.get("taskName").toString());//任务名称
                        Date dt = (Date) map.get("createTime");
                        benchTask.setPrePerformTime(sdf2.format(dt));//上一步操作时间
                        benchTask.setCurrentState(eventRun.getStatus());
                        benchTask.setDescription(map.get("description")==null?"":map.get("description").toString());
                        records.add(benchTask);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(records)) {
            Collections.sort(records, new Comparator<BenchTask>() {
                @Override
                public int compare(BenchTask o1, BenchTask o2) {
                    String createTime1 = o1.getPrePerformTime();
                    String createTime2 = o2.getPrePerformTime();
                    if (createTime1.compareTo(createTime2) > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }
        return getDataTable_ideal(records);
    }
}
