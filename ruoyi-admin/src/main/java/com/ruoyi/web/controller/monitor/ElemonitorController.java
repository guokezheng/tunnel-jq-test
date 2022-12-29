package com.ruoyi.web.controller.monitor;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.utils.http.HttpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ElemonitorController {
    @GetMapping("/getToken")
    public Result getToken()
    {
        String token = "";
        String url = "http://10.7.187.220:8000/ruoyi-admin/login";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "admin");
        map.put("password", "HSD123!@#");
        try {
            JSONObject json = JSONObject.parseObject(HttpUtils.sendPostByApplicationJson(url,JSONObject.toJSONString(map)));
            token =  (String)json.get("token");//获取token数据
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(token);
    }
}
