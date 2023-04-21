package com.tunnel.deal.plc.omron;

import org.springframework.util.StringUtils;

import java.util.concurrent.CountDownLatch;


public class OmronConnectProperties {
    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;


    /**
     * 客户端唯一标识key
     */
    private String connectKey;

    /**
     * 本地主机地址
     */
    private String localHost;


    /**
     * 本地主机端口
     */
    private Integer localPort;


    /**
     * 上位机的节点地址，如果你的电脑的Ip地址为192.168.0.8，那么这个值就是8
     */
    private byte SA1;

    /**
     * PLC的节点地址，这个值在配置了ip地址之后是默认赋值的，默认为Ip地址的最后一位
     */
    private byte DA1;

    public byte getSA1() {
        return SA1;
    }

    public void setSA1(byte SA1) {
        this.SA1 = SA1;
    }

    public byte getDA1() {
        return DA1;
    }

    public void setDA1(byte DA1) {
        this.DA1 = DA1;
    }

    /**
     * 同步锁
     */
    private CountDownLatch countDownLatch;

    /**
     * DA1请使用ip地址
     * @param ipAddress
     */
    public void setHost(String ipAddress) {
        if(StringUtils.hasText(ipAddress)) {
            this.host = ipAddress;
            DA1 = (byte) Integer.parseInt(ipAddress.substring(ipAddress.lastIndexOf( "." ) + 1));
        }
    }

    /**
     * SA1请使用ip地址
     * @param ipAddress
     */
    public void setLocalHost(String ipAddress) {
        if(StringUtils.hasText(ipAddress)) {
            SA1 = (byte) Integer.parseInt(ipAddress.substring(ipAddress.lastIndexOf( "." ) + 1));
        }
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getConnectKey() {
        return connectKey;
    }

    public void setConnectKey(String connectKey) {
        this.connectKey = connectKey;
    }

    public String getLocalHost() {
        return localHost;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
