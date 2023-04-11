package com.tunnel.business.mapper.event;

import java.util.List;
import com.tunnel.business.domain.event.SdJoinReserveHandle;
import org.apache.ibatis.annotations.Param;

/**
 * 事件流程设备关联信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-04-08
 */
public interface SdJoinReserveHandleMapper 
{
    /**
     * 查询事件流程设备关联信息
     * 
     * @param id 事件流程设备关联信息主键
     * @return 事件流程设备关联信息
     */
    public SdJoinReserveHandle selectSdJoinReserveHandleById(@Param("id") Long id);

    /**
     * 查询事件流程设备关联信息列表
     * 
     * @param sdJoinReserveHandle 事件流程设备关联信息
     * @return 事件流程设备关联信息集合
     */
    public List<SdJoinReserveHandle> selectSdJoinReserveHandleList(SdJoinReserveHandle sdJoinReserveHandle);

    /**
     * 新增事件流程设备关联信息
     * 
     * @param sdJoinReserveHandle 事件流程设备关联信息
     * @return 结果
     */
    public int insertSdJoinReserveHandle(SdJoinReserveHandle sdJoinReserveHandle);

    /**
     * 修改事件流程设备关联信息
     * 
     * @param sdJoinReserveHandle 事件流程设备关联信息
     * @return 结果
     */
    public int updateSdJoinReserveHandle(SdJoinReserveHandle sdJoinReserveHandle);

    /**
     * 删除事件流程设备关联信息
     * 
     * @param id 事件流程设备关联信息主键
     * @return 结果
     */
    public int deleteSdJoinReserveHandleById(@Param("id") Long id);

    /**
     * 批量删除事件流程设备关联信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdJoinReserveHandleByIds(@Param("ids") Long[] ids);
}
