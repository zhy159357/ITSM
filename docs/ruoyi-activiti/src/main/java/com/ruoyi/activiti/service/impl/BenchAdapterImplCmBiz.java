package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.AbstractBenchAdapter;
import com.ruoyi.activiti.domain.BenchTask;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.ICmBizSingleService;
import com.ruoyi.common.core.domain.entity.CmBizSingle;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("benchAdapterImplCmBiz")
public class BenchAdapterImplCmBiz extends AbstractBenchAdapter {

    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private ICmBizSingleService cmBizSingleService;

    @Override
    public boolean isMainTask() {
        return true;
    }


    @Override
    public List<String> getAuthRoles() {
        List<String> roles = new ArrayList<String>();
        roles.add("2100");
        roles.add("2101");
        roles.add("2102");
        roles.add("2103");
        roles.add("2109");
        return roles;
    }

    @Override
    public String getBenchType() {
        return "CmZy";
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
                    CmBizSingle cmBizSingle = cmBizSingleService.selectCmBizSingleById(business_key);
                    if (cmBizSingle != null) {
                        benchTask = new BenchTask();
                        benchTask.setType("CmZy");//????????????
                        benchTask.setTaskId(map.get("taskId").toString());//??????ID
                        benchTask.setBizId(business_key);//??????ID
                        benchTask.setNumber(cmBizSingle.getChangeCode());//??????
                        benchTask.setTitle(cmBizSingle.getChangeSingleName());//??????
                        benchTask.setTaskName(map.get("taskName").toString());//????????????
                        Date dt = (Date) map.get("createTime");
                        benchTask.setPrePerformTime(sdf2.format(dt));//?????????????????????
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
