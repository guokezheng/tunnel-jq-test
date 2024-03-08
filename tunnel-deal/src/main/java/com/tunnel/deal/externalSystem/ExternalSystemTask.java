package com.tunnel.deal.externalSystem;


import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.ExternalSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * 外部系统-定时任务类
 */
@Component("ExternalSystemTask")
public class ExternalSystemTask {


    @Autowired
    ExternalSystemMapper externalSystemMapper;


    /**
     * 检测网络状态
     */
    //@Scheduled(cron="0/5 * * * * ?")
    public void selectExternalSystemTaskStatus(){
        List<ExternalSystem> externalSystemList = selectExternalSystemCheckList();
        externalSystemList.stream().forEach(item -> {
            try {
                //更新系统状态
                boolean reachable = InetAddress.getByName(item.getCheckNetworkUrl()).isReachable(item.getCheckNetworkTimeout());
                if(reachable != true){
                    externalSystemMapper.updateExternalSystemNetWorkStatus(item.getId(), DevicesStatusEnum.DEVICE_ERROR.getCode());
                }else {
                    externalSystemMapper.updateExternalSystemNetWorkStatus(item.getId(), DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<ExternalSystem> selectExternalSystemCheckList(){
        return externalSystemMapper.selectExternalSystemCheckList();
    }
}
