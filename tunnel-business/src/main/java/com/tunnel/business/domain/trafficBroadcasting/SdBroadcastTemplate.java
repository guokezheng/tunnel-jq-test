package com.tunnel.business.domain.trafficBroadcasting;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 广播模板对象 sd_broadcast_template
 * 
 * @author ruoyi
 * @date 2021-12-03
 */
public class SdBroadcastTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 广播内容 */
    @Excel(name = "广播内容")
    private String broadcastContent;

    /** 发言人 */
    @Excel(name = "发言人")
    private String broadcastSpokesman;

    /** 语速 */
    @Excel(name = "语速")
    private String broadcastSpeed;

    /** 是否保存录音 */
    @Excel(name = "是否保存录音")
    private String isSaveRecording;

    /** 音量 */
    @Excel(name = "音量")
    private String volume;

    /** 广播次数 */
    @Excel(name = "广播次数")
    private String numberOfBroadcasts;

    /** 备注 */
    @Excel(name = "备注")
    private String remake;

    /** 备注1 */
    @Excel(name = "备注1")
    private String remake1;

    /** 备注2 */
    @Excel(name = "备注2")
    private String remake2;

    private String ids;

    public String getIds() {
        return this.ids;
    }

    public void setIds( String ids) {
        this.ids = ids;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
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
    public void setRemake2(String remake2) 
    {
        this.remake2 = remake2;
    }

    public String getRemake2() 
    {
        return remake2;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("broadcastContent", getBroadcastContent())
            .append("broadcastSpokesman", getBroadcastSpokesman())
            .append("broadcastSpeed", getBroadcastSpeed())
            .append("isSaveRecording", getIsSaveRecording())
            .append("volume", getVolume())
            .append("numberOfBroadcasts", getNumberOfBroadcasts())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remake", getRemake())
            .append("remake1", getRemake1())
            .append("remake2", getRemake2())
            .toString();
    }
}
