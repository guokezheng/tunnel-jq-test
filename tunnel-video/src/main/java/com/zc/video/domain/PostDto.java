package com.zc.video.domain;


/**
 * @author ：xieyuwei
 * @date ：Created in 2022/7/13 10:22
 * 文件说明： 实时流传参
 */
public class PostDto {
    /**
     * type  系统编号 默认1 后期通知更改
     */
    private String type;
    /**
     * 相机编号
     */
    private String camId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCamId() {
        return camId;
    }

    public void setCamId(String camId) {
        this.camId = camId;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "type='" + type + '\'' +
                ", camId='" + camId + '\'' +
                '}';
    }
}
