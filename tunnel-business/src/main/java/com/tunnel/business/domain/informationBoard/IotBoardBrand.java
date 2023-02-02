package com.tunnel.business.domain.informationBoard;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IotBoardBrand {
	/**
	 * 设备id
	 */
	private Long brandId;
	/**
	 * 设备名称
	 */
	private String brandName;
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	@Override
	public String toString() {
		 return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
		            .append("brandId", getBrandId())
		            .append("brandName", getBrandName())
		            .toString();
	}

}
