package com.tunnel.business.service.emeResource;


import com.tunnel.business.domain.emeResource.SdEmergencyOrg;

import java.util.List;

/**
 * 应急机构Service接口
 *
 * @author ruoyi
 * @date 2022-08-09
 */
public interface ISdEmergencyOrgService {
    /**
     * 查询应急机构
     *
     * @param orgId 应急机构主键
     * @return 应急机构
     */
    SdEmergencyOrg selectSdEmergencyOrgByOrgId(Long orgId);

    /**
     * 查询应急机构列表
     *
     * @param sdEmergencyOrg 应急机构
     * @return 应急机构集合
     */
    List<SdEmergencyOrg> selectSdEmergencyOrgList(SdEmergencyOrg sdEmergencyOrg);

    /**
     * 新增应急机构
     *
     * @param sdEmergencyOrg 应急机构
     * @return 结果
     */
    int insertSdEmergencyOrg(SdEmergencyOrg sdEmergencyOrg);

    /**
     * 修改应急机构
     *
     * @param sdEmergencyOrg 应急机构
     * @return 结果
     */
    int updateSdEmergencyOrg(SdEmergencyOrg sdEmergencyOrg);

    /**
     * 批量删除应急机构
     *
     * @param orgIds 需要删除的应急机构主键集合
     * @return 结果
     */
    int deleteSdEmergencyOrgByOrgIds(Long[] orgIds);

    /**
     * 删除应急机构信息
     *
     * @param orgId 应急机构主键
     * @return 结果
     */
    int deleteSdEmergencyOrgByOrgId(Long orgId);
}
