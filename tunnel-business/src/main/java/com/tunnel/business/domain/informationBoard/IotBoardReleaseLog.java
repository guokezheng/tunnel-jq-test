package com.tunnel.business.domain.informationBoard;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 情报板内容发布日志对象 iot_board_release_log
 *
 * @author wangyaozong
 * @date 2020-06-01
 */
public class IotBoardReleaseLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 发布前的内容 */
    @Excel(name = "发布前的内容")
    private String releaseOldContent;

    /** 发布后的内容 */
    @Excel(name = "发布后的内容")
    private String releaseNewContent;

    /** 发布人 */
    @Excel(name = "发布人")
    private String releaseBy;

    /** 发布时间 */
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date releaseTime;

    /** 发布状态：0：正常，1：失败 */
    @Excel(name = "发布状态：0：正常，1：失败")
    private String releaseStatus;

    /** 发布机构 */
    @Excel(name = "发布机构")
    private String releaseDeptName;

    /** 发布机构 */
    @Excel(name = "发布机构")
    private Long releaseDeptId;

    /** 发布用户 */
    @Excel(name = "发布用户")
    private String releaseUserName;

    /** 发布用户 */
    @Excel(name = "发布用户")
    private String releaseUserId;

    public String getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getReleaseDeptName() {
        return releaseDeptName;
    }

    public void setReleaseDeptName(String releaseDeptName) {
        this.releaseDeptName = releaseDeptName;
    }

    public Long getReleaseDeptId() {
        return releaseDeptId;
    }

    public void setReleaseDeptId(Long releaseDeptId) {
        this.releaseDeptId = releaseDeptId;
    }

    public String getReleaseUserName() {
        return releaseUserName;
    }

    public void setReleaseUserName(String releaseUserName) {
        this.releaseUserName = releaseUserName;
    }

    public String getReleaseUserId() {
        return releaseUserId;
    }

    public void setReleaseUserId(String releaseUserId) {
        this.releaseUserId = releaseUserId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId()
    {
        return deviceId;
    }
    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName()
    {
        return deviceName;
    }
    public void setReleaseOldContent(String releaseOldContent)
    {
        this.releaseOldContent = releaseOldContent;
    }

    public String getReleaseOldContent()
    {
        return releaseOldContent;
    }
    public void setReleaseNewContent(String releaseNewContent)
    {
        this.releaseNewContent = releaseNewContent;
    }

    public String getReleaseNewContent()
    {
        return releaseNewContent;
    }
    public void setReleaseBy(String releaseBy)
    {
        this.releaseBy = releaseBy;
    }

    public String getReleaseBy()
    {
        return releaseBy;
    }
    public void setReleaseTime(Date releaseTime)
    {
        this.releaseTime = releaseTime;
    }

    public Date getReleaseTime()
    {
        return releaseTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("releaseOldContent", getReleaseOldContent())
            .append("releaseNewContent", getReleaseNewContent())
            .append("releaseBy", getReleaseBy())
            .append("releaseTime", getReleaseTime())
            .toString();
    }
}
