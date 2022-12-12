package com.tunnel.business.service.dataInfo.impl;

import com.tunnel.business.domain.dataInfo.TunnelAssociation;
import com.tunnel.business.mapper.dataInfo.TunnelAssociationMapper;
import com.tunnel.business.service.dataInfo.ITunnelAssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 隧道关联关系Service业务层处理
 */
@Service
public class TunnelAssociationServiceImpl implements ITunnelAssociationService
{
    @Autowired
    private TunnelAssociationMapper tunnelAssociationMapper;

    /**
     * 查询隧道关联关系
     *
     * @param id 隧道关联关系主键
     * @return 隧道关联关系
     */
    @Override
    public TunnelAssociation selectTunnelAssociationById(Long id)
    {
        return tunnelAssociationMapper.selectTunnelAssociationById(id);
    }

    @Override
    public List<TunnelAssociation> selectTunnelAssociationsByTunnelId(String tunnelId)
    {
        return tunnelAssociationMapper.selectTunnelAssociationsByTunnelId(tunnelId);
    }

    /**
     * 查询隧道关联关系列表
     *
     * @param tunnelAssociation 隧道关联关系
     * @return 隧道关联关系
     */
    @Override
    public List<TunnelAssociation> selectTunnelAssociationList(TunnelAssociation tunnelAssociation)
    {
        return tunnelAssociationMapper.selectTunnelAssociationList(tunnelAssociation);
    }

    /**
     * 新增隧道关联关系
     *
     * @param tunnelAssociation 隧道关联关系
     * @return 结果
     */
    @Override
    public int insertTunnelAssociation(TunnelAssociation tunnelAssociation)
    {
        return tunnelAssociationMapper.insertTunnelAssociation(tunnelAssociation);
    }

    /**
     * 修改隧道关联关系
     *
     * @param tunnelAssociation 隧道关联关系
     * @return 结果
     */
    @Override
    public int updateTunnelAssociation(TunnelAssociation tunnelAssociation)
    {
        return tunnelAssociationMapper.updateTunnelAssociation(tunnelAssociation);
    }

    /**
     * 批量删除隧道关联关系
     *
     * @param ids 需要删除的隧道关联关系主键
     * @return 结果
     */
    @Override
    public int deleteTunnelAssociationByIds(Long[] ids)
    {
        return tunnelAssociationMapper.deleteTunnelAssociationByIds(ids);
    }

    @Override
    public int deleteTunnelAssociationByTunnelIds(String[] tunnelIds)
    {
        if (tunnelIds.length > 0) {
            return tunnelAssociationMapper.deleteTunnelAssociationByTunnelIds(tunnelIds);
        } else {
            return 0;
        }
    }

    /**
     * 删除隧道关联关系信息
     *
     * @param id 隧道关联关系主键
     * @return 结果
     */
    @Override
    public int deleteTunnelAssociationById(Long id)
    {
        return tunnelAssociationMapper.deleteTunnelAssociationById(id);
    }

    @Override
    public int updateTunnelAssociations(List<TunnelAssociation> tunnelAssociations) {
        int rows = 0;
        for (TunnelAssociation tunnelAssociation : tunnelAssociations) {
            Long id = tunnelAssociation.getId();
            if (ObjectUtils.isEmpty(id)) {
                int i = tunnelAssociationMapper.insertTunnelAssociation(tunnelAssociation);
                rows += i;
            } else {
                int j = tunnelAssociationMapper.updateTunnelAssociation(tunnelAssociation);
                rows += j;
            }
        }
        return rows;
    }


}
