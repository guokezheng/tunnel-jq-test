package com.zc.relay.handler.uplink;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.zc.common.constant.RedisKeyConstants;
import com.zc.iot.domain.IotCategory;
import com.zc.iot.domain.IotDevice;
import com.zc.iot.domain.IotProduct;
import com.zc.iot.service.IIotDeviceService;
import com.zc.iot.service.impl.IotDeviceServiceImpl;
import com.zc.relay.dto.UplinkDeviceData;
import com.zc.relay.dto.UplinkDeviceStateData;
import com.zc.relay.dto.UplinkFunctionData;
import com.zc.relay.dto.UplinkOperationStateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 上行指令
 * 约定了数据解析到物模型的接口
 */
@Service
public class UplinkInstructHandleImpl implements UplinkInstructHandle
{
    private static final Logger log = LoggerFactory.getLogger(UplinkInstructHandleImpl.class);

    @Resource
    private IIotDeviceService iIotDeviceService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    /**
     * 设备状态变化通知
     * @param uplinkDeviceStateData 设备状态数据
     */
    @Override
    public void uplinkDeviceState(UplinkDeviceStateData uplinkDeviceStateData)
    {

        if (uplinkDeviceStateData == null)
        {
            log.error("设备状态变化通知：{输出条件：(uplinkDeviceStateData == null)}");
            return;
        }

        // 设备编号
        String deviceCode = uplinkDeviceStateData.getDeviceCode();

        // 在线状态
        Integer state = uplinkDeviceStateData.getState();

        if (deviceCode == null || state == null)
        {
            log.error("设备状态变化通知：{输出条件：(deviceCode == null || state == null) }");
            return;
        }

        // 分配新线程执行
        threadPoolTaskExecutor.execute(() ->
        {

            // 根据设备编号从缓存中获取设备主键信息
            Long id = redisCache.getCacheMapValue(RedisKeyConstants.IOT_DEVICE_CODE_ID, deviceCode);

            if (id == null)
            {
                log.error("设备状态变化通知：{无法根据设备编号从“设备编号id映射”缓存中获取设备id；" +
                        "缓存的常量名称：IOT_DEVICE_CODE_ID；输出条件：(id == null)；设备编号deviceCode：" + deviceCode + "}");
                return;
            }

            List<IotDevice> iotDevices = new ArrayList<>();

            // 根据主键从缓存中获取设备信息
            IotDevice iotDevice = redisCache.getCacheMapValue(RedisKeyConstants.IOT_DEVICE, id);

            if (iotDevice == null)
            {
                log.error("设备状态变化通知：{根据设备id冲缓存中获取设备信息；缓存中没有此数据；" +
                        "输出条件：(iotDevice == null) ；缓存常量名称：IOT_DEVICE; 设备id："+ id +"}");
                return;
            }

            iotDevices.add(iotDevice);

            IotDevice iotDevice1 = new IotDevice();
            iotDevice1.setGatewayDevice(iotDevice.getGatewayDevice());

            // 获取所有的子设备
            List<IotDevice> iotDeviceList = iIotDeviceService.selectIotDeviceList(iotDevice1);

            if (iotDeviceList != null && !iotDeviceList.isEmpty())
            {
                iotDevices.addAll(iotDeviceList);
            }

            iotDevices.forEach(device ->
            {
                // 设置在线状态
                device.setOnlineState(state);

                // 更新数据库和缓存
                iIotDeviceService.updateIotDevice(device);

            });
        });

    }

    /**
     * 设备信息上报
     * @param uplinkDeviceData 上报数据设备实时数据
     */
    @Override
    public void uplinkDeviceMsg(UplinkDeviceData uplinkDeviceData)
    {
        if (uplinkDeviceData == null)
        {
            log.error("设备信息上报：{输出条件：(uplinkDeviceData == null)}");
            return;
        }

        String deviceCode = uplinkDeviceData.getDeviceCode();
        List<UplinkFunctionData> uplinkFunctionDataList = uplinkDeviceData.getUplinkFunctionData();

        if (StringUtils.isEmpty(deviceCode) || uplinkFunctionDataList == null || uplinkFunctionDataList.isEmpty())
        {
            log.error("设备信息上报：{输出条件：" +
                    "(StringUtils.isEmpty(deviceCode) || uplinkFunctionDataList == null || uplinkFunctionDataList.isEmpty())}");
            return;
        }

        // 根据设备编号从缓存中获取设备id
        Long deviceId = redisCache.getCacheMapValue(RedisKeyConstants.IOT_DEVICE_CODE_ID, deviceCode);

        if (deviceId == null)
        {
            log.error("设备信息上报：{输出条件：(deviceId == null); 根据 设备编号没能从缓存中获取到设备id；缓存常量：IOT_DEVICE_CODE_ID}");
            return;
        }

        // 根据设备id从缓存中获取设备详细信息
        IotDevice iotDevice = redisCache.getCacheMapValue(RedisKeyConstants.IOT_DEVICE, deviceId);

        if (iotDevice == null)
        {
            log.error("设备信息上报：{输出条件：(iotDevice == null); 根据 设备id获取从缓存中无法获取设备详细信息；缓存常量：IOT_DEVICE}");
            return;
        }

        IotProduct iotProduct = redisCache.getCacheMapValue(RedisKeyConstants.IOT_PRODUCT, iotDevice.getProductKey());

        uplinkFunctionDataList.forEach(uplinkFunctionData ->
        {
            String functionIdentifier = uplinkFunctionData.getIdentifier();
            Number realValue = uplinkFunctionData.getRealValue();

            if (StringUtils.isEmpty(functionIdentifier) || realValue == null)
            {
                return;
            }
        });



        // 根据设备id和功能id获取缓存中的实时数据

    }

    /**
     * 上报操作状态
     * @param uplinkOperationStateData 操作响应数据
     */
    @Override
    public void uplinkOperationState(UplinkOperationStateData uplinkOperationStateData)
    {

    }
}
