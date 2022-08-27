package com.tunnel.platform.business.board.device;

import com.tunnel.platform.utils.util.CRC16;
import com.tunnel.platform.utils.util.CommonUtil;
import com.tunnel.platform.utils.util.RadixUtil;

/**
 *
 */
public class BrightnessUtil {


    private BrightnessUtil() {

    }

    //电明指令格式
    // 0x02(帧头) 0x30 0x30(目的地址) 0x30 0x31(源地址) 0x32 0x33(指令头)
    private static String DIANMING = "303030313233";
    //显科指令格式
    //0x02(帧头) 0x30 0x36(帧类型) 0x30 0x30(设备地址地址)
    private static String XIANKE = "30363030";
    //三思指令格式
    // 设置调节方式 0x02(帧头) 0x30 0x30(目的地址) 0x30 0x34(帧类型)
    private static String SANSI_1 = "30303034";
    //设置亮度 0x02(帧头) 0x30 0x30(目的地址) 0x30 0x35(帧类型)
    private static String SANSI_2 = "30303035";
    //光电指令格式
    //0x02(帧头) 0x30 0x30(设备地址) 0x30 0x33(帧类型)
    private static String GUANGDIAN = "30303033";


    public static String getDianMingBrightnessCommond(String mode, String brightness) throws Exception {
        if (CommonUtil.isNull(mode))
            return null;
        StringBuffer firstCommandBuffer = new StringBuffer(BrightnessUtil.DIANMING);
        StringBuffer append = new StringBuffer();
        if ("0".equals(mode)) {
            append = firstCommandBuffer.append("46").append("46").append("46").append("46").append("46").append("46");
        } else {
            String word = RadixUtil.String2Hex(brightness);
            append = firstCommandBuffer.append(word).append(word).append(word);
        }
        //为自动调节方式
        StringBuffer firstTempCommand = new StringBuffer(append);
        for (int i = append.length() - 2; i > 0; i -= 2) {
            append.insert(i, ",");
        }
        String firstcommandInfo = hex2Str(append.toString());
        byte[] firstBytes = firstcommandInfo.getBytes();
        String firstCrcResult = CRC16.CRC_16_XMODEM(firstBytes, firstBytes.length);
        firstCrcResult = firstCrcResult.replaceAll("1B", "1B00");
        firstCrcResult = firstCrcResult.replaceAll("02", "1BE7");
        firstCrcResult = firstCrcResult.replaceAll("03", "1BE8");
        StringBuffer crcResultBuffer = new StringBuffer(firstCrcResult);
        StringBuffer startBuffer = new StringBuffer("02");
        StringBuffer firstCommand = startBuffer.append(firstTempCommand).append(crcResultBuffer).append("03");
        for (int i = firstCommand.length() - 2; i > 0; i -= 2) {
            firstCommand.insert(i, " ");
        }
        return firstCommand.toString();
    }

    public static String getXiankeBrightnessCommond(String mode, String brightness) {
        if (CommonUtil.isNull(mode))
            return null;

        StringBuffer firstCommandBuffer = new StringBuffer(BrightnessUtil.XIANKE);
        StringBuffer append = new StringBuffer();
        if ("0".equals(mode)) {
            append = firstCommandBuffer.append("31").append("30").append("30").append("30").append("30").append("30").append("30");
        } else {
            String word = RadixUtil.String2Hex(brightness);
            append = firstCommandBuffer.append("30").append(word).append(word).append(word);
        }
        //为自动调节方式
        StringBuffer firstTempCommand = new StringBuffer(append);
        for (int i = append.length() - 2; i > 0; i -= 2) {
            append.insert(i, ",");
        }
        String firstcommandInfo = hex2Str(append.toString());
        byte[] firstBytes = firstcommandInfo.getBytes();
        String firstCrcResult = CRC16.CRC_16_XMODEM(firstBytes, firstBytes.length);
        firstCrcResult = firstCrcResult.replaceAll("1B", "1B00");
        firstCrcResult = firstCrcResult.replaceAll("02", "1BE7");
        firstCrcResult = firstCrcResult.replaceAll("03", "1BE8");
        StringBuffer crcResultBuffer = new StringBuffer(firstCrcResult);
        StringBuffer startBuffer = new StringBuffer("02");
        StringBuffer firstCommand = startBuffer.append(firstTempCommand).append(crcResultBuffer).append("03");
        for (int i = firstCommand.length() - 2; i > 0; i -= 2) {
            firstCommand.insert(i, " ");
        }
        return firstCommand.toString();
    }

    public static String getSanSiBrightnessCommond1(String mode) {

        if (CommonUtil.isNull(mode))
            return null;

        StringBuffer firstCommandBuffer = new StringBuffer(BrightnessUtil.SANSI_1);
        StringBuffer append = new StringBuffer();
        mode = RadixUtil.String2Hex(mode);

        append = firstCommandBuffer.append(mode);
        //为自动调节方式
        StringBuffer firstTempCommand = new StringBuffer(append);
        for (int i = append.length() - 2; i > 0; i -= 2) {
            append.insert(i, ",");
        }
        String firstcommandInfo = hex2Str(append.toString());
        byte[] firstBytes = firstcommandInfo.getBytes();
        String firstCrcResult = CRC16.CRC_16_XMODEM(firstBytes, firstBytes.length);
        firstCrcResult = firstCrcResult.replaceAll("1B", "1B00");
        firstCrcResult = firstCrcResult.replaceAll("02", "1BE7");
        firstCrcResult = firstCrcResult.replaceAll("03", "1BE8");
        StringBuffer crcResultBuffer = new StringBuffer(firstCrcResult);
        StringBuffer startBuffer = new StringBuffer("02");
        StringBuffer firstCommand = startBuffer.append(firstTempCommand).append(crcResultBuffer).append("03");
        for (int i = firstCommand.length() - 2; i > 0; i -= 2) {
            firstCommand.insert(i, " ");
        }
        return firstCommand.toString();
    }

    public static String getSanSiBrightnessCommond2(String brightness) {
        if (CommonUtil.isNull(brightness))
            return null;

        StringBuffer firstCommandBuffer = new StringBuffer(BrightnessUtil.SANSI_2);
        StringBuffer append = new StringBuffer();
        brightness = RadixUtil.String2Hex(brightness);

        append = firstCommandBuffer.append(brightness).append(brightness).append(brightness);
        //为自动调节方式
        StringBuffer firstTempCommand = new StringBuffer(append);
        for (int i = append.length() - 2; i > 0; i -= 2) {
            append.insert(i, ",");
        }
        String firstcommandInfo = hex2Str(append.toString());
        byte[] firstBytes = firstcommandInfo.getBytes();
        String firstCrcResult = CRC16.CRC_16_XMODEM(firstBytes, firstBytes.length);
        firstCrcResult = firstCrcResult.replaceAll("1B", "1B00");
        firstCrcResult = firstCrcResult.replaceAll("02", "1BE7");
        firstCrcResult = firstCrcResult.replaceAll("03", "1BE8");
        StringBuffer crcResultBuffer = new StringBuffer(firstCrcResult);
        StringBuffer startBuffer = new StringBuffer("02");
        StringBuffer firstCommand = startBuffer.append(firstTempCommand).append(crcResultBuffer).append("03");
        for (int i = firstCommand.length() - 2; i > 0; i -= 2) {
            firstCommand.insert(i, " ");
        }
        return firstCommand.toString();

    }

    public static String getGuangDianBrightnessCommond(String mode, String brightness) {
        if (CommonUtil.isNull(mode))
            return null;

        StringBuffer firstCommandBuffer = new StringBuffer(BrightnessUtil.GUANGDIAN);
        StringBuffer append = new StringBuffer();
        if ("0".equals(mode)) {
            brightness = "00";
        }
        mode = RadixUtil.String2Hex(mode);

        brightness = RadixUtil.String2Hex(brightness);
        append = firstCommandBuffer.append(mode).append(brightness);
        //为自动调节方式
        StringBuffer firstTempCommand = new StringBuffer(append);
        for (int i = append.length() - 2; i > 0; i -= 2) {
            append.insert(i, ",");
        }
        String firstcommandInfo = hex2Str(append.toString());
        byte[] firstBytes = firstcommandInfo.getBytes();
        String firstCrcResult = CRC16.CRC_16_XMODEM(firstBytes, firstBytes.length);
        firstCrcResult = firstCrcResult.replaceAll("1B", "1B00");
        firstCrcResult = firstCrcResult.replaceAll("02", "1BE7");
        firstCrcResult = firstCrcResult.replaceAll("03", "1BE8");
        StringBuffer crcResultBuffer = new StringBuffer(firstCrcResult);
        StringBuffer startBuffer = new StringBuffer("02");
        StringBuffer firstCommand = startBuffer.append(firstTempCommand).append(crcResultBuffer).append("03");
        for (int i = firstCommand.length() - 2; i > 0; i -= 2) {
            firstCommand.insert(i, " ");
        }
        return firstCommand.toString();
    }

    public static String getDingEnCommond(String firstCommand, String deviceId) {
        return null;
    }


    public static String hex2Str(String hex) {
        StringBuilder sb = new StringBuilder();
        String[] split = hex.split(",");
        for (String str : split) {
            int i = Integer.parseInt(str, 16);
            sb.append((char) i);
        }
        return sb.toString();
    }

}

