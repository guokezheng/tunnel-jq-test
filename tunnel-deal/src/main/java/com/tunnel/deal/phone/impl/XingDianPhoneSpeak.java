package com.tunnel.deal.phone.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
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
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(map));
        Request request = new Request.Builder()
                .url(systemUrl + "/api/speak/playVoice")
                .method("POST", requestBody)
                .build();

        int status = 0;
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            if (StringUtils.isNotBlank(result) && JSONValidator.from(result).validate()) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                int code = (int) jsonObject.get("code");
                if (code == 0) {
                    status = 1;
                }
            }
        } catch (IOException e) {
            return status;
        }
        return status;
    }

    @Override
    public int tempSpkGroup(String systemUrl, Map<String, Object> map) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(map));
        Request request = new Request.Builder()
                .url(systemUrl + "/api/speak/tempSpkGroup")
                .method("POST", requestBody)
                .build();

        int status = 0;
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();

            if (StringUtils.isNotBlank(result) && JSONValidator.from(result).validate()) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                int code = (int) jsonObject.get("code");
                if (code == 0) {
                    status = 1;
                }
            }
        } catch (IOException e) {
            return status;
        }
        return status;
    }

    @Override
    public int hungUp(String systemUrl,Map<String, Object> map) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, JSONObject.toJSONString(map));
        String url = systemUrl + "/api/speak/hungUp";
        Request request = new Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .build();

        int status = 0;
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            if (StringUtils.isNotBlank(result) && JSONValidator.from(result).validate()) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                int code = (int) jsonObject.get("code");
                if (code == 0) {
                    status = 1;
                }
            }
        } catch (IOException e) {
            return status;
        }

        return status;
    }
}
