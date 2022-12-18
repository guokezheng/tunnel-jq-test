package com.tunnel.business.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.event.SdSafetyIndex;
import com.tunnel.business.mapper.event.SdSafetyIndexMapper;
import com.tunnel.business.service.event.ISdSafetyIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 安全指数Service业务层处理
 *
 * @author ruoyi
 * @date 2021-12-27
 */
@Service
public class SdSafetyIndexServiceImpl implements ISdSafetyIndexService {
    @Autowired
    private SdSafetyIndexMapper sdSafetyIndexMapper;

    /**
     * 查询安全指数
     *
     * @param id 安全指数主键
     * @return 安全指数
     */
    @Override
    public SdSafetyIndex selectSdSafetyIndexById(Long id) {
        return sdSafetyIndexMapper.selectSdSafetyIndexById(id);
    }

    /**
     * 查询安全指数列表
     *
     * @param sdSafetyIndex 安全指数
     * @return 安全指数
     */
    @Override
    public List<SdSafetyIndex> selectSdSafetyIndexList(SdSafetyIndex sdSafetyIndex) {
        String deptId = SecurityUtils.getDeptId();
        sdSafetyIndex.getParams().put("deptId", deptId);
        return sdSafetyIndexMapper.selectSdSafetyIndexList(sdSafetyIndex);
    }

    /**
     * 新增安全指数
     *
     * @param sdSafetyIndex 安全指数
     * @return 结果
     */
    @Override
    public int insertSdSafetyIndex(SdSafetyIndex sdSafetyIndex) {
        sdSafetyIndex.setCreateTime(DateUtils.getNowDate());
        return sdSafetyIndexMapper.insertSdSafetyIndex(sdSafetyIndex);
    }

    /**
     * 修改安全指数
     *
     * @param sdSafetyIndex 安全指数
     * @return 结果
     */
    @Override
    public int updateSdSafetyIndex(SdSafetyIndex sdSafetyIndex) {
        sdSafetyIndex.setUpdateTime(DateUtils.getNowDate());
        return sdSafetyIndexMapper.updateSdSafetyIndex(sdSafetyIndex);
    }

    /**
     * 批量删除安全指数
     *
     * @param ids 需要删除的安全指数主键
     * @return 结果
     */
    @Override
    public int deleteSdSafetyIndexByIds(Long[] ids) {
        return sdSafetyIndexMapper.deleteSdSafetyIndexByIds(ids);
    }

    /**
     * 删除安全指数信息
     *
     * @param id 安全指数主键
     * @return 结果
     */
    @Override
    public int deleteSdSafetyIndexById(Long id) {
        return sdSafetyIndexMapper.deleteSdSafetyIndexById(id);
    }
}
