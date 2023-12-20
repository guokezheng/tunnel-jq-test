package com.tunnel.business.service.vehicle;

import com.tunnel.business.domain.vehicle.SdNoSlowDownTraffic;

import java.util.List;

/**
 * 不降速通行Service接口
 * 
 * @author ruoyi
 * @date 2023-12-14
 */
public interface ISdNoSlowDownTrafficService 
{
    /**
     * 查询不降速通行
     * 
     * @param id 不降速通行主键
     * @return 不降速通行
     */
    public SdNoSlowDownTraffic selectSdNoSlowDownTrafficById(Long id);

    /**
     * 查询不降速通行列表
     * 
     * @param sdNoSlowDownTraffic 不降速通行
     * @return 不降速通行集合
     */
    public List<SdNoSlowDownTraffic> selectSdNoSlowDownTrafficList(SdNoSlowDownTraffic sdNoSlowDownTraffic);

    /**
     * 新增不降速通行
     * 
     * @param sdNoSlowDownTraffic 不降速通行
     * @return 结果
     */
    public int insertSdNoSlowDownTraffic(SdNoSlowDownTraffic sdNoSlowDownTraffic);

    /**
     * 修改不降速通行
     * 
     * @param sdNoSlowDownTraffic 不降速通行
     * @return 结果
     */
    public int updateSdNoSlowDownTraffic(SdNoSlowDownTraffic sdNoSlowDownTraffic);

    /**
     * 批量删除不降速通行
     * 
     * @param ids 需要删除的不降速通行主键集合
     * @return 结果
     */
    public int deleteSdNoSlowDownTrafficByIds(Long[] ids);

    /**
     * 删除不降速通行信息
     * 
     * @param id 不降速通行主键
     * @return 结果
     */
    public int deleteSdNoSlowDownTrafficById(Long id);
}
