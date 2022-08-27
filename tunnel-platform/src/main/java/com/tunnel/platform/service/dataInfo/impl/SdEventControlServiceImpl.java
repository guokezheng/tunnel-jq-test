package com.tunnel.platform.service.dataInfo.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tunnel.platform.mapper.dataInfo.SdEventControlMapper;
import com.tunnel.platform.domain.dataInfo.SdEventControl;
import com.tunnel.platform.service.dataInfo.ISdEventControlService;

/**
 * 事件管控Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
@Service
public class SdEventControlServiceImpl implements ISdEventControlService 
{
    @Autowired
    private SdEventControlMapper sdEventControlMapper;

    /**
     * 查询事件管控
     * 
     * @param id 事件管控主键
     * @return 事件管控
     */
    @Override
    public SdEventControl selectSdEventControlById(Long id)
    {
        return sdEventControlMapper.selectSdEventControlById(id);
    }

    /**
     * 查询事件管控列表
     * 
     * @param sdEventControl 事件管控
     * @return 事件管控
     */
    @Override
    public List<SdEventControl> selectSdEventControlList(SdEventControl sdEventControl)
    {
        return sdEventControlMapper.selectSdEventControlList(sdEventControl);
    }

    /**
     * 新增事件管控
     * 
     * @param sdEventControl 事件管控
     * @return 结果
     */
    @Override
    public int insertSdEventControl(SdEventControl sdEventControl)
    {
        sdEventControl.setCreateTime(DateUtils.getNowDate());
        return sdEventControlMapper.insertSdEventControl(sdEventControl);
    }

    /**
     * 修改事件管控
     * 
     * @param sdEventControl 事件管控
     * @return 结果
     */
    @Override
    public int updateSdEventControl(SdEventControl sdEventControl)
    {
        sdEventControl.setUpdateTime(DateUtils.getNowDate());
        return sdEventControlMapper.updateSdEventControl(sdEventControl);
    }

    /**
     * 批量删除事件管控
     * 
     * @param ids 需要删除的事件管控主键
     * @return 结果
     */
    @Override
    public int deleteSdEventControlByIds(Long[] ids)
    {
        return sdEventControlMapper.deleteSdEventControlByIds(ids);
    }

    /**
     * 删除事件管控信息
     * 
     * @param id 事件管控主键
     * @return 结果
     */
    @Override
    public int deleteSdEventControlById(Long id)
    {
        return sdEventControlMapper.deleteSdEventControlById(id);
    }
}
