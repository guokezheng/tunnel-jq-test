package com.tunnel.business.domain.platformApi;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 推送历史记录
 *
 * @author zhai
 * @date 2022/11/4
 */
public class SdPlatformApi extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 推送接口类型 tunnel device
     */
    private String dataType;

    /**
     * 推送数据
     */
    private String pushData;

    /**
     * 推送状态
     */
    private String pushStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getPushData() {
        return pushData;
    }

    public void setPushData(String pushData) {
        this.pushData = pushData;
    }

    public String getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus;
    }
}
