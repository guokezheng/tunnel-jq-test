package com.tunnel.platform.controller.bigScreenApi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.http.HttpUtils;
import com.tunnel.business.datacenter.domain.dataReport.ExternalSystemCode;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 隧道-能耗Controller
 *
 * @author tianjainwgei
 * @date 2023-1-12
 */
@RestController
@RequestMapping("/tunnelEnergyData")
public class SdTunnelEnergyController extends BaseController {

    @Autowired
    private IExternalSystemService externalSystemService;

    /**
     * 隧道用电排行
     * @param tunnelId
     * @param
     * @return
     */
    @ApiOperation("隧道用电排行")
    @PostMapping("/electricityRankingItemized")
    public AjaxResult electricityRankingItemized(String tunnelId){
        Map<String, Object> allDataList = new HashMap<>();
        ExternalSystem externalSystem = new ExternalSystem();
        externalSystem.setTunnelId(tunnelId);
        //externalSystem.setSystemName("能源管控平台");
        externalSystem.setSystemCode(ExternalSystemCode.ENERGY_MANAGE_OF_TUNNEL.getCode());
        List<ExternalSystem> externalSystems = externalSystemService.selectExternalSystemList(externalSystem);
        if (externalSystems.isEmpty()) {
            return AjaxResult.success(allDataList);
        }

        ExternalSystem system = externalSystems.get(0);
        String url = system.getSystemUrl() + "login";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", system.getUsername());
        map.put("password", system.getPassword());
        String result = "";
        try {
            result = HttpUtils.sendPostByApplicationJson(url, JSONObject.toJSONString(map));
        } catch (Exception e) {
            return AjaxResult.success(allDataList);
        }
        if (result == "" || result.equals("")) {
            return AjaxResult.success(allDataList);
        }
        JSONObject json = JSONObject.parseObject(result);
        if (json == null || json.isEmpty() || json.get("token") == null) {
            return AjaxResult.success(allDataList);
        }
        String token =  json.get("token").toString();
        //获取隧道用电排行，能耗占比
        url = system.getSystemUrl() + "cloud/api/electricityRankingItemized";

        if (token == null || token.equals("")) {
            return AjaxResult.success(allDataList);
        }
        //String params = "deptId=" + tunnelId;
        String params = "";
        json = JSONObject.parseObject(HttpUtils.sendGetWithAuth(url, params, Constants.UTF8, token));
        if (json == null || json.isEmpty() || json.getJSONArray("data") == null) {
            return AjaxResult.success(allDataList);
        }
        JSONArray data = json.getJSONArray("data");
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0;i < data.size(); i++) {
            Map<String, Object> maps = new HashMap<>();
            String value = "0";
            if (data.getJSONObject(i).get("value") != null) {
                value = data.getJSONObject(i).get("value").toString();
            }
            String name = data.getJSONObject(i).get("name").toString();
            String id = data.getJSONObject(i).get("id").toString();
            maps.put("id", id);
            maps.put("name", name);
            maps.put("value", value);
            list.add(maps);
        }
        allDataList.put("energy", list);
        return AjaxResult.success(allDataList);
    }


    /**
     * 累计光伏发电和今日光产能
     * @param tunnelId
     * @return
     */
    @ApiOperation("累计光伏发电和今日光产能")
    @PostMapping("/getEnergyData")
    public AjaxResult getEnergyData(String tunnelId){
        Map<String, Object> allDataList = new HashMap<>();
        ExternalSystem externalSystem = new ExternalSystem();
        externalSystem.setTunnelId(tunnelId);
        //externalSystem.setSystemName("能源管控平台");
        externalSystem.setSystemCode(ExternalSystemCode.ENERGY_MANAGE_OF_TUNNEL.getCode());
        List<ExternalSystem> externalSystems = externalSystemService.selectExternalSystemList(externalSystem);
        if (externalSystems.isEmpty()) {
            return AjaxResult.success(allDataList);
        }

        ExternalSystem system = externalSystems.get(0);
        String url = system.getSystemUrl() + "login";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", system.getUsername());
        map.put("password", system.getPassword());
        String result = "";
        try {
            result = HttpUtils.sendPostByApplicationJson(url, JSONObject.toJSONString(map));
        } catch (Exception e) {
            return AjaxResult.success(allDataList);
        }
        if (result == "" || result.equals("")) {
            return AjaxResult.success(allDataList);
        }
        JSONObject json = JSONObject.parseObject(result);
        if (json == null || json.isEmpty() || json.get("token") == null) {
            return AjaxResult.success(allDataList);
        }
        String token =  json.get("token").toString();
        //获取隧道用电排行，能耗占比
        url = system.getSystemUrl() + "cloud/api/getEnergyData";

        if (token == null || token.equals("")) {
            return AjaxResult.success(allDataList);
        }
        //String params = "deptId=" + tunnelId;
        String params = "";
        json = JSONObject.parseObject(HttpUtils.sendGetWithAuth(url, params, Constants.UTF8, token));
        System.out.println("json=============================="+json);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONObject data = jsonObject.getJSONObject("data");
        if(data.size()>0){
            double lightEnergy = data.getDouble("lightEnergy");
            double totalEnergy = data.getDouble("totalEnergy");
            double bill = data.getDouble("bill");
            double power = data.getDouble("power");
            double capacity = data.getDouble("capacity");
            maps.put("lightEnergy", lightEnergy);
            maps.put("totalEnergy", totalEnergy);
            maps.put("bill", bill);
            maps.put("power", power);
            maps.put("capacity", capacity);
            list.add(maps);
        }
        allDataList.put("energy", list);
        return AjaxResult.success(allDataList);
    }




}
