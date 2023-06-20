package com.tunnel.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class SendConfig {


    @Bean("OkHttpRestTemplate")
    public RestTemplate restTemplate() {
        // 创建 RestTemplate 实例
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory();
        okHttp3ClientHttpRequestFactory.setConnectTimeout(3 * 1000);
        okHttp3ClientHttpRequestFactory.setReadTimeout(3 * 1000);
        okHttp3ClientHttpRequestFactory.setWriteTimeout(3 * 1000);
        RestTemplate restTemplate = new RestTemplate(okHttp3ClientHttpRequestFactory);
        return restTemplate;
    }

    @Bean(name="HttpTemplate")
    public RestTemplate template(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5 * 1000);
        factory.setReadTimeout(5 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return true;
            }
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
            }
        };
        restTemplate.setErrorHandler(responseErrorHandler);
        return restTemplate;
    }
}
