package com.tunnel.platform.controller.energyManagement.energyTask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.datacenter.domain.dataReport.ExternalSystemCode;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.energyManagement.EnergyAnalysisElectricityBill;
import com.tunnel.business.domain.energyManagement.EnergySite;
import com.tunnel.business.mapper.energyManagement.SdEnergyAnalysisElectricityBillMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.platform.controller.energyManagement.enumeration.EnergyTunnelEnum;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 获取能源分析电力账单
 * @author zhai
 * @date 2023/8/1
 */
@Component("energyAnalysisElectricityBillTask")
public class EnergyAnalysisElectricityBillTask {

    @Autowired
    private IExternalSystemService externalSystemService;

    @Autowired
    private SdEnergyAnalysisElectricityBillMapper billMapper;

    @Scheduled(cron = "0 0 1 * * ? ")
    public void syncEnergyAnalysis(){
        ExternalSystem externalSystem = new ExternalSystem();
        //externalSystem.setSystemName("能源管控(能源数据同步)");
        externalSystem.setSystemCode(ExternalSystemCode.ENERGY_MANAGE.getCode());
        List<ExternalSystem> externalSystems = externalSystemService.selectExternalSystemList(externalSystem);
        if (externalSystems.isEmpty()) {
            return;
        }
        //获取能源分析电力账单数据
        String url = externalSystems.get(0).getSystemUrl() + "syncTunnel/getData";
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("tableName", "energy_analysis_electricity_bill"));
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(DateUtils.getNowDate());
        rightNow.add(Calendar.DAY_OF_YEAR,-1);
        parameters.add(new BasicNameValuePair("startTime",DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,rightNow.getTime())));
        parameters.add(new BasicNameValuePair("endTime",DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,rightNow.getTime())));
        JSONObject json = JSONObject.parseObject(HttpURLConnectionUtil.doPost(url, parameters));
        if (json == null || json.isEmpty() || json.getJSONArray("data") == null) {
            return;
        }
        JSONArray data = json.getJSONArray("data");
        List<EnergyAnalysisElectricityBill> list = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            EnergyAnalysisElectricityBill eneData = JSONObject.parseObject(data.get(i).toString(), EnergyAnalysisElectricityBill.class);
            eneData.setDeptCode(EnergyTunnelEnum.getValue(eneData.getDeptCode()));
            list.add(eneData);
        }
        //批量插入
        billMapper.insertEnergyAnalysisElectricityBill(list);
    }
}
