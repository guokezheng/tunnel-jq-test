package com.tunnel.business.service.dataInfo;

import java.util.List;
import com.tunnel.business.domain.dataInfo.TunnelAssociation;

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

    public TunnelAssociation selectTunnelAssociationByTunnelId(String tunnelId);

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
}
