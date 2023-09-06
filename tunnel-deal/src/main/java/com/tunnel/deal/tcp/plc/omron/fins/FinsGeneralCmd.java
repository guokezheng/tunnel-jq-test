package com.tunnel.deal.tcp.plc.omron.fins;

/**
 * describe: fins发送指令格式定义类
 *
 * @author zs
 * @date 2023/8/17
 */
public class FinsGeneralCmd {

    /**
     * 固定值，4字节
     */
    protected String header = "46494E53";

    /**
     * 4字节，从command命令开始到结束的字节长度
     */
    protected String length;

    /**
     * 固定值，00000002
     */
    protected String command = "00000002";

    /**
     * 固定值 00000000
     */
    protected String fixedErrorCode = "00000000";

    /**
     * 固定值 80，其中bit0：0表示需要回复，1表示不需要回复；bit7：0表示命令，1表示响应
     */
    protected String icf = "80";

    /**
     * 固定值 00
     */
    protected String rsv = "00";

    /**
     * 固定值 02
     */
    protected String gct = "02";

    /**
     * 目标网络号，固定值 00
     */
    protected String dna = "00";

    /**
     * 目标节点号， IP地址最后一位而定 比如127.0.0.1，最后一位是01,1字节
     */
    protected String da1;

    /**
     * 目标单元号， 固定值 00
     */
    protected String da2 = "00";

    /**
     * 源网络号，固定值 00
     */
    protected String sna = "00";

    /**
     * 源节点号， IP地址最后一位而定 比如192.168.1.200，最后一位是C8,1字节
     */
    protected String sa1;

    /**
     * 源单元号，固定值 00
     */
    protected String sa2 = "00";

    /**
     * 固定值 00
     */
    protected String sid = "00";

//    /**
//     * 主请求码，1字节
//     */
//    protected String mrc;
//
//    /**
//     * 次请求码，1字节，主/次请求码组合：0101是读操作；0102是写操作代码；2301是强制操作代码；
//     */
//    protected String src;


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFixedErrorCode() {
        return fixedErrorCode;
    }

    public void setFixedErrorCode(String fixedErrorCode) {
        this.fixedErrorCode = fixedErrorCode;
    }

    public String getIcf() {
        return icf;
    }

    public void setIcf(String icf) {
        this.icf = icf;
    }

    public String getRsv() {
        return rsv;
    }

    public void setRsv(String rsv) {
        this.rsv = rsv;
    }

    public String getGct() {
        return gct;
    }

    public void setGct(String gct) {
        this.gct = gct;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }

    public String getDa1() {
        return da1;
    }

    public void setDa1(String da1) {
        this.da1 = da1;
    }

    public String getDa2() {
        return da2;
    }

    public void setDa2(String da2) {
        this.da2 = da2;
    }

    public String getSna() {
        return sna;
    }

    public void setSna(String sna) {
        this.sna = sna;
    }

    public String getSa1() {
        return sa1;
    }

    public void setSa1(String sa1) {
        this.sa1 = sa1;
    }

    public String getSa2() {
        return sa2;
    }

    public void setSa2(String sa2) {
        this.sa2 = sa2;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

//    public String getMrc() {
//        return mrc;
//    }
//
//    public void setMrc(String mrc) {
//        this.mrc = mrc;
//    }
//
//    public String getSrc() {
//        return src;
//    }
//
//    public void setSrc(String src) {
//        this.src = src;
//    }
}
