package com.tunnel.platform.service.deviceControl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.phone.impl.LiDianPhoneSpeak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhai
 * @date 2023/6/9
 */
@Component
public class LiDianUdpControl implements GeneralControlBean {

    @Autowired
    private LiDianPhoneSpeak liDianPhoneSpeak;

    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        liDianPhoneSpeak.playVoice(map,sdDevices);
        return AjaxResult.success();
    }

    @Override
    public Integer controlDevices(Map<String, Object> map) {
        return null;
    }

    /*@Override
    public Integer analogControl(Map<String, Object> map, SdDevices sdDevices) {
        return null;
    }*/
}
