package com.tunnel.platform.domain.trafficOperationControl.eventManage;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 交通事件-处理流程对象 sd_traffic_incident_process
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public class SdTrafficIncidentProcess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long processId;

    /** 事件id,sd_traffic_incident字段id */
    @Excel(name = "事件id,sd_traffic_incident字段id")
    private Long incidentId;

    /** 流程时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "流程时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date processTime;

    /** 流程描述 */
    @Excel(name = "流程描述")
    private String processDesc;

    /** 处理人 */
    @Excel(name = "处理人")
    private String handler;

    /**
     * 图片列表
     */
    private List<SdTrafficImage> imgList;

    public void setProcessId(Long processId)
    {
        this.processId = processId;
    }

    public Long getProcessId()
    {
        return processId;
    }
    public void setIncidentId(Long incidentId)
    {
        this.incidentId = incidentId;
    }

    public Long getIncidentId()
    {
        return incidentId;
    }
    public void setProcessTime(Date processTime)
    {
        this.processTime = processTime;
    }

    public Date getProcessTime()
    {
        return processTime;
    }
    public void setProcessDesc(String processDesc)
    {
        this.processDesc = processDesc;
    }

    public String getProcessDesc()
    {
        return processDesc;
    }
    public void setHandler(String handler)
    {
        this.handler = handler;
    }

    public String getHandler()
    {
        return handler;
    }

    public List<SdTrafficImage> getImgList() {
        return imgList;
    }

    public void setImgList(List<SdTrafficImage> imgList) {
        this.imgList = imgList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("processId", getProcessId())
            .append("incidentId", getIncidentId())
            .append("processTime", getProcessTime())
            .append("processDesc", getProcessDesc())
            .append("handler", getHandler())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
