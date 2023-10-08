package com.tunnel.deal.tcp.modbus;

import com.tunnel.deal.tcp.util.StringUtil;

/**
 * describe: 命令组装工具类
 *
 * @author zs
 * @date 2021/2/6
 */
public class ModbusCmdGenerator {

    /**
     * 读取功能码01
     * @param deviceAddress
     * @param serial
     * @param startAddress
     * @param dataLength
     * @return
     */
    public static String getReadOneCommand(String deviceAddress,String serial,String startAddress,String dataLength){
        //    00 01 00 00 00 06 01 01 00 02 00 08（0002起始地址、0008读取数量）
        String command = "";
        ModbusReadSend send = new ModbusReadSend();
        send.setSerial(serial);
        send.setDeviceAddress(getDeviceAddress(deviceAddress));
        send.setFunctionCode(ModbusFunctionCode.CODE_ONE);
        send.setStartAddress(getHexByte(Integer.valueOf(startAddress)));
        send.setAddressNum(getHexByte(Integer.valueOf(dataLength)));
        command = send.getReadRecv();

        return command;
    }


    /**
     * 读取功能码02
     * @param deviceAddress
     * @param serial
     * @param startAddress
     * @param dataLength
     * @return
     */
    public static String getReadTwoCommand(String deviceAddress,String serial,String startAddress,String dataLength){
        //    0A0A 0000 0006 0102 0000 0008（0000起始地址、0008读取数量）
        String command = "";
        ModbusReadSend send = new ModbusReadSend();
        send.setSerial(serial);
        send.setDeviceAddress(getDeviceAddress(deviceAddress));
        send.setFunctionCode(ModbusFunctionCode.CODE_TWO);
        send.setStartAddress(getHexByte(Integer.valueOf(startAddress)));
        send.setAddressNum(getHexByte(Integer.valueOf(dataLength)));
        command = send.getReadRecv();

        return command;
    }

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
     * 写入功能码05
     * @param deviceAddress
     * @param serial
     * @param writeAddress
     * @param writeData
     * @return
     */
    public static String getWriteFiveCommand(String deviceAddress,String serial,String writeAddress,String writeData){
//        0A 0A 00 00 00 06 01 05 00 10 FF 00（0010写入地址、FF00开、0000关）
        String command = "";
        ModbusWriteSend send = new ModbusWriteSend();
        send.setSerial(serial);
        send.setDeviceAddress(getDeviceAddress(deviceAddress));
        send.setFunctionCode(ModbusFunctionCode.CODE_FIVE);
        send.setWriteAddress(getHexByte(Integer.valueOf(writeAddress)));
        send.setWriteData(getHexByte(Integer.valueOf(writeData)));
        command = send.getWriteSend();

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
     * 写入功能码0F
     * @param deviceAddress
     * @param serial
     * @param writeAddress
     * @param writeData
     * @return
     */
    public static String getWriteFifteenCommand(String deviceAddress,String serial,String writeAddress,String writeLength,String writeData){
        //0A 0A 00 00 00 08 01 0F 00 10 00 08 01 00 （0010起始地址、0008写入数量、01字节数、00写入值）
//        0A 0A 00 00 00 08 01 0F 00 10 00 04 01 09
        //动态拼接
        String command = "";
        ModbusWriteCoilSend send = new ModbusWriteCoilSend();
        send.setSerial(serial);
        send.setDeviceAddress(getDeviceAddress(deviceAddress));
        send.setFunctionCode(ModbusFunctionCode.CODE_FIFTEEN);
        send.setWriteAddress(getHexByte(Integer.valueOf(writeAddress)));
        send.setWriteLength(getHexByte(Integer.valueOf(writeLength)));
        int writeDataByte = Integer.valueOf(writeLength) / 8 + 1;
        send.setWriteDataByte(getByteWithLength(writeDataByte,2));
        int writeDataInt = Integer.parseInt(writeData,2);
        send.setWriteData(getByteWithLength(writeDataInt,writeDataByte * 2));
        int remainLength = send.computeRemainLength();
        send.setRemainLength(getHexByte(remainLength));
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
       return getByteWithLength(serial,4);
//        String hex = Integer.toHexString(serial);
//        String result = StringUtil.fillStringWithZero(hex,4);
//        if(result.length() > 4){
//            result = result.substring(4);
//        }
//        return result;
    }

    /**
     * 转换为16进制的00格式的数据
     * 达不到输入长度，补0
     * @param serial
     * @return
     */
    public static String getByteWithLength(int serial,int length){
        String hex = Integer.toHexString(serial);
        String result = StringUtil.fillStringWithZero(hex,length);
        if(result.length() > length){
            result = result.substring(length);
        }
        return result;
    }

}
