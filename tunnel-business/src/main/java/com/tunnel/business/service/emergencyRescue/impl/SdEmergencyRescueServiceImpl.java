package com.tunnel.business.service.emergencyRescue.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.emergencyRescue.SdEmergencyRescue;
import com.tunnel.business.mapper.emergencyRescue.SdEmergencyRescueMapper;
import com.tunnel.business.service.emergencyRescue.ISdEmergencyRescueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应急救援Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-23
 */
@Service
public class SdEmergencyRescueServiceImpl implements ISdEmergencyRescueService {
    @Autowired
    private SdEmergencyRescueMapper sdEmergencyRescueMapper;

    /**
     * 查询应急救援
     *
     * @param id 应急救援ID
     * @return 应急救援
     */
    @Override
    public SdEmergencyRescue selectSdEmergencyRescueById(Long id) {
        return sdEmergencyRescueMapper.selectSdEmergencyRescueById(id);
    }

    /**
     * 查询应急救援列表
     *
     * @param sdEmergencyRescue 应急救援
     * @return 应急救援
     */
    @Override
    public List<SdEmergencyRescue> selectSdEmergencyRescueList(SdEmergencyRescue sdEmergencyRescue) {
        String deptId = SecurityUtils.getDeptId();
        sdEmergencyRescue.getParams().put("deptId", deptId);
        return sdEmergencyRescueMapper.selectSdEmergencyRescueList(sdEmergencyRescue);
    }

    /**
     * 新增应急救援
     *
     * @param sdEmergencyRescue 应急救援
     * @return 结果
     */
    @Override
    public int insertSdEmergencyRescue(SdEmergencyRescue sdEmergencyRescue) {
        sdEmergencyRescue.setCreateTime(DateUtils.getNowDate());
        return sdEmergencyRescueMapper.insertSdEmergencyRescue(sdEmergencyRescue);
    }

    /**
     * 修改应急救援
     *
     * @param sdEmergencyRescue 应急救援
     * @return 结果
     */
    @Override
    public int updateSdEmergencyRescue(SdEmergencyRescue sdEmergencyRescue) {
        sdEmergencyRescue.setUpdateTime(DateUtils.getNowDate());
        return sdEmergencyRescueMapper.updateSdEmergencyRescue(sdEmergencyRescue);
    }

    /**
     * 批量删除应急救援
     *
     * @param ids 需要删除的应急救援ID
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyRescueByIds(Long[] ids) {
        return sdEmergencyRescueMapper.deleteSdEmergencyRescueByIds(ids);
    }

    /**
     * 删除应急救援信息
     *
     * @param id 应急救援ID
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyRescueById(Long id) {
        return sdEmergencyRescueMapper.deleteSdEmergencyRescueById(id);
    }
}
