package com.tunnel.business.service.informationBoard.impl;

import com.tunnel.business.domain.informationBoard.SdEventOperating;
import com.tunnel.business.mapper.informationBoard.SdEventOperatingMapper;
import com.tunnel.business.service.informationBoard.ISdEventOperatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交通事件Service业务层处理
 *
 * @author 刘方堃
 * @date 2021-12-03
 */
@Service
public class SdEventOperatingServiceImpl implements ISdEventOperatingService {
    @Autowired
    private SdEventOperatingMapper sdEventOperatingMapper;

    /**
     * 查询交通事件
     *
     * @param id 交通事件ID
     * @return 交通事件
     */
    @Override
    public SdEventOperating selectSdEventOperatingById(Long id) {
        return sdEventOperatingMapper.selectSdEventOperatingById(id);
    }

    /**
     * 查询交通事件列表
     *
     * @param sdEventOperating 交通事件
     * @return 交通事件
     */
    @Override
    public List<SdEventOperating> selectSdEventOperatingList(SdEventOperating sdEventOperating) {
        return sdEventOperatingMapper.selectSdEventOperatingList(sdEventOperating);
    }

    /**
     * 新增交通事件
     *
     * @param sdEventOperating 交通事件
     * @return 结果
     */
    @Override
    public int insertSdEventOperating(SdEventOperating sdEventOperating) {
        return sdEventOperatingMapper.insertSdEventOperating(sdEventOperating);
    }

    /**
     * 修改交通事件
     *
     * @param sdEventOperating 交通事件
     * @return 结果
     */
    @Override
    public int updateSdEventOperating(SdEventOperating sdEventOperating) {
        return sdEventOperatingMapper.updateSdEventOperating(sdEventOperating);
    }

    /**
     * 批量删除交通事件
     *
     * @param ids 需要删除的交通事件ID
     * @return 结果
     */
    @Override
    public int deleteSdEventOperatingByIds(Long[] ids) {
        return sdEventOperatingMapper.deleteSdEventOperatingByIds(ids);
    }

    /**
     * 删除交通事件信息
     *
     * @param id 交通事件ID
     * @return 结果
     */
    @Override
    public int deleteSdEventOperatingById(Long id) {
        return sdEventOperatingMapper.deleteSdEventOperatingById(id);
    }
}
