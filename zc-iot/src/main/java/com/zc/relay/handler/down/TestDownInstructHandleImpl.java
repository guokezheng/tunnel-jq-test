package com.zc.relay.handler.down;

import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.zc.relay.dto.DownData;
import com.zc.relay.dto.DownDataChildDevice;
import com.zc.relay.dto.DownSetData;
import com.zc.relay.dto.DownSetDataChildDevice;

@AutoService(DownInstructHandle.class)
public class TestDownInstructHandleImpl implements DownInstructHandle
{

    @Override
    public boolean setAttribute(DownSetData<Number> downSetData) {
        return false;
    }

    @Override
    public boolean setAttribute(DownSetDataChildDevice<Number> downSetDataChildDevice) {
        return false;
    }

    @Override
    public boolean getAttribute(DownData downData) {
        return false;
    }

    @Override
    public boolean getAttribute(DownDataChildDevice downDataChildDevice) {
        return false;
    }

    @Override
    public boolean serveInvoke(DownSetData<JSONObject> downSetData) {
        return false;
    }

    @Override
    public boolean serveInvoke(DownSetDataChildDevice<JSONObject> downSetDataChildDevice) {
        return false;
    }
}
