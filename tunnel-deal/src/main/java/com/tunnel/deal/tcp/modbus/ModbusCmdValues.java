package com.tunnel.deal.tcp.modbus;

/**
 * describe: modbus协议格式常量类
 *
 * @author zs
 * @date 2021/2/6
 */
public class ModbusCmdValues {

    /**
     * 1个字节读取的数据长度
     */
    public static final int ONE_BYTE_DATA_LENGTH = 2;

    /**
     * 全部数据读取长度,11个数据，十六进制，【一个数据2个字节，一个寄存器地址1个字节】
     * 总共22个的寄存器地址，十六进制是16，22个字节
     */
    public static final String ALL_DATA_LENGTH = "0018";

    /**
     * 读取设备状态的读取地址长度，2个地址，4个字节
     */
    public static final String READ_STATE_DATA_LENGTH = "0002";


    /**
     * 读取设备状态的读取的数据长度，单位字节
     */
    public static final int READ_ALL_DATA_READ_BYTES = 22;

    /**
     * 读取设备状态的读取的数据长度，单位字节
     */
    public static final int READ_STATE_READ_BYTES = 4;

    /**
     * 默认的起始地址，十六进制，2个字节
     */
    public static final String START_ADDRESS = "0064";


    /**
     * 读取设备状态的起始地址，十六进制，2个字节
     */
    public static final String READ_STATE_START_ADDRESS = "0006";

    /**
     * 接收指令最小长度（接收指令字符串的长度）,7个字节，14个长度
     * 从序列号到设备地址
     */
    public static final int RECV_CMD_MIN_LENGTH = 14;

    /**
     * 接收指令正常情况下的最小长度，9个字节，18个长度
     * 从序列号到数据长度
     */
    public static final int RECV_CMD_NORMAL_MIN_LENGTH = 18;


    public static final String DEVICE_ADDRESS_DEFAULT = "01";
}
