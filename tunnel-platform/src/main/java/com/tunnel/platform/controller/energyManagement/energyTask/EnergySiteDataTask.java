package com.tunnel.platform.controller.energyManagement.energyTask;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.http.HttpUtils;
import com.tunnel.business.datacenter.domain.dataReport.ExternalSystemCode;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.energyManagement.EnergyConfigcenterClassification;
import com.tunnel.business.domain.energyManagement.EnergySjfx;
import com.tunnel.business.mapper.energyManagement.SdEnergyDataMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.platform.controller.energyManagement.energyTools.CompareEnergyDataListsTools;
import com.tunnel.platform.controller.energyManagement.energyTools.CompareListsTools;
import com.tunnel.platform.controller.energyManagement.energyTools.TunnelEnergyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 能源数据同步定时任务
 */
@Component("EnergySiteDataTask")
public class EnergySiteDataTask {

    @Autowired
    private IExternalSystemService externalSystemService;

    @Autowired
    private SdEnergyDataMapper sdEnergyDataMapper;



//    @Async(value = "synchronousEnergyTaskExecutor")
    @Scheduled(cron="30 5 * * * ?")
    //@Scheduled(fixedRate = 5000*60*60)
    public void synchronousEnergyDataTask() throws ParseException {
        ExternalSystem externalSystem = new ExternalSystem();
        List<EnergySjfx>energySiteList  = new ArrayList<>();
        //externalSystem.setSystemName("能源管控(能源数据同步)");
        externalSystem.setSystemCode(ExternalSystemCode.ENERGY_MANAGE.getCode());
        List<ExternalSystem> externalSystems = externalSystemService.selectExternalSystemList(externalSystem);
        if (externalSystems.isEmpty()) {
            return;
        }

        String url = externalSystems.get(0).getSystemUrl() + "login";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", externalSystems.get(0).getUsername());
        map.put("password", externalSystems.get(0).getPassword());
        String result = "";
        try {
            result = HttpUtils.sendPostByApplicationJson(url, JSONObject.toJSONString(map));
        } catch (Exception e) {
            return ;
        }
        if (result == "" || result.equals("")) {
            return ;
        }
        JSONObject json = JSONObject.parseObject(result);
        if (json == null || json.isEmpty() || json.get("token") == null) {
            return;
        }
        String token =  json.get("token").toString();
        //获取站点用能(tabType=1)/分项用能(tabType=3)/分类用能(tabType=4)
        /*http://10.168.73.54/dev-api/analysis/getElectricityReportList?codeList=000000s001637000010&baseTime=2023-07-11&type=month&tabType=1&deptCode=null*/
        url = externalSystems.get(0).getSystemUrl() + "analysis/getElectricityReportList";
        if (token == null || token.equals("")) {
            return ;
        }
        //String[] datas = {getCurrentTime("0"),getCurrentTime("1"),getCurrentTime("2")};//获取日报，月报，年报
        //String[] tabTypes = {"1","3","4"};//获取站点，分项，分类
        String tunnelId = "";
        String[] strArr = externalSystems.get(0).getTunnel().split(",");
        // 拼接成字符串
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : strArr) {
            stringBuffer.append(TunnelEnergyUtil.tunnelConvertEnergy(s)).append(",");
        }
        // 去掉最后一个逗号
        tunnelId = String.valueOf(stringBuffer.delete(stringBuffer.length()-1,stringBuffer.length()));
        /* for(int q=0;q<tabTypes.length;q++){*/
        String type,baseTime = null;
        for(int k=0;k<3;k++){
        System.out.println("k========"+k);
            if(k==0){//日报
                type = "day";
                baseTime = TunnelEnergyUtil.getCurrentTime("0");
            }else if(k==1){//月报
                type = "month";
                baseTime = TunnelEnergyUtil.getCurrentTime("1");
            }else{
                type = "year";
                baseTime = TunnelEnergyUtil.getCurrentTime("2");
            }
            String params = "codeList=" + tunnelId+"&baseTime="+baseTime+"&type="+type+"&tabType=1"+"&deptCode=null";
            json = JSONObject.parseObject(HttpUtils.sendGetWithAuth(url, params, Constants.UTF8, token));
            if (json == null || json.isEmpty() || String.valueOf(json.get("data")) == null) {
                continue ;
            }
            if("200".equals(json.getString("code"))){
                List<Map<String, Object>> mapList = (List<Map<String, Object>>)json.get("data");
                for(int p = 0; p < mapList.size(); p++){
                    List<Map<String, Object>> itemList = (List<Map<String, Object>>)mapList.get(p);
                    for(Map<String, Object> itemData : itemList){
                        // 处理JSON对象的数据
                        EnergySjfx energySite = new EnergySjfx();
                        Double value = 0.00;
                        if (itemData.get("value")!= null) {
                            value = Double.valueOf(itemData.get("value").toString());
                        }//用电量
                        String name = itemData.get("name").toString();
                        energySite.setTunnelId(TunnelEnergyUtil.EnergyConvertTunnel(itemData.get("id").toString()));//隧道id
                        energySite.setEnergyValue(value);
                        energySite.setStatisticsType(k);
                        SimpleDateFormat  sdf2= null;
                        String dateString = itemData.get("rt").toString();
                        if(k==0){
                            sdf2 = new SimpleDateFormat("yyyy-MM-dd HH");
                        }else if(k==1){
                            sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                        }else{
                            sdf2 = new SimpleDateFormat("yyyy-MM");
                        }
                        java.util.Date d1=null;
                        d1 = sdf2.parse(dateString);//将string类型转化为sql.util.Date的时间
                        java.sql.Timestamp resultDate= new java.sql.Timestamp(d1.getTime());
                        energySite.setCreateTime(resultDate);
                        energySiteList.add(energySite);
                        sdEnergyDataMapper.deleteEnergySiteData(energySite);
                    }
                }
            }
        }
        if(energySiteList.size() > 0){
            //批量存入energy_site表
            sdEnergyDataMapper.insertEnergySiteData(energySiteList);
        }
    }
}
