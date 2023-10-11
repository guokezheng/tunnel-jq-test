package com.tunnel.business.strategy.domain;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.business.strategy.service.DeviceDataStrategyService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * describe: 默认的设备处理类
 *
 * @author zs
 * @date 2023/4/23
 */
public class DefaultType implements DeviceDataStrategyService {

    private ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);

    private ISdDeviceDataService sdDeviceDataService = SpringUtils.getBean(ISdDeviceDataService.class);

    private CommonControlService commonControlService = SpringUtils.getBean(CommonControlService.class);

    private ISdDeviceTypeItemService sdDeviceTypeItemService = SpringUtils.getBean(ISdDeviceTypeItemService.class);

    /**
     * 获取实时数据
     *
     * @param devices
     * @param data
     */
    @Override
    public void getDeviceData(Map<String, String> devices, SdDeviceData data) {
        if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.FENG_JI_STATUS.getCode()) {
            devices.put("state", data.getData());
        } else if (data != null && data.getItemId() == (long) DevicesTypeItemEnum.FENG_JI_MO_SHI.getCode()) {
            devices.put("autoState", data.getData());
        }else{
            devices.put("state", data.getData());
        }
    }

    /**
     * 模拟控制设备
     *
     * @param map       控制参数
     * @param sdDevices 设备信息
     */
    @Override
    public AjaxResult analogControl(Map<String, Object> map, SdDevices sdDevices) {
        //模拟控制状态-控制成功
        int controlState = 1;
        String deviceId = sdDevices.getEqId();
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        //设置为设备在线
        sdDevicesService.updateOnlineStatus(deviceId,false);
        //修改实时状态
        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
        sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
        List<SdDeviceTypeItem> itemList = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
        if(itemList != null && itemList.size() > 0){
            sdDeviceTypeItem = itemList.get(0);
            sdDeviceDataService.updateDeviceData(sdDevices, state, sdDeviceTypeItem.getId());
        }
        String beforeState = commonControlService.selectBeforeState(sdDevices);
        //添加控制日志
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);
        return AjaxResult.success(controlState);
    }
}
