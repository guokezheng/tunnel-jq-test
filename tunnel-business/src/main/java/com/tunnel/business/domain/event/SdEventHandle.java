package com.tunnel.business.domain.event;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 事件处置信息对象 sd_event_handle
 * 
 * @author ruoyi
 * @date 2022-12-15
 */
public class SdEventHandle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 事件id */
    @Excel(name = "事件id")
    private Long eventId;

    /** 流程id */
    @Excel(name = "流程id")
    private Long flowId;

    /** 流程pid */
    @Excel(name = "流程pid")
    private Long flowPid;

    /** 事件流程内容 */
    @Excel(name = "事件流程内容")
    private String flowContent;

    /**
     * 事件状态 0:未完成 1:已完成
     */
    @Excel(name = "事件状态")
    private String eventState;

    /**
     * 子节点
     */
    private List<SdEventHandle> children;

    @ApiModelProperty("预案Id")
    private Long reserveId;

    @ApiModelProperty("环节Id")
    private Long processId;

    @ApiModelProperty("流程排序")
    private String flowSort;

    public String getFlowSort() {
        return flowSort;
    }

    public void setFlowSort(String flowSort) {
        this.flowSort = flowSort;
    }

    public Long getReserveId() {
        return reserveId;
    }

    public void setReserveId(Long reserveId) {
        this.reserveId = reserveId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public List<SdEventHandle> getChildren() {
        return children;
    }

    public void setChildren(List<SdEventHandle> children) {
        this.children = children;
    }

    public String getEventState() {
        return eventState;
    }

    public void setEventState(String eventState) {
        this.eventState = eventState;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEventId(Long eventId) 
    {
        this.eventId = eventId;
    }

    public Long getEventId() 
    {
        return eventId;
    }
    public void setFlowId(Long flowId) 
    {
        this.flowId = flowId;
    }

    public Long getFlowId() 
    {
        return flowId;
    }
    public void setFlowPid(Long flowPid) 
    {
        this.flowPid = flowPid;
    }

    public Long getFlowPid() 
    {
        return flowPid;
    }
    public void setFlowContent(String flowContent) 
    {
        this.flowContent = flowContent;
    }

    public String getFlowContent() 
    {
        return flowContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eventId", getEventId())
            .append("flowId", getFlowId())
            .append("flowPid", getFlowPid())
            .append("flowContent", getFlowContent())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
