package com.tunnel.deal.xiaofangpao.msgEnum;

/**
 * describe: 设备协议标识-枚举类
 *
 * @author zs
 * @date 2023/5/29
 */
public enum SendMsgCodeEnum {

    XIAOFANGPAO_QUANXIANJIAOYAN_CODE("40 40 02 00 01 06 01 02 11 04 0A 17 00 00 00 00 00 00 00 00 00 00 00 00 24 00 02 3C 01 01 02 36 45 36 30 31 37 41 37 31 34 35 39 33 35 41 30 45 46 46 45 31 37 31 34 46 42 41 41 32 38 34 38 D8 23 23","权限校验"),
    XIAOFANGPAO_SYSTEM_STATUS_CODE("40 40 03 00 01 06 28 02 11 04 0A 17 00 00 00 00 00 00 00 00 00 00 00 00 04 00 04 3D 01 18 D4 9C 23 23","系统状态");

    private String code;
    private String name;

    SendMsgCodeEnum(String code, String name) {
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

        for(SendMsgCodeEnum codeEnum: SendMsgCodeEnum.values()){

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
        for (SendMsgCodeEnum value : SendMsgCodeEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))){
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }

}
