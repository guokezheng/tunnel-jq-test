package com.tunnel.platform.business.vms;

import com.tunnel.business.domain.informationBoard.IotDeviceProtocol;
import com.tunnel.business.service.informationBoard.IIotDeviceProtocolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 驱动配置类
 *
 * @author Alvin
 * @date 2018年7月13日
 */
@Component
public class DriverConfig {


    @Autowired
    private IIotDeviceProtocolService iotDeviceProtocolService;
    private static DriverConfig driverConfig;

    @PostConstruct
    public void init() {
        driverConfig = this;
        driverConfig.iotDeviceProtocolService = this.iotDeviceProtocolService;
    }

    /**
     * 根据设备的ip获取设备的配置信息
     *
     * @return
     */
    public DriverBean getDriverById(String deviceProtocol) {
        List<IotDeviceProtocol> protocolFunctionByName = driverConfig.iotDeviceProtocolService.getProtocolFunctionByName(deviceProtocol);
        Map<String, String> rulesMap = new HashMap<String, String>();
        Map<String, String> commandMap = new HashMap<String, String>();
        DriverBean driverBean = null;
        if (protocolFunctionByName.size() > 0) {
            for (IotDeviceProtocol deviceProtocol1 : protocolFunctionByName) {
                driverBean = new DriverBean();
                String command = deviceProtocol1.getProtocolFunction();
                String param = deviceProtocol1.getProtocolParam();
                if ("FAULT_CALLBACK".equals(command)) {
                    rulesMap.put(command, param);
                } else {
                    commandMap.put(command, param);
                }
                driverBean.setCommands(commandMap);
                driverBean.setRules(rulesMap);
                driverBean.setVender_version(deviceProtocol1.getProtocolName());
            }
        }
        return driverBean;
    }

}