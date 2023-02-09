package com.ruoyi.activiti.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.service.ItsmToDevopsService;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.netty.utils.Base64Encoder;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import net.sf.json.JSONException;
import net.sf.json.util.JSONStringer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItsmToDevopsServiceImpl implements ItsmToDevopsService {

    private static final Logger log = LoggerFactory.getLogger(ItsmToDevopsServiceImpl.class);

    @Value(value = "${itsm.workAutoUrl}")
    private String EnterUrl;

    public Map<String, Object> toAutoByExcl(String workId, String worktTitle, String planImplPer, String planReiwPer, String ifAuto, String planBeginTime, String planEndTime, String groupName, String fileFast) {

        Map<String, Object> map = new HashMap<>();
        JSONStringer stringer = new JSONStringer();
        stringer.object();
        if ("cancel".equals(worktTitle)) {//为撤销任务标识
            stringer.key("workId").value(workId);        //单号id
            stringer.key("cancel").value("yes");        //撤销动作标识
        } else {
            String startTime = timeToTimeMillis(planBeginTime);
            String endTime = timeToTimeMillis(planEndTime);
            stringer.key("workId").value(workId);        //单号id
            stringer.key("workTitle").value(worktTitle);         //单号主题
            stringer.key("planImplPer").value(planImplPer);       //实施人登录名
            stringer.key("planReiwPer").value(planReiwPer);                 //复核人登录名
            stringer.key("ifauto").value(ifAuto);                      //是否时间段内手工启动
            stringer.key("planBeginTime").value(startTime);         //计划开始时间
            stringer.key("planEndTime").value(endTime);             //计划结束时间
            stringer.key("groupName").value(groupName);//ITSM文件服务器中附件所在组信息
            stringer.key("fileFast").value(fileFast);   //ITSM文件服务器中附件fast加密字符串
        }
        stringer.endObject();
        String jsonStr = stringer.toString();
        Base64Encoder encode = new Base64Encoder();


        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            String encodeStr = encode.encode(jsonStr.getBytes("GBK"));
            HttpPost httpost = new HttpPost(EnterUrl + "/aoms/ItsmNet?action=ycNetStart");

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // 添加参数集合
            multipartEntityBuilder.addTextBody("param", encodeStr);
            multipartEntityBuilder.setCharset(Charset.defaultCharset());
            HttpEntity entity = multipartEntityBuilder.build();
            // 发送请求
            httpost.setEntity(entity);
            HttpResponse response = httpclient.execute(httpost);
            response.getStatusLine().getStatusCode();
            // 接口返回结果
            String returnInfo = new String(EntityUtils.toString(response.getEntity()).trim().getBytes("UTF-8"), "UTF-8");
            System.out.println("调用发送自动化excl接口返回结果信息是：" + returnInfo);

            // 将返回结果转成MAP
            map = net.sf.json.JSONObject.fromObject(returnInfo);
        } catch (Exception e) {
            log.info("发送网络自动化平台失败！");
        } finally {
            try {
                httpclient.close();
            } catch (Exception ignore) {

            }
        }
        return map;
    }

    public Map<String, String> getFinalPdfPath(String changeCode) {
        Map params = new HashMap();
        Map<String, String> map = new HashMap<>();
        try {
//            params.put("action", getNetOrdState);
            params.put("workId", changeCode);
            map = doGet01(EnterUrl + "/aoms/ItsmNet?action=getFinalPdfPath", params);

        } catch (Exception e) {
            throw new BusinessException(map.get("mes"));
        }
        return map;
    }

    public static Map<String, String> doGet01(String url, Map params) throws Exception {
        HttpPost post = new HttpPost(url);

        String jsonParams = JSON.toJSONString(params);
        log.info("传入参数是：" + jsonParams);
        StringEntity entityPost = new StringEntity(jsonParams, "GBK");
        log.info("传入参数entityPost是：" + entityPost);
        // 把参数放入请求对象
        post.setEntity(entityPost);
        // 添加请求头参数
        post.addHeader("Content-type", "application/json");
        post.addHeader("Accept", "application/json");
        CloseableHttpClient client = HttpClients.createDefault();
        // 启动执行请求，并获得返回值
        CloseableHttpResponse response = client.execute(post);
        // 得到返回的entity对象
        System.out.println(response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        // 把实体对象转换为string
        String res = EntityUtils.toString(entity, "UTF-8");
        Map<String, String> map = JSON.parseObject(res, HashMap.class);

        String state = map.get("state");
        String result = map.get("result");
        log.info("得到请求结果:result=" + result + ",state=" + state);

        return map;
    }

    public Map<String, String> startChangeTask(String serialNumber, String worktTitle, String applicantId, String planImplPer,
                                               String planReiwPer, String ifauto, String planBeginTime, String planEndTime) {

        Map<String, String> map = new HashMap<>();

        //转时间戳
        String startTime = DateUtils.timeToTimeMillis(planBeginTime);
        String endTime = DateUtils.timeToTimeMillis(planEndTime);
        // 获取accessToken
        String accessToken = getAccessToken(EnterUrl + "/aoms/ItsmNet?action=login", worktTitle, applicantId, "31", planImplPer, planReiwPer, startTime, endTime, ifauto);
        // 通知自动化平台启动
        if (StringUtils.isNotEmpty(accessToken)) {
            map = start(EnterUrl + "/aoms/ItsmNet?action=start", accessToken, serialNumber);
        }
        return map;
    }

    public static Map<String, String> start(String url, String accessToken, String serialNumber) {

        Map<String, String> map = new HashMap<>();

        String modultype = "31";

        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            // token
            BasicNameValuePair valuePair = new BasicNameValuePair("access_token", accessToken);
            paramList.add(valuePair);
            // 固定值31
            BasicNameValuePair valuePair1 = new BasicNameValuePair("modultype", modultype);
            paramList.add(valuePair1);
            // 单号
            BasicNameValuePair valuePair2 = new BasicNameValuePair("serialNumber", serialNumber);
            paramList.add(valuePair2);

            // 创建post请求，设置参数
            HttpPost post = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
            post.setEntity(formEntity);

            // 请求接口，返回执行结果
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse result = httpClient.execute(post);
            // 输出请求结果
            System.out.println(result);
            // 获取请求接口的状态码
            int statusCode = result.getStatusLine().getStatusCode();
            // 输出请求接口的状态码
            System.out.println(statusCode);
            // 接口返回结果
            String returnInfo = new String(EntityUtils.toString(result.getEntity()).trim().getBytes("UTF-8"), "UTF-8");
            System.out.println("返回结果returnInfo=" + returnInfo);
            // 将返回结果转成MAP
            map = net.sf.json.JSONObject.fromObject(returnInfo);

        } catch (Exception e) {
            map.put("state", "2");
            map.put("mes", "连接网络自动化失败");
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (Exception ignore) {

            }
        }
        return map;
    }

    //获取token
    public static String getAccessToken(String url, String serialName, String user, String modultype, String planImplPer, String
            planReiwPer, String startTime, String endTime, String ifauto) {
        String access_token = "";

        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            // itsm当前操作人的登录名称
            BasicNameValuePair valuePair = new BasicNameValuePair("user", user);
            paramList.add(valuePair);
            // 固定值31
            BasicNameValuePair valuePair1 = new BasicNameValuePair("modultype", modultype);
            paramList.add(valuePair1);
            // 计划实施人
            BasicNameValuePair valuePair2 = new BasicNameValuePair("planImplPer", planImplPer);
            paramList.add(valuePair2);
            // 计划复核人
            BasicNameValuePair valuePair3 = new BasicNameValuePair("planReiwPer", planReiwPer);
            paramList.add(valuePair3);
            // 计划开始时间
            BasicNameValuePair valuePair4 = new BasicNameValuePair("planBeginTime", startTime);
            paramList.add(valuePair4);
            // 计划结束时间
            BasicNameValuePair valuePair5 = new BasicNameValuePair("planEndTime", endTime);
            paramList.add(valuePair5);
            // 是否时间段内手工启动（1：是，0：否）
            BasicNameValuePair valuePair6 = new BasicNameValuePair("ifauto", ifauto);
            paramList.add(valuePair6);
            BasicNameValuePair valuePair7 = new BasicNameValuePair("serialName", serialName);
            paramList.add(valuePair7);

            // 创建post请求，设置参数
            HttpPost post = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
            post.setEntity(formEntity);

            // 请求接口，返回执行结果
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse result = httpClient.execute(post);
            // 输出请求结果
            System.out.println(result);
            // 获取请求接口的状态码
            int statusCode = result.getStatusLine().getStatusCode();
            // 输出请求接口的状态码
            System.out.println(statusCode);
            // 接口返回结果
            String returnInfo = new String(EntityUtils.toString(result.getEntity()).trim().getBytes("UTF-8"), "UTF-8");
            System.out.println("returnInfo=" + returnInfo);
            Map<String, Object> map = net.sf.json.JSONObject.fromObject(returnInfo);
            String state = map.get("state").toString();             // 变更发起成功或失败的原因说明
            if ("0".equals(state)) {
                access_token = map.get("access_token").toString();         // 变更发起成功或失败的原因说明
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (Exception ignore) {

            }
        }
        return access_token;
    }

    /**
     * 时间转成时间戳
     *
     * @param time
     * @return
     */
    private static String timeToTimeMillis(String time) {
        String timeMillis = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            long millils = simpleDateFormat.parse(time).getTime();
            timeMillis = Long.toString(millils);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMillis;
    }

    public static void main(String[] args) throws Exception {
//        String startTime = timeToTimeMillis("2021-03-30 05:00:00");
//        String endTime = timeToTimeMillis("2021-03-31 06:00:00");
//        toAutoByExcl2("BG20210330000009","测试发送自动化接口",startTime,endTime,"group1","M00/00/5D/FMhZB2BiuyWAVVlDAABOAB5cPcU265.xls");
//        GetNetOrdState2("BG20210330000011");
    }

    public String toPage(String workId, String worktTitle, String planImplPer,
                         String planReiwPer, String ifAuto, String planBeginTime, String planEndTime) {
        String encodeStr = editParam(workId, worktTitle, planImplPer, planReiwPer, ifAuto, planBeginTime, planEndTime);
        String url = EnterUrl + "/aoms/mainPage.do?type=itsm&action=netstart&param=" + encodeStr.replace("+", "%2B");
        return url;

    }

    public static String editParam(String workId, String worktTitle, String planImplPer,
                                   String planReiwPer, String ifAuto, String planBeginTime, String planEndTime) throws JSONException {
        planBeginTime = timeToTimeMillis(planBeginTime);   // 计划开始时间
        planEndTime = timeToTimeMillis(planEndTime);     // 计划结束时间

        // 将时间后三位截取
        planBeginTime = planBeginTime.substring(0, planBeginTime.length() - 3);
        planEndTime = planEndTime.substring(0, planEndTime.length() - 3);

        JSONStringer stringer = new JSONStringer();
        stringer.object();
        stringer.key("workSheetId").value(workId);             // 单号
        stringer.key("workSheetTitle").value(worktTitle);       // 工单主题
        stringer.key("equipGroupType").value("非核心设备");       // 风险级别（核心设备、非核心设备）
        stringer.key("ifauto").value(ifAuto);                       // 是否时间段内手工启动
        stringer.key("planImplPer").value(planImplPer);             // 计划实施人
        stringer.key("planReiwPer").value(planReiwPer);             // 计划复核人
        stringer.key("planBeginTime").value(planBeginTime);         //单号id
        stringer.key("planEndTime").value(planEndTime);             //单号id
        stringer.endObject();
        String jsonStr = stringer.toString();
        System.out.println("编排任务传入参数：" + jsonStr);
        Base64Encoder encode = new Base64Encoder();
        String s = "";
        try {
            s = encode.encode(jsonStr.getBytes("GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(s);
        return s;
    }

}
