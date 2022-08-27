package com.tunnel.platform.service.emeResource.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.emeResource.SdEmergencyOrg;
import com.tunnel.platform.mapper.emeResource.SdEmergencyOrgMapper;
import com.tunnel.platform.service.emeResource.ISdEmergencyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 应急机构Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-08-09
 */
@Service
public class SdEmergencyOrgServiceImpl implements ISdEmergencyOrgService
{
    @Autowired
    private SdEmergencyOrgMapper sdEmergencyOrgMapper;

    /**
     * 查询应急机构
     * 
     * @param orgId 应急机构主键
     * @return 应急机构
     */
    @Override
    public SdEmergencyOrg selectSdEmergencyOrgByOrgId(Long orgId)
    {
        return sdEmergencyOrgMapper.selectSdEmergencyOrgByOrgId(orgId);
    }

    /**
     * 查询应急机构列表
     * 
     * @param sdEmergencyOrg 应急机构
     * @return 应急机构
     */
    @Override
    public List<SdEmergencyOrg> selectSdEmergencyOrgList(SdEmergencyOrg sdEmergencyOrg)
    {
        return sdEmergencyOrgMapper.selectSdEmergencyOrgList(sdEmergencyOrg);
    }

    /**
     * 新增应急机构
     * 
     * @param sdEmergencyOrg 应急机构
     * @return 结果
     */
    @Override
    public int insertSdEmergencyOrg(SdEmergencyOrg sdEmergencyOrg)
    {
        sdEmergencyOrg.setCreateTime(DateUtils.getNowDate());
        return sdEmergencyOrgMapper.insertSdEmergencyOrg(sdEmergencyOrg);
    }

    /**
     * 修改应急机构
     * 
     * @param sdEmergencyOrg 应急机构
     * @return 结果
     */
    @Override
    public int updateSdEmergencyOrg(SdEmergencyOrg sdEmergencyOrg)
    {
        sdEmergencyOrg.setUpdateTime(DateUtils.getNowDate());
        return sdEmergencyOrgMapper.updateSdEmergencyOrg(sdEmergencyOrg);
    }

    /**
     * 批量删除应急机构
     * 
     * @param orgIds 需要删除的应急机构主键
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyOrgByOrgIds(Long[] orgIds)
    {
        return sdEmergencyOrgMapper.deleteSdEmergencyOrgByOrgIds(orgIds);
    }

    /**
     * 删除应急机构信息
     * 
     * @param orgId 应急机构主键
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyOrgByOrgId(Long orgId)
    {
        return sdEmergencyOrgMapper.deleteSdEmergencyOrgByOrgId(orgId);
    }
}
