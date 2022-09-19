package com.tunnel.business.service.trafficOperationControl.controlConfig.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.CongestionDegreeEnum;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.ControlConfigCauseEnum;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.RoadConditionEnum;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlConfigCause;
import com.tunnel.business.mapper.trafficOperationControl.controlConfig.SdControlConfigCauseMapper;
import com.tunnel.business.service.trafficOperationControl.controlConfig.ISdControlConfigCauseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管控等级配置措施-管控原因Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-15
 */
@Service
public class SdControlConfigCauseServiceImpl implements ISdControlConfigCauseService {
    @Autowired
    private SdControlConfigCauseMapper sdControlConfigCauseMapper;

    /**
     * 查询管控等级配置措施-管控原因
     *
     * @param id 管控等级配置措施-管控原因主键
     * @return 管控等级配置措施-管控原因
     */
    @Override
    public SdControlConfigCause selectSdControlConfigCauseById(Long id) {
        return sdControlConfigCauseMapper.selectSdControlConfigCauseById(id);
    }

    /**
     * 查询管控等级配置措施-管控原因列表
     *
     * @param sdControlConfigCause 管控等级配置措施-管控原因
     * @return 管控等级配置措施-管控原因
     */
    @Override
    public List<SdControlConfigCause> selectSdControlConfigCauseList(SdControlConfigCause sdControlConfigCause) {
        return sdControlConfigCauseMapper.selectSdControlConfigCauseList(sdControlConfigCause);
    }

    /**
     * 新增管控等级配置措施-管控原因
     *
     * @param sdControlConfigCause 管控等级配置措施-管控原因
     * @return 结果
     */
    @Override
    public int insertSdControlConfigCause(SdControlConfigCause sdControlConfigCause) {
        sdControlConfigCause.setCreateTime(DateUtils.getNowDate());
        return sdControlConfigCauseMapper.insertSdControlConfigCause(sdControlConfigCause);
    }

    /**
     * 修改管控等级配置措施-管控原因
     *
     * @param sdControlConfigCause 管控等级配置措施-管控原因
     * @return 结果
     */
    @Override
    public int updateSdControlConfigCause(SdControlConfigCause sdControlConfigCause) {
        sdControlConfigCause.setUpdateTime(DateUtils.getNowDate());
        return sdControlConfigCauseMapper.updateSdControlConfigCause(sdControlConfigCause);
    }

    /**
     * 批量删除管控等级配置措施-管控原因
     *
     * @param ids 需要删除的管控等级配置措施-管控原因主键
     * @return 结果
     */
    @Override
    public int deleteSdControlConfigCauseByIds(Long[] ids) {
        return sdControlConfigCauseMapper.deleteSdControlConfigCauseByIds(ids);
    }

    /**
     * 删除管控等级配置措施-管控原因信息
     *
     * @param id 管控等级配置措施-管控原因主键
     * @return 结果
     */
    @Override
    public int deleteSdControlConfigCauseById(Long id) {
        return sdControlConfigCauseMapper.deleteSdControlConfigCauseById(id);
    }


    /**
     * 根据等级配置id删除管控原因记录
     *
     * @param configLevelId 等级配置id
     * @return
     */
    @Override
    public int delConfigCauseByLevelId(Long configLevelId) {
        return sdControlConfigCauseMapper.delConfigCauseByLevelId(configLevelId);
    }


    /**
     * 删除管控等级关联的管控原因
     *
     * @param levelIds 等级id数组
     * @return
     */
    @Override
    public int delConfigCauseByLevelIds(Long[] levelIds) {
        return sdControlConfigCauseMapper.delConfigCauseByLevelIds(levelIds);
    }

    /**
     * 根据等级配置id查询管控原因
     *
     * @param configLevelId 等级配置id
     * @return
     */
    @Override
    public List<SdControlConfigCause> getConfigCauseByLevelId(Long configLevelId) {
        return sdControlConfigCauseMapper.getConfigCauseByLevelId(configLevelId);
    }

    /**
     * 查询可用的管控等级配置的管控原因
     *
     * @param status 状态
     * @return
     */
    @Override
    public List<SdControlConfigCause> selectValidConfigCauseList(String status) {
        return sdControlConfigCauseMapper.selectValidConfigCauseList(status);
    }

    /**
     * 获取管控原因
     *
     * @param list
     * @return
     */
    @Override
    public String getControlCauseDescription(List<SdControlConfigCause> list) {
        StringBuffer content = new StringBuffer();
        if (list != null && list.size() > 0) {
            //todo 暂时取一个管控原因
            for (SdControlConfigCause configCause : list) {
                StringBuffer item = new StringBuffer();
                String causeType = configCause.getCauseType();
                if (ControlConfigCauseEnum.visibility.getCode().equals(causeType)) {
                    //能见度
                    item.append("能见度范围在").append(configCause.getVisibilityMin()).append("-").append(configCause.getVisibilityMax()).append("米");
                }
                if (ControlConfigCauseEnum.road_condition.getCode().equals(causeType)) {
                    //路面情况
                    String roadCondition = RoadConditionEnum.findName(configCause.getRoadCondition());
                    item.append("路面").append(roadCondition);
                }
                if (ControlConfigCauseEnum.congestion_degree.getCode().equals(causeType)) {
                    //拥挤度
                    String congestionDegree = CongestionDegreeEnum.findName(configCause.getCongestionDegree());
                    item.append("道路").append(congestionDegree);
                }
                if (item.length() > 0) {
                    item.append("、");
                    content.append(item);
                }
            }
        }

        String result = "";
        if (content.length() > 0) {
            result = content.substring(0, content.length() - 1);
        }
        return result;
    }
}
