package com.tunnel.business.service.xfWaterRecord.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.xfWaterRecord.SdXfwaterRecord;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.xfWaterRecord.SdXfwaterRecordMapper;
import com.tunnel.business.service.xfWaterRecord.ISdXfwaterRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 消防水设备监测记录Service业务层处理
 *
 * @date 2022-02-11
 */
@Service
public class SdXfwaterRecordServiceImpl implements ISdXfwaterRecordService {
    @Autowired
    private SdXfwaterRecordMapper sdXfwaterRecordMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    @Resource
    private RedisCache redisCache;

    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    /**
     * 查询消防水设备监测记录
     *
     * @param id 消防水设备监测记录主键
     * @return 消防水设备监测记录
     */
    @Override
    public SdXfwaterRecord selectSdXfwaterRecordById(Long id) {
        return sdXfwaterRecordMapper.selectSdXfwaterRecordById(id);
    }

    /**
     * 查询消防水设备监测记录列表
     *
     * @param sdXfwaterRecord 消防水设备监测记录
     * @return 消防水设备监测记录
     */
    @Override
    public List<SdXfwaterRecord> selectSdXfwaterRecordList(SdXfwaterRecord sdXfwaterRecord) {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        sdXfwaterRecord.setDeptId(deptId);
        List<SdXfwaterRecord> sdXfwaterRecords = sdXfwaterRecordMapper.selectSdXfwaterRecordList(sdXfwaterRecord);
        Date nowdate = new Date();
        for (int i = 0; i < sdXfwaterRecords.size(); i++) {
            SdXfwaterRecord sw = sdXfwaterRecords.get(i);
            Date createTime = sw.getCreateTime();
            if (createTime != null) {
                nowdate = new Date();
                long sec = (nowdate.getTime() - createTime.getTime()) / 1000L / 60L / 60L;
                if (sec > 48L) {
                    sw.setIsOff("offline");
                } else {
                    sw.setIsOff("online");
                }
            } else {
                // 设备状态设为离线
                sw.setIsOff("offline");
            }
        }
        return sdXfwaterRecords;
    }

    /**
     * 新增消防水设备监测记录
     *
     * @param sdXfwaterRecord 消防水设备监测记录
     * @return 结果
     */
    @Override
    public int insertSdXfwaterRecord(SdXfwaterRecord sdXfwaterRecord) {
        sdXfwaterRecord.setCreateTime(DateUtils.getNowDate());
        return sdXfwaterRecordMapper.insertSdXfwaterRecord(sdXfwaterRecord);
    }

    /**
     * 修改消防水设备监测记录
     *
     * @param sdXfwaterRecord 消防水设备监测记录
     * @return 结果
     */
    @Override
    public int updateSdXfwaterRecord(SdXfwaterRecord sdXfwaterRecord) {
        return sdXfwaterRecordMapper.updateSdXfwaterRecord(sdXfwaterRecord);
    }

    /**
     * 批量删除消防水设备监测记录
     *
     * @param ids 需要删除的消防水设备监测记录主键
     * @return 结果
     */
    @Override
    public int deleteSdXfwaterRecordByIds(Long[] ids) {
        return sdXfwaterRecordMapper.deleteSdXfwaterRecordByIds(ids);
    }

    /**
     * 删除消防水设备监测记录信息
     *
     * @param id 消防水设备监测记录主键
     * @return 结果
     */
    @Override
    public int deleteSdXfwaterRecordById(Long id) {
        return sdXfwaterRecordMapper.deleteSdXfwaterRecordById(id);
    }

    /**
     * 获取指定压力表压力上下限和压力数据
     *
     * @param equipmentId
     * @return
     */
    @Override
    public List<SdXfwaterRecord> getNumberOfPressureGaugesCollectedPerDay(String equipmentId, String tunnelId) {
        if (equipmentId.equals("") || equipmentId.isEmpty()) {
            throw new RuntimeException("当前查询当天压力表上下限和压力参数为空");
        }
        Date nowdate = new Date();
        List<SdXfwaterRecord> numberOfPressureGaugesCollectedPerDay;
        String keyName = "pressureRecord" + tunnelId;
        if (redisCache.getCacheList(keyName) != null && redisCache.getCacheList(keyName).size() != 0) {
            List<SdXfwaterRecord> pressureRecord = redisCache.getCacheList(keyName);
            SdXfwaterRecord sdXfwaterRecord = pressureRecord.get(0);
            Date createTime = sdXfwaterRecord.getCreateTime();
            if (createTime != null) {
                long sec = (nowdate.getTime() - createTime.getTime()) / 1000L / 60L / 60L;
                if (sec > 48L) {
                    sdXfwaterRecord.setIsOff("offline");
                } else {
                    sdXfwaterRecord.setIsOff("online");
                }
            } else {
                // 设备状态设为离线
                sdXfwaterRecord.setIsOff("offline");
            }
            return pressureRecord;
        } else {
            numberOfPressureGaugesCollectedPerDay = sdXfwaterRecordMapper.getNumberOfPressureGaugesCollectedPerDay(equipmentId, tunnelId);
            for (int i = 0; i < numberOfPressureGaugesCollectedPerDay.size(); i++) {
                SdXfwaterRecord sw = numberOfPressureGaugesCollectedPerDay.get(i);
                Date createTime = sw.getCreateTime();
                if (createTime != null) {
                    long sec = (nowdate.getTime() - createTime.getTime()) / 1000L / 60L / 60L;
                    if (sec > 48L) {
                        sw.setIsOff("offline");
                    } else {
                        sw.setIsOff("online");
                    }
                } else {
                    // 设备状态设为离线
                    sw.setIsOff("offline");
                }
            }
        }
        return numberOfPressureGaugesCollectedPerDay;
    }

    /**
     * 获取所有压力表一天上报次数
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getAllPressureGaugesCollectedPerDay() {
        String deptId = SecurityUtils.getDeptId();
        return sdXfwaterRecordMapper.getAllPressureGaugesCollectedPerDay(deptId);
    }

    @Override
    public List<Map<String, Object>> getNumberOfPressureGaugesCollectedPerMonth(String equipmentId) {
        if (equipmentId.equals("") || equipmentId.isEmpty()) {
            throw new RuntimeException("当前查询当月压力表采集记录参数为空");
        }
        return sdXfwaterRecordMapper.getNumberOfPressureGaugesCollectedPerMonth(equipmentId);
    }

    @Override
    public List<SdXfwaterRecord> getObtainTheStatusOfAllPressureGauges(String tunnelId) {
        if (tunnelId.equals("") || tunnelId.isEmpty()) {
            throw new RuntimeException("当前查询指定隧道下所有压力表状态参数为空");
        }
        //获取指定隧道下所有压力表设备
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(tunnelId);
        sdDevices.setEqType(111L);
        List<SdDevices> devices = sdDevicesMapper.selectDropSdDevicesList(sdDevices);
        List<SdXfwaterRecord> status = new ArrayList<>();
        Date nowdate = new Date();
        String deptId = SecurityUtils.getDeptId();
        for (int i = 0; i < devices.size(); i++) {
            String equipmentId = devices.get(i).getEqId();
            SdXfwaterRecord recentRecordByEqId = sdXfwaterRecordMapper.getRecentRecordByEqId(equipmentId, deptId);
            Date createTime = recentRecordByEqId.getCreateTime();
            if (createTime != null) {
                nowdate = new Date();
                long sec = (nowdate.getTime() - createTime.getTime()) / 1000L / 60L / 60L;
                if (sec > 48L) {
                    recentRecordByEqId.setIsOff("offline");
                } else {
                    recentRecordByEqId.setIsOff("online");
                }
            } else {
                // 设备状态设为离线
                recentRecordByEqId.setIsOff("offline");
            }
            status.add(recentRecordByEqId);
        }
        return status;
    }

    @Override
    public int getStatusOfAllPressureGauges() {
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(111L);
        List<SdDevices> devices = sdDevicesMapper.selectDropSdDevicesList(sdDevices);
        Date nowdate = new Date();
        int count = 0;
        String deptId = SecurityUtils.getDeptId();
        for (int i = 0; i < devices.size(); i++) {
            String equipmentId = devices.get(i).getEqId();
            SdXfwaterRecord recentRecordByEqId = sdXfwaterRecordMapper.getRecentRecordByEqId(equipmentId, deptId);
            Date createTime = recentRecordByEqId.getCreateTime();
            if (createTime != null) {
                nowdate = new Date();
                long sec = (nowdate.getTime() - createTime.getTime()) / 1000L / 60L / 60L;
                if (sec > 48L) {
                    count++;
                }
            } else {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<Map<String, Object>> getHistoryOfPressureGauges(SdXfwaterRecord sdXfwaterRecord) throws ParseException {
        String statisticalType = sdXfwaterRecord.getStatisticalType();
        String tunnelId = sdXfwaterRecord.getTunnelId();
        String tunnelName = "";
        if (null == tunnelId || tunnelId.equals("") || tunnelId.isEmpty()) {
            SdTunnels sdT = new SdTunnels();
            SdTunnels sdTunnel = sdTunnelsMapper.selectSdTunnelsList(sdT).get(0);
            tunnelId = sdTunnel.getTunnelId();
            tunnelName = sdTunnel.getTunnelName();
        }
        String equipmentId = sdXfwaterRecord.getEquipmentId();
        if (null == equipmentId || equipmentId.equals("") || equipmentId.isEmpty()) {
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqTunnelId(tunnelId);
            sdDevices.setEqType(111L);
            List<SdDevices> sdDevicesList = sdDevicesMapper.selectDropSdDevicesList(sdDevices);
            if (sdDevicesList.size() > 0) {
                SdDevices sdDevice = sdDevicesList.get(0);
                equipmentId = sdDevice.getEqId();
            }
        }
        Map<String, Object> params = sdXfwaterRecord.getParams();
        params.put("tunnelName", tunnelName);
        if (params.get("tunnelName") == "") {
            SdTunnels sdTunnels = sdTunnelsMapper.selectSdTunnelsById(tunnelId);
            params.put("tunnelName", sdTunnels.getTunnelName());
        }
        String deptId = SecurityUtils.getDeptId();
        if (statisticalType.equals("1")) {
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> warn = new HashMap<>();
            //按年查询每月数量
            if (params.get("year") != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String metEnd = params.get("year").toString();
                String metEndTemp = metEnd.replace("GMT", "").replaceAll("\\(.*\\)", "");
                SimpleDateFormat format2 = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
                metEnd = format.format(format2.parse(metEndTemp));
                String substring = metEnd.substring(0, 4);
                params.put("year", substring);
                params.put("deptId", deptId);
                List<Map<String, Object>> yearmaps = sdXfwaterRecordMapper.selectWarningInfoMsgByYear(equipmentId, tunnelId, params);
                warn.put("year", yearmaps);
            }
            //按月查询每日数量
            if (params.get("month") != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String metEnd = params.get("month").toString();
                String metEndTemp = metEnd.replace("GMT", "").replaceAll("\\(.*\\)", "");
                SimpleDateFormat format2 = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
                metEnd = format.format(format2.parse(metEndTemp));
                String substring = metEnd.substring(0, 7);
                params.put("month", substring);
                params.put("deptId", deptId);
                String lastDayOfMonth = getLastDayOfMonth(Integer.parseInt(substring.substring(0, 4)), Integer.parseInt(substring.substring(6)));
                List<Map<String, Object>> monthmaps = sdXfwaterRecordMapper.selectWarningInfoMsgByMonth(equipmentId, tunnelId, lastDayOfMonth, params);
                warn.put("month", monthmaps);
            }
            //按小时查询
            if (params.get("date") != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String metEnd = params.get("date").toString();
                String metEndTemp = metEnd.replace("GMT", "").replaceAll("\\(.*\\)", "");
                SimpleDateFormat format2 = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
                metEnd = format.format(format2.parse(metEndTemp));
                params.put("datea", metEnd + " 00:00:00");
                params.put("dateb", metEnd + " 23:59:59");
                params.put("deptId", deptId);
                List<Map<String, Object>> daymaps = sdXfwaterRecordMapper.selectWarningInfoMsgByDay(equipmentId, tunnelId, params);
                warn.put("date", daymaps);
            }
            list.add(warn);
            return list;
        } else if (statisticalType.equals("3")) {
            List<Map<String, Object>> maps = sdXfwaterRecordMapper.selectXfRecord(equipmentId, tunnelId, params);
            return maps;
        }
        return null;
    }
}
