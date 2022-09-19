package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdEventDetection;

import java.util.List;

/**
 * 事件监测记录Mapper接口
 * 
 * @author liubaokui
 * @date 2021-04-21
 */
public interface SdEventDetectionMapper 
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
     * 删除事件监测记录
     * 
     * @param id 事件监测记录ID
     * @return 结果
     */
    public int deleteSdEventDetectionById(Long id);

    /**
     * 批量删除事件监测记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEventDetectionByIds(Long[] ids);
}
