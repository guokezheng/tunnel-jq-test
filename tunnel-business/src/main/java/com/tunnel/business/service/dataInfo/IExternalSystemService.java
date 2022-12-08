package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.ExternalSystem;

import java.util.List;

/**
 * 外部系统Service接口

 */
public interface IExternalSystemService
{
    /**
     * 查询外部系统
     *
     * @param id 外部系统主键
     * @return 外部系统
     */
    public ExternalSystem selectExternalSystemById(Long id);

    /**
     * 查询外部系统
     *
     * @param externalSystem 外部系统
     * @return 外部系统
     */
    public ExternalSystem getByBrandAndDept(ExternalSystem externalSystem);


    /**
     * 查询外部系统列表
     *
     * @param externalSystem 外部系统
     * @return 外部系统集合
     */
    public List<ExternalSystem> selectExternalSystemList(ExternalSystem externalSystem);

    /**
     * 新增外部系统
     *
     * @param externalSystem 外部系统
     * @return 结果
     */
    public int insertExternalSystem(ExternalSystem externalSystem);

    /**
     * 修改外部系统
     *
     * @param externalSystem 外部系统
     * @return 结果
     */
    public int updateExternalSystem(ExternalSystem externalSystem);

    /**
     * 批量删除外部系统
     *
     * @param ids 需要删除的外部系统主键集合
     * @return 结果
     */
    public int deleteExternalSystemByIds(Long[] ids);

    /**
     * 删除外部系统信息
     *
     * @param id 外部系统主键
     * @return 结果
     */
    public int deleteExternalSystemById(Long id);
}
