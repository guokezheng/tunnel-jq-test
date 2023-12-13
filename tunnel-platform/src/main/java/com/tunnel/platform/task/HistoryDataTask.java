package com.tunnel.platform.task;

import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataRecordMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public void  AnalogQuantityHistoryData() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String formattedDate = sdf.format(currentDate);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date updateTime = sdf1.parse(formattedDate);

        SdDeviceData sdDeviceDataOne = new SdDeviceData();
        sdDeviceDataOne.setUpdateTime(updateTime);
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
