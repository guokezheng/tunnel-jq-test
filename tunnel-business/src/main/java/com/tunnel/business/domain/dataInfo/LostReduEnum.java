package com.tunnel.business.domain.dataInfo;

/**
 * 漏检多捡：0-正常，1-漏检，2-多捡
 */
public enum LostReduEnum {
    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 漏检
     */
    LOST(1),

    /**
     * 多捡
     */
    REDU(2);

    LostReduEnum(int i) {

    }
}
