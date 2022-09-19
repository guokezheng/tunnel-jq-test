package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdEventControl;

import java.util.List;

/**
 * 事件管控Service接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdEventControlService {
    /**
     * 查询事件管控
     *
     * @param id 事件管控主键
     * @return 事件管控
     */
    SdEventControl selectSdEventControlById(Long id);

    /**
     * 查询事件管控列表
     *
     * @param sdEventControl 事件管控
     * @return 事件管控集合
     */
    List<SdEventControl> selectSdEventControlList(SdEventControl sdEventControl);

    /**
     * 新增事件管控
     *
     * @param sdEventControl 事件管控
     * @return 结果
     */
    int insertSdEventControl(SdEventControl sdEventControl);

    /**
     * 修改事件管控
     *
     * @param sdEventControl 事件管控
     * @return 结果
     */
    int updateSdEventControl(SdEventControl sdEventControl);

    /**
     * 批量删除事件管控
     *
     * @param ids 需要删除的事件管控主键集合
     * @return 结果
     */
    int deleteSdEventControlByIds(Long[] ids);

    /**
     * 删除事件管控信息
     *
     * @param id 事件管控主键
     * @return 结果
     */
    int deleteSdEventControlById(Long id);
}
