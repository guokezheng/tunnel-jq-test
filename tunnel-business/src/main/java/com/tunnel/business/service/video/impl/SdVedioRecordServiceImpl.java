package com.tunnel.business.service.video.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.video.SdVedioRecord;
import com.tunnel.business.mapper.video.SdVedioRecordMapper;
import com.tunnel.business.service.video.ISdVedioRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 历史视频信息Service业务层处理
 *
 * @author xuebi
 * @date 2020-11-20
 */
@Service
public class SdVedioRecordServiceImpl implements ISdVedioRecordService {
    @Autowired
    private SdVedioRecordMapper sdVedioRecordMapper;

    /**
     * 查询历史视频信息
     *
     * @param id 历史视频信息ID
     * @return 历史视频信息
     */
    @Override
    public SdVedioRecord selectSdVedioRecordById(Long id) {
        return sdVedioRecordMapper.selectSdVedioRecordById(id);
    }

    /**
     * 查询历史视频信息列表
     *
     * @param sdVedioRecord 历史视频信息
     * @return 历史视频信息
     */
    @Override
    public List<SdVedioRecord> selectSdVedioRecordList(SdVedioRecord sdVedioRecord) {
        Long deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        sdVedioRecord.getParams().put("deptId", deptId);
        return sdVedioRecordMapper.selectSdVedioRecordList(sdVedioRecord);
    }

    /**
     * 新增历史视频信息
     *
     * @param sdVedioRecord 历史视频信息
     * @return 结果
     */
    @Override
    public int insertSdVedioRecord(SdVedioRecord sdVedioRecord) {
        sdVedioRecord.setCreateTime(DateUtils.getNowDate());
        return sdVedioRecordMapper.insertSdVedioRecord(sdVedioRecord);
    }

    /**
     * 修改历史视频信息
     *
     * @param sdVedioRecord 历史视频信息
     * @return 结果
     */
    @Override
    public int updateSdVedioRecord(SdVedioRecord sdVedioRecord) {
        sdVedioRecord.setUpdateTime(DateUtils.getNowDate());
        return sdVedioRecordMapper.updateSdVedioRecord(sdVedioRecord);
    }

    /**
     * 批量删除历史视频信息
     *
     * @param ids 需要删除的历史视频信息ID
     * @return 结果
     */
    @Override
    public int deleteSdVedioRecordByIds(Long[] ids) {
        return sdVedioRecordMapper.deleteSdVedioRecordByIds(ids);
    }

    /**
     * 删除历史视频信息信息
     *
     * @param id 历史视频信息ID
     * @return 结果
     */
    @Override
    public int deleteSdVedioRecordById(Long id) {
        return sdVedioRecordMapper.deleteSdVedioRecordById(id);
    }
}
