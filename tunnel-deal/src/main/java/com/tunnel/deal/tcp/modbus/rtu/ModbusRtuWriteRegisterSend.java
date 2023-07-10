package com.tunnel.deal.tcp.modbus.rtu;

/**
 * describe: modbus Rtu写入寄存器指令类
 *
 * @author zs
 * @date 2023/7/5
 */
public class ModbusRtuWriteRegisterSend {

    /**
     * 设备地址，默认0x01
     * 1个字节
     */
    private String deviceAddress = "01";

    /**
     * 功能码,1个字节
     */
    private String functionCode = "06";

    /**
     * 写入寄存器地址
     * 2个字节
     */
    private String writeAddress="";

    /**
     * 写入数据
     * 2个字节
     */
    private String writeData="";


    private String crc="";

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

    public String getWriteData() {
        return writeData;
    }

    public void setWriteData(String writeData) {
        this.writeData = writeData;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    /**
     * 获取完整的命令
     * @return
     */
    public String getWriteSend(){
        StringBuffer s = new StringBuffer();
        s.append(deviceAddress).append(functionCode)
                .append(writeAddress).append(writeData).append(crc);
        String result = s.toString().replace(" ","");
        return result;
    }
}
