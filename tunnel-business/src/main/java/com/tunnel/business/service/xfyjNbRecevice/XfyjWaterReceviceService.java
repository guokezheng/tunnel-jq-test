package com.tunnel.business.service.xfyjNbRecevice;

import com.alibaba.fastjson.JSONObject;

/**
 * 名称:消防水接收协议解析
 */
public interface XfyjWaterReceviceService {
    /**
     * 解析消防水协议
     *
     * @param jsonObject
     */
    void analysisWater(JSONObject jsonObject);
}
