package com.tunnel.deal.guidancelamp.redisHandle;

import com.alibaba.fastjson.JSON;
import com.google.auto.service.AutoService;
import com.tunnel.deal.guidancelamp.control.NettyClient;
import com.tunnel.deal.guidancelamp.control.inductionlamp.InductionlampUtil;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.utils.util.SpringContextUtils;
import com.zc.common.core.redis.RedisMessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;

import java.util.Map;

/**
 * 处理接收 Redis 诱导灯控制缓存订阅消息
 */
@AutoService(RedisMessageDispatcher.class)
public class GuidanceLampMsgReceivedHandle implements RedisMessageDispatcher {

    private static Logger logger = LoggerFactory.getLogger(GuidanceLampMsgReceivedHandle.class);

    @Autowired
    ISdDevicesService devicesService;

    private Integer a = 0;

    @Override
    public boolean isChannelExist(String channel) {
        if ("GL:CONTROL".equals(channel)) {
            return true;
        }
        return false;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        a++;
        String ctrBody = new String(message.getBody());
        logger.info("收到的mq消息" + ctrBody);
        toControlDev(ctrBody);
    }

    // 进行诱导灯设备控制
    private void toControlDev(String ctrBody) {
        Map<String, Object> ctrResult = (Map<String, Object>) JSON.parse(ctrBody);
        String deviceId = ctrResult.get("deviceId").toString();
        devicesService = (ISdDevicesService) SpringContextUtils.getBean(ISdDevicesService.class);
        SdDevices sdDevices = devicesService.selectSdDevicesById(deviceId);
        String ip = sdDevices.getIp();
        int port = Integer.parseInt(sdDevices.getPort());
        int brightness = Integer.parseInt(ctrResult.get("brightness").toString());
        int frequency = Integer.parseInt(ctrResult.get("frequency").toString());
        Integer controlModeType = Integer.parseInt(ctrResult.get("ctrState").toString());
        //发送诱导灯控制指令
        try {
            String code = "1GH+STATUS?\r\n";
            NettyClient client = new NettyClient(ip, port,code,1);
            client.start(null);
            Map codeMap = InductionlampUtil.getPilotLightMode(controlModeType,brightness,frequency);
            client.pushCode(codeMap.get("code").toString());
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
