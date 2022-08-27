package com.tunnel.platform.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.business.board.display.GuangDianAndSanSiDeviceDisplay;
import com.tunnel.platform.domain.event.SdEmergencyPer;
import com.tunnel.platform.mapper.event.SdEmergencyPerMapper;
import com.tunnel.platform.service.event.ISdEmergencyPerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 应急人员信息Service业务层处理
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Service
public class SdEmergencyPerServiceImpl implements ISdEmergencyPerService
{

    private static final Logger logger = LoggerFactory.getLogger(SdEmergencyPerServiceImpl.class);

    @Autowired
    private SdEmergencyPerMapper sdEmergencyPerMapper;

    /**
     * 查询应急人员信息
     *
     * @param id 应急人员信息ID
     * @return 应急人员信息
     */
    @Override
    public SdEmergencyPer selectSdEmergencyPerById(Long id)
    {
        return sdEmergencyPerMapper.selectSdEmergencyPerById(id);
    }

    /**
     * 查询应急人员信息列表
     *
     * @param sdEmergencyPer 应急人员信息
     * @return 应急人员信息
     */
    @Override
    public List<SdEmergencyPer> selectSdEmergencyPerList(SdEmergencyPer sdEmergencyPer)
    {
        Long deptId = SecurityUtils.getDeptId();
        sdEmergencyPer.getParams().put("deptId",deptId);
        return sdEmergencyPerMapper.selectSdEmergencyPerList(sdEmergencyPer);
    }

    /**
     * 新增应急人员信息
     *
     * @param sdEmergencyPer 应急人员信息
     * @return 结果
     */
    @Override
    public int insertSdEmergencyPer(SdEmergencyPer sdEmergencyPer)
    {
        //添加前先检查当前隧道中新增人员是否已经存在
        List<SdEmergencyPer> sdEmergencyPers = sdEmergencyPerMapper.selectPersonList(sdEmergencyPer);
        if (sdEmergencyPers.size() > 0){
            logger.error("当前人员已经存在！");
            throw new RuntimeException("当前人员已经存在！");
        }
        return sdEmergencyPerMapper.insertSdEmergencyPer(sdEmergencyPer);
    }

    /**
     * 修改应急人员信息
     *
     * @param sdEmergencyPer 应急人员信息
     * @return 结果
     */
    @Override
    public int updateSdEmergencyPer(SdEmergencyPer sdEmergencyPer)
    {
        sdEmergencyPer.setUpdateTime(DateUtils.getNowDate());
        return sdEmergencyPerMapper.updateSdEmergencyPer(sdEmergencyPer);
    }

    /**
     * 批量删除应急人员信息
     *
     * @param ids 需要删除的应急人员信息ID
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyPerByIds(Long[] ids)
    {
        return sdEmergencyPerMapper.deleteSdEmergencyPerByIds(ids);
    }

    /**
     * 删除应急人员信息信息
     *
     * @param id 应急人员信息ID
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyPerById(Long id)
    {
        return sdEmergencyPerMapper.deleteSdEmergencyPerById(id);
    }
}
