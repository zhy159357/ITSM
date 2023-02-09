package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.adapter.AbstractBenchAdapter;
import com.ruoyi.activiti.domain.BenchTask;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;


@Service("benchAdapterImplFmBiz")
public class BenchAdapterImplFmBiz extends AbstractBenchAdapter {

    private static final Logger log = LoggerFactory.getLogger(BenchAdapterImplFmBiz.class);

    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private IFmBizService fmBizService;

    @Override
    public boolean isMainTask() {
        return true;
    }

    @Override
    public List<String> getAuthRoles() {
        List<String> roles = new ArrayList<String>();
        roles.add("0300");
        roles.add("0301");
        roles.add("0302");
        roles.add("0303");
        roles.add("0304");
        roles.add("0305");
        roles.add("0306");
        roles.add("0307");
        return roles;
    }

    @Override
    public String getBenchType() {
        return "FmBiz";
    }

    @Override
    public TableDataInfo getBenchPagerTasks(String userId, String type) {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<BenchTask> records = new ArrayList<>();//最终待办对象存入的集合
        List<String> idList = new ArrayList<>();//取出所有的businesskey
        List<Map<String, Object>> mapList = new ArrayList<>();//取出map里的值放到集合
        List<Map<String, Object>> reList = activitiCommService.userTaskUserId(userId, type, "");
        List<Map<String, Object>> regList = activitiCommService.groupTasks(type, "");
        if (!reList.isEmpty()) {
            reList.addAll(regList);
        } else {
            reList = regList;
        }
        long s2 = System.currentTimeMillis();
        if (!reList.isEmpty()) {
            for (Map<String, Object> map : reList) {
                if (!StringUtils.isEmpty(map.get("businesskey"))) {
                    String business_key = map.get("businesskey").toString();
                    if (StringUtils.isNotEmpty(business_key)) {
                        idList.add(business_key);
                        Map<String, Object> noMap = new HashMap<>();
                        noMap.put("businesskey", business_key);
                        noMap.put("taskId", map.get("taskId"));
                        noMap.put("taskName", map.get("taskName"));
                        noMap.put("createTime", map.get("createTime"));
                        mapList.add(noMap);
                    }
                }
            }
            if (!idList.isEmpty()) {
                List<FmBiz> fbList = fmBizService.getFmBizByAll(idList);
                if (!fbList.isEmpty()) {
                    for (FmBiz fb : fbList) {
                        for (Map<String, Object> map : mapList) {
                            if (fb.getFmId().equals(map.get("businesskey"))) {
                                BenchTask benchTask = new BenchTask();
                                benchTask.setType("FmBiz");//任务类型
                                benchTask.setTaskId(map.get("taskId").toString());//任务ID
                                benchTask.setBizId(map.get("businesskey").toString());//业务ID
                                benchTask.setNumber(fb.getFaultNo());//单号
                                benchTask.setTitle(fb.getOgSys().getCaption() + "|" + fb.getFaultDecriptSummary());//标题
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
        long s3 = System.currentTimeMillis() - s2;
        log.info("运维事件单去获取存在业务数据和非转数据变更单耗时：" + s3);
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
