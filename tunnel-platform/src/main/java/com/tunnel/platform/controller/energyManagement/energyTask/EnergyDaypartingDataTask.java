package com.tunnel.platform.controller.energyManagement.energyTask;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.http.HttpUtils;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.energyManagement.EnergyDayparting;
import com.tunnel.business.mapper.energyManagement.SdEnergyDataMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.platform.controller.energyManagement.energyTools.CompareEnergyDataListsTools;
import com.tunnel.platform.controller.energyManagement.energyTools.TunnelEnergyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 能源数据同步定时任务(分时段)
 */
@Component("EnergyDaypartingDataTask")
public class EnergyDaypartingDataTask {

    @Autowired
    private IExternalSystemService externalSystemService;

    @Autowired
    private SdEnergyDataMapper sdEnergyDataMapper;


    @Async(value = "synchronousEnergyTaskExecutor")
    @Scheduled(cron="30 5 * * * ?")
    //@Scheduled(fixedRate = 5000*60*60)
    public void synchronousEnergyDataTask() throws ParseException {
        ExternalSystem externalSystem = new ExternalSystem();
            List<EnergyDayparting>energyDaypartingList  = new ArrayList<>();
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
                url = externalSystems.get(0).getSystemUrl() + "EnergyAnalyze/getSplitTimeByDept";
                if (token == null || token.equals("")) {
                    return ;
                }
                //String[] datas = {getCurrentTime("0"),getCurrentTime("1"),getCurrentTime("2")};//获取日报，月报，年报
                //String[] tabTypes = {"1","3","4"};//获取站点，分项，分类
                String tunnelId = "";
                String type,baseTime = null;
                String[] strArr = externalSystems.get(0).getTunnel().split(",");
                if(strArr.length>0){
                    for(int b=0;b<strArr.length;b++) {
                        tunnelId = TunnelEnergyUtil.tunnelConvertEnergy(strArr[b]);
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
                            String params = "deptCodeList=" + tunnelId+"&baseTime="+baseTime+"&type="+type;
                            json = JSONObject.parseObject(HttpUtils.sendGetWithAuth(url, params, Constants.UTF8, token));
                            if (json == null || json.isEmpty() || String.valueOf(json.get("data")) == null) {
                                continue ;
                            }
                            JSONArray data = json.getJSONArray("data");
                            for (int j = 0;j < data.size(); j++) {
                                EnergyDayparting energyDayparting = new EnergyDayparting();
                                energyDayparting.setTunnelId(TunnelEnergyUtil.EnergyConvertTunnel(tunnelId));//隧道id
                                double fValue = 0.00;
                                double pValue = 0.00;
                                double jValue = 0.00;
                                double sValue = 0.00;
                                double gValue = 0.00;
                                if(data.getJSONObject(j).get("fValue").toString()!=null){
                                    fValue = Double.parseDouble(data.getJSONObject(j).get("fValue").toString());
                                }
                                energyDayparting.setfValue(fValue);
                                if(data.getJSONObject(j).get("pValue").toString()!=null){
                                    pValue = Double.parseDouble(data.getJSONObject(j).get("pValue").toString());
                                }
                                energyDayparting.setpValue(pValue);
                                if(data.getJSONObject(j).get("jValue").toString()!=null){
                                    jValue = Double.parseDouble(data.getJSONObject(j).get("jValue").toString());
                                }
                                energyDayparting.setjValue(jValue);
                                if(data.getJSONObject(j).get("sValue").toString()!=null){
                                    sValue = Double.parseDouble(data.getJSONObject(j).get("sValue").toString());
                                }
                                energyDayparting.setsValue(sValue);
                                if(data.getJSONObject(j).get("gValue").toString()!=null){
                                    gValue = Double.parseDouble(data.getJSONObject(j).get("gValue").toString());
                                }
                                energyDayparting.setgValue(gValue);
                                energyDayparting.setStatisticsType(k);//统计类型: 0时 1日 2月 3年
                                SimpleDateFormat  sdf2= null;
                                String dateString = baseTime;
                                if(k==0){
                                    sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                                }else if(k==1){
                                    sdf2 = new SimpleDateFormat("yyyy-MM");
                                }else{
                                    sdf2 = new SimpleDateFormat("yyyy");
                                }

                                java.util.Date d1=null;
                                d1 = sdf2.parse(dateString);//将string类型转化为sql.util.Date的时间
                                java.sql.Timestamp resultDate= new java.sql.Timestamp(d1.getTime());
                                energyDayparting.setCreateTime(resultDate);
                                energyDaypartingList.add(energyDayparting);
                            }

                        }
                    }

                }

            if(energyDaypartingList!=null&&energyDaypartingList.size()>0){
                //先判断有没有当天的数据
                List<EnergyDayparting>currentDayDaypartingDatas = sdEnergyDataMapper.getEnergycurrentDayDaypartingData(TunnelEnergyUtil.getCurrentTime("0"));
                //判断有没有当月的数据
                List<EnergyDayparting>currentMonthDaypartingDatas = sdEnergyDataMapper.getEnergycurrentMonthDaypartingData(TunnelEnergyUtil.getCurrentTime("1"));
                List<EnergyDayparting> energyDaypartingDatas = sdEnergyDataMapper.getEnergyDaypartingData(energyDaypartingList);
                if(energyDaypartingDatas==null||energyDaypartingDatas.size()==0){
                    //批量存入energy_site表
                    sdEnergyDataMapper.insertEnergyDaypartingData(energyDaypartingList);
                }else{

                    if(energyDaypartingDatas.size()>0&&currentDayDaypartingDatas.size()==0){//有当天的未添加
                        List<EnergyDayparting> newEnergyDayparting = energyDaypartingList.stream().filter(p ->  p.getStatisticsType() != null && p.getStatisticsType()==0).collect(Collectors.toList());
                        sdEnergyDataMapper.insertEnergyDaypartingData(newEnergyDayparting);
                    }if(energyDaypartingDatas.size()>0&&currentMonthDaypartingDatas.size()==0){//有当月的未添加
                        List<EnergyDayparting> newEnergyDayparting = energyDaypartingList.stream().filter(p ->  p.getStatisticsType() != null && p.getStatisticsType()==1).collect(Collectors.toList());
                        sdEnergyDataMapper.insertEnergyDaypartingData(newEnergyDayparting);
                    }

                    List<EnergyDayparting> updateEnergyDayparting = CompareEnergyDataListsTools.getDiffSetDayparting(energyDaypartingDatas,energyDaypartingList);
                    if(updateEnergyDayparting!=null&&!updateEnergyDayparting.isEmpty()){
                        sdEnergyDataMapper.updateEnergyDayparting(updateEnergyDayparting);
                    }
                }

            }



    }






}
