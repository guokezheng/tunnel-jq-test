package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdEventDetection;

import java.util.List;

/**
 * 事件监测记录Service接口
 * 
 * @author liubaokui
 * @date 2021-04-21
 */
public interface ISdEventDetectionService 
{
    /**
     * 查询事件监测记录
     * 
     * @param id 事件监测记录ID
     * @return 事件监测记录
     */
    public SdEventDetection selectSdEventDetectionById(Long id);

    /**
     * 查询事件监测记录列表
     * 
     * @param sdEventDetection 事件监测记录
     * @return 事件监测记录集合
     */
    public List<SdEventDetection> selectSdEventDetectionList(SdEventDetection sdEventDetection);

    /**
     * 新增事件监测记录
     * 
     * @param sdEventDetection 事件监测记录
     * @return 结果
     */
    public int insertSdEventDetection(SdEventDetection sdEventDetection);

    /**
     * 修改事件监测记录
     * 
     * @param sdEventDetection 事件监测记录
     * @return 结果
     */
    public int updateSdEventDetection(SdEventDetection sdEventDetection);

    /**
     * 批量删除事件监测记录
     * 
     * @param ids 需要删除的事件监测记录ID
     * @return 结果
     */
    public int deleteSdEventDetectionByIds(Long[] ids);

    /**
     * 删除事件监测记录信息
     * 
     * @param id 事件监测记录ID
     * @return 结果
     */
    public int deleteSdEventDetectionById(Long id);
}
