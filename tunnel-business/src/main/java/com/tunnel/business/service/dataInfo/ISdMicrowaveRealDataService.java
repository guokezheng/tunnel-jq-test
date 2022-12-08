package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.SdMicrowaveRealData;

import java.util.List;

/**
 * 微波车检单车实时数据Service接口
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
public interface ISdMicrowaveRealDataService 
{
    /**
     * 查询微波车检单车实时数据
     * 
     * @param id 微波车检单车实时数据主键
     * @return 微波车检单车实时数据
     */
    public SdMicrowaveRealData selectSdMicrowaveRealDataById(Long id);

    /**
     * 查询微波车检单车实时数据列表
     * 
     * @param sdMicrowaveRealData 微波车检单车实时数据
     * @return 微波车检单车实时数据集合
     */
    public List<SdMicrowaveRealData> selectSdMicrowaveRealDataList(SdMicrowaveRealData sdMicrowaveRealData);

    /**
     * 新增微波车检单车实时数据
     * 
     * @param sdMicrowaveRealData 微波车检单车实时数据
     * @return 结果
     */
    public int insertSdMicrowaveRealData(SdMicrowaveRealData sdMicrowaveRealData);

    /**
     * 修改微波车检单车实时数据
     * 
     * @param sdMicrowaveRealData 微波车检单车实时数据
     * @return 结果
     */
    public int updateSdMicrowaveRealData(SdMicrowaveRealData sdMicrowaveRealData);

    /**
     * 批量删除微波车检单车实时数据
     * 
     * @param ids 需要删除的微波车检单车实时数据主键集合
     * @return 结果
     */
    public int deleteSdMicrowaveRealDataByIds(Long[] ids);

    /**
     * 删除微波车检单车实时数据信息
     * 
     * @param id 微波车检单车实时数据主键
     * @return 结果
     */
    public int deleteSdMicrowaveRealDataById(Long id);
}
