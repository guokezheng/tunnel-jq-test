package com.tunnel.deal.mca.modbus;

/**
 * describe: modbus功能码常量类
 *
 * @author zs
 * @date 2021/2/6
 */
public class ModbusFunctionCode {

    /**
     * 读线圈状态	READ COIL STATUS	位操作	单个或多个
     */
    public static final String CODE_ONE = "01";

    /**
     * 读离散输入状态	READ INPUT STATUS	位操作	单个或多个
     */
    public static final String CODE_TWO = "02";
    /**
     * 读保持寄存器	READ HOLDING REGISTER	字操作	单个或多个
     */
    public static final String CODE_THREE = "03";

    /**
     * 读输入寄存器	READ INPUT REGISTER	字操作	单个或多个
     */
    public static final String CODE_FOUR = "04";


    /**
     * 写线圈状态	WRITE SINGLE COIL	位操作	单个
     */
    public static final String CODE_FIVE = "05";

    /**
     * 写单个保持寄存器	WRITE SINGLE REGISTER	字操作	单个
     */
    public static final String CODE_SIX = "06";

    /**
     * 写多个线圈	WRITE MULTIPLE COIL	位操作	多个
     */
    public static final String CODE_FIFTEEN = "15";

    /**
     * 写多个保持寄存器	WRITE MULTIPLE REGISTER	字操作	多个
     */
    public static final String CODE_SIXTEEN = "16";

}
