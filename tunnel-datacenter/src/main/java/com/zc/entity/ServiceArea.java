package com.zc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 服务区
 */
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

    public String getServiceareaId() {
        return serviceareaId;
    }

    public void setServiceareaId(String serviceareaId) {
        this.serviceareaId = serviceareaId;
    }

    public String getServiceareaCode() {
        return serviceareaCode;
    }

    public void setServiceareaCode(String serviceareaCode) {
        this.serviceareaCode = serviceareaCode;
    }

    public String getServiceareaName() {
        return serviceareaName;
    }

    public void setServiceareaName(String serviceareaName) {
        this.serviceareaName = serviceareaName;
    }

    public String getServiceareaType() {
        return serviceareaType;
    }

    public void setServiceareaType(String serviceareaType) {
        this.serviceareaType = serviceareaType;
    }

    public String getStakeNum() {
        return stakeNum;
    }

    public void setStakeNum(String stakeNum) {
        this.stakeNum = stakeNum;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(String areaSize) {
        this.areaSize = areaSize;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getFirstManageUnit() {
        return firstManageUnit;
    }

    public void setFirstManageUnit(String firstManageUnit) {
        this.firstManageUnit = firstManageUnit;
    }

    public String getSecondManageUnit() {
        return secondManageUnit;
    }

    public void setSecondManageUnit(String secondManageUnit) {
        this.secondManageUnit = secondManageUnit;
    }

    public String getThirdManageUnit() {
        return thirdManageUnit;
    }

    public void setThirdManageUnit(String thirdManageUnit) {
        this.thirdManageUnit = thirdManageUnit;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDirection_id() {
        return direction_id;
    }

    public void setDirection_id(String direction_id) {
        this.direction_id = direction_id;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOilStation() {
        return oilStation;
    }

    public void setOilStation(String oilStation) {
        this.oilStation = oilStation;
    }

    public String getGasStation() {
        return gasStation;
    }

    public void setGasStation(String gasStation) {
        this.gasStation = gasStation;
    }

    public String gethStation() {
        return hStation;
    }

    public void sethStation(String hStation) {
        this.hStation = hStation;
    }

    public String getChargeStation() {
        return chargeStation;
    }

    public void setChargeStation(String chargeStation) {
        this.chargeStation = chargeStation;
    }

    public String getChargePileCount() {
        return chargePileCount;
    }

    public void setChargePileCount(String chargePileCount) {
        this.chargePileCount = chargePileCount;
    }

    public String getChargeGunCount() {
        return chargeGunCount;
    }

    public void setChargeGunCount(String chargeGunCount) {
        this.chargeGunCount = chargeGunCount;
    }

    public String getChargePower() {
        return chargePower;
    }

    public void setChargePower(String chargePower) {
        this.chargePower = chargePower;
    }

    public String getCafeteria() {
        return cafeteria;
    }

    public void setCafeteria(String cafeteria) {
        this.cafeteria = cafeteria;
    }

    public String getStreetFood() {
        return streetFood;
    }

    public void setStreetFood(String streetFood) {
        this.streetFood = streetFood;
    }

    public String getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(String supermarket) {
        this.supermarket = supermarket;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getGarage() {
        return garage;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }

    public String getThirdToilet() {
        return thirdToilet;
    }

    public void setThirdToilet(String thirdToilet) {
        this.thirdToilet = thirdToilet;
    }

    public String getMbRoom() {
        return mbRoom;
    }

    public void setMbRoom(String mbRoom) {
        this.mbRoom = mbRoom;
    }

    public String getMenCount() {
        return menCount;
    }

    public void setMenCount(String menCount) {
        this.menCount = menCount;
    }

    public String getWomenCount() {
        return womenCount;
    }

    public void setWomenCount(String womenCount) {
        this.womenCount = womenCount;
    }

    public String getCarParkCount() {
        return carParkCount;
    }

    public void setCarParkCount(String carParkCount) {
        this.carParkCount = carParkCount;
    }

    public String getBusParkCount() {
        return busParkCount;
    }

    public void setBusParkCount(String busParkCount) {
        this.busParkCount = busParkCount;
    }

    public String getTruckParkCount() {
        return truckParkCount;
    }

    public void setTruckParkCount(String truckParkCount) {
        this.truckParkCount = truckParkCount;
    }

    public String getDangerParkCount() {
        return dangerParkCount;
    }

    public void setDangerParkCount(String dangerParkCount) {
        this.dangerParkCount = dangerParkCount;
    }

    public String getnEnergyParkCount() {
        return nEnergyParkCount;
    }

    public void setnEnergyParkCount(String nEnergyParkCount) {
        this.nEnergyParkCount = nEnergyParkCount;
    }

    public String getDirveHome() {
        return dirveHome;
    }

    public void setDirveHome(String dirveHome) {
        this.dirveHome = dirveHome;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDirCode() {
        return dirCode;
    }

    public void setDirCode(String dirCode) {
        this.dirCode = dirCode;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public String getReplaceStation() {
        return replaceStation;
    }

    public void setReplaceStation(String replaceStation) {
        this.replaceStation = replaceStation;
    }

    public String getStatusRemark() {
        return statusRemark;
    }

    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark;
    }

    public String getOilStationRemrk() {
        return oilStationRemrk;
    }

    public void setOilStationRemrk(String oilStationRemrk) {
        this.oilStationRemrk = oilStationRemrk;
    }

    public String getGasStationRemark() {
        return gasStationRemark;
    }

    public void setGasStationRemark(String gasStationRemark) {
        this.gasStationRemark = gasStationRemark;
    }

    public String gethStationRemark() {
        return hStationRemark;
    }

    public void sethStationRemark(String hStationRemark) {
        this.hStationRemark = hStationRemark;
    }

    public String getChargeStationRemark() {
        return chargeStationRemark;
    }

    public void setChargeStationRemark(String chargeStationRemark) {
        this.chargeStationRemark = chargeStationRemark;
    }

    public String getGarageRemark() {
        return garageRemark;
    }

    public void setGarageRemark(String garageRemark) {
        this.garageRemark = garageRemark;
    }

    public String getReplaceStationRemark() {
        return replaceStationRemark;
    }

    public void setReplaceStationRemark(String replaceStationRemark) {
        this.replaceStationRemark = replaceStationRemark;
    }

    public String getHotelRemark() {
        return hotelRemark;
    }

    public void setHotelRemark(String hotelRemark) {
        this.hotelRemark = hotelRemark;
    }

    public String getPoliceStation() {
        return policeStation;
    }

    public void setPoliceStation(String policeStation) {
        this.policeStation = policeStation;
    }

    public String getMtoilet() {
        return mtoilet;
    }

    public void setMtoilet(String mtoilet) {
        this.mtoilet = mtoilet;
    }

    public String getWtoilet() {
        return wtoilet;
    }

    public void setWtoilet(String wtoilet) {
        this.wtoilet = wtoilet;
    }

    public String getBoilerRoom() {
        return boilerRoom;
    }

    public void setBoilerRoom(String boilerRoom) {
        this.boilerRoom = boilerRoom;
    }

    public String getEmergencyRoom() {
        return emergencyRoom;
    }

    public void setEmergencyRoom(String emergencyRoom) {
        this.emergencyRoom = emergencyRoom;
    }

    @Override
    public String toString() {
        return "ServiceArea{" +
                "serviceareaId='" + serviceareaId + '\'' +
                ", serviceareaCode='" + serviceareaCode + '\'' +
                ", serviceareaName='" + serviceareaName + '\'' +
                ", serviceareaType='" + serviceareaType + '\'' +
                ", stakeNum='" + stakeNum + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", roadId='" + roadId + '\'' +
                ", roadName='" + roadName + '\'' +
                ", areaSize='" + areaSize + '\'' +
                ", buildTime='" + buildTime + '\'' +
                ", firstManageUnit='" + firstManageUnit + '\'' +
                ", secondManageUnit='" + secondManageUnit + '\'' +
                ", thirdManageUnit='" + thirdManageUnit + '\'' +
                ", managerName='" + managerName + '\'' +
                ", managerPhone='" + managerPhone + '\'' +
                ", introduce='" + introduce + '\'' +
                ", area='" + area + '\'' +
                ", direction_id='" + direction_id + '\'' +
                ", directionName='" + directionName + '\'' +
                ", status='" + status + '\'' +
                ", oilStation='" + oilStation + '\'' +
                ", gasStation='" + gasStation + '\'' +
                ", hStation='" + hStation + '\'' +
                ", chargeStation='" + chargeStation + '\'' +
                ", chargePileCount='" + chargePileCount + '\'' +
                ", chargeGunCount='" + chargeGunCount + '\'' +
                ", chargePower='" + chargePower + '\'' +
                ", cafeteria='" + cafeteria + '\'' +
                ", streetFood='" + streetFood + '\'' +
                ", supermarket='" + supermarket + '\'' +
                ", hotel='" + hotel + '\'' +
                ", wifi='" + wifi + '\'' +
                ", garage='" + garage + '\'' +
                ", thirdToilet='" + thirdToilet + '\'' +
                ", mbRoom='" + mbRoom + '\'' +
                ", menCount='" + menCount + '\'' +
                ", womenCount='" + womenCount + '\'' +
                ", carParkCount='" + carParkCount + '\'' +
                ", busParkCount='" + busParkCount + '\'' +
                ", truckParkCount='" + truckParkCount + '\'' +
                ", dangerParkCount='" + dangerParkCount + '\'' +
                ", nEnergyParkCount='" + nEnergyParkCount + '\'' +
                ", dirveHome='" + dirveHome + '\'' +
                ", orgId='" + orgId + '\'' +
                ", dirCode='" + dirCode + '\'' +
                ", searchContent='" + searchContent + '\'' +
                ", replaceStation='" + replaceStation + '\'' +
                ", statusRemark='" + statusRemark + '\'' +
                ", oilStationRemrk='" + oilStationRemrk + '\'' +
                ", gasStationRemark='" + gasStationRemark + '\'' +
                ", hStationRemark='" + hStationRemark + '\'' +
                ", chargeStationRemark='" + chargeStationRemark + '\'' +
                ", garageRemark='" + garageRemark + '\'' +
                ", replaceStationRemark='" + replaceStationRemark + '\'' +
                ", hotelRemark='" + hotelRemark + '\'' +
                ", policeStation='" + policeStation + '\'' +
                ", mtoilet='" + mtoilet + '\'' +
                ", wtoilet='" + wtoilet + '\'' +
                ", boilerRoom='" + boilerRoom + '\'' +
                ", emergencyRoom='" + emergencyRoom + '\'' +
                '}';
    }
}
