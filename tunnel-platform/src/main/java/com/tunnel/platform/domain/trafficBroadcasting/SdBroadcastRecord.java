package com.tunnel.platform.domain.trafficBroadcasting;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 广播记录对象 sd_broadcast_record
 * 
 * @author ruoyi
 * @date 2021-11-25
 */
public class SdBroadcastRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 广播设备id */
    @Excel(name = "广播设备id")
    private String broadcastEqids;

    private String broadcastEqnames;

    public String getBroadcastEqnames() {
        return broadcastEqnames;
    }

    public void setBroadcastEqnames(String broadcastEqnames) {
        this.broadcastEqnames = broadcastEqnames;
    }

    /** 发布结果 */
    @Excel(name = "发布结果")
    private String publishResults;

    /** 广播内容 */
    @Excel(name = "广播内容")
    private String broadcastContent;

    /** 发言人 */
    @Excel(name = "发言人")
    private String broadcastSpokesman;

    /** 语速 */
    @Excel(name = "语速")
    private String broadcastSpeed;

    /** 是否保存录音    1：不保存;      0：保存 */
    @Excel(name = "是否保存录音")
    private String isSaveRecording;

    /** 音量 */
    @Excel(name = "音量")
    private String volume;

    /** 广播次数 */
    @Excel(name = "广播次数")
    private String numberOfBroadcasts;

    /** 录音地址 */
    @Excel(name = "录音地址")
    private String recordingAddress;

    /** 备注 */
    @Excel(name = "备注")
    private String remake;

    /** 备注1 */
    @Excel(name = "备注1")
    private String remake1;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setBroadcastEqids(String broadcastEqids) 
    {
        this.broadcastEqids = broadcastEqids;
    }

    public String getBroadcastEqids() 
    {
        return broadcastEqids;
    }
    public void setPublishResults(String publishResults) 
    {
        this.publishResults = publishResults;
    }

    public String getPublishResults() 
    {
        return publishResults;
    }
    public void setBroadcastContent(String broadcastContent) 
    {
        this.broadcastContent = broadcastContent;
    }

    public String getBroadcastContent() 
    {
        return broadcastContent;
    }
    public void setBroadcastSpokesman(String broadcastSpokesman) 
    {
        this.broadcastSpokesman = broadcastSpokesman;
    }

    public String getBroadcastSpokesman() 
    {
        return broadcastSpokesman;
    }
    public void setBroadcastSpeed(String broadcastSpeed) 
    {
        this.broadcastSpeed = broadcastSpeed;
    }

    public String getBroadcastSpeed() 
    {
        return broadcastSpeed;
    }
    public void setIsSaveRecording(String isSaveRecording) 
    {
        this.isSaveRecording = isSaveRecording;
    }

    public String getIsSaveRecording() 
    {
        return isSaveRecording;
    }
    public void setVolume(String volume) 
    {
        this.volume = volume;
    }

    public String getVolume() 
    {
        return volume;
    }
    public void setNumberOfBroadcasts(String numberOfBroadcasts) 
    {
        this.numberOfBroadcasts = numberOfBroadcasts;
    }

    public String getNumberOfBroadcasts() 
    {
        return numberOfBroadcasts;
    }
    public void setRecordingAddress(String recordingAddress) 
    {
        this.recordingAddress = recordingAddress;
    }

    public String getRecordingAddress() 
    {
        return recordingAddress;
    }
    public void setRemake(String remake) 
    {
        this.remake = remake;
    }

    public String getRemake() 
    {
        return remake;
    }
    public void setRemake1(String remake1) 
    {
        this.remake1 = remake1;
    }

    public String getRemake1() 
    {
        return remake1;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("broadcastEqids", getBroadcastEqids())
            .append("broadcastEqnames", getBroadcastEqnames())
            .append("createTime", getCreateTime())
            .append("publishResults", getPublishResults())
            .append("broadcastContent", getBroadcastContent())
            .append("broadcastSpokesman", getBroadcastSpokesman())
            .append("broadcastSpeed", getBroadcastSpeed())
            .append("isSaveRecording", getIsSaveRecording())
            .append("volume", getVolume())
            .append("numberOfBroadcasts", getNumberOfBroadcasts())
            .append("recordingAddress", getRecordingAddress())
            .append("remake", getRemake())
            .append("remake1", getRemake1())
            .toString();
    }
}
