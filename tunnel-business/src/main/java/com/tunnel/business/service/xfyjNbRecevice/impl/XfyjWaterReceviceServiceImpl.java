package com.tunnel.business.service.xfyjNbRecevice.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.SdWarningInfo;
import com.tunnel.business.domain.xfWaterRecord.SdXfwaterRecord;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.event.SdWarningInfoMapper;
import com.tunnel.business.mapper.xfWaterRecord.SdXfwaterRecordMapper;
import com.tunnel.business.service.xfyjNbRecevice.XfyjWaterReceviceService;
import com.tunnel.business.utils.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class XfyjWaterReceviceServiceImpl implements XfyjWaterReceviceService {

    private static final Logger log = LoggerFactory.getLogger(XfyjWaterReceviceServiceImpl.class);
    /**
     * 十六进制码
     */
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F'};
    /**
     * 设备信息
     */
    @Autowired
    private SdDevicesMapper sdDevicesMapper;
    /**
     * 隧道信息
     */
    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;
    /**
     * 消防水报警管理
     */
    @Autowired
    private SdWarningInfoMapper sdWarningInfoMapper;
    /**
     * 解析记录
     */
    @Autowired
    private SdXfwaterRecordMapper sdXfwaterRecordMapper;

    @Resource
    private RedisCache redisCache;

    /**
     * base64转字符串
     *
     * @param str
     * @return
     */
    public String baseConvertStr(String str) {
        if (null != str) {
            Base64.Decoder decoder = Base64.getDecoder();
            try {
                return new String(decoder.decode(str.getBytes()), "GBK");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 解析
     *
     * @param jsonObject JSONObject
     * @return
     */
    @Override
    public void analysisWater(JSONObject jsonObject) {
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info("接收到了 消防水 设备的信息，但是个空的！");
            return;
        }
        String imei = jsonObject.getString("IMEI");
        if (null == imei || imei.isEmpty()) {
            log.info("IMEI 不是一个有效的参数！");
            return;
        }
        String strBase64 = jsonObject.getJSONObject("payload").getString("APPdata");
        // 将base64转为16进制
        String electrical = baseConvertStr(strBase64);
        // 空数据返回
        if (electrical == null || "".equals(electrical)) {
            log.info("## 消防水获取数据为空  ##");
            return;
        }
        // 获取站点
        // 判断312d312828存在的情况
        if (electrical.contains("NB")) {
            // 类型编号
            String type = electrical.substring(2, 6);
            String electricalHeader = electrical.substring(9);
            // 获取IMEI
            String strImei = electricalHeader.substring(0, 15);
            // 根据设备编号查询该设备信息
            SdDevices xfyjMaintenanceOfEquipment = sdDevicesMapper.selectSdDevicesById(strImei);
            if (null == xfyjMaintenanceOfEquipment) {
                log.info("## 根据 IMEI 没有对应的设备信息  ##");
                return;
            }
            // 通过隧道Id查询隧道所属部门，用于设备记录保存
            SdTunnels sdTunnels = sdTunnelsMapper.selectSdTunnelsById(xfyjMaintenanceOfEquipment.getEqTunnelId());
            String zdbh = sdTunnels.getDeptId();
            // 通讯记录插入
            SdXfwaterRecord electricalDetectorRecord = new SdXfwaterRecord();
            electricalDetectorRecord.setDeptId(zdbh);
            electricalDetectorRecord.setEquipmentId(strImei);
            electricalDetectorRecord.setTunnelId(sdTunnels.getTunnelId());
            electricalDetectorRecord.setTunnelName(sdTunnels.getTunnelName());
            electricalDetectorRecord.setEquipmentName(xfyjMaintenanceOfEquipment.getEqName());
            electricalDetectorRecord.setPosition(xfyjMaintenanceOfEquipment.getPile());
            electricalDetectorRecord.setEqDirection(xfyjMaintenanceOfEquipment.getEqDirection());
            electrical = electricalHeader.substring(15);
            // 状态
            String state = electrical.substring(0, 2);
            if (state.equals(null) || state.equals("")) {
                state = "0";
            }
            electricalDetectorRecord.setState(state);
            // 最低值
            electrical = electrical.substring(2);
            String low = "";
            if (xfyjMaintenanceOfEquipment.getEqType() == 111) {
                low = String.valueOf(Double.valueOf(electrical.substring(0, 4)) / 1000);
            }
            electricalDetectorRecord.setLow(low);
            // 最高值
            electrical = electrical.substring(4);
            String height = "";
            if (xfyjMaintenanceOfEquipment.getEqType() == 111) {
                height = String.valueOf(Double.valueOf(electrical.substring(0, 4)) / 1000);
            }

            electricalDetectorRecord.setHighest(height);
            // 设备模拟量
            electrical = electrical.substring(4);
            String imitateData = "";
            if (xfyjMaintenanceOfEquipment.getEqType() == 111) {
                imitateData = String.valueOf(Double.valueOf(electrical.substring(0, 4)) / 1000);
            }
            // 结果保留2位小数

            // 获取模拟量测量值
            electricalDetectorRecord.setAnalogQuantity(imitateData);
            // 创建时间
            electricalDetectorRecord.setCreateTime(new Date());
            redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
            List<SdXfwaterRecord> list = new ArrayList<>();
            list.add(electricalDetectorRecord);
            String keyName = "pressureRecord" + sdTunnels.getTunnelId();
            redisCache.setCacheList(keyName, list);
            // 存储消防水信息
            sdXfwaterRecordMapper.insertSdXfwaterRecord(electricalDetectorRecord);

            String alarmContent = "";
            switch (state) {
                // 设备正常
                case "10":
                    break;
                // 电量不足报警
//			case "02":
//				alarmContent = "电量不足报警";
//				break;
                // 低压报警-低水位报警
                case "03":
                    // NB压力表
                    if ("9002".equals(type)) {
                        alarmContent = "压力表低压报警";
                    }
                    break;
                // 低压报警恢复
                case "13":
                    break;
                // 高压报警-高水位报警
                case "04":
                    // NB压力表
                    if ("9002".equals(type)) {
                        alarmContent = "压力表高压报警";
                    }
                    break;
                // 高压报警恢复
                case "14":
                    break;
            }

            // 4.判断报警状态
            if (!"".equals(alarmContent)) {
                eleAlarm(xfyjMaintenanceOfEquipment, alarmContent);
            }

            // 5.判断正常状态
            if ("".equals(alarmContent)) {
                normalEle(xfyjMaintenanceOfEquipment);
            }
        } else {
            log.info("## 消防水获取数据不正确  ##");
            return;
        }

    }

    /**
     * 恢复状态解除预警
     */
    private void normalEle(SdDevices equipment) {
        //执行查询消防水报警信息，应该从sd_warning_info中查询，方法后要对报警事件进行更新
        SdWarningInfo elseAlarm = new SdWarningInfo();
        elseAlarm.setEqId(equipment.getEqId());
        // 处理状态  0：未处置；1：已处置
        elseAlarm.setProcessState("0");
        //应该从sd_warning_info查询已经发生报警的数据
        List<SdWarningInfo> elseAlarmInfoList = sdWarningInfoMapper.selectSdWarningInfoList(elseAlarm);
        if (elseAlarmInfoList != null && elseAlarmInfoList.size() > 0) {
            SdWarningInfo elseAlarmInfo = elseAlarmInfoList.get(0);
            elseAlarmInfo.setProcessState("1");
            elseAlarmInfo.setEventEndTime(new Date());
            elseAlarmInfo.setUpdateTime(new Date());
            sdWarningInfoMapper.updateSdWarningInfo(elseAlarmInfo);
        }
    }


    /**
     * 报警
     */
    private void eleAlarm(SdDevices equipment, String content) {
        Date date = new Date();
        //执行查询消防水报警信息，应该从sd_warning_info中查询，方法后如果预警已经存在要对报警事件进行更新，否则新增
        SdWarningInfo elseAlarm = new SdWarningInfo();
        elseAlarm.setEqId(equipment.getEqId());
        // 处理状态  0：未处置；1：已处置
        elseAlarm.setProcessState("0");
        //应该从sd_warning_info查询已经发生报警的数据
        List<SdWarningInfo> elseAlarmInfoList = sdWarningInfoMapper.selectSdWarningInfoList(elseAlarm);
        //电气信息表中有进行时电气存在
        if (elseAlarmInfoList != null && elseAlarmInfoList.size() > 0) {
            // 更新最新报警时间
            elseAlarm.setUpdateTime(date);
            // 更新报警内容
            elseAlarm.setWarningName(content);
            elseAlarm.setUpdateTime(date);
            sdWarningInfoMapper.updateSdWarningInfo(elseAlarm);
        } else {
            //插入预警信息表
            elseAlarm.setTunnelId(equipment.getEqTunnelId());
            elseAlarm.setWarningName(content);
            elseAlarm.setWarningTime(date);
            elseAlarm.setInforSources("消防压力表预警事件信息");
            elseAlarm.setProcessState("0");
            elseAlarm.setCreateTime(date);
            elseAlarm.setWarningTypeId(54L);
            elseAlarm.setEqId(equipment.getEqId());
            sdWarningInfoMapper.insertSdWarningInfo(elseAlarm);
        }
    }

}
