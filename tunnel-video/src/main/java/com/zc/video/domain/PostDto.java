package com.zc.video.domain;

import lombok.Data;

/**
 * @author ：xieyuwei
 * @date ：Created in 2022/7/13 10:22
 * 文件说明： 实时流传参
 */
@Data
public class PostDto {
    /**
     * type  系统编号 默认1 后期通知更改
     */
    private String type;
    /**
     * 相机编号
     */
    private String camId;
}
