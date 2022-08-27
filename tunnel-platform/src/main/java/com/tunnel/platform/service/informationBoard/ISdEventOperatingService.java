package com.tunnel.platform.service.informationBoard;


import com.tunnel.platform.domain.informationBoard.SdEventOperating;

import java.util.List;

/**
 * 交通事件Service接口
 * 
 * @author 刘方堃
 * @date 2021-12-03
 */
public interface ISdEventOperatingService 
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
     * 批量删除交通事件
     * 
     * @param ids 需要删除的交通事件ID
     * @return 结果
     */
    public int deleteSdEventOperatingByIds(Long[] ids);

    /**
     * 删除交通事件信息
     * 
     * @param id 交通事件ID
     * @return 结果
     */
    public int deleteSdEventOperatingById(Long id);
}
