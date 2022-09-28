package com.tunnel.business.datacenter.domain.dataVo;

import java.util.List;
import java.util.Map;

public class CmdInfo {
    private static final long serialVersionUID = 1L;

    private String hostId; //plc主机id

    private String plcIp; //plc主机ip

    private String hostControlType; //plc主机控制类型

    private List<Map<String, String>> cmdList;


    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getPlcIp() {
        return plcIp;
    }

    public void setPlcIp(String plcIp) {
        this.plcIp = plcIp;
    }

    public String getHostControlType() {
        return hostControlType;
    }

    public void setHostControlType(String hostControlType) {
        this.hostControlType = hostControlType;
    }

    public List<Map<String, String>> getCmdList() {
        return cmdList;
    }

    public void setCmdList(List<Map<String, String>> cmdList) {
        this.cmdList = cmdList;
    }
}
