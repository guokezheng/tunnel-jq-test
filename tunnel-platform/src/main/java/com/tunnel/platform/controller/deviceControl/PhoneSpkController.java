package com.tunnel.platform.controller.deviceControl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.platform.service.deviceControl.PhoneSpkService;
import io.swagger.annotations.Api;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(tags = "电话广播")
@RestController
@RequestMapping("/phoneSpk")
public class PhoneSpkController {
    @Autowired
    private PhoneSpkService phoneSpkService;
    @Autowired
    private RedisCache redisCache;



    @PostMapping(value = "/onMessage")
    public void onMessage(@RequestBody JSONObject jsonObject) {
        phoneSpkService.onMessage(jsonObject);
    }


    public static String login(String username, String password) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", "admin");
        jsonObject.addProperty("password", "admin");

        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonObject.toString());
        String url = "http://10.7.187.6:8980/api/platform/login";
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        Response response = null;
        String result = null;
        try {
            response = client.newCall(request).execute();
            if (response.body() != null) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotBlank(result)) {
            JSONObject jo = JSONObject.parseObject(result);
            result = jo.getJSONObject("data").getString("token");
        }
        return result;
    }

    public AjaxResult getAudioFileList() {
        redisCache.getCacheObject("");
        return null;
    }





    public static void main(String[] args) {
       /* JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", "admin");
        jsonObject.addProperty("password", "admin");

        String string = jsonObject.toString();

        System.out.println(string);*/


        login("admin", "admin");

    }
}
