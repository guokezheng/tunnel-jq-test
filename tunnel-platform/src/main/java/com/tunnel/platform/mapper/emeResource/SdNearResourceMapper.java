package com.tunnel.platform.mapper.emeResource;


import com.tunnel.platform.domain.emeResource.SdNearResource;

import java.util.List;

/**
 * 周边资源Mapper接口
 * 
 * @author ruoyi
 * @date 2021-11-22
 */
public interface SdNearResourceMapper 
{
    /**
     * 查询周边资源
     * 
     * @param id 周边资源ID
     * @return 周边资源
     */
    public SdNearResource selectSdNearResourceById(Long id);

    /**
     * 查询周边资源列表
     * 
     * @param sdNearResource 周边资源
     * @return 周边资源集合
     */
    public List<SdNearResource> selectSdNearResourceList(SdNearResource sdNearResource);

    /**
     * 新增周边资源
     * 
     * @param sdNearResource 周边资源
     * @return 结果
     */
    public int insertSdNearResource(SdNearResource sdNearResource);

    /**
     * 修改周边资源
     * 
     * @param sdNearResource 周边资源
     * @return 结果
     */
    public int updateSdNearResource(SdNearResource sdNearResource);

    /**
     * 删除周边资源
     * 
     * @param id 周边资源ID
     * @return 结果
     */
    public int deleteSdNearResourceById(Long id);

    /**
     * 批量删除周边资源
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdNearResourceByIds(Long[] ids);
}
