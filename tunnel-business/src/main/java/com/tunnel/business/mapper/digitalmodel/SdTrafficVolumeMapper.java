package com.tunnel.business.mapper.digitalmodel;


import com.tunnel.business.domain.event.SdEventType;
import com.tunnel.business.domain.event.SdTrafficVolume;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 事件类型Mapper接口
 * 
 * @author gongfanfei
 * @date 2020-08-24
 */
public interface SdTrafficVolumeMapper
{
    /**
     * 查询事件类型
     * 
     * @param id 事件类型ID
     * @return 事件类型
     */
    public SdEventType selectSdTrafficVolumeById(@Param("id") Long id);

    /**
     * 查询车流量列表
     * 
     * @param sdTrafficVolume
     * @return 事件类型集合
     */
    public List<SdEventType> selectSdTrafficVolumeList(SdTrafficVolume sdTrafficVolume);

    /**
     * 新增车流量
     * 
     * @param sdTrafficVolume
     * @return 结果
     */
    public int insertSdTrafficVolume(SdTrafficVolume sdTrafficVolume);

    /**
     * 修改车流量
     * 
     * @param sdTrafficVolume
     * @return 结果
     */
    public int updateSdTrafficVolume(SdTrafficVolume sdTrafficVolume);

    /**
     * 删除事件类型
     * 
     * @param id 事件类型ID
     * @return 结果
     */
    public int deleteSdTrafficVolumeById(@Param("id") Long id);

    /**
     * 批量删除事件类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdTrafficVolumeByIds(@Param("ids") Long[] ids);

    List<Map<String, Object>> selectCarNumber(@Param("tunnelId") String tunnelId);
}