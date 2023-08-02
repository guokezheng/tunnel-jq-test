package com.tunnel.business.domain.energyManagement;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhai
 * @date 2023/8/1
 */
public class EnergyAnalysisElectricityBill extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 隧道id
     */
    private String deptCode;

    /**
     * 统计类型
     */
    private int statisticsType;

    /**
     * 时间
     */
    private Date statisticsDate;

    /**
     * 分时段类型
     */
    private String splitTimeType;

    /**
     * 用电量
     */
    private BigDecimal value;

    private String rt;

    private String tunnelName;

    private Map<String,Object> map;

    private List<EnergyAnalysisElectricityBill> eneDataList;

    public List<EnergyAnalysisElectricityBill> getEneDataList() {
        return eneDataList;
    }

    public void setEneDataList(List<EnergyAnalysisElectricityBill> eneDataList) {
        this.eneDataList = eneDataList;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getTunnelName() {
        return tunnelName;
    }

    public void setTunnelName(String tunnelName) {
        this.tunnelName = tunnelName;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public int getStatisticsType() {
        return statisticsType;
    }

    public void setStatisticsType(int statisticsType) {
        this.statisticsType = statisticsType;
    }

    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public String getSplitTimeType() {
        return splitTimeType;
    }

    public void setSplitTimeType(String splitTimeType) {
        this.splitTimeType = splitTimeType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
