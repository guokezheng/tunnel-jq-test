package com.tunnel.deal.hik;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import com.sun.jna.Pointer;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdWarningInfo;
import com.tunnel.business.domain.event.SdWarningType;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.event.ISdWarningInfoService;
import com.tunnel.business.service.event.ISdWarningTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
public class FMSGCallBack implements HCNetSDK.FMSGCallBack {

    @Autowired
    private ISdWarningInfoService iSdWarningInfoService;
    @Autowired
    private ISdDevicesService iSdDevicesService;
    @Autowired
    private ISysDictDataService iSysDictDataService;
    @Autowired
    private ISdWarningTypeService iSdWarningTypeService;

    @Override
    public void invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
                       Pointer pUser) {
        AlarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
    }

    public void AlarmDataHandle(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        try {
            String sAlarmType = new String();
            // 存储设备报警信息
//			DefaultTableModel alarmTableModel = ((DefaultTableModel) jTableAlarm.getModel());//获取表格模型
            String[] newRow = new String[3];
            //报警时间
            Date today = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String[] sIP = new String[2];

            sAlarmType = new String("lCommand=0x") + Integer.toHexString(lCommand);
            //lCommand是传的报警类型
            switch (lCommand) {
                case HCNetSDK.COMM_ALARM_V40:
                    HCNetSDK.NET_DVR_ALARMINFO_V40 struAlarmInfoV40 = new HCNetSDK.NET_DVR_ALARMINFO_V40();
                    struAlarmInfoV40.write();
                    Pointer pInfoV40 = struAlarmInfoV40.getPointer();
                    pInfoV40.write(0, pAlarmInfo.getByteArray(0, struAlarmInfoV40.size()), 0, struAlarmInfoV40.size());
                    struAlarmInfoV40.read();

                    switch (struAlarmInfoV40.struAlarmFixedHeader.dwAlarmType) {
                        case 0:
                            struAlarmInfoV40.struAlarmFixedHeader.ustruAlarm.setType(HCNetSDK.struIOAlarm.class);
                            struAlarmInfoV40.read();
                            sAlarmType = sAlarmType + new String("：信号量报警") + "，" + "报警输入口：" + struAlarmInfoV40.struAlarmFixedHeader.ustruAlarm.struioAlarm.dwAlarmInputNo;
                            break;
                        case 1:
                            sAlarmType = sAlarmType + new String("：硬盘满");
                            break;
                        case 2:
                            sAlarmType = sAlarmType + new String("：信号丢失");
                            break;
                        case 3:
                            struAlarmInfoV40.struAlarmFixedHeader.ustruAlarm.setType(HCNetSDK.struAlarmChannel.class);
                            struAlarmInfoV40.read();
                            int iChanNum = struAlarmInfoV40.struAlarmFixedHeader.ustruAlarm.sstrualarmChannel.dwAlarmChanNum;
                            sAlarmType = sAlarmType + new String("：移动侦测") + "，" + "报警通道个数：" + iChanNum + "，" + "报警通道号：";

                            for (int i = 0; i < iChanNum; i++) {
                                byte[] byChannel = struAlarmInfoV40.pAlarmData.getByteArray(i * 4, 4);

                                int iChanneNo = 0;
                                for (int j = 0; j < 4; j++) {
                                    int ioffset = j * 8;
                                    int iByte = byChannel[j] & 0xff;
                                    iChanneNo = iChanneNo + (iByte << ioffset);
                                }

                                sAlarmType = sAlarmType + "+ch[" + iChanneNo + "]";
                            }

                            break;
                        case 4:
                            sAlarmType = sAlarmType + new String("：硬盘未格式化");
                            break;
                        case 5:
                            sAlarmType = sAlarmType + new String("：读写硬盘出错");
                            break;
                        case 6:
                            sAlarmType = sAlarmType + new String("：遮挡报警");
                            break;
                        case 7:
                            sAlarmType = sAlarmType + new String("：制式不匹配");
                            break;
                        case 8:
                            sAlarmType = sAlarmType + new String("：非法访问");
                            break;
                    }

                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);
                    break;
                case HCNetSDK.COMM_ALARM_V30:
                    HCNetSDK.NET_DVR_ALARMINFO_V30 strAlarmInfoV30 = new HCNetSDK.NET_DVR_ALARMINFO_V30();
                    strAlarmInfoV30.write();
                    Pointer pInfoV30 = strAlarmInfoV30.getPointer();
                    pInfoV30.write(0, pAlarmInfo.getByteArray(0, strAlarmInfoV30.size()), 0, strAlarmInfoV30.size());
                    strAlarmInfoV30.read();
                    switch (strAlarmInfoV30.dwAlarmType) {
                        case 0:
                            sAlarmType = sAlarmType + new String("：信号量报警") + "，" + "报警输入口：" + (strAlarmInfoV30.dwAlarmInputNumber + 1);
                            break;
                        case 1:
                            sAlarmType = sAlarmType + new String("：硬盘满");
                            break;
                        case 2:
                            sAlarmType = sAlarmType + new String("：信号丢失");
                            break;
                        case 3:
                            sAlarmType = sAlarmType + new String("：移动侦测") + "，" + "报警通道：";
                            for (int i = 0; i < 64; i++) {
                                if (strAlarmInfoV30.byChannel[i] == 1) {
                                    sAlarmType = sAlarmType + "ch" + (i + 1) + " ";
                                }
                            }
                            break;
                        case 4:
                            sAlarmType = sAlarmType + new String("：硬盘未格式化");
                            break;
                        case 5:
                            sAlarmType = sAlarmType + new String("：读写硬盘出错");
                            break;
                        case 6:
                            sAlarmType = sAlarmType + new String("：遮挡报警");
                            break;
                        case 7:
                            sAlarmType = sAlarmType + new String("：制式不匹配");
                            break;
                        case 8:
                            sAlarmType = sAlarmType + new String("：非法访问");
                            break;
                    }
                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);
                    break;
                case HCNetSDK.COMM_ALARM_RULE:
                    HCNetSDK.NET_VCA_RULE_ALARM strVcaAlarm = new HCNetSDK.NET_VCA_RULE_ALARM();
                    strVcaAlarm.write();
                    Pointer pVcaInfo = strVcaAlarm.getPointer();
                    pVcaInfo.write(0, pAlarmInfo.getByteArray(0, strVcaAlarm.size()), 0, strVcaAlarm.size());
                    strVcaAlarm.read();

                    switch (strVcaAlarm.struRuleInfo.wEventTypeEx) {
                        case 1:
                            sAlarmType = sAlarmType + new String("：穿越警戒面") + "，" +
                                    "_wPort:" + strVcaAlarm.struDevInfo.wPort +
                                    "_byChannel:" + strVcaAlarm.struDevInfo.byChannel +
                                    "_byIvmsChannel:" + strVcaAlarm.struDevInfo.byIvmsChannel +
                                    "_Dev IP：" + new String(strVcaAlarm.struDevInfo.struDevIP.sIpV4);
                            break;
                        case 2:
                            sAlarmType = sAlarmType + new String("：目标进入区域") + "，" +
                                    "_wPort:" + strVcaAlarm.struDevInfo.wPort +
                                    "_byChannel:" + strVcaAlarm.struDevInfo.byChannel +
                                    "_byIvmsChannel:" + strVcaAlarm.struDevInfo.byIvmsChannel +
                                    "_Dev IP：" + new String(strVcaAlarm.struDevInfo.struDevIP.sIpV4);
                            break;
                        case 3:
                            sAlarmType = sAlarmType + new String("：目标离开区域") + "，" +
                                    "_wPort:" + strVcaAlarm.struDevInfo.wPort +
                                    "_byChannel:" + strVcaAlarm.struDevInfo.byChannel +
                                    "_byIvmsChannel:" + strVcaAlarm.struDevInfo.byIvmsChannel +
                                    "_Dev IP：" + new String(strVcaAlarm.struDevInfo.struDevIP.sIpV4);
                            break;
                        default:
                            sAlarmType = sAlarmType + new String("：其他行为分析报警，事件类型：")
                                    + strVcaAlarm.struRuleInfo.wEventTypeEx +
                                    "_wPort:" + strVcaAlarm.struDevInfo.wPort +
                                    "_byChannel:" + strVcaAlarm.struDevInfo.byChannel +
                                    "_byIvmsChannel:" + strVcaAlarm.struDevInfo.byIvmsChannel +
                                    "_Dev IP：" + new String(strVcaAlarm.struDevInfo.struDevIP.sIpV4);
                            break;
                    }
                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);

                    if (strVcaAlarm.dwPicDataLen > 0) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            fout = new FileOutputStream(".\\pic\\" + new String(pAlarmer.sDeviceIP).trim()
                                    + "wEventTypeEx[" + strVcaAlarm.struRuleInfo.wEventTypeEx + "]_" + newName + "_vca.jpg");
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = strVcaAlarm.pImage.getByteBuffer(offset, strVcaAlarm.dwPicDataLen);
                            byte[] bytes = new byte[strVcaAlarm.dwPicDataLen];
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
                case HCNetSDK.COMM_UPLOAD_PLATE_RESULT:
                    HCNetSDK.NET_DVR_PLATE_RESULT strPlateResult = new HCNetSDK.NET_DVR_PLATE_RESULT();
                    strPlateResult.write();
                    Pointer pPlateInfo = strPlateResult.getPointer();
                    pPlateInfo.write(0, pAlarmInfo.getByteArray(0, strPlateResult.size()), 0, strPlateResult.size());
                    strPlateResult.read();
                    try {
                        String srt3 = new String(strPlateResult.struPlateInfo.sLicense, "GBK");
                        sAlarmType = sAlarmType + "：交通抓拍上传，车牌：" + srt3;
                    } catch (UnsupportedEncodingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);

                    if (strPlateResult.dwPicLen > 0) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            fout = new FileOutputStream(".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() + "_"
                                    + newName + "_plateResult.jpg");
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = strPlateResult.pBuffer1.getByteBuffer(offset, strPlateResult.dwPicLen);
                            byte[] bytes = new byte[strPlateResult.dwPicLen];
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
                case HCNetSDK.COMM_ITS_PLATE_RESULT:
                    HCNetSDK.NET_ITS_PLATE_RESULT strItsPlateResult = new HCNetSDK.NET_ITS_PLATE_RESULT();
                    strItsPlateResult.write();
                    Pointer pItsPlateInfo = strItsPlateResult.getPointer();
                    pItsPlateInfo.write(0, pAlarmInfo.getByteArray(0, strItsPlateResult.size()), 0, strItsPlateResult.size());
                    strItsPlateResult.read();
                    try {
                        String srt3 = new String(strItsPlateResult.struPlateInfo.sLicense, "GBK");
                        sAlarmType = sAlarmType + ",车辆类型：" + strItsPlateResult.byVehicleType + ",交通抓拍上传，车牌：" + srt3;
                    } catch (UnsupportedEncodingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);

                    for (int i = 0; i < strItsPlateResult.dwPicNum; i++) {
                        if (strItsPlateResult.struPicInfo[i].dwDataLen > 0) {
                            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                            String newName = sf.format(new Date());
                            FileOutputStream fout;
                            try {
                                String filename = ".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() + "_"
                                        + newName + "_type[" + strItsPlateResult.struPicInfo[i].byType + "]_ItsPlate.jpg";
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
                case HCNetSDK.COMM_ALARM_PDC:
                    HCNetSDK.NET_DVR_PDC_ALRAM_INFO strPDCResult = new HCNetSDK.NET_DVR_PDC_ALRAM_INFO();
                    strPDCResult.write();
                    Pointer pPDCInfo = strPDCResult.getPointer();
                    pPDCInfo.write(0, pAlarmInfo.getByteArray(0, strPDCResult.size()), 0, strPDCResult.size());
                    strPDCResult.read();

                    if (strPDCResult.byMode == 0) {
                        strPDCResult.uStatModeParam.setType(HCNetSDK.NET_DVR_STATFRAME.class);
                        sAlarmType = sAlarmType + "：客流量统计，进入人数：" + strPDCResult.dwEnterNum + "，离开人数：" + strPDCResult.dwLeaveNum +
                                ", byMode:" + strPDCResult.byMode + ", dwRelativeTime:" + strPDCResult.uStatModeParam.struStatFrame.dwRelativeTime +
                                ", dwAbsTime:" + strPDCResult.uStatModeParam.struStatFrame.dwAbsTime;
                    }
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
                        sAlarmType = sAlarmType + "：客流量统计，进入人数：" + strPDCResult.dwEnterNum + "，离开人数：" + strPDCResult.dwLeaveNum +
                                ", byMode:" + strPDCResult.byMode + ", tmStart:" + strtmStart + ",tmEnd :" + strtmEnd;
                    }

                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(strPDCResult.struDevInfo.struDevIP.sIpV4).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);
                    break;
                case HCNetSDK.COMM_ALARM_TFS:
                    // 违章取证报警信息获取
                    HCNetSDK.NET_DVR_TFS_ALARM strTFSAlarmInfo = new HCNetSDK.NET_DVR_TFS_ALARM();
                    strTFSAlarmInfo.write();
                    Pointer pTFSInfo = strTFSAlarmInfo.getPointer();
                    pTFSInfo.write(0, pAlarmInfo.getByteArray(0, strTFSAlarmInfo.size()), 0, strTFSAlarmInfo.size());
                    strTFSAlarmInfo.read();
                    String warningType = "";
                    String pictures = "";
                    try {
                        //车牌号码
                        String srtPlate = new String(strTFSAlarmInfo.struPlateInfo.sLicense, "GBK").trim();
                        //违章类型编号
                        int dwIllegalType = strTFSAlarmInfo.dwIllegalType;
                        List<SysDictData> illegalTypeList = iSysDictDataService.getSysDictDataByDictType("sd_HKDevice_dwIllegalType");
                        String illegalTypeName = "";
                        if (illegalTypeList.size() > 0) {
                            for (int j = 0; j < illegalTypeList.size(); j++) {
                                SysDictData sysDictData = illegalTypeList.get(j);
                                String dictValue = sysDictData.getDictValue();
                                if (dictValue.equals(String.valueOf(dwIllegalType))) {
                                    warningType = sysDictData.getDictLabel();
                                    illegalTypeName = sysDictData.getDictLabel() + "(" + sysDictData.getRemark() + ")";
                                }
                            }
                        } else {
                            illegalTypeName = "当前违章类型编号未知";
                        }
                        //车辆类型
                        byte bySpecificVehicleType = strTFSAlarmInfo.bySpecificVehicleType;
                        String carType = "未知";
                        switch (bySpecificVehicleType) {
                            case 1:
                                carType = "客车";
                                break;
                            case 2:
                                carType = "货车";
                                break;
                            case 3:
                                carType = "轿车";
                                break;
                            case 4:
                                carType = "面包车";
                                break;
                            case 5:
                                carType = "小货车";
                                break;
                            case 6:
                                carType = "行人";
                                break;
                            case 7:
                                carType = "二轮车";
                                break;
                            case 8:
                                carType = "三轮车";
                                break;
                        }
                        sAlarmType = sAlarmType + "：交通取证报警信息，违章类型：" + illegalTypeName + "，车牌号码：" + srtPlate
                                + "，车型为：" + carType;
//								+ "，车辆出入状态：" + strTFSAlarmInfo.struAIDInfo.byVehicleEnterState;
                        //违章拍照照片存储
                        if (strTFSAlarmInfo.dwPicNum > 0 && strTFSAlarmInfo.struPicInfo != null) {
                            for (int i = 0; i < strTFSAlarmInfo.dwPicNum; i++) {
                                if (strTFSAlarmInfo.struPicInfo[i].dwDataLen > 0) {
                                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String newName = sf.format(new Date());
                                    FileOutputStream fout;
                                    try {
//										String filename = ".\\pic\\"+ new String(pAlarmer.sDeviceIP).trim() + "_"
//												+ newName+"_type["+strTFSAlarmInfo.struPicInfo[i].byType+"]_ItsPlate.jpg";
                                        File filepath = new File(RuoYiConfig.getUploadPath() + "/HKDevicePicture");
                                        if (!filepath.exists()) {
                                            filepath.mkdirs();
                                        }
                                        String filename = filepath + "/" + new String(pAlarmer.sDeviceIP).trim() + "_"
                                                + newName + "_type[" + strTFSAlarmInfo.struPicInfo[i].byType + "]_ItsPlate.jpg";
                                        fout = new FileOutputStream(filename);
                                        //将字节写入文件
                                        long offset = 0;
                                        ByteBuffer buffers = strTFSAlarmInfo.struPicInfo[i].pBuffer.getByteBuffer(offset, strTFSAlarmInfo.struPicInfo[i].dwDataLen);
                                        byte[] bytes = new byte[strTFSAlarmInfo.struPicInfo[i].dwDataLen];
                                        buffers.rewind();
                                        buffers.get(bytes);
                                        fout.write(bytes);
                                        fout.close();
                                        //filename-->图片
                                        //拼接所有图片作为字符串存储
                                        pictures = pictures + filename + ";";
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 报警时间
                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(strTFSAlarmInfo.struDevInfo.struDevIP.sIpV4).split("\0", 2);
                    //设备信息
                    SdDevices sdDevices = new SdDevices();
                    String join = String.join(".", sIP);
                    System.out.println("------------------------设备IPV4地址--------------------------:" + join);
                    List<SdDevices> devices = iSdDevicesService.selectSdDevicesList(sdDevices);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);
                    //存库
                    SdWarningType sdWarningType = new SdWarningType();
                    sdWarningType.setTypeName(warningType);
                    List<SdWarningType> sdWarningTypes = iSdWarningTypeService.selectSdWarningTypeList(sdWarningType);
                    long warningTypeId = 0L;
                    if (sdWarningTypes.size() > 0) {
                        warningTypeId = sdWarningTypes.get(0).getId();
                    }
                    SdDevices device = devices.get(0);
                    SdWarningInfo sdWarningInfo = new SdWarningInfo();
                    sdWarningInfo.setEqId(device.getEqId());
                    sdWarningInfo.setTunnelId(device.getEqTunnelId());
                    sdWarningInfo.setPosition(device.getPile());
                    String direction = "上行";
                    if (device.getEqDirection() != null && !device.getEqDirection().equals("") && device.getEqDirection() == "1") {
                        direction = "下行";
                    }
                    sdWarningInfo.setInforSources(device.getEqTunnelId() + direction + device.getPile() + sAlarmType);
                    sdWarningInfo.setProcessState("0");
                    if (warningTypeId != 0) {
                        sdWarningInfo.setWarningTypeId(warningTypeId);
                        sdWarningInfo.setWarningName(warningType);
                    }
                    List<SdWarningInfo> sdWarningInfos = iSdWarningInfoService.selectSdWarningInfoList(sdWarningInfo);
                    if (sdWarningInfos.size() > 0) {
                        sdWarningInfo.setWarningTime(new Date());
                        sdWarningInfo.setUpdateTime(new Date());
                        sdWarningInfo.setUpdateBy("系统");
                        iSdWarningInfoService.updateSdWarningInfo(sdWarningInfos.get(0));
                    } else {
                        sdWarningInfo.setWarningTime(new Date());
                        sdWarningInfo.setCreateTime(new Date());
                        sdWarningInfo.setCreateBy("系统");
                        sdWarningInfo.setPicture(pictures);
                        iSdWarningInfoService.insertSdWarningInfo(sdWarningInfo);
                    }
                    break;
                case HCNetSDK.COMM_ALARM_AID_V41:
                    HCNetSDK.NET_DVR_AID_ALARM_V41 struAIDAlarmInfo = new HCNetSDK.NET_DVR_AID_ALARM_V41();
                    struAIDAlarmInfo.write();
                    Pointer pAIDInfo = struAIDAlarmInfo.getPointer();
                    pAIDInfo.write(0, pAlarmInfo.getByteArray(0, struAIDAlarmInfo.size()), 0, struAIDAlarmInfo.size());
                    struAIDAlarmInfo.read();
                    int dwAIDTypeEx = struAIDAlarmInfo.struAIDInfo.dwAIDTypeEx;
                    // 交通事件类型编号
                    int trafficEventNumber = 0;
                    if (dwAIDTypeEx != 0) {
                        trafficEventNumber = dwAIDTypeEx;
                    } else {
                        trafficEventNumber = struAIDAlarmInfo.struAIDInfo.dwAIDType;
                    }
                    String trafficEventName = "未知";
                    if (trafficEventNumber != 0) {
                        switch (trafficEventNumber) {
                            case 1:
                                trafficEventName = "拥堵";
                                break;
                            case 2:
                                trafficEventName = "停车";
                                break;
                            case 3:
                                trafficEventName = "逆行";
                                break;
                            case 4:
                                trafficEventName = "行人";
                                break;
                            case 5:
                                trafficEventName = "抛洒物";
                                break;
                            case 6:
                                trafficEventName = "烟雾";
                                break;
                            case 7:
                                trafficEventName = "压线";
                                break;
                            case 8:
                                trafficEventName = "黑名单数据";
                                break;
                            case 9:
                                trafficEventName = "超速";
                                break;
                            case 10:
                                trafficEventName = "变道";
                                break;
                            case 11:
                                trafficEventName = "掉头";
                                break;
                            case 12:
                                trafficEventName = "机占非";
                                break;
                            case 13:
                                trafficEventName = "加塞";
                                break;
                            case 14:
                                trafficEventName = "路障";
                                break;
                            case 15:
                                trafficEventName = "施工";
                                break;
                            case 16:
                                trafficEventName = "交通事故检测";
                                break;
                            case 18:
                                trafficEventName = "浓雾检测";
                                break;
                            case 19:
                                trafficEventName = "占用紧急车道";
                                break;
                            case 20:
                                trafficEventName = "火灾";
                                break;
                            case 23:
                                trafficEventName = "占用超车道";
                                break;
                            case 24:
                                trafficEventName = "违反禁令标志";
                                break;
                            case 32:
                                trafficEventName = "飙车";
                                break;
                            case 33:
                                trafficEventName = "连续变道";
                                break;
                            case 34:
                                trafficEventName = "蛇形行驶";
                                break;
                            case 35:
                                trafficEventName = "大车占道";
                                break;
                            case 36:
                                trafficEventName = "障碍物";
                                break;
                        }
                    }
                    sAlarmType = sAlarmType + "：交通事件报警信息，交通事件类型：" + trafficEventName + "，规则ID："
                            + struAIDAlarmInfo.struAIDInfo.byRuleID + "，车辆出入状态：" + struAIDAlarmInfo.struAIDInfo.byVehicleEnterState;
                    //获取照片数据
                    File filepath = new File(RuoYiConfig.getUploadPath() + "/HKDevicePicture");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newNames = sdf.format(new Date());
                    String picturesList = "";
                    if (struAIDAlarmInfo.dwPicDataLen > 0) {
//						for(int i=0;i<struAIDAlarmInfo.dwPicDataLen;i++)
//						{
//							try {
//								Pointer pImage = struAIDAlarmInfo.pImage;
//								byte[] imgByteArray = pImage.getByteArray(0, struAIDAlarmInfo.dwPicDataLen);
//								BufferedImage bufferedImage = new BufferedImage(120, 120, BufferedImage.TYPE_BYTE_GRAY);
//								bufferedImage.getRaster().setDataElements(0,0,120,120,imgByteArray);
//								ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//								ImageIO.write(bufferedImage,"jpg",new File(filepath+newNames+trafficEventName+".jpg"));
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//
//						}
                        for (int i = 0; i < struAIDAlarmInfo.dwPicDataLen; i++) {
                            HCNetSDK.NET_DVR_ALARM_ISAPI_PICDATA struPicData = new HCNetSDK.NET_DVR_ALARM_ISAPI_PICDATA();
                            struPicData.write();
                            Pointer pPicData = struPicData.getPointer();
                            pPicData.write(0, struAIDAlarmInfo.pImage.getByteArray(i * struPicData.size(), struPicData.size()), 0, struPicData.size());
                            struPicData.read();
                            FileOutputStream fout;
                            try {
                                File filepaths = new File(RuoYiConfig.getUploadPath() + "/HKDevicePicture");
                                if (!filepaths.exists()) {
                                    filepaths.mkdirs();
                                }
                                String filename = filepath + "/" + new String(pAlarmer.sDeviceIP).trim() + newNames +
                                        "_Pic_" + i + "_" + new String(struPicData.szFilename).trim() + ".jpg";
                                fout = new FileOutputStream(filename);
                                //将字节写入文件
                                long offset = 0;
                                ByteBuffer buffers = struPicData.pPicData.getByteBuffer(offset, struPicData.dwPicLen);
                                byte[] bytes = new byte[struPicData.dwPicLen];
                                buffers.rewind();
                                buffers.get(bytes);
                                fout.write(bytes);
                                fout.close();
                                picturesList = picturesList + filename + ";";
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(struAIDAlarmInfo.struDevInfo.struDevIP.sIpV4).split("\0", 2);
                    //设备信息
                    SdDevices dev = new SdDevices();
                    String sip = String.join(".", sIP);
                    System.out.println("------------------------设备IPV4地址--------------------------:" + sip);
                    List<SdDevices> devicesList = iSdDevicesService.selectSdDevicesList(dev);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);
                    SdWarningType warningtype = new SdWarningType();
                    warningtype.setTypeName(trafficEventName);
                    List<SdWarningType> warningTypeList = iSdWarningTypeService.selectSdWarningTypeList(warningtype);
                    long warningTypeid = 0L;
                    if (warningTypeList.size() > 0) {
                        warningTypeid = warningTypeList.get(0).getId();
                    }
                    SdDevices sddevice = devicesList.get(0);
                    SdWarningInfo warningInfo = new SdWarningInfo();
                    warningInfo.setEqId(sddevice.getEqId());
                    warningInfo.setTunnelId(sddevice.getEqTunnelId());
                    warningInfo.setPosition(sddevice.getPile());
                    String directions = "上行";
                    if (sddevice.getEqDirection() != null && !sddevice.getEqDirection().equals("") && sddevice.getEqDirection() == "1") {
                        directions = "下行";
                    }
                    warningInfo.setInforSources(sddevice.getEqTunnelId() + directions + sddevice.getPile() + sAlarmType);
                    warningInfo.setProcessState("0");
                    if (warningTypeid != 0) {
                        warningInfo.setWarningTypeId(warningTypeid);
                        warningInfo.setWarningName(trafficEventName);
                    }
                    List<SdWarningInfo> sdWarningInfosList = iSdWarningInfoService.selectSdWarningInfoList(warningInfo);
                    if (sdWarningInfosList.size() > 0) {
                        warningInfo.setWarningTime(new Date());
                        warningInfo.setUpdateTime(new Date());
                        warningInfo.setUpdateBy("系统");
                        iSdWarningInfoService.updateSdWarningInfo(sdWarningInfosList.get(0));
                    } else {
                        warningInfo.setWarningTime(new Date());
                        warningInfo.setCreateTime(new Date());
                        warningInfo.setCreateBy("系统");
                        warningInfo.setPicture(picturesList);
                        iSdWarningInfoService.insertSdWarningInfo(warningInfo);
                    }
                    break;
                case HCNetSDK.COMM_ALARM_TPS_V41:
                    // 车流量数据采集
                    HCNetSDK.NET_DVR_TPS_ALARM_V41 struTPSAlarmInfo = new HCNetSDK.NET_DVR_TPS_ALARM_V41();
                    struTPSAlarmInfo.write();
                    Pointer pTPSInfo = struTPSAlarmInfo.getPointer();
                    pTPSInfo.write(0, pAlarmInfo.getByteArray(0, struTPSAlarmInfo.size()), 0, struTPSAlarmInfo.size());
                    struTPSAlarmInfo.read();

                    sAlarmType = sAlarmType + "：交通统计报警信息，绝对时标：" + struTPSAlarmInfo.dwAbsTime
                            + "，能见度:" + struTPSAlarmInfo.struDevInfo.byIvmsChannel
                            + "，车道1交通状态:" + struTPSAlarmInfo.struTPSInfo.struLaneParam[0].byTrafficState
                            + "，监测点编号：" + new String(struTPSAlarmInfo.byMonitoringSiteID).trim()
                            + "，设备编号：" + new String(struTPSAlarmInfo.byDeviceID).trim()
                            + "，开始统计时间：" + struTPSAlarmInfo.dwStartTime
                            + "，结束统计时间：" + struTPSAlarmInfo.dwStopTime;
                    System.err.println("sAlarmType:" + sAlarmType);
                    //报警信息
                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(struTPSAlarmInfo.struDevInfo.struDevIP.sIpV4).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);
                    break;
                case HCNetSDK.COMM_UPLOAD_FACESNAP_RESULT:
                    //实时人脸抓拍上传
                    HCNetSDK.NET_VCA_FACESNAP_RESULT strFaceSnapInfo = new HCNetSDK.NET_VCA_FACESNAP_RESULT();
                    strFaceSnapInfo.write();
                    Pointer pFaceSnapInfo = strFaceSnapInfo.getPointer();
                    pFaceSnapInfo.write(0, pAlarmInfo.getByteArray(0, strFaceSnapInfo.size()), 0, strFaceSnapInfo.size());
                    strFaceSnapInfo.read();
                    sAlarmType = sAlarmType + "：人脸抓拍上传，人脸评分：" + strFaceSnapInfo.dwFaceScore + "，年龄段：" + strFaceSnapInfo.struFeature.byAgeGroup + "，性别：" + strFaceSnapInfo.struFeature.bySex;
                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(strFaceSnapInfo.struDevInfo.struDevIP.sIpV4).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); //设置日期格式
                    String time = df.format(new Date()); // new Date()为获取当前系统时间
                    //人脸图片写文件
                    try {
                        FileOutputStream small = new FileOutputStream(System.getProperty("user.dir") + "\\pic\\" + time + "small.jpg");
                        FileOutputStream big = new FileOutputStream(System.getProperty("user.dir") + "\\pic\\" + time + "big.jpg");

                        if (strFaceSnapInfo.dwFacePicLen > 0) {
                            try {
                                small.write(strFaceSnapInfo.pBuffer1.getByteArray(0, strFaceSnapInfo.dwFacePicLen), 0, strFaceSnapInfo.dwFacePicLen);
                                small.close();
                            } catch (IOException ex) {
//								Logger.getLogger(AlarmJavaDemoView.class.getName()).log(Level.SEVERE, null, ex);
                                ex.printStackTrace();
                            }

                        }
                        if (strFaceSnapInfo.dwFacePicLen > 0) {
                            try {
                                big.write(strFaceSnapInfo.pBuffer2.getByteArray(0, strFaceSnapInfo.dwBackgroundPicLen), 0, strFaceSnapInfo.dwBackgroundPicLen);
                                big.close();
                            } catch (IOException ex) {
//								Logger.getLogger(AlarmJavaDemoView.class.getName()).log(Level.SEVERE, null, ex);
                                ex.printStackTrace();
                            }
                        }
                    } catch (FileNotFoundException ex) {
//						Logger.getLogger(AlarmJavaDemoView.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace();
                    }
                    break;
                case HCNetSDK.COMM_SNAP_MATCH_ALARM:
                    //人脸名单比对报警
                    HCNetSDK.NET_VCA_FACESNAP_MATCH_ALARM strFaceSnapMatch = new HCNetSDK.NET_VCA_FACESNAP_MATCH_ALARM();
                    strFaceSnapMatch.write();
                    Pointer pFaceSnapMatch = strFaceSnapMatch.getPointer();
                    pFaceSnapMatch.write(0, pAlarmInfo.getByteArray(0, strFaceSnapMatch.size()), 0, strFaceSnapMatch.size());
                    strFaceSnapMatch.read();

                    if ((strFaceSnapMatch.dwSnapPicLen > 0) && (strFaceSnapMatch.byPicTransType == 0)) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            String filename = System.getProperty("user.dir") + "\\pic\\" + newName + "_pSnapPicBuffer" + ".jpg";
                            fout = new FileOutputStream(filename);
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = strFaceSnapMatch.pSnapPicBuffer.getByteBuffer(offset, strFaceSnapMatch.dwSnapPicLen);
                            byte[] bytes = new byte[strFaceSnapMatch.dwSnapPicLen];
                            buffers.rewind();
                            buffers.get(bytes);
                            fout.write(bytes);
                            fout.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if ((strFaceSnapMatch.struSnapInfo.dwSnapFacePicLen > 0) && (strFaceSnapMatch.byPicTransType == 0)) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            String filename = System.getProperty("user.dir") + "\\pic\\" + newName + "_struSnapInfo_pBuffer1" + ".jpg";
                            fout = new FileOutputStream(filename);
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = strFaceSnapMatch.struSnapInfo.pBuffer1.getByteBuffer(offset, strFaceSnapMatch.struSnapInfo.dwSnapFacePicLen);
                            byte[] bytes = new byte[strFaceSnapMatch.struSnapInfo.dwSnapFacePicLen];
                            buffers.rewind();
                            buffers.get(bytes);
                            fout.write(bytes);
                            fout.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if ((strFaceSnapMatch.struBlockListInfo.dwBlockListPicLen > 0) && (strFaceSnapMatch.byPicTransType == 0)) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            String filename = System.getProperty("user.dir") + "\\pic\\" + newName + "_fSimilarity_" + strFaceSnapMatch.fSimilarity + "_struBlockListInfo_pBuffer1" + ".jpg";
                            fout = new FileOutputStream(filename);
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = strFaceSnapMatch.struBlockListInfo.pBuffer1.getByteBuffer(offset, strFaceSnapMatch.struBlockListInfo.dwBlockListPicLen);
                            byte[] bytes = new byte[strFaceSnapMatch.struBlockListInfo.dwBlockListPicLen];
                            buffers.rewind();
                            buffers.get(bytes);
                            fout.write(bytes);
                            fout.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    sAlarmType = sAlarmType + "：人脸名单比对报警，相识度：" + strFaceSnapMatch.fSimilarity + "，名单姓名：" + new String(strFaceSnapMatch.struBlockListInfo.struBlockListInfo.struAttribute.byName, "GBK").trim() + "，\n名单证件信息：" + new String(strFaceSnapMatch.struBlockListInfo.struBlockListInfo.struAttribute.byCertificateNumber).trim();

                    //获取人脸库ID
                    byte[] FDIDbytes;
                    if ((strFaceSnapMatch.struBlockListInfo.dwFDIDLen > 0) && (strFaceSnapMatch.struBlockListInfo.pFDID != null)) {
                        ByteBuffer FDIDbuffers = strFaceSnapMatch.struBlockListInfo.pFDID.getByteBuffer(0, strFaceSnapMatch.struBlockListInfo.dwFDIDLen);
                        FDIDbytes = new byte[strFaceSnapMatch.struBlockListInfo.dwFDIDLen];
                        FDIDbuffers.rewind();
                        FDIDbuffers.get(FDIDbytes);
                        sAlarmType = sAlarmType + "，人脸库ID:" + new String(FDIDbytes).trim();
                    }
                    //获取人脸图片ID
                    byte[] PIDbytes;
                    if ((strFaceSnapMatch.struBlockListInfo.dwPIDLen > 0) && (strFaceSnapMatch.struBlockListInfo.pPID != null)) {
                        ByteBuffer PIDbuffers = strFaceSnapMatch.struBlockListInfo.pPID.getByteBuffer(0, strFaceSnapMatch.struBlockListInfo.dwPIDLen);
                        PIDbytes = new byte[strFaceSnapMatch.struBlockListInfo.dwPIDLen];
                        PIDbuffers.rewind();
                        PIDbuffers.get(PIDbytes);
                        sAlarmType = sAlarmType + "，人脸图片ID:" + new String(PIDbytes).trim();
                    }
                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);
                    break;
                case HCNetSDK.COMM_UPLOAD_AIOP_VIDEO: //设备支持AI开放平台接入，上传视频检测数据
                    HCNetSDK.NET_AIOP_VIDEO_HEAD struAIOPVideo = new HCNetSDK.NET_AIOP_VIDEO_HEAD();
                    struAIOPVideo.write();
                    Pointer pAIOPVideo = struAIOPVideo.getPointer();
                    pAIOPVideo.write(0, pAlarmInfo.getByteArray(0, struAIOPVideo.size()), 0, struAIOPVideo.size());
                    struAIOPVideo.read();

                    String strTime = "" + String.format("%04d", struAIOPVideo.struTime.wYear) +
                            String.format("%02d", struAIOPVideo.struTime.wMonth) +
                            String.format("%02d", struAIOPVideo.struTime.wDay) +
                            String.format("%02d", struAIOPVideo.struTime.wHour) +
                            String.format("%02d", struAIOPVideo.struTime.wMinute) +
                            String.format("%02d", struAIOPVideo.struTime.wSecond) +
                            String.format("%03d", struAIOPVideo.struTime.wMilliSec);

                    sAlarmType = sAlarmType + "：AI开放平台接入，上传视频检测数据，通道号:" + struAIOPVideo.dwChannel +
                            ", 时间:" + strTime;

                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);

                    if (struAIOPVideo.dwAIOPDataSize > 0) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            String filename = ".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() +
                                    "_" + newName + "_AIO_VideoData.json";
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
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (struAIOPVideo.dwPictureSize > 0) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            String filename = ".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() +
                                    "_" + newName + "_AIO_VideoPic.jpg";
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
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case HCNetSDK.COMM_UPLOAD_AIOP_PICTURE: //设备支持AI开放平台接入，上传视频检测数据
                    HCNetSDK.NET_AIOP_PICTURE_HEAD struAIOPPic = new HCNetSDK.NET_AIOP_PICTURE_HEAD();
                    struAIOPPic.write();
                    Pointer pAIOPPic = struAIOPPic.getPointer();
                    pAIOPPic.write(0, pAlarmInfo.getByteArray(0, struAIOPPic.size()), 0, struAIOPPic.size());
                    struAIOPPic.read();

                    String strPicTime = "" + String.format("%04d", struAIOPPic.struTime.wYear) +
                            String.format("%02d", struAIOPPic.struTime.wMonth) +
                            String.format("%02d", struAIOPPic.struTime.wDay) +
                            String.format("%02d", struAIOPPic.struTime.wHour) +
                            String.format("%02d", struAIOPPic.struTime.wMinute) +
                            String.format("%02d", struAIOPPic.struTime.wSecond) +
                            String.format("%03d", struAIOPPic.struTime.wMilliSec);

                    sAlarmType = sAlarmType + "：AI开放平台接入，上传图片检测数据，通道号:" + new String(struAIOPPic.szPID) +
                            ", 时间:" + strPicTime;

                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);

                    if (struAIOPPic.dwAIOPDataSize > 0) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            String filename = ".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() +
                                    "_" + newName + "_AIO_PicData.json";
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
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case HCNetSDK.COMM_ISAPI_ALARM: //ISAPI协议报警信息
                    HCNetSDK.NET_DVR_ALARM_ISAPI_INFO struEventISAPI = new HCNetSDK.NET_DVR_ALARM_ISAPI_INFO();
                    struEventISAPI.write();
                    Pointer pEventISAPI = struEventISAPI.getPointer();
                    pEventISAPI.write(0, pAlarmInfo.getByteArray(0, struEventISAPI.size()), 0, struEventISAPI.size());
                    struEventISAPI.read();

                    sAlarmType = sAlarmType + "：ISAPI协议报警信息, 数据格式:" + struEventISAPI.byDataType +
                            ", 图片个数:" + struEventISAPI.byPicturesNumber;

                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);

                    SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmmss");
                    String curTime = sf1.format(new Date());
                    FileOutputStream foutdata;
                    try {
                        String jsonfilename = ".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() + curTime + "_ISAPI_Alarm_" + ".json";
                        foutdata = new FileOutputStream(jsonfilename);
                        //将字节写入文件
                        ByteBuffer jsonbuffers = struEventISAPI.pAlarmData.getByteBuffer(0, struEventISAPI.dwAlarmDataLen);
                        byte[] jsonbytes = new byte[struEventISAPI.dwAlarmDataLen];
                        jsonbuffers.rewind();
                        jsonbuffers.get(jsonbytes);
                        foutdata.write(jsonbytes);
                        foutdata.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < struEventISAPI.byPicturesNumber; i++) {
                        HCNetSDK.NET_DVR_ALARM_ISAPI_PICDATA struPicData = new HCNetSDK.NET_DVR_ALARM_ISAPI_PICDATA();
                        struPicData.write();
                        Pointer pPicData = struPicData.getPointer();
                        pPicData.write(0, struEventISAPI.pPicPackData.getByteArray(i * struPicData.size(), struPicData.size()), 0, struPicData.size());
                        struPicData.read();

                        FileOutputStream fout;
                        try {
                            String filename = ".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() + curTime +
                                    "_ISAPIPic_" + i + "_" + new String(struPicData.szFilename).trim() + ".jpg";
                            fout = new FileOutputStream(filename);
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = struPicData.pPicData.getByteBuffer(offset, struPicData.dwPicLen);
                            byte[] bytes = new byte[struPicData.dwPicLen];
                            buffers.rewind();
                            buffers.get(bytes);
                            fout.write(bytes);
                            fout.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    newRow[0] = dateFormat.format(today);
                    //报警类型
                    newRow[1] = sAlarmType;
                    //报警设备IP地址
                    sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                    newRow[2] = sIP[0];
//					alarmTableModel.insertRow(0, newRow);
                    break;
            }
        } catch (UnsupportedEncodingException ex) {
//			Logger.getLogger(AlarmJavaDemoView.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

}
