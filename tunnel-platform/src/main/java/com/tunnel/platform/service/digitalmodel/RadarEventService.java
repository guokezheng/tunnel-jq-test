package com.tunnel.platform.service.digitalmodel;

import com.ruoyi.common.core.domain.AjaxResult;

import java.text.ParseException;
import java.util.Map;

/**
 * @author dzy
 * @date 2022/9/4 16:51
 */
public interface RadarEventService {
    /**
     * 新增万集事件
     * @param map
     */
    AjaxResult insertWjEvent(Map<String, Object> map);

    AjaxResult uploadPic(Map<String, Object> map);

    AjaxResult eventVideo(Map<String,Object> map);

    AjaxResult specialCar(Map<String, Object> map);

    void insertRadarDetect(Map<String, Object> map) throws ParseException;

    void saveRedis(Map<String, Object> map);

    void sendBaseDeviceStatus(Map<String, Object> map);
}
