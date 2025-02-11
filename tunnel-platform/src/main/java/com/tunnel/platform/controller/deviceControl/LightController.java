package com.tunnel.platform.controller.deviceControl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.platform.service.SdOptDeviceService;
import com.tunnel.platform.service.deviceControl.LightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "照明控制")
@RestController
@RequestMapping("/light")
@ApiSupport(order = 16)
public class LightController {

    @Value("${authorize.name}")
    private String deploymentType;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    @Autowired
    private LightService lightService;

    @Autowired
    private SdOptDeviceService sdOptDeviceService;


//    /**
//     * 调整亮度
//     *
//     * @param deviceId 设备ID
//     * @param bright   调光值(0~100 之间的整数)
//     * @throws IOException
//     */
//    @GetMapping(value = "/setBrightness")
//    @ApiOperation(value = "调整亮度", httpMethod = "GET")
//    public AjaxResult setBrightness(
//            @ApiParam(name = "deviceId", value = "设备ID", required = true) @RequestParam(name = "deviceId") String deviceId,
//            @ApiParam(name = "bright", value = "调光值(0~100 之间的整数)", required = true) @RequestParam(name = "bright") Integer bright,
//            @ApiParam(name = "controlType", value = "控制类型(0-手动控制,1-定时控制,4-预案执行)", required = true) @RequestParam(name = "controlType") String controlType,
//            @ApiParam(name = "operIp", value = "操作者IP,如果部署在高速云，此参数必传！", required = false) @RequestParam(name = "operIp", required = false) String operIp) throws IOException {
//        // 如果是部署在高速云，校验Ip地址参数
//        if ("GSY".equals(deploymentType)) {
//            // Assert.hasText(operIp, "操作方IP地址参数{operIp}必传");
//            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("deviceId", deviceId);
//            paramMap.put("bright", bright);
//            paramMap.put("controlType", controlType);
//            paramMap.put("operIp", operIp);
//
//            SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);
//            SdTunnels tunnel = sdTunnelsService.selectSdTunnelsById(sdDevices.getEqTunnelId());
//            //设备所属管理站host
//            String host = sdOptDeviceService.getGlzHost(String.valueOf(tunnel.getDeptId()));
//            //接口地址
//            String url = host + "/light/setBrightness";
//            String response = HttpUtil.get(url, paramMap);
//            if (StringUtils.isNotBlank(response) && JSONValidator.from(response).validate()) {
//                JSONObject jsonObject = JSONObject.parseObject(response);
//                jsonObject.containsKey("code");
//                if (200 == jsonObject.getInteger("code")) {
//                    return AjaxResult.success();
//                }
//            }
//            return AjaxResult.error();
//        }
//
//        operIp = InetAddress.getLocalHost().getHostAddress();
//        int resultStatus = lightService.setBrightness(deviceId, bright, controlType, operIp);
//
//        return resultStatus == 1 ? AjaxResult.success() : AjaxResult.error();
//    }

//    /**
//     * 开关灯
//     *
//     * @param deviceId  设备ID
//     * @param openClose 开关值（1：表示开，0：表示关）
//     * @throws IOException
//     */
//    @GetMapping(value = "/lineControl")
//    @ApiOperation(value = "控制开和关", httpMethod = "GET")
//    public AjaxResult lineControl(
//            @ApiParam(name = "deviceId", value = "设备ID", required = true) @RequestParam(name = "deviceId") String deviceId,
//            @ApiParam(name = "openClose", value = "状态（1-开启，0-关闭）", required = true) @RequestParam(name = "openClose") Integer openClose,
//            @ApiParam(name = "controlType", value = "控制类型(0-手动控制,1-定时控制,4-预案执行)", required = true) @RequestParam(name = "controlType") String controlType,
//            @ApiParam(name = "operIp", value = "操作者IP,如果部署在高速云，此参数必传！", required = false) @RequestParam(name = "operIp", required = false) String operIp) throws IOException {
//        operIp = InetAddress.getLocalHost().getHostAddress();
//        int resultStatus = lightService.lineControl(deviceId, openClose, controlType, operIp);
//
//        return resultStatus == 1 ? AjaxResult.success() : AjaxResult.error();
//    }

}
