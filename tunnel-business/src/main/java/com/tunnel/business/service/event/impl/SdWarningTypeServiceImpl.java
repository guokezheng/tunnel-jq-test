package com.tunnel.business.service.event.impl;

import com.tunnel.business.domain.event.SdReservePlan;
import com.tunnel.business.domain.event.SdWarningType;
import com.tunnel.business.mapper.event.SdReservePlanMapper;
import com.tunnel.business.mapper.event.SdWarningTypeMapper;
import com.tunnel.business.service.event.ISdWarningTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预警类型Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-12-21
 */
@Service
public class SdWarningTypeServiceImpl implements ISdWarningTypeService {
    @Autowired
    private SdWarningTypeMapper sdWarningTypeMapper;
    @Autowired
    private SdReservePlanMapper sdReservePlanMapper;

    /**
     * 查询预警类型
     *
     * @param id 预警类型ID
     * @return 预警类型
     */
    @Override
    public SdWarningType selectSdWarningTypeById(Long id) {
        return sdWarningTypeMapper.selectSdWarningTypeById(id);
    }

    /**
     * 查询预警类型列表
     *
     * @param sdWarningType 预警类型
     * @return 预警类型
     */
    @Override
    public List<SdWarningType> selectSdWarningTypeList(SdWarningType sdWarningType) {
        List<SdWarningType> list = sdWarningTypeMapper.selectSdWarningTypeList(sdWarningType);
        for (SdWarningType s : list) {
            String planInfo = "";
            String planIds = s.getReservePlanIds();
            if (planIds != null && !"".equals(planIds)) {
                String[] planIdArray = planIds.split(",");
                for (int i = 0; i < planIdArray.length; i++) {
                    SdReservePlan plan = sdReservePlanMapper.selectSdReservePlanById(Long.parseLong(planIdArray[i]));
                    if (plan != null) {
                        planInfo = planInfo.concat((i + 1) + "").concat("、").concat(plan.getPlanName() + " ");
                    }
                }
                s.setReservePlanInfo(planInfo);
            }
        }

        return list;

    }

    @Override
    public boolean checkSdWarningTypeNameUnique(String typeName) {
        int count = sdWarningTypeMapper.countSdWarningTypeByTypeName(typeName);
        return count <= 0;
    }

    /**
     * 新增预警类型
     *
     * @param sdWarningType 预警类型
     * @return 结果
     */
    @Override
    public int insertSdWarningType(SdWarningType sdWarningType) {
        return sdWarningTypeMapper.insertSdWarningType(sdWarningType);
    }

    /**
     * 修改预警类型
     *
     * @param sdWarningType 预警类型
     * @return 结果
     */
    @Override
    public int updateSdWarningType(SdWarningType sdWarningType) {
        return sdWarningTypeMapper.updateSdWarningType(sdWarningType);
    }

    /**
     * 批量删除预警类型
     *
     * @param ids 需要删除的预警类型ID
     * @return 结果
     */
    @Override
    public int deleteSdWarningTypeByIds(Long[] ids) {
        return sdWarningTypeMapper.deleteSdWarningTypeByIds(ids);
    }

    /**
     * 删除预警类型信息
     *
     * @param id 预警类型ID
     * @return 结果
     */
    @Override
    public int deleteSdWarningTypeById(Long id) {
        return sdWarningTypeMapper.deleteSdWarningTypeById(id);
    }
}
