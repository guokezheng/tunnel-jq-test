package com.tunnel.deal.enums;

/**
 * describe: 设备协议标识-枚举类
 *
 * @author zs
 * @date 2023/5/29
 */
public enum DeviceProtocolCodeEnum {
    SANJING_LIGHT_PROTOCOL_CODE("SANJING_LIGHT_PROTOCOL","三晶照明HTTP协议"),
    XINGDIAN_BROADCAST_PROTOCOL_CODE("XINGDIAN_BROADCAST_PROTOCOL","兴电广播HTTP协议"),
    TUNSHAN_PLC_PROTOCOL_CODE("TUNSHAN_PLC_PROTOCOL","上海吞山PLC Modbus TCP协议"),
    ZHENGCHEN_MCA_PROTOCOL_CODE("ZHENGCHEN_MCA_PROTOCOL","正晨测控执行器Modbus TCP协议"),
    HONGMENG_HTTP_PROTOCOL_CODE("HONGMENG_HTTP_PROTOCOL","杭山东隧道鸿蒙控制器HTTP协议"),
    HONGMENG_MQTT_PROTOCOL_CODE("HONGMENG_MQTT_PROTOCOL","胡山隧道鸿蒙控制器MQTT协议"),
    SANSI_LIGHT_PROTOCOL_CODE("SANSI_LIGHT_PROTOCOL","三思照明HTTP协议"),
    ZHENGCHEN_INDUCTION_LAMP_PROTOCOL_CODE("ZHENGCHEN_INDUCTION_LAMP_PROTOCOL","山东正晨诱导灯TCP协议"),
    XIANKE_INDUCTION_LAMP_PROTOCOL_CODE("XIANKE_INDUCTION_LAMP_PROTOCOL","深圳显科诱导灯TCP协议"),
    ZHENGCHEN_EVACUATION_SIGN_PROTOCOL_CODE("ZHENGCHEN_EVACUATION_SIGN_PROTOCOL","山东正晨疏散标志TCP协议"),
    GUANGDIAN_VMS_PROTOCOL_CODE("GUANGDIAN_VMS_PROTOCOL","光电比特可变信息标志TCP协议"),
    SANSI_VMS_PROTOCOL("SANSI_VMS_PROTOCOL","三思可变信息标志TCP协议"),
    XIANKE_WARN_LIGHT_PROTOCOL_CODE("XIANKE_WARN_LIGHT_PROTOCOL","深圳显科警示灯带Modbus RTU协议"),
    OU_MU_LONG_PLC_PROTOCOL_CODE("OU_MU_LONG_PLC_PROTOCOL","欧姆龙PLC TCP通信协议"),
    LI_DIAN_BROADCAST_PROTOCOL_CODE("LI_DIAN_BROADCAST_PROTOCOL","深圳力电广播HTTP协议"),
    OU_MU_LONG_PLC_TCP_NETTY_PROTOCOL_CODE("OU_MU_LONG_PLC_TCP_NETTY_PROTOCOL","欧姆龙PLC TCP Netty通信协议");


    private String code;
    private String name;

    DeviceProtocolCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 判断是否包含
     * @param code
     * @return
     */
    public static Boolean contains(String code){

        if(null == code){
            return false;
        }

        for(DeviceProtocolCodeEnum codeEnum: DeviceProtocolCodeEnum.values()){

            if(code.equals(codeEnum.code)){
                return true;
            }

        }

        return false;
    }

    /**
     * 传code返回name
     * @param code
     * @return
     */
    public static String getValue(String code) {
        // 遍历枚举
        for (DeviceProtocolCodeEnum value : DeviceProtocolCodeEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))){
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }

}
