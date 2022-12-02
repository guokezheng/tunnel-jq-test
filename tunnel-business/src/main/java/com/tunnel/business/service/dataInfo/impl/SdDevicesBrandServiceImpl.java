package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdDevicesBrand;
import com.tunnel.business.mapper.dataInfo.SdDevicesBrandMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesBrandService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物联设备厂商Service业务层处理
 *
 * @author ruoyi
 * @date 2022-12-02
 */
@Service
public class SdDevicesBrandServiceImpl implements ISdDevicesBrandService {
    @Autowired
    private SdDevicesBrandMapper sdDevicesBrandMapper;

    /**
     * 查询物联设备厂商
     *
     * @param supplierId 物联设备厂商主键
     * @return 物联设备厂商
     */
    @Override
    public SdDevicesBrand selectSdDevicesBrandBySupplierId(String supplierId) {
        return sdDevicesBrandMapper.selectSdDevicesBrandBySupplierId(supplierId);
    }

    /**
     * 查询物联设备厂商列表
     *
     * @param sdDevicesBrand 物联设备厂商
     * @return 物联设备厂商
     */
    @Override
    public List<SdDevicesBrand> selectSdDevicesBrandList(SdDevicesBrand sdDevicesBrand) {
        return sdDevicesBrandMapper.selectSdDevicesBrandList(sdDevicesBrand);
    }

    /**
     * 新增物联设备厂商
     *
     * @param sdDevicesBrand 物联设备厂商
     * @return 结果
     */
    @Override
    public int insertSdDevicesBrand(SdDevicesBrand sdDevicesBrand) {
        sdDevicesBrand.setSupplierId(UUIDUtil.getRandom32PK());
        sdDevicesBrand.setIsDel(0);
        sdDevicesBrand.setCreateTime(DateUtils.getNowDate());
        return sdDevicesBrandMapper.insertSdDevicesBrand(sdDevicesBrand);
    }

    /**
     * 修改物联设备厂商
     *
     * @param sdDevicesBrand 物联设备厂商
     * @return 结果
     */
    @Override
    public int updateSdDevicesBrand(SdDevicesBrand sdDevicesBrand) {
        sdDevicesBrand.setUpdateTime(DateUtils.getNowDate());
        return sdDevicesBrandMapper.updateSdDevicesBrand(sdDevicesBrand);
    }

    /**
     * 批量删除物联设备厂商
     *
     * @param supplierIds 需要删除的物联设备厂商主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicesBrandBySupplierIds(String[] supplierIds) {
        return sdDevicesBrandMapper.deleteSdDevicesBrandBySupplierIds(supplierIds);
    }

    /**
     * 删除物联设备厂商信息
     *
     * @param supplierId 物联设备厂商主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicesBrandBySupplierId(String supplierId) {
        return sdDevicesBrandMapper.deleteSdDevicesBrandBySupplierId(supplierId);
    }
}
