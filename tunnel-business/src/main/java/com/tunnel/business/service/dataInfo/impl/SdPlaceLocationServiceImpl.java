package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdPlaceLocation;
import com.tunnel.business.mapper.dataInfo.SdPlaceLocationMapper;
import com.tunnel.business.service.dataInfo.ISdPlaceLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 安装场所Service业务层处理
 *
 * @author yanghousheng
 * @date 2020-11-23
 */
@Service
public class SdPlaceLocationServiceImpl implements ISdPlaceLocationService {
    @Autowired
    private SdPlaceLocationMapper sdPlaceLocationMapper;

    /**
     * 查询安装场所
     *
     * @param placeId 安装场所ID
     * @return 安装场所
     */
    @Override
    public SdPlaceLocation selectSdPlaceLocationById(Long placeId) {
        return sdPlaceLocationMapper.selectSdPlaceLocationById(placeId);
    }

    /**
     * 查询安装场所列表
     *
     * @param sdPlaceLocation 安装场所
     * @return 安装场所
     */
    @Override
    public List<SdPlaceLocation> selectSdPlaceLocationList(SdPlaceLocation sdPlaceLocation) {
        return sdPlaceLocationMapper.selectSdPlaceLocationList(sdPlaceLocation);
    }

    /**
     * 新增安装场所
     *
     * @param sdPlaceLocation 安装场所
     * @return 结果
     */
    @Override
    public int insertSdPlaceLocation(SdPlaceLocation sdPlaceLocation) {
        sdPlaceLocation.setCreateTime(DateUtils.getNowDate());
        return sdPlaceLocationMapper.insertSdPlaceLocation(sdPlaceLocation);
    }

    /**
     * 修改安装场所
     *
     * @param sdPlaceLocation 安装场所
     * @return 结果
     */
    @Override
    public int updateSdPlaceLocation(SdPlaceLocation sdPlaceLocation) {
        sdPlaceLocation.setUpdateTime(DateUtils.getNowDate());
        return sdPlaceLocationMapper.updateSdPlaceLocation(sdPlaceLocation);
    }

    /**
     * 批量删除安装场所
     *
     * @param placeIds 需要删除的安装场所ID
     * @return 结果
     */
    @Override
    public int deleteSdPlaceLocationByIds(Long[] placeIds) {
        return sdPlaceLocationMapper.deleteSdPlaceLocationByIds(placeIds);
    }

    /**
     * 删除安装场所信息
     *
     * @param placeId 安装场所ID
     * @return 结果
     */
    @Override
    public int deleteSdPlaceLocationById(Long placeId) {
        return sdPlaceLocationMapper.deleteSdPlaceLocationById(placeId);
    }
}
