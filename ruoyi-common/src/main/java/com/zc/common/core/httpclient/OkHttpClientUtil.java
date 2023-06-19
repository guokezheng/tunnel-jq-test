package com.zc.common.core.httpclient;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * describe: OkHttpClient工具类  单实例复用
 *  当你创建一个单独的{@code OkHttpClient}实例，并重用它所有的HTTP调用，OkHttp性能最好。
 *  这是因为每个客户端都拥有自己的连接池和线程池。重用连接和线程可以减少延迟并节省内存。
 *  相反，为每个请求创建一个客户端都在空闲池中浪费资源。
 *
 * @author zs
 * @date 2023/6/19
 */
public class OkHttpClientUtil {

    /**
     * 默认超时时间，单位毫秒
     */
    private static int timeOut = 3000;

    public static OkHttpClient client;

   static  {
         client = new OkHttpClient().newBuilder()
                .connectTimeout(timeOut,TimeUnit.MILLISECONDS)
                .readTimeout(timeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(timeOut,TimeUnit.MILLISECONDS)
                .build();
    }


}
