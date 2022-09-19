package com.tunnel.deal.hik;

import com.ruoyi.common.core.redis.RedisCache;
import com.sun.jna.Pointer;
import com.tunnel.business.domain.dataInfo.SdDataTrafficHour;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.service.dataInfo.ISdDataTrafficHourService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FMSGCallBack_V31 implements HCNetSDK.FMSGCallBack_V31 {

    @Resource
    private RedisCache redisCache;

    @Autowired
    private ISdDevicesService iSdDevicesService;

    @Autowired
    private ISdDataTrafficHourService iSdDataTrafficHourService;

    @Autowired
    private ISdTunnelsService iSdTunnelsService;

    //报警信息回调函数
    @Override
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        AlarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
        return true;
    }

    public void AlarmDataHandle(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        System.out.println("报警事件类型： lCommand:" + Integer.toHexString(lCommand));
        String sTime;
        String MonitoringSiteID;
        //lCommand是传的报警类型
        switch (lCommand) {

            case HCNetSDK.COMM_ITS_PLATE_RESULT://交通抓拍结果(新报警信息)
                HCNetSDK.NET_ITS_PLATE_RESULT strItsPlateResult = new HCNetSDK.NET_ITS_PLATE_RESULT();
                strItsPlateResult.write();
                Pointer pItsPlateInfo = strItsPlateResult.getPointer();
                pItsPlateInfo.write(0, pAlarmInfo.getByteArray(0, strItsPlateResult.size()), 0, strItsPlateResult.size());
                strItsPlateResult.read();
                try {
                    String sLicense = new String(strItsPlateResult.struPlateInfo.sLicense, "GBK");
                    byte VehicleType = strItsPlateResult.byVehicleType;  //0-其他车辆，1-小型车，2-大型车，3- 行人触发，4- 二轮车触发，5- 三轮车触发，6- 机动车触发
                    MonitoringSiteID = new String(strItsPlateResult.byMonitoringSiteID);
                    System.out.println("车牌号：" + sLicense + ":车辆类型：" + VehicleType + ":监控点编号：" + MonitoringSiteID);
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                /**
                 * 报警图片保存，车牌，车辆图片
                 */
                for (int i = 0; i < strItsPlateResult.dwPicNum; i++) {
                    if (strItsPlateResult.struPicInfo[i].dwDataLen > 0) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            String filename = "../pic/" + newName + "_type[" + strItsPlateResult.struPicInfo[i].byType + "]_ItsPlate.jpg";
                            fout = new FileOutputStream(filename);
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = strItsPlateResult.struPicInfo[i].pBuffer.getByteBuffer(offset, strItsPlateResult.struPicInfo[i].dwDataLen);
                            byte[] bytes = new byte[strItsPlateResult.struPicInfo[i].dwDataLen];
                            buffers.rewind();
                            buffers.get(bytes);
                            fout.write(bytes);
                            fout.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case HCNetSDK.COMM_ALARM_TFS: //道路违章取证报警
                HCNetSDK.NET_DVR_TFS_ALARM strTfsAlarm = new HCNetSDK.NET_DVR_TFS_ALARM();
                strTfsAlarm.write();
                Pointer pTfsAlarm = strTfsAlarm.getPointer();
                pTfsAlarm.write(0, pAlarmInfo.getByteArray(0, strTfsAlarm.size()), 0, strTfsAlarm.size());
                strTfsAlarm.read();
                sTime = CommonUtil.parseTime(strTfsAlarm.dwAbsTime); //报警绝对时间
                int IllegalType = strTfsAlarm.dwIllegalType; // 违章类型
                MonitoringSiteID = strTfsAlarm.byMonitoringSiteID.toString(); //监控点编号
                // 车牌信息
                try {
                    String PlateInfo = "车牌号：" + new String(strTfsAlarm.struPlateInfo.sLicense, "GBK");
                    System.out.println("【道路违章取证报警】时间：" + sTime + "违章类型：" + IllegalType + "车牌信息：" + PlateInfo);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //报警图片信息
                for (int i = 0; i < strTfsAlarm.dwPicNum; i++) {
                    if (strTfsAlarm.struPicInfo[i].dwDataLen > 0) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            String filename = "../pic/" + newName + "_type[" + strTfsAlarm.struPicInfo[i].byType + "]_TfsPlate.jpg";
                            fout = new FileOutputStream(filename);
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = strTfsAlarm.struPicInfo[i].pBuffer.getByteBuffer(offset, strTfsAlarm.struPicInfo[i].dwDataLen);
                            byte[] bytes = new byte[strTfsAlarm.struPicInfo[i].dwDataLen];
                            buffers.rewind();
                            buffers.get(bytes);
                            fout.write(bytes);
                            fout.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case HCNetSDK.COMM_ALARM_AID_V41: //道路事件检测
                HCNetSDK.NET_DVR_AID_ALARM_V41 strAIDAlarmV41 = new HCNetSDK.NET_DVR_AID_ALARM_V41();
                strAIDAlarmV41.write();
                Pointer pstrAIDAlarmV41 = strAIDAlarmV41.getPointer();
                pstrAIDAlarmV41.write(0, pAlarmInfo.getByteArray(0, strAIDAlarmV41.size()), 0, strAIDAlarmV41.size());
                strAIDAlarmV41.read();
                sTime = CommonUtil.parseTime(strAIDAlarmV41.dwAbsTime); //报警触发绝对时间
                MonitoringSiteID = strAIDAlarmV41.byMonitoringSiteID.toString(); //监控点编号
                int AIDType = strAIDAlarmV41.struAIDInfo.dwAIDType; //    交通事件类型
                int AIDTypeEx = strAIDAlarmV41.struAIDInfo.dwAIDTypeEx; //交通事件类型扩展
                System.out.println("【道路事件检测】" + "时间：" + sTime + "监控点：" + MonitoringSiteID + "交通事件类型：" + AIDType + "交通事件类型扩展：" + AIDTypeEx);
                //报警图片信息
                if (strAIDAlarmV41.dwPicDataLen > 0 && strAIDAlarmV41.pImage != null) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + newName + "_AIDalarm.jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = strAIDAlarmV41.pImage.getByteBuffer(offset, strAIDAlarmV41.dwPicDataLen);
                        byte[] bytes = new byte[strAIDAlarmV41.dwPicDataLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case HCNetSDK.COMM_ALARM_TPS_V41://交通数据统计的报警
                HCNetSDK.NET_DVR_TPS_ALARM_V41 strTPSalarmV41 = new HCNetSDK.NET_DVR_TPS_ALARM_V41();
                strTPSalarmV41.write();
                Pointer pstrTPSalarmV41 = strTPSalarmV41.getPointer();
                pstrTPSalarmV41.write(0, pAlarmInfo.getByteArray(0, strTPSalarmV41.size()), 0, strTPSalarmV41.size());
                strTPSalarmV41.read();
                sTime = CommonUtil.parseTime(strTPSalarmV41.dwAbsTime);
                MonitoringSiteID = strTPSalarmV41.byMonitoringSiteID.toString(); //监控点编号
                String StartTime = CommonUtil.parseTime(strTPSalarmV41.dwStartTime); //开始统计时间；
                String StopTime = CommonUtil.parseTime(strTPSalarmV41.dwStopTime); //结束统计时间；
                System.out.println("【交通数据统计】" + "时间：" + sTime + "监控点编号：" + MonitoringSiteID + "开始统计时间：" + StartTime + "结束统计时间：" + StopTime);
                //车道统计参数信息
                for (int i = 0; i <= HCNetSDK.MAX_TPS_RULE; i++) {
                    byte LaneNo = strTPSalarmV41.struTPSInfo.struLaneParam[i].byLaneNo; //车道号
                    byte TrafficState = strTPSalarmV41.struTPSInfo.struLaneParam[i].byTrafficState; //车道状态 0-无效，1-畅通，2-拥挤，3-堵塞
                    int TpsType = strTPSalarmV41.struTPSInfo.struLaneParam[i].dwTpsType; //数据变化类型标志，表示当前上传的统计参数中，哪些数据有效，按位区分
                    int LaneVolume = strTPSalarmV41.struTPSInfo.struLaneParam[i].dwLaneVolume; //车道流量
                    int LaneVelocity = strTPSalarmV41.struTPSInfo.struLaneParam[i].dwLaneVelocity; //车道平均速度
                    float SpaceOccupyRation = strTPSalarmV41.struTPSInfo.struLaneParam[i].fSpaceOccupyRation;  //车道占有率，百分比计算（空间上，车辆长度与监控路段总长度的比值)
                    System.out.println("车道号：" + LaneNo + "车道状态：" + TrafficState + "车道流量：" + LaneVolume + "车道占有率：" + SpaceOccupyRation + "\n");
                }
                break;
            //实时过车数据数据,包含车速信息
            case HCNetSDK.COMM_ALARM_TPS_REAL_TIME:
                HCNetSDK.NET_DVR_TPS_REAL_TIME_INFO netDvrTpsParam = new HCNetSDK.NET_DVR_TPS_REAL_TIME_INFO();
                netDvrTpsParam.write();
                Pointer pItsParkVehicle = netDvrTpsParam.getPointer();
                pItsParkVehicle.write(0, pAlarmInfo.getByteArray(0, netDvrTpsParam.size()), 0, netDvrTpsParam.size());
                netDvrTpsParam.read();
                String struTime = "" + String.format("%04d", netDvrTpsParam.struTime.wYear) +
                        String.format("%02d", netDvrTpsParam.struTime.byMonth) +
                        String.format("%02d", netDvrTpsParam.struTime.byDay) +
//                        String.format("%02d", netDvrTpsParam.struTime.byDay) +
                        String.format("%02d", netDvrTpsParam.struTime.byHour) +
                        String.format("%02d", netDvrTpsParam.struTime.byMinute) +
                        String.format("%02d", netDvrTpsParam.struTime.bySecond);
                Short wDeviceID = new Short(netDvrTpsParam.struTPSRealTimeInfo.wDeviceID);//设备ID
                int channel = netDvrTpsParam.dwChan; //触发报警通道号
                String byLane = new String(String.valueOf(netDvrTpsParam.struTPSRealTimeInfo.byLane)).trim();// 对应车道号
                // 车道状态
                String byLaneState = new String(String.valueOf(netDvrTpsParam.struTPSRealTimeInfo.byLaneState)).trim();
                String byLaneStateName = "无状态";
                switch (byLaneState) {
                    case "1":
                        byLaneStateName = "畅通";
                        break;
                    case "2":
                        byLaneStateName = "拥挤";
                        break;
                    case "3":
                        byLaneStateName = "堵塞";
                        break;
                }
                // 对应车速（KM/H)
                String bySpeed = new String(String.valueOf(netDvrTpsParam.struTPSRealTimeInfo.bySpeed)).trim();
                int dwDownwardFlow = netDvrTpsParam.struTPSRealTimeInfo.dwDownwardFlow;//当前车道 从上到下车流量
                int dwUpwardFlow = netDvrTpsParam.struTPSRealTimeInfo.dwUpwardFlow;       //当前车道 从下到上车流量
                System.out.println("通道号：" + channel + "; 时间：" + struTime + ";对应车道号：" + byLane + ";当前车道 从上到下车流量：" + dwDownwardFlow + ";当前车道 从下到上车流量：" + dwUpwardFlow);
                //获取设备相关信息
                String deviceIp = new String(pAlarmer.sDeviceIP).trim();
                SdDevices sdDevices = new SdDevices();
                List<SdDevices> devicesList = iSdDevicesService.selectSdDevicesList(sdDevices);
                SdDevices sdDevices1 = devicesList.get(0);
                //实时车流量信息
                String redisKeyName = sdDevices1.getEqTunnelId() + "-" + sdDevices1.getPile() + "-" + sdDevices1.getEqId();
//                String realTimeTrafficFlowInformation = "通道号：" + channel + "; 时间：" + struTime + ";对应车道号：" + byLane
//                        + ";当前车道 从上到下车流量：" + dwDownwardFlow + ";当前车道 从下到上车流量：" + dwUpwardFlow
//                        + ";对应车速：" + bySpeed + "KM/H;当前车道拥挤状态：" + byLaneStateName;
                Map<String, Object> realTimeTrafficFlowInformationMap = new HashMap<>();
                realTimeTrafficFlowInformationMap.put("channel", channel);
                realTimeTrafficFlowInformationMap.put("struTime", struTime);
                realTimeTrafficFlowInformationMap.put("byLane", byLane);
                realTimeTrafficFlowInformationMap.put("dwDownwardFlow", dwDownwardFlow);
                realTimeTrafficFlowInformationMap.put("dwUpwardFlow", dwUpwardFlow);
                realTimeTrafficFlowInformationMap.put("bySpeed", bySpeed);
                realTimeTrafficFlowInformationMap.put("byLaneStateName", byLaneStateName);
                realTimeTrafficFlowInformationMap.put("eqDirection", sdDevices1.getEqDirection());
                //存储实时车流量到redis
//                redisCache.setCacheObject(redisKeyName,realTimeTrafficFlowInformation);
                redisCache.setCacheMap(redisKeyName, realTimeTrafficFlowInformationMap);
                break;

            case HCNetSDK.COMM_ALARM_TPS_STATISTICS: //统计过车数据
                HCNetSDK.NET_DVR_TPS_STATISTICS_INFO netDvrTpsStatisticsInfo = new HCNetSDK.NET_DVR_TPS_STATISTICS_INFO();
                netDvrTpsStatisticsInfo.write();
                Pointer pTpsVehicle = netDvrTpsStatisticsInfo.getPointer();
                pTpsVehicle.write(0, pAlarmInfo.getByteArray(0, netDvrTpsStatisticsInfo.size()), 0, netDvrTpsStatisticsInfo.size());
                netDvrTpsStatisticsInfo.read();
                int Tpschannel = netDvrTpsStatisticsInfo.dwChan; //触发报警通道号
                //统计开始时间
                String struStartTime = "" + String.format("%04d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.wYear) +
                        String.format("%02d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.byMonth) +
                        String.format("%02d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.byDay) +
//                        String.format("%02d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.byDay) +
                        String.format("%02d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.byHour) +
                        String.format("%02d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.byMinute) +
                        String.format("%02d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.bySecond);
                byte TotalLaneNum = netDvrTpsStatisticsInfo.struTPSStatisticsInfo.byTotalLaneNum; //有效车道总数
                //设备IP
                String deviceIP = new String(pAlarmer.sDeviceIP).trim();
                SdDevices devices = new SdDevices();
                List<SdDevices> devicesLists = iSdDevicesService.selectSdDevicesList(devices);
                SdDevices devices1 = devicesLists.get(0);
                //统计数据存库，有几条有效车道就要存多少条数据到小时车流量统计表
                for (int j = 0; j < TotalLaneNum; j++) {
                    SdDataTrafficHour sdDataTrafficHour = new SdDataTrafficHour();
                    try {
                        sdDataTrafficHour.setStatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(struStartTime));
                        String year = String.format("%04d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.wYear);
                        sdDataTrafficHour.setYear(Long.parseLong(year));
                        String month = String.format("%02d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.byMonth);
                        sdDataTrafficHour.setMonth(Long.parseLong(month));
                        String day = String.format("%02d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.byDay);
                        sdDataTrafficHour.setDay(Long.parseLong(day));
                        String hour = String.format("%02d", netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struStartTime.byHour);
                        sdDataTrafficHour.setHour(Long.parseLong(hour));
                        //采集单个车道TPS交通信息
                        byte lane = 0;
                        byte speed = 0;
                        int lightVehicle = 0;
                        int midVehicle = 0;
                        int heavyVehicle = 0;
                        for (int i = 0; i <= HCNetSDK.MAX_TPS_RULE; i++) {
                            byte byLane1 = netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struLaneParam[i].byLane;
                            if (byLane1 != 0 && "null".equals(String.valueOf(byLane1))) {
                                lane = byLane1;
                            }
                            byte bySpeed1 = netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struLaneParam[i].bySpeed;
                            if (bySpeed1 != 0 && "null".equals(String.valueOf(bySpeed1))) {
                                speed = bySpeed1;
                            }
                            int dwLightVehicle = netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struLaneParam[i].dwLightVehicle;
                            if (dwLightVehicle != 0 && "null".equals(String.valueOf(dwLightVehicle))) {
                                lightVehicle = dwLightVehicle;
                            }
                            int dwMidVehicle = netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struLaneParam[i].dwMidVehicle;
                            if (dwMidVehicle != 0 && "null".equals(String.valueOf(dwMidVehicle))) {
                                midVehicle = dwMidVehicle;
                            }
                            int dwHeavyVehicle = netDvrTpsStatisticsInfo.struTPSStatisticsInfo.struLaneParam[i].dwHeavyVehicle;
                            if (dwHeavyVehicle != 0 && "null".equals(String.valueOf(dwHeavyVehicle))) {
                                heavyVehicle = dwHeavyVehicle;
                            }
                        }
                        int sumVehicleNumber = lightVehicle + midVehicle + heavyVehicle;
                        String direction = "上行";
                        if (devices1.getEqDirection() != null && !devices1.getEqDirection().equals("") && devices1.getEqDirection() == "1") {
                            direction = "下行";
                        }
                        sdDataTrafficHour.setDirection(direction);
                        sdDataTrafficHour.setSegmentNo(devices1.getEqTunnelId() + devices1.getPile() + direction + String.valueOf(lane));
                        sdDataTrafficHour.setTotalFlow(Long.parseLong(String.valueOf(sumVehicleNumber)));
                        sdDataTrafficHour.setFlowSveh(Long.parseLong(String.valueOf(lightVehicle)));
                        sdDataTrafficHour.setFlowMveh(Long.parseLong(String.valueOf(midVehicle)));
                        sdDataTrafficHour.setFlowBveh(Long.parseLong(String.valueOf(heavyVehicle)));
                        sdDataTrafficHour.setAvgSpeed(String.valueOf(speed));
                        sdDataTrafficHour.setTunnelId(devices1.getEqTunnelId());
                        SdTunnels sdTunnels = iSdTunnelsService.selectSdTunnelsById(devices1.getEqTunnelId());
                        sdDataTrafficHour.setTunnelName(sdTunnels.getTunnelName());
                        sdDataTrafficHour.setDeviceId(devices1.getEqId());
                        sdDataTrafficHour.setEqType(devices1.getEqType());
                        iSdDataTrafficHourService.insertSdDataTrafficHour(sdDataTrafficHour);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("通道号：" + Tpschannel + "; 开始统计时间：" + struStartTime + "有效车道总数：" + TotalLaneNum);
                break;
            //  客流量报警信息
            case HCNetSDK.COMM_ALARM_PDC:
                HCNetSDK.NET_DVR_PDC_ALRAM_INFO strPDCResult = new HCNetSDK.NET_DVR_PDC_ALRAM_INFO();
                strPDCResult.write();
                Pointer pPDCInfo = strPDCResult.getPointer();
                pPDCInfo.write(0, pAlarmInfo.getByteArray(0, strPDCResult.size()), 0, strPDCResult.size());
                strPDCResult.read();
                // byMode=0-实时统计结果(联合体中struStatFrame有效)，
                if (strPDCResult.byMode == 0) {
                    strPDCResult.uStatModeParam.setType(HCNetSDK.NET_DVR_STATFRAME.class);
                    String sAlarmPDC0Info = "实时客流量统计，进入人数：" + strPDCResult.dwEnterNum + "，离开人数：" + strPDCResult.dwLeaveNum +
                            ", byMode:" + strPDCResult.byMode + ", dwRelativeTime:" + strPDCResult.uStatModeParam.struStatFrame.dwRelativeTime +
                            ", dwAbsTime:" + strPDCResult.uStatModeParam.struStatFrame.dwAbsTime;
                }
                // byMode=1-周期统计结果(联合体中struStatTime有效)，
                if (strPDCResult.byMode == 1) {
                    strPDCResult.uStatModeParam.setType(HCNetSDK.NET_DVR_STATTIME.class);
                    String strtmStart = "" + String.format("%04d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwYear) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwMonth) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwDay) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwHour) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwMinute) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwSecond);
                    String strtmEnd = "" + String.format("%04d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwYear) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwMonth) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwDay) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwHour) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwMinute) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwSecond);
                    String sAlarmPDC1Info = "周期性客流量统计，进入人数：" + strPDCResult.dwEnterNum + "，离开人数：" + strPDCResult.dwLeaveNum +
                            ", byMode:" + strPDCResult.byMode + ", tmStart:" + strtmStart + ",tmEnd :" + strtmEnd;
                }
                break;
            case HCNetSDK.COMM_ALARM_V30:  //移动侦测、视频丢失、遮挡、IO信号量等报警信息(V3.0以上版本支持的设备)
                HCNetSDK.NET_DVR_ALARMINFO_V30 struAlarmInfo = new HCNetSDK.NET_DVR_ALARMINFO_V30();
                struAlarmInfo.write();
                Pointer pAlarmInfo_V30 = struAlarmInfo.getPointer();
                pAlarmInfo_V30.write(0, pAlarmInfo.getByteArray(0, struAlarmInfo.size()), 0, struAlarmInfo.size());
                struAlarmInfo.read();
                System.out.println("报警类型：" + struAlarmInfo.dwAlarmType);  // 3-移动侦测
                break;
            case HCNetSDK.COMM_ALARM_V40: //移动侦测、视频丢失、遮挡、IO信号量等报警信息，报警数据为可变长
                HCNetSDK.NET_DVR_ALARMINFO_V40 struAlarmInfoV40 = new HCNetSDK.NET_DVR_ALARMINFO_V40();
                struAlarmInfoV40.write();
                Pointer pAlarmInfoV40 = struAlarmInfoV40.getPointer();
                pAlarmInfoV40.write(0, pAlarmInfo.getByteArray(0, struAlarmInfoV40.size()), 0, struAlarmInfoV40.size());
                struAlarmInfoV40.read();
                System.out.println("报警类型:" + struAlarmInfoV40.struAlarmFixedHeader.dwAlarmType); //3-移动侦测
                break;
            case HCNetSDK.COMM_FIREDETECTION_ALARM://烟火检测
                HCNetSDK.NET_DVR_FIREDETECTION_ALARM struFireDecAlarm = new HCNetSDK.NET_DVR_FIREDETECTION_ALARM();
                struFireDecAlarm.write();
                Pointer pFireDecAlarm = struFireDecAlarm.getPointer();
                pFireDecAlarm.write(0, pAlarmInfo.getByteArray(0, struFireDecAlarm.size()), 0, struFireDecAlarm.size());
                struFireDecAlarm.read();
                String sFireDecAlarmInfo = "绝对时间：" + struFireDecAlarm.dwAbsTime + ",报警子类型：" + struFireDecAlarm.byAlarmSubType + ",火点最高温度 :" +
                        struFireDecAlarm.wFireMaxTemperature + ",火点目标距离：" + struFireDecAlarm.wTargetDistance;
                System.out.println(sFireDecAlarmInfo);
                //可见光图片保存
                if ((struFireDecAlarm.dwVisiblePicLen > 0) && (struFireDecAlarm.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;

                    try {
                        String filename = "../pic/" + newName + "_FireDecAlarm" + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struFireDecAlarm.pVisiblePicBuf.getByteBuffer(offset, struFireDecAlarm.dwVisiblePicLen);
                        byte[] bytes = new byte[struFireDecAlarm.dwVisiblePicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //热成像图片保存
                if ((struFireDecAlarm.dwPicDataLen > 0) && (struFireDecAlarm.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;

                    try {
                        String filename = "../pic/" + newName + "_" + "_ThermalFireAlarm" + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struFireDecAlarm.pBuffer.getByteBuffer(offset, struFireDecAlarm.dwPicDataLen);
                        byte[] bytes = new byte[struFireDecAlarm.dwPicDataLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                break;
            case HCNetSDK.COMM_UPLOAD_AIOP_VIDEO: //AI开放平台接入视频检测报警信息
                System.out.println("AI开放平台接入视频检测报警上传");
                HCNetSDK.NET_AIOP_VIDEO_HEAD struAIOPVideo = new HCNetSDK.NET_AIOP_VIDEO_HEAD();
                struAIOPVideo.write();
                Pointer pAIOPVideo = struAIOPVideo.getPointer();
                pAIOPVideo.write(0, pAlarmInfo.getByteArray(0, struAIOPVideo.size()), 0, struAIOPVideo.size());
                struAIOPVideo.read();
                System.out.println("视频任务ID" + new String(struAIOPVideo.szTaskID));
                System.out.println("通道号：" + struAIOPVideo.dwChannel);
                System.out.println("检测模型包ID" + new String(struAIOPVideo.szMPID));
                String strTime = String.format("%04d", struAIOPVideo.struTime.wYear) +
                        String.format("%02d", struAIOPVideo.struTime.wMonth) +
                        String.format("%02d", struAIOPVideo.struTime.wDay) +
                        String.format("%02d", struAIOPVideo.struTime.wHour) +
                        String.format("%02d", struAIOPVideo.struTime.wMinute) +
                        String.format("%02d", struAIOPVideo.struTime.wSecond) +
                        String.format("%03d", struAIOPVideo.struTime.wMilliSec);
                //AIOPData数据
                if (struAIOPVideo.dwAIOPDataSize > 0) {
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + new String(pAlarmer.sDeviceIP).trim() +
                                "_" + strTime + "_VideoData.json";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struAIOPVideo.pBufferAIOPData.getByteBuffer(offset, struAIOPVideo.dwAIOPDataSize);
                        byte[] bytes = new byte[struAIOPVideo.dwAIOPDataSize];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //图片数据保存
                if (struAIOPVideo.dwPictureSize > 0) {
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + new String(pAlarmer.sDeviceIP).trim() +
                                "_" + strTime + "_VideoPic.jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struAIOPVideo.pBufferPicture.getByteBuffer(offset, struAIOPVideo.dwPictureSize);
                        byte[] bytes = new byte[struAIOPVideo.dwPictureSize];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case HCNetSDK.COMM_UPLOAD_AIOP_PICTURE: //AI开放平台接入图片检测报警信息
                System.out.println("AI开放平台接入图片检测报警上传");
                HCNetSDK.NET_AIOP_PICTURE_HEAD struAIOPPic = new HCNetSDK.NET_AIOP_PICTURE_HEAD();
                struAIOPPic.write();
                Pointer pAIOPPic = struAIOPPic.getPointer();
                pAIOPPic.write(0, pAlarmInfo.getByteArray(0, struAIOPPic.size()), 0, struAIOPPic.size());
                struAIOPPic.read();
                System.out.println("图片ID：" + new String(struAIOPPic.szPID));
                System.out.println("检测模型包ID：" + new String(struAIOPPic.szMPID));
                String strPicTime = "" + String.format("%04d", struAIOPPic.struTime.wYear) +
                        String.format("%02d", struAIOPPic.struTime.wMonth) +
                        String.format("%02d", struAIOPPic.struTime.wDay) +
                        String.format("%02d", struAIOPPic.struTime.wHour) +
                        String.format("%02d", struAIOPPic.struTime.wMinute) +
                        String.format("%02d", struAIOPPic.struTime.wSecond) +
                        String.format("%03d", struAIOPPic.struTime.wMilliSec);
                //AIOPData数据
                if (struAIOPPic.dwAIOPDataSize > 0) {
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + new String(pAlarmer.sDeviceIP).trim() +
                                "_" + strPicTime + "_AIO_PicData.json";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struAIOPPic.pBufferAIOPData.getByteBuffer(offset, struAIOPPic.dwAIOPDataSize);
                        byte[] bytes = new byte[struAIOPPic.dwAIOPDataSize];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            default:
                System.out.println("报警类型" + Integer.toHexString(lCommand));
                break;
        }
    }
}





