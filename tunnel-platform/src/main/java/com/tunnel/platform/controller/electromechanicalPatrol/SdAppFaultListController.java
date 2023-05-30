package com.tunnel.platform.controller.electromechanicalPatrol;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdEquipmentTypeService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import com.tunnel.business.service.electromechanicalPatrol.ISdTeamsListService;
import com.tunnel.business.utils.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * app故障填报Controller
 *
 * @author tjw
 * @date 2023-05-26
 */
@RestController
@RequestMapping("/app/fault/list")
public class SdAppFaultListController extends BaseController
{

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    @Autowired
    private ISdFaultListService sdFaultListService;

    @Autowired
    private ISdEquipmentTypeService sdEquipmentTypeService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDevicesService isdDevicesService;

    @Autowired
    private ISdTaskListService sdTaskListService;


    /**
     *  查询隧道列表
     */
    @GetMapping("app/tunnelList")
    public TableDataInfo<List<SdTunnels>> list(SdTunnels sdTunnels)
    {
        startPage();
        if (null == sdTunnels.getDeptId() || "".equals(sdTunnels.getDeptId())){
            String deptId = SecurityUtils.getDeptId();
            if (deptId == null) {
                throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
            }
            sdTunnels.setDeptId(deptId);
        }
        List<SdTunnels> list = sdTunnelsService.selectSdTunnelsList(sdTunnels);
        return getDataTable(list);
    }

    /**
     * 查询故障类型
     */
    @PostMapping("/app/getFaultType")
    public Result getFaultType(){
        String dictType = "fault_type";
        List<SdFaultList> faultList = sdFaultListService.getFaultDictValue(dictType);
        return Result.success(faultList);
    }

    /**
     * 查询故障类型
     */
    @PostMapping("/app/faultEscalationType")
    public Result faultEscalationType(){
        String dictType = "fault_escalation_type";
        List<SdFaultList> faultList = sdFaultListService.getFaultDictValue(dictType);
        return Result.success(faultList);
    }

    /**
     * 查询设备状态
     */
    @PostMapping("/app/eqStatue")
    public Result eqStatue(){
        String dictType = "sd_monitor_state";
        List<SdFaultList> faultList = sdFaultListService.getFaultDictValue(dictType);
        return Result.success(faultList);
    }


    /**
     * 查询故障等级
     */
    @PostMapping("/app/faultLevel")
    public Result faultLevel(){
        String dictType = "fault_level";
        List<SdFaultList> faultList = sdFaultListService.getFaultDictValue(dictType);
        return Result.success(faultList);
    }


    /**
     * 查询设备类型列表
     */
    @GetMapping("/app/deviceTypeList")
    @ApiOperation("查询设备类型列表")
    public TableDataInfo<List<SdEquipmentType>> list(SdEquipmentType sdEquipmentType)
    {
        startPage();
        List<SdEquipmentType> list = sdEquipmentTypeService.selectSdEquipmentTypeList(sdEquipmentType);
        return getDataTable(list);
    }

    /**
     * 查询设备列表
     */
    @GetMapping("/app/deviceList")
    public TableDataInfo<List<SdDevices>> list(SdDevices sdDevices) {
        if (null == sdDevices.getDeptId() || "".equals(sdDevices.getDeptId())) {
            String deptId = SecurityUtils.getDeptId();
            sdDevices.setDeptId(deptId);
        }
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        //1：代表执行校验指令SQL
        if ("1".equals(sdDevices.getEqDirection())) {
            List<String> eqIds = new ArrayList<String>();
            List<SdDevices> checklist = sdDevicesService.getChecklist(list);
            for (SdDevices devices : checklist) {
                eqIds.add(devices.getEqId());
            }
            startPage();
            sdDevices.setEqIds(eqIds);
            list = sdDevicesService.selectSdDevicesList(sdDevices);
        } else {
            startPage();
            list = sdDevicesService.selectSdDevicesList(sdDevices);
        }

        // 如果当前设备是加强照明 基本照明，双向显示设备去重
        if(list != null && list.size() > 0  && sdDevices.getEqType() != null &&
                (
                        sdDevices.getEqType().longValue() == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().longValue()
                                || sdDevices.getEqType().longValue() == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().longValue()
                )
        ){
            Collections.reverse(list);
            list = list.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SdDevices::getExternalDeviceId))), ArrayList::new));
        }
        return getDataTable(list);
    }


    /**
     * 根据设备名称点击事件
     */
    @PostMapping(value = "/app/getEquipmentInfo")
    public Result getEquipmentInfo(String eqId) {
        return Result.success(isdDevicesService.getEquipmentInfo(eqId));
    }


    /**
     * 故障图片保存
     * @param
     * @return
     */
    @PostMapping("/app/faultUploadPicture")
    public AjaxResult uploadPicture(@RequestParam(name = "file", required = false) MultipartFile[] file)
    {
        return AjaxResult.success(sdTaskListService.uploadPicture(file));
    }


    /**
     * 新增故障清单
     */
    @PostMapping("/app/saveFault")
    public AjaxResult add(@RequestBody  SdFaultList sdFaultList) {
        return toAjax(sdFaultListService.saveFault(sdFaultList));
    }






}
