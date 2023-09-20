package com.tunnel.deal.tcp.modbus;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.deal.tcp.client.netty.NettyCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * describe: Modbus指令发送类
 *
 * @author zs
 * @date 2023/4/3
 */
@Component
public class ModbusCmd {

    private static final Logger log = LoggerFactory.getLogger(ModbusCmd.class);

    private NettyCmd nettyCmd = SpringUtils.getBean(NettyCmd.class);


    public static Boolean commandLock = true;


    /**
     * 发送控制指令
     * @param deviceMap
     * @param deviceId
     * @param functionCode
     * @param writeAddress
     * @param writeData
     */
    public AjaxResult sendControlCommand(Map<String,Map> deviceMap,String deviceId,String functionCode,String writeAddress,String writeLength,String writeData){
       return sendCommand(deviceMap,deviceId,functionCode,writeAddress,"",writeLength,writeData);

    }

    /**
     * 发送查询指令
     * @param deviceId 设备ID
     * @param functionCode 功能码
     * @param address 起始地址
     * @param dataLength 读取长度
     */
    public void sendQueryCommand(Map<String,Map> deviceMap,String deviceId,String functionCode,String address,String dataLength){
        if(!commandLock){
            //没有拿到锁,不执行查询指令
            return;
        }
        sendCommand(deviceMap,deviceId,functionCode,address,dataLength,"","");
    }

    /**
     * 发送写入线圈指令
     * @param deviceId 设备ID
     * @param functionCode 功能码
     * @param writeLength 写入数量
     * @param writeData 写入数据
     * @return
     */
    public AjaxResult sendWriteCoilCommand(Map<String,Map> deviceMap,String deviceId,String functionCode,String writeAddress,String writeLength,String writeData){
        return sendCommand(deviceMap,deviceId,functionCode,writeAddress,"",writeLength,writeData);
    }

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
    public AjaxResult sendCommand(Map<String,Map> deviceMap,String deviceId,String functionCode,String address,String dataLength,String writeLength,String writeData){

        Map map = deviceMap.get(deviceId);

        String ip = map.get("ip") == null ? "" : map.get("ip").toString();
        String port = map.get("port") == null ? "" : map.get("port").toString();
//        String deviceAddress = map.get("deviceAddress") == null ? "" : map.get("deviceAddress").toString();

        String command  = getCommand(functionCode,address,dataLength,writeLength,writeData);
        //发送指令
      return nettyCmd.executeCommand(deviceId,ip,port,command);
    }


    /**
     * 获得下发的指令报文
     * @param functionCode 功能码
     * @param address 起始地址
     * @param dataLength 读取长度
     * @param writeLength 写入数量
     * @param writeData 写入数据
     * @return
     */
    public String getCommand(String functionCode,String address,String dataLength,String writeLength,String writeData){
        //使用指令的起始地址生成报文序列号，返回报文中也会携带起始地址，方便定位数据点位，
        // 解决返回数据多个字节中需要丢弃部分点位数据的问题，比如 40051-40053，读取3个点位，舍弃40052的读数
        //2个字节的序列号最大数为65535，点位表中录入的点位地址不可超过此最大值
        String hexSerial = ModbusCmdGenerator.getHexByte(Integer.valueOf(address));

        String command  = "";
        //获取完整的发送指令
        switch (functionCode){
            case ModbusFunctionCode.CODE_TWO:
                command =  ModbusCmdGenerator.getReadTwoCommand("",hexSerial,address,dataLength);
                break;
            case ModbusFunctionCode.CODE_THREE:
                command =  ModbusCmdGenerator.getReadThreeCommand("",hexSerial,address,dataLength);
                break;
            case ModbusFunctionCode.CODE_FOUR:
                command = ModbusCmdGenerator.getReadFourCommand("",hexSerial,address,dataLength);
                break;
            case ModbusFunctionCode.CODE_SIX:
                command = ModbusCmdGenerator.getWriteSixCommand("",hexSerial,address,writeData);
                break;
            case ModbusFunctionCode.CODE_FIFTEEN:
                command = ModbusCmdGenerator.getWriteFifteenCommand("",hexSerial,address,writeLength,writeData);
            default:
                break;
        }
        return command;
    }


    /**
     * 发送指令
     * @param deviceId 设备ID
     * @param ip
     * @param port
     * @param command
     */
    public  AjaxResult executeCommand(String deviceId, String ip, String port, String command){
        return nettyCmd.executeCommand(deviceId,ip,port,command);
    }


    /**
     * 线程休眠固定时间
     * @param ms 毫秒
     */
    public void sleep(int ms){
        //间隔固定时间（毫秒）发送指令，避免同一个设备连续多次发送指令无回复
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
