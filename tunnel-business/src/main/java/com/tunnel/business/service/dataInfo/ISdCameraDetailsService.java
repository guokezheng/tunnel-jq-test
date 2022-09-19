package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdCameraDetails;

import java.util.List;

/**
 * 摄像机详情Service接口
 *
 * @author liubaokui
 * @date 2021-05-08
 */
public interface ISdCameraDetailsService {
    /**
     * 查询摄像机详情
     *
     * @param id 摄像机详情ID
     * @return 摄像机详情
     */
    SdCameraDetails selectSdCameraDetailsById(Long id);

    /**
     * 查询摄像机详情
     *
     * @param id 摄像机详情ID
     * @return 摄像机详情
     */
    SdCameraDetails selectSdCameraDetailsByCamId(String camid);

    /**
     * 查询摄像机详情列表
     *
     * @param sdCameraDetails 摄像机详情
     * @return 摄像机详情集合
     */
    List<SdCameraDetails> selectSdCameraDetailsList(SdCameraDetails sdCameraDetails);

    /**
     * 新增摄像机详情
     *
     * @param sdCameraDetails 摄像机详情
     * @return 结果
     */
    int insertSdCameraDetails(SdCameraDetails sdCameraDetails);

    /**
     * 修改摄像机详情
     *
     * @param sdCameraDetails 摄像机详情
     * @return 结果
     */
    int updateSdCameraDetails(SdCameraDetails sdCameraDetails);

    /**
     * 批量删除摄像机详情
     *
     * @param ids 需要删除的摄像机详情ID
     * @return 结果
     */
    int deleteSdCameraDetailsByIds(Long[] ids);

    /**
     * 删除摄像机详情信息
     *
     * @param id 摄像机详情ID
     * @return 结果
     */
    int deleteSdCameraDetailsById(Long id);
}
