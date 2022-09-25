package com.tunnel.business.datacenter.domain.enumeration;

/**
 * describe: 事件流程描述信息枚举类
 *
 * @author zs
 * @date 2022/9/8
 */
public enum EventDescEnum {

    event_source_radar("0","接收到雷达监测信息，"),
    event_source_fire("1","接收到火灾报警系统信息，"),
    event_source_phone("2","接收到紧急电话系统信息，"),
    event_source_add("3","事件信息录入，"),
    event_source_robot("4","接收到巡检机器人系统信息，"),
    event_source_fire_monitor("5","接收到消防炮系统信息，"),
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


    /**
     * 传code返回name
     * @param code
     * @return
     */
    public static String getName(String code) {
        // 遍历枚举
        for (EventDescEnum item : EventDescEnum.values()) {
            String s = item.getCode();
            if (s.equals(String.valueOf(code))){
                return item.getName();
            }
        }
        // 其他情况
        return "";
    }
}
