package com.tunnel.business.mapper.event;


import com.tunnel.business.domain.event.SdTrigger;

import java.util.List;
import java.util.Map;

/**
 * 触发器Mapper接口
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
public interface SdTriggerMapper 
{
    /**
     * 查询触发器
     * 
     * @param id 触发器主键
     * @return 触发器
     */
    public SdTrigger selectSdTriggerById(Long id);

    /**
     * 查询触发器
     *
     * @param relateId 策略id
     * @return 触发器
     */
    public SdTrigger selectSdTriggerByRelateId(Long relateId);


    /**
     * 查询触发器列表
     * 
     * @param sdTrigger 触发器
     * @return 触发器集合
     */
    public List<SdTrigger> selectSdTriggerList(SdTrigger sdTrigger);

    /**
     * 新增触发器
     * 
     * @param sdTrigger 触发器
     * @return 结果
     */
    public int insertSdTrigger(SdTrigger sdTrigger);

    /**
     * 修改触发器
     * 
     * @param sdTrigger 触发器
     * @return 结果
     */
    public int updateSdTrigger(SdTrigger sdTrigger);

    /**
     * 删除触发器
     * 
     * @param id 触发器主键
     * @return 结果
     */
    public int deleteSdTriggerById(Long id);

    /**
     * 批量删除触发器
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdTriggerByIds(Long[] ids);

    /**
     * 根据策略id删除触发器
     * @param rid
     * @return
     */
    public int deleteSdTriggerByRelateId(Long rid);

    /**
     * 根据策略id删除触发器
     * @param ids
     * @return
     */
    public int deleteSdTriggerByRelateIds(Long[] ids);

}
