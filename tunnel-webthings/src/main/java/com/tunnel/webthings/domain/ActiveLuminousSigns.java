package com.tunnel.webthings.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 主动发光标志
 * @author dear_
 * 主动发光标志类
 */
@ApiModel("主动发光标志实体")
public class ActiveLuminousSigns {

    @ApiModelProperty("任务类型")
    private String taskType;

    @ApiModelProperty("背光开关")
    private String backlitSwitch;

    @ApiModelProperty("背光亮度")
    private String backlightBrightness;

    @ApiModelProperty("可变车道")
    private String reversibleLanes;

    @ApiModelProperty("光衰补偿等级")
    private String droopCompensation;

    @ApiModelProperty("实测电流")
    private String measuredCurrent;

    @ApiModelProperty("默认电流")
    private String defaultCurrent;

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getBacklitSwitch() {
        return backlitSwitch;
    }

    public void setBacklitSwitch(String backlitSwitch) {
        this.backlitSwitch = backlitSwitch;
    }

    public String getBacklightBrightness() {
        return backlightBrightness;
    }

    public void setBacklightBrightness(String backlightBrightness) {
        this.backlightBrightness = backlightBrightness;
    }

    public String getReversibleLanes() {
        return reversibleLanes;
    }

    public void setReversibleLanes(String reversibleLanes) {
        this.reversibleLanes = reversibleLanes;
    }

    public String getDroopCompensation() {
        return droopCompensation;
    }

    public void setDroopCompensation(String droopCompensation) {
        this.droopCompensation = droopCompensation;
    }

    public String getMeasuredCurrent() {
        return measuredCurrent;
    }

    public void setMeasuredCurrent(String measuredCurrent) {
        this.measuredCurrent = measuredCurrent;
    }

    public String getDefaultCurrent() {
        return defaultCurrent;
    }

    public void setDefaultCurrent(String defaultCurrent) {
        this.defaultCurrent = defaultCurrent;
    }

    @Override
    public String toString() {
        return "ActiveLuminousSigns{" +
                "taskType='" + taskType + '\'' +
                ", backlitSwitch='" + backlitSwitch + '\'' +
                ", backlightBrightness='" + backlightBrightness + '\'' +
                ", reversibleLanes='" + reversibleLanes + '\'' +
                ", droopCompensation='" + droopCompensation + '\'' +
                ", measuredCurrent='" + measuredCurrent + '\'' +
                ", defaultCurrent='" + defaultCurrent + '\'' +
                '}';
    }
}
