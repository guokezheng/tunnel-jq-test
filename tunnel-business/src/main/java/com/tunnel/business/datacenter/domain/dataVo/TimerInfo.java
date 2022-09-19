package com.tunnel.business.datacenter.domain.dataVo;

import java.util.List;

public class TimerInfo {

    private List<String> plcIdList;

    private String state;


    public List<String> getPlcIdList() {
        return plcIdList;
    }
    public void setPlcIdList(List<String> plcIdList) {
        this.plcIdList = plcIdList;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
