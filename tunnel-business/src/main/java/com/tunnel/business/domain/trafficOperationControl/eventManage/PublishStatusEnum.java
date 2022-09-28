package com.tunnel.business.domain.trafficOperationControl.eventManage;

/**
 * describe: 发布状态枚举类
 *
 * @author zs
 * @date 2022/3/15
 */
public enum PublishStatusEnum {

    /**
     * 未发布
     */
    un_publish("1","未发布"),

    /**
     * 已发布
     */
    publish("2","已发布");

    PublishStatusEnum(String code,String name){
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
