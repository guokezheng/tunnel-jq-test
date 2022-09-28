package com.tunnel.business.service.event;


import com.tunnel.business.domain.event.SdEmergencyPer;

import java.util.List;

/**
 * 应急人员信息Service接口
 *
 * @author ruoyi
 * @date 2021-05-12
 */
public interface ISdEmergencyPerService {
    /**
     * 查询应急人员信息
     *
     * @param id 应急人员信息ID
     * @return 应急人员信息
     */
    SdEmergencyPer selectSdEmergencyPerById(Long id);

    /**
     * 查询应急人员信息列表
     *
     * @param sdEmergencyPer 应急人员信息
     * @return 应急人员信息集合
     */
    List<SdEmergencyPer> selectSdEmergencyPerList(SdEmergencyPer sdEmergencyPer);

    /**
     * 新增应急人员信息
     *
     * @param sdEmergencyPer 应急人员信息
     * @return 结果
     */
    int insertSdEmergencyPer(SdEmergencyPer sdEmergencyPer);

    /**
     * 修改应急人员信息
     *
     * @param sdEmergencyPer 应急人员信息
     * @return 结果
     */
    int updateSdEmergencyPer(SdEmergencyPer sdEmergencyPer);

    /**
     * 批量删除应急人员信息
     *
     * @param ids 需要删除的应急人员信息ID
     * @return 结果
     */
    int deleteSdEmergencyPerByIds(Long[] ids);

    /**
     * 删除应急人员信息信息
     *
     * @param id 应急人员信息ID
     * @return 结果
     */
    int deleteSdEmergencyPerById(Long id);
}
