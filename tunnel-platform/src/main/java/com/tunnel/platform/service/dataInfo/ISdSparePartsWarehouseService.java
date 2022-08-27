package com.tunnel.platform.service.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdSparePartsWarehouse;

/**
 * 备品备件库Service接口
 * 
 * @author ruoyi
 * @date 2022-01-21
 */
public interface ISdSparePartsWarehouseService 
{
    /**
     * 查询备品备件库
     * 
     * @param id 备品备件库主键
     * @return 备品备件库
     */
    public SdSparePartsWarehouse selectSdSparePartsWarehouseById(Long id);

    /**
     * 查询备品备件库列表
     * 
     * @param sdSparePartsWarehouse 备品备件库
     * @return 备品备件库集合
     */
    public List<SdSparePartsWarehouse> selectSdSparePartsWarehouseList(SdSparePartsWarehouse sdSparePartsWarehouse);

    /**
     * 新增备品备件库
     * 
     * @param sdSparePartsWarehouse 备品备件库
     * @return 结果
     */
    public int insertSdSparePartsWarehouse(SdSparePartsWarehouse sdSparePartsWarehouse);

    /**
     * 修改备品备件库
     * 
     * @param sdSparePartsWarehouse 备品备件库
     * @return 结果
     */
    public int updateSdSparePartsWarehouse(SdSparePartsWarehouse sdSparePartsWarehouse);

    /**
     * 批量删除备品备件库
     * 
     * @param ids 需要删除的备品备件库主键集合
     * @return 结果
     */
    public int deleteSdSparePartsWarehouseByIds(Long[] ids);

    /**
     * 删除备品备件库信息
     * 
     * @param id 备品备件库主键
     * @return 结果
     */
    public int deleteSdSparePartsWarehouseById(Long id);
}
