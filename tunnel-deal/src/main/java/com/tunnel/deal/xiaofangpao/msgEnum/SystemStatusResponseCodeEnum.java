package com.tunnel.deal.xiaofangpao.msgEnum;

/**
 * describe: 设备协议标识-枚举类
 *
 * @author zs
 * @date 2023/5/29
 */
public enum SystemStatusResponseCodeEnum {


    XITONGZHUANGTAI_SHANGCHUAN("02","系统状态/设备状态上传"),
    XITONGZHUANGTAI_SHANGCHUANYINGDA("03","系统状态/设备状态上传应答"),
    XITONGZHUANGTAI_XIAXING("04","系统状态/设备状态下行"),
    XITONGZHUANGTAI_XIAXINGYINGDA("05","系统状态/设备状态下行应答");



    private String code;
    private String name;

    SystemStatusResponseCodeEnum(String code, String name) {
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

        for(SystemStatusResponseCodeEnum codeEnum: SystemStatusResponseCodeEnum.values()){

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
        for (SystemStatusResponseCodeEnum value : SystemStatusResponseCodeEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))){
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }

}
