package com.tunnel.business.service.emeResource.impl;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.emeResource.SdEmergencyOrg;
import com.tunnel.business.mapper.emeResource.SdEmergencyOrgMapper;
import com.tunnel.business.service.emeResource.ISdEmergencyOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应急机构Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-09
 */
@Service
public class SdEmergencyOrgServiceImpl implements ISdEmergencyOrgService {
    @Autowired
    private SdEmergencyOrgMapper sdEmergencyOrgMapper;

    /**
     * 查询应急机构列表
     *
     * @param sdEmergencyOrg 应急机构
     * @return 应急机构
     */
    @Override
    public List<SysDept> selectSdEmergencyOrgList(SdEmergencyOrg sdEmergencyOrg) {
        return sdEmergencyOrgMapper.selectSdEmergencyOrgList(sdEmergencyOrg);
    }
}
