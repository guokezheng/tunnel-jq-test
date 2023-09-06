package com.tunnel.deal.tcp.plc.omron;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.deal.tcp.client.netty.NettyCmd;
import com.tunnel.deal.tcp.plc.omron.fins.FinsCmdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * describe: fins指令下发类
 *
 * @author zs
 * @date 2023/8/17
 */
@Component
public class FinsCmd {

    private static final Logger log = LoggerFactory.getLogger(FinsCmd.class);

    private NettyCmd nettyCmd = SpringUtils.getBean(NettyCmd.class);

    /**
     * 发送查询指令
     * @param deviceMap
     * @param deviceId 设备ID
     * @param sourceAddress 源地址
     * @param destinationAddress 目的地址
     * @param area
     * @param address 起始地址
     * @param bitAddress 位地址
     * @param readLength 读取长度
     */
    public void sendQueryCommand(Map<String,Map> deviceMap,String deviceId,String sourceAddress,String destinationAddress,String area,String address,String bitAddress,String readLength){

        Map map = deviceMap.get(deviceId);
        String ip = map.get("ip") == null ? "" : map.get("ip").toString();
        String port = map.get("port") == null ? "" : map.get("port").toString();

       String command = FinsCmdGenerator.getReadCommand(sourceAddress,destinationAddress,area,address,bitAddress,readLength);
       nettyCmd.executeCommand(deviceId,ip,port,command);

    }


    /**
     * 发送控制指令
     * @param deviceMap
     * @param deviceId 设备ID
     * @param sourceAddress 源地址
     * @param destinationAddress 目的地址
     * @param area
     * @param address 起始地址
     * @param bitAddress 位地址
     * @param writeLength 写入长度
     * @param value 写入数据
     */
    public AjaxResult sendControlCommand(Map<String,Map> deviceMap,String deviceId,String sourceAddress,String destinationAddress,String area,String address,String bitAddress,String writeLength,String value){

        Map map = deviceMap.get(deviceId);
        String ip = map.get("ip") == null ? "" : map.get("ip").toString();
        String port = map.get("port") == null ? "" : map.get("port").toString();

        String command = FinsCmdGenerator.getControlCommand(sourceAddress,destinationAddress,area,address,bitAddress,writeLength,value);
       return nettyCmd.executeCommand(deviceId,ip,port,command);
    }


    /**
     * 发送握手指令
     * @param deviceMap
     * @param deviceId
     * @param clientNodeAddress
     * @return
     */
    public AjaxResult sendHandshakeCommand(Map<String,Map> deviceMap,String deviceId,String clientNodeAddress){
        Map map = deviceMap.get(deviceId);
        String ip = map.get("ip") == null ? "" : map.get("ip").toString();
        String port = map.get("port") == null ? "" : map.get("port").toString();
        String command = FinsCmdGenerator.getHandshakeCommand(clientNodeAddress);
        return nettyCmd.executeCommand(deviceId,ip,port,command);
    }


//
//    /**
//     * 发送特定类型的指令，读取设备数据或设备状态
//     * @param deviceMap 设备信息缓存Map
//     * @param deviceId 设备ID
//     * @param sourceAddress 源地址
//     * @param destinationAddress 目的地址
//     * @param area
//     * @param address 起始地址
//     * @param bitAddress 位地址
//     * @param readLength 读取长度
//     * @return
//     */
//    public AjaxResult sendCommand(Map<String,Map> deviceMap,String deviceId,String sourceAddress,String destinationAddress,String area,String address,String bitAddress,String readLength){
//
//        Map map = deviceMap.get(deviceId);
//        String ip = map.get("ip") == null ? "" : map.get("ip").toString();
//        String port = map.get("port") == null ? "" : map.get("port").toString();
//
//        String command  = "";
//
//        //获取完整的发送指令
//        //发送指令
////        return executeCommand(deviceId,ip,port,command);
//        return null;
//    }
}
