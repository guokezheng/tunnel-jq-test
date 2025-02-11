package com.zc.relay.handler.down;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.zc.common.constant.RedisChannelConstants;
import com.zc.common.core.redis.pubsub.RedisPubSub;
import com.zc.relay.dto.DownData;
import com.zc.relay.dto.DownDataChildDevice;
import com.zc.relay.dto.DownSetData;
import com.zc.relay.dto.DownSetDataChildDevice;

import java.lang.reflect.Type;

/**
 * 功能调试，发布数据到 redis
 * @author Athena-xiepufeng
 */
public class DownInstructRedisPubHandleImpl implements DownInstructHandle
{
    private static final RedisPubSub redisPubSub = SpringUtils.getBean(RedisPubSub.class);

    /**
     * 网关设备下行设置属性（发布数据到 redis）
     * @param downSetData 下行指令数据
     * @return
     */
    @Override
    public boolean setAttribute(DownSetData<Number> downSetData)
    {
        if (downSetData == null)
        {
            return false;
        }

        Number value = downSetData.getValue();
        String deviceCode = downSetData.getDeviceCode();
        String functionIdentify = downSetData.getFunctionIdentify();

        if (value == null || deviceCode == null || functionIdentify == null)
        {
            return false;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<DownSetData<Number>>()
        {
        }.getType();

        String msg = gson.toJson(downSetData, type);

        redisPubSub.publish(RedisChannelConstants.IOT_SET_ATTRIBUTE, msg);

        return true;
    }

    /**
     * 子设备下行设置属性（发布数据到 redis）
     * @param downSetDataChildDevice 下行指令数据
     * @return
     */
    @Override
    public boolean setAttribute(DownSetDataChildDevice<Number> downSetDataChildDevice)
    {

        if (downSetDataChildDevice == null)
        {
            return false;
        }

        Number value = downSetDataChildDevice.getValue();
        String deviceCode = downSetDataChildDevice.getDeviceCode();
        String childDeviceCode = downSetDataChildDevice.getChildDeviceCode();
        String functionIdentify = downSetDataChildDevice.getFunctionIdentify();

        if (value == null
                || deviceCode == null
                || childDeviceCode == null
                || functionIdentify == null)
        {
            return false;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<DownSetDataChildDevice<Number>>()
        {
        }.getType();

        String msg = gson.toJson(downSetDataChildDevice, type);

        redisPubSub.publish(RedisChannelConstants.IOT_SET_ATTRIBUTE_CHILD_DEVICE, msg);

        return true;

    }

    /**
     * 网关设备下行获取属性（发布数据到 redis）
     * @param downData 下行指令数据
     * @return
     */
    @Override
    public boolean getAttribute(DownData downData)
    {

        if (downData == null)
        {
            return false;
        }

        String deviceCode = downData.getDeviceCode();
        String functionIdentify = downData.getFunctionIdentify();

        if (deviceCode == null || functionIdentify == null)
        {
            return false;
        }


        Gson gson = new Gson();
        Type type = new TypeToken<DownData>()
        {
        }.getType();

        String msg = gson.toJson(downData, type);

        redisPubSub.publish(RedisChannelConstants.IOT_GET_ATTRIBUTE, msg);

        return true;
    }

    /**
     * 子设备下行获取属性（发布数据到 redis）
     * @param downDataChildDevice 下行指令数据
     * @return
     */
    @Override
    public boolean getAttribute(DownDataChildDevice downDataChildDevice)
    {

        if (downDataChildDevice == null)
        {
            return false;
        }

        String deviceCode = downDataChildDevice.getDeviceCode();
        String childDeviceCode = downDataChildDevice.getChildDeviceCode();
        String functionIdentify = downDataChildDevice.getFunctionIdentify();

        if (deviceCode == null || childDeviceCode == null || functionIdentify == null)
        {
            return false;
        }


        Gson gson = new Gson();
        Type type = new TypeToken<DownDataChildDevice>()
        {
        }.getType();

        String msg = gson.toJson(downDataChildDevice, type);

        redisPubSub.publish(RedisChannelConstants.IOT_GET_ATTRIBUTE_CHILD_DEVICE, msg);

        return true;
    }


    /**
     * 网关设备下行服务调用（发布数据到 redis）
     * @param downSetData 下行指令数据
     * @return
     */
    @Override
    public boolean serveInvoke(DownSetData<JSONObject> downSetData)
    {

        if (downSetData == null)
        {
            return false;
        }

        JSONObject value = downSetData.getValue();
        String deviceCode = downSetData.getDeviceCode();
        String functionIdentify = downSetData.getFunctionIdentify();


        if (value == null
                || value.isEmpty()
                || deviceCode == null
                || functionIdentify == null)
        {
            return false;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<DownSetData<JsonObject>>()
        {
        }.getType();

        String msg = gson.toJson(downSetData, type);

        redisPubSub.publish(RedisChannelConstants.IOT_SERVE_INVOKE, msg);

        return false;
    }

    /**
     * 子设备下行服务调用（发布数据到 redis）
     * @param downSetDataChildDevice 下行指令数据
     * @return
     */
    @Override
    public boolean serveInvoke(DownSetDataChildDevice<JSONObject> downSetDataChildDevice)
    {

        if (downSetDataChildDevice == null)
        {
            return false;
        }

        JSONObject value = downSetDataChildDevice.getValue();
        String deviceCode = downSetDataChildDevice.getDeviceCode();
        String childDeviceCode = downSetDataChildDevice.getChildDeviceCode();
        String functionIdentify = downSetDataChildDevice.getFunctionIdentify();


        if (value == null
                || value.isEmpty()
                || deviceCode == null
                || childDeviceCode == null
                || functionIdentify == null)
        {
            return false;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<DownSetDataChildDevice<JsonObject>>()
        {
        }.getType();

        String msg = gson.toJson(downSetDataChildDevice, type);

        redisPubSub.publish(RedisChannelConstants.IOT_SERVE_INVOKE_CHILD_DEVICE, msg);

        return false;
    }
}
