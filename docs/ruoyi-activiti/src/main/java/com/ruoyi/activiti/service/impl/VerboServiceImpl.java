package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.service.IVerboService;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.StringUtils;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * 告警实现类
 */
@Service
public class VerboServiceImpl implements IVerboService {
    private static final Logger logger = LoggerFactory.getLogger(VerboServiceImpl.class);
    /**
     * 告警等级列表
     * @return
     */
    @Override
    public List selectSeverity() {
        Object obj = CacheUtils.get("RealTimeAlarmDataSeverity");
        JSONArray jsons = null;
        if(StringUtils.isNotNull(obj)){
            jsons = JSONArray.fromObject(obj);
        }
        return jsons;
    }

    /**
     * 告警状态
     * @return
     */
    @Override
    public List selectStatus() {
        Object obj = CacheUtils.get("RealTimeAlarmDataStatus");
        JSONArray jsons = null;
        if(StringUtils.isNotNull(obj)){
            jsons = JSONArray.fromObject(obj);
        }
        return jsons;
    }
    /**
     * 重要指标
     * @return
     */
    @Override
    public List targetList() {
        logger.debug("------------------------------请求重要系统看板");
        //Object a = CacheUtils.get("targetImportant");
       // JSONArray jsons = JSONArray.fromObject(a);
        Map<String,String> map = new HashMap<>();
        map.put("pageSize","30");
        map.put("pageNum","0");
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
        rows.put("update_time","2021-06-30?19:01:00");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", 1);
        jsonObject.put("rows", rows);
        jsonObject.put("code", 0);
        jsonObject.put("msg", "操作成功");
        List list = new ArrayList();
        list.add(jsonObject);
        return list;
    }
}