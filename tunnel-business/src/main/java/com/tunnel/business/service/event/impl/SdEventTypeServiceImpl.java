package com.tunnel.business.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.event.SdEventType;
import com.tunnel.business.mapper.event.SdEventTypeMapper;
import com.tunnel.business.service.event.ISdEventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 事件类型Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
@Service
public class SdEventTypeServiceImpl implements ISdEventTypeService {
    @Autowired
    private SdEventTypeMapper sdEventTypeMapper;

    /**
     * 查询事件类型
     *
     * @param id 事件类型ID
     * @return 事件类型
     */
    @Override
    public SdEventType selectSdEventTypeById(Long id) {
        return sdEventTypeMapper.selectSdEventTypeById(id);
    }

    /**
     * 查询事件类型列表
     *
     * @param sdEventType 事件类型
     * @return 事件类型
     */
    @Override
    public List<SdEventType> selectSdEventTypeList(SdEventType sdEventType) {
        return sdEventTypeMapper.selectSdEventTypeList(sdEventType);
    }

    /**
     * 新增事件类型
     *
     * @param sdEventType 事件类型
     * @return 结果
     */
    @Override
    public int insertSdEventType(SdEventType sdEventType) {
        //sdEventType.setCreateTime(DateUtils.getNowDate());
        return sdEventTypeMapper.insertSdEventType(sdEventType);
    }

    /**
     * 修改事件类型
     *
     * @param sdEventType 事件类型
     * @return 结果
     */
    @Override
    public int updateSdEventType(SdEventType sdEventType) {
        sdEventType.setUpdateTime(DateUtils.getNowDate());
        return sdEventTypeMapper.updateSdEventType(sdEventType);
    }

    /**
     * 批量删除事件类型
     *
     * @param ids 需要删除的事件类型ID
     * @return 结果
     */
    @Override
    public int deleteSdEventTypeByIds(Long[] ids) {
        return sdEventTypeMapper.deleteSdEventTypeByIds(ids);
    }

    /**
     * 删除事件类型信息
     *
     * @param id 事件类型ID
     * @return 结果
     */
    @Override
    public int deleteSdEventTypeById(Long id) {
        return sdEventTypeMapper.deleteSdEventTypeById(id);
    }
}