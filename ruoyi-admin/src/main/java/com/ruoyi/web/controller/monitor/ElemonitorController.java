package com.ruoyi.web.controller.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.service.ISElemonitorService;
import com.tunnel.business.datacenter.domain.dataReport.ExternalSystemCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ElemonitorController {

    @Autowired
    private ISElemonitorService elemonitorService;

    @GetMapping("/getToken")
    public Result<String> getToken()
    {
        String token = "";
        String url = "http://10.7.187.220:8000/ruoyi-admin/login";
        Map<String, Object> map = new HashMap<>();
        map.put("username", "admin");
        map.put("password", "HSD123!@#");
        String result = "";
        try {
            result = HttpUtils.sendPostByApplicationJson(url, JSON.toJSONString(map));
        } catch (Exception e) {
            return null;
        }
        if ("".equals(result)) {
            return null;
        }
        JSONObject json = JSON.parseObject(result);
        if (json == null || json.isEmpty()) {
            return null;
        }
        token =  json.get("token").toString();//获取token数据
        return Result.success(token);
    }


    @GetMapping("/getThreeUrl")
    public Result getThreeUrl()
    {
        String tunnelId ="JQ-WeiFang-JiuLongYu-HSD";
        //String sysName = "能源管控平台";
        String sysName = ExternalSystemCode.ENERGY_MANAGE_OF_TUNNEL.getCode();
        List<Map> externalSystem=elemonitorService.getExternalSystemEnergy(tunnelId,sysName);
        return Result.success(externalSystem);
    }
}
