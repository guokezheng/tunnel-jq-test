package com.tunnel.deal.tcp.modbus;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.tcp.client.config.ChannelKey;
import com.tunnel.deal.tcp.client.config.DeviceManager;
import com.tunnel.deal.tcp.client.netty.MCASocketClient;
import com.tunnel.deal.tcp.util.ByteBufUtil;
import io.netty.channel.Channel;
import org.apache.commons.codec.DecoderException;
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

    private static final ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);


    /**
     * 截取有效地址
     * @param writeAddress 写入地址
     * @return
     */
    public String getValidAddress(String writeAddress){
       String subStr =  writeAddress.substring(1);
      return  String.valueOf(Integer.valueOf(subStr));
    }


    /**
     * 发送控制指令
     * @param deviceId
     * @param functionCode
     * @param writeAddress
     * @param writeData
     */
    public AjaxResult sendControlCommand(String deviceId,String functionCode,String writeAddress,String writeLength,String writeData){
       return sendCommand(deviceId,functionCode,writeAddress,"",writeLength,writeData);

    }

    /**
     * 发送查询指令
     * @param deviceId 设备ID
     * @param functionCode 功能码
     * @param address 起始地址
     * @param dataLength 读取长度
     */
    public void sendQueryCommand(String deviceId,String functionCode,String address,String dataLength){
        sendCommand(deviceId,functionCode,address,dataLength,"","");
    }

    /**
     * 发送写入线圈指令
     * @param deviceId 设备ID
     * @param functionCode 功能码
     * @param writeLength 写入数量
     * @param writeData 写入数据
     * @return
     */
    public AjaxResult sendWriteCoilCommand(String deviceId,String functionCode,String writeAddress,String writeLength,String writeData){
        return sendCommand(deviceId,functionCode,writeAddress,"",writeLength,writeData);
    }

    /**
     * 发送特定类型的指令，读取设备数据或设备状态
     * @param deviceId 设备ID
     * @param functionCode 功能码
     * @param address 起始地址
     * @param dataLength 读取长度
     * @param writeLength 写入数量
     * @param writeData 写入数据
     * @return
     */
    public AjaxResult sendCommand(String deviceId,String functionCode,String address,String dataLength,String writeLength,String writeData){

        //使用指令的起始地址生成报文序列号，返回报文中也会携带起始地址，方便定位数据点位，
        // 解决返回数据多个字节中需要丢弃部分点位数据的问题，比如 40051-40053，读取3个点位，舍弃40052的读数
        //2个字节的序列号最大数为65535，点位表中录入的点位地址不可超过此最大值
        String hexSerial = ModbusCmdGenerator.getHexByte(Integer.valueOf(address));

        Map<String, Map> deviceMap = DeviceManager.deviceMap;
        Map map = deviceMap.get(deviceId);

        String ip = map.get("ip") == null ? "" : map.get("ip").toString();
        String port = map.get("port") == null ? "" : map.get("port").toString();
//        String deviceAddress = map.get("deviceAddress") == null ? "" : map.get("deviceAddress").toString();

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
        //发送指令
      return executeCommand(deviceId,ip,port,command);
    }


    /**
     * 发送指令
     * @param deviceId 设备ID
     * @param ip
     * @param port
     * @param command
     */
    public AjaxResult executeCommand(String deviceId,String ip,String port,String command){

        if(!"".equals(ip) && !"".equals(port)){
            int portNum = Integer.valueOf(port);
            Channel channel = MCASocketClient.channels.get(ChannelKey.getChannelKey(ip,portNum));
            if (channel != null && channel.isActive()) {
                try {
                    channel.writeAndFlush(ByteBufUtil.convertStringToByteBuf(command.replace(" ","")));
//                    System.out.println("发送命令：ip="+ip+",cmd="+command);
//                    log.info("向设备["+ip+":"+port+"],发送指令："+command);
                } catch (DecoderException e) {
                    e.printStackTrace();
                    //  报错判定设备离线，将网关设备及子设备设置为离线
                    sdDevicesService.updateOfflineStatus(deviceId,true);
                    return AjaxResult.error("设备指令发送报错");
                }
            }else{
                //未连接到设备，将网关设备及子设备设置为离线
                sdDevicesService.updateOfflineStatus(deviceId,true);
                return AjaxResult.error("未连接到设备，发送失败");
            }
        }else{
            return AjaxResult.error("未配置测控执行器IP或端口号");
        }
        return AjaxResult.success("设备指令发送成功");
    }




    /**
     * 循环发送特定类型的指令，读取设备数据或设备状态
     * @param functionCode
     */
    public void sendCommand( String functionCode){
        Map<String, Map> deviceMap = DeviceManager.deviceMap;
        deviceMap.forEach((deviceId,map) ->{
            String hexSerial = "0000";
            String ip = map.get("ip") == null ? "" : map.get("ip").toString();
            String port = map.get("port") == null ? "" : map.get("port").toString();
            String deviceAddress = map.get("deviceAddress") == null ? "" : map.get("deviceAddress").toString();
            String command  = "";
            //获取完整的发送指令
            switch (functionCode){
                case ModbusFunctionCode.CODE_THREE:
                    //暂时用固定地址点位测试，后期优化 todo
                    command =  ModbusCmdGenerator.getReadThreeCommand(deviceAddress,hexSerial,"49","10");
                    break;
                case ModbusFunctionCode.CODE_FOUR:
                    command = ModbusCmdGenerator.getReadFourCommand(deviceAddress,hexSerial,"24","4");
                    break;
                default:
                    break;
            }
            //发送指令
            executeCommand(deviceId,ip,port,command);


        });
    }

}
