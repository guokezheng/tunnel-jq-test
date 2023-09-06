package com.tunnel.deal.tcp.plc.omron.fins;

/**
 * describe: Fins 握手发送命令
 *
 * @author zs
 * @date 2023/8/17
 */
public class FinsHandshakeSendCmd {

    /**
     * FINS的ASCII码值，即命令头
     */
    private String header = "46494E53";


    /**
     * 命令长度20（这个是按长度后面的命令长度来算，这里即：命令+错误码+客户机节点地址）
     */
    private String length = "0000000C";

    /**
     * 命令
     */
    private String command = "00000000";

    /**
     * 错误码
     */
    private String errorCode = "00000000";

    /**
     * 客户机节点地址,服务器的IP尾数16进制，4字节
     */
    private String clientNodeAddress;

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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getClientNodeAddress() {
        return clientNodeAddress;
    }

    public void setClientNodeAddress(String clientNodeAddress) {
        this.clientNodeAddress = clientNodeAddress;
    }


    /**
     * 获取完整的命令
     * @return
     */
    public String getFullCmd(){
        StringBuffer s = new StringBuffer();
        s.append(header).append(length).append(command).append(errorCode).append(clientNodeAddress);
        String result = s.toString();
        return result;
    }
}
