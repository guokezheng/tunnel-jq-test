package com.tunnel.business.mapper.event;


import com.tunnel.business.domain.event.SdEventType;

import java.util.List;

/**
 * 事件类型Mapper接口
 * 
 * @author gongfanfei
 * @date 2020-08-24
 */
public interface SdEventTypeMapper 
{
    /**
     * 查询事件类型
     * 
     * @param id 事件类型ID
     * @return 事件类型
     */
    public SdEventType selectSdEventTypeById(Long id);

    /**
     * 查询事件类型列表
     * 
     * @param sdEventType 事件类型
     * @return 事件类型集合
     */
    public List<SdEventType> selectSdEventTypeList(SdEventType sdEventType);

    /**
     * 新增事件类型
     * 
     * @param sdEventType 事件类型
     * @return 结果
     */
    public int insertSdEventType(SdEventType sdEventType);

    /**
     * 修改事件类型
     * 
     * @param sdEventType 事件类型
     * @return 结果
     */
    public int updateSdEventType(SdEventType sdEventType);

    public int updateSdEventTypeAudio(SdEventType sdEventType);

    /**
     * 删除事件类型
     * 
     * @param id 事件类型ID
     * @return 结果
     */
    public int deleteSdEventTypeById(Long id);

    /**
     * 批量删除事件类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEventTypeByIds(Long[] ids);

    /**
     * 校验查询是否数据重复
     * @param sdEventType
     * @return
     */
    int checkData(SdEventType sdEventType);
}