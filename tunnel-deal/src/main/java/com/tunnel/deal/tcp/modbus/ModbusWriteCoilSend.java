package com.tunnel.deal.tcp.modbus;

/**
 * describe: modbus写入线圈指令类
 *
 * @author zs
 * @date 2023/6/15
 */
public class ModbusWriteCoilSend {
    /**
     * 此次通信事务处理标识符，一般每次通信之后将被要求加1以区别不同的通信数据报文
     * 2个字节
     */
    private String serial;

    /**
     * 表示协议标识符，00 00为modbus的TCP/IP协议
     * 2个字节
     */
    private final String protocolSign = "00 00";

    /**
     * 数据长度，用来指示接下来数据的长度，单位字
     * 2个字节,
     * 默认的数据长度为8个字节
     */
    private String remainLength = "00 08";

    /**
     * 设备地址，用以标识连接在串行线或者网络上的远程服务端的地址
     * 1个字节
     */
    private String deviceAddress;

    /**
     * 功能码,1个字节
     */
    private String functionCode;

    /**
     * 写入线圈起始地址
     * 2个字节
     */
    private String writeAddress;

    /**
     * 写入线圈数量
     * 2个字节
     */
    private String writeLength;

    /**
     * 写入数据字节数
     * 1个字节
     */
    private String writeDataByte;

    /**
     * 写入数据
     * 不固定字节，根据写入数量确定
     */
    private String writeData;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getProtocolSign() {
        return protocolSign;
    }

    public String getRemainLength() {
        return remainLength;
    }

    public void setRemainLength(String remainLength) {
        this.remainLength = remainLength;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getWriteAddress() {
        return writeAddress;
    }

    public void setWriteAddress(String swriteAddress) {
        this.writeAddress = swriteAddress;
    }

    public String getWriteLength() {
        return writeLength;
    }

    public void setWriteLength(String writeLength) {
        this.writeLength = writeLength;
    }

    public String getWriteDataByte() {
        return writeDataByte;
    }

    public void setWriteDataByte(String writeDataByte) {
        this.writeDataByte = writeDataByte;
    }

    public String getWriteData() {
        return writeData;
    }

    public void setWriteData(String writeData) {
        this.writeData = writeData;
    }

    /**
     * 获取完整的命令
     * @return
     */
    public String getWriteSend(){
        StringBuffer s = new StringBuffer();
        s.append(serial).append(protocolSign).append(remainLength)
                .append(deviceAddress).append(functionCode)
                .append(writeAddress).append(writeLength).append(writeDataByte).append(writeData);
        String result = s.toString().replace(" ","");
        return result;
    }

    public int computeRemainLength(){
        int length =  deviceAddress.length() + functionCode.length() + writeAddress.length()
                + writeLength.length() + writeDataByte.length()+writeData.length();
        return length / 2;
    }

}
