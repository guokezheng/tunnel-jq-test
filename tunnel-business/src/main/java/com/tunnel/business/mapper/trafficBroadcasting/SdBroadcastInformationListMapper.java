package com.tunnel.business.mapper.trafficBroadcasting;


import com.tunnel.business.domain.trafficBroadcasting.SdBroadcastInformationList;

import java.util.List;

/**
 * 广播信息列Mapper接口
 * 
 * @author ruoyi
 * @date 2021-11-24
 */
public interface SdBroadcastInformationListMapper 
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
     * 删除广播信息列
     * 
     * @param id 广播信息列ID
     * @return 结果
     */
    public int deleteSdBroadcastInformationListById(Long id);

    /**
     * 批量删除广播信息列
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdBroadcastInformationListByIds(Long[] ids);
}
