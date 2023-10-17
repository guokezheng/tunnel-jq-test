package com.tunnel.business.service.vehicle.impl;

import com.tunnel.business.domain.event.SdEventType;
import com.tunnel.business.domain.event.SdTrafficVolume;
import com.tunnel.business.mapper.digitalmodel.SdTrafficVolumeMapper;
import com.tunnel.business.service.vehicle.SdTrafficVolumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhai
 * @date 2023/10/Sd
 */
@Service
public class SdTrafficVolumeServiceImpl implements SdTrafficVolumeService {

    @Autowired
    private SdTrafficVolumeMapper trafficVolumeMapper;

    @Override
    public List<SdTrafficVolume> selectTrafficVolumeList(SdTrafficVolume sdTrafficVolume) {
        if("3".equals(sdTrafficVolume.getDirection())){
            sdTrafficVolume.setDirection(null);
        }
        List<SdTrafficVolume> sdTrafficVolumes = trafficVolumeMapper.selectSdTrafficVolumeList(sdTrafficVolume);
        return sdTrafficVolumes;
    }

    @Override
    public List<SdTrafficVolume> exportTrafficVolumeList(SdTrafficVolume sdTrafficVolume) {
        if("3".equals(sdTrafficVolume.getDirection())){
            sdTrafficVolume.setDirection(null);
        }
        List<SdTrafficVolume> sdTrafficVolumes = trafficVolumeMapper.exportSdTrafficVolumeList(sdTrafficVolume);
        return sdTrafficVolumes;
    }
}
