package com.tunnel.business.domain.energyManagement;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * ElectricityData
 *
 * @date: 2022-11-27
 * @version: 1.0
 */
public class ElectricityData {

    private String id;

    private String name;

    private Double value;

    private Double lastValue;

    private String type;

    private String rt;

    @JsonIgnore
    private Map<String,Object> map;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Double getLastValue() {
        return lastValue;
    }

    public void setLastValue(Double lastValue) {
        this.lastValue = lastValue;
    }
}
