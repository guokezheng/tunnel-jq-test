package com.tunnel.business.domain.electromechanicalPatrol;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 巡查点清单对象 sd_patrol_list
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public class SdPointList extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /** 巡检点类型（0：巡检点；1：故障点） */
    @Excel(name = "巡检点类型", readConverterExp = "0=：巡检点；1：故障点")
    private String patrol_type;

    /*设备桩号*/
    private String pile;

    /*设备id  故障id */
    private String eq_id;
    /*设备类型名称  故障类型名称*/
    private String type_name;
    /*设备名称*/
    private String eq_name;
    /*隧道名称*/
    private Integer xc_sort;

    private String tunnel_id;

    private String tunnel_name;

    private String dict_lable;

    public String getDict_lable() {
        return this.dict_lable;
    }

    public void setDict_lable(final String dict_lable) {
        this.dict_lable = dict_lable;
    }

    public String getPatrol_type() {
        return this.patrol_type;
    }

    public void setPatrol_type(final String patrol_type) {
        this.patrol_type = patrol_type;
    }

    public Integer getXc_sort() {
        return this.xc_sort;
    }

    public void setXc_sort(final Integer xc_sort) {
        this.xc_sort = xc_sort;
    }

    public String getPile() {
        return this.pile;
    }

    public void setPile(final String pile) {
        this.pile = pile;
    }

    public String getEq_id() {
        return this.eq_id;
    }

    public void setEq_id(final String eq_id) {
        this.eq_id = eq_id;
    }

    public String getType_name() {
        return this.type_name;
    }

    public void setType_name(final String type_name) {
        this.type_name = type_name;
    }

    public String getEq_name() {
        return this.eq_name;
    }

    public void setEq_name(final String eq_name) {
        this.eq_name = eq_name;
    }

    public String getTunnel_id() {
        return this.tunnel_id;
    }

    public void setTunnel_id(final String tunnel_id) {
        this.tunnel_id = tunnel_id;
    }

    public String getTunnel_name() {
        return this.tunnel_name;
    }

    public void setTunnel_name(final String tunnel_name) {
        this.tunnel_name = tunnel_name;
    }
}
