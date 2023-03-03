package com.tunnel.deal.plc.omron.exception;

/**
 * 错误描述
 */
public class PlcErrorConst {
    // Omron PLC
    public String OmronAddressMustBeZeroToFifteen(){ return "输入的位地址只能在0-15之间"; }
    public String OmronReceiveDataError (){ return "数据接收异常"; }
    public String OmronStatus0 (){ return "通讯正常"; }
    public String OmronStatus1 (){ return "消息头不是FINS"; }
    public String OmronStatus2 (){ return "数据长度太长"; }
    public String OmronStatus3 (){ return "该命令不支持"; }
    public String OmronStatus20 (){ return "超过连接上限"; }
    public String OmronStatus21 (){ return "指定的节点已经处于连接中"; }
    public String OmronStatus22 (){ return "尝试去连接一个受保护的网络节点，该节点还未配置到PLC中"; }
    public String OmronStatus23 (){ return "当前客户端的网络节点超过正常范围"; }
    public String OmronStatus24 (){ return "当前客户端的网络节点已经被使用"; }
    public String OmronStatus25 (){ return "所有的网络节点已经被使用"; }

    public String UnknownError() {
        return "未知错误";
    }
}
