package com.tunnel.platform.datacenter.domain.dataVo;


import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeEnum;

public class DataInfo  {
    private static final long serialVersionUID = 1L;

    private String deviceId; //设备ID

    private Long deviceType; //设备类型

    private String deviceName; //设备名称

    private String stakeMark; //设备桩号


    private char data[] = new char[5];
    private String name[] = new String[5];
    private int x[] = new int[5];
    private int y[] = new int[5];


    private String distance;
    
    private Integer jlmState; //1:上限位2:下限位0:故障
    
    public Integer getJlmState() {
		return jlmState;
	}

	public void setJlmState(Integer jlmState) {
		this.jlmState = jlmState;
	}
	private Long concentration;
    
    private Long visibility;
    
    private Long windspeed;

    private String sbState;

    private String sb1State;

    private String sb2State;

    private String yewei;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
        if (deviceType == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode()) {
            this.name[0] = "正面绿";
            this.name[1] = "正面红";
            this.name[2] = "反面绿";
            this.name[3] = "反面红";
        } else if (deviceType == DevicesTypeEnum.TU_SHU_CHE_ZHI.getCode()) {
            this.name[0] = "正面绿";
            this.name[1] = "正面红";
            this.name[2] = "反面绿";
            this.name[3] = "反面红";
            this.name[4] = "正面左转";
        } else if (deviceType == DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode()) {
            this.name[0] = "红灯";
            this.name[1] = "黄灯";
            this.name[2] = "绿灯";
        } else if (deviceType == DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode()) {
            this.name[0] = "红灯";
            this.name[1] = "黄灯";
            this.name[2] = "绿灯";
            this.name[3] = "左转";
        } else if (deviceType == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode()) {
            this.name[0] = "开";
            this.name[1] = "远程";
        } else if (deviceType == DevicesTypeEnum.YIN_DAO_ZHAO_MING.getCode()) {
            this.name[0] = "开";
            this.name[1] = "远程";
        } else if (deviceType == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()) {
            this.name[0] = "开";
            this.name[1] = "远程";
        } else if (deviceType == DevicesTypeEnum.FENG_JI_1.getCode()|| deviceType == DevicesTypeEnum.FENG_JI_2.getCode()) {
            this.name[0] = "自动";
            this.name[1] = "正转";
            this.name[2] = "故障";
            this.name[3] = "反转";
        }
    }


    public String getStakeMark() {
        return stakeMark;
    }

    public void setStakeMark(String stakeMark) {
        this.stakeMark = stakeMark;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public char[] getData() {
        return data;
    }

    public void setData(char[] data) {
        this.data = data;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public int[] getX() {
        return x;
    }

    public void setX(int a, int b) {
        this.x[a] = b;
    }

    public int[] getY() {
        return y;
    }

    public void setY(int a, int b) {
        this.y[a] = b;
    }

    public void upData(char a[][]) {
        if (deviceType == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode()) {
            for (int i = 0; i < 4; i++) {
                this.data[i] = a[this.x[i]][this.y[i]];
            }
        } else if (deviceType == DevicesTypeEnum.TU_SHU_CHE_ZHI.getCode()) {
            for (int i = 0; i < 5; i++) {
                this.data[i] = a[this.x[i]][this.y[i]];
            }
        } else if (deviceType == DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode()) {
            for (int i = 0; i < 3; i++) {
                this.data[i] = a[this.x[i]][this.y[i]];
            }
        } else if (deviceType == DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode()) {
            for (int i = 0; i < 4; i++) {
                this.data[i] = a[this.x[i]][this.y[i]];
            }
        } else if (deviceType == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode() || deviceType == DevicesTypeEnum.YIN_DAO_ZHAO_MING.getCode()||deviceType == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()) {
            for (int i = 0; i < 2; i++) {
                this.data[i] = a[this.x[i]][this.y[i]];
            }
        }  else if (deviceType == DevicesTypeEnum.FENG_JI_1.getCode()||deviceType == DevicesTypeEnum.FENG_JI_2.getCode()) {
            for (int i = 0; i < 4; i++) {
                this.data[i] = a[this.x[i]][this.y[i]];
            }
        }


    }

    public void upDistance(String a) {
        this.distance = a;

    }
    public void upConcentration(Long a) {
        this.concentration = a;

    }
    public void upVisibility(Long a) {
        this.visibility = a;

    }
    public void upWindspeed(Long a) {
        this.windspeed = a;

    }


    public String getSbState() {
        return sbState;
    }

    public void setSbState(String sbState) {
        this.sbState = sbState;
    }

    public String getYewei() {
        return yewei;
    }

    public void setYewei(String yewei) {
        this.yewei = yewei;
    }

    public String getSb1State() {
        return sb1State;
    }

    public void setSb1State(String sb1State) {
        this.sb1State = sb1State;
    }

    public String getSb2State() {
        return sb2State;
    }

    public void setSb2State(String sb2State) {
        this.sb2State = sb2State;
    }
}