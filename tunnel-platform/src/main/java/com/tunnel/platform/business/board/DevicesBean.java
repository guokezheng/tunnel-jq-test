package com.tunnel.platform.business.board;

import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.utils.util.CommonUtil;

import java.util.Map;

/**
 * 设备Bean
 *
 * @author Alvin
 * @date 2018年7月13日
 */
public class DevicesBean {

    private static final long serialVersionUID = 1L;
    private String id = null;
    private String ip = null;
    private int port;
    private String vender_version = null;
    private String type = null;

    // 配置信息
    private String configInformation;

    public DevicesBean(String id, String configInformation) {
        this.id = id;
        this.configInformation = configInformation;
        init();
    }

    public DevicesBean() {
    }

    /**
     * 分解传入的配置信息
     */
    private void init() {
        if (StringUtils.isEmpty(configInformation))
            return;
        Map<String, String> attributes = CommonUtil.splitStringToMap(configInformation);
        ip = attributes.get("ip");
        port = Integer.parseInt(attributes.get("port"));
        vender_version = attributes.get("vender_version");
        type = attributes.get("type");
    }

    public int getPort() {
        return port;
    }

    public String getVender_version() {
        return vender_version;
    }

    public void setVender_version(String vender_version) {
        this.vender_version = vender_version;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}