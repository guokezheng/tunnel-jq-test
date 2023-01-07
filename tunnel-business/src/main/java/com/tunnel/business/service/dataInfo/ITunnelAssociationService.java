package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.TunnelAssociation;

import java.util.List;

/**
 * 隧道关联关系Service接口
 */
public interface ITunnelAssociationService
{
    /**
     * 查询隧道关联关系
     *
     * @param id 隧道关联关系主键
     * @return 隧道关联关系
     */
    public TunnelAssociation selectTunnelAssociationById(Long id);

    public List<TunnelAssociation> selectTunnelAssociationsByTunnelId(String tunnelId);

    /**
     * 查询隧道关联关系列表
     *
     * @param tunnelAssociation 隧道关联关系
     * @return 隧道关联关系集合
     */
    public List<TunnelAssociation> selectTunnelAssociationList(TunnelAssociation tunnelAssociation);

    /**
     * 新增隧道关联关系
     *
     * @param tunnelAssociation 隧道关联关系
     * @return 结果
     */
    public int insertTunnelAssociation(TunnelAssociation tunnelAssociation);

    /**
     * 修改隧道关联关系
     *
     * @param tunnelAssociation 隧道关联关系
     * @return 结果
     */
    public int updateTunnelAssociation(TunnelAssociation tunnelAssociation);

    /**
     * 批量删除隧道关联关系
     *
     * @param ids 需要删除的隧道关联关系主键集合
     * @return 结果
     */
    public int deleteTunnelAssociationByIds(Long[] ids);

    public int deleteTunnelAssociationByTunnelIds(String[] tunnelIds);

    /**
     * 删除隧道关联关系信息
     *
     * @param id 隧道关联关系主键
     * @return 结果
     */
    public int deleteTunnelAssociationById(Long id);

    int updateTunnelAssociations(List<TunnelAssociation> tunnelAssociations);

    List<TunnelAssociation> checkInsertUnique(TunnelAssociation tunnelAssociation);

    List<TunnelAssociation> checkUpdateUnique(TunnelAssociation tunnelAssociation);

    /**
     * 根据隧道、方向、外部系统 获取外部系统隧道洞ID
     * @param eqTunnelId
     * @param eqDirection
     * @param externalSystemId
     * @return
     */
    public String getExternalSystemTunnelId(String eqTunnelId, String eqDirection, Long externalSystemId);
}
