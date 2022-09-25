package com.tunnel.business.domain.event;


public class SdDeviceNowState {

    private String eqId;

    private Long eqType;

    private String eqStatus;

    private String eqDirection;

    private String eqName;

    private String eqTunnelId;

    private String pile;

    private String state;

    private String fs;

    private String fx;

    private String co;

    private String vi;

    private String brightness;

    private String frequency;

    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    public Long getEqType() {
        return eqType;
    }

    public void setEqType(Long eqType) {
        this.eqType = eqType;
    }

    public String getEqStatus() {
        return eqStatus;
    }

    public void setEqStatus(String eqStatus) {
        this.eqStatus = eqStatus;
    }

    public String getEqDirection() {
        return eqDirection;
    }

    public void setEqDirection(String eqDirection) {
        this.eqDirection = eqDirection;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    public String getEqTunnelId() {
        return eqTunnelId;
    }

    public void setEqTunnelId(String eqTunnelId) {
        this.eqTunnelId = eqTunnelId;
    }

    public String getPile() {
        return pile;
    }

    public void setPile(String pile) {
        this.pile = pile;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "SdDeviceNowState{" +
                "eqId='" + eqId + '\'' +
                ", eqType=" + eqType +
                ", eqStatus='" + eqStatus + '\'' +
                ", eqDirection='" + eqDirection + '\'' +
                ", eqName='" + eqName + '\'' +
                ", eqTunnelId='" + eqTunnelId + '\'' +
                ", pile='" + pile + '\'' +
                ", state='" + state + '\'' +
                ", fs='" + fs + '\'' +
                ", fx='" + fx + '\'' +
                ", co='" + co + '\'' +
                ", vi='" + vi + '\'' +
                ", brightness='" + brightness + '\'' +
                ", frequency='" + frequency + '\'' +
                '}';
    }
}
