package com.tunnel.business.service.vehicle;

import com.tunnel.business.domain.event.SdTrafficVolume;

import java.util.List;

/**
 * @author zhai
 * @date 2023/10/16
 */
public interface SdTrafficVolumeService {

    /**
     * 查询车流量列表
     * @param sdTrafficVolume
     * @return
     */
    List<SdTrafficVolume> selectTrafficVolumeList(SdTrafficVolume sdTrafficVolume);

    /**
     * 导出车流量列表
     * @param sdTrafficVolume
     * @return
     */
    List<SdTrafficVolume> exportTrafficVolumeList(SdTrafficVolume sdTrafficVolume);

}
