package com.tunnel.deal.xiaofangpao.msgEnum;

/**
 * describe: 设备方向-枚举类
 *
 * @author zs
 * @date 2023/5/29
 */
public enum DevicesDirectionCodeEnum {


    JINANFANGXIANG("1","潍坊方向"),
    WEIFANGFANGXIANG("2","济南方向");


    private String code;
    private String name;

    DevicesDirectionCodeEnum(String code, String name) {
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

        for(DevicesDirectionCodeEnum codeEnum: DevicesDirectionCodeEnum.values()){

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
        for (DevicesDirectionCodeEnum value : DevicesDirectionCodeEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))){
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }

}
