package com.tunnel.business.service.vehicle.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.vehicle.SdNoSlowDownTraffic;
import com.tunnel.business.mapper.vehicle.SdNoSlowDownTrafficMapper;
import com.tunnel.business.service.vehicle.ISdNoSlowDownTrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 不降速通行Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-14
 */
@Service
public class SdNoSlowDownTrafficServiceImpl implements ISdNoSlowDownTrafficService
{
    @Autowired
    private SdNoSlowDownTrafficMapper sdNoSlowDownTrafficMapper;

    /**
     * 查询不降速通行
     * 
     * @param id 不降速通行主键
     * @return 不降速通行
     */
    @Override
    public SdNoSlowDownTraffic selectSdNoSlowDownTrafficById(Long id)
    {
        return sdNoSlowDownTrafficMapper.selectSdNoSlowDownTrafficById(id);
    }

    /**
     * 查询不降速通行列表
     * 
     * @param sdNoSlowDownTraffic 不降速通行
     * @return 不降速通行
     */
    @Override
    public List<SdNoSlowDownTraffic> selectSdNoSlowDownTrafficList(SdNoSlowDownTraffic sdNoSlowDownTraffic)
    {
        return sdNoSlowDownTrafficMapper.selectSdNoSlowDownTrafficList(sdNoSlowDownTraffic);
    }

    /**
     * 新增不降速通行
     * 
     * @param sdNoSlowDownTraffic 不降速通行
     * @return 结果
     */
    @Override
    public int insertSdNoSlowDownTraffic(SdNoSlowDownTraffic sdNoSlowDownTraffic)
    {
        sdNoSlowDownTraffic.setCreateTime(DateUtils.getNowDate());
        return sdNoSlowDownTrafficMapper.insertSdNoSlowDownTraffic(sdNoSlowDownTraffic);
    }

    /**
     * 修改不降速通行
     * 
     * @param sdNoSlowDownTraffic 不降速通行
     * @return 结果
     */
    @Override
    public int updateSdNoSlowDownTraffic(SdNoSlowDownTraffic sdNoSlowDownTraffic)
    {
        sdNoSlowDownTraffic.setUpdateTime(DateUtils.getNowDate());
        return sdNoSlowDownTrafficMapper.updateSdNoSlowDownTraffic(sdNoSlowDownTraffic);
    }

    /**
     * 批量删除不降速通行
     * 
     * @param ids 需要删除的不降速通行主键
     * @return 结果
     */
    @Override
    public int deleteSdNoSlowDownTrafficByIds(Long[] ids)
    {
        return sdNoSlowDownTrafficMapper.deleteSdNoSlowDownTrafficByIds(ids);
    }

    /**
     * 删除不降速通行信息
     * 
     * @param id 不降速通行主键
     * @return 结果
     */
    @Override
    public int deleteSdNoSlowDownTrafficById(Long id)
    {
        return sdNoSlowDownTrafficMapper.deleteSdNoSlowDownTrafficById(id);
    }
}
