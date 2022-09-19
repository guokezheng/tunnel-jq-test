package com.tunnel.business.domain.roadState;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 路面监测信息对象 sd_road_detector_data
 *
 * @author lihaodong
 * @date 2021-11-17
 */
public class SdRoadDetectorData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Integer id;

    /** 设备编号 */
    //@Excel(name = "设备编号", sort = 1)
    private String devno;

    /** 路面温度 -40~80 */
    @Excel(name = "路面温度(℃)", sort = 5)
    private String pavementtemp;

    /** 冰点温度 -40~80 */
    //@Excel(name = "冰点温度(℃)", sort = 1)
    private String icepointtemp;

    /** 路面状况 */
    @Excel(name = "路面状况", sort = 10)
    private String pavementstatus;

    /** 摩擦系数 */
    @Excel(name = "湿滑系数", sort = 9)
    private String frictionalcoe;

    /** 水膜厚度 */
    //@Excel(name = "水膜厚度(MM)", sort = 1)
    private String waterfilmheigh;

    /** 水膜厚度 */
    @Excel(name = "积水深度(MM)", sort = 6)
    private String waterthickness;

    /** 水膜厚度 */
    @Excel(name = "雪厚度(MM)", sort = 8)
    private String snowthickness;

    /** 水膜厚度 */
    @Excel(name = "冰厚度(MM)", sort = 7)
    private String icethickness;

    /** 含盐量(NaCl) */
    //@Excel(name = "含盐量(NaCl)")
    private String saltnessnacl;

    /** 含盐量(MgCl2) */
    //@Excel(name = "含盐量(MgCl2)")
    private String saltnessmgcl2;

    /** 含盐量(CaCl2) */
    //@Excel(name = "含盐量(CaCl2)")
    private String saltnesscacl2;

    /** 含冰比例 */
    //@Excel(name = "含冰比例(%)")
    private String icepercent;

    /** 0：无结冰可能
1：有结冰可能
2：无法判断是否有结冰可能 */
    //@Excel(name = "是否有结冰可能")
    private Integer icepossibiblity;

    /** 采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "采集时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date timeCollect;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date timeUpdate;

    public SdRoadDetectorData() {
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setDevno(String devno)
    {
        this.devno = devno;
    }

    public String getDevno()
    {
        return devno;
    }
    public void setPavementtemp(String pavementtemp)
    {
        this.pavementtemp = pavementtemp;
    }

    public String getPavementtemp()
    {
        return pavementtemp;
    }
    public void setIcepointtemp(String icepointtemp)
    {
        this.icepointtemp = icepointtemp;
    }

    public String getIcepointtemp()
    {
        return icepointtemp;
    }
    public void setPavementstatus(String pavementstatus)
    {
        this.pavementstatus = pavementstatus;
    }

    public String getPavementstatus()
    {
        return pavementstatus;
    }
    public void setFrictionalcoe(String frictionalcoe)
    {
        this.frictionalcoe = frictionalcoe;
    }

    public String getFrictionalcoe()
    {
        return frictionalcoe;
    }
    public void setWaterfilmheigh(String waterfilmheigh)
    {
        this.waterfilmheigh = waterfilmheigh;
    }

    public String getWaterfilmheigh()
    {
        return waterfilmheigh;
    }

    public String getWaterthickness() {
        return waterthickness;
    }

    public void setWaterthickness(String waterthickness) {
        this.waterthickness = waterthickness;
    }

    public String getSnowthickness() {
        return snowthickness;
    }

    public void setSnowthickness(String snowthickness) {
        this.snowthickness = snowthickness;
    }

    public String getIcethickness() {
        return icethickness;
    }

    public void setIcethickness(String icethickness) {
        this.icethickness = icethickness;
    }

    public void setSaltnessnacl(String saltnessnacl)
    {
        this.saltnessnacl = saltnessnacl;
    }

    public String getSaltnessnacl()
    {
        return saltnessnacl;
    }
    public void setSaltnessmgcl2(String saltnessmgcl2)
    {
        this.saltnessmgcl2 = saltnessmgcl2;
    }

    public String getSaltnessmgcl2()
    {
        return saltnessmgcl2;
    }
    public void setSaltnesscacl2(String saltnesscacl2)
    {
        this.saltnesscacl2 = saltnesscacl2;
    }

    public String getSaltnesscacl2()
    {
        return saltnesscacl2;
    }
    public void setIcepercent(String icepercent)
    {
        this.icepercent = icepercent;
    }

    public String getIcepercent()
    {
        return icepercent;
    }
    public void setIcepossibiblity(Integer icepossibiblity)
    {
        this.icepossibiblity = icepossibiblity;
    }

    public Integer getIcepossibiblity()
    {
        return icepossibiblity;
    }
    public void setTimeCollect(Date timeCollect)
    {
        this.timeCollect = timeCollect;
    }

    public Date getTimeCollect()
    {
        return timeCollect;
    }
    public void setTimeUpdate(Date timeUpdate)
    {
        this.timeUpdate = timeUpdate;
    }

    public Date getTimeUpdate()
    {
        return timeUpdate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("devno", getDevno())
            .append("pavementtemp", getPavementtemp())
            .append("icepointtemp", getIcepointtemp())
            .append("pavementstatus", getPavementstatus())
            .append("frictionalcoe", getFrictionalcoe())
            .append("waterfilmheigh", getWaterfilmheigh())
            .append("waterthickness", getWaterthickness())
            .append("snowthickness", getSnowthickness())
            .append("icethickness", getIcethickness())
            .append("saltnessnacl", getSaltnessnacl())
            .append("saltnessmgcl2", getSaltnessmgcl2())
            .append("saltnesscacl2", getSaltnesscacl2())
            .append("icepercent", getIcepercent())
            .append("icepossibiblity", getIcepossibiblity())
            .append("timeCollect", getTimeCollect())
            .append("timeUpdate", getTimeUpdate())
            .toString();
    }
}
