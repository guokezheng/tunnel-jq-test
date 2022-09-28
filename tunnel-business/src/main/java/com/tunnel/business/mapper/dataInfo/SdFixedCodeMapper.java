package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdFixedCode;

import java.util.List;

/**
 * 指令码固定字段表Mapper接口
 * 
 * @author ruoyi
 * @date 2021-04-10
 */
public interface SdFixedCodeMapper 
{
    /**
     * 查询指令码固定字段表
     * 
     * @param id 指令码固定字段表ID
     * @return 指令码固定字段表
     */
    public SdFixedCode selectSdFixedCodeById(Long id);

    /**
     * 查询指令码固定字段表列表
     * 
     * @param sdFixedCode 指令码固定字段表
     * @return 指令码固定字段表集合
     */
    public List<SdFixedCode> selectSdFixedCodeList(SdFixedCode sdFixedCode);

    /**
     * 新增指令码固定字段表
     * 
     * @param sdFixedCode 指令码固定字段表
     * @return 结果
     */
    public int insertSdFixedCode(SdFixedCode sdFixedCode);

    /**
     * 修改指令码固定字段表
     * 
     * @param sdFixedCode 指令码固定字段表
     * @return 结果
     */
    public int updateSdFixedCode(SdFixedCode sdFixedCode);

    /**
     * 删除指令码固定字段表
     * 
     * @param id 指令码固定字段表ID
     * @return 结果
     */
    public int deleteSdFixedCodeById(Long id);

    /**
     * 批量删除指令码固定字段表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdFixedCodeByIds(Long[] ids);
}
