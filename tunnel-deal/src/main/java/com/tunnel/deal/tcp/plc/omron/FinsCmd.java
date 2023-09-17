package com.tunnel.deal.tcp.plc.omron;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.tcp.client.config.ChannelKey;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import com.tunnel.deal.tcp.client.netty.TcpNettySocketClient;
import com.tunnel.deal.tcp.plc.omron.fins.FinsCmdGenerator;
import com.tunnel.deal.tcp.util.ByteBufUtil;
import io.netty.channel.Channel;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
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

    private static final ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);
    private TcpClientGeneralService tcpClientGeneralService = SpringUtils.getBean(TcpClientGeneralService.class);

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
       executeCommand(deviceId,ip,port,command);

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
       return executeCommand(deviceId,ip,port,command);
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
        return executeCommand(deviceId,ip,port,command);
    }

    public static void main(String[] args) {
        long timestamp1 = 1694166600552L;
        long timestamp2 = 1694166600464L;

        Instant instant1 = Instant.ofEpochMilli(timestamp1);
        Instant instant2 = Instant.ofEpochMilli(timestamp2);

        Duration duration = Duration.between(instant2, instant1);
        System.out.println("差异（毫秒）: " + duration.toMillis());
    }

    /**
     * 发送指令
     * @param deviceId 设备ID
     * @param ip
     * @param port
     * @param command
     */
    public AjaxResult executeCommand(String deviceId, String ip, String port, String command){

        if(!"".equals(ip) && !"".equals(port)){
            int portNum = Integer.valueOf(port);
            Channel channel = TcpNettySocketClient.channels.get(ChannelKey.getChannelKey(ip,portNum));
            if (channel != null && channel.isActive()) {
                try {
                    channel.writeAndFlush(ByteBufUtil.convertStringToByteBuf(command.replace(" ","")));
                    String deviceId1 = tcpClientGeneralService.getDeviceIdByIp(ip);
                    System.out.println("发送命令：ip="+ip+",cmd="+command+",时间："+System.currentTimeMillis());
//                    System.out.println("发送命令：ip="+ip+",设备,"+"设备id"+deviceId+",时间："+System.currentTimeMillis());
                    log.info("向设备["+ip+":"+port+"],发送指令："+command);
                } catch (DecoderException e) {
                    e.printStackTrace();
                    //  报错判定设备离线，将网关设备及子设备设置为离线
//                    sdDevicesService.updateOfflineStatus(deviceId,true);
                    return AjaxResult.error("设备指令发送报错");
                }
            }else{
                //未连接到设备，将网关设备及子设备设置为离线
//                sdDevicesService.updateOfflineStatus(deviceId,true);
                return AjaxResult.error("未连接到设备，发送失败");
            }
        }else{
            return AjaxResult.error("未配置设备IP或端口号");
        }
        return AjaxResult.success("设备指令发送成功");
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
