package com.tunnel.platform.domain.dataInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备控制状态参数对象 inductionlamp_control_status_param
 *
 * @author ruoyi
 * @date 2022-08-30
 */
public class InductionlampControlStatusParam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 设备id */
    @Excel(name = "设备id")
    private String equipmentId;

    /** 模式名称 */
    @Excel(name = "模式名称")
    private String modeName;

    /** 模式编号 */
    @Excel(name = "模式编号")
    private Integer modeCode;

    /** 光照开始值 */
    @Excel(name = "光照开始值")
    private Integer illuminationStart;

    /** 光照开始值关系符号 */
    @Excel(name = "光照开始值关系符号")
    private Integer illuminationStartSymbol;

    /** 光照结束值 */
    @Excel(name = "光照结束值")
    private Integer illuminationEnd;

    /** 光照结束值关系符号 */
    @Excel(name = "光照结束值关系符号")
    private Integer illuminationEndSymbol;

    /** 湿度开始值 */
    @Excel(name = "湿度开始值")
    private Integer humidityStart;

    /** 湿度开始值关系符号 */
    @Excel(name = "湿度开始值关系符号")
    private Integer humidityStartSymbol;

    /** 湿度结束值 */
    @Excel(name = "湿度结束值")
    private Integer humidityEnd;

    /** 湿度结束值关系符号 */
    @Excel(name = "湿度结束值关系符号")
    private Integer humidityEndSymbol;

    /** 温度开始值 */
    @Excel(name = "温度开始值")
    private Integer temperatureStart;

    /** 温度开始值关系符号 */
    @Excel(name = "温度开始值关系符号")
    private Integer temperatureStartSymbol;

    /** 温度结束值 */
    @Excel(name = "温度结束值")
    private Integer temperatureEnd;

    /** 温度结束值关系符号 */
    @Excel(name = "温度结束值关系符号")
    private Integer temperatureEndSymbol;

    /** 指令编码 */
    @Excel(name = "指令编码")
    private String instructionsCode;

    /** 亮度参数 */
    @Excel(name = "亮度参数")
    private Integer brightnessParam;

    /** 次/min */
    @Excel(name = "次/min")
    private Integer timeSecond;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setEquipmentId(String equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentId()
    {
        return equipmentId;
    }
    public void setModeName(String modeName)
    {
        this.modeName = modeName;
    }

    public String getModeName()
    {
        return modeName;
    }
    public void setModeCode(Integer modeCode)
    {
        this.modeCode = modeCode;
    }

    public Integer getModeCode()
    {
        return modeCode;
    }
    public void setIlluminationStart(Integer illuminationStart)
    {
        this.illuminationStart = illuminationStart;
    }

    public Integer getIlluminationStart()
    {
        return illuminationStart;
    }
    public void setIlluminationStartSymbol(Integer illuminationStartSymbol)
    {
        this.illuminationStartSymbol = illuminationStartSymbol;
    }

    public Integer getIlluminationStartSymbol()
    {
        return illuminationStartSymbol;
    }
    public void setIlluminationEnd(Integer illuminationEnd)
    {
        this.illuminationEnd = illuminationEnd;
    }

    public Integer getIlluminationEnd()
    {
        return illuminationEnd;
    }
    public void setIlluminationEndSymbol(Integer illuminationEndSymbol)
    {
        this.illuminationEndSymbol = illuminationEndSymbol;
    }

    public Integer getIlluminationEndSymbol()
    {
        return illuminationEndSymbol;
    }
    public void setHumidityStart(Integer humidityStart)
    {
        this.humidityStart = humidityStart;
    }

    public Integer getHumidityStart()
    {
        return humidityStart;
    }
    public void setHumidityStartSymbol(Integer humidityStartSymbol)
    {
        this.humidityStartSymbol = humidityStartSymbol;
    }

    public Integer getHumidityStartSymbol()
    {
        return humidityStartSymbol;
    }
    public void setHumidityEnd(Integer humidityEnd)
    {
        this.humidityEnd = humidityEnd;
    }

    public Integer getHumidityEnd()
    {
        return humidityEnd;
    }
    public void setHumidityEndSymbol(Integer humidityEndSymbol)
    {
        this.humidityEndSymbol = humidityEndSymbol;
    }

    public Integer getHumidityEndSymbol()
    {
        return humidityEndSymbol;
    }
    public void setTemperatureStart(Integer temperatureStart)
    {
        this.temperatureStart = temperatureStart;
    }

    public Integer getTemperatureStart()
    {
        return temperatureStart;
    }
    public void setTemperatureStartSymbol(Integer temperatureStartSymbol)
    {
        this.temperatureStartSymbol = temperatureStartSymbol;
    }

    public Integer getTemperatureStartSymbol()
    {
        return temperatureStartSymbol;
    }
    public void setTemperatureEnd(Integer temperatureEnd)
    {
        this.temperatureEnd = temperatureEnd;
    }

    public Integer getTemperatureEnd()
    {
        return temperatureEnd;
    }
    public void setTemperatureEndSymbol(Integer temperatureEndSymbol)
    {
        this.temperatureEndSymbol = temperatureEndSymbol;
    }

    public Integer getTemperatureEndSymbol()
    {
        return temperatureEndSymbol;
    }
    public void setInstructionsCode(String instructionsCode)
    {
        this.instructionsCode = instructionsCode;
    }

    public String getInstructionsCode()
    {
        return instructionsCode;
    }
    public void setBrightnessParam(Integer brightnessParam)
    {
        this.brightnessParam = brightnessParam;
    }

    public Integer getBrightnessParam()
    {
        return brightnessParam;
    }
    public void setTimeSecond(Integer timeSecond)
    {
        this.timeSecond = timeSecond;
    }

    public Integer getTimeSecond()
    {
        return timeSecond;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("equipmentId", getEquipmentId())
            .append("modeName", getModeName())
            .append("modeCode", getModeCode())
            .append("illuminationStart", getIlluminationStart())
            .append("illuminationStartSymbol", getIlluminationStartSymbol())
            .append("illuminationEnd", getIlluminationEnd())
            .append("illuminationEndSymbol", getIlluminationEndSymbol())
            .append("humidityStart", getHumidityStart())
            .append("humidityStartSymbol", getHumidityStartSymbol())
            .append("humidityEnd", getHumidityEnd())
            .append("humidityEndSymbol", getHumidityEndSymbol())
            .append("temperatureStart", getTemperatureStart())
            .append("temperatureStartSymbol", getTemperatureStartSymbol())
            .append("temperatureEnd", getTemperatureEnd())
            .append("temperatureEndSymbol", getTemperatureEndSymbol())
            .append("instructionsCode", getInstructionsCode())
            .append("brightnessParam", getBrightnessParam())
            .append("timeSecond", getTimeSecond())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
