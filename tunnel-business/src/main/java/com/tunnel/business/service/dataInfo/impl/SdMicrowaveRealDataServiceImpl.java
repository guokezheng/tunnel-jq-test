package com.tunnel.business.service.dataInfo.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdMicrowaveRealData;
import com.tunnel.business.mapper.dataInfo.SdMicrowaveRealDataMapper;
import com.tunnel.business.service.dataInfo.ISdMicrowaveRealDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微波车检单车实时数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
@Service
public class SdMicrowaveRealDataServiceImpl implements ISdMicrowaveRealDataService
{
    @Autowired
    private SdMicrowaveRealDataMapper sdMicrowaveRealDataMapper;

    /**
     * 查询微波车检单车实时数据
     * 
     * @param id 微波车检单车实时数据主键
     * @return 微波车检单车实时数据
     */
    @Override
    public SdMicrowaveRealData selectSdMicrowaveRealDataById(Long id)
    {
        return sdMicrowaveRealDataMapper.selectSdMicrowaveRealDataById(id);
    }

    /**
     * 查询微波车检单车实时数据列表
     * 
     * @param sdMicrowaveRealData 微波车检单车实时数据
     * @return 微波车检单车实时数据
     */
    @Override
    public List<SdMicrowaveRealData> selectSdMicrowaveRealDataList(SdMicrowaveRealData sdMicrowaveRealData)
    {
        return sdMicrowaveRealDataMapper.selectSdMicrowaveRealDataList(sdMicrowaveRealData);
    }

    /**
     * 新增微波车检单车实时数据
     * 
     * @param sdMicrowaveRealData 微波车检单车实时数据
     * @return 结果
     */
    @Override
    public int insertSdMicrowaveRealData(SdMicrowaveRealData sdMicrowaveRealData)
    {
        sdMicrowaveRealData.setCreateTime(DateUtils.getNowDate());
        return sdMicrowaveRealDataMapper.insertSdMicrowaveRealData(sdMicrowaveRealData);
    }

    /**
     * 修改微波车检单车实时数据
     * 
     * @param sdMicrowaveRealData 微波车检单车实时数据
     * @return 结果
     */
    @Override
    public int updateSdMicrowaveRealData(SdMicrowaveRealData sdMicrowaveRealData)
    {
        return sdMicrowaveRealDataMapper.updateSdMicrowaveRealData(sdMicrowaveRealData);
    }

    /**
     * 批量删除微波车检单车实时数据
     * 
     * @param ids 需要删除的微波车检单车实时数据主键
     * @return 结果
     */
    @Override
    public int deleteSdMicrowaveRealDataByIds(Long[] ids)
    {
        return sdMicrowaveRealDataMapper.deleteSdMicrowaveRealDataByIds(ids);
    }

    /**
     * 删除微波车检单车实时数据信息
     * 
     * @param id 微波车检单车实时数据主键
     * @return 结果
     */
    @Override
    public int deleteSdMicrowaveRealDataById(Long id)
    {
        return sdMicrowaveRealDataMapper.deleteSdMicrowaveRealDataById(id);
    }
}
