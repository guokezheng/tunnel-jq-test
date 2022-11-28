package com.tunnel.business.service.trafficBroadcasting.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.trafficBroadcasting.SdBroadcastInformationList;
import com.tunnel.business.mapper.trafficBroadcasting.SdBroadcastInformationListMapper;
import com.tunnel.business.service.trafficBroadcasting.ISdBroadcastInformationListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 广播信息列Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-24
 */
@Service
public class SdBroadcastInformationListServiceImpl implements ISdBroadcastInformationListService {
    @Autowired
    private SdBroadcastInformationListMapper sdBroadcastInformationListMapper;

    /**
     * 查询广播信息列
     *
     * @param id 广播信息列ID
     * @return 广播信息列
     */
    @Override
    public SdBroadcastInformationList selectSdBroadcastInformationListById(Long id) {
        return sdBroadcastInformationListMapper.selectSdBroadcastInformationListById(id);
    }

    /**
     * 查询广播信息列列表
     *
     * @param sdBroadcastInformationList 广播信息列
     * @return 广播信息列
     */
    @Override
    public List<SdBroadcastInformationList> selectSdBroadcastInformationListList(SdBroadcastInformationList sdBroadcastInformationList) {
        String deptId = SecurityUtils.getDeptId();
        sdBroadcastInformationList.getParams().put("deptId", deptId);
        return sdBroadcastInformationListMapper.selectSdBroadcastInformationListList(sdBroadcastInformationList);
    }

    /**
     * 新增广播信息列
     *
     * @param sdBroadcastInformationList 广播信息列
     * @return 结果
     */
    @Override
    public int insertSdBroadcastInformationList(SdBroadcastInformationList sdBroadcastInformationList) {
        sdBroadcastInformationList.setCreateTime(DateUtils.getNowDate());
        return sdBroadcastInformationListMapper.insertSdBroadcastInformationList(sdBroadcastInformationList);
    }

    /**
     * 修改广播信息列
     *
     * @param sdBroadcastInformationList 广播信息列
     * @return 结果
     */
    @Override
    public int updateSdBroadcastInformationList(SdBroadcastInformationList sdBroadcastInformationList) {
        sdBroadcastInformationList.setUpdateTime(DateUtils.getNowDate());
        return sdBroadcastInformationListMapper.updateSdBroadcastInformationList(sdBroadcastInformationList);
    }

    /**
     * 批量删除广播信息列
     *
     * @param ids 需要删除的广播信息列ID
     * @return 结果
     */
    @Override
    public int deleteSdBroadcastInformationListByIds(Long[] ids) {
        return sdBroadcastInformationListMapper.deleteSdBroadcastInformationListByIds(ids);
    }

    /**
     * 删除广播信息列信息
     *
     * @param id 广播信息列ID
     * @return 结果
     */
    @Override
    public int deleteSdBroadcastInformationListById(Long id) {
        return sdBroadcastInformationListMapper.deleteSdBroadcastInformationListById(id);
    }
}
