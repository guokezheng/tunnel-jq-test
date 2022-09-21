package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdSparePartsWarehouse;

import java.util.List;

/**
 * 备品备件库Service接口
 *
 * @author ruoyi
 * @date 2022-01-21
 */
public interface ISdSparePartsWarehouseService {
    /**
     * 查询备品备件库
     *
     * @param id 备品备件库主键
     * @return 备品备件库
     */
    SdSparePartsWarehouse selectSdSparePartsWarehouseById(Long id);

    /**
     * 查询备品备件库列表
     *
     * @param sdSparePartsWarehouse 备品备件库
     * @return 备品备件库集合
     */
    List<SdSparePartsWarehouse> selectSdSparePartsWarehouseList(SdSparePartsWarehouse sdSparePartsWarehouse);

    /**
     * 新增备品备件库
     *
     * @param sdSparePartsWarehouse 备品备件库
     * @return 结果
     */
    int insertSdSparePartsWarehouse(SdSparePartsWarehouse sdSparePartsWarehouse);

    /**
     * 修改备品备件库
     *
     * @param sdSparePartsWarehouse 备品备件库
     * @return 结果
     */
    int updateSdSparePartsWarehouse(SdSparePartsWarehouse sdSparePartsWarehouse);

    /**
     * 批量删除备品备件库
     *
     * @param ids 需要删除的备品备件库主键集合
     * @return 结果
     */
    int deleteSdSparePartsWarehouseByIds(Long[] ids);

    /**
     * 删除备品备件库信息
     *
     * @param id 备品备件库主键
     * @return 结果
     */
    int deleteSdSparePartsWarehouseById(Long id);
}
