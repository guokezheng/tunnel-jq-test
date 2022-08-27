package com.tunnel.plc.plcutil;


import com.tunnel.platform.datacenter.domain.dataVo.DataInfo;
import com.tunnel.platform.datacenter.domain.enumeration.CommandEnum;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.platform.datacenter.domain.enumeration.EnumUtil;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.utils.util.CommonUtil;
import com.tunnel.platform.utils.util.RadixUtil;

import java.net.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author yangqichao
 * @date 2019/9/17 13:52
 */
public class DisPlayUtil {

    private DisPlayUtil() {

    }

    /*
     * 拼装PLC握手报文：
     * 替换掉报文中的地址
     * PLC地址：第一个##
     * 本机地址：第二个@@
     *
     * */
    public static String handshake() {
        StringBuffer content = new StringBuffer();

        String command = "46494E530000000C0000000000000000000000@@";
        //获取PlcIP地址
        String heXipByLocalIp = getHEXipByLocalIp();
        String replace = command.replaceFirst("@@", heXipByLocalIp);
        content.append(replace);
        for (int i = content.length() - 2; i > 0; i -= 2) {
            content.insert(i, " ");
        }
        return content.toString();
    }

    /*
     * 拼装PLC查询报文：
     * 替换掉报文中的地址
     * PLC地址：第一个##
     * 本机地址：第二个@@
     *
     * */
    public static String getComDisPlay(String deviceId, String plcIp, String cmd) {
        StringBuffer content = new StringBuffer();
        if (CommonUtil.isNull(cmd))
            return null;
        String heXipByPlcIp = getHEXipByPlcIp(plcIp);
        String heXipByLocalIp = getHEXipByLocalIp();
        String replaceCmd = cmd.replaceFirst("##", heXipByPlcIp);
        String replace = replaceCmd.replace("@@", heXipByLocalIp);
        content.append(replace);
        for (int i = content.length() - 2; i > 0; i -= 2) {
            content.insert(i, " ");
        }
        return content.toString();
    }

    public static String getComDisPlay(String plcIp, String cmd) {
        StringBuffer content = new StringBuffer();
        if (CommonUtil.isNull(cmd))
            return null;
        String heXipByLocalIp = getHEXipByLocalIp();
        String replaceCmd = cmd.replaceFirst("##", heXipByLocalIp);
        content.append(replaceCmd);
        return content.toString();
    }

    /*
     * 拼装设备控制命令：
     * 替换掉报文中的地址
     * PLC地址：第一个##
     * 本机地址：第二个##
     *
     * */
//    public static String itemContent(SdDevices sdDevices, SdHosts sdHosts, Integer deviceType, String state) {
//        String commands = null;
//        //设备控制状态
//        int states = Integer.parseInt(state);
//        //获取上位机的控制地址
//        String controlPointAddress = sdDevices.getDmcontrolSeat();
//        if (deviceType == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode() || deviceType == DevicesTypeEnum.YIN_DAO_ZHAO_MING.getCode() ||
//                deviceType == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode() || deviceType == DevicesTypeEnum.PENG_DONG_ZHAO_MING.getCode()) {
//            //例子： "10.00,10.01"   分割后位[0]:10.00   [1]10.01
//            //states   为设备开关状态  0表示取第0位 , 1表示取第1位
//            String[] split = controlPointAddress.split(",");
//            String controlAddressDevice = split[states];
//            int x = controlAddressDevice.lastIndexOf(".");
//            //10.00   前两位位上位机地址码
//            String controlAddress = controlAddressDevice.substring(0, x);
//            String command = EnumUtil.getType(deviceType, CommandEnum.class);
//            //获取PlcIP地址
//            String heXipByPlcIp = getHEXipByPlcIp(sdHosts.getPlcIp());
//            String replacePLC = command.replaceFirst("##", heXipByPlcIp);
//            //获取本机IP地址
//            String heXipByLocalIp = getHEXipByLocalIp();
//            String replaceLocal = replacePLC.replaceFirst("@@", heXipByLocalIp);
//            //获取上位机地址10 即为0A
//            String heXipByAddress = RadixUtil.intToHex(Integer.parseInt(controlAddress));
//            String replaceByAddress = replaceLocal.replace("##", heXipByAddress);
//            //创建长度为16的字符串
//            //10.01   后两位位上位机设备码
//            String controlDevice = controlAddressDevice.substring(x + 1, controlAddressDevice.length());
//            int i = Integer.parseInt(controlDevice);
//            String commandStart = "0000000000000000";
//            char[] chars = commandStart.toCharArray();
//            chars[i] = '1';
//            String commandChars = String.valueOf(chars);
//            String commandReverse = new StringBuilder(commandChars).reverse().toString();
//            String s = RadixUtil.binary2Hex(commandReverse);
//            StringBuffer str = new StringBuffer();
//            for (int q = 0; q < 4 - s.length(); q++) {
//                str.append("0");
//            }
//            s = str.toString() + s;//0001
//            StringBuffer stringBuffer = new StringBuffer();
//            StringBuffer cmd = stringBuffer.append(replaceByAddress).append(s);
//            for (int q = cmd.length() - 2; q > 0; q -= 2) {
//                cmd.insert(q, " ");
//            }
//            commands = cmd.toString();
//        } else if (deviceType == DevicesTypeEnum.FENG_JI_1.getCode() || deviceType == DevicesTypeEnum.FENG_JI_2.getCode()) {
//            Map<String, List<DataInfo>> dataMap = DataUtil.getDataMap();
//            //state = "3,3,3";
//            String[] stateSplit = state.split(",");
//            int state1 = Integer.parseInt(stateSplit[0]);
//            int state2 = Integer.parseInt(stateSplit[1]);
//            int state3 = Integer.parseInt(stateSplit[2]);
//            //例子： "10.00,10.01"   分割后位[0]:10.00   [1]10.01
//            //states   为设备开关状态  0表示取第0位 , 1表示取第1位
//            String[] split = controlPointAddress.split(",");
//            String controlAddressDevice = split[0];
//            int x = controlAddressDevice.lastIndexOf(".");
//            //10.00   前两位位上位机地址码
//            String controlAddress = controlAddressDevice.substring(0, x);
//            String command = EnumUtil.getType(deviceType, CommandEnum.class);
//            //获取PlcIP地址
//            String heXipByPlcIp = getHEXipByPlcIp(sdHosts.getPlcIp());
//            String replacePLC = command.replaceFirst("##", heXipByPlcIp);
//            //获取本机IP地址
//            String heXipByLocalIp = getHEXipByLocalIp();
//            String replaceLocal = replacePLC.replaceFirst("@@", heXipByLocalIp);
//            //获取上位机地址10 即为0A
//            String heXipByAddress = RadixUtil.intToHex(Integer.parseInt(controlAddress));
//            String replaceByAddress = replaceLocal.replace("##", heXipByAddress);
//            //创建长度为16的字符串
//            //10.01   后两位位上位机设备码
//            StringBuffer commandEnd = new StringBuffer("");
//            for (int i = 0; i < stateSplit.length; i++) {
//                if ("1".equals(stateSplit[i])) {
//                    commandEnd.append("1");
//                } else if ("2".equals(stateSplit[i])) {
//                    commandEnd.append("0");
//                } else if ("3".equals(stateSplit[i])) {
//                    commandEnd.append("0");
//                }
//            }
//            for (int i = 0; i < stateSplit.length; i++) {
//                if ("1".equals(stateSplit[i])) {
//                    commandEnd.append("0");
//                } else if ("2".equals(stateSplit[i])) {
//                    commandEnd.append("1");
//                } else if ("3".equals(stateSplit[i])) {
//                    commandEnd.append("0");
//                }
//            }
//            commandEnd.append("0000000000");
//            String commandReverse = new StringBuilder(commandEnd).reverse().toString();
//            String s = RadixUtil.binary2Hex(commandReverse);
//            StringBuffer commandStart = new StringBuffer();
//            for (int q = 0; q < 4 - s.length(); q++) {
//                commandStart.append("0");
//            }
//            s = commandStart.toString() + s;//0001
//            StringBuffer cmd = new StringBuffer().append(replaceByAddress).append(s);
//            //分割
//            for (int q = cmd.length() - 2; q > 0; q -= 2) {
//                cmd.insert(q, " ");
//            }
//            commands = cmd.toString();
//
//        } else if (deviceType == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode() || deviceType == DevicesTypeEnum.TU_SHU_CHE_ZHI.getCode() ||
//                deviceType == DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode() || deviceType == DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode()) {
//            //DM101->101
//            String controlAddress = null;
//            if (deviceType == DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode() || deviceType == DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode()) {
//                if ("5".equals(state) || "6".equals(state)) {
//                    controlAddress = "7A";
//                } else {
//                    controlAddress = RadixUtil.intToHex(Integer.parseInt(controlPointAddress.substring(2, 5)));
//                }
//
//            } else {
//                controlAddress = RadixUtil.intToHex(Integer.parseInt(controlPointAddress.substring(2, 5)));
//            }
//            String command = EnumUtil.getType(deviceType, CommandEnum.class);
//            //获取PlcIP地址
//            String heXipByPlcIp = getHEXipByPlcIp(sdHosts.getPlcIp());
//            //获取本机IP地址
//            String heXipByLocalIp = getHEXipByLocalIp();
//
//            String replacePLC = command.replaceFirst("##", heXipByPlcIp);
//            String replaceLocal = replacePLC.replaceFirst("@@", heXipByLocalIp);
//            String replaceCon = replaceLocal.replace("##", controlAddress);
//
//            StringBuffer cmd = new StringBuffer().append(replaceCon).append(state);
//            for (int i = cmd.length() - 2; i > 0; i -= 2) {
//                cmd.insert(i, " ");
//            }
//            commands = cmd.toString();
//        } else if (deviceType == DevicesTypeEnum.JUAN_LIAN_MEN.getCode()) {
//            //例子： "10.00,10.01"   分割后位[0]:10.00   [1]10.01
//            //states   为设备开关状态  0表示取第0位 , 1表示取第1位
//            String[] split = controlPointAddress.split(",");
//            String controlAddressDevice = split[states];
//            int x = controlAddressDevice.lastIndexOf(".");
//            //10.00   前两位位上位机地址码
//            String controlAddress = controlAddressDevice.substring(0, x);
//            String command = EnumUtil.getType(deviceType, CommandEnum.class);
//            //获取PlcIP地址
//            String heXipByPlcIp = getHEXipByPlcIp(sdHosts.getPlcIp());
//            String replacePLC = command.replaceFirst("##", heXipByPlcIp);
//            //获取本机IP地址
//            String heXipByLocalIp = getHEXipByLocalIp();
//            String replaceLocal = replacePLC.replaceFirst("@@", heXipByLocalIp);
//            //获取上位机地址10 即为0A
//            String heXipByAddress = RadixUtil.intToHex(Integer.parseInt(controlAddress));
//            String replaceByAddress = replaceLocal.replace("##", heXipByAddress);
//            //创建长度为16的字符串
//            //10.01   后两位位上位机设备码
//            String controlDevice = controlAddressDevice.substring(x + 1, controlAddressDevice.length());
//            int i = Integer.parseInt(controlDevice);
//            String commandStart = "0000000000000000";
//            char[] chars = commandStart.toCharArray();
//            chars[i] = '1';
//            String commandChars = String.valueOf(chars);
//            String commandReverse = new StringBuilder(commandChars).reverse().toString();
//            String s = RadixUtil.binary2Hex(commandReverse);
//            StringBuffer str = new StringBuffer();
//            for (int q = 0; q < 4 - s.length(); q++) {
//                str.append("0");
//            }
//            s = str.toString() + s;//0001
//            StringBuffer cmd = new StringBuffer().append(replaceByAddress).append(s);
//            for (int q = cmd.length() - 2; q > 0; q -= 2) {
//                cmd.insert(q, " ");
//            }
//            commands = cmd.toString();
//        } else if (deviceType == DevicesTypeEnum.SHUI_BENG.getCode()) {
//            String stakeMark = sdDevices.getStakeMark();
//            Map<String, List<DataInfo>> dataMap = DataUtil.getDataMap();
//            //例子： "10.00,10.01"   分割后位[0]:10.00   [1]10.01
//            //states   为设备开关状态  0表示取第0位 , 1表示取第1位
//            String[] split = controlPointAddress.split(",");
//            String controlAddressDevice = split[0];
//            int x = controlAddressDevice.lastIndexOf(".");
//            //10.00   前两位位上位机地址码
//            String controlAddress = controlAddressDevice.substring(0, x);
//            String command = EnumUtil.getType(deviceType, CommandEnum.class);
//            //获取PlcIP地址
//            String heXipByPlcIp = getHEXipByPlcIp(sdHosts.getPlcIp());
//            String replacePLC = command.replaceFirst("##", heXipByPlcIp);
//            //获取本机IP地址
//            String heXipByLocalIp = getHEXipByLocalIp();
//            String replaceLocal = replacePLC.replaceFirst("@@", heXipByLocalIp);
//            //获取上位机地址10 即为0A
//            String heXipByAddress = RadixUtil.intToHex(Integer.parseInt(controlAddress));
//            String replaceByAddress = replaceLocal.replace("##", heXipByAddress);
//            //创建长度为16的字符串
//            //10.01   后两位位上位机设备码
//            StringBuffer commandEnd = new StringBuffer("");
//            if ("1#水泵".equals(stakeMark)) {
//                if ("1".equals(state)) {
//                    commandEnd.append("0001");
//                } else if ("0".equals(state)) {
//                    commandEnd.append("0000");
//                }
//            } else {
//                if ("1".equals(state)) {
//                    commandEnd.append("0002");
//                } else if ("0".equals(state)) {
//                    commandEnd.append("0000");
//                }
//            }
//            StringBuffer cmd = new StringBuffer().append(replaceByAddress).append(commandEnd);
//            //分割
//            for (int q = cmd.length() - 2; q > 0; q -= 2) {
//                cmd.insert(q, " ");
//            }
//            commands = cmd.toString();
//        }
//        return commands;
//    }


    /*
     * 获取PLCip截取最后两位转换为HEX
     * */
    public static String getHEXipByPlcIp(String PlcIp) {
        int x = PlcIp.lastIndexOf(".");
        String substring = PlcIp.substring(x + 1, PlcIp.length());
        return RadixUtil.intToHex(Integer.parseInt(substring));
    }


    /*
     * 获取本机ip截取最后两位转换为HEX
     * */
    public static String getHEXipByLocalIp() {
        try {
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
                NetworkInterface item = e.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    if (item.isLoopback() || !item.isUp()) {
                        continue;
                    }
                    if (address.getAddress() instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        String localIpByNetCard = inet4Address.getHostAddress();
                        int x = localIpByNetCard.lastIndexOf(".");
                        String substring = localIpByNetCard.substring(x + 1, localIpByNetCard.length());
                        return RadixUtil.intToHex(Integer.parseInt(substring));
                    }
                }
            }
            String localIpByNetCard = InetAddress.getLocalHost().getHostAddress();
            int x = localIpByNetCard.lastIndexOf(".");
            String substring = localIpByNetCard.substring(x + 1, localIpByNetCard.length());
            return RadixUtil.intToHex(Integer.parseInt(substring));
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}

