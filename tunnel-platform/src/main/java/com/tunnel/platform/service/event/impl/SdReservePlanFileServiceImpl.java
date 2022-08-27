package com.tunnel.platform.service.event.impl;

import com.tunnel.platform.domain.event.SdReservePlanFile;
import com.tunnel.platform.mapper.event.SdReservePlanFileMapper;
import com.tunnel.platform.service.event.ISdReservePlanFileService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预案文件Service业务层处理
 * 
 * @author gongfanfei
 * @date 2020-09-10
 */
@Service
public class SdReservePlanFileServiceImpl implements ISdReservePlanFileService 
{
    @Autowired
    private SdReservePlanFileMapper sdReservePlanFileMapper;

    /**
     * 查询预案文件
     * 
     * @param id 预案文件ID
     * @return 预案文件
     */
    @Override
    public SdReservePlanFile selectSdReservePlanFileById(Long id)
    {
        return sdReservePlanFileMapper.selectSdReservePlanFileById(id);
    }

    /**
     * 查询预案文件列表
     * 
     * @param sdReservePlanFile 预案文件
     * @return 预案文件
     */
    @Override
    public List<SdReservePlanFile> selectSdReservePlanFileList(SdReservePlanFile sdReservePlanFile)
    {
        return sdReservePlanFileMapper.selectSdReservePlanFileList(sdReservePlanFile);
    }

    /**
     * 新增预案文件
     * 
     * @param sdReservePlanFile 预案文件
     * @return 结果
     */
    @Override
    public int insertSdReservePlanFile(SdReservePlanFile sdReservePlanFile)
    {
        sdReservePlanFile.setCreateTime(DateUtils.getNowDate());
        return sdReservePlanFileMapper.insertSdReservePlanFile(sdReservePlanFile);
    }

    /**
     * 修改预案文件
     * 
     * @param sdReservePlanFile 预案文件
     * @return 结果
     */
    @Override
    public int updateSdReservePlanFile(SdReservePlanFile sdReservePlanFile)
    {
        sdReservePlanFile.setUpdateTime(DateUtils.getNowDate());
        return sdReservePlanFileMapper.updateSdReservePlanFile(sdReservePlanFile);
    }

    /**
     * 批量删除预案文件
     * 
     * @param ids 需要删除的预案文件ID
     * @return 结果
     */
    @Override
    public int deleteSdReservePlanFileByIds(Long[] ids)
    {

        return sdReservePlanFileMapper.deleteSdReservePlanFileByIds(ids);
    }

    /**
     * 删除预案文件信息
     * 
     * @param id 预案文件ID
     * @return 结果
     */
    @Override
    public int deleteSdReservePlanFileById(Long id)
    {
        return sdReservePlanFileMapper.deleteSdReservePlanFileById(id);
    }
}