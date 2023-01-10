package com.tunnel.business.datacenter.domain.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum MergeRhyEventTypeEnum {
    SJYS("100001",14L,1),
    BD("100002",13L,1),
    MX("100004",11L,1),
    SG("100009",10L,0),
    YJCD("1000012",15L,0),
    JTSG("200001",12L,1),
    HZ("200002",20L,0),
    YCTC("200003",4L,1),
    NX("200004",1L,1),
    DLYS("200007",3L,1),
    JTYD("200008",2L,1),
    XR("200005",18L,1),
    JJDH("2000011",21L,0),
    HZBJ("2000012",20L,0);
    //瑞华赢事件类型
    private String rhyType;
    //对应本系统事件类型ID
    private Long typeId;
    //是否推送,1 推送，0 不推送（上传到物联平台的事件类型过滤）
    private int pushOrNot;

    MergeRhyEventTypeEnum(String rhyType, Long typeId, int pushOrNot) {
        this.rhyType = rhyType;
        this.typeId = typeId;
        this.pushOrNot = pushOrNot;
    }

    public String getRhyType() {
        return rhyType;
    }

    public void setRhyType(String rhyType) {
        this.rhyType = rhyType;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public int getPushOrNot() {
        return pushOrNot;
    }

    public void setPushOrNot(int pushOrNot) {
        this.pushOrNot = pushOrNot;
    }

    private final static Map<String, MergeRhyEventTypeEnum> map = new HashMap<>();

    static {
        for (MergeRhyEventTypeEnum mergeRhyEventTypeEnum : MergeRhyEventTypeEnum.values()) {
            map.put(mergeRhyEventTypeEnum.rhyType, mergeRhyEventTypeEnum);
        }
    }

    public static MergeRhyEventTypeEnum getMergeEvent(String param) {
        return map.get(param);
    }
}
