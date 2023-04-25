package com.tunnel.business.datacenter.domain.enumeration;

/**
 * describe: 操作日志-枚举类
 *
 * @author zs
 * @date 2023/4/8
 */
public enum OperationLogEnum {


    /**
     * 操作成功
     */
    STATE_SUCCESS("1","操作成功"),
    /**
     * 操作失败
     */
    STATE_ERROR("0","操作失败");



    OperationLogEnum(String code,String name){
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
