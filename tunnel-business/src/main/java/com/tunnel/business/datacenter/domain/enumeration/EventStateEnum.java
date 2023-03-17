package com.tunnel.business.datacenter.domain.enumeration;

/**
 * describe: 事件状态枚举类
 *
 * @author zs
 * @date 2022/9/25
 */
public enum EventStateEnum {

    processing("0","处理中"),
    processed("1","已处理"),
    ignore("2","已挂起"),
    unprocessed("3","待确认"),
    YI_QUE_REN("4","已确认"),
    WU_BAO("5","误报");

    EventStateEnum(String code,String name){
        this.code = code;
        this.name = name;
    }

    /**
     * 编码
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
