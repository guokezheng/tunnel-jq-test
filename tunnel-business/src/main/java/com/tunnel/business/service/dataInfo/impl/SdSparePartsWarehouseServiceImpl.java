package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdSparePartsWarehouse;
import com.tunnel.business.mapper.dataInfo.SdSparePartsWarehouseMapper;
import com.tunnel.business.service.dataInfo.ISdSparePartsWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 备品备件库Service业务层处理
 *
 * @author ruoyi
 * @date 2022-01-21
 */
@Service
public class SdSparePartsWarehouseServiceImpl implements ISdSparePartsWarehouseService {
    @Autowired
    private SdSparePartsWarehouseMapper sdSparePartsWarehouseMapper;

    /**
     * 查询备品备件库
     *
     * @param id 备品备件库主键
     * @return 备品备件库
     */
    @Override
    public SdSparePartsWarehouse selectSdSparePartsWarehouseById(Long id) {
        return sdSparePartsWarehouseMapper.selectSdSparePartsWarehouseById(id);
    }

    /**
     * 查询备品备件库列表
     *
     * @param sdSparePartsWarehouse 备品备件库
     * @return 备品备件库
     */
    @Override
    public List<SdSparePartsWarehouse> selectSdSparePartsWarehouseList(SdSparePartsWarehouse sdSparePartsWarehouse) {
        return sdSparePartsWarehouseMapper.selectSdSparePartsWarehouseList(sdSparePartsWarehouse);
    }

    /**
     * 新增备品备件库
     *
     * @param sdSparePartsWarehouse 备品备件库
     * @return 结果
     */
    @Override
    public int insertSdSparePartsWarehouse(SdSparePartsWarehouse sdSparePartsWarehouse) {
        sdSparePartsWarehouse.setCreateTime(DateUtils.getNowDate());
        return sdSparePartsWarehouseMapper.insertSdSparePartsWarehouse(sdSparePartsWarehouse);
    }

    /**
     * 修改备品备件库
     *
     * @param sdSparePartsWarehouse 备品备件库
     * @return 结果
     */
    @Override
    public int updateSdSparePartsWarehouse(SdSparePartsWarehouse sdSparePartsWarehouse) {
        sdSparePartsWarehouse.setUpdateTime(DateUtils.getNowDate());
        return sdSparePartsWarehouseMapper.updateSdSparePartsWarehouse(sdSparePartsWarehouse);
    }

    /**
     * 批量删除备品备件库
     *
     * @param ids 需要删除的备品备件库主键
     * @return 结果
     */
    @Override
    public int deleteSdSparePartsWarehouseByIds(Long[] ids) {
        return sdSparePartsWarehouseMapper.deleteSdSparePartsWarehouseByIds(ids);
    }

    /**
     * 删除备品备件库信息
     *
     * @param id 备品备件库主键
     * @return 结果
     */
    @Override
    public int deleteSdSparePartsWarehouseById(Long id) {
        return sdSparePartsWarehouseMapper.deleteSdSparePartsWarehouseById(id);
    }
}
