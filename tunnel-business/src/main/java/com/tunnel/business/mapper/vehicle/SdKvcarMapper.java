package com.tunnel.business.mapper.vehicle;

import com.tunnel.business.domain.vehicle.SdKvcar;

import java.util.List;

/**
 * 两客一危车辆数据Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-14
 */
public interface SdKvcarMapper 
{
    /**
     * 查询两客一危车辆数据
     * 
     * @param id 两客一危车辆数据主键
     * @return 两客一危车辆数据
     */
    public SdKvcar selectSdKvcarById(Long id);

    /**
     * 查询两客一危车辆数据列表
     * 
     * @param sdKvcar 两客一危车辆数据
     * @return 两客一危车辆数据集合
     */
    public List<SdKvcar> selectSdKvcarList(SdKvcar sdKvcar);

    /**
     * 新增两客一危车辆数据
     * 
     * @param sdKvcar 两客一危车辆数据
     * @return 结果
     */
    public int insertSdKvcar(SdKvcar sdKvcar);

    /**
     * 修改两客一危车辆数据
     * 
     * @param sdKvcar 两客一危车辆数据
     * @return 结果
     */
    public int updateSdKvcar(SdKvcar sdKvcar);

    /**
     * 删除两客一危车辆数据
     * 
     * @param id 两客一危车辆数据主键
     * @return 结果
     */
    public int deleteSdKvcarById(Long id);

    /**
     * 批量删除两客一危车辆数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdKvcarByIds(Long[] ids);
}
