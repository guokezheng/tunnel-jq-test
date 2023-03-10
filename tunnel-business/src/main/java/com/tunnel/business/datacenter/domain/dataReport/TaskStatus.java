package com.tunnel.business.datacenter.domain.dataReport;

/**
 * describe: 字典类型枚举类
 *
 * @author tjw
 * @date 2023/3/8
 */
public enum TaskStatus {
    /**
     * 任务状态字典
     */
    DAIXUNCHA("0", "待巡检"),
    XUNCHAZHONG("1", "巡检中"),
    YIWANJIE("2", "已完结"),
    DAIHUICHUAN("3", "待回传"),
    YICHAOSHI("4", "已超时");



    TaskStatus(String code, String name){
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
