package com.tunnel.deal.tcp.plc.omron.fins;

/**
 * describe: Fins 握手回复命令
 *
 * @author zs
 * @date 2023/8/17
 */
public class FinsHandshakeReplyCmd {

    /**
     * FINS的ASCII码值，即命令头
     */
    private String header = "46494E53";


    /**
     * 命令长度24
     */
    private String length = "00000010";

    /**
     * 命令码
     */
    private String command = "00000001";

    /**
     * 错误码
     */
    private String errorCode = "00000000";

    /**
     * 客户节点地址，即电脑IP地址的末位（电脑的IP尾数16进制）
     */
    private String clientNodeAddress;

    /**
     * 服务器节点地址，即PLC IP地址的末位（电脑的IP尾数16进制）
     */
    private String serverNodeAddress;


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

    public String getServerNodeAddress() {
        return serverNodeAddress;
    }

    public void setServerNodeAddress(String serverNodeAddress) {
        this.serverNodeAddress = serverNodeAddress;
    }


    /**
     * 获取完整的命令
     * @return
     */
    public String getFullCmd(){
        StringBuffer s = new StringBuffer();
        s.append(header).append(length).append(command).append(errorCode).append(clientNodeAddress).append(serverNodeAddress);
        String result = s.toString();
        return result;
    }
}
