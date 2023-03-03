package com.tunnel.business.domain.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * 事件类型预案流程关联对象 sd_join_type_flow
 * 
 * @author ruoyi
 * @date 2022-12-10
 */
public class SdJoinTypeFlow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 事件类型id */
    /*@Excel(name = "事件类型id")*/
    private Long eventTypeId;

    /** 环节id */
   /* @Excel(name = "环节id")*/
    private Long flowId;

    /** 环节pid */
   /* @Excel(name = "环节pid")*/
    private Long flowPid;

    /** 环节名称 */
   /* @Excel(name = "环节名称")*/
    private String flowName;

    /** 环节排序 */
   /* @Excel(name = "环节排序")*/
    private String flowSort;

    /**
     * 流程list
     */
    List<SdPlanFlow> planFlowList;
    @Excel(name = "事件分类")
    private String dictLabel;
    @Excel(name = "事件类型")
    private String eventType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    private String ids;

    public String getIds() {
        return this.ids;
    }

    public void setIds( String ids) {
        this.ids = ids;
    }

    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime( Date createTime) {
        this.createTime = createTime;
    }

    public String getEventType() {
        return this.eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    private List<SdJoinTypeFlow> children;

    public List<SdJoinTypeFlow> getChildren() {
        return children;
    }

    public void setChildren(List<SdJoinTypeFlow> children) {
        this.children = children;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public List<SdPlanFlow> getPlanFlowList() {
        return planFlowList;
    }

    public void setPlanFlowList(List<SdPlanFlow> planFlowList) {
        this.planFlowList = planFlowList;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEventTypeId(Long eventTypeId) 
    {
        this.eventTypeId = eventTypeId;
    }

    public Long getEventTypeId() 
    {
        return eventTypeId;
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
    public void setFlowName(String flowName) 
    {
        this.flowName = flowName;
    }

    public String getFlowName() 
    {
        return flowName;
    }
    public void setFlowSort(String flowSort) 
    {
        this.flowSort = flowSort;
    }

    public String getFlowSort() 
    {
        return flowSort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eventTypeId", getEventTypeId())
            .append("flowId", getFlowId())
            .append("flowPid", getFlowPid())
            .append("flowName", getFlowName())
            .append("flowSort", getFlowSort())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
