package com.tunnel.business.mapper.event;

import com.tunnel.business.domain.event.SdEventHandle;
import com.tunnel.business.domain.event.SdEventHandleHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 事件处置信息Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-15
 */
public interface SdEventHandleHistoryMapper
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
     * @param sdEventHandleHistory 事件处置信息
     * @return 事件处置信息集合
     */
    public List<SdEventHandle> selectSdEventHandleList(SdEventHandle sdEventHandleHistory);

    /**
     * 新增事件处置信息
     * 
     * @param sdEventHandleHistory 事件处置信息
     * @return 结果
     */
    public int insertSdEventHandle(SdEventHandle sdEventHandleHistory);

    /**
     * 修改事件处置信息
     * 
     * @param sdEventHandleHistory 事件处置信息
     * @return 结果
     */
    public int updateSdEventHandle(SdEventHandle sdEventHandleHistory);

    /**
     * 设备管控下发设备成功时修改状态
     * @param sdEventHandleHistory
     * @return
     */
    public int updateHandleState(SdEventHandle sdEventHandleHistory);

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
     * 查询历史流程数量
     * @param eventId
     * @return
     */
    String selectNum(@Param("eventId") Long eventId);
}
