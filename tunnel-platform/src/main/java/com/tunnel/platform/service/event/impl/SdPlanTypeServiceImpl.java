package com.tunnel.platform.service.event.impl;

import com.tunnel.platform.domain.event.SdPlanType;
import com.tunnel.platform.mapper.event.SdPlanTypeMapper;
import com.tunnel.platform.service.event.ISdPlanTypeService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预案类型Service业务层处理
 * 
 * @author gongfanfei
 * @date 2020-08-25
 */
@Service
public class SdPlanTypeServiceImpl implements ISdPlanTypeService 
{
    @Autowired
    private SdPlanTypeMapper sdPlanTypeMapper;

    /**
     * 查询预案类型
     * 
     * @param id 预案类型ID
     * @return 预案类型
     */
    @Override
    public SdPlanType selectSdPlanTypeById(Long id)
    {
        return sdPlanTypeMapper.selectSdPlanTypeById(id);
    }

    /**
     * 查询预案类型列表
     * 
     * @param sdPlanType 预案类型
     * @return 预案类型
     */
    @Override
    public List<SdPlanType> selectSdPlanTypeList(SdPlanType sdPlanType)
    {
        return sdPlanTypeMapper.selectSdPlanTypeList(sdPlanType);
    }

    /**
     * 新增预案类型
     * 
     * @param sdPlanType 预案类型
     * @return 结果
     */
    @Override
    public int insertSdPlanType(SdPlanType sdPlanType)
    {
        sdPlanType.setCreateTime(DateUtils.getNowDate());
        return sdPlanTypeMapper.insertSdPlanType(sdPlanType);
    }

    /**
     * 修改预案类型
     * 
     * @param sdPlanType 预案类型
     * @return 结果
     */
    @Override
    public int updateSdPlanType(SdPlanType sdPlanType)
    {
        sdPlanType.setUpdateTime(DateUtils.getNowDate());
        return sdPlanTypeMapper.updateSdPlanType(sdPlanType);
    }

    /**
     * 批量删除预案类型
     * 
     * @param ids 需要删除的预案类型ID
     * @return 结果
     */
    @Override
    public int deleteSdPlanTypeByIds(Long[] ids)
    {
        return sdPlanTypeMapper.deleteSdPlanTypeByIds(ids);
    }

    /**
     * 删除预案类型信息
     * 
     * @param id 预案类型ID
     * @return 结果
     */
    @Override
    public int deleteSdPlanTypeById(Long id)
    {
        return sdPlanTypeMapper.deleteSdPlanTypeById(id);
    }
}