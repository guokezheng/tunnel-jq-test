package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdEventControl;

import java.util.List;

/**
 * 事件管控Mapper接口
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
public interface SdEventControlMapper 
{
    /**
     * 查询事件管控
     * 
     * @param id 事件管控主键
     * @return 事件管控
     */
    public SdEventControl selectSdEventControlById(Long id);

    /**
     * 查询事件管控列表
     * 
     * @param sdEventControl 事件管控
     * @return 事件管控集合
     */
    public List<SdEventControl> selectSdEventControlList(SdEventControl sdEventControl);

    /**
     * 新增事件管控
     * 
     * @param sdEventControl 事件管控
     * @return 结果
     */
    public int insertSdEventControl(SdEventControl sdEventControl);

    /**
     * 修改事件管控
     * 
     * @param sdEventControl 事件管控
     * @return 结果
     */
    public int updateSdEventControl(SdEventControl sdEventControl);

    /**
     * 删除事件管控
     * 
     * @param id 事件管控主键
     * @return 结果
     */
    public int deleteSdEventControlById(Long id);

    /**
     * 批量删除事件管控
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdEventControlByIds(Long[] ids);
}
