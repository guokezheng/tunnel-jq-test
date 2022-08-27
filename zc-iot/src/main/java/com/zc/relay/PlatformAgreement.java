package com.zc.relay;

import com.zc.iot.domain.IotDevice;

/**
 * 物联网平台端到业务平台端接口
 * 约定物联网平台到业务平台接口
 */
public interface PlatformAgreement
{
    /**
     * 设备状态变化通知
     * @return 设备对象
     */
    IotDevice deviceStateNotify();

    /**
     * 设备生命周期变化
     * @return
     */
    IotDevice lifecycleChange();
}
