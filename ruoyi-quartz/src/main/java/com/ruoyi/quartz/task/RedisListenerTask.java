package com.ruoyi.quartz.task;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.digitalmodel.SdSpecialVehicles;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.mapper.digitalmodel.SdSpecialVehiclesMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @author zhai
 * @date 2022/11/12
 */
@Component
public class RedisListenerTask extends KeyExpirationEventMessageListener {

    private static final Logger log = LoggerFactory.getLogger(RedisListenerTask.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SdSpecialVehiclesMapper sdSpecialVehiclesMapper;

    @Autowired
    private ISdDevicesService devicesService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //获取过期key键
        String key = message.toString();
        if(key.contains("pushData")){
            String eqId = key.substring(key.indexOf(":") + 1, key.length());
            devicesService.updateOfflineStatus(eqId,false);
        }
        //Collection<String> keys = redisCache.keys(message.toString() + "*");
        //获取过期key键的value
        //List<SdRadarDetectData> sdRadarDetectData = (List<SdRadarDetectData>)redisTemplate.opsForValue().multiGet(keys);
    }

    public RedisListenerTask(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }
}
