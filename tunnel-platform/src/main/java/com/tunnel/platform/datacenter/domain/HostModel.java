package com.tunnel.platform.datacenter.domain;


/**
 * @author xiepufeng
 * @date 2019/9/17 13:36
 */
public class HostModel{

    private static final long serialVersionUID = 1L;

    private Integer id;         // 主键

    private Integer tunnelId;   // 所属隧道

    private String  name;       // 主机名称

    private String  code;       // 主机编号

    private String  ip;         // 主机ip地址

    private String  subnetMask; // 子网掩码

    private String  gateway;    // 网关

    private String  cmdCZ;      // 车指报文

    private String  cmdXHD;     // 信号灯报文

    private String  cmdFJ;      // 风机报文

    private String  cmdDN;      // 洞内亮度报文

    private String  cmdDW;      // 洞外亮度报文

    private String  cmdZM;      // 照明报文
    
    private String  cmdFX;      // 风向报文
    
    private String  cmdCO;      // 一氧化碳报文 
    
    private String  cmdVI;      // 能见度报文 
    
    private String  cmdFS;      // 风速报文 
    
    private String  cmdJLM;     // 卷帘门报文 
    
    private String  cmd;        //

    private String  cmdSB;        //水泵

    private String  cmdSBZT;        //状态

    private String  cmdYW;        //液位

    private String  remark;     // 备注

    private Integer poll;       // 是否可以轮询

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTunnelId() {
        return tunnelId;
    }

    public void setTunnelId(Integer tunnelId) {
        this.tunnelId = tunnelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getCmdCZ() {
        return cmdCZ;
    }

    public void setCmdCZ(String cmdCZ) {
        this.cmdCZ = cmdCZ;
    }

    public String getCmdXHD() {
        return cmdXHD;
    }

    public void setCmdXHD(String cmdXHD) {
        this.cmdXHD = cmdXHD;
    }

    public String getCmdFJ() {
        return cmdFJ;
    }

    public void setCmdFJ(String cmdFJ) {
        this.cmdFJ = cmdFJ;
    }

    public String getCmdDN() {
        return cmdDN;
    }

    public void setCmdDN(String cmdDN) {
        this.cmdDN = cmdDN;
    }

    public String getCmdDW() {
        return cmdDW;
    }

    public void setCmdDW(String cmdDW) {
        this.cmdDW = cmdDW;
    }

    public String getCmdZM() {
        return cmdZM;
    }

    public void setCmdZM(String cmdZM) {
        this.cmdZM = cmdZM;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPoll() {
        return poll;
    }

    public void setPoll(Integer poll) {
        this.poll = poll;
    }

	public String getCmdFX() {
		return cmdFX;
	}

	public void setCmdFX(String cmdFX) {
		this.cmdFX = cmdFX;
	}

	public String getCmdCO() {
		return cmdCO;
	}

	public void setCmdCO(String cmdCO) {
		this.cmdCO = cmdCO;
	}

	public String getCmdVI() {
		return cmdVI;
	}

	public void setCmdVI(String cmdVI) {
		this.cmdVI = cmdVI;
	}

	public String getCmdFS() {
		return cmdFS;
	}

	public void setCmdFS(String cmdFS) {
		this.cmdFS = cmdFS;
	}

	public String getCmdJLM() {
		return cmdJLM;
	}

	public void setCmdJLM(String cmdJLM) {
		this.cmdJLM = cmdJLM;
	}

    public String getCmdSB() {
        return cmdSB;
    }

    public void setCmdSB(String cmdSB) {
        this.cmdSB = cmdSB;
    }

    public String getCmdSBZT() {
        return cmdSBZT;
    }

    public void setCmdSBZT(String cmdSBZT) {
        this.cmdSBZT = cmdSBZT;
    }

    public String getCmdYW() {
        return cmdYW;
    }

    public void setCmdYW(String cmdYW) {
        this.cmdYW = cmdYW;
    }
}
