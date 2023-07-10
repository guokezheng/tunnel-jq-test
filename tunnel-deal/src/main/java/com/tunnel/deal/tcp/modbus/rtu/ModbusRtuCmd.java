package com.tunnel.deal.tcp.modbus.rtu;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.deal.tcp.modbus.ModbusCmd;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * describe: Modbus Rtu指令发送类
 *
 * @author zs
 * @date 2023/7/5
 */
@Component
public class ModbusRtuCmd {

    private static final Logger log = LoggerFactory.getLogger(ModbusRtuCmd.class);

    @Autowired
    private ModbusCmd modbusCmd;


    /**
     * 发送特定类型的指令，读取设备数据或设备状态
     * @param deviceMap 设备信息缓存Map
     * @param deviceId 设备ID
     * @param functionCode 功能码
     * @param address 起始地址
     * @param dataLength 读取长度
     * @param writeLength 写入数量
     * @param writeData 写入数据
     * @return
     */
    public AjaxResult sendCommand(Map<String,Map> deviceMap,String deviceId,String functionCode,String address,String dataLength,String writeLength,String writeData) {

        String command = getSendCommand(functionCode,address,dataLength,writeLength,writeData);

        Map map = deviceMap.get(deviceId);
        String ip = map.get("ip") == null ? "" : map.get("ip").toString();
        String port = map.get("port") == null ? "" : map.get("port").toString();
        //发送指令
        return modbusCmd.executeCommand(deviceId,ip,port,command);
    }

    /**
     * 发送特定类型的指令，读取设备数据或设备状态
     * @param deviceMap 设备信息缓存Map
     * @param deviceId 设备ID
     * @param command 下发指令
     * @return
     */
    public AjaxResult sendCommand(Map<String,Map> deviceMap,String deviceId,String command) {

        Map map = deviceMap.get(deviceId);
        String ip = map.get("ip") == null ? "" : map.get("ip").toString();
        String port = map.get("port") == null ? "" : map.get("port").toString();
        //发送指令
        return modbusCmd.executeCommand(deviceId,ip,port,command);
    }


        /**
         * 发送特定类型的指令，读取设备数据或设备状态
         * @param functionCode 功能码
         * @param address 起始地址
         * @param dataLength 读取长度
         * @param writeLength 写入数量
         * @param writeData 写入数据
         * @return
         */
    public String getSendCommand(String functionCode,String address,String dataLength,String writeLength,String writeData){

        String command  = "";
        //获取完整的发送指令
        switch (functionCode){
            case ModbusFunctionCode.CODE_SIX:
                command = ModbusRtuCmdGenerator.getWriteSixCommand("",address,writeData);
                break;
            default:
                break;
        }
      return command;
    }

}
