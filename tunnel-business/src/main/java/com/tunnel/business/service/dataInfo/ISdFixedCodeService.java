package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdFixedCode;

import java.util.List;

/**
 * 指令码固定字段表Service接口
 *
 * @author ruoyi
 * @date 2021-04-10
 */
public interface ISdFixedCodeService {
    /**
     * 查询指令码固定字段表
     *
     * @param id 指令码固定字段表ID
     * @return 指令码固定字段表
     */
    SdFixedCode selectSdFixedCodeById(Long id);

    /**
     * 查询指令码固定字段表列表
     *
     * @param sdFixedCode 指令码固定字段表
     * @return 指令码固定字段表集合
     */
    List<SdFixedCode> selectSdFixedCodeList(SdFixedCode sdFixedCode);

    /**
     * 新增指令码固定字段表
     *
     * @param sdFixedCode 指令码固定字段表
     * @return 结果
     */
    int insertSdFixedCode(SdFixedCode sdFixedCode);

    /**
     * 修改指令码固定字段表
     *
     * @param sdFixedCode 指令码固定字段表
     * @return 结果
     */
    int updateSdFixedCode(SdFixedCode sdFixedCode);

    /**
     * 批量删除指令码固定字段表
     *
     * @param ids 需要删除的指令码固定字段表ID
     * @return 结果
     */
    int deleteSdFixedCodeByIds(Long[] ids);

    /**
     * 删除指令码固定字段表信息
     *
     * @param id 指令码固定字段表ID
     * @return 结果
     */
    int deleteSdFixedCodeById(Long id);
}
