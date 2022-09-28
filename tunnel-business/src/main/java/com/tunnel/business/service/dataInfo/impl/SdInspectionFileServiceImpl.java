package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdInspectionFile;
import com.tunnel.business.mapper.dataInfo.SdInspectionFileMapper;
import com.tunnel.business.service.dataInfo.ISdInspectionFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 巡检任务文件Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-25
 */
@Service
public class SdInspectionFileServiceImpl implements ISdInspectionFileService {
    @Autowired
    private SdInspectionFileMapper sdInspectionFileMapper;

    /**
     * 查询巡检任务文件
     *
     * @param id 巡检任务文件主键
     * @return 巡检任务文件
     */
    @Override
    public SdInspectionFile selectSdInspectionFileById(Long id) {
        return sdInspectionFileMapper.selectSdInspectionFileById(id);
    }

    /**
     * 查询巡检任务文件列表
     *
     * @param sdInspectionFile 巡检任务文件
     * @return 巡检任务文件
     */
    @Override
    public List<SdInspectionFile> selectSdInspectionFileList(SdInspectionFile sdInspectionFile) {
        return sdInspectionFileMapper.selectSdInspectionFileList(sdInspectionFile);
    }

    /**
     * 新增巡检任务文件
     *
     * @param sdInspectionFile 巡检任务文件
     * @return 结果
     */
    @Override
    public int insertSdInspectionFile(SdInspectionFile sdInspectionFile) {
        sdInspectionFile.setCreateTime(DateUtils.getNowDate());
        return sdInspectionFileMapper.insertSdInspectionFile(sdInspectionFile);
    }

    /**
     * 修改巡检任务文件
     *
     * @param sdInspectionFile 巡检任务文件
     * @return 结果
     */
    @Override
    public int updateSdInspectionFile(SdInspectionFile sdInspectionFile) {
        sdInspectionFile.setUpdateTime(DateUtils.getNowDate());
        return sdInspectionFileMapper.updateSdInspectionFile(sdInspectionFile);
    }

    /**
     * 批量删除巡检任务文件
     *
     * @param ids 需要删除的巡检任务文件主键
     * @return 结果
     */
    @Override
    public int deleteSdInspectionFileByIds(Long[] ids) {
        return sdInspectionFileMapper.deleteSdInspectionFileByIds(ids);
    }

    /**
     * 删除巡检任务文件信息
     *
     * @param id 巡检任务文件主键
     * @return 结果
     */
    @Override
    public int deleteSdInspectionFileById(Long id) {
        return sdInspectionFileMapper.deleteSdInspectionFileById(id);
    }
}
