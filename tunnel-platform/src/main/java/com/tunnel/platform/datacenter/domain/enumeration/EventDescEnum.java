package com.tunnel.platform.datacenter.domain.enumeration;

/**
 * describe: 事件流程描述信息枚举类
 *
 * @author zs
 * @date 2022/9/8
 */
public enum EventDescEnum {

    event_source_radar("radar","根据雷达设备监测，"),
    event_source_add("add","事件信息录入，"),
    event_end("end","事件结束"),
    event_ignore("ignore","事件忽略"),
    event_handle("handle","事件已确认"),
    event_update("update","事件信息更新"),
    event_phone("phone","拨打联系人电话"),
    event_plan("plan","执行预案");

    EventDescEnum(String code,String name){
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
