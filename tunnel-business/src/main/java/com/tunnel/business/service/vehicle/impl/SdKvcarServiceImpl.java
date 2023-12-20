package com.tunnel.business.service.vehicle.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.vehicle.SdKvcar;
import com.tunnel.business.mapper.vehicle.SdKvcarMapper;
import com.tunnel.business.service.vehicle.ISdKvcarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 两客一危车辆数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-14
 */
@Service
public class SdKvcarServiceImpl implements ISdKvcarService
{
    @Autowired
    private SdKvcarMapper sdKvcarMapper;

    /**
     * 查询两客一危车辆数据
     * 
     * @param id 两客一危车辆数据主键
     * @return 两客一危车辆数据
     */
    @Override
    public SdKvcar selectSdKvcarById(Long id)
    {
        return sdKvcarMapper.selectSdKvcarById(id);
    }

    /**
     * 查询两客一危车辆数据列表
     * 
     * @param sdKvcar 两客一危车辆数据
     * @return 两客一危车辆数据
     */
    @Override
    public List<SdKvcar> selectSdKvcarList(SdKvcar sdKvcar)
    {
        return sdKvcarMapper.selectSdKvcarList(sdKvcar);
    }

    /**
     * 新增两客一危车辆数据
     * 
     * @param sdKvcar 两客一危车辆数据
     * @return 结果
     */
    @Override
    public int insertSdKvcar(SdKvcar sdKvcar)
    {
        sdKvcar.setCreateTime(DateUtils.getNowDate());
        return sdKvcarMapper.insertSdKvcar(sdKvcar);
    }

    /**
     * 修改两客一危车辆数据
     * 
     * @param sdKvcar 两客一危车辆数据
     * @return 结果
     */
    @Override
    public int updateSdKvcar(SdKvcar sdKvcar)
    {
        sdKvcar.setUpdateTime(DateUtils.getNowDate());
        return sdKvcarMapper.updateSdKvcar(sdKvcar);
    }

    /**
     * 批量删除两客一危车辆数据
     * 
     * @param ids 需要删除的两客一危车辆数据主键
     * @return 结果
     */
    @Override
    public int deleteSdKvcarByIds(Long[] ids)
    {
        return sdKvcarMapper.deleteSdKvcarByIds(ids);
    }

    /**
     * 删除两客一危车辆数据信息
     * 
     * @param id 两客一危车辆数据主键
     * @return 结果
     */
    @Override
    public int deleteSdKvcarById(Long id)
    {
        return sdKvcarMapper.deleteSdKvcarById(id);
    }
}
