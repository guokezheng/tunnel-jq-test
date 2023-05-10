package com.tunnel.deal.mca;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.mca.service.McaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * describe: MCA控制类
 *
 * @author zs
 * @date 2023/4/4
 */
@Component
public class McaControl implements GeneralControlBean {

    @Autowired
    private McaService mcaService;

//    /**
//     * 控制方法
//     *
//     * @param sdDevices 设备信息
//     * @param state     控制状态
//     * @return
//     */
//    @Override
//    public AjaxResult control(SdDevices sdDevices, String state) {
//        return null;
//    }
//
//    @Override
//    public AjaxResult control(Map<String, Object> map) {
//        return null;
//    }

    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {

        String deviceId = sdDevices.getEqId();
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        return mcaService.control(deviceId,state);
    }
}
