package com.tunnel.business.mapper.event;

import com.tunnel.business.domain.event.SdEventHandle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 事件处置信息Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-15
 */
public interface SdEventHandleMapper 
{
    /**
     * 查询事件处置信息
     * 
     * @param id 事件处置信息主键
     * @return 事件处置信息
     */
    public SdEventHandle selectSdEventHandleById(Long id);

    public int selectSdEventHandle(@Param("eventId") Long eventId);

    /**
     * 查询事件处置信息列表
     * 
     * @param sdEventHandle 事件处置信息
     * @return 事件处置信息集合
     */
    public List<SdEventHandle> selectSdEventHandleList(SdEventHandle sdEventHandle);

    /**
     * 新增事件处置信息
     * 
     * @param sdEventHandle 事件处置信息
     * @return 结果
     */
    public int insertSdEventHandle(SdEventHandle sdEventHandle);

    /**
     * 修改事件处置信息
     * 
     * @param sdEventHandle 事件处置信息
     * @return 结果
     */
    public int updateSdEventHandle(SdEventHandle sdEventHandle);

    /**
     * 删除事件处置信息
     * 
     * @param id 事件处置信息主键
     * @return 结果
     */
    public int deleteSdEventHandleById(Long id);

    /**
     * 批量删除事件处置信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdEventHandleByIds(Long[] ids);

    /**
     * 更新应急调度关联
     * @param sdEventHandle
     * @return
     */
    int updateSdEventHandleRelation(SdEventHandle sdEventHandle);

    /**
     * 删除应急调度关联
     * @param sdEventHandle
     * @return
     */
    int deleteRelation(SdEventHandle sdEventHandle);
}
