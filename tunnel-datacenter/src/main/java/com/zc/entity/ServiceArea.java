package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 服务区
 */
@Data
public class ServiceArea {

    @JsonProperty(value = "servicearea_id")
    private String serviceareaId;

    @JsonProperty(value = "servicearea_code")
    private String serviceareaCode;

    @JsonProperty(value = "servicearea_name")
    private String serviceareaName;

    @JsonProperty(value = "servicearea_type")
    private String serviceareaType;

    @JsonProperty(value = "stake_num")
    private String stakeNum;

    private String lat;

    private String lng;

    @JsonProperty(value = "road_id")
    private String roadId;

    @JsonProperty(value = "road_name")
    private String roadName;

    @JsonProperty(value = "area_size")
    private String areaSize;

    @JsonProperty(value = "build_time")
    private String buildTime;

    @JsonProperty(value = "first_manage_unit")
    private String firstManageUnit;

    @JsonProperty(value = "second_manage_unit")
    private String secondManageUnit;

    @JsonProperty(value = "third_manage_unit")
    private String thirdManageUnit;

    @JsonProperty(value = "manager_name")
    private String managerName;

    @JsonProperty(value = "manager_phone")
    private String managerPhone;

    private String introduce;

    private String area;

    @JsonProperty(value = "direction_id")
    private String direction_id;

    @JsonProperty(value = "direction_name")
    private String directionName;

    private String status;

    @JsonProperty(value = "oil_station")
    private String oilStation;

    @JsonProperty(value = "gas_station")
    private String gasStation;

    @JsonProperty(value = "h_station")
    private String hStation;

    @JsonProperty(value = "charge_station")
    private String chargeStation;

    @JsonProperty(value = "charge_pile_count")
    private String chargePileCount;

    @JsonProperty(value = "charge_gun_count")
    private String chargeGunCount;

    @JsonProperty(value = "charge_power")
    private String chargePower;

    private String cafeteria;

    @JsonProperty(value = "street_food")
    private String streetFood;

    private String supermarket;

    private String hotel;

    private String wifi;

    private String garage;

    private String thirdToilet;

    @JsonProperty(value = "mb_room")
    private String mbRoom;

    @JsonProperty(value = "men_count")
    private String menCount;

    @JsonProperty(value = "women_count")
    private String womenCount;

    @JsonProperty(value = "car_park_count")
    private String carParkCount;

    @JsonProperty(value = "bus_park_count")
    private String busParkCount;

    @JsonProperty(value = "truck_park_count")
    private String truckParkCount;

    @JsonProperty(value = "danger_park_count")
    private String dangerParkCount;

    @JsonProperty(value = "nEnergy_park_count")
    private String nEnergyParkCount;

    @JsonProperty(value = "dirve_home")
    private String dirveHome;

    @JsonProperty(value = "org_id")
    private String orgId;

    @JsonProperty(value = "dir_code")
    private String dirCode;

    @JsonProperty(value = "search_content")
    private String searchContent;

    @JsonProperty(value = "replace_station")
    private String replaceStation;

    @JsonProperty(value = "status_remark")
    private String statusRemark;

    @JsonProperty(value = "oilStation_remrk")
    private String oilStationRemrk;

    @JsonProperty(value = "gasStation_remark")
    private String gasStationRemark;

    @JsonProperty(value = "hStation_remark")
    private String hStationRemark;

    @JsonProperty(value = "charge_station_remark")
    private String chargeStationRemark;

    @JsonProperty(value = "garage_remark")
    private String garageRemark;

    @JsonProperty(value = "replace_station_remark")
    private String replaceStationRemark;

    @JsonProperty(value = "hotel_remark")
    private String hotelRemark;

    @JsonProperty(value = "police_station")
    private String policeStation;

    private String mtoilet;

    private String wtoilet;

    @JsonProperty(value = "boiler_room")
    private String boilerRoom;

    @JsonProperty(value = "emergency_room")
    private String emergencyRoom;

}
