package com.tunnel.deal.tcp.client.netty;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.deal.tcp.client.config.ChannelKey;
import com.tunnel.deal.tcp.util.ByteBufUtil;
import io.netty.channel.Channel;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * describe: netty下发命令类
 *
 * @author zs
 * @date 2023/8/22
 */
@Component
public class NettyCmd {

    private static final Logger log = LoggerFactory.getLogger(NettyCmd.class);

    private static final ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

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
                    System.out.println("发送命令：ip="+ip+",cmd="+command+",时间："+System.currentTimeMillis());
                    log.info("向设备["+ip+":"+port+"],发送指令："+command);
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
            return AjaxResult.error("未配置设备IP或端口号");
        }
        return AjaxResult.success("设备指令发送成功");
    }
}
