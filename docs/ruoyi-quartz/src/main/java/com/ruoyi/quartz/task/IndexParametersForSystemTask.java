package com.ruoyi.quartz.task;

import com.ruoyi.activiti.utils.RestTemplateUtil;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.data.JSONObject;
import com.ruoyi.common.utils.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("IndexParametersForSystem")
public class IndexParametersForSystemTask {
    private static final Logger logger = LoggerFactory.getLogger(IndexParametersForSystemTask.class);

    @Autowired
    private TaskLockManager taskLockManager;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    //重要系统指标参数地址
    @Value("${IndexParametersForSystem.url}")
    private String url;

    //对接监控告警定时器  30s 调用一次
    public void saveRedisIndexParameters() {
        if (taskLockManager.lock("saveRedisIndexParameters")) {
            long start = System.currentTimeMillis();
            try{
                Map<String,String> map = new HashMap<>();
                map.put("pageSize","30");
                map.put("pageNum","0");
                logger.debug("--------定时任务【saveRedisIndexParameters】----------");
                //String responseResult = restTemplateUtil.sendPost(url, map);

                Map<String,Object> data1 = new HashMap<>();
                data1.put("code", "双中心总系统成功率");
                data1.put("value", "98%");
                data1.put("content", "登陆交易3分钟交易成功率");

                Map<String,Object> data2 = new HashMap<>();
                data2.put("code", "亦庄中心系统成功率");
                data2.put("value", "8");
                data2.put("content", "失败笔数");

                Map<String,Object> data3 = new HashMap<>();
                data3.put("code", "丰台中心系统成功率");
                data3.put("value", "8");
                data3.put("content", "失败笔数");

                List<Map<String, Object>> data = new ArrayList<>();
                data.add(data1);
                data.add(data2);
                data.add(data3);

                Map<String,Object> rows = new HashMap<>();
                rows.put("id","99700040000");
                rows.put("sys_num","99700040000");
                rows.put("sys_name","TEST");
                rows.put("send_time","202106301902");
                rows.put("priority",0);
                rows.put("data", data);
                rows.put("update_time","2021-06-30 19:01:00");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("total", 1);
                jsonObject.put("rows", rows);
                jsonObject.put("code", 0);
                jsonObject.put("msg", "操作成功");

                //CacheUtils.remove("IndexParametersForSystem");
                CacheUtils.put("IndexParametersForSystem", jsonObject);
                logger.debug("--------定时任务【saveRedisIndexParameters】----------" + CacheUtils.get("IndexParametersForSystem"));
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                taskLockManager.unlock("saveRedisIndexParameters");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【saveRedisIndexParameters】执行总时长【"+(end-start)+"】毫秒");
        } else {
            logger.debug("saveRedisIndexParameters - 任务已有其他服务执行...");
        }
    }
}
