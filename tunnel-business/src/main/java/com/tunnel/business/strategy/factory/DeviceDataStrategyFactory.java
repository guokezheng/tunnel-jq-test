package com.tunnel.business.strategy.factory;

import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.strategy.domain.*;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe: 设备数据处理工厂类
 *
 * @author zs
 * @date 2023/4/23
 */
@Component
public class DeviceDataStrategyFactory {

    private static final Map<Long, DeviceDataStrategyService> MAP = new ConcurrentHashMap<>();

    /**
     * 默认匹配
     */
    private static final DeviceDataStrategyService DEFAULT = new DefaultType();

    // 版本升级或新增模块、插件（添加枚举，定好具体实现类）
    static {
//        //车指
//        MAP.put(DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode(), new LaneIndicator());
//        //左转车指
//        MAP.put(DevicesTypeEnum.ZHUO_ZHUAN_CHE_ZHI.getCode(),new LaneIndicator());
//        //射流风机
//        MAP.put(DevicesTypeEnum.FENG_JI.getCode(), new JetFan());
        MAP.put(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode(), new EvacuationSign());
        MAP.put(DevicesTypeEnum.YOU_DAO_DENG.getCode(), new InductionLamp());
        MAP.put(DevicesTypeEnum.SHENG_GUANG_BAO_JING.getCode(), new AuralVisualAlarm());
        MAP.put(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode(), new EnhancedLight());
        MAP.put(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode(), new BasicLight());
        MAP.put(DevicesTypeEnum.ROBOT.getCode(), new PatrolRobot());
        MAP.put(DevicesTypeEnum.SHUI_BENG.getCode(), new FirePump());
        MAP.put(DevicesTypeEnum.WEN_SHI_DU_CHUAN_GAN_QI.getCode(), new RHTempSensor());
        //警示灯带
        MAP.put(DevicesTypeEnum.JING_SHI_DENG_DAI.getCode(), new WarningLight());
    }

    /**
     * 匹配策略
     * @param type 设备类型ID
     * @return
     */
    public DeviceDataStrategyService strategy(String type) {
        DeviceDataStrategyService strategyService = MAP.get(Long.valueOf(type));
        if(strategyService == null){
            //默认匹配
            strategyService = DEFAULT;
        }
      return strategyService;
    }


}
