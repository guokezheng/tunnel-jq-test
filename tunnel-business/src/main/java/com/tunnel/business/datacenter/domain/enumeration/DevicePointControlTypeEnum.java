package com.tunnel.business.datacenter.domain.enumeration;

/**
 * describe: 设备点位控制类型
 *
 * @author zs
 * @date 2023/4/24
 */
public enum DevicePointControlTypeEnum {
    /**
     * 只读点位
     */
    only_read(1,"只读点位"),
    /**
     * 控制点位
     */
    control_enable(2,"控制点位");




    DevicePointControlTypeEnum(long code,String name){
        this.code = code;
        this.name = name;
    }

    /**
     * 代号
     */
    private long code;

    /**
     * 名称
     */
    private String name;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
