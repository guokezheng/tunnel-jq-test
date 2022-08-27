package com.tunnel.platform.service.emeResource;

import java.util.List;
import com.tunnel.platform.domain.emeResource.SdEmergencyOrg;

/**
 * 应急机构Service接口
 * 
 * @author ruoyi
 * @date 2022-08-09
 */
public interface ISdEmergencyOrgService 
{
    /**
     * 查询应急机构
     * 
     * @param orgId 应急机构主键
     * @return 应急机构
     */
    public SdEmergencyOrg selectSdEmergencyOrgByOrgId(Long orgId);

    /**
     * 查询应急机构列表
     * 
     * @param sdEmergencyOrg 应急机构
     * @return 应急机构集合
     */
    public List<SdEmergencyOrg> selectSdEmergencyOrgList(SdEmergencyOrg sdEmergencyOrg);

    /**
     * 新增应急机构
     * 
     * @param sdEmergencyOrg 应急机构
     * @return 结果
     */
    public int insertSdEmergencyOrg(SdEmergencyOrg sdEmergencyOrg);

    /**
     * 修改应急机构
     * 
     * @param sdEmergencyOrg 应急机构
     * @return 结果
     */
    public int updateSdEmergencyOrg(SdEmergencyOrg sdEmergencyOrg);

    /**
     * 批量删除应急机构
     * 
     * @param orgIds 需要删除的应急机构主键集合
     * @return 结果
     */
    public int deleteSdEmergencyOrgByOrgIds(Long[] orgIds);

    /**
     * 删除应急机构信息
     * 
     * @param orgId 应急机构主键
     * @return 结果
     */
    public int deleteSdEmergencyOrgByOrgId(Long orgId);
}
