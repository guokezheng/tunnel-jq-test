package com.tunnel.deal.tcp.plc.omron.fins;

/**
 * describe: Fins 读取发送指令
 *
 * @author zs
 * @date 2023/8/17
 */
public class FinsReadSendCmd extends FinsGeneralCmd {


    /**
     * 主请求码，1字节
     */
    private String mrc = "01";

    /**
     * 次请求码，1字节，主/次请求码组合：0101是读操作；0102是写操作代码；2301是强制操作代码；
     */
    private String src = "01";

    /**
     * 不同存储区值不一样，D区：82（DM存储区代码）；W区：B1（W字代码），31（W位代码）
     */
    private String area;

    /**
     * 起始地址（2byte）
     */
    private String address;

    /**
     * 位地址（1byte）
     */
    private String bitAddress = "00";

    /**
     * 读取或写入长度，2字节
     */
    private String readLength;


    public String getMrc() {
        return mrc;
    }

    public void setMrc(String mrc) {
        this.mrc = mrc;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBitAddress() {
        return bitAddress;
    }

    public void setBitAddress(String bitAddress) {
        this.bitAddress = bitAddress;
    }

    public String getReadLength() {
        return readLength;
    }

    public void setReadLength(String readLength) {
        this.readLength = readLength;
    }

    /**
     * 获取完整的命令
     * @return
     */
    public String getFullCmd(){
        StringBuffer s = new StringBuffer();
        s.append(command).append(fixedErrorCode).append(icf)
                .append(rsv).append(gct).append(dna).append(da1).append(da2).append(sna)
                .append(sa1).append(sa2).append(sid).append(mrc).append(src)
                .append(area).append(address).append(bitAddress).append(readLength);
        //4字节，8位16进制数
        length = String.format("%08x",s.length()/2);

        StringBuffer cmd = new StringBuffer();
        cmd.append(header).append(length).append(s);
        String result = cmd.toString();
        return result;
    }

    /**
     * 欧姆龙Udp读
     * @return
     */
    public String getOmlUdpCmd(){
        StringBuffer s = new StringBuffer();
        s.append(dna).append(da1).append(da2).append(sna)
                .append(sa1).append(sa2).append(sid).append(mrc).append(src)
                .append(area).append(address).append(bitAddress).append(readLength);

        StringBuffer cmd = new StringBuffer();
        cmd.append(omlUdpHeader).append(s);
        String result = cmd.toString();
        return result;
    }
}
