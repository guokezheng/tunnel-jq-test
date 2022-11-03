package com.tunnel.business.service.electromechanicalPatrol.impl;


import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 故障清单Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-02
 */
@Service
public class SdFaultListServiceImpl implements ISdFaultListService
{
    @Autowired
    private SdFaultListMapper sdFaultListMapper;

    /**
     * 查询故障清单
     * 
     * @param id 故障清单主键
     * @return 故障清单
     */
    @Override
    public SdFaultList selectSdFaultListById(String id)
    {
        return sdFaultListMapper.selectSdFaultListById(id);
    }

    /**
     * 查询故障清单列表
     * 
     * @param sdFaultList 故障清单
     * @return 故障清单
     */
    @Override
    public List<SdFaultList> selectSdFaultListList(SdFaultList sdFaultList)
    {
        return sdFaultListMapper.selectSdFaultListList(sdFaultList);
    }

    /**
     * 新增故障清单
     * 
     * @param sdFaultList 故障清单
     * @return 结果
     */
    @Override
    public int insertSdFaultList(SdFaultList sdFaultList)
    {
        return sdFaultListMapper.insertSdFaultList(sdFaultList);
    }

    /**
     * 修改故障清单
     * 
     * @param sdFaultList 故障清单
     * @return 结果
     */
    @Override
    public int updateSdFaultList(SdFaultList sdFaultList)
    {
        return sdFaultListMapper.updateSdFaultList(sdFaultList);
    }

    /**
     * 批量删除故障清单
     * 
     * @param ids 需要删除的故障清单主键
     * @return 结果
     */
    @Override
    public int deleteSdFaultListByIds(String[] ids)
    {
        return sdFaultListMapper.deleteSdFaultListByIds(ids);
    }

    /**
     * 删除故障清单信息
     * 
     * @param id 故障清单主键
     * @return 结果
     */
    @Override
    public int deleteSdFaultListById(String id)
    {
        return sdFaultListMapper.deleteSdFaultListById(id);
    }
}
