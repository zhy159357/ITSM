package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.AbstractBenchAdapter;
import com.ruoyi.activiti.domain.BenchTask;
import com.ruoyi.activiti.domain.FmSawo;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IFmSawoService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("benchAdapterImplFmSawo")
public class BenchAdapterImplFmSawo extends AbstractBenchAdapter {

    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private IFmSawoService fmSawoService;

    @Override
    public List<String> getAuthRoles() {
        List<String> roles = new ArrayList<String>();
        roles.add("666669");
        roles.add("00128");
        roles.add("666670");
        roles.add("0103");
        return roles;
    }

    @Override
    public String getBenchType() {
        return "FMSAWO";
    }

    @Override
    public TableDataInfo getBenchPagerTasks(String userId, String type) {
        List<BenchTask> records = new ArrayList<BenchTask>();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> reList = activitiCommService.userTaskUserId(userId, type, "");
        List<Map<String, Object>> maps = activitiCommService.groupTasks(type, "");
        List<Map<String, Object>> list = new ArrayList();
        list.addAll(reList);
        list.addAll(maps);
        if (!list.isEmpty()) {
            BenchTask benchTask = null;
            for (Map<String, Object> map : list) {
                String business_key = map.get("businesskey").toString();
                if (StringUtils.isNotEmpty(business_key)) {
                    FmSawo fmSawo = fmSawoService.selectFmSawoById(business_key);
                    if (fmSawo != null) {
                        benchTask = new BenchTask();
                        benchTask.setType("FMSAWO");//任务类型
                        benchTask.setTaskId(map.get("taskId").toString());//任务ID
                        benchTask.setBizId(business_key);//业务ID
                        benchTask.setNumber(fmSawo.getOrdNo());//单号
                        benchTask.setTitle(fmSawo.getGaaName());//标题
                        benchTask.setTaskName(map.get("taskName").toString());//任务名称
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
