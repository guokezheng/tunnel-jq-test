package com.tunnel.webthings.domain;

import lombok.Data;

import java.util.Date;

/**
 * 字典类
 * @author dear_
 */
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

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public String getDict_name() {
        return dict_name;
    }

    public void setDict_name(String dict_name) {
        this.dict_name = dict_name;
    }

    public String getDict_type() {
        return dict_type;
    }

    public void setDict_type(String dict_type) {
        this.dict_type = dict_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Jqdict{" +
                "dictId=" + dictId +
                ", dict_name='" + dict_name + '\'' +
                ", dict_type='" + dict_type + '\'' +
                ", status='" + status + '\'' +
                ", dictLabel='" + dictLabel + '\'' +
                ", dictValue='" + dictValue + '\'' +
                ", sort='" + sort + '\'' +
                ", createUser='" + createUser + '\'' +
                ", create_time=" + create_time +
                ", remark='" + remark + '\'' +
                '}';
    }
}
