package com.tunnel.business.mapper.emeResource;


import com.tunnel.business.domain.emeResource.SdMaterialRecord;

import java.util.List;

/**
 * 出入库记录Mapper接口
 * 
 * @author xuebi
 * @date 2020-08-26
 */
public interface SdMaterialRecordMapper 
{
    /**
     * 查询出入库记录
     * 
     * @param id 出入库记录ID
     * @return 出入库记录
     */
    public SdMaterialRecord selectSdMaterialRecordById(Long id);

    /**
     * 查询出入库记录列表
     * 
     * @param sdMaterialRecord 出入库记录
     * @return 出入库记录集合
     */
    public List<SdMaterialRecord> selectSdMaterialRecordList(SdMaterialRecord sdMaterialRecord);

    /**
     * 新增出入库记录
     * 
     * @param sdMaterialRecord 出入库记录
     * @return 结果
     */
    public int insertSdMaterialRecord(SdMaterialRecord sdMaterialRecord);

    /**
     * 修改出入库记录
     * 
     * @param sdMaterialRecord 出入库记录
     * @return 结果
     */
    public int updateSdMaterialRecord(SdMaterialRecord sdMaterialRecord);

    /**
     * 删除出入库记录
     * 
     * @param id 出入库记录ID
     * @return 结果
     */
    public int deleteSdMaterialRecordById(Long id);

    /**
     * 批量删除出入库记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdMaterialRecordByIds(Long[] ids);
}