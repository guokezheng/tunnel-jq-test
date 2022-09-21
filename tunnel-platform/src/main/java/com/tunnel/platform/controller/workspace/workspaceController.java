package com.tunnel.platform.controller.workspace;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDeviceCmdService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.deal.plc.modbus.ModbusTcpHandle;
import com.zc.common.core.redis.pubsub.RedisPubSub;
import com.zc.common.core.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作台
 *
 * @author
 * @date 2020-08-24
 */
@RestController
@RequestMapping("/workspace")
public class workspaceController extends BaseController {
    @Autowired
    private ISdEventService sdEventService;
    @Autowired
    private ISdDeviceCmdService sdDeviceCmdService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private static RedisCache redisCache;
    @Autowired
    private ISdTunnelsService sdTunnelsService;


    //3d测试
    @PostMapping("/test")
    public String test() {
        JSONObject object = new JSONObject();
        object.put("dataList", 11111);
        WebSocketService.broadcast("dataList", object);
        return "get 3d info";
    }

    @PostMapping("/controlDevice")
    public void controlDevice(String devId, String devType, String brightness, String frequency, String state, String tunnelId) {
        Map<String, Object> instruction = new HashMap<>();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
        //设备id
        instruction.put("deviceId", devId);
        instruction.put("ctrState", state);
        if (devType != null && devType.equals(DevicesTypeEnum.YOU_DAO_DENG.getCode().toString())) {
            instruction.put("brightness", brightness);
            instruction.put("frequency", frequency);
            SpringUtils.getBean(RedisPubSub.class).publish("GL:CONTROL", JSON.toJSONString(instruction));
        } else {
            ModbusTcpHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
        }
    }


    /**
     * 根据隧道id,方向,所属车道筛选车道指示器
     *
     * @param sdDevices
     * @return
     */
    @PostMapping("/updateCarFingerById")
    public AjaxResult updateCarFingerById(@RequestBody List<SdDevices> sdDevices) {
        return AjaxResult.success(sdDevicesService.updateCarFingerById(sdDevices));
    }
}
