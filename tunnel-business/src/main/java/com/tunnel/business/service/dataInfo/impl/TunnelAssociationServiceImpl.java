package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.system.mapper.SysDictDataMapper;
import com.tunnel.business.datacenter.domain.enumeration.DictTypeEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.dataInfo.TunnelAssociation;
import com.tunnel.business.mapper.dataInfo.ExternalSystemMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.dataInfo.TunnelAssociationMapper;
import com.tunnel.business.service.dataInfo.ITunnelAssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 隧道关联关系Service业务层处理
 */
@Service
public class TunnelAssociationServiceImpl implements ITunnelAssociationService {
    @Autowired
    private TunnelAssociationMapper tunnelAssociationMapper;

    /**
     * 查询隧道关联关系
     *
     * @param id 隧道关联关系主键
     * @return 隧道关联关系
     */
    @Override
    public TunnelAssociation selectTunnelAssociationById(Long id) {
        return tunnelAssociationMapper.selectTunnelAssociationById(id);
    }

    @Override
    public List<TunnelAssociation> selectTunnelAssociationsByTunnelId(String tunnelId) {
        return tunnelAssociationMapper.selectTunnelAssociationsByTunnelId(tunnelId);
    }

    /**
     * 查询隧道关联关系列表
     *
     * @param tunnelAssociation 隧道关联关系
     * @return 隧道关联关系
     */
    @Override
    public List<TunnelAssociation> selectTunnelAssociationList(TunnelAssociation tunnelAssociation) {
        return tunnelAssociationMapper.selectTunnelAssociationList(tunnelAssociation);
    }

    /**
     * 新增隧道关联关系
     *
     * @param tunnelAssociation 隧道关联关系
     * @return 结果
     */
    @Override
    public int insertTunnelAssociation(TunnelAssociation tunnelAssociation) {
        return tunnelAssociationMapper.insertTunnelAssociation(tunnelAssociation);
    }

    /**
     * 修改隧道关联关系
     *
     * @param tunnelAssociation 隧道关联关系
     * @return 结果
     */
    @Override
    public int updateTunnelAssociation(TunnelAssociation tunnelAssociation) {
        return tunnelAssociationMapper.updateTunnelAssociation(tunnelAssociation);
    }

    /**
     * 批量删除隧道关联关系
     *
     * @param ids 需要删除的隧道关联关系主键
     * @return 结果
     */
    @Override
    public int deleteTunnelAssociationByIds(Long[] ids) {
        return tunnelAssociationMapper.deleteTunnelAssociationByIds(ids);
    }

    @Override
    public int deleteTunnelAssociationByTunnelIds(String[] tunnelIds) {
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
    public int deleteTunnelAssociationById(Long id) {
        return tunnelAssociationMapper.deleteTunnelAssociationById(id);
    }


    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;
    @Autowired
    private ExternalSystemMapper externalSystemMapper;
    @Autowired
    private SysDictDataMapper sysDictDataMapper;


    @Override

    public int updateTunnelAssociations(List<TunnelAssociation> tunnelAssociations) {
        int rows = 0;

        for (TunnelAssociation tunnelAssociation : tunnelAssociations) {
            String tunnelDirection = tunnelAssociation.getTunnelDirection();
            Long externalSystemId = tunnelAssociation.getExternalSystemId();
            Assert.hasText(tunnelDirection, "隧道方向和外部系统为必选项，请核对");
            Assert.notNull(externalSystemId, "隧道方向和外部系统为必选项，请核对");
        }

        for (TunnelAssociation tunnelAssociation : tunnelAssociations) {
            Long id = tunnelAssociation.getId();
            List<TunnelAssociation> list = null;
            if (ObjectUtils.isEmpty(id)) {
                list = checkInsertUnique(tunnelAssociation);
            } else {
                list = checkUpdateUnique(tunnelAssociation);

            }
            if (null != list && list.size() > 0) {
                String tunnelId = tunnelAssociation.getTunnelId();
                String tunnelDirection = tunnelAssociation.getTunnelDirection();
                Long externalSystemId = tunnelAssociation.getExternalSystemId();

                SdTunnels sdTunnels = sdTunnelsMapper.selectSdTunnelsById(tunnelId);
                String directionName = sysDictDataMapper.selectDictLabel(DictTypeEnum.sd_direction.getCode(), tunnelDirection);
                ExternalSystem externalSystem = externalSystemMapper.selectExternalSystemById(externalSystemId);

                String msg = "隧道名称为：【"+sdTunnels.getTunnelName() + "】 方向为【" + directionName + "】系统名称为 【" + externalSystem.getSystemName() + "】的数据有重复，请确认！";
                throw new RuntimeException(msg);
            }
        }


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

    @Override
    public List<TunnelAssociation> checkInsertUnique(TunnelAssociation tunnelAssociation) {

        return tunnelAssociationMapper.checkInsertUnique(tunnelAssociation);
    }

    @Override
    public List<TunnelAssociation> checkUpdateUnique(TunnelAssociation tunnelAssociation) {

        return tunnelAssociationMapper.checkUpdateUnique(tunnelAssociation);
    }


}
