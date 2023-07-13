package com.tunnel.deal.mqtt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * describe: MQTT配置类
 *
 * @author zs
 * @date 2023/5/25
 */
//@Component
//@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    private String url;
//    private String clientId;
    private String clientInId;
    private String clientOutId;
    private String topics;
    private String username;
    private String password;
    private String timeout;
    private String keepalive;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public String getClientId() {
//        return clientId;
//    }
//
//    public void setClientId(String clientId) {
//        this.clientId = clientId;
//    }

    public String getClientInId() {
        return clientInId;
    }

    public void setClientInId(String clientInId) {
        this.clientInId = clientInId;
    }

    public String getClientOutId() {
        return clientOutId;
    }

    public void setClientOutId(String clientOutId) {
        this.clientOutId = clientOutId;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(String keepalive) {
        this.keepalive = keepalive;
    }
}
