package com.tunnel.business.service.emeResource;


import com.tunnel.business.domain.emeResource.SdNearResource;

import java.util.List;

/**
 * 周边资源Service接口
 *
 * @author ruoyi
 * @date 2021-11-22
 */
public interface ISdNearResourceService {
    /**
     * 查询周边资源
     *
     * @param id 周边资源ID
     * @return 周边资源
     */
    SdNearResource selectSdNearResourceById(Long id);

    /**
     * 查询周边资源列表
     *
     * @param sdNearResource 周边资源
     * @return 周边资源集合
     */
    List<SdNearResource> selectSdNearResourceList(SdNearResource sdNearResource);

    /**
     * 新增周边资源
     *
     * @param sdNearResource 周边资源
     * @return 结果
     */
    int insertSdNearResource(SdNearResource sdNearResource);

    /**
     * 修改周边资源
     *
     * @param sdNearResource 周边资源
     * @return 结果
     */
    int updateSdNearResource(SdNearResource sdNearResource);

    /**
     * 批量删除周边资源
     *
     * @param ids 需要删除的周边资源ID
     * @return 结果
     */
    int deleteSdNearResourceByIds(Long[] ids);

    /**
     * 删除周边资源信息
     *
     * @param id 周边资源ID
     * @return 结果
     */
    int deleteSdNearResourceById(Long id);
}
