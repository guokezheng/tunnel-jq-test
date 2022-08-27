package com.zc.connect.config;

import java.io.Serializable;

/**
 * @auther YangChao
 * @description netty配置属性类
 * @date 2021/11/18/018
 * @apiNote
 */
public class NettyServerConfigAttr implements Serializable {

    /** 是否开启 */
    public Boolean enabled;

    /** 端口号 */
    public int port;

    /** handler处理类 */
    public String handler_name;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHandler_name() {
        return handler_name;
    }

    public void setHandler_name(String handler_name) {
        this.handler_name = handler_name;
    }

    @Override
    public String toString() {
        return "config{" +
                "enabled=" + enabled +
                ", port='" + port + '\'' +
                ", handler_name='" + handler_name + '\'' +
                '}';
    }
}
