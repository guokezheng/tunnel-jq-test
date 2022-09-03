package com.tunnel.platform.service.dataInfo.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.dataInfo.InductionlampControlStatusDetails;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdStateStorage;
import com.tunnel.platform.mapper.dataInfo.SdStateStorageMapper;
import com.tunnel.platform.service.dataInfo.IInductionlampControlStatusDetailsService;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.service.dataInfo.ISdStateStorageService;
import com.tunnel.platform.utils.util.NettyClient;
import com.tunnel.platform.utils.util.protocol.inductionlamp.InductionlampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 隧道数据存储表Service业务层处理
 *
 * @author 刘方堃
 * @date 2022-01-07
 */
@Service
public class SdStateStorageServiceImpl implements ISdStateStorageService
{
    @Autowired
    private SdStateStorageMapper sdStateStorageMapper;
    @Autowired
    private ISdDevicesService iSdDevicesService;
    @Autowired
    private IInductionlampControlStatusDetailsService iInductionlampControlStatusDetailsService;

    /**
     * 查询隧道数据存储表
     *
     * @param id 隧道数据存储表主键
     * @return 隧道数据存储表
     */
    @Override
    public SdStateStorage selectSdStateStorageById(Long id)
    {
        return sdStateStorageMapper.selectSdStateStorageById(id);
    }

    /**
     * 查询隧道数据存储表列表
     *
     * @param sdStateStorage 隧道数据存储表
     * @return 隧道数据存储表
     */
    @Override
    public List<SdStateStorage> selectSdStateStorageList(SdStateStorage sdStateStorage)
    {
        return sdStateStorageMapper.selectSdStateStorageList(sdStateStorage);
    }

    /**
     * 新增隧道数据存储表
     *
     * @param sdStateStorage 隧道数据存储表
     * @return 结果
     */
    @Override
    public int insertSdStateStorage(SdStateStorage sdStateStorage)
    {
        sdStateStorage.setCreateTime(DateUtils.getNowDate());
        return sdStateStorageMapper.insertSdStateStorage(sdStateStorage);
    }

    /**
     * 修改隧道数据存储表
     *
     * @param sdStateStorage 隧道数据存储表
     * @return 结果
     */
    @Override
    public int updateSdStateStorage(SdStateStorage sdStateStorage)
    {
        String deviceId = sdStateStorage.getDeviceId();
        SdDevices sdDevices = iSdDevicesService.selectSdDevicesById(deviceId);
        if (sdDevices.getEqType() == 31L && sdDevices.getControlStatus().equals("1")) {
            //如果当前是诱导灯走通信更改模式
            String ip = sdDevices.getIp();
            int port = Integer.parseInt(sdDevices.getPort());
            Integer controlModeType = Integer.parseInt(sdStateStorage.getState());
            int brightness = Integer.parseInt(sdStateStorage.getBrightness());
            int frequency = Integer.parseInt(sdStateStorage.getFrequency());
            InductionlampControlStatusDetails inductionlampControlStatusDetails = new InductionlampControlStatusDetails();
            inductionlampControlStatusDetails.setEquipmentId(deviceId);
            List<InductionlampControlStatusDetails> statusDetails = iInductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
            if (statusDetails.size() == 0) {
                inductionlampControlStatusDetails.setEquipmentModeType(Integer.parseInt(sdStateStorage.getState()));
                inductionlampControlStatusDetails.setFrequency(sdStateStorage.getFrequency());
                inductionlampControlStatusDetails.setBrightness(sdStateStorage.getBrightness());
                iInductionlampControlStatusDetailsService.insertInductionlampControlStatusDetails(inductionlampControlStatusDetails);
            } else {
                statusDetails.get(0).setFrequency(sdStateStorage.getFrequency());
                statusDetails.get(0).setBrightness(sdStateStorage.getBrightness());
                iInductionlampControlStatusDetailsService.updateInductionlampControlStatusDetails(statusDetails.get(0));
            }
            //根据查询到的设备配置信息拿到设备报文发送种类和内容
            try {
                String code = "0GH+STATUS?\r\n";
                NettyClient client = new NettyClient(ip, port,code,1);
                client.start(null);
                Map codeMap = InductionlampUtil.getPilotLightMode(controlModeType,brightness,frequency);
                client.pushCode(codeMap.get("code").toString());
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SdStateStorage deviceState = sdStateStorageMapper.selectSdStateStorage(deviceId);
        if (null == deviceState) {
            SdStateStorage storage = new SdStateStorage();
            storage.setDeviceId(deviceId);
            storage.setState(sdStateStorage.getState());
            storage.setCreateTime(new Date());
            return sdStateStorageMapper.insertSdStateStorage(storage);
        }

        deviceState.setState(sdStateStorage.getState());
        deviceState.setCreateTime(new Date());
        return sdStateStorageMapper.updateSdStateStorage(deviceState);
    }

    /**
     * 批量删除隧道数据存储表
     *
     * @param ids 需要删除的隧道数据存储表主键
     * @return 结果
     */
    @Override
    public int deleteSdStateStorageByIds(Long[] ids)
    {
        return sdStateStorageMapper.deleteSdStateStorageByIds(ids);
    }

    /**
     * 删除隧道数据存储表信息
     *
     * @param id 隧道数据存储表主键
     * @return 结果
     */
    @Override
    public int deleteSdStateStorageById(Long id)
    {
        return sdStateStorageMapper.deleteSdStateStorageById(id);
    }

	@Override
	public SdStateStorage selectSdStateStorage(String deviceId) {
		// TODO Auto-generated method stub
		return sdStateStorageMapper.selectSdStateStorage(deviceId);
	}
}
