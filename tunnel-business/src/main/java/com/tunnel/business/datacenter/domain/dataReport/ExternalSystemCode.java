package com.tunnel.business.datacenter.domain.dataReport;

/**
 * describe: 字典类型枚举类
 *
 * @author tjw
 * @date 2023/3/8
 */
public enum ExternalSystemCode {
    /**
     * 任务状态字典
     */

    VIDEO_MANGE("video_manage","视频平台"),
    ENERGY_MANAGE("energy_manage","能源管控平台"),
    ENERGY_MANAGE_OF_TUNNEL("energy_manage_of_tunnel","隧道能源管控平台");



    ExternalSystemCode(String code, String name){
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
