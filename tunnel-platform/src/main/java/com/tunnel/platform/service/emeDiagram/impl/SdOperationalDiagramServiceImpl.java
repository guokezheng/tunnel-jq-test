package com.tunnel.platform.service.emeDiagram.impl;

import com.tunnel.platform.domain.emeDiagram.SdOperationalDiagram;
import com.tunnel.platform.mapper.emeDiagram.SdOperationalDiagramMapper;
import com.tunnel.platform.service.emeDiagram.ISdOperationalDiagramService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作战示意图Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-11-22
 */
@Service
public class SdOperationalDiagramServiceImpl implements ISdOperationalDiagramService
{
    @Autowired
    private SdOperationalDiagramMapper sdOperationalDiagramMapper;

    /**
     * 查询作战示意图
     * 
     * @param id 作战示意图ID
     * @return 作战示意图
     */
    @Override
    public SdOperationalDiagram selectSdOperationalDiagramById(Long id)
    {
        return sdOperationalDiagramMapper.selectSdOperationalDiagramById(id);
    }

    /**
     * 查询作战示意图列表
     * 
     * @param sdOperationalDiagram 作战示意图
     * @return 作战示意图
     */
    @Override
    public List<SdOperationalDiagram> selectSdOperationalDiagramList(SdOperationalDiagram sdOperationalDiagram)
    {
        return sdOperationalDiagramMapper.selectSdOperationalDiagramList(sdOperationalDiagram);
    }

    /**
     * 新增作战示意图
     * 
     * @param sdOperationalDiagram 作战示意图
     * @return 结果
     */
    @Override
    public int insertSdOperationalDiagram(SdOperationalDiagram sdOperationalDiagram)
    {
        sdOperationalDiagram.setCreateTime(DateUtils.getNowDate());
        return sdOperationalDiagramMapper.insertSdOperationalDiagram(sdOperationalDiagram);
    }

    /**
     * 修改作战示意图
     * 
     * @param sdOperationalDiagram 作战示意图
     * @return 结果
     */
    @Override
    public int updateSdOperationalDiagram(SdOperationalDiagram sdOperationalDiagram)
    {
        sdOperationalDiagram.setUpdateTime(DateUtils.getNowDate());
        return sdOperationalDiagramMapper.updateSdOperationalDiagram(sdOperationalDiagram);
    }

    /**
     * 批量删除作战示意图
     * 
     * @param ids 需要删除的作战示意图ID
     * @return 结果
     */
    @Override
    public int deleteSdOperationalDiagramByIds(Long[] ids)
    {
        return sdOperationalDiagramMapper.deleteSdOperationalDiagramByIds(ids);
    }

    /**
     * 删除作战示意图信息
     * 
     * @param id 作战示意图ID
     * @return 结果
     */
    @Override
    public int deleteSdOperationalDiagramById(Long id)
    {
        return sdOperationalDiagramMapper.deleteSdOperationalDiagramById(id);
    }
}
