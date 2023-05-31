package com.tunnel.deal.mqtt.strategy;

import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.deal.mqtt.service.HongMengMqttService;
import com.tunnel.deal.mqtt.service.impl.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe: 鸿蒙控制器--设备数据处理工厂类
 *
 * @author zs
 * @date 2023/4/23
 */
@Component
public class HongMengMqttStrategyFactory {

    private static final Map<Long, HongMengMqttService> MAP = new ConcurrentHashMap<>();

//    /**
//     * 默认匹配
//     */
//    private static final HongMengMqttService DEFAULT = new DefaultType();

    // 版本升级或新增模块、插件（添加枚举，定好具体实现类）
    static {
        //车指
        MAP.put(DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode(), new LaneIndicatorMqttServiceImpl());
        MAP.put(DevicesTypeEnum.ZHUO_ZHUAN_CHE_ZHI.getCode(), new LaneIndicatorMqttServiceImpl());
        //风机
        MAP.put(DevicesTypeEnum.FENG_JI.getCode(), new FanMqttServiceImpl());
        MAP.put(DevicesTypeEnum.ZHOU_LIU_FENG_JI.getCode(), new FanMqttServiceImpl());
        //交通信号灯
        MAP.put(DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode(),new TrafficLightsMqttServiceImpl());
        MAP.put(DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode(),new TrafficLightsMqttServiceImpl());
        //卷帘门
        MAP.put(DevicesTypeEnum.JUAN_LIAN_MEN.getCode(),new RollDoorMqttServiceImpl());
        //风速风向
        MAP.put(DevicesTypeEnum.FENG_SU_FENG_XIANG.getCode(),new AnemographMqttServiceImpl());
        //警示灯带
        MAP.put(DevicesTypeEnum.JING_SHI_DENG_DAI.getCode(),new WarnLightStripMqttServiceImpl());
        //CO、VI
        MAP.put(DevicesTypeEnum.CO_VI.getCode(),new CoViMqttServiceImpl());
        //洞内亮度
        MAP.put(DevicesTypeEnum.LIANG_DU_JIAN_CE_INSIDE.getCode(),new IlluminanceMqttServiceImpl());
        //洞外亮度
        MAP.put(DevicesTypeEnum.LIANG_DU_JIAN_CE.getCode(),new BrightDetectorMqttServiceImpl());
        //远传压力表
        MAP.put(DevicesTypeEnum.YA_LI_BIAO.getCode(),new PressureGaugeMqttServiceImpl());
        //风机安全检测仪
        MAP.put(DevicesTypeEnum.NEI_WAI_ZHEN_DONG_YI_JIANCEQI.getCode(),new FanSafeMonitorMqttServiceImpl());
        //鸿蒙控制器
        MAP.put(DevicesTypeEnum.CE_KONG_ZHI_XING_QI.getCode(),new HongMengControllerMqttServiceImpl());


    }

    /**
     * 匹配策略
     * @param type 设备类型ID
     * @return
     */
    public HongMengMqttService strategy(String type) {
        HongMengMqttService strategyService = MAP.get(Long.valueOf(type));
      return strategyService;
    }


}
