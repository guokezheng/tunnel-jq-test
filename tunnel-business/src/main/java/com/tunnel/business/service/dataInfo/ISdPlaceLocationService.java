package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdPlaceLocation;

import java.util.List;

/**
 * 安装场所Service接口
 *
 * @author yanghousheng
 * @date 2020-11-23
 */
public interface ISdPlaceLocationService {
    /**
     * 查询安装场所
     *
     * @param placeId 安装场所ID
     * @return 安装场所
     */
    SdPlaceLocation selectSdPlaceLocationById(Long placeId);

    /**
     * 查询安装场所列表
     *
     * @param sdPlaceLocation 安装场所
     * @return 安装场所集合
     */
    List<SdPlaceLocation> selectSdPlaceLocationList(SdPlaceLocation sdPlaceLocation);

    /**
     * 新增安装场所
     *
     * @param sdPlaceLocation 安装场所
     * @return 结果
     */
    int insertSdPlaceLocation(SdPlaceLocation sdPlaceLocation);

    /**
     * 修改安装场所
     *
     * @param sdPlaceLocation 安装场所
     * @return 结果
     */
    int updateSdPlaceLocation(SdPlaceLocation sdPlaceLocation);

    /**
     * 批量删除安装场所
     *
     * @param placeIds 需要删除的安装场所ID
     * @return 结果
     */
    int deleteSdPlaceLocationByIds(Long[] placeIds);

    /**
     * 删除安装场所信息
     *
     * @param placeId 安装场所ID
     * @return 结果
     */
    int deleteSdPlaceLocationById(Long placeId);
}
