package com.tunnel.business.service.dataInfo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.dataReport.DeviceType;
import com.tunnel.business.datacenter.domain.dataReport.ExternalSystemCode;
import com.tunnel.business.datacenter.domain.enumeration.DevicesBrandEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.*;
import com.tunnel.business.mapper.dataInfo.*;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 设备实时数据（存储模拟量）Service业务层处理
 *
 * @author ruoyi
 * @date 2022-09-13
 */
@Service
public class SdDeviceDataServiceImpl implements ISdDeviceDataService {
    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;
    @Autowired
    private SdDevicesMapper sdDevicesMapper;
    @Autowired
    private IExternalSystemService externalSystemService;
    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    @Autowired
    private SdDeviceDataRecordMapper sdDeviceDataRecordMapper;

    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;

    /**
     * 查询设备实时数据（存储模拟量）
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 设备实时数据（存储模拟量）
     */
    @Override
    public SdDeviceData selectSdDeviceDataById(Long id) {
        return sdDeviceDataMapper.selectSdDeviceDataById(id);
    }

    /**
     * 查询设备实时数据（存储模拟量）列表
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 设备实时数据（存储模拟量）
     */
    @Override
    public List<SdDeviceData> selectSdDeviceDataList(SdDeviceData sdDeviceData) {
        return sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
    }

    /**
     * 新增设备实时数据（存储模拟量）
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 结果
     */
    @Override
    public int insertSdDeviceData(SdDeviceData sdDeviceData) {
        sdDeviceData.setCreateTime(DateUtils.getNowDate());
        return sdDeviceDataMapper.insertSdDeviceData(sdDeviceData);
    }

    /**
     * 修改设备实时数据（存储模拟量）
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 结果
     */
    @Override
    public int updateSdDeviceData(SdDeviceData sdDeviceData) {
        sdDeviceData.setUpdateTime(DateUtils.getNowDate());
        SdDevices devices = new SdDevices();
        devices.setEqId(sdDeviceData.getDeviceId());
        devices.setEqStatus("1");
        sdDevicesMapper.updateSdDevices(devices);
        return sdDeviceDataMapper.updateSdDeviceData(sdDeviceData);
    }

    /**
     * 批量删除设备实时数据（存储模拟量）
     *
     * @param ids 需要删除的设备实时数据（存储模拟量）主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceDataByIds(Long[] ids) {
        return sdDeviceDataMapper.deleteSdDeviceDataByIds(ids);
    }

    /**
     * 删除设备实时数据（存储模拟量）信息
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceDataById(Long id) {
        return sdDeviceDataMapper.deleteSdDeviceDataById(id);
    }

    /**
     * 根据隧道id查询当前设备的监测状态、实时数据或状态
     *
     * @param tunnelId 隧道id
     * @return
     */
    @Override
    public List<Map<String, Object>> getDeviceDataByTunnelId(String tunnelId) {
        return sdDeviceDataMapper.getDeviceDataByTunnelId(tunnelId);
    }

    @Override
    public Map<String, Object> getTodayCOVIData(String deviceId) {
        Long itemId = Long.valueOf(DevicesTypeItemEnum.CO.getCode());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today = simpleDateFormat.format(new Date());
        SdDeviceData data = new SdDeviceData();
        data.setDeviceId(deviceId);
        data.setItemId(Long.valueOf(itemId));
        Map<String, Object> map = new HashMap<String, Object>();
        SdDeviceData devData = sdDeviceDataMapper.selectLastRecord(data);
        if (devData != null && devData.getUpdateTime() != null) {
            today = simpleDateFormat.format(devData.getUpdateTime());
        } else if (devData != null && devData.getCreateTime() != null) {
            today = simpleDateFormat.format(devData.getCreateTime());
        }
        if (devData != null) {
            map.put("COnowData", devData.getData());
        } else {
            map.put("COnowData", null);
        }
        SdDeviceTypeItem sdDeviceTypeItem = sdDeviceTypeItemService.selectSdDeviceTypeItemById(itemId);
        if(sdDeviceTypeItem != null){
            map.put("COUnit", sdDeviceTypeItem.getUnit());
        }

        List<Map<String, Double>> todayCOData = sdDeviceDataMapper.getTodayData(deviceId, itemId, today);
        itemId = Long.valueOf(DevicesTypeItemEnum.VI.getCode());
        data.setItemId(Long.valueOf(itemId));
        devData = sdDeviceDataMapper.selectLastRecord(data);
        if (devData != null && devData.getUpdateTime() != null) {
            today = simpleDateFormat.format(devData.getUpdateTime());
        } else if (devData != null && devData.getCreateTime() != null) {
            today = simpleDateFormat.format(devData.getCreateTime());
        }
        if (devData != null) {
            map.put("VInowData", devData.getData());
        } else {
            map.put("VInowData", null);
        }

        sdDeviceTypeItem = sdDeviceTypeItemService.selectSdDeviceTypeItemById(itemId);
        if(sdDeviceTypeItem != null){
            map.put("VIUnit", sdDeviceTypeItem.getUnit());
        }

        List<Map<String, Double>> todayVIData = sdDeviceDataMapper.getTodayData(deviceId, itemId, today);
        map.put("todayCOData", todayCOData);
        map.put("todayVIData", todayVIData);
        return map;
    }

    @Override
    public Map<String, Object> getTodayFSFXData(String deviceId) {
        Long itemId = Long.valueOf(DevicesTypeItemEnum.FENG_SU.getCode());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today = simpleDateFormat.format(new Date());
        SdDeviceData data = new SdDeviceData();
        data.setDeviceId(deviceId);
        data.setItemId(Long.valueOf(itemId));
        SdDeviceData devData = sdDeviceDataMapper.selectLastRecord(data);
        if (devData != null && devData.getUpdateTime() != null) {
            today = simpleDateFormat.format(devData.getUpdateTime());
        } else if (devData != null && devData.getCreateTime() != null) {
            today = simpleDateFormat.format(devData.getCreateTime());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (devData != null) {
            map.put("nowData", devData.getData());
        } else {
            map.put("nowData", null);
        }
        List<Map<String, Double>> todayFSData = sdDeviceDataMapper.getTodayData(deviceId, itemId, today);
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);
        itemId = Long.valueOf(DevicesTypeItemEnum.FENG_XIANG.getCode());
        sdDeviceData.setItemId(itemId);
        SdDeviceData deviceData = sdDeviceDataMapper.selectLastRecord(sdDeviceData);
        map.put("todayFSData", todayFSData);
        if (deviceData != null) {
            map.put("windDirection", deviceData.getData());
        } else {
            map.put("windDirection", null);
        }
        return map;
    }

    @Override
    public Map<String, Object> getTodayLDData(String deviceId) {
        SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(deviceId);
        Long ldInsideTypeCode = DevicesTypeEnum.LIANG_DU_JIAN_CE_INSIDE.getCode();
        Long ldOutsideTypeCode = DevicesTypeEnum.LIANG_DU_JIAN_CE.getCode();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Double>> todayLDData = null;
        Map<String, Object> map = new HashMap<String, Object>();
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);
        if (sdDevices != null && sdDevices.getEqType().longValue() == ldInsideTypeCode.longValue()) {
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.LIANG_DU_INSIDE.getCode()));
            SdDeviceData deviceData = sdDeviceDataMapper.selectLastRecord(sdDeviceData);
            String today = "";
            if (deviceData != null && deviceData.getUpdateTime() != null) {
                today = simpleDateFormat.format(deviceData.getUpdateTime());
            } else if (deviceData != null && deviceData.getCreateTime() != null) {
                today = simpleDateFormat.format(deviceData.getCreateTime());
            } else if (deviceData == null) {
                today = simpleDateFormat.format(new Date());
            }
            todayLDData = sdDeviceDataMapper.getTodayData(deviceId, Long.valueOf(DevicesTypeItemEnum.LIANG_DU_INSIDE.getCode()), today);
            map.put("todayLDInsideData", todayLDData);
            if (deviceData != null) {
                map.put("nowData", deviceData.getData());
            } else {
                map.put("nowData", null);
            }
        } else if (sdDevices != null && sdDevices.getEqType().longValue() == ldOutsideTypeCode.longValue()) {
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.LIANG_DU_OUTSIDE.getCode()));
            SdDeviceData deviceData = sdDeviceDataMapper.selectLastRecord(sdDeviceData);
            String today = "";
            if (deviceData != null && deviceData.getUpdateTime() != null) {
                today = simpleDateFormat.format(deviceData.getUpdateTime());
            } else if (deviceData != null && deviceData.getCreateTime() != null) {
                today = simpleDateFormat.format(deviceData.getCreateTime());
            } else if (deviceData == null) {
                today = simpleDateFormat.format(new Date());
            }
            todayLDData = sdDeviceDataMapper.getTodayData(deviceId, Long.valueOf(DevicesTypeItemEnum.LIANG_DU_OUTSIDE.getCode()), today);
            map.put("todayLDOutsideData", todayLDData);
            double num = 0;
            int count = 5;
            for(int i = 0; i < todayLDData.size(); i++){
                if(i <= 12){
                    num = num + 100 - (i*count);
                }else {
                    num = num - 100 + (i*count);
                }
                todayLDData.get(i).put("ctCount",num);
            }
            if (deviceData != null) {
                map.put("nowData", deviceData.getData());
            } else {
                map.put("nowData", null);
            }
        }
        return map;
    }

    @Override
    public List<Map<String, String>> dataLogInfoLineList(SdDeviceData sdDeviceData) {
        List<Map<String, String>> maps = new ArrayList<>();
        String searchValue = null;
        if (sdDeviceData.getSearchValue() != null && !sdDeviceData.getSearchValue().equals("")) {
            searchValue = sdDeviceData.getSearchValue();
        }
        String deviceId = "";
        if (sdDeviceData.getDeviceId() != null && !sdDeviceData.getDeviceId().equals("")) {
            deviceId = sdDeviceData.getDeviceId();
        }
        String beginTime = getCurrentDayTime().get(0);
        String endTime = getCurrentDayTime().get(1);
        if (!sdDeviceData.getParams().isEmpty()) {
            beginTime = sdDeviceData.getParams().get("beginTime").toString();
            endTime = sdDeviceData.getParams().get("endTime").toString();
        }
        if (searchValue.equals(DeviceType.COVIITEM.getCode())) {//covi
            maps = sdDeviceDataMapper.selectCOVIDataLineList(beginTime, endTime, deviceId);
            return maps;
        } else if (searchValue.equals(DeviceType.FENGSHUFENGXIANGITEM.getCode())) {//风速风向
            maps = sdDeviceDataMapper.selectFSFXDataLineList(beginTime, endTime, deviceId);
            return maps;
        } else if (searchValue.equals(DeviceType.DONGNEILIANGDUITEM.getCode())) {//洞内亮度
            maps = sdDeviceDataMapper.selectDNDataLineList(beginTime, endTime, deviceId);
            return maps;
        } else if (searchValue.equals(DeviceType.DONGFANGDUITEM.getCode())) {//风机内外振动仪检测器
            maps = sdDeviceDataMapper.selectDWDataLineList(beginTime, endTime, deviceId);
            return maps;
        }else if (searchValue.equals(DeviceType.DONGWATERGDUITEM.getCode())) {//水浸传感器
            maps = sdDeviceDataMapper.selectDWDataLineList(beginTime, endTime, deviceId);
            return maps;
        } else if (searchValue.equals(DeviceType.DONGHUMIDGDUITEM.getCode())) {//温湿度
            maps = sdDeviceDataMapper.selectDWDataLineList(beginTime, endTime, deviceId);
            return maps;
        }else if (searchValue.equals(DeviceType.DONGCATGDUITEM.getCode())) {//微波车辆检测器
            maps = sdDeviceDataMapper.selectDWDataLineList(beginTime, endTime, deviceId);
            return maps;
        }else {
            return null;
        }

    }

    /**
     * 查询设备列表
     * @param sdDeviceData
     * @return
     */
    @Override
    public List<Map<String, String>> dataDevicesLogInfoList(SdDeviceData sdDeviceData) {
        deviceDataTab(sdDeviceData);
        List<Map<String, String>> maps = sdDeviceDataMapper.dataDevicesLogInfoList(sdDeviceData);
        return maps;
    }


    @Override
    public List<SdDeviceData> exportDatainforTab(SdDeviceData sdDeviceData) {
        deviceDataTab(sdDeviceData);
        List<SdDeviceData> list = sdDeviceDataMapper.exportDatainforTab(sdDeviceData);
        return list;
    }


    private SdDeviceData deviceDataTab(SdDeviceData sdDeviceData){
        if(sdDeviceData!=null&&!"".equals(sdDeviceData)){
            if (DeviceType.COVIITEM.getCode().equals(sdDeviceData.getSearchValue())) {//covi
                sdDeviceData.setSearchValue(DeviceType.COVI.getCode());
            }else if (DeviceType.FENGSHUFENGXIANGITEM.getCode().equals(sdDeviceData.getSearchValue())) {//风速风向
                sdDeviceData.setSearchValue(DeviceType.FENGSHUFENGXIANG.getCode());
            }else if (DeviceType.DONGNEILIANGDUITEM.getCode().equals(sdDeviceData.getSearchValue())) {//洞内亮度
                sdDeviceData.setSearchValue(DeviceType.DONGNEILIANGDU.getCode());
            }else if (DeviceType.DONGWAILIANGDUITEM.getCode().equals(sdDeviceData.getSearchValue())) {//洞外亮度
                sdDeviceData.setSearchValue(DeviceType.DONGWAILIANGDU.getCode());
            }else if (DeviceType.DONGFANGDUITEM.getCode().equals(sdDeviceData.getSearchValue())) {//风机内外振动仪检测器
                sdDeviceData.setSearchValue(DeviceType.DONGFANGDU.getCode());
            }else if (DeviceType.DONGWATERGDUITEM.getCode().equals(sdDeviceData.getSearchValue())) {//水浸传感器
                sdDeviceData.setSearchValue(DeviceType.DONGWATERGDU.getCode());
            }else if (DeviceType.DONGHUMIDGDUITEM.getCode().equals(sdDeviceData.getSearchValue())) {//温湿度传感器
                sdDeviceData.setSearchValue(DeviceType.DONGHUMIDGDU.getCode());
            }else if (DeviceType.DONGCATGDUITEM.getCode().equals(sdDeviceData.getSearchValue())) {//微波车检
                sdDeviceData.setSearchValue(DeviceType.DONGCATGDU.getCode());
            }
        }
        if (sdDeviceData.getDeptId() == null) {
            sdDeviceData.setDept(SecurityUtils.getDeptId());
        }
        return sdDeviceData;

    };




    @Override
    public List<SdDeviceCOVIData> handleExportRecord(SdDeviceCOVIData sdDeviceCOVIData) {
        getSelectExportParams(sdDeviceCOVIData);
        List<SdDeviceCOVIData> list = sdDeviceDataMapper.selectCOVIExportDataList(sdDeviceCOVIData.getBeginTime(), sdDeviceCOVIData.getEndTime(), sdDeviceCOVIData.getDeviceId(),sdDeviceCOVIData.getIds());
        return list;
    }

    @Override
    public List<SdDeviceFSFXData> handleFSFXExportRecord( SdDeviceCOVIData sdDeviceCOVIData) {
        getSelectExportParams(sdDeviceCOVIData);
        List<SdDeviceFSFXData> list = sdDeviceDataMapper.selectFSFXExportDataList(sdDeviceCOVIData.getBeginTime(), sdDeviceCOVIData.getEndTime(), sdDeviceCOVIData.getDeviceId(),sdDeviceCOVIData.getIds());
        return list;
    }

    @Override
    public List<SdDeviceDNData> handleDNExportRecord(SdDeviceCOVIData sdDeviceCOVIData) {
        getSelectExportParams(sdDeviceCOVIData);
        List<SdDeviceDNData> list = sdDeviceDataMapper.selectDNExportDataList(sdDeviceCOVIData.getBeginTime(), sdDeviceCOVIData.getEndTime(), sdDeviceCOVIData.getDeviceId(),sdDeviceCOVIData.getIds());
        return list;
    }

    @Override
    public List<SdDeviceDWData> handleDWExportRecord(SdDeviceCOVIData sdDeviceCOVIData) {
        getSelectExportParams(sdDeviceCOVIData);
        List<SdDeviceDWData> list = sdDeviceDataMapper.selectDWExportDataList(sdDeviceCOVIData.getBeginTime(), sdDeviceCOVIData.getEndTime(), sdDeviceCOVIData.getDeviceId(),sdDeviceCOVIData.getIds());
        return list;
    }


    /**
     * 工作台能耗图表
     * @param tunnelId
     * @return
     */
    @Override
    public Map<String, Object> energyConsumptionDetection(String tunnelId) {
        Map<String, Object> allDataList = new HashMap<>();
        ExternalSystem externalSystem = new ExternalSystem();
        externalSystem.setTunnelId(tunnelId);
        externalSystem.setBrandId(DevicesBrandEnum.SHAN_DONG_ZHENG_CHEN.getCode());
        externalSystem.setSystemCode(ExternalSystemCode.ENERGY_MANAGE_OF_TUNNEL.getCode());
        List<ExternalSystem> externalSystems = externalSystemService.queryExternalSystemList(externalSystem);
        if (externalSystems.isEmpty()) {
            return setAllData();
        }
        ExternalSystem system = externalSystems.get(0);
        SdDevices sdDevices = new SdDevices();
        sdDevices.setExternalSystemId(system.getId());
        List<SdDevices> sdDevicesList = sdDevicesMapper.selectSdDevicesList(sdDevices);
        if (sdDevicesList.isEmpty()) {
            return setAllData();
        }
        SdDevices devices = sdDevicesList.get(0);
        String eqId = devices.getExternalDeviceId();
        String url = system.getSystemUrl() + "login";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", system.getUsername());
        map.put("password", system.getPassword());
        String result = "";
        try {
            result = HttpUtils.sendPostByApplicationJson(url, JSONObject.toJSONString(map));
        } catch (Exception e) {
            return setAllData();
        }
        if (result == "" || result.equals("")) {
            return setAllData();
        }
        JSONObject json = JSONObject.parseObject(result);
        if (json == null || json.isEmpty() || json.get("token") == null) {
            return setAllData();
        }
        String token =  json.get("token").toString();
        //获取能耗数据
        url = system.getSystemUrl() + "sjfx/getEnergyByStatisticsType";
        String[] str = new String[]{"day","month","year"};
        for (int j = 0;j < str.length;j++) {
            if (token == null || token.equals("")) {
                break;
            }
            String type = str[j];
            String params = "powerCode=" + eqId + "&type="+type;
            json = JSONObject.parseObject(HttpUtils.sendGetWithAuth(url, params, Constants.UTF8, token));
            if (json == null || json.isEmpty() || json.getJSONArray("data") == null) {
                continue;
            }
            JSONArray data = json.getJSONArray("data");
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0;i < data.size(); i++) {
                Map<String, Object> maps = new HashMap<>();
                String value = "0";
                if (data.getJSONObject(i).get("value") != null) {
                    value = data.getJSONObject(i).get("value").toString();
                }
                String rt = data.getJSONObject(i).get("rt").toString();
                maps.put("value", value);
                maps.put("rt", rt);
                list.add(maps);
            }
            allDataList.put(type, list);
        }

        return allDataList;
    }

    public Map<String, Object> setAllData(){
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> dayList = new ArrayList<>();
        List<Map<String, Object>> monthList = new ArrayList<>();
        List<Map<String, Object>> yearList = new ArrayList<>();
        for(int i = 0; i < 24; i++){
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("value", "0");
            if(i < 10){
                mapData.put("rt", "2023-07-13 0" + i);
            }else {
                mapData.put("rt", "2023-07-13 " + i);
            }
            dayList.add(mapData);
        }
        for(int i = 0; i < 31; i++){
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("value", "0");
            if(i < 9){
                int count = i+1;
                mapData.put("rt", "2023-07-0" + count);
            }else {
                int count = i+1;
                mapData.put("rt", "2023-07-" + count);
            }
            monthList.add(mapData);
        }
        for(int i = 0; i < 12; i++){
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("value", "0");
            if(i < 9){
                int count = i+1;
                mapData.put("rt", "2023-0" + count);
            }else {
                int count = i+1;
                mapData.put("rt", "2023-" + count);
            }
            yearList.add(mapData);
        }
        map.put("day",dayList);
        map.put("month",monthList);
        map.put("year",yearList);
        return map;
    }

    @Override
    public Map<String, Object> getTodayYcylData(String deviceId) {
        Long itemId = Long.valueOf(DevicesTypeItemEnum.YUAN_CHUAN_YA_LI_ZHI.getCode());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today = simpleDateFormat.format(new Date());
        SdDeviceData data = new SdDeviceData();
        data.setDeviceId(deviceId);
        data.setItemId(Long.valueOf(itemId));
        SdDeviceData devData = sdDeviceDataMapper.selectLastRecord(data);
        if (devData != null && devData.getUpdateTime() != null) {
            today = simpleDateFormat.format(devData.getUpdateTime());
        } else if (devData != null && devData.getCreateTime() != null) {
            today = simpleDateFormat.format(devData.getCreateTime());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (devData != null) {
            map.put("nowData", devData.getData());
        } else {
            map.put("nowData", null);
        }
        List<Map<String, Double>> todayYcylData = sdDeviceDataMapper.getTodayData(deviceId, itemId, today);
        map.put("todayYcylData", todayYcylData);
        return map;
    }



    @Override
    public AjaxResult getFanSafeData(String deviceId) {
        SdDevices sdDevices = sdDevicesMapper.selectZdyDevice(deviceId, DevicesTypeEnum.NEI_WAI_ZHEN_DONG_YI_JIANCEQI.getCode());
        //振动速度值
        List<SdDeviceData> suDuList = getDeviceDataList(Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_SU_DU.getCode()), sdDevices.getEqId());
        //振动幅度值
        List<SdDeviceData> fuDuList = getDeviceDataList(Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_FU_DU.getCode()), sdDevices.getEqId());
        //沉降值
        List<SdDeviceData> chenJiangList = getDeviceDataList(Long.valueOf(DevicesTypeItemEnum.CHEN_JIANG_ZHI.getCode()), sdDevices.getEqId());
        //倾斜值
        List<SdDeviceData> qingXieList = getDeviceDataList(Long.valueOf(DevicesTypeItemEnum.QING_XIE_ZHI.getCode()), sdDevices.getEqId());
        //振动告警
        List<SdDeviceData> zhenGaoJingList = getDeviceDataList(Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_GAO_JING.getCode()), sdDevices.getEqId());
        //沉降倾斜告警
        List<SdDeviceData> chenQingGaoJingList = getDeviceDataList(Long.valueOf(DevicesTypeItemEnum.CHEN_JIANG_QING_XIE_GAO_JING.getCode()), sdDevices.getEqId());
        //风机告警
        List<SdDeviceData> fengJiGaoJingList = getDeviceDataList(Long.valueOf(DevicesTypeItemEnum.FENG_JI_GAO_JING.getCode()), sdDevices.getEqId());
        //封装到mqp
        Map<String, String> map = new HashMap<>();
        map.put("shakeSpeed",suDuList.size() == 0 ? "" : suDuList.get(0).getData());
        map.put("amplitude",fuDuList.size() == 0 ? "" : fuDuList.get(0).getData());
        map.put("subside",chenJiangList.size() == 0 ? "" : chenJiangList.get(0).getData());
        map.put("slope",qingXieList.size() == 0 ? "" : qingXieList.get(0).getData());
        map.put("shakeAlaram",zhenGaoJingList.size() == 0 ? "" : zhenGaoJingList.get(0).getData());
        map.put("subsideSlopeAlaram",chenQingGaoJingList.size() == 0 ? "" : chenQingGaoJingList.get(0).getData());
        map.put("alaram",fengJiGaoJingList.size() == 0 ? "" : fengJiGaoJingList.get(0).getData());
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getLevelData(String deviceId) {
        List<SdDeviceData> deviceDataList = getDeviceDataList(Long.valueOf(DevicesTypeItemEnum.SHUI_JIN_LEVEL.getCode()), deviceId);
        //封装到mqp
        Map<String, String> map = new HashMap<>();
        map.put("level",deviceDataList.size() == 0 ? "" : deviceDataList.get(0).getData());
        return AjaxResult.success(map);
    }

    @Override
    public List<Map<String, String>> dataLogInfoList(SdDeviceData sdDeviceData) {
        String deviceId = sdDeviceData.getDeviceId();

        String beginTime = "";
        String endTime = "";
        if (!sdDeviceData.getParams().isEmpty()) {
            beginTime = sdDeviceData.getParams().get("beginTime").toString();
            endTime = sdDeviceData.getParams().get("endTime").toString();
        }

        if (sdDeviceData.getSearchValue().equals(DeviceType.COVIITEM.getCode())) {//covi
            List<Map<String, String>> maps = sdDeviceDataMapper.selectCOVIDataList(beginTime, endTime, deviceId);
            return maps;
        } else if (sdDeviceData.getSearchValue().equals(DeviceType.FENGSHUFENGXIANGITEM.getCode())) {//风速风向
            List<Map<String, String>> maps = sdDeviceDataMapper.selectFSFXDataList(beginTime, endTime, deviceId);
            return maps;
        } else if (sdDeviceData.getSearchValue().equals(DeviceType.DONGNEILIANGDUITEM.getCode())) {//洞内亮度
            List<Map<String, String>> maps = sdDeviceDataMapper.selectDNDataList(beginTime, endTime, deviceId);
            return maps;
        } else if (sdDeviceData.getSearchValue().equals(DeviceType.DONGWAILIANGDUITEM.getCode())) {//洞外亮度
            List<Map<String, String>> maps = sdDeviceDataMapper.selectDWDataList(beginTime, endTime, deviceId);
            return maps;
        }  else if (sdDeviceData.getSearchValue().equals(DeviceType.DONGFANGDUITEM.getCode())) {//风机内外振动仪检测器
            List<Map<String, String>> maps = sdDeviceDataMapper.selectFJDataList(beginTime, endTime, deviceId);
            return maps;
        }else if (sdDeviceData.getSearchValue().equals(DeviceType.DONGWATERGDUITEM.getCode())) {//水浸传感器
            List<Map<String, String>> maps = sdDeviceDataMapper.selectDWDataList(beginTime, endTime, deviceId);
            return maps;
        }else if (sdDeviceData.getSearchValue().equals(DeviceType.DONGHUMIDGDUITEM.getCode())) {//温湿度传感器
            List<Map<String, String>> maps = sdDeviceDataMapper.selectDWDataList(beginTime, endTime, deviceId);
            return maps;
        }else if (sdDeviceData.getSearchValue().equals(DeviceType.DONGCATGDUITEM.getCode())) {//微波车辆检测器
            List<Map<String, String>> maps = SpringUtils.getBean(SdMicrowavePeriodicStatisticsMapper.class).selectCatHistory(beginTime, endTime, deviceId);
            if(maps.size()>0){
                SdTunnels tunnelId = sdTunnelsMapper.selectSdTunnelsById(maps.get(0).get("tunnelId"));
                SdDevices tunnelId1 = sdDevicesMapper.selectSdDevicesById(maps.get(0).get("deviceId"));
                for (Map<String, String> map :maps){
                    map.put("tunnelName",tunnelId.getTunnelName());
                    map.put("deptName",tunnelId.getTunnelStationName());
                    map.put("direction",tunnelId1.getDirection());
                    map.put("pile",tunnelId1.getPile());
                }
            }
            return maps;
        }else {
            return null;
        }


    }

    /**
     * 获取设备实时数据
     * @param itemId
     * @param deviceId
     * @return
     */
    public List<SdDeviceData> getDeviceDataList(Long itemId, String deviceId){
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setItemId(itemId);
        sdDeviceData.setDeviceId(deviceId);
        return sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
    }

    /**
     * 默认获取当天时间
     * @return
     */
    private List<String> getCurrentDayTime(){
        List<String> intraday = new ArrayList<>();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String beginTime = now + " 00:00:00";
        String endTime = now + " 23:59:59";
        intraday.add(beginTime);
        intraday.add(endTime);
        return intraday;
    }


    private SdDeviceCOVIData getSelectExportParams(SdDeviceCOVIData sdDeviceCOVIData){
        String beginTime = getCurrentDayTime().get(0);
        String endTime = getCurrentDayTime().get(1);
        if (!sdDeviceCOVIData.getParams().isEmpty()) {
            beginTime = sdDeviceCOVIData.getParams().get("beginTime").toString();
            endTime = sdDeviceCOVIData.getParams().get("endTime").toString();
        }
        String ids = sdDeviceCOVIData.getIds();
        sdDeviceCOVIData.setBeginTime(beginTime);
        sdDeviceCOVIData.setEndTime(endTime);
        return sdDeviceCOVIData;
    }


    @Override
    public void updateDeviceData(String deviceId, String value, Integer itemId,boolean createLog) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            updateSdDeviceData(data);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            insertSdDeviceData(sdDeviceData);
        }

        // 是否插入log
        if(createLog){
            //存入数据记录表中
            SdDeviceDataRecord sdDeviceDataRecord = new SdDeviceDataRecord();
            sdDeviceDataRecord.setDeviceId(deviceId);
            sdDeviceDataRecord.setItemId(Long.valueOf(itemId));
            sdDeviceDataRecord.setData(value);
            sdDeviceDataRecord.setCreateTime(new Date());
            sdDeviceDataRecordMapper.insertSdDeviceDataRecord(sdDeviceDataRecord);
        }

    }


    /**
     * 修改设备数据表中实时数据
     * @param sdDevices 设备信息
     * @param value 数据
     * @param itemId 数据项
     */
    @Override
    public void updateDeviceData(SdDevices sdDevices, String value, Long itemId) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            updateSdDeviceData(data);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            insertSdDeviceData(sdDeviceData);
        }
    }

    /**
     * 修改设备数据表中实时数据
     * @param sdDevices 设备信息
     * @param value 数据
     * @param itemId 数据项
     */
    @Override
    public void updateDeviceData(SdDevices sdDevices, String value, Long itemId,boolean createLog) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            updateSdDeviceData(data);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            insertSdDeviceData(sdDeviceData);
        }
        // 是否插入log
        if(createLog){
            //存入数据记录表中
            SdDeviceDataRecord sdDeviceDataRecord = new SdDeviceDataRecord();
            sdDeviceDataRecord.setDeviceId(sdDevices.getEqId());
            sdDeviceDataRecord.setItemId(Long.valueOf(itemId));
            sdDeviceDataRecord.setData(value);
            sdDeviceDataRecord.setCreateTime(new Date());
            sdDeviceDataRecordMapper.insertSdDeviceDataRecord(sdDeviceDataRecord);
        }
    }

    @Override
    public List<Map> getItemDataByEqId(String eqId) {
        return sdDeviceDataMapper.getItemDataByEqId(eqId);
    }
}

