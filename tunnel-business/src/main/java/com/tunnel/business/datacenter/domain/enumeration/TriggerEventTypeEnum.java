package com.tunnel.business.datacenter.domain.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * 作用于自动触发预警生成预警事件
 * 根据设备类型及数据项匹配事件类型
 */
public enum TriggerEventTypeEnum {
    //光强异常 洞内亮度
    INSIDE_BRIGHTNESS("18","7",102L),
    //光强异常 洞外亮度
    OUTSIDE_BRIGHTNESS("5","12",102L),
    //CO异常
    CO_ABNORMAL("19","1",103L),
    //能见度异常
    VI_ABNORMAL("19","2",101L);

    //设备类型
    private String eqType;
    //数据项
    private String itemId;
    //事件类型
    private Long eventType;

    public String getItemId() {
        return itemId;
    }

    TriggerEventTypeEnum(String eqType, String itemId, Long eventType) {
        this.eqType = eqType;
        this.itemId = itemId;
        this.eventType = eventType;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getEqType() {
        return eqType;
    }

    public void setEqType(String eqType) {
        this.eqType = eqType;
    }

    public Long getEventType() {
        return eventType;
    }

    public void setEventType(Long eventType) {
        this.eventType = eventType;
    }

    private final static Map<String, TriggerEventTypeEnum> map = new HashMap<>();

    static {
        for (TriggerEventTypeEnum TriggerEventTypeEnum : TriggerEventTypeEnum.values()) {
            map.put(TriggerEventTypeEnum.getEqType()+TriggerEventTypeEnum.getItemId(), TriggerEventTypeEnum);
        }
    }

    public static TriggerEventTypeEnum getTriggerEventTypeEnum(String param) {
        return map.get(param);
    }
}
