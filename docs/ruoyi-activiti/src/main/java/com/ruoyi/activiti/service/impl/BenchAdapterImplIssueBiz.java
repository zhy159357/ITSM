package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.AbstractBenchAdapter;
import com.ruoyi.activiti.domain.BenchTask;
import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IImBizIssuefxService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("benchAdapterImplIssueBiz")
public class BenchAdapterImplIssueBiz extends AbstractBenchAdapter {

    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private IImBizIssuefxService iImBizIssuefxService;

    @Override
    public boolean isMainTask() {
        return true;
    }


    @Override
    public List<String> getAuthRoles() {
        List<String> roles = new ArrayList<String>();
        roles.add("9980");
        roles.add("9982");
        roles.add("9983");
        roles.add("9984");
        roles.add("9985");
        roles.add("9986");
        roles.add("9987");
        roles.add("0206");
        roles.add("0207");
        roles.add("0208");
        return roles;
    }

    @Override
    public String getBenchType() {
        return "PMYY";
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
                    ImBizIssuefx imBizIssuefx = iImBizIssuefxService.selectImBizIssuefxById(business_key);
                    if (imBizIssuefx != null) {
                        benchTask = new BenchTask();
                        benchTask.setType("PMYY");//任务类型
                        benchTask.setTaskId(map.get("taskId").toString());//任务ID
                        benchTask.setBizId(business_key);//业务ID
                        benchTask.setNumber(imBizIssuefx.getIssuefxNo());//单号
                        benchTask.setTitle(imBizIssuefx.getIssuefxName());//标题
                        benchTask.setTaskName(map.get("taskName").toString());//任务名称
                        benchTask.setDescription(map.get("description").toString());
                        Date dt = (Date) map.get("createTime");
                        benchTask.setPrePerformTime(sdf2.format(dt));//上一步操作时间
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
