package com.tunnel.business.datacenter.domain.dataReport;

/**
 * describe: 字典类型枚举类
 *
 * @author tjw
 * @date 2023/4/26
 */
public enum FaultStatus {
    /**
     * 故障消除状态字典
     */
    FAULTNULL("2", "无故障"),
    FAULTWEIXIAOCHU("1", "未消除"),
    FAULTYIXIAOCHU("0", "已消除"),
    /**
     * 巡检点故障处理情况
     */
    PATROLNULL("0", "无故障"),
    PATROLYIXIAOCHU("1", "已消除"),
    PATROLWEIXIAOCHU("2", "未消除");

    FaultStatus(String code, String name){
        this.code = code;
        this.name = name;
    }

    /**
     * 代号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
