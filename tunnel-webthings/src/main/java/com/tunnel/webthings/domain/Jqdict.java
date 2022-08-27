package com.tunnel.webthings.domain;

import lombok.Data;

import java.util.Date;

/**
 * 字典类
 * @author dear_
 */
@Data
public class Jqdict {
    private Integer dictId;
    private String dict_name;
    private String dict_type;
    private String status;
    private String dictLabel;
    private String dictValue;
    private String sort;
    private String createUser;
    private Date create_time;
    private String remark;

}
