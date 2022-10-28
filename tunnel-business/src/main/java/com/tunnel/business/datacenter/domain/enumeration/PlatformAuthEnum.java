package com.tunnel.business.datacenter.domain.enumeration;

/**
 * describe: 两级平台权限控制
 *
 * @author zs
 * @date 2022/10/24
 */
public enum PlatformAuthEnum {

    GLZ("GLZ","管理站平台"),
    GSY("GSY","高速云平台");

    PlatformAuthEnum(String code,String name){
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
