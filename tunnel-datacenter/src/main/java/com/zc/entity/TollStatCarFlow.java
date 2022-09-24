package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 收费站车流量信息
 */
public class TollStatCarFlow {

    @JsonProperty(value = "total_flow")
    private String totalFlow;

    @JsonProperty(value = "bus1_flow")
    private String bus1Flow;

    @JsonProperty(value = "bus2_flow")
    private String bus2Flow;

    @JsonProperty(value = "bus3_flow")
    private String bus3Flow;

    @JsonProperty(value = "bus4_flow")
    private String bus4Flow;

    @JsonProperty(value = "truck1_flow")
    private String truck1Flow;

    @JsonProperty(value = "truck2_flow")
    private String truck2Flow;

    @JsonProperty(value = "truck3_flow")
    private String truck3Flow;

    @JsonProperty(value = "truck4_flow")
    private String truck4Flow;

    @JsonProperty(value = "truck5_flow")
    private String truck5Flow;

    @JsonProperty(value = "truck6_flow")
    private String truck6Flow;

    @JsonProperty(value = "total_free")
    private String totalFree;

    @JsonProperty(value = "bus1_fee")
    private String bus1Fee;

    @JsonProperty(value = "bus2_fee")
    private String bus2Fee;

    @JsonProperty(value = "bus3_fee")
    private String bus3Fee;

    @JsonProperty(value = "bus4_fee")
    private String bus4Fee;

    @JsonProperty(value = "truck1_fee")
    private String truck1Fee;

    @JsonProperty(value = "truck2_fee")
    private String truck2Fee;

    @JsonProperty(value = "truck3_fee")
    private String truck3Fee;

    @JsonProperty(value = "truck4_fee")
    private String truck4Fee;

    @JsonProperty(value = "truck5_fee")
    private String truck5Fee;

    @JsonProperty(value = "truck6_fee")
    private String truck6Fee;

    public String getTotalFlow() {
        return totalFlow;
    }

    public void setTotalFlow(String totalFlow) {
        this.totalFlow = totalFlow;
    }

    public String getBus1Flow() {
        return bus1Flow;
    }

    public void setBus1Flow(String bus1Flow) {
        this.bus1Flow = bus1Flow;
    }

    public String getBus2Flow() {
        return bus2Flow;
    }

    public void setBus2Flow(String bus2Flow) {
        this.bus2Flow = bus2Flow;
    }

    public String getBus3Flow() {
        return bus3Flow;
    }

    public void setBus3Flow(String bus3Flow) {
        this.bus3Flow = bus3Flow;
    }

    public String getBus4Flow() {
        return bus4Flow;
    }

    public void setBus4Flow(String bus4Flow) {
        this.bus4Flow = bus4Flow;
    }

    public String getTruck1Flow() {
        return truck1Flow;
    }

    public void setTruck1Flow(String truck1Flow) {
        this.truck1Flow = truck1Flow;
    }

    public String getTruck2Flow() {
        return truck2Flow;
    }

    public void setTruck2Flow(String truck2Flow) {
        this.truck2Flow = truck2Flow;
    }

    public String getTruck3Flow() {
        return truck3Flow;
    }

    public void setTruck3Flow(String truck3Flow) {
        this.truck3Flow = truck3Flow;
    }

    public String getTruck4Flow() {
        return truck4Flow;
    }

    public void setTruck4Flow(String truck4Flow) {
        this.truck4Flow = truck4Flow;
    }

    public String getTruck5Flow() {
        return truck5Flow;
    }

    public void setTruck5Flow(String truck5Flow) {
        this.truck5Flow = truck5Flow;
    }

    public String getTruck6Flow() {
        return truck6Flow;
    }

    public void setTruck6Flow(String truck6Flow) {
        this.truck6Flow = truck6Flow;
    }

    public String getTotalFree() {
        return totalFree;
    }

    public void setTotalFree(String totalFree) {
        this.totalFree = totalFree;
    }

    public String getBus1Fee() {
        return bus1Fee;
    }

    public void setBus1Fee(String bus1Fee) {
        this.bus1Fee = bus1Fee;
    }

    public String getBus2Fee() {
        return bus2Fee;
    }

    public void setBus2Fee(String bus2Fee) {
        this.bus2Fee = bus2Fee;
    }

    public String getBus3Fee() {
        return bus3Fee;
    }

    public void setBus3Fee(String bus3Fee) {
        this.bus3Fee = bus3Fee;
    }

    public String getBus4Fee() {
        return bus4Fee;
    }

    public void setBus4Fee(String bus4Fee) {
        this.bus4Fee = bus4Fee;
    }

    public String getTruck1Fee() {
        return truck1Fee;
    }

    public void setTruck1Fee(String truck1Fee) {
        this.truck1Fee = truck1Fee;
    }

    public String getTruck2Fee() {
        return truck2Fee;
    }

    public void setTruck2Fee(String truck2Fee) {
        this.truck2Fee = truck2Fee;
    }

    public String getTruck3Fee() {
        return truck3Fee;
    }

    public void setTruck3Fee(String truck3Fee) {
        this.truck3Fee = truck3Fee;
    }

    public String getTruck4Fee() {
        return truck4Fee;
    }

    public void setTruck4Fee(String truck4Fee) {
        this.truck4Fee = truck4Fee;
    }

    public String getTruck5Fee() {
        return truck5Fee;
    }

    public void setTruck5Fee(String truck5Fee) {
        this.truck5Fee = truck5Fee;
    }

    public String getTruck6Fee() {
        return truck6Fee;
    }

    public void setTruck6Fee(String truck6Fee) {
        this.truck6Fee = truck6Fee;
    }

    @Override
    public String toString() {
        return "TollStatCarFlow{" +
                "totalFlow='" + totalFlow + '\'' +
                ", bus1Flow='" + bus1Flow + '\'' +
                ", bus2Flow='" + bus2Flow + '\'' +
                ", bus3Flow='" + bus3Flow + '\'' +
                ", bus4Flow='" + bus4Flow + '\'' +
                ", truck1Flow='" + truck1Flow + '\'' +
                ", truck2Flow='" + truck2Flow + '\'' +
                ", truck3Flow='" + truck3Flow + '\'' +
                ", truck4Flow='" + truck4Flow + '\'' +
                ", truck5Flow='" + truck5Flow + '\'' +
                ", truck6Flow='" + truck6Flow + '\'' +
                ", totalFree='" + totalFree + '\'' +
                ", bus1Fee='" + bus1Fee + '\'' +
                ", bus2Fee='" + bus2Fee + '\'' +
                ", bus3Fee='" + bus3Fee + '\'' +
                ", bus4Fee='" + bus4Fee + '\'' +
                ", truck1Fee='" + truck1Fee + '\'' +
                ", truck2Fee='" + truck2Fee + '\'' +
                ", truck3Fee='" + truck3Fee + '\'' +
                ", truck4Fee='" + truck4Fee + '\'' +
                ", truck5Fee='" + truck5Fee + '\'' +
                ", truck6Fee='" + truck6Fee + '\'' +
                '}';
    }
}
