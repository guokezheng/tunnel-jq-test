package com.tunnel.business.domain.trafficOperationControl.controlConfig;

/**
 * describe: 管控等级配置-状态枚举值
 *
 * @author zs
 * @date 2022/2/24
 */
public enum LevelConfigStatusEnum {

    /**
     * 开启
     */
    open_status("0","开启"),

    /**
     * 关闭
     */
    close_status("1","关闭");

    LevelConfigStatusEnum(String code,String name){
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
