package com.tunnel.platform.service.emeResource;

import com.tunnel.platform.domain.emeResource.SdMaterial;
import com.tunnel.platform.domain.emeResource.SdMaterialRecord;

import java.util.List;

/**
 * 应急资源Service接口
 * 
 * @author xuebi
 * @date 2020-08-28
 */
public interface ISdMaterialService 
{
    /**
     * 查询应急资源
     * 
     * @param id 应急资源ID
     * @return 应急资源
     */
    public SdMaterial selectSdMaterialById(Long id);

    /**
     * 查询应急资源列表
     * 
     * @param sdMaterial 应急资源
     * @return 应急资源集合
     */
    public List<SdMaterial> selectSdMaterialList(SdMaterial sdMaterial);

    /**
     * 新增应急资源
     * 
     * @param sdMaterial 应急资源
     * @return 结果
     */
    public int insertSdMaterial(SdMaterial sdMaterial);

    /**
     * 修改应急资源
     *
     * @param sdMaterial 应急资源
     * @param sdMaterial
     * @return 结果
     */
    public int updateSdMaterial(SdMaterial sdMaterial);

    /**
     * 应急资源出入库
     * 
     * @param sdmaterialRecord 应急资源
     * @param sdmaterialRecord
     * @return 结果
     */
    public int insertSdMaterialRecord(SdMaterialRecord sdmaterialRecord);
    /**
     * 批量删除应急资源
     * 
     * @param ids 需要删除的应急资源ID
     * @return 结果
     */
    public int deleteSdMaterialByIds(Long[] ids);

    /**
     * 删除应急资源信息
     * 
     * @param id 应急资源ID
     * @return 结果
     */
    public int deleteSdMaterialById(Long id);
    /**
     * 查询出入库详情
     * @param sdmaterialrecord
     * @return
     */
	public List<SdMaterialRecord> selectSdMaterialRecordList(SdMaterialRecord sdmaterialrecord);


}