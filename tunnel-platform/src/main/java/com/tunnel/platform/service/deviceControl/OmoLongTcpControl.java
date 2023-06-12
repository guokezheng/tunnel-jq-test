package com.tunnel.platform.service.deviceControl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.platform.service.deviceFunctions.DeviceFunctionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
/**
 * describe: 欧姆龙协议控制-杭山东
 *
 * @author hyw
 * @date 2023/6/8
 */
@Component
public class OmoLongTcpControl implements GeneralControlBean {

    @Autowired
    private DeviceFunctionsService deviceFunctionsService;

    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        return null;
    }

    @Override
    public Integer controlDevices(Map<String, Object> map) {
        boolean  b = deviceFunctionsService.deviceControlByParam( map.get("comType").toString(), map.get("eqId").toString(), map.get("data").toString());
        return b ? 1 : 0;
    }

    @Override
    public Integer analogControl(Map<String, Object> map, SdDevices sdDevices) {
        return null;
    }
}
