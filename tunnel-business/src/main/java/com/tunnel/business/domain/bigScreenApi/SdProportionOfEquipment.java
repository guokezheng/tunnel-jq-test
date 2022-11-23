package com.tunnel.business.domain.bigScreenApi;

import com.ruoyi.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * @author zhai
 * @date 2022/11/7
 */
public class SdProportionOfEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备数量
     */
    private int number;

    /**
     * 设备状态
     */
    private String eqStatus;

    /**
     * 百分比
     */
    private String percentage;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getEqStatus() {
        return eqStatus;
    }

    public void setEqStatus(String eqStatus) {
        this.eqStatus = eqStatus;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
