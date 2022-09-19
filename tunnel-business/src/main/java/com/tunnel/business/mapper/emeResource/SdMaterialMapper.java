package com.tunnel.business.mapper.emeResource;


import com.tunnel.business.domain.emeResource.SdMaterial;

import java.util.List;

/**
 * 应急资源Mapper接口
 * 
 * @author xuebi
 * @date 2020-08-21
 */
public interface SdMaterialMapper 
{
    /**
     * 查询应急资源
     * 
     * @param id 应急资源ID
     * @return 应急资源
     */
    public SdMaterial selectSdMaterialById(Long id);

    /**
     * 查询应急资源
     * 
     * @param materialId 应急资源ID
     * @return 应急资源
     */
    public SdMaterial selectSdMaterialByMId(String materialId);

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
     * @return 结果
     */
    public int updateSdMaterial(SdMaterial sdMaterial);

    /**
     * 删除应急资源
     * 
     * @param id 应急资源ID
     * @return 结果
     */
    public int deleteSdMaterialById(Long id);

    /**
     * 批量删除应急资源
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdMaterialByIds(Long[] ids);
    /**
     * 修改应急资源信息
     * 
     * @param sdMaterial 应急资源
     * @return 结果
     */
	public int updateMaterialMessage(SdMaterial sdMaterial);

}