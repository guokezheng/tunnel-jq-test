package com.tunnel.business.service.dataInfo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
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
    public List<Map<String, String>> getDeviceDataByTunnelId(String tunnelId) {
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
        List<Map<String, Object>> todayCOData = sdDeviceDataMapper.getTodayCOVIData(deviceId, itemId, today);
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
        List<Map<String, Object>> todayVIData = sdDeviceDataMapper.getTodayCOVIData(deviceId, itemId, today);
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
        List<Map<String, Object>> todayFSData = sdDeviceDataMapper.getTodayCOVIData(deviceId, itemId, today);
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
        List<Map<String, Object>> todayLDData = null;
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
            }
            todayLDData = sdDeviceDataMapper.getTodayCOVIData(deviceId, Long.valueOf(DevicesTypeItemEnum.LIANG_DU_INSIDE.getCode()), today);
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
            }
            todayLDData = sdDeviceDataMapper.getTodayCOVIData(deviceId, Long.valueOf(DevicesTypeItemEnum.LIANG_DU_OUTSIDE.getCode()), today);
            map.put("todayLDOutsideData", todayLDData);
            if (deviceData != null) {
                map.put("nowData", deviceData.getData());
            } else {
                map.put("nowData", null);
            }
        }
        return map;
    }

    @Override
    public List<Map<String, String>> dataLogInfoList(SdDeviceData sdDeviceData) {
        String dept = "";
        if (sdDeviceData.getDeptId() == null) {
            String deptId = SecurityUtils.getDeptId();
            dept = deptId;
        } else if (sdDeviceData.getDeptId() != null) {
            dept = sdDeviceData.getDeptId().toString();
        }
        String tunnelId = "";
        if (sdDeviceData.getTunnelId() != null && !sdDeviceData.getTunnelId().equals("")) {
            tunnelId = sdDeviceData.getTunnelId();
        }
        String searchValue = "1";
        if (sdDeviceData.getSearchValue() != null && !sdDeviceData.getSearchValue().equals("")) {
            searchValue = sdDeviceData.getSearchValue();
        }
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String beginTime = now + " 00:00:00";
        String endTime = now + " 23:59:59";
        if (!sdDeviceData.getParams().isEmpty()) {
//            Map<String, String> params = (Map<String, String>)map.get("params");
            beginTime = sdDeviceData.getParams().get("beginTime").toString();
            endTime = sdDeviceData.getParams().get("endTime").toString();
        }
        if (searchValue.equals("1")) {
            List<Map<String, String>> maps = sdDeviceDataMapper.selectCOVIDataList(dept, tunnelId, beginTime, endTime);
            return maps;
        } else if (searchValue.equals("2")) {
            List<Map<String, String>> maps = sdDeviceDataMapper.selectFSFXDataList(dept, tunnelId, beginTime, endTime);
            return maps;
        } else if (searchValue.equals("3")) {
            List<Map<String, String>> maps = sdDeviceDataMapper.selectDNDataList(dept, tunnelId, beginTime, endTime);
            return maps;
        } else if (searchValue.equals("4")) {
            List<Map<String, String>> maps = sdDeviceDataMapper.selectDWDataList(dept, tunnelId, beginTime, endTime);
            return maps;
        } else {
            return null;
        }
    }

    @Override
    public Map<String, Object> energyConsumptionDetection(String tunnelId) {
        ExternalSystem externalSystem = new ExternalSystem();
        externalSystem.setTunnelId(tunnelId);
        externalSystem.setSystemName("能源管控平台");
        List<ExternalSystem> externalSystems = externalSystemService.selectExternalSystemList(externalSystem);
        if (externalSystems.isEmpty()) {
            return null;
        }
        ExternalSystem system = externalSystems.get(0);
        SdDevices sdDevices = new SdDevices();
        sdDevices.setExternalSystemId(system.getId());
        List<SdDevices> sdDevicesList = sdDevicesMapper.selectSdDevicesList(sdDevices);
        SdDevices devices = sdDevicesList.get(0);
        String eqId = devices.getEqId();
        eqId = eqId.substring(eqId.lastIndexOf("-")+1);
        String url = system.getSystemUrl() + "login";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "admin");
        map.put("password", "HSD123!@#");
        JSONObject json = JSONObject.parseObject(HttpUtils.sendPostByApplicationJson(url,JSONObject.toJSONString(map)));
        String token =  json.get("token").toString();
        //获取能耗数据
        url = system.getSystemUrl() + "sjfx/getEnergyByStatisticsType";
        String[] str = new String[]{"day","month","year"};
        Map<String, Object> allDataList = new HashMap<>();
        for (int j = 0;j < str.length;j++) {
            String type = str[j];
            String params = "powerCode=" + eqId + "&type="+type;
            json = JSONObject.parseObject(HttpUtils.sendGetWithAuth(url, params, Constants.UTF8, token));
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

//        List<Long> list = Arrays.asList(1450L, 1650L, 1500L, 1430L, 1580L, 1530L, 1580L, 1460L, 1400L, 1540L);
        return allDataList;
    }

}
