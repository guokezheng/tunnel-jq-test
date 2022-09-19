package com.tunnel.business.domain.videoevents;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 容器表对象 sd_create_docker
 * 
 * @author ruoyi
 * @date 2021-04-24
 */
public class SdCreateDocker extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 事件ID */
    private Integer id;

    /** 显卡编号 */
    @Excel(name = "显卡编号")
    private Integer deviceID;
    /** 显卡编号 */
    @Excel(name = "服务器IP")
    private String dockerIp;

    /** 端口号 */
    @Excel(name = "端口号")
    private Integer dockerPort;

    /** 容器名称 */
    @Excel(name = "容器名称")
    private String dockerName;

    /** 容器所占用的cpu核个数 */
    @Excel(name = "容器所占用的cpu核个数")
    private Integer vcpus;

    /** 容器所占用的内存空间大小 */
    @Excel(name = "容器所占用的内存空间大小")
    private Integer mems;

    /** 显卡或者芯片的编号 */
    @Excel(name = "显卡或者芯片的编号")
    private Integer nGpuId;

    /** 是否检测人脸：1表示检测，0表示不检测 */
    @Excel(name = "是否检测人脸：1表示检测，0表示不检测")
    private Integer nFaceEnable;

    /** 是否检测人体：1表示检测，0表示不检测 */
    @Excel(name = "是否检测人体：1表示检测，0表示不检测")
    private Integer nBodyEnable;

    /** 是否检测机动车：1表示检测，0表示不检测 */
    @Excel(name = "是否检测机动车：1表示检测，0表示不检测")
    private Integer nMotorEnable;

    /** 是否检测非机动车：1表示检测，0表示不检测 */
    @Excel(name = "是否检测非机动车：1表示检测，0表示不检测")
    private Integer nNonMotorEnable;

    /** 是否检测事件：1表示检测，0表示不检测 */
    @Excel(name = "是否检测事件：1表示检测，0表示不检测")
    private Integer nEventEnable;

    /** 事件模式：监管行为分析设置为0，交通事件需设置为105,安全生产算子业务设置为103 */
    @Excel(name = "事件模式：监管行为分析设置为0，交通事件需设置为105,安全生产算子业务设置为103")
    private Integer nEventMode;

    /** 授权文件路径，保护授权码，需要/home目录下的子目录下 */
    @Excel(name = "授权文件路径，保护授权码，需要/home目录下的子目录下")
    private String authPath;

    private String serverName;
    private String serverPwd;
    /** 创建者 */
    private String create_by;

    /** 创建时间 */
    private Date create_time;

    /** 修改者 */
    private String update_by;

    /** 修改时间 */
    private Date update_time;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setDeviceID(Integer deviceID) 
    {
        this.deviceID = deviceID;
    }

    public Integer getDeviceID() 
    {
        return deviceID;
    }

    public String getDockerIp() {
        return dockerIp;
    }

    public void setDockerIp(String dockerIp) {
        this.dockerIp = dockerIp;
    }

    public void setDockerPort(Integer dockerPort)
    {
        this.dockerPort = dockerPort;
    }

    public Integer getDockerPort() 
    {
        return dockerPort;
    }
    public void setDockerName(String dockerName) 
    {
        this.dockerName = dockerName;
    }

    public String getDockerName() 
    {
        return dockerName;
    }
    public void setVcpus(Integer vcpus) 
    {
        this.vcpus = vcpus;
    }

    public Integer getVcpus() 
    {
        return vcpus;
    }
    public void setMems(Integer mems) 
    {
        this.mems = mems;
    }

    public Integer getMems() 
    {
        return mems;
    }
    public void setnGpuId(Integer nGpuId) 
    {
        this.nGpuId = nGpuId;
    }

    public Integer getnGpuId() 
    {
        return nGpuId;
    }
    public void setnFaceEnable(Integer nFaceEnable) 
    {
        this.nFaceEnable = nFaceEnable;
    }

    public Integer getnFaceEnable() 
    {
        return nFaceEnable;
    }
    public void setnBodyEnable(Integer nBodyEnable) 
    {
        this.nBodyEnable = nBodyEnable;
    }

    public Integer getnBodyEnable() 
    {
        return nBodyEnable;
    }
    public void setnMotorEnable(Integer nMotorEnable) 
    {
        this.nMotorEnable = nMotorEnable;
    }

    public Integer getnMotorEnable() 
    {
        return nMotorEnable;
    }
    public void setnNonMotorEnable(Integer nNonMotorEnable) 
    {
        this.nNonMotorEnable = nNonMotorEnable;
    }

    public Integer getnNonMotorEnable() 
    {
        return nNonMotorEnable;
    }
    public void setnEventEnable(Integer nEventEnable) 
    {
        this.nEventEnable = nEventEnable;
    }

    public Integer getnEventEnable() 
    {
        return nEventEnable;
    }
    public void setnEventMode(Integer nEventMode) 
    {
        this.nEventMode = nEventMode;
    }

    public Integer getnEventMode() 
    {
        return nEventMode;
    }
    public void setAuthPath(String authPath) 
    {
        this.authPath = authPath;
    }

    public String getAuthPath() 
    {
        return authPath;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerPwd() {
        return serverPwd;
    }

    public void setServerPwd(String serverPwd) {
        this.serverPwd = serverPwd;
    }

    public void setCreate_by(String create_by)
    {
        this.create_by = create_by;
    }

    public String getCreate_by() 
    {
        return create_by;
    }
    public void setCreate_time(Date create_time) 
    {
        this.create_time = create_time;
    }

    public Date getCreate_time() 
    {
        return create_time;
    }
    public void setUpdate_by(String update_by) 
    {
        this.update_by = update_by;
    }

    public String getUpdate_by() 
    {
        return update_by;
    }
    public void setUpdate_time(Date update_time) 
    {
        this.update_time = update_time;
    }

    public Date getUpdate_time() 
    {
        return update_time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceID", getDeviceID())
            .append("dockerPort", getDockerPort())
            .append("dockerName", getDockerName())
            .append("vcpus", getVcpus())
            .append("mems", getMems())
            .append("nGpuId", getnGpuId())
            .append("nFaceEnable", getnFaceEnable())
            .append("nBodyEnable", getnBodyEnable())
            .append("nMotorEnable", getnMotorEnable())
            .append("nNonMotorEnable", getnNonMotorEnable())
            .append("nEventEnable", getnEventEnable())
            .append("nEventMode", getnEventMode())
            .append("authPath", getAuthPath())
            .append("create_by", getCreate_by())
            .append("create_time", getCreate_time())
            .append("update_by", getUpdate_by())
            .append("update_time", getUpdate_time())
            .toString();
    }
}
