package com.tunnel.deal.tcp.plc.omron.fins;

/**
 * describe: Fins 请求码枚举类
 *
 * @author zs
 * @date 2023/8/24
 */
public enum FinsQueryCodeEnum {

    READ_CODE("0101","读操作"),
    WRITE_CODE("0102","写操作"),
    FORCE_CODE("2301","强制操作");

    private String code;
    private String name;

    FinsQueryCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
