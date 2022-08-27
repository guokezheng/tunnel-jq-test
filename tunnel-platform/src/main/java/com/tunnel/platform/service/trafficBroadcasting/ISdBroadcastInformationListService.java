package com.tunnel.platform.service.trafficBroadcasting;

import com.tunnel.platform.domain.trafficBroadcasting.SdBroadcastInformationList;

import java.util.List;

/**
 * 广播信息列Service接口
 * 
 * @author ruoyi
 * @date 2021-11-24
 */
public interface ISdBroadcastInformationListService 
{
    /**
     * 查询广播信息列
     * 
     * @param id 广播信息列ID
     * @return 广播信息列
     */
    public SdBroadcastInformationList selectSdBroadcastInformationListById(Long id);

    /**
     * 查询广播信息列列表
     * 
     * @param sdBroadcastInformationList 广播信息列
     * @return 广播信息列集合
     */
    public List<SdBroadcastInformationList> selectSdBroadcastInformationListList(SdBroadcastInformationList sdBroadcastInformationList);

    /**
     * 新增广播信息列
     * 
     * @param sdBroadcastInformationList 广播信息列
     * @return 结果
     */
    public int insertSdBroadcastInformationList(SdBroadcastInformationList sdBroadcastInformationList);

    /**
     * 修改广播信息列
     * 
     * @param sdBroadcastInformationList 广播信息列
     * @return 结果
     */
    public int updateSdBroadcastInformationList(SdBroadcastInformationList sdBroadcastInformationList);

    /**
     * 批量删除广播信息列
     * 
     * @param ids 需要删除的广播信息列ID
     * @return 结果
     */
    public int deleteSdBroadcastInformationListByIds(Long[] ids);

    /**
     * 删除广播信息列信息
     * 
     * @param id 广播信息列ID
     * @return 结果
     */
    public int deleteSdBroadcastInformationListById(Long id);
}
