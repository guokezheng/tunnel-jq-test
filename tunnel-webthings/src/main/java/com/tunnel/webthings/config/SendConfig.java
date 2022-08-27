package com.tunnel.webthings.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SendConfig {


    @Bean
    public RestTemplate restTemplate() {
        // 创建 RestTemplate 实例
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory();
        okHttp3ClientHttpRequestFactory.setConnectTimeout(20 * 1000);
        okHttp3ClientHttpRequestFactory.setReadTimeout(20 * 1000);
        RestTemplate restTemplate = new RestTemplate(okHttp3ClientHttpRequestFactory);
        return restTemplate;
    }
}
