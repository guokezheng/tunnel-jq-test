package com.tunnel.business.service.trafficBroadcasting.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.trafficBroadcasting.SdBroadcastTemplate;
import com.tunnel.business.mapper.trafficBroadcasting.SdBroadcastTemplateMapper;
import com.tunnel.business.service.trafficBroadcasting.ISdBroadcastTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 广播模板Service业务层处理
 *
 * @author ruoyi
 * @date 2021-12-03
 */
@Service
public class SdBroadcastTemplateServiceImpl implements ISdBroadcastTemplateService {
    @Autowired
    private SdBroadcastTemplateMapper sdBroadcastTemplateMapper;

    /**
     * 查询广播模板
     *
     * @param id 广播模板ID
     * @return 广播模板
     */
    @Override
    public SdBroadcastTemplate selectSdBroadcastTemplateById(Long id) {
        return sdBroadcastTemplateMapper.selectSdBroadcastTemplateById(id);
    }

    /**
     * 查询广播模板列表
     *
     * @param sdBroadcastTemplate 广播模板
     * @return 广播模板
     */
    @Override
    public List<SdBroadcastTemplate> selectSdBroadcastTemplateList(SdBroadcastTemplate sdBroadcastTemplate) {
        return sdBroadcastTemplateMapper.selectSdBroadcastTemplateList(sdBroadcastTemplate);
    }

    /**
     * 新增广播模板
     *
     * @param sdBroadcastTemplate 广播模板
     * @return 结果
     */
    @Override
    public int insertSdBroadcastTemplate(SdBroadcastTemplate sdBroadcastTemplate) {
        sdBroadcastTemplate.setCreateTime(DateUtils.getNowDate());
        return sdBroadcastTemplateMapper.insertSdBroadcastTemplate(sdBroadcastTemplate);
    }

    /**
     * 修改广播模板
     *
     * @param sdBroadcastTemplate 广播模板
     * @return 结果
     */
    @Override
    public int updateSdBroadcastTemplate(SdBroadcastTemplate sdBroadcastTemplate) {
        sdBroadcastTemplate.setUpdateTime(DateUtils.getNowDate());
        return sdBroadcastTemplateMapper.updateSdBroadcastTemplate(sdBroadcastTemplate);
    }

    /**
     * 批量删除广播模板
     *
     * @param ids 需要删除的广播模板ID
     * @return 结果
     */
    @Override
    public int deleteSdBroadcastTemplateByIds(Long[] ids) {
        return sdBroadcastTemplateMapper.deleteSdBroadcastTemplateByIds(ids);
    }

    /**
     * 删除广播模板信息
     *
     * @param id 广播模板ID
     * @return 结果
     */
    @Override
    public int deleteSdBroadcastTemplateById(Long id) {
        return sdBroadcastTemplateMapper.deleteSdBroadcastTemplateById(id);
    }
}
