package com.tunnel.business.datacenter.domain.enumeration;

/**
 * describe: 事件来源
 *
 * @author zs
 * @date 2022/9/15
 */
public enum EventSourceEnum {
//    事件来源（0：雷达，1：火灾报警，2：紧急电话，3：其他-手动录入，4：机器人，5：消防炮）
    radar("0","雷达"),
    fire_alarm("1","火灾报警"),
    phone("2","紧急电话"),
    hand_type("3","手动录入"),
    robot("4","机器人"),
    fire_monitor("5","消防炮");


    EventSourceEnum(String code,String name){
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
