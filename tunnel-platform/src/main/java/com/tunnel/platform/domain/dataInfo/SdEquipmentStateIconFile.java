package com.tunnel.platform.domain.dataInfo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备状态图标对象 sd_equipment_state_icon_file
 * 
 * @author yujieying
 * @date 2021-1-23
 */
public class SdEquipmentStateIconFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /**图标文件关联ID  */
    @Excel(name = "图标文件关联ID")
    private String stateIconId;
    
    /**图标名称 */
    @Excel(name = "图标名称")
    private String stateIconName;

    /**图标地址 */
    @Excel(name = "图标地址")
    private String url;
 

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setStateIconId(String stateIconId) 
    {
        this.stateIconId = stateIconId;
    }

    public String getStateIconId() 
    {
        return stateIconId;
    }
    public void setStateIconName(String stateIconName) {
		this.stateIconName = stateIconName;
	}

    public String getStateIconName() {
		return stateIconName;
	}
	
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }

   
	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("stateIconId", getStateIconId())
            .append("url", getUrl())
            .append("stateIconName", getStateIconName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
