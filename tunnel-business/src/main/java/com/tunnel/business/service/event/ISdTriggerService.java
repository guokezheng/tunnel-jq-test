package com.tunnel.business.service.event;


import com.tunnel.business.domain.event.SdTrigger;

import java.util.List;

/**
 * 触发器Service接口
 *
 * @author ruoyi
 * @date 2022-09-01
 */
public interface ISdTriggerService {
    /**
     * 查询触发器
     *
     * @param id 触发器主键
     * @return 触发器
     */
    SdTrigger selectSdTriggerById(Long id);

    /**
     * 根据策略id查询触发器
     *
     * @param relateId
     * @return
     */
    SdTrigger selectSdTriggerByRelateId(Long relateId);

    /**
     * 查询触发器列表
     *
     * @param sdTrigger 触发器
     * @return 触发器集合
     */
    List<SdTrigger> selectSdTriggerList(SdTrigger sdTrigger);

    /**
     * 新增触发器
     *
     * @param sdTrigger 触发器
     * @return 结果
     */
    int insertSdTrigger(SdTrigger sdTrigger);

    /**
     * 修改触发器
     *
     * @param sdTrigger 触发器
     * @return 结果
     */
    int updateSdTrigger(SdTrigger sdTrigger);

    /**
     * 批量删除触发器
     *
     * @param ids 需要删除的触发器主键集合
     * @return 结果
     */
    int deleteSdTriggerByIds(Long[] ids);

    /**
     * 删除触发器信息
     *
     * @param id 触发器主键
     * @return 结果
     */
    int deleteSdTriggerById(Long id);
}
