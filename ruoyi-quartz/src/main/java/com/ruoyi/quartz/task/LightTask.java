package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.deal.light.impl.SanJingLight;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("lightTask")
public class LightTask {

    @Autowired
    private SanJingLight sanJingLight;

    public void syncDeviceData() {
        //同步左洞
        // syncLeftDeviceData();
        //同步右洞
        syncRightDeviceData();

        System.out.println(">>>>>>>>>>>>" + DateUtils.getTime());
    }

    public void syncLeftDeviceData() {

        // sanJingLight.login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
        String jessionId = sanJingLight.login("admin", "admin123", "http://10.7.187.6:8080");


        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("http://10.7.187.6:8080/api/getLatestDeviceData?tunnelId=1")
                .method("GET", null)
                .addHeader("Cookie", jessionId)
                .build();
        try {
            Response response = client.newCall(request).execute();
            boolean isSuccessful = response.isSuccessful();
            String responseBody = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void syncRightDeviceData() {

        // sanJingLight.login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
        String jessionId = sanJingLight.login("admin", "admin123", "http://10.7.187.6:8080");


        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("http://10.7.187.6:8080/api/getLatestDeviceData?tunnelId=2")
                .method("GET", null)
                .addHeader("Cookie", jessionId)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            if (responseBody.contains("发送成功")) {
                JSONObject jsonObject = JSONObject.parseObject(responseBody);

                System.out.println(jsonObject);

                JSONObject data = jsonObject.getJSONObject("data");

                Integer light1 = data.getInteger("light1");// 0
                Integer light2 = data.getInteger("light2");// 1
                Integer light3 = data.getInteger("light3");// 2
                Integer light4 = data.getInteger("light4");// 3
                Integer light5 = data.getInteger("light5");// 4
                Integer light6 = data.getInteger("light6");// 5
                Integer light7 = data.getInteger("light7");// 6

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
