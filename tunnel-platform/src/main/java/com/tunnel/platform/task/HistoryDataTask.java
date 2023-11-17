package com.tunnel.platform.task;

import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataRecordMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 模拟量历史记录
 */
@Component("historyDataTask")
public class HistoryDataTask {

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    @Autowired
    private SdDeviceDataRecordMapper sdDeviceDataRecordMapper;
    /**
     * 历史模拟量
     */
    public void  AnalogQuantityHistoryData(){
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.getParams().put("pageNum",1);
        sdDeviceData.getParams().put("pageSize",999);
        sdDeviceData.setDeptId(0l);
        List<Map<String, String>> maps = sdDeviceDataService.dataDevicesLogInfoList(sdDeviceData);
        if(maps.size()>0){
            for (Map<String, String> map :maps){
                String eqId = map.get("eqId");
                SdDeviceData sdDeviceDataOne = new SdDeviceData();
                sdDeviceDataOne.setDeviceId(eqId);
                List<SdDeviceData> deviceData =  sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceDataOne);
                for (SdDeviceData analogData :deviceData){
                    SdDeviceDataRecord sdDeviceDataRecord = new SdDeviceDataRecord();
                    sdDeviceDataRecord.setDeviceId(analogData.getDeviceId());
                    sdDeviceDataRecord.setItemId(analogData.getItemId());
                    sdDeviceDataRecord.setData(analogData.getData());
                    sdDeviceDataRecord.setCreateTime(analogData.getUpdateTime());
                    sdDeviceDataRecordMapper.insertSdDeviceDataRecord(sdDeviceDataRecord);
                }
            }
        }
    }

}
