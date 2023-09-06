package com.tunnel.deal.tcp.plc.omron.fins;

/**
 * describe: Fins错误码枚举类
 *
 * @author zs
 * @date 2023/8/24
 */
public enum FinsErrorCodeEnum {

    //十六进制错误码	含义
    //00000000	正常
    //00000001	头不是‘FINS’ (ASCII code)。
    //00000002	数据太长。
    //00000003	不支持的命令。
    //00000020	所有的连接被占用。
    //00000021	制定的节点已经连接。
    //00000022	未被指定的IP地址试图访问一个被保护的节点。
    //00000023	客户端FINS节点地址超范围。
    //00000024	相同的FINS节点地址已经被使用。
    //00000025	所有可用的节点地址都已使用。

    NORMAL("00000000","正常"),
    HEADER_ERROR("00000001","头不是'FINS'"),
    DATA_TOO_LONG("00000002","数据太长"),
    UNSUPPORTED_COMMAND("00000003","不支持的命令"),
    ALL_CONNECT_OCCUPIED("00000020","所有的连接被占用"),
    CONNECT_DONE("00000021","制定的节点已经连接");

    private String code;
    private String name;

    FinsErrorCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
