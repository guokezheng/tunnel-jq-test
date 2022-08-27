package com.zc.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.zc.domain.dataShareDto.*;
import com.zc.service.DataShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record")
@Api(tags = "数据中台")
public class DataShareController {

    @Autowired
    DataShareService dataShareService;

    /**
     * 气象设备环境数据查询
     * @param meteorologicalEquipmentDto
     * @return
     */
    @PostMapping("/queryMeteorologicalEquipment")
    @ApiOperation("气象设备环境数据查询")
    public Result queryMeteorologicalEquipment(@RequestBody MeteorologicalEquipmentDto meteorologicalEquipmentDto)  {
        return Result.toResult((Integer) dataShareService.queryMeteorologicalEquipment(meteorologicalEquipmentDto));
    }

    /**
     * 能见度设备数据查询
     * @param visibilityDeviceDto
     * @return
     */
    @PostMapping("/queryVisibilityDevice")
    @ApiOperation("能见度设备数据查询")
    public Result queryVisibilityDevice(@RequestBody VisibilityDeviceDto visibilityDeviceDto)  {
        return Result.toResult((Integer) dataShareService.queryVisibilityDevice(visibilityDeviceDto));
    }

    /**
     * 桥梁明细查询
     * @param bridgeDataDto
     * @return
     */
    @PostMapping("/queryBridgeData")
    @ApiOperation("桥梁明细查询")
    public Result queryBridgeData(@RequestBody BridgeDataDto bridgeDataDto)  {
        return Result.toResult((Integer)dataShareService.queryBridgeData(bridgeDataDto));
    }

    /**
     * 枢纽立交信息查询
     * @param hingeInterchangeDto
     * @return
     */
    @PostMapping("/queryHingeInterchange")
    @ApiOperation("枢纽立交信息查询")
    public Result queryHingeInterchange(@RequestBody HingeInterchangeDto hingeInterchangeDto)  {
        return Result.toResult((Integer)dataShareService.queryHingeInterchange(hingeInterchangeDto));
    }

    /**
     * 服务区详情查询
     * @param serviceAreaDto
     * @return
     */
    @PostMapping("/queryServiceArea")
    @ApiOperation("服务区详情查询")
    public Result queryServiceArea(@RequestBody ServiceAreaDto serviceAreaDto)  {
        return Result.toResult((Integer)dataShareService.queryServiceArea(serviceAreaDto));
    }

    /**
     * 收费车道信息查询
     * @param chargeVehicleLaneDto
     * @return
     */
    @PostMapping("/queryChargeVehicleLane")
    @ApiOperation("收费车道信息查询")
    public Result queryChargeVehicleLane(@RequestBody ChargeVehicleLaneDto chargeVehicleLaneDto)  {
        return Result.toResult((Integer)dataShareService.queryChargeVehicleLane(chargeVehicleLaneDto));
    }

    /**
     * 收费站车流量信息
     * @param tollStatTrafficFlowDto
     * @return
     */
    @PostMapping("/queryTollStatTrafficFlow")
    @ApiOperation("收费站车流量信息")
    public Result queryTollStatTrafficFlow(@RequestBody TollStatTrafficFlowDto tollStatTrafficFlowDto)  {
        return Result.toResult((Integer)dataShareService.queryTollStatTrafficFlow(tollStatTrafficFlowDto));
    }

    /**
     * 微波车检器周期过车数据查询
     * @param microwaveCarDetectorDto
     * @return
     */
    @PostMapping("/queryMicrowaveCarDetector")
    @ApiOperation("微波车检器周期过车数据查询")
    public Result queryMicrowaveCarDetector(@RequestBody MicrowaveCarDetectorDto microwaveCarDetectorDto)  {
        return Result.toResult((Integer)dataShareService.queryMicrowaveCarDetector(microwaveCarDetectorDto));
    }

    /**
     * 微波车检器周期过车数据查询2
     * @param microwaveDataDto
     * @return
     */
    @PostMapping("/queryMicrowaveData")
    @ApiOperation("微波车检器周期过车数据查询2")
    public Result queryMicrowaveData(@RequestBody MicrowaveDataDto microwaveDataDto)  {
        return Result.toResult((Integer)dataShareService.queryMicrowaveData(microwaveDataDto));
    }

}
