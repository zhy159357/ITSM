package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.domain.VerbVo;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.JsonUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.quartz.util.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("RealTimeAlarmDatas")
public class RealTimeAlarmDataTask {
    private static final Logger logger = LoggerFactory.getLogger(RealTimeAlarmDataTask.class);

    @Autowired
    private TaskLockManager taskLockManager;
    //查询实时告警数据
    @Value("${RealTimeAlarmData.url}")
    private String url;

    //查询实时告警数据apiKey
    @Value("${RealTimeAlarmData.apiKey}")
    private String apiKey;
    @Value("${RealTimeAlarmData.parmSql}")
    private String parmSql;
    @Value("${RealTimeAlarmData.nameUrl}")
    private String nameUrl;

   // @Value("{RealTimeAlarmData.targetUrl}")
    private String targetUrl;

    //"http://20.200.47.213:7508/udap/openapi/v1/portal/dquery/query/sql?apikey=9cc4871e46094635a19d26557f9bb7f4"

    /**
     * 查询实时告警数据  60s 调用一次
     */
    //@Scheduled(cron = "0/5 * * * * ?")
    public void queryRealTimeAlarmData() {
        if (taskLockManager.lock("queryRealTimeAlarmData")) {
            long start = System.currentTimeMillis();
            try {
                logger.debug("--------定时任务【queryRealTimeAlarmData】----------");
                String responseResult = HttpRequest.httpPost(url, apiKey, parmSql);
                List<VerbVo> verbVoList = jsonDateSeverity(responseResult, nameUrl, apiKey);
                Map<String, List<VerbVo>> mapSeverity = SeverityDate(verbVoList);
                List<Map<String, List<VerbVo>>> jsonDateSeverity = new ArrayList();
                mapSeverity.entrySet().stream().forEach(maps -> {
                    Map maplist = new HashMap();
                    maplist.put("severity", maps.getKey());
                    maplist.put("total", maps.getValue().size());
                    maplist.put("data", maps.getValue());
                    jsonDateSeverity.add(maplist);
                });
                Map<String, List<VerbVo>> mapStatus = StatusDate(verbVoList);
                List<Map<String, List<VerbVo>>> jsonDataStatus = new ArrayList();
                mapStatus.entrySet().stream().forEach(maps -> {
                    Map maplist = new HashMap();
                    maplist.put("status", maps.getKey());
                    maplist.put("total", maps.getValue().size());
                    maplist.put("data", maps.getValue());
                    jsonDataStatus.add(maplist);
                });
                String dataSeverity = JSON.toJSONString(jsonDateSeverity);
                String dataStatus = JSON.toJSONString(jsonDataStatus);
                CacheUtils.remove("RealTimeAlarmDataSeverity");
                CacheUtils.remove("RealTimeAlarmDataStatus");
                CacheUtils.put("RealTimeAlarmDataSeverity", dataSeverity);
                CacheUtils.put("RealTimeAlarmDataStatus", dataStatus);
                logger.debug("--------定时任务【queryRealTimeAlarmData】----------" + CacheUtils.get("RealTimeAlarmData"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                taskLockManager.unlock("queryRealTimeAlarmData");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【queryRealTimeAlarmData】执行总时长【" + (end - start) + "】毫秒");
        } else {
            logger.debug("queryRealTimeAlarmData - 任务已有其他服务执行...");
        }
    }

    /**
     * 重要指标定时任务
     */
   //@Scheduled(cron = "0/5 * * * * ?")
    public void targetImportant() {
        if (taskLockManager.lock("targetImportant")) {
            long start = System.currentTimeMillis();
            try {
                logger.debug("--------定时任务【targetImportant】重要指标----------");
                String pageSize = "30";
                String pageNum = "0";
                //String responseResult = HttpRequest.httpGet(targetUrl,pageSize,pageNum);
                String data = JSON.toJSONString(json);
                CacheUtils.remove("targetImportant");
                CacheUtils.put("targetImportant", data);
                logger.debug("--------定时任务【targetImportant】----------" + CacheUtils.get("targetImportant"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                taskLockManager.unlock("targetImportant");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【targetImportant】执行总时长【" + (end - start) + "】毫秒");
        } else {
            logger.debug("targetImportant - 任务已有其他服务执行...");
        }
    }


    /**
     * 根据状态处理获取的数据
     *
     * @param responseResult
     * @return
     */
    public static List<VerbVo> jsonDateSeverity(String responseResult, String userNameUrl, String apiKey) throws Exception {
        String str = JsonUtil.jsonStr(responseResult, "data").toString();
        List<VerbVo> list = JSONObject.parseArray(str, VerbVo.class);
        list.stream().forEach(p -> {
            String userName = "";
            try {
                if (StringUtils.isNotEmpty(p.getOwner())) {
                    String nameJson = HttpRequest.httpGet(userNameUrl, apiKey, p.getOwner());
                    userName = JsonUtil.jsonStr(nameJson, "account").toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.setUserName(userName);
        });
        return list;
    }

    /**
     * 告警等级
     *
     * @param verbVoList
     * @return
     */
    public static Map<String, List<VerbVo>> SeverityDate(List<VerbVo> verbVoList) {
        Map<String, List<VerbVo>> collect = verbVoList.stream().filter(lists -> (lists.getStatus().equals("0") || lists.getStatus().equals("40") || lists.equals("150"))).
                collect(Collectors.groupingBy(p -> p.getSeverity(), Collectors.toList()));
        return collect;
    }

    /**
     * 告警状态
     *
     * @param verbVoList
     * @return
     */
    public static Map<String, List<VerbVo>> StatusDate(List<VerbVo> verbVoList) {
        Map<String, List<VerbVo>> collect = verbVoList.stream().filter(lists -> (lists.getStatus().equals("0") || lists.getStatus().equals("40") || lists.equals("150")))
                .collect(Collectors.groupingBy(p -> p.getStatus(), Collectors.toList()));
        return collect;
    }

    static String json = "{\n" +
            "    \"total\": 4,\n" +
            "    \"rows\": [\n" +
            "        {\n" +
            "            \"id\": \"99700040001\",\n" +
            "            \"sys_num\": \"99700040000\",\n" +
            "            \"sys_name\": \"TEST1\",\n" +
            "            \"send_time\": \"202106301902\",\n" +
            "            \"priority\": 0,\n" +
            "            \"data\": [\n" +
            "                {\n" +
            "                    \"code\": \"双中心总系统成功率\",\n" +
            "                    \"value\": \"98%\",\n" +
            "                    \"content\": \"登陆交易3分钟交易成功率\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"code\": \"亦庄中心系统成功率\",\n" +
            "                    \"value\": \"8\",\n" +
            "                    \"content\": \"失败笔数\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"code\": \"丰台中心系统成功率\",\n" +
            "                    \"value\": \"8\",\n" +
            "                    \"content\": \"失败笔数\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"update_time\": \"2021-06-30 19:01:00\"\n" +
            "        },\n" +
            "\t\t{\n" +
            "            \"id\": \"99700040002\",\n" +
            "            \"sys_num\": \"99700040002\",\n" +
            "            \"sys_name\": \"TEST2\",\n" +
            "            \"send_time\": \"202106301902\",\n" +
            "            \"priority\": 0,\n" +
            "            \"data\": [\n" +
            "                {\n" +
            "                    \"code\": \"双中心总系统成功率\",\n" +
            "                    \"value\": \"98%\",\n" +
            "                    \"content\": \"登陆交易3分钟交易成功率\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"code\": \"亦庄中心系统成功率\",\n" +
            "                    \"value\": \"8\",\n" +
            "                    \"content\": \"失败笔数\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"code\": \"丰台中心系统成功率\",\n" +
            "                    \"value\": \"8\",\n" +
            "                    \"content\": \"失败笔数\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"update_time\": \"2021-06-30 19:01:00\"\n" +
            "        },\n" +
            "\t\t{\n" +
            "            \"id\": \"99700040003\",\n" +
            "            \"sys_num\": \"99700040003\",\n" +
            "            \"sys_name\": \"TEST3\",\n" +
            "            \"send_time\": \"202106301902\",\n" +
            "            \"priority\": 0,\n" +
            "            \"data\": [\n" +
            "                {\n" +
            "                    \"code\": \"双中心总系统成功率\",\n" +
            "                    \"value\": \"98%\",\n" +
            "                    \"content\": \"登陆交易3分钟交易成功率\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"code\": \"亦庄中心系统成功率\",\n" +
            "                    \"value\": \"8\",\n" +
            "                    \"content\": \"失败笔数\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"code\": \"丰台中心系统成功率\",\n" +
            "                    \"value\": \"8\",\n" +
            "                    \"content\": \"失败笔数\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"update_time\": \"2021-06-30 19:01:00\"\n" +
            "        },\n" +
            "\t\t{\n" +
            "            \"id\": \"99700040004\",\n" +
            "            \"sys_num\": \"99700040004\",\n" +
            "            \"sys_name\": \"TEST4\",\n" +
            "            \"send_time\": \"202106301902\",\n" +
            "            \"priority\": 0,\n" +
            "            \"data\": [\n" +
            "                {\n" +
            "                    \"code\": \"双中心总系统成功率\",\n" +
            "                    \"value\": \"98%\",\n" +
            "                    \"content\": \"登陆交易3分钟交易成功率\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"code\": \"亦庄中心系统成功率\",\n" +
            "                    \"value\": \"8\",\n" +
            "                    \"content\": \"失败笔数\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"code\": \"丰台中心系统成功率\",\n" +
            "                    \"value\": \"8\",\n" +
            "                    \"content\": \"失败笔数\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"update_time\": \"2021-06-30 19:01:00\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"code\": 0,\n" +
            "    \"msg\": \"操作成功\"\n" +
            "}";


    public static void main(String[] args) {


    }
}
















