package com.tunnel.platform.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.dataInfo.SdFixedCode;
import com.tunnel.platform.mapper.dataInfo.SdFixedCodeMapper;
import com.tunnel.platform.service.dataInfo.ISdFixedCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 指令码固定字段表Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-04-10
 */
@Service
public class SdFixedCodeServiceImpl implements ISdFixedCodeService
{
    @Autowired
    private SdFixedCodeMapper sdFixedCodeMapper;

    /**
     * 查询指令码固定字段表
     * 
     * @param id 指令码固定字段表ID
     * @return 指令码固定字段表
     */
    @Override
    public SdFixedCode selectSdFixedCodeById(Long id)
    {
        return sdFixedCodeMapper.selectSdFixedCodeById(id);
    }

    /**
     * 查询指令码固定字段表列表
     * 
     * @param sdFixedCode 指令码固定字段表
     * @return 指令码固定字段表
     */
    @Override
    public List<SdFixedCode> selectSdFixedCodeList(SdFixedCode sdFixedCode)
    {
        return sdFixedCodeMapper.selectSdFixedCodeList(sdFixedCode);
    }

    /**
     * 新增指令码固定字段表
     * 
     * @param sdFixedCode 指令码固定字段表
     * @return 结果
     */
    @Override
    public int insertSdFixedCode(SdFixedCode sdFixedCode)
    {
        sdFixedCode.setCreateTime(DateUtils.getNowDate());
        return sdFixedCodeMapper.insertSdFixedCode(sdFixedCode);
    }

    /**
     * 修改指令码固定字段表
     * 
     * @param sdFixedCode 指令码固定字段表
     * @return 结果
     */
    @Override
    public int updateSdFixedCode(SdFixedCode sdFixedCode)
    {
        sdFixedCode.setUpdateTime(DateUtils.getNowDate());
        return sdFixedCodeMapper.updateSdFixedCode(sdFixedCode);
    }

    /**
     * 批量删除指令码固定字段表
     * 
     * @param ids 需要删除的指令码固定字段表ID
     * @return 结果
     */
    @Override
    public int deleteSdFixedCodeByIds(Long[] ids)
    {
        return sdFixedCodeMapper.deleteSdFixedCodeByIds(ids);
    }

    /**
     * 删除指令码固定字段表信息
     * 
     * @param id 指令码固定字段表ID
     * @return 结果
     */
    @Override
    public int deleteSdFixedCodeById(Long id)
    {
        return sdFixedCodeMapper.deleteSdFixedCodeById(id);
    }
}
