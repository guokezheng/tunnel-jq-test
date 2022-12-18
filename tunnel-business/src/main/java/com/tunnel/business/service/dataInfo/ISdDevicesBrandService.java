package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.SdDevicesBrand;

import java.util.List;

/**
 * 物联设备厂商Service接口
 *
 * @author ruoyi
 * @date 2022-12-02
 */
public interface ISdDevicesBrandService {
    /**
     * 查询物联设备厂商
     *
     * @param supplierId 物联设备厂商主键
     * @return 物联设备厂商
     */
    public SdDevicesBrand selectSdDevicesBrandBySupplierId(String supplierId);

    /**
     * 查询物联设备厂商列表
     *
     * @param sdDevicesBrand 物联设备厂商
     * @return 物联设备厂商集合
     */
    public List<SdDevicesBrand> selectSdDevicesBrandList(SdDevicesBrand sdDevicesBrand);

    /**
     * 新增物联设备厂商
     *
     * @param sdDevicesBrand 物联设备厂商
     * @return 结果
     */
    public int insertSdDevicesBrand(SdDevicesBrand sdDevicesBrand);

    /**
     * 修改物联设备厂商
     *
     * @param sdDevicesBrand 物联设备厂商
     * @return 结果
     */
    public int updateSdDevicesBrand(SdDevicesBrand sdDevicesBrand);

    /**
     * 批量删除物联设备厂商
     *
     * @param supplierIds 需要删除的物联设备厂商主键集合
     * @return 结果
     */
    public int deleteSdDevicesBrandBySupplierIds(String[] supplierIds);

    /**
     * 删除物联设备厂商信息
     *
     * @param supplierId 物联设备厂商主键
     * @return 结果
     */
    public int deleteSdDevicesBrandBySupplierId(String supplierId);
}
