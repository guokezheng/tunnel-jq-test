package com.tunnel.platform.domain.informationBoard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 发布记录对象 sd_release_record
 * 
 * @author 刘方堃
 * @date 2021-11-29
 */
public class SdReleaseRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 事件发布设备关联表ID */
    private Long vmsEventId;

    /** 发布设备 */
    @Excel(name = "发布设备")
    private String releaseDev;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
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

    /** 审核机构 */
    @Excel(name = "审核机构")
    private String examineDeptName;

    /** 审核机构 */
    @Excel(name = "审核机构")
    private Long examineDeptId;

    /** 审核用户 */
    @Excel(name = "审核用户")
    private String examineUserId;

    /** 审核用户 */
    @Excel(name = "审核用户")
    private String examineUserName;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date examineTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setVmsEventId(Long vmsEventId) 
    {
        this.vmsEventId = vmsEventId;
    }

    public Long getVmsEventId() 
    {
        return vmsEventId;
    }
    public void setReleaseDev(String releaseDev) 
    {
        this.releaseDev = releaseDev;
    }

    public String getReleaseDev() 
    {
        return releaseDev;
    }
    public void setReleaseTime(Date releaseTime) 
    {
        this.releaseTime = releaseTime;
    }

    public Date getReleaseTime() 
    {
        return releaseTime;
    }
    public void setReleaseStatus(String releaseStatus) 
    {
        this.releaseStatus = releaseStatus;
    }

    public String getReleaseStatus() 
    {
        return releaseStatus;
    }
    public void setReleaseDeptName(String releaseDeptName) 
    {
        this.releaseDeptName = releaseDeptName;
    }

    public String getReleaseDeptName() 
    {
        return releaseDeptName;
    }
    public void setReleaseDeptId(Long releaseDeptId) 
    {
        this.releaseDeptId = releaseDeptId;
    }

    public Long getReleaseDeptId() 
    {
        return releaseDeptId;
    }
    public void setReleaseUserName(String releaseUserName) 
    {
        this.releaseUserName = releaseUserName;
    }

    public String getReleaseUserName() 
    {
        return releaseUserName;
    }
    public void setReleaseUserId(String releaseUserId) 
    {
        this.releaseUserId = releaseUserId;
    }

    public String getReleaseUserId() 
    {
        return releaseUserId;
    }
    public void setExamineDeptName(String examineDeptName) 
    {
        this.examineDeptName = examineDeptName;
    }

    public String getExamineDeptName() 
    {
        return examineDeptName;
    }
    public void setExamineDeptId(Long examineDeptId) 
    {
        this.examineDeptId = examineDeptId;
    }

    public Long getExamineDeptId() 
    {
        return examineDeptId;
    }
    public void setExamineUserId(String examineUserId) 
    {
        this.examineUserId = examineUserId;
    }

    public String getExamineUserId() 
    {
        return examineUserId;
    }
    public void setExamineUserName(String examineUserName) 
    {
        this.examineUserName = examineUserName;
    }

    public String getExamineUserName() 
    {
        return examineUserName;
    }
    public void setExamineTime(Date examineTime) 
    {
        this.examineTime = examineTime;
    }

    public Date getExamineTime() 
    {
        return examineTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vmsEventId", getVmsEventId())
            .append("releaseDev", getReleaseDev())
            .append("releaseTime", getReleaseTime())
            .append("releaseStatus", getReleaseStatus())
            .append("releaseDeptName", getReleaseDeptName())
            .append("releaseDeptId", getReleaseDeptId())
            .append("releaseUserName", getReleaseUserName())
            .append("releaseUserId", getReleaseUserId())
            .append("examineDeptName", getExamineDeptName())
            .append("examineDeptId", getExamineDeptId())
            .append("examineUserId", getExamineUserId())
            .append("examineUserName", getExamineUserName())
            .append("examineTime", getExamineTime())
            .toString();
    }
}
