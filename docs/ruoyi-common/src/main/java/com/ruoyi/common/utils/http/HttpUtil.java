package com.ruoyi.common.utils.http;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * http请求工具类
 *
 * @since 2022-07-12
 */
public class HttpUtil {
    /**
     * get请求
     *
     * @param url
     * @param params 请求参数
     * @return
     */
    public static String get(String url, MultiValueMap<String, String> params) {
        return get(url, params, null);
    }

    /**
     * get请求
     *
     * @param url
     * @param params  请求参数
     * @param headers 请求头
     * @return
     */
    public static String get(String url, MultiValueMap<String, String> params, Map<String, String> headers) {
        return request(url, params, headers, HttpMethod.GET);
    }

//    /**
//     * post请求
//     *
//     * @param url
//     * @param params 请求参数
//     * @return
//     */
//    public static String post(String url, MultiValueMap<String, String> params, HttpMethod method) {
//        return post(url, params, method);
//    }

    /**
     * post请求
     *
     * @param url
     * @param params  请求参数
     * @param headers 请求头
     * @return
     */
    public static String send(String url, Object params, Map<String, String> headers, HttpMethod method) {
//        if(!CollectionUtils.isEmpty(headers)){
//            LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//            headers.forEach((s, s2) -> {
//
//            });
//        }
        return request(url, params, headers, method);
    }

    /**
     * put请求
     *
     * @param url
     * @param params 请求参数
     * @return
     */
    public static String put(String url, MultiValueMap<String, String> params) {
        return put(url, params, null);
    }

    /**
     * put请求
     *
     * @param url
     * @param params  请求参数
     * @param headers 请求头
     * @return
     */
    public static String put(String url, MultiValueMap<String, String> params, Map<String, String> headers) {
        return request(url, params, headers, HttpMethod.PUT);
    }

    /**
     * delete请求
     *
     * @param url
     * @param params 请求参数
     * @return
     */
    public static String delete(String url, Map<String, String> params) {
        return delete(url, params, null, HttpMethod.DELETE);
    }

    /**
     * delete请求
     *
     * @param url
     * @param params  请求参数
     * @param headers 请求头
     * @return
     */
    public static String delete(String url, Map<String, String> params, Map<String, String> headers, HttpMethod method) {
        return request(url, params, headers, method);
    }

    /**
     * 表单请求
     *
     * @param url
     * @param params  请求参数
     * @param headers 请求头
     * @param method  请求方式
     * @return
     */
    public static String request(String url, Object params, Map<String, String> headers, HttpMethod method) {
        return request(url, params, headers, method, MediaType.APPLICATION_JSON_UTF8);
    }

    /**
     * http请求
     *
     * @param url
     * @param params    请求参数 body 使用json
     * @param headers   请求头
     * @param method    请求方式
     * @param mediaType 参数类型
     * @return
     */
    public static String request(String url, Object params, Map<String, String> headers, HttpMethod method, MediaType mediaType) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }
        RestTemplate restTemplate = new RestTemplate();
        //解决乱码
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setRequestFactory(new HttpComponentsClientRestfulHttpRequestFactory());
        // header
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }
        // 提交方式：表单、json
        httpHeaders.setContentType(mediaType);
        HttpEntity<Object> httpEntity = new HttpEntity(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, method, httpEntity, String.class);
        return response.getBody();
    }


}
