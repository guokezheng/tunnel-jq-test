package com.tunnel.platform.controller.energyManagement.energyTask;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.http.HttpUtils;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.energyManagement.EnergySjfx;
import com.tunnel.business.mapper.energyManagement.SdEnergyDataMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.platform.controller.energyManagement.energyTools.CompareEnergyDataListsTools;
import com.tunnel.platform.controller.energyManagement.energyTools.TunnelEnergyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 能源数据同步定时任务
 */
@Component("EnergyItemDataTask")
public class EnergyItemDataTask {

    @Autowired
    private IExternalSystemService externalSystemService;

    @Autowired
    private SdEnergyDataMapper sdEnergyDataMapper;


    @Async(value = "synchronousEnergyTaskExecutor")
    @Scheduled(cron="30 5 * * * ?")
    //@Scheduled(fixedRate = 5000*60*60)
    public void synchronousEnergyDataTask() throws ParseException {
        ExternalSystem externalSystem = new ExternalSystem();
            List<EnergySjfx>energyItemList  = new ArrayList<>();
            List<EnergySjfx>energyClassifList  = new ArrayList<>();
            externalSystem.setSystemName("能源管控(能源数据同步)");
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
                String tabType = "";//获取分项，分类
                String deptCode = "";
                String codes = "";
                List<EnergySjfx> itemCodes = sdEnergyDataMapper.getEnergyItemCodes();
                String itemCode = itemCodes.stream().map(EnergySjfx::getItemizedCode).collect(Collectors.joining(","));
                List<EnergySjfx> classifCodes = sdEnergyDataMapper.getEnergyClassifCodes();
                String classifCode = classifCodes.stream().map(EnergySjfx::getClassificationCode).collect(Collectors.joining(","));
                String[] strArr = externalSystems.get(0).getTunnel().split(",");
                if(strArr.length>0){
                    for(int a=0;a<2;a++){
                        if(a==0){
                            codes = itemCode;
                            tabType ="3";
                        }else{
                            codes = classifCode;
                            tabType ="4";
                        }
                        for(int b=0;b<strArr.length;b++){
                            deptCode = TunnelEnergyUtil.tunnelConvertEnergy(strArr[b]);
                            String type,baseTime = null;
                            for(int k=0;k<3;k++){
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
                                String params = "codeList=" + codes+"&baseTime="+baseTime+"&type="+type+"&tabType="+tabType+"&deptCode="+deptCode;
                                json = JSONObject.parseObject(HttpUtils.sendGetWithAuth(url, params, Constants.UTF8, token));
                                if (json == null || json.isEmpty() || String.valueOf(json.get("data")) == null) {
                                    continue ;
                                }
                                String data = String.valueOf(json.get("data"));
                                // 使用Jackson库解析JSON数据
                                ObjectMapper objectMapper = new ObjectMapper();
                                List<Map<String, Object>> list = new ArrayList<>();
                                try {
                                    JsonNode rootNode = objectMapper.readTree(data);
                                    // 解析嵌套的JSON数组
                                    if (rootNode.isArray() && rootNode.size() > 0) {
                                        //for(int x = 0;x < rootNode.size(); x++){
                                        JsonNode innerArray = rootNode.get(0);
                                        if (innerArray.isArray() && innerArray.size() > 0) {
                                            for(int y=0;y<innerArray.size();y++ ){
                                                // 获取JSON对象
                                                JsonNode jsonObject = innerArray.get(y);
                                                if (jsonObject.isObject()) {
                                                    if(a==0){
                                                        // 处理JSON对象的数据
                                                        EnergySjfx energyItem = new EnergySjfx();

                                                        Double value = 0.00;
                                                        if (jsonObject.get("value")!= null) {
                                                            value = Double.valueOf(jsonObject.get("value").asText());
                                                        }//用电量
                                                        String name = jsonObject.get("name").asText();
                                                        energyItem.setItemizedCode(jsonObject.get("id").asText());//隧道id
                                                        energyItem.setEnergyValue(value);
                                                        energyItem.setTunnelId(TunnelEnergyUtil.EnergyConvertTunnel(deptCode));//隧道id
                                                        energyItem.setStatisticsType(k);
                                                        SimpleDateFormat  sdf2= null;
                                                        String dateString = jsonObject.get("rt").asText();
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
                                                        energyItem.setCreateTime(resultDate);
                                                        energyItemList.add(energyItem);
                                                    }else{
                                                        // 处理JSON对象的数据
                                                        EnergySjfx energyClassif = new EnergySjfx();

                                                        Double value = 0.00;
                                                        if (jsonObject.get("value")!= null) {
                                                            value = Double.valueOf(jsonObject.get("value").asText());
                                                        }//用电量
                                                        String name = jsonObject.get("name").asText();
                                                        energyClassif.setClassificationCode(jsonObject.get("id").asText());//隧道id
                                                        energyClassif.setEnergyValue(value);
                                                        energyClassif.setTunnelId(TunnelEnergyUtil.EnergyConvertTunnel(deptCode));
                                                        energyClassif.setStatisticsType(k);
                                                        SimpleDateFormat  sdf2= null;
                                                        String dateString = jsonObject.get("rt").asText();
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
                                                        energyClassif.setCreateTime(resultDate);
                                                        energyClassifList.add(energyClassif);
                                                    }

                                                }
                                            }

                                        }
                                        //}

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }




        if(energyItemList!=null&&energyItemList.size()>0){

            //先判断有没有当天的数据
            List<EnergySjfx>currentDayItemDatas = sdEnergyDataMapper.getEnergycurrentDayItemData(TunnelEnergyUtil.getCurrentTime("0"));
            //判断有没有当月的数据
            List<EnergySjfx>currentMonthItemDatas = sdEnergyDataMapper.getEnergycurrentMonthItemData(TunnelEnergyUtil.getCurrentTime("1"));
            List<EnergySjfx> energyItemDatas = sdEnergyDataMapper.getEnergyItemData(energyItemList);
            if(energyItemDatas==null||energyItemDatas.size()==0){
                //批量存入energy_site表
                sdEnergyDataMapper.insertEnergyItemData(energyItemList);
            }else{
                if(energyItemDatas.size()>0&&currentDayItemDatas.size()==0){//有当天的未添加
                    List<EnergySjfx> newEnergySite = energyItemList.stream().filter(p ->  p.getStatisticsType() != null && p.getStatisticsType()==0).collect(Collectors.toList());
                    sdEnergyDataMapper.insertEnergyItemData(newEnergySite);
                }if(energyItemDatas.size()>0&&currentMonthItemDatas.size()==0){//有当月的未添加
                    List<EnergySjfx> newEnergySite = energyItemList.stream().filter(p ->  p.getStatisticsType() != null && p.getStatisticsType()==1).collect(Collectors.toList());
                    sdEnergyDataMapper.insertEnergyItemData(newEnergySite);
                }
                List<EnergySjfx> updateEnergyItemDataList = CompareEnergyDataListsTools.getDiffSetItem(energyItemDatas,energyItemList);
                if(updateEnergyItemDataList!=null&&!updateEnergyItemDataList.isEmpty()){
                        sdEnergyDataMapper.updateEnergyItemData(updateEnergyItemDataList);
                }

            }

        }

        if(energyClassifList!=null&&energyClassifList.size()>0){
            //先判断有没有当天的数据
            List<EnergySjfx>currentDayClassifDatas = sdEnergyDataMapper.getEnergycurrentDayClassifData(TunnelEnergyUtil.getCurrentTime("0"));
            //判断有没有当月的数据
            List<EnergySjfx>currentMonthClassifDatas = sdEnergyDataMapper.getEnergycurrentMonthClassifData(TunnelEnergyUtil.getCurrentTime("1"));
            List<EnergySjfx> energyClassifDatas = sdEnergyDataMapper.getEnergyClassifData(energyClassifList);
            if(energyClassifDatas==null||energyClassifDatas.size()==0){
                //批量存入energy_site表
                sdEnergyDataMapper.insertEnergyClassifData(energyClassifList);
            }else{
                if(energyClassifDatas.size()>0&&currentDayClassifDatas.size()==0){//有当天的未添加
                    List<EnergySjfx> newEnergySite = energyClassifList.stream().filter(p ->  p.getStatisticsType() != null && p.getStatisticsType()==0).collect(Collectors.toList());
                    sdEnergyDataMapper.insertEnergyClassifData(newEnergySite);
                }if(energyClassifDatas.size()>0&&currentMonthClassifDatas.size()==0){//有当月的未添加
                    List<EnergySjfx> newEnergySite = energyClassifList.stream().filter(p ->  p.getStatisticsType() != null && p.getStatisticsType()==1).collect(Collectors.toList());
                    sdEnergyDataMapper.insertEnergyClassifData(newEnergySite);
                }
                List<EnergySjfx> updateEnergyClassifDataList = CompareEnergyDataListsTools.getDiffSetClassif(energyClassifDatas,energyClassifList);
                if(updateEnergyClassifDataList!=null&&!updateEnergyClassifDataList.isEmpty()){
                    sdEnergyDataMapper.updateEnergyClassifData(updateEnergyClassifDataList);
                }

            }

        }


    }






}
