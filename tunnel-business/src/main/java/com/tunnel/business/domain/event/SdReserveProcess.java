package com.tunnel.business.domain.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 预案流程节点对象 sd_reserve_process
 * 
 * @author ruoyi
 * @date 2022-09-02
 */
public class SdReserveProcess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预案流程节点id */
    private Long id;

    /** 预案id */
    @Excel(name = "预案id")
    private Long reserveId;

    /** 设备类型id */
    @Excel(name = "设备类型id")
    private Long deviceTypeId;

    /** 策略id */
    @Excel(name = "策略id")
    private Long strategyId;

    /** 流程节点名称 */
    @Excel(name = "流程节点名称")
    private String processName;

    /** 流程节点顺序 */
    @Excel(name = "流程节点顺序")
    private Integer processSort;

    private String strategyIds;

    private Long[] handleStrategyList;

    //策略关联设备信息对象
    private SdStrategyRl sdStrategyRl;

    /**
     * 流程阶段名称
     */
    private String processStageName;

    /**
     * 流程集合
     */
    private List<SdReserveProcess> processesList;

    /**
     * 设备名称
     */
    private String equipments;

    /**
     * 设备集合
     */
    private List<String> equipmentList;

    /**
     * 规则条件
     */
    private String retrievalRule;

    /**
     * 指令
     */
    private String state;

    private String type;

    /**
     * 亮度值
     */
    private String brightness;

    private String content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    public String getRetrievalRule() {
        return retrievalRule;
    }

    public void setRetrievalRule(String retrievalRule) {
        this.retrievalRule = retrievalRule;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEquipments() {
        return equipments;
    }

    public void setEquipments(String equipments) {
        this.equipments = equipments;
    }

    public List<String> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<String> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public List<SdReserveProcess> getProcessesList() {
        return processesList;
    }

    public void setProcessesList(List<SdReserveProcess> processesList) {
        this.processesList = processesList;
    }

    public String getStrategyIds() {
        return strategyIds;
    }

    public void setStrategyIds(String strategyIds) {
        this.strategyIds = strategyIds;
    }

    public String getProcessStageName() {
        return processStageName;
    }

    public void setProcessStageName(String processStageName) {
        this.processStageName = processStageName;
    }

    public SdStrategyRl getSdStrategyRl() {
        return sdStrategyRl;
    }

    public void setSdStrategyRl(SdStrategyRl sdStrategyRl) {
        this.sdStrategyRl = sdStrategyRl;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setReserveId(Long reserveId) 
    {
        this.reserveId = reserveId;
    }

    public Long getReserveId() 
    {
        return reserveId;
    }
    public void setDeviceTypeId(Long deviceTypeId) 
    {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getDeviceTypeId() 
    {
        return deviceTypeId;
    }
    public void setStrategyId(Long strategyId)
    {
        this.strategyId = strategyId;
    }

    public Long getStrategyId()
    {
        return strategyId;
    }
    public void setProcessName(String processName) 
    {
        this.processName = processName;
    }

    public String getProcessName() 
    {
        return processName;
    }
    public void setProcessSort(Integer processSort) 
    {
        this.processSort = processSort;
    }

    public Integer getProcessSort() 
    {
        return processSort;
    }

    public Long[] getHandleStrategyList() {
        return handleStrategyList;
    }

    public void setHandleStrategyList(Long[] handleStrategyList) {
        this.handleStrategyList = handleStrategyList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("reserveId", getReserveId())
            .append("deviceTypeId", getDeviceTypeId())
            .append("strategyId", getStrategyId())
            .append("processName", getProcessName())
            .append("processSort", getProcessSort())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
