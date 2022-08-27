package com.tunnel.platform.service.trafficOperationControl.eventManage.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficIncidentProcess;
import com.tunnel.platform.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.platform.mapper.trafficOperationControl.eventManage.SdTrafficIncidentProcessMapper;
import com.tunnel.platform.service.trafficOperationControl.eventManage.ISdTrafficIncidentProcessService;
import com.tunnel.platform.utils.util.ImgTobase64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 交通事件-处理流程Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-18
 */
@Service
public class SdTrafficIncidentProcessServiceImpl implements ISdTrafficIncidentProcessService
{
    @Autowired
    private SdTrafficIncidentProcessMapper sdTrafficIncidentProcessMapper;

    @Autowired
    private SdTrafficImageMapper imageMapper;

    /**
     * 查询交通事件-处理流程
     *
     * @param processId 交通事件-处理流程主键
     * @return 交通事件-处理流程
     */
    @Override
    public SdTrafficIncidentProcess selectSdTrafficIncidentProcessByProcessId(Long processId)
    {
        return sdTrafficIncidentProcessMapper.selectSdTrafficIncidentProcessByProcessId(processId);
    }

    /**
     * 查询交通事件-处理流程列表
     *
     * @param sdTrafficIncidentProcess 交通事件-处理流程
     * @return 交通事件-处理流程
     */
    @Override
    public List<SdTrafficIncidentProcess> selectSdTrafficIncidentProcessList(SdTrafficIncidentProcess sdTrafficIncidentProcess)
    {
        return sdTrafficIncidentProcessMapper.selectSdTrafficIncidentProcessList(sdTrafficIncidentProcess);
    }

    /**
     * 新增交通事件-处理流程
     *
     * @param sdTrafficIncidentProcess 交通事件-处理流程
     * @return 结果
     */
    @Override
    public int insertSdTrafficIncidentProcess(SdTrafficIncidentProcess sdTrafficIncidentProcess)
    {
        sdTrafficIncidentProcess.setCreateTime(DateUtils.getNowDate());
        return sdTrafficIncidentProcessMapper.insertSdTrafficIncidentProcess(sdTrafficIncidentProcess);
    }

    /**
     * 修改交通事件-处理流程
     *
     * @param sdTrafficIncidentProcess 交通事件-处理流程
     * @return 结果
     */
    @Override
    public int updateSdTrafficIncidentProcess(SdTrafficIncidentProcess sdTrafficIncidentProcess)
    {
        sdTrafficIncidentProcess.setUpdateTime(DateUtils.getNowDate());
        return sdTrafficIncidentProcessMapper.updateSdTrafficIncidentProcess(sdTrafficIncidentProcess);
    }

    /**
     * 批量删除交通事件-处理流程
     *
     * @param processIds 需要删除的交通事件-处理流程主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficIncidentProcessByProcessIds(Long[] processIds)
    {
        return sdTrafficIncidentProcessMapper.deleteSdTrafficIncidentProcessByProcessIds(processIds);
    }

    /**
     * 删除交通事件-处理流程信息
     *
     * @param processId 交通事件-处理流程主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficIncidentProcessByProcessId(Long processId)
    {
        return sdTrafficIncidentProcessMapper.deleteSdTrafficIncidentProcessByProcessId(processId);
    }

    /**
     * 获取交通事件-处理流程列表
     *
     * @param incidentId
     * @return
     */
    @Override
    public List<SdTrafficIncidentProcess> getProcessList(Long incidentId) {
        List<SdTrafficIncidentProcess> processList =  sdTrafficIncidentProcessMapper.getProcessList(incidentId);
        List<Long> idList = new ArrayList<>();
        processList.forEach(process ->{
            idList.add(process.getProcessId());
        });

        if(idList.size() > 0){
            //查询流程关联的图片信息
            List<SdTrafficImage> imgList = imageMapper.selectImageByBusinessIds(idList);
            processList.forEach(process ->{
                String processId = String.valueOf(process.getProcessId());
                if(processId != null){
                    imgList.forEach(img -> {
                        if(processId.equals(img.getBusinessId())){
                            //将图片url转换成base64格式，避免黄金服务器不显示图片
                            String base64Str = ImgTobase64.ioToBase64(img.getImgUrl());
                            img.setImgBase64(base64Str);

                            List<SdTrafficImage> list = process.getImgList();
                            if(list == null){
                                list = new ArrayList<>();
                                process.setImgList(list);
                            }
                            list.add(img);
                        }
                    });
                }
            });
        }

        return processList;
    }

    /**
     * 删除事件关联的流程
     *
     * @param incidentId 事件id
     * @return
     */
    @Override
    public int delProcessByIncidentId(Long incidentId) {
        return sdTrafficIncidentProcessMapper.delProcessByIncidentId(incidentId);
    }

    /**
     * 查询事件关联的所有流程id
     *
     * @param incidentId 事件id
     * @return
     */
    @Override
    public Long[] queryProcessIdByIncidentId(Long incidentId) {
        return sdTrafficIncidentProcessMapper.queryProcessIdByIncidentId(incidentId);
    }

    /**
     * 删除事件关联的流程以及图片信息
     *
     * @param incidentId 事件id
     * @return
     */
    @Override
    public int delProcessAndImage(Long incidentId) {
        //查询事件关联的全部流程信息id
        Long[] processIds = sdTrafficIncidentProcessMapper.queryProcessIdByIncidentId(incidentId);

        if(processIds !=null && processIds.length > 0){
            //删除流程关联的图片信息
            imageMapper.delImageByBusinessIds(processIds);
        }

        //删除事件关联的全部流程信息
        int row = sdTrafficIncidentProcessMapper.delProcessByIncidentId(incidentId);
        return row;
    }
}
