package com.tunnel.webthings.service;

import com.tunnel.webthings.vo.SdActiveLuminousSignsVO;
import com.tunnel.webthings.vo.SdConfluenceDevFaultWarnVO;
import com.tunnel.webthings.vo.SdRadarMsgTopicVO;
import com.tunnel.webthings.vo.SdStateStorageVO;

/**
 * @author ZHC
 * {@code @date} 2022/7/18 9:31
 * 隧道物联设备Service接口
 */
public interface TunnelIotDeviceService {

    /**
     * 查询设备类型
     * @param devType
     * @return
     */
    String selectIotDeviceType(String devType);

    /**
     * 添加主动发光标志数据控制信息
     * @param vo
     * @return
     */
    int addActiveLuminousSigns(SdActiveLuminousSignsVO vo);

    /**
     *添加合流区预警设备故障告警
     * @param vo
     * @return
     */
    int addConfluenceDevFaultWarn(SdConfluenceDevFaultWarnVO vo);

    /**
     * 添加雷达信息数据
     * @param vo
     * @return
     */
    int addRadarMag(SdRadarMsgTopicVO vo);

    /**
     * 添加设备状态更改信息
     * @param vo
     * @return
     */
    int addStateStorage(SdStateStorageVO vo);

    void method();
}
