package com.tunnel.deal.tcp.plc.omron.fins;

/**
 * describe: Fins指令常量类
 *
 * @author zs
 * @date 2023/8/24
 */
public class FinsCmdValues {

    /**
     * 失败的响应指令长度
     */
    public static final Integer ERROR_REPLY_LENGTH = 32;

    /**
     * 握手指令长度
     */
    public static final Integer HANDSHAKE_CMD_LENGTH = 48;
    /**
     * 正常指令最小长度
     */
    public static final Integer COMMAND_MIN_LENGTH = 60;
    /**
     * 正常响应码
     */
    public static final String NORMAL_REPLY_CODE = "0000";


    /**
     * D区：82（DM存储区代码）；
     */
    public static final String D_AREA_CODE = "82";

    /**
     * W区：B1（W字代码）
     */
    public static final String W_WORD_AREA_CODE = "B1";

    /**
     * 31（W位代码）
     */
    public static final String W_BIT_AREA_CODE = "31";

}
