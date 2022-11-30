package com.tunnel.business.service.emeResource;


import com.ruoyi.common.core.domain.entity.SysDept;
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
     * 查询应急机构列表
     *
     * @param sdEmergencyOrg 应急机构
     * @return 应急机构集合
     */
    List<SysDept> selectSdEmergencyOrgList(SdEmergencyOrg sdEmergencyOrg);
}
