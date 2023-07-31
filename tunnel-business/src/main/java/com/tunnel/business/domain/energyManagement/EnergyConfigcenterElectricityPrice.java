package com.tunnel.business.domain.energyManagement;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 电价配置对象 energy_configcenter_electricity_price
 *
 * @author ruoyi
 * @date 2023-07-14
 */
public class EnergyConfigcenterElectricityPrice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 代理购电价格 */
    @Excel(name = "代理购电价格")
    private BigDecimal unitPrice2;

    /** 容量补偿电价 */
    @Excel(name = "容量补偿电价")
    private BigDecimal unitPrice3;

    /** 综合损益分摊 */
    @Excel(name = "综合损益分摊")
    private BigDecimal unitPrice4;

    /** 电度输配电价 */
    @Excel(name = "电度输配电价")
    private BigDecimal unitPrice5;

    /** 政府性基金及附加 */
    @Excel(name = "政府性基金及附加")
    private BigDecimal unitPrice6;

    /** 尖系数 */
    @Excel(name = "尖系数")
    private BigDecimal jianCof;

    /** 峰系数 */
    @Excel(name = "峰系数")
    private BigDecimal fengCof;

    /** 谷系数 */
    @Excel(name = "谷系数")
    private BigDecimal guCof;

    /** 深谷系数 */
    @Excel(name = "深谷系数")
    private BigDecimal shenCof;

    /** 对应月份 */
    @Excel(name = "对应月份")
    private String month;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUnitPrice2(BigDecimal unitPrice2)
    {
        this.unitPrice2 = unitPrice2;
    }

    public BigDecimal getUnitPrice2()
    {
        return unitPrice2;
    }
    public void setUnitPrice3(BigDecimal unitPrice3)
    {
        this.unitPrice3 = unitPrice3;
    }

    public BigDecimal getUnitPrice3()
    {
        return unitPrice3;
    }
    public void setUnitPrice4(BigDecimal unitPrice4)
    {
        this.unitPrice4 = unitPrice4;
    }

    public BigDecimal getUnitPrice4()
    {
        return unitPrice4;
    }
    public void setUnitPrice5(BigDecimal unitPrice5)
    {
        this.unitPrice5 = unitPrice5;
    }

    public BigDecimal getUnitPrice5()
    {
        return unitPrice5;
    }
    public void setUnitPrice6(BigDecimal unitPrice6)
    {
        this.unitPrice6 = unitPrice6;
    }

    public BigDecimal getUnitPrice6()
    {
        return unitPrice6;
    }
    public void setJianCof(BigDecimal jianCof)
    {
        this.jianCof = jianCof;
    }

    public BigDecimal getJianCof()
    {
        return jianCof;
    }
    public void setFengCof(BigDecimal fengCof)
    {
        this.fengCof = fengCof;
    }

    public BigDecimal getFengCof()
    {
        return fengCof;
    }
    public void setGuCof(BigDecimal guCof)
    {
        this.guCof = guCof;
    }

    public BigDecimal getGuCof()
    {
        return guCof;
    }
    public void setShenCof(BigDecimal shenCof)
    {
        this.shenCof = shenCof;
    }

    public BigDecimal getShenCof()
    {
        return shenCof;
    }
    public void setMonth(String month)
    {
        this.month = month;
    }

    public String getMonth()
    {
        return month;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("unitPrice2", getUnitPrice2())
                .append("unitPrice3", getUnitPrice3())
                .append("unitPrice4", getUnitPrice4())
                .append("unitPrice5", getUnitPrice5())
                .append("unitPrice6", getUnitPrice6())
                .append("jianCof", getJianCof())
                .append("fengCof", getFengCof())
                .append("guCof", getGuCof())
                .append("shenCof", getShenCof())
                .append("month", getMonth())
                .toString();
    }
}
