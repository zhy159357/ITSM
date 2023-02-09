package com.ruoyi.common.utils.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

/**
 * @description 让RestTemplate的GET请求支持RequestBody参数
 * @create 2021-04-30 09:26:15
 */

public class HttpComponentsClientRestfulHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {
    @Override
    protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {

        if (httpMethod == HttpMethod.GET) {
            return new HttpGetRequestWithEntity(uri);
        }
        return super.createHttpUriRequest(httpMethod, uri);
    }

    /**
     * 定义HttpGetRequestWithEntity实现HttpEntityEnclosingRequestBase抽象类，以支持GET请求携带body数据
     */
    private static final class HttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {
        public HttpGetRequestWithEntity(final URI uri) {
            super.setURI(uri);
        }
        @Override
        public String getMethod() {
            return HttpMethod.GET.name();

        }
    }
}
