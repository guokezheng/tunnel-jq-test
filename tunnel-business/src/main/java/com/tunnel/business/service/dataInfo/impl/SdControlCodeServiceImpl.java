package com.tunnel.business.service.dataInfo.impl;

import com.tunnel.business.domain.dataInfo.SdControlCode;
import com.tunnel.business.mapper.dataInfo.SdControlCodeMapper;
import com.tunnel.business.service.dataInfo.ISdControlCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 控制码Service业务层处理
 *
 * @author zhangweitian
 * @date 2020-09-08
 */
@Service
public class SdControlCodeServiceImpl implements ISdControlCodeService {
    @Autowired
    private SdControlCodeMapper sdControlCodeMapper;

    /**
     * 查询控制码
     *
     * @param controlId 控制码ID
     * @return 控制码
     */
    @Override
    public SdControlCode selectSdControlCodeById(Long controlId) {
        return sdControlCodeMapper.selectSdControlCodeById(controlId);
    }

    /**
     * 查询控制码列表
     *
     * @param sdControlCode 控制码
     * @return 控制码
     */
    @Override
    public List<SdControlCode> selectSdControlCodeList(SdControlCode sdControlCode) {
        return sdControlCodeMapper.selectSdControlCodeList(sdControlCode);
    }

    /**
     * 通过key查询控制码
     */
    @Override
    public List<SdControlCode> selectSdControlCodeByKey(SdControlCode sdControlCode) {
        return sdControlCodeMapper.selectSdControlCodeByKey(sdControlCode);
    }

    /**
     * 新增控制码
     *
     * @param sdControlCode 控制码
     * @return 结果
     */
    @Override
    public int insertSdControlCode(SdControlCode sdControlCode) {
        return sdControlCodeMapper.insertSdControlCode(sdControlCode);
    }

    /**
     * 修改控制码
     *
     * @param sdControlCode 控制码
     * @return 结果
     */
    @Override
    public int updateSdControlCode(SdControlCode sdControlCode) {
        return sdControlCodeMapper.updateSdControlCode(sdControlCode);
    }

    /**
     * 批量删除控制码
     *
     * @param controlIds 需要删除的控制码ID
     * @return 结果
     */
    @Override
    public int deleteSdControlCodeByIds(Long[] controlIds) {
        return sdControlCodeMapper.deleteSdControlCodeByIds(controlIds);
    }

    /**
     * 删除控制码信息
     *
     * @param controlId 控制码ID
     * @return 结果
     */
    @Override
    public int deleteSdControlCodeById(Long controlId) {
        return sdControlCodeMapper.deleteSdControlCodeById(controlId);
    }
}