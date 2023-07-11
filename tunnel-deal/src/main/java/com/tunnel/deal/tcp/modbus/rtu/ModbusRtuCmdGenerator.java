package com.tunnel.deal.tcp.modbus.rtu;

import com.tunnel.deal.guidancelamp.protocol.JavaCrc16;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;

import static com.tunnel.deal.tcp.modbus.ModbusCmdGenerator.getDeviceAddress;

/**
 * describe: Modbus Rtu命令组装工具类
 *
 * @author zs
 * @date 2021/2/6
 */
public class ModbusRtuCmdGenerator {

    /**
     * 写入功能码06
     * @param deviceAddress
     * @param writeAddress
     * @param writeData
     * @return
     */
    public static String getWriteSixCommand(String deviceAddress,String writeAddress,String writeData){
        String command = "";
        ModbusRtuWriteRegisterSend send = new ModbusRtuWriteRegisterSend();
        send.setDeviceAddress(getDeviceAddress(deviceAddress));
        send.setFunctionCode(ModbusFunctionCode.CODE_SIX);
        send.setWriteAddress(writeAddress);
        send.setWriteData(writeData);
        command = send.getWriteSend();
        String crc = JavaCrc16.getCRC(command);
        send.setCrc(crc);
        command = send.getWriteSend();
        return command;
    }

}
