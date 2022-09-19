package com.tunnel.business.domain.trafficOperationControl.eventManage;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 交通事件-发布对象对象 sd_traffic_incident_publish_object
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public class SdTrafficIncidentPublishObject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long objectId;

    /** 事件id,sd_traffic_incident主键 */
    @Excel(name = "事件id,sd_traffic_incident主键")
    private Long incidentId;

    /** 发布对象 */
    @Excel(name = "发布对象")
    private String publishObject;

    public void setObjectId(Long objectId)
    {
        this.objectId = objectId;
    }

    public Long getObjectId()
    {
        return objectId;
    }
    public void setIncidentId(Long incidentId)
    {
        this.incidentId = incidentId;
    }

    public Long getIncidentId()
    {
        return incidentId;
    }
    public void setPublishObject(String publishObject)
    {
        this.publishObject = publishObject;
    }

    public String getPublishObject()
    {
        return publishObject;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("objectId", getObjectId())
            .append("incidentId", getIncidentId())
            .append("publishObject", getPublishObject())
            .toString();
    }
}
