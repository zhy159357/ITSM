package com.ruoyi.quartz.util;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.domain.entity.ApiResData;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 第三方接口post和get请求
 */
public class HttpRequest {
    // 向第三方接口发送一个post 请求的参数的看具体的要求，该接口想要的数据是什么类型，如果是json，那就把参数转换为json类型，其他的转换为其它类型，如阿里的接口参数就有的不是json类型
    public static String httpPost(String url, String apiKey, String sql) throws Exception {

        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("请求参数-url不能为空");
        }
        if (StringUtils.isEmpty(apiKey)) {
            throw new RuntimeException("请求参数-apikey不能为空");
        }

        if (StringUtils.isEmpty(sql)) {
            throw new RuntimeException("请求参数-parmsSql不能为空");
        }
        String urlRes = url+"?"+"apikey="+apiKey;
        HttpClient client = new HttpClient(); // 客户端实例化
        PostMethod postMethod = new PostMethod(urlRes); // 请求方法post，可以将请求路径传入构造参数中
        byte[] responseBody = null;
        try {
            postMethod.addRequestHeader("Content-type", "application/json; charset=utf-8");
            byte[] requestBytes = sql.getBytes("utf-8"); // 将参数转为二进制流
            InputStream inputStream = new ByteArrayInputStream(requestBytes, 0,
                    requestBytes.length);
            postMethod.setRequestBody(inputStream);
            int statusCode = client.executeMethod(postMethod);  // 执行方法
            if (statusCode != HttpStatus.SC_OK) {
                throw new BusinessException("Method failed: " + postMethod.getStatusLine());
            }
            responseBody = postMethod.getResponseBody(); // 得到相应数据
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            postMethod.releaseConnection();
        }
        return new String(responseBody);

    }



    public static String httpGet(String url,String apikey,String userId) throws Exception {
        //String url = "http://20.200.47.213:7508/tenant/openapi/v2/users/get";
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("请求参数-url不能为空");
        }
        if (StringUtils.isEmpty(apikey)) {
            throw new RuntimeException("请求参数-apikey不能为空");
        }
        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("请求参数-userId不能为空");
        }
        String urls = url+"?apikey="+apikey+"&user_id="+userId;
        HttpClient client = new HttpClient(); // 客户端实例化
        GetMethod getMethod = new GetMethod(urls); // 请求方法post，可以将请求路径传入构造参数中
        byte[] responseBody = null;
        try {
            getMethod.addRequestHeader("Content-type", "application/json; charset=utf-8");
            int statusCode = client.executeMethod(getMethod);  // 执行方法
            if (statusCode != HttpStatus.SC_OK) {
                throw new BusinessException("Method failed: " + getMethod.getStatusLine());
            }
            responseBody = getMethod.getResponseBody(); // 得到相应数据
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            getMethod.releaseConnection();
        }
        return new String(responseBody);

    }



    public static void main(String[] args) throws Exception {

    }
}
