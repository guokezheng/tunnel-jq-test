package com.tunnel.deal.phone.impl;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.deal.phone.PhoneSpeak;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 兴电-电话广播-协议
 */
@Component
public class XingDianPhoneSpeak implements PhoneSpeak {


    @Override
    public int playVoice(String systemUrl, Map<String, Object> map) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");

        //构建json格式参数
        JSONObject jsonParam = new JSONObject();
        jsonParam.putAll(map);
        String content = jsonParam.toString();

        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, content);

        String url = systemUrl + "/api/speak/playVoice";
        Request request = new Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .build();
        String result = null;
        int status = 0;
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotBlank(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            int code = (int) jsonObject.get("code");
            if (code == 0) {
                status = 1;
            }
        }
        return status;
    }

    @Override
    public int tempSpkGroup(String systemUrl, Map<String, Object> map) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");

        //构建json格式参数
        JSONObject jsonParam = new JSONObject();
        jsonParam.putAll(map);
        String content = jsonParam.toString();

        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, content);
        String url = systemUrl + "/api/speak/tempSpkGroup";
        Request request = new Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .build();
        String result = null;
        int status = 0;
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotBlank(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            int code = (int) jsonObject.get("code");
            if (code == 0) {
                status = 1;
            }
        }
        return status;
    }

    @Override
    public int hungUp(String systemUrl,Map<String, Object> map) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");

        //构建json格式参数
        JSONObject jsonParam = new JSONObject();
        jsonParam.putAll(map);
        String content = jsonParam.toString();

        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, content);
        String url = systemUrl + "/api/speak/hungUp";
        Request request = new Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .build();
        String result = null;
        int status = 0;
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotBlank(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            int code = (int) jsonObject.get("code");
            if (code == 0) {
                status = 1;
            }
        }
        return status;
    }
}
