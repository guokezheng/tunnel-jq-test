package com.ruoyi.quartz.task;

import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.deal.enums.DeviceProtocolCodeEnum;
import com.tunnel.deal.guidancelamp.control.util.GuidanceLampHandle;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 诱导标同步
 * @author zhai
 * @date 2023/7/10
 */
@Component("InduceTask")
public class InduceTask {

    @Autowired
    private TcpClientGeneralService tcpClientGeneralService;

    /**
     * 诱导标同步
     */
    @Scheduled(fixedRate = 300000)
    public void syncInduce(){
        List<SdDevices> devicesList = tcpClientGeneralService.getDevicesList(DeviceProtocolCodeEnum.XIANKE_INDUCTION_LAMP_PROTOCOL_code.getCode(), DevicesTypeEnum.YOU_DAO_DENG_CONTROL.getCode());
        try{
            for(SdDevices item : devicesList){
                GuidanceLampHandle.getInstance().syncYdd(item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
