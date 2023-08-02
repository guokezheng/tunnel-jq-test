package com.tunnel.platform.controller.energyManagement.enumeration;

import com.tunnel.business.datacenter.domain.enumeration.TunnelCodeEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;

/**
 * @author yangqichao
 */
public enum EnergyTunnelEnum implements TunnelCodeEnum {


    HANGSHANDONG("JQ-WeiFang-JiuLongYu-HSD", "000000s001637000010"),
    JINJIALOU("JQ-WeiFang-JiuLongYu-JJL", "000000s001637000009"),
    MAANSHAN("JQ-WeiFang-JiuLongYu-MAS", "000000s001637000008"),
    SHUANGZISHAN("JQ-WeiFang-YangTianShan-SZS", "000000s001637000007"),
    YANGTIANSHAN("JQ-WeiFang-YangTianShan-YTS", "000000s001637000006"),
    BEIJIAYU("JQ-WeiFang-MiaoZi-BJY", "000000s001637000005"),
    WANCHANGLIU("JQ-WeiFang-MiaoZi-WCL", "000000s001637000004"),
    PANDINGSHAN("JQ-ZiBo-TaiHe-PDS", "000000s001637000003"),
    QINGFENGLING("JQ-ZiBo-TaiHe-QFL", "000000s001637000002"),
    MAJIAYU("JQ-JiNan-WenZuBei-MJY", "000000s001637000001");
    //FENGHUANGSHAN("WLJD-JiNan-YanJiuYuan-FHS", ""),



    private String tunnelCode;
    private String energyCode;

    EnergyTunnelEnum(String tunnelCode, String energyCode) {
        this.tunnelCode = tunnelCode;
        this.energyCode = energyCode;
    }

    @Override
    public String getTunnel() {
        return tunnelCode;
    }

    @Override
    public String getEnergy() {
        return energyCode;
    }

    public static String getValue(String code) {
        // 遍历枚举
        for (EnergyTunnelEnum value : EnergyTunnelEnum.values()) {
            String s = value.getEnergy() + "";
            if (s.equals(String.valueOf(code))) {
                return value.getTunnel();
            }
        }
        // 其他情况
        return null;
    }
}
