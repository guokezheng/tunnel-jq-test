package com.tunnel.business.mapper.informationBoard;


import com.tunnel.business.domain.informationBoard.SdEventOperating;

import java.util.List;

/**
 * 交通事件Mapper接口
 * 
 * @author 刘方堃
 * @date 2021-12-03
 */
public interface SdEventOperatingMapper 
{
    /**
     * 查询交通事件
     * 
     * @param id 交通事件ID
     * @return 交通事件
     */
    public SdEventOperating selectSdEventOperatingById(Long id);

    /**
     * 查询交通事件列表
     * 
     * @param sdEventOperating 交通事件
     * @return 交通事件集合
     */
    public List<SdEventOperating> selectSdEventOperatingList(SdEventOperating sdEventOperating);

    /**
     * 新增交通事件
     * 
     * @param sdEventOperating 交通事件
     * @return 结果
     */
    public int insertSdEventOperating(SdEventOperating sdEventOperating);

    /**
     * 修改交通事件
     * 
     * @param sdEventOperating 交通事件
     * @return 结果
     */
    public int updateSdEventOperating(SdEventOperating sdEventOperating);

    /**
     * 删除交通事件
     * 
     * @param id 交通事件ID
     * @return 结果
     */
    public int deleteSdEventOperatingById(Long id);

    /**
     * 批量删除交通事件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEventOperatingByIds(Long[] ids);
}
