package com.tunnel.platform.domain.dataInfo;
/**
 * 旋转方向：00 无法判定，01   顺时针，02   逆时针
 */
public enum RotateEnum {

    /**
     * 旋转方向：00 无法判定
     */
    UNKNOWN(0),

    /**
     * 旋转方向：00 无法判定，01   顺时针，02   逆时针
     */
    CLOCKWISE(1),

    /**
     * 旋转方向：00 无法判定，01   顺时针，02   逆时针
     */
    ANTI_CLOCK(2);

    RotateEnum(int i) {

    }
}
