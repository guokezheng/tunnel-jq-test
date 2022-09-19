package com.tunnel.business.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.event.SdStrategyBack;
import com.tunnel.business.mapper.event.SdStrategyBackMapper;
import com.tunnel.business.service.event.ISdStrategyBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 策略还原Service业务层处理
 *
 * @author ruoyi
 * @date 2021-06-08
 */
@Service
public class SdStrategyBackServiceImpl implements ISdStrategyBackService {
    @Autowired
    private SdStrategyBackMapper sdStrategyBackMapper;

    /**
     * 查询策略还原
     *
     * @param id 策略还原ID
     * @return 策略还原
     */
    @Override
    public SdStrategyBack selectSdStrategyBackById(Long id) {
        return sdStrategyBackMapper.selectSdStrategyBackById(id);
    }

    /**
     * 查询策略还原列表
     *
     * @param SdStrategyBack 策略还原
     * @return 策略还原
     */
    @Override
    public List<SdStrategyBack> selectSdStrategyBackList(SdStrategyBack SdStrategyBack) {
        return sdStrategyBackMapper.selectSdStrategyBackList(SdStrategyBack);
    }

    /**
     * 新增策略还原
     *
     * @param SdStrategyBack 策略还原
     * @return 结果
     */
    @Override
    public int insertSdStrategyBack(SdStrategyBack SdStrategyBack) {
        SdStrategyBack.setCreateTime(DateUtils.getNowDate());
        return sdStrategyBackMapper.insertSdStrategyBack(SdStrategyBack);
    }

    /**
     * 修改策略还原
     *
     * @param SdStrategyBack 策略还原
     * @return 结果
     */
    @Override
    public int updateSdStrategyBack(SdStrategyBack SdStrategyBack) {
        return sdStrategyBackMapper.updateSdStrategyBack(SdStrategyBack);
    }

    /**
     * 批量删除策略还原
     *
     * @param ids 需要删除的策略还原ID
     * @return 结果
     */
    @Override
    public int deleteSdStrategyBackByIds(Long[] ids) {
        return sdStrategyBackMapper.deleteSdStrategyBackByIds(ids);
    }

    /**
     * 删除策略还原信息
     *
     * @param id 策略还原ID
     * @return 结果
     */
    @Override
    public int deleteSdStrategyBackById(Long id) {
        return sdStrategyBackMapper.deleteSdStrategyBackById(id);
    }
}
