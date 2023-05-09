package com.tunnel.business.datacenter.domain.dataReport;

/**
 * describe: 字典类型枚举类
 *
 * @author tjw
 * @date 2023/4/26
 */
public enum OptType {
    /**
     * 操作类型状态字典
     */
    PAIDAN("0", "派单"),
    JEISHOU("1", "接收"),
    TIJIAO("2", "提交"),
    DELETE("3", "删除");

    OptType(String code, String name){
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
