package com.tunnel.webthings.service;

import com.tunnel.webthings.domain.DeviceParameter;
import com.tunnel.webthings.vo.ResponseVO;

import java.util.List;

/**
 * @author dzy
 * @date 2022/8/22 17:20
 */
public interface GetDeviceService {

    List<ResponseVO> getDevList(DeviceParameter dev);

    void copyData();
}
