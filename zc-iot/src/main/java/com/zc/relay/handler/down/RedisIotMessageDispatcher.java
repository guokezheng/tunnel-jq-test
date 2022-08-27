package com.zc.relay.handler.down;

import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.zc.common.constant.RedisChannelConstants;
import com.zc.common.core.redis.RedisMessageDispatcher;
import com.zc.relay.dto.DownData;
import com.zc.relay.dto.DownDataChildDevice;
import com.zc.relay.dto.DownSetData;
import com.zc.relay.dto.DownSetDataChildDevice;
import org.springframework.data.redis.connection.Message;

import java.lang.reflect.Type;
import java.util.ServiceLoader;

@AutoService(RedisMessageDispatcher.class)
public class RedisIotMessageDispatcher implements RedisMessageDispatcher
{

    private static DownInstructHandle downInstructHandle;

    static
    {
        ServiceLoader<DownInstructHandle> load = ServiceLoader.load(DownInstructHandle.class);

        for (DownInstructHandle item : load)
        {
            downInstructHandle = item;
            break;
        }
    }


    @Override
    public void onMessage(Message message, byte[] pattern)
    {

        String channel = new String(message.getChannel());
        String body = new String(message.getBody());

        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        switch (channel)
        {
            // 下发获取属性
            case RedisChannelConstants.IOT_GET_ATTRIBUTE:
            {
                Type type = new TypeToken<DownData>()
                {
                }.getType();

                Gson gson = new Gson();

                DownData downData = gson.fromJson(jsonObject, type);
                downInstructHandle.getAttribute(downData);
                break;
            }
            // 下发设置属性
            case RedisChannelConstants.IOT_SET_ATTRIBUTE:
            {
                Type type = new TypeToken<DownSetData<Number>>()
                {
                }.getType();

                Gson gson = new Gson();

                DownSetData<Number> downSetData = gson.fromJson(jsonObject, type);

                downInstructHandle.setAttribute(downSetData);

                break;
            }
            // 下发服务调用
            case RedisChannelConstants.IOT_SERVE_INVOKE:
            {
                Type type = new TypeToken<DownSetData<JSONObject>>()
                {
                }.getType();

                Gson gson = new Gson();

                DownSetData<JSONObject> downSetData = gson.fromJson(jsonObject, type);

                downInstructHandle.serveInvoke(downSetData);

                break;
            }
            // 下发获取属性
            case RedisChannelConstants.IOT_GET_ATTRIBUTE_CHILD_DEVICE:
            {
                Type type = new TypeToken<DownDataChildDevice>()
                {
                }.getType();

                Gson gson = new Gson();

                DownDataChildDevice downData = gson.fromJson(jsonObject, type);
                downInstructHandle.getAttribute(downData);
                break;
            }
            // 下发设置属性
            case RedisChannelConstants.IOT_SET_ATTRIBUTE_CHILD_DEVICE:
            {
                Type type = new TypeToken<DownSetDataChildDevice<Number>>()
                {
                }.getType();

                Gson gson = new Gson();

                DownSetDataChildDevice<Number> downSetData = gson.fromJson(jsonObject, type);

                downInstructHandle.setAttribute(downSetData);
                break;
            }
            // 下发服务调用
            case RedisChannelConstants.IOT_SERVE_INVOKE_CHILD_DEVICE:
            {
                Type type = new TypeToken<DownSetDataChildDevice<JSONObject>>()
                {
                }.getType();

                Gson gson = new Gson();

                DownSetDataChildDevice<JSONObject> downSetData = gson.fromJson(jsonObject, type);

                downInstructHandle.serveInvoke(downSetData);
                break;
            }
        }

    }

    @Override
    public boolean isChannelExist(String channel)
    {

        return RedisChannelConstants.IOT_SET_ATTRIBUTE.equals(channel)
                || RedisChannelConstants.IOT_GET_ATTRIBUTE.equals(channel)
                || RedisChannelConstants.IOT_SERVE_INVOKE.equals(channel)
                || RedisChannelConstants.IOT_SET_ATTRIBUTE_CHILD_DEVICE.equals(channel)
                || RedisChannelConstants.IOT_GET_ATTRIBUTE_CHILD_DEVICE.equals(channel)
                || RedisChannelConstants.IOT_SERVE_INVOKE_CHILD_DEVICE.equals(channel);
    }
}
