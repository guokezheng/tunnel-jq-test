package com.tunnel.business.service.emeDrill.impl;

import com.tunnel.business.domain.emeDrill.SdEmergencyDrill;
import com.tunnel.business.mapper.emeDrill.SdEmergencyDrillMapper;
import com.tunnel.business.service.emeDrill.ISdEmergencyDrillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应急演练Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-22
 */
@Service
public class SdEmergencyDrillServiceImpl implements ISdEmergencyDrillService {
    @Autowired
    private SdEmergencyDrillMapper sdEmergencyDrillMapper;

    /**
     * 查询应急演练
     *
     * @param id 应急演练ID
     * @return 应急演练
     */
    @Override
    public SdEmergencyDrill selectSdEmergencyDrillById(Long id) {
        return sdEmergencyDrillMapper.selectSdEmergencyDrillById(id);
    }

    /**
     * 查询应急演练列表
     *
     * @param sdEmergencyDrill 应急演练
     * @return 应急演练
     */
    @Override
    public List<SdEmergencyDrill> selectSdEmergencyDrillList(SdEmergencyDrill sdEmergencyDrill) {
        return sdEmergencyDrillMapper.selectSdEmergencyDrillList(sdEmergencyDrill);
    }

    /**
     * 新增应急演练
     *
     * @param sdEmergencyDrill 应急演练
     * @return 结果
     */
    @Override
    public int insertSdEmergencyDrill(SdEmergencyDrill sdEmergencyDrill) {
        return sdEmergencyDrillMapper.insertSdEmergencyDrill(sdEmergencyDrill);
    }

    /**
     * 修改应急演练
     *
     * @param sdEmergencyDrill 应急演练
     * @return 结果
     */
    @Override
    public int updateSdEmergencyDrill(SdEmergencyDrill sdEmergencyDrill) {
        return sdEmergencyDrillMapper.updateSdEmergencyDrill(sdEmergencyDrill);
    }

    /**
     * 批量删除应急演练
     *
     * @param ids 需要删除的应急演练ID
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyDrillByIds(Long[] ids) {
        return sdEmergencyDrillMapper.deleteSdEmergencyDrillByIds(ids);
    }

    /**
     * 删除应急演练信息
     *
     * @param id 应急演练ID
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyDrillById(Long id) {
        return sdEmergencyDrillMapper.deleteSdEmergencyDrillById(id);
    }
}
