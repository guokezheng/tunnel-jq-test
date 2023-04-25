package com.tunnel.business.datacenter.domain.enumeration;

/**
 * describe: 设备状态类型定义
 *
 * @author zs
 * @date 2023/4/24
 */
public enum DeviceStateTypeEnum {

    /**
     * 运行状态
     */
    run_status("1","运行状态"),
    /**
     * 数据状态
     */
    data_status("2","数据状态");



    DeviceStateTypeEnum(String code,String name){
        this.code = code;
        this.name = name;
    }

    /**
     * 代号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

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


}
