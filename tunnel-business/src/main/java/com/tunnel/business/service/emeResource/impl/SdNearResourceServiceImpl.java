package com.tunnel.business.service.emeResource.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.emeResource.SdNearResource;
import com.tunnel.business.mapper.emeResource.SdNearResourceMapper;
import com.tunnel.business.service.emeResource.ISdNearResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 周边资源Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-22
 */
@Service
public class SdNearResourceServiceImpl implements ISdNearResourceService {
    @Autowired
    private SdNearResourceMapper sdNearResourceMapper;

    /**
     * 查询周边资源
     *
     * @param id 周边资源ID
     * @return 周边资源
     */
    @Override
    public SdNearResource selectSdNearResourceById(Long id) {
        return sdNearResourceMapper.selectSdNearResourceById(id);
    }

    /**
     * 查询周边资源列表
     *
     * @param sdNearResource 周边资源
     * @return 周边资源
     */
    @Override
    public List<SdNearResource> selectSdNearResourceList(SdNearResource sdNearResource) {
        Long deptId = SecurityUtils.getDeptId();
        sdNearResource.getParams().put("deptId", deptId);
        return sdNearResourceMapper.selectSdNearResourceList(sdNearResource);
    }

    /**
     * 新增周边资源
     *
     * @param sdNearResource 周边资源
     * @return 结果
     */
    @Override
    public int insertSdNearResource(SdNearResource sdNearResource) {
        sdNearResource.setCreateTime(DateUtils.getNowDate());
        return sdNearResourceMapper.insertSdNearResource(sdNearResource);
    }

    /**
     * 修改周边资源
     *
     * @param sdNearResource 周边资源
     * @return 结果
     */
    @Override
    public int updateSdNearResource(SdNearResource sdNearResource) {
        sdNearResource.setUpdateTime(DateUtils.getNowDate());
        return sdNearResourceMapper.updateSdNearResource(sdNearResource);
    }

    /**
     * 批量删除周边资源
     *
     * @param ids 需要删除的周边资源ID
     * @return 结果
     */
    @Override
    public int deleteSdNearResourceByIds(Long[] ids) {
        return sdNearResourceMapper.deleteSdNearResourceByIds(ids);
    }

    /**
     * 删除周边资源信息
     *
     * @param id 周边资源ID
     * @return 结果
     */
    @Override
    public int deleteSdNearResourceById(Long id) {
        return sdNearResourceMapper.deleteSdNearResourceById(id);
    }
}
