package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdSensorMessage;

import java.util.List;

/**
 * 传感器采集数据信息Service接口
 * 
 * @author yanghousheng
 * @date 2020-12-25
 */
public interface ISdSensorMessageService 
{
    /**
     * 查询传感器采集数据信息
     * 
     * @param id 传感器采集数据信息ID
     * @return 传感器采集数据信息
     */
    public SdSensorMessage selectSdSensorMessageById(Long id);

    /**
     * 查询传感器采集数据信息列表
     * 
     * @param sdSensorMessage 传感器采集数据信息
     * @return 传感器采集数据信息集合
     */
    public List<SdSensorMessage> selectSdSensorMessageList(SdSensorMessage sdSensorMessage);

    /**
     * 新增传感器采集数据信息
     * 
     * @param sdSensorMessage 传感器采集数据信息
     * @return 结果
     */
    public int insertSdSensorMessage(SdSensorMessage sdSensorMessage);

    /**
     * 修改传感器采集数据信息
     * 
     * @param sdSensorMessage 传感器采集数据信息
     * @return 结果
     */
    public int updateSdSensorMessage(SdSensorMessage sdSensorMessage);

    /**
     * 批量删除传感器采集数据信息
     * 
     * @param ids 需要删除的传感器采集数据信息ID
     * @return 结果
     */
    public int deleteSdSensorMessageByIds(Long[] ids);

    /**
     * 删除传感器采集数据信息信息
     * 
     * @param id 传感器采集数据信息ID
     * @return 结果
     */
    public int deleteSdSensorMessageById(Long id);
    
    /**
     * 查询今天的信息
     * @param sdSensorMessage
     * @return
     */
    public List<SdSensorMessage> selectSdSensorMessageNow(SdSensorMessage sdSensorMessage);
    
    /**
     * 按时间段查询传感器信息
     * @param sdSensorMessage
     * @return
     */
    public List<SdSensorMessage> seleteSdSensorMessageByTime(SdSensorMessage sdSensorMessage);
}
 
