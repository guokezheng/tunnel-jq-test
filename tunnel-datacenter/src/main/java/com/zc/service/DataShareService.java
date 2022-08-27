package com.zc.service;

import com.zc.domain.dataShareDto.*;


public interface DataShareService {

    /**
     * 气象设备环境数据查询
     * @param meteorologicalEquipmentDto
     * @return
     */
    public Object queryMeteorologicalEquipment(MeteorologicalEquipmentDto meteorologicalEquipmentDto);

    /**
     * 能见度设备数据查询
     * @param visibilityDeviceDto
     * @return
     */
    public Object queryVisibilityDevice(VisibilityDeviceDto visibilityDeviceDto);

    /**
     * 桥梁明细查询
     * @param bridgeDataDto
     * @return
     */
    public Object queryBridgeData(BridgeDataDto bridgeDataDto);

    /**
     * 枢纽立交信息查询
     * @param hingeInterchangeDto
     * @return
     */
    public Object queryHingeInterchange(HingeInterchangeDto hingeInterchangeDto);

    /**
     * 服务区详情查询
     * @param serviceAreaDto
     * @return
     */
    public Object queryServiceArea(ServiceAreaDto serviceAreaDto);

    /**
     * 收费车道信息查询
     * @param chargeVehicleLaneDto
     * @return
     */
    public Object queryChargeVehicleLane(ChargeVehicleLaneDto chargeVehicleLaneDto);

    /**
     * 收费站车流量信息
     * @param tollStatTrafficFlowDto
     * @return
     */
    public Object queryTollStatTrafficFlow(TollStatTrafficFlowDto tollStatTrafficFlowDto);

    /**
     * 微波车检器周期过车数据查询
     * @param microwaveCarDetectorDto
     * @return
     */
    public Object queryMicrowaveCarDetector(MicrowaveCarDetectorDto microwaveCarDetectorDto);

    /**
     * 微波车检器周期过车数据查询2
     * @param microwaveDataDto
     * @return
     */
    public Object queryMicrowaveData(MicrowaveDataDto microwaveDataDto);
}
