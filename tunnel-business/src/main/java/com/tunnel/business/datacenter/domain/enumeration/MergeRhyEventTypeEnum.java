package com.tunnel.business.datacenter.domain.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum MergeRhyEventTypeEnum {
    SJYS("100001",14L),
    BD("100002",13L),
    MX("100004",11L),
    SG("100009",10L),
    YJCD("1000012",15L),
    JTSG("200001",12L),
    HZ("200002",20L),
    YCTC("200003",4L),
    NX("200004",1L),
    DLYS("200007",3L),
    JTYD("200008",2L),
    JJDH("2000011",21L),
    HZBJ("2000012",20L);
    //瑞华赢事件类型
    private String rhyType;
    //对应本系统事件类型ID
    private Long typeId;
    MergeRhyEventTypeEnum(String rhyType,Long typeId){
        this.typeId = typeId;
        this.rhyType = rhyType;
    }
    private final static Map<String, Long> map = new HashMap<>();

    static {
        for (MergeRhyEventTypeEnum mergeRhyEventTypeEnum : MergeRhyEventTypeEnum.values()) {
            map.put(mergeRhyEventTypeEnum.rhyType, mergeRhyEventTypeEnum.typeId);
        }
    }

    public static Long getMergeEventTypeId(String param) {
        return map.get(param);
    }
}
