package com.tunnel.deal.xiaofangpao.msgEnum;

/**
 * describe: 设备协议标识-枚举类
 *
 * @author zs
 * @date 2023/5/29
 */
public enum DataTypeCodeEnum {


    XITONGZHUANGTAI_UP("01","系统状态"),
    XITONGZHUANGTAI_DOWN("3D","系统状态"),
    SHEBEIZHUANGTAI_UP("02","设备状态"),
    SHEBEIZHUANGTAI_DOWN("3E","设备状态"),
    QUANXIANJIAOYAN("3C","权限校验");

    private String code;
    private String name;

    DataTypeCodeEnum(String code, String name) {
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

        for(DataTypeCodeEnum codeEnum: DataTypeCodeEnum.values()){

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
        for (DataTypeCodeEnum value : DataTypeCodeEnum.values()) {
            String s = value.getCode() + "";
            if (s.equals(String.valueOf(code))){
                return value.getName();
            }
        }
        // 其他情况
        return null;
    }

}
