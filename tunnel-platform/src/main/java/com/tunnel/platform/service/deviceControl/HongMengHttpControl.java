package com.tunnel.platform.service.deviceControl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * describe: 鸿蒙控制器控制-杭山东
 *
 * @author zs
 * @date 2023/4/4
 */
@Component
public class HongMengHttpControl implements GeneralControlBean {


    @Autowired
    private HongMengDevService hongMengDevService;


//    /**
//     * 控制方法
//     *
//     * @param sdDevices 设备信息
//     * @param state     控制状态
//     * @return
//     */
//    @Override
//    public AjaxResult control(SdDevices sdDevices, String state) {
//        String devId = sdDevices.getEqId();
//        Map<String, String> hongMap = hongMengDevService.updateHua(devId, state);
//        Integer code = Integer.valueOf(hongMap.get("code"));
//        String msg = hongMap.get("msg");
//        if(code == 200){
//            return AjaxResult.success(1);
//        }else {
//            return AjaxResult.success(msg,0);
//        }
//    }

//    @Override
    public AjaxResult control(Map<String, Object> map) {
        //设备ID
        String devId = Optional.ofNullable(map.get("devId")).orElse("").toString();

//        if (devId == null || "".equals(devId)) {
//            AjaxResult.error("未指定设备");
//        }
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        //设备控制
        Map<String, String> hongMap = hongMengDevService.updateHua(devId, state);
        Integer code = Integer.valueOf(hongMap.get("code"));
        String msg = hongMap.get("msg");
        if(code == 200){
            return AjaxResult.success(1);
        }else {
            return AjaxResult.error(msg,0);
        }
    }

    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        return control(map);
    }
}
