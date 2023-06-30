package com.tunnel.deal.enums;

/**
 * describe: 设备协议标识-枚举类
 *
 * @author zs
 * @date 2023/5/29
 */
public enum DeviceProtocolCodeEnum {

    TUNSHAN_MCA_PROTOCOL_CODE("TUNSHAN_MCA_PROTOCOL","吞山测控执行器协议"),
    HONGMENG_MQTT_PROTOCOL_CODE("HONGMENG_MQTT_PROTOCOL","胡山隧道鸿蒙控制器MQTT协议"),
    HONGMENG_HTTP_PROTOCOL_CODE("HONGMENG_HTTP_PROTOCOL","杭山东隧道鸿蒙控制器HTTP控制协议"),
    SANJING_LIGHT_PROTOCOL_CODE("SANJING_LIGHT_PROTOCOL","三晶照明协议"),
    SANSI_LIGHT_PROTOCOL_CODE("SANSI_LIGHT_PROTOCOL","三思照明协议");

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
