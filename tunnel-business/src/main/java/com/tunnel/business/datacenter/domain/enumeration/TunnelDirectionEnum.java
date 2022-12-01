package com.tunnel.business.datacenter.domain.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum TunnelDirectionEnum {
    UP_DIRECTION("1", "上行"),
    DOWN_DIRECTION("2", "下行");

    private String code;
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

    TunnelDirectionEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private final static Map<String, String> map = new HashMap<>();

    static {
        for (TunnelDirectionEnum tunnelDirectionEnum : TunnelDirectionEnum.values()) {
            map.put(tunnelDirectionEnum.getCode(), tunnelDirectionEnum.getName());
        }
    }

    public static String getTunnelDirection(String param) {
        return map.get(param);
    }
}
