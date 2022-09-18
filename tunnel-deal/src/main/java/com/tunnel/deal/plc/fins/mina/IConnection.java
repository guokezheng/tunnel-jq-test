package com.tunnel.deal.plc.fins.mina;

public interface IConnection {

    /**
     * @Description: 初始化连接。
     */
    public void init();

    /**
     * @return
     * @Description: 判断连接是否成功。
     */
    public boolean isConnection();

    /**
     * @Description: 断开连接。
     */
    public void disconnect();

}
