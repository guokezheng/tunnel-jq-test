package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.TunnelAssociation;

import java.util.List;

/**
 * 隧道关联关系Mapper接口
 */
public interface TunnelAssociationMapper
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
     * 删除隧道关联关系
     *
     * @param id 隧道关联关系主键
     * @return 结果
     */
    public int deleteTunnelAssociationById(Long id);

    /**
     * 批量删除隧道关联关系
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTunnelAssociationByIds(Long[] ids);

    public int deleteTunnelAssociationByTunnelIds(String[] tunnelIds);

    List<TunnelAssociation> checkInsertUnique(TunnelAssociation tunnelAssociation);

    List<TunnelAssociation> checkUpdateUnique(TunnelAssociation tunnelAssociation);

}
