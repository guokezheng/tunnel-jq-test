package com.tunnel.platform.controller.energyManagement.energyTask;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.energyManagement.EnergyConfigcenterClassification;
import com.tunnel.business.domain.energyManagement.EnergyConfigcenterElectricityPrice;
import com.tunnel.business.domain.energyManagement.EnergyConfigcenterItemized;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyConfigcenterClassificationMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyConfigcenterElectricityPriceMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyConfigcenterItemizedMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.platform.controller.energyManagement.energyTools.CompareListsTools;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 能源配置信息同步定时任务
 */
@Component("EnergyConfigurationTask")
public class EnergyConfigurationTask {

    @Autowired
    private IExternalSystemService externalSystemService;

    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    @Autowired
    private SdEnergyConfigcenterClassificationMapper sdEnergyConfigcenterClassificationMapper;

    @Autowired
    private SdEnergyConfigcenterElectricityPriceMapper sdEnergyConfigcenterElectricityPriceMapper;

    @Autowired
    private SdEnergyConfigcenterItemizedMapper sdEnergyConfigcenterItemizedMapper;

    @Async(value = "synchronousEnergyTaskExecutor")
    //@Scheduled(fixedRate = 5000*60*60)
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨一点执行一次
    public void synchronousEnergyConfigurationTask() throws ParseException {
        ExternalSystem externalSystem = new ExternalSystem();
        List<EnergyConfigcenterClassification>eccList  = new ArrayList<>();
        List<EnergyConfigcenterElectricityPrice> ecepList = new ArrayList<>();
        List<EnergyConfigcenterItemized> eciList = new ArrayList<>();
        List<SdTunnels> tunnelList = sdTunnelsMapper.selectAllSdTunnelsList1();
        if (tunnelList.isEmpty()) {
            return;
        } else {
            //for (int i = 0; i < tunnelList.size(); i++) {
                //externalSystem.setTunnelId(tunnelList.get(i).getTunnelId());
                externalSystem.setSystemName("能源管控(能源数据同步)");
                List<ExternalSystem> externalSystems = externalSystemService.selectExternalSystemList(externalSystem);
                if (externalSystems.isEmpty()) {
                    return;
                }
                //获取能耗数据
                String url = externalSystems.get(0).getSystemUrl() + "syncTunnel/getConfig";
                String[] tableNames={"energy_configcenter_classification","energy_configcenter_electricity_price","energy_configcenter_itemized"};
                for(int t =0;t<tableNames.length;t++){
                    List<NameValuePair> parameters = new ArrayList<>();
                    String tableName = tableNames[t];
                    parameters.add(new BasicNameValuePair("tableName", tableName));
                    JSONObject json = JSONObject.parseObject(HttpURLConnectionUtil.doPost(url, parameters));
                    if (json == null || json.isEmpty() || json.getJSONArray("data") == null) {
                        continue;
                    }
                    JSONArray data = json.getJSONArray("data");
                    for (int j = 0;j < data.size(); j++) {
                        if(t==0){//energy_configcenter_classification
                            EnergyConfigcenterClassification energyConfigcenterClassification = new EnergyConfigcenterClassification();
                            energyConfigcenterClassification.setClassificationName(data.getJSONObject(j).get("classification_name").toString());
                            energyConfigcenterClassification.setClassificationCode(data.getJSONObject(j).get("classification_code").toString());
                            energyConfigcenterClassification.setParentCode(data.getJSONObject(j).get("parent_code").toString());
                            //energyConfigcenterClassification.setType(data.getJSONObject(j).get("type").toString()==null?"":data.getJSONObject(j).get("type").toString());
                            //energyConfigcenterClassification.setRemark(data.getJSONObject(j).get("remark").toString());
                            energyConfigcenterClassification.setCreateBy("admin");
                            energyConfigcenterClassification.setCreateTime(DateUtils.getNowDate());
                            eccList.add(energyConfigcenterClassification);
                        }else if(t==1){//energy_configcenter_electricity_price
                            EnergyConfigcenterElectricityPrice energyConfigcenterElectricityPrice = new EnergyConfigcenterElectricityPrice();
                            energyConfigcenterElectricityPrice.setUnitPrice2((BigDecimal) data.getJSONObject(j).get("unit_price2"));
                            energyConfigcenterElectricityPrice.setUnitPrice3((BigDecimal) data.getJSONObject(j).get("unit_price3"));
                            energyConfigcenterElectricityPrice.setUnitPrice4((BigDecimal) data.getJSONObject(j).get("unit_price4"));
                            energyConfigcenterElectricityPrice.setUnitPrice5((BigDecimal) data.getJSONObject(j).get("unit_price5"));
                            energyConfigcenterElectricityPrice.setUnitPrice6((BigDecimal) data.getJSONObject(j).get("unit_price6"));
                            energyConfigcenterElectricityPrice.setJianCof((BigDecimal) data.getJSONObject(j).get("jian_cof"));
                            energyConfigcenterElectricityPrice.setFengCof((BigDecimal) data.getJSONObject(j).get("feng_cof"));
                            energyConfigcenterElectricityPrice.setGuCof((BigDecimal) data.getJSONObject(j).get("gu_cof"));
                            energyConfigcenterElectricityPrice.setShenCof((BigDecimal) data.getJSONObject(j).get("shen_cof"));
                            energyConfigcenterElectricityPrice.setMonth(data.getJSONObject(j).get("month").toString());
                            ecepList.add(energyConfigcenterElectricityPrice);
                        }else{
                            EnergyConfigcenterItemized energyConfigcenterItemized = new EnergyConfigcenterItemized();
                            energyConfigcenterItemized.setItemizedName(data.getJSONObject(j).get("itemized_name").toString());
                            energyConfigcenterItemized.setItemizedCode(data.getJSONObject(j).get("itemized_code").toString());
                            energyConfigcenterItemized.setParentCode(data.getJSONObject(j).get("parent_code").toString());
                            //energyConfigcenterItemized.setRemark(data.getJSONObject(j).get("remark").toString());
                            energyConfigcenterItemized.setCreateBy("admin");
                            energyConfigcenterItemized.setCreateTime(DateUtils.getNowDate());
                            eciList.add(energyConfigcenterItemized);
                        }

                    }
                }



            }
        if(eccList!=null&&eccList.size()>0){
            List<String> codeList = eccList.stream().map(EnergyConfigcenterClassification::getClassificationCode).collect(Collectors.toList());

            List<EnergyConfigcenterClassification> energyTypeDatas = sdEnergyConfigcenterClassificationMapper.getEnergyTypeData(codeList);
            List<EnergyConfigcenterClassification> insertEnergyTypeDataList = new ArrayList<>();
            List<EnergyConfigcenterClassification> updateEnergyTypeDataList = new ArrayList<>();
            if(energyTypeDatas==null||energyTypeDatas.size()==0){
                //批量存入energy_configcenter_classification表
                sdEnergyConfigcenterClassificationMapper.insertEnergyTypeData(eccList);
            }else{

                Map<String, List<EnergyConfigcenterClassification>> map = CompareListsTools.getDiffSet(energyTypeDatas,eccList);
                if(map!=null){
                    updateEnergyTypeDataList = map.get("update");
                    insertEnergyTypeDataList = map.get("insert");

                    if(updateEnergyTypeDataList!=null&&!updateEnergyTypeDataList.isEmpty()){
                        sdEnergyConfigcenterClassificationMapper.updateEnergyTypeData(updateEnergyTypeDataList);
                    }
                    if(insertEnergyTypeDataList!=null&&!insertEnergyTypeDataList.isEmpty()){
                        sdEnergyConfigcenterClassificationMapper.insertEnergyTypeData(insertEnergyTypeDataList);
                    }
                }

            }
        }
        if(ecepList!=null&&ecepList.size()>0){
            List<String> codeList = ecepList.stream().map(EnergyConfigcenterElectricityPrice::getMonth).collect(Collectors.toList());

            List<EnergyConfigcenterElectricityPrice> energyPriceDatas = sdEnergyConfigcenterElectricityPriceMapper.getEnergyPriceData(codeList);
            List<EnergyConfigcenterElectricityPrice> insertEnergyPriceDataList = new ArrayList<>();
            List<EnergyConfigcenterElectricityPrice> updateEnergyPriceDataList = new ArrayList<>();
            if(energyPriceDatas==null||energyPriceDatas.size()==0){
                //批量存入energy_configcenter_electricity_price表
                sdEnergyConfigcenterElectricityPriceMapper.insertEnergyPriceData(ecepList);
            }else{
                Map<String, List<EnergyConfigcenterElectricityPrice>> map = CompareListsTools.getDiffSetPrice(energyPriceDatas,ecepList);
                if(map!=null){
                    updateEnergyPriceDataList = map.get("update");
                    insertEnergyPriceDataList = map.get("insert");

                    if(updateEnergyPriceDataList!=null&&!updateEnergyPriceDataList.isEmpty()){
                        sdEnergyConfigcenterElectricityPriceMapper.updateEnergyPriceData(updateEnergyPriceDataList);
                    }
                    if(insertEnergyPriceDataList!=null&&!insertEnergyPriceDataList.isEmpty()){
                        sdEnergyConfigcenterElectricityPriceMapper.insertEnergyPriceData(insertEnergyPriceDataList);
                    }
                }

            }

        }
        if(eciList!=null&&eciList.size()>0){
            List<String> codeList = eciList.stream().map(EnergyConfigcenterItemized::getItemizedCode).collect(Collectors.toList());

            List<EnergyConfigcenterItemized> energyItemDatas = sdEnergyConfigcenterItemizedMapper.getEnergyItemData(codeList);
            List<EnergyConfigcenterItemized> insertEnergyItemizedList = new ArrayList<>();
            List<EnergyConfigcenterItemized> updateEnergyItemizedList = new ArrayList<>();
            if(energyItemDatas==null||energyItemDatas.size()==0){
                //批量存入energy_configcenter_itemized表
                sdEnergyConfigcenterItemizedMapper.insertEnergyItemData(eciList);
            }else{
                Map<String, List<EnergyConfigcenterItemized>> map = CompareListsTools.getDiffSetItem(energyItemDatas,eciList);
                if(map!=null){
                    updateEnergyItemizedList = map.get("update");
                    insertEnergyItemizedList = map.get("insert");

                    if(updateEnergyItemizedList!=null&&!updateEnergyItemizedList.isEmpty()){
                        sdEnergyConfigcenterItemizedMapper.updateEnergyItemizedData(updateEnergyItemizedList);
                    }
                    if(insertEnergyItemizedList!=null&&!insertEnergyItemizedList.isEmpty()){
                        sdEnergyConfigcenterItemizedMapper.insertEnergyItemData(insertEnergyItemizedList);
                    }
                }
            }

        }

        //}


    }








}