package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.AbstractBenchAdapter;
import com.ruoyi.activiti.domain.BenchTask;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.common.core.domain.entity.VmBizInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("benchAdapterImplVmBizInfo")
public class BenchAdapterImplVmBizInfo extends AbstractBenchAdapter {
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Override
    public boolean isMainTask() {
        return true;
    }


    @Override
    public List<String> getAuthRoles() {
        List<String> roles = new ArrayList<String>();
        roles.add("2300");
        roles.add("2301");
        roles.add("2302");
        roles.add("2303");
        roles.add("2304");
        roles.add("2305");
        roles.add("2306");
        roles.add("2307");
        roles.add("2308");
        roles.add("2309");
        roles.add("2310");
        return roles;
    }

    @Override
    public String getBenchType() {
        return "VmBn";
    }

    @Override
    public TableDataInfo getBenchPagerTasks(String userId, String type) {
        List<BenchTask> records = new ArrayList<BenchTask>();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> reList = activitiCommService.userTaskUserId(userId, type, "");
        if (!reList.isEmpty()) {
            BenchTask benchTask = null;
            for (Map<String, Object> map : reList) {
                if (map.containsKey("businesskey")) {
                    if (map.get("businesskey") != null) {
                        String business_key = map.get("businesskey").toString();
                        if (StringUtils.isNotEmpty(business_key)) {
                            VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoByQuartz(business_key);
                            if (vmBizInfo != null) {
                                benchTask = new BenchTask();
                                benchTask.setType("VmBn");//任务类型
                                benchTask.setTaskId(map.get("taskId").toString());//任务ID
                                benchTask.setBizId(business_key);//业务ID
                                benchTask.setNumber(vmBizInfo.getVersionInfoNo());//单号
                                benchTask.setTitle(vmBizInfo.getVersionInfoName());//标题
                                benchTask.setTaskName(map.get("taskName").toString());//任务名称
                                Date dt = (Date) map.get("createTime");
                                benchTask.setPrePerformTime(sdf2.format(dt));//上一步操作时间
                                records.add(benchTask);
                            }
                        }
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
