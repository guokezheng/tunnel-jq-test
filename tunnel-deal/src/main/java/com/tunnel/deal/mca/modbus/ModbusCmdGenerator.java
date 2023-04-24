package com.tunnel.deal.mca.modbus;

import com.tunnel.deal.mca.util.StringUtil;

/**
 * describe: 命令组装工具类
 *
 * @author zs
 * @date 2021/2/6
 */
public class ModbusCmdGenerator {

    /**
     * 读取功能码03
     * @param deviceAddress
     * @param serial
     * @param startAddress
     * @param dataLength
     * @return
     */
    public static String getReadThreeCommand(String deviceAddress,String serial,String startAddress,String dataLength){
        String command = "";
        ModbusReadSend send = new ModbusReadSend();
        send.setSerial(serial);
        send.setDeviceAddress(getDeviceAddress(deviceAddress));
        send.setFunctionCode(ModbusFunctionCode.CODE_THREE);
        send.setStartAddress(getHexByte(Integer.valueOf(startAddress)));
        send.setAddressNum(getHexByte(Integer.valueOf(dataLength)));
        command = send.getReadRecv();

        return command;
    }

    /**
     * 读取功能码04
     * @param deviceAddress
     * @param serial
     * @param startAddress
     * @param dataLength
     * @return
     */
    public static String getReadFourCommand(String deviceAddress,String serial,String startAddress,String dataLength){
        String command = "";
        ModbusReadSend send = new ModbusReadSend();
        send.setSerial(serial);
        send.setDeviceAddress(getDeviceAddress(deviceAddress));
        send.setFunctionCode(ModbusFunctionCode.CODE_FOUR);
        send.setStartAddress(getHexByte(Integer.valueOf(startAddress)));
        send.setAddressNum(getHexByte(Integer.valueOf(dataLength)));
        command = send.getReadRecv();

        return command;
    }

    /**
     * 写入功能码06
     * @param deviceAddress
     * @param serial
     * @param writeAddress
     * @param writeData
     * @return
     */
    public static String getWriteSixCommand(String deviceAddress,String serial,String writeAddress,String writeData){
        String command = "";
        ModbusWriteSend send = new ModbusWriteSend();
        send.setSerial(serial);
        send.setDeviceAddress(getDeviceAddress(deviceAddress));
        send.setFunctionCode(ModbusFunctionCode.CODE_SIX);
        send.setWriteAddress(getHexByte(Integer.valueOf(writeAddress)));
        send.setWriteData(getHexByte(Integer.valueOf(writeData)));
        command = send.getWriteSend();

        return command;
    }


    /**
     * 获取16进制，1个字节长度的设备地址
     * @param deviceAddress 设备地址
     * @return
     */
    public static String getDeviceAddress(String deviceAddress){
        if(deviceAddress == null || "".equals(deviceAddress)){
            //设备地址为空，默认按照设备地址01读取
            return ModbusCmdValues.DEVICE_ADDRESS_DEFAULT;
        }
//        int address = Integer.parseInt(deviceAddress,16);
        String addressStr = StringUtil.fillStringWithZero(deviceAddress,2);
        return addressStr;
    }

    /**
     * 转换为16进制的0000格式的数据
     * @param serial
     * @return
     */
    public static String getHexByte(int serial){
        String hex = Integer.toHexString(serial);
        String result = StringUtil.fillStringWithZero(hex,4);
        if(result.length() > 4){
            result = result.substring(4);
        }
        return result;
    }

}
