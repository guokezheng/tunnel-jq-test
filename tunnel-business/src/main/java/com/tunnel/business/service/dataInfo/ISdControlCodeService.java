package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdControlCode;

import java.util.List;

/**
 * 控制码Service接口
 *
 * @author zhangweitian
 * @date 2020-09-08
 */
public interface ISdControlCodeService {
    /**
     * 查询控制码
     *
     * @param controlId 控制码ID
     * @return 控制码
     */
    SdControlCode selectSdControlCodeById(Long controlId);

    /**
     * 查询控制码列表
     *
     * @param sdControlCode 控制码
     * @return 控制码集合
     */
    List<SdControlCode> selectSdControlCodeList(SdControlCode sdControlCode);

    /**
     * 通过key查询控制码
     *
     * @param sdControlCode
     * @return
     */
    List<SdControlCode> selectSdControlCodeByKey(SdControlCode sdControlCode);

    /**
     * 新增控制码
     *
     * @param sdControlCode 控制码
     * @return 结果
     */
    int insertSdControlCode(SdControlCode sdControlCode);

    /**
     * 修改控制码
     *
     * @param sdControlCode 控制码
     * @return 结果
     */
    int updateSdControlCode(SdControlCode sdControlCode);

    /**
     * 批量删除控制码
     *
     * @param controlIds 需要删除的控制码ID
     * @return 结果
     */
    int deleteSdControlCodeByIds(Long[] controlIds);

    /**
     * 删除控制码信息
     *
     * @param controlId 控制码ID
     * @return 结果
     */
    int deleteSdControlCodeById(Long controlId);
}