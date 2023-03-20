package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdSparePartsWarehouse;
import com.tunnel.business.domain.dataInfo.vo.SdSparePartsWarehouseVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 备品备件库Mapper接口
 *
 * @author ruoyi
 * @date 2022-01-21
 */
public interface SdSparePartsWarehouseMapper
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

    public List<SdSparePartsWarehouseVO> getHsdSparePartList(SdSparePartsWarehouse sdSparePartsWarehouse);

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
     * 删除备品备件库
     *
     * @param id 备品备件库主键
     * @return 结果
     */
    public int deleteSdSparePartsWarehouseById(Long id);

    /**
     * 批量删除备品备件库
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdSparePartsWarehouseByIds(Long[] ids);

    /**
     * 查询备件名称是否重复
     * @param partName
     * @return
     */
    List<SdSparePartsWarehouse> verifyPartNameOnly(@Param("partName") String partName);
}
