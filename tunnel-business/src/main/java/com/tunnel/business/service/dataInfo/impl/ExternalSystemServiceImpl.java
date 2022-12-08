package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.mapper.dataInfo.ExternalSystemMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 外部系统Service业务层处理
 */
@Service
public class ExternalSystemServiceImpl implements IExternalSystemService
{
    @Autowired
    private ExternalSystemMapper externalSystemMapper;

    /**
     * 查询外部系统
     *
     * @param id 外部系统主键
     * @return 外部系统
     */
    @Override
    public ExternalSystem selectExternalSystemById(Long id)
    {
        return externalSystemMapper.selectExternalSystemById(id);
    }

    /**
     * 查询外部系统
     *
     * @param externalSystem 外部系统
     * @return 外部系统
     */
    @Override
    public ExternalSystem getByBrandAndDept(ExternalSystem externalSystem)
    {
        return externalSystemMapper.getByBrandAndDept(externalSystem);
    }

    /**
     * 查询外部系统列表
     *
     * @param externalSystem 外部系统
     * @return 外部系统
     */
    @Override
    public List<ExternalSystem> selectExternalSystemList(ExternalSystem externalSystem)
    {
        return externalSystemMapper.selectExternalSystemList(externalSystem);
    }

    /**
     * 新增外部系统
     *
     * @param externalSystem 外部系统
     * @return 结果
     */
    @Override
    public int insertExternalSystem(ExternalSystem externalSystem)
    {
        externalSystem.setCreateTime(DateUtils.getNowDate());
        return externalSystemMapper.insertExternalSystem(externalSystem);
    }

    /**
     * 修改外部系统
     *
     * @param externalSystem 外部系统
     * @return 结果
     */
    @Override
    public int updateExternalSystem(ExternalSystem externalSystem)
    {
        externalSystem.setUpdateTime(DateUtils.getNowDate());
        return externalSystemMapper.updateExternalSystem(externalSystem);
    }

    /**
     * 批量删除外部系统
     *
     * @param ids 需要删除的外部系统主键
     * @return 结果
     */
    @Override
    public int deleteExternalSystemByIds(Long[] ids)
    {
        return externalSystemMapper.deleteExternalSystemByIds(ids);
    }

    /**
     * 删除外部系统信息
     *
     * @param id 外部系统主键
     * @return 结果
     */
    @Override
    public int deleteExternalSystemById(Long id)
    {
        return externalSystemMapper.deleteExternalSystemById(id);
    }
}
