package com.tunnel.platform.controller.dataInfo;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.serotonin.modbus4j.ModbusMaster;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.platform.domain.dataInfo.InductionlampControlStatusDetails;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdStateStorage;
import com.tunnel.platform.service.dataInfo.IInductionlampControlStatusDetailsService;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.service.dataInfo.ISdStateStorageService;
import com.tunnel.platform.service.dataInfo.ISdTunnelsService;
import com.tunnel.platform.utils.mpdbus4j.Modbus4jWriteUtils;
import com.tunnel.platform.utils.mpdbus4j.ModbusTcpMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 隧道数据存储表Controller
 *
 * @author 刘方堃
 * @date 2022-01-07
 */
@RestController
@RequestMapping("/system/storage")
public class SdStateStorageController extends BaseController {
    @Autowired
    private ISdStateStorageService sdStateStorageService;
    @Autowired
    private ISdTunnelsService sdTunnelsService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private IInductionlampControlStatusDetailsService iInductionlampControlStatusDetailsService;
    /**
     * 模拟获取隧道设备状态
     */
    @GetMapping("/getDevState")
    public String getDevState(SdStateStorage storage) {
        String tunnelId = storage.getTunnelId();
        List<Map<String, Object>> devList = new ArrayList<>();
        if (tunnelId == null || tunnelId.equals("")) {
            return JSON.toJSONString(devList);
        }
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(tunnelId);
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        for (SdDevices sdDevice : list) {
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("devId", sdDevice.getEqId().toString());
            if (sdDevice.getEqType() != null) {
                json.put("devType", sdDevice.getEqType().toString());
            }
            //方向
            if (sdDevice.getEqDirection() != null) {
                json.put("direction", sdDevice.getEqDirection());
            }
            //设备状态
            if (sdDevice.getEqType() != null && "109".equals(sdDevice.getEqType().toString())) {
                continue;
            }
            SdStateStorage sdStateStorage = sdStateStorageService.selectSdStateStorage(String.valueOf(sdDevice.getEqId()) + "");
            if (sdStateStorage == null || sdStateStorage.getState() == null) {
                json.put("state", "");
            } else {
                json.put("state", sdStateStorage.getState());
            }
            //诱导灯设备增加亮度和频率
            Long yddType = Long.parseLong(String.valueOf(DevicesTypeEnum.YOU_DAO_DENG.getCode()));
            if (sdDevice.getEqType().longValue() == yddType.longValue()) {
                InductionlampControlStatusDetails inductionlampControlStatusDetails = new InductionlampControlStatusDetails();
                inductionlampControlStatusDetails.setEquipmentId(sdDevice.getEqId());
                List<InductionlampControlStatusDetails> statusDetails = iInductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
                if (statusDetails.size() == 0) {
                    json.put("brightness", "50");
                    json.put("frequency", "69");
                    if (sdStateStorage == null) {
                        sdStateStorage = new SdStateStorage();
                    }
                    sdStateStorage.setBrightness("50");
                    sdStateStorage.setFrequency("69");
                    sdStateStorage.setTunnelId(tunnelId);
                    if (sdStateStorage == null || sdStateStorage.getState() == null || sdStateStorage.getState().equals("")) {
                        sdStateStorage.setState("2");
                    }
                    sdStateStorage.setDeviceId(sdDevice.getEqId());
                    if (null == sdStateStorage.getId()) {
                        sdStateStorageService.insertSdStateStorage(sdStateStorage);
                    } else {
                        sdStateStorageService.updateSdStateStorage(sdStateStorage);
                    }
                } else {
                    json.put("brightness", statusDetails.get(0).getBrightness());
                    json.put("frequency", statusDetails.get(0).getFrequency());
                }
            } else {
                json.put("brightness", "");
                json.put("frequency", "");
            }
            devList.add(json);
        }
        String res = JSON.toJSONString(devList);
        return res;
    }


    /**
     * 查询隧道数据存储表列表
     */
    @PreAuthorize("@ss.hasPermi('system:storage:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdStateStorage sdStateStorage) {
        startPage();
        List<SdStateStorage> list = sdStateStorageService.selectSdStateStorageList(sdStateStorage);
        return getDataTable(list);
    }

    /**
     * 导出隧道数据存储表列表
     */
    @PreAuthorize("@ss.hasPermi('system:storage:export')")
    @Log(title = "隧道数据存储表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdStateStorage sdStateStorage) {
        List<SdStateStorage> list = sdStateStorageService.selectSdStateStorageList(sdStateStorage);
        ExcelUtil<SdStateStorage> util = new ExcelUtil<SdStateStorage>(SdStateStorage.class);
        return util.exportExcel(list, "隧道数据存储表数据");
    }

    /**
     * 获取隧道数据存储表详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:storage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sdStateStorageService.selectSdStateStorageById(id));
    }

    /**
     * 新增隧道数据存储表
     */
    @PreAuthorize("@ss.hasPermi('system:storage:add')")
    @Log(title = "隧道数据存储表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdStateStorage sdStateStorage) {
        return toAjax(sdStateStorageService.insertSdStateStorage(sdStateStorage));
    }

    /**
     * 修改隧道数据存储表
     */
//    @PreAuthorize("@ss.hasPermi('system:storage:edit')")
    @Log(title = "隧道数据存储表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdStateStorage sdStateStorage) {
        ModbusMaster master = ModbusTcpMaster.master;
        //改变车指
        try{
            SdDevices devices = sdDevicesService.selectSdDevicesById(sdStateStorage.getDeviceId());
            Long yddType = Long.parseLong(String.valueOf(DevicesTypeEnum.YOU_DAO_DENG.getCode()));
            if (devices.getEqType().longValue() == yddType.longValue()) {
                return toAjax(sdStateStorageService.updateSdStateStorage(sdStateStorage));
            }
            String eqFeedbackAddress2 = devices.getEqFeedbackAddress2();
            String eqFeedbackAddress3 = devices.getEqFeedbackAddress3();
            String eqFeedbackAddress4 = devices.getEqFeedbackAddress4();
            boolean[] fuWei={false,false,false};
            //复位
            if (sdStateStorage.getState().equals("1")){
                boolean b = Modbus4jWriteUtils.writeCoils(master, 1,Integer.parseInt(eqFeedbackAddress2) ,fuWei );
            }else  if (sdStateStorage.getState().equals("2")){
                boolean b = Modbus4jWriteUtils.writeCoils(master, 1,Integer.parseInt(eqFeedbackAddress3)-1 ,fuWei );
            } if (sdStateStorage.getState().equals("3")){
                boolean b = Modbus4jWriteUtils.writeCoils(master, 1,Integer.parseInt(eqFeedbackAddress4)-2 ,fuWei );
            }
            //控制
            if (sdStateStorage.getState().equals("1")){
                boolean b = Modbus4jWriteUtils.writeCoil(master, 1,Integer.parseInt(eqFeedbackAddress2) ,false );
            }else  if (sdStateStorage.getState().equals("2")){
                boolean b = Modbus4jWriteUtils.writeCoil(master, 1,Integer.parseInt(eqFeedbackAddress4) ,true );
            } if (sdStateStorage.getState().equals("3")){
                boolean b = Modbus4jWriteUtils.writeCoil(master, 1,Integer.parseInt(eqFeedbackAddress3) ,true );
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        return toAjax(1);//sdStateStorageService.updateSdStateStorage(sdStateStorage)
    }

    /**
     * 删除隧道数据存储表
     */
    @PreAuthorize("@ss.hasPermi('system:storage:remove')")
    @Log(title = "隧道数据存储表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sdStateStorageService.deleteSdStateStorageByIds(ids));
    }
}
