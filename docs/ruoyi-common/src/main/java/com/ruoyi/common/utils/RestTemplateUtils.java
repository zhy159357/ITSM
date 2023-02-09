package com.ruoyi.common.utils;

import com.ruoyi.common.utils.http.HttpClientUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import java.util.Map;

@Component
public class RestTemplateUtils {

    @Autowired
    private  RestTemplate restTemplate;

    private  HttpEntity<Map<String, String>> generatePostJson(Map<String, String> jsonMap) {

        //如果需要其它的请求头信息、都可以在这里追加
        HttpHeaders httpHeaders = new HttpHeaders();

        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");

        httpHeaders.setContentType(type);

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(jsonMap, httpHeaders);

        return httpEntity;
    }

    /**
     * post请求、请求参数为json
     *
     * @return
     */
    public String sendPost(String url, Map<String,String> jsonMap) {
        /*String uri = "http://127.0.0.1:80";

        Map<String, String> jsonMap = new HashMap<>(6);
        jsonMap.put("name", "张耀烽");
        jsonMap.put("sex", "男");*/

        ResponseEntity<String> apiResponse = restTemplate.postForEntity
                (
                        url,
                        generatePostJson(jsonMap),
                        String.class
                );
        return apiResponse.getBody();
    }

    public static RestTemplate restTemplate() {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            CloseableHttpClient httpClient = HttpClientUtils.acceptsUntrustedCertsHttpClient();
            requestFactory.setHttpClient(httpClient);
            requestFactory.setReadTimeout(40000);
            requestFactory.setConnectTimeout(40000);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }
}
