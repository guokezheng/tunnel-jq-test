package com.tunnel.deal.plc.omron.finsTCP;


import com.tunnel.deal.plc.omron.util.ByteUtil;

public class OmronMessageHeader {

    /**
     * 信息控制字段，默认0x80
     */
    public byte ICF = (byte) 0x80;

    /**
     * 系统使用的内部信息
     */
    public byte RSV = 0x00;

    /**
     * 网络层信息，默认0x02，如果有八层消息，就设置为0x07
     */
    public byte GCT = 0x02;

    /**
     * PLC的网络号地址，默认0x00
     */
    public byte DNA = 0x00;

    /**
     * PLC的单元号地址，通常都为0
     */
    public byte DA2 = 0x00;

    /**
     * 上位机的网络号地址
     */
    public byte SNA = 0x00;
    /**
     * 上位机的单元号地址
     */
    public byte SA2 = 0x00;

    /**
     * 设备的标识号
     */
    public byte SID = 0x00;

    private byte[] message;

    /**
     * 不同存储区值不一样，D区：82（DM存储区代码）；W区：B1（W字代码），31（W位代码）
     *
     * MRC(主请求码)  /  SRC（次请求码）， 主/次请求码组合：0101是读操作；0102是写操作代码；2301是强制操作代码；
     * @param SA1   PLC  IP地址最后一位
     * @param DA1  PC   IP地址最后一位
     * @param bodyLength
     * @return
     */
    public void buildRequestHeader(byte SA1, byte DA1, int bodyLength) {
        byte[] length = ByteUtil.getBytesOfReverse(bodyLength + 18);
        message = new byte[] {
                0x46, 0x49, 0x4E, 0x53, // FINS
                0x00, 0x00, 0x00, 0x0C, // 后面的命令长度
                0x00, 0x00, 0x00, 0x02,
                0x00, 0x00, 0x00, 0x00,
                ICF, RSV, GCT, DNA, DA1,
                DA2, SNA, SA1, SA2, SID
        };
        System.arraycopy(length, 0, message, 4, 4);
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}
