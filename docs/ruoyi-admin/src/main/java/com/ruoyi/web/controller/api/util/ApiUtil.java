package com.ruoyi.web.controller.api.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ruoyi.common.core.domain.entity.ApiResData;
import com.ruoyi.web.controller.api.exception.ApiException;


public class ApiUtil {

    private static Log logger = LogFactory.getLog(ApiUtil.class);
    /**
     * 约定秘钥，最好移植配置文件中,系统初始化时调用set方法赋值。
     */
    private static String secret = "45134236-ad76-441c-9fb4-31affe7622c1a";

    /**
     * 设置秘钥
     *
     * @param secret
     */
    public static void setSecret(String secret) {
        ApiUtil.secret = secret;
    }

    /**
     * 发送post请求
     * <p>
     * url地址
     * 参数集合
     * 请求编码
     *
     * @param timeout 超时时间（秒）
     * @return byte[] byte数组
     * @throws Exception
     */
    public static ApiResData postJson(String reqUrl, Object param, int timeout)
            throws Exception {
        return postJson(secret, reqUrl, param, timeout);
    }

    /**
     * 发送post请求
     *
     * @param secret
     * @param reqUrl
     * @param timeout
     * @return
     * @throws Exception
     */
    public static ApiResData postJson(String secret, String reqUrl,
                                      Object reqData, int timeout) throws Exception {
//		System.out.println("secret的值是："+secret);
        if (reqData == null) {
            throw new RuntimeException("请求参数-reqData：不能为空");
        }
        byte[] resultBuffer = null;
        // 时间戳
        Long timestamp = new Date().getTime();
        // 请求对象转json字符串
        String paramsStr = JSONUtils.toJSONString(reqData);
//		System.out.println("去拿到的值是："+paramsStr);
        // 签名
        String signature = MD5Utils.md5(secret + timestamp + paramsStr);

        byte[] data = paramsStr.getBytes();
        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(timeout * 1000);
            conn.setReadTimeout(timeout * 1000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Timestamp", String.valueOf(timestamp));
            conn.setRequestProperty("Signature", signature);
            conn.setRequestProperty("Content-Type", " application/json");
            conn.setRequestProperty("Content-Length",
                    String.valueOf(data.length));
            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
            outStream.write(data);
            outStream.flush();
            outStream.close();
            if (conn.getResponseCode() == 200) {
                resultBuffer = readStream(conn.getInputStream());
            } else {
                throw new RuntimeException("请求失败：resCode:"
                        + conn.getResponseCode());
            }
        } finally {
            conn.disconnect();
        }
//System.out.println("new String(resultBuffer)值是："+new String(resultBuffer));
        return JSONUtils
                .parseObject(new String(resultBuffer), ApiResData.class);
    }

    /**
     * 验证并获取请求数据对象
     *
     * @param request
     * @param clazz
     * @return
     */
    public static <T> T verifyAndGetReqData(HttpServletRequest request,
                                            Class<T> clazz) {
        T data = null;
        try {
            // 签名
            String signature = request.getHeader("Signature");
            // 时间戳
            String timestamp = request.getHeader("Timestamp");
            if (signature == null || timestamp == null) {
                throw new ApiException(ApiException.PARAM_FAILD, "参数异常,"
                        + signature + "--" + timestamp);
            }
            // 请求对象json字符串
            byte[] reqByte = readStream(request.getInputStream());
            String paramsStr2 = new String((reqByte), "GBK");
            // 计算数据签名
            String dataSignature = MD5Utils.md5(secret + timestamp + paramsStr2);

            if (!dataSignature.equals(signature)) {
                throw new ApiException(ApiException.VERIFY_FAILD, "签名校验异常");
            }

            try {
                data = JSONUtils.parseObject(paramsStr2, clazz);
            } catch (Exception e) {
                throw new ApiException(ApiException.PARSE_FAILD, "请求数据转换异常", e);
            }
            return data;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("解析请求参数异常", e);
        }
    }

    /**
     * 验证并获取请求数据集合
     *
     * @param request
     * @param clazz
     * @return
     */
    public static <T> List<T> verifyAndGetReqDatas(HttpServletRequest request,
                                                   Class<T> clazz) {
        List<T> datas = null;
        try {
            // 签名
            String signature = request.getHeader("Signature");
            // 时间戳
            String timestamp = request.getHeader("Timestamp");
            if (signature == null || timestamp == null) {
                throw new ApiException(ApiException.PARAM_FAILD, "参数异常,"
                        + signature + "--" + timestamp);
            }
            // 请求对象json字符串
            byte[] reqByte = readStream(request.getInputStream());
            String paramsStr = new String(reqByte);
            // 计算数据签名
            String dataSignature = MD5Utils.md5(secret + timestamp + paramsStr);

            if (!dataSignature.equals(signature)) {
                throw new ApiException(ApiException.VERIFY_FAILD, "签名校验异常");
            }

            try {
                datas = JSONUtils.parseArray(paramsStr, clazz);
            } catch (Exception e) {
                throw new ApiException(ApiException.PARSE_FAILD, "请求数据转换异常");
            }
            return datas;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("解析请求参数异常", e);
        }
    }

    /**
     * 解析输入流
     *
     * @param inStream 输入流
     * @return byte[] byte数组
     * @throws Exception
     */
    private static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /**
     * 返回json
     *
     * @param response
     * @param obj
     */
    public static void resJSONObj(HttpServletResponse response, Object obj) {
        if (obj != null) {
            try {
                String jsonString = JSONUtils.toJSONString(obj);
                response.setCharacterEncoding("GBK");
                response.setContentType("application/json;GBK");
                response.getWriter().write(jsonString);
                response.getWriter().close();
            } catch (Exception e) {
                logger.error("返回json对象失败", e);
            }
        }
    }

    /**
     * 返回json
     *
     * @param response
     * @param obj
     */
    public static void resJSONObjUTF8(HttpServletResponse response, Object obj) {
        if (obj != null) {
            try {
                String jsonString = JSONUtils.toJSONString(obj);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;UTF-8");
                response.getWriter().write(jsonString);
                response.getWriter().close();
            } catch (Exception e) {
                logger.error("返回json对象失败", e);
            }
        }
    }
}