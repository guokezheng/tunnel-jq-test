package com.tunnel.platform.service.dataInfo.impl;

import com.tunnel.platform.domain.dataInfo.SdCameraDetails;
import com.tunnel.platform.mapper.dataInfo.SdCameraDetailsMapper;
import com.tunnel.platform.service.dataInfo.ISdCameraDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 摄像机详情Service业务层处理
 * 
 * @author liubaokui
 * @date 2021-05-08
 */
@Service
public class SdCameraDetailsServiceImpl implements ISdCameraDetailsService
{
    @Autowired
    private SdCameraDetailsMapper sdCameraDetailsMapper;

    /**
     * 查询摄像机详情
     * 
     * @param id 摄像机详情ID
     * @return 摄像机详情
     */
    @Override
    public SdCameraDetails selectSdCameraDetailsById(Long id)
    {
        return sdCameraDetailsMapper.selectSdCameraDetailsById(id);
    }

    /**
     * 查询摄像机详情
     * 
     * @param id 摄像机详情ID
     * @return 摄像机详情
     */
    @Override
    public SdCameraDetails selectSdCameraDetailsByCamId(String camId)
    {
        return sdCameraDetailsMapper.selectSdCameraDetailsByCamId(camId);
    }
    
    /**
     * 查询摄像机详情列表
     * 
     * @param sdCameraDetails 摄像机详情
     * @return 摄像机详情
     */
    @Override
    public List<SdCameraDetails> selectSdCameraDetailsList(SdCameraDetails sdCameraDetails)
    {
        return sdCameraDetailsMapper.selectSdCameraDetailsList(sdCameraDetails);
    }

    /**
     * 新增摄像机详情
     * 
     * @param sdCameraDetails 摄像机详情
     * @return 结果
     */
    @Override
    public int insertSdCameraDetails(SdCameraDetails sdCameraDetails)
    {
        return sdCameraDetailsMapper.insertSdCameraDetails(sdCameraDetails);
    }

    /**
     * 修改摄像机详情
     * 
     * @param sdCameraDetails 摄像机详情
     * @return 结果
     */
    @Override
    public int updateSdCameraDetails(SdCameraDetails sdCameraDetails)
    {
        return sdCameraDetailsMapper.updateSdCameraDetails(sdCameraDetails);
    }

    /**
     * 批量删除摄像机详情
     * 
     * @param ids 需要删除的摄像机详情ID
     * @return 结果
     */
    @Override
    public int deleteSdCameraDetailsByIds(Long[] ids)
    {
        return sdCameraDetailsMapper.deleteSdCameraDetailsByIds(ids);
    }

    /**
     * 删除摄像机详情信息
     * 
     * @param id 摄像机详情ID
     * @return 结果
     */
    @Override
    public int deleteSdCameraDetailsById(Long id)
    {
        return sdCameraDetailsMapper.deleteSdCameraDetailsById(id);
    }
}
