package com.tunnel.deal.warninglightstrip;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.digitalmodel.RadarEventService;
import com.tunnel.deal.guidancelamp.control.ClientHandler;
import com.tunnel.deal.guidancelamp.control.NettyClient;
import com.tunnel.deal.guidancelamp.control.inductionlamp.InductionlampUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarningLightStripHandle {

    private static final Logger log = LoggerFactory.getLogger(WarningLightStripHandle.class);

    /**
     * 懒汉模式实例化
     */
    private static WarningLightStripHandle instance;

    private static SdDeviceDataMapper deviceDataMapper = SpringUtils.getBean(SdDeviceDataMapper.class);

    private static RadarEventService radarEventService = SpringUtils.getBean(RadarEventService.class);

    private static SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);

    private WarningLightStripHandle() {
    }

    public static WarningLightStripHandle getInstance() {
        if (instance == null) {
            instance = new WarningLightStripHandle();
        }
        return instance;
    }

    public void updateDeviceData(String deviceId, Long itemId, String value) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);
        sdDeviceData.setItemId(itemId);
        List<SdDeviceData> deviceData = deviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            deviceDataMapper.updateSdDeviceData(data);
        }
    }

    /**
     * 获取CRC16校验码
     * String firstCrcResult = CRC16.CRC_16_XMODEM(command.getBytes(), command.getBytes().length);
     * */

    public static String getCRC(String data) {
        data = data.replace(" ", "");
        int len = data.length();
        if (!(len % 2 == 0)) {
            return "0000";
        }
        int num = len / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
        }
        return getCRC(para);
    }

    public static String getCRC(byte[] bytes) {
        //CRC寄存器全为1
        int CRC = 0x0000ffff;
        //多项式校验值
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        //结果转换为16进制
        String result = Integer.toHexString(CRC).toUpperCase();
        if (result.length() != 4) {
            StringBuffer sb = new StringBuffer("0000");
            result = sb.replace(4 - result.length(), 4, result).toString();
        }
        //交换高低位
        return result.substring(2, 4) + result.substring(0, 2);
    }

    //发送深圳显科警示灯带控制指令
    public int toControlDev(String deviceId, Integer ctrState, SdDevices sdDevices) {
        String ip = sdDevices.getIp();
        Integer port = Integer.parseInt(sdDevices.getPort());
        //控制灯带改变显示的颜色，需要根据不同的颜色分别发送两个寄存器地址报文
        //颜色暂定0、关灯；1、全红；2、全绿；3、全黄；4、红绿；5、红黄；6、绿红；7、绿黄；8、黄红；9、黄绿；
        Map codeMap = getWarningLightStripMode(ctrState);
        Map code = sendCommandToWarningLightStrip(ip, port, codeMap.get("one").toString());
        if (code.get("isSuccess").toString().equals("0")) {
            return 0;
        }
        code = sendCommandToWarningLightStrip(ip, port, codeMap.get("two").toString());
        if (code.get("isSuccess").toString().equals("0")) {
            return 0;
        }
        code = sendCommandToWarningLightStrip(ip, port, codeMap.get("three").toString());
        if (code.get("isSuccess").toString().equals("0")) {
            return 0;
        }
        code = sendCommandToWarningLightStrip(ip, port, codeMap.get("four").toString());
        if (code.get("isSuccess").toString().equals("0")) {
            return 0;
        }
        //存储变更后控制器状态到数据库
        updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode()), ctrState.toString());
        //设置设备为在线
        sdDevices.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
        sdDevices.setEqStatusTime(new Date());
        sdDevicesMapper.updateSdDevices(sdDevices);
        return 1;
    }

    //下发指令
    public static Map sendCommandToWarningLightStrip(String ip, Integer port, String command) {
        Map<String, Object> map = new HashMap<>();
        //获取当前设备亮灯状态
//        String crcResult = getCRC(command);
//        command = command + crcResult;
        NettyClient client = new NettyClient(ip, port, command, 3);
        try {
            client.start(null);
        } catch (Exception e) {
            log.error(ip+":"+port+" 请求链接超时，请联系管理员。");
            client.stop();
            map.put("isSuccess", 0);
            return map;
        }
        try {
            //获取返回数据
            ClientHandler clientHandler =  client.getClientHandler();
            //推送数据开始时间
            long st = System.currentTimeMillis();
            //等待返回数据
            while (clientHandler.FLAG){
                long ed = System.currentTimeMillis();
                //判断当前时间是否超时
                if((ed-st)/1000>client.OVERTIME){
                    clientHandler.stop();
                    map.put("isSuccess", 0);
                    return map;
                }
                if(clientHandler.DOWNLOADFLAG){
                    //响应指令:
                    String codeInfo = clientHandler.getCode().toString().replace(" ","");
                    if (codeInfo != "" && codeInfo.equals(command)) {
                        client.stop();
                        map.put("isSuccess", 1);
                        return map;
                    } else if (codeInfo != "" && codeInfo.startsWith("0103")) {
                        client.stop();
                        map.put("isSuccess", 1);
                        map.put("data", codeInfo);
                        return map;
                    } else {
                        client.stop();
                        map.put("isSuccess", 0);
                        return map;
                    }
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            client.stop();
            map.put("isSuccess", 0);
            return map;
        }
        client.stop();
        map.put("isSuccess", 1);
        return map;
    }

    //颜色暂定0、关灯；1、全红；2、全绿；3、全黄；4、红绿；5、红黄；6、绿红；7、绿黄；8、黄红；9、黄绿；
    public static Map getWarningLightStripMode(Integer type){
        Map resultMap = new HashMap();
        String crcResult = "";
        String one = "";
        String two = "";
        String three = "";
        String four = "";
        switch (type){
            case 1 :
                one = "010600000009";
                crcResult = getCRC(one);
                one = one + crcResult;
                two = "010600030000";
                crcResult = getCRC(two);
                two = two + crcResult;
                three = "010600060009";
                crcResult = getCRC(three);
                three = three + crcResult;
                four = "010600090000";
                crcResult = getCRC(four);
                four = four + crcResult;
                resultMap.put("msgInfo","一组和二组为全红灯光。");
                break;
            case 2 :
                one = "010600000000";
                crcResult = getCRC(one);
                one = one + crcResult;
                two = "010600030009";
                crcResult = getCRC(two);
                two = two + crcResult;
                three = "010600060000";
                crcResult = getCRC(three);
                three = three + crcResult;
                four = "010600090009";
                crcResult = getCRC(four);
                four = four + crcResult;
                resultMap.put("msgInfo","一组和二组为全绿灯光。");
                break;
            case 3 :
                one = "010600000009";
                crcResult = getCRC(one);
                one = one + crcResult;
                two = "010600030009";
                crcResult = getCRC(two);
                two = two + crcResult;
                three = "010600060009";
                crcResult = getCRC(three);
                three = three + crcResult;
                four = "010600090009";
                crcResult = getCRC(four);
                four = four + crcResult;
                resultMap.put("msgInfo","一组和二组为全黄灯光。");
                break;
            case 4 :
                one = "010600000009";
                crcResult = getCRC(one);
                one = one + crcResult;
                two = "010600030000";
                crcResult = getCRC(two);
                two = two + crcResult;
                three = "010600060000";
                crcResult = getCRC(three);
                three = three + crcResult;
                four = "010600090009";
                crcResult = getCRC(four);
                four = four + crcResult;
                resultMap.put("msgInfo","一组和二组为红绿灯光。");
                break;
            case 5 :
                one = "010600000009";
                crcResult = getCRC(one);
                one = one + crcResult;
                two = "010600030000";
                crcResult = getCRC(two);
                two = two + crcResult;
                three = "010600060009";
                crcResult = getCRC(three);
                three = three + crcResult;
                four = "010600090009";
                crcResult = getCRC(four);
                four = four + crcResult;
                resultMap.put("msgInfo","一组和二组为红黄灯光。");
                break;
            case 6 :
                one = "010600000000";
                crcResult = getCRC(one);
                one = one + crcResult;
                two = "010600030009";
                crcResult = getCRC(two);
                two = two + crcResult;
                three = "010600060009";
                crcResult = getCRC(three);
                three = three + crcResult;
                four = "010600090000";
                crcResult = getCRC(four);
                four = four + crcResult;
                resultMap.put("msgInfo","一组和二组为绿红灯光。");
                break;
            case 7 :
                one = "010600000000";
                crcResult = getCRC(one);
                one = one + crcResult;
                two = "010600030009";
                crcResult = getCRC(two);
                two = two + crcResult;
                three = "010600060009";
                crcResult = getCRC(three);
                three = three + crcResult;
                four = "010600090009";
                crcResult = getCRC(four);
                four = four + crcResult;
                resultMap.put("msgInfo","一组和二组为绿黄灯光。");
                break;
            case 8 :
                one = "010600000009";
                crcResult = getCRC(one);
                one = one + crcResult;
                two = "010600030009";
                crcResult = getCRC(two);
                two = two + crcResult;
                three = "010600060009";
                crcResult = getCRC(three);
                three = three + crcResult;
                four = "010600090000";
                crcResult = getCRC(four);
                four = four + crcResult;
                resultMap.put("msgInfo","一组和二组为黄红灯光。");
                break;
            case 9 :
                one = "010600000009";
                crcResult = getCRC(one);
                one = one + crcResult;
                two = "010600030009";
                crcResult = getCRC(two);
                two = two + crcResult;
                three = "010600060000";
                crcResult = getCRC(three);
                three = three + crcResult;
                four = "010600090009";
                crcResult = getCRC(four);
                four = four + crcResult;
                resultMap.put("msgInfo","一组和二组为黄绿灯光。");
                break;
            default:
                one = "010600000000";
                crcResult = getCRC(one);
                one = one + crcResult;
                two = "010600030000";
                crcResult = getCRC(two);
                two = two + crcResult;
                three = "010600060000";
                crcResult = getCRC(three);
                three = three + crcResult;
                four = "010600090000";
                crcResult = getCRC(four);
                four = four + crcResult;
                resultMap.put("msgInfo","关闭所有灯光");
                break;
        }
        resultMap.put("one",one);
        resultMap.put("two",two);
        resultMap.put("three",three);
        resultMap.put("four",four);
        return resultMap;
    }

    // 深圳显科诱导灯控制设备逻辑
    public int toControlXianKeDev(String deviceId,Integer ctrState,SdDevices sdDevices,String brightness, String frequency) {
        String ip = sdDevices.getIp();
        Integer port = Integer.parseInt(sdDevices.getPort());
        //发送诱导灯控制指令
        try {
            String code = "000000000006010300010001";
            NettyClient client = new NettyClient(ip, port,code,3);
            client.start(null);
            //控制亮度
            Map codeMap = InductionlampUtil.getXianKePilotLightMode(ctrState,Integer.parseInt(brightness));
            client.pushHexCode(codeMap.get("code").toString());
            //控制频率（1S闪几次）
            codeMap = InductionlampUtil.getXianKeFrequency(ctrState,Integer.parseInt(frequency));
            client.pushHexCode(codeMap.get("code").toString());
            //控制占空比（亮的时间在一整个周期的比例）
//            codeMap = InductionlampUtil.getXianKeDutyCycle(ctrState,Integer.parseInt(frequency));
//            client.pushCode(codeMap.get("code").toString());
            client.stop();
        } catch (Exception e) {
            System.err.println("设备编号为" + deviceId + "的设备变更状态失败");
            return 0;
        }
        //存储变更后控制器状态到数据库
        updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()), ctrState.toString());
        updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()), brightness);
        updateDeviceData(deviceId, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()), frequency);
        return 1;
    }

    private void updateDeviceDatas(SdDevices sdDevices, String value, Integer itemId) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = deviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            deviceDataMapper.updateSdDeviceData(data);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            deviceDataMapper.insertSdDeviceData(sdDeviceData);
        }
    }

    private void sendDataToWanJi(SdDevices sdDevices, String runStatus, String runMode) {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", sdDevices.getEqId());
        map.put("deviceType", sdDevices.getEqType());
        JSONObject jsonObject = new JSONObject();
        if (runStatus.equals("lightOn")) {
            runStatus = "1";
        } else if (runStatus.equals("lightOff")) {
            runStatus = "2";
        }
        jsonObject.put("runStatus", Integer.valueOf(runStatus));
        jsonObject.put("runMode", runMode);
        map.put("deviceData", jsonObject);
        radarEventService.sendBaseDeviceStatus(map);
    }


}
